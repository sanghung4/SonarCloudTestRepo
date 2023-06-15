package com.reece.platform.inventory.service;

import com.reece.platform.inventory.dto.*;
import com.reece.platform.inventory.exception.CountNotFoundException;
import com.reece.platform.inventory.exception.InValidCountDeletionException;
import com.reece.platform.inventory.exception.InvalidDateException;
import com.reece.platform.inventory.external.eclipse.EclipseService;
import com.reece.platform.inventory.external.mincron.MincronService;
import com.reece.platform.inventory.model.Count;
import com.reece.platform.inventory.model.CountStatus;
import com.reece.platform.inventory.model.ERPSystemName;
import com.reece.platform.inventory.repository.CountRepository;
import com.reece.platform.inventory.util.DateUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CountAdminService {

    /** Repositories */
    private final CountRepository countRepository;

    /** Services */
    private final LoadCountService loadCountService;
    private final MincronService mincronService;
    private final EclipseService eclipseService;
    private final ArchiveCountService archiveCountService;

    /**
     * Load count for branchId and countId
     * @param branchId
     * @param countId
     * @return
     */
    public CountStatusDTO loadCount(String branchId, String countId) {
        var count = loadCountService.findOrCreateCount(branchId, countId, false);

        // If count is not in progress or already completed, load count
        if (count.getStatus() != CountStatus.IN_PROGRESS && count.getStatus() != CountStatus.COMPLETE) {
            loadCountService.doAsyncLoad(count);
        }

        return CountStatusDTO.fromEntity(count);
    }

    /**
     * Get Counts by Time Range
     * @param startDate
     * @param endDate
     * @return
     */
    public List<CountStatusDTO> getCountsByTimeRange(Date startDate, Date endDate) {
        var counts = new ArrayList<CountStatusDTO>();
        var loadedCounts = countRepository.findCountsByTimeRange(startDate, endDate);

        // 1. fetch mincron counts - filter out by time range
        var mincronCounts = mincronService
            .getCounts()
            .getCounts()
            .stream()
            .map(CountStatusDTO::new)
            .collect(Collectors.toList());
        mincronCounts =
            mincronCounts.stream().filter(c -> c.withinDateRange(startDate, endDate)).collect(Collectors.toList());
        counts.addAll(mincronCounts);

        // 2. fetch eclipse counts
        var eclipseCounts = eclipseService
            .getCounts(startDate, endDate)
            .stream()
            .map(CountStatusDTO::new)
            .collect(Collectors.toList());
        counts.addAll(eclipseCounts);

        // 3. fetch all counts from db
        var countIds = counts.stream().map(CountStatusDTO::getCountId).collect(Collectors.toList());
        var dbCounts = countRepository
            .findCountsByIds(countIds)
            .stream()
            .collect(Collectors.toMap(Count::getBranchAndCountId, item -> item, (item1, item2) -> item1));
        // 4. Add in loaded counts that have not already been found
        counts.addAll(
            loadedCounts.stream().filter(c -> !countIds.contains(c.getCountId())).collect(Collectors.toList())
        );

        // Return counts with status from db
        return counts
            .stream()
            .peek(count -> {
                var dbCount = dbCounts.get(count.getCountId() + count.getBranchId());
                if (dbCount != null) {
                    count.setId(dbCount.getId());
                    count.setStatus(dbCount.getStatus());
                    count.setCreatedAt(dbCount.getCreatedAt());
                    count.setErrorMessage(dbCount.getErrorMessage());
                }
            })
            .collect(Collectors.toList());
    }

    /**
     * Get Count Status Progress
     * @param id
     * @return
     */
    public CountStatusDTO getCountStatus(UUID id) {
        var count = countRepository.findById(id).orElseThrow(CountNotFoundException::new);
        var loaded_products = countRepository.getNumberOfProductsForCount(count.getId());

        return new CountStatusDTO(count, loaded_products);
    }

    /**
     * Purge Mincron Counts (will be used to delete eclipse counts as well)
     * @param deleteCountsDto
     * @return
     */
    public Integer deleteCounts(DeleteCountsDTO deleteCountsDto) throws ParseException {
        var dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        var endDate = dateFormat.parse(deleteCountsDto.getEndDate());
        var erpSystemName = deleteCountsDto.getErpSystemName();

        // 1. Delete Write-Ins
        log.info("Starting Delete Write-Ins...");
        var writeIns = archiveCountService.deleteWriteIns(erpSystemName, endDate);
        log.info("Deleted {} Write Ins", writeIns);

        // 2. Delete count location item quantities
        log.info("Starting Delete Count Location Item Quantities...");
        var countLocationItemQuantities = archiveCountService.deleteCountLocationItemQuantities(erpSystemName, endDate);
        log.info("Deleted {} Delete Count Location Item Quantities", countLocationItemQuantities);

        // 3. Delete count location item variance quantities
        log.info("Starting Delete Variance Count Location Item Quantities...");
        var varianceCountLocationItemQuantities = archiveCountService.deleteCountLocationItemVarianceQuantities(
            erpSystemName,
            endDate
        );
        log.info("Deleted {} Delete Variance Count Location Item Quantities", varianceCountLocationItemQuantities);

        // 4. Delete count location items
        log.info("Starting Delete Count Location Items...");
        var countLocationItems = archiveCountService.deleteCountLocationItems(erpSystemName, endDate);
        log.info("Deleted {} Delete Count Location Items", countLocationItems);

        // 5. Delete count locations
        log.info("Starting Delete Count Locations...");
        var countLocations = archiveCountService.deleteCountLocations(erpSystemName, endDate);
        log.info("Deleted {} Delete Count Locations", countLocations);

        // 6. Delete counts
        log.info("Starting Delete Counts...");
        var counts = archiveCountService.deleteCounts(erpSystemName, endDate);
        log.info("Deleted {} Delete Counts", counts);

        return counts;
    }

    /**
     * Delete Single Count from Database
     * @param countId
     * @return
     */
    public DeletedCountDTO deleteCount(UUID countId) {
        return archiveCountService.deleteCount(countId);
    }

    /**
     * Soft Delete Counts (used for mincron purge)
     * @param softDeleteCountsDTO
     * @return
     * @throws ParseException
     */
    public Integer softDeleteCounts(SoftDeleteCountsDTO softDeleteCountsDTO) {
        var dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            var startDate = dateFormat.parse(softDeleteCountsDTO.getStartDate());
            var endDate = dateFormat.parse(softDeleteCountsDTO.getEndDate());
            List<Count> counts = countRepository.findMincronCountsBetweenDates(startDate, endDate);
            if (null == counts || counts.size() == 0) return 0;
            counts.forEach(count -> count.setDeleted(true));
            countRepository.saveAll(counts);

            return counts.size();
        } catch (ParseException exception) {
            exception.printStackTrace();
            throw new InvalidDateException();
        }
    }

    /**
     *  Delete Counts using deleteCountsDto
     * @param deleteCountsDto
     * @return
     * @throws ParseException
     */
    public DeletedMultipleCountResponse deleteCountsForBranch(DeleteCountsDTO deleteCountsDto) {
        deleteCountsDto.setErpSystemName(ERPSystemName.fromBranchNumber(deleteCountsDto.getBranchId()));

        val uuidsList = countRepository.findCountsUUID(
            deleteCountsDto.getErpSystemName(),
            deleteCountsDto.getCountId(),
            deleteCountsDto.getBranchId()
        );

        if (Objects.isNull(uuidsList) || uuidsList.isEmpty()) throw new InValidCountDeletionException(
            HttpStatus.BAD_REQUEST,
            "INVALID_INPUT",
            String.format(
                "No counts found to be deleted for countid (%s) and branchId (%s)",
                deleteCountsDto.getCountId(),
                deleteCountsDto.getBranchId()
            )
        );

        return DeletedMultipleCountResponse
            .builder()
            .deletedCounts(uuidsList.stream().map(archiveCountService::deleteCount).collect(Collectors.toList()))
            .build();
    }
}
