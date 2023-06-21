package com.reece.platform.products.branches.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.h2.util.StringUtils;

@Data
public class WorkdayBranchDTO {

    @JsonProperty("RVP_Phone_Number")
    private String RVP_Phone_Number;

    @JsonProperty("Branch_Manager_Phone_Number")
    private String Branch_Manager_Phone_Number;

    @JsonProperty("Address_-_Line_1")
    private String Address_Line_1;

    @JsonProperty("Address_-_Line_2")
    private String Address_Line_2;

    @JsonProperty("Branch_Manager_Email")
    private String Branch_Manager_Email;

    @JsonProperty("Location_Code")
    private String Location_Code;

    @JsonProperty("GMT_Offset")
    private String GMT_Offset;

    @JsonProperty("Manager")
    private String Manager;

    @JsonProperty("Daylight_Savings_Time")
    private String Daylight_Savings_Time;

    @JsonProperty("RVP")
    private String RVP;

    @JsonProperty("B_K")
    private String B_K;

    @JsonProperty("President")
    private String President;

    @JsonProperty("HVAC")
    private String HVAC;

    @JsonProperty("Location_Attributes")
    private String Location_Attributes;

    @JsonProperty("Phone")
    private String Phone;

    @JsonProperty("Division")
    private String Division;

    @JsonProperty("Zip_Code")
    private String Zip_Code;

    @JsonProperty("Customer_Facing")
    private String Customer_Facing;

    @JsonProperty("Plumbing")
    private String Plumbing;

    @JsonProperty("Waterworks")
    private String Waterworks;

    @JsonProperty("City")
    private String City;

    @JsonProperty("Market")
    private String Market;

    @JsonProperty("Brand")
    private String Brand;

    @JsonProperty("Branch_Name")
    private String Branch_Name;

    @JsonProperty("State")
    private String State;

    @JsonProperty("Region")
    private String Region;

    @JsonProperty("Emergency")
    private String Emergency;

    @JsonProperty("Fax")
    private String Fax;

    @JsonProperty("Headquarters")
    private String Headquarters;

    public String getFullAddress() {
        return String.format(
                "%s%s, %s, %s, %s",
                this.getAddress_Line_1(),
                StringUtils.isNullOrEmpty(this.getAddress_Line_2()) ? "" : String.format(" %s", this.getAddress_Line_2()),
                this.getCity(),
                this.getState(),
                this.getZip_Code()
        );
    }
}
