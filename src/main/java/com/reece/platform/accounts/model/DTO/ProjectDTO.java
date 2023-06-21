package com.reece.platform.accounts.model.DTO;
import lombok.Data;

@Data
public class ProjectDTO {
    private String jobName;
    private Boolean taxable;
    private String lotNoAndTrack;
    private String streetLineOne;
    private String streetLineTwo;
    private String streetLineThree;
    private String city;
    private String state;
    private String postalCode;
    private Float estimatedProjectAmount;
}
