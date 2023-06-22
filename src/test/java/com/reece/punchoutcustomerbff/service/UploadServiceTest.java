package com.reece.punchoutcustomerbff.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.reece.punchoutcustomerbff.dto.CatalogProductDto;
import com.reece.punchoutcustomerbff.dto.InputCsvRecord;
import com.reece.punchoutcustomerbff.dto.UploadInputDto;
import com.reece.punchoutcustomerbff.dto.UploadOutputDto;
import com.reece.punchoutcustomerbff.models.daos.AuthorizedUserDao;
import com.reece.punchoutcustomerbff.models.daos.CustomerDao;
import com.reece.punchoutcustomerbff.models.daos.ProductDao;
import com.reece.punchoutcustomerbff.models.daos.UploadDao;
import com.reece.punchoutcustomerbff.models.dtos.ProductToCsvRecordDto;
import com.reece.punchoutcustomerbff.models.repositories.CustomerRepository;
import com.reece.punchoutcustomerbff.models.repositories.ProductRepository;
import com.reece.punchoutcustomerbff.models.repositories.UploadRepository;
import com.reece.punchoutcustomerbff.util.DateGenerator;
import com.reece.punchoutcustomerbff.util.DateUtil;
import com.reece.punchoutcustomerbff.util.TestUtils;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UploadServiceTest {

    @Mock
    private UploadRepository uploadRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SecurityService securityService;

    @Mock
    private DateGenerator dateGenerator;

    @InjectMocks
    private UploadService subject;

    @Mock
    private Optional<CustomerDao> optionalCustomer;

    @Captor
    private ArgumentCaptor<UploadDao> uploadCaptor;

    @Test
    public void testDeleteUpload() {
        // given:
        String uploadId = "0e3a4ecc-6cfa-4821-8d80-b85a28f27dfa";

        // when:
        subject.deleteUpload(uploadId);

        // then
        verify(uploadRepository).deleteById(UUID.fromString(uploadId));
    }

    @Test
    public void testUpload() {
        // given: The input payload
        String content = "Product ID,Branch,Customer ID\n";
        content += "TEST-PART-04,TEST-BRANCH-01,TEST-01\n";
        content += "TEST-PART-05,TEST-BRANCH-01,TEST-01\n";
        content += "TEST-PART-06,TEST-BRANCH-01,TEST-01\n";

        UploadInputDto input = UploadInputDto
            .builder()
            .fileName("charlie.csv")
            .encoded(Base64.getEncoder().encodeToString(content.getBytes()))
            .build();
        String customerId = "b32a915d-cd68-4d24-997c-3cff04fe0162";

        // and: the given customer exists
        CustomerDao customer = TestUtils.generateCustomerDao();
        when(customerRepository.findById(UUID.fromString(customerId))).thenReturn(optionalCustomer);
        when(optionalCustomer.get()).thenReturn(customer);

        // and: that there is a single existing product
        List<String> list = List.of("TEST-PART-04", "TEST-PART-05", "TEST-PART-06");
        List<ProductDao> products = List.of(TestUtils.generateDefaultProduct());
        when(productRepository.findByPartNumbers(list)).thenReturn(products);

        // and: there is a currently logged in user
        AuthorizedUserDao user = TestUtils.generateDefaultUser();
        when(securityService.currentLoggedInUser()).thenReturn(user);

        // and: we mock the current date/time
        when(dateGenerator.generateTimestamp()).thenReturn(DateUtil.toTimestamp("2023-05-17T05:00:00.000+0000"));

        // when
        UploadOutputDto result = subject.upload(input, customerId);

        // then
        assertThat(result.getProducts().size(), equalTo(3));
        assertThat(result.getProducts().get(0).getPartNumber(), equalTo("TEST-PART-04"));
        assertThat(result.getProducts().get(1).getPartNumber(), equalTo("TEST-PART-05"));
        assertThat(result.getProducts().get(2).getPartNumber(), equalTo("TEST-PART-06"));

        // and: we capture the created upload record
        verify(uploadRepository).save(uploadCaptor.capture());
        UploadDao upload = uploadCaptor.getValue();
        assertThat(DateUtil.fromDate(upload.getUploadDatetime()), equalTo("2023-05-17T05:00:00.000+0000"));
        assertThat(upload.getId(), is(notNullValue()));
        assertThat(upload.getContent(), equalTo(content));
        assertThat(upload.getFileName(), equalTo("charlie.csv"));
        assertThat(upload.getCustomer(), equalTo(customer));
        assertThat(upload.getUser(), equalTo(user));
    }

    @Test
    public void testProcessInputCsv() {
        // given
        String content = "Product ID,Branch,Customer ID\n";
        content += "TEST-PART-04,TEST-BRANCH-01,TEST-01\n";
        content += "TEST-PART-05,TEST-BRANCH-02,TEST-02\n";
        content += "TEST-PART-06,TEST-BRANCH-03,TEST-03\n";

        // when
        List<InputCsvRecord> results = subject.processInputCsv(content);

        // then
        assertThat(results.size(), equalTo(3));

        // and
        InputCsvRecord one = results.get(0);
        assertThat(one.getProductId(), equalTo("TEST-PART-04"));
        assertThat(one.getBranch(), equalTo("TEST-BRANCH-01"));
        assertThat(one.getCustomerId(), equalTo("TEST-01"));

        InputCsvRecord two = results.get(1);
        assertThat(two.getProductId(), equalTo("TEST-PART-05"));
        assertThat(two.getBranch(), equalTo("TEST-BRANCH-02"));
        assertThat(two.getCustomerId(), equalTo("TEST-02"));

        InputCsvRecord three = results.get(2);
        assertThat(three.getProductId(), equalTo("TEST-PART-06"));
        assertThat(three.getBranch(), equalTo("TEST-BRANCH-03"));
        assertThat(three.getCustomerId(), equalTo("TEST-03"));
    }

    @Test
    public void testSplitInto() {
        // given
        CatalogProductDto catalogProductA = CatalogProductDto.builder().partNumber("alpha").build();
        CatalogProductDto catalogProductB = CatalogProductDto.builder().partNumber("bravo").build();
        CatalogProductDto catalogProductC = CatalogProductDto.builder().partNumber("charlie").build();
        CatalogProductDto catalogProductD = CatalogProductDto.builder().partNumber("delta").build();
        CatalogProductDto catalogProductE = CatalogProductDto.builder().partNumber("echo").build();
        ProductToCsvRecordDto inputA = ProductToCsvRecordDto.builder().catalogProduct(catalogProductA).build();
        ProductToCsvRecordDto inputB = ProductToCsvRecordDto.builder().catalogProduct(catalogProductB).build();
        ProductToCsvRecordDto inputC = ProductToCsvRecordDto.builder().catalogProduct(catalogProductC).build();
        ProductToCsvRecordDto inputD = ProductToCsvRecordDto.builder().catalogProduct(catalogProductD).build();
        ProductToCsvRecordDto inputE = ProductToCsvRecordDto.builder().catalogProduct(catalogProductE).build();
        List<ProductToCsvRecordDto> inputs = new ArrayList<>(Arrays.asList(inputA, inputB, inputC, inputD, inputE));

        // and
        int size = 2;

        // when
        List<List<ProductToCsvRecordDto>> results = subject.splitInto(inputs, size);

        // then: there are three total lists
        assertThat(results.size(), equalTo(3));

        // and: the first list is alpha and bravo
        assertThat(results.get(0).size(), equalTo(2));
        assertThat(results.get(0).get(0).getCatalogProduct().getPartNumber(), equalTo("alpha"));
        assertThat(results.get(0).get(1).getCatalogProduct().getPartNumber(), equalTo("bravo"));

        // and: the second list is charlie and delta
        assertThat(results.get(1).size(), equalTo(2));
        assertThat(results.get(1).get(0).getCatalogProduct().getPartNumber(), equalTo("charlie"));
        assertThat(results.get(1).get(1).getCatalogProduct().getPartNumber(), equalTo("delta"));

        // and: the third list is just echo
        assertThat(results.get(2).size(), equalTo(1));
        assertThat(results.get(2).get(0).getCatalogProduct().getPartNumber(), equalTo("echo"));
    }
}
