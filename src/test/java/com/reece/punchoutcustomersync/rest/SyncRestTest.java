package com.reece.punchoutcustomersync.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.reece.punchoutcustomerbff.dto.CatalogDto;
import com.reece.punchoutcustomerbff.dto.ListCustomersDto;
import com.reece.punchoutcustomerbff.dto.ResultDto;
import com.reece.punchoutcustomersync.dto.AuditInputDto;
import com.reece.punchoutcustomersync.dto.AuditOutputDto;
import com.reece.punchoutcustomersync.service.AuditService;
import com.reece.punchoutcustomersync.service.CatalogService;
import com.reece.punchoutcustomersync.service.CustomerService;
import com.reece.punchoutcustomersync.service.RefreshService;
import com.reece.punchoutcustomersync.service.SyncService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SyncRestTest {

  @Mock
  private CustomerService customerService;

  @Mock
  private CatalogService catalogService;

  @Mock
  private AuditService auditService;

  @Mock
  private RefreshService refreshService;

  @Mock
  private SyncService syncService;

  @InjectMocks
  private SyncRest subject;

  @Test
  public void testRetrieveCustomers() {
    // given
    when(customerService.retrieveCustomersWithEligibleCatalogs()).thenReturn(new ListCustomersDto());

    // when
    ListCustomersDto result = subject.retrieveCustomers();

    // then
    assertThat(result, not(nullValue()));
  }

  @Test
  public void testRetrieveCatalogWithMappings() {
    // given: the ID of the catalog
    String catalogId = "6a946dc1-e0ef-4fae-8dcc-9ed24b89e3aa";

    // and: that a catalog is returned on lookup
    CatalogDto catalog = new CatalogDto();
    when(catalogService.retrieveCatalogWithMappings(catalogId)).thenReturn(catalog);

    // when: we call the endpoint to retrieve the catalog
    CatalogDto result = subject.retrieveCatalogWithMappings(catalogId);

    // then: we get a result
    assertThat(result, not(nullValue()));
  }

  @Test
  public void testAudit() {
    // given
    AuditInputDto input = new AuditInputDto();

    when(auditService.audit(input)).thenReturn(new AuditOutputDto());

    // when
    AuditOutputDto result = subject.audit(input);

    // then
    assertThat(result.isSuccess(), equalTo(true));
  }

  @Test
  public void testRefresh() {
    // given
    ResultDto mockedResult = new ResultDto();
    when(refreshService.refresh()).thenReturn(mockedResult);

    // when
    ResultDto result = subject.refreshTestData();

    // then
    assertThat(result.isSuccess(), equalTo(true));
  }

  @Test
  public void testProductSync() {
    // when
    ResultDto result = subject.sync();

    // then
    assertThat(result.isSuccess(), equalTo(true));

    // and
    verify(syncService).syncCatalogProducts();
  }
}
