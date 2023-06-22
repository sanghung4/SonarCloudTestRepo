"""Module description goes here."""
import re
import typing
from collections import Counter
from typing import Callable

import orjson
from loguru import logger
from nltk.stem import WordNetLemmatizer
from redis import ResponseError

from post.config.configuration import SETTINGS
from post.config.parts_of_speech import (
    MASTER_TERMS_SET,
    MASTER_WORDS_SET,
    MODEL_REGEX_PARSABLE_NUMBER,
    POS,
    POS_ATTRIBUTES_TYPE,
    POS_GROUPED_BY_PRIORITY,
    POS_PRIORITY_LEVELS,
    POS_UNITS_TYPE,
    UNABBR_DASHES_REGEX,
    UNABBR_W_REGEX,
    UNIT_SI_SUFFIXES,
)
from post.utils.utils import ifnan
from post.utils.utils_nlp_pos import (
    SPACE_REPLACER_DICT,
    STOP_WORDS,
    fuzzy_match_word_to_manfs,
    text_clean_and_spellcheck,
    unabbreviate_pos,
)
from post.utils.utils_nlp_std import (
    VULGAR_FRACTIONS,
    is_english_word,
    multiword_lemmatizer,
    parse_str_as_fraction,
    remove_dbl_spaces,
    replace_all,
    text_alpha_or_seps_atend,
    text_parsable_num_atend,
)
from post.utils.utils_redis import get_redis_conn

########################################################################
# helper functions
########################################################################

# punctuation that separates words
WORD_DELIMITERS = " ,_\n*+:@[\\]^`{|}~"
SENTENCE_DELIMITERS = ".!?;"
CHARS_IGNORE = "()"
ONECHARS_TO_IGNORE = (
    WORD_DELIMITERS + SENTENCE_DELIMITERS + "-"
)  # chars that when appearing by themselves, should be removed

# chars that when appearing by themselves, should cause a combination of surrounding words.
# Do not add "-" to this without careful consideration
COMPOUND_PUNCTUATION = POS["DIMENSIONS"]["delimiters"] + [
    "/",
    "&",
]
lemmatizer = WordNetLemmatizer()

BAD_CHAR_REGEX = r"[\!\^\*\(\)\{\[\}\]\\\|\~]"

DEFAULT_DISALLOWED_POS = [
    "DESC_OR_NOTES",
    "MANUFACTURER_AND_MODEL",
    "TAG_AND_OR_PART",
    "TAG",
]

_cache = {}


def prepare_text_for_tagging(text: str) -> list:
    """parses text into sentences and words."""
    text = text.upper()
    # bad chars - always remove
    text = re.sub(BAD_CHAR_REGEX, "", text)

    # remove the period from the end of the text
    text = re.sub("\.$", "", text)

    # handle unicode replacements
    text = replace_all(text, VULGAR_FRACTIONS.values(), VULGAR_FRACTIONS.keys())

    # parse words
    try:
        text = text.translate(
            str.maketrans(WORD_DELIMITERS, " " * len(WORD_DELIMITERS), CHARS_IGNORE)
        ).strip()
        text = remove_dbl_spaces(text)
        words = text.split(" ")
    except Exception as error:
        logger.error(f"Unable to parse words for {text}\n{error}")

    # remove words that are stopwords, or only one character and in the DELIMITERS
    words = [
        word
        for word in words
        if not (len(word) == 1 and word in ONECHARS_TO_IGNORE)
        and word.upper() not in STOP_WORDS
    ]

    # HACK
    # explicit bug fix for 1 or 2 digit numbers are immediately followed by “pvc” (12pvc -> 12 pvc) or (1pvc -> 1 pvc)
    for word in words:
        if "pvc" in word.lower() and word[0].isnumeric() and word[1].isnumeric():
            index = words.index(word)
            nums = word[0:2]
            chars = word[2:]
            words[index : index + 1] = nums, chars

        elif "pvc" in word.lower() and word[0].isnumeric() and word[1].isalpha():
            index = words.index(word)
            nums = word[0:1]
            chars = word[1:]
            words[index : index + 1] = nums, chars

    # explicit space replacements where space have been omitted
    def space_replacer(lst: list):
        for wrd in lst:
            wrd_upper = wrd.upper()
            if wrd_upper in SPACE_REPLACER_DICT:
                for subwrd in SPACE_REPLACER_DICT[wrd_upper].split(" "):
                    yield subwrd
            else:
                yield wrd

    words = list(space_replacer(words))

    try:
        # parse sentences.  do this after words bc we want to distinguish between periods that separate sentences
        # and those that are part of numbers, like 1.2
        # then keep the appropriate periods but split the rest
        trans = str.maketrans(SENTENCE_DELIMITERS, " " * len(SENTENCE_DELIMITERS))
        words = [w if re.search(r"\d", w) else w.translate(trans) for w in words]
        words = [w for w in " ".join(words).split(" ") if len(w)]

    except Exception as error:
        logger.error(f"Unable to parse sentences for {text}\n{error}")
        raise error

    # 2023-01-23: temporarily deprecating this in favor of a more robust way in dimensions handling
    # rejoin words delimited by certain punctuation, as dimensions sometimes are; unpack the dimensions later
    # also include certain other delimiters that probably join two options together
    # BUT - only do this if words on both sides have numbers in them
    # consider changing this rule to only if words on both sides START with a number
    i = 1
    while i < len(words) - 1:
        if words[i] in COMPOUND_PUNCTUATION:
            # combine
            if re.search(r"\d", words[i - 1]) and re.search(r"\d", words[i + 1]):
                words[i - 1] = "".join(
                    words[i - 1 : i + 2]
                ).lower()  # the lower prevents capitals from re-entering
                del words[i : i + 2]

            # else just delete the word

            # else go to the next word
            i += 1

        # else just go to next word
        else:
            i += 1

    return words


