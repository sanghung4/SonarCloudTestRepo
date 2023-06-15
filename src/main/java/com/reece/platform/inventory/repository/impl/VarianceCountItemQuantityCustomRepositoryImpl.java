package com.reece.platform.inventory.repository.impl;

import com.reece.platform.inventory.model.*;
import com.reece.platform.inventory.repository.VarianceCountItemQuantityCustomRepository;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import lombok.val;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class VarianceCountItemQuantityCustomRepositoryImpl
    extends QuerydslRepositorySupport
    implements VarianceCountItemQuantityCustomRepository {

    public VarianceCountItemQuantityCustomRepositoryImpl() {
        super(VarianceItemQuantity.class);
    }

    @Override
    public Optional<VarianceItemQuantity> findMostRecentQuantity(CountItem countItem) {
        val varianceItemQuantity = QVarianceItemQuantity.varianceItemQuantity;

        val result = from(varianceItemQuantity)
            .where(varianceItemQuantity.countItem.eq(countItem))
            .orderBy(varianceItemQuantity.updatedAt.desc())
            .fetchFirst();

        return Optional.ofNullable(result);
    }

    @Override
    public Integer delete(UUID countId) {
        val varianceItemQuantity = QVarianceItemQuantity.varianceItemQuantity;
        val countItem = QCountItem.countItem;
        val locationCount = QLocationCount.locationCount;

        val result = delete(varianceItemQuantity)
            .where(
                varianceItemQuantity.countItem.in(
                    from(countItem)
                        .where(
                            countItem.locationCount.id.in(
                                from(locationCount).where(locationCount.count.id.eq(countId)).select(locationCount.id)
                            )
                        )
                )
            )
            .execute();

        return Math.toIntExact(result);
    }

    @Override
    public Integer delete(ERPSystemName erpSystemName, Date endDate) {
        val varianceItemQuantity = QVarianceItemQuantity.varianceItemQuantity;
        val countItem = QCountItem.countItem;
        val locationCount = QLocationCount.locationCount;
        val count = QCount.count;

        var whereClause = locationCount.count.createdAt.before(endDate);
        if (erpSystemName != null) {
            whereClause =
                whereClause.and(locationCount.count.in(from(count).where(count.branch.erpSystem.eq(erpSystemName))));
        }

        val result = delete(varianceItemQuantity)
            .where(
                varianceItemQuantity.countItem.in(
                    from(countItem)
                        .where(
                            countItem.locationCount.id.in(
                                from(locationCount).where(whereClause).select(locationCount.id)
                            )
                        )
                )
            )
            .execute();

        return Math.toIntExact(result);
    }
}
