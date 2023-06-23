package com.reece.punchoutcustomersync.service;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.reece.punchoutcustomerbff.models.daos.CustomerDao;
import com.reece.punchoutcustomerbff.models.daos.SyncLogDao;
import com.reece.punchoutcustomerbff.models.repositories.CustomerRepository;
import com.reece.punchoutcustomerbff.models.repositories.SyncLogRepository;
import com.reece.punchoutcustomerbff.util.SyncLogStatusUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class SyncService {
    @Autowired
    private SyncLogRepository syncLogRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PricingService pricingService;

    @Value("${sync.syncDaysCooldown}")
    private String syncDaysCooldown;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");


    /**
     * This scheduled task checks sync log records given dynamically set values (syncDaysCooldown and schedule)
     * in the application properties to ensure execution of the task is required.
     *
     * If the task is ready to execute, sync eligible customers are retrieved and each customer is used to sync the
     * given customer's catalogs, catalog-products, and products.
     *
     * @author austin.norryce
     */
    public void syncCatalogProducts() {
        final Timestamp executionStartTimestamp = new Timestamp(System.currentTimeMillis());

        log.info("The scheduled task \"{}\" started execution on {}", this.getClass().getName(), DATE_FORMAT.format(executionStartTimestamp));

        SyncLogDao mostRecentSyncLog = syncLogRepository.getMostRecentSyncLog();

        if (isSyncReadyToExecute(executionStartTimestamp, mostRecentSyncLog)) {
            SyncLogDao syncLog = SyncLogDao.builder()
                    .id(UUID.randomUUID())
                    .startDatetime(executionStartTimestamp)
                    .status(SyncLogStatusUtil.STARTED)
                    .build();
            syncLogRepository.save(syncLog);

            List<CustomerDao> syncEligibleCustomers = getSyncEligibleCustomers(mostRecentSyncLog);

            try {
                for (CustomerDao syncEligibleCustomer : syncEligibleCustomers) {
                    pricingService.syncCustomerProductCatalogs(syncEligibleCustomers.size(), syncEligibleCustomer.getId(), syncEligibleCustomers.indexOf(syncEligibleCustomer) + 1, syncLog.getId());
                }
            } catch(IOException | JSchException | SftpException exception) {
                syncLog.setEndDatetime(new Timestamp(System.currentTimeMillis()));
                syncLog.setStatus(SyncLogStatusUtil.FAILED);
                syncLogRepository.save(syncLog);
                log.error(exception.getMessage());
                return;
            }

            syncLog.setStatus(SyncLogStatusUtil.COMPLETED);
            syncLog.setEndDatetime(new Timestamp(System.currentTimeMillis()));
            syncLogRepository.save(syncLog);
        } else {
            log.info("The most recent sync executed on {} which falls in the past {} day cool-down period.", DATE_FORMAT.format(mostRecentSyncLog.getStartDatetime()), syncDaysCooldown);
        }

        log.info("The scheduled task \"{}\" finished executing on {}", this.getClass().getName(), DATE_FORMAT.format(System.currentTimeMillis()));
    }

    private boolean isSyncReadyToExecute(Timestamp executionStartTime, SyncLogDao syncLog) {
        long coolDown = (long) 1000 * 60 * 60 * 24 * Integer.parseInt(syncDaysCooldown);
        return syncLog == null || executionStartTime.after(new Timestamp(syncLog.getStartDatetime().getTime() + coolDown));
    }

    private List<CustomerDao> getSyncEligibleCustomers(SyncLogDao syncLog) {
        if (syncLog == null) {
            return customerRepository.findAll();
        }
        return customerRepository.retrieveAllCustomersUpdatedSince(syncLog.getStartDatetime());
    }
}
