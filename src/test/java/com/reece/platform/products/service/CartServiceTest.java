package com.reece.platform.products.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.AdditionalMatchers.or;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.reece.platform.products.branches.model.DTO.BranchResponseDTO;
import com.reece.platform.products.branches.service.BranchesService;
import com.reece.platform.products.exceptions.*;
import com.reece.platform.products.helpers.ERPSystem;
import com.reece.platform.products.model.*;
import com.reece.platform.products.model.DTO.*;
import com.reece.platform.products.model.eclipse.common.EclipseAddressResponseDTO;
import com.reece.platform.products.model.entity.*;
import com.reece.platform.products.model.repository.*;
import com.reece.platform.products.orders.model.WebStatus;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;

class CartServiceTest {

    @Mock
    private CartDAO cartDAO;

    @Mock
    private OrderStatusDAO orderStatusDAO;

    @Mock
    private LineItemsDAO lineItemsDAO;

    @Mock
    private ListsDAO listsDAO;

    @Mock
    private AddressDAO addressDAO;

    @Mock
    private OrdersPendingApprovalDAO ordersPendingApprovalDAO;

    @Mock
    private WillCallDAO willCallDAO;

    @Mock
    private DeliveryDAO deliveryDAO;

    @Mock
    private ApprovalFlowStateDAO approvalFlowStateDAO;

    @Mock
    private AccountService accountService;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private ErpService erpService;

    @Mock
    private ProductService productService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private BranchesService branchesService;

    @Mock
    private UserDAO userDAO;

    @Captor
    private ArgumentCaptor<LineItems> lineItemsArgumentCaptor;

    @Captor
    private ArgumentCaptor<List<LineItems>> lineItemsListArgumentCaptor;

    @Captor
    private ArgumentCaptor<Cart> cartArgumentCaptor;

    @Captor
    private ArgumentCaptor<WillCall> willCallArgumentCaptor;

    @Captor
    private ArgumentCaptor<Address> addressArgumentCaptor;

    @Captor
    private ArgumentCaptor<Delivery> deliveryArgumentCaptor;

    @Captor
    private ArgumentCaptor<CreateSalesOrderRequestDTO> salesOrderDTOArgumentCaptor;

    @Captor
    private ArgumentCaptor<SalesOrderSubmitNotificationDTO> salesOrderSubmitNotificationDTOArgumentCaptor;

    private CartService cartService;

    private Cart getCartDefault;
    private Cart getCartReset;
    private Cart getCartNoProducts;
    private Cart getCartWithProducts;
    private Cart getCartWithCustomAddress;
    private Cart getCartWithProductsWrongBranch;
    private Cart cartWillCallBranch;
    private Cart getCartNullBranch;
    private Cart getCartUnavailableItems;

    private CreditCardDTO creditCardWithDelivery = new CreditCardDTO();
    private com.reece.platform.products.model.entity.List listWithProducts;
    private com.reece.platform.products.model.entity.List listWithProductsLimit;
    private ListLineItem listLineItemPipe;
    private List<LineItems> lineItems;
    private List<LineItems> lineItemsUnexpired;
    private List<LineItems> listItemUnavailable;
    private List<LineItemDTO> lineItemDTOList1;

    private WillCallDTO willCallDTO;
    private DeliveryDTO deliveryDTO;
    private DeliveryDTO deliveryBadAccountDTO;
    private ProductDTO productDTO;
    private SubmitOrderDTO submitOrderDTO;

    private Address address;
    private Address customAddress;
    private GetOrderResponseDTO genericGetOrderResponseDTO;

    private static final String ERP_PRICE = "10.0";
    private static final String HOME_BRANCH_ID = "HOME_BRANCH_ID";
    private static final String UPDATE_WILL_CALL_BRANCH = "UPDATE_WILL_CALL_BRANCH";
    private static final String HOME_BRANCH_NAME = "HOME_BRANCH_NAME";
    private static final String BASE_BRANCH_ID = "BASE_BRANCH_ID";
    private static final String WRONG_BRANCH_ID = "WRONG";
    private static final String PICKUP_INSTRUCTIONS = "PICKUP_INSTRUCTIONS";
    private static final String DELIVERY_INSTRUCTIONS = "DELIVERY_INSTRUCTIONS";
    private static final String ERP_ACCOUNT_ID = UUID.randomUUID().toString();
    private static final String BILL_TO_ACCOUNT_ID = UUID.randomUUID().toString();
    private static final String PHONE_NUMBER = "111-111-1111";
    private static final String UOM_PRICING = "ea";
    private static final String HOURS = "Hours";
    private static final String BRAND = "Brand";
    private static final String DOMAIN = "Domain";

    private static final String ZIP_CODE = "zip";
    private static final String STATE = "state";
    private static final String COUNTRY = "country";
    private static final String CITY = "city";
    private static final String STREET1 = "street1";
    private static final String STREET2 = "street2";
    private static final Boolean CUSTOM_ADDRESS = true;
    public static final String POSTAL = "Postal";
    public static final String PHONE = "Phone";

    private static final UUID CART_DEFAULT = UUID.randomUUID();
    private static final UUID CART_EXISTS = UUID.randomUUID();
    private static final UUID CART_PRODUCTS_UNEXPIRED = UUID.randomUUID();
    private static final UUID CART_PRODUCTS_EXPIRED = UUID.randomUUID();
    private static final UUID CART_NOT_FOUND = UUID.randomUUID();
    private static final UUID CART_PRODUCTS_BRANCH_NOT_FOUND = UUID.randomUUID();
    private static final UUID CART_NO_DELIVERY_OR_WILL_CALL = UUID.randomUUID();
    private static final UUID CART_ID_WILL_CALL_BRANCH = UUID.randomUUID();

    private static final UUID USER_EXISTS = UUID.randomUUID();
    private static final UUID ACCOUNT_EXISTS = UUID.randomUUID();
    private static final UUID USER_NOT_FOUND = UUID.randomUUID();
    private static final UUID ACCOUNT_NOT_FOUND = UUID.randomUUID();

    private static final UUID USER_ALREADY_CREATED_CART = UUID.randomUUID();
    private static final UUID ACCOUNT_ALREADY_CREATED_CART = UUID.randomUUID();

    private static final UUID CART_ID = UUID.randomUUID();
    private static final UUID CART_ID_WILL_CALL = UUID.randomUUID();
    private static final UUID CART_ID_DELIVERY = UUID.randomUUID();
    private static final UUID CART_ID_CUSTOM_ADDRESS = UUID.randomUUID();
    private static final UUID CART_ID_UNAVAILABLE_ITEMS = UUID.randomUUID();
    private static final UUID ERP_ID = UUID.randomUUID();
    private static final UUID DELIVERY_ID = UUID.randomUUID();
    private static final UUID DELIVERY_ID_CUSTOM_ADDRESS = UUID.randomUUID();
    private static final UUID DELIVERY_ID_DEFAULT = UUID.randomUUID();
    private static final UUID WILL_CALL_ID = UUID.randomUUID();
    private static final UUID ITEM_ID = UUID.randomUUID();
    private static final UUID ITEM_ID_NOT_FOUND = UUID.randomUUID();
    private static final UUID APPROVAL_STATE_ID = UUID.randomUUID();
    private static final UUID ADDRESS_ID = UUID.randomUUID();
    private static final UUID ADDRESS_ID_CUSTOM = UUID.randomUUID();
    private static final UUID BAD_SHIP_TO_ID = UUID.randomUUID();
    private static final UUID CART_NULL_BRANCH = UUID.randomUUID();
    private static final UUID SHIP_TO_ID = UUID.randomUUID();
    private static final UUID LIST_WITH_PRODUCTS_ID = UUID.randomUUID();
    private static final UUID LIST_WITH_PRODUCTS_LIMIT_ID = UUID.randomUUID();
    private static final UUID BILL_TO_ACCOUNT_ID_UUID = UUID.randomUUID();
    private static final String LIST_WITH_PRODUCTS_NAME = "List with Products";
    private static final String LIST_WITH_PRODUCTS_LIMIT_NAME = "list with products limit";
    private static final UUID LIST_LINE_ITEM_PIPE_ID = UUID.randomUUID();
    private static final String ERP_PART_NUMBER_PIPE = "12345";
    private static final Integer PIPE_QUANTITY = 2;

    private static final Date orderDate = new Date();
    private static final PreferredTimeEnum orderTime = PreferredTimeEnum.AFTERNOON;

    private ErpUserInformation erpUserInformation;
    private ErpUserInformation erpUserInformationWrongBranch;
    private ErpUserInformation erpUserInformationMincron;

    private static final int QUANTITY = 5;
    private static final int ERP_QUANTITY = 321;
    private static final String PRODUCT_ID = "123";
    private static final String UNAVAILABLE_PRODUCT_ID = "123-unavailable";
    private static final String UNAVAILABLE_PRODUCT_DESCRIPTION = "desc_unavail";
    private static final String CUSTOMER_PART_NUMBER = "999";
    private static final String AUTH_TOKEN =
        "Bearer eyJraWQiOiItYlVObXhLMndvLUR4OFg5elRlb0drTU1DZWFlMW8wRnVjYzdNVWV1QkZ3IiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULjVhNzNyOEtQRHJXLWJZMkJQenlKWUJQRnFucVZqdlJEcEdMU1k0c1lYYkkiLCJpc3MiOiJodHRwczovL2Rldi00MzI1NDYub2t0YS5jb20vb2F1dGgyL2RlZmF1bHQiLCJhdWQiOiJhcGk6Ly9kZWZhdWx0IiwiaWF0IjoxNjE1ODE5NzMwLCJleHAiOjE2MTU5MDYxMzAsImNpZCI6IjBvYTEzYjBiNWJNS1haZkpVNHg3IiwidWlkIjoiMDB1MmQzMXBpejVLS0Y2c2I0eDciLCJzY3AiOlsiZW1haWwiLCJvcGVuaWQiLCJwcm9maWxlIl0sInN1YiI6InNkZmtqYnJhc250QHRlc3QuY29tIiwiZWNvbW1Vc2VySWQiOiI5ZmQxNjJiOS1iMzU0LTRkMDctYWM1My0yMDkxYjFmMmJlYzIiLCJlY29tbVBlcm1pc3Npb25zIjpbImVkaXRfcHJvZmlsZSIsImVkaXRfbGlzdCIsInN1Ym1pdF9xdW90ZV9vcmRlciIsImludml0ZV91c2VyIiwidmlld19pbnZvaWNlIiwiYXBwcm92ZV9jYXJ0IiwibWFuYWdlX3BheW1lbnRfbWV0aG9kcyIsImFwcHJvdmVfYWNjb3VudF91c2VyIiwibWFuYWdlX3JvbGVzIiwic3VibWl0X2NhcnRfd2l0aG91dF9hcHByb3ZhbCJdfQ.Ew3owigdqaPNQDUiG1kOxqkSZFFRHxFB2qAlzPiAIkT4ruXTqad9-cGZGBkhRYLjxhuuIJ2tdilb1nwuxNKEcZbxOBTrXkrfubh-_FrDkHjVVa59mvcXbgd1gCcADd3bs_wZCzpyJDBF_LQ3h5cYOtgvwubMQ-71Hi127fP-SgVwYmb38bMVY4IKTkja-vCeoixKGjk8p_1YRCvdpfnGxJaP9vgUqGP1-ebnN5EzO0WlkC7IHtnjXcHZdUHtIKA6NCkvlCI127_Rh8gCU6mRVoQdHjKqya8zvSkNuKAz8NijsJAk3gLSJo4Fxv_7qedBDW5XbmBaaHhw-pBezn0JSQ";

    private static final String URL = "URL";
    private static final String PRODUCT_NAME = "Product Name";
    private static final String PART_NUMBER = "Part Number";
    private static final String MANUFACTURER_NAME = "Manufacturer Name";
    private static final String PURCHASE_ORDER_NUMBER = "PO Number";
    private static final int ORDER_TOTAL = 90;
    private static final String FIRST_NAME = "FIRST";
    private static final String LAST_NAME = "LAST";
    private static final String FULL_NAME = FIRST_NAME + " " + LAST_NAME;
    private static final Date SUBMITTED_DATE = DateUtil.now();
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private final String SUBMITTED_DATE_FORMATTED = dateFormat.format(SUBMITTED_DATE);

    private static final String AWAITING_APPROVAL_ORDER_ID = "AA09898098";

    private static final String UOM = "ea";

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        Delivery delivery = new Delivery();
        delivery.setId(DELIVERY_ID);

        address = new Address();
        address.setId(ADDRESS_ID);
        address.setZip(ZIP_CODE);
        address.setState(STATE);
        address.setCountry(COUNTRY);
        address.setCity(CITY);
        address.setStreet1(STREET1);
        address.setStreet2(STREET2);

        delivery.setAddress(address);

        Delivery deliveryWithCustomAddress = new Delivery();
        deliveryWithCustomAddress.setId(DELIVERY_ID_CUSTOM_ADDRESS);
        deliveryWithCustomAddress.setDeliveryInstructions(DELIVERY_INSTRUCTIONS);
        deliveryWithCustomAddress.setShouldShipFullOrder(true);
        deliveryWithCustomAddress.setPreferredDate(orderDate);
        deliveryWithCustomAddress.setPreferredTime(orderTime);

        Delivery deliveryDefault = new Delivery();
        deliveryDefault.setId(DELIVERY_ID_DEFAULT);

        Delivery deliveryReset = new Delivery();
        deliveryReset.setId(DELIVERY_ID_DEFAULT);
        deliveryReset.setAddress(address);
        deliveryReset.setDeliveryInstructions(DELIVERY_INSTRUCTIONS);
        deliveryReset.setShouldShipFullOrder(true);
        deliveryReset.setPreferredDate(orderDate);
        deliveryReset.setPreferredTime(orderTime);

