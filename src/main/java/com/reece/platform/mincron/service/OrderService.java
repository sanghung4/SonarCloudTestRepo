package com.reece.platform.mincron.service;

import com.reece.platform.mincron.callBuilder.CallBuilderConfig;
import com.reece.platform.mincron.callBuilder.ManagedCallFactory;
import com.reece.platform.mincron.callBuilder.ResponseBuilderConfig;
import com.reece.platform.mincron.exceptions.MincronException;
import com.reece.platform.mincron.model.*;
import com.reece.platform.mincron.model.common.PageDTO;
import com.reece.platform.mincron.model.common.ProductLineItemDTO;
import com.reece.platform.mincron.model.enums.OrderStatusEnum;
import com.reece.platform.mincron.model.enums.ProgramCallNumberEnum;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.reece.platform.mincron.model.enums.ShipmentMethodEnum;
import com.reece.platform.mincron.utilities.MincronDataFormatting;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final ManagedCallFactory mcf;
    private static final int ORDER_CALLS_STARTING_ROW = 2;

    @Autowired
    public OrderService(ManagedCallFactory mcf) {
        this.mcf = mcf;
    }

    public PageDTO<OrderDTO> getOrderList(
        String accountId,
        String orderType,
        String orderStatus,
        Integer startRow,
        Integer maxRows,
        String searchFilter,
        String fromDate,
        String toDate,
        String sortOrder,
        String sortDirection
    ) throws MincronException {
        PageDTO<OrderDTO> orderPage = new PageDTO<>();
        orderPage.setStartRow(startRow);

        try (CallBuilderConfig cbc = mcf.makeManagedCall(ProgramCallNumberEnum.GET_ORDER_LIST.getProgramCallNumber(), 21, true)) {
            cbc.setInputString(accountId);
            cbc.setInputString("");
            cbc.setInputString(orderType.toUpperCase());
            cbc.setInputString(orderStatus);
            cbc.setInputInt(startRow);
            cbc.setInputInt(maxRows);
            cbc.setInputString(searchFilter);
            cbc.setInputString(fromDate);
            cbc.setInputString(toDate);
            cbc.setInputString(sortOrder);
            cbc.setInputString(sortDirection);
            cbc.setInputString("");

            cbc.setOutputChar();
            cbc.setOutputChar();
            cbc.setOutputDecimal();
            cbc.setOutputDecimal();

            ResponseBuilderConfig rb = cbc.getResultSet(ORDER_CALLS_STARTING_ROW);

            while (rb.hasMoreData()) {
                OrderDTO order = new OrderDTO();
                order.setOrderNumber(rb.getResultString());
                order.setInvoiceNumber(rb.getResultString());
                order.setStatus(OrderStatusEnum.normalizeOrderStatus(OrderStatusEnum.valueOf(rb.getResultString())));
                order.setOrderDate(rb.getResultString());
                order.setShipDate(rb.getResultString());
                order.setInvoiceDate(rb.getResultString());
                order.setTrackingNumber(rb.getResultString());
                order.setPurchaseOrderNumber(rb.getResultString());
                order.setJobNumber(rb.getResultString());
                order.setJobName(rb.getResultString());
                order.setOrderTotal(rb.getResultString());
                rb.getResultString();

                order.setContractNumber(rb.getResultString());
                String allZerosPattern = "^[0]+$";
                Pattern r = Pattern.compile(allZerosPattern);
                Matcher m = r.matcher(order.getContractNumber());
                if (m.find()) order.setContractNumber("");

                orderPage.results.add(order);
            }

            orderPage.setRowsReturned(orderPage.results.size());

            orderPage.setTotalRows(cbc.getNumberOfRows());
        } catch (SQLException e) {
            throw new MincronException(
                String.format("%s SQL State: %s", e.getMessage(), e.getSQLState()),
                HttpStatus.BAD_REQUEST
            );
        } catch (Exception e) {
            throw new MincronException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return orderPage;
    }

    public OrderHeaderDTO getOrderHeader(String orderType, String orderNumber) {
        OrderHeaderDTO orderHeaderDTO = new OrderHeaderDTO();

        try (CallBuilderConfig cbc = mcf.makeManagedCall("AIR7130", 78, true)) {
            // 2 input parameters
            cbc.setInputString(orderNumber);
            cbc.setInputString(orderType);

            // 2 error outputs
            cbc.setOutputChar(); // ERRCD
            cbc.setOutputChar(); // ERRMSG

            cbc.setOutputNumeric(0); // out CUSTOMERNUMBER NUMERIC(6)
            cbc.setOutputChar(); // out CUSTOMERNAME CHAR(30)
            cbc.setOutputChar(); // out INVOICENUMBER CHAR(7)
            cbc.setOutputChar(); // out RETORDERNUMBER CHAR(7)
            cbc.setOutputChar(); // out PROMISEDSHIPMO CHAR(2)
            cbc.setOutputChar(); // out PROMISEDSHIPDYTE CHAR(2)
            cbc.setOutputChar(); // out PROMISEDSHIPCC CHAR(2)
            cbc.setOutputChar(); // out PROMISEDSHIPYR CHAR(2)
            cbc.setOutputChar(); // out STATUS CHAR(1)
            cbc.setOutputChar(); // out ORDERDATEMO CHAR(2)
            cbc.setOutputChar(); // out ORDERDATEDY CHAR(2)
            cbc.setOutputChar(); // out ORDERDATECC CHAR(2),
            cbc.setOutputChar(); // out ORDERDATEYR CHAR(2)
            cbc.setOutputChar(); // out SHIPDATEMO CHAR(2)
            cbc.setOutputChar(); // out SHIPDATEDY CHAR(2),
            cbc.setOutputChar(); // out SHIPDATECC CHAR(2)
            cbc.setOutputChar(); // out SHIPDATEYR CHAR(2)
            cbc.setOutputChar(); // out RETORDERTYPE CHAR(1),
            cbc.setOutputChar(); // out ENTEREDBY CHAR(3)
            cbc.setOutputNumeric(0); // out BRANCHNUMBER NUMERIC(3)
            cbc.setOutputChar(); // out BRANCHNAME CHAR(25),
            cbc.setOutputChar(); // out ADDRESS1 CHAR(30)
            cbc.setOutputChar(); // out ADDRESS2 CHAR(30)
            cbc.setOutputChar(); // out ADDRESS3 CHAR(30)
            cbc.setOutputChar(); // out CITY CHAR(25),
            cbc.setOutputChar(); // out STATE CHAR(2)
            cbc.setOutputChar(); // out ZIP CHAR(10)
            cbc.setOutputChar(); // out COUNTRY CHAR(10)
            cbc.setOutputChar(); // out PAIDSTATUS CHAR(12),
            cbc.setOutputChar(); // out JOBNUMBER CHAR(7)
            cbc.setOutputChar(); // out JOBNAME CHAR(15)
            cbc.setOutputChar(); // out PURCHASEORDNUM CHAR(22),
            cbc.setOutputChar(); // out ORDEREDBY CHAR(30)
            cbc.setOutputChar(); // out SHIPMENTMETHOD CHAR(1)
            cbc.setOutputChar(); // out SHIPVIA CHAR(15),
            cbc.setOutputChar(); // out TRACKINGNUMBER CHAR(30)
            cbc.setOutputChar(); // out TRACKINGURL CHAR(80)
            cbc.setOutputChar(); // out SHIPTOADDRESS1 CHAR(30),
            cbc.setOutputChar(); // out SHIPTOADDRESS2 CHAR(30)
            cbc.setOutputChar(); // out SHIPTOADDRESS3 CHAR(30)
            cbc.setOutputChar(); // out SHIPTOCITY CHAR(25),
            cbc.setOutputChar(); // out SHIPTOSTATE CHAR(2)
            cbc.setOutputChar(); // out SHIPTOZIPCODE CHAR(10)
            cbc.setOutputChar(); // out SHIPTOCOUNTRY CHAR(10),
            cbc.setOutputChar(); // out PHONENUMBER CHAR(12)
            cbc.setOutputNumeric(2); // out SUBTOTAL NUMERIC(9, 2)
            cbc.setOutputNumeric(2); // out OTHCHARGES NUMERIC(9, 2),
            cbc.setOutputNumeric(2); // out TAXAMOUNT NUMERIC(9, 2)
            cbc.setOutputNumeric(2); // out OTHERTAXAMT NUMERIC(9, 2)
            cbc.setOutputNumeric(2); // out TOTALAMOUNT NUMERIC(9, 2),
            cbc.setOutputChar(); // out GSTHSTCODE CHAR(3))
            cbc.setOutputChar(); // out FCC CHAR(1)
            cbc.setOutputNumeric(2); // out SIBO NUMERIC(9, 2)
            cbc.setOutputChar(); // out ContractNumber CHAR(7)
            cbc.setOutputChar(); // out TERMSDESC CHAR(30)
            cbc.setOutputChar(); // out DUEDATEMO CHAR(2)
            cbc.setOutputChar(); // out DUEDATEDY CHAR(2)
            cbc.setOutputChar(); // out DUEDATECC CHAR(2)
            cbc.setOutputChar(); // out DUEDATEYR CHAR(2)
            cbc.setOutputChar(); // out INOVICEDATEMO CHAR(2)
            cbc.setOutputChar(); // out INOVICEDUEDATEDY CHAR(2)
            cbc.setOutputChar(); // out INOVICEDUEDATECC CHAR(2)
            cbc.setOutputChar(); // out INOVICEDUEDATEYR CHAR(2)
            cbc.setOutputChar(); // out INSTRUCTIONS1 CHAR(40)
            cbc.setOutputChar(); // out INSTRUCTIONS2 CHAR(40)
            cbc.setOutputChar(); // out INSTRUCTIONS3 CHAR(40)
            cbc.setOutputChar(); // out INSTRUCTIONS4 CHAR(40)
            cbc.setOutputChar(); // out INSTRUCTIONS5 CHAR(40)
            cbc.setOutputChar(); // out INSTRUCTIONS6 CHAR(40)

            ResponseBuilderConfig rb = cbc.getResultSet(ORDER_CALLS_STARTING_ROW);

            val cs = cbc.getCs();

            val customerNumber = cs.getInt(10); // out CUSTOMERNUMBER NUMERIC(6)
            val customerName = cs.getString(11); // out CUSTOMERNAME CHAR(30)
            val invoiceNumber = cs.getString(12); // out INVOICENUMBER CHAR(7)
            val retOrderNumber = cs.getString(13); // out RETORDERNUMBER CHAR(7)
            val promisedShipMo = cs.getString(14); // out PROMISEDSHIPMO CHAR(2)
            val promisedShipDyte = cs.getString(15); // out PROMISEDSHIPDYTE CHAR(2)
            val promisedShipCC = cs.getString(16); // out PROMISEDSHIPCC CHAR(2)
            val promisedShipYr = cs.getString(17); // out PROMISEDSHIPYR CHAR(2)
            val status = cs.getString(18); // out STATUS CHAR(1)
            val orderDateMo = cs.getString(19); // out ORDERDATEMO CHAR(2)
            val orderDateDy = cs.getString(20); // out ORDERDATEDY CHAR(2)
            val orderDateCC = cs.getString(21); // out ORDERDATECC CHAR(2),
            val orderDateYr = cs.getString(22); // out ORDERDATEYR CHAR(2)
            val shipDateMo = cs.getString(23); // out SHIPDATEMO CHAR(2)
            val shipDateDy = cs.getString(24); // out SHIPDATEDY CHAR(2),
            val shipDateCc = cs.getString(25); // out SHIPDATECC CHAR(2)
            val shipDateYr = cs.getString(26); // out SHIPDATEYR CHAR(2)
            val retOrderType = cs.getString(27); // out RETORDERTYPE CHAR(1),
            val enteredBy = cs.getString(28); // out ENTEREDBY CHAR(3)
            val branchNumber = cs.getInt(29); // out BRANCHNUMBER NUMERIC(3)
            val branchName = cs.getString(30); // out BRANCHNAME CHAR(25),
            val address1 = cs.getString(31); // out ADDRESS1 CHAR(30)
            val address2 = cs.getString(32); // out ADDRESS2 CHAR(30)
            val address3 = cs.getString(33); // out ADDRESS3 CHAR(30)
            val city = cs.getString(34); // out CITY CHAR(25),
            val state = cs.getString(35); // out STATE CHAR(2)
            val zip = cs.getString(36); // out ZIP CHAR(10)
            val country = cs.getString(37); // out COUNTRY CHAR(10)
            val paidStatus = cs.getString(38); // out PAIDSTATUS CHAR(12),
            val jobNumber = cs.getString(39); // out JOBNUMBER CHAR(7)
            val jobName = cs.getString(40); // out JOBNAME CHAR(15)
            val purchaseOrdNum = cs.getString(41); // out PURCHASEORDNUM CHAR(22),
            val orderedBy = cs.getString(42); // out ORDEREDBY CHAR(30)
            val shipMethod = cs.getString(43); // out SHIPMENTMETHOD CHAR(1)
            val shipVia = cs.getString(44); // out SHIPVIA CHAR(15),
            val trackingNumber = cs.getString(45); // out TRACKINGNUMBER CHAR(30)
            val trackingUrl = cs.getString(46); // out TRACKINGURL CHAR(80)
            val shipToAddress1 = cs.getString(47); // out SHIPTOADDRESS1 CHAR(30),
            val shipToAddress2 = cs.getString(48); // out SHIPTOADDRESS2 CHAR(30)
            val shipToAddress3 = cs.getString(49); // out SHIPTOADDRESS3 CHAR(30)
            val shipToCity = cs.getString(50); // out SHIPTOCITY CHAR(25),
            val shipToState = cs.getString(51); // out SHIPTOSTATE CHAR(2)
            val shipToZipCode = cs.getString(52); // out SHIPTOZIPCODE CHAR(10)
            val shipToCountry = cs.getString(53); // out SHIPTOCOUNTRY CHAR(10),
            val phoneNumber = cs.getString(54); // out PHONENUMBER CHAR(12)
            val subTotal = cs.getBigDecimal(55); // out SUBTOTAL NUMERIC(9, 2)
            val othCharges = cs.getBigDecimal(56); // out OTHCHARGES NUMERIC(9, 2),
            val taxAmount = cs.getBigDecimal(57); // out TAXAMOUNT NUMERIC(9, 2)
            val otherTaxAmt = cs.getBigDecimal(58); // out OTHERTAXAMT NUMERIC(9, 2)
            val totalAmount = cs.getBigDecimal(59); // out TOTALAMOUNT NUMERIC(9, 2),
            val gstHstCode = cs.getString(60); // out GSTHSTCODE CHAR(3))
            val fcc = cs.getString(61); // out FCC CHAR(1)
            val sibo = cs.getBigDecimal(62); // out SIBO NUMERIC(9, 2)
            val contractNumber = cs.getString(63); // out SIBO NUMERIC(9, 2)
            val termsDesc =  cs.getString(64); // out TERMS CHAR(30)
            val dueDateMo = cs.getString(65); // out DUEDATEMO CHAR(2)
            val dueDateDy = cs.getString(66); // out DUEDATEDY CHAR(2)
            val dueDateCC = cs.getString(67); // out DUEDATECC CHAR(2),
            val dueDateYr = cs.getString(68); // out DUEDATEYR CHAR(2)
            val invoiceDateMo = cs.getString(69); // out INOVICEDATEMO CHAR(2)
            val invoiceDateDy = cs.getString(70); // out INOVICEDUEDATEDY CHAR(2)
            val invoiceDateCC = cs.getString(71); // out INOVICEDUEDATECC CHAR(2),
            val invoiceDateYr = cs.getString(72); // out INOVICEDUEDATEYR CHAR(2)
            List<String> splInstructions= new ArrayList<>();
            splInstructions.add( cs.getString(73)); // out INSTRUCTION1 CHAR(40)
            splInstructions.add( cs.getString(74)); // out INSTRUCTION2 CHAR(40)
            splInstructions.add( cs.getString(75)); // out INSTRUCTION3 CHAR(40)
            splInstructions.add( cs.getString(76)); // out INSTRUCTION4 CHAR(40)
            splInstructions.add( cs.getString(77)); // out INSTRUCTION5 CHAR(40)
            splInstructions.add( cs.getString(78)); // out INSTRUCTION6 CHAR(40)



                orderHeaderDTO.setOrderNumber(orderNumber);

                orderHeaderDTO.setOrderStatus(
                        OrderStatusEnum.normalizeOrderStatus(OrderStatusEnum.valueOf(status)));

                orderHeaderDTO.setJobName(
                        !jobName.isBlank() ? jobName : "N/A");

                orderHeaderDTO.setJobNumber(jobNumber);

                orderHeaderDTO.setOrderDate(
                        MincronDataFormatting.formatDate(orderDateDy, orderDateMo, orderDateYr));

                orderHeaderDTO.setDueDate( "0".equals(dueDateDy) || "00".equals(dueDateDy) ? "N/A":
                        MincronDataFormatting.formatDate(dueDateDy, dueDateMo, dueDateYr));

                orderHeaderDTO.setOrderBy(
                        !orderedBy.isBlank() ? orderedBy : "N/A");

                orderHeaderDTO.setPurchaseOrderNumber(
                        !purchaseOrdNum.isBlank() ? purchaseOrdNum : "N/A");

                orderHeaderDTO.setShipDate(
                        shipDateMo.trim().equals("0") ||  shipDateMo.trim().equals("00") ? "N/A" :
                        MincronDataFormatting.formatDate(shipDateDy, shipDateMo, shipDateYr));

            orderHeaderDTO.setInvoiceDate(
                    invoiceDateMo.trim().equals("0") ||  invoiceDateMo.trim().equals("00") ? "N/A" :
                            MincronDataFormatting.formatDate(invoiceDateDy, invoiceDateMo, invoiceDateYr));

                orderHeaderDTO.setShipmentMethod(!"".equals(shipMethod.trim()) ?
                        ShipmentMethodEnum.valueOf(shipMethod).getShipMethod() : "N/A");

                orderHeaderDTO.setSubTotal(subTotal.floatValue());
                orderHeaderDTO.setTaxAmount(taxAmount.floatValue());
                orderHeaderDTO.setTotalAmount(totalAmount.floatValue());
                orderHeaderDTO.setOtherCharges(othCharges.floatValue());

                orderHeaderDTO.setDelivery(!"".equals(shipMethod.trim()) ?
                        ShipmentMethodEnum.isDelivery(ShipmentMethodEnum.valueOf(shipMethod)) : false);

                if (orderHeaderDTO.isDelivery()) {
                    AddressDTO addressDTO = new AddressDTO(shipToAddress1, shipToAddress2, shipToAddress3,
                            shipToCity, shipToState, shipToCountry, shipToZipCode);

                    orderHeaderDTO.setShipToAddress(addressDTO);
                }

                orderHeaderDTO.setBranchNumber(branchNumber);
                orderHeaderDTO.setContractNumber(contractNumber);
                orderHeaderDTO.setTerms(termsDesc.trim());
                orderHeaderDTO.setSpecialInstructions(splInstructions);

        } catch (SQLException e) {
            throw new MincronException(
                String.format("%s SQL State: %s", e.getMessage(), e.getSQLState()),
                HttpStatus.BAD_REQUEST
            );
        } catch (Exception e) {
            throw new MincronException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return orderHeaderDTO;
    }

    public List<ProductLineItemDTO> getOrderItemList(
        String accountId,
        String orderType,
        String orderNumber,
        String WCStdOrder,
        Integer startRow,
        Integer maxRows
    ) throws MincronException {
        PageDTO<ProductLineItemDTO> orderPage = new PageDTO<>();
        orderPage.setStartRow(startRow);

        try (
            CallBuilderConfig cbc = mcf.makeManagedCall(
                ProgramCallNumberEnum.GET_ORDER_ITEM_LIST.getProgramCallNumber(),
                14,
                true
            )
        ) {
            // 6 input parameters
            cbc.setInputString(accountId);
            cbc.setInputString(orderType.toUpperCase());
            cbc.setInputString(orderNumber);
            cbc.setInputString(WCStdOrder);
            cbc.setInputInt(startRow);
            cbc.setInputInt(maxRows);

            // 2 error outputs
            cbc.setOutputChar();
            cbc.setOutputChar();

            // 1 table
            cbc.setOutputDecimal();

            // Param count = 6 input + 1 output + 5 default = 14

            ResponseBuilderConfig rb = cbc.getResultSet(ORDER_CALLS_STARTING_ROW);

            while (rb.hasMoreData()) {
                ProductLineItemDTO productLineItemDTO = new ProductLineItemDTO();
                productLineItemDTO.setOrderNumber(rb.getResultString());
                productLineItemDTO.setDisplayOnly(rb.getResultString());
                productLineItemDTO.setProductNumber(rb.getResultString());
                productLineItemDTO.setDescription(rb.getResultString());
                rb.getResultString();
                productLineItemDTO.setUom(rb.getResultString());
                productLineItemDTO.setLineNumber(rb.getResultString());
                productLineItemDTO.setQuantityOrdered(rb.getResultString());
                productLineItemDTO.setUnitPrice(rb.getResultString());
                productLineItemDTO.setExtendedPrice(rb.getResultString());
                productLineItemDTO.setQuantityBackOrdered(rb.getResultString());
                productLineItemDTO.setQuantityShipped(rb.getResultString());
                productLineItemDTO.setOrderLineItemTypeCode(rb.getResultString());
                productLineItemDTO.setPricingUom(rb.getResultString());
                orderPage.results.add(productLineItemDTO);
            }

            orderPage.setRowsReturned(orderPage.results.size());

            orderPage.setTotalRows(orderPage.getRowsReturned());
        } catch (SQLException e) {
            throw new MincronException(
                String.format("%s SQL State: %s", e.getMessage(), e.getSQLState()),
                HttpStatus.BAD_REQUEST
            );
        } catch (Exception e) {
            throw new MincronException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return orderPage.getResults();
    }
}
