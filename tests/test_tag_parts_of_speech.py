import pytest

from post.processing.tag_parts_of_speech import prepare_text_for_tagging


class TestCleanAndParseWords:
    """Test clean_and_parse_words"""

    def test_int(self):
        """Test what happens when an int is passed"""
        with pytest.raises(UnboundLocalError):
            prepare_text_for_tagging(0)

    def test_single_word(self):
        """Test what happens when a single word is passed"""
        assert prepare_text_for_tagging("test  ") == ["test"]

    def test_sentence(self):
        """Test what happens when a sentence is passed"""
        result = prepare_text_for_tagging("this is x test")
        assert result == ["this", "is", "test"]
