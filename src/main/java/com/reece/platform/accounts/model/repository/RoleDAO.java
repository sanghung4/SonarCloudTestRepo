package com.reece.platform.accounts.model.repository;

import com.reece.platform.accounts.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleDAO extends JpaRepository<Role, UUID> {
    Role findByName(String roleName);
}
