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

package com.reece.platform.eclipse.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Object representing an Eclipse WarehouseTotePackage.
 */
@Schema(description = "Object representing an Eclipse WarehouseTotePackage.")
public class WarehouseTotePackages {

    @JsonProperty("invoiceNumber")
    private String invoiceNumber = null;

    @JsonProperty("branchId")
    private String branchId = null;

    @JsonProperty("tote")
    private String tote = null;

    @JsonProperty("packageList")
    private List<WarehouseTotePackage> packageList = null;

    public WarehouseTotePackages invoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
        return this;
    }

    /**
     * Desc: Order Id
     * @return invoiceNumber
     **/
    @NotNull
    @Schema(required = true, description = "Desc: Order Id")
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public WarehouseTotePackages branchId(String branchId) {
        this.branchId = branchId;
        return this;
    }

    /**
     * Desc: Branch Id
     * @return branchId
     **/
    @NotNull
    @Schema(required = true, description = "Desc: Branch Id")
    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public WarehouseTotePackages tote(String tote) {
        this.tote = tote;
        return this;
    }

    /**
     * Desc: Tote name
     * @return tote
     **/
    @NotNull
    @Schema(required = true, description = "Desc: Tote name")
    public String getTote() {
        return tote;
    }

    public void setTote(String tote) {
        this.tote = tote;
    }

    public WarehouseTotePackages packageList(List<WarehouseTotePackage> packageList) {
        this.packageList = packageList;
        return this;
    }

    public WarehouseTotePackages addPackageListItem(WarehouseTotePackage packageListItem) {
        if (this.packageList == null) {
            this.packageList = new ArrayList<>();
        }
        this.packageList.add(packageListItem);
        return this;
    }

    /**
     * A list of the Packages contained by the Tote.
     * @return packageList
     **/
    @Valid
    @Schema(description = "A list of the Packages contained by the Tote.")
    public List<WarehouseTotePackage> getPackageList() {
        return packageList;
    }

    public void setPackageList(List<WarehouseTotePackage> packageList) {
        this.packageList = packageList;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WarehouseTotePackages warehouseTotePackages = (WarehouseTotePackages) o;
        return (
            Objects.equals(this.invoiceNumber, warehouseTotePackages.invoiceNumber) &&
            Objects.equals(this.branchId, warehouseTotePackages.branchId) &&
            Objects.equals(this.tote, warehouseTotePackages.tote) &&
            Objects.equals(this.packageList, warehouseTotePackages.packageList)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoiceNumber, branchId, tote, packageList);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class WarehouseTotePackages {\n");

        sb.append("    invoiceNumber: ").append(toIndentedString(invoiceNumber)).append("\n");
        sb.append("    branchId: ").append(toIndentedString(branchId)).append("\n");
        sb.append("    tote: ").append(toIndentedString(tote)).append("\n");
        sb.append("    packageList: ").append(toIndentedString(packageList)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
