package com.reece.platform.products.model.repository;

import com.reece.platform.products.model.entity.QuoteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteStatusDAO extends JpaRepository<QuoteStatus, String> {}
