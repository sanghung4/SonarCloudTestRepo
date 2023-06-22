package com.reece.platform.mincron.service;

import com.reece.platform.mincron.callBuilder.CallBuilderConfig;
import com.reece.platform.mincron.callBuilder.ManagedCallFactory;
import com.reece.platform.mincron.callBuilder.ResponseBuilderConfig;
import com.reece.platform.mincron.exceptions.MincronException;
import com.reece.platform.mincron.model.InvoiceDTO;
import com.reece.platform.mincron.model.OrderDTO;
import com.reece.platform.mincron.model.common.PageDTO;
import com.reece.platform.mincron.model.enums.ProgramCallNumberEnum;
import com.reece.platform.mincron.utilities.MincronDataFormatting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class InvoiceService {

    private final ManagedCallFactory mcf;
    private final OrderService orderService;
    private static final SimpleDateFormat MINCRON_INVOICE_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat MINCRON_ORDER_DATE_FORMAT = new SimpleDateFormat("MMddyyyy");
    private static final SimpleDateFormat MAX_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
    private static final int MAX_ORDER_RESULT_SIZE = 10000;
    private static final String OPEN_STATUS = "Open";
    private static final String INVOICED_STATUS = "Invoiced";
    private static final int LATEST_CLOSED_INVOICE_MONTH_COUNT = 14;

    @Autowired
    public InvoiceService(ManagedCallFactory mcf, OrderService orderService) {
        this.mcf = mcf;
        this.orderService = orderService;
    }

    /**
     * Fetches invoice data from Mincron for given accountId
     *
     * @param accountId accountId to fetch invoice from
     * @return list of invoice data
     * @throws ParseException thrown when dates coming back from Mincron ERP are incorrectly formatted
     */
    public List<InvoiceDTO> getInvoices(String accountId) throws ParseException, MincronException {
        List<InvoiceDTO> invoiceDTOS = new ArrayList<>();
        try (CallBuilderConfig cbc = mcf.makeManagedCall(ProgramCallNumberEnum.GET_INVOICE_LIST.getProgramCallNumber(), 9, true)) {
            cbc.setInputString(accountId);
            cbc.setInputString("");

            cbc.setOutputChar();
            cbc.setOutputChar();

            ResponseBuilderConfig rb = cbc.getResultSet(1);

            while (rb.hasMoreData()) {
                InvoiceDTO invoice = new InvoiceDTO();
                invoice.setInvoiceNumber(rb.getResultString());

                String invoiceMonth = rb.getResultString();
                String invoiceDay = rb.getResultString();
                String invoiceCentury = rb.getResultString();
                String invoiceYear = rb.getResultString();
                invoice.setInvoiceDate(MincronDataFormatting.formatDate(invoiceDay, invoiceMonth, invoiceYear));

                invoice.setCustomerPo(rb.getResultString());
                invoice.setAge(rb.getResultString());
                invoice.setJobNumber(rb.getResultString());
                invoice.setJobName(rb.getResultString());
                invoice.setOpenBalance(rb.getResultString());
                rb.getResultString();
                invoice.setContractNumber(rb.getResultString());
                invoice.setOriginalAmt(rb.getResultString());

                //program call returns "unpaid" invoices which is synonymous to "Open" status
                invoice.setStatus(OPEN_STATUS);

                invoiceDTOS.add(invoice);
            }
        } catch (SQLException e) {
            throw new MincronException(
                    String.format("%s SQL State: %s", e.getMessage(), e.getSQLState()),
                    HttpStatus.BAD_REQUEST
            );
        } catch (Exception e) {
            throw new MincronException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (invoiceDTOS.isEmpty()) {
            return invoiceDTOS;
        }

        // Find earliest and latest unpaid invoice to filter down request for order list.
        // Use minimum 14 months to ensure all "Closed" invoices are found within that time period per requirements.
        Date today = new Date();
        Calendar calendarLatestClosedInvoiceDate = Calendar.getInstance();
        calendarLatestClosedInvoiceDate.setTime(today);
        calendarLatestClosedInvoiceDate.add(Calendar.MONTH, -LATEST_CLOSED_INVOICE_MONTH_COUNT);
        Date latestClosedInvoiceDate = calendarLatestClosedInvoiceDate.getTime();
        Date earliestDate = latestClosedInvoiceDate;
        Date latestDate = today;
        for (InvoiceDTO invoiceDTO: invoiceDTOS) {
            Date currentDate = MAX_DATE_FORMAT.parse(invoiceDTO.getInvoiceDate());
            if (!currentDate.after(earliestDate)) {
                earliestDate = currentDate;
            }

            if (currentDate.after(latestDate)) {
                latestDate = currentDate;
            }
        }
        PageDTO<OrderDTO> invoiceListData = orderService.getOrderList(accountId, "order", "ALL", 1, MAX_ORDER_RESULT_SIZE, "", MINCRON_INVOICE_DATE_FORMAT.format(earliestDate), MINCRON_INVOICE_DATE_FORMAT.format(latestDate), "", "");

        Map<InvoiceDTO, OrderDTO> dtoMap = new HashMap<>();
        for (InvoiceDTO invoiceDTO: invoiceDTOS) {
            Optional<OrderDTO> maybeMatchingOrder = invoiceListData.getResults()
                    .stream().filter((orderDTO -> orderDTO.getOrderNumber() != null && orderDTO.getOrderNumber().equals(invoiceDTO.getInvoiceNumber())))
                    .findFirst();
            maybeMatchingOrder.ifPresent(orderDTO -> dtoMap.put(invoiceDTO, orderDTO));
        }

        List<InvoiceDTO> invoiceList = new ArrayList<>();
        for (OrderDTO orderDTO: invoiceListData.getResults()) {
            if (invoiceDTOS.stream().noneMatch((invoiceDTO -> invoiceDTO.getInvoiceNumber().equals(orderDTO.getOrderNumber())))) {
                String invoiceDate = orderDTO.getInvoiceDate();
                if (invoiceDate != null && invoiceDate.length() == 7) {
                    invoiceDate = "0" + invoiceDate;
                }
                if (orderDTO.getStatus().equals(INVOICED_STATUS)
                        && orderDTO.getInvoiceDate() != null
                        && !orderDTO.getInvoiceDate().equals("0")) {
                    Date invoiceDateObj = MINCRON_ORDER_DATE_FORMAT.parse(invoiceDate);
                    orderDTO.setInvoiceDate(MAX_DATE_FORMAT.format(invoiceDateObj));
                    if (!MAX_DATE_FORMAT.parse(orderDTO.getInvoiceDate()).before(latestClosedInvoiceDate)) {
                        invoiceList.add(new InvoiceDTO(orderDTO));
                    }
                }
            }
        }

        invoiceList.addAll(dtoMap.keySet());
        return invoiceList;
    }
}
