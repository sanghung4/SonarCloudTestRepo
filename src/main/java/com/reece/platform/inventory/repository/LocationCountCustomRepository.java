package com.reece.platform.inventory.repository;

import com.reece.platform.inventory.dto.LocationDTO;
import com.reece.platform.inventory.dto.LocationSummaryDTO;
import com.reece.platform.inventory.dto.variance.VarianceLocationDTO;
import com.reece.platform.inventory.dto.variance.VarianceLocationSummaryDTO;
import com.reece.platform.inventory.model.ERPSystemName;
import com.reece.platform.inventory.model.LocationCount;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LocationCountCustomRepository {
    Optional<LocationCount> findLocationCount(String branchId, String countId, String locationId);

    List<LocationSummaryDTO> findLocationSummaries(String branchId, UUID countId);

    Optional<LocationDTO> findLocation(String branchId, String countId, String locationId);

    Optional<String> getNextLocationId(String branchId, String countId, String locationId);

    List<VarianceLocationSummaryDTO> findVarianceLocationSummaries(String branchId, String countId);

    Optional<VarianceLocationDTO> findVarianceLocation(String branchId, String countId, String locationId);

    Optional<String> getVarianceNextLocationId(String branchId, String countId, String locationId);

    Integer delete(UUID countId);

    Integer delete(ERPSystemName erpSystemName, Date endDate);
}