        customAddress = new Address();
        customAddress.setId(ADDRESS_ID_CUSTOM);
        customAddress.setCompanyName(HOME_BRANCH_NAME);
        customAddress.setStreet1(STREET1);
        customAddress.setStreet2(STREET2);
        customAddress.setCity(CITY);
        customAddress.setState(STATE);
        customAddress.setZip(ZIP_CODE);
        customAddress.setCountry(COUNTRY);
        customAddress.setCustom(CUSTOM_ADDRESS);

        deliveryWithCustomAddress.setAddress(customAddress);

        WillCall willCall = new WillCall();
        willCall.setId(WILL_CALL_ID);

        var approverId = UUID.randomUUID();

        User approver = new User();
        User user = new User();
        user.setId(USER_EXISTS);
        approver.setId(approverId);
        user.setApprover(approver);

        var userDTO = new UserDTO();
        userDTO.setId(USER_EXISTS);
        userDTO.setIsEmployee(false);
        userDTO.setEcommShipToIds(List.of(SHIP_TO_ID));
        userDTO.setFirstName(FIRST_NAME);
        userDTO.setLastName(LAST_NAME);
        userDTO.setApproverId(approverId);

        var approverDTO = new UserDTO();
        approverDTO.setId(approverId);

        getCartDefault = new Cart();
        getCartDefault.setId(CART_DEFAULT);
        getCartDefault.setApprovalState(APPROVAL_STATE_ID);
        getCartDefault.setDelivery(deliveryDefault);
        getCartDefault.setDeliveryMethod(DeliveryMethodEnum.DELIVERY);
        getCartDefault.setWillCall(willCall);
        getCartDefault.setShipToId(ACCOUNT_EXISTS);
        getCartDefault.setShippingBranchId(HOME_BRANCH_ID);
        getCartDefault.setErpSystemName(ERPSystem.ECLIPSE);
        getCartDefault.setPaymentMethodType(PaymentMethodTypeEnum.BILLTOACCOUNT);
        getCartDefault.setApproverId(approverDTO.getId());

        getCartReset = new Cart();
        getCartReset.setId(CART_DEFAULT);
        getCartReset.setApprovalState(APPROVAL_STATE_ID);
        getCartReset.setDelivery(deliveryReset);
        getCartReset.setWillCall(willCall);
        getCartReset.setShippingBranchId(HOME_BRANCH_ID);
        getCartReset.setErpSystemName(ERPSystem.ECLIPSE);

        getCartNoProducts = new Cart();
        getCartNoProducts.setApprovalState(APPROVAL_STATE_ID);
        getCartNoProducts.setDelivery(delivery);
        getCartNoProducts.setWillCall(willCall);
        getCartNoProducts.setId(CART_ID);
        getCartNoProducts.setShippingBranchId(HOME_BRANCH_ID);
        getCartNoProducts.setErpSystemName(ERPSystem.ECLIPSE);

        getCartUnavailableItems = new Cart();
        getCartUnavailableItems.setApprovalState(APPROVAL_STATE_ID);
        getCartUnavailableItems.setDelivery(delivery);
        getCartUnavailableItems.setWillCall(willCall);
        getCartUnavailableItems.setId(CART_ID_UNAVAILABLE_ITEMS);
        getCartUnavailableItems.setShippingBranchId(HOME_BRANCH_ID);
        getCartUnavailableItems.setErpSystemName(ERPSystem.ECLIPSE);

        getCartWithProducts = new Cart();
        getCartWithProducts.setApprovalState(APPROVAL_STATE_ID);
        getCartWithProducts.setDelivery(delivery);
        getCartWithProducts.setWillCall(willCall);
        getCartWithProducts.setShippingBranchId(HOME_BRANCH_ID);
        getCartWithProducts.setErpSystemName(ERPSystem.ECLIPSE);

        getCartWithCustomAddress = new Cart();
        getCartWithCustomAddress.setId(CART_ID_CUSTOM_ADDRESS);
        getCartWithCustomAddress.setApprovalState(APPROVAL_STATE_ID);
        getCartWithCustomAddress.setDeliveryMethod(DeliveryMethodEnum.DELIVERY);
        getCartWithCustomAddress.setPaymentMethodType(PaymentMethodTypeEnum.BILLTOACCOUNT);
        getCartWithCustomAddress.setShippingBranchId(HOME_BRANCH_ID);
        getCartWithCustomAddress.setErpSystemName(ERPSystem.ECLIPSE);
        getCartWithCustomAddress.setDelivery(deliveryWithCustomAddress);
        getCartWithCustomAddress.setWillCall(willCall);
        getCartWithCustomAddress.setShipToId(SHIP_TO_ID);
        getCartWithCustomAddress.setOwnerId(USER_EXISTS);

        getCartWithProductsWrongBranch = new Cart();
        getCartWithProductsWrongBranch.setShippingBranchId(WRONG_BRANCH_ID);
        getCartWithProductsWrongBranch.setId(CART_ID);
        getCartWithProductsWrongBranch.setDelivery(delivery);
        getCartWithProductsWrongBranch.setErpSystemName(ERPSystem.ECLIPSE);

        getCartNullBranch = new Cart();
        getCartNullBranch.setApprovalState(APPROVAL_STATE_ID);
        getCartNullBranch.setDelivery(delivery);
        getCartNullBranch.setWillCall(willCall);
        getCartNullBranch.setErpSystemName(ERPSystem.ECLIPSE);

        lineItems = new ArrayList<>();
        LineItems lineItem = new LineItems();
        lineItem.setErpPartNumber(PRODUCT_ID);
        lineItem.setQtyAvailable(10);
        lineItem.setQuantity(5);
        lineItems.add(lineItem);

        lineItemDTOList1 = new ArrayList<>();
        LineItemDTO lineItemDTO = new LineItemDTO(lineItem);
        lineItemDTO.setQuantity(5);
        lineItemDTO.setErpPartNumber(PRODUCT_ID);
        lineItemDTO.setCustomerPartNumber(CUSTOMER_PART_NUMBER);
        lineItemDTO.setQtyAvailable(ERP_QUANTITY);
        lineItemDTOList1.add(lineItemDTO);
        //lineItemDTO.setCustomerPartNumber();

        getCartReset.setProducts(lineItems);
        getCartWithProducts.setProducts(lineItems);
        getCartWithCustomAddress.setProducts(lineItems);
        getCartDefault.setProducts(lineItems);

        when(accountService.getUser(USER_EXISTS.toString(), AUTH_TOKEN)).thenReturn(userDTO);
        when(accountService.getUser(approverId.toString(), AUTH_TOKEN)).thenReturn(approverDTO);

        when(cartDAO.findById(CART_DEFAULT)).thenReturn(Optional.of(getCartReset));
        when(cartDAO.findById(CART_NULL_BRANCH)).thenReturn(Optional.of(getCartNullBranch));
        when(cartDAO.findById(CART_EXISTS)).thenReturn(Optional.of(getCartNoProducts));
        when(cartDAO.findById(CART_ID)).thenReturn(Optional.of(getCartNoProducts));
        when(cartDAO.findById(CART_ID_CUSTOM_ADDRESS)).thenReturn(Optional.of(getCartWithCustomAddress));
        when(cartDAO.findById(CART_PRODUCTS_UNEXPIRED)).thenReturn(Optional.of(getCartWithProducts));
        when(cartDAO.findById(CART_PRODUCTS_EXPIRED)).thenReturn(Optional.of(getCartWithProducts));
        when(cartDAO.findById(CART_PRODUCTS_BRANCH_NOT_FOUND)).thenReturn(Optional.of(getCartWithProductsWrongBranch));
        when(cartDAO.findById(CART_ID_UNAVAILABLE_ITEMS)).thenReturn(Optional.of(getCartUnavailableItems));
        when(cartDAO.existsById(CART_EXISTS)).thenReturn(true);
        when(cartDAO.existsById(CART_PRODUCTS_UNEXPIRED)).thenReturn(true);
        when(cartDAO.existsById(CART_PRODUCTS_BRANCH_NOT_FOUND)).thenReturn(true);
        when(cartDAO.findById(CART_NOT_FOUND)).thenReturn(Optional.empty());

        Cart cartWithDelivery = new Cart();
        cartWithDelivery.setDelivery(delivery);
        cartWithDelivery.setId(CART_ID);
        cartWithDelivery.setDeliveryMethod(DeliveryMethodEnum.DELIVERY);
        cartWithDelivery.setPaymentMethodType(PaymentMethodTypeEnum.CREDITCARD);
        cartWithDelivery.setCreditCard(creditCardWithDelivery);
        cartWithDelivery.setWillCall(willCall);
        cartWithDelivery.setShippingBranchId(UUID.randomUUID().toString());
        cartWithDelivery.setOwnerId(USER_EXISTS);
        cartWithDelivery.setShipToId(SHIP_TO_ID);
        cartWithDelivery.setErpSystemName(ERPSystem.ECLIPSE);

        Cart cartWithWillCall = new Cart();
        cartWithWillCall.setWillCall(willCall);
        cartWithWillCall.setId(CART_ID);
        cartWithWillCall.setDeliveryMethod(DeliveryMethodEnum.WILLCALL);
        cartWithWillCall.setPaymentMethodType(PaymentMethodTypeEnum.BILLTOACCOUNT);
        cartWithWillCall.setDelivery(delivery);
        cartWithWillCall.setShippingBranchId(UUID.randomUUID().toString());
        cartWithWillCall.setOwnerId(USER_EXISTS);
        cartWithWillCall.setShipToId(SHIP_TO_ID);
        cartWithWillCall.setApproverId(approverId);
        cartWithWillCall.setErpSystemName(ERPSystem.ECLIPSE);

        when(cartDAO.findById(CART_ID_WILL_CALL)).thenReturn(Optional.of(cartWithWillCall));
        when(cartDAO.findById(CART_ID_DELIVERY)).thenReturn(Optional.of(cartWithDelivery));
        when(cartDAO.findById(CART_ID_CUSTOM_ADDRESS)).thenReturn(Optional.of(getCartWithCustomAddress));
        Cart cartNoDeliveryWillCall = new Cart();
        cartNoDeliveryWillCall.setId(CART_NO_DELIVERY_OR_WILL_CALL);
        cartNoDeliveryWillCall.setDeliveryMethod(DeliveryMethodEnum.WILLCALL);
        cartNoDeliveryWillCall.setOwnerId(USER_EXISTS);
        cartNoDeliveryWillCall.setShipToId(SHIP_TO_ID);
        when(cartDAO.findById(CART_NO_DELIVERY_OR_WILL_CALL)).thenReturn(Optional.of(cartNoDeliveryWillCall));

        when(cartDAO.findAllByOwnerIdAndShipToId(USER_EXISTS, ACCOUNT_EXISTS)).thenReturn(new ArrayList<>());
        when(cartDAO.findAllByOwnerIdAndShipToId(USER_NOT_FOUND, ACCOUNT_NOT_FOUND)).thenReturn(new ArrayList<>());
        when(cartDAO.findAllByOwnerIdAndShipToId(USER_ALREADY_CREATED_CART, ACCOUNT_ALREADY_CREATED_CART))
            .thenReturn(Arrays.asList(getCartNoProducts));

        Cart cartCreated = new Cart();
        cartCreated.setId(CART_ID);
        when(cartDAO.save(not(or(eq(getCartDefault), eq(getCartReset))))).thenReturn(cartCreated);
        when(cartDAO.save(or(eq(getCartDefault), eq(getCartReset)))).thenReturn(getCartDefault);

        ApprovalFlowState approvalFlowState = new ApprovalFlowState();
        approvalFlowState.setId(APPROVAL_STATE_ID);
        when(approvalFlowStateDAO.findByDisplayName(ApprovalFlowStateEnum.ACTIVE.getDisplayName()))
            .thenReturn(approvalFlowState);
        when(approvalFlowStateDAO.findByDisplayName(ApprovalFlowStateEnum.AWAITING_APPROVAL.getDisplayName()))
            .thenReturn(approvalFlowState);

        LineItems lineItems1 = new LineItems();
        lineItems1.setErpPartNumber(PRODUCT_ID);
        when(lineItemsDAO.findByIdAndCartId(ITEM_ID, CART_ID)).thenReturn(Optional.of(lineItems1));
        when(lineItemsDAO.findByIdAndCartId(ITEM_ID, CART_ID_CUSTOM_ADDRESS)).thenReturn(Optional.of(lineItems1));
        when(lineItemsDAO.findByIdAndCartId(ITEM_ID_NOT_FOUND, CART_ID)).thenReturn(Optional.empty());
        when(lineItemsDAO.save(any())).thenReturn(new LineItems());

        when(lineItemsDAO.findAllByCartIdOrderByIdAsc(CART_PRODUCTS_EXPIRED)).thenReturn(lineItems);
        when(lineItemsDAO.findAllByCartIdOrderByIdAsc(CART_PRODUCTS_BRANCH_NOT_FOUND)).thenReturn(lineItems);
        when(lineItemsDAO.findAllSortedLineItem(CART_PRODUCTS_EXPIRED)).thenReturn(lineItems);
        when(lineItemsDAO.findAllSortedLineItem(CART_PRODUCTS_UNEXPIRED)).thenReturn(lineItems);
        when(lineItemsDAO.findAllSortedLineItem(CART_PRODUCTS_BRANCH_NOT_FOUND)).thenReturn(lineItems);

        lineItemsUnexpired = new ArrayList<>();
        LineItems lineItemUnexpired = new LineItems();
        lineItemUnexpired.setErpPartNumber(PRODUCT_ID);
        lineItemUnexpired.setQtyAvailable(ERP_QUANTITY);
        lineItemUnexpired.setQuantity(QUANTITY);
        lineItemUnexpired.setCustomerPartNumber(CUSTOMER_PART_NUMBER);
        lineItemUnexpired.setQtyAvailableLastUpdatedAt(new Date());
        lineItemUnexpired.setPriceLastUpdatedAt(new Date());
        lineItemsUnexpired.add(lineItemUnexpired);

