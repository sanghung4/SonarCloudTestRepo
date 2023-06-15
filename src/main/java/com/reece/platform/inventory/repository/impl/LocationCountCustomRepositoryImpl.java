package com.reece.platform.inventory.repository.impl;

import com.reece.platform.inventory.dto.LocationDTO;
import com.reece.platform.inventory.dto.LocationProductDTO;
import com.reece.platform.inventory.dto.LocationSummaryDTO;
import com.reece.platform.inventory.dto.variance.VarianceLocationDTO;
import com.reece.platform.inventory.dto.variance.VarianceLocationProductDTO;
import com.reece.platform.inventory.dto.variance.VarianceLocationSummaryDTO;
import com.reece.platform.inventory.model.*;
import com.reece.platform.inventory.repository.LocationCountCustomRepository;
import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

@Slf4j
public class LocationCountCustomRepositoryImpl
    extends QuerydslRepositorySupport
    implements LocationCountCustomRepository {

    public LocationCountCustomRepositoryImpl() {
        super(LocationCount.class);
    }

    @Override
    public Optional<LocationCount> findLocationCount(String branchId, String countId, String locationId) {
        val locationCount = QLocationCount.locationCount;
        val location = QLocation.location;
        val count = QCount.count;
        val branch = QBranch.branch;

        val result = Optional.ofNullable(
            from(locationCount)
                .join(locationCount.location, location)
                .join(locationCount.count, count)
                .join(count.branch, branch)
                .where(
                    branch.erpBranchNum
                        .eq(branchId)
                        .and(location.erpLocationId.eq(locationId))
                        .and(count.erpCountId.eq(countId))
                        .and(count.isDeleted.eq(false))
                )
                .orderBy(locationCount.createdAt.desc())
                .fetchFirst()
        );

        if (result.isEmpty()) {
            log.debug("all counts are purged for the specified branch id");
            return Optional.empty();
        }
        return result;
    }

    @Override
    public List<LocationSummaryDTO> findLocationSummaries(String branchId, UUID countId) {
        val locationCount = QLocationCount.locationCount;
        val location = QLocation.location;
        val count = QCount.count;
        val branch = QBranch.branch;
        val countItem = QCountItem.countItem;

        val q = from(countItem)
            .leftJoin(countItem.locationCount, locationCount)
            .leftJoin(locationCount.location, location)
            .leftJoin(locationCount.count, count)
            .leftJoin(count.branch, branch)
            .where(branch.erpBranchNum.eq(branchId).and(count.id.eq(countId)))
            .groupBy(locationCount.id, location.erpLocationId, locationCount.committed)
            .orderBy(location.erpLocationId.asc())
            .select(
                location.erpLocationId,
                locationCount.committed,
                countItem.id.count().as("totalProducts"),
                countItem.status.when(CountLocationItemStatus.UNCOUNTED).then(0).otherwise(1).sum().as("totalCounted")
            );

        List<LocationSummaryDTO> locationSummaryDTOS = q
            .fetch()
            .stream()
            .map(t -> {
                val locationId = t.get(location.erpLocationId);
                val committed = Boolean.TRUE.equals(t.get(locationCount.committed));
                val totalProducts = Optional.ofNullable(t.get(2, Long.class)).orElse(0L);
                val totalCounted = Optional.ofNullable(t.get(3, Integer.class)).orElse(0);
                return new LocationSummaryDTO(locationId, committed, totalProducts, totalCounted);
            })
            .collect(Collectors.toList());

        List<LocationSummaryDTO> sortedLocationSummaryDTOS = locationSummaryDTOS
            .stream()
            .sorted(Comparator.comparing(LocationSummaryDTO::getId, Collator.getInstance(Locale.US)))
            .collect(Collectors.toList());

        return sortedLocationSummaryDTOS;
    }

    @Override
    public Optional<LocationDTO> findLocation(String branchId, String countId, String locationId) {
        val startTime = System.currentTimeMillis();
        log.debug("entering findLocation(\"{}\", \"{}\", \"{}\")", branchId, countId, locationId);
        val countItem = QCountItem.countItem;
        val locationCount = QLocationCount.locationCount;
        val location = QLocation.location;
        val count = QCount.count;
        val branch = QBranch.branch;

        val result = from(countItem)
            .leftJoin(countItem.locationCount, locationCount)
            .leftJoin(locationCount.location, location)
            .leftJoin(locationCount.count, count)
            .leftJoin(count.branch, branch)
            .where(
                branch.erpBranchNum
                    .eq(branchId)
                    .and(count.erpCountId.eq(countId))
                    .and(location.erpLocationId.eq(locationId).and(count.isDeleted.eq(false)))
            )
            .orderBy(countItem.sequence.asc())
            .select(countItem, locationCount.committed)
            .fetch();

        if (result.isEmpty()) {
            log.debug(
                "Empty result set, returning null. Elapsed time: {}s",
                (System.currentTimeMillis() - startTime) / 1000.0
            );
            return Optional.empty();
        }

        val committed = Boolean.TRUE.equals(result.stream().findFirst().get().get(locationCount.committed));

        val products = result
            .stream()
            .map(t -> t.get(countItem))
            .map(LocationProductDTO::fromEntity)
            .collect(Collectors.toList());
        val totalCounted = products.stream().filter(p -> p.getStatus() != CountLocationItemStatus.UNCOUNTED).count();

        log.debug(
            "Returning from findLocation(). Elapsed time: {}s",
            (System.currentTimeMillis() - startTime) / 1000.0
        );
        return Optional.of(new LocationDTO(locationId, "", products.size(), totalCounted, committed, products));
    }

    public Optional<String> getNextLocationId(String branchId, String countId, String locationId) {
        val branch = QBranch.branch;
        val count = QCount.count;
        val location = QLocation.location;
        val locationCount = QLocationCount.locationCount;

        val result = from(locationCount)
            .leftJoin(locationCount.location, location)
            .leftJoin(locationCount.count, count)
            .leftJoin(count.branch, branch)
            .orderBy(location.erpLocationId.asc())
            .where(
                branch.erpBranchNum
                    .eq(branchId)
                    .and(count.erpCountId.eq(countId))
                    .and(count.isDeleted.eq(false))
                    .and(location.erpLocationId.gt(locationId))
                    .and(locationCount.committed.coalesce(false).eq(false))
            )
            .select(location.erpLocationId)
            .fetchFirst();

        if (result == null || result.isEmpty()) {
            log.debug("Empty result set, returning null");
            return Optional.ofNullable("");
        }

        return Optional.ofNullable(result);
    }

    @Override
    public List<VarianceLocationSummaryDTO> findVarianceLocationSummaries(String branchId, String countId) {
        val locationCount = QLocationCount.locationCount;
        val location = QLocation.location;
        val count = QCount.count;
        val branch = QBranch.branch;
        val countItem = QCountItem.countItem;

        val q = from(countItem)
            .leftJoin(countItem.locationCount, locationCount)
            .leftJoin(locationCount.location, location)
            .leftJoin(locationCount.count, count)
            .leftJoin(count.branch, branch)
            .where(
                branch.erpBranchNum
                    .eq(branchId)
                    .and(count.erpCountId.eq(countId))
                    .and(
                        countItem.varianceStatus
                            .eq(VarianceCountItemStatus.UNCOUNTED)
                            .or(countItem.varianceStatus.eq(VarianceCountItemStatus.STAGED))
                    )
            )
            .groupBy(locationCount.id, location.erpLocationId, locationCount.committed)
            .orderBy(location.erpLocationId.asc())
            .select(
                location.erpLocationId,
                locationCount.committed,
                countItem.id.count().as("totalProducts"),
                countItem.varianceCost.abs().sum(),
                countItem.varianceCost.sum()
            );

        return q
            .fetch()
            .stream()
            .map(t -> {
                val locationId = t.get(location.erpLocationId);
                val totalProducts = Optional.ofNullable(t.get(2, Long.class)).orElse(0L);
                val grossVarianceCost = Optional.of(t.get(3, Double.class)).orElse(0.0);
                val netVarianceCost = Optional.of(t.get(4, Double.class)).orElse(0.0);
                VarianceLocationSummaryDTO varianceLocation = new VarianceLocationSummaryDTO();
                varianceLocation.setId(locationId);
                varianceLocation.setTotalProducts(totalProducts);
                varianceLocation.setGrossVarianceCost(grossVarianceCost);
                varianceLocation.setNetVarianceCost(netVarianceCost);
                return varianceLocation;
            })
            .collect(Collectors.toList());
    }

    @Override
    public Optional<VarianceLocationDTO> findVarianceLocation(String branchId, String countId, String locationId) {
        val startTime = System.currentTimeMillis();
        log.debug("entering findLocation(\"{}\", \"{}\", \"{}\")", branchId, countId, locationId);
        val countItem = QCountItem.countItem;
        val locationCount = QLocationCount.locationCount;
        val location = QLocation.location;
        val count = QCount.count;
        val branch = QBranch.branch;
        val varQty = QVarianceItemQuantity.varianceItemQuantity;

        val result = from(countItem)
            .leftJoin(countItem.locationCount, locationCount)
            .leftJoin(locationCount.location, location)
            .leftJoin(locationCount.count, count)
            .leftJoin(count.branch, branch)
            .where(
                branch.erpBranchNum
                    .eq(branchId)
                    .and(count.erpCountId.eq(countId))
                    .and(location.erpLocationId.eq(locationId))
                    .and(
                        countItem.varianceStatus
                            .eq(VarianceCountItemStatus.UNCOUNTED)
                            .or(countItem.varianceStatus.eq(VarianceCountItemStatus.STAGED))
                    )
            )
            .orderBy(countItem.sequence.asc())
            .select(countItem, locationCount.committed)
            .fetch();

        if (result.isEmpty()) {
            log.debug(
                "Empty result set, returning null. Elapsed time: {}s",
                (System.currentTimeMillis() - startTime) / 1000.0
            );
            return Optional.empty();
        }

        val committed = Boolean.TRUE.equals(result.stream().findFirst().get().get(locationCount.committed));
        val productList = result.stream().collect(Collectors.toList());
        val products = productList
            .stream()
            .map(t -> t.get(countItem))
            .map(VarianceLocationProductDTO::fromEntity)
            .collect(Collectors.toList());
        val totalCounted = products.stream().filter(p -> p.getStatus() != CountLocationItemStatus.UNCOUNTED).count();
        val netVarianceCost = productList
            .stream()
            .map(t -> t.get(countItem))
            .map(VarianceLocationProductDTO::fromEntity)
            .mapToDouble(product -> product.getVarianceCost() == null ? 0 : product.getVarianceCost())
            .reduce(0, (total, cost) -> total + cost);
        val grossVarianceCost = productList
            .stream()
            .map(t -> t.get(countItem))
            .map(VarianceLocationProductDTO::fromEntity)
            .mapToDouble(product -> product.getVarianceCost() == null ? 0 : Math.abs(product.getVarianceCost()))
            .reduce(0, (total, cost) -> total + cost);
        log.debug(
            "Returning from findVarianceLocation(). Elapsed time: {}s",
            (System.currentTimeMillis() - startTime) / 1000.0
        );
        return Optional.of(
            new VarianceLocationDTO(
                locationId,
                "",
                Long.valueOf(products.size()),
                totalCounted,
                netVarianceCost,
                grossVarianceCost,
                committed,
                products
            )
        );
    }

    public Optional<String> getVarianceNextLocationId(String branchId, String countId, String locationId) {
        val countItem = QCountItem.countItem;
        val branch = QBranch.branch;
        val count = QCount.count;
        val location = QLocation.location;
        val locationCount = QLocationCount.locationCount;

        val temp = from(countItem)
            .leftJoin(countItem.locationCount, locationCount)
            .leftJoin(locationCount.location, location)
            .leftJoin(locationCount.count, count)
            .leftJoin(count.branch, branch)
            .orderBy(location.erpLocationId.asc())
            .where(
                branch.erpBranchNum
                    .eq(branchId)
                    .and(count.erpCountId.eq(countId))
                    .and(location.erpLocationId.gt(locationId))
                    .and(
                        countItem.varianceStatus
                            .eq(VarianceCountItemStatus.UNCOUNTED)
                            .or(countItem.varianceStatus.eq(VarianceCountItemStatus.STAGED))
                    )
            )
            .groupBy(location.id)
            .select(location.erpLocationId);

        val result = temp.fetchFirst();

        return Optional.ofNullable(result);
    }

    @Override
    public Integer delete(UUID countId) {
        val countLocations = QLocationCount.locationCount;

        val result = delete(countLocations).where(countLocations.count.id.eq(countId)).execute();

        return Math.toIntExact(result);
    }

    @Override
    public Integer delete(ERPSystemName erpSystemName, Date endDate) {
        val countLocations = QLocationCount.locationCount;
        val count = QCount.count;

        var whereClause = count.createdAt.before(endDate);
        if (erpSystemName != null) {
            whereClause = whereClause.and(count.branch.erpSystem.eq(erpSystemName));
        }

        val result = delete(countLocations).where(countLocations.count.in(from(count).where(whereClause))).execute();

        return Math.toIntExact(result);
    }
}
