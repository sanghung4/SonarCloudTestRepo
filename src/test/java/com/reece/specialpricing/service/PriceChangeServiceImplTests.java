package com.reece.specialpricing.service;

import com.reece.specialpricing.model.exception.CSVUploadException;
import com.reece.specialpricing.repository.FileUploadDataService;
import com.reece.specialpricing.utilities.TestCommon;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PriceChangeServiceImplTests extends TestCommon {
    @Mock(name = "localUploadService")
    private FileUploadDataService fileService;

    @InjectMocks
    private PriceChangeServiceImpl priceChangeService;

    @BeforeEach
    public void setUp(){
        reset(priceChangeService);
    }

    @Test
    public void uploadPriceChanges_shouldGroupByCustomerIdAndBranch() throws Exception {
        when(fileService.uploadFile(any(), anyString())).thenReturn("/somePath/someFile.csv");
        var result = priceChangeService.uploadPriceChanges(listOfSuggestions);
        verify(fileService, times(1)).uploadFile(any(), matches("Amend - customer1 - branch1 - \\d{13}.csv"));
        verify(fileService, times(1)).uploadFile(any(), matches("Amend - customer2 - branch1 - \\d{13}.csv"));
        verify(fileService, times(1)).uploadFile(any(), matches("Amend - customer2 - branch2 - \\d{13}.csv"));
        verify(fileService, times(3)).uploadFile(any(), anyString());
        assert result.getSuccessfulUploadPaths().size() == 3;
        assert result.getFailedUpdateSuggestions().size() == 0;
    }

    @Test
    public void uploadPriceChanged_shouldAddFailedChangesIfFileUploadFails() throws Exception {
        when(fileService.uploadFile(any(), matches("Amend - customer1 - branch1 - \\d{13}.csv"))).thenReturn(null);
        when(fileService.uploadFile(any(), matches("Amend - (customer2 - branch1|customer2 - branch2) - \\d{13}.csv"))).thenReturn("/somePath/someFile.csv");
        var result = priceChangeService.uploadPriceChanges(listOfSuggestions);
        verify(fileService, times(3)).uploadFile(any(), anyString());
        assert result.getSuccessfulUploadPaths().size() == 2;
        assert result.getFailedUpdateSuggestions().size() == 1;
        assert result.getFailedUpdateSuggestions().get(0).getCustomerId().equals("customer1");
        assert result.getFailedUpdateSuggestions().get(0).getBranch().equals("branch1");
    }

    @Test
    public void uploadPriceChanged_shouldAddFailedChangesIfFileGenerationFails() throws Exception {
        when(fileService.uploadFile(any(), matches("Amend - customer1 - branch1 - \\d{13}.csv"))).thenReturn(null);
        when(fileService.uploadFile(any(), matches("Amend - (customer2 - branch1|customer2 - branch2) - \\d{13}.csv"))).thenReturn("/somePath/someFile.csv");
        var result = priceChangeService.uploadPriceChanges(listOfSuggestions);
        verify(fileService, times(3)).uploadFile(any(), anyString());
        assert result.getSuccessfulUploadPaths().size() == 2;
        assert result.getFailedUpdateSuggestions().size() == 1;
        assert result.getFailedUpdateSuggestions().get(0).getCustomerId().equals("customer1");
        assert result.getFailedUpdateSuggestions().get(0).getBranch().equals("branch1");
    }

    @Test(expected = CSVUploadException.class)
    public void uploadPriceChanged_shouldThrowExceptionIfAllUploadsFail() throws Exception {
        when(fileService.uploadFile(any(), any())).thenReturn(null);
        priceChangeService.uploadPriceChanges(listOfSuggestions);
    }


}
