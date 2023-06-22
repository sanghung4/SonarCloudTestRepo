import { useState, useEffect, useContext } from 'react';

import {
  Box,
  Grid,
  Hidden,
  Image,
  Link,
  Skeleton,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import slugify from 'react-slugify';

import FeedbackButton from 'common/FeedbackButton';
import ItemUnavailable from 'common/ItemUnavailable';
import QtyInput from 'common/QtyInput';
import { PreviouslyPurchasedProduct, ProductPricing } from 'generated/graphql';
import notfound from 'images/notfound.png';
import CheckNearbyBranches from 'PreviouslyPurchasedProducts/CheckNearbyBranches';
import {
  PPPLineItemAtBranch,
  PPPLineItemContainer,
  PPPLineItemLastPurchaseDate
} from 'PreviouslyPurchasedProducts/util/styles';
import AddToListbutton from 'Product/AddToListButton';
import AvailabilityChip from 'Product/AvailabilityChip';
import { BranchContext } from 'providers/BranchProvider';
import { useCartContext } from 'providers/CartProvider';
import { format } from 'utils/currency';
import CustomerPartNumber from 'Search/CustomerPartNumber';
import { WarningIcon } from 'icons';
import AdvancedToolTip from 'common/AdvancedToolTip';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';

type Props = {
  item: PreviouslyPurchasedProduct;
  loading: boolean;
  pricingData?: ProductPricing;
  pricingDataLoading: boolean;
};

function PreviouslyPurchasedProductsLineItem(props: Props) {
  /**
   * Custom hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * State
   */
  const [quantity, setQuantity] = useState(
    props?.item?.product?.minIncrementQty || 1
  );
  const [availableInList, setAvailableInList] = useState<string[]>([]);

  /**
   * Context
   */
  const { addItemToCart, disableAddToCart } = useCartContext();
  const { shippingBranch, setBranchSelectOpen, setProductId } =
    useContext(BranchContext);

  const { selectedAccounts } = useSelectedAccountsContext();

  /**
   * Effect
   */
  useEffect(handleAvailableInList, [props.pricingData]);

  /**
   * Callbacks
   */
  const updatedAddedToLists = (value: string[]) => {
    setAvailableInList(value);
  };

  /**
   * Other values
   */
  const productSlug = slugify(
    `${props.item?.product?.manufacturerName}-${props.item?.product?.name}-${props.item?.product?.manufacturerNumber}`
  );

  const stockQty = shippingBranch?.isPricingOnly
    ? props.pricingData?.totalAvailableQty
    : props.pricingData?.branchAvailableQty;

  const showAvailabilityChip =
    shippingBranch?.isPricingOnly ||
    !!props.pricingData?.branchAvailableQty ||
    (!props.pricingData?.totalAvailableQty &&
      !props.pricingData?.branchAvailableQty);

  /**
   * Render
   */
  return (
    <PPPLineItemContainer
      container
      spacing={2}
      data-testid={`row_${props.item?.product?.id}`}
    >
      {/* 1st half */}
      <Grid item container xs={isSmallScreen ? 12 : 7}>
        {/* Thumbnail */}
        <Grid item xs={isSmallScreen ? 5 : 3}>
          <Hidden mdDown>
            <Box py={1} p={0}>
              {props.loading ? (
                <Skeleton width={90} height={20} variant="rectangular" />
              ) : (
                !!props.pricingData?.sellPrice && (
                  <AddToListbutton
                    availableInList={availableInList}
                    updatedAddedToLists={updatedAddedToLists}
                    partNumber={props.item?.product?.partNumber ?? ''}
                    quantity={quantity}
                  />
                )
              )}
            </Box>
          </Hidden>
          <Link
            href={
              !props.loading && !!Boolean(props.pricingData?.sellPrice)
                ? `/product/${productSlug}/${props.item?.product?.id}`
                : undefined
            }
            data-testid="product-link"
          >
            <Box
              mr={2}
              width="100%"
              height="100%"
              display="flex"
              alignItems="center"
              justifyContent="center"
            >
              {props.loading ? (
                <Skeleton variant="rectangular" height="100%" width="100%" />
              ) : (
                <Image
                  alt={t('common.productPicture')}
                  fallback={notfound}
                  src={props.item?.product?.imageUrls?.thumb ?? ''}
                  data-testid="product-image"
                />
              )}
            </Box>
          </Link>
        </Grid>
        {/* Add to List + Brand, Title, MFR, Last Purchased */}
        <Grid item container xs={isSmallScreen ? 7 : 9}>
          <Box ml={2} width="100%">
            {/** Mobile only */}
            <Hidden mdUp>
              <Box>
                <AddToListbutton
                  availableInList={availableInList}
                  updatedAddedToLists={updatedAddedToLists}
                  partNumber={props.item?.product?.partNumber ?? ''}
                  quantity={quantity}
                />
              </Box>
            </Hidden>
            <Typography
              color="textSecondary"
              variant="caption"
              data-testid="product-mfrName"
              textTransform="uppercase"
            >
              {props.loading ? (
                <Skeleton width={100} />
              ) : (
                props.item?.product?.manufacturerName ?? '-'
              )}
            </Typography>
            <Link
              href={
                !props.loading && !!props.pricingData?.sellPrice
                  ? `/product/${productSlug}/${props.item?.product?.id}`
                  : undefined
              }
              pb={0.5}
            >
              <Typography
                color="primary"
                variant="body1"
                data-testid="product-name"
                fontWeight={isSmallScreen ? 400 : 500}
              >
                {props.loading ? (
                  <>
                    <Skeleton width={isSmallScreen ? '100%' : 200} />
                    <Hidden mdUp>
                      <Skeleton width="100%" />
                    </Hidden>
                  </>
                ) : (
                  props.item?.product?.name ?? '-'
                )}
              </Typography>
            </Link>
            <Typography
              color="textSecondary"
              variant="caption"
              data-testid="product-mfrNumber"
            >
              {props.loading ? (
                <Skeleton width={120} />
              ) : (
                `${t('product.mfr')} ${
                  props.item?.product?.manufacturerNumber ?? '-'
                }`
              )}
              {props.loading ? (
                <Skeleton width={120} />
              ) : (
                props.item?.product?.customerPartNumber && (
                  <CustomerPartNumber
                    partNumber={props.item.product.customerPartNumber}
                    stylingForPage="PPP"
                  />
                )
              )}
            </Typography>
            <Hidden mdUp>
              <Box mr={1} my={props.loading ? 1.5 : 0} color="primary.main">
                <Typography variant="body1" data-testid="product-price-mobile">
                  {props.pricingDataLoading || props.loading ? (
                    <Skeleton width={60} />
                  ) : (
                    <Box display="flex" justifyContent="evenly">
                      <Typography
                        fontWeight={500}
                        marginRight={1}
                        fontSize={18}
                      >
                        {format(props.pricingData?.sellPrice ?? 0)}
                      </Typography>

                      <Typography fontWeight={300} fontSize={18}>
                        {props.pricingData?.orderUom.toLocaleLowerCase() ??
                          t('product.each')}
                      </Typography>
                    </Box>
                  )}
                </Typography>
              </Box>
            </Hidden>
            {/** Desktop Only */}
            {props.loading ? (
              <Skeleton width={90} height={10} variant="rectangular" />
            ) : (
              <Hidden mdDown>
                <Typography color="primary" variant="body2">
                  {t('previouslyPurchasedProducts.lastPurchasedOn')}
                  <Box
                    component="span"
                    fontWeight="fontWeightMedium"
                    data-testid="last-purchase-date"
                    color="primary02.main"
                    ml={0.5}
                  >
                    {new Date(props.item?.lastOrder?.lastDate!).toLocaleString(
                      'default',
                      { day: 'numeric', month: 'long', year: 'numeric' }
                    )}
                  </Box>
                </Typography>
              </Hidden>
            )}
            {!props.pricingData?.sellPrice && !props.loading && (
              <ItemUnavailable />
            )}
            <Hidden mdDown>
              <Grid display="flex" mt={1} gap=".5rem">
                {showAvailabilityChip && (
                  <AvailabilityChip
                    branch={shippingBranch}
                    loading={props.pricingDataLoading}
                    stock={stockQty}
                    data-testid="availability-chip"
                  />
                )}

                <Grid>
                  {props.pricingData?.totalAvailableQty &&
                  props.pricingData?.totalAvailableQty > 0 ? (
                    <Box
                      onClick={() => {
                        setBranchSelectOpen(true);
                        setProductId(props.item?.product?.partNumber ?? '');
                      }}
                    >
                      <CheckNearbyBranches />
                    </Box>
                  ) : (
                    <Box
                      display="flex"
                      fontWeight={400}
                      fontSize=".8rem"
                      marginTop={0.4}
                      lineHeight="1rem"
                    >
                      {t('product.shipmentWillBeAvailable')}
                    </Box>
                  )}
                </Grid>
              </Grid>
            </Hidden>
          </Box>
        </Grid>
        {/**Mobile Only */}
        <Hidden mdUp>
          <Grid>
            {props.loading ? (
              <Skeleton width={90} height={10} variant="rectangular" />
            ) : (
              <Typography color="primary" variant="body2" fontSize="16" mt={1}>
                {t('previouslyPurchasedProducts.lastPurchasedOn')}
                <PPPLineItemLastPurchaseDate
                  component="span"
                  data-testid="last-purchase-date"
                  color="primary02.main"
                >
                  {new Date(props.item?.lastOrder?.lastDate!).toLocaleString(
                    'default',
                    { day: 'numeric', month: 'long', year: 'numeric' }
                  )}
                </PPPLineItemLastPurchaseDate>
              </Typography>
            )}
            {/**Link of branch name */}
            <Grid display="flex" mt={1}>
              {showAvailabilityChip && (
                <AvailabilityChip
                  branch={shippingBranch}
                  loading={props.pricingDataLoading}
                  stock={stockQty}
                  data-testid="availability-chip"
                />
              )}
              {!shippingBranch?.isPricingOnly &&
                !!props.pricingData?.totalAvailableQty &&
                !props.pricingData?.branchAvailableQty && (
                  <CheckNearbyBranches />
                )}
              {!shippingBranch?.isPricingOnly &&
                !!props.pricingData?.branchAvailableQty && (
                  <PPPLineItemAtBranch
                    onClick={() => setBranchSelectOpen(true)}
                  >
                    {t('common.at')} {`${shippingBranch?.name}`},
                    {`${shippingBranch?.city}`}
                    {`${shippingBranch?.state}`}
                  </PPPLineItemAtBranch>
                )}
            </Grid>
          </Grid>
        </Hidden>
      </Grid>

      {/* 2nd half */}
      <Grid
        item
        container
        xs={isSmallScreen ? 12 : 6}
        alignItems="baseline"
        justifyContent="space-between"
        wrap="nowrap"
        direction="row"
      >
        {/** Price + stock chip + qty + add to cart */}
        <Hidden mdDown>
          <Box mr={1} my={props.loading ? 1.5 : 0}>
            <Typography variant="body1" data-testid="product-price-desktop">
              {props.pricingDataLoading || props.loading ? (
                <Skeleton width={60} />
              ) : (
                `${format(props.pricingData?.sellPrice ?? 0)}
                  ${
                    props.pricingData?.orderUom.toLocaleLowerCase() ??
                    t('product.each')
                  }`
              )}
            </Typography>
          </Box>
        </Hidden>

        <Grid display="flex" justifyContent="flex-end" alignItems="flex-start">
          <Grid
            container
            alignItems="center"
            justifyContent="flex-end"
            xs={5}
            px={1}
          >
            {props.loading ? (
              <Skeleton width={60} height={50} variant="rectangular" />
            ) : (
              <QtyInput
                value={
                  quantity <= 1
                    ? props?.item?.product?.minIncrementQty || 1
                    : quantity
                }
                onUpdate={setQuantity}
                max={9999}
                increment={props?.item?.product?.minIncrementQty || 1}
                disabled={props.loading}
                size="small"
                noDebounce
              />
            )}
          </Grid>

          <Grid item xs={6} display="flex">
            <AdvancedToolTip
              title="Warning"
              text={t('cart.maxLimitToolTip')}
              icon={<WarningIcon width={16} height={16} />}
              placement="bottom"
              disabled={disableAddToCart && !isSmallScreen}
            >
              <FeedbackButton
                fullWidth
                testId={`add-to-cart-button`}
                onClick={handleAddToCart}
                disabled={
                  props.loading ||
                  !Boolean(props.pricingData?.sellPrice) ||
                  disableAddToCart
                }
                value={t('common.addToCart')}
                valueDone={t('common.addedToCart')}
                sx={{ height: '37px' }}
              />
            </AdvancedToolTip>
          </Grid>
        </Grid>
      </Grid>
    </PPPLineItemContainer>
  );

  /**
   * Callbacks
   */
  function handleAddToCart() {
    addItemToCart(
      props!.item!.product?.partNumber!,
      quantity,
      props.item?.product?.minIncrementQty!,
      props.pricingData,
      selectedAccounts.billToErpAccount?.erpAccountId ?? undefined
    );
  }

  function handleAvailableInList() {
    const listIdsAvailable = props.pricingData?.listIds ?? [];
    setAvailableInList(listIdsAvailable);
  }
}

export default PreviouslyPurchasedProductsLineItem;
