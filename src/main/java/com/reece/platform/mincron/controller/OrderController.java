package com.reece.platform.mincron.controller;

import com.reece.platform.mincron.exceptions.MincronException;
import com.reece.platform.mincron.model.ErrorDTO;
import com.reece.platform.mincron.model.OrderDTO;
import com.reece.platform.mincron.model.OrderHeaderDTO;
import com.reece.platform.mincron.model.common.PageDTO;
import com.reece.platform.mincron.model.common.ProductLineItemDTO;
import com.reece.platform.mincron.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * @param accountId     bill-to account Id
     * @param orderType     type of order uses to filter results
     * @param orderStatus   status of the order used to filter results
     * @param startRow      first row to retrieve from list of contracts
     * @param maxRows       the maximum amount of rows to return from list of contracts
     * @param searchFilter  query string used to filter results
     * @param fromDate      if specified, contracts from this date on will be returned
     * @param toDate        if specified, contracts up to this date will be returned
     * @param sortOrder     used to define which data value to sort by
     * @param sortDirection used to specify whether results should be returned in ascending or descending order
     * @return list of contracts for given bill-to account Id
     * @throws MincronException exception thrown when there is any error in the mincron server
     */
    @GetMapping
    public @ResponseBody
    PageDTO<OrderDTO> getOrderList(
            @RequestParam String accountId,
            @RequestParam String orderType,
            @RequestParam String orderStatus,
            @RequestParam(defaultValue = "1") Integer startRow,
            @RequestParam(defaultValue = "10") Integer maxRows,
            @RequestParam(defaultValue = "") String searchFilter,
            @RequestParam(defaultValue = "") String fromDate,
            @RequestParam(defaultValue = "") String toDate,
            @RequestParam(defaultValue = "") String sortOrder,
            @RequestParam(defaultValue = "") String sortDirection
    ) throws MincronException {
        PageDTO<OrderDTO> orderPage = orderService.getOrderList(
                accountId,
                orderType,
                orderStatus,
                startRow,
                maxRows,
                searchFilter,
                fromDate,
                toDate,
                sortOrder,
                sortDirection
        );

        return orderPage;
    }

    /**
     * Retrieve order header for a given order number.
     *
     * @param orderNumber order number
     * @return order header
     * @throws MincronException if there is an internal server error when making API request
     */
    @GetMapping("/order-header")
    public @ResponseBody
    OrderHeaderDTO getOrderHeader(@RequestParam String orderType, @RequestParam String orderNumber) throws MincronException {
        OrderHeaderDTO orderHeader = orderService.getOrderHeader(orderType, orderNumber);
        return orderHeader;
    }

    /**
     * Returns a list of products for a given contract.
     *
     * @param accountId   account id
     * @param orderType   type of order uses to filter results
     * @param orderNumber order number
     * @param WCStdOrder  indicates whether this is a standard order or not (Y / N)
     * @param startRow    indicates which page to retrieve results from (if not provided the Websmart API will default to 1)
     * @param maxRows     Indicates how many rows to retrieve per page (Required by the Websmart API)
     * @return returns a list of products for a given order
     * @throws MincronException if there is an internal server error when making API request
     */
    @GetMapping("/order-item-list")
    public @ResponseBody
    List<ProductLineItemDTO> getOrderItemList(
            @RequestParam String accountId,
            @RequestParam String orderType,
            @RequestParam String orderNumber,
            @RequestParam String WCStdOrder,
            @RequestParam(defaultValue = "1") Integer startRow,
            @RequestParam(defaultValue = "10000") Integer maxRows
    ) throws MincronException {
        return orderService.getOrderItemList(accountId, orderType, orderNumber, WCStdOrder, startRow, maxRows);
    }

}
