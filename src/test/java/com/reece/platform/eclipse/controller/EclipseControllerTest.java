package com.reece.platform.eclipse.controller;

import static com.reece.platform.eclipse.testConstants.TestConstants.CreateContactRequest;
import static com.reece.platform.eclipse.testConstants.TestConstants.CreateUpdateContactRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.reece.platform.eclipse.EclipseServiceApplication;
import com.reece.platform.eclipse.exceptions.EclipseException;
import com.reece.platform.eclipse.exceptions.EclipseTokenException;
import com.reece.platform.eclipse.exceptions.InvalidIdException;
import com.reece.platform.eclipse.model.DTO.*;
import com.reece.platform.eclipse.model.DTO.kourier.ProductSearchResponseDTO;
import com.reece.platform.eclipse.model.XML.ElementAccount.ElementSetupQueryResponse;
import com.reece.platform.eclipse.model.XML.ElementAccount.ElementSetupQueryResponseWrapper;
import com.reece.platform.eclipse.model.XML.EntityResponse.EntityResponse;
import com.reece.platform.eclipse.model.XML.EntitySearchResponse.EntitySearchResponseTypes;
import com.reece.platform.eclipse.model.XML.EntitySearchResponse.EntitySearchResult;
import com.reece.platform.eclipse.model.XML.MassSalesOrderInquiryResponse.MassSalesOrderInquiryResponse;
import com.reece.platform.eclipse.model.XML.MassSalesOrderInquiryResponse.MassSalesOrderResponse;
import com.reece.platform.eclipse.model.XML.MassSalesOrderInquiryResponse.SalesOrderList;
import com.reece.platform.eclipse.model.XML.ProductResponse.ProductResponse;
import com.reece.platform.eclipse.model.XML.ReorderPadInquiryResponse.ReorderPadInquiryResponse;
import com.reece.platform.eclipse.model.XML.SalesOrder.OrderHeader;
import com.reece.platform.eclipse.model.XML.SalesOrderResponse.SalesOrder;
import com.reece.platform.eclipse.model.XML.common.OrderStatus;
import com.reece.platform.eclipse.model.XML.common.OrderTotals;
import com.reece.platform.eclipse.model.XML.common.StatusResult;
import com.reece.platform.eclipse.service.EclipseService.AsyncExecutionsService;
import com.reece.platform.eclipse.service.EclipseService.EclipseService;
import com.reece.platform.eclipse.service.EclipseService.EclipseSessionService;
import com.reece.platform.eclipse.service.EclipseService.FileTransferService;
import com.reece.platform.eclipse.service.Kourier.KourierService;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest(classes = { EclipseServiceApplication.class, GlobalExceptionHandler.class })
@AutoConfigureMockMvc
@PropertySource("classpath:application.properties")
public class EclipseControllerTest {

    @MockBean
    private EclipseService eclipseService;

    @MockBean
    private EclipseSessionService eclipseSessionService;

    @MockBean
    private FileTransferService fileTransferService;

    @MockBean
    private AsyncExecutionsService asyncExecutionsService;

    @MockBean
    private KourierService kourierService;

    @Autowired
    private EclipseController eclipseController;

    private static final String TEST_ACCOUNT_ID = "555";

    @Autowired
    private MockMvc mockMvc;

    private CreateContactRequestDTO createContactRequestDTO = CreateContactRequest();
    private UpdateContactRequestDTO updateContactRequestDTO = CreateUpdateContactRequest();

    @BeforeEach
    public void setup() throws Exception {
        eclipseController = new EclipseController(eclipseService, fileTransferService);
    }

