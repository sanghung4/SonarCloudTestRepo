/*
 * Eclipse API Developer Documentation
 * This documentation provides a list of API endpoints provided in this release as well as examples for using the various API endpoints
 *
 * OpenAPI spec version: 9.1.1
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package com.reece.platform.picking.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 * Object representing an Eclipse WarehouseTask.
 */
@Data
public class WarehouseTotePackagesDTO {

    private String invoiceNumber;

    @NotBlank(message = "Invalid parameter: 'branchId' is blank, which is not valid")
    private String branchId;

    @NotBlank(message = "Invalid parameter: 'tote' is blank, which is not valid")
    private String tote;

    private List<PackageDTO> packageList;
}
