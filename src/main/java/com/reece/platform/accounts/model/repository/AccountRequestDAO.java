package com.reece.platform.accounts.model.repository;

import com.reece.platform.accounts.model.entity.AccountRequest;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRequestDAO extends JpaRepository<AccountRequest, UUID> {
    List<AccountRequest> findAllByAccountId(UUID accountId);

    @Query(
        "SELECT a FROM AccountRequest a WHERE a.accountId IN (:ids) AND a.isCompleted = (:isCompleted) AND a.isEmployee = (:isEmployee) AND a.rejectionReason = NULL"
    )
    List<AccountRequest> findAllByAccountIdsAndIsCompletedAndIsEmployee(
        @Param("ids") Set<UUID> accountIds,
        @Param("isCompleted") boolean isCompleted,
        @Param("isEmployee") boolean isEmployee
    );

    List<AccountRequest> findAllByAccountIdAndIsCompletedAndIsEmployeeAndRejectionReasonNull(
        UUID accountId,
        boolean isCompleted,
        boolean isEmployee
    );
    List<AccountRequest> findAllByRejectionReasonNotNullAndAccountId(UUID accountId);
    Optional<AccountRequest> findByEmailAndRejectionReasonIsNull(String email);

    @Query(
        value = "SELECT * FROM account_requests WHERE email = :email ORDER BY created_at DESC LIMIT 1",
        nativeQuery = true
    )
    AccountRequest findMostRecentRequestByEmail(@Param("email") String email);

    Optional<AccountRequest> findByVerificationToken(UUID token);
    void deleteByAccountId(UUID accountId);
}
