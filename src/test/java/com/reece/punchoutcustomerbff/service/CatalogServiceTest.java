package com.reece.punchoutcustomerbff.service;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.reece.punchoutcustomerbff.dto.*;
import com.reece.punchoutcustomerbff.mapper.CatalogProductMapper;
import com.reece.punchoutcustomerbff.mapper.CustomerMapper;
import com.reece.punchoutcustomerbff.models.daos.CatalogDao;
import com.reece.punchoutcustomerbff.models.daos.CatalogProductDao;
import com.reece.punchoutcustomerbff.models.daos.CustomerDao;
import com.reece.punchoutcustomerbff.models.daos.ProductDao;
import com.reece.punchoutcustomerbff.models.daos.UploadDao;
import com.reece.punchoutcustomerbff.models.dtos.ProductToCsvRecordDto;
import com.reece.punchoutcustomerbff.models.repositories.*;
import com.reece.punchoutcustomerbff.util.DateGenerator;
import com.reece.punchoutcustomerbff.util.DateUtil;
import com.reece.punchoutcustomerbff.util.TestUtils;
import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CatalogServiceTest {

    @Mock
    private CatalogRepository catalogRepository;

    @Mock
    private CatalogProductRepository catalogProductRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private DateGenerator dateGenerator;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UploadRepository uploadRepository;

    @Mock
    private CatalogService instance;

    @InjectMocks
    private CatalogService subject;

    @Mock
    private UploadService uploadService;

    @Mock
    private Optional<CatalogDao> optionalCatalog;

    @Mock
    private Optional<CustomerDao> optionalCustomer;

    @Captor
    private ArgumentCaptor<CatalogDao> catalogCaptor;

    @Captor
    private ArgumentCaptor<List<CatalogProductDao>> mappingsCaptor;

    @Mock
    private Page<CatalogProductDao> paging;

    @Test
    public void testRenameCatalog() {
        // given
        String catalogId = "0e3a4ecc-6cfa-4821-8d80-b85a28f27dfa";
        RenameCatalogDto input = RenameCatalogDto.builder().name("alpha").build();

        // and
        CatalogDao catalog = TestUtils.generateCatalog();
        when(catalogRepository.findById(UUID.fromString(catalogId))).thenReturn(optionalCatalog);
        when(optionalCatalog.get()).thenReturn(catalog);

        // when
        subject.renameCatalog(catalogId, input);

        // then
        verify(catalogRepository).save(catalogCaptor.capture());
        CatalogDao result = catalogCaptor.getValue();
        assertThat(result.getName(), equalTo("alpha"));
    }

    @Test
    public void testListCatalogProducts() {
        // given
        String catalogId = "0e3a4ecc-6cfa-4821-8d80-b85a28f27dfa";
        int page = 1;
        int perPage = 2;

        // and: we find a matching catalog
        CatalogDao catalog = TestUtils.generateCatalog();
        when(catalogRepository.findById(UUID.fromString(catalogId))).thenReturn(optionalCatalog);
        when(optionalCatalog.get()).thenReturn(catalog);

        // and: We have a paging object returned
        when(catalogProductRepository.findWithProducts(eq(UUID.fromString(catalogId)), Mockito.any(Pageable.class)))
            .thenReturn(paging);

        // and: the paging object has data
        when(paging.getTotalPages()).thenReturn(10);
        CatalogProductDao mapping = TestUtils.generateMappingWithProduct();
        when(paging.getContent()).thenReturn(List.of(mapping));

        // when
        CatalogViewDto result = subject.listCatalogProducts(catalogId, page, perPage);

        // then: verify the pagination settings
        assertThat(result.getCatalogId(), equalTo("0e3a4ecc-6cfa-4821-8d80-b85a28f27dfa"));
        assertThat(result.getPage(), equalTo(1));
        assertThat(result.getResultsPerPage(), equalTo(2));
        assertThat(result.getTotalPages(), equalTo(10));

        // and: verify the catalog
        TestUtils.assertDefaultCatalogDto(result.getCatalog());

        // and: verify the customer
        Assertions.assertEquals(
            result.getCustomer(),
            CustomerMapper.toDTO(catalog.getCustomer()),
            "Customer retrieved is the same as customer created"
        );

        // and: verify the returned mapping
        assertThat(result.getResults().size(), equalTo(1));
        TestUtils.assertDefaultMapping(result.getResults().get(0));

        // and: verify the product
        TestUtils.assertDefaultProduct(result.getResults().get(0).getProduct());
    }

    @Test
    public void testSaveNewCatalog() {
        // given
        CustomerDao customer = TestUtils.generateCustomerDao();
        String content1 = "Product ID,Branch,Customer ID\n";
        content1 += "part-01,TEST-BRANCH-01,TEST-01\n";
        String content2 = "Product ID,Branch,Customer ID\n";
        content2 += "TEST-PART-04,TEST-BRANCH-01,TEST-01\n";
        UploadDao upload1 = UploadDao.builder().content(content1).id(UUID.randomUUID()).build();
        InputCsvRecord input1 = InputCsvRecord
            .builder()
            .customerId(customer.getCustomerId())
            .branch("TEST-BRANCH-01")
            .productId("part-01")
            .lineNumber(1)
            .build();
        UploadDao upload2 = UploadDao.builder().content(content2).id(UUID.randomUUID()).build();
        InputCsvRecord input2 = InputCsvRecord
            .builder()
            .customerId(customer.getCustomerId())
            .branch("TEST-BRANCH-01")
            .productId("TEST-PART-04")
            .lineNumber(1)
            .build();
        String customerId = customer.getId().toString();
        NewCatalogInputDto input = NewCatalogInputDto
            .builder()
            .name("alpha")
            .uploadIds(List.of(upload1.getId().toString(), upload2.getId().toString()))
            .procurementSystem("Greenwing")
            .build();

        // and: mock the call to itself for listing
        subject.instance = instance;

        // and: an existing customer is returned
        when(customerRepository.findById(UUID.fromString(customerId))).thenReturn(optionalCustomer);
        when(optionalCustomer.get()).thenReturn(customer);

        when(uploadService.processInputCsv(content1)).thenReturn(List.of(input1));
        when(uploadService.processInputCsv(content2)).thenReturn(List.of(input2));

        when(uploadRepository.findAllById(List.of(upload1.getId(), upload2.getId())))
            .thenReturn(List.of(upload1, upload2));

        // and: we have a generated date
        when(dateGenerator.generateTimestamp()).thenReturn(DateUtil.toTimestamp("2023-05-17T05:00:00.000+0000"));

        // and: that one of the products is found by part number
        List<ProductDao> products = List.of(TestUtils.generateDefaultProduct());
        when(productRepository.findByPartNumbers(List.of("part-01"))).thenReturn(List.of(products.get(0)));
        when(productRepository.findByPartNumbers(List.of("TEST-PART-04"))).thenReturn(List.of(products.get(0)));

        // and: we make an internal call ot the existing functionality for view catalog
        CatalogViewDto mockResult = new CatalogViewDto();
        when(instance.listCatalogProducts(Mockito.any(String.class), eq(1), eq(100))).thenReturn(mockResult);

        // when
        CatalogViewDto result = subject.saveNewCatalog(customerId, input);

        // then
        assertThat(result, equalTo(mockResult));

        // and: verify that a new catalog was created
        verify(catalogRepository).save(catalogCaptor.capture());
        CatalogDao catalog = catalogCaptor.getValue();
        assertThat(catalog.getId(), is(notNullValue()));
        assertThat(catalog.getName(), equalTo("alpha"));
        assertThat(catalog.getStatus(), equalTo("DRAFT"));
        assertThat(catalog.getCustomer().getId().toString(), equalTo(customer.getId().toString()));
        assertThat(DateUtil.fromDate(catalog.getLastUpdate()), equalTo("2023-05-17T05:00:00.000+0000"));
        assertThat(catalog.getProcSystem(), equalTo("Greenwing"));

        // and: verify two new mappings are created
        verify(catalogProductRepository, times(2)).saveAll(mappingsCaptor.capture());
        List<List<CatalogProductDao>> mappings = mappingsCaptor.getAllValues();

        assertThat(mappings.size(), equalTo(2));
        List<CatalogProductDao> mapping1 = mappings.get(0);
        assertThat(mapping1.size(), equalTo(1));
        assertThat(mapping1.get(0).getProduct().getPartNumber(), equalTo("part-01"));
        assertThat(mapping1.get(0).getPartNumber(), equalTo("part-01"));

        List<CatalogProductDao> mapping2 = mappings.get(1);
        assertThat(mapping2.size(), equalTo(1));
        assertThat(mapping2.get(0).getProduct().getPartNumber(), equalTo("TEST-PART-04"));
        assertThat(mapping2.get(0).getPartNumber(), equalTo("TEST-PART-04"));
    }

    @Test
    public void testListCatalogAndUploadProducts() {
        // given
        String catalogId = "0e3a4ecc-6cfa-4821-8d80-b85a28f27dfa";
        int page = 1;
        int perPage = 2;
        CatalogProductDao catalogProduct = TestUtils.generateMappingWithProduct();
        CatalogProductDao uploadProduct = TestUtils.generateMappingWithProduct();
        uploadProduct.setPartNumber("UPLOAD-PRODUCT");
        uploadProduct.setId(UUID.randomUUID());

        // and: we find a matching catalog
        CatalogDao catalog = TestUtils.generateCatalog();
        catalog.setId(UUID.fromString(catalogId));
        when(catalogRepository.findById(catalog.getId())).thenReturn(optionalCatalog);
        when(optionalCatalog.get()).thenReturn(catalog);

        // and: we find a matching upload
        UploadDao upload = UploadDao
            .builder()
            .id(UUID.randomUUID())
            .uploadDatetime(dateGenerator.generateTimestamp())
            .build();
        List<UUID> uploadIds = new ArrayList<>(Arrays.asList(upload.getId()));
        List<UploadDao> uploads = new ArrayList<>(Arrays.asList(upload));
        when(uploadRepository.findAllById(uploadIds)).thenReturn(uploads);
        when(optionalCatalog.get()).thenReturn(catalog);

        // and: We have a paging object returned
        List<CatalogProductDao> catalogProducts = new ArrayList<>(Arrays.asList(catalogProduct));
        when(catalogProductRepository.findWithProducts(eq(UUID.fromString(catalogId)))).thenReturn(catalogProducts);

        // and: the paging object has data
        List<InputCsvRecord> inputCsvs = new ArrayList<>();

        List<ProductToCsvRecordDto> productMapping = new ArrayList<>(
            Arrays.asList(
                ProductToCsvRecordDto.builder().catalogProduct(CatalogProductMapper.toDTO(uploadProduct)).build()
            )
        );
        when(uploadService.processInputCsv(upload.getContent())).thenReturn(inputCsvs);
        when(uploadService.generateProductMap(inputCsvs)).thenReturn(productMapping);

        // when
        Optional<String> optionalCatalogId = Optional.of(optionalCatalog.get().getId().toString());
        CatalogViewDto result = subject.listCatalogAndUploadProducts(page, perPage, uploadIds, optionalCatalogId);

        // then: verify the pagination settings
        assertThat(result.getCatalogId(), equalTo("0e3a4ecc-6cfa-4821-8d80-b85a28f27dfa"));
        assertThat(result.getPage(), equalTo(1));
        assertThat(result.getResultsPerPage(), equalTo(2));
        assertThat(result.getTotalPages(), equalTo(1));

        // and: verify the customer
        Assertions.assertEquals(
            result.getCustomer(),
            CustomerMapper.toDTO(catalog.getCustomer()),
            "Customer retrieved is the same as customer created"
        );

        // and: verify the returned mapping
        assertThat(result.getResults().size(), equalTo(2));
        TestUtils.assertDefaultMapping(result.getResults().get(0));

        // and: verify the product
        TestUtils.assertDefaultProduct(result.getResults().get(0).getProduct());
    }
}
