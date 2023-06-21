package com.reece.platform.notifications.model.DataExtensions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.platform.notifications.model.DTO.ProductDTO;
import com.reece.platform.notifications.model.DTO.SubmitOrderDTO;
import com.reece.platform.notifications.model.WebStatusesEnum;
import com.reece.platform.notifications.service.SalesforceMarketingCloudService;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderInfoDE extends BaseDataExtension {

    public static final int MAX_PRODUCT_ROWS_SALESFORCE = 15;

    public OrderInfoDE(SubmitOrderDTO submitOrderDTO, String email) throws JsonProcessingException {
        super.setTo(email);
        HashMap<String, String> attr = new HashMap<>();
        attr.put("FirstName", submitOrderDTO.getName());
        attr.put("EmailAddress", email);
        attr.put("DateCreated", submitOrderDTO.getOrderDate());
        attr.put("Domain", submitOrderDTO.getDomain());
        attr.put("Brand", submitOrderDTO.getBrand());
        if (!StringUtils.isEmpty(submitOrderDTO.getOrderNumber())) {
            attr.put("OrderNumber", submitOrderDTO.getOrderNumber());
        }
        String shippinpMethod= submitOrderDTO.getShippingMethod()!=null? submitOrderDTO.getShippingMethod():"";
        attr.put("ShippingMethod", shippinpMethod.replaceAll("_", ""));
        String erpSystemName=submitOrderDTO.getErpSystemName();

        if (erpSystemName!=null && erpSystemName.equals("MINCRON")) {
            attr.put("ContractName", submitOrderDTO.getContractName());
            attr.put("ContractNumber", submitOrderDTO.getContractNumber());
        }
        attr.put("PendingApprovalOrderNumber", submitOrderDTO.getPendingApprovalOrderNumber());
        attr.put("OrderDate", submitOrderDTO.getOrderDate());
        ObjectMapper objectMapper = new ObjectMapper();
        attr.put("Products", objectMapper.writeValueAsString(submitOrderDTO.getProductDTOs().stream().limit(MAX_PRODUCT_ROWS_SALESFORCE).map(p->{
                p.setUnitPrice(SalesforceMarketingCloudService.checkPriceFormat(p.getUnitPrice()));
                p.setProductTotal(SalesforceMarketingCloudService.checkPriceFormat(p.getProductTotal()));
            return p;
        }).collect(Collectors.toList())));

        attr.put("RemainingProductCount", String.valueOf(submitOrderDTO.getProductDTOs().size() <= MAX_PRODUCT_ROWS_SALESFORCE ? 0 : submitOrderDTO.getProductDTOs().size() - MAX_PRODUCT_ROWS_SALESFORCE));
        attr.put("PO", submitOrderDTO.getPoNumber());
        attr.put("BranchName", submitOrderDTO.getBranchDTO().getBranchName());
        attr.put("BranchStreetAddress1", submitOrderDTO.getBranchDTO().getStreetLineOne());
        attr.put("BranchStreetAddress2", submitOrderDTO.getBranchDTO().getStreetLineTwo());
        attr.put("BranchCity", submitOrderDTO.getBranchDTO().getCity());
        attr.put("BranchState", submitOrderDTO.getBranchDTO().getState());
        attr.put("BranchZip", submitOrderDTO.getBranchDTO().getPostalCode());

        attr.put("Total", SalesforceMarketingCloudService.checkPriceFormat(submitOrderDTO.getTotal()));
        attr.put("SalesTax", SalesforceMarketingCloudService.checkPriceFormat(submitOrderDTO.getTax()));
        attr.put("Subtotal", SalesforceMarketingCloudService.checkPriceFormat(submitOrderDTO.getSubTotal()));

        attr.put("BranchPhone", submitOrderDTO.getBranchDTO().getBranchPhone());
        String branchHours = submitOrderDTO.getBranchDTO().getBranchHours()!=null? submitOrderDTO.getBranchDTO().getBranchHours():"";
        branchHours= branchHours.replace("M-F","Mon - Fri");
        attr.put("BranchHours", branchHours);

        if (!StringUtils.isEmpty(submitOrderDTO.getRejectionReason())) {
            attr.put("RejectionReason", submitOrderDTO.getRejectionReason());
        }

        if (!Objects.isNull(submitOrderDTO.getWebStatus())) {
            if (submitOrderDTO.getWebStatus().equals(WebStatusesEnum.READY_FOR_PICKUP.name())) {
                attr.put("OrderStatus", "Ready for pick up");
            } else if (submitOrderDTO.getWebStatus().equals(WebStatusesEnum.SHIPPED.name())) {
                attr.put("OrderStatus", "Shipped");
            } else if (submitOrderDTO.getWebStatus().equals(WebStatusesEnum.DELIVERED.name())) {
                attr.put("OrderStatus", "Delivered");
            }
            else if (submitOrderDTO.getWebStatus().equals(WebStatusesEnum.PENDING.name())) {
            attr.put("OrderStatus", "Pending");
           }
        }

        super.setAttr(attr);
    }
}
