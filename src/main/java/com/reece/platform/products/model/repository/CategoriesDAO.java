package com.reece.platform.products.model.repository;

import com.reece.platform.products.model.entity.Categories;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Deprecated(since = "0.94", forRemoval = true)
@Repository
public interface CategoriesDAO extends JpaRepository<Categories, String> {
    @Deprecated
    Optional<Categories> findByErp(String erp);
}
