package com.reece.platform.inventory.service;

import com.reece.platform.inventory.dto.DeletedCountDTO;
import com.reece.platform.inventory.exception.CountNotFoundException;
import com.reece.platform.inventory.model.Action;
import com.reece.platform.inventory.model.Count;
import com.reece.platform.inventory.model.CountAudit;
import com.reece.platform.inventory.model.ERPSystemName;
import com.reece.platform.inventory.repository.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArchiveCountService {

    /** Repositories */
    private final CountRepository countRepository;
    private final WriteInRepository writeInRepository;
    private final CountItemQuantityRepository countItemQuantityRepository;
    private final CountLocationItemRepository countLocationItemRepository;
    private final VarianceCountItemQuantityRepository varianceCountItemQuantityRepository;
    private final LocationCountRepository locationCountRepository;
    private final CountAuditRepository countAuditRepository;

    @Transactional
    public DeletedCountDTO deleteCount(UUID countId) {
        var count = countRepository.findById(countId).orElseThrow(CountNotFoundException::new);

        // 1. Delete Write-Ins
        var writeIns = writeInRepository.deleteWriteIns(count.getId());

        // 2. Delete count location item quantities
        var countLocationItemQuantities = countItemQuantityRepository.delete(count.getId());

        // 3. Delete count location item variance quantities
        var varianceCountLocationItemQuantities = varianceCountItemQuantityRepository.delete(count.getId());

        // 4. Delete count location items
        var countLocationItems = countLocationItemRepository.delete(count.getId());

        // 5. Delete count locations
        var countLocations = locationCountRepository.delete(count.getId());

        // 6. Delete count record
        countRepository.delete(count);

        //Audit the count deletion
        auditDeleteCounts(
            CountAudit
                .builder()
                .erpCountId(count.getErpCountId())
                .erpBranchNum(count.getBranch().getErpBranchNum())
                .erpSystem(count.getBranch().getErpSystem())
                .action(Action.PURGE)
                .build()
        );

        return new DeletedCountDTO(
            countLocations,
            countLocationItems,
            countLocationItemQuantities,
            varianceCountLocationItemQuantities,
            writeIns
        );
    }

    @Transactional
    public Integer deleteWriteIns(ERPSystemName erpSystemName, Date endDate) {
        return writeInRepository.deleteWriteIns(erpSystemName, endDate);
    }

    @Transactional
    public Integer deleteCountLocationItemQuantities(ERPSystemName erpSystemName, Date endDate) {
        return countItemQuantityRepository.delete(erpSystemName, endDate);
    }

    @Transactional
    public Integer deleteCountLocationItemVarianceQuantities(ERPSystemName erpSystemName, Date endDate) {
        return varianceCountItemQuantityRepository.delete(erpSystemName, endDate);
    }

    @Transactional
    public Integer deleteCountLocationItems(ERPSystemName erpSystemName, Date endDate) {
        return countLocationItemRepository.delete(erpSystemName, endDate);
    }

    @Transactional
    public Integer deleteCountLocations(ERPSystemName erpSystemName, Date endDate) {
        return locationCountRepository.delete(erpSystemName, endDate);
    }

    @Transactional
    public Integer deleteCounts(ERPSystemName erpSystemName, Date endDate) {
        List<Count> countsToBeDeleted = countRepository.findCountsByErpSystemAndEndDate(erpSystemName, endDate);
        if (Objects.isNull(countsToBeDeleted) || countsToBeDeleted.isEmpty()) return 0; else {
            countsToBeDeleted
                .stream()
                .forEach(count -> {
                    countRepository.deleteById(count.getId());
                    auditDeleteCounts(
                        CountAudit
                            .builder()
                            .erpCountId(count.getErpCountId())
                            .erpSystem(erpSystemName)
                            .erpBranchNum(count.getBranch().getErpBranchNum())
                            .action(Action.PURGE)
                            .build()
                    );
                });
            return countsToBeDeleted.size();
        }
    }

    @Transactional
    public void auditDeleteCounts(CountAudit countAudit) {
        countAuditRepository.save(countAudit);
    }
}
