package com.reece.platform.products.model.repository;

import com.reece.platform.products.model.ErpEnum;
import com.reece.platform.products.model.entity.ListLineItem;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ListLineItemsDAO extends JpaRepository<ListLineItem, UUID> {
    @Modifying
    @Query(value = "delete from List_line_items lt where lt.list_id=:listId", nativeQuery = true)
    void deleteAllByListId(@Param("listId") UUID listId);

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from List_line_items lt where lt.id=:uuid", nativeQuery = true)
    void deleteById(@Param("uuid") UUID uuid);

    @Query(
        value = "SELECT lt.* FROM Lists l inner JOIN List_line_items lt on " +
        " lt.list_id=l.id where l.billto_account_id =:billToAccountId AND " +
        " lt.erp_part_number=:partNumber ",
        nativeQuery = true
    )
    List<ListLineItem> findAllAddedItemListsByBillToAccountIdAndPartNumber(
        @Param("billToAccountId") UUID billToAccountId,
        @Param("partNumber") String partNumber
    );

    @Query(
        value = """
                SELECT DISTINCT lli.erp_part_number,array_to_string(array_agg(lli.list_id), ',')
                FROM list_line_items lli
                    LEFT JOIN lists l ON l.id = lli.list_id
                    LEFT JOIN accounts a ON a.parent_account_id = l.billto_account_id
                WHERE a.erp_account_id=:erpAccountId
                AND a.erp = :#{#erp?.name()}
                AND lli.erp_part_number IN (:erpPartNumbers) GROUP BY lli.erp_part_number """,
        nativeQuery = true
    )
    List<String[]> findAllListIdsByPartNumbersAndErpAccountId(
        @Param("erpPartNumbers") Collection<String> erpPartNumbers,
        String erpAccountId,
        ErpEnum erp
    );

    @Query(
        value = """
                SELECT DISTINCT lli.erp_part_number,array_to_string(array_agg(lli.list_id), ',')
                FROM list_line_items lli
                    LEFT JOIN lists l ON l.id = lli.list_id
                    LEFT JOIN accounts a ON a.id = l.billto_account_id
                WHERE a.erp_account_id=:erpAccountId
                AND a.erp = :#{#erp?.name()}
                AND lli.erp_part_number IN (:erpPartNumbers) GROUP BY lli.erp_part_number """,
        nativeQuery = true
    )
    List<String[]> findAllListIdsByProductAndBillToAccount(
        @Param("erpPartNumbers") Collection<String> erpPartNumbers,
        String erpAccountId,
        ErpEnum erp
    );
}
