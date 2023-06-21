package com.reece.platform.products.search.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EnvironmentalOption {
    ENERGY_STAR("Energy Star", "energy_star_flag"),
    HAZARDS_MATERIAL("Hazardous material", "hazardous_material_flag"),
    WATERSENSE_COMPLIANT("WaterSense compliant", "water_sense_compliant_flag"),
    LOW_LEAD_COMPLIANT("Low lead compliant", "low_lead_compliant_flag"),
    MERCURY_FREE("Mercury free", "mercury_free_flag");

    private final String displayName;
    private final String flagKey;
}
