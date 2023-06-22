package com.reece.platform.mincron.controller;

import com.reece.platform.mincron.exceptions.MincronException;
import com.reece.platform.mincron.model.*;
import com.reece.platform.mincron.model.contracts.*;
import com.reece.platform.mincron.model.common.PageDTO;
import com.reece.platform.mincron.model.common.ProductLineItemDTO;
import com.reece.platform.mincron.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("contracts")
public class ContractController {

    @Autowired
    private ContractService contractService;

    @Autowired
    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    /**
     *
     * @param accountId bill-to account Id
     * @param startRow first row to retrieve from list of contracts
     * @param maxRows the maximum amount of rows to return from list of contracts
     * @param searchFilter query string used to filter results
     * @param fromDate if specified, contracts from this date on will be returned
     * @param toDate if specified, contracts up to this date will be returned
     * @param sortOrder used to define which data value to sort by
     * @param sortDirection used to specify whether results should be returned in ascending or descending order
     * @return list of contracts for given bill-to account Id
     * @throws MincronException exception thrown when there is any error in the mincron server
     */
    @GetMapping
    public @ResponseBody
    PageDTO<ContractListDTO> getContractList(
            @RequestParam String accountId,
            @RequestParam(defaultValue = "1") Integer startRow,
            @RequestParam(defaultValue = "10") Integer maxRows,
            @RequestParam(defaultValue = "") String searchFilter,
            @RequestParam(defaultValue = "") String fromDate,
            @RequestParam(defaultValue = "") String toDate,
            @RequestParam(defaultValue = "lastRelease") String sortOrder,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) throws MincronException {
        PageDTO<ContractListDTO> contractPage = contractService.getContractList(
                accountId,
                startRow,
                maxRows,
                searchFilter,
                fromDate,
                toDate,
                sortOrder,
                sortDirection
        );
        return contractPage;
    }

    /**
     * Retrieve contract header for a given contract number.
     * @param contractNumber contract number
     * @return contract header
     * @throws MincronException if there is an internal server error when making API request
     */
    @GetMapping("/contract-header")
    public @ResponseBody
    ContractDTO getContractHeader(@RequestParam String contractNumber) throws MincronException {
        ContractDTO contractHeader = contractService.getContractHeader(contractNumber);
        return contractHeader;
    }

    /**
     * Returns a list of products for a given contract.
     * @param accountId account id
     * @param contractNumber contract number
     * @param itemNumber optional item number param if needing to find a specific item
     * @param startRow indicates which page to retrieve results from (if not provided the Websmart API will default to 1)
     * @param maxRows Indicates how many rows to retrieve per page (Required by the Websmart API)
     * @return returns a list of products for a given contract
     * @throws MincronException if there is an internal server error when making API request
     */
    @GetMapping("/contract-item-list")
    public @ResponseBody
    List<ProductLineItemDTO> getContractItemList(
            @RequestParam String accountId,
            @RequestParam String contractNumber,
            @RequestParam(defaultValue = "") String itemNumber,
            @RequestParam(defaultValue = "1") Integer startRow,
            @RequestParam(defaultValue = "10") Integer maxRows
            )
            throws MincronException {
        List<ProductLineItemDTO> contractItemList = contractService.getContractItemList(accountId, contractNumber, itemNumber, startRow, maxRows);
        return contractItemList;
    }

    /**
     * Retrieve tax information by submitting order review
     * @requestBody Needs contract details and product details
     * @return Order details for given cart and contract
     * @throws MincronException if there is an internal server error when making API request
     */
    @PostMapping("orders/review")
    public @ResponseBody SubmitOrderReviewResponseDTO submitOrderReview(@RequestBody SubmitOrderReviewRequestDTO submitOrderReviewRequest) throws MincronException {
        SubmitOrderReviewResponseDTO orderReviewResponse = contractService.orderReview(submitOrderReviewRequest);
        return orderReviewResponse;
    }


    /**
     * Submit order from cart
     * @requestBody Needs contract details and product details
     * @return Order number
     * @throws MincronException if there is an internal server error when making API request
     */
    @PostMapping("orders/submit")
    public  @ResponseBody ContractCreateCartReturnTableDTO submitContractToReleaseOrder(@RequestBody SubmitOrderRequestDTO submitOrderRequest,
                                                              @RequestParam String application,
                                                              @RequestParam String accountId,
                                                              @RequestParam String userId,
                                                              @RequestParam String shoppingCartId) throws MincronException {
        ContractCreateCartReturnTableDTO orderNumber = contractService.submitContractToReleaseOrder(submitOrderRequest,application,accountId,userId,shoppingCartId);
        return orderNumber;
    }

    /**
     * Delete cart items
     * @RequestParam Needs cart details
     * @return boolean
     * @throws MincronException if there is an internal server error when making API request
     */
    @DeleteMapping("orders/delete-cart")
    public @ResponseBody ContractCreateCartReturnTableDTO deleteCart(@RequestParam String application,
                                            @RequestParam String accountId,
                                            @RequestParam String userId,
                                            @RequestParam String shoppingCartId,
                                            @RequestParam String branchNumber) throws MincronException {
        return contractService.deleteCartItems(application,accountId,userId,shoppingCartId,branchNumber);
    }

    /**
     * get item availability details
     * @RequestParam Needs item number and branch details
     * @return item availability count
     * @throws MincronException if there is an internal server error when making API request
     */
    @PostMapping("orders/product-details")
    public @ResponseBody ProductDetailsResponseDTO getProductDetails(@RequestBody ProductDetailRequestDTO productDetailRequestDTO) throws MincronException {
        return contractService.getProductDetails(productDetailRequestDTO);
    }

}
