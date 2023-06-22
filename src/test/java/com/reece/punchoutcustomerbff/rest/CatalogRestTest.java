package com.reece.punchoutcustomerbff.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.reece.punchoutcustomerbff.dto.CatalogViewDto;
import com.reece.punchoutcustomerbff.dto.NewCatalogInputDto;
import com.reece.punchoutcustomerbff.dto.RenameCatalogDto;
import com.reece.punchoutcustomerbff.dto.ResultDto;
import com.reece.punchoutcustomerbff.service.CatalogService;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CatalogRestTest {

    @Mock
    private CatalogService catalogService;

    @InjectMocks
    private CatalogRest subject;

    @Test
    public void testRenameCatalog() {
        // given
        String catalogId = "0e3a4ecc-6cfa-4821-8d80-b85a28f27dfa";
        RenameCatalogDto input = new RenameCatalogDto();

        // when
        ResultDto result = subject.renameCatalog(catalogId, input);

        // then
        assertThat(result.isSuccess(), equalTo(true));

        // and
        verify(catalogService).renameCatalog(catalogId, input);
    }

    @Test
    public void testViewCatalog() {
        // given
        String catalogId = "0e3a4ecc-6cfa-4821-8d80-b85a28f27dfa";
        int page = 1;
        int perPage = 2;

        // and
        CatalogViewDto mockedResult = catalogService.listCatalogProducts(catalogId, page, perPage);

        // when
        CatalogViewDto result = subject.viewCatalog(catalogId, page, perPage);

        // then
        assertThat(result, equalTo(mockedResult));
    }

    @Test
    public void testSaveCatalog() {
        // given
        String customerId = "0e3a4ecc-6cfa-4821-8d80-b85a28f27dfa";
        NewCatalogInputDto input = new NewCatalogInputDto();

        // and
        CatalogViewDto mockedResult = new CatalogViewDto();
        when(catalogService.saveNewCatalog(customerId, input)).thenReturn(mockedResult);

        // when
        CatalogViewDto result = subject.saveCatalog(customerId, input);

        // then
        assertThat(result, equalTo(mockedResult));
    }

    @Test
    public void testViewCatalogUploads() {
        // given
        Optional<String> catalogId = Optional.of("0e3a4ecc-6cfa-4821-8d80-b85a28f27dfa");
        List<UUID> uploadIds = new ArrayList<>(Arrays.asList(UUID.randomUUID()));
        int page = 1;
        int perPage = 2;

        // and
        CatalogViewDto mockedResult = catalogService.listCatalogAndUploadProducts(page, perPage, uploadIds, catalogId);

        // when
        CatalogViewDto result = subject.viewCatalogUploads(catalogId, page, perPage, uploadIds);

        // then
        assertThat(result, equalTo(mockedResult));
    }
}
