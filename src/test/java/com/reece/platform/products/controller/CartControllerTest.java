package com.reece.platform.products.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.products.TestUtils;
import com.reece.platform.products.exceptions.*;
import com.reece.platform.products.helpers.ERPSystem;
import com.reece.platform.products.model.*;
import com.reece.platform.products.model.DTO.*;
import com.reece.platform.products.model.entity.Cart;
import com.reece.platform.products.model.entity.Delivery;
import com.reece.platform.products.model.entity.WillCall;
import com.reece.platform.products.service.AccountService;
import com.reece.platform.products.service.AuthorizationService;
import com.reece.platform.products.service.CartService;
import com.reece.platform.products.service.OrdersService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = { CartController.class, GlobalExceptionHandler.class })
@AutoConfigureMockMvc
@EnableWebMvc
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AuthorizationService authorizationService;

    @MockBean
    private CartService cartService;

    @MockBean
    private OrdersService ordersService;

    private UserAccountInfo userAccountInfo;
    private ErpUserInformation erpUserInformation;
    private ItemQuantityErpInformation itemQuantityErpInformation;
    private LineItemResponseDTO updatedQuantityProduct;
    private GetOrderResponseDTO deliveryOrderResponseDTO;
    private GetOrderResponseDTO willCallOrderResponseDTO;
    private SubmitOrderDTO submitOrderDTO;
    private WillCallDTO willCallDto;
    private DeliveryDTO deliveryDto;
    private WillCall willCall;
    private Delivery delivery;
    private CartResponseDTO cart;
    private CartResponseDTO cartWithProducts;
    private UpdateItemQtyResponseDTO updatedItemQtyResponse;
    private CartResponseDTO cartAfterDelete;
    private CartResponseDTO cartAfterBranchUpdate;
    private Cart cartAfterWillCallCreate;
    private Cart cartAfterWillCallUpdate;
    private Cart cartAfterDeliveryCreate;
    private Cart cartAfterDeliveryUpdate;
    private ItemsInfoDTO itemsInfoDTO;
    private static final UUID CART_ID = UUID.randomUUID();
    private static final UUID LIST_ID = UUID.randomUUID();
    private static final UUID CART_ID_NOT_FOUND = UUID.randomUUID();
    private static final UUID CART_ID_BRANCH_NOT_FOUND = UUID.randomUUID();
    private static final UUID USER_ID_BRANCH_NOT_FOUND = UUID.randomUUID();
    private static final UUID CART_ID_INVALID_QTY = UUID.randomUUID();
    private static final UUID ACCOUNT_ID = UUID.randomUUID();
    private static final UUID BILL_TO_ACCOUNT_ID = UUID.randomUUID();
    private static final UUID USER_ID = UUID.randomUUID();
    private static final UUID ACCOUNT_ID_CART_EXISTS = UUID.randomUUID();
    private static final UUID USER_ID_CART_EXISTS = UUID.randomUUID();
    private static final UUID ITEM_ID = UUID.randomUUID();
    private static final UUID ITEM_ID_NOT_FOUND = UUID.randomUUID();
    private static final UUID CART_ID_NO_WILL_CALL_OR_DELIVERY = UUID.randomUUID();
    private static final UUID CART_ID_DELIVERY = UUID.randomUUID();
    private static final UUID CART_ID_WILL_CALL = UUID.randomUUID();
    private static final UUID CART_ID_NO_DELIVERY = UUID.randomUUID();
    private static final UUID CART_ID_NO_WILL_CALL = UUID.randomUUID();
    private static final String HOME_BRANCH_NAME = "HOME_BRANCH_NAME";
    private static final String PO_NUMBER = "PO_NUMBER";
    private static final String PRICING_BRANCH_ID = "PRICING_BRANCH_ID";
    private static final String REJECTION_REASON = "REJECTION_REASON";
    private static final String SHIPPING_BRANCH_ID = "SHIPPING_BRANCH_ID";
    private static final String SHIPPING_BRANCH_ID_AFTER_BRANCH_UPDATE = "SHIPPING_BRANCH_ID_AFTER_BRANCH_UPDATE";
    private static final String PRODUCT_ID = "PRODUCT_ID";
    private static final String ERP_PASSWORD = "password";
    private static final String ZIP_CODE = "zip";
    private static final String STATE = "state";
    private static final String CITY = "city";
    private static final String STREET1 = "street1";
    private static final String STREET2 = "street2";
    private static final String PHONE_NUMBER = "111-111-1111";
    private static final String AWAITING_APPROVAL_ORDER_ID = "AA1175057246";
    private static final String QUOTE_ID = "quote";
    private static final String BRANCH_ID = "branch";
    private static final UUID ERP_SYSTEM = UUID.randomUUID();
    private static final UUID ERP_USER_ID = UUID.randomUUID();
    private static final int SHIPPING_AND_HANDLING = 12;
    private static final int SUBTOTAL = 13;
    private static final int TAX = 15;
    private static final int TOTAL = 16;
    private static final int QUANTITY = 5;
    private static final float PRICE_PER_UNIT = 22.44f;
    private static final int QUANTITY_AVAILABLE = 20;
    private static final String UOM = "ea";
    private static final int INC_QUANTITY = 1;
    private static final String AUTH_TOKEN =
        "Bearer eyJraWQiOiItYlVObXhLMndvLUR4OFg5elRlb0drTU1DZWFlMW8wRnVjYzdNVWV1QkZ3IiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULjVhNzNyOEtQRHJXLWJZMkJQenlKWUJQRnFucVZqdlJEcEdMU1k0c1lYYkkiLCJpc3MiOiJodHRwczovL2Rldi00MzI1NDYub2t0YS5jb20vb2F1dGgyL2RlZmF1bHQiLCJhdWQiOiJhcGk6Ly9kZWZhdWx0IiwiaWF0IjoxNjE1ODE5NzMwLCJleHAiOjE2MTU5MDYxMzAsImNpZCI6IjBvYTEzYjBiNWJNS1haZkpVNHg3IiwidWlkIjoiMDB1MmQzMXBpejVLS0Y2c2I0eDciLCJzY3AiOlsiZW1haWwiLCJvcGVuaWQiLCJwcm9maWxlIl0sInN1YiI6InNkZmtqYnJhc250QHRlc3QuY29tIiwiZWNvbW1Vc2VySWQiOiI5ZmQxNjJiOS1iMzU0LTRkMDctYWM1My0yMDkxYjFmMmJlYzIiLCJlY29tbVBlcm1pc3Npb25zIjpbImVkaXRfcHJvZmlsZSIsImVkaXRfbGlzdCIsInN1Ym1pdF9xdW90ZV9vcmRlciIsImludml0ZV91c2VyIiwidmlld19pbnZvaWNlIiwiYXBwcm92ZV9jYXJ0IiwibWFuYWdlX3BheW1lbnRfbWV0aG9kcyIsImFwcHJvdmVfYWNjb3VudF91c2VyIiwibWFuYWdlX3JvbGVzIiwic3VibWl0X2NhcnRfd2l0aG91dF9hcHByb3ZhbCJdfQ.Ew3owigdqaPNQDUiG1kOxqkSZFFRHxFB2qAlzPiAIkT4ruXTqad9-cGZGBkhRYLjxhuuIJ2tdilb1nwuxNKEcZbxOBTrXkrfubh-_FrDkHjVVa59mvcXbgd1gCcADd3bs_wZCzpyJDBF_LQ3h5cYOtgvwubMQ-71Hi127fP-SgVwYmb38bMVY4IKTkja-vCeoixKGjk8p_1YRCvdpfnGxJaP9vgUqGP1-ebnN5EzO0WlkC7IHtnjXcHZdUHtIKA6NCkvlCI127_Rh8gCU6mRVoQdHjKqya8zvSkNuKAz8NijsJAk3gLSJo4Fxv_7qedBDW5XbmBaaHhw-pBezn0JSQ";
    private static final String AUTH_TOKEN_CANT_APPROVE_ORDER =
        "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ2ZXIiOjEsImp0aSI6IkFULnFaY1dWVFUybEtubExBZkhWbmt2SGRSdmQ0R0hPVGVsZUNHM3o1X0E2OTQiLCJpc3MiOiJodHRwczovL2Rldi00MzI1NDYub2t0YS5jb20vb2F1dGgyL2RlZmF1bHQiLCJhdWQiOiJhcGk6Ly9kZWZhdWx0IiwiaWF0IjoxNjI5MjM2NTgyLCJleHAiOjE2MjkzMjI5ODIsImNpZCI6IjBvYTEzYjBiNWJNS1haZkpVNHg3IiwidWlkIjoiMDB1M2R2NW4zaFp4Ulh4UzE0eDciLCJzY3AiOlsiZW1haWwiLCJwcm9maWxlIiwib3BlbmlkIl0sInN1YiI6ImVjbGlwc2VzdGFuZHVzZXJkZXYyKzM1NjQ4QG1vcnNjby5jb20iLCJlY29tbVVzZXJJZCI6ImRjOTc4ZGNhLTVjNmEtNDZlOS05YzhkLTdhODI4ZDhlNjljMiIsImVjb21tUGVybWlzc2lvbnMiOlsic3VibWl0X3F1b3RlX29yZGVyIiwiZWRpdF9saXN0Iiwidmlld19pbnZvaWNlIl19.UcyawVE7hJJqFVmWb0wpQRl_WB2tXXCFgN8NenhcfgU";
    private static final UUID CUSTOMER_NUMBER = UUID.randomUUID();

    @BeforeEach
    public void setup() throws Exception {
        userAccountInfo = new UserAccountInfo();
        userAccountInfo.setShipToAccountId(UUID.randomUUID());
        userAccountInfo.setUserId(UUID.randomUUID());

        erpUserInformation = new ErpUserInformation();
        erpUserInformation.setErpAccountId(ACCOUNT_ID.toString());

        submitOrderDTO = new SubmitOrderDTO();
        submitOrderDTO.setErpUserInformation(erpUserInformation);
        submitOrderDTO.setBillToAccountId(BILL_TO_ACCOUNT_ID.toString());

        itemQuantityErpInformation = new ItemQuantityErpInformation();
        itemQuantityErpInformation.setErpUserInformation(erpUserInformation);
        itemQuantityErpInformation.setQty(QUANTITY);

        updatedQuantityProduct = new LineItemResponseDTO();

        CreditCardDTO creditCardDTO = new CreditCardDTO();

        when(authorizationService.userIsEmployee(any())).thenReturn(Boolean.FALSE);
        when(authorizationService.userCanSubmitOrder(any())).thenCallRealMethod();

        when(cartService.createCart(userAccountInfo, null)).thenReturn(CART_ID);
        when(cartService.getCart(eq(CART_ID_NOT_FOUND), eq(Boolean.FALSE), any()))
            .thenThrow(CartNotFoundException.class);

        AccountResponseDTO accountResponseDTO = new AccountResponseDTO();
        accountResponseDTO.setErpId(UUID.randomUUID());
        accountResponseDTO.setErpAccountId(UUID.randomUUID().toString());
        accountResponseDTO.setCompanyName(HOME_BRANCH_NAME);
        accountResponseDTO.setStreet1(STREET1);
        accountResponseDTO.setStreet2(STREET2);
        accountResponseDTO.setCity(CITY);
        accountResponseDTO.setState(STATE);
        accountResponseDTO.setZip(ZIP_CODE);
        accountResponseDTO.setPhoneNumber(PHONE_NUMBER);
        accountResponseDTO.setBranchId(SHIPPING_BRANCH_ID);
        when(accountService.getAccountData(any(), any(), any())).thenReturn(accountResponseDTO);

        Cart cartEntity = new Cart();
        cartEntity.setApprovalState(UUID.randomUUID());
        cartEntity.setApproverId(UUID.randomUUID());
        cartEntity.setCreditCard(creditCardDTO);
        cartEntity.setId(UUID.randomUUID());
        cartEntity.setOwnerId(UUID.randomUUID());
        cartEntity.setPaymentMethodType(PaymentMethodTypeEnum.BILLTOACCOUNT);
        cartEntity.setPoNumber(PO_NUMBER);
        cartEntity.setPricingBranchId(PRICING_BRANCH_ID);
        cartEntity.setRejectionReason(REJECTION_REASON);
        cartEntity.setShippingBranchId(SHIPPING_BRANCH_ID);
        cartEntity.setShippingHandling(SHIPPING_AND_HANDLING);
        cartEntity.setSubtotal(SUBTOTAL);
        cartEntity.setTax(TAX);
        cartEntity.setTotal(TOTAL);
        cartEntity.setProducts(null);
        cartEntity.setErpSystemName(ERPSystem.ECLIPSE);
        cart = new CartResponseDTO(cartEntity);

        cartWithProducts = new CartResponseDTO();
        cartWithProducts.setApprovalState(UUID.randomUUID());
        cartWithProducts.setApproverId(UUID.randomUUID());
        cartWithProducts.setCreditCard(creditCardDTO);
        cartWithProducts.setId(UUID.randomUUID());
        cartWithProducts.setOwnerId(UUID.randomUUID());
        cartWithProducts.setPaymentMethodType(PaymentMethodTypeEnum.BILLTOACCOUNT.toString());
        cartWithProducts.setPoNumber(PO_NUMBER);
        cartWithProducts.setPricingBranchId(PRICING_BRANCH_ID);
        cartWithProducts.setRejectionReason(REJECTION_REASON);
        cartWithProducts.setShippingBranchId(SHIPPING_BRANCH_ID);
        cartWithProducts.setShippingHandling(SHIPPING_AND_HANDLING);
        cartWithProducts.setSubtotal(SUBTOTAL);
        cartWithProducts.setTax(TAX);
        cartWithProducts.setTotal(TOTAL);
        cartWithProducts.setProducts(new ArrayList<>());
        cartWithProducts.setErpSystemName(ERPSystem.ECLIPSE.name());

        updatedItemQtyResponse = new UpdateItemQtyResponseDTO();
        updatedItemQtyResponse.setSubtotal(BigDecimal.valueOf(SUBTOTAL));

        updatedItemQtyResponse.setProduct(updatedQuantityProduct); //Connerly remove when done, what product to set?

        cartAfterDelete = new CartResponseDTO();
        cartAfterDelete.setApprovalState(UUID.randomUUID());
        cartAfterDelete.setApproverId(UUID.randomUUID());
        cartAfterDelete.setCreditCard(creditCardDTO);
        cartAfterDelete.setId(UUID.randomUUID());
        cartAfterDelete.setOwnerId(UUID.randomUUID());
        cartAfterDelete.setPaymentMethodType(PaymentMethodTypeEnum.BILLTOACCOUNT.toString());
        cartAfterDelete.setPoNumber(PO_NUMBER);
        cartAfterDelete.setPricingBranchId(PRICING_BRANCH_ID);
        cartAfterDelete.setRejectionReason(REJECTION_REASON);
        cartAfterDelete.setShippingBranchId(SHIPPING_BRANCH_ID);
        cartAfterDelete.setShippingHandling(SHIPPING_AND_HANDLING);
        cartAfterDelete.setSubtotal(SUBTOTAL);
        cartAfterDelete.setTax(TAX);
        cartAfterDelete.setTotal(TOTAL);
        cartAfterDelete.setErpSystemName(ERPSystem.ECLIPSE.name());

        cartAfterBranchUpdate = new CartResponseDTO();
        cartAfterBranchUpdate.setApprovalState(UUID.randomUUID());
        cartAfterBranchUpdate.setApproverId(UUID.randomUUID());
        cartAfterBranchUpdate.setCreditCard(creditCardDTO);
        cartAfterBranchUpdate.setId(UUID.randomUUID());
        cartAfterBranchUpdate.setOwnerId(UUID.randomUUID());
        cartAfterBranchUpdate.setPaymentMethodType(PaymentMethodTypeEnum.BILLTOACCOUNT.toString());
        cartAfterBranchUpdate.setPoNumber(PO_NUMBER);
        cartAfterBranchUpdate.setPricingBranchId(PRICING_BRANCH_ID);
        cartAfterBranchUpdate.setRejectionReason(REJECTION_REASON);
        cartAfterBranchUpdate.setShippingBranchId(SHIPPING_BRANCH_ID_AFTER_BRANCH_UPDATE);
        cartAfterBranchUpdate.setShippingHandling(SHIPPING_AND_HANDLING);
        cartAfterBranchUpdate.setSubtotal(SUBTOTAL);
        cartAfterBranchUpdate.setTax(TAX);
        cartAfterBranchUpdate.setTotal(TOTAL);
        cartAfterBranchUpdate.setProducts(new ArrayList<>());
        cartAfterBranchUpdate.setErpSystemName(ERPSystem.ECLIPSE.name());

        when(cartService.updateCart(eq(CART_ID), any())).thenReturn(cart);

        when(cartService.getCart(eq(CART_ID), eq(Boolean.FALSE), any())).thenReturn(cart);
        when(cartService.getCart(eq(CART_ID), eq(true), any())).thenReturn(cartWithProducts);
        when(
            cartService.getCart(
                eq(USER_ID),
                eq(ACCOUNT_ID),
                eq(Boolean.FALSE),
                eq(
                    new ErpUserInformation(
                        ERP_USER_ID.toString(),
                        ERP_PASSWORD,
                        ACCOUNT_ID.toString(),
                        null,
                        ERP_SYSTEM.toString(),
                        null
                    )
                )
            )
        )
            .thenReturn(cart);
        when(
            cartService.getCart(
                eq(USER_ID),
                eq(ACCOUNT_ID),
                eq(true),
                eq(
                    new ErpUserInformation(
                        ERP_USER_ID.toString(),
                        ERP_PASSWORD,
                        ACCOUNT_ID.toString(),
                        null,
                        ERP_SYSTEM.toString(),
                        null
                    )
                )
            )
        )
            .thenReturn(cartWithProducts);
        when(cartService.refreshCart(any(), any(), eq(Boolean.TRUE))).thenReturn(cartWithProducts);
        when(cartService.refreshCart(any(), any(), eq(Boolean.FALSE))).thenReturn(cart);

        GetOrderResponseDTO getOrderResponseDTO = new GetOrderResponseDTO();
        when(
            ordersService.getOrderById(
                eq(ACCOUNT_ID.toString()),
                eq(QUOTE_ID),
                eq("1"),
                any(ErpUserInformation.class),
                isNull(),
                eq(BRANCH_ID)
            )
        )
            .thenReturn(getOrderResponseDTO);
        when(
            cartService.getCartFromQuote(
                eq(getOrderResponseDTO),
                any(ErpUserInformation.class),
                eq(ACCOUNT_ID),
                eq(BRANCH_ID)
            )
        )
            .thenReturn(cartWithProducts);

        itemsInfoDTO = new ItemsInfoDTO();
        itemsInfoDTO.setItemInfoList(
            List.of(new ItemInfoDTO(PRODUCT_ID, QUANTITY, INC_QUANTITY, QUANTITY_AVAILABLE, UOM, PRICE_PER_UNIT))
        );
        itemsInfoDTO.setErpUserInformation(erpUserInformation);

        when(cartService.deleteItem(eq(CART_ID), eq(ITEM_ID), any())).thenReturn(cartAfterDelete);

        when(cartService.updateItemQuantity(CART_ID, ITEM_ID, itemQuantityErpInformation))
            .thenReturn(updatedItemQtyResponse);
        when(cartService.updateItemQuantity(CART_ID, ITEM_ID_NOT_FOUND, itemQuantityErpInformation))
            .thenThrow(ItemNotFoundException.class);
        when(cartService.updateItemQuantity(CART_ID_INVALID_QTY, ITEM_ID, itemQuantityErpInformation))
            .thenThrow(QtyIncrementInvalidException.class);
        when(cartService.deleteItem(eq(CART_ID), eq(ITEM_ID_NOT_FOUND), any())).thenThrow(ItemNotFoundException.class);

        doThrow(QtyIncrementInvalidException.class)
            .when(cartService)
            .addItems(
                eq(CART_ID_INVALID_QTY),
                any(ItemsInfoDTO.class),
                any(ErpUserInformation.class),
                eq(Boolean.FALSE)
            );
        doThrow(CartNotFoundException.class)
            .when(cartService)
            .addItems(eq(CART_ID_NOT_FOUND), any(ItemsInfoDTO.class), any(ErpUserInformation.class), eq(Boolean.FALSE));

        willCallDto = new WillCallDTO();
        willCallDto.setBranchId(SHIPPING_BRANCH_ID);

        deliveryDto = new DeliveryDTO();
        deliveryDto.setPreferredTime(PreferredTimeEnum.ASAP);

        cartAfterWillCallCreate = new Cart();
        cartAfterWillCallCreate.setId(UUID.randomUUID());
        when(cartService.createWillCall(CART_ID, willCallDto)).thenReturn(cartAfterWillCallCreate);
        when(cartService.createWillCall(CART_ID_NOT_FOUND, willCallDto)).thenThrow(CartNotFoundException.class);

        cartAfterDeliveryCreate = new Cart();
        cartAfterDeliveryCreate.setId(UUID.randomUUID());
        when(cartService.createDelivery(CART_ID, deliveryDto)).thenReturn(cartAfterDeliveryCreate);
        when(cartService.createDelivery(CART_ID_NOT_FOUND, deliveryDto)).thenThrow(CartNotFoundException.class);

        willCall = new WillCall();
        willCall.setBranchId(SHIPPING_BRANCH_ID);

        cartAfterWillCallUpdate = new Cart();
        cartAfterWillCallUpdate.setId(UUID.randomUUID());
        when(cartService.updateWillCall(CART_ID, willCallDto)).thenReturn(cartAfterWillCallUpdate);
        when(cartService.updateWillCall(CART_ID_NOT_FOUND, willCallDto)).thenThrow(CartNotFoundException.class);
        when(cartService.updateWillCall(CART_ID_NO_WILL_CALL, willCallDto)).thenThrow(WillCallNotFoundException.class);

        delivery = new Delivery();
        delivery.setPreferredTime(PreferredTimeEnum.ASAP);

        cartAfterDeliveryUpdate = new Cart();
        cartAfterDeliveryUpdate.setId(UUID.randomUUID());
        when(cartService.updateDelivery(CART_ID, deliveryDto)).thenReturn(cartAfterDeliveryUpdate);
        when(cartService.updateDelivery(CART_ID_NOT_FOUND, deliveryDto)).thenThrow(CartNotFoundException.class);
        when(cartService.updateDelivery(CART_ID_NO_DELIVERY, deliveryDto)).thenThrow(DeliveryNotFoundException.class);

        deliveryOrderResponseDTO = new GetOrderResponseDTO();
        willCallOrderResponseDTO = new GetOrderResponseDTO();
        when(cartService.submitOrder(eq(CART_ID_DELIVERY), eq(submitOrderDTO), any()))
            .thenReturn(deliveryOrderResponseDTO);
        when(cartService.submitOrder(eq(CART_ID_WILL_CALL), eq(submitOrderDTO), any()))
            .thenReturn(willCallOrderResponseDTO);

        when(cartService.submitOrder(eq(CART_ID_NOT_FOUND), eq(submitOrderDTO), any()))
            .thenThrow(CartNotFoundException.class);
        when(cartService.submitOrder(eq(CART_ID_NO_WILL_CALL_OR_DELIVERY), eq(submitOrderDTO), any()))
            .thenThrow(DeliveryAndWillCallNotFoundException.class);
        when(cartService.submitOrder(eq(CART_ID_BRANCH_NOT_FOUND), eq(submitOrderDTO), any()))
            .thenThrow(BranchNotFoundPricingAndAvailabilityException.class);

        when(cartService.submitOrderPreview(eq(CART_ID_DELIVERY), eq(submitOrderDTO), any()))
            .thenReturn(deliveryOrderResponseDTO);
        when(cartService.submitOrderPreview(eq(CART_ID_WILL_CALL), eq(submitOrderDTO), any()))
            .thenReturn(willCallOrderResponseDTO);

        when(cartService.submitOrderPreview(eq(CART_ID_NOT_FOUND), eq(submitOrderDTO), any()))
            .thenThrow(CartNotFoundException.class);
        when(cartService.submitOrderPreview(eq(CART_ID_NO_WILL_CALL_OR_DELIVERY), eq(submitOrderDTO), any()))
            .thenThrow(DeliveryAndWillCallNotFoundException.class);

        when(
            cartService.updateWillCallBranch(
                eq(CART_ID),
                eq(SHIPPING_BRANCH_ID_AFTER_BRANCH_UPDATE),
                any(ErpUserInformation.class),
                any()
            )
        )
            .thenReturn(cartAfterBranchUpdate);
    }

    @Test
    void createCart_success() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(userAccountInfo);
        when(cartService.createCart(any(), any())).thenReturn(CART_ID);

        MvcResult resultActions = mockMvc
            .perform(
                post("/cart")
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
        assertEquals(
            UUID.fromString(resultActions.getResponse().getContentAsString().replace("\"", "")),
            CART_ID,
            "Expected mocked cart id to return from controller layer"
        );
    }

    @Test
    void createCart_cartAlreadyExists() throws Exception {
        UserAccountInfo userAccountInfo = new UserAccountInfo();
        userAccountInfo.setShipToAccountId(ACCOUNT_ID_CART_EXISTS);
        userAccountInfo.setUserId(USER_ID_CART_EXISTS);

        when(cartService.createCart(userAccountInfo, AUTH_TOKEN)).thenThrow(CartAlreadyExistsException.class);

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(userAccountInfo);
        mockMvc
            .perform(
                post("/cart")
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andDo(print())
            .andExpect(status().isConflict())
            .andReturn();
    }

    @Test
    void updateCart() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(cart);
        MvcResult resultActions = mockMvc
            .perform(
                patch("/cart/{cartId}", CART_ID)
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
        assertDoesNotThrow(() ->
            objectMapper.readValue(resultActions.getResponse().getContentAsString(), CartResponseDTO.class)
        );
        CartResponseDTO returnedCart = objectMapper.readValue(
            resultActions.getResponse().getContentAsString(),
            CartResponseDTO.class
        );
        assertEquals(returnedCart.getId(), cart.getId(), "Expected mocked out cart to return from controller layer");
    }

    @Test
    void updateWillCallBranch() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(erpUserInformation);
        MvcResult resultActions = mockMvc
            .perform(
                patch("/cart/{cartId}/will-calls/branch/{branchId}", CART_ID, SHIPPING_BRANCH_ID_AFTER_BRANCH_UPDATE)
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
        assertDoesNotThrow(() ->
            objectMapper.readValue(resultActions.getResponse().getContentAsString(), CartResponseDTO.class)
        );
        CartResponseDTO returnedCart = objectMapper.readValue(
            resultActions.getResponse().getContentAsString(),
            CartResponseDTO.class
        );
        assertEquals(
            returnedCart.getId(),
            cartAfterBranchUpdate.getId(),
            "Expected mocked out cart to return from controller layer"
        );
        assertEquals(
            returnedCart.getShippingBranchId(),
            cartAfterBranchUpdate.getShippingBranchId(),
            "Expected mocked out cart with updated shipping branch id to return from controller layer"
        );
        assertNotNull(
            cartAfterBranchUpdate.getProducts(),
            "Expected mocked out cart with products to return from controller layer"
        );
    }

    @Test
    void getCart_cartIdNoProducts() throws Exception {
        MvcResult resultActions = mockMvc
            .perform(
                get("/cart/{cartId}", CART_ID)
                    .header("Authorization", AUTH_TOKEN)
                    .param("erpAccountId", ACCOUNT_ID.toString())
                    .param("erpPassword", ERP_PASSWORD)
                    .param("erpSystemName", ERP_SYSTEM.toString())
                    .param("erpUserId", ERP_USER_ID.toString())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
        CartResponseDTO returnedCart = new ObjectMapper()
            .readValue(resultActions.getResponse().getContentAsString(), CartResponseDTO.class);
        assertEquals(returnedCart, cart, "Expected mocked out cart without products to return from controller layer");
    }

    @Test
    void getCart_cartIdWithProducts() throws Exception {
        MvcResult resultActions = mockMvc
            .perform(
                get("/cart/{cartId}", CART_ID)
                    .header("Authorization", AUTH_TOKEN)
                    .param("erpAccountId", ACCOUNT_ID.toString())
                    .param("erpPassword", ERP_PASSWORD)
                    .param("erpSystemName", ERP_SYSTEM.toString())
                    .param("erpUserId", ERP_USER_ID.toString())
                    .param("includeProducts", "true")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
        CartResponseDTO returnedCart = new ObjectMapper()
            .readValue(resultActions.getResponse().getContentAsString(), CartResponseDTO.class);
        assertEquals(
            returnedCart,
            cartWithProducts,
            "Expected mocked out cart with products to return from controller layer"
        );
    }

    @Test
    void getCart_cartIdNotFound() throws Exception {
        mockMvc
            .perform(
                get("/cart/{cartId}", CART_ID_NOT_FOUND)
                    .header("Authorization", AUTH_TOKEN)
                    .param("erpAccountId", ACCOUNT_ID.toString())
                    .param("erpPassword", ERP_PASSWORD)
                    .param("erpSystemName", ERP_SYSTEM.toString())
                    .param("erpUserId", ERP_USER_ID.toString())
            )
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    void getCart_userIdAccountIdNoProducts() throws Exception {
        MvcResult resultActions = mockMvc
            .perform(
                get("/cart/")
                    .header("Authorization", AUTH_TOKEN)
                    .param("erpAccountId", ACCOUNT_ID.toString())
                    .param("erpPassword", ERP_PASSWORD)
                    .param("erpSystemName", ERP_SYSTEM.toString())
                    .param("erpUserId", ERP_USER_ID.toString())
                    .param("accountId", ACCOUNT_ID.toString())
                    .param("userId", USER_ID.toString())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
        CartResponseDTO returnedCart = new ObjectMapper()
            .readValue(resultActions.getResponse().getContentAsString(), CartResponseDTO.class);
        assertEquals(returnedCart, cart, "Expected mocked out cart without products to return from controller layer");
    }

    @Test
    void getCart_userIdAccountIdWithProducts() throws Exception {
        MvcResult resultActions = mockMvc
            .perform(
                get("/cart/")
                    .header("Authorization", AUTH_TOKEN)
                    .param("accountId", ACCOUNT_ID.toString())
                    .param("userId", USER_ID.toString())
                    .param("includeProducts", "true")
                    .param("erpAccountId", ACCOUNT_ID.toString())
                    .param("erpPassword", ERP_PASSWORD)
                    .param("erpSystemName", ERP_SYSTEM.toString())
                    .param("erpUserId", ERP_USER_ID.toString())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
        CartResponseDTO returnedCart = new ObjectMapper()
            .readValue(resultActions.getResponse().getContentAsString(), CartResponseDTO.class);
        assertEquals(
            returnedCart,
            cartWithProducts,
            "Expected mocked out cart with products to return from controller layer"
        );
    }

    @Test
    void getCart_userIdAccountIdWithProducts_quote() throws Exception {
        MvcResult resultActions = mockMvc
            .perform(
                get("/cart/")
                    .header("Authorization", AUTH_TOKEN)
                    .param("accountId", ACCOUNT_ID.toString())
                    .param("userId", USER_ID.toString())
                    .param("includeProducts", "true")
                    .param("erpAccountId", ACCOUNT_ID.toString())
                    .param("erpPassword", ERP_PASSWORD)
                    .param("erpSystemName", ERP_SYSTEM.toString())
                    .param("erpUserId", ERP_USER_ID.toString())
                    .param("branchId", BRANCH_ID)
                    .param("quoteId", QUOTE_ID)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
        CartResponseDTO returnedCart = new ObjectMapper()
            .readValue(resultActions.getResponse().getContentAsString(), CartResponseDTO.class);
        assertEquals(
            returnedCart,
            cartWithProducts,
            "Expected mocked out cart with products to return from controller layer"
        );
    }

    @Test
    void addItems_success() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(itemsInfoDTO);
        mockMvc
            .perform(
                patch("/cart/{cartId}/items", CART_ID)
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    void addItem_cartNotFound() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(itemsInfoDTO);
        mockMvc
            .perform(
                patch("/cart/{cartId}/items", CART_ID_NOT_FOUND)
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    void addAllListItemsToCart_success() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ItemsInfoDTO itemsInfoDTO = new ItemsInfoDTO();
        itemsInfoDTO.setErpUserInformation(erpUserInformation);
        String itemsInfo = objectMapper.writeValueAsString(itemsInfoDTO);
        mockMvc
            .perform(
                patch("/cart/{cartId}/addAll/{listId}", CART_ID, LIST_ID)
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(itemsInfo)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    void updateItemQuantity_success() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(itemQuantityErpInformation);
        MvcResult resultActions = mockMvc
            .perform(
                patch("/cart/{cartId}/items/{itemId}", CART_ID, ITEM_ID)
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
        UpdateItemQtyResponseDTO returnedItemQtyResponse = new ObjectMapper()
            .readValue(resultActions.getResponse().getContentAsString(), UpdateItemQtyResponseDTO.class);
        assertEquals(
            returnedItemQtyResponse,
            updatedItemQtyResponse,
            "Expected mocked out UpdateItemQuantityResponseDTO to return from controller layer"
        );
    }

    @Test
    void updateItemQuantity_itemNotFound() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(itemQuantityErpInformation);
        mockMvc
            .perform(
                patch("/cart/{cartId}/items/{itemId}", CART_ID, ITEM_ID_NOT_FOUND)
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    void deleteItem_success() throws Exception {
        MvcResult resultActions = mockMvc
            .perform(
                delete("/cart/{cartId}/items/{itemId}", CART_ID, ITEM_ID)
                    .header("Authorization", AUTH_TOKEN)
                    .param("erpAccountId", ACCOUNT_ID.toString())
                    .param("erpPassword", ERP_PASSWORD)
                    .param("erpSystemName", ERP_SYSTEM.toString())
                    .param("erpUserId", ERP_USER_ID.toString())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
        CartResponseDTO returnedCart = new ObjectMapper()
            .readValue(resultActions.getResponse().getContentAsString(), CartResponseDTO.class);
        assertEquals(returnedCart, cartAfterDelete, "Expected mocked out cart to return from controller layer");
    }

    @Test
    void deleteItem_itemNotFound() throws Exception {
        mockMvc
            .perform(
                delete("/cart/{cartId}/items/{itemId}", CART_ID, ITEM_ID_NOT_FOUND)
                    .header("Authorization", AUTH_TOKEN)
                    .param("erpAccountId", ACCOUNT_ID.toString())
                    .param("erpPassword", ERP_PASSWORD)
                    .param("erpSystemName", ERP_SYSTEM.toString())
                    .param("erpUserId", ERP_USER_ID.toString())
            )
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    void createWillCall_success() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(willCallDto);
        MvcResult resultActions = mockMvc
            .perform(
                post("/cart/{cartId}/will-calls", CART_ID)
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
        Cart returnedCart = new ObjectMapper().readValue(resultActions.getResponse().getContentAsString(), Cart.class);
        assertEquals(
            returnedCart.getId(),
            cartAfterWillCallCreate.getId(),
            "Expected mocked out cart to return from controller layer"
        );
    }

    @Test
    void createWillCall_cartNotFound() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(willCallDto);
        mockMvc
            .perform(
                post("/cart/{cartId}/will-calls", CART_ID_NOT_FOUND)
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    void createDelivery_success() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(deliveryDto);
        MvcResult resultActions = mockMvc
            .perform(
                post("/cart/{cartId}/deliveries", CART_ID)
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
        Cart returnedCart = new ObjectMapper().readValue(resultActions.getResponse().getContentAsString(), Cart.class);
        assertEquals(
            returnedCart.getId(),
            cartAfterDeliveryCreate.getId(),
            "Expected mocked out cart to return from controller layer"
        );
    }

    @Test
    void createDelivery_cartNotFound() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(deliveryDto);
        mockMvc
            .perform(
                post("/cart/{cartId}/deliveries", CART_ID_NOT_FOUND)
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    void updateWillCall_success() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(willCall);
        MvcResult resultActions = mockMvc
            .perform(
                patch("/cart/{cartId}/will-calls", CART_ID)
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
        Cart returnedCart = new ObjectMapper().readValue(resultActions.getResponse().getContentAsString(), Cart.class);
        assertEquals(
            returnedCart.getId(),
            cartAfterWillCallUpdate.getId(),
            "Expected mocked out cart to return from controller layer"
        );
    }

    @Test
    void updateWillCall_cartNotFound() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(willCall);
        mockMvc
            .perform(
                patch("/cart/{cartId}/will-calls", CART_ID_NOT_FOUND)
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    void updateWillCall_willCallNotFound() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(willCall);
        mockMvc
            .perform(
                patch("/cart/{cartId}/will-calls", CART_ID_NO_WILL_CALL)
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    void updateDelivery_success() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(delivery);
        MvcResult resultActions = mockMvc
            .perform(
                patch("/cart/{cartId}/deliveries", CART_ID)
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
        Cart returnedCart = new ObjectMapper().readValue(resultActions.getResponse().getContentAsString(), Cart.class);
        assertEquals(
            returnedCart.getId(),
            cartAfterDeliveryUpdate.getId(),
            "Expected mocked out cart to return from controller layer"
        );
    }

    @Test
    void updateDelivery_cartNotFound() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(delivery);
        mockMvc
            .perform(
                patch("/cart/{cartId}/deliveries", CART_ID_NOT_FOUND)
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    void updateDelivery_deliveryNotFound() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(delivery);
        mockMvc
            .perform(
                patch("/cart/{cartId}/deliveries", CART_ID_NO_DELIVERY)
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    void submitOrder_success_delivery() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(submitOrderDTO);
        MvcResult resultActions = mockMvc
            .perform(
                post("/cart/{cartId}/order", CART_ID_DELIVERY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
                    .header("authorization", AUTH_TOKEN)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
        GetOrderResponseDTO getOrderResponseDTO = new ObjectMapper()
            .readValue(resultActions.getResponse().getContentAsString(), GetOrderResponseDTO.class);
        assertEquals(
            getOrderResponseDTO,
            deliveryOrderResponseDTO,
            "Expected mocked out order response to equal expected value"
        );
    }

    @Test
    void submitOrder_success_will_call() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(submitOrderDTO);
        MvcResult resultActions = mockMvc
            .perform(
                post("/cart/{cartId}/order", CART_ID_WILL_CALL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
                    .header("authorization", AUTH_TOKEN)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
        GetOrderResponseDTO getOrderResponseDTO = new ObjectMapper()
            .readValue(resultActions.getResponse().getContentAsString(), GetOrderResponseDTO.class);
        assertEquals(
            getOrderResponseDTO,
            willCallOrderResponseDTO,
            "Expected mocked out order response to equal expected value"
        );
    }

    @Test
    void submitOrder_deliveryAndWillCallNotFound() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(submitOrderDTO);
        mockMvc
            .perform(
                post("/cart/{cartId}/order", CART_ID_NO_WILL_CALL_OR_DELIVERY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
                    .header("authorization", AUTH_TOKEN)
            )
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    void submitOrder_submitOrderForApprovalSuccess() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(submitOrderDTO);
        mockMvc
            .perform(
                post("/cart/{cartId}/order", CART_ID_NO_WILL_CALL_OR_DELIVERY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
                    .header("authorization", AUTH_TOKEN_CANT_APPROVE_ORDER)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
        verify(cartService, times(1)).submitOrderForApproval(any(), any(), any());
    }

    @Test
    void submitOrder_cartNotFound() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(submitOrderDTO);
        mockMvc
            .perform(
                post("/cart/{cartId}/order", CART_ID_NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
                    .header("authorization", AUTH_TOKEN)
            )
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    void submitOrder_branchNotFound() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(submitOrderDTO);
        mockMvc
            .perform(
                post("/cart/{cartId}/order", CART_ID_BRANCH_NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
                    .header("authorization", AUTH_TOKEN)
            )
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    void submitOrderPreview_success_delivery() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(submitOrderDTO);
        MvcResult resultActions = mockMvc
            .perform(
                post("/cart/{cartId}/order/preview", CART_ID_DELIVERY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
                    .header("authorization", AUTH_TOKEN)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
        GetOrderResponseDTO getOrderResponseDTO = new ObjectMapper()
            .readValue(resultActions.getResponse().getContentAsString(), GetOrderResponseDTO.class);
        assertEquals(
            getOrderResponseDTO,
            deliveryOrderResponseDTO,
            "Expected mocked out order response to equal expected value"
        );
    }

    @Test
    void submitOrderPreview_success_will_call() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(submitOrderDTO);
        MvcResult resultActions = mockMvc
            .perform(
                post("/cart/{cartId}/order/preview", CART_ID_WILL_CALL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
                    .header("authorization", AUTH_TOKEN)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
        GetOrderResponseDTO getOrderResponseDTO = new ObjectMapper()
            .readValue(resultActions.getResponse().getContentAsString(), GetOrderResponseDTO.class);
        assertEquals(
            getOrderResponseDTO,
            willCallOrderResponseDTO,
            "Expected mocked out order response to equal expected value"
        );
    }

    @Test
    void submitOrderPreview_deliveryAndWillCallNotFound() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(submitOrderDTO);
        mockMvc
            .perform(
                post("/cart/{cartId}/order/preview", CART_ID_NO_WILL_CALL_OR_DELIVERY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
                    .header("authorization", AUTH_TOKEN)
            )
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    void submitOrderPreview_cartNotFound() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(submitOrderDTO);
        mockMvc
            .perform(
                post("/cart/{cartId}/order/preview", CART_ID_NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
                    .header("authorization", AUTH_TOKEN)
            )
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    void addItem_qtyIncrementInvalid() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(itemsInfoDTO);
        mockMvc
            .perform(
                patch("/cart/{cartId}/items", CART_ID_INVALID_QTY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    void updateItemQuantity_qtyIncrementInvalid() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(itemQuantityErpInformation);
        mockMvc
            .perform(
                patch("/cart/{cartId}/items/{itemId}", CART_ID_INVALID_QTY, ITEM_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    void getOrdersPendingApproval_success() throws Exception {
        when(authorizationService.userCanApproveOrder(any())).thenReturn(true);
        mockMvc
            .perform(
                get("/cart/orders-pending-approval")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("authorization", AUTH_TOKEN)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    void getOrdersPendingApproval_unauthorizedUserException() throws Exception {
        when(authorizationService.userCanApproveOrder(any())).thenReturn(false);
        org.assertj.core.api.Assertions
            .assertThatThrownBy(() ->
                mockMvc
                    .perform(
                        get("/cart/orders-pending-approval")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("authorization", AUTH_TOKEN_CANT_APPROVE_ORDER)
                    )
                    .andExpect(status().isOk())
            )
            .hasCause(new UserUnauthorizedException().getCause());
    }

    @Test
    void approveOrder_Success() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(erpUserInformation);
        when(cartService.approveOrder(any(), any(), any())).thenReturn(new GetOrderResponseDTO());
        when(authorizationService.userCanApproveOrder(any())).thenReturn(true);
        mockMvc
            .perform(
                post("/cart/{cartId}/approve", CART_ID_NO_WILL_CALL_OR_DELIVERY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
                    .header("authorization", AUTH_TOKEN)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    void approveOrder_Unauthorized() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(erpUserInformation);
        when(authorizationService.userCanApproveOrder(any())).thenReturn(false);
        mockMvc
            .perform(
                post("/cart/{cartId}/approve", CART_ID_NO_WILL_CALL_OR_DELIVERY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
                    .header("authorization", AUTH_TOKEN)
            )
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    void rejectOrder_Success() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(erpUserInformation);
        when(cartService.rejectOrder(any(), any(), any())).thenReturn("");
        when(authorizationService.userCanApproveOrder(any())).thenReturn(true);
        mockMvc
            .perform(
                post("/cart/{cartId}/reject", CART_ID_NO_WILL_CALL_OR_DELIVERY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
                    .header("authorization", AUTH_TOKEN)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    void getOrderPendingApproval_success() throws Exception {
        when(authorizationService.userCanApproveOrder(any())).thenReturn(true);
        mockMvc
            .perform(
                get("/cart/orders-pending-approval/{orderId}", AWAITING_APPROVAL_ORDER_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("authorization", AUTH_TOKEN)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    void rejectOrder_Unauthorized() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(erpUserInformation);
        when(authorizationService.userCanApproveOrder(any())).thenReturn(false);
        mockMvc
            .perform(
                post("/cart/{cartId}/reject", CART_ID_NO_WILL_CALL_OR_DELIVERY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
                    .header("authorization", AUTH_TOKEN)
            )
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    void getOrderPendingApproval_unauthorizedUserException() throws Exception {
        when(authorizationService.userCanApproveOrder(any())).thenReturn(false);
        org.assertj.core.api.Assertions
            .assertThatThrownBy(() ->
                mockMvc
                    .perform(
                        get("/cart/orders-pending-approval/{orderId}", AWAITING_APPROVAL_ORDER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("authorization", AUTH_TOKEN_CANT_APPROVE_ORDER)
                    )
                    .andExpect(status().isOk())
            )
            .hasCause(new UserUnauthorizedException().getCause());
    }

    @Test
    void deleteCart_happyPath() throws Exception {
        val request = new DeleteCartsRequestDTO(UUID.randomUUID());
        String content = TestUtils.jsonStringify(request);
        when(cartService.deleteCartsByShipToAccountId(any())).thenReturn(new CartDeleteResponseDTO(1, 5, true));
        mockMvc
            .perform(
                delete("/cart", USER_ID)
                    .header("authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    void refreshCart_success() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(erpUserInformation);
        MvcResult result =
            this.mockMvc.perform(
                    put("/cart/{cartId}/refresh", CART_ID)
                        .header("Authorization", AUTH_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        CartResponseDTO returnedCart = new ObjectMapper()
            .readValue(result.getResponse().getContentAsString(), CartResponseDTO.class);
        assertEquals(returnedCart, cart, "Expected mocked cart to return on refresh cart call from controller layer");
    }

    @Test
    void refreshCart_cartIdNoProducts() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(erpUserInformation);
        MvcResult result =
            this.mockMvc.perform(
                    put("/cart/{cartId}/refresh", CART_ID)
                        .header("Authorization", AUTH_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        CartResponseDTO returnedCart = new ObjectMapper()
            .readValue(result.getResponse().getContentAsString(), CartResponseDTO.class);
        assertEquals(returnedCart, cart, "Expected mocked cart to return on refresh cart call from controller layer");
    }

    @Test
    void refreshCart_cartIdNotFound() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(erpUserInformation);
        when(cartService.refreshCart(eq(CART_ID_NOT_FOUND), any(ErpUserInformation.class), anyBoolean()))
            .thenThrow(CartNotFoundException.class);
        mockMvc
            .perform(
                put("/cart/{cartId}/refresh", CART_ID_NOT_FOUND)
                    .header("Authorization", AUTH_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
            )
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn();
    }
}
