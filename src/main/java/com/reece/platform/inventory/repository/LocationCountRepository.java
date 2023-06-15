package com.reece.platform.inventory.repository;

import com.reece.platform.inventory.model.Count;
import com.reece.platform.inventory.model.Location;
import com.reece.platform.inventory.model.LocationCount;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationCountRepository
    extends
        JpaRepository<LocationCount, UUID>, QuerydslPredicateExecutor<LocationCount>, LocationCountCustomRepository {
    Optional<LocationCount> findByLocationAndCount(Location location, Count count);
    List<LocationCount> findByCount(Count count);
}
