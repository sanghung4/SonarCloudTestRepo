package com.reece.platform.accounts.model.repository;

import com.reece.platform.accounts.model.entity.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FeaturesDAO extends JpaRepository<Feature, UUID> {
    Feature findByName(String featureName);
}