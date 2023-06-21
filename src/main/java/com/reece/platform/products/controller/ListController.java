package com.reece.platform.products.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.bean.CsvToBeanBuilder;
import com.reece.platform.products.constants.ListConstants;
import com.reece.platform.products.exceptions.*;
import com.reece.platform.products.model.DTO.*;
import com.reece.platform.products.model.UploadListData;
import com.reece.platform.products.service.AuthorizationService;
import com.reece.platform.products.service.ListService;
import com.reece.platform.products.utilities.StringUtilities;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@Slf4j
@RequestMapping("/lists")
public class ListController {

    private final AuthorizationService authorizationService;
    private final ListService listService;

    @Autowired
    public ListController(AuthorizationService authorizationService, ListService listService) {
        this.authorizationService = authorizationService;
        this.listService = listService;
    }

    /**
     * Fetches a single list
     * @param listId id of list to fetch
     * @return a list with all of its products
     * @throws ListNotFoundException thrown when given listId does not exist
     */
    @PostMapping("{listId}")
    public @ResponseStatus ResponseEntity<ListDTO> getList(
        @PathVariable UUID listId,
        @RequestParam String branchId,
        @RequestParam(name = "erpAccountId", required = false) String erpAccountId,
        @RequestHeader(name = "authorization") String authorization
    ) throws ListNotFoundException, ElasticsearchException {
        if (authorization == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        var list = listService.getList(listId, branchId);
        if (erpAccountId != null && !erpAccountId.isEmpty()) {
            try {
                list
                    .getListLineItems()
                    .forEach(p -> p.setCustomerPartNumbers(p.getCustomerPartNumbers(), erpAccountId));
            } catch (Exception e) {}
        }
        return ResponseEntity.ok(list);
    }

    @PostMapping("exportListToCSV/{listId}")
    public @ResponseStatus ResponseEntity<ExportListDTO> exportListIntoCSV(
        @PathVariable UUID listId,
        @RequestParam String branchId,
        @RequestHeader(name = "authorization") String authorization
    ) throws ListNotFoundException, ElasticsearchException, ExportListIntoCSVFoundException {
        if (authorization == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        var list = listService.getList(listId, branchId);
        List<UploadListData> csvData = listService.exportListIntoCSV(list);
        return ResponseEntity.ok(new ExportListDTO(csvData));
    }

    /**
     * Fetches all the lists for a billToAccountId
     * @param billToAccountId
     * @return all the lists for that account
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<ListDTO> getLists(@RequestParam UUID billToAccountId) {
        return listService.getListsByBillToAccountId(billToAccountId);
    }

    @PostMapping("/toggle")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ToggleListItemResponseDTO toggleListItem(
        @RequestBody ToggleListLineItemDTO toggleListLineItemDTO
    ) throws AddItemToListFoundException {
        return listService.toggleItemFromLists(toggleListLineItemDTO);
    }

    @PostMapping
    public @ResponseStatus ResponseEntity<ListDTO> createList(
        @RequestBody CreateListDTO createListDTO,
        @RequestHeader(name = "authorization") String authorization
    ) {
        if (!authorizationService.userCanEditList(authorization)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        var list = listService.createList(createListDTO);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // ADD item to list
    @PostMapping("{listId}/items")
    public ResponseEntity<UUID> addItemToList(
        @PathVariable UUID listId,
        @RequestBody ListLineItemDTO listLineItemDTO,
        @RequestHeader(name = "authorization") String authorization
    ) throws ItemAlreadyInListException, ListNotFoundException, AddItemToListFoundException {
        if (!authorizationService.userCanEditList(authorization)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        var list = listService.addItemToList(listId, listLineItemDTO);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // ADD all cart items to new list
    @PostMapping("/addCartItemsToNewList/{accountId}/{cartId}")
    public ResponseEntity<UUID> addAllCartItemsToNewList(
        @PathVariable UUID accountId,
        @PathVariable UUID cartId,
        @RequestParam String name,
        @RequestHeader(name = "authorization") String authorization
    )
        throws ItemAlreadyInListException, ListNotFoundException, AddItemToListFoundException, CartNotFoundException, AddItemsToCartFoundException {
        if (!authorizationService.userCanEditList(authorization)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        var list = listService.addAllCartItemsToNewList(name, cartId, accountId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // ADD all cart items to existing lists
    @PostMapping("/addCartItemsToExistingLists/{cartId}")
    public ResponseEntity<List<UUID>> addAllCartItemsToExistingLists(
        @RequestBody List<String> listIds,
        @PathVariable UUID cartId,
        @RequestHeader(name = "authorization") String authorization
    )
        throws ItemAlreadyInListException, ListNotFoundException, AddItemToListFoundException, CartNotFoundException, AddItemsToCartFoundException {
        if (!authorizationService.userCanEditList(authorization)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        var list = listService.addAllCartItemsToLists(cartId, listIds);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // UPDATE list
    @PutMapping
    public @ResponseStatus ResponseEntity<ListDTO> updateList(
        @RequestBody ListDTO updateListDTO,
        @RequestHeader(name = "authorization") String authorization
    ) throws ListNotFoundException {
        if (!authorizationService.userCanEditList(authorization)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        var list = listService.updateList(updateListDTO);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/_syncFromInsite")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody WishListSyncResponseDTO syncListsFromInsite(
        @RequestBody SyncListsFromInsiteDTO syncListsFromInsiteDTO
    ) {
        return listService.syncListsFromInsite(
            syncListsFromInsiteDTO.getErpAccountId(),
            syncListsFromInsiteDTO.getErpEnum()
        );
    }

    @DeleteMapping("{listId}")
    public @ResponseStatus ResponseEntity<DeleteListResponseDTO> deleteList(
        @PathVariable UUID listId,
        @RequestHeader(name = "authorization") String authorization
    ) throws ListNotFoundException {
        if (!authorizationService.userCanEditList(authorization)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        DeleteListResponseDTO response = new DeleteListResponseDTO(listId, listService.deleteListById(listId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/upload")
    public @ResponseStatus ResponseEntity<ListUploadResponseDTO> uploadNewList(
        @RequestParam("file") MultipartFile file,
        @RequestParam("name") String name,
        @RequestParam("billToAccountId") UUID billToAccountId,
        @RequestHeader(name = "authorization") String authorization
    ) throws ErrorsFoundInFileException, BranchServiceCoreException {
        if (!authorizationService.userCanEditList(authorization)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        if (file.isEmpty()) {
            return new ResponseEntity<>(new ListUploadResponseDTO(List.of(), 0, null), HttpStatus.OK);
        } else {
            List<String> errors = new ArrayList<>();
            if (listService.isDuplicate(name, billToAccountId)) {
                errors.add("A list with name " + name + " already exists");
                throw new ErrorsFoundInFileException(errors);
            }
            try (
                Reader reader = new BufferedReader(
                    new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)
                );
                CSVReaderHeaderAware headerAware = new CSVReaderHeaderAware(
                    new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))
                );
            ) {
                CsvToBeanBuilder<UploadListData> uploadListDataParser = new CsvToBeanBuilder<>(reader);
                uploadListDataParser.withType(UploadListData.class);
                uploadListDataParser.withIgnoreLeadingWhiteSpace(true);
                uploadListDataParser.withThrowExceptions(false);

                List<String> currentHeadersList = null;
                try {
                    currentHeadersList = headerAware.readMap().keySet().stream().collect(Collectors.toList());
                } catch (Exception e) {
                    log.error("Error reading list upload file: " + e);
                }
                var predefinedHeadersList = getPredefinedHeaders();
                if (
                    currentHeadersList == null ||
                    currentHeadersList.isEmpty() ||
                    !currentHeadersList.containsAll(predefinedHeadersList)
                ) {
                    errors.add(ListConstants.INCORRECT_COLUMN_HEADERS);
                    throw new ErrorsFoundInFileException(errors);
                }
                var listParser = uploadListDataParser.build().parse();
                if (listParser.size() > listService.MAX_LIST_SIZE) {
                    throw new AddItemToListFoundException();
                }

                listService.checkFile(listParser, errors);
                if (!errors.isEmpty()) {
                    throw new ErrorsFoundInFileException(errors);
                }

                return new ResponseEntity<>(
                    listService.handleUploadNewList(listParser, billToAccountId, name),
                    HttpStatus.OK
                );
            } catch (IOException e) {
                log.error("Error reading list upload file: " + e);
                return new ResponseEntity<>(
                    new ListUploadResponseDTO(List.of(), 0, null),
                    HttpStatus.INTERNAL_SERVER_ERROR
                );
            } catch (AddItemToListFoundException e) {
                errors.add(e.getMessage());
                throw new ErrorsFoundInFileException(errors);
            } catch (RuntimeException e) {
                log.debug("Formatting error in uploaded list: " + e);
                return new ResponseEntity<>(new ListUploadResponseDTO(List.of(), 0, null), HttpStatus.BAD_REQUEST);
            }
        }
    }

    @PostMapping("{listId}/upload")
    public @ResponseStatus ResponseEntity<ListUploadResponseDTO> uploadToList(
        @RequestParam("file") MultipartFile file,
        @PathVariable("listId") UUID listId,
        @RequestHeader(name = "authorization") String authorization
    ) throws ErrorsFoundInFileException, BranchServiceCoreException {
        if (!authorizationService.userCanEditList(authorization)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        if (file.isEmpty()) {
            return new ResponseEntity<>(new ListUploadResponseDTO(List.of(), 0, null), HttpStatus.OK);
        } else {
            List<String> errors = new ArrayList<>();

            try (
                Reader reader = new BufferedReader(
                    new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)
                );
                CSVReaderHeaderAware headerAware = new CSVReaderHeaderAware(
                    new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))
                );
            ) {
                CsvToBeanBuilder<UploadListData> uploadListDataParser = new CsvToBeanBuilder<>(reader);
                uploadListDataParser.withType(UploadListData.class);
                uploadListDataParser.withIgnoreLeadingWhiteSpace(true);
                uploadListDataParser.withThrowExceptions(false);

                List<String> currentHeadersList = null;
                try {
                    currentHeadersList = headerAware.readMap().keySet().stream().collect(Collectors.toList());
                } catch (Exception e) {
                    log.error("Error reading list upload file: " + e);
                }

                var predefinedHeadersList = getPredefinedHeaders();
                if (
                    currentHeadersList == null ||
                    currentHeadersList.isEmpty() ||
                    !currentHeadersList.containsAll(predefinedHeadersList)
                ) {
                    errors.add(ListConstants.INCORRECT_COLUMN_HEADERS);
                    throw new ErrorsFoundInFileException(errors);
                }
                var listParser = uploadListDataParser.build().parse();

                if (listParser.size() > listService.MAX_LIST_SIZE) {
                    throw new AddItemToListFoundException();
                }

                listService.checkFile(listParser, errors);
                if (!errors.isEmpty()) {
                    throw new ErrorsFoundInFileException(errors);
                }

                return new ResponseEntity<>(listService.handleUploadedList(listParser, listId), HttpStatus.OK);
            } catch (IOException e) {
                log.error("Error reading list upload file: " + e);
                return new ResponseEntity<>(
                    new ListUploadResponseDTO(List.of(), 0, null),
                    HttpStatus.INTERNAL_SERVER_ERROR
                );
            } catch (AddItemToListFoundException e) {
                errors.add(e.getMessage());
                throw new ErrorsFoundInFileException(errors);
            } catch (RuntimeException e) {
                log.debug("Formatting error in uploaded list: " + e);
                return new ResponseEntity<>(new ListUploadResponseDTO(List.of(), 0, null), HttpStatus.BAD_REQUEST);
            }
        }
    }

    @ExceptionHandler({ ItemAlreadyInListException.class })
    public ResponseEntity<String> handleConflictException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    private List<String> getPredefinedHeaders() {
        List<String> predefinedHeadersList = new ArrayList<String>();
        predefinedHeadersList.add(ListConstants.HEADER_COLUMN1);
        predefinedHeadersList.add(ListConstants.HEADER_COLUMN2);
        predefinedHeadersList.add(ListConstants.HEADER_COLUMN3);
        predefinedHeadersList.add(ListConstants.HEADER_COLUMN4);

        return predefinedHeadersList;
    }

    @PostMapping("/copy")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void copy(@RequestParam String fromBillTo, @RequestParam String toBillTo)
        throws ListNotFoundException, CopyListException {
        listService.copy(fromBillTo, toBillTo);
    }
}
