"""Max Result Data Model"""
from typing import Any, Dict, List, Optional

from pydantic import BaseModel, Field


class MaxProductField(BaseModel):
    raw: Any = Field(description="Raw value", example="", default="Hello!")


class MaxProduct(BaseModel):
    id: Optional[MaxProductField] = MaxProductField(raw=None)
    erp_product_id: Optional[MaxProductField] = MaxProductField(raw=None)
    buylineid: Optional[MaxProductField] = MaxProductField(raw=None)
    mfr_full_name: Optional[MaxProductField] = MaxProductField(raw=None)
    vendor_part_nbr: Optional[MaxProductField] = MaxProductField(raw=None)
    upc_id: Optional[MaxProductField] = MaxProductField(raw=None)
    unspc_id: Optional[MaxProductField] = MaxProductField(raw=None)
    search_keyword_text: Optional[MaxProductField] = MaxProductField(raw=None)
    category_1_name: Optional[MaxProductField] = MaxProductField(raw=None)
    category_2_name: Optional[MaxProductField] = MaxProductField(raw=None)
    category_3_name: Optional[MaxProductField] = MaxProductField(raw=None)
    web_description: Optional[MaxProductField] = MaxProductField(raw=None)
    product_overview_description: Optional[MaxProductField] = MaxProductField(raw=None)
    feature_benefit_list_text: Optional[MaxProductField] = MaxProductField(raw=None)
    customer_part_numbers: Optional[MaxProductField] = MaxProductField(raw=None)
    thumbnail_image_url_name: Optional[MaxProductField] = MaxProductField(raw=None)
    medium_image_url_name: Optional[MaxProductField] = MaxProductField(raw=None)
    full_image_url_name: Optional[MaxProductField] = MaxProductField(raw=None)
    product_line: Optional[MaxProductField] = MaxProductField(raw=None)
    material: Optional[MaxProductField] = MaxProductField(raw=None)
    width: Optional[MaxProductField] = MaxProductField(raw=None)
    height: Optional[MaxProductField] = MaxProductField(raw=None)
    product_sold_count: Optional[MaxProductField] = MaxProductField(raw=None)
    last_update_date: Optional[MaxProductField] = MaxProductField(raw=None)
    cmp: Optional[MaxProductField] = MaxProductField(raw=None)
    minimum_increment_qty: Optional[MaxProductField] = MaxProductField(raw=None)
    territory_exclusion_list: Optional[MaxProductField] = MaxProductField(raw=None)
    colorfinish: Optional[MaxProductField] = MaxProductField(raw=None)
    in_stock_location: Optional[MaxProductField] = MaxProductField(raw=None)
    clean_vendor_part_nbr: Optional[MaxProductField] = MaxProductField(raw=None)
    clean_web_description: Optional[MaxProductField] = MaxProductField(raw=None)
    internal_item_nbr: Optional[MaxProductField] = MaxProductField(raw=None)
    technical_specifications: Optional[MaxProductField] = MaxProductField(raw=None)
    mfr_spec_tech_doc_file_name: Optional[MaxProductField] = MaxProductField(raw=None)
    mfr_install_instruction_doc_file_name: Optional[MaxProductField] = MaxProductField(
        raw=None
    )
    package_volume_nbr: Optional[MaxProductField] = MaxProductField(raw=None)
    package_volume_uom_code: Optional[MaxProductField] = MaxProductField(raw=None)
    package_weight_uom_code: Optional[MaxProductField] = MaxProductField(raw=None)


class MaxFacetDatum(BaseModel):
    value: str
    count: int


class MaxFacet(BaseModel):
    type: str
    data: List[MaxFacetDatum]


class MaxFacetSet(BaseModel):
    mfr_full_name: List[MaxFacet]
    flow_rate: List[MaxFacet]
    capacity: List[MaxFacet]
    hazardous_material_flag: List[MaxFacet]
    wattage: List[MaxFacet]
    voltage: List[MaxFacet]
    water_sense_compliant_flag: List[MaxFacet]
    category_1_name: List[MaxFacet]
    category_2_name: List[MaxFacet]
    category_3_name: List[MaxFacet]
    material: List[MaxFacet]
    pressure_rating: List[MaxFacet]
    inlet_size: List[MaxFacet]
    energy_star_flag: List[MaxFacet]
    low_lead_compliant_flag: List[MaxFacet]
    mercury_free_flag: List[MaxFacet]
    colorfinish: List[MaxFacet]
    btu: List[MaxFacet]
    temperature_rating: List[MaxFacet]
    size: List[MaxFacet]
    height: List[MaxFacet]
    depth: List[MaxFacet]
    width: List[MaxFacet]
    length: List[MaxFacet]
    in_stock_location: List[MaxFacet]
    product_line: List[MaxFacet]


class MaxResultPageMeta(BaseModel):
    current: int
    total_pages: int
    total_results: int
    size: int


class MaxResultMeta(BaseModel):
    alerts: List = []
    warnings: List = []
    precision: int = 0
    page: MaxResultPageMeta
    engine: Dict = {"name": "PoST", "type": "product search"}
    request_id: str = "unspecifed"


class MaxResult(BaseModel):
    meta: Optional[MaxResultMeta] = None
    results: Optional[List[MaxProduct]] = None
    facets: Optional[MaxFacetSet]
