package com.reece.platform.products.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reece.platform.products.exceptions.*;
import com.reece.platform.products.helpers.ERPSystem;
import com.reece.platform.products.insite.entity.Customer;
import com.reece.platform.products.insite.entity.WishList;
import com.reece.platform.products.insite.entity.WishListProduct;
import com.reece.platform.products.insite.repository.WishListDAO;
import com.reece.platform.products.model.DTO.*;
import com.reece.platform.products.model.ErpEnum;
import com.reece.platform.products.model.ErpUserInformation;
import com.reece.platform.products.model.UploadListData;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.MassProductInquiryResponse;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.AvailabilityList.AvailabilityList;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.AvailabilityList.BranchAvailability;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.AvailabilityList.NowQuantity;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.AvailabilityList.Quantity;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.PartIdentifiers;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.Pricing.Pricing;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.Product;
import com.reece.platform.products.model.eclipse.ProductResponse.MassProductInquiryResponse.Product.ProductList;
import com.reece.platform.products.model.eclipse.ProductResponse.ProductResponse;
import com.reece.platform.products.model.eclipse.common.Branch;
import com.reece.platform.products.model.entity.Cart;
import com.reece.platform.products.model.entity.LineItems;
import com.reece.platform.products.model.entity.List;
import com.reece.platform.products.model.entity.ListLineItem;
import com.reece.platform.products.model.repository.CartDAO;
import com.reece.platform.products.model.repository.LineItemsDAO;
import com.reece.platform.products.model.repository.ListLineItemsDAO;
import com.reece.platform.products.model.repository.ListsDAO;
import java.util.*;
import java.util.stream.Collectors;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ListServiceTest {

    private ListService listService;

    @Captor
    private ArgumentCaptor<List> listArgumentCaptor;

    @Captor
    private ArgumentCaptor<Set<ListLineItem>> listLineItemsArgumentCaptor;

    @Mock
    private ListsDAO listsDAO;

    @Mock
    private CartDAO cartDAO;

    @Mock
    private ListLineItemsDAO listLineItemsDAO;

    @Mock
    private LineItemsDAO lineItemsDAO;

    @Mock
    private ProductService productService;

    @Mock
    private ErpService erpService;

    @Mock
    private WishListDAO wishListDAO;

    @Mock
    private AccountService accountService;

    private ArrayList<List> allLists;
    private List listWithProducts;
    private List listWithNoProducts;
    private List listWithOneFakeProduct;
    private List listToDelete;
    private List listToCreate;
    private CreateListDTO listToCreateDTO;
    private ListLineItem listLineItemPipe;
    private ListLineItem listLineItemElbow;
    private ListLineItem listLineItemToAdd;
    private ListLineItem listLineItemOld;

    private ToggleListLineItemDTO toggleListLineItemDTO;
    private ListLineItemDTO toggleLineItemDTO;
    private java.util.List<UUID> listIDs;
    private ToggleListItemResponseDTO toggleListItemResponseDTO;
    private java.util.List<ListDTO> lists;
    private ListDTO fistList;
    private ListDTO secondList;
    private java.util.List<String> errors;

    private static final String LIST_WITH_PRODUCTS_NAME = "List with Products";
    private static final String LIST_WITH_NO_PRODUCTS_NAME = "List without Products";
    private static final String LIST_WITH_ONE_FAKE_PRODUCT_NAME = "List with one fake product";
    private static final String LIST_TO_CREATE_NAME = "List to Create";
    private static final String ERP_PART_NUMBER_PIPE = "12345";
    private static final String ERP_PART_NUMBER_ELBOW = "75290";
    private static final String ERP_PART_NUMBER_TO_ADD = "24095";
    private static final String ERP_PART_NUMBER_OLD = "99999";
    private static final String BRANCH_ID = "123";

    private static final UUID BILL_TO_ACCOUNT_ID = UUID.randomUUID();
    private static final UUID LIST_WITH_PRODUCTS_ID = UUID.randomUUID();
    private static final UUID LIST_WITH_NO_PRODUCTS_ID = UUID.randomUUID();
    private static final UUID LIST_WITH_ONE_FAKE_PRODUCT_ID = UUID.randomUUID();
    private static final UUID LIST_TO_CREATE_ID = UUID.randomUUID();
    private static final UUID LIST_TO_DELETE_ID = UUID.randomUUID();
    private static final UUID LIST_LINE_ITEM_PIPE_ID = UUID.randomUUID();
    private static final UUID LIST_LINE_ITEM_ELBOW_ID = UUID.randomUUID();
    private static final UUID LIST_LINE_ITEM_TO_ADD_ID = UUID.randomUUID();
    private static final UUID LIST_LINE_ITEM_OLD_ID = UUID.randomUUID();
    private static final UUID CART_ID = UUID.randomUUID();

    private static final Integer PIPE_QUANTITY = 2;
    private static final Integer ELBOW_QUANTITY = 3;
    private static final Integer ITEM_TO_ADD_QUANTITY = 14;
    private static final String ITEM_TO_ADD_ERP_PART_NUMBER = "472081";
    private static final UUID ITEM_TO_ADD_FIRST_LIST_ID = UUID.fromString("17be7006-26a0-48bb-b232-38f2ed2c746e");
    private static final UUID ITEM_TO_ADD_SECOND_LIST_ID = UUID.fromString("82a7c255-5f42-416e-84ef-7fc0c02a3dba");

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // List with products
        listLineItemPipe =
            new ListLineItem(LIST_LINE_ITEM_PIPE_ID, ERP_PART_NUMBER_PIPE, PIPE_QUANTITY, LIST_WITH_PRODUCTS_ID, 0);

        listLineItemElbow =
            new ListLineItem(LIST_LINE_ITEM_ELBOW_ID, ERP_PART_NUMBER_ELBOW, ELBOW_QUANTITY, LIST_WITH_PRODUCTS_ID, 1);

        // This will be added later to test the addItemToList func
        listLineItemToAdd =
            new ListLineItem(
                LIST_LINE_ITEM_TO_ADD_ID,
                ERP_PART_NUMBER_TO_ADD,
                ITEM_TO_ADD_QUANTITY,
                LIST_WITH_PRODUCTS_ID,
                2
            );

        // This fake item will be added to the oldlist to check getDetails func
        listLineItemOld = new ListLineItem(LIST_LINE_ITEM_OLD_ID, ERP_PART_NUMBER_OLD, 1, LIST_WITH_PRODUCTS_ID, 2);

        var lineItems = new HashSet<ListLineItem>();
        lineItems.add(listLineItemPipe);
        lineItems.add(listLineItemElbow);

        var lineItemsWithFakeItem = new HashSet<ListLineItem>();
        lineItemsWithFakeItem.add(listLineItemPipe);
        lineItemsWithFakeItem.add(listLineItemElbow);
        lineItemsWithFakeItem.add(listLineItemOld);

        listWithProducts = new List(LIST_WITH_PRODUCTS_ID, LIST_WITH_PRODUCTS_NAME, BILL_TO_ACCOUNT_ID, lineItems);

        listWithNoProducts =
            new List(LIST_WITH_NO_PRODUCTS_ID, LIST_WITH_NO_PRODUCTS_NAME, BILL_TO_ACCOUNT_ID, Collections.emptySet());

        listWithOneFakeProduct =
            new List(
                LIST_WITH_ONE_FAKE_PRODUCT_ID,
                LIST_WITH_ONE_FAKE_PRODUCT_NAME,
                BILL_TO_ACCOUNT_ID,
                lineItemsWithFakeItem
            );

        var listToCreateLineItems = new HashSet<ListLineItem>();
        listToCreateLineItems.add(listLineItemToAdd);

        listToCreate = new List(LIST_TO_CREATE_ID, LIST_TO_CREATE_NAME, BILL_TO_ACCOUNT_ID, listToCreateLineItems);

        listToCreateDTO = new CreateListDTO();
        listToCreateDTO.setBillToAccountId(BILL_TO_ACCOUNT_ID);
        listToCreateDTO.setName(LIST_TO_CREATE_NAME);
        listToCreateDTO.setListLineItems(
            listToCreateLineItems.stream().map(ListLineItemDTO::new).collect(Collectors.toList())
        );

        allLists = new ArrayList<List>();
        allLists.add(listWithNoProducts);
        allLists.add(listWithProducts);
        allLists.add(listWithOneFakeProduct);

        listToDelete = new List();
        listToDelete.setBillToAccountId(BILL_TO_ACCOUNT_ID);
        listToDelete.setId(LIST_TO_DELETE_ID);

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

        LineItems lineItems1 = new LineItems();
        lineItems1.setQuantity(1);
        lineItems1.setErpPartNumber("23456");
        java.util.List<LineItems> lineItemsList = new ArrayList<>();
        lineItemsList.add(lineItems1);
        // Mocks
        when(listsDAO.findById(LIST_WITH_PRODUCTS_ID)).thenReturn(Optional.of(listWithProducts));
        when(listsDAO.findById(LIST_TO_DELETE_ID)).thenReturn(Optional.of(listToDelete));
        when(listsDAO.findById(LIST_WITH_ONE_FAKE_PRODUCT_ID)).thenReturn(Optional.of(listWithOneFakeProduct));
        when(listsDAO.findAllByBillToAccountId(BILL_TO_ACCOUNT_ID)).thenReturn(allLists);
        when(listsDAO.save(any(List.class))).thenReturn(listWithProducts);
        when(listsDAO.saveAndFlush(any(List.class))).thenReturn(listToCreate);
        when(listLineItemsDAO.save(any(ListLineItem.class))).thenReturn(listLineItemToAdd);
        when(cartDAO.findById(CART_ID)).thenReturn(Optional.of(new Cart()));
        when(lineItemsDAO.findAllByCartId(any())).thenReturn(lineItemsList);
        when(listLineItemsDAO.saveAll(anyList())).thenReturn(new ArrayList<>());

        ProductResponse productResponse = new ProductResponse();
        MassProductInquiryResponse massProductInquiryResponse = new MassProductInquiryResponse();

        java.util.List<BranchAvailability> branchAvailabilityList2 = new ArrayList<>();
        BranchAvailability branchAvailability = new BranchAvailability();
        Branch branch2 = new Branch();
        branch2.setBranchId(BRANCH_ID);
        branchAvailability.setBranch(branch2);
        NowQuantity nowQuantity = new NowQuantity();
        Quantity quantity = new Quantity();
        quantity.setQuantity(10);
        nowQuantity.setQuantity(quantity);
        branchAvailability.setNowQuantity(nowQuantity);
        branchAvailabilityList2.add(branchAvailability);

        Pricing pricing = new Pricing();
        pricing.setCustomerPrice("10.0");

        ProductList productList = new ProductList();
        Product product1 = new Product();
        PartIdentifiers partIdentifiers1 = new PartIdentifiers();
        partIdentifiers1.setEclipsePartNumber(ERP_PART_NUMBER_PIPE);
        product1.setPartIdentifiers(partIdentifiers1);
        AvailabilityList availabilityList1 = new AvailabilityList();
        availabilityList1.setBranchAvailabilityList(branchAvailabilityList2);
        product1.setAvailabilityList(availabilityList1);
        product1.setPricing(pricing);

        Product product2 = new Product();
        PartIdentifiers partIdentifiers2 = new PartIdentifiers();
        partIdentifiers2.setEclipsePartNumber(ERP_PART_NUMBER_ELBOW);
        product2.setPartIdentifiers(partIdentifiers2);
        AvailabilityList availabilityList2 = new AvailabilityList();
        availabilityList2.setBranchAvailabilityList(branchAvailabilityList2);
        product2.setAvailabilityList(availabilityList2);
        product2.setPricing(pricing);

        java.util.List<Product> products = new ArrayList<>(Arrays.asList(product1, product2));
        productList.setProducts(products);
        massProductInquiryResponse.setProductList(productList);
        productResponse.setMassProductInquiryResponse(massProductInquiryResponse);
        when(erpService.getEclipseProductData(anyList(), any(), any())).thenReturn(productResponse);

        listService =
            new ListService(
                listsDAO,
                cartDAO,
                lineItemsDAO,
                listLineItemsDAO,
                productService,
                erpService,
                wishListDAO,
                accountService
            );
    }

    @Test
    public void createList_success() {
        listService.createList(listToCreateDTO);
        verify(listsDAO, times(1)).save(any(List.class));
    }

    @Test
    public void getList_success() throws Exception {
        var list = listService.getList(LIST_WITH_PRODUCTS_ID, BRANCH_ID);

        assertEquals(list.getId(), LIST_WITH_PRODUCTS_ID);
        assertEquals(list.getListLineItems().size(), 2);
    }

    @Test
    public void getDetails_works_for_missing_productItem() throws Exception {
        var list = this.listsDAO.findById(LIST_WITH_ONE_FAKE_PRODUCT_ID);
        var details = listService.getDetails(list.get(), BRANCH_ID);

        assertEquals(details.size(), 3);
        var itemDetails = details.stream().filter(d -> d.getId() == LIST_LINE_ITEM_OLD_ID).findFirst().get();
        assertNotNull(itemDetails);
        assertEquals(itemDetails.getErpPartNumber(), ERP_PART_NUMBER_OLD);
    }

    @Test
    public void getList_notFound_failure() {
        assertThrows(ListNotFoundException.class, () -> listService.getList(UUID.randomUUID(), null));
    }

    @Test
    public void getListsByBillToAccountId_success() {
        var response = listService.getListsByBillToAccountId(BILL_TO_ACCOUNT_ID);

        assertEquals(response.size(), 3);
        assertEquals(response.stream().findFirst().get().getBillToAccountId(), BILL_TO_ACCOUNT_ID);
    }

    @Test
    public void updateList_success() throws Exception {
        var listDTO = new ListDTO(listWithProducts);
        listDTO.setName("New Name");
        listService.updateList(listDTO);
        verify(listsDAO, times(1)).save(any(List.class));
    }

    @Test
    public void updateList_notFound_failure() {
        var listDTO = new ListDTO();
        listDTO.setId(UUID.randomUUID());
        assertThrows(ListNotFoundException.class, () -> listService.updateList(listDTO));
    }

    @Test
    public void addItemToList_success() throws Exception {
        var listLineItemDTO = new ListLineItemDTO(listLineItemToAdd);
        var response = listService.addItemToList(LIST_WITH_PRODUCTS_ID, listLineItemDTO);
        verify(listsDAO, times(1)).findById(LIST_WITH_PRODUCTS_ID);
        verify(listLineItemsDAO, times(1)).save(any(ListLineItem.class));
        assertEquals(response, LIST_LINE_ITEM_TO_ADD_ID);
    }

    @Test
    public void addItemToList_listNotFound_failure() {
        assertThrows(
            ListNotFoundException.class,
            () -> listService.addItemToList(UUID.randomUUID(), new ListLineItemDTO())
        );
    }

    @Test
    public void addItemToList_duplicateErpPartNumber_failure() throws Exception {
        var duplicateItem = new ListLineItemDTO();
        duplicateItem.setErpPartNumber(ERP_PART_NUMBER_ELBOW);
        assertThrows(
            ItemAlreadyInListException.class,
            () -> listService.addItemToList(LIST_WITH_PRODUCTS_ID, duplicateItem)
        );
    }

    @Test
    public void addAllCartItemsToLists_success() throws Exception {
        java.util.List<LineItems> lineItemsList = new ArrayList<>();
        LineItems lineItems = new LineItems();
        lineItems.setErpPartNumber("12345");
        lineItems.setQuantity(1);
        lineItemsList.add(lineItems);

        var response = listService.cartToList(LIST_WITH_PRODUCTS_ID, lineItemsList);
        verify(listsDAO, times(1)).findById(LIST_WITH_PRODUCTS_ID);
        verify(listLineItemsDAO, times(1)).save(any());
        assertEquals(response, LIST_WITH_PRODUCTS_ID);
    }

    @Test
    public void addAllCartItemsToLists_listNotFound_failure() {
        assertThrows(ListNotFoundException.class, () -> listService.cartToList(UUID.randomUUID(), new ArrayList<>()));
    }

    @Test
    public void addAllCartItemsToNewList_success() throws Exception {
        listService.addAllCartItemsToNewList("newlist", CART_ID, UUID.randomUUID());
        verify(cartDAO, times(1)).findById(any());
        verify(listLineItemsDAO, times(1)).saveAll(anyList());
    }

    @Test
    public void addAllCartItemsToNewList_cartNotFound_failure() {
        assertThrows(
            CartNotFoundException.class,
            () -> listService.addAllCartItemsToNewList("newlist", UUID.randomUUID(), UUID.randomUUID())
        );
    }

    @Test
    public void deleteList_success() throws Exception {
        var response = listService.deleteListById(LIST_TO_DELETE_ID);
        verify(listsDAO, times(1)).deleteById(eq(LIST_TO_DELETE_ID));
        assertTrue(response);
    }

    @Test
    public void deleteList_notFound_failure() {
        assertThrows(ListNotFoundException.class, () -> listService.deleteListById(UUID.randomUUID()));
    }

    @Test
    public void handleUploadedList_happyPath() throws JsonProcessingException, BranchServiceCoreException {
        ArrayList<UploadListData> mockData = new ArrayList<>();
        List mockList = new com.reece.platform.products.model.entity.List();
        mockList.setListLineItems(Set.of());
        mockList.setName("newlist");
        mockList.setId(UUID.randomUUID());

        mockData.add(new UploadListData("12345", "something", "bob", 10));
        mockData.add(new UploadListData("77777", "something", "bob", 15));
        mockData.add(new UploadListData("33333", "something", "bob", 1));

        when(listsDAO.save(any())).thenReturn(mockList);
        when(listsDAO.findById(mockList.getId())).thenReturn(Optional.of(mockList));
        when(productService.getProductByNumber(any(), any())).thenReturn(new ProductDTO());

        assertDoesNotThrow(() -> {
            var errors = listService.handleUploadNewList(mockData, UUID.randomUUID(), "newlist");
            assertEquals(errors.getErrors().size(), 0);
        });

        verify(listsDAO, times(1)).save(any());
        verify(listLineItemsDAO, times(3)).save(any());
    }

    @Test
    public void handleUploadedList_negativeQuantity() throws JsonProcessingException, BranchServiceCoreException {
        ArrayList<UploadListData> mockData = new ArrayList<>();
        List mockList = new com.reece.platform.products.model.entity.List();
        mockList.setListLineItems(Set.of());
        mockList.setName("newlist");
        mockList.setId(UUID.randomUUID());

        mockData.add(new UploadListData("12345", "something", "bob", -10));
        mockData.add(new UploadListData("77777", "something", "bob", 15));
        mockData.add(new UploadListData("33333", "something", "bob", 1));

        when(listsDAO.save(any())).thenReturn(mockList);
        when(listsDAO.findById(mockList.getId())).thenReturn(Optional.of(mockList));
        when(productService.getProductByNumber(any(), any())).thenReturn(new ProductDTO());

        assertDoesNotThrow(() -> {
            var errors = listService.handleUploadNewList(mockData, UUID.randomUUID(), "newlist");
            assertEquals(errors.getErrors().size(), 0);
        });

        verify(listsDAO, times(1)).save(any());
        verify(listLineItemsDAO, times(2)).save(any());
    }

    @Test
    public void handleUploadedList_itemNotFound() throws JsonProcessingException, BranchServiceCoreException {
        ArrayList<UploadListData> mockData = new ArrayList<>();
        com.reece.platform.products.model.entity.List mockList = new com.reece.platform.products.model.entity.List();
        mockList.setListLineItems(Set.of());
        mockList.setName("newlist");
        mockList.setId(UUID.randomUUID());

        mockData.add(new UploadListData("12345", "something", "bob", 10));
        mockData.add(new UploadListData("77777", "something", "bob", 15));
        mockData.add(new UploadListData("33333", "something", "bob", 1));

        when(listsDAO.save(any())).thenReturn(mockList);
        when(listsDAO.findById(mockList.getId())).thenReturn(Optional.of(mockList));
        when(productService.getProductByNumber("MSC-12345", ERPSystem.ECLIPSE)).thenReturn(null);
        when(productService.getProductByNumber("MSC-77777", ERPSystem.ECLIPSE)).thenReturn(new ProductDTO());
        when(productService.getProductByNumber("MSC-33333", ERPSystem.ECLIPSE)).thenReturn(new ProductDTO());

        assertDoesNotThrow(() -> {
            var errors = listService.handleUploadNewList(mockData, UUID.randomUUID(), "newlist");
            assertEquals(errors.getErrors().size(), 0);
        });

        verify(listsDAO, times(1)).save(any());
        verify(listLineItemsDAO, times(3)).save(any());
    }

    @Test
    public void handleUploadedList_duplicateExistingItem() throws JsonProcessingException, BranchServiceCoreException {
        ArrayList<UploadListData> mockData = new ArrayList<>();
        List mockList = new com.reece.platform.products.model.entity.List();
        ListLineItem existingLineItem = new ListLineItem();
        existingLineItem.setErpPartNumber("12345");
        existingLineItem.setQuantity(10);
        existingLineItem.setListId(UUID.randomUUID());
        existingLineItem.setSortOrder(0);

        mockList.setListLineItems(Set.of());
        mockList.setName("newlist");
        mockList.setId(UUID.randomUUID());
        mockList.setListLineItems(Set.of(existingLineItem));

        mockData.add(new UploadListData("12345", "something", "bob", 15));
        mockData.add(new UploadListData("33333", "something", "bob", 1));

        when(listsDAO.save(any())).thenReturn(mockList);
        when(listsDAO.findById(mockList.getId())).thenReturn(Optional.of(mockList));
        when(listLineItemsDAO.getOne(existingLineItem.getId())).thenReturn(existingLineItem);
        when(productService.getProductByNumber(any(), any())).thenReturn(new ProductDTO());

        assertDoesNotThrow(() -> {
            var errors = listService.handleUploadNewList(mockData, UUID.randomUUID(), "newlist");
            assertEquals(errors.getErrors().size(), 1);
        });

        verify(listsDAO, times(1)).save(any());
        verify(listLineItemsDAO, times(2)).save(any());
        assertEquals(existingLineItem.getQuantity(), 25);
    }

    @Test
    public void handleUploadedList_duplicateNewItem() throws JsonProcessingException, BranchServiceCoreException {
        ArrayList<UploadListData> mockData = new ArrayList<>();
        List mockList = new com.reece.platform.products.model.entity.List();

        mockList.setListLineItems(Set.of());
        mockList.setName("newlist");
        mockList.setId(UUID.randomUUID());
        mockList.setListLineItems(Set.of());

        mockData.add(new UploadListData("12345", "blah", "blah", 15));
        mockData.add(new UploadListData("12345", "something", "bob", 15));
        mockData.add(new UploadListData("33333", "something", "bob", 1));

        when(listsDAO.save(any())).thenReturn(mockList);
        when(listsDAO.findById(mockList.getId())).thenReturn(Optional.of(mockList));
        when(productService.getProductByNumber(any(), any())).thenReturn(new ProductDTO());

        assertDoesNotThrow(() -> {
            var errors = listService.handleUploadNewList(mockData, UUID.randomUUID(), "newlist");
            assertEquals(errors.getErrors().size(), 0);
            assertEquals(errors.getSuccessfulRowCount(), 3);
        });

        verify(listsDAO, times(1)).save(any());
        verify(listLineItemsDAO, times(2)).save(any());
    }

    @Test
    public void handleUploadedList_emptyList() {
        ArrayList<UploadListData> mockData = new ArrayList<>();
        List mockList = new com.reece.platform.products.model.entity.List();

        mockList.setListLineItems(Set.of());
        mockList.setName("newlist");
        mockList.setId(UUID.randomUUID());
        mockList.setListLineItems(Set.of());

        when(listsDAO.saveAndFlush(any())).thenReturn(mockList);

        assertDoesNotThrow(() -> {
            var errors = listService.handleUploadNewList(mockData, UUID.randomUUID(), "newlist");
            assertEquals(errors.getErrors().size(), 0);
        });

        verify(listsDAO, times(1)).save(any());
        verify(listLineItemsDAO, times(0)).save(any());
    }

    @Test
    public void handleUploadNewList_happyPath() {
        ArrayList<UploadListData> mockData = new ArrayList<>();

        assertDoesNotThrow(() -> {
            var errors = listService.handleUploadNewList(mockData, UUID.randomUUID(), "newlist");
            assertEquals(errors.getErrors().size(), 0);
        });

        verify(listsDAO, times(1)).save(any());
    }

    @Test
    public void syncListsFromInsite_success() {
        val erpAccountId = "11362";
        val billToAccountId = UUID.randomUUID();
        val customer = new Customer(UUID.randomUUID(), erpAccountId);

        val wishListOneId = UUID.randomUUID();
        val wishListTwoId = UUID.randomUUID();

        val wishListProductOne = new WishListProduct(
            UUID.randomUUID(),
            1,
            0,
            null,
            new com.reece.platform.products.insite.entity.Product(UUID.randomUUID(), "1234")
        );
        val wishListProductTwo = new WishListProduct(
            UUID.randomUUID(),
            3,
            1,
            null,
            new com.reece.platform.products.insite.entity.Product(UUID.randomUUID(), "3481")
        );
        val wishListProductThree = new WishListProduct(
            UUID.randomUUID(),
            23,
            2,
            null,
            new com.reece.platform.products.insite.entity.Product(UUID.randomUUID(), "10493")
        );

        val wishListProductsOne = Arrays.asList(wishListProductOne, wishListProductTwo);
        val wishListProductsTwo = Arrays.asList(wishListProductOne, wishListProductTwo, wishListProductThree);

        val existingWishListName = "Wish List One";
        val wishListOne = new WishList(wishListOneId, existingWishListName, customer, wishListProductsOne);
        val wishListTwo = new WishList(wishListTwoId, "Wish List Two", customer, wishListProductsTwo);

        val wishLists = Arrays.asList(wishListOne, wishListTwo);

        val existingList = new List();
        existingList.setName(existingWishListName);

        when(wishListDAO.findAllByCustomerErpAccountId(erpAccountId)).thenReturn(wishLists);
        when(accountService.getEcommBillToId(erpAccountId, "ECLIPSE")).thenReturn(billToAccountId);
        when(listsDAO.findAllByMatchingNameAndBillToAccountId(existingWishListName, billToAccountId))
            .thenReturn(Arrays.asList(existingList));

        listService.syncListsFromInsite(erpAccountId, ErpEnum.ECLIPSE);

        verify(listsDAO, times(2)).save(listArgumentCaptor.capture());
        val capturedLists = listArgumentCaptor.getAllValues();

        assertEquals("Wish List One (1)", capturedLists.get(0).getName());
        assertEquals("Wish List Two", capturedLists.get(1).getName());

        verify(listLineItemsDAO, times(2)).saveAll(listLineItemsArgumentCaptor.capture());
        val capturedLineItems = listLineItemsArgumentCaptor.getAllValues();

        assertEquals(2, capturedLineItems.get(0).size());
        assertEquals(3, capturedLineItems.get(1).size());
    }

    @Test
    public void handleDuplicateList() {
        ArrayList<UploadListData> mockData = new ArrayList<>();
        List mockList = new com.reece.platform.products.model.entity.List();
        mockList.setListLineItems(Set.of());
        mockList.setName("newlist");
        mockList.setId(UUID.randomUUID());
        mockList.setListLineItems(Set.of());
        when(listsDAO.findCountByMatchingNameAndBillToAccountId(any(), any())).thenReturn(Integer.valueOf(1));
        var isDuplicate = listService.isDuplicate(mockList.getName(), UUID.randomUUID());
        assertEquals(isDuplicate, true);

        verify(listsDAO, times(1)).findCountByMatchingNameAndBillToAccountId(any(), any());
    }

    @Test
    public void toggleItemFromLists_itemNotFound() throws Exception {
        java.util.List<List> mockLists = new ArrayList<>();

        List mockFistList = new com.reece.platform.products.model.entity.List();
        mockFistList.setId(ITEM_TO_ADD_FIRST_LIST_ID);

        ListLineItem listLineItem = new ListLineItem();
        listLineItem.setListId(ITEM_TO_ADD_FIRST_LIST_ID);
        listLineItem.setId(UUID.randomUUID());
        listLineItem.setErpPartNumber(ITEM_TO_ADD_ERP_PART_NUMBER);

        java.util.Set<ListLineItem> listLineItems = new HashSet<>();
        listLineItems.add(listLineItem);

        mockFistList.setListLineItems(listLineItems);
        mockLists.add(mockFistList);
        Optional<List> List = Optional.of(mockFistList);

        when(productService.getProductByNumber(any(), any())).thenReturn(null);
        when(listLineItemsDAO.findAllAddedItemListsByBillToAccountIdAndPartNumber(any(), any())).thenReturn(null);
        when(listsDAO.findById(ITEM_TO_ADD_FIRST_LIST_ID)).thenReturn(List);
        when(listsDAO.findAllById(toggleListLineItemDTO.getListIds())).thenReturn(mockLists);
        when(listsDAO.save(any())).thenReturn(mockFistList);

        assertThrows(AddItemToListFoundException.class, () -> listService.toggleItemFromLists(toggleListLineItemDTO));
    }

    @Test
    public void toggleItemFromLists_success() throws Exception {
        java.util.List<List> mockLists = new ArrayList<>();
        List mockFistList = new com.reece.platform.products.model.entity.List();
        mockFistList.setId(ITEM_TO_ADD_FIRST_LIST_ID);

        ListLineItem listLineItem = new ListLineItem();
        listLineItem.setListId(ITEM_TO_ADD_FIRST_LIST_ID);
        listLineItem.setId(UUID.randomUUID());
        listLineItem.setErpPartNumber("1234");

        java.util.Set<ListLineItem> listLineItems = new HashSet<>();
        listLineItems.add(listLineItem);

        mockFistList.setListLineItems(listLineItems);
        mockLists.add(mockFistList);

        Optional<List> list = Optional.of(mockFistList);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setPartNumber(ITEM_TO_ADD_ERP_PART_NUMBER);
        listLineItem.setListId(ITEM_TO_ADD_FIRST_LIST_ID);

        var largestSortOrder = mockFistList
            .getListLineItems()
            .stream()
            .max(Comparator.comparing(ListLineItem::getSortOrder));
        listLineItem.setSortOrder(0);

        when(productService.getProductByNumber(any(), any())).thenReturn(productDTO);
        when(listLineItemsDAO.findAllAddedItemListsByBillToAccountIdAndPartNumber(any(), any())).thenReturn(null);
        when(listsDAO.findById(ITEM_TO_ADD_FIRST_LIST_ID)).thenReturn(list);
        when(listsDAO.findAllById(toggleListLineItemDTO.getListIds())).thenReturn(mockLists);
        when(listsDAO.save(any())).thenReturn(mockFistList);

        var response = listService.toggleItemFromLists(toggleListLineItemDTO);
        verify(listLineItemsDAO, times(1)).save(any(ListLineItem.class));
        assertEquals(1, response.getLists().size());
    }

    @Test
    public void toggleItemFromLists_removeUnselected() throws Exception {
        java.util.List<List> mockLists = new ArrayList<>();

        List mockFistList = new com.reece.platform.products.model.entity.List();
        mockFistList.setId(ITEM_TO_ADD_FIRST_LIST_ID);
        mockFistList.setBillToAccountId(BILL_TO_ACCOUNT_ID);
        ListLineItem listLineItem = new ListLineItem();
        listLineItem.setListId(ITEM_TO_ADD_FIRST_LIST_ID);
        listLineItem.setId(UUID.randomUUID());
        listLineItem.setErpPartNumber(ITEM_TO_ADD_ERP_PART_NUMBER);
        toggleListLineItemDTO.setBillToAccountId(BILL_TO_ACCOUNT_ID);

        java.util.List<ListLineItem> addItemInLists = new ArrayList<>();
        addItemInLists.add(listLineItem);

        java.util.Set<ListLineItem> listLineItems = new HashSet<>();
        listLineItems.add(listLineItem);

        mockFistList.setListLineItems(listLineItems);
        mockLists.add(mockFistList);
        Optional<List> List = Optional.of(mockFistList);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setPartNumber(ITEM_TO_ADD_ERP_PART_NUMBER);

        java.util.List<UUID> duplicateItemInList = new ArrayList<>();
        duplicateItemInList.add(UUID.fromString("82a7c255-5f42-416e-84ef-7fc0c02a3dba"));
        toggleListLineItemDTO.setListIds(duplicateItemInList);

        when(productService.getProductByNumber(any(), any())).thenReturn(productDTO);
        when(
            listLineItemsDAO.findAllAddedItemListsByBillToAccountIdAndPartNumber(
                BILL_TO_ACCOUNT_ID,
                ITEM_TO_ADD_ERP_PART_NUMBER
            )
        )
            .thenReturn(addItemInLists);
        when(listsDAO.findById(ITEM_TO_ADD_FIRST_LIST_ID)).thenReturn(List);
        when(listsDAO.findAllById(toggleListLineItemDTO.getListIds())).thenReturn(mockLists);
        when(listsDAO.save(any())).thenReturn(mockFistList);

        var response = listService.toggleItemFromLists(toggleListLineItemDTO);
        verify(listLineItemsDAO, times(1)).deleteById(listLineItem.getId());
        assertEquals(1, response.getLists().size());
    }

    @Test
    public void toggleItemFromLists_remove_item() throws Exception {
        java.util.List<List> mockLists = new ArrayList<>();

        List mockFistList = new com.reece.platform.products.model.entity.List();
        mockFistList.setId(ITEM_TO_ADD_FIRST_LIST_ID);
        mockFistList.setBillToAccountId(BILL_TO_ACCOUNT_ID);
        ListLineItem listLineItem = new ListLineItem();
        listLineItem.setListId(ITEM_TO_ADD_FIRST_LIST_ID);
        listLineItem.setId(UUID.randomUUID());
        listLineItem.setErpPartNumber(ITEM_TO_ADD_ERP_PART_NUMBER);

        java.util.List<ListLineItem> addItemInLists = new ArrayList<>();
        addItemInLists.add(listLineItem);

        java.util.Set<ListLineItem> listLineItems = new HashSet<>();
        listLineItems.add(listLineItem);

        mockFistList.setListLineItems(listLineItems);
        mockLists.add(mockFistList);
        Optional<List> List = Optional.of(mockFistList);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setPartNumber(ITEM_TO_ADD_ERP_PART_NUMBER);
        toggleListLineItemDTO.getListLineItemDTO().setQuantity(null);

        when(productService.getProductByNumber(any(), any())).thenReturn(productDTO);
        when(
            listLineItemsDAO.findAllAddedItemListsByBillToAccountIdAndPartNumber(
                mockFistList.getBillToAccountId(),
                ITEM_TO_ADD_ERP_PART_NUMBER
            )
        )
            .thenReturn(addItemInLists);
        when(listsDAO.findById(ITEM_TO_ADD_FIRST_LIST_ID)).thenReturn(List);
        when(listsDAO.findAllById(toggleListLineItemDTO.getListIds())).thenReturn(mockLists);
        when(listsDAO.save(any())).thenReturn(mockFistList);

        var response = listService.toggleItemFromLists(toggleListLineItemDTO);
        verify(listLineItemsDAO, times(1)).deleteById(any());
        assertEquals(1, response.getLists().size());
    }

    @Test
    public void exportListIntoCSV_Sucess() throws Exception {
        java.util.List<ListLineItemDTO> listLineItemDTO = new ArrayList<ListLineItemDTO>();
        ListLineItemDTO listLineItem1 = new ListLineItemDTO();
        listLineItem1.setErpPartNumber("45088");
        listLineItem1.setName("Multichoice Universal Tub / Shower Rough - Pex Crimp");
        listLineItem1.setManufacturerName("Delta");
        listLineItem1.setQuantity(1);
        listLineItem1.setPricePerUnit(0);
        listLineItem1.setManufacturerNumber("R10000-PX");

        listLineItemDTO.add(listLineItem1);

        ListLineItemDTO listLineItem2 = new ListLineItemDTO();
        listLineItem2.setErpPartNumber("1184848");
        listLineItem2.setName("M-Pact Posi-Temp Pressure Balancing Valve Crimp Ring Pex");
        listLineItem2.setManufacturerName("Moen");
        listLineItem2.setQuantity(1);
        listLineItem2.setPricePerUnit(0);
        listLineItem2.setManufacturerNumber("FP62380PF");

        listLineItemDTO.add(listLineItem2);
        fistList.setListLineItems(listLineItemDTO);

        var list = listService.exportListIntoCSV(fistList);

        assertNotNull(list);
    }

    @Test
    public void exportListIntoCSV_failure() throws Exception {
        assertThrows(ExportListIntoCSVFoundException.class, () -> listService.exportListIntoCSV(fistList));
    }

    @Test
    public void checkFile_Sucess() throws Exception {
        java.util.List<UploadListData> listData = new ArrayList();
        UploadListData uploadListData = new UploadListData();
        uploadListData.setPartNumber("MSC-123");
        uploadListData.setQuantity(1);
        listData.add(uploadListData);

        java.util.List<ProductDTO> productDTOS = new ArrayList();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setPartNumber("123");
        productDTOS.add(productDTO);
        java.util.List<String> errors = new ArrayList();
        java.util.List<String> productIds = new ArrayList();
        productIds.add("123");

        when(productService.getProductsByNumber(productIds)).thenReturn(productDTOS);
        listService.checkFile(listData, errors);

        assertEquals(errors.size(), 0);
    }

    @Test
    public void checkFile_failure() throws Exception {
        java.util.List<UploadListData> listData = new ArrayList();
        UploadListData uploadListData = new UploadListData();
        uploadListData.setPartNumber("MSC-123");
        uploadListData.setQuantity(1);
        listData.add(uploadListData);
        java.util.List<ProductDTO> productDTOS = new ArrayList();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setPartNumber("MSC-123");
        productDTOS.add(productDTO);
        java.util.List<String> errors = new ArrayList();
        java.util.List<String> productIds = new ArrayList();
        productIds.add("123");

        when(productService.getProductsByNumber(productIds)).thenReturn(productDTOS);
        listService.checkFile(listData, errors);

        assertEquals(errors.size(), 1);
    }

    @Test
    public void checkFile_QuantityNullFailure() throws Exception {
        java.util.List<UploadListData> listData = new ArrayList();
        UploadListData uploadListData = new UploadListData();
        uploadListData.setPartNumber("MSC-123");
        uploadListData.setQuantity(-1);
        listData.add(uploadListData);
        java.util.List<ProductDTO> productDTOS = new ArrayList();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setPartNumber("123");
        productDTOS.add(productDTO);
        java.util.List<String> errors = new ArrayList();
        java.util.List<String> productIds = new ArrayList();
        productIds.add("123");

        when(productService.getProductsByNumber(productIds)).thenReturn(productDTOS);
        listService.checkFile(listData, errors);

        assertEquals(errors.size(), 1);
    }

    @Test
    public void checkFile_NegativeQuantityFailure() throws Exception {
        java.util.List<UploadListData> listData = new ArrayList();
        UploadListData uploadListData = new UploadListData();
        uploadListData.setPartNumber("MSC-123");
        listData.add(uploadListData);
        java.util.List<ProductDTO> productDTOS = new ArrayList();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setPartNumber("123");
        productDTOS.add(productDTO);
        java.util.List<String> errors = new ArrayList();
        java.util.List<String> productIds = new ArrayList();
        productIds.add("123");

        when(productService.getProductsByNumber(productIds)).thenReturn(productDTOS);
        listService.checkFile(listData, errors);

        assertEquals(errors.size(), 1);
    }
}
