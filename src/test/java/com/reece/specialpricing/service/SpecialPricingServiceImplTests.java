package com.reece.specialpricing.service;

import com.reece.specialpricing.model.PaginationContext;
import com.reece.specialpricing.postgres.SpecialPrice;
import com.reece.specialpricing.model.exception.CSVUploadException;
import com.reece.specialpricing.repository.FileUploadDataService;
import com.reece.specialpricing.postgres.SpecialPricingDataService;
import com.reece.specialpricing.utilities.ExcludedPriceLineEnum;
import com.reece.specialpricing.utilities.TestCommon;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SpecialPricingServiceImplTests extends TestCommon {
    @Mock(name = "localUploadService")
    private FileUploadDataService fileService;

    @Mock
    private SpecialPricingDataService specialPricingDataService;

    @InjectMocks
    private SpecialPricingServiceImpl priceChangeService;

    @BeforeEach
    public void setUp(){
        reset(priceChangeService, specialPricingDataService);
    }

    @Test @Ignore //TODO:to be fixed
    public void uploadPriceChanges_shouldGroupByCustomerIdAndBranch() throws Exception {
        when(fileService.uploadFile(any(), anyString())).thenReturn("/somePath/someFile.csv");
        var result = priceChangeService.createAndUpdatePrices(validChangeRequest);
        verify(fileService, times(1)).uploadFile(any(), matches("Amend - customer1 - branch1 - \\d{13}.csv"));
        verify(fileService, times(1)).uploadFile(any(), matches("Amend - customer2 - branch1 - \\d{13}.csv"));
        verify(fileService, times(1)).uploadFile(any(), matches("Amend - customer2 - branch2 - \\d{13}.csv"));
        verify(fileService, times(3)).uploadFile(any(), anyString());
        assert result.getSuccessfulUploadPaths().size() == 3;
        assert result.getFailedUpdateSuggestions().size() == 0;
        assert result.getFailedCreateSuggestions().size() == 0;
    }

    @Test @Ignore //TODO:to be fixed
    public void uploadPriceChanged_shouldAddFailedChangesIfFileUploadFails() throws Exception {
        when(fileService.uploadFile(any(), matches("Amend - customer1 - branch1 - \\d{13}.csv"))).thenReturn(null);
        when(fileService.uploadFile(any(), matches("Amend - (customer2 - branch1|customer2 - branch2) - \\d{13}.csv"))).thenReturn("/somePath/someFile.csv");
        var result = priceChangeService.createAndUpdatePrices(validChangeRequest);
        verify(fileService, times(3)).uploadFile(any(), anyString());
        assert result.getSuccessfulUploadPaths().size() == 2;
        assert result.getFailedUpdateSuggestions().size() == 1;
        assert result.getFailedUpdateSuggestions().get(0).getCustomerId().equals("customer1");
        assert result.getFailedUpdateSuggestions().get(0).getBranch().equals("branch1");
    }

    @Test  @Ignore //TODO:to be fixed
    public void uploadPriceChanged_shouldAddFailedChangesIfFileGenerationFails() throws Exception {
        when(fileService.uploadFile(any(), matches("Amend - customer1 - branch1 - \\d{13}.csv"))).thenReturn(null);
        when(fileService.uploadFile(any(), matches("Amend - (customer2 - branch1|customer2 - branch2) - \\d{13}.csv"))).thenReturn("/somePath/someFile.csv");
        var result = priceChangeService.createAndUpdatePrices(validChangeRequest);
        verify(fileService, times(3)).uploadFile(any(), anyString());
        assert result.getSuccessfulUploadPaths().size() == 2;
        assert result.getFailedUpdateSuggestions().size() == 1;
        assert result.getFailedUpdateSuggestions().get(0).getCustomerId().equals("customer1");
        assert result.getFailedUpdateSuggestions().get(0).getBranch().equals("branch1");
    }

    @Test(expected = CSVUploadException.class)
    public void uploadPriceChanged_shouldThrowExceptionIfAllUploadsFail() throws Exception {
        when(fileService.uploadFile(any(), any())).thenReturn(null);
        priceChangeService.createAndUpdatePrices(validChangeRequest);
    }

    @Test
    public void getPrices_shouldSortPricesAccordingToOrderDirection(){
        var specialPrice1 = new SpecialPrice();
        var specialPrice2 = new SpecialPrice();
        specialPrice1.setDisplayName("first");
        specialPrice2.setDisplayName("second");
        specialPrice1.setPriceLine("DEF");
        specialPrice2.setPriceLine("ABC");

        when(specialPricingDataService.findByCustomerIdAndProductId("1", "1")).thenReturn(List.of(specialPrice1,specialPrice2));
        var paginationContext = new PaginationContext();
        paginationContext.setOrderBy("displayName");
        paginationContext.setOrderDirection("asc");
        var result = priceChangeService.getPrices("1", "1", null, paginationContext);

        assert result.getResults().size() == 2;
        assert ((SpecialPrice)result.getResults().get(0)).getDisplayName().equals("first");
        assert ((SpecialPrice)result.getResults().get(1)).getDisplayName().equals("second");
        verify(specialPricingDataService, times(1)).findByCustomerIdAndProductId("1","1");

        reset(specialPricingDataService);

        when(specialPricingDataService.findByCustomerIdAndProductId("1", "1")).thenReturn(List.of(specialPrice1,specialPrice2));
        paginationContext.setOrderBy("displayName");
        paginationContext.setOrderDirection("desc");
        result = priceChangeService.getPrices("1", "1", null, paginationContext);
        assert result.getResults().size() == 2;
        assert ((SpecialPrice)result.getResults().get(0)).getDisplayName().equals("second");
        assert ((SpecialPrice)result.getResults().get(1)).getDisplayName().equals("first");
        verify(specialPricingDataService, times(1)).findByCustomerIdAndProductId("1","1");
    }

    @Test
    public void getPrices_shouldPagePricesAfterOrdering(){
        var specialPrice1 = new SpecialPrice();
        var specialPrice2 = new SpecialPrice();
        var specialPrice3 = new SpecialPrice();

        specialPrice1.setBranch("branchThird");
        specialPrice1.setDisplayName("first");
        specialPrice1.setPriceLine("DEF");

        specialPrice2.setBranch("branchSecond");
        specialPrice2.setDisplayName("second");
        specialPrice2.setPriceLine("ABC");

        specialPrice3.setBranch("branchFirst");
        specialPrice3.setDisplayName("third");
        specialPrice3.setPriceLine("XYZ");

        when(specialPricingDataService.findByCustomerIdAndProductId("1", "1")).thenReturn(List.of(specialPrice1, specialPrice2, specialPrice3));
        var paginationContext = new PaginationContext(1, 2, "branch", "asc");

        var result = priceChangeService.getPrices("1", "1", null, paginationContext);

        assert result.getResults().size() == 2;
        assert ((SpecialPrice)result.getResults().get(0)).getDisplayName().equals("third");
        assert ((SpecialPrice)result.getResults().get(1)).getDisplayName().equals("second");
        assert result.getNextPage() == 2;
        verify(specialPricingDataService, times(1)).findByCustomerIdAndProductId("1","1");
    }

    @Test
    public void getPriceLines_shouldReturnADistinctSetOfResultsForCustomerIdAndProductId() {
        var specialPrice1 = new SpecialPrice();
        var specialPrice2 = new SpecialPrice();
        var specialPrice3 = new SpecialPrice();

        specialPrice1.setPriceLine("priceLine1");
        specialPrice2.setPriceLine("priceLine1");
        specialPrice3.setPriceLine("priceLine2");

        when(specialPricingDataService.findByCustomerIdAndProductId(any(), any())).thenReturn(List.of(specialPrice1, specialPrice2, specialPrice3));

        var result = priceChangeService.getPriceLines("1", "1");

        assert result.size() == 2;
    }

    @Test
    public void getPriceLines_shouldReturnADistinctSetOfResultsForCustomerId() {
        var specialPrice1 = new SpecialPrice();
        var specialPrice2 = new SpecialPrice();
        var specialPrice3 = new SpecialPrice();

        specialPrice1.setPriceLine("priceLine1");
        specialPrice2.setPriceLine("priceLine1");
        specialPrice3.setPriceLine("priceLine2");

        when(specialPricingDataService.findByCustomerId(any())).thenReturn(List.of(specialPrice1, specialPrice2, specialPrice3));

        var result = priceChangeService.getPriceLines("1", null);

        assert result.size() == 2;
    }

    @Test
    public void getPriceLines_shouldReturnAPriceLinesInSortedOrder() {
        var specialPrice1 = new SpecialPrice();
        var specialPrice2 = new SpecialPrice();
        var specialPrice3 = new SpecialPrice();

        specialPrice1.setPriceLine("priceLineB");
        specialPrice2.setPriceLine("priceLineC");
        specialPrice3.setPriceLine("priceLineA");

        when(specialPricingDataService.findByCustomerId(any())).thenReturn(List.of(specialPrice1, specialPrice2, specialPrice3));

        var result = priceChangeService.getPriceLines("1", null);

        List<String> list = new ArrayList<>(result);
        assert Objects.equals(list.get(0), "priceLineA");
        assert Objects.equals(list.get(1), "priceLineB");
        assert Objects.equals(list.get(2), "priceLineC");

        assert result.size() == 3;
    }

    @Test
    public void getPriceLines_shouldReturnADistinctSetOfResultsForProductId() {
        var specialPrice1 = new SpecialPrice();
        var specialPrice2 = new SpecialPrice();
        var specialPrice3 = new SpecialPrice();

        specialPrice1.setPriceLine("priceLine1");
        specialPrice2.setPriceLine("priceLine1");
        specialPrice3.setPriceLine("priceLine2");

        when(specialPricingDataService.findByProductId(any())).thenReturn(List.of(specialPrice1, specialPrice2, specialPrice3));

        var result = priceChangeService.getPriceLines(null, "1");

        assert result.size() == 2;
    }

    @Test
    public void getPriceLines_shouldReturnEmptySet() {
        when(specialPricingDataService.findByCustomerIdAndProductId(any(), any())).thenReturn(Collections.emptyList());

        var result = priceChangeService.getPriceLines("1", "1");

        assert result.size() == 0;
    }
}
