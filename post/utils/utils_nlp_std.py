"""Natural Language Processing Utilities 2
THE FUNCTIONS IN THIS FILE SHOULD HAVE NO DEPENDENCE ON OTHER MODULES OR POS.
"""
import re
import typing
from fractions import Fraction

import inflect
import nltk
import numpy as np
from nltk import WordNetLemmatizer

# NTLK resources
NLTK_RESOURCES = {"stopwords", "words", "omw-1.4", "wordnet"}
nltk.data.path.append("/tmp")
for r in NLTK_RESOURCES:
    nltk.download(r, quiet=True, download_dir="/tmp")
NTLK_ENGLISH_WORDS_SET = set(nltk.corpus.words.words())

# lemmatizer and pluraizer
lemmatizer = WordNetLemmatizer()
# force NTLK LazyCorpusLoader to load so as not to crash in multiprocessing
_ = lemmatizer.lemmatize("test")
pluralizer = inflect.engine()

# GLOBALS and NLP constants - 2023-03-22 added "/" - be aware of and review this choice later
REGEX_TEXT_SPLITERS = r",| |_|-|-|/"

# vulgar fractions
VULGAR_FRACTIONS = {
    "¼": "1/4",
    "½": "1/2",
    "¾": "3/4",
    "⅐": "1/7",
    "⅑": "1/9",
    "⅒": "1/10",
    "⅓": "1/3",
    "⅔": "2/3",
    "⅕": "1/5",
    "⅖": "2/5",
    "⅗": "3/5",
    "⅘": "4/5",
    "⅙": "1/6",
    "⅚": "5/6",
    "⅛": "1/8",
    "⅜": "3/8",
    "⅝": "5/8",
    "⅞": "7/8",
    "⅟": "1/",
}

# NUMBERS FROM 1 TO 10
ORDINAL_WORDS = {
    "ONE": 1,
    "SINGLE": 1,
    "TWO": 2,
    "DOUBLE": 2,
    "DUAL": 2,
    "THREE": 3,
    "TRIPLE": 3,
    "FOUR": 4,
    "FIVE": 5,
    "SIX": 6,
    "SEVEN": 7,
    "EIGHT": 8,
    "NINE": 9,
    "TEN": 10,
}


REGEX_TEXT_ALPHA = re.compile(r"[^A-Za-z]+")


def text_alpha(x: str) -> str:
    """only alpha chars."""
    return re.sub(REGEX_TEXT_ALPHA, "", x)


REGEX_TEXT_NUMERIC = re.compile(r"[^0-9]+")


def text_numeric(x: str) -> str:
    """only numeric chars."""
    return re.sub(REGEX_TEXT_NUMERIC, "", x)


REGEX_TEXT_NON_NUMERIC = re.compile(r"[0-9]+")


def text_nonnumeric(x: str) -> str:
    """only non-numeric chars."""
    return re.sub(REGEX_TEXT_NON_NUMERIC, "", x)


REGEX_TEXT_ALPHANUMERIC = re.compile(r"[^A-Za-z0-9]+")


def text_alphanumeric(x: str) -> str:
    """only alphanumeric chars."""
    return re.sub(REGEX_TEXT_ALPHANUMERIC, "", x)


REGEX_TEXT_PARSABLE_NUM_ATEND = re.compile(r"[0-9.-\/]+")


def text_parsable_num_atend(
    x: str, which_end: int = 1, convert_to_float: bool = False
) -> str:
    """only parts of a parsable number at the start of a string."""
    strs = re.findall(REGEX_TEXT_PARSABLE_NUM_ATEND, x)
    if strs:
        val = strs[-1 if which_end else 0]
        return val if not convert_to_float else parse_str_as_fraction(val)
    else:
        return "" if not convert_to_float else np.NaN


def text_alpha_or_seps_atend(x: str, separators: str = " ", which_end: int = 1) -> str:
    """only those alpha chars or additionally separators at the end of the string
    set which_end = 0 to return the first (start of the string); 1 to return to last (end of the string)
    .
    """
    strs = re.findall(r"[a-zA-Z{separators}]+".format(separators=separators), x)
    if strs:
        return strs[-1 if which_end else 0]
    else:
        return ""


def text_dashclean(x: str, replace_with: str) -> str:
    """replaces dashes in text with the indicated replacement."""
    return x.replace("-", replace_with)


def parse_str_as_fraction(x: str) -> float:
    """attempts to parse a string as a fraction and returns a float."""
    try:
        value = round(
            float(sum(Fraction(s) for s in x.replace("-", " ").split())),
            3,
        )
        return value

    except Exception:
        return None


def replace_all(
    text: str, replacements: typing.Union[str, typing.Iterable], terms: typing.Iterable
) -> str:
    """replace all terms in text with replacements, which may be a string or list."""
    if isinstance(replacements, str):
        for term in terms:
            text = text.replace(term, replacements)
    else:
        for term, replacement in zip(terms, replacements):
            text = text.replace(term, replacement)
    return text


def remove_dbl_spaces(x: str) -> str:
    """remove double spaces, and trim."""
    return re.sub(" +", " ", x).strip()


# various pre-compiled regexs for util functions
PARSABLE_NUMBER_REGEX = re.compile(r"^-?\d{0,}\.?\d+?$")
INT_REGEX = re.compile(r"^[0-9]*$")


def str_is_parsable_number(s: str) -> bool:
    """returns true if the string is parsable as a float or int."""
    s = s.strip()
    return bool(PARSABLE_NUMBER_REGEX.search(s))


def str_is_int(s: str) -> bool:
    """True if the string is an int."""
    return bool(INT_REGEX.search(s))


def is_english_word(text: str, check_length: bool = True) -> bool:
    """check if every word in text is a word in english vocabulary and has length more than 2."""
    words = [w.lower() for w in re.split(REGEX_TEXT_SPLITERS, text)]
    return all(
        [
            i in NTLK_ENGLISH_WORDS_SET and (not check_length or len(i) > 2)
            for i in words
        ]
    )


def multiword_lemmatizer(text: str) -> str:
    """lemmatizes the last word in a multiword phrase."""
    words = text.strip().split(" ")
    words = [w.strip().upper() for w in words if len(w)]
    if (
        len(words) == 0
    ):  # to do: figure out how this can ever be True, but it is apparently sometimes
        return ""
    words[-1] = lemmatizer.lemmatize(words[-1].lower()).upper()
    return " ".join(words)


def multiword_pluralizer(text: str) -> str:
    """pluralizes the last word in a multiword phrase."""
    words = text.strip().split(" ")
    words = [w.strip().upper() for w in words if len(w)]
    words[-1] = pluralizer.plural(words[-1].lower()).upper()
    return " ".join(words)
