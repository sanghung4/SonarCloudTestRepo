package com.reece.platform.products.model.repository;

import com.reece.platform.products.model.entity.WillCall;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WillCallDAO extends JpaRepository<WillCall, UUID> {}
