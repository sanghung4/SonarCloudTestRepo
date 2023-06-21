package com.reece.platform.products.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.reece.platform.products.TestUtils;
import com.reece.platform.products.branches.model.DTO.BranchResponseDTO;
import com.reece.platform.products.branches.service.BranchesService;
import com.reece.platform.products.exceptions.InvoiceDateRangeException;
import com.reece.platform.products.exceptions.OrderNotFoundException;
import com.reece.platform.products.external.appsearch.model.PageRequest;
import com.reece.platform.products.external.mincron.MincronServiceClient;
import com.reece.platform.products.external.mincron.model.ContractHeader;
import com.reece.platform.products.external.mincron.model.OrderHeader;
import com.reece.platform.products.external.mincron.model.ProductLineItem;
import com.reece.platform.products.model.DTO.GetInvoiceResponseDTO;
import com.reece.platform.products.model.DTO.InvoiceDTO;
import com.reece.platform.products.model.DTO.MincronSingleInvoiceDTO;
import com.reece.platform.products.model.DTO.ProductDTO;
import com.reece.platform.products.model.ErpEnum;
import com.reece.platform.products.model.ImageUrls;
import com.reece.platform.products.model.MincronAgingEnum;
import com.reece.platform.products.model.MincronOrderStatus;
import com.reece.platform.products.model.eclipse.common.EclipseAddressResponseDTO;
import com.reece.platform.products.search.SearchService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

class InvoiceServiceTest {

    @Mock
    private ErpService erpService;

    @Mock
    private BranchesService branchesService;

    @Mock
    private MincronServiceClient mincronServiceClient;

    @Mock
    private SearchService searchService;

    private InvoiceService invoiceService;

    private String clientGuid;
    private static final Double CURRENT_BALANCE = 5.5d;
    private static final Double FUTURE_BALANCE = 6.6d;
    private static final Double DAYS_30_BALANCE = 7.7d;
    private static final Double DAYS_60_BALANCE = 8.8d;
    private static final Double DAYS_90_BALANCE = 9.9d;
    private static final Double DAYS_120_BALANCE = 10.10d;
    private static final String INVOICE_NUMBER = "invoiceNumber";
    private static final String CONTRACT_NUMBER = "123";
    private static final String ACCOUNT_ID = "accountId";

