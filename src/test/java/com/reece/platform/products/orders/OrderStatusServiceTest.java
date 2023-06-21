package com.reece.platform.products.orders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

import com.reece.platform.products.external.eclipse.EclipseServiceClient;
import com.reece.platform.products.model.DTO.GetOrderResponseDTO;
import com.reece.platform.products.model.DTO.OrderLineItemResponseDTO;
import com.reece.platform.products.model.DTO.SalesOrderSubmitNotificationDTO;
import com.reece.platform.products.model.WebStatusesEnum;
import com.reece.platform.products.model.eclipse.common.EclipseAddressResponseDTO;
import com.reece.platform.products.model.entity.OrderStatus;
import com.reece.platform.products.model.repository.OrderStatusDAO;
import com.reece.platform.products.orders.model.WebStatus;
import com.reece.platform.products.service.CartService;
import com.reece.platform.products.service.NotificationService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OrderStatusServiceTest {

    private static final String ERP_ACCOUNT_ID = "123123";
    private static final String ORDER_NUMBER = "1234";
    private static final String ORDER_STATUS = "Shipped";
    private static final String PART_NUMBER = "Part Number";
    private static final String CITY = "City";
    private static final String STATE = "State";
    private static final String STREET = "Street";
    private static final String ZIP = "ZIP";

    private OrderStatusService orderStatusService;

    @Mock
    private EclipseServiceClient eclipseServiceClient;

    @Mock
    private OrderStatusDAO orderStatusDAO;

    @Mock
    private CartService cartService;

    @Mock
    private NotificationService notificationService;

    private GetOrderResponseDTO getOrderResponseDTO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        orderStatusService =
            new OrderStatusService(eclipseServiceClient, orderStatusDAO, cartService, notificationService);

        getOrderResponseDTO = new GetOrderResponseDTO();
        getOrderResponseDTO.setOrderNumber(ORDER_NUMBER);
        getOrderResponseDTO.setOrderStatus(ORDER_STATUS);

        var lineItem = new OrderLineItemResponseDTO();
        lineItem.setErpPartNumber(PART_NUMBER);
        val lineItems = List.of(lineItem);

        getOrderResponseDTO.setLineItems(lineItems);
        EclipseAddressResponseDTO eclipseAddressResponseDTO = new EclipseAddressResponseDTO();
        eclipseAddressResponseDTO.setStreetLineOne(STREET);
        eclipseAddressResponseDTO.setPostalCode(ZIP);
        eclipseAddressResponseDTO.setCity(CITY);
        eclipseAddressResponseDTO.setState(STATE);
        getOrderResponseDTO.setShipAddress(eclipseAddressResponseDTO);
    }

    @Test
    public void updateOrderStatus_Invoiced() throws Exception {
        OrderStatus mockDbOrderData = new OrderStatus();
        mockDbOrderData.setErpAccountId(ERP_ACCOUNT_ID);
        mockDbOrderData.setWebStatus(WebStatus.SUBMITTED);
        mockDbOrderData.setOrderId(ORDER_NUMBER);
        List<OrderStatus> mockDbOrderDataList = List.of(mockDbOrderData);

        getOrderResponseDTO.setWebStatus(WebStatusesEnum.INVOICED.name());

        when(orderStatusDAO.findAll()).thenReturn(mockDbOrderDataList);
        when(eclipseServiceClient.getOrder(ERP_ACCOUNT_ID, ORDER_NUMBER, null))
            .thenReturn(Optional.of(getOrderResponseDTO));
        when(cartService.buildOrderNotificationDTO(any(), any(), any()))
            .thenReturn(new SalesOrderSubmitNotificationDTO());

        orderStatusService.updateAllOrderStatuses();

        verify(notificationService, times(0)).sendOrderStatusUpdateEmail(any());
        verify(orderStatusDAO, times(1)).delete(any());
    }

    @Test
    public void updateOrderStatus_ReadyForPickup() throws Exception {
        OrderStatus mockDbOrderData = new OrderStatus();
        mockDbOrderData.setErpAccountId(ERP_ACCOUNT_ID);
        mockDbOrderData.setWebStatus(WebStatus.SUBMITTED);
        mockDbOrderData.setOrderId(ORDER_NUMBER);
        List<OrderStatus> mockDbOrderDataList = Arrays.asList(mockDbOrderData);

        getOrderResponseDTO.setWebStatus(WebStatusesEnum.READY_FOR_PICKUP.name());

        when(orderStatusDAO.findAll()).thenReturn(mockDbOrderDataList);
        doNothing().when(notificationService).sendOrderStatusUpdateEmail(any());
        when(eclipseServiceClient.getOrder(ERP_ACCOUNT_ID, ORDER_NUMBER, null))
            .thenReturn(Optional.of(getOrderResponseDTO));

        orderStatusService.updateAllOrderStatuses();
        verify(notificationService, times(1)).sendOrderStatusUpdateEmail(any());
    }

    @Test
    public void updateOrderStatus_Shipped() throws Exception {
        OrderStatus mockDbOrderData = new OrderStatus();
        mockDbOrderData.setErpAccountId(ERP_ACCOUNT_ID);
        mockDbOrderData.setWebStatus(WebStatus.SUBMITTED);
        mockDbOrderData.setOrderId(ORDER_NUMBER);
        List<OrderStatus> mockDbOrderDataList = Arrays.asList(mockDbOrderData);

        getOrderResponseDTO.setWebStatus(WebStatusesEnum.SHIPPED.name());

        when(orderStatusDAO.findAll()).thenReturn(mockDbOrderDataList);
        doNothing().when(notificationService).sendOrderStatusUpdateEmail(any());
        when(eclipseServiceClient.getOrder(ERP_ACCOUNT_ID, ORDER_NUMBER, null))
            .thenReturn(Optional.of(getOrderResponseDTO));

        orderStatusService.updateAllOrderStatuses();
        verify(notificationService, times(1)).sendOrderStatusUpdateEmail(any());
    }

    @Test
    public void updateOrderStatus_Delivered() throws Exception {
        OrderStatus mockDbOrderData = new OrderStatus();
        mockDbOrderData.setErpAccountId(ERP_ACCOUNT_ID);
        mockDbOrderData.setWebStatus(WebStatus.SUBMITTED);
        mockDbOrderData.setOrderId(ORDER_NUMBER);
        List<OrderStatus> mockDbOrderDataList = Arrays.asList(mockDbOrderData);

        getOrderResponseDTO.setWebStatus(WebStatusesEnum.DELIVERED.name());

        when(orderStatusDAO.findAll()).thenReturn(mockDbOrderDataList);
        doNothing().when(notificationService).sendOrderStatusUpdateEmail(any());
        when(eclipseServiceClient.getOrder(ERP_ACCOUNT_ID, ORDER_NUMBER, null))
            .thenReturn(Optional.of(getOrderResponseDTO));

        orderStatusService.updateAllOrderStatuses();
        verify(notificationService, times(1)).sendOrderStatusUpdateEmail(any());
    }
}