        listItemUnavailable = new ArrayList<>();
        LineItems lineItemUnavailable = new LineItems();
        lineItemUnavailable.setErpPartNumber(UNAVAILABLE_PRODUCT_ID);
        lineItemUnavailable.setQtyAvailable(ERP_QUANTITY);
        lineItemUnavailable.setQuantity(QUANTITY);
        lineItemUnavailable.setCustomerPartNumber(CUSTOMER_PART_NUMBER);
        lineItemUnavailable.setQtyAvailableLastUpdatedAt(new Date());
        lineItemUnavailable.setPriceLastUpdatedAt(new Date());
        listItemUnavailable.add(lineItemUnavailable);

        when(lineItemsDAO.findAllByCartIdOrderByIdAsc(CART_PRODUCTS_UNEXPIRED)).thenReturn(lineItemsUnexpired);
        when(lineItemsDAO.findAllByCartIdOrderByIdAsc(CART_ID)).thenReturn(lineItemsUnexpired);
        when(lineItemsDAO.findAllByCartIdOrderByIdAsc(CART_ID_CUSTOM_ADDRESS)).thenReturn(lineItemsUnexpired);
        when(lineItemsDAO.findAllByCartIdOrderByIdAsc(CART_ID_WILL_CALL)).thenReturn(lineItemsUnexpired);
        when(lineItemsDAO.findAllByCartIdOrderByIdAsc(CART_ID_UNAVAILABLE_ITEMS)).thenReturn(listItemUnavailable);
        when(lineItemsDAO.findAllSortedLineItem(CART_PRODUCTS_UNEXPIRED)).thenReturn(lineItemsUnexpired);
        when(lineItemsDAO.findAllSortedLineItem(CART_ID)).thenReturn(lineItemsUnexpired);

        //addAllListItemsToCart test inputs
        listLineItemPipe =
            new ListLineItem(LIST_LINE_ITEM_PIPE_ID, ERP_PART_NUMBER_PIPE, PIPE_QUANTITY, LIST_WITH_PRODUCTS_ID, 0);

        var lineItems = new HashSet<ListLineItem>();
        lineItems.add(listLineItemPipe);

        var limitLineItems = new HashSet<ListLineItem>();

        for (int i = 0; i <= 600; i++) {
            var itemLimit = new ListLineItem();
            itemLimit.setId(UUID.randomUUID());
            itemLimit.setErpPartNumber(ERP_PART_NUMBER_PIPE);
            itemLimit.setQuantity(PIPE_QUANTITY);
            itemLimit.setListId(LIST_WITH_PRODUCTS_LIMIT_ID);
            itemLimit.setSortOrder(i);
            limitLineItems.add(itemLimit);
        }

        listWithProducts =
            new com.reece.platform.products.model.entity.List(
                LIST_WITH_PRODUCTS_ID,
                LIST_WITH_PRODUCTS_NAME,
                BILL_TO_ACCOUNT_ID_UUID,
                lineItems
            );

        listWithProductsLimit =
            new com.reece.platform.products.model.entity.List(
                LIST_WITH_PRODUCTS_LIMIT_ID,
                LIST_WITH_PRODUCTS_LIMIT_NAME,
                BILL_TO_ACCOUNT_ID_UUID,
                limitLineItems
            );

        when(listsDAO.findById(LIST_WITH_PRODUCTS_ID)).thenReturn(Optional.of(listWithProducts));
        when(listsDAO.findById(LIST_WITH_PRODUCTS_LIMIT_ID)).thenReturn(Optional.of(listWithProductsLimit));

        erpUserInformation = new ErpUserInformation();
        erpUserInformation.setErpSystemName(ERPSystem.ECLIPSE.name());
        erpUserInformation.setErpAccountId(BILL_TO_ACCOUNT_ID);
        erpUserInformation.setName("First Last");

        erpUserInformationWrongBranch = new ErpUserInformation();
        erpUserInformationWrongBranch.setErpSystemName(ERPSystem.ECLIPSE.name());

        erpUserInformationMincron = new ErpUserInformation();
        erpUserInformationMincron.setErpSystemName(ERPSystem.MINCRON.name());

        submitOrderDTO = new SubmitOrderDTO();
        submitOrderDTO.setErpUserInformation(erpUserInformation);
        submitOrderDTO.setBillToAccountId(BILL_TO_ACCOUNT_ID);

        cartWillCallBranch = new Cart();
        cartWillCallBranch.setShippingBranchId(UPDATE_WILL_CALL_BRANCH);
        cartWillCallBranch.setId(CART_ID_WILL_CALL_BRANCH);
        when(
            cartDAO.save(
                argThat(cart ->
                    cart.getShippingBranchId() != null && cart.getShippingBranchId().equals(UPDATE_WILL_CALL_BRANCH)
                )
            )
        )
            .thenReturn(cartWillCallBranch);

        ProductPricingResponseDTO productPricingResponseDTO = new ProductPricingResponseDTO();
        ProductPricingResponseDTO unavailablePricingResponseDTO = new ProductPricingResponseDTO();
        ProductPricingDTO productPricingDTO = new ProductPricingDTO();
        ProductPricingDTO unavailableProductPricingDTO = new ProductPricingDTO();

        productPricingDTO.setProductId(PRODUCT_ID);
        productPricingDTO.setSellPrice(BigDecimal.TEN);
        productPricingDTO.setBranchAvailableQty(ERP_QUANTITY);
        productPricingDTO.setTotalAvailableQty(ERP_QUANTITY);
        productPricingDTO.setOrderUom(UOM);
        productPricingDTO.setOrderPerQty(1);

        unavailableProductPricingDTO.setProductId(UNAVAILABLE_PRODUCT_ID);

        productPricingResponseDTO.setProducts(List.of(productPricingDTO));
        unavailablePricingResponseDTO.setProducts(List.of(unavailableProductPricingDTO));

        when(erpService.getProductPricing(BILL_TO_ACCOUNT_ID, HOME_BRANCH_ID, Collections.singletonList(PRODUCT_ID)))
            .thenReturn(productPricingResponseDTO);
        when(
            erpService.getProductPricing(
                BILL_TO_ACCOUNT_ID,
                UPDATE_WILL_CALL_BRANCH,
                Collections.singletonList(PRODUCT_ID)
            )
        )
            .thenReturn(productPricingResponseDTO);
        when(
            erpService.getProductPricing(
                BILL_TO_ACCOUNT_ID,
                HOME_BRANCH_ID,
                Collections.singletonList(UNAVAILABLE_PRODUCT_ID)
            )
        )
            .thenReturn(unavailablePricingResponseDTO);

        ProductPricingRequestDTO requestDTO = new ProductPricingRequestDTO();
        requestDTO.setBranchId(cartWithDelivery.getPricingBranchId());
        requestDTO.setCustomerId(erpUserInformation.getErpAccountId());
        requestDTO.setIncludeListData(false);
        requestDTO.setProductIds(Collections.singletonList(PRODUCT_ID));

        when(productService.getPricing(any())).thenReturn(productPricingResponseDTO);

        ProductPricingRequestDTO willCallRequestDTO = new ProductPricingRequestDTO();
        willCallRequestDTO.setCustomerId(erpUserInformation.getErpAccountId());
        willCallRequestDTO.setBranchId(cartWillCallBranch.getPricingBranchId());
        willCallRequestDTO.setIncludeListData(false);
        willCallRequestDTO.setProductIds(Collections.singletonList(PRODUCT_ID));

        AccountResponseDTO accountResponseDTO = new AccountResponseDTO();
        accountResponseDTO.setErpId(ERP_ID);
        accountResponseDTO.setErpAccountId(ERP_ACCOUNT_ID);
        accountResponseDTO.setStreet1(STREET1);
        accountResponseDTO.setStreet2(STREET2);
        accountResponseDTO.setCity(CITY);
        accountResponseDTO.setState(STATE);
        accountResponseDTO.setZip(ZIP_CODE);
        accountResponseDTO.setPhoneNumber(PHONE_NUMBER);
        accountResponseDTO.setBranchId(HOME_BRANCH_ID);
        when(accountService.getAccountData(any(), any(), any())).thenReturn(accountResponseDTO);

        willCallDTO = new WillCallDTO();
        willCallDTO.setBranchId(HOME_BRANCH_ID);
        willCallDTO.setPickupInstructions(PICKUP_INSTRUCTIONS);
        willCallDTO.setPreferredDate(new Date());
        willCallDTO.setPreferredTime(PreferredTimeEnum.ASAP);
        when(willCallDAO.save(any(WillCall.class))).thenReturn(new WillCall(willCallDTO));
        WillCall willCallGetOne = new WillCall();
        willCallGetOne.setPreferredTime(PreferredTimeEnum.ASAP);
        willCallGetOne.setPreferredDate(new Date());
        willCallGetOne.setPickupInstructions(DELIVERY_INSTRUCTIONS);
        willCallGetOne.setPreferredDate(orderDate);
        willCallGetOne.setPreferredTime(orderTime);
        when(willCallDAO.getOne(WILL_CALL_ID)).thenReturn(willCallGetOne);

        deliveryDTO = new DeliveryDTO();
        deliveryDTO.setAddress(address);
        deliveryDTO.setPreferredTime(PreferredTimeEnum.ASAP);
        deliveryDTO.setDeliveryInstructions(DELIVERY_INSTRUCTIONS);
        deliveryDTO.setPreferredDate(new Date());
        willCallDTO.setPreferredTime(PreferredTimeEnum.ASAP);
        deliveryDTO.setShipTo(UUID.randomUUID());
        deliveryDTO.setShouldShipFullOrder(true);
        when(deliveryDAO.save(any(Delivery.class))).thenReturn(new Delivery(deliveryDTO));
        when(addressDAO.save(any(Address.class))).thenReturn(address);

        deliveryBadAccountDTO = new DeliveryDTO();
        deliveryDTO.setShipTo(BAD_SHIP_TO_ID);

        when(addressDAO.getOne(ADDRESS_ID)).thenReturn(address);
        when(addressDAO.getOne(ADDRESS_ID_CUSTOM)).thenReturn(customAddress);
        Delivery deliveryGetOne = new Delivery();
        Address getOneAddress = new Address();
        getOneAddress.setId(ADDRESS_ID);
        deliveryGetOne.setAddress(getOneAddress);
        deliveryGetOne.setDeliveryInstructions(DELIVERY_INSTRUCTIONS);
        deliveryGetOne.setShouldShipFullOrder(true);
        deliveryGetOne.setPreferredDate(orderDate);
        deliveryGetOne.setPreferredTime(orderTime);
        when(deliveryDAO.getOne(DELIVERY_ID)).thenReturn(deliveryGetOne);
        when(deliveryDAO.getOne(DELIVERY_ID_CUSTOM_ADDRESS)).thenReturn(deliveryWithCustomAddress);

        genericGetOrderResponseDTO = buildGenericGetOrderResponseDTO();

        when(erpService.submitSalesOrder(any(CreateSalesOrderRequestDTO.class))).thenReturn(genericGetOrderResponseDTO);
        when(erpService.submitSalesOrderPreview(any(CreateSalesOrderRequestDTO.class)))
            .thenReturn(genericGetOrderResponseDTO);

        ImageUrls imageUrls = new ImageUrls();
        imageUrls.setLarge(URL);
        productDTO = new ProductDTO();
        productDTO.setImageUrls(imageUrls);
        productDTO.setPartNumber(PRODUCT_ID);
        productDTO.setName(PRODUCT_NAME);
        productDTO.setManufacturerName(MANUFACTURER_NAME);
        when(productService.getProductsByNumber(any(), any())).thenReturn(Collections.singletonList(productDTO));

        when(accountService.getHomeBranch(any())).thenReturn(new BranchDTO());
        when(userDAO.findByEmail(any())).thenReturn(Optional.of(user));

        BranchResponseDTO shippingBranch = new BranchResponseDTO();
        shippingBranch.setBranchId("1083");
        shippingBranch.setIsPricingOnly(false);
        when(branchesService.getBranch(any())).thenReturn(shippingBranch);

        cartService =
            new CartService(
                cartDAO,
                lineItemsDAO,
                approvalFlowStateDAO,
                deliveryDAO,
                willCallDAO,
                addressDAO,
                ordersPendingApprovalDAO,
                orderStatusDAO,
                listsDAO,
                accountService,
                authorizationService,
                erpService,
                productService,
                notificationService,
                branchesService,
                userDAO
            );

