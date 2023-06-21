package com.reece.platform.products.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reece.platform.products.exceptions.*;
import com.reece.platform.products.helpers.ERPSystem;
import com.reece.platform.products.insite.repository.WishListDAO;
import com.reece.platform.products.model.DTO.*;
import com.reece.platform.products.model.ErpEnum;
import com.reece.platform.products.model.UploadListData;
import com.reece.platform.products.model.eclipse.ProductResponse.ProductResponse;
import com.reece.platform.products.model.entity.Cart;
import com.reece.platform.products.model.entity.LineItems;
import com.reece.platform.products.model.entity.ListLineItem;
import com.reece.platform.products.model.repository.CartDAO;
import com.reece.platform.products.model.repository.LineItemsDAO;
import com.reece.platform.products.model.repository.ListLineItemsDAO;
import com.reece.platform.products.model.repository.ListsDAO;
import com.reece.platform.products.utilities.StringUtilities;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ListService {

    private final ListsDAO listsDAO;
    private final CartDAO cartDAO;
    private final LineItemsDAO lineItemsDAO;
    private final ListLineItemsDAO listLineItemsDAO;
    private final ProductService productService;
    private final ErpService erpService;
    private final WishListDAO wishListDAO;
    private final AccountService accountService;

    public static int MAX_LIST_SIZE = 600;

    @Autowired
    public ListService(
        ListsDAO listsDAO,
        CartDAO cartDAO,
        LineItemsDAO lineItemsDAO,
        ListLineItemsDAO listLineItemsDAO,
        ProductService productService,
        ErpService erpService,
        WishListDAO wishListDAO,
        AccountService accountService
    ) {
        this.listsDAO = listsDAO;
        this.cartDAO = cartDAO;
        this.lineItemsDAO = lineItemsDAO;
        this.listLineItemsDAO = listLineItemsDAO;
        this.productService = productService;
        this.erpService = erpService;
        this.wishListDAO = wishListDAO;
        this.accountService = accountService;
    }

    /**
     * Get List by List ID
     * @param listId
     * @param branchId
     * @return
     * @throws ListNotFoundException
     * @throws ElasticsearchException
     * @throws JsonProcessingException
     */
    public ListDTO getList(UUID listId, String branchId) throws ListNotFoundException, ElasticsearchException {
        var list = listsDAO.findById(listId).orElseThrow(() -> new ListNotFoundException((listId.toString())));
        var listDTO = new ListDTO(list);
        if (list.getListLineItems().size() > 0) {
            listDTO.setListLineItems(getDetails(list, branchId));
        }
        return listDTO;
    }

    public List<ListDTO> getListsByBillToAccountId(UUID billToAccountId) {
        var lists = listsDAO.findAllByBillToAccountId(billToAccountId);
        return lists.stream().map(ListDTO::createWithoutLineItems).toList();
    }

    @Transactional
    public ListDTO createList(CreateListDTO createListDTO) {
        var list = new com.reece.platform.products.model.entity.List(createListDTO);
        var savedList = list = listsDAO.save(list);

        // Save items so a list can be created with items
        if (list.getListLineItems() != null) {
            var items = list
                .getListLineItems()
                .stream()
                .peek(l -> l.setListId(savedList.getId()))
                .collect(Collectors.toSet());
            listLineItemsDAO.saveAll(items);
        } else {
            list.setListLineItems(Collections.emptySet());
        }

        return new ListDTO(list);
    }

    @Transactional
    public ListDTO updateList(ListDTO updateListDTO) throws ListNotFoundException {
        var list = listsDAO
            .findById(updateListDTO.getId())
            .orElseThrow(() -> new ListNotFoundException(updateListDTO.toString()));
        list.setName(updateListDTO.getName());

        if (updateListDTO.getListLineItems() != null) {
            var items = updateListDTO.getListLineItems().stream().map(ListLineItem::new).collect(Collectors.toSet());
            listLineItemsDAO.saveAll(items);
        }

        list = listsDAO.save(list);
        return new ListDTO(list);
    }

    @Transactional
    public ToggleListItemResponseDTO toggleItemFromLists(ToggleListLineItemDTO toggleListLineItemDTO)
        throws AddItemToListFoundException {
        var listLineItemDTO = toggleListLineItemDTO.getListLineItemDTO();
        var listsIDs = toggleListLineItemDTO.getListIds();
        List<String> errors = new ArrayList<>();
        List<UUID> addedListIDs = new ArrayList<>();

        if (listsIDs.isEmpty()) {
            List<com.reece.platform.products.model.entity.List> addedLists = listsDAO.findListsByBillToAccountIdAndPartNumber(
                toggleListLineItemDTO.getBillToAccountId(),
                listLineItemDTO.getErpPartNumber()
            );
            addedListIDs =
                addedLists
                    .stream()
                    .map(com.reece.platform.products.model.entity.List::getId)
                    .collect(Collectors.toList());
        }

        if (listLineItemDTO.getQuantity() != null) {
            try {
                var product = productService.getProductByNumber(
                    "MSC-" + listLineItemDTO.getErpPartNumber(),
                    ERPSystem.ECLIPSE
                );
                if (product == null) {
                    log.info("The Product MSC-" + listLineItemDTO.getErpPartNumber() + " does not exist");
                    errors.add("The Product MSC-" + listLineItemDTO.getErpPartNumber() + " does not exist");
                }
            } catch (JsonProcessingException | BranchServiceCoreException | ElasticsearchException e) {
                log.error("Error importing line item: " + e);
                errors.add("Error importing line item: " + e);
            }
            if (!errors.isEmpty()) {
                throw new AddItemToListFoundException(errors);
            }
            validateAndRemoveItemFromLists(toggleListLineItemDTO);
        }

        if (!listsIDs.isEmpty()) {
            var lists = listsDAO.findAllById(listsIDs);
            for (com.reece.platform.products.model.entity.List list : lists) {
                try {
                    addItemToList(list.getId(), toggleListLineItemDTO.getListLineItemDTO());
                } catch (ItemAlreadyInListException e) {
                    if (listLineItemDTO.getQuantity() == null) {
                        log.info(
                            String.format(
                                "The Product MSC-" +
                                listLineItemDTO.getErpPartNumber() +
                                " removed from list: " +
                                list.getName()
                            )
                        );
                        listLineItemsDAO.deleteById(e.getItemId());
                    } else {
                        log.info(
                            "The Product MSC-" +
                            listLineItemDTO.getErpPartNumber() +
                            " is already added to List: " +
                            list.getName()
                        );
                    }
                } catch (ListNotFoundException e) {
                    log.info("Unable to find list with id: %s, skipped.", list.getId().toString());
                    errors.add("Unable to find list with id: %s, skipped." + list.getId().toString());
                }
            }
        } else {
            listsIDs = addedListIDs;
        }

        if (!errors.isEmpty()) {
            throw new AddItemToListFoundException(errors);
        }

        // Refetch latest
        var updates = listsDAO.findAllById(listsIDs);
        return new ToggleListItemResponseDTO(
            updates.stream().map(ListDTO::createWithoutLineItems).collect(Collectors.toList())
        );
    }

    @Transactional
    public UUID addItemToList(UUID listId, ListLineItemDTO listLineItemDTO)
        throws ItemAlreadyInListException, ListNotFoundException, AddItemToListFoundException {
        var item = new ListLineItem(listLineItemDTO);
        var list = listsDAO.findById(listId).orElseThrow(() -> new ListNotFoundException(listId.toString()));

        // Don't let them add the same item twice
        var existingListLineItems = list.getListLineItems();

        var maybeItem = existingListLineItems
            .stream()
            .filter(l -> l.getErpPartNumber().equals(listLineItemDTO.getErpPartNumber()))
            .findFirst();

        if (maybeItem.isPresent()) {
            throw new ItemAlreadyInListException(listLineItemDTO.getErpPartNumber(), listId, maybeItem.get().getId());
        }

        if (existingListLineItems.size() >= MAX_LIST_SIZE) {
            throw new AddItemToListFoundException();
        }

        item.setListId(listId);

        // Ensure that any time we add an item it's the largest in the list if sort order not defined in the lineItem
        var largestSortOrder = list.getListLineItems().stream().max(Comparator.comparing(ListLineItem::getSortOrder));
        if (listLineItemDTO.getSortOrder() != null) {
            item.setSortOrder(listLineItemDTO.getSortOrder());
        } else if (largestSortOrder.isPresent()) {
            item.setSortOrder(largestSortOrder.get().getSortOrder() + 1);
        } else {
            item.setSortOrder(0);
        }

        var lineItem = listLineItemsDAO.save(item);
        return lineItem.getId();
    }

    @Transactional
    public List<UUID> addAllCartItemsToLists(UUID cartId, List<String> listIds)
        throws ListNotFoundException, CartNotFoundException, AddItemToListFoundException {
        List<UUID> addedToLists = new ArrayList<>();
        Cart cart = cartDAO.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId.toString()));
        var cartLineItems = lineItemsDAO.findAllByCartId(cartId);
        List<String> errors = new ArrayList<>();
        List<String> listNotFoundErrors = new ArrayList<>();
        List<String> moreItemsErrors = new ArrayList<>();

        for (String listId : listIds) {
            try {
                var addedList = cartToList(UUID.fromString(listId), cartLineItems);
                addedToLists.add(addedList);
            } catch (AddItemToListFoundException e) {
                log.info(
                    "Item(s) could not be added to list: " + listId.toString() + ". List cannot exceed 600 line items."
                );
                moreItemsErrors.add(listId.toString());
            } catch (ListNotFoundException e) {
                log.info("Unable to find list. Item(s) could not be added to list: " + listId.toString());
                listNotFoundErrors.add(listId.toString());
            }
        }
        if (!moreItemsErrors.isEmpty()) {
            errors.add(
                "Item(s) could not be added to list: " +
                String.join(" or ", moreItemsErrors) +
                ". List cannot exceed 600 line items."
            );
        }
        if (!listNotFoundErrors.isEmpty()) {
            errors.add(
                "Unable to find list. Item(s) could not be added to list: " + String.join(" or ", listNotFoundErrors)
            );
        }
        if (!errors.isEmpty()) {
            throw new AddItemToListFoundException(errors);
        }
        return addedToLists;
    }

    @Transactional
    public UUID cartToList(UUID listId, List<LineItems> cartLineItems)
        throws ListNotFoundException, AddItemToListFoundException {
        var maybeList = listsDAO.findById(listId).orElseThrow(() -> new ListNotFoundException(listId.toString()));

        var listLineItems = maybeList.getListLineItems();
        var newLineItems = new ArrayList<ListLineItem>();

        for (LineItems itemInfo : cartLineItems) {
            Optional<ListLineItem> existingItem = listLineItems
                .stream()
                .filter(l -> l.getErpPartNumber().equals(itemInfo.getErpPartNumber()))
                .findFirst();

            if (existingItem.isEmpty()) {
                ListLineItem newItem = new ListLineItem();

                newItem.setErpPartNumber(itemInfo.getErpPartNumber());
                newItem.setQuantity(itemInfo.getQuantity());
                newItem.setListId(listId);
                newItem.setSortOrder(0);
                newLineItems.add(newItem);
            } else {
                existingItem.get().setQuantity(existingItem.get().getQuantity() + itemInfo.getQuantity());
                listLineItemsDAO.save(existingItem.get());
            }
        }

        if (newLineItems.size() > 0) {
            int existingListLineItemsSize = 0;

            if (listLineItems != null) {
                existingListLineItemsSize = listLineItems.size();
            }

            if ((existingListLineItemsSize + newLineItems.size()) <= MAX_LIST_SIZE) {
                listLineItemsDAO.saveAll(newLineItems);
            } else {
                throw new AddItemToListFoundException();
            }
        }
        return listId;
    }

    @Transactional
    public UUID addAllCartItemsToNewList(String name, UUID cartId, UUID accountId) throws CartNotFoundException {
        Cart cart = cartDAO.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId.toString()));
        var list = new com.reece.platform.products.model.entity.List(name, accountId);
        var savedList = list = listsDAO.save(list);
        var cartLineItems = lineItemsDAO.findAllByCartId(cartId);
        var newLineItems = new ArrayList<ListLineItem>();
        for (LineItems itemInfo : cartLineItems) {
            ListLineItem newItem = new ListLineItem();
            newItem.setErpPartNumber(itemInfo.getErpPartNumber());
            newItem.setQuantity(itemInfo.getQuantity());
            newItem.setListId(savedList.getId());
            newItem.setSortOrder(0);
            newLineItems.add(newItem);
        }

        if (newLineItems.size() > 0) {
            listLineItemsDAO.saveAll(newLineItems);
        }

        return list.getId();
    }

    @Transactional
    public boolean deleteListById(UUID listId) throws ListNotFoundException {
        var list = listsDAO.findById(listId).orElseThrow(() -> new ListNotFoundException((listId.toString())));
        listLineItemsDAO.deleteAllByListId(listId);
        listsDAO.deleteById(listId);
        return true;
    }

    /**
     * Pulls legacy lists from insite and merges them into the maX system
     * @param erpAccountId
     * @return
     */
    @Transactional
    public WishListSyncResponseDTO syncListsFromInsite(String erpAccountId, ErpEnum erpEnum) {
        val wishLists = wishListDAO.findAllByCustomerErpAccountId(erpAccountId);
        val billToAccountId = accountService.getEcommBillToId(erpAccountId, erpEnum.name());

        for (val wishList : wishLists) {
            val existingLists = listsDAO.findAllByMatchingNameAndBillToAccountId(wishList.getName(), billToAccountId);
            val newListName = existingLists.size() > 0
                ? String.format("%s (%d)", wishList.getName(), existingLists.size())
                : wishList.getName();
            val newList = new com.reece.platform.products.model.entity.List(newListName, billToAccountId);

            listsDAO.save(newList);

            val items = wishList
                .getWishListProducts()
                .stream()
                .map(ListLineItem::new)
                .map(l -> {
                    l.setListId(newList.getId());
                    return l;
                })
                .collect(Collectors.toSet());

            listLineItemsDAO.saveAll(items);
        }

        return new WishListSyncResponseDTO(wishLists.size());
    }

    /**
     * Fetch pricing and availability for the list line items
     *
     * @param list list of products to fetch price and availability
     * @param branchId branch to see availability for
     * @return list of line items with pricing and availability
     */
    // TODO: tw - refactor to use a single call in product service
    public List<ListLineItemDTO> getDetails(com.reece.platform.products.model.entity.List list, String branchId)
        throws ElasticsearchException {
        List<ListLineItemDTO> lineItemResponseDTOS = new ArrayList<>();

        List<String> productIds = list
            .getListLineItems()
            .stream()
            .map(ListLineItem::getErpPartNumber)
            .collect(Collectors.toList());

        // Add elastic data
        List<ProductDTO> productDTOS = productService.getProductsByNumber(productIds);

        List<String> elasticIds = productDTOS.stream().filter(Objects::nonNull).map(ProductDTO::getPartNumber).toList();
        List<String> missingIds = productIds.stream().filter(id -> !elasticIds.contains(id)).toList();

        // Add missing stub data
        if (!missingIds.isEmpty()) {
            List<ProductDTO> missingProductDTOs = missingIds
                .stream()
                .map(id -> {
                    ProductDTO pDTO = new ProductDTO();
                    pDTO.setPartNumber(id);
                    pDTO.setId("MSC-" + id);
                    pDTO.setStatus("NotInElastic");
                    return pDTO;
                })
                .toList();

            productDTOS.removeAll(Collections.singleton(null));
            productDTOS.addAll(missingProductDTOs);
        }

        List<ListLineItem> lineItemsList = list.getListLineItems().stream().toList();

        for (ListLineItem lineItem : lineItemsList) {
            ProductDTO product = productDTOS
                .stream()
                .filter(prod -> prod.getPartNumber().equals(lineItem.getErpPartNumber()))
                .findFirst()
                .get();

            lineItemResponseDTOS.add(buildLineItemResponseDTO(lineItem, product, branchId));

            var item = lineItemResponseDTOS
                .stream()
                .filter(i -> i.getErpPartNumber() != null && i.getErpPartNumber().equals(product.getPartNumber()))
                .findFirst();

            if (item.isPresent()) {
                var itemData = item.get();

                itemData.setName(product.getName());
                itemData.setManufacturerName(product.getManufacturerName());
                itemData.setManufacturerNumber(product.getManufacturerNumber());
                itemData.setImageUrls(product.getImageUrls());
                itemData.setTechSpecs(product.getTechSpecifications());
                itemData.setMinIncrementQty(product.getMinIncrementQty());
                itemData.setCustomerPartNumbers(product.getCustomerPartNumbers());
                itemData.setCustomerPartNumber(product.getCustomerPartNumber());
            }
        }

        return lineItemResponseDTOS;
    }

    /**
     * Build DTO from eclipse information regarding pricing and availability
     *
     * @param listLineItem line item from DB
     * @param product product information from eclipse
     * @param branchId branch to see availability for
     * @return DTO for list line item with pricing and availability
     */
    private ListLineItemDTO buildLineItemResponseDTO(ListLineItem listLineItem, ProductDTO product, String branchId) {
        ListLineItemDTO lineItemResponseDTO = new ListLineItemDTO(listLineItem);

        lineItemResponseDTO.setName(product.getName());
        lineItemResponseDTO.setStatus(product.getStatus());

        return lineItemResponseDTO;
    }

    @Transactional
    public void incrementItemInList(UUID lineItemId, int additionalQuantity) {
        var lineItem = listLineItemsDAO.getOne(lineItemId);
        lineItem.setQuantity(lineItem.getQuantity() + additionalQuantity);
        listLineItemsDAO.save(lineItem);
    }

    @Transactional
    public ListUploadResponseDTO handleUploadNewList(List<UploadListData> data, UUID billToAccountId, String name)
        throws AddItemToListFoundException {
        CreateListDTO createListDTO = new CreateListDTO();
        createListDTO.setName(name);
        createListDTO.setBillToAccountId(billToAccountId);

        var listDTO = createList(createListDTO);

        return handleUploadedList(data, listDTO.getId());
    }

    @Transactional
    public ListUploadResponseDTO handleUploadedList(List<UploadListData> data, UUID listId)
        throws AddItemToListFoundException {
        List<UploadListData> dataMap = data
            .stream()
            .collect(
                Collectors.toMap(
                    UploadListData::getPartNumber,
                    Function.identity(),
                    (UploadListData left, UploadListData right) -> {
                        left.setQuantity(right.getQuantity() + left.getQuantity());
                        right.setPartNumber(null);
                        return left;
                    },
                    LinkedHashMap::new
                )
            )
            .values()
            .stream()
            .filter(item -> item.getPartNumber() != null)
            .filter(item -> item.getQuantity() >= 0)
            .toList();

        var list = listsDAO.findById(listId);
        int existingListLineItems = 0;
        int startingSortOrder = 0;
        if (list.isPresent() && !list.get().getListLineItems().isEmpty()) {
            existingListLineItems = list.get().getListLineItems().size();

            startingSortOrder =
                list
                    .get()
                    .getListLineItems()
                    .stream()
                    .max(Comparator.comparing(ListLineItem::getSortOrder))
                    .get()
                    .getSortOrder() +
                1;
        }

        if ((existingListLineItems + dataMap.size()) > MAX_LIST_SIZE) {
            throw new AddItemToListFoundException();
        }

        AtomicInteger currentSortOrder = new AtomicInteger(startingSortOrder);
        List<ListUploadErrorDTO> errors = IntStream
            .range(0, dataMap.size())
            .mapToObj(i -> {
                UploadListData lineItem = dataMap.get(i);
                ListLineItemDTO listLineItemDTO = new ListLineItemDTO();
                listLineItemDTO.setErpPartNumber(lineItem.getPartNumber());
                listLineItemDTO.setQuantity(lineItem.getQuantity());
                listLineItemDTO.setSortOrder(currentSortOrder.get());
                try {
                    var addResult = addItemToList(listId, listLineItemDTO);
                } catch (ItemAlreadyInListException e) {
                    incrementItemInList(e.getItemId(), lineItem.getQuantity());
                    return new ListUploadErrorDTO(
                        lineItem.getPartNumber(),
                        lineItem.getDescription(),
                        lineItem.getMfrName(),
                        lineItem.getQuantity()
                    );
                } catch (ListNotFoundException e) {
                    log.error("Error importing line item: " + e);
                    return new ListUploadErrorDTO(
                        lineItem.getPartNumber(),
                        lineItem.getDescription(),
                        lineItem.getMfrName(),
                        lineItem.getQuantity()
                    );
                } catch (Exception e) {
                    log.error("Error importing line item: " + e);
                    return new ListUploadErrorDTO(
                        lineItem.getPartNumber(),
                        lineItem.getDescription(),
                        lineItem.getMfrName(),
                        lineItem.getQuantity()
                    );
                }
                currentSortOrder.getAndIncrement();

                return null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        long successfulRowCount = data.size() - errors.size();

        return new ListUploadResponseDTO(errors, successfulRowCount, listId);
    }

    public void checkFile(List<UploadListData> listData, List<String> errors)
        throws BranchServiceCoreException, JsonProcessingException, ElasticsearchException {
        var productIds = listData
            .stream()
            .map(a -> {
                return StringUtilities.formatProductNumber(a.getPartNumber());
            })
            .collect(Collectors.toList());
        List<ProductDTO> productDTOS = productService.getProductsByNumber(productIds);
        productDTOS = productDTOS != null ? productDTOS : Collections.emptyList();
        var avaliableProductIds = productDTOS.stream().map(ProductDTO::getPartNumber).collect(Collectors.toList());
        for (UploadListData data : listData) {
            data.setPartNumber(StringUtilities.formatProductNumber(data.getPartNumber()));

            if (data.getQuantity() == null) {
                errors.add("Quantity is missing for part number " + data.getPartNumber());
            } else if (data.getQuantity() < 0) {
                errors.add("The part # " + data.getPartNumber() + " must have a positive quantity");
            }

            if (data.getPartNumber().isEmpty()) {
                errors.add("Part # is required");
            } else {
                if (!avaliableProductIds.contains(data.getPartNumber())) {
                    errors.add("The Part # " + data.getPartNumber() + " does not exist");
                }
            }
        }
    }

    public boolean isDuplicate(String name, UUID billToAccountId) {
        int count = listsDAO.findCountByMatchingNameAndBillToAccountId(name, billToAccountId);
        return count > 0;
    }

    @Transactional
    public void validateAndRemoveItemFromLists(ToggleListLineItemDTO toggleListLineItemDTO) {
        ListLineItemDTO listLineItemDTO = toggleListLineItemDTO.getListLineItemDTO();
        List<UUID> lists = toggleListLineItemDTO.getListIds();
        var billToAccountId = toggleListLineItemDTO.getBillToAccountId();

        List<ListLineItem> addedItemInLists = listLineItemsDAO.findAllAddedItemListsByBillToAccountIdAndPartNumber(
            billToAccountId,
            listLineItemDTO.getErpPartNumber()
        );

        if (addedItemInLists != null) {
            for (ListLineItem listLineitem : addedItemInLists) {
                if (!lists.contains(listLineitem.getListId())) {
                    try {
                        listLineItemsDAO.deleteById(listLineitem.getId());
                        log.info(
                            "The Product MSC-" + listLineItemDTO.getErpPartNumber() + " has been removed from list"
                        );
                    } catch (Exception e) {
                        log.error(e.toString());
                    }
                }
            }
        }
    }

    public List<UploadListData> exportListIntoCSV(ListDTO listDTO) throws ExportListIntoCSVFoundException {
        List<ListLineItemDTO> listLineItemDTOList = listDTO.getListLineItems();
        List<UploadListData> csvList = new ArrayList<>();
        try {
            for (ListLineItemDTO listLineItemDTO : listLineItemDTOList) {
                csvList.add(
                    new UploadListData(
                        listLineItemDTO.getErpPartNumber(),
                        listLineItemDTO.getName(),
                        listLineItemDTO.getManufacturerName(),
                        listLineItemDTO.getQuantity(),
                        listLineItemDTO.getPricePerUnit(),
                        listLineItemDTO.getManufacturerNumber()
                    )
                );
            }

            return csvList;
        } catch (Exception e) {
            throw new ExportListIntoCSVFoundException("Csv writing error: " + e.getMessage());
        }
    }

    @Transactional
    public void copy(String fromBillTo, String toBillTo) throws CopyListException {
        try {
            val fromBillToAccountId = accountService.getEcommBillToId(fromBillTo, ErpEnum.ECLIPSE.toString());
            val toBillToAccountId = accountService.getEcommBillToId(toBillTo, ErpEnum.ECLIPSE.toString());
            val listsToSave = listsDAO.findAllByBillToAccountId(fromBillToAccountId);
            for (val wishList : listsToSave) {
                //Find if the list is already existing by name in the toBillTo account
                val existingLists = listsDAO.findAllByMatchingNameAndBillToAccountId(
                    wishList.getName(),
                    toBillToAccountId
                );
                //if the lists is already existing then append suffix
                val newListName = existingLists.size() > 0
                    ? String.format("%s (%d)", wishList.getName(), existingLists.size())
                    : wishList.getName();

                val newList = new com.reece.platform.products.model.entity.List(newListName, toBillToAccountId);
                var savedList = listsDAO.saveAndFlush(newList);

                // Copy the line items (or) products . Since only lists are copied above
                if (wishList.getListLineItems() != null) {
                    var items = wishList
                        .getListLineItems()
                        .stream()
                        .map(l -> new ListLineItem(l, savedList.getId()))
                        .collect(Collectors.toSet());
                    listLineItemsDAO.saveAll(items);
                }
            }
        } catch (Exception exception) {
            throw new CopyListException(fromBillTo, toBillTo);
        }
    }
}
