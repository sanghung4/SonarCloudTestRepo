"""Natural Language Processing Utilities
THE FUNCTIONS IN THIS FILE MAY HAVE DEPENDENCE ON OTHER MODULES OR POS.
"""
import typing

import contractions
import pandas as pd
import symspellpy
from fuzzyset import FuzzySet
from fuzzywuzzy import process
from nltk.corpus import stopwords
from symspellpy import Verbosity

from post.config.parts_of_speech import MASTER_TERMS_SET, MASTER_WORDS_SET, POS
from post.utils.utils_nlp_std import ORDINAL_WORDS, REGEX_TEXT_SPLITERS, is_english_word

# GLOBALS and NLP constants
PUNCTUATION_REMOVE = "\"#&'()*+:;<=>@[\\]^`{|}~.!?-"
SENTENCE_DELIMITER = "|"

# space proxies and replacer
SPACE_PROXIES = "/,_\n"
SPACE_REPLACER_FILEPATH = "post/static_lists/space_replacer/space_replacer.csv"
SPACE_REPLACER_DICT = {
    x[0]: x[1]
    for x in (pd.read_csv(SPACE_REPLACER_FILEPATH, header=0).itertuples(index=False))
}

# combine words - keep these as lists
COMBINE_WORDS_FROM = []
COMBINE_WORDS_TO = [w.replace(" ", "") for w in COMBINE_WORDS_FROM]

# stop words and other flags
# short stop words can intersect with abbreviatons for parts and things so exclude them
STOP_WORDS = {
    sw.upper()
    for sw in stopwords.words("english")
    if len(sw) >= 3 and sw.upper() not in MASTER_WORDS_SET
}
NON_MEANINGFUL_FLAGS = set(
    map(str.upper, {"deleted", "[deleted]", "removed", "[removed]"})
)

# words that negate the thing in front of them
NEGATION_WORDS = {"NON"}

# speller and lemmatizer objects
SPELLER_IGNORE_WORDS = set(COMBINE_WORDS_TO)

# symspell config.  get files from https://github.com/wolfgarbe/SymSpell/blob/master/SymSpell
symspell = symspellpy.SymSpell(max_dictionary_edit_distance=3, prefix_length=7)
SYMSPELL_DICTIONARY_PATH = "post/utils/symspell/frequency_dictionary_en_82_765.txt"
symspell.load_dictionary(SYMSPELL_DICTIONARY_PATH, term_index=0, count_index=1)
SYMSPELL_BIGRAM_PATH = "post/utils/symspell/frequency_bigramdictionary_en_243_342.txt"
symspell.load_bigram_dictionary(SYMSPELL_BIGRAM_PATH, term_index=0, count_index=1)

# pre-made fuzzyset objects
FUZZY_SEARCH_TOLERANCE = 80

fuzzyset_MANUFACTURERS = FuzzySet()
for x in POS["MANUFACTURER"]["df_names"].iloc[:, 0]:
    fuzzyset_MANUFACTURERS.add(x)

fuzzyset_MANUFACTURERS_AND_SYNONYMS = FuzzySet()
for x in POS["MANUFACTURER"]["dict_names_syns"].keys():
    fuzzyset_MANUFACTURERS_AND_SYNONYMS.add(x)

fuzzyset_MASTER_DICT_NAMES_SYNS = FuzzySet()
for x in MASTER_TERMS_SET:
    fuzzyset_MASTER_DICT_NAMES_SYNS.add(x)


