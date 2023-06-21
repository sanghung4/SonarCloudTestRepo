package com.reece.platform.accounts.model.repository;
import com.reece.platform.accounts.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDAO extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    List<User> findAllByEmailIn(List<String> emails);
    List<User> findAllByApproverId(UUID approverId);
    List<User> findByBillToAccounts_Id(UUID id);
    @Query(value="SELECT * FROM users u  INNER JOIN users_billto_accounts aba " +
            " ON u.id = aba.user_id  INNER JOIN roles r  ON r.id = u.role_id " +
            " INNER JOIN account_requests ar  ON ar.email = u.email WHERE " +
            " aba.account_id =:accountId and r.name =:roleName" +
            " ORDER BY ar.created_at ASC LIMIT 1", nativeQuery = true)
    Optional<User> findUserByAccountIdAndRoleName(@Param("accountId") UUID accountId, @Param("roleName") String roleName);

}
