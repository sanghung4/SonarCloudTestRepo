package com.reece.platform.products.model.DTO;

import com.reece.platform.products.model.entity.Cart;
import com.reece.platform.products.model.entity.OrdersPendingApproval;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;
import lombok.Data;

@Data
public class OrderPendingApprovalDTO {

    public OrderPendingApprovalDTO(Cart cart, OrdersPendingApproval orderPendingApproval, String submittedByName) {
        this.orderId = orderPendingApproval.getOrderId();
        this.purchaseOrderNumber = cart.getPoNumber();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        this.submissionDate = dateFormat.format(orderPendingApproval.getSubmissionDate());
        this.submittedByName = submittedByName;
        this.orderTotal = cart.getTotal();
        this.cartId = cart.getId();
    }

    private String orderId;
    private String purchaseOrderNumber;
    private String submissionDate;
    private String submittedByName;
    private int orderTotal;
    private UUID cartId;
}
