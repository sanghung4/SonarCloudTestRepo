package com.reece.platform.inventory.repository.impl;

import com.querydsl.core.types.Ops;
import com.querydsl.core.types.dsl.Expressions;
import com.reece.platform.inventory.dto.CountStatusDTO;
import com.reece.platform.inventory.model.*;
import com.reece.platform.inventory.repository.CountCustomRepository;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.val;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class CountCustomRepositoryImpl extends QuerydslRepositorySupport implements CountCustomRepository {

    public CountCustomRepositoryImpl() {
        super(Count.class);
    }

    @PersistenceContext
    private EntityManager entityManager;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Optional<Count> findCount(String branchId, String countId) {
        val count = QCount.count;
        val branch = QBranch.branch;

        return Optional.ofNullable(
            from(count)
                .join(count.branch, branch)
                .where(
                    branch.erpBranchNum.eq(branchId).and(count.erpCountId.eq(countId)).and(count.isDeleted.eq(false))
                )
                .orderBy(count.createdAt.desc())
                .fetchFirst()
        );
    }

    @Override
    public List<CountStatusDTO> findCountsByTimeRange(Date startDate, Date endDate) {
        val count = QCount.count;
        val locationCount = QLocationCount.locationCount;
        val countItem = QCountItem.countItem;
        val branch = QBranch.branch;

        val result = from(countItem)
            .join(countItem.locationCount, locationCount)
            .rightJoin(locationCount.count, count)
            .join(count.branch, branch)
            .where(count.createdAt.goe(startDate).and(count.createdAt.lt(endDate)))
            .groupBy(count.id, branch.id)
            .orderBy(count.createdAt.asc())
            .select(
                count.id,
                branch.erpBranchNum,
                count.erpCountId,
                branch.name,
                count.status,
                count.errorMessage,
                count.createdAt,
                countItem.id.count().as("totalProducts")
            )
            .fetch();

        if (result.isEmpty()) {
            return Collections.emptyList();
        }

        return result
            .stream()
            .map(t -> {
                Long totalProducts = t.get(7, Long.class);
                return new CountStatusDTO(
                    t.get(count.id),
                    t.get(branch.erpBranchNum),
                    t.get(count.erpCountId),
                    t.get(branch.name),
                    t.get(count.status),
                    t.get(count.errorMessage),
                    t.get(count.createdAt),
                    Math.toIntExact(totalProducts)
                );
            })
            .collect(Collectors.toList());
    }

    @Override
    public List<Count> findMincronCountsBetweenDates(Date startDate, Date endDate) {
        val count = QCount.count;
        val branch = QBranch.branch;

        //TODO: Find a better way to do regex with DSL Query
        String queryString = String.format(
            "select * from public.pi_counts where branch_id in (select id from pi_branches where (erp_system ~ '^\\d{3}$' OR erp_system = 'MINCRON')) AND is_deleted=false AND created_at > '%s' and created_at < '%s'",
            dateFormat.format(startDate),
            dateFormat.format(endDate)
        );

        return entityManager.createNativeQuery(queryString, Count.class).getResultList();
    }

    @Override
    public Integer getNumberOfProductsForCount(UUID uuid) {
        val locationCount = QLocationCount.locationCount;
        val countItem = QCountItem.countItem;

        val result = from(countItem)
            .join(countItem.locationCount, locationCount)
            .where(locationCount.count.id.eq(uuid))
            .select(countItem.id.count())
            .fetchFirst();

        return result.intValue();
    }

    @Override
    public List<Count> findCountsByIds(List<String> countIds) {
        val count = QCount.count;

        return from(count).where(count.erpCountId.in(countIds)).fetch();
    }

    @Override
    public Integer delete(ERPSystemName erpSystemName, Date endDate) {
        val count = QCount.count;

        var whereClause = count.createdAt.before(endDate);
        if (erpSystemName != null) {
            whereClause = whereClause.and(count.in(from(count).where(count.branch.erpSystem.eq(erpSystemName))));
        }

        val result = delete(count).where(whereClause).execute();

        return Math.toIntExact(result);
    }

    @Override
    public List<UUID> findCountsUUID(ERPSystemName erpSystemName, String countId, String branchId) {
        val countItem = QCount.count;
        val branchItem = QBranch.branch;

        var whereClause = branchItem.erpSystem
            .eq(erpSystemName)
            .and(countItem.erpCountId.eq(countId))
            .and(branchItem.erpBranchNum.eq(branchId).and(countItem.isDeleted.eq(false)));

        val result = from(countItem)
            .innerJoin(countItem.branch, branchItem)
            .fetchJoin()
            .where(whereClause)
            .fetch()
            .stream()
            .map(Count::getId)
            .collect(Collectors.toList());

        return result;
    }

    @Override
    public List<Count> findCountsByErpSystemAndEndDate(ERPSystemName erpSystemName, Date endDate) {
        val count = QCount.count;

        var whereClause = count.createdAt.before(endDate);
        if (erpSystemName != null) {
            whereClause = whereClause.and(count.in(from(count).where(count.branch.erpSystem.eq(erpSystemName))));
        }

        return from(count).where(whereClause).fetch();
    }
}
