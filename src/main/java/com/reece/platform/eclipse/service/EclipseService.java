package com.reece.platform.eclipse.service;

import com.reece.platform.eclipse.dto.*;
import com.reece.platform.eclipse.dto.productsearch.request.ProductSearchRequestDTO;
import com.reece.platform.eclipse.dto.productsearch.response.ProductSearchResponseDTO;
import com.reece.platform.eclipse.dto.productsearch.response.ProductSearchResult;
import com.reece.platform.eclipse.dto.productsearch.response.ProductWebReference;
import com.reece.platform.eclipse.enums.SearchInputTypeEnum;
import com.reece.platform.eclipse.enums.SearchTypeEnum;
import com.reece.platform.eclipse.exceptions.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
@RequiredArgsConstructor
public class EclipseService extends BaseEclipseService {

    private final EclipseSessionService eclipseSessionService;

    /**
     * Verify Credentials by creating Session Token for Eclipse EPICOR
     */

    /**
     * Query Eclipse API to fetch all orders ready for picking for a specific branch
     * @param branchId
     * @param userId
     * @param orderType
     * @return
     * @throws EclipseTokenException
     */
    public PickingTasksResponseDTO getPickingTasks(String branchId, String userId, OrderMode orderType)
        throws EclipseTokenException {
        var request = new HttpEntity<>(getEclipseHttpSessionHeaders());

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(eclipseApiEndpoint)
            .path("WarehouseTasks/")
            .path("PickTasks")
            .queryParam("branchId", "{branchId}")
            .queryParam("userId", "{userId}")
            .queryParam("orderType", "{orderType}")
            .encode()
            .toUriString();

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("branchId", branchId);
        queryParams.put("userId", userId);
        queryParams.put("orderType", orderType.getValue());

        ResponseEntity<PickingTasksResponseDTO> response;

        try {
            response =
                restTemplate.exchange(urlTemplate, HttpMethod.GET, request, PickingTasksResponseDTO.class, queryParams);
        } catch (Exception e) {
            //TODO: check & catch exceptions
            throw e;
        }
        return response.getBody();
    }

    /**
     * Assign a pick task to a user via Eclipse API
     * @param list
     * @return
     * @throws EclipseTokenException
     */
    public WarehousePickTaskList assignPickingTasks(WarehousePickTaskList list) throws EclipseTokenException {
        String assignPickingTaskUri = UriComponentsBuilder
            .fromHttpUrl(eclipseApiEndpoint)
            .path("WarehouseTasks/")
            .path("PickTasks")
            .toUriString();

        HttpHeaders httpHeaders = getEclipseHttpSessionHeaders();

        HttpEntity<WarehousePickTaskList> eclipseRequest = new HttpEntity<>(list, httpHeaders);

        ResponseEntity<WarehousePickTaskList> response;

        try {
            response =
                restTemplate.exchange(
                    assignPickingTaskUri,
                    HttpMethod.PUT,
                    eclipseRequest,
                    WarehousePickTaskList.class
                );
        } catch (Exception e) {
            //TODO: check & catch exceptions
            throw e;
        }
        return response.getBody();
    }

    public WarehouseTaskUserPicksDTO getUserPicks(String branchId, String userId)
        throws EclipseTokenException, EclipseException {
        HttpHeaders httpHeaders = getEclipseHttpSessionHeaders();

        HttpEntity<HttpHeaders> eclipseRequest = new HttpEntity<>(httpHeaders);

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(eclipseApiEndpoint)
            .path("WarehouseTasks/")
            .path("UserPick")
            .queryParam("branchId", branchId)
            .queryParam("userId", userId)
            .encode()
            .toUriString();

        ResponseEntity<WarehouseTaskUserPicksDTO> response;

        try {
            response =
                restTemplate.exchange(urlTemplate, HttpMethod.GET, eclipseRequest, WarehouseTaskUserPicksDTO.class);
        } catch (Exception e) {
            //TODO: check & catch exceptions
            log.error(
                "Got an exception while calling getUserPicks for request in wms-eclipse-core-service " + urlTemplate
            );
            log.error("The exception is : " + e.getMessage());
            throw new EclipseException("Exception in getUserPicks " + e.getMessage());
        }
        return response.getBody();
    }

