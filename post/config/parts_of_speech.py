import itertools
import operator
import os.path
import pickle
import re
import typing

import orjson
import pandas as pd
from loguru import logger

from post.config.configuration import SETTINGS
from post.static_lists.manufacturers.generate_manuf_abbrevations import (
    make_manufacturer_syns,
)
from post.utils.utils_nlp_std import (
    ORDINAL_WORDS,
    multiword_pluralizer,
    remove_dbl_spaces,
)

# certain POS-specific NLP constants which need to be defined at top level as they are referenced elsewhere
UNABBR_DASHES_REGEX = re.compile(r" ?[\-\/] ?")  # dashes "-" and slashes "/"
UNABBR_AMPERSAND_REGEX = re.compile(r" ?[&\+] ?")  # make "&" " AND "
UNABBR_W_REGEX = re.compile(r"^W/")  # W followed by a slash
UNIT_SI_SUFFIXES = {"K": 10e3, "M": 10e6}
MODEL_REGEX_PARSABLE_NUMBER = r"\d{2,}|\d{4,}((.)\d+)"


# The new default behavior is to cache the POS calculations in this file to disk and read from that cache if populated
# otherwise, or if the recalculation flag is on, re-compute everything
# define these variables at top level so that imports from other files work
(
    POS,
    POS_ATTRIBUTES_TYPE,
    POS_UNITS_TYPE,
    POS_GROUPED_BY_PRIORITY,
    POS_PRIORITY_LEVELS,
    MASTER_WORDS_SET,
    MASTER_TERMS_SET,
) = [None] * 7
if not SETTINGS.force_POS_recalc and os.path.exists(SETTINGS.pos_cache_filepath):
    with open(SETTINGS.pos_cache_filepath, "rb") as handle:
        (
            POS,
            POS_ATTRIBUTES_TYPE,
            POS_UNITS_TYPE,
            POS_GROUPED_BY_PRIORITY,
            POS_PRIORITY_LEVELS,
            MASTER_WORDS_SET,
            MASTER_TERMS_SET,
        ) = pickle.load(handle)

