package com.reece.platform.products.branches.model.repository;

import com.reece.platform.products.branches.model.entity.Branch;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchesDAO extends JpaRepository<Branch, UUID> {
    @Query(
        value = "SELECT *, st_y(st_astext(location)) as latitude, st_x(st_astext(location)) as longitude, null AS distance FROM branches WHERE branches.branch_id = ?1",
        nativeQuery = true
    )
    List<Branch> findAllByBranchId(String branchId);

    @Query(
        value = "SELECT *, st_y(st_astext(location)) as latitude, st_x(st_astext(location)) as longitude, null AS distance FROM branches WHERE branches.entity_id = ?1",
        nativeQuery = true
    )
    Optional<List<Branch>> findAllByEntityId(String entityId);

    @Query(
        value = "SELECT *, st_y(st_astext(location)) as latitude, st_x(st_astext(location)) as longitude, null AS distance FROM branches",
        nativeQuery = true
    )
    List<Branch> findAllBranches();

    @Query(
        value = "SELECT *, st_y(st_astext(location)) as latitude, st_x(st_astext(location)) as longitude, null AS distance FROM branches LIMIT ?1 OFFSET ?2",
        nativeQuery = true
    )
    List<Branch> findAllBranchesPaginated(Integer pageSize, Integer currentPage);

    @Query(
        value = "SELECT *, st_y(st_astext(location)) as latitude, st_x(st_astext(location)) as longitude, null AS distance FROM branches WHERE branches.branch_id IN ?1",
        nativeQuery = true
    )
    List<Branch> findByBranchIdIn(List<String> branchIds);

    @Query(
        value = "SELECT *, st_y(st_astext(location)) as latitude, st_x(st_astext(location)) as longitude, ST_DISTANCE(ST_POINT(?1, ?2), branches.location)*0.000621371192 AS distance FROM branches ORDER BY ST_DISTANCE(ST_POINT(?1, ?2), branches.location) LIMIT ?3",
        nativeQuery = true
    )
    List<Branch> findByLocation(Float longitude, Float latitude, Integer limit);

    @Query(
        value = "SELECT *, st_y(st_astext(location)) as latitude, st_x(st_astext(location)) as longitude, ST_DISTANCE(branches.location, ST_GeographyFromText(?1))*0.000621371 AS distance FROM branches WHERE branches.branch_id IN ?2 ORDER BY distance",
        nativeQuery = true
    )
    List<Branch> findByBranchIdAndGetDistance(String lonLatString, Collection<String> branchIds);

    @Modifying
    @Query(value = "DELETE FROM branches", nativeQuery = true)
    void deleteAll();

    @Query(
        value = "SELECT *, st_y(st_astext(location)) as latitude, st_x(st_astext(location)) as longitude, null AS distance FROM branches WHERE id = ?1",
        nativeQuery = true
    )
    Optional<Branch> findById(UUID id);

    @Query(
        value = "UPDATE branches SET is_active = ?1, is_available_in_store_finder = ?2, is_shoppable = ?3, is_pricing_only = ?4 WHERE id = ?5 RETURNING *, null AS distance",
        nativeQuery = true
    )
    Branch updateBranch(
        Boolean isActive,
        Boolean isAvailableInStoreFinder,
        Boolean isShoppable,
        Boolean isPricingOnly,
        UUID id
    );

    @Query(
            value = "UPDATE branches SET branch_id = ?1, entity_id= ?2, name = ?3, address1=?4, address2= ?5, city=?6, state=?7, zip=?8, phone=?9, location = ?10, erp_system_name= ?11, website=?12, workday_id =?13, " +
                    "workday_location_name=?14, rvp=?15, is_plumbing=?16, is_waterworks = ?17, is_hvac= ?18, is_bandk=?19, acting_branch_manager=?20, acting_branch_manager_phone =?21," +
                    "acting_branch_manager_email= ?22, business_hours =?23, is_active = ?24, is_available_in_store_finder = ?25, is_shoppable = ?26, is_pricing_only = ?27 WHERE id = ?28 RETURNING *, null AS distance",
            nativeQuery = true
    )
    Branch updateBranch(String branchId, String entityId, String name, String address1, String address2, String city, String state, String zip, String phone, Point location, String erpSystemName, String website, String workdayId, String workdayLocationName, String rvp, Boolean isPlumbing, Boolean isWaterworks, Boolean isHvac, Boolean isBandK, String actingBranchManager, String actingBranchManagerPhone, String actingBranchManagerEmail, String businessHours, Boolean isActive, Boolean isAvailableInStoreFinder, Boolean isShoppable, Boolean isPricingOnly, UUID id);

    @Query(
            value = "INSERT INTO branches(branch_id, entity_id, name, address1, address2, city, state, zip, phone, location, erp_system_name, website, workday_id, " +
                    "workday_location_name, rvp, is_plumbing, is_waterworks, is_hvac, is_bandk, acting_branch_manager, acting_branch_manager_phone, " +
                    "acting_branch_manager_email, business_hours, is_active, is_available_in_store_finder, is_shoppable, is_pricing_only) " +
                    "values(?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13, ?14, ?15, ?16, ?17, ?18, ?19, ?20, ?21, ?22, ?23, ?24, ?25, ?26, ?27) RETURNING *, null AS distance",
            nativeQuery = true
    )
    Branch createBranch(String branchId, String entityId, String name, String address1, String address2, String city, String state, String zip, String phone, Point location, String erpSystemName, String website, String workdayId, String workdayLocationName, String rvp, Boolean isPlumbing, Boolean isWaterworks, Boolean isHvac, Boolean isBandK, String actingBranchManager, String actingBranchManagerPhone, String actingBranchManagerEmail, String businessHours, Boolean isActive, Boolean isAvailableInStoreFinder, Boolean isShoppable, Boolean isPricingOnly);

    @Query(
            value = "SELECT *, null AS distance from branches where branches.branch_id=?1",
            nativeQuery = true
    )
    Branch findByBranchId(String branchId);

    @Query(
            value = "UPDATE branches SET is_active = ?1 WHERE branch_id = ?2 RETURNING *, null AS distance ",
            nativeQuery = true
    )
    Branch updateBranchStatusByBranchId(Boolean isActive, String branchId);

}
