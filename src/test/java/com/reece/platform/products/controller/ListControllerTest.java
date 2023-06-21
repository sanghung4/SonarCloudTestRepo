package com.reece.platform.products.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.products.TestUtils;
import com.reece.platform.products.exceptions.ListNotFoundException;
import com.reece.platform.products.model.DTO.*;
import com.reece.platform.products.model.ErpEnum;
import com.reece.platform.products.model.ErpUserInformation;
import com.reece.platform.products.model.Stock;
import com.reece.platform.products.model.UploadListData;
import com.reece.platform.products.service.AuthorizationService;
import com.reece.platform.products.service.ListService;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = { ListController.class, GlobalExceptionHandler.class })
@AutoConfigureMockMvc
@EnableWebMvc
public class ListControllerTest {

    private final String TEST_DATA_PATH = "src/test/resources/testData";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorizationService authorizationService;

    @MockBean
    private ListService listService;

    @InjectMocks
    private ListController listController;

    private ListDTO createdList;
    private ListDTO listToCreate;
    private ListDTO listWithItems;
    private ListDTO listWithNoItems;
    private ListDTO listWithNoItemsUpdate;
    private ArrayList<ListDTO> allLists;
    private ListLineItemDTO pipeLineItem;
    private ListLineItemDTO itemToAdd;
    private ErpUserInformation erpUserInformation;

    private ToggleListLineItemDTO toggleListLineItemDTO;
    private ListLineItemDTO toggleLineItemDTO;
    private List<UUID> listIDs;
    private ToggleListItemResponseDTO toggleListItemResponseDTO;
    private List<ListDTO> lists;
    private ListDTO fistList;
    private ListDTO secondList;
    private List<String> errors;

    private static final String BRANCH_ID = "123";
    private static final UUID CREATED_LIST_ID = UUID.randomUUID();
    private static final UUID LIST_WITH_ITEMS_ID = UUID.randomUUID();
    private static final UUID LIST_WITH_NO_ITEMS_ID = UUID.randomUUID();
    private static final UUID LIST_TO_DELETE = UUID.randomUUID();
    private static final UUID PIPE_LINE_ITEM_ID = UUID.randomUUID();
    private static final UUID ITEM_TO_ADD_ID = UUID.randomUUID();
    private static final UUID BILLTO_ACCOUNT_ID = UUID.randomUUID();
    private static final UUID CART_ID = UUID.randomUUID();
    private static final String LIST_NAME = "newlist";
    private static final UUID DNE_ID = UUID.randomUUID();

