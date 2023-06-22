import typing
from copy import deepcopy

from post.config.parts_of_speech import POS
from post.processing.tag_parts_of_speech import tag_parts_of_speech
from post.utils.utils_nlp_std import text_numeric


def related_part_finder(
    part: typing.Union[str, typing.Iterable],
    max_related_parts: int = 50,
    end_to_search: str = "end",
) -> list:
    """expand a PART or PARTS to also include "related" parts
    which for now is determined by assuming the user may have only typed in part of the PART and searching
    for the tagged PART at the END or START of other parts (as determined by the third parameter).
    This should be distinct from the synonyms and abbreviation
    functionality which handle different conceptual adjustments.  Prioritize the related parts by
    shorter length, and limit the results to MAX_RELATED_PARTS
    probably could alternatively sort by frequency of PARTS in the PDW, or $ sales, eventually
    consider replacing this functionality by using the TYPE part of speech and pruning the PARTS list.
    """
    if isinstance(part, str):
        part = [part]
    part = tuple((p.upper() for p in part))
    res = sorted(
        [
            new_part
            for new_part in POS["PART"]["df_names"].iloc[:, 0]
            if (
                (end_to_search == "end" and new_part.endswith(part))
                or (end_to_search == "start" and new_part.startswith(part))
            )
            and new_part not in part
        ],
        key=len,
    )[:max_related_parts]
    return res