    @Test
    public void deleteContact_success() throws Exception {
        when(eclipseService.deleteContact(any())).thenReturn("");
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        MvcResult result =
            this.mockMvc.perform(
                    delete("/accounts/{accountId}/user/{userId}", "1", "1").contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getOrder_success() throws Exception {
        GetOrderResponseDTO getOrderResponseDTO = new GetOrderResponseDTO();
        when(eclipseService.getOrderById(anyString(), anyString(), anyString())).thenReturn(getOrderResponseDTO);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("invoiceNumber", "1");
        MvcResult result =
            this.mockMvc.perform(
                    get("/accounts/{accountId}/orders/{orderId}", "1", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .params(requestParams)
                )
                .andExpect(status().isOk())
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        assertDoesNotThrow(() ->
            objectMapper.readValue(result.getResponse().getContentAsString(), GetOrderResponseDTO.class)
        );
    }

    @Test
    public void createJob_success() throws Exception {
        String createJobResponse = "successful file upload";
        JobFormDTO jobFormDTO = new JobFormDTO();
        jobFormDTO.setBonding(new BondingDTO());
        ObjectMapper objectMapper = new ObjectMapper();
        when(fileTransferService.uploadJobForm(jobFormDTO)).thenReturn(createJobResponse);
        MvcResult result =
            this.mockMvc.perform(
                    post("/accounts/job/")
                        .content(objectMapper.writeValueAsString(jobFormDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(createJobResponse, result.getResponse().getContentAsString());
    }

    @Test
    public void createJob_SftpException() throws Exception {
        JobFormDTO jobFormDTO = new JobFormDTO();
        jobFormDTO.setBonding(new BondingDTO());
        ObjectMapper objectMapper = new ObjectMapper();
        when(fileTransferService.uploadJobForm(jobFormDTO)).thenThrow(new SftpException(0, ""));
        this.mockMvc.perform(
                post("/accounts/job/")
                    .content(objectMapper.writeValueAsString(jobFormDTO))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    public void createJob_JSchException() throws Exception {
        JobFormDTO jobFormDTO = new JobFormDTO();
        jobFormDTO.setBonding(new BondingDTO());
        ObjectMapper objectMapper = new ObjectMapper();
        when(fileTransferService.uploadJobForm(jobFormDTO)).thenThrow(new JSchException());
        this.mockMvc.perform(
                post("/accounts/job/")
                    .content(objectMapper.writeValueAsString(jobFormDTO))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    public void createJob_IOException() throws Exception {
        JobFormDTO jobFormDTO = new JobFormDTO();
        jobFormDTO.setBonding(new BondingDTO());
        ObjectMapper objectMapper = new ObjectMapper();
        when(fileTransferService.uploadJobForm(jobFormDTO)).thenThrow(new IOException());
        this.mockMvc.perform(
                post("/accounts/job/")
                    .content(objectMapper.writeValueAsString(jobFormDTO))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    public void getQuote_success() throws Exception {
        GetOrderResponseDTO getOrderResponseDTO = new GetOrderResponseDTO();
        when(eclipseService.getQuoteById(anyString(), anyString())).thenReturn(getOrderResponseDTO);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        MvcResult result =
            this.mockMvc.perform(
                    get("/accounts/{accountId}/quotes/{quoteId}", "1", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .params(requestParams)
                )
                .andExpect(status().isOk())
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        assertDoesNotThrow(() ->
            objectMapper.readValue(result.getResponse().getContentAsString(), GetOrderResponseDTO.class)
        );
    }

    @Test
    public void getOrder_failure() throws Exception {
        when(eclipseService.getOrderById(anyString(), anyString(), anyString())).thenThrow(new EclipseException());
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("invoiceNumber", "1");
        MvcResult result =
            this.mockMvc.perform(get("/accounts/{accountId}/orders/{orderId}", "1", "1").params(requestParams))
                .andExpect(status().isNotFound())
                .andReturn();
        assert (result.getResponse().getContentAsString().equals("Eclipse encountered an issue"));
    }

    @Test
    public void getQuote_failure() throws Exception {
        when(eclipseService.getQuoteById(anyString(), anyString())).thenThrow(new EclipseException());
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("invoiceNumber", "1");
        MvcResult result =
            this.mockMvc.perform(get("/accounts/{accountId}/quotes/{quoteId}", "1", "1").params(requestParams))
                .andExpect(status().isNotFound())
                .andReturn();
        assert (result.getResponse().getContentAsString().equals("Eclipse encountered an issue"));
    }

    @Test
    public void getOrders_success() throws Exception {
        List<GetOrderResponseDTO> getOrderResponseDTOList = new ArrayList<>();
        getOrderResponseDTOList.add(new GetOrderResponseDTO());
        when(eclipseService.getOrdersByAccountId(anyString(), anyString(), anyString()))
            .thenReturn(getOrderResponseDTOList);
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("orderPostStartDate", "11/10/09");
        requestParams.add("orderPostEndDate", "11/11/09");
        MvcResult result =
            this.mockMvc.perform(
                    get("/accounts/{accountId}/orders", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .params(requestParams)
                )
                .andExpect(status().isOk())
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        assertDoesNotThrow(() ->
            objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<List<GetOrderResponseDTO>>() {}
            )
        );
    }

    @Test
    public void getOrders_failure() throws Exception {
        when(eclipseService.getOrdersByAccountId(anyString(), anyString(), anyString()))
            .thenThrow(new EclipseException());
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("orderPostStartDate", "11/10/09");
        requestParams.add("orderPostEndDate", "11/11/09");
        MvcResult result =
            this.mockMvc.perform(get("/accounts/{accountId}/orders", "123").params(requestParams))
                .andExpect(status().isNotFound())
                .andReturn();
        assert (result.getResponse().getContentAsString().equals("Eclipse encountered an issue"));
    }

    @Test
    public void getEclipseProduct_success_no_user_info() throws Exception {
        when(eclipseService.getProductById(anyString(), any(ErpUserInformationDTO.class), eq(false)))
            .thenReturn(new ProductResponse());
        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult result =
            this.mockMvc.perform(
                    post("/product/{productId}", 1)
                        .content(objectMapper.writeValueAsString(new ErpUserInformationDTO()))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        assertDoesNotThrow(() ->
            objectMapper.readValue(result.getResponse().getContentAsString(), ProductResponse.class)
        );
    }

    @Test
    public void getEclipseProduct_success_with_user_info() throws Exception {
        when(eclipseService.getProductById(anyString(), any(ErpUserInformationDTO.class), eq(false)))
            .thenReturn(new ProductResponse());
        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult result =
            this.mockMvc.perform(
                    post("/product/{productId}", 1)
                        .content(objectMapper.writeValueAsString(new ErpUserInformationDTO()))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        assertDoesNotThrow(() ->
            objectMapper.readValue(result.getResponse().getContentAsString(), ProductResponse.class)
        );
    }

    @Test
    public void getEclipseProduct_failure() throws Exception {
        when(eclipseService.getProductById(anyString(), any(ErpUserInformationDTO.class), eq(false)))
            .thenThrow(new EclipseException());

        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult result =
            this.mockMvc.perform(
                    post("/product/{productId}", 2)
                        .content(objectMapper.writeValueAsString(new ErpUserInformationDTO()))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn();
        assert (result.getResponse().getContentAsString().equals("Eclipse encountered an issue"));
    }

    @Test
    public void getEclipseAccount_success() throws Exception {
        when(eclipseService.getAccountById(anyString(), anyBoolean(), anyBoolean()))
            .thenReturn(new GetAccountResponseDTO());

        MvcResult result =
            this.mockMvc.perform(get("/accounts/{accountId}", "1")).andExpect(status().isOk()).andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        assertDoesNotThrow(() ->
            objectMapper.readValue(result.getResponse().getContentAsString(), GetAccountResponseDTO.class)
        );
    }

    @Test
    public void getEclipseAccount_withBillTo_success() throws Exception {
        when(eclipseService.getAccountById(anyString(), anyBoolean(), anyBoolean()))
            .thenReturn(new GetAccountResponseDTO());

        MvcResult result =
            this.mockMvc.perform(get("/accounts/{accountId}?retrieveBillTo=true", "1"))
                .andExpect(status().isOk())
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        assertDoesNotThrow(() ->
            objectMapper.readValue(result.getResponse().getContentAsString(), GetAccountResponseDTO.class)
        );
    }

    @Test
    public void getEclipseAccount_withShipToList_success() throws Exception {
        GetAccountResponseDTO dto2 = new GetAccountResponseDTO(new EntityResponse(), null);
        GetAccountResponseDTO dto3 = new GetAccountResponseDTO(new EntityResponse(), null);
        List<GetAccountResponseDTO> shipToAccounts = new ArrayList<>();
        shipToAccounts.add(dto2);
        shipToAccounts.add(dto3);
        GetAccountResponseDTO dto1 = new GetAccountResponseDTO(new EntityResponse(), shipToAccounts);
        dto1.setShipToAccounts(shipToAccounts);

        when(eclipseService.getAccountById(eq("1"), anyBoolean(), anyBoolean())).thenReturn(dto1);

        MvcResult result =
            this.mockMvc.perform(get("/accounts/{accountId}?retrieveShipToList=true", "1"))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        assertDoesNotThrow(() ->
            objectMapper.readValue(result.getResponse().getContentAsString(), GetAccountResponseDTO.class)
        );
    }

    @Test
    public void getEclipseAccount_failure() throws Exception {
        when(eclipseService.getAccountById(anyString(), anyBoolean(), anyBoolean())).thenThrow(new EclipseException());

        MvcResult result =
            this.mockMvc.perform(get("/accounts/{accountId}", 2)).andExpect(status().isNotFound()).andReturn();
        assert (result.getResponse().getContentAsString().equals("Eclipse encountered an issue"));
    }

    @Test
    public void getContact_success() throws Exception {
        GetContactResponseDTO dto = new GetContactResponseDTO();
        when(eclipseService.getContactById(anyString(), anyString())).thenReturn(dto);

        MvcResult result =
            this.mockMvc.perform(get("/accounts/{accountId}/user/{userId}", "1", "2"))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        assertDoesNotThrow(() ->
            objectMapper.readValue(result.getResponse().getContentAsString(), GetContactResponseDTO.class)
        );
    }

    @Test
    public void getContactAPI_success() throws Exception {
        when(eclipseService.getContact(any())).thenReturn("");
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        MvcResult result =
            this.mockMvc.perform(get("/contacts/{userId}", "1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getContact_failure() throws Exception {
        when(eclipseService.getContactById(anyString(), anyString())).thenThrow(new EclipseException());

        MvcResult result =
            this.mockMvc.perform(get("/accounts/{accountId}/user/{userId}", "1", "2"))
                .andExpect(status().isNotFound())
                .andReturn();
        assert (result.getResponse().getContentAsString().equals("Eclipse encountered an issue"));
    }

    @Test
    public void createContact_success() throws Exception {
        when(eclipseService.createContact(anyString(), any(CreateContactRequestDTO.class)))
            .thenReturn(new CreateContactResponseDTO());

        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult result =
            this.mockMvc.perform(
                    post("/accounts/{accountId}/user", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createContactRequestDTO))
                )
                .andExpect(status().isOk())
                .andReturn();
        assertDoesNotThrow(() ->
            objectMapper.readValue(result.getResponse().getContentAsString(), CreateContactResponseDTO.class)
        );
    }

    @Test
    public void createContact_failure() throws Exception {
        when(eclipseService.createContact(anyString(), any(CreateContactRequestDTO.class)))
            .thenThrow(new EclipseException());

        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult result =
            this.mockMvc.perform(
                    post("/accounts/{accountId}/user", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createContactRequestDTO))
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void updateContact_success() throws Exception {
        when(eclipseService.updateContact(anyString(), anyString(), any(UpdateContactRequestDTO.class)))
            .thenReturn(new UpdateContactResponseDTO());

        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult result =
            this.mockMvc.perform(
                    put("/accounts/{accountId}/user/{userId}", "123", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateContactRequestDTO))
                )
                .andExpect(status().isOk())
                .andReturn();
        assertDoesNotThrow(() ->
            objectMapper.readValue(result.getResponse().getContentAsString(), UpdateContactRequestDTO.class)
        );
    }

    @Test
    public void updateContact_failure() throws Exception {
        when(eclipseService.updateContact(anyString(), anyString(), any(UpdateContactRequestDTO.class)))
            .thenThrow(new EclipseException());

        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult result =
            this.mockMvc.perform(
                    put("/accounts/{accountId}/user/{userId}", "123", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateContactRequestDTO))
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void submitOrder_success() throws Exception {
        GetOrderResponseDTO getOrderResponseDTO = new GetOrderResponseDTO();
        getOrderResponseDTO.setOrderNumber("5555555555");

        SalesOrderDTO salesOrderDTO = new SalesOrderDTO();
        salesOrderDTO.setPhoneNumber("555-555-5555");
        when(eclipseService.submitOrder(salesOrderDTO)).thenReturn(getOrderResponseDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult result =
            this.mockMvc.perform(
                    post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(salesOrderDTO))
                )
                .andExpect(status().isOk())
                .andReturn();
        assertDoesNotThrow(() ->
            objectMapper.readValue(result.getResponse().getContentAsString(), GetOrderResponseDTO.class)
        );
    }

    @Test
    public void submitOrderPreview_success() throws Exception {
        GetOrderResponseDTO getOrderResponseDTO = new GetOrderResponseDTO();
        getOrderResponseDTO.setOrderNumber("5555555555");

        SalesOrderDTO salesOrderDTO = new SalesOrderDTO();
        salesOrderDTO.setPhoneNumber("555-555-5555");
        when(eclipseService.submitOrderPreview(salesOrderDTO)).thenReturn(getOrderResponseDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult result =
            this.mockMvc.perform(
                    post("/orders/preview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(salesOrderDTO))
                )
                .andExpect(status().isOk())
                .andReturn();
        assertDoesNotThrow(() ->
            objectMapper.readValue(result.getResponse().getContentAsString(), GetOrderResponseDTO.class)
        );
    }

    @Test
    public void getSalesOrders_success() throws Exception {
        String startDate = "1/1/2001";
        String endDate = "1/1/2008";
        MassSalesOrderResponseDTO massSalesOrderResponseDTO = new MassSalesOrderResponseDTO();
        when(eclipseService.getSalesOrders(TEST_ACCOUNT_ID, startDate, endDate, 0))
            .thenReturn(massSalesOrderResponseDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult result =
            this.mockMvc.perform(
                    get("/accounts/{accountId}/salesorders", TEST_ACCOUNT_ID)
                        .param("orderPostStartDate", startDate)
                        .param("orderPostEndDate", endDate)
                        .param("page", "0")
                )
                .andExpect(status().isOk())
                .andReturn();
        assertDoesNotThrow(() ->
            objectMapper.readValue(result.getResponse().getContentAsString(), MassSalesOrderResponseDTO.class)
        );
    }

    @Test
    public void uploadTaxDocument_success() throws Exception {
        String encodedFileData = "test";
        DocumentImagingFileDTO documentImagingFileDTO = new DocumentImagingFileDTO();
        when(eclipseService.uploadTaxDocument(TEST_ACCOUNT_ID, encodedFileData)).thenReturn(documentImagingFileDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult result =
            this.mockMvc.perform(post("/accounts/{accountId}/tax-document", TEST_ACCOUNT_ID).content(encodedFileData))
                .andExpect(status().isOk())
                .andReturn();
        assertDoesNotThrow(() ->
            objectMapper.readValue(result.getResponse().getContentAsString(), DocumentImagingFileDTO.class)
        );
    }

    @Test
    public void uploadTaxDocument_invalidId() throws Exception {
        String encodedFileData = "test";
        when(eclipseService.uploadTaxDocument(TEST_ACCOUNT_ID, encodedFileData)).thenThrow(new InvalidIdException(""));

        this.mockMvc.perform(post("/accounts/{accountId}/tax-document", TEST_ACCOUNT_ID).content(encodedFileData))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    public void uploadTaxDocument_eclipseTokenException() throws Exception {
        String encodedFileData = "test";
        when(eclipseService.uploadTaxDocument(TEST_ACCOUNT_ID, encodedFileData)).thenThrow(new EclipseTokenException());

        this.mockMvc.perform(post("/accounts/{accountId}/tax-document", TEST_ACCOUNT_ID).content(encodedFileData))
            .andExpect(status().isInternalServerError())
            .andReturn();
    }

    @Test
    public void searchEntity_success() throws Exception {
        EntitySearchResponseDTO dto = new EntitySearchResponseDTO();

        EntitySearchResult res = new EntitySearchResult();
        res.setIsBillTo(true);
        List<EntitySearchResult> resultList = new ArrayList<>();
        resultList.add(res);
        EntitySearchResponseTypes type = new EntitySearchResponseTypes();
        type.setResults(resultList);
        dto.setCustomers(type);
        when(eclipseService.searchEntity(anyString())).thenReturn(dto);

        MvcResult result =
            this.mockMvc.perform(get("/entitySearch/{accountId}", "1")).andExpect(status().isOk()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        assertDoesNotThrow(() ->
            objectMapper.readValue(result.getResponse().getContentAsString(), EntitySearchResult.class)
        );
    }

    @Test
    public void searchEntity_failure() throws Exception {
        EntitySearchResponseDTO dto = new EntitySearchResponseDTO();

        List<EntitySearchResult> resultList = new ArrayList<>();
        EntitySearchResponseTypes type = new EntitySearchResponseTypes();
        type.setResults(resultList);
        dto.setCustomers(type);
        when(eclipseService.searchEntity(anyString())).thenReturn(dto);

        MvcResult result =
            this.mockMvc.perform(get("/entitySearch/{accountId}", "1")).andExpect(status().isNotFound()).andReturn();
        assert (result.getResolvedException().getMessage().contains("No account with id 1 found"));
    }

    @Test
    public void kourierProductSearch_success() throws Exception {
        ProductSearchResponseDTO mockResponse = new ProductSearchResponseDTO();
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("keywords", URLEncoder.encode("keywords", "UTF-8"));
        requestParams.add("displayName", "displayName");

        when(kourierService.getProductSearch(any(), any())).thenReturn(mockResponse);
        this.mockMvc.perform(get("/products/search").queryParams(requestParams)).andExpect(status().isOk()).andReturn();
    }

    @Test
    void kourierSearchProducts_failure() throws Exception {
        mockMvc
            .perform(post("/products/search").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    public void getProducts_success() throws Exception {
        ProductResponse getProductResponse = new ProductResponse();
        List<String> productIds = new ArrayList<String>();
        productIds.add("123");
        productIds.add("111");
        when(eclipseService.getProducts(eq(productIds), any(ErpUserInformationDTO.class), anyBoolean()))
            .thenReturn(getProductResponse);
        var resp = eclipseController.getProducts(anyList(), any(ErpUserInformationDTO.class), anyBoolean());
        assertEquals(resp.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getQuotes_success() throws Exception {
        MassSalesOrderResponseDTO getOrderResponseDTO = new MassSalesOrderResponseDTO();
        when(eclipseService.getQuotesByAccountId(anyString(), anyString(), anyString(), anyInt()))
            .thenReturn(getOrderResponseDTO);
        var resp = eclipseController.getQuotes(anyString(), anyString(), anyString(), anyInt());
        assertEquals(resp.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getApproveQuote_success() throws Exception {
        GetOrderResponseDTO getOrderResponseDTO = new GetOrderResponseDTO();
        when(eclipseService.approveQuote(any(ApproveQuoteDTO.class))).thenReturn(getOrderResponseDTO);
        var resp = eclipseController.approveQuote(any(ApproveQuoteDTO.class));
        assertEquals(resp.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getRejectQuote_success() throws Exception {
        RejectQuoteDTO getRejectQuote = new RejectQuoteDTO();
        getRejectQuote.setQuoteId("1234");

        doNothing().when(eclipseService).rejectQuote(getRejectQuote);
        var resp = eclipseController.rejectQuote(any(RejectQuoteDTO.class));
        assertEquals(resp.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getCreditCardList_success() throws Exception {
        CreditCardListResponseDTO getCreditCardlistRespDTO = new CreditCardListResponseDTO();
        when(eclipseService.getCreditCardList(eq(TEST_ACCOUNT_ID))).thenReturn(getCreditCardlistRespDTO);
        var resp = eclipseController.getCreditCardList(eq(TEST_ACCOUNT_ID));
        assertEquals(resp.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void updateCreditCardList_success() throws Exception {
        EntityUpdateSubmitResponseDTO entityUpdateSubmitResponseDTO = new EntityUpdateSubmitResponseDTO();
        when(
            eclipseService.updateEntityInquiryForCreditCard(
                eq(TEST_ACCOUNT_ID),
                any(EntityUpdateSubmitRequestDTO.class)
            )
        )
            .thenReturn(entityUpdateSubmitResponseDTO);
        var resp = eclipseController.updateCreditCardList(eq(TEST_ACCOUNT_ID), any(EntityUpdateSubmitRequestDTO.class));
        assertEquals(resp.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getCreditCardSetupUrl_success() throws Exception {
        ElementAccountSetupResponseDTO elementAccountSetupResponseDTO = new ElementAccountSetupResponseDTO();
        when(eclipseService.getCreditCardSetupUrl(eq(TEST_ACCOUNT_ID), any(ElementAccountSetupDataDTO.class)))
            .thenReturn(elementAccountSetupResponseDTO);
        var resp = eclipseController.getCreditCardSetupUrl(eq(TEST_ACCOUNT_ID), any(ElementAccountSetupDataDTO.class));
        assertEquals(resp.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getCreditCardElementInfo_success() throws Exception {
        String elementSetupId = "1111";
        ElementSetupQueryResponse elementSetupQueryResponse = new ElementSetupQueryResponse();
        ElementSetupQueryResponseWrapper elementSetupQueryResponseWrapper = new ElementSetupQueryResponseWrapper();
        elementSetupQueryResponseWrapper.setElementSetupQueryResponse(elementSetupQueryResponse);
        ElementSetupQueryResponseDTO elementSetupQueryResponseDTO = new ElementSetupQueryResponseDTO(
            elementSetupQueryResponseWrapper
        );

        when(eclipseService.getCreditCardElementInfo(eq(TEST_ACCOUNT_ID), eq(elementSetupId)))
            .thenReturn(elementSetupQueryResponseDTO);
        var resp = eclipseController.getCreditCardElementInfo(eq(TEST_ACCOUNT_ID), eq(elementSetupId));
        assertEquals(resp.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getPreviouslyPurchasedProducts_success() throws Exception {
        int currentPage = 1;
        int pageSize = 10;
        ReorderPadInquiryResponse recordPadInquiryResponse = new ReorderPadInquiryResponse();
        PreviouslyPurchasedProductsDTO previouslyPurchasedProductsDTO = new PreviouslyPurchasedProductsDTO(
            recordPadInquiryResponse,
            currentPage,
            pageSize
        );

        when(eclipseService.getPreviouslyPurchasedProducts(eq(TEST_ACCOUNT_ID), anyInt(), anyInt()))
            .thenReturn(previouslyPurchasedProductsDTO);
        var resp = eclipseController.getPreviouslyPurchasedProducts(eq(TEST_ACCOUNT_ID), anyInt(), anyInt());
        assertEquals(resp.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void deleteCreditCard_success() throws Exception {
        String creditCardId = "111";
        doNothing().when(eclipseService).deleteCreditCard(eq(TEST_ACCOUNT_ID), eq(creditCardId));
        var resp = eclipseController.deleteCreditCard(eq(TEST_ACCOUNT_ID), eq(creditCardId));
        assertEquals(resp.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getProductImageUrl_success() throws Exception {
        String productId = "1234";
        when(eclipseService.getProductImageUrl(eq(productId))).thenReturn(productId);
        var resp = eclipseController.getProductImageUrl(productId);
        assertNotNull(resp);
    }

    @Test
    public void getSalesOrders_webstatus_success() throws Exception {
        String startDate = "1/1/2001";
        String endDate = "1/1/2008";
        MassSalesOrderResponse massSalesOrderResponse = new MassSalesOrderResponse();

        MassSalesOrderInquiryResponse massSalesOrderInquiryResponse = new MassSalesOrderInquiryResponse();

        massSalesOrderInquiryResponse.setSessionID("123");
        com.reece.platform.eclipse.model.XML.common.StatusResult status = new StatusResult();
        status.setSuccess("Submitted");
        massSalesOrderInquiryResponse.setStatusResult(status);
        SalesOrderList salesOrderList = new SalesOrderList();
        List<SalesOrder> salesorderList = new ArrayList();

        SalesOrder salesOrder = new SalesOrder();
        OrderHeader orderHeader = new OrderHeader();
        orderHeader.setQuoteStatus("WEB NON-GENUINE");
        salesOrder.setOrderHeader(orderHeader);
        OrderTotals orderTotals = new OrderTotals();

        salesOrder.setOrderTotals(orderTotals);
        salesorderList.add(salesOrder);
        salesOrderList.setSalesOrderList(salesorderList);
        massSalesOrderInquiryResponse.setSalesOrderList(salesOrderList);
        massSalesOrderResponse.setMassSalesOrderInquiryResponse(massSalesOrderInquiryResponse);
        MassSalesOrderResponseDTO massSalesOrderResponseDTO = new MassSalesOrderResponseDTO(
            massSalesOrderResponse,
            false
        );

        when(eclipseService.getSalesOrders(TEST_ACCOUNT_ID, startDate, endDate, 0))
            .thenReturn(massSalesOrderResponseDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult result =
            this.mockMvc.perform(
                    get("/accounts/{accountId}/salesorders", TEST_ACCOUNT_ID)
                        .param("orderPostStartDate", startDate)
                        .param("orderPostEndDate", endDate)
                        .param("page", "0")
                )
                .andExpect(status().isOk())
                .andReturn();
        MassSalesOrderResponseDTO massSalesOrderResponseDTOResponse = objectMapper.readValue(
            result.getResponse().getContentAsString(),
            MassSalesOrderResponseDTO.class
        );

        assertEquals(massSalesOrderResponseDTOResponse.getSalesOrders().get(0).getQuoteStatus(), "WEB NON-GENUINE");
    }

    @Test
    public void getSalesOrders_webstatus_success1() throws Exception {
        String startDate = "1/1/2001";
        String endDate = "1/1/2008";
        MassSalesOrderResponse massSalesOrderResponse = new MassSalesOrderResponse();

        MassSalesOrderInquiryResponse massSalesOrderInquiryResponse = new MassSalesOrderInquiryResponse();

        massSalesOrderInquiryResponse.setSessionID("123");
        com.reece.platform.eclipse.model.XML.common.StatusResult status = new StatusResult();
        status.setSuccess("Submitted");
        massSalesOrderInquiryResponse.setStatusResult(status);
        SalesOrderList salesOrderList = new SalesOrderList();
        List<SalesOrder> salesorderList = new ArrayList();

        SalesOrder salesOrder = new SalesOrder();
        OrderHeader orderHeader = new OrderHeader();
        orderHeader.setQuoteStatus("WEB VIEW");
        salesOrder.setOrderHeader(orderHeader);
        OrderTotals orderTotals = new OrderTotals();

        salesOrder.setOrderTotals(orderTotals);
        salesorderList.add(salesOrder);
        salesOrderList.setSalesOrderList(salesorderList);
        massSalesOrderInquiryResponse.setSalesOrderList(salesOrderList);
        massSalesOrderResponse.setMassSalesOrderInquiryResponse(massSalesOrderInquiryResponse);
        MassSalesOrderResponseDTO massSalesOrderResponseDTO = new MassSalesOrderResponseDTO(
            massSalesOrderResponse,
            false
        );

        when(eclipseService.getSalesOrders(TEST_ACCOUNT_ID, startDate, endDate, 0))
            .thenReturn(massSalesOrderResponseDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult result =
            this.mockMvc.perform(
                    get("/accounts/{accountId}/salesorders", TEST_ACCOUNT_ID)
                        .param("orderPostStartDate", startDate)
                        .param("orderPostEndDate", endDate)
                        .param("page", "0")
                )
                .andExpect(status().isOk())
                .andReturn();
        MassSalesOrderResponseDTO massSalesOrderResponseDTOResponse = objectMapper.readValue(
            result.getResponse().getContentAsString(),
            MassSalesOrderResponseDTO.class
        );

        assertEquals(massSalesOrderResponseDTOResponse.getSalesOrders().get(0).getQuoteStatus(), "WEB VIEW");
    }

    @Test
    public void getSalesOrders_webstatus_success2() throws Exception {
        String startDate = "1/1/2001";
        String endDate = "1/1/2008";
        MassSalesOrderResponse massSalesOrderResponse = new MassSalesOrderResponse();

        MassSalesOrderInquiryResponse massSalesOrderInquiryResponse = new MassSalesOrderInquiryResponse();

        massSalesOrderInquiryResponse.setSessionID("123");
        com.reece.platform.eclipse.model.XML.common.StatusResult status = new StatusResult();
        status.setSuccess("Submitted");
        massSalesOrderInquiryResponse.setStatusResult(status);
        SalesOrderList salesOrderList = new SalesOrderList();
        List<SalesOrder> salesorderList = new ArrayList();

        SalesOrder salesOrder = new SalesOrder();
        OrderHeader orderHeader = new OrderHeader();
        orderHeader.setQuoteStatus("WEB EDIT");
        salesOrder.setOrderHeader(orderHeader);
        OrderTotals orderTotals = new OrderTotals();

        salesOrder.setOrderTotals(orderTotals);
        salesorderList.add(salesOrder);
        salesOrderList.setSalesOrderList(salesorderList);
        massSalesOrderInquiryResponse.setSalesOrderList(salesOrderList);
        massSalesOrderResponse.setMassSalesOrderInquiryResponse(massSalesOrderInquiryResponse);
        MassSalesOrderResponseDTO massSalesOrderResponseDTO = new MassSalesOrderResponseDTO(
            massSalesOrderResponse,
            false
        );

        when(eclipseService.getSalesOrders(TEST_ACCOUNT_ID, startDate, endDate, 0))
            .thenReturn(massSalesOrderResponseDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult result =
            this.mockMvc.perform(
                    get("/accounts/{accountId}/salesorders", TEST_ACCOUNT_ID)
                        .param("orderPostStartDate", startDate)
                        .param("orderPostEndDate", endDate)
                        .param("page", "0")
                )
                .andExpect(status().isOk())
                .andReturn();
        MassSalesOrderResponseDTO massSalesOrderResponseDTOResponse = objectMapper.readValue(
            result.getResponse().getContentAsString(),
            MassSalesOrderResponseDTO.class
        );

        assertEquals(massSalesOrderResponseDTOResponse.getSalesOrders().get(0).getQuoteStatus(), "WEB EDIT");
    }
}
