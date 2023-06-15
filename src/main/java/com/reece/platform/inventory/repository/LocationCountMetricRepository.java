package com.reece.platform.inventory.repository;

import com.reece.platform.inventory.model.Branch;
import com.reece.platform.inventory.model.Location;
import com.reece.platform.inventory.model.LocationCountMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LocationCountMetricRepository extends JpaRepository<LocationCountMetric, UUID>, QuerydslPredicateExecutor<LocationCountMetric> {
}
