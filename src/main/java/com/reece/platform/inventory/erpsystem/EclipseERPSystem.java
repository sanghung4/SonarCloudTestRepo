package com.reece.platform.inventory.erpsystem;

import com.reece.platform.inventory.dto.CountDTO;
import com.reece.platform.inventory.dto.LocationDTO;
import com.reece.platform.inventory.dto.LocationProductDTO;
import com.reece.platform.inventory.dto.internal.PostCountDTO;
import com.reece.platform.inventory.exception.InvalidCountForBranchException;
import com.reece.platform.inventory.external.eclipse.EclipsePostCountDTO;
import com.reece.platform.inventory.external.eclipse.EclipseProductDTO;
import com.reece.platform.inventory.external.eclipse.EclipseService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EclipseERPSystem implements ERPSystem {

    private final EclipseService eclipseService;

    @Override
    public CountDTO validateCount(String branchId, String countId) {
        var eclipseCount = eclipseService.validateBatch(branchId, countId);

        if (!branchId.equals(eclipseCount.getBranchId())) {
            throw new InvalidCountForBranchException();
        }
        return CountDTO.fromEclipseBatchDTO(eclipseCount);
    }

    @Override
    public List<LocationDTO> loadAllLocationItems(String branchId, String countId) {
        var count = eclipseService.getCount(countId);

        HashMap<String, List<EclipseProductDTO>> locationsMap = new HashMap<>();

        for (var product : count.getProducts()) {
            if (!locationsMap.containsKey(product.getLocationId())) {
                locationsMap.put(product.getLocationId(), new ArrayList<>());
            }

            locationsMap.get(product.getLocationId()).add(product);
        }

        var locations = new ArrayList<LocationDTO>();

        for (var products : locationsMap.values()) {
            var locationId = products.get(0).getLocationId();
            var location = LocationDTO.fromEclipseLocationItemDTOs(locationId, products);
            locations.add(location);
        }

        return locations;
    }

    @Override
    public List<LocationProductDTO> loadLocationItems(String branchId, String countId, String locationId) {
        val eclipseLocationItems = eclipseService.getLocationItems(countId, locationId);
        return eclipseLocationItems
            .stream()
            .map(LocationProductDTO::fromEclipseLocationItemDTO)
            .collect(Collectors.toList());
    }

    @Override
    public void updateCount(String _branchId, String countId, String locationId, List<PostCountDTO> itemsToUpdate) {
        List<EclipsePostCountDTO> listOfEclipseProducts = itemsToUpdate
            .stream()
            .map(EclipsePostCountDTO::new)
            .collect(Collectors.toList());

        eclipseService.updateCount(countId, locationId, listOfEclipseProducts);
    }

    @Override
    public void addToCount(String _branchId, String countId, String locationId, String prodNum, int quantity) {
        eclipseService.addToCount(countId, locationId, prodNum, quantity);
    }
}
