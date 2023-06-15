package com.reece.platform.inventory.service;

import com.reece.platform.inventory.dto.EclipseLoadCountDto;
import com.reece.platform.inventory.dto.LocationProductDTO;
import com.reece.platform.inventory.erpsystem.ERPSystemFactory;
import com.reece.platform.inventory.model.*;
import com.reece.platform.inventory.repository.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoadCountService {

    private final BranchRepository branchRepository;
    private final CountRepository countRepository;
    private final CountLocationItemRepository countLocationItemRepository;
    private final LocationCountRepository locationCountRepository;
    private final LocationRepository locationRepository;

    private final ERPSystemFactory erpSystemFactory;
    private static final String NO_LOCATION = "_NOLOC";

    public Count findOrCreateCount(String branchId, String countId, boolean formatCountId) {
        return countRepository
            .findCount(branchId, countId)
            .orElseGet(() -> {
                var branch = getOrCreateBranch(branchId);
                var newCount = new Count();
                newCount.setBranch(branch);
                if (formatCountId) {
                    newCount.setErpCountId(countId.replaceFirst("^0+(?!$)", ""));
                } else {
                    newCount.setErpCountId(countId);
                }
                newCount.setStatus(CountStatus.NOT_LOADED);
                countRepository.save(newCount);

                return newCount;
            });
    }

    @Async("taskExecutor")
    public void doAsyncLoad(Count count) {
        doLoad(count);
    }

    public void doLoad(Count count) {
        StopWatch sw1 = new StopWatch();
        StopWatch sw2 = new StopWatch();
        sw1.start();
        val start = System.currentTimeMillis();
        log.debug("Entering doLoad({})", count.getId());

        try {
            // 1. Set Count status to In Progress
            count.setStatus(CountStatus.IN_PROGRESS);
            count = countRepository.save(count);

            // 2: load branch and branch metadata from ERP
            verifyCountAndBranch(count, sw2);

            // 3: fetch list of locations (including products) for count from ERP
            loadLocations(count, sw2);

            // 4: update count status
            completeLoad(count);
        } catch (Throwable throwable) {
            log.error("Error caught in background process.", throwable);

            count.setStatus(CountStatus.ERROR);
            count.setErrorMessage(throwable.getMessage());
            countRepository.save(count);
        } finally {
            log.debug("Exiting doLoad({}), {}ms", count.getId(), System.currentTimeMillis() - start);
            sw1.stop();
            log.warn(
                "Total execution time: " +
                sw1.getTotalTimeMillis() +
                " | " +
                "API execution time: " +
                sw2.getTotalTimeMillis()
            );
        }
    }

    /**
     * Load a single count from list of products in file (used for eclipse batch load)
     * @param countId
     * @param products
     */
    public void loadCountFromProductList(String countId, List<EclipseLoadCountDto> products) {
        var branchId = products.get(0).getBranch();

        // Find or create the count if it does not exist
        var count = findOrCreateCount(branchId, countId, true);
        var branch = count.getBranch();

        // Set to in progress so other process does not begin loading count
        count.setStatus(CountStatus.IN_PROGRESS);

        // Get any existing locactions and location counts.
        var locations = locationRepository.findByBranch(branch).orElse(new ArrayList<>());
        var locationCounts = locationCountRepository.findByCount(count);
        var countItems = new ArrayList<CountItem>();

        // Iterate over all products in list
        for (var product : products) {
            // Find existing product
            var location = locations
                .stream()
                .filter(x -> x.getErpLocationId().equals(product.getLocation()))
                .findAny()
                .orElse(null);

            // Create if location it does not exist
            if (location == null) {
                if ((product.getLocation() == null || product.getLocation().isBlank())) {
                    continue;
                }
                var newLocation = new Location();
                newLocation.setBranch(branch);
                newLocation.setErpLocationId(product.getLocation());
                locationRepository.save(newLocation);
                locations.add(newLocation);
                location = newLocation;
            }

            // Find existing locationCount record
            var locationCount = locationCounts
                .stream()
                .filter(l -> l.getLocation().getErpLocationId().equals(product.getLocation()))
                .findAny()
                .orElse(null);

            // Create new locationCount record if not exists
            if (locationCount == null) {
                var newLocationCount = new LocationCount();
                newLocationCount.setCount(count);
                newLocationCount.setLocation(location);
                locationCountRepository.save(newLocationCount);
                locationCounts.add(newLocationCount);
                locationCount = newLocationCount;
            }

            // Create the product count item record and add to list
            var item = new CountItem(product);
            item.setLocationCount(locationCount);
            countItems.add(item);
        }

        // save all new count items
        countLocationItemRepository.saveAll(countItems);

        // wrap up completing count status
        count.setStatus(CountStatus.COMPLETE);
        count.setErrorMessage(null);
        count.setAllLocationsFetched(true);
        countRepository.save(count);
    }

    /**
     * Private Methods
     */

    private Branch getOrCreateBranch(String branchId) {
        var erpSystem = ERPSystemName.fromBranchNumber(branchId);
        return branchRepository
            .findByErpSystemAndErpBranchNum(erpSystem, branchId)
            .orElseGet(() -> {
                var b = new Branch();
                b.setErpSystem(erpSystem);
                b.setErpBranchNum(branchId);
                return branchRepository.save(b);
            });
    }

    private void verifyCountAndBranch(Count count, StopWatch stopWatch) {
        log.debug("Entering verifyCountAndBranch({})", count.getId());

        val start = System.currentTimeMillis();
        val branch = count.getBranch();
        val branchId = branch.getErpBranchNum();
        val erpSystem = erpSystemFactory.getErpSystem(branchId);

        stopWatch.start();
        val erpCount = erpSystem.validateCount(branchId, count.getErpCountId());
        stopWatch.stop();

        branch.setName(erpCount.getBranchName());
        branchRepository.save(branch);

        log.debug("Exiting verifyCountAndBranch({}), {}ms", count.getId(), System.currentTimeMillis() - start);
    }

    private void loadLocations(Count count, StopWatch stopWatch) {
        log.info("Entering loadLocations({})", count.getId());

        val start = System.currentTimeMillis();
        val branch = count.getBranch();
        val branchId = branch.getErpBranchNum();

        stopWatch.start();

        val erpSystem = erpSystemFactory.getErpSystem(branchId);
        val erpSystemName = ERPSystemName.fromBranchNumber(branchId);

        val locations = erpSystem.loadAllLocationItems(branchId, count.getErpCountId());

        for (var erpLocation : locations) {
            // Sometimes the locationId is null but still has products
            // We need to set these as no loc
            if (erpLocation.getId() == null || erpLocation.getId().trim().isEmpty()) {
                erpLocation.setId(NO_LOCATION);
            }

            // Create Location record if it doesn't exist
            val location = locationRepository
                .findByBranchAndErpLocationId(branch, erpLocation.getId())
                .orElseGet(() -> {
                    val l = new Location();
                    l.setBranch(branch);
                    l.setErpLocationId(erpLocation.getId());
                    return locationRepository.save(l);
                });

            // Create LocationCount record if it doesn't exist
            val locationCountOpt = locationCountRepository.findByLocationAndCount(location, count);
            if (locationCountOpt.isEmpty()) {
                val lc = new LocationCount();
                lc.setLocation(location);
                lc.setCount(count);

                val locationCount = locationCountRepository.save(lc);
                val seq = new AtomicInteger();

                // If mincron need to load the count items as the loadAllLocationItems will not have that data
                if (erpSystemName == ERPSystemName.MINCRON) {
                    var locationItems = erpSystem.loadLocationItems(
                        branchId,
                        count.getErpCountId(),
                        erpLocation.getId()
                    );
                    erpLocation.setProducts(locationItems);
                }

                val countItems = erpLocation
                    .getProducts()
                    .stream()
                    .map(LocationProductDTO::toEntity)
                    .peek(countItem -> {
                        countItem.setLocationCount(locationCount);
                        countItem.setSequence(seq.getAndIncrement());
                        countItem.setVarianceStatus(VarianceCountItemStatus.NONVARIANCE);
                    })
                    .collect(Collectors.toList());

                countLocationItemRepository.saveAll(countItems);
            }
        }

        stopWatch.stop();

        log.info("Exiting loadLocations({}), {}ms", count.getId(), System.currentTimeMillis() - start);
    }

    private void completeLoad(Count count) {
        log.info("Entering completeLoad({})", count.getId());
        val start = System.currentTimeMillis();

        count.setStatus(CountStatus.COMPLETE);
        count.setErrorMessage(null);
        count.setAllLocationsFetched(true);
        countRepository.save(count);

        log.info("Exiting completeLoad({}), {}ms", count.getId(), System.currentTimeMillis() - start);
    }
}