########################################################################
# cache related functions
########################################################################


def _get_redis_client():
    if "redis" not in _cache:
        _cache["redis"] = get_redis_conn()
    # open one connection for this file; I think this is a good place to open the conn
    return _cache["redis"]


def make_redis_key(text: str, prec_word: str = None, foll_word: str = None) -> str:
    """Makes the redis key from the text and its surrounding words.
    the key is text|prec_word|foll_word
    there should never be a pipe in text, but make sure here
    .
    """
    try:
        return (
            text.replace("|", "")
            + "|"
            + ifnan(prec_word, "")
            + "|"
            + ifnan(foll_word, "")
        )
    except Exception as error:
        logger.error(f"Unable to make redis key.\n{error}")


def get_pos_from_cache(text: str, prec_word: str = None, foll_word: str = None) -> dict:
    """Check if a POS is in Redis.
    This saves time by skipping brute force categorization.

    Args:
    ----
        text (str): Part of speech to be categorized
        prec_word (str, optional): Preceding word. Defaults to None.
        foll_word (str, optional): Following word. Defaults to None.

    Returns
    -------
        dict: Dictionary of POS data
    """
    try:
        conn_redis = _get_redis_client()
        key = make_redis_key(text, prec_word, foll_word)
        if conn_redis.exists(key) and conn_redis.type(key) == b"string":
            val = conn_redis.get(key)
            return orjson.loads(val)
        return None
    except ResponseError as key_error:
        logger.error("Unable to get POS from cache")
        logger.exception(key_error)


def write_pos_to_cache(fun: Callable) -> dict:  # todo: add logging
    """thin wrapper on top of categorize_text which adds the result to the global categorization cache."""

    def wrapper(
        text: str,
        prec_word: str = None,
        foll_word: str = None,
        clean_words: bool = True,
        allow_spellcheck: bool = True,
        allowed_cats: typing.Iterable = None,
        disallowed_cats: typing.Iterable = DEFAULT_DISALLOWED_POS,
    ) -> dict:
        # call the actual tag_parts_of_speech function
        res = fun(
            text=text,
            prec_word=prec_word,
            foll_word=foll_word,
            clean_words=clean_words,
            allow_spellcheck=allow_spellcheck,
            allowed_cats=allowed_cats,
            disallowed_cats=disallowed_cats,
        )

        # store results in the cache, if desired
        # don't store things that are too long or short
        # also, prevent OTHER from ever being stored except at the lowest ngram level
        if (
            not res["bypass_cache"]
            and SETTINGS.use_word_category_cache in [1, 2]
            and 0 < len(text) <= 20
            and not (res["pos"] == "OTHER" and res["n_words"] > 1)
        ):
            # put value to redis as serialized JSON
            # consider using the redis JSON functionality in the future
            key = make_redis_key(res["text"], res["prec_word"], res["foll_word"])
            _get_redis_client().set(key, orjson.dumps(res))

        return res

    return wrapper