def spellcheck_word(word: str, word_min_len: int = 4, custom_dict: set = False) -> str:
    """spellcheck word, unless it's in SPELLER_IGNORE_WORDS or the ENGLISH words or shorter than word_min_len
    if custom_dict is passed, it checks words in this set first for a close match.
    """
    if (
        len(word) < word_min_len
        or not word.isalpha()
        or word.upper() in SPELLER_IGNORE_WORDS
    ):
        return word
    elif word in STOP_WORDS or word == "":
        return None
    else:

        def correct_one_subword(subword: str) -> str:
            """corrects one sub-word at a time."""
            # if it's an english word, return immediately
            if is_english_word(subword):
                return subword

            # else try to match to plumbing terms
            if custom_dict is not None:
                match = fuzzy_match_word(subword, custom_dict, FUZZY_SEARCH_TOLERANCE)
                if match[0] is not None:
                    return match[0]

            # else run the spell checker
            suggestions = symspell.lookup(
                subword, Verbosity.CLOSEST, max_edit_distance=3
            )
            return subword if len(suggestions) == 0 else suggestions[0].term

        subwords = word.split("-")
        corrections = [correct_one_subword(sw) for sw in subwords]
        res = " ".join(
            c if c is not None else sw for c, sw in zip(corrections, subwords)
        )
        return res


def text_clean_and_spellcheck(x: str) -> typing.Tuple[list, int]:
    """clean text for various things, including spell check."""

    # space proxies
    x = str(x).lower().translate(str.maketrans(SPACE_PROXIES, " " * len(SPACE_PROXIES)))

    # contractions and punctuations
    x = contractions.fix(x)
    x = x.translate(str.maketrans("", "", PUNCTUATION_REMOVE))

    # combine words which are currently blank, but keep the functionality
    for cwf, cwt in zip(COMBINE_WORDS_FROM, COMBINE_WORDS_TO):
        x = x.replace(cwf, cwt)
    words = [w.strip() for w in x.split(" ")]
    words = list(
        filter(
            None,
            [
                spellcheck_word(w, custom_dict=fuzzyset_MASTER_DICT_NAMES_SYNS)
                for w in words
            ],
        )
    )

    # non-meaningful
    if (len(words) == 0) or (words[0] in NON_MEANINGFUL_FLAGS):
        return "", 0

    return words, len(words)


def fuzzy_match_single_best_match(
    word: str,
    choices: typing.Iterable,
    accuracy: float,
    return_vals: typing.Iterable = None,
) -> typing.Tuple:
    """check for the word argument in a list of strings and returns the closest word, assuming the word is in the list
    # this uses fuzzywuzzy.  returns the closest one, if above an accuracy threshold
    otherwise, returns the original word.  Return value is a tuple of the (matched/orig word, accuracy)
    If return_vals is an iterable, it will replace the matched word with the corresponding item in return_vals
    .
    """
    if word is None or len(word) == 0:
        return (word, 0)
    res = process.extractOne(word, choices)
    if res[1] >= accuracy:
        if return_vals is None:
            return res
        idx_of_result = choices.index(res[0])
        return (return_vals[idx_of_result], res[1])
    else:
        return (word, 0)


def fuzzy_match_word(
    word: str,
    fuzzyset: FuzzySet,
    accuracy: float = 0.80,
) -> typing.Tuple:
    """Fuzzy match a word to a fuzzy set."""
    res = fuzzyset.get(word)
    if res is None or not len(res):
        return (None, 0)
    top_res = res[0]
    score, best_match = top_res[0] * 100, top_res[1]
    if score >= accuracy:
        return (best_match, score)
    else:
        return (None, 0)


def fuzzy_match_word_to_manfs(
    word: str,
    accuracy: float = FUZZY_SEARCH_TOLERANCE,
    include_syns: bool = True,
) -> typing.Tuple:
    """Check word is in list of manufacturer synonymns
    Args:
        word (str): Word to be checked and matched
        accuracy (float, optional): Accuracy threshold. Defaults to MANUFACTURER_FUZZY_SEARCH_TOLERANCE.
        include_syns (bool, optional): set False to exclude maufacturer synonyms from being checked.

    Returns
    -------
        (Matched manf, confidence): Tuple of matched manf and confidence. Returns None and confidence 0 if no match found.
    """
    if include_syns:
        res = fuzzyset_MANUFACTURERS_AND_SYNONYMS.get(word)
    else:
        res = fuzzyset_MANUFACTURERS.get(word)
    if res is None or not len(res):
        return (None, 0)
    top_res = res[0]
    score, best_match = top_res[0] * 100, top_res[1]
    if score >= accuracy:
        return (best_match, score)
    else:
        return (None, 0)


