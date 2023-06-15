package com.reece.platform.inventory.repository.impl;

import com.reece.platform.inventory.model.*;
import com.reece.platform.inventory.repository.CountItemQuantityCustomRepository;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import lombok.val;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class CountItemQuantityCustomRepositoryImpl
    extends QuerydslRepositorySupport
    implements CountItemQuantityCustomRepository {

    public CountItemQuantityCustomRepositoryImpl() {
        super(CountItemQuantity.class);
    }

    @Override
    public Optional<CountItemQuantity> findMostRecentQuantity(CountItem countItem) {
        val countItemQuantity = QCountItemQuantity.countItemQuantity;

        val result = from(countItemQuantity)
            .where(countItemQuantity.countItem.eq(countItem))
            .orderBy(countItemQuantity.updatedAt.desc())
            .fetchFirst();

        return Optional.ofNullable(result);
    }

    @Override
    public Integer delete(UUID countId) {
        val countItemQuantity = QCountItemQuantity.countItemQuantity;
        val countItem = QCountItem.countItem;
        val locationCount = QLocationCount.locationCount;

        val result = delete(countItemQuantity)
            .where(
                countItemQuantity.countItem.in(
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
        val countItemQuantity = QCountItemQuantity.countItemQuantity;
        val countItem = QCountItem.countItem;
        val locationCount = QLocationCount.locationCount;
        val count = QCount.count;

        var whereClause = locationCount.count.createdAt.before(endDate);
        if (erpSystemName != null) {
            whereClause =
                whereClause.and(locationCount.count.in(from(count).where(count.branch.erpSystem.eq(erpSystemName))));
        }

        val result = delete(countItemQuantity)
            .where(
                countItemQuantity.countItem.in(
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
