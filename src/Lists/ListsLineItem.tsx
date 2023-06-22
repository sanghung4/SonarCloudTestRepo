import { useCallback, useContext, useMemo } from 'react';

import {
  Grid,
  Hidden,
  IconButton,
  Image,
  Link,
  Skeleton,
  useScreenSize
} from '@dialexa/reece-component-library';
import { DraggableProvidedDragHandleProps } from 'react-beautiful-dnd';
import Dotdotdot from 'react-dotdotdot';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink } from 'react-router-dom';
import slugify from 'react-slugify';

import FeedbackButton from 'common/FeedbackButton';
import Loader from 'common/Loader';
import QtyInput from 'common/QtyInput';
import { ListLineItem, ProductPricing } from 'generated/graphql';
import { DeleteBinIcon, HandleIcon, WarningIcon } from 'icons';
import notfound from 'images/notfound.png';
import 'Lists/ListsLineItem.scss';
import { BranchContext } from 'providers/BranchProvider';
import { useCartContext } from 'providers/CartProvider';
import AvailabilityChip from 'Product/AvailabilityChip';
import AdvancedToolTip from 'common/AdvancedToolTip';
import { useListsContext } from 'providers/ListsProvider';
import { format } from 'utils/currency';
import ListLineItemAvailability from 'Lists/ListLineItemAvailability';
import CustomerPartNumber from 'Search/CustomerPartNumber';

/**
 * Config
 */
const NOT_AVAILABLE_STATS = [
  'Delete',
  'Purge',
  'NonPDW',
  'Discontinued',
  'Comment',
  'NotInElastic',
  'NotInEclipse'
];

/**
 * Types
 */
type Props = {
  item: ListLineItem;
  loading?: boolean;
  disabled?: boolean;
  updateItem?: (item: ListLineItem) => void;
  priceData?: ProductPricing;
  priceDataLoading?: boolean;
  dragHandleProps?: DraggableProvidedDragHandleProps;
};

/**
 * Component
 */
