package com.reece.platform.products.search.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TechDocType {
    MFR_CATALOG_PAGE("MFR Catalog Page", "mfr_catalog_doc_file_name"),
    TECHNICAL_SPECIFICATION("Technical Specification", "mfr_spec_tech_doc_file_name"),
    SDS_SHEET("SDS_SHEET", "mfr_msds_doc_file_name"),
    INSTALLATION_SHEET("Installation Sheet", "mfr_install_instruction_doc_file_name"),
    MFR_ITEM_DATA("MFR Item Data", "mfr_item_data_doc_file_name");

    private final String displayName;
    private final String fileNameKey;
}