    /**
     * Get Serial Number for Product
     * @param warehouseId
     * @return
     * @throws ParseException
     * @throws InvalidSerializedProductException
     */
    public ProductSerialNumbersResponseDTO getSerialNumbers(String warehouseId)
        throws InvalidSerializedProductException, EclipseTokenException {
        var request = new HttpEntity<>(getEclipseHttpSessionHeaders());

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(eclipseApiEndpoint)
            .path("WarehouseTasks/")
            .path("SerialNumbers")
            .queryParam("warehouseId", warehouseId)
            .encode()
            .toUriString();

        ResponseEntity<ProductSerialNumbersResponseDTO> response;

        try {
            response =
                restTemplate.exchange(urlTemplate, HttpMethod.GET, request, ProductSerialNumbersResponseDTO.class);
        } catch (Exception e) {
            if (e.getMessage().contains("This is not a serialized product")) {
                throw new InvalidSerializedProductException();
            }
            throw e;
        }
        return response.getBody();
    }

    /**
     * Update Serial Numbers with Eclipse REST API
     * @param warehouseId
     * @param serialNumbers
     * @return
     * @throws EclipseTokenException
     */
    public ProductSerialNumbersResponseDTO updateSerialNumbers(
        String warehouseId,
        WarehouseSerialNumbers serialNumbers
    ) throws InvalidSerializedProductException, EclipseTokenException {
        HttpHeaders httpHeaders = getEclipseHttpSessionHeaders();
        HttpEntity<WarehouseSerialNumbers> eclipseRequest = new HttpEntity<>(serialNumbers, httpHeaders);

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(eclipseApiEndpoint)
            .pathSegment("WarehouseTasks", "SerialNumbers", warehouseId)
            .build()
            .toUriString();

        ResponseEntity<ProductSerialNumbersResponseDTO> response;

        try {
            response =
                restTemplate.exchange(
                    urlTemplate,
                    HttpMethod.PUT,
                    eclipseRequest,
                    ProductSerialNumbersResponseDTO.class
                );
        } catch (Exception e) {
            if (e.getMessage().contains("This is not a serialized product")) {
                throw new InvalidSerializedProductException();
            }
            throw e;
        }
        return response.getBody();
    }

    /**
     * Complete a User Pick
     * @param pickId
     * @param userPick
     * @return
     * @throws PickNotFoundException
     * @throws InvalidToteException
     * @throws EclipseTokenException
     */
    public WarehousePickComplete completeUserPick(String pickId, WarehousePickComplete userPick)
        throws PickNotFoundException, InvalidToteException, EclipseTokenException {
        HttpHeaders httpHeaders = getEclipseHttpSessionHeaders();
        HttpEntity<WarehousePickComplete> eclipseRequest = new HttpEntity<>(userPick, httpHeaders);
        ResponseEntity<WarehousePickComplete> response;

        String pickingUri = UriComponentsBuilder
            .fromHttpUrl(eclipseApiEndpoint)
            .path("WarehouseTasks/")
            .path("UserPick/")
            .path(pickId)
            .toUriString();

        try {
            String uriWithCarrot = pickingUri.replace("%5E", "^");
            String warehouseId = userPick.getWarehouseID().replace("%5E", "^");
            userPick.setWarehouseID(warehouseId);
            response =
                restTemplate.exchange(uriWithCarrot, HttpMethod.PUT, eclipseRequest, WarehousePickComplete.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getMessage() != null && e.getMessage().contains("Pick Not Found.")) {
                throw new PickNotFoundException(pickId);
            } else if (e.getMessage() != null && e.getMessage().contains("Tote")) {
                throw new InvalidToteException();
            }
            throw e;
        } catch (Exception e) {
            log.error("Error in completeUserPick" + e.getMessage());
            throw e;
        }
    }

    /**
     * Close Task
     * @param closeTask
     * @return
     * @throws EclipseTokenException
     */
    public WarehouseCloseTask closeTask(WarehouseCloseTask closeTask) throws EclipseTokenException {
        HttpHeaders httpHeaders = getEclipseHttpSessionHeaders();
        HttpEntity<WarehouseCloseTask> eclipseRequest = new HttpEntity<>(closeTask, httpHeaders);

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(eclipseApiEndpoint)
            .pathSegment("WarehouseTasks", "CloseTask", closeTask.getInvoiceNumber())
            .encode()
            .toUriString();

        ResponseEntity<WarehouseCloseTask> response;

        try {
            response = restTemplate.exchange(urlTemplate, HttpMethod.PUT, eclipseRequest, WarehouseCloseTask.class);
        } catch (Exception e) {
            throw e;
        }
        return response.getBody();
    }

