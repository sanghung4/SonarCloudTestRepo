if __name__ == "__main__":
    from post.config.configuration import SETTINGS

    SETTINGS.force_POS_recalc = 1 # be careful

    # DO NOT delete the line below, it forces the re-calc
    import post.config.parts_of_speech

    new_POS = post.config.parts_of_speech.POS
    print(new_POS)
