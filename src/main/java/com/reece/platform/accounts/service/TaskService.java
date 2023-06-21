package com.reece.platform.accounts.service;

import com.reece.platform.accounts.model.DTO.ErpAccountInfo;
import com.reece.platform.accounts.model.entity.Account;
import com.reece.platform.accounts.model.repository.AccountDAO;
import com.reece.platform.accounts.utilities.AccountDataFormatting;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class TaskService {

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private ErpService erpService;

    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<Integer> getEclipseAccounts(List<Account> listAccounts) {
        ErpAccountInfo erpAccountInfo = null;
        long startTime = System.currentTimeMillis();
        int index = 0;
        for (Account account : listAccounts) {
            try {
                erpAccountInfo = erpService.getEclipseAccount(account.getErpAccountId(), false, false);
                account.setName(erpAccountInfo.getCompanyName());
                account.setAddress(
                    AccountDataFormatting.formatAccountAddress(
                        erpAccountInfo.getStreet1(),
                        erpAccountInfo.getCity(),
                        erpAccountInfo.getState(),
                        erpAccountInfo.getZip()
                    )
                );
                account.setBranchId(erpAccountInfo.getBranchId());
                accountDAO.save(account);
                index++;
                log.info("Thread-Refreshed account #{} of {}", index, listAccounts.size());
            } catch (Exception e) {
                log.error("ERP account ID not found in Eclipse: {}", account.getErpAccountId());
            }
        }
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);
        log.info(
            "Thread-Account refresh completed for {} total accounts.  Time elapsed: {} seconds",
            listAccounts.size(),
            duration / 1000
        );
        return CompletableFuture.completedFuture(index);
    }
}