    private static final String AUTH_TOKEN =
        "Bearer eyJraWQiOiJGeEkzZmNPWmhLRExzbHExOU9CLUJvNWE5MHc5Mjlwd0ktUENzVkZnaVkwIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULlE3dVpmY1pXLWdLRTF1YVNScVdIQVU0RlNMWl9jUW1xWFdRZXU0aF8xTVUiLCJpc3MiOiJodHRwczovL2Rldi00MzI1NDYub2t0YS5jb20vb2F1dGgyL2RlZmF1bHQiLCJhdWQiOiJhcGk6Ly9kZWZhdWx0IiwiaWF0IjoxNjI5Mjg4NDM3LCJleHAiOjE2MjkzNzQ4MzcsImNpZCI6IjBvYTEzYjBiNWJNS1haZkpVNHg3IiwidWlkIjoiMDB1MzhqN280cWpQcjFlNzY0eDciLCJzY3AiOlsicHJvZmlsZSIsImVtYWlsIiwib3BlbmlkIl0sInN1YiI6ImVjbGlwc2VhZG1pbmRldisyMTcyNjBAbW9yc2NvLmNvbSIsImVjb21tVXNlcklkIjoiMmZlMmI4YWUtNTk0ZS00MzU2LWE5MzAtZmE2N2YyZjM4ZDBkIiwiZWNvbW1QZXJtaXNzaW9ucyI6WyJhcHByb3ZlX2NhcnQiLCJlZGl0X3Byb2ZpbGUiLCJzdWJtaXRfcXVvdGVfb3JkZXIiLCJtYW5hZ2Vfcm9sZXMiLCJlZGl0X2xpc3QiLCJ2aWV3X2ludm9pY2UiLCJpbnZpdGVfdXNlciIsIm1hbmFnZV9wYXltZW50X21ldGhvZHMiLCJhcHByb3ZlX2FjY291bnRfdXNlciIsInN1Ym1pdF9jYXJ0X3dpdGhvdXRfYXBwcm92YWwiXX0.gNf1TDHghj7FvkikMNGilXYQDPiBiIr7t5DeOEIqSBDbmCHyupiN_J13t2TftBhZvdDtlqXdGrVqIuYgWghRZK-EGjHzPFZ0lSADkaM2k6XBsjxOxIv_vdxYAqlO5Y5ODXtO2RMY3q-Z0bp51Cq0w5Mck7Xq_AGHo3oaESMM-QounwULH2qVTE_Obi1EwZ7zATFjTXctaC-Q0VIhba7qk0EC6kjbNfDqOR8KvBr40TL1YVK4eOEMBA5NWwga0h0_1STBqn7KabcYqMiFJga1FEbQtxzC6ckF20HhozZDLDdoxNgTBMmucZ2z7hbFkyIJ4rkDnEmq4TJ_R762aEn60A";
    private static final String AUTH_TOKEN_UNAUTHORIZED =
        "Bearer eyJraWQiOiJGeEkzZmNPWmhLRExzbHExOU9CLUJvNWE5MHc5Mjlwd0ktUENzVkZnaVkwIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULnJERE1icGVxNWxyTE9iWFQ3Z3JSbVMweFVLeEV4Zmkwc3pGaldDRFIwckUiLCJpc3MiOiJodHRwczovL2Rldi00MzI1NDYub2t0YS5jb20vb2F1dGgyL2RlZmF1bHQiLCJhdWQiOiJhcGk6Ly9kZWZhdWx0IiwiaWF0IjoxNjI5Mjg4ODI2LCJleHAiOjE2MjkzNzUyMjYsImNpZCI6IjBvYTEzYjBiNWJNS1haZkpVNHg3IiwidWlkIjoiMDB1NDdhZXk4NlpSUHpsVXc0eDciLCJzY3AiOlsicHJvZmlsZSIsIm9wZW5pZCIsImVtYWlsIl0sInN1YiI6InRpbStpbnZvaWNlb25seUBkaWFsZXhhLmNvbSIsImVjb21tVXNlcklkIjoiYWIyNDUyNDItMTFiMS00YjNhLTkyYzItNzZlM2E1YzZlMTYzIiwiZWNvbW1QZXJtaXNzaW9ucyI6WyJ2aWV3X2ludm9pY2UiXX0.SeK3QOM4OxpNGsg7avdQwKB0taAzsFaAP2rTfPq5vRe7EKnsB1inAgilL2YNQz76M6m-GzQDtmWHx0Y1uGiMjQ8HB6uMye9KJDkDRjl00Bb9CrzCCiJK-NIbDxP4HtTUeM6BJsQLh3vu9sDz5NEmhgFlsSmRhGypVYTj5C3pO8jjN8ZTNznGH4YrTHb7GipCF2a_bGKQ1vfss6rJ_2WrIKUkLoTaQ-xmUZmTIbjkE7ECK9Snu6yLKHgcCHjaa10WPBvDRdZ3nLU8Es4a1dF4vFfWEhH-dy_soeSZmYcP9xMatbmviXlcUsiSoCr-vu64ztEllfhsCP5dJT8ocE_pQg";
    private static final String PIPE_ERP_PART_NUMBER = "123942";
    private static final String ITEM_TO_ADD_ERP_PART_NUMBER = "472081";
    private static final String LIST_WITH_ITEMS_NAME = "List With Items";
    private static final String LIST_WITH_NO_ITEMS_NAME = "List With No Items";
    private static final String LIST_WITH_NO_ITEMS_NEW_NAME = "List With No Items - new";