    /**
     * Update Tote Task
     * @param toteTask
     * @return
     * @throws EclipseTokenException
     * @throws ToteUnavailableException
     * @throws InvalidToteException
     */
    public WarehouseToteTask updateToteTask(WarehouseToteTask toteTask)
        throws EclipseTokenException, ToteUnavailableException, InvalidToteException {
        HttpHeaders httpHeaders = getEclipseHttpSessionHeaders();
        HttpEntity<WarehouseToteTask> eclipseRequest = new HttpEntity<>(toteTask, httpHeaders);

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(eclipseApiEndpoint)
            .pathSegment("WarehouseTasks", "ToteTask", toteTask.getTote())
            .encode()
            .toUriString();

        ResponseEntity<WarehouseToteTask> response;

        try {
            response = restTemplate.exchange(urlTemplate, HttpMethod.PUT, eclipseRequest, WarehouseToteTask.class);
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("not available to be staged")) {
                throw new ToteUnavailableException(toteTask.getTote());
            } else if (e.getMessage() != null && e.getMessage().contains("is not in file")) {
                throw new InvalidToteException();
            }
            throw e;
        }
        return response.getBody();
    }

    /**
     * Update Tote
     * @param totePackages
     * @return
     * @throws EclipseTokenException
     * @throws ToteLockedException
     * @throws InvalidToteException
     */
    public WarehouseTotePackages updateTotePackages(WarehouseTotePackages totePackages)
        throws EclipseTokenException, ToteLockedException, InvalidToteException {
        HttpHeaders httpHeaders = getEclipseHttpSessionHeaders();
        HttpEntity<WarehouseTotePackages> eclipseRequest = new HttpEntity<>(totePackages, httpHeaders);

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(eclipseApiEndpoint)
            .pathSegment("WarehouseTasks", "TotePackages", totePackages.getTote())
            .encode()
            .toUriString();

        ResponseEntity<WarehouseTotePackages> response;

        try {
            response = restTemplate.exchange(urlTemplate, HttpMethod.PUT, eclipseRequest, WarehouseTotePackages.class);
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("is currently Locked")) {
                throw new ToteLockedException(totePackages.getTote());
            } else if (e.getMessage() != null && e.getMessage().contains("is not in file")) {
                throw new InvalidToteException();
            }
            throw e;
        }
        return response.getBody();
    }

    /**
     * Get a product image url
     * @param productId
     * @return
     * @throws EclipseTokenException
     * @throws ProductImageUrlNotFoundException
     */
    public String getProductImageUrl(String productId) throws EclipseTokenException, ProductImageUrlNotFoundException {
        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(eclipseApiEndpoint)
            .path("Products/")
            .path(productId)
            .path("/ImageUrl")
            .queryParam("thumbnail", true)
            .queryParam("keyword", true)
            .encode()
            .toUriString();

        EclipseRestSessionDTO sessionToken = eclipseSessionService
            .getSessionToken()
            .orElseThrow(EclipseTokenException::new);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "SessionToken " + sessionToken.getSessionToken());
        httpHeaders.set("Connection", "keep-alive");

        HttpEntity<HttpHeaders> eclipseRequest = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(urlTemplate, HttpMethod.GET, eclipseRequest, String.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ProductImageUrlNotFoundException();
            }
            throw e;
        }

        return response.getBody();
    }

    /**
     * Get Customer By ID
     * @param customerId
     * @return
     * @throws EclipseTokenException
     * @throws CustomerNotFoundException
     */
    public Customer getCustomerById(String customerId) throws EclipseTokenException, CustomerNotFoundException {
        HttpHeaders httpHeaders = getEclipseHttpSessionHeaders();
        HttpEntity<HttpHeaders> eclipseRequest = new HttpEntity<>(httpHeaders);

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(eclipseApiEndpoint)
            .pathSegment("Customers", customerId)
            .encode()
            .toUriString();

        ResponseEntity<Customer> response;

        try {
            response = restTemplate.exchange(urlTemplate, HttpMethod.GET, eclipseRequest, Customer.class);
        } catch (HttpClientErrorException e) {
            throw new CustomerNotFoundException(customerId);
        } catch (Exception e) {
            throw e;
        }

        return response.getBody();
    }

    /**
     * Get Product by Id
     * @param productId
     * @return
     */
    public ProductDetailsDTO getProduct(String productId) throws EclipseTokenException {
        HttpHeaders httpHeaders = getEclipseHttpSessionHeaders();
        HttpEntity<HttpHeaders> eclipseRequest = new HttpEntity<>(httpHeaders);

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(eclipseApiEndpoint)
            .pathSegment("Products", productId)
            .encode()
            .toUriString();

        ResponseEntity<ProductDetailsDTO> response;

        try {
            response = restTemplate.exchange(urlTemplate, HttpMethod.GET, eclipseRequest, ProductDetailsDTO.class);
        } catch (HttpClientErrorException e) {
            throw new ProductNotFoundException(productId);
        } catch (Exception e) {
            throw e;
        }

        return response.getBody();
    }

    /**
     * ERPCP-1275 -- Start
     * Query Eclipse API for product search results
     * @param request
     * @return
     * @throws EclipseTokenException
     */
    public ProductSearchResponseDTO getProductSearch(ProductSearchRequestDTO request) throws EclipseTokenException {
        final int DEFAULT_PAGE_SIZE = 8;
        final int DEFAULT_CURRENT_PAGE = 1;

        int searchMode = SearchTypeEnum.ALL_PRIMARY.getValue();
        String keyword = request.getSearchTerm();
        Integer pageSize = request.getPageSize();
        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        Integer currentPage = request.getCurrentPage();
        if (currentPage == null || currentPage < 1) {
            currentPage = DEFAULT_CURRENT_PAGE;
        }
        int startIndex = pageSize * (currentPage - 1) + 1; // sets start index at beginning of page, eclipse products are index 1

        boolean includeTotalItems = true;

        int searchInputType = request.getSearchInputType();

        String updatedAfter; //Unused for now

        String searchUri = String.format("%s%s", eclipseApiEndpoint, "/Products");

        HttpHeaders httpHeaders = getEclipseHttpSessionHeaders();

        HttpEntity<HttpHeaders> eclipseRequest = new HttpEntity<>(httpHeaders);

        // Omitting ID query since this is covered in keyword
        UriComponentsBuilder partialUri = UriComponentsBuilder
            .fromHttpUrl(searchUri)
            .queryParam("searchMode", "{searchMode}")
            .queryParam("keyword", "{keyword}") // This field is required even if ID is supplied, in which case the value does not matter
            .queryParam("startIndex", "{startIndex}")
            .queryParam("pageSize", "{pageSize}")
            .queryParam("includeTotalItems", "{includeTotalItems}");
        //                .queryParam("updatedAfter", "updatedAfter")

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("searchMode", searchMode);
        queryParams.put("keyword", keyword);
        queryParams.put("startIndex", startIndex);
        queryParams.put("pageSize", pageSize);
        queryParams.put("includeTotalItems", includeTotalItems);

        if (searchInputType == SearchInputTypeEnum.ID.getValue()) {
            partialUri = partialUri.queryParam("id", "{id}");
            queryParams.put("id", keyword);
        }

        String urlTemplate = partialUri.encode().toUriString();

        ResponseEntity<ProductSearchResponseDTO> response;
        ProductSearchResponseDTO results;

        response =
            restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                eclipseRequest,
                ProductSearchResponseDTO.class,
                queryParams
            );
        results = response.getBody();
        for (ProductSearchResult result : results.getResults()) {
            var imageUrl = getProductImageUrlOrNull(Integer.toString(result.getId()));
            ProductWebReference pwr = new ProductWebReference();
            pwr.setWebReferenceId("THUMB");
            pwr.setWebReferenceDescription("THUMBNAIL");
            pwr.setWebReferenceParameters(imageUrl);
            result.setWebReferences(Collections.singletonList(pwr));
        }
        return response.getBody();
    }

    public String getProductImageUrlOrNull(String productId) {
        try {
            return getProductImageUrl(productId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * ERPCP-1275 -- End
     */

    public BranchValidationDTO validateBranch(String branchId) throws EclipseTokenException {
        HttpHeaders httpHeaders = getEclipseHttpSessionHeaders();
        HttpEntity<HttpHeaders> eclipseRequest = new HttpEntity<>(httpHeaders);

        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(eclipseApiEndpoint)
            .pathSegment("Branches", branchId)
            .encode()
            .toUriString();

        ResponseEntity<EpicoreBranchResponse> response;

        try {
            response = restTemplate.exchange(urlTemplate, HttpMethod.GET, eclipseRequest, EpicoreBranchResponse.class);
        } catch (HttpClientErrorException e) {
            return BranchValidationDTO.builder().isValid(Boolean.FALSE).build();
        } catch (Exception e) {
            throw e;
        }
        EpicoreBranchResponse epicoreBranchResponse = response.getBody();
        return BranchValidationDTO
            .builder()
            .isValid(Boolean.TRUE)
            .branch(
                EclipseBranchDetails
                    .builder()
                    .branchId(epicoreBranchResponse.getId())
                    .branchName(epicoreBranchResponse.getLongDescription())
                    .build()
            )
            .build();
    }

    private HttpHeaders getEclipseHttpSessionHeaders() throws EclipseTokenException {
        EclipseRestSessionDTO sessionToken = eclipseSessionService
            .getSessionToken()
            .orElseThrow(EclipseTokenException::new);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "SessionToken " + sessionToken.getSessionToken());
        httpHeaders.setAccept(Collections.singletonList(MediaType.ALL));
        httpHeaders.set("Connection", "keep-alive");
        return httpHeaders;
    }
}
