"""Max Search Data Model"""
from typing import Dict, List, Optional

from pydantic import BaseModel, Field, validator

from post.search.facets import FACETS


class MaxSearchMeta(BaseModel):
    SOURCE: str = Field(
        default="MAXDEV", description="Source of the search", example="MAXDEV"
    )
    DEST: str = Field(
        default="MAXDEV", description="Destination of the search", example="MAXDEV"
    )


class MaxSearchPage(BaseModel):
    size: int = Field(description="Number of results per page", example=24, default=24)
    current: int = Field(description="Current page number", example=1, default=1)
    total_pages: Optional[int] = Field(
        description="Total pages number", example=1, default=1
    )
    total_results: Optional[int] = Field(
        description="Total results number", example=1, default=1
    )


class MaxSearch(BaseModel):
    query: Optional[str] = Field(
        description="What you want to search.",
        example="Black kohler toilet",
        default=None,
    )
    search_meta: MaxSearchMeta = MaxSearchMeta(SOURCE="MAXDEV", DEST="MAXDEV")
    page: MaxSearchPage

    filters: Dict[str, List[Dict[str, List[str]]]] = Field(
        description="Filters to include & exclude.",
        default={"all": [], "none": []},
        example={
            "all": [{"mfr_full_name": ["KOHLER CO"]}, {"color_finish": ["BLACK"]}],
            "none": [],
        },
    )

    @validator("filters")
    def check_filters(cls, filters):
        """Ensures all filters have lowercase keys

        Args:
            filters (dict): Filters

        Returns:
            dict: Filters with lowercase keys
        """
        new_all = []
        if "all" in filters.keys():
            for filter in filters["all"]:
                for filter_name, data_list in filter.items():
                    if filter_name.lower() not in list(FACETS.keys()):
                        raise ValueError(f"Filter {filter_name} is not valid.")
                    new_all.append({filter_name.lower(): data_list})

        new_none = []
        if "none" in filters.keys():
            for filter in filters["none"]:
                for filter_name, data_list in filter.items():
                    if filter_name.lower() not in list(FACETS.keys()):
                        raise ValueError(f"Filter {filter_name} is not valid.")
                    new_none.append({filter_name.lower(): data_list})
        filters["all"] = new_all
        filters["none"] = new_none
        return filters
