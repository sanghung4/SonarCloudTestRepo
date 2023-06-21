package com.reece.platform.accounts.model.repository;

import com.reece.platform.accounts.model.entity.UsersBillToAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersBillToAccountsDAO extends JpaRepository<UsersBillToAccounts, UUID> {
    List<UsersBillToAccounts> findAllByAccountId(UUID accountId);
    List<UsersBillToAccounts> findAllByUserId(UUID userId);
    Optional<UsersBillToAccounts> findByUserIdAndAccountId(UUID userId, UUID accountId);
}
