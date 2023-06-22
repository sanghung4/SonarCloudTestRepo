package com.reece.platform.mincron.service;

import static com.reece.platform.mincron.utilities.MincronDataFormatting.convertDateFormat;

import com.google.gson.Gson;
import com.reece.platform.mincron.callBuilder.CallBuilderConfig;
import com.reece.platform.mincron.callBuilder.ManagedCallFactory;
import com.reece.platform.mincron.callBuilder.ResponseBuilderConfig;
import com.reece.platform.mincron.exceptions.MincronException;
import com.reece.platform.mincron.model.AddressDTO;
import com.reece.platform.mincron.model.BranchDTO;
import com.reece.platform.mincron.model.common.CartLineItemDTO;
import com.reece.platform.mincron.model.common.PageDTO;
import com.reece.platform.mincron.model.common.ProductLineItemDTO;
import com.reece.platform.mincron.model.contracts.ContractDTO;
import com.reece.platform.mincron.model.contracts.ContractListDTO;
import com.reece.platform.mincron.model.enums.ProgramCallNumberEnum;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.reece.platform.mincron.model.contracts.*;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ContractService {

    public static final String MINCRON_CONTRACTS_DATE_FORMAT = "yyyyMMdd";
    public static final String SUBMIT_ORDER_DTO_DATE_FORMAT = "MMddyyyy";
    public static final String PROMISE_DATE_FORMAT = "yyyyMMdd";
    public static final String MAX_DATE_FORMAT = "MM/dd/yyyy";
    private static final int CONTRACT_CALLS_STARTING_ROW = 2;
    private static final Pattern ALL_ZEROES_PATTERN = Pattern.compile("^[0]+$");
    private static final int MINCRON_MAX_ROWS_RETURNED = 10_000;
    private static final String MINCRON_USER_ID="JHILLIN";

    private final ManagedCallFactory mcf;
    private final RestTemplate restTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(ContractService.class);

    @Value("${mincron_host_websmart}")
    private String mincronHostUrl;

    @Autowired
    public ContractService(ManagedCallFactory mcf, RestTemplateBuilder restTemplateBuilder) {
        this.mcf = mcf;
        this.restTemplate = restTemplateBuilder.build();
    }

    public PageDTO<ContractListDTO> getContractList(
        String accountId,
        Integer startRow,
        Integer maxRows,
        String searchFilter,
        String fromDate,
        String toDate,
        String sortOrder,
        String sortDirection
    ) throws MincronException {
        PageDTO<ContractListDTO> contractPage = new PageDTO<>();
        contractPage.setStartRow(startRow);

        try (
            CallBuilderConfig cbc = mcf.makeManagedCall(
                ProgramCallNumberEnum.GET_CONTRACT_LIST.getProgramCallNumber(),
                18,
                true
            )
        ) {
            cbc.setInputString(accountId);
            cbc.setInputInt(startRow);
            cbc.setInputInt(maxRows);
            cbc.setInputString(searchFilter);

            cbc.setInputString(
                fromDate.length() > 0
                    ? convertDateFormat(fromDate, MAX_DATE_FORMAT, MINCRON_CONTRACTS_DATE_FORMAT)
                    : fromDate
            );

            cbc.setInputString(
                toDate.length() > 0 ? convertDateFormat(toDate, MAX_DATE_FORMAT, MINCRON_CONTRACTS_DATE_FORMAT) : toDate
            );

            cbc.setInputString(sortOrder);
            cbc.setInputString(sortDirection);

            cbc.setOutputChar();
            cbc.setOutputChar();
            cbc.setOutputDecimal();
            cbc.setOutputDecimal();
            cbc.setOutputDecimal();

            ResponseBuilderConfig rb = cbc.getResultSet(CONTRACT_CALLS_STARTING_ROW);

            while (rb.hasMoreData()) {
                ContractListDTO contract = new ContractListDTO();
                contract.setContractNumber(rb.getResultString());
                contract.setDescription(rb.getResultString());

                contract.setContractDate(
                    convertDateFormat(rb.getMincronDate(), MINCRON_CONTRACTS_DATE_FORMAT, MAX_DATE_FORMAT)
                );

                contract.setPromiseDate(
                    convertDateFormat(rb.getMincronDate(), MINCRON_CONTRACTS_DATE_FORMAT, MAX_DATE_FORMAT)
                );

                String firstReleaseDateMincron = rb.getMincronDate();
                contract.setFirstReleaseDate(!isZeroDate(firstReleaseDateMincron) ?
                        convertDateFormat(firstReleaseDateMincron, MINCRON_CONTRACTS_DATE_FORMAT, MAX_DATE_FORMAT) : null
                );

                String lastReleaseDateMincron = rb.getMincronDate();
                contract.setLastReleaseDate(!isZeroDate(lastReleaseDateMincron) ?
                        convertDateFormat(lastReleaseDateMincron, MINCRON_CONTRACTS_DATE_FORMAT, MAX_DATE_FORMAT) : null
                );

                contract.setJobNumber(rb.getResultString());
                contract.setJobName(rb.getResultString());
                contract.setPurchaseOrderNumber(rb.getResultString());
                contractPage.results.add(contract);
            }

            contractPage.setRowsReturned(contractPage.results.size());

            contractPage.setTotalRows(cbc.getNumberOfRows());
        } catch (SQLException e) {
            throw new MincronException(
                String.format("%s SQL State: %s", e.getMessage(), e.getSQLState()),
                HttpStatus.BAD_REQUEST
            );
        } catch (Exception e) {
            throw new MincronException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return contractPage;
    }


    public ContractDTO getContractHeader(String contractNumber) {
        ContractDTO contract = new ContractDTO();
        contract.setContractNumber(contractNumber);
        try (
                CallBuilderConfig cbc = mcf.makeManagedCall(
                        ProgramCallNumberEnum.GET_CONTRACT_HEADER.getProgramCallNumber(),
                        41,
                        true
                )
        ) {
            cbc.setInputString(contractNumber);

            cbc.setOutputChar(); // ERRCD
            cbc.setOutputChar(); // ERRMSG

            cbc.setOutputChar(); // PROMISEDDATE
            cbc.setOutputChar(); // ORDERDATE
            cbc.setOutputChar(); // ENTEREDBY
            cbc.setOutputChar(); // BRANCHNUMBER
            cbc.setOutputChar(); // BRANCHNAME
            cbc.setOutputChar(); // BRADDRESS1
            cbc.setOutputChar(); // BRADDRESS2
            cbc.setOutputChar(); // BRADDRESS3
            cbc.setOutputChar(); // BRCITY
            cbc.setOutputChar(); // BRSTATE
            cbc.setOutputChar(); // BRZIP
            cbc.setOutputChar(); // JOBNUMBER
            cbc.setOutputChar(); // JOBNAME
            cbc.setOutputChar(); // PURCHORDNUM
            cbc.setOutputChar(); // FIRSTRELDATE
            cbc.setOutputChar(); // LASTRELDATE
            cbc.setOutputChar(); // FIRSTSHIPDAT
            cbc.setOutputChar(); // LASTSHIPDATE
            cbc.setOutputChar(); // SHIPTOADDR1
            cbc.setOutputChar(); // SHIPTOADDR2
            cbc.setOutputChar(); // SHIPTOADDR3
            cbc.setOutputChar(); // SHIPTOCITY
            cbc.setOutputChar(); // SHIPTOSTATE
            cbc.setOutputChar(); // SHIPTOZIPCDE
            cbc.setOutputChar(); // SHIPTOCOUNTY
            cbc.setOutputNumeric(2); // SUBTOTAL
            cbc.setOutputNumeric(2); // OTHERCHARGE
            cbc.setOutputNumeric(2); // TAX
            cbc.setOutputNumeric(2); // OTHERTAX
            cbc.setOutputNumeric(2); // ORDERTOTAL
            cbc.setOutputDecimal(); // GSTHSTCODE
            cbc.setOutputDecimal(); // DESCRIPTION
            cbc.setOutputNumeric(2); // INVTODATEAMT

            ResponseBuilderConfig rb = cbc.getResultSet(CONTRACT_CALLS_STARTING_ROW);

            val cs = cbc.getCs();

            val promisedDate = cs.getString(9);
            val contractDate = cs.getString(10);
            val enteredBy = cs.getString(11);
            val branchNumber = cs.getString(12);
            val branchName = cs.getString(13);
            val branchAddress1 = cs.getString(14);
            val branchAddress2 = cs.getString(15);
            val branchAddress3 = cs.getString(16);
            val branchCity = cs.getString(17);
            val branchState = cs.getString(18);
            val branchZip = cs.getString(19);
            val jobNumber = cs.getString(20);
            val jobName = cs.getString(21);
            val purchaseOrderNum = cs.getString(22);
            val firstReleaseDate = cs.getString(23);
            val lastReleaseDate = cs.getString(24);
            val firstShipmentDate = cs.getString(25);
            val lastShipmentDate = cs.getString(26);
            val shipAddress1 = cs.getString(27);
            val shipAddress2 = cs.getString(28);
            val shipAddress3 = cs.getString(29);
            val shipCity = cs.getString(30);
            val shipState = cs.getString(31);
            val shipZip = cs.getString(32);
            val shipCounty = cs.getString(33);
            val subTotal = cs.getBigDecimal(34);
            val otherCharges = cs.getBigDecimal(35);
            val taxAmount = cs.getBigDecimal(36);
            val otherTaxAmount = cs.getBigDecimal(37);
            val totalAmount = cs.getBigDecimal(38);
            val gstHstUseCode = cs.getString(39);
            val contractDescription = cs.getString(40);
            val invoiceToDateAmount = cs.getBigDecimal(41);

            // Dates
            contract.setPromisedDate(!isZeroDate(promisedDate.trim()) ?
                    LocalDate.parse(promisedDate.trim(), DateTimeFormatter.ofPattern("MMddyyyy")) : null);
            contract.setFirstReleaseDate(!isZeroDate(firstReleaseDate.trim()) ?
                    LocalDate.parse(firstReleaseDate.trim(), DateTimeFormatter.ofPattern("MMddyyyy")) : null);
            contract.setContractDate(!isZeroDate(contractDate.trim()) ?
                    LocalDate.parse(contractDate.trim(), DateTimeFormatter.ofPattern("MMddyyyy")) : null);
            contract.setLastReleaseDate(!isZeroDate(lastReleaseDate.trim()) ?
                    LocalDate.parse(lastReleaseDate.trim(), DateTimeFormatter.ofPattern("MMddyyyy")) : null);
            contract.setFirstShipmentDate(!isZeroDate(firstShipmentDate.trim()) ?
                    LocalDate.parse(firstShipmentDate.trim(), DateTimeFormatter.ofPattern("MMddyyyy")) : null);
            contract.setLastShipmentDate(!isZeroDate(lastShipmentDate.trim()) ?
                    LocalDate.parse(lastShipmentDate.trim(), DateTimeFormatter.ofPattern("MMddyyyy")) : null);

            // Branch details
            BranchDTO branchDTO = new BranchDTO();
            branchDTO.setBranchNumber(branchNumber);
            branchDTO.setBranchName(branchName);
            AddressDTO branchAddressDTO = new AddressDTO();
            branchAddressDTO.setAddress1(branchAddress1);
            branchAddressDTO.setAddress2(branchAddress2);
            branchAddressDTO.setAddress3(branchAddress3);
            branchAddressDTO.setCity(branchCity);
            branchAddressDTO.setState(branchState);
            branchAddressDTO.setZip(branchZip);
            branchDTO.setAddressDTO(branchAddressDTO);
            contract.setBranch(branchDTO);

            // Shipping address
            AddressDTO shipAddressDTO = new AddressDTO();
            shipAddressDTO.setAddress1(shipAddress1);
            shipAddressDTO.setAddress2(shipAddress2);
            shipAddressDTO.setAddress3(shipAddress3);
            shipAddressDTO.setCity(shipCity);
            shipAddressDTO.setState(shipState);
            shipAddressDTO.setZip(shipZip);
            shipAddressDTO.setCounty(shipCounty);
            contract.setShipToAddress(shipAddressDTO);

            // Amounts
            contract.setSubTotal(subTotal);
            contract.setOtherCharges(otherCharges);
            contract.setTaxAmount(taxAmount);
            contract.setOtherTaxAmount(otherTaxAmount);
            contract.setTotalAmount(totalAmount);
            contract.setInvoicedToDateAmount(invoiceToDateAmount);

            // Other
            contract.setGstHstUseCode(gstHstUseCode);
            contract.setContractDescription(contractDescription);
            contract.setEnteredBy(enteredBy);
            contract.setJobNumber(jobNumber);
            contract.setJobName(jobName);
            contract.setPurchaseOrderNumber(purchaseOrderNum);

        } catch (SQLException e) {
            throw new MincronException(
                    String.format("%s SQL State: %s", e.getMessage(), e.getSQLState()),
                    HttpStatus.BAD_REQUEST
            );
        } catch (Exception e) {
            throw new MincronException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return contract;
    }

    public List<ProductLineItemDTO> getContractItemList(
        String accountId,
        String contractNumber,
        String itemNumber,
        Integer startRow,
        Integer maxRows
    ) throws MincronException {
        PageDTO<ProductLineItemDTO> contractItemList = new PageDTO<>();
        contractItemList.setStartRow(startRow);

        try (
                CallBuilderConfig cbc = mcf.makeManagedCall(
                        ProgramCallNumberEnum.GET_CONTRACT_ITEM_LIST.getProgramCallNumber(),
                        14,
                        true
                )
        ) {
            cbc.setInputString(accountId);
            cbc.setInputString(contractNumber);
            cbc.setInputString(itemNumber);
            cbc.setInputInt(startRow);
            cbc.setInputInt(maxRows);

            cbc.setOutputChar();
            cbc.setOutputChar();
            cbc.setOutputDecimal();
            cbc.setOutputDecimal();

            ResponseBuilderConfig rb = cbc.getResultSet(CONTRACT_CALLS_STARTING_ROW);

            while (rb.hasMoreData()) {
                ProductLineItemDTO productLineItemDTO = new ProductLineItemDTO();
                rb.getResultString(); // orderNumber
                productLineItemDTO.setDisplayOnly(rb.getResultString());
                productLineItemDTO.setProductNumber(rb.getResultString());
                productLineItemDTO.setDescription(rb.getResultString());
                productLineItemDTO.setUnitPrice(rb.getResultString());
                productLineItemDTO.setUom(rb.getResultString());
                productLineItemDTO.setQuantityOrdered(rb.getResultString());
                productLineItemDTO.setQuantityReleasedToDate(rb.getResultString());
                productLineItemDTO.setExtendedPrice(rb.getResultString());
                productLineItemDTO.setNetPrice(rb.getResultString());
                productLineItemDTO.setOrderLineItemTypeCode(rb.getResultString());
                productLineItemDTO.setLineNumber(rb.getResultString());
                productLineItemDTO.setSequenceNumber(rb.getResultString());
                productLineItemDTO.setQuantityShipped(rb.getResultString());
                productLineItemDTO.setPricingUom(rb.getResultString());

                contractItemList.results.add(productLineItemDTO);
            }

            contractItemList.setRowsReturned(contractItemList.results.size());
        } catch (SQLException e) {
            throw new MincronException(
                    String.format("%s SQL State: %s", e.getMessage(), e.getSQLState()),
                    HttpStatus.BAD_REQUEST
            );
        } catch (Exception e) {
            throw new MincronException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return contractItemList.getResults();
    }

    public ProductDetailsResponseDTO getProductDetails(ProductDetailRequestDTO productDetailRequestDTO)throws MincronException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.ALL));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setContentLength(new Gson().toJson(productDetailRequestDTO.getItemDTO()).length());

        HttpEntity<String> request = new HttpEntity<>(new Gson().toJson(productDetailRequestDTO.getItemDTO()),httpHeaders);

        String itemDetailUrlTemplate = UriComponentsBuilder
                .fromHttpUrl(mincronHostUrl)
                .path("/itemDetail")
                .queryParam("accountId", productDetailRequestDTO.getAccountId())
                .queryParam("application",productDetailRequestDTO.getApplication())
                .queryParam("userId", productDetailRequestDTO.getUserId())
                .queryParam("url", "SHOP.FORTILINE.COM")
                .build()
                .toUriString();

        ResponseEntity<ProductDetailsResponseDTO> response = restTemplate
                .exchange(itemDetailUrlTemplate, HttpMethod.POST, request, ProductDetailsResponseDTO.class);
        if(response.getStatusCodeValue() != HttpStatus.OK.value()){
            throw new MincronException("Item details could not be found", response.getStatusCode());
        }
        return response.getBody();

    }

    public ContractCreateCartReturnTableDTO deleteCartItems(String application, String accountId, String userId, String shoppingCartId, String branchNumber)throws MincronException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.ALL));

        HttpEntity<HttpHeaders> request = new HttpEntity<>(httpHeaders);
        String updateCartUrlTemplate = UriComponentsBuilder
                .fromHttpUrl(mincronHostUrl)
                .path("/shoppingCart")
                .queryParam("accountId", accountId)
                .queryParam("application",application)
                .queryParam("userId", userId)
                .queryParam("branchNumber", branchNumber)
                .queryParam("shoppingCartId",shoppingCartId)
                .build()
                .toUriString();

            ResponseEntity<ContractCreateCartReturnTableDTO> response = restTemplate
                    .exchange(updateCartUrlTemplate, HttpMethod.DELETE, request, ContractCreateCartReturnTableDTO.class);
            if(response.getStatusCodeValue() != HttpStatus.OK.value()){
                throw new MincronException("Cart items not deleted", response.getStatusCode());
            }
            return response.getBody();

    }

    public SubmitOrderReviewResponseDTO orderReview(SubmitOrderReviewRequestDTO orderRequest) throws MincronException  {
        List<ProductLineItemDTO> actualContractItems = getContractItemList(orderRequest.getCreateCartRequest().getAccountId(), orderRequest.getCreateCartRequest().getContractNumber(), "", 1, MINCRON_MAX_ROWS_RETURNED);
        for (CartLineItemDTO cartLineItemDTO: orderRequest.getAddItemsToCart().getItems()) {
            Optional<ProductLineItemDTO> optionalProductLineItemDTO = actualContractItems.stream().filter((item) -> item.getSequenceNumber().equals(String.valueOf(cartLineItemDTO.getLineNumber()))).findFirst();
            optionalProductLineItemDTO.ifPresent(productLineItemDTO -> cartLineItemDTO.setLineNumber(Integer.valueOf(productLineItemDTO.getLineNumber())));
        }
        ContractCreateCartReturnTableDTO dto=createShoppingCart(orderRequest.getCreateCartRequest());
        LOGGER.info("Created cart. ID: "+dto.getReturnTable().toString());
        orderRequest.getCreateCartRequest().setShoppingCartId(dto.getReturnTable());
        updateShoppingCart(orderRequest.getCreateCartRequest());
        addItemsToCart(orderRequest);
        SubmitOrderReviewResponseDTO response= submitOrderForReview(orderRequest.getCreateCartRequest());
        response.getReturnTable().setShoppingCartId(orderRequest.getCreateCartRequest().getShoppingCartId());
        return response;
    }

    // GET : /shoppingCartOrderReview (AIR7062)
    public SubmitOrderReviewResponseDTO submitOrderForReview(CreateCartRequestDTO createCartRequest) throws MincronException  {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.ALL));

        HttpEntity<HttpHeaders> request = new HttpEntity<>(httpHeaders);

        val orderReviewUrlTemplate = UriComponentsBuilder.newInstance()
                .fromHttpUrl(mincronHostUrl)
                .path("/shoppingCartOrderReview")
                .queryParam("shoppingCartId", createCartRequest.getShoppingCartId())
                .queryParam("branchNumber", createCartRequest.getBranchNumber())
                .queryParam("application",createCartRequest.getApplication())
                .queryParam("shippingAddress1", createCartRequest.getShipmentDetail().getShippingAddress1())
                .queryParam("shippingAddress2", createCartRequest.getShipmentDetail().getShippingAddress2())
                .queryParam("shippingAddress3", createCartRequest.getShipmentDetail().getShippingAddress3())
                .queryParam("shippingCity",createCartRequest.getShipmentDetail().getShippingCity())
                .queryParam("shippingState",createCartRequest.getShipmentDetail().getShippingState())
                .queryParam("shippingZip",createCartRequest.getShipmentDetail().getShippingZip())
                .queryParam("shippingCountry",createCartRequest.getShipmentDetail().getShippingCountry())
                .queryParam("shippingTaxJurisdiction",createCartRequest.getShipmentDetail().getShippingTaxJurisdiction())
                .queryParam("shipMethod",createCartRequest.getShipmentDetail().getShipMethod())
                .build()
                .toUriString();

            ResponseEntity<SubmitOrderReviewResponseDTO> response = restTemplate
                    .exchange(orderReviewUrlTemplate, HttpMethod.GET, request, SubmitOrderReviewResponseDTO.class);
            if(response.getStatusCodeValue() != HttpStatus.OK.value()){
                throw new MincronException("Review order failed", response.getStatusCode());
            }
            return response.getBody();
    }

    // PUT : /shoppingCartItem (AIR7023)
    public ContractCreateCartReturnTableDTO addItemsToCart(SubmitOrderReviewRequestDTO orderRequest) throws MincronException  {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.ALL));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setContentLength(new Gson().toJson(orderRequest.getAddItemsToCart()).length());

        HttpEntity<String> request = new HttpEntity<>(new Gson().toJson(orderRequest.getAddItemsToCart()),httpHeaders);

        String addItemToCartUrl = UriComponentsBuilder
                .fromHttpUrl(mincronHostUrl)
                .path("/shoppingCartItem")
                .queryParam("application", orderRequest.getCreateCartRequest().getApplication())
                .queryParam("shoppingCartId", orderRequest.getCreateCartRequest().getShoppingCartId())
                .queryParam("rePrice", orderRequest.getCreateCartRequest().getRePrice())
                .build()
                .toUriString();

            ResponseEntity<ContractCreateCartReturnTableDTO>  response=restTemplate.exchange(addItemToCartUrl, HttpMethod.PUT, request, ContractCreateCartReturnTableDTO.class);
            if(response.getStatusCodeValue() != HttpStatus.OK.value()){
                throw new MincronException("Cart items not added", response.getStatusCode());
            }
            return response.getBody();
    }

    // POST : /shoppingCart  (AIR7022)
    public ContractCreateCartReturnTableDTO updateShoppingCart(CreateCartRequestDTO createCartRequest) throws MincronException  {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.ALL));

        HttpEntity<HttpHeaders> request = new HttpEntity<>(httpHeaders);
        String updateCartUrlTemplate = UriComponentsBuilder.newInstance()
                .fromHttpUrl(mincronHostUrl)
                .path("/shoppingCart")
                .queryParam("accountId", createCartRequest.getAccountId())
                .queryParam("contractNumber", createCartRequest.getContractNumber())
                .queryParam("application",createCartRequest.getApplication())
                .queryParam("userId", createCartRequest.getUserId())
                .queryParam("jobName", createCartRequest.getJobName())
                .queryParam("jobNumber", createCartRequest.getJobNumber())
                .queryParam("shoppingCartId",createCartRequest.getShoppingCartId())
                .build()
                .toUriString();

            ResponseEntity<ContractCreateCartReturnTableDTO> response= restTemplate
                    .exchange(updateCartUrlTemplate, HttpMethod.POST, request, ContractCreateCartReturnTableDTO.class);
            if(response.getStatusCodeValue() != HttpStatus.OK.value()){
                throw new MincronException("Update cart failed", response.getStatusCode());
            }
            return response.getBody();
    }

    // PUT : /shoppingCart (AIR7020)  Create new shopping cart Id
    public ContractCreateCartReturnTableDTO createShoppingCart(CreateCartRequestDTO createCartRequest) throws MincronException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.ALL));
        HttpEntity<HttpHeaders> request = new HttpEntity<>(httpHeaders);

        String createCartUrlTemplate = UriComponentsBuilder
                .fromHttpUrl(mincronHostUrl)
                .path("/shoppingCart")
                .queryParam("accountId", createCartRequest.getAccountId())
                .queryParam("contractNumber", createCartRequest.getContractNumber())
                .queryParam("application",createCartRequest.getApplication())
                .queryParam("userId", createCartRequest.getUserId())
                .queryParam("jobNumber", createCartRequest.getJobNumber())
                .build()
                .toUriString();

            ResponseEntity<ContractCreateCartReturnTableDTO>  response=restTemplate.exchange(createCartUrlTemplate, HttpMethod.PUT, request, ContractCreateCartReturnTableDTO.class);
            if(response.getStatusCodeValue() != HttpStatus.OK.value()){
                throw new MincronException("Cart creation failed", response.getStatusCode());
            }
            return response.getBody();
    }

    // PUT : /shoppingCartOrder (AIR7100)
    public ContractCreateCartReturnTableDTO submitContractToReleaseOrder(SubmitOrderRequestDTO submitOrderRequest, String application, String accountId, String userId, String shoppingCartId) throws MincronException {
        if (submitOrderRequest.getPromiseDate() != null && !submitOrderRequest.getPromiseDate().trim().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat(SUBMIT_ORDER_DTO_DATE_FORMAT);
            try {
                Date date = sdf.parse(submitOrderRequest.getPromiseDate().trim());
                sdf.applyPattern(PROMISE_DATE_FORMAT);
                submitOrderRequest.setPromiseDate(sdf.format(date));
            } catch (ParseException e) {
                // log an error if promise date given throws a parse exception.  Branch Managers can fix this date after creating the order, so it shouldn't block order submission.
                LOGGER.error("Unable to parse Contract promise date: {}", submitOrderRequest.getPromiseDate().trim());
            }
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.ALL));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setContentLength(new Gson().toJson(submitOrderRequest).length());

        HttpEntity<String> request = new HttpEntity<>(new Gson().toJson(submitOrderRequest),httpHeaders);
        String addItemToCartUrl = UriComponentsBuilder
                .fromHttpUrl(mincronHostUrl)
                .path("/shoppingCartOrder")
                .queryParam("application", application)
                .queryParam("shoppingCartId", shoppingCartId)
                .queryParam("userId", userId)
                .queryParam("accountId", accountId)
                .build()
                .toUriString();

            ResponseEntity<ContractCreateCartReturnTableDTO>  response =restTemplate.exchange(addItemToCartUrl, HttpMethod.PUT, request, ContractCreateCartReturnTableDTO.class);
            if(response.getStatusCodeValue() != HttpStatus.OK.value()){
                throw new MincronException("There was a problem creating the order: No Order Number returned from Kerridge", response.getStatusCode());
            }
            return response.getBody();
    }

    private static boolean isZeroDate(String date) {
        val m = ALL_ZEROES_PATTERN.matcher(date);
        return m.matches();
    }
}
