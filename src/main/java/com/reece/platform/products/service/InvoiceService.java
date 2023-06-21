package com.reece.platform.products.service;

import com.reece.platform.products.branches.service.BranchesService;
import com.reece.platform.products.exceptions.InvoiceDateRangeException;
import com.reece.platform.products.exceptions.OrderNotFoundException;
import com.reece.platform.products.external.appsearch.model.PageRequest;
import com.reece.platform.products.external.mincron.MincronServiceClient;
import com.reece.platform.products.external.mincron.model.OrderHeader;
import com.reece.platform.products.external.mincron.model.ProductLineItem;
import com.reece.platform.products.model.DTO.GetInvoiceResponseDTO;
import com.reece.platform.products.model.DTO.InvoiceDTO;
import com.reece.platform.products.model.DTO.MincronSingleInvoiceDTO;
import com.reece.platform.products.model.ErpEnum;
import com.reece.platform.products.model.MincronAgingEnum;
import com.reece.platform.products.model.MincronOrderStatus;
import com.reece.platform.products.model.eclipse.common.EclipseAddressResponseDTO;
import com.reece.platform.products.search.SearchService;
import com.reece.platform.products.utilities.InvoiceUtils;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class InvoiceService {

    @Value("${billtrust_base_url}")
    private String billtrustBaseUrl;

    @Value("${billtrust_encryption_key}")
    private String billtrustEncryptionKey;

    @Value("${billtrust_client_guid}")
    private String billtrustClientGuid;

    private final ErpService erpService;
    private final MincronServiceClient mincronServiceClient;
    private final BranchesService branchesService;
    private final SearchService searchService;

    @Autowired
    public InvoiceService(
        ErpService erpService,
        MincronServiceClient mincronServiceClient,
        BranchesService branchesService,
        SearchService searchService
    ) {
        this.erpService = erpService;
        this.mincronServiceClient = mincronServiceClient;
        this.branchesService = branchesService;
        this.searchService = searchService;
    }

    /**
     * Fetch invoices for a given account.
     *
     * @param erpAccountId account to fetch invoices for
     * @param erpName erp system the invoices should be fetched for
     * @return list of invoice DTOs
     */
    public GetInvoiceResponseDTO getInvoices(
        String erpAccountId,
        String erpName,
        String shipTo,
        Date startDate,
        Date endDate,
        String invoiceStatus
    ) throws HttpClientErrorException, InvoiceDateRangeException {
        if (ErpEnum.MINCRON.name().equals(erpName)) {
            List<InvoiceDTO> invoiceDTOS = erpService.getMincronInvoices(erpAccountId);
            return buildMincronInvoiceResponse(invoiceDTOS);
        } else {
            long monthMilis = 1000L * 3600 * 24 * 30 * 14;
            Date minDate = new Date(new Date().getTime() - monthMilis);
            if (startDate != null) {
                if (startDate.before(minDate)) {
                    throw new InvoiceDateRangeException("Start date exceeds 14 months");
                } else if (endDate != null) {
                    if (endDate.before(startDate)) {
                        throw new InvoiceDateRangeException("End date must be after start date");
                    } else if ((endDate.getTime() - startDate.getTime()) > monthMilis) {
                        throw new InvoiceDateRangeException("Date range exceeds 14 months");
                    }
                }
            }
            GetInvoiceResponseDTO erpInvoices = erpService.getEclipseInvoices(
                erpAccountId,
                shipTo,
                startDate,
                endDate,
                invoiceStatus
            );
            if (erpInvoices.getInvoices() != null) {
                erpInvoices
                    .getInvoices()
                    .stream()
                    .forEach(invoice ->
                        invoice.setInvoiceUrl(
                            new InvoiceUtils()
                                .getInvoiceUrl(
                                    erpAccountId,
                                    invoice.getInvoiceNumber(),
                                    billtrustBaseUrl,
                                    billtrustEncryptionKey,
                                    billtrustClientGuid
                                )
                        )
                    );
            }
            return erpInvoices;
        }
    }

    /**
     * Builds the aggregate buckets and total amounts for the response data on Mincron invoices
     *
     * @param invoiceDTOS list of invoices to aggregate and calculate totals on
     * @return response DTO with aggregations and invoice totals
     */
    private GetInvoiceResponseDTO buildMincronInvoiceResponse(List<InvoiceDTO> invoiceDTOS) {
        GetInvoiceResponseDTO getInvoiceResponseDTO = new GetInvoiceResponseDTO();

        double bucketThirty = 0.0d;
        double bucketSixty = 0.0d;
        double bucketNinety = 0.0d;
        double bucketOneTwenty = 0.0d;
        double bucketFuture = 0.0d;
        double currentAmt = 0.0d;
        for (InvoiceDTO invoiceDTO : invoiceDTOS) {
            MincronAgingEnum agingEnum = MincronAgingEnum.fromAgingCode(invoiceDTO.getAge());
            if (agingEnum != null) {
                switch (agingEnum) {
                    case DAYS_30 -> {
                        bucketThirty += invoiceDTO.getOpenBalance();
                        invoiceDTO.setAge(MincronAgingEnum.DAYS_30.getName());
                    }
                    case DAYS_60 -> {
                        bucketSixty += invoiceDTO.getOpenBalance();
                        invoiceDTO.setAge(MincronAgingEnum.DAYS_60.getName());
                    }
                    case DAYS_90 -> {
                        bucketNinety += invoiceDTO.getOpenBalance();
                        invoiceDTO.setAge(MincronAgingEnum.DAYS_90.getName());
                    }
                    case DAYS_120 -> {
                        bucketOneTwenty += invoiceDTO.getOpenBalance();
                        invoiceDTO.setAge(MincronAgingEnum.DAYS_120.getName());
                    }
                    case FUTURE -> {
                        bucketFuture += invoiceDTO.getOpenBalance();
                        invoiceDTO.setAge(MincronAgingEnum.FUTURE.getName());
                    }
                    case CURRENT -> {
                        currentAmt += invoiceDTO.getOpenBalance();
                        invoiceDTO.setAge(MincronAgingEnum.CURRENT.getName());
                    }
                    default -> {}
                }
            }
        }

        getInvoiceResponseDTO.setBucketThirty(bucketThirty);
        getInvoiceResponseDTO.setBucketSixty(bucketSixty);
        getInvoiceResponseDTO.setBucketNinety(bucketNinety);
        getInvoiceResponseDTO.setBucketOneTwenty(bucketOneTwenty);
        getInvoiceResponseDTO.setBucketFuture(bucketFuture);
        getInvoiceResponseDTO.setCurrentAmt(currentAmt);

        double totalPastDue = bucketThirty + bucketSixty + bucketNinety + bucketOneTwenty;
        getInvoiceResponseDTO.setTotalPastDue(totalPastDue);
        getInvoiceResponseDTO.setTotalAmt(totalPastDue + currentAmt + bucketFuture);

        getInvoiceResponseDTO.setInvoices(invoiceDTOS);
        return getInvoiceResponseDTO;
    }

    public String getInvoicesPdfUrl(String erpAccountId, List<String> invoiceNumbers) {
        return new InvoiceUtils()
            .getInvoiceUrl(erpAccountId, invoiceNumbers, billtrustBaseUrl, billtrustEncryptionKey, billtrustClientGuid);
    }

    public MincronSingleInvoiceDTO getMincronInvoice(String erpAccountId, String invoiceNumber)
        throws OrderNotFoundException {
        OrderHeader invoiceHeader = mincronServiceClient
            .getOrderHeader(invoiceNumber, MincronOrderStatus.INVOICED.getOrderType())
            .orElseThrow(() -> new OrderNotFoundException(erpAccountId, invoiceNumber));

        List<InvoiceDTO> invoiceDTOS = erpService.getMincronInvoices(erpAccountId);

        Double openBalance = invoiceDTOS
            .stream()
            .filter(invoice -> invoice.getInvoiceNumber().equals(invoiceNumber))
            .findFirst()
            .orElseThrow(() -> new OrderNotFoundException(erpAccountId, invoiceNumber))
            .getOpenBalance();

        MincronSingleInvoiceDTO mincronSingleInvoiceDTO = new MincronSingleInvoiceDTO(invoiceHeader, openBalance);

        if (!mincronSingleInvoiceDTO.isDelivery()) {
            val branch = branchesService.getBranchByEntityId(
                String.valueOf(invoiceHeader.getBranchNumber()),
                ErpEnum.MINCRON.name()
            );
            mincronSingleInvoiceDTO.setAddress(new EclipseAddressResponseDTO(branch));
        }

        val productLineItems = mincronServiceClient
            .getOrderItemList(erpAccountId, "invoice", invoiceNumber)
            .stream()
            .filter(
                (
                    productLineItem ->
                        !(
                            productLineItem.getDisplayOnly() != null &&
                            productLineItem.getDisplayOnly().toUpperCase(Locale.ROOT).equals("Y")
                        )
                )
            )
            .toList();
        val customerPartNumbers = productLineItems
            .stream()
            .map(ProductLineItem::getProductNumber)
            .collect(Collectors.toList());

        val products = searchService
            .getProductsByCustomerPartNumber(
                "mincron-ecomm-products",
                customerPartNumbers,
                new PageRequest(customerPartNumbers.size(), 1)
            )
            .stream()
            .collect(
                Collectors.toMap(
                    p -> {
                        val customerNumbers = p.getCustomerPartNumber();
                        return customerNumbers.get(0);
                    },
                    p -> p
                )
            );

        mincronSingleInvoiceDTO.setInvoiceItems(
            productLineItems
                .stream()
                .map(lineItem -> {
                    val product = products.get(lineItem.getProductNumber());
                    if (product == null) {
                        log.warn(
                            "No product found in search engine for productNumber: '{}'",
                            lineItem.getProductNumber()
                        );
                    }

                    return new MincronSingleInvoiceDTO.InvoiceProduct(product, lineItem);
                })
                .collect(Collectors.toList())
        );

        return mincronSingleInvoiceDTO;
    }
}
