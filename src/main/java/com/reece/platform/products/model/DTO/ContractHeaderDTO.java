package com.reece.platform.products.model.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.reece.platform.products.branches.model.DTO.BranchResponseDTO;
import com.reece.platform.products.external.mincron.model.Address;
import com.reece.platform.products.external.mincron.model.ContractHeader;
import com.reece.platform.products.external.mincron.model.ProductLineItem;
import com.reece.platform.products.model.TechDoc;
import com.reece.platform.products.model.TechSpec;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ContractHeaderDTO {

    @Data
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public static class AccountInformation {

        @Data
        @JsonInclude(JsonInclude.Include.NON_ABSENT)
        public static class ContractAddress {

            private String entityId;
            private String branchNumber;
            private String branchName;
            private String address1;
            private String address2;
            private String address3;
            private String city;
            private String state;
            private String zip;
            private String county;
            private String country;

            public ContractAddress(Address mincronAddress) {
                address1 = mincronAddress.getAddress1();
                address2 = mincronAddress.getAddress2();
                address3 = mincronAddress.getAddress3();
                city = mincronAddress.getCity();
                state = mincronAddress.getState();
                zip = mincronAddress.getZip();
                county = mincronAddress.getCounty();
                country = mincronAddress.getCountry();
            }

            public ContractAddress(BranchResponseDTO branch) {
                entityId = branch.getEntityId();
                branchNumber = branch.getBranchId();
                branchName = branch.getName();
                address1 = branch.getAddress1();
                address2 = branch.getAddress2();
                city = branch.getCity();
                state = branch.getState();
                zip = branch.getZip();
            }
        }

        private ContractAddress shipToAddress;
        private ContractAddress branch;

        public AccountInformation(ContractHeader mincronContract, BranchResponseDTO branchResponseDTO) {
            shipToAddress = new ContractAddress(mincronContract.getShipToAddress());
            branch = new ContractAddress(branchResponseDTO);
        }
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public static class ContractDates {

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
        private LocalDate promisedDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
        private LocalDate contractDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
        private LocalDate firstReleaseDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
        private LocalDate lastReleaseDate;

        public ContractDates(ContractHeader mincronContract) {
            promisedDate = mincronContract.getPromisedDate();
            contractDate = mincronContract.getContractDate();
            firstReleaseDate = mincronContract.getFirstReleaseDate();
            lastReleaseDate = mincronContract.getLastReleaseDate();
        }
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public static class CustomerInfo {

        private String customerNumber;
        private String jobNumber;
        private String enteredBy;

        public CustomerInfo(ContractHeader mincronContract) {
            jobNumber = mincronContract.getJobNumber().trim();
            enteredBy = mincronContract.getEnteredBy();
        }
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public static class ContractSummary {

        private String subTotal;
        private String taxAmount;
        private String otherCharges;
        private String totalAmount;
        private String invoicedToDateAmount;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
        private LocalDate firstShipmentDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
        private LocalDate lastShipmentDate;

        public ContractSummary(ContractHeader mincronContract) {
            subTotal = mincronContract.getSubTotal();
            taxAmount = mincronContract.getTaxAmount();
            otherCharges = mincronContract.getOtherCharges();
            invoicedToDateAmount = mincronContract.getInvoicedToDateAmount();
            totalAmount = mincronContract.getTotalAmount();
            firstShipmentDate = mincronContract.getFirstShipmentDate();
            lastShipmentDate = mincronContract.getLastShipmentDate();
        }
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public static class ContractProduct {

        @Data
        @JsonInclude(JsonInclude.Include.NON_ABSENT)
        public static class ContractProductQty {

            private int quantityOrdered = 0;
            private int quantityReleasedToDate = 0;
            private int quantityShipped = 0;

            public ContractProductQty(ProductLineItem productLineItem) {
                if (!productLineItem.getQuantityOrdered().isEmpty()) {
                    quantityOrdered = Integer.parseInt(productLineItem.getQuantityOrdered());
                }

                if (!productLineItem.getQuantityReleasedToDate().isEmpty()) {
                    quantityReleasedToDate = Integer.parseInt(productLineItem.getQuantityReleasedToDate());
                }

                if (!productLineItem.getQuantityShipped().isEmpty()) {
                    quantityShipped = Integer.parseInt(productLineItem.getQuantityShipped());
                }
            }
        }

        private String id;
        private String brand;
        private String name;
        private String lineComments;
        private String partNumber;
        private String mfr;
        private String thumb;
        private double unitPrice;
        private double netPrice;
        private ContractProductQty qty;
        private List<TechDoc> technicalDocuments;
        private List<TechSpec> techSpecifications;
        private boolean displayOnly;
        private String sequenceNumber;
        private String pricingUom;

        public ContractProduct(ProductDTO product, ProductLineItem productLineItem) {
            lineComments = productLineItem.getLineComments();
            partNumber = productLineItem.getProductNumber();
            displayOnly = productLineItem.getDisplayOnly().equals("Y");
            unitPrice = Double.parseDouble(productLineItem.getUnitPrice());
            netPrice = Double.parseDouble(productLineItem.getNetPrice());
            qty = new ContractProductQty(productLineItem);
            name = productLineItem.getDescription();
            sequenceNumber = productLineItem.getSequenceNumber();
            pricingUom = productLineItem.getPricingUom();

            if (product != null) {
                id = product.getId();
                brand = product.getManufacturerName();
                name = product.getName();
                mfr = product.getManufacturerNumber();
                thumb = product.getImageUrls().getThumb();
                technicalDocuments = product.getTechnicalDocuments();
                techSpecifications = product.getTechSpecifications();
            } else {
                id = "null-product-" + UUID.randomUUID();
            }
        }
    }

    private String contractNumber;
    private String jobName;
    private String purchaseOrderNumber;
    private String contractDescription;
    private AccountInformation accountInformation;
    private ContractDates contractDates;
    private CustomerInfo customerInfo;
    private ContractSummary contractSummary;
    private List<ContractProduct> contractProducts;

    public ContractHeaderDTO(ContractHeader mincronContract, BranchResponseDTO branch) {
        contractNumber = mincronContract.getContractNumber();
        contractDescription = mincronContract.getContractDescription();
        jobName = mincronContract.getJobName().trim();
        purchaseOrderNumber = mincronContract.getPurchaseOrderNumber().trim();
        accountInformation = new AccountInformation(mincronContract, branch);
        contractDates = new ContractDates(mincronContract);
        customerInfo = new CustomerInfo(mincronContract);
        contractSummary = new ContractSummary(mincronContract);
    }
}
