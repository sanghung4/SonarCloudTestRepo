package com.reece.platform.inventory.repository.impl;

import com.reece.platform.inventory.model.*;
import com.reece.platform.inventory.repository.CountItemCustomRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.val;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class CountItemCustomRepositoryImpl extends QuerydslRepositorySupport implements CountItemCustomRepository {

    public CountItemCustomRepositoryImpl() {
        super(CountItem.class);
    }

    @Override
    public Optional<CountItem> findCountItem(String branchId, String countId, String locationId, String tagNum) {
        val countItem = QCountItem.countItem;
        val locationCount = QLocationCount.locationCount;
        val location = QLocation.location;
        val branch = QBranch.branch;
        val count = QCount.count;

        return Optional.ofNullable(
            from(countItem)
                .join(countItem.locationCount, locationCount)
                .join(locationCount.location, location)
                .join(locationCount.count, count)
                .join(count.branch, branch)
                .where(
                    branch.erpBranchNum
                        .eq(branchId)
                        .and(location.erpLocationId.eq(locationId))
                        .and(count.erpCountId.eq(countId))
                        .and(countItem.tagNum.eq(tagNum))
                )
                .orderBy(countItem.createdAt.desc())
                .fetchFirst()
        );
    }

    @Override
    public List<CountItem> findStagedCountItems(String branchId, String countId, String locationId) {
        val countItem = QCountItem.countItem;
        val locationCount = QLocationCount.locationCount;
        val location = QLocation.location;
        val branch = QBranch.branch;
        val count = QCount.count;

        return from(countItem)
            .join(countItem.locationCount, locationCount)
            .join(locationCount.location, location)
            .join(locationCount.count, count)
            .join(count.branch, branch)
            .where(
                branch.erpBranchNum
                    .eq(branchId)
                    .and(count.erpCountId.eq(countId))
                    .and(location.erpLocationId.eq(locationId))
                    .and(countItem.status.eq(CountLocationItemStatus.STAGED))
            )
            .fetch();
    }

    @Override
    public Optional<CountItem> findVarianceCountItem(
        String branchId,
        String countId,
        String locationId,
        String tagNum
    ) {
        val countItem = QCountItem.countItem;
        val locationCount = QLocationCount.locationCount;
        val location = QLocation.location;
        val branch = QBranch.branch;
        val count = QCount.count;

        return Optional.ofNullable(
            from(countItem)
                .join(countItem.locationCount, locationCount)
                .join(locationCount.location, location)
                .join(locationCount.count, count)
                .join(count.branch, branch)
                .where(
                    branch.erpBranchNum
                        .eq(branchId)
                        .and(location.erpLocationId.eq(locationId))
                        .and(count.erpCountId.eq(countId))
                        .and(countItem.tagNum.eq(tagNum))
                        .and(
                            countItem.varianceStatus
                                .eq(VarianceCountItemStatus.UNCOUNTED)
                                .or(countItem.varianceStatus.eq(VarianceCountItemStatus.STAGED))
                        )
                )
                .orderBy(countItem.createdAt.desc())
                .fetchFirst()
        );
    }

    @Override
    public List<CountItem> findVarianceStagedCountItems(String branchId, String countId, String locationId) {
        val countItem = QCountItem.countItem;
        val locationCount = QLocationCount.locationCount;
        val location = QLocation.location;
        val branch = QBranch.branch;
        val count = QCount.count;

        return from(countItem)
            .join(countItem.locationCount, locationCount)
            .join(locationCount.location, location)
            .join(locationCount.count, count)
            .join(count.branch, branch)
            .where(
                branch.erpBranchNum
                    .eq(branchId)
                    .and(count.erpCountId.eq(countId))
                    .and(location.erpLocationId.eq(locationId))
                    .and(countItem.varianceStatus.eq(VarianceCountItemStatus.STAGED))
            )
            .fetch();
    }

    @Override
    public Integer delete(UUID countId) {
        val countItem = QCountItem.countItem;
        val locationCount = QLocationCount.locationCount;

        val result = delete(countItem)
            .where(
                countItem.locationCount.id.in(
                    from(locationCount).where(locationCount.count.id.eq(countId)).select(locationCount.id)
                )
            )
            .execute();

        return Math.toIntExact(result);
    }

    @Override
    public Integer delete(ERPSystemName erpSystemName, Date endDate) {
        val countItem = QCountItem.countItem;
        val locationCount = QLocationCount.locationCount;
        val count = QCount.count;

        var whereClause = locationCount.count.createdAt.before(endDate);
        if (erpSystemName != null) {
            whereClause =
                whereClause.and(locationCount.count.in(from(count).where(count.branch.erpSystem.eq(erpSystemName))));
        }

        val result = delete(countItem)
            .where(countItem.locationCount.id.in(from(locationCount).where(whereClause).select(locationCount.id)))
            .execute();

        return Math.toIntExact(result);
    }
}