def unabbreviate_pos(
    text: str, pos: str, convert_number_words: bool = False
) -> typing.Tuple:
    """Unabbreviate text using specific pos abbreviation list.  Checks n-grams in descending order,
    (allowing for multi-word abbreviations), keeping the first found abbreviations from left to right
    and continuing until no more are found.  simply looping over all possible abbreviations doing search and replace is
    1. slow and 2. does not guarantee the proper behvaior or result
    EG: in the phrase BATH TUB SPOUT, check BATH TUB prior to TUB both of which are abbreviations for BATH.

    Args:
    ----
        text (str): Text to be unabbreviated
        pos (str): Part of speech to consider
        skip_pos_syn_existence_check (bool, optional): Skip checking if POS is valid. Defaults to False.
        convert_number_words: makes words like THREE into their numerical equivalent

    Returns
    -------
        A tuple of:
            (str: Unabbreviated version of text, bool: whether or not a replacement was made)

    TO DO: call myself multiple times on each word to handle the case where multi-word abbreviations
    have been linked together to form ANOTHER abbreviation, like TEMP & PRESSURE RELIEF VLVE
    """
    if text is None:
        return (None, False)

    # check words starting with the highest n-grams and going down
    words = text.translate(
        str.maketrans(REGEX_TEXT_SPLITERS, " " * len(REGEX_TEXT_SPLITERS))
    ).split(" ")
    n_words = len(words)
    final_words = [None] * n_words

    # loop over n-grams levels, assuming abbreviations are at max MAX_ABBR_NGRAM_LEVEL words
    MAX_ABBR_NGRAM_LEVEL = 5
    for ngram_level in reversed(range(1, min(n_words, MAX_ABBR_NGRAM_LEVEL) + 1)):

        def one_unabbr_lookup(text: str) -> typing.Tuple:
            """unabbreviates one text in the given POS
            Returns a tuple of:
                (str: Unabbreviated version of text,
                bool: whether or not a replacement was made,
            ngram_level: the ngram level we are currently on)
            .
            """
            if "dict_abbrs" in POS[pos] and text in POS[pos]["dict_abbrs"]:
                return (POS[pos]["dict_abbrs"][text], True, ngram_level)

            # convert_number_words
            if convert_number_words and text in ORDINAL_WORDS:
                return (str(ORDINAL_WORDS[text]), True, ngram_level)

            return (text, False, ngram_level)

        # get unabbrs for all possible ngrams of this ngram level in the words.
        # for speed, we may skip any ngram in which any word has been previously unabbred.
        unabbrs = [
            None
            if any(final_words[i : i + ngram_level])
            else one_unabbr_lookup(" ".join(words[i : i + ngram_level]))
            for i, word in enumerate(
                words if ngram_level == 1 else words[: -(ngram_level - 1)]
            )
        ]

        # assign the categories of the n-grams (if multi-word) to all words in the phrase,
        # if nothing has been assigned yet; the latter condition should be redundant given the skip logic above.
        i = 0
        while i < len(unabbrs):
            unabbr = unabbrs[i]
            if unabbr is not None and (unabbr[1] or ngram_level == 1):
                final_words[i : i + ngram_level] = [unabbr] * ngram_level
                i += ngram_level
            else:
                i += 1

    # combine the results within each n-gram into one record and form phrases
    i, phrases = 0, []
    while i < len(final_words):
        ngram_length = final_words[i][2]
        del final_words[i + 1 : i + ngram_length]
        phrases.append(final_words[i][0])
        i += 1
    replacement_was_made = any(fw[1] for fw in final_words)

    return (" ".join((fw[0] for fw in final_words)), replacement_was_made)


def get_pos_syns(pos: str, text: str) -> list:
    """returns the synonyms for a given pos and text."""
    df_names = POS[pos]["df_names"]
    df_syns = df_names[df_names.iloc[:, 0] == text]
    if df_syns.empty:
        return []
    syns = df_syns.iloc[0, 1]
    return [] if len(syns) == 0 else syns.split("|")


# def remove_pos_suffixes(text: str, suffixes: typing.Iterable) -> list:
#     '''removes any of the passed suffixes from the text'''