@write_pos_to_cache
def tag_parts_of_speech(
    text: str,
    prec_word: str = None,
    foll_word: str = None,
    clean_words: bool = True,
    allow_spellcheck: bool = True,
    allowed_cats: typing.Iterable = None,
    disallowed_cats: typing.Iterable = None,
) -> dict:
    """Identify text to see if it is a Tag, Model, Manufacturer, etc.
    returns a JSON OBJECT
    Set prec_word or foll_word to values to compute the category in the context of them
    set clean_words = False if the text has already been cleaned
    if allowed_cats are passed, only these cats will be allowed.  otherwise,
    disallowed_cats are categories which may not be returned.  It defaults to certain combined cats applicable mainly to OCR.
    """
    # determine which categories are allowed
    allowed_cats = (
        set(POS.keys()) if allowed_cats is None else set(allowed_cats)
    ) - set(disallowed_cats)

    def make_json_tag_obj(
        correct_text: str,
        n_words: int,
        pos: str,
        confidence: float,
        bypass_cache: bool,
        allow_spellcheck: bool,
        is_category: bool = False,
        other_info: dict = None,
    ) -> dict:
        """Helper function to return results in JSON form
        other_info is a JSON object of additional information of any kind for this POS
        is_category if True means that the POS refers to a broad category of this POS.
        """
        res = {
            "text": text,
            "correct_text": correct_text,
            "n_words": n_words,
            "prec_word": prec_word,
            "foll_word": foll_word,
            "pos": pos,
            "confidence": confidence,
            "bypass_cache": bypass_cache,
            "allow_spellcheck": allow_spellcheck,
            "is_category": is_category,
            "other_info": other_info,
        }
        return res

    ########################################################################
    #  the logic to categorize a word.
    ########################################################################

    # instantiate the category counter
    cat_counter_all = Counter()

    # parse words
    if clean_words:
        words = prepare_text_for_tagging(text)
    else:
        words = text.split(" ")
    if len(words) == 0:
        # return None with confidence 100 and skip caching
        logger.warning("No words detected, skipping caching")
        return make_json_tag_obj(None, 0, None, 100, True, allow_spellcheck)

    # final word count
    n_words = len(words)
    word_lens = [len(word) for word in words]

    # see if it's in the cache already
    if SETTINGS.use_word_category_cache == 2:
        cached_val = get_pos_from_cache(text, prec_word, foll_word)
        if cached_val is not None:
            if cached_val["pos"] in allowed_cats:
                return cached_val
            else:
                # the cached value is not allowed.
                return make_json_tag_obj(None, 0, None, 100, True, allow_spellcheck)

    # remember case and split words into long and short
    text_isupper = text.isupper()
    text_upper = text.upper()
    words_upper = [word.upper() for word in words]
    words_long = [word for word, length in zip(words, word_lens) if length >= 4]
    n_words_long = len(words_long)
    words_short = [word for word, length in zip(words, word_lens) if length < 4]

    # forgo spell check if disallowed, the length of text is very long, the spellcheck returns nothing,
    # there is a number in the text, or the text is in the master terms list.
    text_spellchecked = (
        text_upper
        if not allow_spellcheck
        or len(text_upper) > 20
        or not text.isalpha()
        or text_upper in MASTER_TERMS_SET
        else " ".join(text_clean_and_spellcheck(text_upper)[0]).upper()
    )
    if len(text_spellchecked) == 0:
        text_spellchecked = text_upper

    # other verisons of the text we may need to check.
    text_dashclean = re.sub(UNABBR_DASHES_REGEX, " ", text_upper)
    text_dashclean2 = re.sub(UNABBR_DASHES_REGEX, "", text_upper)
    text_withslash_clean = re.sub(UNABBR_W_REGEX, "", text_upper)
    text_nospaces = text_dashclean.replace(" ", "")

    # first, search for certain words which are unique IDs or can be ignored.
    if n_words == 1:
        # assume an integer greater than 1000 is an internal ID.
        if "ID" in allowed_cats and re.search(POS["ID"]["regex"], text_upper):
            return make_json_tag_obj(text, n_words, "ID", 100, False, allow_spellcheck)

        if "UPC" in allowed_cats and re.search(POS["UPC"]["regex"], text_upper):
            return make_json_tag_obj(text, n_words, "UPC", 100, False, allow_spellcheck)

        # another type of ID starts with "MSC"
        if "MSC" in allowed_cats and re.search(POS["MSC"]["regex"], text_upper):
            correct_text = text_upper
            if correct_text[3] != "-":  # the dash is missing
                correct_text = correct_text[:3] + "-" + correct_text[3:]
            return make_json_tag_obj(
                correct_text, n_words, "MSC", 100, False, allow_spellcheck
            )

        # stop words
        if "STOPWORD" in allowed_cats and text_upper in STOP_WORDS:
            return make_json_tag_obj(
                text_upper, n_words, "STOPWORD", 100, False, allow_spellcheck
            )

        # operators may also be searched for at this point as they are only one word.
        if (
            "OPERATOR" in allowed_cats
            and re.match(POS["OPERATOR"]["regex"], text) is not None
        ):
            return make_json_tag_obj(
                text_upper, n_words, "OPERATOR", 100, False, allow_spellcheck
            )

    # ACTIONS, which are special words which trigger special downstream behavior
    if "ACTION" in allowed_cats:
        for key, val in POS["ACTION"]["actions"].items():
            if re.search(val["regex"], text):
                other_info = {"action_name": key}
                return make_json_tag_obj(
                    text_upper,
                    n_words,
                    "ACTION",
                    100,
                    False,
                    allow_spellcheck,
                    other_info=other_info,
                )

    # the text fuzzy matches a manufacturer, or the text is a manufacturer synonym
    if "MANUFACTURER" in allowed_cats and n_words <= 5:
        # first, unabbreviate
        text_unabbr = unabbreviate_pos(text_upper, "MANUFACTURER")[0]

        # see if the text matches an exact synonym.
        if text_unabbr in POS["MANUFACTURER"]["dict_names_syns"]:
            return make_json_tag_obj(
                POS["MANUFACTURER"]["dict_names_syns"][text_unabbr],
                n_words,
                "MANUFACTURER",
                100,
                False,
                allow_spellcheck,
            )

        # next see if there is a fuzzy match, but don't do that for very short words.
        if len(text) >= 6:
            match_fuzzy = fuzzy_match_word_to_manfs(text_unabbr, include_syns=False)
            if match_fuzzy[0] is not None:
                # if there was a match, look up the master manufacturer name from whatever matched.
                # (match_fuzzy[0].  the confidence is in match_fuzzy[1])
                return make_json_tag_obj(
                    POS["MANUFACTURER"]["dict_names_syns"][match_fuzzy[0]],
                    n_words,
                    "MANUFACTURER",
                    match_fuzzy[1],
                    False,
                    allow_spellcheck,
                )

    # now categorize individual words, if more than 1 exists
    if n_words > 1:
        cat_counter_long = Counter(
            [
                tag_parts_of_speech(
                    word, pw, fw, clean_words, disallowed_cats=disallowed_cats
                )["pos"]
                for word, pw, fw in zip(
                    words_long, [None] + words_long[:-1], words_long[1:] + [None]
                )
            ]
        )
        cat_counter_short = Counter(
            [
                tag_parts_of_speech(
                    word, pw, fw, clean_words, disallowed_cats=disallowed_cats
                )["pos"]
                for word, pw, fw in zip(
                    words_short, [None] + words_short[:-1], words_short[1:] + [None]
                )
            ]
        )
        cat_counter_all = cat_counter_long + cat_counter_short

    ###
    # the following COMBINED categories are for parsing schedules / tables only
    ###

    # logic for large fields.  first check for a combined manufacturer / model column
    # changed to 2 on 2022-08-02 by RPC this used to be 4 which was a bad assumption
    if "MANUFACTURER_AND_MODEL" in allowed_cats and n_words_long >= 2:
        # the criteria is: 5% of the words are models or manfs, EACH
        n_models = 0 if "MODEL" not in cat_counter_long else cat_counter_long["MODEL"]
        n_manufacturers = (
            0
            if "MANUFACTURER" not in cat_counter_long
            else cat_counter_long["MANUFACTURER"]
        )
        if n_models >= 0.05 * n_words_long and n_manufacturers >= 0.05 * n_words_long:
            confidence = (
                (n_models + n_manufacturers) / n_words_long * 100
            )  # pct of words which are manf of models
            return make_json_tag_obj(
                None,
                n_words,
                "MANUFACTURER_AND_MODEL",
                confidence,
                False,
                allow_spellcheck,
            )

    # check for a combined TAG / PART column for medium length texts
    if "TAG_AND_OR_PART" in allowed_cats and 2 <= n_words <= 12:
        # if I re-write these statements more succinctly as above, Pycharm will not debug them
        n_tags = n_parts = 0
        if "TAG" in cat_counter_all:
            n_tags = cat_counter_all["TAG"]
        if "PART" in cat_counter_all:
            n_parts = cat_counter_all["PART"]

        if n_tags > 0 and n_parts > 0:
            confidence = (
                (n_tags + n_parts) / n_words * 100
            )  # pct of words which are tags of parts
            return make_json_tag_obj(
                None,
                n_words,
                "TAG_AND_OR_PART",
                confidence,
                False,
                allow_spellcheck,
            )

    # else assume it's a DESC_OR_NOTES column, if the n_words > 4
    if "DESC_OR_NOTES" in allowed_cats and n_words > 4:
        confidence = 80  # hard-coded for now
        return make_json_tag_obj(
            None, n_words, "DESC_OR_NOTES", confidence, False, allow_spellcheck
        )

    ###
    # end large / combined cats
    ###

    # tests for a plumbing part, including via abbreviation.
    if "PART" in allowed_cats:
        is_part = False
        text_unabbr = unabbreviate_pos(text_upper, "PART")[0]
        text_spellchecked_unabbr = unabbreviate_pos(text_spellchecked, "PART")[0]
        texts_to_check = set(
            (
                text_unabbr,
                text_spellchecked,
                text_spellchecked_unabbr,
                text_dashclean,
                text_dashclean2,
                text_withslash_clean,
                text_nospaces,
            )
        )

        # check categories, first
        if text_upper in POS["PART"]["dict_cats"]:
            confidence = 100
            return make_json_tag_obj(
                text_upper,
                n_words,
                "PART",
                confidence,
                False,
                allow_spellcheck,
                is_category=True,
            )

        # loop over texts to check
        for match_text in texts_to_check:
            if match_text is not None and match_text in POS["PART"]["dict_names_syns"]:
                is_part = True
                correct_text = POS["PART"]["dict_names_syns"][match_text]
                confidence = 90
                break

        # test the singular of the last word after spell check.
        if not is_part:
            text_upper_singular = multiword_lemmatizer(text_spellchecked)
            if (
                len(words_upper) != 0
                and text_upper_singular in POS["PART"]["dict_names_syns"]
            ):
                is_part = True
                correct_text = POS["PART"]["dict_names_syns"][text_upper_singular]
                confidence = 75

        if is_part:
            return make_json_tag_obj(
                correct_text, n_words, "PART", confidence, False, allow_spellcheck
            )

    # the following checks use the same pattern of just looking at regex plus word and/or synonym matches,
    # so set up a loop to reduce code size.  These are generally quantities followed by UNITS, like 10 GPF
    if n_words <= 5:
        for pos in [p for p in POS_UNITS_TYPE if p in allowed_cats]:
            # run it twice, once for units on the end (like 100 F) and another for at the start (like F 100)
            # if the latter is allowed
            for direction in ("normal", "reversed"):
                if direction == "normal":
                    regex = "regex"
                    end_with_numbers, end_with_alpha = 0, 1

                else:
                    # make sure reversed direction is allowed
                    if (
                        "allow_reverse_regex" not in POS[pos]
                        or not POS[pos]["allow_reverse_regex"]
                    ):
                        continue
                    regex = "regex_reversed"
                    end_with_numbers, end_with_alpha = 1, 0

                # first, unabbreviate, then run regex, which includes all the synonyms AND abbreviations
                # convert numbers to words in the abbreviations
                text_unabbr = unabbreviate_pos(
                    text_upper, pos, convert_number_words=True
                )[0]
                if POS[pos][regex].search(text_unabbr):
                    # the number at the start of the text
                    value = text_parsable_num_atend(
                        text_unabbr, which_end=end_with_numbers
                    )
                    value = parse_str_as_fraction(value)

                    # the non-numeric at the end
                    units = (
                        text_alpha_or_seps_atend(
                            text_unabbr, separators=" ./°#-", which_end=end_with_alpha
                        )
                        .strip()
                        .upper()
                    )

                    # test for a SI unit
                    if len(units) >= 3 and units[1] in [" ", "-"]:
                        si_prefix = units[0]
                        if si_prefix in UNIT_SI_SUFFIXES and value is not None:
                            value *= UNIT_SI_SUFFIXES[si_prefix]
                        units = units[2:]

                    # now unabbreviate
                    units = unabbreviate_pos(units, pos)[0]
                    if units in POS[pos]["dict_names_syns"]:
                        units = POS[pos]["dict_names_syns"][units]
                    else:
                        # TODO: Handle #ERROR better
                        units = ""

                    # don't count it if the value is out of bounds
                    if value is not None and "value_bounds" in POS[pos]:
                        lower, upper = POS[pos]["value_bounds"]
                        if not lower <= value <= upper:
                            continue

                    # somehow the parsing of a dimension failed, despite it matching the regex, so abandon
                    if value is None and units == "":  # TODO - handle #ERROR better
                        continue

                    # matched
                    other_info = {"value": value, "units": units}
                    correct_text = str(value) + (
                        " " + units if units is not None else ""
                    )
                    confidence = 80
                    return make_json_tag_obj(
                        correct_text,
                        n_words,
                        pos,
                        confidence,
                        False,
                        allow_spellcheck,
                        other_info=other_info,
                    )

    # search for DIMENSIONS: if the entire string matches the dimensions regex, proceed
    # but, skip this if the text is actually a plumbing term, like "90"
    if (
        "DIMENSIONS" in allowed_cats
        and text_upper not in MASTER_TERMS_SET
        and POS["DIMENSIONS"]["regex"].search(text_upper)
    ):
        other_info = {}

        # see if there are multiple parts to the dimension
        dim_parts = [
            x.strip() for x in re.split(POS["DIMENSIONS"]["split_regex"], text_upper)
        ]
        for idx, dim_part in enumerate(list(filter(None, dim_parts))):
            # extract and remove any TYPE like length or width
            dim_part_orig_text = dim_part
            dim_part.upper().split()

            # unabbr the dim type(s), and remove it from the dim_part, and store it.  There may be multiple matches.
            # consider re-writing this not in a loop.
            dim_types = []
            dim_type_text = re.search(POS["DIMENSIONS"]["types_regex"], dim_part)
            while dim_type_text is not None:
                dim_type = POS["DIMENSIONS"]["dict_types_syns"][dim_type_text[0]]
                dim_types.append(dim_type)
                dim_part = remove_dbl_spaces(dim_part.replace(dim_type_text[0], "", 1))
                dim_type_text = re.search(POS["DIMENSIONS"]["types_regex"], dim_part)

            # parse units
            text_atend_upper = text_alpha_or_seps_atend(dim_part, "'\"“”‘’").upper()
            units = (
                None
                if text_atend_upper not in POS["DIMENSIONS"]["dict_names_syns"]
                else POS["DIMENSIONS"]["dict_names_syns"][text_atend_upper]
            )

            # parse value (extract numerical part)
            value_str = (
                dim_part
                if len(text_atend_upper) == 0
                else dim_part[0 : -len(text_atend_upper)]
            ).strip()

            # new rule as of 2023-02-09: disallow improper fractions and make them mixed numbers instead.
            # new rule as of 2023-03-27: disallow improper fractions and make them mixed numbers instead.
            frac_parts = value_str.split("/")
            if len(frac_parts) == 2:
                num, den = frac_parts
                if " " not in num and "-" not in num and "-" not in den:
                    if int(num) > int(den):
                        num = num[0:-1] + " " + num[-1]
                        value_str = num + "/" + den

            # send to fraction parser
            value = parse_str_as_fraction(value_str)
            if value is None:
                units = "#ERROR"
                correct_text = None
            else:
                correct_text = str(value) + (" " + units if units is not None else "")

            # add this dimension to the json.
            other_info["dim_" + str(idx + 1)] = {
                "text": dim_part_orig_text,
                "correct_text": correct_text,
                "value": value,
                "units": units,
                "type": None if len(dim_types) == 0 else dim_types,
            }

        confidence = 100  # hard-coded for now
        return make_json_tag_obj(
            None,
            n_words,
            "DIMENSIONS",
            confidence,
            False,
            allow_spellcheck,
            other_info=other_info,
        )

    # the following checks use the same pattern of just looking at name and/or synonym matches,
    # so set up a loop to reduce code size
    if n_words <= 3:
        for pos in [p for p in POS_ATTRIBUTES_TYPE if p in allowed_cats]:
            # first, unabbreviate, then run name or synonym matches on it, the spellchecked version,
            # and a version which replaces dashes with spaces (which we need to be very careful about doing elsewhere)
            # and a version which removes spaces
            text_unabbr = unabbreviate_pos(text_upper, pos)[0]

            match = None
            for match_text in (
                text_unabbr,
                text_spellchecked,
                text_dashclean,
                text_dashclean2,
                text_withslash_clean,
                text_nospaces,
            ):
                if match_text in POS[pos]["dict_names_syns"]:
                    match = POS[pos]["dict_names_syns"][match_text]

            if len(words_upper) != 0 and match is not None:
                confidence = 80  # hard-coded for now
                return make_json_tag_obj(
                    match, n_words, pos, confidence, False, allow_spellcheck
                )

            # finally, check for a CATEGORY if those exist for this POS
            if "dict_cats" in POS[pos]:
                if text_upper in POS[pos]["dict_cats"]:
                    confidence = 100
                    return make_json_tag_obj(
                        text_upper,
                        n_words,
                        pos,
                        confidence,
                        False,
                        allow_spellcheck,
                        is_category=True,
                    )

    # test for TAG.  TO DO: clean this up; it is a remnant from OCR
    if (
        "TAG" in allowed_cats
        and text_isupper
        and n_words == 1
        and POS["TAG"]["regex"].match(text)
        and len(text) <= 5
        and not text.startswith("#")
        and not text.isnumeric()
        and not is_english_word(text)
        and text not in POS["TAG"]["not_names"]
    ):
        # if it has a number part, it must be below 99
        number_part = re.sub("[^0-9]", "", text)
        if len(number_part) == 0 or int(number_part) <= 99:
            # hard code a simple confidence measure: high if it starts with a letter and ends with a number
            if text[0].isalpha() and text[-1].isnumeric():
                confidence = 90
            else:
                confidence = 80
            confidence -= len(
                text
            )  # also penalize for long tags, bc they usually are not.

            # attempt to split the tag.
            other_info = {
                "text_part": text_alpha_or_seps_atend(text, which_end=0),
                "num": text_parsable_num_atend(text.replace("-", ""), which_end=0),
            }

            return make_json_tag_obj(
                text_upper,
                n_words,
                "TAG",
                confidence,
                False,
                allow_spellcheck,
                other_info=other_info,
            )

    # check MODEL
    if "MODEL" in allowed_cats:
        is_model = False
        if (
            not is_english_word(text_upper, check_length=False)
            and text_upper not in MASTER_WORDS_SET
        ):
            # see the text contains titles like "MODEL NO."
            title_matches = re.match(
                POS["MODEL"]["regex_titles"], ifnan(text_spellchecked, text)
            )

            """ see if any word in the text has been categorized differently by excluding specific categories and either if
            the text contains a Model title or the regex matches a Model which doesn't contain any English word
            """
            if (
                all(
                    cat not in cat_counter_all.keys()
                    for cat in POS["MODEL"]["subword_categories_indicating_not_model"]
                )
                and (
                    title_matches
                    or (
                        POS["MODEL"]["regex"].search(text)
                        and not any(
                            list(
                                map(
                                    lambda word: is_english_word(
                                        multiword_lemmatizer(word), check_length=False
                                    ),
                                    text.split(),
                                )
                            )
                        )
                    )
                )
            ) or (
                title_matches
                and (
                    re.compile(MODEL_REGEX_PARSABLE_NUMBER).search(text.split()[-1])
                    or (
                        POS["TAG"]["regex"].search(text.split()[-1])
                        and not is_english_word(text.split()[-1])
                    )
                )
            ):
                is_model = True
                confidence = 80

            # see if it matches some other patterns
            elif (
                text_spellchecked is not None
                and text_spellchecked in ["EQUIVALENT"]
                or text.startswith("#")
            ) and len(cat_counter_all) == 1:
                is_model = True
                confidence = 95

        if is_model:
            # strip "MODEL" title if it exists, first
            other_info = {
                "title": None if not title_matches else title_matches.groups(0)[0]
            }
            text_wo_title = (
                text
                if not title_matches
                else re.sub(POS["MODEL"]["regex_titles"], "", text).strip()
            )
            other_info.update(
                {
                    "part_" + str(idx): mp
                    for idx, mp in enumerate(
                        re.split(POS["MODEL"]["regex_split"], text_wo_title)
                    )
                }
            )
            wo_last_part = re.findall(
                POS["MODEL"]["regex_split_lastpart"], text_wo_title
            )
            other_info["wo_chars"] = re.sub(
                POS["MODEL"]["regex_split"], "", text_wo_title
            )
            other_info["wo_chars_upper"] = other_info["wo_chars"].upper()
            if len(wo_last_part) != 0:
                other_info["wo_lastpart"] = wo_last_part[0][0]
                other_info["wo_lastpart_upper"] = wo_last_part[0][0].upper()
                other_info["wo_chars_or_lastpart_upper"] = re.sub(
                    POS["MODEL"]["regex_split"], "", wo_last_part[0][0].upper()
                )
            return make_json_tag_obj(
                text_wo_title,
                n_words,
                "MODEL",
                confidence,
                False,
                allow_spellcheck,
                other_info=other_info,
            )

    # we can't categorize this text so return "OTHER"
    if "OTHER" in allowed_cats:
        confidence = 0
        return make_json_tag_obj(
            text_upper, n_words, "OTHER", confidence, False, allow_spellcheck
        )

    # we weren't able to use OTHER either.
    return make_json_tag_obj(None, 0, None, 100, True, allow_spellcheck)


