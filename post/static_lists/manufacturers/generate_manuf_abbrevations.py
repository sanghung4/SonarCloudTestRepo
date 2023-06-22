import re
import typing
from itertools import combinations

import pandas as pd

###################################
# generate abbreviations for Manufacturers
###################################

NOT_ABBREVIATION_SUFFIXES = ["&", "(", ")", "AND", "BY", "DE", "/", "-", "OF"]
NOT_MANUF_DELIMETERS = ["AND", "&"]
VOWELS = ["A", "E", "I", "O", "U"]


# remove vowels from a giving word
def remove_vowels(word):
    return "".join([letter for letter in word.upper() if letter not in VOWELS])


# giving a manufacturer name return all possible abbreviations
def make_one_manufacturer_abbr(
    manuf_name: str,
    manuf_abbreviations: pd.DataFrame,
    exclude_list: typing.Iterable = None,
) -> list:
    abbreviations = []

    # strip the name of unnecessary punctuations
    manuf_name = re.sub("[(),]", "", manuf_name)

    # load the abbreviations file as a dictionary
    abbreviations_dict = dict(
        zip(manuf_abbreviations.iloc[:, 0], manuf_abbreviations["ABBRS"].str.split("|"))
    )

    # construct a list of the abbreviations values
    abbreviations_list = list(
        set(
            [
                abbrev
                for abbrevs in list(abbreviations_dict.values())
                for abbrev in abbrevs
            ]
        )
    )

    # check if the last word in the name is a manuf suffix and drop it
    manuf_name_splitted = manuf_name.split()
    if manuf_name_splitted[-1] in abbreviations_list:
        manuf_name_splitted = manuf_name_splitted[:-1]
        abbreviations.append(" ".join(manuf_name_splitted))

    # add possible combination of the first 2,3,.. words
    if len(manuf_name_splitted) > 1:
        for i in range(len(manuf_name_splitted) - 2):
            # add the abbreviation only if the last word isn't in the NOT_ABBREVIATION_SUFFIXES
            if (
                manuf_name_splitted[i + 2] not in NOT_MANUF_DELIMETERS
            ) and manuf_name_splitted[i + 1] not in NOT_ABBREVIATION_SUFFIXES:
                abbreviations.append(" ".join(manuf_name_splitted[: i + 2]))

    # add Maunfacturer abbreviations like CO for COMPANY
    for word in manuf_name_splitted:
        if word in abbreviations_dict.keys():
            for w in abbreviations_dict[word]:
                abbreviations.append(" ".join(manuf_name_splitted).replace(word, w))

    myList = manuf_name.split()
    all_combs = []
    for start, end in combinations(range(len(myList)), 2):
        all_combs.append(myList[start : end + 1])

    for comb in all_combs:
        new_text = manuf_name
        for word in comb:
            if word in abbreviations_dict.keys():
                new_text = new_text.replace(word, abbreviations_dict[word][0])
        abbreviations.append(new_text)

    # remove vowels from manuf_name first 1 or 2 words
    new_words = []
    for word in manuf_name_splitted[:2]:
        if word[0] not in VOWELS:
            word_wo_vowels = remove_vowels(word)
            new_words.append(
                word_wo_vowels if len(word_wo_vowels) >= 3 and word.isalpha() else word
            )
    if (
        (len(new_words) == 2)
        and all((len(nw) >= 3 for nw in new_words))
        or (len(new_words) == 1)
        and len(new_words[0]) >= 4
    ):
        abbreviations.append(" ".join(new_words))

    # remove any terms on the exclude list
    abbreviations = set(abbreviations)
    if exclude_list is not None:
        abbreviations = {abbr for abbr in abbreviations if abbr not in exclude_list}

    return list(abbreviations)


# giving a manufacturer df and abbreviations df add all possible new abbreviations to the original df
def make_manufacturer_syns(
    df_manfs: pd.DataFrame,
    df_manf_abbrs: pd.DataFrame,
    exclude_list: typing.Iterable = None,
) -> pd.DataFrame:
    # split SYNONYMS strings to lists and replace empty values with empty lists
    df_manfs.iloc[:, 1] = (
        df_manfs.iloc[:, 1]
        .str.split("|")
        .apply(lambda d: d if isinstance(d, list) else [])
    )
    # generate all possible abbreviations to each manufacturer name
    df_manfs.iloc[:, 1] = df_manfs.iloc[:, 1] + df_manfs.iloc[:, 0].apply(
        lambda manuf_name: make_one_manufacturer_abbr(
            manuf_name, df_manf_abbrs, exclude_list
        )
    )

    # drop duplicate abbreviations
    df_manfs.iloc[:, 1] = df_manfs.iloc[:, 1].apply(set)
    # convert list of abbreviations to strings with "|" delimeter
    df_manfs.iloc[:, 1] = df_manfs.iloc[:, 1].apply(list).apply("|".join)

    return df_manfs