    private static final String PRODUCT_DESCRIPTION_1 = "product description 1";
    private static final String PRODUCT_NUMBER_1 = "product number 1";
    private static final String PRODUCT_PRICE_1 = "11.10";
    private static final String QUANTITY_ORDERED_1 = "11";
    private static final String QUANTITY_SHIPPED_1 = "11";
    private static final String PRODUCT_MANUFACTURER_NAME_1 = "product mfr name 1";
    private static final String PRODUCT_MANUFACTURER_NUMBER_1 = "product mfr number 1";
    private static final String PRODUCT_THUMB_1 = "thumb1";
    private static final String PRODUCT_DESCRIPTION_2 = "product description 2";
    private static final String PRODUCT_NUMBER_2 = "product number 2";
    private static final String PRODUCT_PRICE_2 = "12.20";
    private static final String QUANTITY_ORDERED_2 = "12";
    private static final String QUANTITY_SHIPPED_2 = "12";
    private static final String PRODUCT_MANUFACTURER_NAME_2 = "product mfr name 2";
    private static final String PRODUCT_MANUFACTURER_NUMBER_2 = "product mfr number 2";
    private static final String PRODUCT_THUMB_2 = "thumb2";
    private static final String DISPLAY_ONLY_N = "N";
    private static final String DISPLAY_ONLY_Y = "Y";

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        clientGuid = "bacfdd9b-649f-4be7-8a6a-7e7ea2837c89&";
        invoiceService = new InvoiceService(erpService, mincronServiceClient, branchesService, searchService);
        ReflectionTestUtils.setField(
            invoiceService,
            "billtrustBaseUrl",
            "http://imweb.billtrust.com/xmlwebservices/webServiceDispatch.php"
        );
        ReflectionTestUtils.setField(invoiceService, "billtrustEncryptionKey", "A5D2B3C54F321D3A315DF41D");
        ReflectionTestUtils.setField(invoiceService, "billtrustClientGuid", clientGuid);
    }

    @Test
    void getInvoices_success_eclipse() throws InvoiceDateRangeException {
        String accountId = "555";
        String erpName = ErpEnum.ECLIPSE.name();
        GetInvoiceResponseDTO invoiceResponseDTO = new GetInvoiceResponseDTO();
        List<InvoiceDTO> invoiceDTOS = new ArrayList<>();
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setInvoiceNumber("test");
        invoiceDTOS.add(invoiceDTO);
        invoiceResponseDTO.setInvoices(invoiceDTOS);
        when(erpService.getEclipseInvoices(accountId, null, null, null, null)).thenReturn(invoiceResponseDTO);

        GetInvoiceResponseDTO getInvoiceResponseDTO = invoiceService.getInvoices(
            accountId,
            erpName,
            null,
            null,
            null,
            null
        );
        assertEquals(
            invoiceResponseDTO,
            getInvoiceResponseDTO,
            "Expected invoice to return from mocked erp service layer"
        );
        invoiceResponseDTO.getInvoices().stream().forEach(invoice -> assertNotNull(invoice.getInvoiceUrl()));
    }

    @Test
    void getInvoices_failure_startDateEarly() throws InvoiceDateRangeException {
        String accountId = "555";
        String erpName = ErpEnum.ECLIPSE.name();
        Date startDate = new Date(112, 11, 8);

        InvoiceDateRangeException exception = assertThrows(
            InvoiceDateRangeException.class,
            () -> invoiceService.getInvoices(accountId, erpName, null, startDate, null, null)
        );
        assertTrue(exception.getMessage().contains("Start date exceeds 14 months"));
    }

    @Test
    void getInvoices_failure_endBeforeBeg() throws InvoiceDateRangeException {
        String accountId = "555";
        String erpName = ErpEnum.ECLIPSE.name();
        Date startDate = new Date(122, 11, 8);
        Date endDate = new Date(121, 11, 8);

        InvoiceDateRangeException exception = assertThrows(
            InvoiceDateRangeException.class,
            () -> invoiceService.getInvoices(accountId, erpName, null, startDate, endDate, null)
        );
        assertTrue(exception.getMessage().contains("End date must be after start date"));
    }

    @Test
    void getInvoices_failure_dateRangeExcess() throws InvoiceDateRangeException {
        String accountId = "555";
        String erpName = ErpEnum.ECLIPSE.name();
        Date startDate = new Date(122, 11, 8);
        Date endDate = new Date(125, 11, 8);

        InvoiceDateRangeException exception = assertThrows(
            InvoiceDateRangeException.class,
            () -> invoiceService.getInvoices(accountId, erpName, null, startDate, endDate, null)
        );
        assertTrue(exception.getMessage().contains("Date range exceeds 14 months"));
    }

    @Test
    void getInvoices_success_mincron() throws InvoiceDateRangeException {
        String accountId = "555";
        String erpName = ErpEnum.MINCRON.name();
        List<InvoiceDTO> invoiceDTOS = buildMockInvoiceDtos();
        when(erpService.getMincronInvoices(accountId)).thenReturn(invoiceDTOS);

        GetInvoiceResponseDTO getInvoiceResponseDTO = invoiceService.getInvoices(
            accountId,
            erpName,
            null,
            null,
            null,
            null
        );

        assertEquals(
            getInvoiceResponseDTO.getInvoices(),
            invoiceDTOS,
            "Expected list of invoices to equal mocked ERP response of invoices."
        );
        assertEquals(
            getInvoiceResponseDTO.getBucketFuture(),
            FUTURE_BALANCE,
            "Expected aggregated bucket data to equal calculated invoice totals"
        );
        assertEquals(
            getInvoiceResponseDTO.getCurrentAmt(),
            CURRENT_BALANCE,
            "Expected aggregated bucket data to equal calculated invoice totals"
        );
        assertEquals(
            getInvoiceResponseDTO.getBucketThirty(),
            DAYS_30_BALANCE,
            "Expected aggregated bucket data to equal calculated invoice totals"
        );
        assertEquals(
            getInvoiceResponseDTO.getBucketSixty(),
            DAYS_60_BALANCE,
            "Expected aggregated bucket data to equal calculated invoice totals"
        );
        assertEquals(
            getInvoiceResponseDTO.getBucketNinety(),
            DAYS_90_BALANCE,
            "Expected aggregated bucket data to equal calculated invoice totals"
        );
        assertEquals(
            getInvoiceResponseDTO.getBucketOneTwenty(),
            DAYS_120_BALANCE,
            "Expected aggregated bucket data to equal calculated invoice totals"
        );
        assertEquals(
            round(getInvoiceResponseDTO.getTotalAmt(), 2),
            round(
                FUTURE_BALANCE +
                CURRENT_BALANCE +
                DAYS_30_BALANCE +
                DAYS_90_BALANCE +
                DAYS_120_BALANCE +
                DAYS_60_BALANCE,
                2
            ),
            "Expected aggregated bucket data to equal calculated invoice totals"
        );
        assertEquals(
            round(getInvoiceResponseDTO.getTotalPastDue(), 2),
            round(DAYS_30_BALANCE + DAYS_90_BALANCE + DAYS_120_BALANCE + DAYS_60_BALANCE, 2),
            "Expected aggregated bucket data to equal calculated invoice totals"
        );
    }

    @Test
    void getInvoicesPdf_success() {
        String result = invoiceService.getInvoicesPdfUrl(
            "606800",
            Arrays.asList("S110425446.001", "S110426036.001", "S110419487.001")
        );
        assertEquals(
            result,
            "http://imweb.billtrust.com/xmlwebservices/webServiceDispatch.php?custnbr=bacfdd9b-649f-4be7-8a6a-7e7ea2837c89&&p=HQzpOGev8EkEwsIH5HfKKcsfhSZ0j0RBA3UEMypIGRIK8t8CmUR0ZsRTseny7SgCELxGBu4tJgYFcvxNCI1f8E0hy0+uqc6AExgmzmudjqzxA2yDUa5v3kkviiOnj2AwehdniXvkXCdOHF5HJp1b6Bp0NkoIQxWKwM4i0Rk9pPXHel9LOvl1zJ7I2uyX0Fl+fRa7GgrZ2xHu4RM9R0xRlg=="
        );
    }

    @Test
    void getMincronInvoice_singleInvoiceDataSuccess() {
        val testOrderHeader = TestUtils.loadResponseJson("get-invoice-header-success-response.json", OrderHeader.class);
        when(mincronServiceClient.getOrderHeader(INVOICE_NUMBER, MincronOrderStatus.INVOICED.getOrderType()))
            .thenReturn(Optional.of(testOrderHeader));
        when(
            mincronServiceClient.getOrderItemList(
                ACCOUNT_ID,
                MincronOrderStatus.INVOICED.getOrderType(),
                INVOICE_NUMBER
            )
        )
            .thenReturn(generateInvoiceItems());
        when(searchService.getProductsByCustomerPartNumber(eq("mincron-ecomm-products"), any(), any()))
            .thenReturn(generateElasticItems());
        when(erpService.getMincronInvoices(ACCOUNT_ID)).thenReturn(buildMockInvoiceDtos());

        MincronSingleInvoiceDTO mincronSingleInvoiceDTO = invoiceService.getMincronInvoice(ACCOUNT_ID, INVOICE_NUMBER);
        assertEquals(INVOICE_NUMBER, mincronSingleInvoiceDTO.getInvoiceNumber());
        assertEquals(testOrderHeader.getOrderStatus(), mincronSingleInvoiceDTO.getStatus());
        assertEquals(testOrderHeader.getPurchaseOrderNumber(), mincronSingleInvoiceDTO.getCustomerPo());
        assertEquals(testOrderHeader.getInvoiceDate(), mincronSingleInvoiceDTO.getInvoiceDate());
        assertEquals(testOrderHeader.getDueDate(), mincronSingleInvoiceDTO.getDueDate());
        assertEquals(testOrderHeader.getJobName(), mincronSingleInvoiceDTO.getJobName());
        assertEquals(mincronSingleInvoiceDTO.getSubtotal(), String.valueOf(testOrderHeader.getSubTotal()));
        assertEquals(mincronSingleInvoiceDTO.getTax(), String.valueOf(testOrderHeader.getTaxAmount()));
        assertEquals(mincronSingleInvoiceDTO.getOtherCharges(), String.valueOf(testOrderHeader.getOtherCharges()));
        assertEquals(
            mincronSingleInvoiceDTO.getPaidToDate(),
            String.format("%.2f", testOrderHeader.getTotalAmount() - CURRENT_BALANCE)
        );
        assertEquals(mincronSingleInvoiceDTO.getOpenBalance(), String.format("%.2f", CURRENT_BALANCE));
        assertEquals(mincronSingleInvoiceDTO.getInvoiceItems().size(), 2);

        MincronSingleInvoiceDTO.InvoiceProduct invoiceProduct1 = mincronSingleInvoiceDTO.getInvoiceItems().get(0);
        MincronSingleInvoiceDTO.InvoiceProduct invoiceProduct2 = mincronSingleInvoiceDTO.getInvoiceItems().get(1);

        assertEquals(invoiceProduct1.getId(), PRODUCT_NUMBER_1);
        assertEquals(invoiceProduct1.getBrand(), PRODUCT_MANUFACTURER_NAME_1);
        assertEquals(invoiceProduct1.getName(), PRODUCT_DESCRIPTION_1);
        assertEquals(invoiceProduct1.getPartNumber(), PRODUCT_NUMBER_1);
        assertEquals(invoiceProduct1.getMfr(), PRODUCT_MANUFACTURER_NUMBER_1);
        assertEquals(invoiceProduct1.getThumb(), PRODUCT_THUMB_1);
        assertEquals(invoiceProduct1.getPrice(), PRODUCT_PRICE_1);
        assertEquals(invoiceProduct1.getQty().getQuantityOrdered(), Integer.parseInt(QUANTITY_ORDERED_1));
        assertEquals(invoiceProduct1.getQty().getQuantityShipped(), Integer.parseInt(QUANTITY_SHIPPED_1));

        assertEquals(invoiceProduct2.getId(), PRODUCT_NUMBER_2);
        assertEquals(invoiceProduct2.getBrand(), PRODUCT_MANUFACTURER_NAME_2);
        assertEquals(invoiceProduct2.getName(), PRODUCT_DESCRIPTION_2);
        assertEquals(invoiceProduct2.getPartNumber(), PRODUCT_NUMBER_2);
        assertEquals(invoiceProduct2.getMfr(), PRODUCT_MANUFACTURER_NUMBER_2);
        assertEquals(invoiceProduct2.getThumb(), PRODUCT_THUMB_2);
        assertEquals(invoiceProduct2.getPrice(), PRODUCT_PRICE_2);
        assertEquals(invoiceProduct2.getQty().getQuantityOrdered(), Integer.parseInt(QUANTITY_ORDERED_2));
        assertEquals(invoiceProduct2.getQty().getQuantityShipped(), Integer.parseInt(QUANTITY_SHIPPED_2));
    }

    @Test
    void getMincronInvoice_isUsingHomeBranchWhenPickUp() {
        val testOrderHeader = TestUtils.loadResponseJson("get-invoice-header-success-response.json", OrderHeader.class);
        testOrderHeader.setDelivery(false);
        val testBranch = buildTestBranch(String.valueOf(testOrderHeader.getBranchNumber()));
        when(mincronServiceClient.getOrderHeader(INVOICE_NUMBER, MincronOrderStatus.INVOICED.getOrderType()))
            .thenReturn(Optional.of(testOrderHeader));
        when(erpService.getMincronInvoices(ACCOUNT_ID)).thenReturn(buildMockInvoiceDtos());
        when(
            branchesService.getBranchByEntityId(
                String.valueOf(testOrderHeader.getBranchNumber()),
                ErpEnum.MINCRON.name()
            )
        )
            .thenReturn(testBranch);

        MincronSingleInvoiceDTO mincronSingleInvoiceDTO = invoiceService.getMincronInvoice(ACCOUNT_ID, INVOICE_NUMBER);
        assertEquals(testBranch.getAddress1(), mincronSingleInvoiceDTO.getAddress().getStreetLineOne());
        assertEquals(testBranch.getCity(), mincronSingleInvoiceDTO.getAddress().getCity());
        assertEquals(testBranch.getState(), mincronSingleInvoiceDTO.getAddress().getState());
        assertEquals(testBranch.getZip(), mincronSingleInvoiceDTO.getAddress().getPostalCode());
    }

    @Test
    void getMincronInvoice_NoInvoiceWithOpenBalance() {
        val testOrderHeader = TestUtils.loadResponseJson("get-invoice-header-success-response.json", OrderHeader.class);
        when(mincronServiceClient.getOrderHeader(INVOICE_NUMBER, MincronOrderStatus.INVOICED.getOrderType()))
            .thenReturn(Optional.of(testOrderHeader));
        when(erpService.getMincronInvoices(ACCOUNT_ID)).thenReturn(List.of());
        assertThrows(OrderNotFoundException.class, () -> invoiceService.getMincronInvoice(ACCOUNT_ID, INVOICE_NUMBER));
    }

    @Test
    void getMincronInvoice_NoOrderHeader() {
        when(mincronServiceClient.getOrderHeader(INVOICE_NUMBER, MincronOrderStatus.INVOICED.getOrderType()))
            .thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> invoiceService.getMincronInvoice(ACCOUNT_ID, INVOICE_NUMBER));
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private List<InvoiceDTO> buildMockInvoiceDtos() {
        InvoiceDTO invoiceDTO1 = new InvoiceDTO();
        invoiceDTO1.setInvoiceNumber(INVOICE_NUMBER);
        invoiceDTO1.setAge(MincronAgingEnum.CURRENT.getAgingCode());
        invoiceDTO1.setOpenBalance(CURRENT_BALANCE);
        invoiceDTO1.setContractNumber(CONTRACT_NUMBER);

        InvoiceDTO invoiceDTO2 = new InvoiceDTO();
        invoiceDTO2.setAge(MincronAgingEnum.FUTURE.getAgingCode());
        invoiceDTO2.setOpenBalance(FUTURE_BALANCE);
        invoiceDTO2.setContractNumber(CONTRACT_NUMBER);

        InvoiceDTO invoiceDTO3 = new InvoiceDTO();
        invoiceDTO3.setAge(MincronAgingEnum.DAYS_30.getAgingCode());
        invoiceDTO3.setOpenBalance(DAYS_30_BALANCE);
        invoiceDTO3.setContractNumber(CONTRACT_NUMBER);

        InvoiceDTO invoiceDTO4 = new InvoiceDTO();
        invoiceDTO4.setAge(MincronAgingEnum.DAYS_60.getAgingCode());
        invoiceDTO4.setOpenBalance(DAYS_60_BALANCE);
        invoiceDTO4.setContractNumber(CONTRACT_NUMBER);

        InvoiceDTO invoiceDTO5 = new InvoiceDTO();
        invoiceDTO5.setAge(MincronAgingEnum.DAYS_90.getAgingCode());
        invoiceDTO5.setOpenBalance(DAYS_90_BALANCE);
        invoiceDTO5.setContractNumber(CONTRACT_NUMBER);

        InvoiceDTO invoiceDTO6 = new InvoiceDTO();
        invoiceDTO6.setAge(MincronAgingEnum.DAYS_120.getAgingCode());
        invoiceDTO6.setOpenBalance(DAYS_120_BALANCE);
        invoiceDTO6.setContractNumber(CONTRACT_NUMBER);

        return Arrays.asList(invoiceDTO1, invoiceDTO2, invoiceDTO3, invoiceDTO4, invoiceDTO5, invoiceDTO6);
    }

    private BranchResponseDTO buildTestBranch(String branchNumber) {
        val branch = new BranchResponseDTO();
        branch.setBranchId(branchNumber);
        branch.setAddress1("123 Main St");
        branch.setCity("Dallas");
        branch.setState("TX");
        branch.setZip("75201");
        return branch;
    }

    private List<ProductLineItem> generateInvoiceItems() {
        ProductLineItem productLineItem1 = new ProductLineItem();
        productLineItem1.setDescription(PRODUCT_DESCRIPTION_1);
        productLineItem1.setProductNumber(PRODUCT_NUMBER_1);
        productLineItem1.setUnitPrice(PRODUCT_PRICE_1);
        productLineItem1.setQuantityOrdered(QUANTITY_ORDERED_1);
        productLineItem1.setQuantityShipped(QUANTITY_SHIPPED_1);
        productLineItem1.setDisplayOnly(DISPLAY_ONLY_N);

        ProductLineItem productLineItem2 = new ProductLineItem();
        productLineItem2.setDescription(PRODUCT_DESCRIPTION_2);
        productLineItem2.setProductNumber(PRODUCT_NUMBER_2);
        productLineItem2.setUnitPrice(PRODUCT_PRICE_2);
        productLineItem2.setQuantityOrdered(QUANTITY_ORDERED_2);
        productLineItem2.setQuantityShipped(QUANTITY_SHIPPED_2);
        productLineItem2.setDisplayOnly(DISPLAY_ONLY_N);

        ProductLineItem productLineItem3 = new ProductLineItem();
        productLineItem3.setDisplayOnly(DISPLAY_ONLY_Y);

        return Arrays.asList(productLineItem1, productLineItem2, productLineItem3);
    }

    private List<ProductDTO> generateElasticItems() {
        ProductDTO productDTO1 = new ProductDTO();
        productDTO1.setId(PRODUCT_NUMBER_1);
        productDTO1.setCustomerPartNumber(Arrays.asList(PRODUCT_NUMBER_1));
        productDTO1.setManufacturerName(PRODUCT_MANUFACTURER_NAME_1);
        productDTO1.setManufacturerNumber(PRODUCT_MANUFACTURER_NUMBER_1);
        productDTO1.setName(PRODUCT_DESCRIPTION_1);

        ImageUrls imageUrls1 = new ImageUrls();
        imageUrls1.setThumb(PRODUCT_THUMB_1);
        productDTO1.setImageUrls(imageUrls1);

        ProductDTO productDTO2 = new ProductDTO();
        productDTO2.setId(PRODUCT_NUMBER_2);
        productDTO2.setCustomerPartNumber(Arrays.asList(PRODUCT_NUMBER_2));
        productDTO2.setManufacturerName(PRODUCT_MANUFACTURER_NAME_2);
        productDTO2.setManufacturerNumber(PRODUCT_MANUFACTURER_NUMBER_2);
        productDTO2.setName(PRODUCT_DESCRIPTION_2);

        ImageUrls imageUrls2 = new ImageUrls();
        imageUrls2.setThumb(PRODUCT_THUMB_2);
        productDTO2.setImageUrls(imageUrls2);

        return Arrays.asList(productDTO1, productDTO2);
    }
}
