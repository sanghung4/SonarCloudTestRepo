package com.reece.platform.accounts.model.repository;

import com.reece.platform.accounts.model.entity.InvitedUser;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitedUserDAO extends JpaRepository<InvitedUser, UUID> {
    Optional<InvitedUser> findByEmail(String email);
    void deleteAllByBillToAccountId(UUID accountId);
    void deleteAllByApproverId(UUID userId);
}
