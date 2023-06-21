package com.reece.platform.accounts.model.repository;

import com.reece.platform.accounts.model.entity.ErpsUsers;
import com.reece.platform.accounts.model.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErpsUsersDAO extends JpaRepository<ErpsUsers, UUID> {
    Optional<ErpsUsers> findByUserId(UUID userId);
    Optional<ErpsUsers> findFirstByUserIdOrderByLastModifiedDateDesc(UUID userId);
}
