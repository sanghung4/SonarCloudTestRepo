from post.utils.utils import isnan


class TestIsNan:
    "Test isnan"

    def test_none(self):
        """Test None"""
        assert isnan(None) is True
