import pandas as pd
import typer
from processing.tag_parts_of_speech import tag_parts_of_speech_ngrams
from utils.utils import make_worker_pool

import post.config.configuration

post.config.configuration.USE_WORD_CATEGORY_CACHE = 0

app = typer.Typer()


def normalize_part_name(part: str) -> str:
    """cleans up a potential new part name."""
    try:
        # run through the tagger
        tags = tag_parts_of_speech_ngrams(part)
        if len(tags) == 0:
            return None

        # remove any types at the start
        while tags[0]["pos"] == "TYPE":
            tags.pop(0)

        # make normalized name
        def text_chooser(tag):
            # don't expand a part to have more words
            if tag["correct_text"] is None or tag["correct_text"].count(" ") > tag[
                "text"
            ].count(" "):
                return tag["text"]
            return tag["correct_text"]

        normalized_name = " ".join((text_chooser(tag) for tag in tags))

        return normalized_name

    except Exception as e:
        print(f"Part {part} failed to normalize with exception {e}")
        return None


@app.command()
def make_new_parts():
    """given a file of potential new parts, normalizes them to the extent possible by for example
    removing things that are types, and applying existing abbreviations to them, so that we do not
    corrupt the parts list.
    """
    INPUT_FILE = "post/static_lists/parts/temp_potential_parts.csv"
    OUTPUT_FILE = "post/static_lists/parts/temp_normalized_parts.csv"
    df_pot_parts = pd.read_csv(INPUT_FILE, header=None)

    pool = make_worker_pool(15)
    res = pool.map(normalize_part_name, df_pot_parts.iloc[:, 0])
    df_pot_parts["normalized_part"] = res
    df_pot_parts.to_csv(OUTPUT_FILE)


# entry point.
if __name__ == "__main__":
    app()
