package com.reece.platform.inventory.service;

import com.reece.platform.inventory.dto.CountDTO;
import com.reece.platform.inventory.dto.CountStatusDTO;
import com.reece.platform.inventory.dto.DeleteCountsDTO;
import com.reece.platform.inventory.exception.EclipseLoadCountsException;
import com.reece.platform.inventory.external.eclipse.EclipseService;
import com.reece.platform.inventory.model.Count;
import com.reece.platform.inventory.model.CountStatus;
import com.reece.platform.inventory.model.ERPSystemName;
import com.reece.platform.inventory.repository.CountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InternalService {

    /** Services */
    private final LoadCountService loadCountService;
    private final FileTransferService fileTransferService;
    private final CountAdminService countAdminService;

    /** Repositories */
    private final CountRepository countRepository;

    /** Services */
    private final EclipseService eclipseService;

    /**
     * Load count for branchId and countId - Mincron
     * @param branchId
     * @param countId
     * @return
     */
    public CountDTO loadMincronCount(String branchId, String countId) {
        var start = System.currentTimeMillis();
        log.info("ERPCP-935: Entering loadCount({})", branchId, countId);
        var count = loadCountService.findOrCreateCount(branchId, countId, false);

        // If count is not in progress and not completed, load count
        if (count.getStatus() != CountStatus.IN_PROGRESS && count.getStatus() != CountStatus.COMPLETE) {
            loadCountService.doLoad(count);
        }

        log.info("ERPCP-935: Exiting loadCount({}), {}ms", branchId, countId, System.currentTimeMillis() - start);
        return CountDTO.fromEntity(count);
    }

    /**
     * Load count for Eclipse, by reading SFTP files
     */
    public void batchLoadEclipseCounts() throws EclipseLoadCountsException {
        fileTransferService.downloadLatestEclipseCountsFile();
    }

    private boolean isCountLoaded(String branchId, String countId) {
        Optional<Count> countOptional = countRepository.findCount(branchId, countId);
        return countOptional.isPresent() && !countOptional.get().getStatus().equals(CountStatus.NOT_LOADED);
   }

    private List<CountStatusDTO> getEclipseNotLoadedCounts(Date startDate, Date endDate) {
        List<CountStatusDTO> countsToLoad;

        // 1. fetch eclipse counts
        var eclipseCounts = eclipseService
            .getCounts(startDate, endDate)
            .stream()
            .map(CountStatusDTO::new)
            .collect(Collectors.toList());

        // 2. Filter NOT_LOADED counts
        countsToLoad =
            eclipseCounts
                .stream()
                .filter(c -> !isCountLoaded(c.getBranchId(), c.getCountId()))
                .collect(Collectors.toList());

        return countsToLoad;
    }

    public List<CountStatusDTO> loadEclipseAPICount(Date startDate, Date endDate) {
        List<CountStatusDTO> counts;

        if (startDate == null || endDate == null) {
            counts = getEclipseNotLoadedCounts(new Date(), new Date());
        } else {
            counts = getEclipseNotLoadedCounts(startDate, endDate);
        }

        // 3. Load List of counts
        counts
            .stream()
            .forEach(count -> {
                countAdminService.loadCount(count.getBranchId(), count.getCountId());
                count.setStatus(CountStatus.IN_PROGRESS);
            });
        return counts;
    }

    public Integer deleteCounts(ERPSystemName erpSystemName, int endDateOffset) throws ParseException {
        LocalDateTime prevDate = LocalDateTime.now().minusDays(endDateOffset);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String endDate = prevDate.format(formatter);
        log.info("Starting Delete Counts of " + erpSystemName + " ERP System before Date: " + endDate);
        DeleteCountsDTO deleteCountsDTO = new DeleteCountsDTO(erpSystemName, endDate);

        return countAdminService.deleteCounts(deleteCountsDTO);
    }
}