function ListsLineItem(props: Props) {
  /**
   * Props
   */
  const {
    item,
    loading,
    disabled,
    updateItem,
    priceData,
    priceDataLoading,
    dragHandleProps
  } = props;

  /**
   * Custom hooks
   */
  const { t } = useTranslation();
  const { isSmallScreen } = useScreenSize();

  /**
   * Memos
   */
  const notAvailable = useMemo(notAvailableMemo, [
    priceData,
    priceDataLoading,
    item.status
  ]);

  /**
   * Context
   */
  const { addItemToCart, disableAddToCart } = useCartContext();
  const {
    availableListIds,
    isUpdating,
    toggleItemFromList,
    selectedList,
    setAvailableListIds,
    setSelectedList
  } = useListsContext();
  const { shippingBranch } = useContext(BranchContext);

  /**
   * Callbacks
   */
  const deleteItem = useCallback(deleteItemCb, [
    availableListIds,
    item?.erpPartNumber,
    item.id,
    selectedList,
    setAvailableListIds,
    setSelectedList,
    toggleItemFromList
  ]);
  const handleAddToCart = () => {
    addItemToCart(item.erpPartNumber ?? '', item?.quantity ?? 0, 0, priceData);
  };
  const updateQty = (newValue: number) => {
    updateItem?.({ ...props.item, quantity: newValue } as ListLineItem);
  };

  /**
   * Misc
   */
  // URL
  const toSlug = `${item.manufacturerName}-${item.name}-${item.manufacturerNumber}`;
  const productSlug = slugify(toSlug);
  const productUrl = `/product/${productSlug}/MSC-${item?.erpPartNumber}`;
  // Display
  const uom = priceData?.orderUom?.toLowerCase() ?? t('product.each');
  const pricePerUnit = `${format(priceData?.sellPrice ?? 0)} ${uom}`;
  const manufacturerNumber = item?.manufacturerNumber
    ? `${t('product.mfr')} ${item?.manufacturerNumber}`
    : '-';
  const msc = item?.erpPartNumber
    ? `${t('product.msc')}${item.erpPartNumber}`
    : '-';
  // CSS Util
  const cn = (name: string) => 'lists-line-item' + name;

  /**
   * Render
   */
  return (
    <Grid container className={cn('__container')}>
      {/* 🔵 Loader */}
      {isUpdating === item?.erpPartNumber && priceData === undefined && (
        <Grid item className={cn('__loader')}>
          <Loader backdrop />
        </Grid>
      )}
      {/* 🔵 CHUNK 1 - Reorder Handle, Product Image, Info */}
      <Grid
        container
        item
        xs={12}
        md={7} // UPDATE THIS
        py={0}
        {...dragHandleProps}
        flexWrap="nowrap"
      >
        {/* Reorder Handle */}
        <Grid item xs="auto" className={cn('__reoder-handle-container')}>
          {!disabled && <HandleIcon />}
        </Grid>
        {/* Product Image */}
        <Grid item xs="auto" className={cn('__image-container')}>
          <div
            className={cn('__product--image-subcontainer')}
            data-testid={`product-picture-${item?.erpPartNumber}`}
          >
            {loading ? (
              <Skeleton variant="rectangular" height="100%" width="100%" />
            ) : (
              <Link to={productUrl} component={RouterLink}>
                <Image
                  alt={t('common.productPicture')}
                  fallback={notfound}
                  src={item?.imageUrls?.thumb ?? ''}
                />
              </Link>
            )}
          </div>
        </Grid>
        {/* Product Info */}
        <Grid item xs className={cn('__product--details-container')}>
          {/* Availability */}
          <div className={cn('__hidden-print-block')}>
            <AvailabilityChip
              branch={shippingBranch}
              loading={Boolean(priceDataLoading)}
              stock={
                shippingBranch?.isPricingOnly
                  ? priceData?.totalAvailableQty
                  : priceData?.branchAvailableQty
              }
            />
          </div>
          {/* Main Product Details Container*/}
          <div className={cn('__product--details-block')}>
            {/* Manufacturer name */}
            <span
              className={cn('__product--mfr-name')}
              data-testid={`manufacturer-name-${item?.erpPartNumber}`}
            >
              {loading ? (
                <Skeleton data-testid="loading-manufacturer-name" />
              ) : (
                item?.manufacturerName ?? '-'
              )}
            </span>
            {/* Item name */}
            <div className={cn('__product--item-name')}>
              <span data-testid={`item-name-${item?.erpPartNumber}`}>
                {loading ? (
                  <>
                    <Skeleton />
                    <Hidden mdUp>
                      <Skeleton />
                    </Hidden>
                  </>
                ) : (
                  <Dotdotdot clamp={2}>
                    <Link to={productUrl} component={RouterLink}>
                      {item?.name ?? '-'}
                    </Link>
                  </Dotdotdot>
                )}
              </span>
            </div>
            {/* MFR */}
            <span
              className={cn('__product--mfr-msc')}
              data-testid={`manufacturer-number-${item?.erpPartNumber}`}
            >
              {loading ? <Skeleton /> : manufacturerNumber}
            </span>
            {/* MSC */}
            <span
              className={cn('__product--mfr-msc')}
              data-testid={`product-id-${item?.erpPartNumber}`}
            >
              {loading ? <Skeleton /> : msc}
            </span>
            {loading ? (
              <Skeleton />
            ) : (
              props.item.customerPartNumber && (
                <CustomerPartNumber
                  partNumber={props.item.customerPartNumber}
                  stylingForPage="MyLists"
                />
              )
            )}
          </div>
          {/* [Desktop 🖥️] Availability and branch */}
          <Hidden mdDown>
            <ListLineItemAvailability notAvailable={notAvailable} {...props} />
          </Hidden>
          {/* [MOBILE 📱] Price per unit */}
          <Hidden mdUp>
            <div>
              <span
                className={cn('__product--mobile-price-per-unit')}
                data-testid={`price-per-unit-${item?.erpPartNumber}`}
              >
                {loading || priceDataLoading ? (
                  <Skeleton
                    width={60}
                    data-testid={`price-skeleton-${item?.erpPartNumber}`}
                  />
                ) : (
                  pricePerUnit
                )}
              </span>
            </div>
          </Hidden>
        </Grid>
        {/* [MOBILE 📱] Delete button */}
        <Hidden mdUp>
          <div className={cn('__mobile-delete-box')}>
            <IconButton
              color="primary"
              disabled={Boolean(
                loading ||
                  isUpdating === item?.erpPartNumber ||
                  priceDataLoading
              )}
              onClick={deleteItem}
              data-testid={`delete-button-${item?.erpPartNumber}`}
              size="large"
              sx={{ displayPrint: 'none' }}
            >
              <DeleteBinIcon />
            </IconButton>
          </div>
        </Hidden>
      </Grid>
      {/* 🔵 MOBILE BONUS CHUNK 📱 -  Availability and branch */}
      <Hidden mdUp>
        <Grid xs={12}>
          <ListLineItemAvailability notAvailable={notAvailable} {...props} />
        </Grid>
      </Hidden>
      {/* 🔵 CHUNK 2 - Pricing, Qty, Add to Cart, Delete */}
      <Grid
        container
        item
        xs={12}
        md={5}
        spacing={1}
        alignItems="center"
        justifyContent="flex-start"
        wrap="nowrap"
        direction="row"
      >
        {/* [Desktop 🖥️] Price per unit */}
        <Hidden mdDown>
          <Grid item xs={4} textAlign="center">
            <span data-testid={`price-per-unit-${item?.erpPartNumber}`}>
              {loading || priceDataLoading ? (
                <Skeleton
                  width={60}
                  data-testid={`price-skeleton-${item?.erpPartNumber}`}
                />
              ) : (
                pricePerUnit
              )}
            </span>
          </Grid>
        </Hidden>
        {/* Qty input */}
        <Grid item xs={6} md="auto">
          {loading ? (
            <Skeleton width={125} height={37} variant="rectangular" />
          ) : (
            <QtyInput
              value={item?.quantity || item?.minIncrementQty || 1}
              onUpdate={updateQty}
              max={9999}
              increment={item?.minIncrementQty || 1}
              disabled={
                isUpdating === item?.erpPartNumber || loading || notAvailable
              }
              data-testid="list-line-item-qty-input"
              size="small"
              noDebounce
            />
          )}
        </Grid>
        {/* Add to Cart */}
        <Grid item xs={6} md="auto">
          <AdvancedToolTip
            title="Warning"
            text={t('cart.maxLimitToolTip')}
            icon={<WarningIcon width={16} height={16} />}
            placement="bottom"
            disabled={disableAddToCart && !isSmallScreen}
          >
            <FeedbackButton
              fullWidth
              testId={`${item?.id}-add-to-cart-button`}
              onClick={handleAddToCart}
              disabled={
                loading || priceDataLoading || notAvailable || disableAddToCart
              }
              value={t('common.addToCart')}
              valueDone={t('common.addedToCart')}
              sx={{ displayPrint: 'none', height: '37px' }}
            />
          </AdvancedToolTip>
        </Grid>
        {/* [Desktop 🖥️] Delete button */}
        <Hidden mdDown>
          <Grid item xs="auto" sx={{ displayPrint: 'none' }}>
            <IconButton
              color="primary"
              onClick={deleteItem}
              data-testid={`delete-button-${item?.erpPartNumber}`}
              disabled={
                loading ||
                isUpdating === item?.erpPartNumber ||
                priceDataLoading
              }
              size="small"
            >
              <DeleteBinIcon />
            </IconButton>
          </Grid>
        </Hidden>
      </Grid>
      {/* Pricing - END */}
    </Grid>
  );

  /**
   * Callback Defs
   */
  function deleteItemCb() {
    if (!selectedList) {
      return;
    }

    // Filter List
    const updatedListLineItems = selectedList.listLineItems.filter(
      ({ id }) => id !== item.id
    );
    setSelectedList({ ...selectedList, listLineItems: updatedListLineItems });

    // Remove the current listId from item's availableListIds
    const listIds = availableListIds.filter(
      (listId) => listId !== selectedList.id
    );
    setAvailableListIds(listIds);

    // Call
    toggleItemFromList?.(item?.erpPartNumber ?? '');
  }

  /**
   * Memo Defs
   */
  function notAvailableMemo() {
    const status =
      !priceData && !priceDataLoading ? 'NotInEclipse' : item.status;

    return NOT_AVAILABLE_STATS.includes(status ?? '');
  }
}

export default ListsLineItem;