    private static final Integer PIPE_QUANTITY = 4;
    private static final Integer MIN_INCREMENT_QTY = 1;
    private static final Integer ITEM_TO_ADD_QUANTITY = 4;

    private static final UUID ITEM_TO_ADD_FIRST_LIST_ID = UUID.fromString("17be7006-26a0-48bb-b232-38f2ed2c746e");
    private static final UUID ITEM_TO_ADD_SECOND_LIST_ID = UUID.fromString("82a7c255-5f42-416e-84ef-7fc0c02a3dba");

    @BeforeEach
    public void setup() throws Exception {
        erpUserInformation = new ErpUserInformation();

        itemToAdd =
            new ListLineItemDTO(
                ITEM_TO_ADD_ID,
                ITEM_TO_ADD_ERP_PART_NUMBER,
                ITEM_TO_ADD_QUANTITY,
                MIN_INCREMENT_QTY,
                LIST_WITH_ITEMS_ID,
                1,
                0,
                new Stock(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            );

        pipeLineItem =
            new ListLineItemDTO(
                PIPE_LINE_ITEM_ID,
                PIPE_ERP_PART_NUMBER,
                PIPE_QUANTITY,
                MIN_INCREMENT_QTY,
                LIST_WITH_ITEMS_ID,
                0,
                0,
                new Stock(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            );

        var listItems = new ArrayList<ListLineItemDTO>();
        listItems.add(pipeLineItem);

        listWithItems = new ListDTO(LIST_WITH_ITEMS_ID, LIST_WITH_ITEMS_NAME, BILLTO_ACCOUNT_ID, listItems, 0);

        listWithNoItems = new ListDTO(LIST_WITH_NO_ITEMS_ID, LIST_WITH_NO_ITEMS_NAME, BILLTO_ACCOUNT_ID, null, 0);

        listWithNoItemsUpdate =
            new ListDTO(LIST_WITH_NO_ITEMS_ID, LIST_WITH_NO_ITEMS_NEW_NAME, BILLTO_ACCOUNT_ID, null, 0);

        listToCreate = new ListDTO();
        createdList = new ListDTO();
        createdList.setId(CREATED_LIST_ID);

        allLists = new ArrayList<ListDTO>();
        allLists.add(listWithItems);
        allLists.add(listWithNoItems);

        toggleListLineItemDTO = new ToggleListLineItemDTO();
        toggleLineItemDTO = new ListLineItemDTO();
        toggleLineItemDTO.setErpPartNumber(ITEM_TO_ADD_ERP_PART_NUMBER);
        toggleLineItemDTO.setQuantity(1);

        listIDs = new ArrayList<>();
        listIDs.add(ITEM_TO_ADD_FIRST_LIST_ID);
        listIDs.add(ITEM_TO_ADD_SECOND_LIST_ID);

        toggleListLineItemDTO.setListIds(listIDs);
        toggleListLineItemDTO.setListLineItemDTO(toggleLineItemDTO);

        lists = new ArrayList<>();
        fistList = new ListDTO();
        fistList.setId(ITEM_TO_ADD_FIRST_LIST_ID);
        lists.add(fistList);

        secondList = new ListDTO();
        secondList.setId(ITEM_TO_ADD_FIRST_LIST_ID);
        lists.add(secondList);
        toggleListItemResponseDTO = new ToggleListItemResponseDTO(lists);

        List<UUID> listIds = new ArrayList<>();
        listIds.add(UUID.randomUUID());
        when(authorizationService.userCanEditList(AUTH_TOKEN)).thenReturn(true);
        when(authorizationService.userCanEditList(AUTH_TOKEN_UNAUTHORIZED)).thenReturn(false);

        when(listService.getList(LIST_WITH_ITEMS_ID, BRANCH_ID)).thenReturn(listWithItems);
        when(listService.getList(DNE_ID, BRANCH_ID)).thenThrow(ListNotFoundException.class);
        when(listService.getListsByBillToAccountId(BILLTO_ACCOUNT_ID)).thenReturn(allLists);
        when(listService.createList(any())).thenReturn(createdList);
        when(listService.addItemToList(LIST_WITH_ITEMS_ID, itemToAdd)).thenReturn(ITEM_TO_ADD_ID);
        when(listService.addAllCartItemsToNewList(any(), any(), any())).thenReturn(UUID.randomUUID());
        when(listService.addAllCartItemsToLists(any(), anyList())).thenReturn(listIds);
        when(listService.updateList(listWithNoItemsUpdate)).thenReturn(listWithNoItemsUpdate);
        when(listService.deleteListById(LIST_TO_DELETE)).thenReturn(true);

        when(listService.toggleItemFromLists(toggleListLineItemDTO)).thenReturn(toggleListItemResponseDTO);
    }

    @Test
    public void getList_success() throws Exception {
        String content = new ObjectMapper().writeValueAsString(new ErpUserInformation());
        MvcResult result = mockMvc
            .perform(
                post("/lists/{listId}", LIST_WITH_ITEMS_ID)
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("branchId", BRANCH_ID)
                    .param("erpAccountId", "123")
                    .content(content)
            )
            .andExpect(status().isOk())
            .andReturn();

        var returnedList = new ObjectMapper().readValue(result.getResponse().getContentAsString(), ListDTO.class);
        assertEquals(returnedList, listWithItems, "Expected list with items to be fetched by ID");
    }

    @Test
    public void getList_notFound() throws Exception {
        String content = new ObjectMapper().writeValueAsString(new ErpUserInformation());
        MvcResult result = mockMvc
            .perform(
                post("/lists/{listId}", DNE_ID)
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("branchId", BRANCH_ID)
                    .content(content)
            )
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    public void getLists_success() throws Exception {
        MvcResult result = mockMvc
            .perform(
                get("/lists")
                    .param("billToAccountId", BILLTO_ACCOUNT_ID.toString())
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn();

        assertDoesNotThrow(() ->
            new ObjectMapper().readValue(result.getResponse().getContentAsString(), ListDTO[].class)
        );
    }

    @Test
    public void createList_success() throws Exception {
        String content = new ObjectMapper().writeValueAsString(listToCreate);

        MvcResult result = mockMvc
            .perform(
                post("/lists")
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andExpect(status().isOk())
            .andReturn();

        var returnedList = new ObjectMapper().readValue(result.getResponse().getContentAsString(), ListDTO.class);
        assertEquals(returnedList.getId(), CREATED_LIST_ID);
    }

    @Test
    public void createList_unauthorized() throws Exception {
        String content = new ObjectMapper().writeValueAsString(listToCreate);

        MvcResult result = mockMvc
            .perform(
                post("/lists")
                    .header("Authorization", AUTH_TOKEN_UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    public void addItemToList_success() throws Exception {
        String content = new ObjectMapper().writeValueAsString(itemToAdd);

        MvcResult result = mockMvc
            .perform(
                post("/lists/{listId}/items", LIST_WITH_ITEMS_ID)
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andExpect(status().isOk())
            .andReturn();

        var uuid = UUID.fromString(result.getResponse().getContentAsString().replace("\"", ""));
        assertEquals(uuid, ITEM_TO_ADD_ID);
    }

    @Test
    public void addItemToList_unauthorized() throws Exception {
        String content = new ObjectMapper().writeValueAsString(itemToAdd);

        MvcResult result = mockMvc
            .perform(
                post("/lists/{listId}/items", LIST_WITH_ITEMS_ID)
                    .header("Authorization", AUTH_TOKEN_UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    public void addAllCartItemsToNewList_success() throws Exception {
        MvcResult result = mockMvc
            .perform(
                post("/lists/addCartItemsToNewList/{accountId}/{cartId}", BILLTO_ACCOUNT_ID, CART_ID)
                    .param("name", "testList")
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    public void addAllCartItemsToNewList_unauthorized() throws Exception {
        MvcResult result = mockMvc
            .perform(
                post("/lists/addCartItemsToNewList/{accountId}/{cartId}", BILLTO_ACCOUNT_ID, CART_ID)
                    .param("name", "testList")
                    .header("Authorization", AUTH_TOKEN_UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    public void addAllCartItemsToExistingLists_success() throws Exception {
        List<String> listIds = new ArrayList<>();
        listIds.add(String.valueOf(UUID.randomUUID()));
        String content = new ObjectMapper().writeValueAsString(listIds);

        MvcResult result = mockMvc
            .perform(
                post("/lists/addCartItemsToExistingLists/{cartId}", CART_ID)
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    public void addAllCartItemsToExistingLists_unauthorized() throws Exception {
        List<String> listIds = new ArrayList<>();
        listIds.add(String.valueOf(UUID.randomUUID()));
        String content = new ObjectMapper().writeValueAsString(listIds);

        MvcResult result = mockMvc
            .perform(
                post("/lists/addCartItemsToExistingLists/{cartId}", CART_ID)
                    .header("Authorization", AUTH_TOKEN_UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    public void updateList_success() throws Exception {
        String content = new ObjectMapper().writeValueAsString(listWithNoItemsUpdate);

        MvcResult result = mockMvc
            .perform(
                put("/lists")
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andExpect(status().isOk())
            .andReturn();

        var updatedList = new ObjectMapper().readValue(result.getResponse().getContentAsString(), ListDTO.class);
        assertEquals(updatedList, listWithNoItemsUpdate, "Expected list to be updated");
    }

    @Test
    public void updateList_failure() throws Exception {
        String content = new ObjectMapper().writeValueAsString(listWithNoItemsUpdate);

        MvcResult result = mockMvc
            .perform(
                put("/lists")
                    .header("Authorization", AUTH_TOKEN_UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    public void deleteList_success() throws Exception {
        MvcResult result = mockMvc
            .perform(
                delete("/lists/{listId}", LIST_TO_DELETE)
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    public void deleteList_failure() throws Exception {
        MvcResult result = mockMvc
            .perform(
                delete("/lists/{listId}", LIST_TO_DELETE)
                    .header("Authorization", AUTH_TOKEN_UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    public void uploadList_happyPath() throws Exception {
        MockMultipartFile mockTestFile = new MockMultipartFile(
            "file",
            "validData.csv",
            MediaType.TEXT_PLAIN_VALUE,
            TestUtils.loadTestFile("validUpload.csv").getBytes(StandardCharsets.UTF_8)
        );

        MvcResult result = mockMvc
            .perform(
                multipart("/lists/upload")
                    .file(mockTestFile)
                    .param("name", "testlist")
                    .param("billToAccountId", UUID.randomUUID().toString())
                    .header("Authorization", AUTH_TOKEN)
            )
            .andExpect(status().isOk())
            .andReturn();

        verify(listService, times(1)).handleUploadNewList(anyList(), any(), any());
    }

    @Test
    public void uploadList_noHeader() throws Exception {
        MockMultipartFile mockTestFile = new MockMultipartFile(
            "file",
            "uploadNoHeader.csv",
            MediaType.TEXT_PLAIN_VALUE,
            TestUtils.loadTestFile("uploadNoHeader.csv").getBytes(StandardCharsets.UTF_8)
        );

        MvcResult result = mockMvc
            .perform(
                multipart("/lists/upload")
                    .file(mockTestFile)
                    .param("name", "testlist")
                    .param("billToAccountId", UUID.randomUUID().toString())
                    .header("Authorization", AUTH_TOKEN)
            )
            .andExpect(status().isBadRequest())
            .andReturn();

        verify(listService, times(0)).handleUploadedList(anyList(), any());
    }

    @Test
    public void uploadList_missingColumn() throws Exception {
        MockMultipartFile mockTestFile = new MockMultipartFile(
            "file",
            "missingColumn.csv",
            MediaType.TEXT_PLAIN_VALUE,
            TestUtils.loadTestFile("missingColumn.csv").getBytes(StandardCharsets.UTF_8)
        );

        MvcResult result = mockMvc
            .perform(
                multipart("/lists/upload")
                    .file(mockTestFile)
                    .param("name", "testlist")
                    .param("billToAccountId", UUID.randomUUID().toString())
                    .header("Authorization", AUTH_TOKEN)
            )
            .andExpect(status().isBadRequest())
            .andReturn();

        verify(listService, times(0)).handleUploadedList(anyList(), any());
    }

    @Test
    public void uploadList_missingHeader() throws Exception {
        MockMultipartFile mockTestFile = new MockMultipartFile(
            "file",
            "missingHeader.csv",
            MediaType.TEXT_PLAIN_VALUE,
            TestUtils.loadTestFile("missingHeader.csv").getBytes(StandardCharsets.UTF_8)
        );

        MvcResult result = mockMvc
            .perform(
                multipart("/lists/upload")
                    .file(mockTestFile)
                    .param("name", "testlist")
                    .param("billToAccountId", UUID.randomUUID().toString())
                    .header("Authorization", AUTH_TOKEN)
            )
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    public void uploadList_unauthorized() throws Exception {
        MockMultipartFile mockTestFile = new MockMultipartFile(
            "file",
            "validUpload.csv",
            MediaType.TEXT_PLAIN_VALUE,
            TestUtils.loadTestFile("validUpload.csv").getBytes(StandardCharsets.UTF_8)
        );

        MvcResult result = mockMvc
            .perform(
                multipart("/lists/upload")
                    .file(mockTestFile)
                    .param("name", "testlist")
                    .param("billToAccountId", UUID.randomUUID().toString())
                    .header("Authorization", AUTH_TOKEN_UNAUTHORIZED)
            )
            .andExpect(status().isUnauthorized())
            .andReturn();

        verify(listService, times(0)).handleUploadedList(anyList(), any());
    }

    @Test
    public void uploadToList_happyPath() throws Exception {
        MockMultipartFile mockTestFile = new MockMultipartFile(
            "file",
            "validData.csv",
            MediaType.TEXT_PLAIN_VALUE,
            TestUtils.loadTestFile("validUpload.csv").getBytes(StandardCharsets.UTF_8)
        );

        MvcResult result = mockMvc
            .perform(
                multipart(String.format("/lists/%s/upload", UUID.randomUUID()))
                    .file(mockTestFile)
                    .header("Authorization", AUTH_TOKEN)
            )
            .andExpect(status().isOk())
            .andReturn();

        verify(listService, times(1)).handleUploadedList(anyList(), any());
    }

    @Test
    public void uploadToList_duplicateList() throws Exception {
        MockMultipartFile mockTestFile = new MockMultipartFile(
            "file",
            "validData.csv",
            MediaType.TEXT_PLAIN_VALUE,
            TestUtils.loadTestFile("validUpload.csv").getBytes(StandardCharsets.UTF_8)
        );

        when(listService.isDuplicate(any(), any())).thenReturn(Boolean.valueOf(true));
        MvcResult result = mockMvc
            .perform(
                multipart("/lists/upload")
                    .file(mockTestFile)
                    .param("name", "testlist")
                    .param("billToAccountId", UUID.randomUUID().toString())
                    .header("Authorization", AUTH_TOKEN)
            )
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    public void uploadToList_noHeader() throws Exception {
        MockMultipartFile mockTestFile = new MockMultipartFile(
            "file",
            "uploadNoHeader.csv",
            MediaType.TEXT_PLAIN_VALUE,
            TestUtils.loadTestFile("uploadNoHeader.csv").getBytes(StandardCharsets.UTF_8)
        );

        MvcResult result = mockMvc
            .perform(
                multipart(String.format("/lists/%s/upload", UUID.randomUUID()))
                    .file(mockTestFile)
                    .header("Authorization", AUTH_TOKEN)
            )
            .andExpect(status().isBadRequest())
            .andReturn();

        verify(listService, times(0)).handleUploadedList(anyList(), any());
    }

    @Test
    public void uploadToList_missingColumn() throws Exception {
        MockMultipartFile mockTestFile = new MockMultipartFile(
            "file",
            "missingColumn.csv",
            MediaType.TEXT_PLAIN_VALUE,
            TestUtils.loadTestFile("missingColumn.csv").getBytes(StandardCharsets.UTF_8)
        );

        MvcResult result = mockMvc
            .perform(
                multipart(String.format("/lists/%s/upload", UUID.randomUUID()))
                    .file(mockTestFile)
                    .header("Authorization", AUTH_TOKEN)
            )
            .andExpect(status().isBadRequest())
            .andReturn();

        verify(listService, times(0)).handleUploadedList(anyList(), any());
    }

    @Test
    public void uploadToList_unauthorized() throws Exception {
        MockMultipartFile mockTestFile = new MockMultipartFile(
            "file",
            "validUpload.csv",
            MediaType.TEXT_PLAIN_VALUE,
            TestUtils.loadTestFile("validUpload.csv").getBytes(StandardCharsets.UTF_8)
        );

        MvcResult result = mockMvc
            .perform(
                multipart(String.format("/lists/%s/upload", UUID.randomUUID()))
                    .file(mockTestFile)
                    .header("Authorization", AUTH_TOKEN_UNAUTHORIZED)
            )
            .andExpect(status().isUnauthorized())
            .andReturn();

        verify(listService, times(0)).handleUploadedList(anyList(), any());
    }

    @Test
    public void syncListsFromInsite_success() throws Exception {
        val erpAccountId = "11362";
        val syncListsBody = new SyncListsFromInsiteDTO(erpAccountId, ErpEnum.ECLIPSE);

        when(listService.syncListsFromInsite(erpAccountId, ErpEnum.ECLIPSE)).thenReturn(new WishListSyncResponseDTO(1));

        mockMvc
            .perform(
                post("/lists/_syncFromInsite")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.jsonStringify(syncListsBody))
            )
            .andExpect(status().isOk())
            .andReturn();

        verify(listService, times(1)).syncListsFromInsite(erpAccountId, ErpEnum.ECLIPSE);
    }

    @Test
    public void toggleListItem_happyPath() throws Exception {
        String content = new ObjectMapper().writeValueAsString(toggleListLineItemDTO);
        MvcResult result = mockMvc
            .perform(post("/lists/toggle").contentType(MediaType.APPLICATION_JSON).content(content))
            .andExpect(status().isOk())
            .andReturn();

        verify(listService, times(1)).toggleItemFromLists(toggleListLineItemDTO);
    }

    @Test
    public void getListInCSVFormat_success() throws Exception {
        String content = new ObjectMapper().writeValueAsString(new ErpUserInformation());

        List<UploadListData> csvData = new ArrayList<>();
        UploadListData uploadListData = new UploadListData();
        uploadListData.setPartNumber("88427");
        uploadListData.setDescription("Monticello, Posi-Temp Tub and Shower Faucet Cartridge");
        uploadListData.setMfrName("Moen");
        uploadListData.setQuantity(1);
        uploadListData.setPrice(0);
        uploadListData.setMfrNumber("1222B");
        csvData.add(uploadListData);

        when(listService.exportListIntoCSV(listWithItems)).thenReturn(csvData);
        MvcResult result = mockMvc
            .perform(
                post("/lists/exportListToCSV/{listId}", LIST_WITH_ITEMS_ID)
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("branchId", BRANCH_ID)
                    .content(content)
            )
            .andExpect(status().isOk())
            .andReturn();
        var returnedList = result.getResponse();
        assertNotNull(returnedList);
    }

    @Test
    public void getListInCSVFormat_Failure() throws Exception {
        String content = new ObjectMapper().writeValueAsString(new ErpUserInformation());
        MvcResult result = mockMvc
            .perform(
                post("/lists/exportListToCSV/{listId}", LIST_WITH_ITEMS_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("branchId", BRANCH_ID)
                    .content(content)
            )
            .andExpect(status().isUnauthorized())
            .andReturn();
    }
}