def tag_parts_of_speech_ngrams(
    texts: typing.Union[str, list],
    allow_spellcheck: bool = True,
    source: str = None,
    allow_actions: bool = True,
    allowed_cats: list = None,
    disallowed_cats: list = DEFAULT_DISALLOWED_POS,
    use_priorities: bool = True,
) -> dict:
    """categorizes text by tagging combos of MAX_NGRAM_LEVEL to 1 word(s) at a time, prioritizing results in that order (*)
    text may also be a list, in which case each entry will be parsed separately and the results concatenated
    source is an optional label which will be appended to each record if sent
    allow_actions processes ACTIONS which are custom expansions of generic terms
    if allowed_cats are passed, only these cats will be allowed.  otherwise,
    disallowed_cats are categories which may not be returned.  It defaults to certain combined cats applicable mainly to OCR
    (*) use_priorities (default True) causes certain pos to be prioritized higher than others, rather than the n-gram priority rule.
    """
    # if not already a list, split the text by certain characters.  Be VERY sparse in choosing these chars
    # as most common sentence delimiters have actual non-punctuation meanings downstream in the tagger.
    # ! This arg was unused previously.
    if not allow_actions:
        disallowed_cats = disallowed_cats + ["ACTION"]
    if not isinstance(texts, list):
        texts = texts.split(";")

    # loop over texts
    all_final_tags, final_tags = [], []
    for text_n, text in enumerate(texts):
        for t in text.split(" "):
            if "/" in t:
                units = []
                for item in t.split("/"):
                    for pos in [p for p in POS_UNITS_TYPE]:
                        if match := POS[pos]["regex"].search(item):
                            units.append(match.group(0))
                            break
                if len(units) == len(t.split("/")):
                    text = text.replace(t, " ".join(units))
        # parse words
        words = prepare_text_for_tagging(text)
        n_words = len(words)

        # keep track of the final word categories; start with None.  The tags are json objects
        final_tags = [None] * n_words

        # this controls how many levels of n-grams we look at.
        MAX_NGRAM_LEVEL = 7

        # changed on 2023-03-27
        # loop first over ngram levels, then priority levels.
        # to do: check that this works for more cases than the opposite order
        # it's possible than in different circumstances, different orders would lead to better results

        # loop over ngram_levels and prioritize the findings of the higher ngram_levels;
        # it should be the last resort when no other combinations make sense
        for ngram_level in reversed(range(1, min(n_words, MAX_NGRAM_LEVEL) + 1)):
            # these are the categories that we will allow to span multiple words at this ngram level
            allowed_cats_at_ngram_level = [
                pos
                for pos in POS
                if ngram_level == 1
                or (
                    "max_ngram_span" in POS[pos]
                    and POS[pos]["max_ngram_span"] >= ngram_level
                )
            ]

            # loop over priority levels, if desired
            priority_levels = [-1] if not use_priorities else POS_PRIORITY_LEVELS
            for priority_level in priority_levels:
                # compute allowed categories based on both the passed allowed cats
                # and the cats at this priority level
                cats_at_plevel = (
                    set(POS.keys())
                    if priority_level == -1
                    else POS_GROUPED_BY_PRIORITY[priority_level]
                )
                allowed_cats_at_plevel = (
                    set(POS.keys()) if allowed_cats is None else set(allowed_cats)
                ) & set(cats_at_plevel)

                # OTHER is never allowed except for at the lowest priority and ngram level.
                remove_OTHER = not (
                    priority_level == min(priority_levels) and ngram_level == 1
                )

                # get tags for all possible ngrams of this ngram level in the words.
                # for speed, we may skip any ngram in which any word has been previously categorized.
                ngram_tags = [
                    None
                    if any(final_tags[i : i + ngram_level])
                    else tag_parts_of_speech(
                        " ".join(words[i : i + ngram_level]),
                        prec_word=None if i == 0 else words[i - 1],
                        foll_word=None
                        if i + ngram_level >= n_words
                        else words[i + ngram_level],
                        clean_words=False,
                        allow_spellcheck=allow_spellcheck,
                        allowed_cats=allowed_cats_at_plevel
                        - (set("OTHER") if remove_OTHER else set()),
                        disallowed_cats=disallowed_cats,
                    )
                    for i, word in enumerate(
                        words if ngram_level == 1 else words[: -(ngram_level - 1)]
                    )
                ]

                # assign the categories of the n-grams (if multi-word) to all words in the phrase,
                # if nothing has been assigned yet; the latter condition should be redundant given the skip logic above.
                # also record the starting word_n of each phrase
                i = 0
                while i < len(ngram_tags):
                    tag = ngram_tags[i]
                    if (
                        tag is not None
                        and tag["pos"] is not None
                        and (
                            tag["pos"] in allowed_cats_at_ngram_level
                            or ngram_level == 1
                        )
                    ):
                        tag["first_word_n"] = i + 1
                        final_tags[i : i + ngram_level] = [tag] * ngram_level
                        i += ngram_level
                    else:
                        i += 1

                ######## end one n-gram level loop

            ######## end one priority level loop

        # combine the results within each n-gram into one record
        i = 0
        while i < len(final_tags):
            ngram_length = final_tags[i]["n_words"]
            del final_tags[i + 1 : i + ngram_length]
            i += 1

        # loop over tags and record some final things.
        for idx, tag in enumerate(final_tags):
            # record the preceding tag and the following tag.
            # only save a few fields of the tag in the interest of space savings
            tag["prec_tag"] = (
                None
                if idx == 0
                else {
                    key: val
                    for key, val in final_tags[idx - 1].items()
                    if key in ("pos", "correct_text")
                }
            )
            tag["foll_tag"] = (
                None
                if idx == len(final_tags) - 1
                else {
                    key: val
                    for key, val in final_tags[idx + 1].items()
                    if key in ("pos", "correct_text")
                }
            )

            # set text_n and source (if passed)
            tag["text_n"] = text_n + 1
            if source is not None:
                tag["source"] = source

        # add to results
        all_final_tags += final_tags

        ######## end one text loop

    # return the final json
    return all_final_tags
