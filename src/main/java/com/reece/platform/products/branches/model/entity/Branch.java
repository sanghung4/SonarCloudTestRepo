package com.reece.platform.products.branches.model.entity;

import com.reece.platform.products.branches.model.enums.MileRadiusEnum;
import com.reece.platform.products.model.ErpEnum;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.h2.util.StringUtils;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Point;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@SQLDelete(sql = "UPDATE branches SET is_active = false WHERE id=?")
@Table(name = "branches")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "pg-uuid")
    private UUID id;

    @Column(name = "branch_id")
    private String branchId;

    @Column(name = "entity_id")
    private String entityId;

    @Column(name = "name")
    private String name;

    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "zip")
    private String zip;

    @Column(name = "phone")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "erp_system_name")
    private ErpEnum erpSystemName;

    @Column(name = "website")
    private String website;

    @Column(name = "workday_id")
    private String workdayId;

    @Column(name = "workday_location_name")
    private String workdayLocationName;

    @Column(name = "rvp")
    private String rvp;

    @Column(name = "acting_branch_manager")
    private String actingBranchManager;

    @Column(name = "acting_branch_manager_phone")
    private String actingBranchManagerPhone;

    @Column(name = "acting_branch_manager_email")
    private String actingBranchManagerEmail;

    @Column(name = "business_hours")
    private String businessHours;

    @Column(name = "is_plumbing")
    private Boolean isPlumbing;

    @Column(name = "is_waterworks")
    private Boolean isWaterworks;

    @Column(name = "is_hvac")
    private Boolean isHvac;

    @Column(name = "is_bandk")
    private Boolean isBandK;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_available_in_store_finder")
    private Boolean isAvailableInStoreFinder;

    @Column(name = "is_shoppable")
    private Boolean isShoppable;

    @Column(name = "is_pricing_only")
    private Boolean isPricingOnly;

    @Column(columnDefinition = "geometry(Point, 4326")
    private Point location;

    @Transient
    private Double latitude;

    @Transient
    private Double longitude;

    @Column(updatable = false, insertable = false)
    private Float distance;

    public Branch(
        String branchId,
        String name,
        String entityId,
        String addressOne,
        String addressTwo,
        String city,
        String state,
        String zip,
        String phone,
        String website,
        String workdayId,
        String workdayLocationName,
        String rvp,
        Boolean isPlumbing,
        Boolean isWaterworks,
        Boolean isHvac,
        Boolean isBandK,
        String actingBranchManager,
        String actingBranchManagerPhone,
        String actingBranchManagerEmail,
        String businessHours
    ) {
        this.branchId = branchId;
        this.name = name;
        this.entityId = entityId;
        this.address1 = addressOne;
        this.address2 = addressTwo;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phone = phone;
        this.website = website;
        this.workdayId = workdayId;
        this.workdayLocationName = workdayLocationName;
        this.rvp = rvp;
        this.isPlumbing = isPlumbing;
        this.isWaterworks = isWaterworks;
        this.isHvac = isHvac;
        this.isBandK = isBandK;
        this.actingBranchManager = actingBranchManager;
        this.actingBranchManagerPhone = actingBranchManagerPhone;
        this.actingBranchManagerEmail = actingBranchManagerEmail;
        this.businessHours = businessHours;

        if (getBrand().equals("Fortiline Waterworks")) {
            this.setErpSystemName(ErpEnum.MINCRON);
        } else {
            this.setErpSystemName(ErpEnum.ECLIPSE);
        }
    }

    public Branch(
        String branchId,
        String name,
        String addressOne,
        String city,
        String state,
        String zip,
        String businessHours,
        String actingBranchManagerEmail,
        Point location
    ) {
        this.branchId = branchId;
        this.name = name;
        this.address1 = addressOne;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.businessHours = businessHours;
        this.actingBranchManagerEmail = actingBranchManagerEmail;
        this.location = location;
    }

    public Double getLatitude() {
        return this.getLocation().getY();
    }

    public Double getLongitude() {
        return this.getLocation().getX();
    }

    public String getBrand() {
        if (this.name.contains("TODD PIPE")) {
            return "Todd Pipe";
        } else {
            // branch name to brand edge cases
            switch (this.name) {
                case "Fortiline Waterworks":
                case "Fortiline Waterworks - HDPE":
                    return "Fortiline Waterworks";
                case "Expressions Home Gallery":
                case "Kohler Experience Center by Expressions Home Gallery":
                    return "Expressions Home Gallery";
                case "Morrison Supply":
                case "Morrison Supply - Showroom":
                    return "Morrison Supply";
                case "L&B Pipe":
                    return "L&B Pipe";
                default:
                    return this.name;
            }
        }
    }

    public String getFullAddress() {
        return String.format(
            "%s%s, %s, %s, %s",
            this.getAddress1(),
            StringUtils.isNullOrEmpty(this.getAddress2()) ? "" : String.format(" %s", this.getAddress2()),
            this.getCity(),
            this.getState(),
            this.getZip()
        );
    }

    /**
     * isActiveAndAvailable
     * Used when filtering list of branches
     * @param isStoreFinder
     * @param isShoppable
     * @return
     */
    public boolean isActiveAndAvailable(Boolean isStoreFinder, Boolean isShoppable) {
        if (!this.getIsActive()) {
            return false;
        }

        if (isStoreFinder != null && isStoreFinder != this.getIsAvailableInStoreFinder()) {
            return false;
        }

        if (isShoppable != null && isShoppable != this.getIsShoppable()) {
            return false;
        }

        return true;
    }

    /**
     * isWithinSearchRadius
     * @param radius
     * @return
     */
    public boolean isWithinSearchRadius(MileRadiusEnum radius) {
        var maxDistance = radius == null ? MileRadiusEnum.MILES_200.getMiles() : radius.getMiles();

        return this.getDistance() < maxDistance;
    }

    /**
     * isInTerritory
     * @param branchIds
     * @return
     */
    public boolean isInTerritory(List<String> branchIds) {
        return branchIds.isEmpty() || branchIds.contains(this.getBranchId());
    }
}
