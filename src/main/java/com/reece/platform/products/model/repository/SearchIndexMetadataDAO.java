package com.reece.platform.products.model.repository;

import com.reece.platform.products.model.entity.SearchIndexMetadata;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchIndexMetadataDAO extends JpaRepository<SearchIndexMetadata, UUID> {}
