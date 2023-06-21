package com.reece.platform.products.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reece.platform.products.branches.model.DTO.BranchResponseDTO;
import com.reece.platform.products.branches.service.BranchesService;
import com.reece.platform.products.exceptions.*;
import com.reece.platform.products.helpers.ERPSystem;
import com.reece.platform.products.model.*;
import com.reece.platform.products.model.DTO.*;
import com.reece.platform.products.model.eclipse.common.EclipseAddressRequestDTO;
import com.reece.platform.products.model.eclipse.common.EclipseAddressResponseDTO;
import com.reece.platform.products.model.entity.*;
import com.reece.platform.products.model.repository.*;
import com.reece.platform.products.orders.model.WebStatus;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Transactional(readOnly = true)
public class CartService {

    public static final String ECLIPSE_SHIP_VIA_ERROR_CODE = "530";
    private final CartDAO cartDAO;
    private final LineItemsDAO lineItemsDAO;
    private final DeliveryDAO deliveryDAO;
    private final WillCallDAO willCallDAO;
    private final AddressDAO addressDAO;
    private final ApprovalFlowStateDAO approvalFlowStateDAO;
    private final OrdersPendingApprovalDAO ordersPendingApprovalDAO;
    private final OrderStatusDAO orderStatusDAO;
    private final ListsDAO listsDAO;
    private final AccountService accountService;
    private final AuthorizationService authorizationService;
    private final ErpService erpService;
    private final ProductService productService;
    private final NotificationService notificationService;
    private final BranchesService branchesService;
    private final UserDAO userDAO;

    @Value("${cart_pricing_availability_ttl}")
    private int cartPricingAvailabilityTTL;

    private static int MAX_CART_SIZE = 600;
    private static int MAX_ORDERED_BY_LENGTH = 35;

    @Autowired
    public CartService(
        CartDAO cartDAO,
        LineItemsDAO lineItemsDAO,
        ApprovalFlowStateDAO approvalFlowStateDAO,
        DeliveryDAO deliveryDAO,
        WillCallDAO willCallDAO,
        AddressDAO addressDAO,
        OrdersPendingApprovalDAO ordersPendingApprovalDAO,
        OrderStatusDAO orderStatusDAO,
        ListsDAO listsDAO,
        AccountService accountService,
        AuthorizationService authorizationService,
        ErpService erpService,
        ProductService productService,
        NotificationService notificationService,
        BranchesService branchesService,
        UserDAO userDAO
    ) {
        this.cartDAO = cartDAO;
        this.lineItemsDAO = lineItemsDAO;
        this.approvalFlowStateDAO = approvalFlowStateDAO;
        this.deliveryDAO = deliveryDAO;
        this.willCallDAO = willCallDAO;
        this.addressDAO = addressDAO;
        this.ordersPendingApprovalDAO = ordersPendingApprovalDAO;
        this.orderStatusDAO = orderStatusDAO;
        this.listsDAO = listsDAO;
        this.accountService = accountService;
        this.authorizationService = authorizationService;
        this.erpService = erpService;
        this.productService = productService;
        this.notificationService = notificationService;
        this.branchesService = branchesService;
        this.userDAO = userDAO;
    }

    /**
     * Create a cart using given user account information
     *
     * @param userAccountInfo user account information to create the cart for
     * @return UUID of created cart
     */
    @Transactional
    public UUID createCart(UserAccountInfo userAccountInfo, String authorization)
        throws CartAlreadyExistsException, UserNotFoundException {
        UUID accountId = userAccountInfo.getShipToAccountId();
        UUID userId = userAccountInfo.getUserId();

        if (userHasActiveCart(accountId, userId)) {
            throw new CartAlreadyExistsException(accountId.toString(), userId.toString());
        }

        Cart cart = new Cart();
        cart.setOwnerId(userId);

        Address address = new Address();
        Delivery delivery = new Delivery();
        WillCall willCall = new WillCall();

        cart = setDefaultCartValues(cart, address, delivery, willCall, userAccountInfo, authorization);

        cart = cartDAO.save(cart);

        return cart.getId();
    }

    private boolean userHasActiveCart(UUID accountId, UUID userId) {
        UUID activeFlowStateId = approvalFlowStateDAO
            .findByDisplayName(ApprovalFlowStateEnum.ACTIVE.getDisplayName())
            .getId();
        List<Cart> carts = cartDAO.findAllByOwnerIdAndShipToId(userId, accountId);
        Optional<Cart> activeCart = carts
            .stream()
            .filter(cart -> cart.getApprovalState().equals(activeFlowStateId))
            .findAny();
        return activeCart.isPresent();
    }

