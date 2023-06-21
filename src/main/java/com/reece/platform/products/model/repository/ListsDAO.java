package com.reece.platform.products.model.repository;

import com.reece.platform.products.model.entity.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ListsDAO extends JpaRepository<List, UUID> {
    @Query("select distinct list FROM List AS list left JOIN FETCH list.listLineItems WHERE list.billToAccountId =?1")
    java.util.List<List> findAllByBillToAccountId(UUID accountId);

    @Query(
        value = "select * from lists l where (l.name = :name or l.name like CONCAT(:name, ' (%)')) and l.billto_account_id = :billToAccountId",
        nativeQuery = true
    )
    java.util.List<List> findAllByMatchingNameAndBillToAccountId(String name, UUID billToAccountId);

    @Query(
        value = "select count(*) from lists l where l.name = :name  AND l.billto_account_id = :billToAccountId",
        nativeQuery = true
    )
    Integer findCountByMatchingNameAndBillToAccountId(String name, UUID billToAccountId);

    @Query(
        value = "SELECT l.* FROM Lists l inner JOIN List_line_items lt on " +
        " lt.list_id=l.id where l.billto_account_id =:billToAccountId AND " +
        " lt.erp_part_number=:partNumber ",
        nativeQuery = true
    )
    java.util.List<List> findListsByBillToAccountIdAndPartNumber(
        @Param("billToAccountId") UUID billToAccountId,
        @Param("partNumber") String partNumber
    );
}
