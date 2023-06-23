package com.reece.punchoutcustomersync.service;

import com.reece.punchoutcustomerbff.models.daos.CustomerDao;
import com.reece.punchoutcustomerbff.models.daos.SyncLogDao;
import com.reece.punchoutcustomerbff.models.repositories.CustomerRepository;
import com.reece.punchoutcustomerbff.models.repositories.SyncLogRepository;
import com.reece.punchoutcustomerbff.util.TestUtils;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

public class SyncServiceTest {

    @Mock
    private SyncLogRepository syncLogRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PricingService pricingService;

    @InjectMocks
    private SyncService subject;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(subject, "syncDaysCooldown", "7");
    }

    @Test
    public void testFirstSyncWithCustomers() throws Exception {
        // Given:
        CustomerDao customer1 = TestUtils.generateCustomer();
        CustomerDao customer2 = TestUtils.generateCustomer();
        List<CustomerDao> customers = List.of(customer1, customer2);

        // When:
        when(syncLogRepository.getMostRecentSyncLog()).thenReturn(null);
        when(customerRepository.findAll()).thenReturn(customers);
        subject.syncCatalogProducts();

        // Then:
        verify(syncLogRepository, times(2)).save(any(SyncLogDao.class));
        verify(pricingService, times(customers.size())).syncCustomerProductCatalogs(any(Integer.class), any(UUID.class), any(Integer.class), any(UUID.class));
    }

    @Test
    public void testNewSyncWithCustomers() throws Exception {
        // Given:
        CustomerDao customer1 = TestUtils.generateCustomer();
        CustomerDao customer2 = TestUtils.generateCustomer();
        List<CustomerDao> customers = List.of(customer1, customer2);

        SyncLogDao syncLog = TestUtils.generateSyncLog();
        syncLog.setStartDatetime(new Timestamp(System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 8)) );

        // When:
        when(syncLogRepository.getMostRecentSyncLog()).thenReturn(syncLog);
        when(customerRepository.retrieveAllCustomersUpdatedSince(any(Timestamp.class))).thenReturn(customers);
        subject.syncCatalogProducts();

        // Then:
        verify(syncLogRepository, times(2)).save(any(SyncLogDao.class));
        verify(pricingService, times(customers.size())).syncCustomerProductCatalogs(any(Integer.class), any(UUID.class), any(Integer.class), any(UUID.class));
    }

    @Test
    public void testNewSyncWithCustomersCooldown() throws Exception {
        // Given:
        CustomerDao customer1 = TestUtils.generateCustomer();
        CustomerDao customer2 = TestUtils.generateCustomer();
        List<CustomerDao> customers = List.of(customer1, customer2);

        SyncLogDao syncLog = TestUtils.generateSyncLog();
        syncLog.setStartDatetime(new Timestamp(System.currentTimeMillis()));

        // When:
        when(syncLogRepository.getMostRecentSyncLog()).thenReturn(syncLog);
        when(customerRepository.findAll()).thenReturn(customers);
        subject.syncCatalogProducts();

        // Then:
        verify(syncLogRepository, times(0)).save(any(SyncLogDao.class));
        verify(pricingService, times(0)).syncCustomerProductCatalogs(any(Integer.class), any(UUID.class), any(Integer.class), any(UUID.class));
    }
}