def invoke_action(action_tag: dict, query: dict) -> dict:
    """alters a query based upon a custom action
    the action_names should correspond to the actions in actions.json
    to do: eventually replace this glorified giant case statement with a class / parent / child based schema.
    """

    def add_action_tag(
        text: str,
        pos: str,
        query: dict = query,
        disallowed_cats: list = ["ACTION"],
        prec_tag: dict = None,
    ):
        """Create and add action tag to query.
        Args:
            text (str): The text to be tagged.
            pos (str): The part of speech of the text.
            query (dict): The query parameters.
            disallowed_cats (list): The list of disallowed categories.
        Returns:
            str: The text with the action tag added.
        """
        if pos not in POS.keys():
            return query

        # create a new tag
        action_tag = tag_parts_of_speech(
            text, allowed_cats=[pos], disallowed_cats=disallowed_cats
        )

        # specify the preceeding tag, if desired
        action_tag["prec_tag"] = prec_tag

        # add to the query
        query[pos] = [action_tag] + ([] if pos not in query else query[pos])

        return query

    # PARTS: find related parts to this PART, not the actual PART.
    if action_tag["other_info"]["action_name"] == "PARTS":
        if (
            "prec_word" not in action_tag
            or action_tag["prec_word"] is None
            or "PART" not in query
        ):
            return query

        # the part is in the preceding word
        part_tag = [x for x in query["PART"] if x["text"] == action_tag["prec_word"]][0]

        # delete the actual part; we don't want to search for it
        query["PART"] = [
            x for x in query["PART"] if x["correct_text"] != part_tag["correct_text"]
        ]

        # add related parts, as defined by things that START with the passed part,
        # like TOILET FLAPPER for TOILET, copying other fields from the original tag
        related_parts = related_part_finder(
            part_tag["correct_text"], end_to_search="start"
        )
        for rp in related_parts:
            related_part_tag = deepcopy(part_tag)
            related_part_tag.update({"text": rp, "correct_text": rp})
            query["PART"].append(related_part_tag)

        return query

    if action_tag["other_info"]["action_name"] == "PEX_PIPE_SHORTHAND":
        dimensions, manufacturer, color = action_tag["correct_text"].upper().split()
        length, width = dimensions.split("X")
        length = length if len(length) == 1 else (length[0] + "/" + length[1])
        # Assume length is always in feet
        # This assumption is good and necessary
        # They don't sell PEX in inches of length
        dimensions = length + '"' + " X " + width + "'"

        # parse LENGTH and widthand add it to the original query
        query = add_action_tag(dimensions, "DIMENSIONS")

        # add manufacturer to query
        query = add_action_tag(manufacturer, "MANUFACTURER")

        # add color to query
        query = add_action_tag(color, "COLOR")

        # add "PEX" to materials
        query = add_action_tag("PEX", "MATERIAL")

        return query

    if action_tag["other_info"]["action_name"] == "PVCDWV_TEE_FITTING_SHORTHAND":
        # This data is very predictable due to the regex being strict
        length = action_tag["correct_text"].upper().split("PT")[0]

        # Add dimensions to query
        length = (length if len(length) == 1 else (length[0] + "/" + length[1])) + '"'
        query = add_action_tag(length, "DIMENSIONS")

        # add "PVC" as a material
        query = add_action_tag("PVC", "MATERIAL")

        # add "DWV" as a type
        query = add_action_tag("DWV", "TYPE")

        # add tee fitting to parts
        query = add_action_tag("TEE FITTING", "PART")

        return query

    if action_tag["other_info"]["action_name"] == "PVC_CAP_FITTING_SHORTHAND":
        # we can slice safely since the regex is strict
        length, schedule = action_tag["correct_text"].upper()[:-1].split("B")
        schedule = f"SCH{schedule}"

        # parse LENGTH and add it to the original query
        length = (length if len(length) == 1 else (length[0] + "/" + length[1])) + '"'

        # add length to dimensions
        query = add_action_tag(length, "DIMENSIONS")

        # add schedule
        query = add_action_tag(schedule, "SCHEDULE")

        # add ccap fitting to parts
        query = add_action_tag("CAP", "PART")

        return query

    if action_tag["other_info"]["action_name"] == "PVC_COUPLING_FITTING_SHORTHAND":
        length = action_tag["correct_text"].upper().split("PC")[0]

        # parse LENGTH and add it to the original query
        length = (length if len(length) == 1 else (length[0] + "/" + length[1])) + '"'
        query = add_action_tag(length, "DIMENSIONS")

        # add "DWV" as a type
        query = add_action_tag("DWV", "TYPE")

        # add material PVC
        query = add_action_tag("PVC", "MATERIAL")

        # add coupling fitting to parts
        query = add_action_tag("COUPLING FITTING", "PART")

        return query

    if action_tag["other_info"]["action_name"] == "PVC_ELBOW_FITTING_SHORTHAND":
        # This data is very predictable due to the regex being strict
        length = action_tag["correct_text"][:-4]
        bend = action_tag["correct_text"][-2:] + "D"
        schedule = f"SCH{action_tag['correct_text'][-4:-2]}"

        length = (length if len(length) == 1 else (length[0] + "/" + length[1])) + '"'

        # add length to dimensions
        query = add_action_tag(length, "DIMENSIONS")

        # parse BEND and add it to the original query
        query = add_action_tag(bend, "BEND")

        # add schedule
        query = add_action_tag(schedule, "SCHEDULE")

        # # add elbow fitting

        return query

    if action_tag["other_info"]["action_name"] == "BLACK_IRON_BUSHING_SHORTHAND":
        # TODO: Make a recursive function to handle these dimensions, use it for all actions
        length = action_tag["correct_text"].upper().split(".")
        length = [length[0], length[1].upper().split("BB")[0]]
        length[0] = (
            length[0][0] + "/" + length[0][1] if len(length[0]) == 2 else length[0]
        )
        length[1] = (
            length[1][0] + "/" + length[1][1] if len(length[1]) == 2 else length[1]
        )
        length = (length if len(length) == 1 else (length[0] + "x" + length[1])) + '"'

        # add length to dimensions
        query = add_action_tag(length, "DIMENSIONS")

        # add malleable iron to materials
        query = add_action_tag("MALLEABLE IRON", "MATERIAL")

        # add bushing to parts
        query = add_action_tag("BUSHING", "PART")

        return query

    if action_tag["other_info"]["action_name"] == "BLACK_IRON_CAP_SHORTHAND":
        length = action_tag["correct_text"].upper().split("BK")[0]
        length = (length if len(length) == 1 else (length[0] + "/" + length[1])) + '"'

        # add length to dimensions
        query = add_action_tag(length, "DIMENSIONS")

        # add mallable iron to materials
        query = add_action_tag("MALLEABLE IRON", "MATERIAL")

        # add cap to parts
        query = add_action_tag("CAP", "PART")

        return query

    if action_tag["other_info"]["action_name"] == "BLACK_IRON_COUPLING_SHORTHAND":
        length = action_tag["correct_text"].upper().split("BC")[0]
        length = (length if len(length) == 1 else (length[0] + "/" + length[1])) + '"'

        # add length to dimensions
        query = add_action_tag(length, "DIMENSIONS")

        # add mallable iron to materials
        query = add_action_tag("MALLEABLE IRON", "MATERIAL")

        # add coupling to parts
        query = add_action_tag("COUPLING", "PART")

        return query

    if action_tag["other_info"]["action_name"] == "VIEGA_UNION_SHORTHAND":
        length = action_tag["correct_text"].upper().split("PCU")[0]
        length = (length if len(length) == 1 else (length[0] + "/" + length[1])) + '"'

        # add length to dimensions
        query = add_action_tag(length, "DIMENSIONS")

        # add viega to manufacturer
        query = add_action_tag("VIEGA", "MANUFACTURER")

        # add union to parts
        query = add_action_tag("UNION", "PART")

        return query

    if action_tag["other_info"]["action_name"] == "VIEGA_COUPLING_STOP_SHORTHAND":
        length = action_tag["correct_text"].upper().split("PCCWS")[0]
        length = (length if len(length) == 1 else (length[0] + "/" + length[1])) + '"'

        # add length to dimensions
        query = add_action_tag(length, "DIMENSIONS")

        # add viega to manufacturer
        query = add_action_tag("VIEGA", "MANUFACTURER")

        # add stop to type
        query = add_action_tag("STOP", "TYPE")

        # add coupling to parts
        query = add_action_tag("COUPLING", "PART")

        return query

    if action_tag["other_info"]["action_name"] == "COPPER_TEE_SHORTHAND":
        length = action_tag["correct_text"].upper().split("CT")[0]
        length = (length if len(length) == 1 else (length[0] + "/" + length[1])) + '"'

        # add length to dimensions
        query = add_action_tag(length, "DIMENSIONS")

        # This is done to exclude copper press tee fittings
        query = add_action_tag("WROT", "PROCESS")

        # add copper to materials
        query = add_action_tag("COPPER", "MATERIAL")

        # add tee to parts
        query = add_action_tag("TEE", "PART")

        return query

    # COPPER_COUPLING_STOP: xxCCWS means a copper coupling w stop of xx dimensions
    if action_tag["other_info"]["action_name"] == "COPPER_COUPLING_STOP_SHORTHAND":
        length = action_tag["correct_text"].upper().split("CCWS")[0]
        length = (length if len(length) == 1 else (length[0] + "/" + length[1])) + '"'

        # Add the dimensions to the query
        query = add_action_tag(length, "DIMENSIONS")

        # Add the material to the query
        query = add_action_tag("COPPER", "MATERIAL")

        # Specify TYPE = STOP
        query = add_action_tag("STOP", "TYPE")

        # Specify PART = COUPLING
        query = add_action_tag("COUPLING", "PART")

        return query

    # COPPER_FITTING_SHORTHAND:  xxCdd means a copper fitting of x/x length and dd degrees
    if action_tag["other_info"]["action_name"] == "COPPER_FITTING_SHORTHAND":
        length, bend = action_tag["correct_text"].upper().split("C")
        length = (length if len(length) == 1 else (length[0] + "/" + length[1])) + '"'
        bend = bend + ("D" if action_tag["correct_text"][-1] != "D" else "")

        # add length to dimensions
        query = add_action_tag(length, "DIMENSIONS")

        # parse BEND and add it to the original query
        query = add_action_tag(bend, "BEND")

        # add copper
        query = add_action_tag("COPPER", "MATERIAL")

        # add elbow fitting
        query = add_action_tag("ELBOW_FITTING", "PART")

        return query

    # ELBOW_FITTING_SHORTHAND:  like 45 or 90 means elbow fitting of that bend
    if action_tag["other_info"]["action_name"] == "ELBOW_FITTING_SHORTHAND":
        bend = text_numeric(action_tag["correct_text"]) + "D"

        # add bend to BEND
        query = add_action_tag(bend, "BEND")

        # add elbow fitting
        query = add_action_tag("ELBOW_FITTING", "PART")

        return query

    # COPPER_COIL:  means SOFT COPPER PIPE
    if action_tag["other_info"]["action_name"] == "COPPER_COIL":
        # just add the three tags
        query = add_action_tag("COPPER", "MATERIAL")
        query = add_action_tag("SOFT", "TYPE")
        query = add_action_tag("PIPE", "PART")

        return query

    # LowBoy: water heaters with max 2 feet height
    if action_tag["other_info"]["action_name"] == "LOWBOY":
        # add the WATER HEATER as a part to the query
        query = add_action_tag("WATER HEATER", "PART")

        # add a dimensions query to the query object with a HEIGHT of max 2 feet
        # simulate an operator in the preceding tag
        query = add_action_tag(
            "2 FOOT HEIGHT",
            "DIMENSIONS",
            prec_tag={"pos": "OPERATOR", "correct_text": "<="},
        )

        return query

    return None
