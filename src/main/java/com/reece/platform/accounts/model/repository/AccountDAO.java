package com.reece.platform.accounts.model.repository;

import com.reece.platform.accounts.model.entity.Account;
import com.reece.platform.accounts.model.enums.ErpEnum;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDAO extends JpaRepository<Account, UUID> {
    List<Account> findAllByParentAccountId(UUID parentAccountId);
    List<Account> findAllByParentAccountIdIn(List<UUID> parentAccountIds);
    List<Account> findAllByParentAccountIdNotNull();
    Optional<Account> findByErpAccountIdAndParentAccountId(String erpAccountId, UUID parentAccountId);
    Optional<Account> findByErpAccountIdAndErp(String erpAccountId, ErpEnum erp);
    List<Account> findAllByErpAccountIdAndErp(String erpAccountId, ErpEnum erp);
    List<Account> findAllByErpAccountIdAndParentAccountId(String erpAccountId, UUID parentAccountId);

    @Override
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    void deleteById(UUID uuid);

    @Query(value = " Select * FROM accounts a where a.erp_account_id =:accountId AND a.erp=:erp", nativeQuery = true)
    List<Account> findByErpAccountIDAndErp(String accountId, String erp);
}
