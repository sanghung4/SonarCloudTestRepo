package com.reece.platform.products.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reece.platform.products.exceptions.*;
import com.reece.platform.products.model.DTO.*;
import com.reece.platform.products.model.ErpUserInformation;
import com.reece.platform.products.model.ItemQuantityErpInformation;
import com.reece.platform.products.model.UserAccountInfo;
import com.reece.platform.products.model.entity.Cart;
import com.reece.platform.products.service.AuthorizationService;
import com.reece.platform.products.service.CartService;
import com.reece.platform.products.service.OrdersService;
import com.reece.platform.products.utilities.DecodedToken;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final AuthorizationService authorizationService;
    private final CartService cartService;
    private final OrdersService ordersService;

    @Autowired
    public CartController(
        AuthorizationService authorizationService,
        CartService cartService,
        OrdersService ordersService
    ) {
        this.authorizationService = authorizationService;
        this.cartService = cartService;
        this.ordersService = ordersService;
    }

    /**
     * Create a new cart for the given user and account IDs given
     *
     * @param userAccountInfo user and account to create a cart for
     * @return ID of the cart created
     */
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody UUID createCart(
        @RequestBody UserAccountInfo userAccountInfo,
        @RequestHeader(name = "authorization") String authorization
    ) throws CartAlreadyExistsException, UserNotFoundException {
        return cartService.createCart(userAccountInfo, authorization);
    }

    /**
     * Reset a cart to the default values
     *
     * @param userAccountInfo user and account to reset a cart for
     * @param cartId ID of the cart to reset
     * @return reset cart data object
     */
    @PatchMapping("{cartId}/reset")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody CartResponseDTO resetCart(
        @RequestBody UserAccountInfo userAccountInfo,
        @PathVariable UUID cartId,
        @RequestHeader(name = "authorization") String authorization
    ) throws CartNotFoundException, UserNotFoundException {
        return cartService.resetCart(cartId, userAccountInfo, authorization);
    }

    /**
     * Update the cartId given with the request body values
     *
     * @param cartId cart to update
     * @return updated cart data object
     */
    @PatchMapping("{cartId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody CartResponseDTO updateCart(@RequestBody Cart cart, @PathVariable UUID cartId)
        throws CartNotFoundException {
        return cartService.updateCart(cartId, cart);
    }

    /**
     * Delete all carts for a shipto
     * @param deleteCartsByShipToDTO
     * @return
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody CartDeleteResponseDTO deleteUserCarts(
        @RequestBody DeleteCartsRequestDTO deleteCartsByShipToDTO
    ) {
        return cartService.deleteCartsByShipToAccountId(deleteCartsByShipToDTO.getShipToAccountId());
    }

    /**
     * Fetch cart by ID
     *
     * @param cartId cart to fetch
     * @param includeProducts if true, return line item products with all product data
     * @return cart information and product information if includeProducts=true
     */
    @GetMapping("{cartId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody CartResponseDTO getCart(
        @PathVariable UUID cartId,
        @RequestParam(required = false, defaultValue = "false") Boolean includeProducts
    ) throws CartNotFoundException, EclipseException {
        return cartService.getCart(cartId, includeProducts, null);
    }

    /**
     * Refresh cart by ID
     * @param cartId cart to fetch
     * @param erpUserInformation user and account to reset a cart for
     * @return cart information and product information
     */
    @PutMapping("/{cartId}/refresh")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody CartResponseDTO refreshCart(
        @PathVariable UUID cartId,
        @RequestBody ErpUserInformation erpUserInformation,
        @RequestHeader(name = "authorization") String authorization
    ) throws CartNotFoundException, EclipseException {
        var userIsEmployee = authorizationService.userIsEmployee(authorization);
        return cartService.refreshCart(cartId, erpUserInformation, userIsEmployee);
    }

    /**
     * Fetch cart by user and account id
     *
     * @param includeProducts if true, return line item products with all product data
     * @param userId user cart is associated with
     * @param accountId account cart is associated with
     * @param erpAccountId account id in erp system
     * @param erpPassword password in erp system
     * @param erpSystemName name of the erp the account is a part of
     * @param erpUserId user id of the erp system
     * @return cart information and product information if includeProducts=true
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody CartResponseDTO getCart(
        @RequestParam(required = false, defaultValue = "false") boolean includeProducts,
        @RequestParam UUID accountId,
        @RequestParam String erpUserId,
        @RequestParam String erpPassword,
        @RequestParam String erpSystemName,
        @RequestParam String erpAccountId,
        @RequestParam Optional<String> quoteId,
        @RequestParam Optional<UUID> userId,
        @RequestParam Optional<String> branchId
    ) throws CartNotFoundException, EclipseException, ElasticsearchException, JsonProcessingException, ParseException {
        if (quoteId.isPresent() && branchId.isPresent()) {
            // TODO: tw - handle qty available
            var quote = ordersService.getOrderById(
                erpAccountId,
                quoteId.get(),
                "1",
                new ErpUserInformation(erpUserId, erpPassword, erpAccountId, null, erpSystemName,null),
                null,
                branchId.get()
            );
            return cartService.getCartFromQuote(
                quote,
                new ErpUserInformation(erpUserId, erpPassword, erpAccountId, null, erpSystemName, null),
                accountId,
                branchId.get()
            );
        } else {
            return cartService.getCart(
                userId.get(),
                accountId,
                includeProducts,
                new ErpUserInformation(erpUserId, erpPassword, erpAccountId, null, erpSystemName,null)
            );
        }
    }

    /**
     * Add new item to cart
     *
     * @param cartId UUID of cart
     * @param itemsInfoDTO contains ErpUserInformation and a list of item info to add
     * @return the updated Cart
     */
    @PatchMapping("{cartId}/items")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody CartResponseDTO addItem(
        @PathVariable UUID cartId,
        @RequestBody ItemsInfoDTO itemsInfoDTO,
        @RequestHeader(name = "authorization") String authorization
    )
        throws CartNotFoundException, QtyIncrementInvalidException, ElasticsearchException, EclipseException, AddItemsToCartFoundException {
        if (itemsInfoDTO.getErpUserInformation() == null) throw new IllegalArgumentException(
            "ErpUserInformation is required"
        );
        return cartService.addItems(
            cartId,
            itemsInfoDTO,
            itemsInfoDTO.getErpUserInformation(),
            authorizationService.userIsEmployee(authorization)
        );
    }

    //Add all items from list to cart
    @PatchMapping("{cartId}/addAll/{listId}")
    public @ResponseBody ResponseEntity<CartResponseDTO> addAllListItemsToCart(
        @PathVariable UUID cartId,
        @PathVariable UUID listId,
        @RequestBody ItemsInfoDTO itemsInfoDTO,
        @RequestHeader(name = "authorization") String authorization
    ) throws ListNotFoundException, CartNotFoundException, AddItemsToCartFoundException, AddItemsToCartDataException {
        if (itemsInfoDTO == null || itemsInfoDTO.getErpUserInformation() == null) throw new IllegalArgumentException(
            "ErpUserInformation is required"
        );

        var isEmployee = authorizationService.userIsEmployee(authorization);
        CartResponseDTO cart = cartService.addAllListItemsToCart(cartId, listId, itemsInfoDTO, isEmployee);

        return ResponseEntity.ok(cart);
    }

    /**
     * Update an existing item's quantity for given cart
     *
     * @param cartId cart item is associated with
     * @param itemId item to update quantity for
     * @param itemQuantityErpInformation user information for fetching availability and pricing and item quantity to update
     * @return updated cart information
     */
    @PatchMapping("{cartId}/items/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody UpdateItemQtyResponseDTO updateItemQuantity(
        @PathVariable UUID cartId,
        @PathVariable UUID itemId,
        @RequestBody ItemQuantityErpInformation itemQuantityErpInformation
    )
        throws CartNotFoundException, ItemNotFoundException, QtyIncrementInvalidException, ElasticsearchException, EclipseException {
        return cartService.updateItemQuantity(cartId, itemId, itemQuantityErpInformation);
    }

    /**
     * Remove an item from a cart by item id
     *
     * @param cartId cart to remove item from
     * @param itemId item to remove from cart
     * @return updated cart information
     */
    @DeleteMapping("{cartId}/items/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody CartResponseDTO deleteItem(
        @PathVariable UUID cartId,
        @PathVariable UUID itemId,
        @RequestParam String erpUserId,
        @RequestParam String erpPassword,
        @RequestParam String erpSystemName,
        @RequestParam String erpAccountId
    ) throws ItemNotFoundException, CartNotFoundException, EclipseException {
        return cartService.deleteItem(
            cartId,
            itemId,
            new ErpUserInformation(erpUserId, erpPassword, erpAccountId, null, erpSystemName, null)
        );
    }

    /**
     * Create a delivery order with the given cart and delivery information
     *
     * @param cartId cart to create the delivery for
     * @param deliveryDTO delivery information
     * @return cart associated with delivery
     * @throws CartNotFoundException thrown when cartId doesn't exist
     */
    @PostMapping("{cartId}/deliveries")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Cart createDelivery(@PathVariable UUID cartId, @RequestBody DeliveryDTO deliveryDTO)
        throws CartNotFoundException {
        return cartService.createDelivery(cartId, deliveryDTO);
    }

    /**
     * Create a will-call order with the given cart and will-call information
     *
     * @param cartId cart to create the will-call for
     * @param willCallDTO will-call information
     * @return cart associated with will call
     * @throws CartNotFoundException thrown when cartId doesn't exist
     */
    @PostMapping("{cartId}/will-calls")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Cart createWillCall(@PathVariable UUID cartId, @RequestBody WillCallDTO willCallDTO)
        throws CartNotFoundException {
        return cartService.createWillCall(cartId, willCallDTO);
    }

    /**
     * Create a delivery order with the given cart and delivery information
     *
     * @param cartId cart to create the delivery for
     * @return cart associated with delivery
     * @param delivery delivery information to update existing delivery with
     * @throws CartNotFoundException thrown when cartId doesn't exist
     */
    @PatchMapping("{cartId}/deliveries")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Cart updateDelivery(@PathVariable UUID cartId, @RequestBody DeliveryDTO delivery)
        throws CartNotFoundException, DeliveryNotFoundException {
        return cartService.updateDelivery(cartId, delivery);
    }

    /**
     * Update an existing will-call order with the given cart
     *
     * @param cartId cart to create the will-call for
     * @param willCallDTO will call information to update existing will call with
     * @return cart associated with will call
     * @throws CartNotFoundException thrown when cartId doesn't exist
     */
    @PatchMapping("{cartId}/will-calls")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Cart updateWillCall(@PathVariable UUID cartId, @RequestBody WillCallDTO willCallDTO)
        throws CartNotFoundException, WillCallNotFoundException {
        return cartService.updateWillCall(cartId, willCallDTO);
    }

    /**
     * Update the branch on the cart and will call object
     *
     * @param cartId cart to create the will-call for
     * @param branchId branch id to update the cart and will-call with
     * @return cart associated with will call
     * @throws CartNotFoundException thrown when cartId doesn't exist
     */
    @PatchMapping("{cartId}/will-calls/branch/{branchId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody CartResponseDTO updateWillCallBranch(
        @PathVariable UUID cartId,
        @PathVariable String branchId,
        @RequestBody ErpUserInformation erpUserInformation,
        @RequestHeader(name = "authorization") String authorization
    ) throws CartNotFoundException, WillCallNotFoundException {
        return cartService.updateWillCallBranch(
            cartId,
            branchId,
            erpUserInformation,
            authorizationService.userIsEmployee(authorization)
        );
    }

    /**
     * Create an eclipse order from an existing cart
     *
     * @param cartId cart to submit the order for
     * @return status of call
     * @throws CartNotFoundException thrown when cartId doesn't exist
     * @throws DeliveryAndWillCallNotFoundException thrown when the cart does not have a will-call or delivery order to submit
     */
    @PostMapping("{cartId}/order")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody GetOrderResponseDTO submitOrder(
        @PathVariable UUID cartId,
        @RequestBody SubmitOrderDTO submitOrderDTO,
        @RequestHeader(name = "authorization") String authorization
    )
        throws CartNotFoundException, DeliveryAndWillCallNotFoundException, BranchNotFoundPricingAndAvailabilityException, ElasticsearchException, JsonProcessingException, CartAlreadyExistsException, UserNotFoundException, EclipseException, UserUnauthorizedException, InvalidShipViaDuringCheckoutException, OrderAlreadyExistsException {
        if (authorizationService.userCanSubmitOrder(authorization)) {
            return cartService.submitOrder(cartId, submitOrderDTO, authorization);
        } else {
            return cartService.submitOrderForApproval(cartId, submitOrderDTO, authorization);
        }
    }

    /**
     * Create an eclipse order preview from an existing cart
     *
     * @param cartId cart to submit the order preview for
     * @return status of call
     * @throws CartNotFoundException thrown when cartId doesn't exist
     * @throws DeliveryAndWillCallNotFoundException thrown when the cart does not have a will-call or delivery order to submit
     */
    @PostMapping("{cartId}/order/preview")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody GetOrderResponseDTO submitOrderPreview(
        @PathVariable UUID cartId,
        @RequestBody SubmitOrderDTO submitOrderDTO,
        @RequestHeader(name = "authorization") String authorization
    )
        throws CartNotFoundException, DeliveryAndWillCallNotFoundException, EclipseException, ElasticsearchException, UserUnauthorizedException, InvalidShipViaDuringCheckoutException {
        return cartService.submitOrderPreview(cartId, submitOrderDTO, authorization);
    }

    /**
     * getOrdersPendingApproval returns orders that are waiting to be reviewed by the user currently logged in to the system.
     * @param authorization user's auth token
     * @return details for orders pending approval
     * @throws UserNotFoundException if approver isn't found in users table
     * @throws UnsupportedEncodingException if token can't be decoded
     * @throws UserUnauthorizedException if user does not have approve_cart permission
     */
    @GetMapping("orders-pending-approval")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<OrderPendingApprovalDTO> getOrdersPendingApproval(
        @RequestHeader(name = "authorization") String authorization
    ) throws UserNotFoundException, UserUnauthorizedException, PendingOrderNotFoundException {
        if (!authorizationService.userCanApproveOrder(authorization)) {
            throw new UserUnauthorizedException();
        }
        UUID approverId = UUID.fromString(DecodedToken.getDecodedHeader(authorization).getEcommUserId());
        return cartService.getOrdersPendingApproval(approverId, authorization);
    }

    /**
     * Admin approves an order that is pending approval.
     * @param cartId user's cart that is pending approval
     * @param authorization approver's authorization token
     * @return GetOrderResponseDTO
     * @throws CartNotFoundException
     * @throws DeliveryAndWillCallNotFoundException
     * @throws BranchNotFoundPricingAndAvailabilityException
     * @throws UnsupportedEncodingException
     * @throws ElasticsearchException
     * @throws JsonProcessingException
     * @throws UserNotFoundException
     * @throws EclipseException
     * @throws AccountNotFoundException
     */
    @PostMapping("{cartId}/approve")
    public @ResponseBody ResponseEntity<GetOrderResponseDTO> approveOrder(
        @PathVariable UUID cartId,
        @RequestBody SubmitOrderDTO submitOrderDTO,
        @RequestHeader(name = "authorization") String authorization
    )
        throws CartNotFoundException, DeliveryAndWillCallNotFoundException, BranchNotFoundPricingAndAvailabilityException, UnsupportedEncodingException, ElasticsearchException, JsonProcessingException, UserNotFoundException, EclipseException {
        if (authorizationService.userCanApproveOrder(authorization)) {
            GetOrderResponseDTO getOrderResponseDTO = cartService.approveOrder(cartId, submitOrderDTO, authorization);
            return ResponseEntity.ok(getOrderResponseDTO);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Admin rejects an order that is pending approval.
     * @param cartId user's cart that is pending approval
     * @param rejectOrderDTO contains rejection reason and date
     * @param authorization approver's authorization token
     * @return orderId for order that was deleted
     * @throws CartNotFoundException
     * @throws DeliveryAndWillCallNotFoundException
     * @throws BranchNotFoundPricingAndAvailabilityException
     * @throws UnsupportedEncodingException
     * @throws ElasticsearchException
     * @throws JsonProcessingException
     * @throws UserNotFoundException
     * @throws EclipseException
     * @throws AccountNotFoundException
     */
    @PostMapping("{cartId}/reject")
    public @ResponseBody ResponseEntity<String> rejectOrder(
        @PathVariable UUID cartId,
        @RequestBody RejectOrderDTO rejectOrderDTO,
        @RequestHeader(name = "authorization") String authorization
    )
        throws CartNotFoundException, UserNotFoundException, BranchNotFoundPricingAndAvailabilityException, ElasticsearchException {
        if (authorizationService.userCanApproveOrder(authorization)) {
            String rejectedOrderId = cartService.rejectOrder(cartId, rejectOrderDTO, authorization);
            return ResponseEntity.ok(rejectedOrderId);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * getOrderPendingApproval returns an order that is waiting to be reviewed by the user currently logged in to the system.
     * @param authorization user's auth token
     * @return details for order pending approval
     * @throws UserNotFoundException if approver isn't found in users table
     * @throws UnsupportedEncodingException if token can't be decoded
     * @throws UserUnauthorizedException if user does not have approve_cart permission
     */
    @GetMapping("orders-pending-approval/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody OrderPendingApprovalDTO getOrderPendingApproval(
        @PathVariable String orderId,
        @RequestHeader(name = "authorization") String authorization
    ) throws UserNotFoundException, UserUnauthorizedException, PendingOrderNotFoundException {
        if (!authorizationService.userCanApproveOrder(authorization)) {
            throw new UserUnauthorizedException();
        }
        return cartService.getOrderPendingApproval(orderId, authorization);
    }

    /**
     * removeAllCartItems returns a Cart after all items have been deleted.
     * @return details for order pending approval
     * @throws UserNotFoundException if approver isn't found in users table
     * @throws CartNotFoundException if cart isn't found
     */
    @DeleteMapping("remove-cart-items/{cartId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody CartResponseDTO removeAllCartItems(@PathVariable UUID cartId)
        throws UserNotFoundException, CartNotFoundException {
        return cartService.removeAllCartItems(cartId);
    }

    /**
     * delete credit card from cart
     *
     * @param cartId cart to update
     * @return updated cart data object
     */
    @PatchMapping("{cartId}/deleteCreditCard")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody CartResponseDTO deleteCreditCard(@PathVariable UUID cartId)
            throws CartNotFoundException {
        return cartService.deleteCreditCardFromCart(cartId);
    }
}
