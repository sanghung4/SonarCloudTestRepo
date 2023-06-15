package com.reece.platform.inventory.repository;

import com.reece.platform.inventory.model.CountItemQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CountItemQuantityRepository extends JpaRepository<CountItemQuantity, UUID>, CountItemQuantityCustomRepository {
}