else:
    ##################################
    # master record of named entities, their synonyms,
    # static lists enumerating their values and their abbreviations and synonyms, etc.
    ##################################
    POS = {
        "ACTION": {
            "titles": [],
            "priority_level": 200,
            "actions_file": "post/static_lists/actions/actions.json",
            "has_units": False,
            "stored_in_es": False,
            "max_ngram_span": 99,
            "pos_type": "search_type",
        },
        "AMOUNT": {
            "titles": ["EXTENDED NET", "AMOUNT", "AMT"],
            "has_units": False,
            "stored_in_es": False,
            "max_ngram_span": 1,
        },
        "BEND": {
            "titles": ["BEND"],
            "priority_level": 100,
            "name_and_syn_file": "post/static_lists/bends/bends.csv",
            "allow_plural_abbrs": False,
            "allow_plural_syns": False,
            "has_units": True,
            "stored_in_es": True,
            "max_ngram_span": 99,
            "pos_type": "attributes_type",
        },
        "CAPACITANCE": {
            "titles": ["CAPACITANCE", "FARADS"],
            "priority_level": 100,
            "name_and_syn_file": "post/static_lists/capacitances/capacitances.csv",
            "allow_plural_abbrs": False,
            "allow_plural_syns": False,
            "has_units": True,
            "stored_in_es": True,
            "max_ngram_span": 99,
            "pos_type": "units_type",
        },
        "COLOR": {
            "titles": ["COLOR", "COLORS"],
            "name_and_syn_file": "post/static_lists/colors/colors.csv",
            "abbreviations_file": "post/static_lists/colors/abbreviations_colors.csv",
            "allow_plural_abbrs": False,
            "allow_plural_syns": False,
            "has_units": False,
            "stored_in_es": True,
            "max_ngram_span": 99,
            "pos_type": "attributes_type",
        },
        "CONNECTION": {
            "titles": ["CONNECTION", "CONNECTIONS"],
            "name_and_syn_file": "post/static_lists/connections/connections.csv",
            "abbreviations_file": "post/static_lists/connections/abbreviations_connections.csv",
            "allow_plural_abbrs": False,
            "allow_plural_syns": False,
            "has_units": False,
            "stored_in_es": True,
            "max_ngram_span": 99,
            "pos_type": "attributes_type",
        },
        "CURRENT": {
            "titles": ["CURRENT", "AMPS"],
            "priority_level": 100,
            "name_and_syn_file": "post/static_lists/currents/currents.csv",
            "allow_plural_abbrs": False,
            "allow_plural_syns": False,
            "has_units": True,
            "stored_in_es": True,
            "max_ngram_span": 99,
            "pos_type": "units_type",
            "value_bounds": (0, 999),
        },
        "DESC_OR_NOTES": {
            "titles": [
                "DESCRIPTION",
                "NOTES",
                "REMARKS",
                "REMARK",
                "FINISH",
                "ITEM",
                "ITEMS",
                "ITEM DESCRIPTION",
            ],
            "has_units": False,
            "stored_in_es": False,
            "max_ngram_span": 1,  # it does, but this refers to the ngram tagger context
        },
        "DIMENSIONS": {
            "titles": ["DIMENSIONS", "DIMENSION", "DIM"],
            "name_and_syn_file": "post/static_lists/dimensions/dimensions.csv",
            "types_and_syn_file": "post/static_lists/dimensions/dimensions_types.csv",
            "delimiters": ["X", "x", "by", "BY"],
            "has_units": True,
            "stored_in_es": True,
            "max_ngram_span": 99,
        },
        "FEATURE_COUNT": {
            "titles": ["FEATURE_COUNT", "FEATURES"],
            "priority_level": 95,
            "name_and_syn_file": "post/static_lists/feature_counts/feature_counts.csv",
            "abbreviations_file": "post/static_lists/feature_counts/abbreviations_feature_counts.csv",
            "allow_plural_abbrs": True,
            "allow_plural_syns": True,
            "has_units": True,
            "stored_in_es": True,
            "max_ngram_span": 99,
            "pos_type": "units_type",
        },
        "FLOW": {
            "titles": ["FLOW", "FLOWS", "FLOW RATE"],
            "priority_level": 100,
            "name_and_syn_file": "post/static_lists/flows/flows.csv",
            "abbreviations_file": "post/static_lists/flows/abbreviations_flows.csv",
            "allow_plural_abbrs": False,
            "allow_plural_syns": False,
            "has_units": True,
            "stored_in_es": True,
            "max_ngram_span": 99,
            "pos_type": "units_type",
            "value_bounds": (0, 999),
        },
        "FREQUENCY": {
            "titles": ["FREQUENCY", "FREQUENCIES"],
            "priority_level": 100,
            "name_and_syn_file": "post/static_lists/frequencies/frequencies.csv",
            "allow_plural_abbrs": False,
            "allow_plural_syns": False,
            "has_units": True,
            "stored_in_es": True,
            "max_ngram_span": 99,
            "pos_type": "units_type",
        },
        "GAUGE": {
            "titles": ["GAUGE"],
            "priority_level": 100,
            "name_and_syn_file": "post/static_lists/gauges/gauges.csv",
            "allow_plural_abbrs": False,
            "allow_plural_syns": False,
            "has_units": True,
            "stored_in_es": True,
            "max_ngram_span": 99,
            "pos_type": "units_type",
        },
        "HEAT": {
            "titles": ["HEAT", "BTU"],
            "priority_level": 100,
            "name_and_syn_file": "post/static_lists/heats/heats.csv",
            "abbreviations_file": "post/static_lists/heats/abbreviations_heats.csv",
            "allow_plural_abbrs": True,
            "allow_plural_syns": True,
            "has_units": True,
            "stored_in_es": True,
            "max_ngram_span": 99,
            "pos_type": "units_type",
        },
        "ID": {
            "titles": ["ID"],
            "has_units": False,
            "stored_in_es": False,
            "max_ngram_span": 1,
            "pos_type": "ID_type",
            "regex": re.compile(r"^[0-9]{4,10}$"),
        },
        "INFO": {
            "titles": ["INFORMATION", "INFO"],
            "name_and_syn_file": "post/static_lists/informations/informations.csv",
            "allow_plural_abbrs": True,
            "allow_plural_syns": True,
            "has_units": False,
            "stored_in_es": True,
            "max_ngram_span": 99,
            "pos_type": "units_type",
            "allow_reverse_regex": True,
        },
        "LUMNINOUS_FLUX": {
            "titles": ["LUMNINOUS_FLUX", "LUMENS"],
            "priority_level": 100,
            "name_and_syn_file": "post/static_lists/luminous_fluxes/luminous_fluxes.csv",
            "allow_plural_abbrs": False,
            "allow_plural_syns": False,
            "has_units": True,
            "stored_in_es": True,
            "max_ngram_span": 99,
            "pos_type": "units_type",
            "value_bounds": (0, 10e12),
        },
        "MANUFACTURER": {
            "titles": [
                "MFR",
                "MANUFACTURER",
                "MANUF",
                "MANUFACTURER (OR EQUAL)",
                "MFGR",
                "FIXTURE MFR",
                "NOT_SELECTED MFR",
                "MANUF",
                "MANU",
                "MANUFACTURER NO",
                "MANUFACTURER NUM",
                "MANUFACTURER NUMBER",
                "MAKE",
                "MFG",
                "MFR CAT NO",
                "MFR CAT NUM",
                "MFR CAT NUMBER",
                "MANUFACTURER OR CAT #",
            ],
            "name_and_syn_file": "post/static_lists/manufacturers/manufacturers.csv",
            "abbreviations_file": "post/static_lists/manufacturers/abbreviations_manufacturers.csv",
            "has_units": False,
            "stored_in_es": True,
            "max_ngram_span": 99,
            "has_probable_scores": True,
        },
        "MATERIAL": {
            "titles": ["MATERIAL", "MATERIALS"],
            "name_and_syn_file": "post/static_lists/materials/materials.csv",
            "abbreviations_file": "post/static_lists/materials/abbreviations_materials.csv",
            "allow_plural_abbrs": False,
            "allow_plural_syns": False,
            "categories_file": "post/static_lists/materials/categories_materials.csv",
            "has_units": False,
            "stored_in_es": True,
            "max_ngram_span": 99,
            "pos_type": "attributes_type",
            "has_probable_scores": True,
        },
        "MODEL": {
            "titles": [
                "MODEL",
                "FIXTURE MODEL",
                "MODEL NUMBER",
                "MODEL NUM",
                "MODEL # (OR EQUAL)",
                "CATALOG NUMBER",
                "MODEL NO",
                "MODELNO",
                "MODEL NO.",
                "FIXTURE",
                "TRIM",
                "PRODUCTCODE",
                "PRODUCT CODE",
                "CODE",
                "PRODUCT",
                "PRODUCT ID",
                "SERIES",
                "NO",
            ],
            "regex_split": re.compile(r"[^A-Za-z0-9]+"),
            "regex_split_lastpart": re.compile(r"(.*)[^A-Za-z0-9]+(.*)"),
            "has_units": False,
            "stored_in_es": True,
            "max_ngram_span": 3,
            "subword_categories_indicating_not_model": [
                "PART",
                "MANUFACTURER",
                "DIMENSIONS",
                "PROCESS",
                "TYPE",
                "MATERIAL",
                "VOLTAGE",
                "ID",
                "FEATURE_COUNT",
                "COLOR",
                "POWER",
                "FREQUENCY",
                "HEAT",
                "VOLUME",
                "TAG",
                "CURRENT",
                "FREQUENCY",
                "OTHER",
                "QUANTITY",
                "PRESSURE_WGT",
                "FLOW",
            ],
        },
        "MSC": {
            "titles": ["MSC"],
            "has_units": False,
            "stored_in_es": False,
            "max_ngram_span": 1,
            "pos_type": "ID_type",
            "regex": re.compile(r"(?i)^(MSC)-?[0-9]+$"),
        },
        "OPERATOR": {
            "titles": None,
            "has_units": False,
            "stored_in_es": True,
            "max_ngram_span": 1,
            "pos_type": "operator_type",
            "priority_level": 199,
            "operators": {
                "<": operator.lt,
                ">": operator.gt,
                "<=": operator.le,
                ">=": operator.ge,
                "=": operator.eq,
                "==": operator.eq,
            },
        },
        "OTHER": {
            "titles": None,
            "has_units": False,
            "stored_in_es": True,
            "max_ngram_span": 1,  # don't change this
            "pos_type": "other_type",
        },
        "PART": {
            "titles": ["PART"],
            "priority_level": 99,
            "name_and_syn_file": "post/static_lists/parts/parts.csv",
            "abbreviations_file": "post/static_lists/parts/abbreviations_parts.csv",
            "categories_file": "post/static_lists/parts/categories_parts.csv",
            "allow_plural_abbrs": True,
            "allow_plural_syns": True,
            "allow_plural_cats": True,
            "has_units": False,
            "stored_in_es": True,
            "max_ngram_span": 99,
            "has_probable_scores": True,
        },
        "POWER": {
            "titles": ["POWER"],
            "priority_level": 100,
            "name_and_syn_file": "post/static_lists/powers/powers.csv",
            "abbreviations_file": "post/static_lists/powers/abbreviations_powers.csv",
            "allow_plural_abbrs": True,
            "allow_plural_syns": True,
            "has_units": True,
            "stored_in_es": True,
            "max_ngram_span": 99,
            "pos_type": "units_type",
        },
        "PRESSURE_WGT": {
            "titles": ["PRESSURE", "PRESS.", "PSI"],
            "priority_level": 100,
            "name_and_syn_file": "post/static_lists/pressure_wgts/pressure_wgts.csv",
            "allow_plural_abbrs": True,
            "allow_plural_syns": True,
            "has_units": True,
            "stored_in_es": True,
            "max_ngram_span": 99,
            "pos_type": "units_type",
        },
        "PRICE": {
            "titles": ["UNITPRICE", "PRICE", "NET"],
            "has_units": True,
            "stored_in_es": False,
            "max_ngram_span": 1,
        },
        "PROCESS": {
            "titles": ["PROCESS", "PROCESSES"],
            "name_and_syn_file": "post/static_lists/processes/processes.csv",
            "allow_plural_abbrs": False,
            "allow_plural_syns": False,
            "has_units": False,
            "stored_in_es": True,
            "max_ngram_span": 99,
            "pos_type": "attributes_type",
        },
        "QUANTITY": {
            "titles": ["QUANTITY", "QTY"],
            "priority_level": 100,
            "name_and_syn_file": "post/static_lists/quantities/quantities.csv",
            "has_units": True,
            "stored_in_es": True,
            "max_ngram_span": 99,
            "pos_type": "units_type",
            "allow_reverse_regex": True,
        },
        "SCHEDULE": {
            "titles": ["SCHEDULE"],
            "priority_level": 100,
            "name_and_syn_file": "post/static_lists/schedules/schedules.csv",
            "abbreviations_file": "post/static_lists/schedules/abbreviations_schedules.csv",
            "has_units": False,
            "stored_in_es": True,
            "max_ngram_span": 99,
            "pos_type": "attributes_type",
        },
        "TAG": {
            "titles": [
                "TYPE",
                "MARK",
                "TAG",
                "SELECTED ID",
                "FIXTURE TAG",
                "ID",
                "DESIGNATION",
                "DESIG",
                "SYMBOL",
                "FIXTURE DESIGNATION",
                "PLAN CODE",
                "MARK MARK",
                "ITEM NO.",
                "PNO",
                "PLAN MARK",
                "REF",
                "REFERENCE",
                "REF NO",
                "REFERENCE NO",
                "REF NUM",
                "REF NUMBER",
                "REFERENCE NUMBER",
                "FIXTURE SYMBOL",
                "EQUIPMENT SYMBOL",
                "UNIT NUMBER",
                "PLAN TAG",
            ],
            "regex": re.compile(
                r"[A-Z]{1,4}(-)\d{0,3}$|\b[A-Z]{1,4}[0-9]{0,1}$|\b[A-Z]{1,3}\d{1,2}\b|\b[A-Z]{1}\d{1,3}\b|[A-Z]{0,2}(-)\d{1}[A-Z]{1}$"
            ),
            "not_names": ["ADA"],
            "has_units": False,
            "stored_in_es": False,
            "max_ngram_span": 99,
        },
        "TEMPERATURE": {
            "titles": ["TEMPERATURE", "TEMP"],
            "priority_level": 100,
            "name_and_syn_file": "post/static_lists/temperatures/temperatures.csv",
            "allow_plural_abbrs": False,
            "allow_plural_syns": False,
            "has_units": True,
            "stored_in_es": True,
            "max_ngram_span": 99,
            "pos_type": "units_type",
            "value_bounds": (-200, 999),
        },
        "THREAD": {
            "titles": ["THREAD"],
            "name_and_syn_file": "post/static_lists/threads/threads.csv",
            "allow_plural_abbrs": False,
            "allow_plural_syns": False,
            "has_units": False,
            "stored_in_es": True,
            "max_ngram_span": 99,
            "pos_type": "attributes_type",
        },
        "TIME": {
            "titles": ["TIME"],
            "priority_level": 100,
            "name_and_syn_file": "post/static_lists/times/times.csv",
            "abbreviations_file": "post/static_lists/times/abbreviations_times.csv",
            "allow_plural_abbrs": True,
            "allow_plural_syns": True,
            "has_units": True,
            "stored_in_es": True,
            "max_ngram_span": 99,
            "pos_type": "units_type",
        },
        "TYPE": {
            "titles": ["TYPE"],
            "name_and_syn_file": "post/static_lists/types/types.csv",
            "abbreviations_file": "post/static_lists/types/abbreviations_types.csv",
            "allow_plural_abbrs": False,
            "allow_plural_syns": False,
            "has_units": False,
            "stored_in_es": True,
            "max_ngram_span": 99,
            "pos_type": "attributes_type",
        },
        "UPC": {
            "titles": ["UPC"],
            "has_units": False,
            "stored_in_es": False,
            "max_ngram_span": 1,
            "pos_type": "ID_type",
            "regex": re.compile(r"^[0-9]{11}$"),
        },
        "VOLTAGE": {
            "titles": ["VOLTAGE"],
            "priority_level": 100,
            "name_and_syn_file": "post/static_lists/voltages/voltages.csv",
            "allow_plural_abbrs": True,
            "allow_plural_syns": True,
            "has_units": True,
            "stored_in_es": True,
            "max_ngram_span": 99,
            "pos_type": "units_type",
            "value_bounds": (0, 999),
        },
        "VOLUME": {
            "titles": ["VOLUME"],
            "priority_level": 100,
            "name_and_syn_file": "post/static_lists/volumes/volumes.csv",
            "abbreviations_file": "post/static_lists/volumes/abbreviations_volumes.csv",
            "allow_plural_abbrs": True,
            "allow_plural_syns": True,
            "has_units": True,
            "stored_in_es": True,
            "max_ngram_span": 99,
            "pos_type": "units_type",
        },
    }

    # programatically add joint columns containing a combination of information
    JOINT_COLUMN_CONCATENATORS = ["&", "/", "AND", "-"]

    def joint_col_concat(col1: str, col2: str) -> list:
        """a helper function to combine joint column names."""
        return [
            " ".join(tup)
            for tup in list(
                itertools.product(
                    *[
                        POS[col1]["titles"],
                        JOINT_COLUMN_CONCATENATORS,
                        POS[col2]["titles"],
                    ]
                )
            )
        ]

    POS.update(
        {
            "MANUFACTURER_AND_MODEL": {
                "titles": joint_col_concat("MANUFACTURER", "MODEL") + ["ACCESSORIES"]
            },
            "TAG_AND_OR_PART": {
                "titles": joint_col_concat("TAG", "DESC_OR_NOTES") + ["FIXTURE NAME"],
            },
        }
    )

    ##################################
    # LISTS OF ABBREVIATIONS AND SYNONYMS
    ##################################
    def syn_or_abbr_expander(
        df: pd.DataFrame, syn_or_abbr_type: str, allow_plurals: bool = True
    ) -> dict:
        """expands a df of names vs. pipe delimited (synonyms or abbreviations) into row-based format
        maps all possible entity names and abbrs to the proper entity into a new dict
        type should be one of ['syns', 'abbrs']
        also generates some automatic synonyms based on "-" or "&" being present.
        """
        tmp_dict = {
            x: {syn_or_abbr_type: y.split("|")} for x, y in df.itertuples(index=False)
        }

        def syn_or_abbr_additional(text: str) -> str:
            """handles "-" and "&" and "/" being in names or abbreviations."""
            additional_vals = []

            # to do: consider handling the many combinations generated by these replacements programmatically
            # using either a common set of reducing rules on both input and static lists, or fuzzy matching,
            # or something else.  The current way should have some  benefit of less computation in the loop.

            # dashes / AND
            dash_replacements = [" ", ""]
            for dr in dash_replacements:
                additional_val = re.sub(UNABBR_DASHES_REGEX, dr, text)
                additional_val = remove_dbl_spaces(
                    re.sub(UNABBR_AMPERSAND_REGEX, " AND ", additional_val)
                )
                if additional_val != text:
                    additional_vals.append(additional_val)

            # slashes / PER
            per_replacements = ["/", " /", "/ ", " / "]
            for pr in per_replacements:
                additional_val = text.replace(" PER ", pr)
                if additional_val != text:
                    additional_vals.append(additional_val)

            return additional_vals

        def new_dict_gen() -> typing.Tuple:
            """generator to loop over the temporary dict."""
            for key in tmp_dict.keys():
                # add the original entry into the dict, if a synonym
                if syn_or_abbr_type == "syns":
                    yield (key, key)
                    for new_abbr in syn_or_abbr_additional(key):
                        yield (new_abbr, key)

                    if allow_plurals:
                        yield (multiword_pluralizer(key), key)
                        for new_abbr in syn_or_abbr_additional(key):
                            yield (multiword_pluralizer(new_abbr), key)

                # loop over all terms and yield the synonym or abbr (plus some variants of it) mapped to the master value
                for syn_or_abbr in tmp_dict[key][syn_or_abbr_type]:
                    if len(syn_or_abbr) != 0:
                        yield (syn_or_abbr, key)

                        new_abbrs = syn_or_abbr_additional(syn_or_abbr)
                        for new_abbr in new_abbrs:
                            yield (new_abbr, key)

                        if allow_plurals:
                            yield (multiword_pluralizer(syn_or_abbr), key)
                            for new_abbr in new_abbrs:
                                yield (multiword_pluralizer(syn_or_abbr), key)

        return dict(list(new_dict_gen()))

    # for each named entity, create a dataframe of the names,
    # a dict mapping all possible names and synonyms to names, and a dict of all abbreviations
    def make_all_dicts(
        pos: str, pos_json: dict, skip_load_from_disk: bool = False
    ) -> None:
        """Makes all synonym, abbr, type dicts for a given POS."""
        # check for a name and synonym file
        if "name_and_syn_file" in pos_json:
            if not skip_load_from_disk:
                try:
                    pos_json["df_names"] = pd.read_csv(
                        pos_json["name_and_syn_file"], keep_default_na=False
                    ).applymap(str.upper)
                except Exception as e:
                    logger.critical(
                        f"The name_and_syn_file is not formatted properly for POS {pos}.  {e}"
                    )
                    raise Exception(
                        f"The name_and_syn_file is not formatted properly for POS {pos}.  {e}"
                    )
            allow_plural_syns = (
                False
                if "allow_plural_syns" not in POS[pos]
                else POS[pos]["allow_plural_syns"]
            )
            pos_json["dict_names_syns"] = syn_or_abbr_expander(
                pos_json["df_names"], "syns", allow_plural_syns
            )

        # check for an abbreviation file
        if "abbreviations_file" in pos_json:
            if not skip_load_from_disk:
                try:
                    pos_json["df_abbrs"] = pd.read_csv(
                        pos_json["abbreviations_file"], keep_default_na=False
                    ).applymap(str.upper)

                except Exception as e:
                    logger.critical(
                        f"The abbreviations_file is not formatted properly for POS {pos}.  {e}"
                    )
                    raise Exception(
                        f"The abbreviations_file is not formatted properly for POS {pos}.  {e}"
                    )
            allow_plural_abbrs = (
                True
                if "allow_plural_abbrs" not in POS[pos]
                else POS[pos]["allow_plural_abbrs"]
            )
            pos_json["dict_abbrs"] = syn_or_abbr_expander(
                pos_json["df_abbrs"], "abbrs", allow_plural_abbrs
            )

        # check for a types and synonym file
        if "types_and_syn_file" in pos_json:
            if not skip_load_from_disk:
                try:
                    pos_json["df_types"] = pd.read_csv(
                        pos_json["types_and_syn_file"], keep_default_na=False
                    ).applymap(str.upper)
                except Exception as e:
                    logger.critical(
                        f"The types_and_syn_file is not formatted properly for POS {pos}.  {e}"
                    )
                    raise Exception(
                        f"The types_and_syn_file is not formatted properly for POS {pos}.  {e}"
                    )
            pos_json["dict_types_syns"] = syn_or_abbr_expander(
                pos_json["df_types"], "syns", False
            )

        # check for a categories file
        if "categories_file" in pos_json:
            if not skip_load_from_disk:
                try:
                    pos_json["df_cats"] = pd.read_csv(
                        pos_json["categories_file"], keep_default_na=False
                    ).applymap(str.upper)
                except Exception as e:
                    logger.critical(
                        f"The categories_file is not formatted properly for POS {pos}.  {e}"
                    )
                    raise Exception(
                        f"The categories_file is not formatted properly for POS {pos}.  {e}"
                    )
            pos_json["dict_cats"] = {
                x: y.split("|") for x, y in pos_json["df_cats"].itertuples(index=False)
            }
            allow_plural_cats = (
                True
                if "allow_plural_cats" not in POS[pos]
                else POS[pos]["allow_plural_cats"]
            )
            if allow_plural_cats:
                pos_json["dict_cats"] = {
                    **pos_json["dict_cats"],
                    **{
                        multiword_pluralizer(x): y.split("|")
                        for x, y in pos_json["df_cats"].itertuples(index=False)
                    },
                }

    for pos, pos_json in POS.items():
        make_all_dicts(pos, pos_json)

    # custom adjustment for MANUFACTURER;
    # take the first list and further expand it to handle co suffixes and also remove vowels
    # don't make an abbr which is a part
    POS["MANUFACTURER"]["df_names"] = make_manufacturer_syns(
        POS["MANUFACTURER"]["df_names"],
        POS["MANUFACTURER"]["df_abbrs"],
        list(POS["PART"]["dict_names_syns"].keys()),
    )
    make_all_dicts("MANUFACTURER", POS["MANUFACTURER"], skip_load_from_disk=True)

    # custom adjustment for PARTS, remove " KIT" from any name or synonym and make a new entry without it
    # also process " SET", " ASSEMBLY", ETC and add those as syns also
    KIT_OR_SET_INDICATORS = ("ASSEMBLY", "ASSEMBLIES", "KIT", "KITS", "SET", "SETS")
    for kosi in KIT_OR_SET_INDICATORS:
        # find the part stems
        matching_part_stems = {
            key[0 : -(len(kosi) + 1)]: val
            for key, val in POS["PART"]["dict_names_syns"].items()
            if key.endswith(" " + kosi)
        }

        # add both the stem and the stem suffixed by all the KIT_OR_SET_INDICATORS
        for mps in matching_part_stems:
            # the stem may already be a synonym, in which case, translate it to the actual part.
            mps_clean = (
                POS["PART"]["dict_names_syns"][mps]
                if mps in POS["PART"]["dict_names_syns"]
                else mps
            )

            # update the dict
            POS["PART"]["dict_names_syns"].update(
                {
                    **{mps_clean: mps_clean},
                    **{
                        mps_clean + " " + kosi2: mps_clean
                        for kosi2 in KIT_OR_SET_INDICATORS
                    },
                }
            )

    ##################################
    # REGEX
    ##################################

    # a fraction, number or decimal
    REGEX_PARSABLE_NUMBER = r"(\d(\/\d{1,3})|(\d{1,3}(\s|-))?\d{1,4}(\/\d{1,3})?((-)(\d{1,3}))?|\d{0,3}\.\d+)"
    DIMENSIONS_REGEX_PARSABLE_NUMBER = r"(\d(\/\d{1,3})|(\d{1,3}(\s|-))?\d{1,3}(\/\d{1,3})?((-)(\d{1,3}))?|\d{0,3}\.\d+)"

    # several of the regex are made in the same way for quantities ending in units.
    # add the synonyms AND abbreviations here
    POS_UNITS_TYPE = [
        pos
        for pos in POS
        if "pos_type" in POS[pos] and POS[pos]["pos_type"] == "units_type"
    ]
    for pos in POS_UNITS_TYPE:
        syns_and_abbrs = set(POS[pos]["dict_names_syns"].keys())
        if "dict_abbrs" in POS[pos]:
            syns_and_abbrs |= set(POS[pos]["dict_abbrs"].keys())

        # the regex for the standard way of writing units, like 100 F
        # we allow dashes or spaces to delimit the suffixes but not SI units
        # also allow for terms like "TWO-PIECE" here with English words for numbers
        POS[pos]["regex"] = re.compile(
            r"^({PARSABLE_NUMBER}|({ORDINAL_WORDS}))\s?[{UNIT_SI_SUFFIXES}]?[\s-]?(?i)({suffix})\Z".format(
                PARSABLE_NUMBER=REGEX_PARSABLE_NUMBER,
                ORDINAL_WORDS="|".join(ORDINAL_WORDS),
                UNIT_SI_SUFFIXES="|".join(UNIT_SI_SUFFIXES.keys()),
                suffix="|".join(syns_and_abbrs),
            )
        )

        # the regex for the reversed way of writing units, like F 100
        # note that we disallow the dash in this one, as opposed to above
        POS[pos]["regex_reversed"] = re.compile(
            r"^(?i)({suffix})\s?{PARSABLE_NUMBER}\s?[{UNIT_SI_SUFFIXES}]?\Z".format(
                PARSABLE_NUMBER=REGEX_PARSABLE_NUMBER,
                UNIT_SI_SUFFIXES="|".join(UNIT_SI_SUFFIXES.keys()),
                suffix="|".join(syns_and_abbrs),
            )
        )

    POS_ATTRIBUTES_TYPE = [
        pos
        for pos in POS
        if "pos_type" in POS[pos] and POS[pos]["pos_type"] == "attributes_type"
    ]

    ##################################
    # some POS have regex that need to be made and/or adjusted manually
    ##################################

    ### DIMENSIONS
    DIMENSIONS_REGEXES = []

    # one dimensions part which can be a number with or without suffix with or without type
    DIMENSIONS_REGEX_DIM_PART = (
        r"(({types})?(\s)?{PARSABLE_NUMBER}(\s|-)?({suffixes})?(\s)?({types})?)".format(
            suffixes="|".join(
                (re.escape(x) for x in POS["DIMENSIONS"]["dict_names_syns"].keys())
            ),
            types="|".join(
                [re.escape(x) for x in POS["DIMENSIONS"]["dict_types_syns"].keys()]
            ),
            PARSABLE_NUMBER=DIMENSIONS_REGEX_PARSABLE_NUMBER,
        )
    )

    # allow for up to three dimension parts to be delimited by the delimiters
    DIMENSIONS_REGEXES.append(
        r"^{dim_part}((\s)?({delims})(\s)?{dim_part}){{0,2}}$".format(
            delims="|".join((re.escape(x) for x in POS["DIMENSIONS"]["delimiters"])),
            dim_part=DIMENSIONS_REGEX_DIM_PART,
        )
    )

    # an alternate way
    DIMENSIONS_REGEXES.append(r"^(#|M)([1-9]|[12][0-9]|3[00])$")

    # a regex to exclude the pattern of two numbers with a space
    NO_SEQUENTIAL_NUMBERS_REGEX = "^(?!\d{1,}(\s)\d{1,}$).*$"

    # combine the DIMENSION_REGEXES with the pattern to be excluded
    FINAL_DIM_REGEX = (
        r"(?={no_sequential_separated_numbers_reg})(?={dim_regexes})".format(
            no_sequential_separated_numbers_reg=NO_SEQUENTIAL_NUMBERS_REGEX,
            dim_regexes="|".join(DIMENSIONS_REGEXES),
        )
    )

    POS["DIMENSIONS"]["regex"] = re.compile(FINAL_DIM_REGEX)

    # another regex for dimensions splitters only.
    POS["DIMENSIONS"]["split_regex"] = re.compile(
        r"\s*{delims}\s*".format(
            delims="|".join((re.escape(x) for x in POS["DIMENSIONS"]["delimiters"]))
        )
    )

    # another regex for dimensions types only.
    POS["DIMENSIONS"]["types_regex"] = re.compile(
        r"{delims}".format(
            delims="|".join(
                re.escape(x) for x in POS["DIMENSIONS"]["dict_types_syns"].keys()
            )
        )
    )

    ### MODEL
    MODEL_REGEXES = []

    # mixed string of word letters and digits
    MODEL_REGEX_PARSABLE_ALPHANUMERIC = r"^(?=.*[a-zA-Z])(?=.*\d)?[A-Za-z\d]{4,}$"

    # allow to match more complicated expressions that includes some punctuations
    MODEL_REGEXES.append(r"^(?=.*[0-9])([A-Z0-9.:\/X~-]{3,})$")

    # allow positive lookahead of numerical part
    MODEL_REGEXES.append(
        r"(?=.*[0-9])({PARSABLE_NUMBER})([a-zA-Z\d\s/~]{{5,}})?$".format(
            PARSABLE_NUMBER=MODEL_REGEX_PARSABLE_NUMBER
        )
    )

    # allow an alphanumeric string followed by / and alphanumeric letters
    MODEL_REGEXES.append(
        r"({PARSABLE_ALPHANUMERIC})(\s)?((\w+)?\/\w+)?(\s\(\w+\))?$".format(
            PARSABLE_ALPHANUMERIC=MODEL_REGEX_PARSABLE_ALPHANUMERIC
        )
    )

    # allow different compinations of alhpanumeric parts delimeted by '-'
    MODEL_REGEXES.append(
        r"\b(\w{1,})(-)([\/a-zA-Z0-9]{1,})(-)\S+$|\b([\/a-zA-Z0-9]{1,})(-)([\/a-zA-Z0-9_]{1,}|(\d+\.\d+))(\s\w+)?(\s?\(\w+\))?$|^[A-Za-z]{2,}\/[A-Za-z]{2,}$"
    )

    # allow any letter after '#'
    MODEL_REGEXES.append(r"#\S+")

    POS["MODEL"]["regex"] = re.compile("|".join(MODEL_REGEXES))

    # make a titles regex out of the model titles
    POS["MODEL"]["regex_titles"] = re.compile(
        r"({MODEL_TITLES})\s".format(
            MODEL_TITLES="|".join(
                sorted(
                    (re.escape(x) for x in POS["MODEL"]["titles"]),
                    key=len,
                    reverse=True,
                )
            )
        )
    )

    ### OPERATORS
    POS["OPERATOR"]["regex"] = re.compile(
        r"^({opers})$".format(opers="|".join(POS["OPERATOR"]["operators"]))
    )

    ##################################
    # ACTIONS
    ##################################
    with open(POS["ACTION"]["actions_file"], "r") as handle:
        file = handle.read()
    POS["ACTION"]["actions"] = orjson.loads(file)
    POS["ACTION"]["actions"] = {
        k.upper(): v for k, v in POS["ACTION"]["actions"].items()
    }
    for pos, pos_json in POS["ACTION"]["actions"].items():
        POS["ACTION"]["actions"][pos]["regex"] = re.compile(
            POS["ACTION"]["actions"][pos]["regex_str"]
        )

    ##################################
    # POS PRIORITIES
    ##################################
    for pos, val in POS.items():
        if "priority_level" not in val:
            val["priority_level"] = 1
    POS_PRIORITY_LEVELS = sorted(
        list(set((val["priority_level"] for pos, val in POS.items()))), reverse=True
    )
    POS_GROUPED_BY_PRIORITY = {
        ppl: [key for key, val in POS.items() if val["priority_level"] == ppl]
        for ppl in POS_PRIORITY_LEVELS
    }

    ##################################
    # MASTER DICTIONARY OF RECOGNIZED TERMS AND SYNOYMS
    ##################################

    # for use in custom spell check and other places.
    MASTER_TERMS_SET = set()
    for pos in POS:
        if "dict_names_syns" in POS[pos]:
            MASTER_TERMS_SET |= set(POS[pos]["dict_names_syns"].keys())
            MASTER_TERMS_SET |= set(POS[pos]["dict_names_syns"].values())
        if "dict_abbrs" in POS[pos]:
            MASTER_TERMS_SET |= set(POS[pos]["dict_abbrs"].keys())
            MASTER_TERMS_SET |= set(POS[pos]["dict_abbrs"].values())
        if "actions" in POS[pos]:
            MASTER_TERMS_SET |= set(POS[pos]["actions"].keys())

    # MASTER DICTIONARY OF RECOGNIZED WORDS
    MASTER_WORDS_SET = set(
        itertools.chain.from_iterable((x.split() for x in MASTER_TERMS_SET))
    )

    # cache the POS and other objects to disk
    cache_obj = [
        POS,
        POS_ATTRIBUTES_TYPE,
        POS_UNITS_TYPE,
        POS_GROUPED_BY_PRIORITY,
        POS_PRIORITY_LEVELS,
        MASTER_WORDS_SET,
        MASTER_TERMS_SET,
    ]
    with open(SETTINGS.pos_cache_filepath, "wb") as handle:
        pickle.dump(cache_obj, handle)

Debug = True
