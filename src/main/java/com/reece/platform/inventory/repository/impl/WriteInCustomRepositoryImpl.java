package com.reece.platform.inventory.repository.impl;

import com.reece.platform.inventory.model.*;
import com.reece.platform.inventory.repository.WriteInCustomRepository;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.repository.support.PageableExecutionUtils;

public class WriteInCustomRepositoryImpl extends QuerydslRepositorySupport implements WriteInCustomRepository {

    public WriteInCustomRepositoryImpl() {
        super(WriteIn.class);
    }

    @Override
    public Page<WriteIn> findAllWriteIns(String branchId, String countId, Pageable pageRequest) {
        val writeIn = QWriteIn.writeIn;
        val count = QCount.count;
        val branch = QBranch.branch;

        val query = from(writeIn)
            .join(writeIn.countId, count)
            .join(count.branch, branch)
            .where(branch.erpBranchNum.eq(branchId).and(count.erpCountId.eq(countId)));

        val totalResults = query.fetchCount();

        val paginated = getQuerydsl().applyPagination(pageRequest, query);

        return PageableExecutionUtils.getPage(paginated.fetch(), pageRequest, () -> totalResults);
    }

    @Override
    public Integer deleteWriteIns(UUID countId) {
        val writeIn = QWriteIn.writeIn;
        val locationCount = QLocationCount.locationCount;

        val query = delete(writeIn)
            .where(
                writeIn.locationCount.in(
                    from(locationCount).where(locationCount.count.id.eq(countId)).select(locationCount.id)
                )
            );
        return Math.toIntExact(query.execute());
    }

    @Override
    public Integer deleteWriteIns(ERPSystemName erpSystemName, Date endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        val writeIn = QWriteIn.writeIn;
        val locationCount = QLocationCount.locationCount;
        val count = QCount.count;

        var whereClause = locationCount.count.createdAt.before(endDate);
        if (erpSystemName != null) {
            whereClause =
                whereClause.and(locationCount.count.in(from(count).where(count.branch.erpSystem.eq(erpSystemName))));
        }

        val query = delete(writeIn)
            .where(writeIn.locationCount.in(from(locationCount).where(whereClause).select(locationCount.id)));
        return Math.toIntExact(query.execute());
    }
}