    /**
     * Reset a cart to the default values
     *
     * @param cartId cart to reset
     * @param userAccountInfo user account information to reset the cart for
     * @return updated cart
     */
    @Transactional
    public CartResponseDTO resetCart(UUID cartId, UserAccountInfo userAccountInfo, String authorization)
        throws CartNotFoundException, UserNotFoundException {
        Cart cart = cartDAO.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId.toString()));

        cart.setApproverId(null);
        cart.setPoNumber(null);
        cart.setPricingBranchId(null);
        cart.setCreditCard(null);
        cart.setRejectionReason(null);
        cart.setDeliveryMethod(null);

        Delivery delivery = new Delivery();
        delivery.setId(cart.getDelivery().getId());
        delivery.setShouldShipFullOrder(true);

        Address address = new Address();
        address.setId(cart.getDelivery().getAddress().getId());

        WillCall willCall = new WillCall();
        willCall.setId(cart.getWillCall().getId());

        cart = setDefaultCartValues(cart, address, delivery, willCall, userAccountInfo, authorization);

        lineItemsDAO.deleteAllByCartId(cart.getId());

        cart = cartDAO.save(cart);

        return new CartResponseDTO(cart);
    }

    @Transactional
    public CartDeleteResponseDTO deleteCartsByShipToAccountId(UUID shipToAccountId) {
        val carts = cartDAO
            .findAllByShipToId(shipToAccountId)
            .stream()
            .map(cart -> {
                long lineItemCount = lineItemsDAO.deleteAllByCartId(cart.getId());
                cartDAO.deleteById(cart.getId());
                return lineItemCount;
            })
            .toList();

        return new CartDeleteResponseDTO(carts.size(), carts.stream().reduce(0L, Long::sum), true);
    }

    /**
     * Set the default values for a cart
     *
     * @param cart cart to set values for
     * @param userAccountInfo user account information to set the cart values for
     * @return updated cart
     */
    @Transactional
    public Cart setDefaultCartValues(
        Cart cart,
        Address address,
        Delivery delivery,
        WillCall willCall,
        UserAccountInfo userAccountInfo,
        String authorization
    ) throws UserNotFoundException {
        UUID accountId = userAccountInfo.getShipToAccountId();
        UUID activeFlowStateId = approvalFlowStateDAO
            .findByDisplayName(ApprovalFlowStateEnum.ACTIVE.getDisplayName())
            .getId();

        cart.setShipToId(accountId);
        if (cart.getShippingBranchId() == null) {
            cart.setShippingBranchId(userAccountInfo.getShippingBranchId());
        }
        cart.setApprovalState(activeFlowStateId);
        cart.setErpSystemName(userAccountInfo.getErpSystemName());
        cart.setPaymentMethodType(PaymentMethodTypeEnum.BILLTOACCOUNT);

        UserDTO user;

        try {
            user = accountService.getUser(userAccountInfo.getUserId().toString(), authorization);
        } catch (Exception e) {
            throw new UserNotFoundException(userAccountInfo.getUserId());
        }

        cart.setApproverId(user.getApproverId());

        var brand = userAccountInfo.getErpSystemName().getDefaultBrand();
        AccountResponseDTO shipToAccount = accountService.getAccountData(accountId.toString(), authorization, brand);
        address.setCompanyName(shipToAccount.getCompanyName());
        address.setStreet1(shipToAccount.getStreet1());
        address.setStreet2(shipToAccount.getStreet2());
        address.setCity(shipToAccount.getCity());
        address.setState(shipToAccount.getState());
        // Hard coding since we're only operating in US
        address.setCountry("USA");
        address.setZip(shipToAccount.getZip());
        address.setCustom(false);
        address = addressDAO.save(address);

        delivery.setShipToId(accountId);
        delivery.setAddress(address);
        delivery = deliveryDAO.save(delivery);

        if (willCall.getBranchId() == null) {
            willCall.setBranchId(userAccountInfo.getShippingBranchId());
        }
        willCall = willCallDAO.save(willCall);

        cart.setDelivery(delivery);
        cart.setWillCall(willCall);

        return cart;
    }

    /**
     * Update an existing cart with car information given
     *
     * @param cartId cart to update
     * @param cart cart information to update existing cart for
     * @return update cart
     */
    @Transactional
    public CartResponseDTO updateCart(UUID cartId, Cart cart) throws CartNotFoundException {
        Cart existingCart = cartDAO.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId.toString()));
        if (cart.getCreditCard() != null) {
            existingCart.setCreditCard(cart.getCreditCard());
        }
        if (cart.getPaymentMethodType() != null) {
            existingCart.setPaymentMethodType(cart.getPaymentMethodType());
        }
        if (cart.getPoNumber() != null) {
            existingCart.setPoNumber(cart.getPoNumber());
        }
        if (cart.getShippingBranchId() != null) {
            existingCart.setShippingBranchId(cart.getShippingBranchId());
        }
        if (cart.getDeliveryMethod() != null) {
            existingCart.setDeliveryMethod(cart.getDeliveryMethod());
        }

        existingCart = cartDAO.save(existingCart);

        return new CartResponseDTO(existingCart);
    }

    /**
     * Fetch a cart by cart id.  Include product information if includeProducts = true
     * @param cartId cart to fetch
     * @param includeProducts parameter to determine whether or not to fetch products
     * @return cart with given cart id
     */
    public CartResponseDTO getCart(UUID cartId, Boolean includeProducts, ErpUserInformation erpUserInformation)
        throws CartNotFoundException, EclipseException {
        Cart cart = cartDAO.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId.toString()));
        List<LineItems> products = lineItemsDAO.findAllSortedLineItem(cartId);

        cart.setSubtotal(calculateSubtotal(products));

        if (includeProducts) {
            cart.setProducts(products);
        }

        CartResponseDTO cartResponseDTO = new CartResponseDTO(cart);
        // TODO: EG - remove dependency of list info within buildCartProducts
        List<LineItemResponseDTO> productsWithElasticData = buildCartProducts(cart, erpUserInformation);
        if (productsWithElasticData != null && productsWithElasticData.size() > 0) {
            cartResponseDTO.setProducts(productsWithElasticData);
        }
        return cartResponseDTO;
    }

    public CartResponseDTO getCartFromQuote(
        GetOrderResponseDTO quote,
        ErpUserInformation erpUserInformation,
        UUID accountId,
        String branchId
    ) throws ParseException {
        return new CartResponseDTO(quote, accountId, erpUserInformation.getErpSystemName(), branchId);
    }

    /**
     * Fetch cart by account id and user id.  Include product information if includeProducts = true
     *
     * @param userId user id the cart is associated with
     * @param accountId account id the car is associated with
     * @param includeProducts parameter to determine whether to fetch products or not
     * @return cart associated with given account and user id
     */
    public CartResponseDTO getCart(
        UUID userId,
        UUID accountId,
        Boolean includeProducts,
        ErpUserInformation erpUserInformation
    ) throws CartNotFoundException, EclipseException {
        UUID activeFlowStateId = approvalFlowStateDAO
            .findByDisplayName(ApprovalFlowStateEnum.ACTIVE.getDisplayName())
            .getId();
        List<Cart> carts = cartDAO.findAllByOwnerIdAndShipToId(userId, accountId);
        Cart activeCart = carts
            .stream()
            .filter(cart -> cart.getApprovalState().equals(activeFlowStateId))
            .findAny()
            .orElseThrow(() -> new CartNotFoundException(userId.toString(), accountId.toString()));

        List<LineItems> products = lineItemsDAO.findAllSortedLineItem(activeCart.getId());
        activeCart.setSubtotal(calculateSubtotal(products));
        if (includeProducts) {
            activeCart.setProducts(products);
        }

        CartResponseDTO cartResponseDTO = new CartResponseDTO(activeCart);

        // TODO: EG - remove dependency of list info within buildCartProducts
        List<LineItemResponseDTO> productsWithElasticData = buildCartProducts(activeCart, erpUserInformation);
        if (productsWithElasticData != null && productsWithElasticData.size() > 0) {
            cartResponseDTO.setProducts(productsWithElasticData);
        }

        return cartResponseDTO;
    }

    /**
     * Build Cart DTO and check if items are missing in Elastic
     * @param cart cart to build DTO and check items for
     * @return DTO with cart and product data
     */
    private CartResponseDTO buildCartResponseDto(Cart cart, ErpUserInformation erpUserInformation) {
        CartResponseDTO cartResponseDTO = new CartResponseDTO(cart);
        // TODO: EG - remove dependency of list info within buildCartProducts
        List<LineItemResponseDTO> productsWithElasticData = buildCartProducts(cart, erpUserInformation);
        if (productsWithElasticData != null && productsWithElasticData.size() > 0) {
            List<String> removedProducts = getRemovedCartItems(productsWithElasticData, cart);
            cartResponseDTO.setRemovedProducts(removedProducts);
            cartResponseDTO.setProducts(productsWithElasticData);
        }
       if (erpUserInformation.getCustomerNumber() != null && !erpUserInformation.getCustomerNumber().isEmpty()) {
            try {
                cartResponseDTO
                        .getProducts()
                        .forEach(p ->
                                p.getProduct().setCustomerPartNumbers(p.getProduct().getCustomerPartNumbers(), erpUserInformation.getCustomerNumber())
                        );
            } catch (Exception e) {}
        }

        return cartResponseDTO;
    }

    /**
     * Refresh the cart and Build Cart DTO and check if items are missing in Elastic
     *
     * @param cartId , to get Cart details
     * @return DTO with cart and product data
     * @Param ErpUserInformation , its for Erp User Information for authentication
     */
    public CartResponseDTO refreshCart(UUID cartId, ErpUserInformation erpUserInformation, Boolean isEmployee)
        throws CartNotFoundException, EclipseException {
        Cart cart = cartDAO.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId.toString()));
        List<LineItems> products = getLineItems(cartId, erpUserInformation, false);
        cart.setSubtotal(calculateSubtotal(products));
        cart.setProducts(products);
        return buildCartResponseDto(cart, erpUserInformation);
    }

    /**
     * Given list of products with elastic data and without, remove products from cart that are missing in Elastic and query Eclipse
     * for product name to set list of removed items in API response
     *
     * @param productsWithElasticData list of products that have Elastic data
     * @param cart                    cart object
     * @return list of removed products if any
     */
    private List<String> getRemovedCartItems(List<LineItemResponseDTO> productsWithElasticData, Cart cart) {
        List<String> removedProducts = new ArrayList<>();

        List<LineItems> itemsNotInElastic = cart
            .getProducts()
            .stream()
            .filter(product ->
                productsWithElasticData
                    .stream()
                    .noneMatch((elasticProduct -> elasticProduct.getErpPartNumber().equals(product.getErpPartNumber())))
            )
            .toList();
        if (!itemsNotInElastic.isEmpty()) {
            var partNumbersToRemove = itemsNotInElastic.stream().map(LineItems::getErpPartNumber).toList();
            for (String partNumber : partNumbersToRemove) {
                removedProducts.add(partNumber);
                lineItemsDAO.deleteByCartIdAndErpPartNumber(cart.getId(), partNumber);
            }
        }
        return removedProducts;
    }

    /**
     * Fills in data on products in given cart from Elastic
     *
     * @param cart cart to update products for
     * @return list of line items with Elastic data
     */
    private List<LineItemResponseDTO> buildCartProducts(Cart cart, ErpUserInformation erpUserInformation) {
        List<LineItemResponseDTO> products = null;
        if (cart.getProducts() != null && !cart.getProducts().isEmpty() && productService != null) {
            // Create LineItemResponseDTOs out of line item entities
            products =
                cart
                    .getProducts()
                    .stream()
                    .map(a -> {
                        LineItemResponseDTO lineItemResponseDTO = new LineItemResponseDTO(a);
                        List<String> partNumbers = new ArrayList<>();
                        if (erpUserInformation != null) {
                            partNumbers.add(a.getErpPartNumber());
                            val erp = ErpEnum.valueOf(erpUserInformation.getErpSystemName());
                            val listIdsPerPartNumber = productService.getAllListIdsByPartNumbersAndErpAccountId(
                                partNumbers,
                                erpUserInformation.getErpAccountId(),
                                erp
                            ); //get listIds corresponding to each line item
                            var listIds = new ArrayList<String>();

                            listIdsPerPartNumber
                                .stream()
                                .filter(p -> p[0].equals(a.getErpPartNumber()))
                                .forEach(p -> {
                                    listIds.addAll(Arrays.asList(p[1].split(",")));
                                });

                            lineItemResponseDTO.setListIds(listIds);
                        }

                        return lineItemResponseDTO;
                    })
                    .collect(Collectors.toList());

            // Join product data (data from Elasticsearch) with the cart-based line item info
            try {
                List<ProductDTO> productDTOS = productService.getProductsByNumber(
                    products.stream().map(LineItemResponseDTO::getErpPartNumber).collect(Collectors.toList()),
                    cart.getErpSystemName()
                );

                // Removing null DTOs (products that do not exist in ElasticSearch
                productDTOS = productDTOS.stream().filter(Objects::nonNull).collect(Collectors.toList());

                List<ProductDTO> finalProductDTOs = productDTOS;
                products =
                    products
                        .stream()
                        .filter(
                            (
                                product ->
                                    finalProductDTOs
                                        .stream()
                                        .anyMatch(
                                            (
                                                elasticProduct ->
                                                    elasticProduct.getPartNumber().equals(product.getErpPartNumber())
                                            )
                                        )
                            )
                        )
                        .collect(Collectors.toList());
                products.forEach(product ->
                    product.setProduct(
                        finalProductDTOs
                            .stream()
                            .filter(p -> p.getPartNumber().equals(product.getErpPartNumber()))
                            .findFirst()
                            .orElse(new ProductDTO())
                    )
                );
            } catch (JsonProcessingException | ElasticsearchException exception) {
                return null;
            }
        }
        return products;
    }

    /**
     * Retrieve line items for the given cartId
     *
     * @param cartId cartId to retrieve line items for
     * @param erpUserInformation user information for authenticating call to erp system
     * @param overrideTtl if true, refresh availability and pricing data regardless of expiry status of TTL
     * @return line items of the given cartId
     */
    private List<LineItems> getLineItems(UUID cartId, ErpUserInformation erpUserInformation, Boolean overrideTtl)
        throws CartNotFoundException, EclipseException {
        Cart cart = cartDAO.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId.toString()));
        List<LineItems> cartItems = lineItemsDAO.findAllSortedLineItem(cartId);
        return saveCartLineItems(cartItems, cart, erpUserInformation, overrideTtl);
    }

    /**
     * Calculates the subtotal for the cart
     *
     * @param products list of products to calculate pricing
     */
    private int calculateSubtotal(List<LineItems> products) {
        int subtotal = 0;

        for (LineItems lineItem : products) {
            subtotal += lineItem.getPricePerUnit() * lineItem.getQuantity();
        }

        return subtotal;
    }

    /**
     * Convert cents to dollars
     * @param amount in cents
     * @return dollar amount
     */
    private float fromCentsToDollars(Integer amount) {
        return amount.floatValue() / 100;
    }

    /**
     * Checks if the date given is older than the current date + TTL
     *
     * @param date date to check
     * @return boolean indicating status of product data
     */
    private Boolean productDataExpired(Date date) {
        if (date == null) {
            return true;
        }

        Date currentDate = new Date();
        return currentDate.getTime() - date.getTime() > cartPricingAvailabilityTTL;
    }

    /**
     * Add an item to the given cart id
     *
     * @param cartId cart to add to
     * @param itemsInfoDTO items to add
     * @param erpUserInformation info about the useitemsInfoDTO
     * @param isEmployee flag when called by a reece employee
     * @returns updated cart
     */
    @Transactional
    public CartResponseDTO addItems(
        UUID cartId,
        ItemsInfoDTO itemsInfoDTO,
        ErpUserInformation erpUserInformation,
        Boolean isEmployee
    )
        throws CartNotFoundException, QtyIncrementInvalidException, ElasticsearchException, EclipseException, AddItemsToCartFoundException {
        Cart cart = cartDAO.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId.toString()));

        var lineItems = new ArrayList<LineItems>();

        for (ItemInfoDTO itemInfo : itemsInfoDTO.getItemInfoList()) {
            if (itemInfo.getMinIncrementQty() != 0 && itemInfo.getQty() % itemInfo.getMinIncrementQty() != 0) {
                throw new QtyIncrementInvalidException(itemInfo.getMinIncrementQty());
            }

            Optional<LineItems> existingItem = lineItemsDAO.findByErpPartNumberAndCartId(
                itemInfo.getProductId(),
                cartId
            );

            if (existingItem.isEmpty()) {
                LineItems newItem = new LineItems();
                newItem.setCartId(cartId);
                newItem.setErpPartNumber(itemInfo.getProductId());
                newItem.setQuantity(itemInfo.getQty());
                BigDecimal priceBd = new BigDecimal(itemInfo.getPricePerUnit()).setScale(2, RoundingMode.HALF_UP);
                newItem.setPricePerUnit(
                    (int) BigDecimal.valueOf(priceBd.floatValue() * 100).setScale(2, RoundingMode.HALF_UP).floatValue()
                );
                newItem.setQtyAvailable(itemInfo.getQtyAvailable());
                newItem.setUom(itemInfo.getUom());
                Date currentDate = new Date();
                newItem.setQtyAvailableLastUpdatedAt(currentDate);
                newItem.setPriceLastUpdatedAt(currentDate);
                lineItems.add(newItem);
            } else {
                existingItem.get().setQuantity(existingItem.get().getQuantity() + itemInfo.getQty());
                lineItemsDAO.save(existingItem.get());
            }
        }

        if (lineItems.size() > 0) {
            var existingLineItems = lineItemsDAO.findAllByCartId(cartId);
            int existingLineItemsSize = 0;

            if (existingLineItems != null) {
                existingLineItemsSize = existingLineItems.size();
            }

            if ((existingLineItemsSize + lineItems.size()) <= MAX_CART_SIZE) {
                lineItemsDAO.saveAll(lineItems);
            } else {
                throw new AddItemsToCartFoundException();
            }
        }

        List<LineItems> products = lineItemsDAO.findAllSortedLineItem(cartId);
        cart.setSubtotal(calculateSubtotal(products));
        cart.setProducts(products);

        return buildCartResponseDto(cart, erpUserInformation);
    }

    //Add all items from list to cart
    @Transactional
    public CartResponseDTO addAllListItemsToCart(
        UUID cartId,
        UUID listId,
        ItemsInfoDTO itemsInfoDto,
        Boolean isEmployee
    ) throws ListNotFoundException, CartNotFoundException, AddItemsToCartFoundException, AddItemsToCartDataException {
        var maybeList = listsDAO.findById(listId).orElseThrow(() -> new ListNotFoundException(listId.toString()));

        Cart cart = cartDAO.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId.toString()));

        var listLineItems = maybeList.getListLineItems();

        ItemsInfoDTO listItemsInfoDto = new ItemsInfoDTO();
        var itemInfoList = new ArrayList<ItemInfoDTO>();

        listLineItems.forEach(lineItem -> {
            ItemInfoDTO itemToAdd = new ItemInfoDTO();
            itemToAdd.setQty(lineItem.getQuantity());
            itemToAdd.setProductId(lineItem.getErpPartNumber());
            itemInfoList.add(itemToAdd);
        });

        listItemsInfoDto.setItemInfoList(itemInfoList);
        var lineItems = new ArrayList<LineItems>();

        for (ItemInfoDTO itemInfo : listItemsInfoDto.getItemInfoList()) {
            Optional<LineItems> existingItem = lineItemsDAO.findByErpPartNumberAndCartId(
                itemInfo.getProductId(),
                cartId
            );

            if (existingItem.isEmpty()) {
                //TODO: Fix test for new exceptions/flows
                ItemInfoDTO newProductItemInfo = itemsInfoDto
                    .getItemInfoList()
                    .stream()
                    .filter(productItem -> itemInfo.getProductId().equals(productItem.getProductId()))
                    .findFirst()
                    .orElseThrow(() -> new AddItemsToCartDataException(itemInfo.getProductId()));

                LineItems newItem = new LineItems();
                newItem.setCartId(cartId);
                newItem.setErpPartNumber(itemInfo.getProductId());
                newItem.setQuantity(itemInfo.getQty());

                BigDecimal priceBd = new BigDecimal(newProductItemInfo.getPricePerUnit())
                    .setScale(2, RoundingMode.HALF_UP);
                newItem.setPricePerUnit(
                    (int) BigDecimal.valueOf(priceBd.floatValue() * 100).setScale(2, RoundingMode.HALF_UP).floatValue()
                );
                newItem.setQtyAvailable(newProductItemInfo.getQtyAvailable());
                newItem.setUom(newProductItemInfo.getUom());
                Date currentDate = new Date();
                newItem.setQtyAvailableLastUpdatedAt(currentDate);
                newItem.setPriceLastUpdatedAt(currentDate);
                lineItems.add(newItem);
            } else {
                existingItem.get().setQuantity(existingItem.get().getQuantity() + itemInfo.getQty());
                lineItemsDAO.save(existingItem.get());
            }
        }

        if (lineItems.size() > 0) {
            var existingLineItems = lineItemsDAO.findAllByCartId(cartId);
            int existingLineItemsSize = 0;

            if (existingLineItems != null) {
                existingLineItemsSize = existingLineItems.size();
            }

            if ((existingLineItemsSize + lineItems.size()) <= MAX_CART_SIZE) {
                lineItemsDAO.saveAll(lineItems);
            } else {
                throw new AddItemsToCartFoundException();
            }
        }

        List<LineItems> products = lineItemsDAO.findAllSortedLineItem(cartId);
        cart.setSubtotal(calculateSubtotal(products));
        cart.setProducts(products);

        return buildCartResponseDto(cart, itemsInfoDto.getErpUserInformation());
    }

    private List<LineItems> saveCartLineItems(
        List<LineItems> cartItems,
        Cart cart,
        ErpUserInformation erpUserInformation,
        Boolean overrideTtl
    ) {
        List<String> productIds = new ArrayList<>();
        if (ERPSystem.isEclipseUser(erpUserInformation)) {
            for (LineItems lineItems : cartItems) {
                if (
                    overrideTtl ||
                    productDataExpired(lineItems.getPriceLastUpdatedAt()) ||
                    productDataExpired(lineItems.getQtyAvailableLastUpdatedAt())
                ) {
                    productIds.add(lineItems.getErpPartNumber());
                }
            }
            if (!productIds.isEmpty()) {
                var shippingBranch = branchesService.getBranch(cart.getShippingBranchId());

                ProductPricingRequestDTO requestDTO = new ProductPricingRequestDTO();
                requestDTO.setBranchId(cart.getShippingBranchId());
                requestDTO.setCustomerId(erpUserInformation.getErpAccountId());
                requestDTO.setIncludeListData(true);
                requestDTO.setProductIds(productIds);

                ProductPricingResponseDTO response = productService.getPricing(requestDTO);

                List<ProductPricingDTO> allProducts = response.getProducts();
                for (var p : allProducts) {
                    LineItems lineItems = cartItems
                        .stream()
                        .filter(lineItem -> lineItem.getErpPartNumber().equals(p.getProductId()))
                        .findFirst()
                        .get();
                    BigDecimal priceBd = p.getSellPrice().setScale(2, RoundingMode.HALF_UP);
                    lineItems.setPricePerUnit(
                        (int) BigDecimal
                            .valueOf(priceBd.floatValue() * 100)
                            .setScale(2, RoundingMode.HALF_UP)
                            .floatValue()
                    );
                    // If pricing only shipping branch, set quantity to total available qty
                    if (shippingBranch.getIsPricingOnly()) {
                        lineItems.setQtyAvailable(p.getTotalAvailableQty());
                    } else {
                        lineItems.setQtyAvailable(p.getBranchAvailableQty());
                    }
                    Date currentDate = new Date();
                    lineItems.setQtyAvailableLastUpdatedAt(currentDate);
                    lineItems.setPriceLastUpdatedAt(currentDate);
                    lineItems.setUom(p.getOrderUom());
                    lineItems.setUmqt(p.getOrderPerQty().toString());
                }
                lineItemsDAO.saveAll(cartItems);
            }
        }
        return cartItems;
    }

    /**
     * Update item quantity for a given cart and item
     *
     * @param cartId cart item is associated with
     * @param itemId item to update quantity for
     * @param itemQuantityErpInformation user information for fetching availability and pricing and quantity to update
     * @return updated cart
     */
    @Transactional
    public UpdateItemQtyResponseDTO updateItemQuantity(
        UUID cartId,
        UUID itemId,
        ItemQuantityErpInformation itemQuantityErpInformation
    ) throws ItemNotFoundException, QtyIncrementInvalidException, ElasticsearchException, EclipseException {
        LineItems lineItem = lineItemsDAO
            .findByIdAndCartId(itemId, cartId)
            .orElseThrow(() -> new ItemNotFoundException(itemId.toString(), cartId.toString()));
        if (
            itemQuantityErpInformation.getMinIncrementQty() != 0 &&
            itemQuantityErpInformation.getQty() % itemQuantityErpInformation.getMinIncrementQty() != 0
        ) {
            throw new QtyIncrementInvalidException(itemQuantityErpInformation.getMinIncrementQty());
        }

        lineItem.setQuantity(itemQuantityErpInformation.getQty());

        lineItemsDAO.save(lineItem);

        UpdateItemQtyResponseDTO updateItemQtyResponseDTO = new UpdateItemQtyResponseDTO();

        LineItemResponseDTO product = new LineItemResponseDTO(lineItem);

        /* build and set the listIds field of the lineItemResponseDTO, needed for the "added to list" yellow star
        to persist after updating item quantity */
        List<String> partNumbers = new ArrayList<>();
        if (itemQuantityErpInformation.getErpUserInformation() != null) {
            partNumbers.add(product.getErpPartNumber());
            val erp = ErpEnum.valueOf(itemQuantityErpInformation.getErpUserInformation().getErpSystemName());
            var listIdsPerPartNumber = productService.getAllListIdsByPartNumbersAndErpAccountId(
                partNumbers,
                itemQuantityErpInformation.getErpUserInformation().getErpAccountId(),
                erp
            );
            var listIds = new ArrayList<String>();
            listIdsPerPartNumber
                .stream()
                .filter(p -> p[0].equals(product.getErpPartNumber()))
                .forEach(p -> {
                    listIds.addAll(Arrays.asList(p[1].split(",")));
                });

            product.setListIds(listIds);
        }

        //add elastic productDTO data to lineItemResponseDTO
        ERPSystem erpSystem = ERPSystem.valueOf(itemQuantityErpInformation.getErpUserInformation().getErpSystemName());
        List<String> prodNums = new ArrayList<>();
        prodNums.add(product.getErpPartNumber());
        try {
            var productDTO = productService.getProductsByNumber(prodNums, erpSystem);
            product.setProduct(productDTO.get(0));
        } catch (JsonProcessingException | ElasticsearchException exception) {
            return null;
        }

        //calculate new subtotal
        List<LineItems> products = lineItemsDAO.findAllByCartIdOrderByIdAsc(cartId);
        BigDecimal subtotal = BigDecimal.valueOf(calculateSubtotal(products));

        //set response
        updateItemQtyResponseDTO.setProduct(product);
        updateItemQtyResponseDTO.setSubtotal(subtotal);

        if (itemQuantityErpInformation.getErpUserInformation().getCustomerNumber() != null
                && !itemQuantityErpInformation.getErpUserInformation().getCustomerNumber().isEmpty()) {
            try {
                updateItemQtyResponseDTO
                        .getProduct()
                        .getProduct()
                         .setCustomerPartNumbers(updateItemQtyResponseDTO.getProduct().getProduct().getCustomerPartNumbers(), itemQuantityErpInformation.getErpUserInformation().getCustomerNumber())
                        ;
            } catch (Exception e) {}
        }

        return updateItemQtyResponseDTO;
    }

    /**
     * Remove an item associated by given item id from the given cart id
     *
     * @param cartId cart to remove item from
     * @param itemId item to remove
     * @return update cart
     */
    @Transactional
    public CartResponseDTO deleteItem(UUID cartId, UUID itemId, ErpUserInformation erpUserInformation)
        throws ItemNotFoundException, EclipseException, CartNotFoundException {
        lineItemsDAO
            .findByIdAndCartId(itemId, cartId)
            .orElseThrow(() -> new ItemNotFoundException(itemId.toString(), cartId.toString()));
        lineItemsDAO.deleteById(itemId);

        return getCart(cartId, true, erpUserInformation);
    }

    /**
     * Create a new delivery with information in DTO for the given cartId
     *
     * @param cartId cartId to create delivery for
     * @param deliveryDTO delivery information
     * @return cart with delivery created
     * @throws CartNotFoundException thrown when cart with given id does not exist
     */
    @Transactional
    public Cart createDelivery(UUID cartId, DeliveryDTO deliveryDTO) throws CartNotFoundException {
        Cart cart = cartDAO.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId.toString()));

        Address address = deliveryDTO.getAddress();
        address = addressDAO.save(address);
        deliveryDTO.setAddress(address);
        Delivery delivery = new Delivery(deliveryDTO);

        delivery = deliveryDAO.save(delivery);

        cart.setDelivery(delivery);
        return cartDAO.save(cart);
    }

    /**
     * Create a new will call with information in DTO for the given cartId
     *
     * @param cartId cartId to create will call for
     * @param willCallDTO will call information
     * @return cart with will call created
     * @throws CartNotFoundException thrown when cart with given id does not exist
     */
    @Transactional
    public Cart createWillCall(UUID cartId, WillCallDTO willCallDTO) throws CartNotFoundException {
        Cart cart = cartDAO.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId.toString()));
        WillCall willCall = new WillCall(willCallDTO);
        willCall = willCallDAO.save(willCall);

        cart.setWillCall(willCall);
        return cartDAO.save(cart);
    }

    /**
     * Update an existing delivery on the given cartId with information given
     *
     * @param cartId cartId to update delivery for
     * @param updatedDelivery delivery information to update
     * @return cart with updated delivery
     * @throws CartNotFoundException thrown when cart with given id does not exist
     * @throws DeliveryNotFoundException thrown when delivery for the given cartId has not been created
     */
    @Transactional
    public Cart updateDelivery(UUID cartId, DeliveryDTO updatedDelivery)
        throws CartNotFoundException, DeliveryNotFoundException {
        Cart cart = cartDAO.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId.toString()));

        if (cart.getDelivery() == null) {
            throw new DeliveryNotFoundException(cartId.toString());
        }
        Delivery delivery = deliveryDAO.getOne(cart.getDelivery().getId());

        if (updatedDelivery.getPreferredTime() != null) {
            delivery.setPreferredTime(updatedDelivery.getPreferredTime());
        }

        Address address = null;
        if (updatedDelivery.getAddress() != null) {
            address = addressDAO.getOne(delivery.getAddress().getId());
            Address updatedAddress = updatedDelivery.getAddress();

            if (updatedAddress.getCompanyName() != null) {
                address.setCompanyName(updatedAddress.getCompanyName());
            }

            if (updatedAddress.getStreet1() != null) {
                address.setStreet1(updatedAddress.getStreet1());
            }

            if (updatedAddress.getStreet2() != null) {
                address.setStreet2(updatedAddress.getStreet2());
            }

            if (updatedAddress.getCity() != null) {
                address.setCity(updatedAddress.getCity());
            }

            if (updatedAddress.getCountry() != null) {
                address.setCountry(updatedAddress.getCountry());
            }

            if (updatedAddress.getState() != null) {
                address.setState(updatedAddress.getState());
            }

            if (updatedAddress.getZip() != null) {
                address.setZip(updatedAddress.getZip());
            }

            if (updatedAddress.getCustom() != null) {
                address.setCustom(updatedAddress.getCustom());
            }
        }

        if (updatedDelivery.getPhoneNumber() != null) {
            delivery.setPhoneNumber(updatedDelivery.getPhoneNumber());
        }

        if (updatedDelivery.getDeliveryInstructions() != null) {
            delivery.setDeliveryInstructions(updatedDelivery.getDeliveryInstructions());
        }

        if (updatedDelivery.getPreferredDate() != null) {
            delivery.setPreferredDate(updatedDelivery.getPreferredDate());
        }

        if (updatedDelivery.getShipTo() != null) {
            delivery.setShipToId(updatedDelivery.getShipTo());
        }
        delivery.setShouldShipFullOrder(updatedDelivery.isShouldShipFullOrder());

        deliveryDAO.save(delivery);

        if (address != null) {
            addressDAO.save(address);
        }

        return cart;
    }

    /**
     * Update an existing will call on the given cartId with the information given
     *
     * @param cartId cartId to update will call for
     * @param updatedWillCall will call information to update
     * @return cart with updated will call
     * @throws CartNotFoundException thrown when cart with given id does not exist
     * @throws WillCallNotFoundException thrown when will call for the given cart id has not been created
     */
    @Transactional
    public Cart updateWillCall(UUID cartId, WillCallDTO updatedWillCall)
        throws CartNotFoundException, WillCallNotFoundException {
        Cart cart = cartDAO.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId.toString()));

        if (cart.getWillCall() == null) {
            throw new WillCallNotFoundException(cartId.toString());
        }
        WillCall willCall = willCallDAO.getOne(cart.getWillCall().getId());

        if (updatedWillCall.getBranchId() != null) {
            willCall.setBranchId(updatedWillCall.getBranchId());
        }

        if (updatedWillCall.getPickupInstructions() != null) {
            willCall.setPickupInstructions(updatedWillCall.getPickupInstructions());
        }

        if (updatedWillCall.getPreferredDate() != null) {
            willCall.setPreferredDate(updatedWillCall.getPreferredDate());
        }

        if (updatedWillCall.getPreferredTime() != null) {
            willCall.setPreferredTime(updatedWillCall.getPreferredTime());
        }

        willCallDAO.save(willCall);

        return cart;
    }

    /**
     * Submit an order for the given cart id.
     *
     * @param cartId cart id associated with order
     * @param submitOrderDTO erp credentials and bill to account ID
     * @return order response object created
     * @throws CartNotFoundException thrown when cart id given doesn't exist
     * @throws DeliveryAndWillCallNotFoundException thrown when the cart does not have a will-call or delivery order to submit
     * @throws BranchNotFoundPricingAndAvailabilityException thrown when the cart's branch isn't found in list of availability branches from eclipse
     */
    @Transactional
    public GetOrderResponseDTO submitOrder(UUID cartId, SubmitOrderDTO submitOrderDTO, String authorization)
        throws CartNotFoundException, DeliveryAndWillCallNotFoundException, BranchNotFoundPricingAndAvailabilityException, ElasticsearchException, UserNotFoundException, EclipseException, UserUnauthorizedException {
        Cart cart = cartDAO.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId.toString()));

        if (cart.getShippingBranchId() == null || cart.getShippingBranchId().isEmpty()) {
            var branchDTO = accountService.getHomeBranch(cart.getShipToId().toString());
            var accountData = accountService.getAccountData(cart.getShipToId().toString(), authorization, branchDTO.getBrand());
            cart.setShippingBranchId(accountData.getBranchId());
        }

        UserDTO user;
        try {
            user = accountService.getUser(cart.getOwnerId().toString(), authorization);
            if (!user.getIsEmployee() && !user.getEcommShipToIds().contains(cart.getShipToId())) {
                throw new UserUnauthorizedException(
                    String.format("User does not have access to the ship to with id: %s", cart.getShipToId().toString())
                );
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new UserNotFoundException(cart.getOwnerId().toString());
            } else {
                throw e;
            }
        }

        CreateSalesOrderRequestDTO salesOrderDTO = createSalesOrder(cart, submitOrderDTO, user.getIsEmployee());

        GetOrderResponseDTO getOrderResponseDTO = erpService.submitSalesOrder(salesOrderDTO);

        UserAccountInfo userAccountInfo = getUserAccountInfo(cart);

        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setErpAccountId(submitOrderDTO.getErpUserInformation().getErpAccountId());
        orderStatus.setOrderId(getOrderResponseDTO.getOrderNumber());
        orderStatus.setWebStatus(WebStatus.valueOf(getOrderResponseDTO.getWebStatus()));
        orderStatus.setShipToId(cart.getShipToId());

        orderStatusDAO.save(orderStatus);

        setProductDetailsOnLineItems(getOrderResponseDTO);

        getOrderResponseDTO
            .getLineItems()
            .forEach(item ->
                item.setUnitPrice(
                    BigDecimal.valueOf(item.getUnitPrice()).setScale(2, RoundingMode.HALF_UP).floatValue()
                )
            );

        getOrderResponseDTO.setOrderTotal(
            BigDecimal.valueOf(getOrderResponseDTO.getOrderTotal()).setScale(2, RoundingMode.HALF_UP).floatValue()
        );

        // TODO: place in queue instead of doing synchronous
        notificationService.sendOrderSubmittedEmail(
            buildOrderNotificationDTO(cart.getShipToId(), getOrderResponseDTO, salesOrderDTO.getBranchId())
        );

        resetCart(cartId, userAccountInfo, authorization);

        return getOrderResponseDTO;
    }

    private UserAccountInfo getUserAccountInfo(Cart cart) {
        UserAccountInfo userAccountInfo = new UserAccountInfo();
        userAccountInfo.setShippingBranchId(cart.getShippingBranchId());
        userAccountInfo.setUserId(cart.getOwnerId());
        userAccountInfo.setShipToAccountId(cart.getShipToId());
        userAccountInfo.setErpSystemName(cart.getErpSystemName());
        return userAccountInfo;
    }

    private void setProductDetailsOnLineItems(GetOrderResponseDTO getOrderResponseDTO) throws ElasticsearchException {
        List<String> productIds = getOrderResponseDTO
            .getLineItems()
            .stream()
            .map(OrderLineItemResponseDTO::getErpPartNumber)
            .collect(Collectors.toList());
        Map<String, ProductDTO> productDTOS = productService
            .getProductsByNumber(productIds)
            .stream()
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(ProductDTO::getPartNumber, p -> p));

        getOrderResponseDTO
            .getLineItems()
            .forEach(l -> {
                var product = productDTOS.get(l.getErpPartNumber());

                if (product != null) {
                    l.setImageUrls(product.getImageUrls());
                    l.setProductName(product.getName());
                    l.setManufacturerName(product.getManufacturerName());
                    l.setManufacturerNumber(product.getManufacturerNumber());
                    l.setProductId(product.getId());
                }
            });
    }

    /**
     * Submit an order preview for the given cart id.
     *
     * @param cartId cart id associated with order
     * @param submitOrderDTO erp credentials and bill to account ID
     * @return order response object created
     * @throws CartNotFoundException thrown when cart id given doesn't exist
     * @throws DeliveryAndWillCallNotFoundException thrown when the cart does not have a will-call or delivery order to submit
     */
    @Transactional
    public GetOrderResponseDTO submitOrderPreview(UUID cartId, SubmitOrderDTO submitOrderDTO, String authorization)
        throws CartNotFoundException, DeliveryAndWillCallNotFoundException, EclipseException, ElasticsearchException, UserUnauthorizedException, InvalidShipViaDuringCheckoutException {
        Cart cart = cartDAO.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId.toString()));

        if (cart.getShippingBranchId() == null || cart.getShippingBranchId().isEmpty()) {
            var branchDTO = accountService.getHomeBranch(cart.getShipToId().toString());
            var accountData = accountService.getAccountData(cart.getShipToId().toString(), authorization, branchDTO.getBrand());
            cart.setShippingBranchId(accountData.getBranchId());
        }

        UserDTO user;
        try {
            user = accountService.getUser(cart.getOwnerId().toString(), authorization);
            if (!user.getIsEmployee() && !user.getEcommShipToIds().contains(cart.getShipToId())) {
                throw new UserUnauthorizedException(
                    String.format("User does not have access to the ship to with id: %s", cart.getShipToId().toString())
                );
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new UserNotFoundException(cart.getOwnerId().toString());
            } else {
                throw e;
            }
        }

        CreateSalesOrderRequestDTO salesOrderDTO = createSalesOrder(cart, submitOrderDTO, user.getIsEmployee());

        GetOrderResponseDTO getOrderResponseDTO;
        try {
            getOrderResponseDTO = erpService.submitSalesOrderPreview(salesOrderDTO);
        } catch (HttpClientErrorException e) {
            if (e.toString().contains(ECLIPSE_SHIP_VIA_ERROR_CODE)) {
                DeliveryMethodErrorDTO deliveryMethodErrorDTO = new DeliveryMethodErrorDTO(
                    salesOrderDTO,
                    cart,
                    user.getEmail()
                );
                String selectedDeliveryMethod = deliveryMethodErrorDTO.getIsDelivery() ? "delivery" : "will call";
                String suggestedDeliveryMethod = deliveryMethodErrorDTO.getIsDelivery() ? "will call" : "delivery";
                notificationService.sendDeliveryOptionMissingFromBranchEmail(deliveryMethodErrorDTO);
                throw new InvalidShipViaDuringCheckoutException(selectedDeliveryMethod, suggestedDeliveryMethod);
            }
            throw e;
        }

        setProductDetailsOnLineItems(getOrderResponseDTO);

        return getOrderResponseDTO;
    }

    /**
     * Submit an order for approval and sends notification email to approver.
     * @param cartId cart for user that is submitting order
     * @param submitOrderDTO erp credentials and bill to account ID
     * @param authorization
     * @return order response object created
     * @throws CartNotFoundException
     * @throws CartAlreadyExistsException
     * @throws DeliveryAndWillCallNotFoundException
     * @throws BranchNotFoundPricingAndAvailabilityException
     * @throws JsonProcessingException
     * @throws ElasticsearchException
     */
    @Transactional
    public GetOrderResponseDTO submitOrderForApproval(UUID cartId, SubmitOrderDTO submitOrderDTO, String authorization)
        throws CartNotFoundException, CartAlreadyExistsException, DeliveryAndWillCallNotFoundException, BranchNotFoundPricingAndAvailabilityException, UserNotFoundException, EclipseException, ElasticsearchException, JsonProcessingException, UserUnauthorizedException, InvalidShipViaDuringCheckoutException, OrderAlreadyExistsException {
        Cart cart = cartDAO.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId.toString()));

        if (cart.getShippingBranchId() == null || cart.getShippingBranchId().isEmpty()) {
            var branchDTO = accountService.getHomeBranch(cart.getShipToId().toString());
            var accountData = accountService.getAccountData(cart.getShipToId().toString(), authorization, branchDTO.getBrand());
            cart.setShippingBranchId(accountData.getBranchId());
        }

        GetOrderResponseDTO getOrderResponseDTO = submitOrderPreview(cartId, submitOrderDTO, authorization);
        String pendingOrderId = "AA" + Math.abs(cart.getId().hashCode());
        getOrderResponseDTO.setOrderNumber(pendingOrderId);
        getOrderResponseDTO.setWebStatus(ApprovalFlowStateEnum.AWAITING_APPROVAL.getDisplayName());
        getOrderResponseDTO.setCustomerPO(cart.getPoNumber());

        // Validation that order is not a duplicate
        if (ordersPendingApprovalDAO.findByCartId(cartId).isPresent()) {
            throw new OrderAlreadyExistsException(cartId.toString());
        }
        OrdersPendingApproval orderPendingApproval = new OrdersPendingApproval();

        orderPendingApproval.setCart(cart);
        orderPendingApproval.setSubmissionDate(new Date(System.currentTimeMillis()));

        orderPendingApproval.setOrderId(pendingOrderId);
        ordersPendingApprovalDAO.save(orderPendingApproval);
        UUID awaitingApprovalId = approvalFlowStateDAO
            .findByDisplayName(ApprovalFlowStateEnum.AWAITING_APPROVAL.getDisplayName())
            .getId();

        cart.setApprovalState(awaitingApprovalId);
        getOrderResponseDTO
            .getLineItems()
            .forEach(item ->
                item.setUnitPrice(
                    BigDecimal.valueOf(item.getUnitPrice()).setScale(2, RoundingMode.HALF_UP).floatValue()
                )
            );

        cart.setTotal(
            (int) BigDecimal
                .valueOf(getOrderResponseDTO.getOrderTotal() * 100)
                .setScale(2, RoundingMode.HALF_UP)
                .floatValue()
        );
        cart.setSubtotal(
            (int) BigDecimal
                .valueOf(getOrderResponseDTO.getSubTotal() * 100)
                .setScale(2, RoundingMode.HALF_UP)
                .floatValue()
        );
        cart.setTax(
            (int) BigDecimal.valueOf(getOrderResponseDTO.getTax() * 100).setScale(2, RoundingMode.HALF_UP).floatValue()
        );
        cartDAO.save(cart);
        UserAccountInfo userAccountInfo = getUserAccountInfo(cart);

        createCart(userAccountInfo, authorization);

        SalesOrderSubmitNotificationDTO salesOrderSubmitNotificationDTO = buildApproveRejectOrderNotificationDTO(
            cart,
            userAccountInfo.getUserId(),
            getOrderResponseDTO,
            authorization,
            orderPendingApproval
        );

        notificationService.sendOrderApprovalRequestEmail(salesOrderSubmitNotificationDTO);

        return getOrderResponseDTO;
    }

    /**
     * Build DTO for approve/reject emails
     * @param cart cart object for user who submitted order for approval
     * @param userId
     * @param getOrderResponseDTO Order data from Eclipse
     * @param ordersPendingApproval pending approval order data from DB
     * @return SalesOrderSubmitNotificationDTO that will be sent to notifications service
     * @throws BranchNotFoundPricingAndAvailabilityException
     */
    public SalesOrderSubmitNotificationDTO buildApproveRejectOrderNotificationDTO(
        Cart cart,
        UUID userId,
        GetOrderResponseDTO getOrderResponseDTO,
        String authorization,
        OrdersPendingApproval ordersPendingApproval
    ) throws BranchNotFoundPricingAndAvailabilityException {
        BranchDTO branch;
        try {
            branch = accountService.getHomeBranch(cart.getShipToId().toString());
        } catch (Exception e) {
            throw new BranchNotFoundPricingAndAvailabilityException(cart.getShipToId().toString());
        }

        UserDTO user;
        try {
            user = accountService.getUser(userId.toString(), authorization);
        } catch (Exception e) {
            throw new UserNotFoundException(userId);
        }

        UserDTO approver;
        try {
            approver = accountService.getUser(cart.getApproverId().toString(), authorization);
        } catch (Exception e) {
            throw new UserNotFoundException(cart.getApproverId());
        }

        BranchOrderInfoDTO branchOrderInfoDTO = buildBranchOrderInfoDTO(
            getOrderResponseDTO,
            branch,
            cart.getDeliveryMethod().toString()
        );

        getOrderResponseDTO.setBranchInfo(branchOrderInfoDTO);

        SalesOrderSubmitNotificationDTO salesOrderSubmitNotificationDTO = new SalesOrderSubmitNotificationDTO(
            getOrderResponseDTO,
            cart.getDeliveryMethod().equals(DeliveryMethodEnum.DELIVERY.name()),
            branch,
            user.getFirstName(),
            Arrays.asList(user.getEmail(), approver.getEmail())
        );

        salesOrderSubmitNotificationDTO.setPendingApprovalOrderNumber(ordersPendingApproval.getOrderId());

        boolean isRejection = salesOrderSubmitNotificationDTO.getOrderDate() == null;
        if (isRejection) {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            salesOrderSubmitNotificationDTO.setOrderDate(dateFormat.format(ordersPendingApproval.getSubmissionDate()));
        }

        return salesOrderSubmitNotificationDTO;
    }

    /**
     * Build DTO for order status notification emails
     * @param shipToId shipToId for user who submitted order
     * @param getOrderResponseDTO Order data from Eclipse
     * @return SalesOrderSubmitNotificationDTO that will be sent to notifications service
     * @throws BranchNotFoundPricingAndAvailabilityException
     */
    public SalesOrderSubmitNotificationDTO buildOrderNotificationDTO(
        UUID shipToId,
        GetOrderResponseDTO getOrderResponseDTO,
        String branchId
    ) throws BranchNotFoundPricingAndAvailabilityException {
        BranchDTO branch;

        try {
            if (branchId != null) {
                BranchResponseDTO branchResponse = branchesService.getBranch(branchId);
                branch = new BranchDTO(branchResponse);
            } else {
                branch = accountService.getHomeBranch(shipToId.toString());
            }
        } catch (Exception e) {
            throw new BranchNotFoundPricingAndAvailabilityException(shipToId.toString());
        }

        /**
         * TODO: tw - remove this
         * Right now I can't remove this because the fetch user endpoint requires a token for authorization.
         * We need to get moved over to Spring security and then we can update the getUser endpoint to consume
         * an API token instead of just an Okta JWT. That way we can more easily make these cross platform requests.
         */
        User user = userDAO
            .findByEmail(getOrderResponseDTO.getEmail())
            .orElseThrow(() -> new UserNotFoundException(getOrderResponseDTO.getEmail()));

        BranchOrderInfoDTO branchOrderInfoDTO = buildBranchOrderInfoDTO(
            getOrderResponseDTO,
            branch,
            getOrderResponseDTO.getDeliveryMethod()
        );

        getOrderResponseDTO.setBranchInfo(branchOrderInfoDTO);

        SalesOrderSubmitNotificationDTO salesOrderSubmitNotificationDTO = new SalesOrderSubmitNotificationDTO(
            getOrderResponseDTO,
            getOrderResponseDTO.getDeliveryMethod().equals(DeliveryMethodEnum.DELIVERY.name()),
            branch,
            user.getFirstName(),
            Arrays.asList(getOrderResponseDTO.getEmail())
        );

        return salesOrderSubmitNotificationDTO;
    }

    private BranchOrderInfoDTO buildBranchOrderInfoDTO(
        GetOrderResponseDTO getOrderResponseDTO,
        BranchDTO branch,
        String deliveryMethod
    ) {
        BranchOrderInfoDTO branchOrderInfoDTO = getOrderResponseDTO.getBranchInfo() == null
            ? new BranchOrderInfoDTO()
            : getOrderResponseDTO.getBranchInfo();
        branchOrderInfoDTO.setBranchHours(branch.getBusinessHours());
        branchOrderInfoDTO.setBranchPhone(branch.getPhone());

        if (deliveryMethod.equals(DeliveryMethodEnum.DELIVERY.name())) {
            EclipseAddressResponseDTO address = getOrderResponseDTO.getShipAddress();
            branchOrderInfoDTO.setStreetLineOne(address.getStreetLineOne());
            branchOrderInfoDTO.setStreetLineTwo(address.getStreetLineTwo());
            branchOrderInfoDTO.setCity(address.getCity());
            branchOrderInfoDTO.setPostalCode(address.getPostalCode());
            branchOrderInfoDTO.setState(address.getState());
        } else {
            branchOrderInfoDTO.setBranchName(branch.getName());
            branchOrderInfoDTO.setStreetLineOne(branch.getAddress1());
            branchOrderInfoDTO.setStreetLineTwo(branch.getAddress2());
            branchOrderInfoDTO.setCity(branch.getCity());
            branchOrderInfoDTO.setPostalCode(branch.getZip());
            branchOrderInfoDTO.setState(branch.getState());
        }

        return branchOrderInfoDTO;
    }

    /**
     * Admin approves an order that is pending approval.
     * @param cartId user's cart that is pending approval
     * @param submitOrderDTO erp credentials and bill to account ID
     * @param authorization approver's authorization token
     * @return
     * @throws CartNotFoundException
     * @throws DeliveryAndWillCallNotFoundException
     * @throws BranchNotFoundPricingAndAvailabilityException
     * @throws ElasticsearchException
     * @throws JsonProcessingException
     * @throws UnsupportedEncodingException
     * @throws UserNotFoundException
     * @throws EclipseException
     */
    @Transactional
    public GetOrderResponseDTO approveOrder(UUID cartId, SubmitOrderDTO submitOrderDTO, String authorization)
        throws CartNotFoundException, DeliveryAndWillCallNotFoundException, BranchNotFoundPricingAndAvailabilityException, ElasticsearchException, JsonProcessingException, UnsupportedEncodingException, UserNotFoundException, EclipseException {
        Boolean isEmployee = authorizationService.userIsEmployee(authorization);
        Cart cart = cartDAO.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId.toString()));

        CreateSalesOrderRequestDTO salesOrderDTO = createSalesOrder(cart, submitOrderDTO, isEmployee);

        GetOrderResponseDTO getOrderResponseDTO = erpService.submitSalesOrder(salesOrderDTO);

        setProductDetailsOnLineItems(getOrderResponseDTO);

        getOrderResponseDTO
            .getLineItems()
            .forEach(item ->
                item.setUnitPrice(
                    BigDecimal.valueOf(item.getUnitPrice()).setScale(2, RoundingMode.HALF_UP).floatValue()
                )
            );

        OrdersPendingApproval ordersPendingApproval = ordersPendingApprovalDAO.findByCartId(cart.getId()).get();

        SalesOrderSubmitNotificationDTO salesOrderSubmitNotificationDTO = buildApproveRejectOrderNotificationDTO(
            cart,
            cart.getOwnerId(),
            getOrderResponseDTO,
            authorization,
            ordersPendingApproval
        );

        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setErpAccountId(submitOrderDTO.getErpUserInformation().getErpAccountId());
        orderStatus.setOrderId(getOrderResponseDTO.getOrderNumber());
        orderStatus.setWebStatus(WebStatus.valueOf(getOrderResponseDTO.getWebStatus()));
        orderStatus.setShipToId(cart.getShipToId());

        orderStatusDAO.save(orderStatus);

        notificationService.sendOrderApprovedEmail(salesOrderSubmitNotificationDTO);

        UserAccountInfo userAccountInfo = getUserAccountInfo(cart);

        cleanApprovedOrRejectedCartData(cart, userAccountInfo, authorization);

        return getOrderResponseDTO;
    }

    /**
     * Admin rejects an order that is pending approval.
     * @param cartId user's cart that is pending approval
     * @param rejectOrderDTO contains rejection reason and date
     * @param authorization approver's authorization token
     * @return orderId for order that was deleted
     * @throws CartNotFoundException
     * @throws UserNotFoundException
     */
    @Transactional
    public String rejectOrder(UUID cartId, RejectOrderDTO rejectOrderDTO, String authorization)
        throws CartNotFoundException, UserNotFoundException, ElasticsearchException, BranchNotFoundPricingAndAvailabilityException {
        Cart cart = cartDAO.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId.toString()));

        List<LineItems> lineItems = lineItemsDAO.findAllByCartId(cartId);
        GetOrderResponseDTO getOrderResponseDTO = new GetOrderResponseDTO();
        getOrderResponseDTO.setLineItems(
            lineItems.stream().map(OrderLineItemResponseDTO::new).collect(Collectors.toList())
        );

        setProductDetailsOnLineItems(getOrderResponseDTO);

        getOrderResponseDTO.setCustomerPO(cart.getPoNumber());

        getOrderResponseDTO.setSubTotal(fromCentsToDollars(cart.getSubtotal()));
        getOrderResponseDTO.setOrderTotal(fromCentsToDollars(cart.getTotal()));
        getOrderResponseDTO.setTax(fromCentsToDollars(cart.getTax()));
        getOrderResponseDTO
            .getLineItems()
            .forEach(item -> item.setUnitPrice(fromCentsToDollars(Math.round(item.getUnitPrice()))));

        if (cart.getDeliveryMethod().equals(DeliveryMethodEnum.DELIVERY)) {
            getOrderResponseDTO.setShipAddress(new EclipseAddressResponseDTO(cart.getDelivery().getAddress()));
        }

        UserAccountInfo userAccountInfo = getUserAccountInfo(cart);

        OrdersPendingApproval ordersPendingApproval = ordersPendingApprovalDAO.findByCartId(cart.getId()).get();

        SalesOrderSubmitNotificationDTO salesOrderSubmitNotificationDTO = buildApproveRejectOrderNotificationDTO(
            cart,
            cart.getOwnerId(),
            getOrderResponseDTO,
            authorization,
            ordersPendingApproval
        );

        if (!StringUtils.isEmpty(rejectOrderDTO.getRejectionReason())) {
            salesOrderSubmitNotificationDTO.setRejectionReason(rejectOrderDTO.getRejectionReason());
        }

        notificationService.sendOrderRejectedEmail(salesOrderSubmitNotificationDTO);

        cleanApprovedOrRejectedCartData(cart, userAccountInfo, authorization);

        return ordersPendingApproval.getOrderId();
    }

    private void cleanApprovedOrRejectedCartData(
        Cart approvedOrRejectedCart,
        UserAccountInfo userAccountInfo,
        String authorization
    ) throws UserNotFoundException, CartNotFoundException {
        ordersPendingApprovalDAO.deleteAllByCartId(approvedOrRejectedCart.getId());
        if (!userHasActiveCart(userAccountInfo.getShipToAccountId(), userAccountInfo.getUserId())) {
            resetCart(approvedOrRejectedCart.getId(), userAccountInfo, authorization);
        } else {
            lineItemsDAO.deleteAllByCartId(approvedOrRejectedCart.getId());
            cartDAO.deleteById(approvedOrRejectedCart.getId());
        }
    }

    /**
     * getOrdersPendingApproval returns orders that are waiting to be approved by the user currently logged in to the system.
     * @param approverId userId for approver
     * @return List of OrderPendingApprovalDTO
     * @throws UserNotFoundException when user listed as approver does not exist on user's table
     * @throws PendingOrderNotFoundException when order pending approval that exists on cart table is not found on orders_pending_approval table
     */
    public List<OrderPendingApprovalDTO> getOrdersPendingApproval(UUID approverId, String authorization)
        throws UserNotFoundException, PendingOrderNotFoundException {
        List<OrderPendingApprovalDTO> orderPendingApprovalDTOList = new ArrayList<>();
        UUID awaitingApprovalId = approvalFlowStateDAO
            .findByDisplayName(ApprovalFlowStateEnum.AWAITING_APPROVAL.getDisplayName())
            .getId();
        Optional<List<Cart>> maybeCartWithAwaitingApprovalStatus = cartDAO.findAllByApproverIdAndApprovalState(
            approverId,
            awaitingApprovalId
        );
        if (maybeCartWithAwaitingApprovalStatus.isEmpty()) return orderPendingApprovalDTOList;
        List<Cart> cartWithAwaitingApprovalStatus = maybeCartWithAwaitingApprovalStatus.get();
        for (Cart cart : cartWithAwaitingApprovalStatus) {
            UserDTO owner;
            try {
                owner = accountService.getUser(cart.getOwnerId().toString(), authorization);
            } catch (Exception e) {
                throw new UserNotFoundException(cart.getOwnerId());
            }

            String submittedByNameFull = owner.getFirstName() + " " + owner.getLastName();
            //truncating name due to eclipse/kourier/sql limit of 35 chars including white space
            String submittedByName = org.apache.commons.lang3.StringUtils.substring(
                submittedByNameFull,
                0,
                MAX_ORDERED_BY_LENGTH
            );
            Optional<OrdersPendingApproval> maybeOrderPendingApproval = ordersPendingApprovalDAO.findByCartId(
                cart.getId()
            );
            if (maybeOrderPendingApproval.isEmpty()) throw new PendingOrderNotFoundException(cart.getId());
            OrdersPendingApproval orderPendingApproval = maybeOrderPendingApproval.get();
            orderPendingApprovalDTOList.add(new OrderPendingApprovalDTO(cart, orderPendingApproval, submittedByName));
        }
        return orderPendingApprovalDTOList;
    }

    /**
     * getOrderPendingApproval returns an order that is waiting to be reviewed by the user currently logged in to the system.
     * @param orderId pending order identifier
     * @return details for order pending approval
     * @throws UserNotFoundException when user listed as approver does not exist on user's table
     * @throws PendingOrderNotFoundException when order pending approval that exists on cart table is not found on orders_pending_approval table
     */
    public OrderPendingApprovalDTO getOrderPendingApproval(String orderId, String authorization)
        throws UserNotFoundException, PendingOrderNotFoundException {
        OrdersPendingApproval orderPendingApproval = ordersPendingApprovalDAO
            .findByOrderId(orderId)
            .orElseThrow(() -> new PendingOrderNotFoundException(orderId));
        Cart cartPendingApproval = orderPendingApproval.getCart();

        UserDTO owner;
        try {
            owner = accountService.getUser(cartPendingApproval.getOwnerId().toString(), authorization);
        } catch (Exception e) {
            throw new UserNotFoundException(cartPendingApproval.getOwnerId());
        }

        String submittedByNameFull = owner.getFirstName() + " " + owner.getLastName();
        //truncated because of 35 char limitation with eclipse/kourier/sql
        String submittedByName = org.apache.commons.lang3.StringUtils.substring(
            submittedByNameFull,
            0,
            MAX_ORDERED_BY_LENGTH
        );
        return new OrderPendingApprovalDTO(cartPendingApproval, orderPendingApproval, submittedByName);
    }

    /**
     * Create a sales order DTO from the given cart and user credentials to submit to Eclipse
     *
     * @param cart cart to create DTO from
     * @param submitOrderDTO erp credentials and bill to account ID
     * @param isEmployee whether user has Morsco employee permissions
     * @return DTO with order information
     * @throws DeliveryAndWillCallNotFoundException thrown when the cart does not have a will-call or delivery order to submit
     * @throws CartNotFoundException thrown when cart id given doesn't exist
     * @throws BranchNotFoundPricingAndAvailabilityException thrown when the cart's branch isn't found in list of availability branches from eclipse
     */
    @Transactional
    public CreateSalesOrderRequestDTO createSalesOrder(Cart cart, SubmitOrderDTO submitOrderDTO, Boolean isEmployee)
        throws DeliveryAndWillCallNotFoundException, CartNotFoundException, EclipseException {
        CreateSalesOrderRequestDTO salesOrderDTO = new CreateSalesOrderRequestDTO();
        boolean isDelivery = cart.getDeliveryMethod().equals(DeliveryMethodEnum.DELIVERY);
        if (isDelivery) {
            Delivery delivery = deliveryDAO.getOne(cart.getDelivery().getId());
            Address address = addressDAO.getOne(delivery.getAddress().getId());
            salesOrderDTO.setAddress(new EclipseAddressRequestDTO(address));
            salesOrderDTO.setInstructions(delivery.getDeliveryInstructions());
            salesOrderDTO.setPreferredDate(delivery.getPreferredDate());
            salesOrderDTO.setPreferredTime(delivery.getPreferredTime());
            salesOrderDTO.setShouldShipFullOrder(delivery.isShouldShipFullOrder());
            salesOrderDTO.setPhoneNumber(delivery.getPhoneNumber());
        } else {
            if (cart.getWillCall() == null) {
                throw new DeliveryAndWillCallNotFoundException(cart.getId().toString());
            }
            WillCall willCall = willCallDAO.getOne(cart.getWillCall().getId());
            salesOrderDTO.setPreferredDate(willCall.getPreferredDate());
            salesOrderDTO.setPreferredTime(willCall.getPreferredTime());
            salesOrderDTO.setInstructions(willCall.getPickupInstructions());
            salesOrderDTO.setBranchId(willCall.getBranchId());
        }

        if (cart.getPaymentMethodType().equals(PaymentMethodTypeEnum.CREDITCARD)) {
            salesOrderDTO.setCreditCard(cart.getCreditCard());
        }
        salesOrderDTO.setOrderedBy(
            org.apache.commons.lang3.StringUtils.substring(
                submitOrderDTO.getErpUserInformation().getName(),
                0,
                MAX_ORDERED_BY_LENGTH
            )
        );
        salesOrderDTO.setShipToEntityId(submitOrderDTO.getErpUserInformation().getErpAccountId());
        salesOrderDTO.setEclipseLoginId(submitOrderDTO.getErpUserInformation().getUserId());
        salesOrderDTO.setEclipsePassword(submitOrderDTO.getErpUserInformation().getPassword());
        salesOrderDTO.setBillToEntityId(submitOrderDTO.getBillToAccountId());
        salesOrderDTO.setIsDelivery(isDelivery);
        salesOrderDTO.setLineItems(
            getLineItems(cart.getId(), submitOrderDTO.getErpUserInformation(), true)
                .stream()
                .map(LineItemDTO::new)
                .collect(Collectors.toList())
        );
        salesOrderDTO.setPoNumber(cart.getPoNumber());

        return salesOrderDTO;
    }

    /**
     * Update the branch on the will call and cart object
     *
     * @param cartId cart to update
     * @param branchId branch to update will call and cart with
     * @return cart object with updated branch id
     */
    @Transactional
    public CartResponseDTO updateWillCallBranch(
        UUID cartId,
        String branchId,
        ErpUserInformation erpUserInformation,
        Boolean isEmployee
    ) throws CartNotFoundException, WillCallNotFoundException {
        Cart cart = cartDAO.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId.toString()));
        WillCall willCall = cart.getWillCall();
        if (willCall == null) {
            throw new WillCallNotFoundException(cartId.toString());
        }

        cart.setShippingBranchId(branchId);
        willCall.setBranchId(branchId);

        cart = cartDAO.save(cart);
        willCallDAO.save(willCall);

        List<LineItems> products = getLineItems(cartId, erpUserInformation, true);
        cart.setSubtotal(calculateSubtotal(products));
        cart.setProducts(products);

        return buildCartResponseDto(cart, erpUserInformation);
    }

    @Transactional
    public CartResponseDTO removeAllCartItems(UUID cartId) throws CartNotFoundException {
        lineItemsDAO.deleteAllByCartId(cartId);
        Cart cart = cartDAO.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId.toString()));
        return new CartResponseDTO(cart);
    }

    /**
     * Remove credit cart from cart
     *
     * @param cartId cart to update
     * @return updated cart
     */
    @Transactional
    public CartResponseDTO deleteCreditCardFromCart(UUID cartId) throws CartNotFoundException {
        Cart existingCart = cartDAO.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId.toString()));
        existingCart.setCreditCard(null);
        existingCart = cartDAO.save(existingCart);
        return new CartResponseDTO(existingCart);
    }
}