        ReflectionTestUtils.setField(cartService, "cartPricingAvailabilityTTL", 300_000);
    }

    @Test
    void createCart_valid() throws Exception {
        UserAccountInfo userAccountInfo = new UserAccountInfo();
        userAccountInfo.setUserId(USER_EXISTS);
        userAccountInfo.setShipToAccountId(ACCOUNT_EXISTS);
        userAccountInfo.setErpSystemName(ERPSystem.ECLIPSE);
        UUID cartId = cartService.createCart(userAccountInfo, AUTH_TOKEN);
        assertEquals(cartId, CART_ID, "Expected mocked cart id to return from cart creation");
    }

    @Test
    void createCart_cartAlreadyExists() {
        UserAccountInfo userAccountInfo = new UserAccountInfo();
        userAccountInfo.setUserId(USER_ALREADY_CREATED_CART);
        userAccountInfo.setShipToAccountId(ACCOUNT_ALREADY_CREATED_CART);
        assertThrows(CartAlreadyExistsException.class, () -> cartService.createCart(userAccountInfo, null));
    }

    @Test
    void resetCart_valid() throws Exception {
        UserAccountInfo userAccountInfo = new UserAccountInfo();
        userAccountInfo.setUserId(USER_EXISTS);
        userAccountInfo.setShipToAccountId(ACCOUNT_EXISTS);
        userAccountInfo.setShippingBranchId(HOME_BRANCH_ID);
        userAccountInfo.setErpSystemName(ERPSystem.ECLIPSE);

        CartResponseDTO resetCart = cartService.resetCart(CART_DEFAULT, userAccountInfo, AUTH_TOKEN);

        CartResponseDTO expectedCart = new CartResponseDTO(getCartDefault);
        LineItemResponseDTO lineItemResponseDTO = new LineItemResponseDTO();
        lineItemResponseDTO.setErpPartNumber(PRODUCT_ID);
        lineItemResponseDTO.setProduct(new ProductDTO());
        assertEquals(resetCart, expectedCart, "Expected default cart to be returned on reset call");
    }

    @Test
    void resetCart_nullBranch() throws Exception {
        UserAccountInfo userAccountInfo = new UserAccountInfo();
        userAccountInfo.setUserId(USER_EXISTS);
        userAccountInfo.setShipToAccountId(ACCOUNT_EXISTS);
        userAccountInfo.setShippingBranchId(BASE_BRANCH_ID);
        userAccountInfo.setErpSystemName(ERPSystem.ECLIPSE);

        CartResponseDTO resetCart = cartService.resetCart(CART_NULL_BRANCH, userAccountInfo, AUTH_TOKEN);

        verify(willCallDAO, times(1)).save(argThat(willCall -> willCall.getBranchId().equals(BASE_BRANCH_ID)));
        verify(cartDAO, times(1)).save(argThat(cart -> cart.getShippingBranchId().equals(BASE_BRANCH_ID)));
    }

    @Test
    void resetCart_cartNotFound() {
        UserAccountInfo userAccountInfo = new UserAccountInfo();
        userAccountInfo.setUserId(USER_EXISTS);
        userAccountInfo.setShipToAccountId(ACCOUNT_EXISTS);
        userAccountInfo.setErpSystemName(ERPSystem.ECLIPSE);

        assertThrows(
            CartNotFoundException.class,
            () -> cartService.resetCart(CART_NOT_FOUND, userAccountInfo, AUTH_TOKEN)
        );
    }

    @Test
    void updateCart_valid() throws Exception {
        CartResponseDTO updatedCart = cartService.updateCart(CART_EXISTS, new Cart());
        assertEquals(
            updatedCart.getId(),
            CART_ID,
            "Expected save function on Cart DAO to be called and mock value returned"
        );
    }

    @Test
    void updateWillCallBranch_valid() throws Exception {
        CartResponseDTO cart = cartService.getCart(CART_PRODUCTS_UNEXPIRED, true, null);
        CartResponseDTO updatedCart = cartService.updateWillCallBranch(
            CART_PRODUCTS_UNEXPIRED,
            UPDATE_WILL_CALL_BRANCH,
            erpUserInformation,
            false
        );
        assertEquals(
            updatedCart.getId(),
            CART_ID_WILL_CALL_BRANCH,
            "Expected save function on Cart DAO to be called and mock value returned"
        );
        assertNotNull(updatedCart.getProducts(), "Expected products to be updated and a mock value returned");
        assertEquals(
            cart.getProducts().get(0).getQtyAvailable(),
            updatedCart.getProducts().get(0).getQtyAvailable(),
            "Expected product quantity available to be updated and mock value returned"
        );
        assertEquals(updatedCart.getSubtotal(), 5000);
        verify(willCallDAO, times(1)).save(argThat(willCall -> willCall.getBranchId().equals(UPDATE_WILL_CALL_BRANCH)));
    }

    @Test
    void updateWillCallBranch_cartNotFound() {
        assertThrows(
            CartNotFoundException.class,
            () -> cartService.updateWillCallBranch(CART_NOT_FOUND, UPDATE_WILL_CALL_BRANCH, erpUserInformation, false)
        );
    }

    @Test
    void updateWillCallBranch_willCallNotFound() {
        assertThrows(
            WillCallNotFoundException.class,
            () ->
                cartService.updateWillCallBranch(
                    CART_NO_DELIVERY_OR_WILL_CALL,
                    UPDATE_WILL_CALL_BRANCH,
                    erpUserInformation,
                    false
                )
        );
    }

    @Test
    void updateCart_cartNotFound() {
        assertThrows(CartNotFoundException.class, () -> cartService.updateCart(CART_NOT_FOUND, new Cart()));
    }

    @Test
    void getCart_cartIdSuccessNoProducts() throws Exception {
        CartResponseDTO cart = cartService.getCart(CART_EXISTS, false, null);
        assertEquals(cart, new CartResponseDTO(getCartNoProducts), "Expected mocked cart to return on get call");
    }

    @Test
    void refreshCart_cartNotFound() {
        assertThrows(
            CartNotFoundException.class,
            () -> cartService.refreshCart(CART_NOT_FOUND, erpUserInformation, false)
        );
    }

    @Test
    void refreshCart_success() throws Exception {
        CartResponseDTO returnedCart = cartService.refreshCart(CART_ID, erpUserInformation, false);
        CartResponseDTO expectedCart = new CartResponseDTO(getCartNoProducts);
        expectedCart.setProducts(
            lineItemsUnexpired
                .stream()
                .map(a -> {
                    LineItemResponseDTO b = new LineItemResponseDTO(a);
                    b.setListIds(new ArrayList<>());
                    return b;
                })
                .collect(Collectors.toList())
        );
        expectedCart.getProducts().forEach(item -> item.setProduct(productDTO));
        expectedCart.setRemovedProducts(new ArrayList<>());
        assertEquals(returnedCart, expectedCart, "Expected mocked cart to return on refresh cart call");
    }

    @Test
    void removeAllCartItems_success() throws Exception {
        CartResponseDTO oldCart = cartService.removeAllCartItems(CART_ID);
        verify(lineItemsDAO, times(1)).deleteAllByCartId(CART_ID);

        CartResponseDTO expectedCart = new CartResponseDTO(getCartNoProducts);
        expectedCart.setProducts(
            lineItemsUnexpired.stream().map(LineItemResponseDTO::new).collect(Collectors.toList())
        );

        expectedCart.getProducts().forEach(item -> item.setProduct(productDTO));
        assertTrue(oldCart.getProducts() == null, "Expected cart to have no products");
        assertFalse(expectedCart.getProducts() == null, "Expected cart to have products");

        CartResponseDTO updatedCart = cartService.removeAllCartItems(expectedCart.getId());
        assertEquals(oldCart, updatedCart, "Expected mocked cart to return on remove all cart items call");
    }

    @Test
    void removeAllCartItems_cartNotFound() throws Exception {
        assertThrows(CartNotFoundException.class, () -> cartService.removeAllCartItems(CART_NOT_FOUND));
    }

    @Test
    void getCart_cartIdSuccessWithProductsExpired() throws Exception {
        CartResponseDTO cart = cartService.getCart(CART_PRODUCTS_EXPIRED, true, null);
        CartResponseDTO expectedCart = new CartResponseDTO(getCartWithProducts);
        expectedCart.setProducts(lineItems.stream().map(LineItemResponseDTO::new).collect(Collectors.toList()));
        expectedCart.getProducts().forEach(item -> item.setProduct(productDTO));
        assertEquals(cart, expectedCart, "Expected mocked cart to return on get call");
    }

    @Test
    void getCart_cartIdBranchNotFound() throws Exception {
        CartResponseDTO cart = cartService.getCart(CART_PRODUCTS_BRANCH_NOT_FOUND, true, null);
        cart
            .getProducts()
            .forEach(product -> {
                assertEquals(product.getQtyAvailable(), 10);
            });
    }

    @Test
    void getCart_dontLookupMincronAccountInfo() {
        assertDoesNotThrow(() -> cartService.getCart(CART_PRODUCTS_BRANCH_NOT_FOUND, true, null));
    }

    @Test
    void getCart_cartIdSuccessWithProductsUnExpired() throws Exception {
        CartResponseDTO cart = cartService.getCart(CART_PRODUCTS_UNEXPIRED, true, null);
        CartResponseDTO expectedCart = new CartResponseDTO(getCartWithProducts);
        expectedCart.setProducts(
            lineItemsUnexpired.stream().map(LineItemResponseDTO::new).collect(Collectors.toList())
        );
        expectedCart.getProducts().forEach(item -> item.setProduct(productDTO));
        assertEquals(cart, expectedCart, "Expected mocked cart to return on get call");
        verify(erpService, times(0)).getEclipseProductData(anyList(), eq(erpUserInformation), any());
        verify(lineItemsDAO, times(0)).save(any());
    }

    @Test
    void getCart_elasticError() throws Exception {
        when(productService.getProductsByNumber(any(), any())).thenThrow(ElasticsearchException.class);
        CartResponseDTO cart = cartService.getCart(CART_ID, true, null);
        CartResponseDTO expectedCart = new CartResponseDTO(getCartNoProducts);
        assertEquals(cart, expectedCart, "Expected mocked cart to return on get call");
    }

    @Test
    void getCart_cartIdNotFound() {
        assertThrows(CartNotFoundException.class, () -> cartService.getCart(CART_NOT_FOUND, false, null));
    }

    @Test
    void getCart_userIdAccountIdSuccess() throws Exception {
        CartResponseDTO cart = cartService.getCart(
            USER_ALREADY_CREATED_CART,
            ACCOUNT_ALREADY_CREATED_CART,
            false,
            erpUserInformation
        );
        assertEquals(cart, new CartResponseDTO(getCartNoProducts), "Expected mocked cart to return on get call");
    }

    @Test
    void getCart_userIdAccountIdNotFound() {
        assertThrows(
            CartNotFoundException.class,
            () -> cartService.getCart(USER_NOT_FOUND, ACCOUNT_NOT_FOUND, false, erpUserInformation)
        );
    }

    @Test
    void addItems_success() throws Exception {
        ItemsInfoDTO itemsInfoDTO = new ItemsInfoDTO();
        itemsInfoDTO.setItemInfoList(Arrays.asList(new ItemInfoDTO(PRODUCT_ID, QUANTITY, 5, 22, "ea", 22.66f)));

        List<LineItems> existingLineItems = new ArrayList<>();
        LineItems lineItem1 = new LineItems();
        lineItem1.setId(UUID.randomUUID());
        lineItem1.setErpPartNumber(PRODUCT_ID);
        lineItem1.setQtyAvailable(10);
        lineItem1.setQuantity(5);
        lineItem1.setCartId(CART_EXISTS);
        existingLineItems.add(lineItem1);
        LineItems lineItem2 = new LineItems();
        lineItem2.setId(UUID.randomUUID());
        lineItem2.setErpPartNumber(PRODUCT_ID);
        lineItem2.setQtyAvailable(10);
        lineItem2.setQuantity(5);
        lineItem2.setCartId(CART_EXISTS);

        existingLineItems.add(lineItem2);
        when(lineItemsDAO.findAllByCartId(eq(CART_EXISTS))).thenReturn(existingLineItems);

        cartService.addItems(CART_EXISTS, itemsInfoDTO, erpUserInformation, false);
        verify(lineItemsDAO, times(1)).findAllSortedLineItem(CART_EXISTS);

        verify(lineItemsDAO, times(1)).saveAll(lineItemsListArgumentCaptor.capture());
        LineItems savedLineItem = lineItemsListArgumentCaptor.getValue().stream().findFirst().get();
        assertEquals(savedLineItem.getCartId(), CART_EXISTS, "Expected item added to be associated with given cart id");
        assertEquals(
            savedLineItem.getErpPartNumber(),
            PRODUCT_ID,
            "Expected item added to be associated with given product id"
        );
        assertEquals(savedLineItem.getQuantity(), QUANTITY, "Expected item to be saved with given quantity");
    }

    @Test
    void addItems_failure() {
        ItemsInfoDTO itemsInfoDTO = new ItemsInfoDTO();
        itemsInfoDTO.setItemInfoList(Arrays.asList(new ItemInfoDTO(PRODUCT_ID, QUANTITY, 5, 22, "ea", 22.66f)));
        List<ItemInfoDTO> itemInfoDTOList = new ArrayList();

        for (int i = 0; i <= 600; i++) {
            var a = new ItemInfoDTO(PRODUCT_ID, QUANTITY, 5, 22, "ea", 22.66f);
            itemInfoDTOList.add(a);
        }
        itemsInfoDTO.setItemInfoList(itemInfoDTOList);

        assertThrows(
            AddItemsToCartFoundException.class,
            () -> cartService.addItems(CART_EXISTS, itemsInfoDTO, erpUserInformation, false),
            "Can not add more than 600 products in Cart"
        );
    }

    @Test
    void addItems_cartNotFound() {
        ItemsInfoDTO itemsInfoDTO = new ItemsInfoDTO();
        itemsInfoDTO.setItemInfoList(Arrays.asList(new ItemInfoDTO(PRODUCT_ID, QUANTITY, 1, 22, "ea", 22.66f)));
        assertThrows(
            CartNotFoundException.class,
            () -> cartService.addItems(CART_NOT_FOUND, itemsInfoDTO, erpUserInformation, false)
        );
    }

    @Test
    void addItem_qtyIncrementInvalid() {
        ItemsInfoDTO itemsInfoDTO = new ItemsInfoDTO();
        itemsInfoDTO.setItemInfoList(Arrays.asList(new ItemInfoDTO(PRODUCT_ID, QUANTITY, 7, 22, "ea", 22.66f)));
        assertThrows(
            QtyIncrementInvalidException.class,
            () -> cartService.addItems(CART_EXISTS, itemsInfoDTO, erpUserInformation, Boolean.FALSE)
        );
    }

    @Test
    void addAllListItemsToCart_success() throws Exception {
        ItemsInfoDTO itemsInfoDto = new ItemsInfoDTO();
        itemsInfoDto.setErpUserInformation(erpUserInformation);
        ItemInfoDTO itemInfoDto = new ItemInfoDTO();
        itemInfoDto.setProductId("12345");
        itemsInfoDto.setItemInfoList(Collections.singletonList(itemInfoDto));
        cartService.addAllListItemsToCart(CART_EXISTS, LIST_WITH_PRODUCTS_ID, itemsInfoDto, false);
        verify(lineItemsDAO, times(1)).saveAll(lineItemsListArgumentCaptor.capture());
        LineItems savedLineItem = lineItemsListArgumentCaptor.getValue().stream().findFirst().get();
        String ADDED_IDS = ERP_PART_NUMBER_PIPE;
        assertEquals(savedLineItem.getCartId(), CART_EXISTS, "Expected item added to be associated with given cart id");
        assertEquals(
            savedLineItem.getErpPartNumber(),
            ADDED_IDS,
            "Expected item added to be associated with given product id"
        );
        assertEquals(savedLineItem.getQuantity(), PIPE_QUANTITY, "Expected item to be saved with given quantity");
    }

    @Test
    void addAllListItemsToCart_addItemsToCartFoundException() throws CartNotFoundException {
        //provide a mock list with 600 line items then add all to cart from list to exceed the 600 line item limit
        ItemsInfoDTO itemsInfoDto = new ItemsInfoDTO();
        itemsInfoDto.setErpUserInformation(erpUserInformation);
        ItemInfoDTO itemInfoDto = new ItemInfoDTO();
        itemInfoDto.setProductId("12345");
        itemsInfoDto.setItemInfoList(Collections.singletonList(itemInfoDto));
        assertThrows(
            AddItemsToCartFoundException.class,
            () -> cartService.addAllListItemsToCart(CART_EXISTS, LIST_WITH_PRODUCTS_LIMIT_ID, itemsInfoDto, false),
            "Can not add more than 600 products in Cart"
        );
    }

    @Test
    void updateItemQuantity_success() throws Exception {
        int updatedQuantity = 5;
        ItemQuantityErpInformation itemQuantityErpInformation = new ItemQuantityErpInformation();
        itemQuantityErpInformation.setErpUserInformation(erpUserInformation);
        itemQuantityErpInformation.setQty(updatedQuantity);
        itemQuantityErpInformation.setMinIncrementQty(1);

        UpdateItemQtyResponseDTO returnedResponse = cartService.updateItemQuantity(
            CART_ID,
            ITEM_ID,
            itemQuantityErpInformation
        );

        CartResponseDTO targetCart = new CartResponseDTO(getCartNoProducts);
        targetCart.setProducts(
            lineItemsUnexpired
                .stream()
                .map(a -> {
                    LineItemResponseDTO b = new LineItemResponseDTO(a);
                    b.setListIds(new ArrayList<>());
                    return b;
                })
                .collect(Collectors.toList())
        );
        targetCart.getProducts().forEach(item -> item.setProduct(productDTO));
        var updatedItem = lineItemsUnexpired.get(0);
        LineItemResponseDTO product = new LineItemResponseDTO(updatedItem);
        var listIds = new ArrayList<String>();
        product.setListIds(listIds);
        product.setQtyAvailable(0);
        var expectedResponse = new UpdateItemQtyResponseDTO();
        expectedResponse.setProduct(product);
        expectedResponse.setSubtotal(BigDecimal.valueOf(0));

        verify(lineItemsDAO, times(1)).save(lineItemsArgumentCaptor.capture());
        LineItems savedLineItem = lineItemsArgumentCaptor.getValue();

        assertEquals(
            savedLineItem.getQuantity(),
            expectedResponse.getProduct().getQuantity(),
            "Expected item to be saved with updated quantity"
        );

        assertEquals(
            expectedResponse.getSubtotal(),
            returnedResponse.getSubtotal(),
            "Expected UpdateItemQtyResponse to return with updated product and updated cart subtotal"
        );
    }

    @Test
    void updateItemQuantity_itemNotFound() {
        assertThrows(
            ItemNotFoundException.class,
            () -> cartService.updateItemQuantity(CART_ID, ITEM_ID_NOT_FOUND, new ItemQuantityErpInformation())
        );
    }

    @Test
    void updateItemQuantity_qtyIncrementInvalid() {
        int updatedQuantity = 7;
        ItemQuantityErpInformation itemQuantityErpInformation = new ItemQuantityErpInformation();
        itemQuantityErpInformation.setErpUserInformation(erpUserInformation);
        itemQuantityErpInformation.setQty(updatedQuantity);
        itemQuantityErpInformation.setMinIncrementQty(10);

        assertThrows(
            QtyIncrementInvalidException.class,
            () -> cartService.updateItemQuantity(CART_ID, ITEM_ID, itemQuantityErpInformation)
        );
    }

    @Test
    void deleteItem_success() throws Exception {
        CartResponseDTO returnedCart = cartService.deleteItem(CART_ID, ITEM_ID, erpUserInformation);
        verify(lineItemsDAO, times(1)).deleteById(eq(ITEM_ID));
        CartResponseDTO expectedCart = new CartResponseDTO(getCartNoProducts);
        expectedCart.setProducts(
            lineItemsUnexpired.stream().map(LineItemResponseDTO::new).collect(Collectors.toList())
        );
        expectedCart.getProducts().forEach(item -> item.setProduct(productDTO));
        assertEquals(returnedCart, expectedCart, "Expected mocked cart to return after item delete call");
    }

    @Test
    void deleteItem_itemNotFound() {
        assertThrows(
            ItemNotFoundException.class,
            () -> cartService.deleteItem(CART_ID, ITEM_ID_NOT_FOUND, erpUserInformation)
        );
    }

    @Test
    void createWillCall_success() throws Exception {
        Cart returnedCart = cartService.createWillCall(CART_ID_WILL_CALL, willCallDTO);
        verify(willCallDAO, times(1)).save(willCallArgumentCaptor.capture());
        verify(cartDAO, times(1)).save(cartArgumentCaptor.capture());
        assertEquals(returnedCart.getId(), CART_ID, "Expected mocked cart to return after create will call");

        WillCall willCall = willCallArgumentCaptor.getValue();
        assertEquals(
            willCall.getBranchId(),
            willCallDTO.getBranchId(),
            "Expected branch id of object saved to equal dto"
        );
        assertEquals(
            willCall.getPickupInstructions(),
            willCallDTO.getPickupInstructions(),
            "Expected pickup instructions of object saved to equal dto"
        );
        assertEquals(
            willCall.getPreferredDate(),
            willCallDTO.getPreferredDate(),
            "Expected preferred date of object saved to equal dto"
        );
        assertEquals(
            willCall.getPreferredTime(),
            willCallDTO.getPreferredTime(),
            "Expected preferred time of object saved to equal dto"
        );
    }

    @Test
    void createWillCall_cartNotFound() {
        assertThrows(CartNotFoundException.class, () -> cartService.createWillCall(CART_NOT_FOUND, willCallDTO));
    }

    @Test
    void createDelivery_success() throws Exception {
        Cart returnedCart = cartService.createDelivery(CART_ID_WILL_CALL, deliveryDTO);
        verify(deliveryDAO, times(1)).save(deliveryArgumentCaptor.capture());
        verify(addressDAO, times(1)).save(addressArgumentCaptor.capture());
        verify(cartDAO, times(1)).save(cartArgumentCaptor.capture());
        assertEquals(returnedCart.getId(), CART_ID, "Expected mocked cart to return after create will call");

        Delivery delivery = deliveryArgumentCaptor.getValue();
        assertEquals(
            delivery.getAddress().getId(),
            address.getId(),
            "Expected id of address object saved to equal expected value"
        );
        assertEquals(
            delivery.getDeliveryInstructions(),
            deliveryDTO.getDeliveryInstructions(),
            "Expected delivery instructions of object saved to equal dto"
        );
        assertEquals(
            delivery.getPreferredDate(),
            deliveryDTO.getPreferredDate(),
            "Expected preferred date of object saved to equal dto"
        );
        assertEquals(
            delivery.getPreferredTime(),
            deliveryDTO.getPreferredTime(),
            "Expected preferred time of object saved to equal dto"
        );
        assertEquals(
            delivery.getShipToId(),
            deliveryDTO.getShipTo(),
            "Expected preferred time of object saved to equal dto"
        );
    }

    @Test
    void createDelivery_cartNotFound() {
        assertThrows(CartNotFoundException.class, () -> cartService.createDelivery(CART_NOT_FOUND, deliveryDTO));
    }

    @Test
    void updateDelivery_success() throws Exception {
        Cart returnedCart = cartService.updateDelivery(CART_ID_DELIVERY, deliveryDTO);
        verify(deliveryDAO, times(1)).save(deliveryArgumentCaptor.capture());
        verify(addressDAO, times(1)).save(addressArgumentCaptor.capture());
        assertEquals(returnedCart.getId(), CART_ID, "Expected mocked cart to return after create will call");

        Delivery delivery = deliveryArgumentCaptor.getValue();
        assertEquals(
            delivery.getAddress().getId(),
            address.getId(),
            "Expected id of address object saved to equal expected value"
        );
        assertEquals(
            delivery.getDeliveryInstructions(),
            deliveryDTO.getDeliveryInstructions(),
            "Expected delivery instructions of object saved to equal dto"
        );
        assertEquals(
            delivery.getPreferredDate(),
            deliveryDTO.getPreferredDate(),
            "Expected preferred date of object saved to equal dto"
        );
        assertEquals(
            delivery.getPreferredTime(),
            deliveryDTO.getPreferredTime(),
            "Expected preferred time of object saved to equal dto"
        );
        assertEquals(
            delivery.getShipToId(),
            deliveryDTO.getShipTo(),
            "Expected preferred time of object saved to equal dto"
        );
    }

    @Test
    void updateDelivery_cartNotFound() {
        assertThrows(CartNotFoundException.class, () -> cartService.updateDelivery(CART_NOT_FOUND, deliveryDTO));
    }

    @Test
    void updateDelivery_deliveryNotFound() {
        assertThrows(
            DeliveryNotFoundException.class,
            () -> cartService.updateDelivery(CART_NO_DELIVERY_OR_WILL_CALL, deliveryDTO)
        );
    }

    @Test
    void updateWillCall_success() throws Exception {
        Cart returnedCart = cartService.updateWillCall(CART_ID_WILL_CALL, willCallDTO);
        verify(willCallDAO, times(1)).save(willCallArgumentCaptor.capture());
        assertEquals(returnedCart.getId(), CART_ID, "Expected mocked cart to return after create will call");

        WillCall willCall = willCallArgumentCaptor.getValue();
        assertEquals(
            willCall.getBranchId(),
            willCallDTO.getBranchId(),
            "Expected branch id of object saved to equal dto"
        );
        assertEquals(
            willCall.getPickupInstructions(),
            willCallDTO.getPickupInstructions(),
            "Expected pickup instructions of object saved to equal dto"
        );
        assertEquals(
            willCall.getPreferredDate(),
            willCallDTO.getPreferredDate(),
            "Expected preferred date of object saved to equal dto"
        );
        assertEquals(
            willCall.getPreferredTime(),
            willCallDTO.getPreferredTime(),
            "Expected preferred time of object saved to equal dto"
        );
    }

    @Test
    void updateWillCall_cartNotFound() {
        assertThrows(CartNotFoundException.class, () -> cartService.updateWillCall(CART_NOT_FOUND, willCallDTO));
    }

    @Test
    void updateWillCall_deliveryNotFound() {
        assertThrows(
            WillCallNotFoundException.class,
            () -> cartService.updateWillCall(CART_NO_DELIVERY_OR_WILL_CALL, willCallDTO)
        );
    }

    @Test
    void submitOrder_delivery_success() throws Exception {
        GetOrderResponseDTO getOrderResponseDTO = cartService.submitOrder(CART_ID_DELIVERY, submitOrderDTO, AUTH_TOKEN);
        verify(erpService, times(1)).submitSalesOrder(salesOrderDTOArgumentCaptor.capture());
        verify(notificationService, times(1))
            .sendOrderSubmittedEmail(salesOrderSubmitNotificationDTOArgumentCaptor.capture());

        CreateSalesOrderRequestDTO salesOrderDTO = salesOrderDTOArgumentCaptor.getValue();
        assertEquals(
            salesOrderDTO.getShipToEntityId(),
            erpUserInformation.getErpAccountId(),
            "Expected shipToEntity ID on sales order to equal value given in erp user info"
        );
        assertEquals(
            salesOrderDTO.getAddress().getCity(),
            address.getCity(),
            "Expected city field on sales order Fto equal value returned from DB"
        );
        assertEquals(
            salesOrderDTO.getAddress().getCountry(),
            address.getCountry(),
            "Expected country field on sales order to equal value returned from DB"
        );
        assertEquals(
            salesOrderDTO.getAddress().getPostalCode(),
            address.getZip(),
            "Expected postal code field on sales order to equal value returned from DB"
        );
        assertEquals(
            salesOrderDTO.getAddress().getState(),
            address.getState(),
            "Expected state code field on sales order to equal value returned from DB"
        );
        assertEquals(
            salesOrderDTO.getAddress().getStreetLineOne(),
            address.getStreet1(),
            "Expected street line 1 field on sales order to equal value returned from DB"
        );
        assertEquals(
            salesOrderDTO.getAddress().getStreetLineTwo(),
            address.getStreet2(),
            "Expected street line 2 field on sales order to equal value returned from DB"
        );
        assertEquals(
            salesOrderDTO.getInstructions(),
            DELIVERY_INSTRUCTIONS,
            "Expected instructions in sales order to equal instructions from delivery"
        );
        assertEquals(salesOrderDTO.getIsDelivery(), true, "Expected delivery flag to be set to true");
        assertEquals(
            salesOrderDTO.getShouldShipFullOrder(),
            true,
            "Expected shouldShipFullOrder flag to be set to true"
        );
        assertEquals(
            salesOrderDTO.getEclipseLoginId(),
            erpUserInformation.getUserId(),
            "Expected eclipse log in id to be equal to erp user info"
        );
        assertEquals(
            salesOrderDTO.getEclipsePassword(),
            erpUserInformation.getPassword(),
            "Expected eclipse password to be equal to erp user info"
        );
        assertEquals(salesOrderDTO.getPreferredDate(), orderDate, "Expected date to be equal to mocked value");
        assertEquals(salesOrderDTO.getPreferredTime(), orderTime, "Expected time to be equal to mocked value");
        assertEquals(salesOrderDTO.getLineItems().size(), 1, "Expected 1 line item to be set on sales order");
        assertEquals(
            salesOrderDTO.getCreditCard(),
            creditCardWithDelivery,
            "Expected credit card on sales order to be saved from the cart"
        );

        LineItemDTO lineItemDTO = salesOrderDTO.getLineItems().get(0);
        assertEquals(
            lineItemDTO.getCustomerPartNumber(),
            CUSTOMER_PART_NUMBER,
            "Expected customer part number to equal expected value"
        );
        assertEquals(
            lineItemDTO.getQtyAvailable(),
            ERP_QUANTITY,
            "Expected quantity available to equal expected value"
        );
        assertEquals(lineItemDTO.getQuantity(), QUANTITY, "Expected quantity to equal expected value");
        assertEquals(lineItemDTO.getErpPartNumber(), PRODUCT_ID, "Expected quantity to equal expected value");
        assertNotNull(getOrderResponseDTO, "Expected non-null value for DTO returned");
    }

    @Test
    void submitOrder_salesOrderDTO_orderedBy_truncated() throws Exception {
        submitOrderDTO.getErpUserInformation().setName("LongerThan35LongerThan35 LongerThan35LongerThan35");
        GetOrderResponseDTO getOrderResponseDTO = cartService.submitOrder(CART_ID_DELIVERY, submitOrderDTO, AUTH_TOKEN);
        verify(erpService, times(1)).submitSalesOrder(salesOrderDTOArgumentCaptor.capture());
        verify(notificationService, times(1))
            .sendOrderSubmittedEmail(salesOrderSubmitNotificationDTOArgumentCaptor.capture());

        CreateSalesOrderRequestDTO salesOrderDTO = salesOrderDTOArgumentCaptor.getValue();
        assertEquals(salesOrderDTO.getOrderedBy().length(), 35, "Expected truncated length to be 35");
    }

    @Test
    void submitOrder_delivery_customAddress_success() throws Exception {
        GetOrderResponseDTO getOrderResponseDTO = cartService.submitOrder(
            CART_ID_CUSTOM_ADDRESS,
            submitOrderDTO,
            AUTH_TOKEN
        );
        verify(erpService, times(1)).submitSalesOrder(salesOrderDTOArgumentCaptor.capture());
        verify(notificationService, times(1))
            .sendOrderSubmittedEmail(salesOrderSubmitNotificationDTOArgumentCaptor.capture());

        CreateSalesOrderRequestDTO salesOrderDTO = salesOrderDTOArgumentCaptor.getValue();
        salesOrderDTO.setLineItems(lineItemDTOList1);
        assertEquals(
            salesOrderDTO.getShipToEntityId(),
            erpUserInformation.getErpAccountId(),
            "Expected shipToEntity ID on sales order to equal value given in erp user info"
        );
        assertEquals(
            salesOrderDTO.getAddress().getCity(),
            customAddress.getCity(),
            "Expected city field on sales order Fto equal value returned from DB"
        );
        assertEquals(
            salesOrderDTO.getAddress().getCountry(),
            customAddress.getCountry(),
            "Expected country field on sales order to equal value returned from DB"
        );
        assertEquals(
            salesOrderDTO.getAddress().getPostalCode(),
            customAddress.getZip(),
            "Expected postal code field on sales order to equal value returned from DB"
        );
        assertEquals(
            salesOrderDTO.getAddress().getState(),
            customAddress.getState(),
            "Expected state code field on sales order to equal value returned from DB"
        );
        assertEquals(
            salesOrderDTO.getAddress().getStreetLineOne(),
            customAddress.getCompanyName(),
            "Expected street line 1 field on sales order to equal company_name value returned from DB"
        );
        assertEquals(
            salesOrderDTO.getAddress().getStreetLineTwo(),
            customAddress.getStreet1(),
            "Expected street line 2 field on sales order to equal street1 value returned from DB"
        );
        assertEquals(
            salesOrderDTO.getAddress().getStreetLineThree(),
            customAddress.getStreet2(),
            "Expected street line 3 field on sales order to equal street2 value returned from DB"
        );
        assertEquals(
            salesOrderDTO.getInstructions(),
            DELIVERY_INSTRUCTIONS,
            "Expected instructions in sales order to equal instructions from delivery"
        );
        assertEquals(salesOrderDTO.getIsDelivery(), true, "Expected delivery flag to be set to true");
        assertEquals(
            salesOrderDTO.getShouldShipFullOrder(),
            true,
            "Expected shouldShipFullOrder flag to be set to true"
        );
        assertEquals(
            salesOrderDTO.getEclipseLoginId(),
            erpUserInformation.getUserId(),
            "Expected eclipse log in id to be equal to erp user info"
        );
        assertEquals(
            salesOrderDTO.getEclipsePassword(),
            erpUserInformation.getPassword(),
            "Expected eclipse password to be equal to erp user info"
        );
        assertEquals(salesOrderDTO.getPreferredDate(), orderDate, "Expected date to be equal to mocked value");
        assertEquals(salesOrderDTO.getPreferredTime(), orderTime, "Expected time to be equal to mocked value");
        assertEquals(salesOrderDTO.getLineItems().size(), 1, "Expected 1 line item to be set on sales order");

        LineItemDTO lineItemDTO = salesOrderDTO.getLineItems().get(0);
        assertEquals(
            lineItemDTO.getCustomerPartNumber(),
            CUSTOMER_PART_NUMBER,
            "Expected customer part number to equal expected value"
        );
        assertEquals(
            lineItemDTO.getQtyAvailable(),
            ERP_QUANTITY,
            "Expected quantity available to equal expected value"
        );
        assertEquals(lineItemDTO.getQuantity(), QUANTITY, "Expected quantity to equal expected value");
        assertEquals(lineItemDTO.getErpPartNumber(), PRODUCT_ID, "Expected quantity to equal expected value");
        assertNotNull(getOrderResponseDTO, "Expected non-null value for DTO returned");
    }

    @Test
    void submitOrder_will_call_success() throws Exception {
        GetOrderResponseDTO getOrderResponseDTO = cartService.submitOrder(
            CART_ID_WILL_CALL,
            submitOrderDTO,
            AUTH_TOKEN
        );
        verify(erpService, times(1)).submitSalesOrder(salesOrderDTOArgumentCaptor.capture());
        verify(notificationService, times(1))
            .sendOrderSubmittedEmail(salesOrderSubmitNotificationDTOArgumentCaptor.capture());

        CreateSalesOrderRequestDTO salesOrderDTO = salesOrderDTOArgumentCaptor.getValue();
        assertEquals(
            salesOrderDTO.getShipToEntityId(),
            erpUserInformation.getErpAccountId(),
            "Expected shipToEntity ID on sales order to equal given value in erp user info"
        );
        assertNull(salesOrderDTO.getAddress(), "Expected address field on sales order to be null for will-call");
        assertEquals(
            salesOrderDTO.getInstructions(),
            DELIVERY_INSTRUCTIONS,
            "Expected instructions in sales order to equal instructions from delivery"
        );
        assertEquals(salesOrderDTO.getIsDelivery(), false, "Expected delivery flag to be set to true");
        assertEquals(
            salesOrderDTO.getEclipseLoginId(),
            erpUserInformation.getUserId(),
            "Expected eclipse log in id to be equal to erp user info"
        );
        assertEquals(
            salesOrderDTO.getEclipsePassword(),
            erpUserInformation.getPassword(),
            "Expected eclipse password to be equal to erp user info"
        );
        assertEquals(salesOrderDTO.getPreferredDate(), orderDate, "Expected date to be equal to mocked value");
        assertEquals(salesOrderDTO.getPreferredTime(), orderTime, "Expected time to be equal to mocked value");
        assertEquals(salesOrderDTO.getLineItems().size(), 1, "Expected 1 line item to be set on sales order");

        LineItemDTO lineItemDTO = salesOrderDTO.getLineItems().get(0);
        assertEquals(
            lineItemDTO.getCustomerPartNumber(),
            CUSTOMER_PART_NUMBER,
            "Expected customer part number to equal expected value"
        );
        assertEquals(
            lineItemDTO.getQtyAvailable(),
            ERP_QUANTITY,
            "Expected quantity available to equal expected value"
        );
        assertEquals(lineItemDTO.getQuantity(), QUANTITY, "Expected quantity to equal expected value");
        assertEquals(lineItemDTO.getErpPartNumber(), PRODUCT_ID, "Expected quantity to equal expected value");
        assertNotNull(getOrderResponseDTO, "Expected non-null value for DTO returned");
    }

    @Test
    void submitOrder_cartNotFound() {
        assertThrows(
            CartNotFoundException.class,
            () -> cartService.submitOrder(CART_NOT_FOUND, submitOrderDTO, AUTH_TOKEN)
        );
    }

    @Test
    void submitOrder_willCallAndDeliveryNotFound() {
        assertThrows(
            DeliveryAndWillCallNotFoundException.class,
            () -> cartService.submitOrder(CART_NO_DELIVERY_OR_WILL_CALL, submitOrderDTO, AUTH_TOKEN)
        );
    }

    @Test
    void submitOrderPreview_delivery_success() throws Exception {
        GetOrderResponseDTO getOrderResponseDTO = cartService.submitOrderPreview(
            CART_ID_DELIVERY,
            submitOrderDTO,
            AUTH_TOKEN
        );
        verify(erpService, times(1)).submitSalesOrderPreview(salesOrderDTOArgumentCaptor.capture());
        CreateSalesOrderRequestDTO salesOrderDTO = salesOrderDTOArgumentCaptor.getValue();
        assertEquals(
            salesOrderDTO.getShipToEntityId(),
            erpUserInformation.getErpAccountId(),
            "Expected shipToEntity ID on sales order to equal value given in erp user info"
        );
        assertEquals(
            salesOrderDTO.getAddress().getCity(),
            address.getCity(),
            "Expected city field on sales order Fto equal value returned from DB"
        );
        assertEquals(
            salesOrderDTO.getAddress().getCountry(),
            address.getCountry(),
            "Expected country field on sales order to equal value returned from DB"
        );
        assertEquals(
            salesOrderDTO.getAddress().getPostalCode(),
            address.getZip(),
            "Expected postal code field on sales order to equal value returned from DB"
        );
        assertEquals(
            salesOrderDTO.getAddress().getState(),
            address.getState(),
            "Expected state code field on sales order to equal value returned from DB"
        );
        assertEquals(
            salesOrderDTO.getAddress().getStreetLineOne(),
            address.getStreet1(),
            "Expected street line 1 field on sales order to equal value returned from DB"
        );
        assertEquals(
            salesOrderDTO.getAddress().getStreetLineTwo(),
            address.getStreet2(),
            "Expected street line 2 field on sales order to equal value returned from DB"
        );
        assertEquals(
            salesOrderDTO.getInstructions(),
            DELIVERY_INSTRUCTIONS,
            "Expected instructions in sales order to equal instructions from delivery"
        );
        assertEquals(salesOrderDTO.getIsDelivery(), true, "Expected delivery flag to be set to true");
        assertEquals(
            salesOrderDTO.getShouldShipFullOrder(),
            true,
            "Expected shouldShipFullOrder flag to be set to true"
        );
        assertEquals(
            salesOrderDTO.getEclipseLoginId(),
            erpUserInformation.getUserId(),
            "Expected eclipse log in id to be equal to erp user info"
        );
        assertEquals(
            salesOrderDTO.getEclipsePassword(),
            erpUserInformation.getPassword(),
            "Expected eclipse password to be equal to erp user info"
        );
        assertEquals(salesOrderDTO.getPreferredDate(), orderDate, "Expected date to be equal to mocked value");
        assertEquals(salesOrderDTO.getPreferredTime(), orderTime, "Expected time to be equal to mocked value");
        assertEquals(salesOrderDTO.getLineItems().size(), 1, "Expected 1 line item to be set on sales order");

        LineItemDTO lineItemDTO = salesOrderDTO.getLineItems().get(0);
        assertEquals(
            lineItemDTO.getCustomerPartNumber(),
            CUSTOMER_PART_NUMBER,
            "Expected customer part number to equal expected value"
        );
        assertEquals(
            lineItemDTO.getQtyAvailable(),
            ERP_QUANTITY,
            "Expected quantity available to equal expected value"
        );
        assertEquals(lineItemDTO.getQuantity(), QUANTITY, "Expected quantity to equal expected value");
        assertEquals(lineItemDTO.getErpPartNumber(), PRODUCT_ID, "Expected quantity to equal expected value");
        assertNotNull(getOrderResponseDTO, "Expected non-null value for DTO returned");
    }

    @Test
    void submitOrderPreview_delivery_customAddress_success() throws Exception {
        GetOrderResponseDTO getOrderResponseDTO = cartService.submitOrderPreview(
            CART_ID_CUSTOM_ADDRESS,
            submitOrderDTO,
            AUTH_TOKEN
        );
        verify(erpService, times(1)).submitSalesOrderPreview(salesOrderDTOArgumentCaptor.capture());

        CreateSalesOrderRequestDTO salesOrderDTO = salesOrderDTOArgumentCaptor.getValue();
        salesOrderDTO.setLineItems(lineItemDTOList1);
        assertEquals(
            salesOrderDTO.getShipToEntityId(),
            erpUserInformation.getErpAccountId(),
            "Expected shipToEntity ID on sales order to equal value given in erp user info"
        );
        assertEquals(
            salesOrderDTO.getAddress().getCity(),
            customAddress.getCity(),
            "Expected city field on sales order Fto equal value returned from DB"
        );
        assertEquals(
            salesOrderDTO.getAddress().getCountry(),
            customAddress.getCountry(),
            "Expected country field on sales order to equal value returned from DB"
        );
        assertEquals(
            salesOrderDTO.getAddress().getPostalCode(),
            customAddress.getZip(),
            "Expected postal code field on sales order to equal value returned from DB"
        );
        assertEquals(
            salesOrderDTO.getAddress().getState(),
            customAddress.getState(),
            "Expected state code field on sales order to equal value returned from DB"
        );
        assertEquals(
            salesOrderDTO.getAddress().getStreetLineOne(),
            customAddress.getCompanyName(),
            "Expected street line 1 field on sales order to equal company_name value returned from DB"
        );
        assertEquals(
            salesOrderDTO.getAddress().getStreetLineTwo(),
            customAddress.getStreet1(),
            "Expected street line 2 field on sales order to equal street1 value returned from DB"
        );
        assertEquals(
            salesOrderDTO.getAddress().getStreetLineThree(),
            customAddress.getStreet2(),
            "Expected street line 3 field on sales order to equal street2 value returned from DB"
        );
        assertEquals(
            salesOrderDTO.getInstructions(),
            DELIVERY_INSTRUCTIONS,
            "Expected instructions in sales order to equal instructions from delivery"
        );
        assertEquals(salesOrderDTO.getIsDelivery(), true, "Expected delivery flag to be set to true");
        assertEquals(
            salesOrderDTO.getShouldShipFullOrder(),
            true,
            "Expected shouldShipFullOrder flag to be set to true"
        );
        assertEquals(
            salesOrderDTO.getEclipseLoginId(),
            erpUserInformation.getUserId(),
            "Expected eclipse log in id to be equal to erp user info"
        );
        assertEquals(
            salesOrderDTO.getEclipsePassword(),
            erpUserInformation.getPassword(),
            "Expected eclipse password to be equal to erp user info"
        );
        assertEquals(salesOrderDTO.getPreferredDate(), orderDate, "Expected date to be equal to mocked value");
        assertEquals(salesOrderDTO.getPreferredTime(), orderTime, "Expected time to be equal to mocked value");
        assertEquals(salesOrderDTO.getLineItems().size(), 1, "Expected 1 line item to be set on sales order");

        LineItemDTO lineItemDTO = salesOrderDTO.getLineItems().get(0);
        assertEquals(
            lineItemDTO.getCustomerPartNumber(),
            CUSTOMER_PART_NUMBER,
            "Expected customer part number to equal expected value"
        );
        assertEquals(
            lineItemDTO.getQtyAvailable(),
            ERP_QUANTITY,
            "Expected quantity available to equal expected value"
        );
        assertEquals(lineItemDTO.getQuantity(), QUANTITY, "Expected quantity to equal expected value");
        assertEquals(lineItemDTO.getErpPartNumber(), PRODUCT_ID, "Expected quantity to equal expected value");
        assertNotNull(getOrderResponseDTO, "Expected non-null value for DTO returned");
    }

    @Test
    void submitOrderPreview_will_call_success() throws Exception {
        GetOrderResponseDTO getOrderResponseDTO = cartService.submitOrderPreview(
            CART_ID_WILL_CALL,
            submitOrderDTO,
            AUTH_TOKEN
        );
        verify(erpService, times(1)).submitSalesOrderPreview(salesOrderDTOArgumentCaptor.capture());

        CreateSalesOrderRequestDTO salesOrderDTO = salesOrderDTOArgumentCaptor.getValue();
        assertEquals(
            salesOrderDTO.getShipToEntityId(),
            erpUserInformation.getErpAccountId(),
            "Expected shipToEntity ID on sales order to equal given value in erp user info"
        );
        assertNull(salesOrderDTO.getAddress(), "Expected address field on sales order to be null for will-call");
        assertEquals(
            salesOrderDTO.getInstructions(),
            DELIVERY_INSTRUCTIONS,
            "Expected instructions in sales order to equal instructions from delivery"
        );
        assertEquals(salesOrderDTO.getIsDelivery(), false, "Expected delivery flag to be set to true");
        assertEquals(
            salesOrderDTO.getEclipseLoginId(),
            erpUserInformation.getUserId(),
            "Expected eclipse log in id to be equal to erp user info"
        );
        assertEquals(
            salesOrderDTO.getEclipsePassword(),
            erpUserInformation.getPassword(),
            "Expected eclipse password to be equal to erp user info"
        );
        assertEquals(salesOrderDTO.getPreferredDate(), orderDate, "Expected date to be equal to mocked value");
        assertEquals(salesOrderDTO.getPreferredTime(), orderTime, "Expected time to be equal to mocked value");
        assertEquals(salesOrderDTO.getLineItems().size(), 1, "Expected 1 line item to be set on sales order");

        LineItemDTO lineItemDTO = salesOrderDTO.getLineItems().get(0);
        assertEquals(
            lineItemDTO.getCustomerPartNumber(),
            CUSTOMER_PART_NUMBER,
            "Expected customer part number to equal expected value"
        );
        assertEquals(
            lineItemDTO.getQtyAvailable(),
            ERP_QUANTITY,
            "Expected quantity available to equal expected value"
        );
        assertEquals(lineItemDTO.getQuantity(), QUANTITY, "Expected quantity to equal expected value");
        assertEquals(lineItemDTO.getErpPartNumber(), PRODUCT_ID, "Expected quantity to equal expected value");
        assertNotNull(getOrderResponseDTO, "Expected non-null value for DTO returned");
    }

    @Test
    void submitOrderPreview_cartNotFound() {
        assertThrows(
            CartNotFoundException.class,
            () -> cartService.submitOrderPreview(CART_NOT_FOUND, submitOrderDTO, AUTH_TOKEN)
        );
    }

    @Test
    void submitOrderPreview_willCallAndDeliveryNotFound() {
        assertThrows(
            DeliveryAndWillCallNotFoundException.class,
            () -> cartService.submitOrderPreview(CART_NO_DELIVERY_OR_WILL_CALL, submitOrderDTO, AUTH_TOKEN)
        );
    }

    @Test
    void submitOrderPreview_invalidShipViaDuringCheckoutExceptionWillCall() throws Exception {
        String eclipseError530 =
            "[ErrorMessageList(ErrorMessages=[ErrorMessage(Code=530, Description=Invalid ShipViaID: WILL CALL.)])]";

        String expectedMessageForMissingWillCallInfo = "Your current branch is not set up for will call orders.";

        when(erpService.submitSalesOrderPreview(any()))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, eclipseError530));

        InvalidShipViaDuringCheckoutException exception = assertThrows(
            InvalidShipViaDuringCheckoutException.class,
            () -> cartService.submitOrderPreview(CART_ID_WILL_CALL, submitOrderDTO, AUTH_TOKEN)
        );

        verify(notificationService, times(1)).sendDeliveryOptionMissingFromBranchEmail(any());
        assertTrue(exception.getMessage().equals(expectedMessageForMissingWillCallInfo));
    }

    @Test
    void submitOrderPreview_invalidShipViaDuringCheckoutExceptionDelivery() throws Exception {
        String eclipseError530 =
            "[ErrorMessageList(ErrorMessages=[ErrorMessage(Code=530, Description=Invalid ShipViaID: OT OUR TRUCK.)])]";

        String exceptionMessageForMissingDeliveryInfo = "Your current branch is not set up for will call orders.";

        when(erpService.submitSalesOrderPreview(any()))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, eclipseError530));

        InvalidShipViaDuringCheckoutException exception = assertThrows(
            InvalidShipViaDuringCheckoutException.class,
            () -> cartService.submitOrderPreview(CART_ID_WILL_CALL, submitOrderDTO, AUTH_TOKEN)
        );

        verify(notificationService, times(1)).sendDeliveryOptionMissingFromBranchEmail(any());
        assertTrue(exception.getMessage().equals(exceptionMessageForMissingDeliveryInfo));
    }

    @Test
    void submitOrderForApproval_cartNotFound() {
        assertThrows(
            CartNotFoundException.class,
            () -> cartService.submitOrderForApproval(CART_NOT_FOUND, submitOrderDTO, AUTH_TOKEN)
        );
    }

    @Test
    void getOrdersPendingApproval_singleOrder() throws Exception {
        Cart cart = new Cart();
        cart.setPoNumber(PURCHASE_ORDER_NUMBER);
        cart.setTotal(ORDER_TOTAL);
        cart.setOwnerId(USER_EXISTS);

        OrdersPendingApproval orderPendingApproval = new OrdersPendingApproval();
        orderPendingApproval.setCart(cart);
        orderPendingApproval.setSubmissionDate(SUBMITTED_DATE);
        orderPendingApproval.setOrderId(AWAITING_APPROVAL_ORDER_ID);

        when(cartDAO.findAllByApproverIdAndApprovalState(any(), any())).thenReturn(Optional.of(Arrays.asList(cart)));
        when(ordersPendingApprovalDAO.findByCartId(any())).thenReturn(Optional.of(orderPendingApproval));

        List<OrderPendingApprovalDTO> orderPendingApprovalDTOList = cartService.getOrdersPendingApproval(
            UUID.randomUUID(),
            AUTH_TOKEN
        );

        assertTrue(orderPendingApprovalDTOList.get(0).getPurchaseOrderNumber().equals(PURCHASE_ORDER_NUMBER));
        assertEquals(ORDER_TOTAL, orderPendingApprovalDTOList.get(0).getOrderTotal());
        assertTrue(orderPendingApprovalDTOList.get(0).getSubmittedByName().equals(FULL_NAME));
        assertTrue(SUBMITTED_DATE_FORMATTED.equals(orderPendingApprovalDTOList.get(0).getSubmissionDate()));
        assertTrue(orderPendingApprovalDTOList.get(0).getOrderId().equals(AWAITING_APPROVAL_ORDER_ID));
    }

    @Test
    void getOrdersPendingApproval_multipleOrders() throws Exception {
        User user = new User();
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);

        Cart firstOrder = new Cart();
        firstOrder.setPoNumber(PURCHASE_ORDER_NUMBER);
        firstOrder.setTotal(ORDER_TOTAL);
        firstOrder.setOwnerId(USER_EXISTS);
        firstOrder.setId(UUID.randomUUID());

        Cart secondOrder = new Cart();
        secondOrder.setPoNumber(PURCHASE_ORDER_NUMBER);
        secondOrder.setTotal(ORDER_TOTAL);
        secondOrder.setOwnerId(USER_EXISTS);
        secondOrder.setId(UUID.randomUUID());

        OrdersPendingApproval orderPendingApprovalFirst = new OrdersPendingApproval();
        orderPendingApprovalFirst.setCart(firstOrder);
        orderPendingApprovalFirst.setSubmissionDate(SUBMITTED_DATE);
        orderPendingApprovalFirst.setOrderId(AWAITING_APPROVAL_ORDER_ID);

        OrdersPendingApproval orderPendingApprovalSecond = new OrdersPendingApproval();
        orderPendingApprovalSecond.setCart(secondOrder);
        orderPendingApprovalSecond.setSubmissionDate(SUBMITTED_DATE);
        orderPendingApprovalSecond.setOrderId(AWAITING_APPROVAL_ORDER_ID);

        when(userDAO.findById(any())).thenReturn(Optional.of(user));
        when(cartDAO.findAllByApproverIdAndApprovalState(any(), any()))
            .thenReturn(Optional.of(Arrays.asList(firstOrder, secondOrder)));
        when(ordersPendingApprovalDAO.findByCartId(any()))
            .thenReturn(Optional.of(orderPendingApprovalFirst), Optional.of(orderPendingApprovalSecond));

        List<OrderPendingApprovalDTO> orderPendingApprovalDTOList = cartService.getOrdersPendingApproval(
            UUID.randomUUID(),
            AUTH_TOKEN
        );

        assertTrue(orderPendingApprovalDTOList.get(0).getPurchaseOrderNumber().equals(PURCHASE_ORDER_NUMBER));
        assertEquals(ORDER_TOTAL, orderPendingApprovalDTOList.get(0).getOrderTotal());
        assertTrue(orderPendingApprovalDTOList.get(0).getSubmittedByName().equals(FULL_NAME));
        assertTrue(SUBMITTED_DATE_FORMATTED.equals(orderPendingApprovalDTOList.get(0).getSubmissionDate()));
        assertTrue(orderPendingApprovalDTOList.get(1).getOrderId().equals(AWAITING_APPROVAL_ORDER_ID));
        assertTrue(orderPendingApprovalDTOList.get(1).getPurchaseOrderNumber().equals(PURCHASE_ORDER_NUMBER));
        assertEquals(ORDER_TOTAL, orderPendingApprovalDTOList.get(1).getOrderTotal());
        assertTrue(orderPendingApprovalDTOList.get(1).getSubmittedByName().equals(FULL_NAME));
        assertTrue(SUBMITTED_DATE_FORMATTED.equals(orderPendingApprovalDTOList.get(1).getSubmissionDate()));
        assertTrue(orderPendingApprovalDTOList.get(1).getOrderId().equals(AWAITING_APPROVAL_ORDER_ID));
        assertEquals(2, orderPendingApprovalDTOList.size(), "User is listed as approver on two separate orders");
    }

    @Test
    void getOrdersPendingApproval_noOrdersToApprove() throws Exception {
        when(cartDAO.findAllByApproverIdAndApprovalState(any(), any())).thenReturn(Optional.empty());

        List<OrderPendingApprovalDTO> orderPendingApprovalDTOList = cartService.getOrdersPendingApproval(
            UUID.randomUUID(),
            AUTH_TOKEN
        );

        assertTrue(orderPendingApprovalDTOList.isEmpty(), "User does not have any orders to approve.");
    }

    @Test
    void getOrdersPendingApproval_UserNotFound() throws Exception {
        var invalidUserId = UUID.randomUUID();

        Cart cart = new Cart();
        cart.setPoNumber(PURCHASE_ORDER_NUMBER);
        cart.setSubtotal(ORDER_TOTAL);
        cart.setOwnerId(invalidUserId);

        when(accountService.getUser(invalidUserId.toString(), AUTH_TOKEN))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        when(userDAO.findById(any())).thenReturn(Optional.empty());
        when(cartDAO.findAllByApproverIdAndApprovalState(any(), any())).thenReturn(Optional.of(Arrays.asList(cart)));

        assertThrows(
            UserNotFoundException.class,
            () -> cartService.getOrdersPendingApproval(UUID.randomUUID(), AUTH_TOKEN)
        );
    }

    @Test
    void getOrdersPendingApproval_OrderNotFound() throws Exception {
        User user = new User();
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);

        Cart cart = new Cart();
        cart.setPoNumber(PURCHASE_ORDER_NUMBER);
        cart.setSubtotal(ORDER_TOTAL);
        cart.setOwnerId(USER_EXISTS);
        cart.setId(UUID.randomUUID());

        when(userDAO.findById(any())).thenReturn(Optional.of(user));
        when(cartDAO.findAllByApproverIdAndApprovalState(any(), any())).thenReturn(Optional.of(Arrays.asList(cart)));
        when(ordersPendingApprovalDAO.findByCartId(any())).thenReturn(Optional.empty());

        assertThrows(
            PendingOrderNotFoundException.class,
            () -> cartService.getOrdersPendingApproval(UUID.randomUUID(), AUTH_TOKEN)
        );
    }

    @Test
    void getOrderPendingApproval_OrderNotFound() throws Exception {
        User user = new User();
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);

        Cart cart = new Cart();
        cart.setPoNumber(PURCHASE_ORDER_NUMBER);
        cart.setSubtotal(ORDER_TOTAL);
        cart.setOwnerId(UUID.randomUUID());
        cart.setId(UUID.randomUUID());

        when(userDAO.findById(any())).thenReturn(Optional.of(user));
        when(ordersPendingApprovalDAO.findByOrderId(any())).thenReturn(Optional.empty());

        assertThrows(
            PendingOrderNotFoundException.class,
            () -> cartService.getOrderPendingApproval(AWAITING_APPROVAL_ORDER_ID, AUTH_TOKEN)
        );
    }

    @Test
    void getOrderPendingApproval_UserNotFound() throws Exception {
        var invalidUserId = UUID.randomUUID();

        Cart cart = new Cart();
        cart.setPoNumber(PURCHASE_ORDER_NUMBER);
        cart.setSubtotal(ORDER_TOTAL);
        cart.setOwnerId(invalidUserId);

        OrdersPendingApproval orderPendingApproval = new OrdersPendingApproval();
        orderPendingApproval.setCart(cart);

        when(accountService.getUser(invalidUserId.toString(), AUTH_TOKEN))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        when(userDAO.findById(any())).thenReturn(Optional.empty());
        when(ordersPendingApprovalDAO.findByOrderId(any())).thenReturn(Optional.of(orderPendingApproval));

        assertThrows(
            UserNotFoundException.class,
            () -> cartService.getOrderPendingApproval(AWAITING_APPROVAL_ORDER_ID, AUTH_TOKEN)
        );
    }

    @Test
    void getOrderPendingApproval_singleOrder() throws Exception {
        User user = new User();
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);

        Cart cart = new Cart();
        cart.setPoNumber(PURCHASE_ORDER_NUMBER);
        cart.setTotal(ORDER_TOTAL);
        cart.setOwnerId(USER_EXISTS);

        OrdersPendingApproval orderPendingApproval = new OrdersPendingApproval();
        orderPendingApproval.setCart(cart);
        orderPendingApproval.setSubmissionDate(SUBMITTED_DATE);
        orderPendingApproval.setOrderId(AWAITING_APPROVAL_ORDER_ID);

        when(userDAO.findById(any())).thenReturn(Optional.of(user));
        when(ordersPendingApprovalDAO.findByOrderId(any())).thenReturn(Optional.of(orderPendingApproval));

        OrderPendingApprovalDTO orderPendingApprovalDTO = cartService.getOrderPendingApproval(
            AWAITING_APPROVAL_ORDER_ID,
            AUTH_TOKEN
        );

        assertTrue(orderPendingApprovalDTO.getPurchaseOrderNumber().equals(PURCHASE_ORDER_NUMBER));
        assertEquals(ORDER_TOTAL, orderPendingApprovalDTO.getOrderTotal());
        assertTrue(orderPendingApprovalDTO.getSubmittedByName().equals(FULL_NAME));
        assertTrue(SUBMITTED_DATE_FORMATTED.equals(orderPendingApprovalDTO.getSubmissionDate()));
        assertTrue(orderPendingApprovalDTO.getOrderId().equals(AWAITING_APPROVAL_ORDER_ID));
    }

    @Test
    void approveOrder_cartNotFound() throws Exception {
        assertThrows(
            CartNotFoundException.class,
            () -> cartService.approveOrder(CART_NOT_FOUND, submitOrderDTO, AUTH_TOKEN)
        );
    }

    @Test
    void approveOrder_orderSubmittedSuccess() throws Exception {
        Cart cart = new Cart();
        cart.setId(CART_ID_WILL_CALL);
        cart.setPoNumber(PURCHASE_ORDER_NUMBER);
        cart.setTotal(ORDER_TOTAL);
        cart.setShipToId(UUID.randomUUID());
        cart.setErpSystemName(ERPSystem.ECLIPSE);

        OrdersPendingApproval orderPendingApproval = new OrdersPendingApproval();
        orderPendingApproval.setCart(cart);
        orderPendingApproval.setSubmissionDate(SUBMITTED_DATE);
        orderPendingApproval.setOrderId(AWAITING_APPROVAL_ORDER_ID);

        when(ordersPendingApprovalDAO.findByCartId(any())).thenReturn(Optional.of(orderPendingApproval));
        GetOrderResponseDTO getOrderResponseDTO = cartService.approveOrder(
            CART_ID_WILL_CALL,
            submitOrderDTO,
            AUTH_TOKEN
        );
        verify(erpService, times(1)).submitSalesOrder(salesOrderDTOArgumentCaptor.capture());

        CreateSalesOrderRequestDTO salesOrderDTO = salesOrderDTOArgumentCaptor.getValue();
        assertEquals(
            salesOrderDTO.getShipToEntityId(),
            erpUserInformation.getErpAccountId(),
            "Expected shipToEntity ID on sales order to equal given value in erp user info"
        );
        assertNull(salesOrderDTO.getAddress(), "Expected address field on sales order to be null for will-call");
        assertEquals(
            salesOrderDTO.getInstructions(),
            DELIVERY_INSTRUCTIONS,
            "Expected instructions in sales order to equal instructions from delivery"
        );
        assertEquals(salesOrderDTO.getIsDelivery(), false, "Expected delivery flag to be set to true");
        assertEquals(
            salesOrderDTO.getEclipseLoginId(),
            erpUserInformation.getUserId(),
            "Expected eclipse log in id to be equal to erp user info"
        );
        assertEquals(
            salesOrderDTO.getEclipsePassword(),
            erpUserInformation.getPassword(),
            "Expected eclipse password to be equal to erp user info"
        );
        assertEquals(salesOrderDTO.getPreferredDate(), orderDate, "Expected date to be equal to mocked value");
        assertEquals(salesOrderDTO.getPreferredTime(), orderTime, "Expected time to be equal to mocked value");
        assertEquals(salesOrderDTO.getLineItems().size(), 1, "Expected 1 line item to be set on sales order");

        LineItemDTO lineItemDTO = salesOrderDTO.getLineItems().get(0);
        assertEquals(
            lineItemDTO.getCustomerPartNumber(),
            CUSTOMER_PART_NUMBER,
            "Expected customer part number to equal expected value"
        );
        assertEquals(
            lineItemDTO.getQtyAvailable(),
            ERP_QUANTITY,
            "Expected quantity available to equal expected value"
        );
        assertEquals(lineItemDTO.getQuantity(), QUANTITY, "Expected quantity to equal expected value");
        assertEquals(lineItemDTO.getErpPartNumber(), PRODUCT_ID, "Expected quantity to equal expected value");
        assertNotNull(getOrderResponseDTO, "Expected non-null value for DTO returned");
    }

    @Test
    void buildApproveRejectOrderNotificationDTO_rejectionOrderDateMatchesPendingApprovalSubmissionDate()
        throws Exception {
        OrdersPendingApproval orderPendingApproval = new OrdersPendingApproval();
        orderPendingApproval.setCart(getCartDefault);
        orderPendingApproval.setSubmissionDate(SUBMITTED_DATE);
        orderPendingApproval.setOrderId(AWAITING_APPROVAL_ORDER_ID);

        when(ordersPendingApprovalDAO.findByCartId(any())).thenReturn(Optional.of(orderPendingApproval));

        SalesOrderSubmitNotificationDTO salesOrderSubmitNotificationDTO = cartService.buildApproveRejectOrderNotificationDTO(
            getCartDefault,
            USER_EXISTS,
            genericGetOrderResponseDTO,
            AUTH_TOKEN,
            orderPendingApproval
        );

        assertTrue(
            salesOrderSubmitNotificationDTO.getOrderDate().equals(SUBMITTED_DATE_FORMATTED),
            "For rejections, order date should be pulled from pending order submission date"
        );
    }

    @Test
    void rejectOrder_cartNotFound() throws Exception {
        assertThrows(
            CartNotFoundException.class,
            () -> cartService.rejectOrder(CART_NOT_FOUND, new RejectOrderDTO(), AUTH_TOKEN)
        );
    }

    @Test
    void buildOrderStatusNotificationDTO_ensureRequiredEmailNotificationDataSet() throws Exception {
        BranchDTO branchDTO = new BranchDTO();
        branchDTO.setDomain(DOMAIN);
        branchDTO.setBrand(BRAND);

        genericGetOrderResponseDTO.setDeliveryMethod(DeliveryMethodEnum.WILLCALL.name());

        List<OrderLineItemResponseDTO> orderLineItemResponseDTOS = new ArrayList<>();
        OrderLineItemResponseDTO orderLineItemResponseDTO = new OrderLineItemResponseDTO();
        orderLineItemResponseDTOS.add(orderLineItemResponseDTO);
        genericGetOrderResponseDTO.setLineItems(orderLineItemResponseDTOS);
        genericGetOrderResponseDTO.setEmail("example@example.com");

        User user = new User();
        user.setFirstName("First");

        when(accountService.getHomeBranch(SHIP_TO_ID.toString())).thenReturn(branchDTO);
        when(userDAO.findByEmail(genericGetOrderResponseDTO.getEmail())).thenReturn(Optional.of(user));

        SalesOrderSubmitNotificationDTO salesOrderSubmitNotificationDTO = cartService.buildOrderNotificationDTO(
            SHIP_TO_ID,
            genericGetOrderResponseDTO,
            null
        );

        OrderSubmitEmailBranchDTO orderSubmitEmailBranchDTO = salesOrderSubmitNotificationDTO.getBranchDTO();
        BranchOrderInfoDTO branchOrderInfoDTO = genericGetOrderResponseDTO.getBranchInfo();

        assertEquals(
            orderSubmitEmailBranchDTO.getBranchHours(),
            branchOrderInfoDTO.getBranchHours(),
            "Branch hours are required for email notification"
        );
        assertEquals(
            orderSubmitEmailBranchDTO.getBranchPhone(),
            branchOrderInfoDTO.getBranchPhone(),
            "Branch phone is required for email notification"
        );
        assertEquals(
            orderSubmitEmailBranchDTO.getStreetLineOne(),
            branchOrderInfoDTO.getStreetLineOne(),
            "Branch address is required for email notification"
        );
        assertEquals(
            orderSubmitEmailBranchDTO.getStreetLineTwo(),
            branchOrderInfoDTO.getStreetLineTwo(),
            "Branch address is required for email notification"
        );
        assertEquals(
            orderSubmitEmailBranchDTO.getCity(),
            branchOrderInfoDTO.getCity(),
            "Branch address is required for email notification"
        );
        assertEquals(
            orderSubmitEmailBranchDTO.getPostalCode(),
            branchOrderInfoDTO.getPostalCode(),
            "Branch address is required for email notification"
        );
        assertEquals(
            orderSubmitEmailBranchDTO.getState(),
            branchOrderInfoDTO.getState(),
            "Branch address is required for email notification"
        );
        assertEquals(
            salesOrderSubmitNotificationDTO.getBrand(),
            branchDTO.getBrand(),
            "Brand is required for email notification"
        );
        assertEquals(
            salesOrderSubmitNotificationDTO.getDomain(),
            branchDTO.getDomain(),
            "Domain is required for email notification"
        );
        assertEquals(
            salesOrderSubmitNotificationDTO.getName(),
            user.getFirstName(),
            "User's first name is required for email notification"
        );
    }

    @Test
    void cartDelete_happyPath() {
        when(cartDAO.findAllByShipToId(CART_DEFAULT)).thenReturn(List.of(getCartDefault));
        when(lineItemsDAO.deleteAllByCartId(any())).thenReturn(5L);

        assertDoesNotThrow(() -> {
            cartService.deleteCartsByShipToAccountId(CART_DEFAULT);
        });

        verify(lineItemsDAO, times(1)).deleteAllByCartId(any());
        verify(cartDAO, times(1)).deleteById(any());
    }

    private GetOrderResponseDTO buildGenericGetOrderResponseDTO() {
        BranchOrderInfoDTO branchOrderInfoDTO = new BranchOrderInfoDTO();
        branchOrderInfoDTO.setBranchHours(HOURS);
        branchOrderInfoDTO.setBranchPhone(PHONE);

        EclipseAddressResponseDTO address = new EclipseAddressResponseDTO();
        address.setStreetLineOne(STREET1);
        address.setStreetLineTwo(STREET2);
        address.setCity(CITY);
        address.setPostalCode(POSTAL);
        address.setState(STATE);

        GetOrderResponseDTO getOrderResponseDTO = new GetOrderResponseDTO();
        getOrderResponseDTO.setBranchInfo(branchOrderInfoDTO);
        getOrderResponseDTO.setShipAddress(address);
        getOrderResponseDTO.setDeliveryMethod(DeliveryMethodEnum.DELIVERY.name());

        List<OrderLineItemResponseDTO> orderLineItemResponseDTOS = new ArrayList<>();
        OrderLineItemResponseDTO orderLineItemResponseDTO = new OrderLineItemResponseDTO();
        orderLineItemResponseDTOS.add(orderLineItemResponseDTO);

        getOrderResponseDTO.setLineItems(orderLineItemResponseDTOS);
        getOrderResponseDTO.setWebStatus(WebStatus.SUBMITTED.name());

        return getOrderResponseDTO;
    }
}
