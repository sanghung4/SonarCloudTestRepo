import React, { useContext, useEffect, useState } from 'react';
import {
  Box,
  Card,
  CardActions,
  CardContent,
  Grid,
  Image,
  Link,
  Typography,
  Skeleton,
  useScreenSize
} from '@dialexa/reece-component-library';
import Dotdotdot from 'react-dotdotdot';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink } from 'react-router-dom';
import slugify from 'react-slugify';

import { AuthContext } from 'AuthProvider';
import FeedbackButton from 'common/FeedbackButton';
import QtyInput from 'common/QtyInput';
import AdvancedToolTip from 'common/AdvancedToolTip';
import { Product, ProductPricing } from 'generated/graphql';
import useSavings from 'hooks/useSavings';
import { WarningIcon } from 'icons';
import notfound from 'images/notfound.png';
import AddToListbutton from 'Product/AddToListButton';
import AvailabilityChip from 'Product/AvailabilityChip';
import { BranchContext } from 'providers/BranchProvider';
import { useCartContext } from 'providers/CartProvider';
import CustomerPartNumber from 'Search/CustomerPartNumber';
import 'Search/styles/featuredSearch.scss';
import { format } from 'utils/currency';
interface Props {
  product: Product;
  pricing: ProductPricing[];
  pricingLoading: boolean;
  index: number;
  onClick: () => void;
}

function SearchResult(props: Props) {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * State
   */
  const [quantity, setQuantity] = useState(props.product.minIncrementQty || 1);
  const [availableInList, setAvailableInList] = useState<string[]>([]);

  /**
   * Context
   */
  const { authState, activeFeatures } = useContext(AuthContext);
  const { addItemToCart, cart, disableAddToCart } = useCartContext();
  const { shippingBranch } = useContext(BranchContext);

  const productSlug = slugify(
    `${props.product.manufacturerName}-${props.product.name}-${props.product.manufacturerNumber}`
  );

  const productPricing = props.pricing.find(
    (p) => p.productId === props.product.partNumber
  );

  const hasValidPrice = productPricing && productPricing?.sellPrice > 0;

  const saved = useSavings(props.product?.cmp, productPricing?.sellPrice);
  const CMPEnabled = activeFeatures?.includes('COMPETITIVE_MARKET_PRICE');
  const isCompetitiveMarketPrice = saved.isSavings && CMPEnabled;
  /**
   * Effects
   */
  useEffect(() => {
    !!productPricing?.listIds && setAvailableInList(productPricing?.listIds);
  }, [productPricing?.listIds]);

  /**
   * Render
   */
  if (isSmallScreen) {
    return (
      <Grid container spacing={2} data-testid={`search-result-${props.index}`}>
        {authState?.isAuthenticated && (
          <Grid
            item
            container
            display="flex"
            justifyContent="flex-end"
            alignItems="top"
          >
            <AddToListbutton
              availableInList={availableInList}
              updatedAddedToLists={updatedAddedToLists}
              partNumber={props.product.partNumber ?? ''}
              quantity={quantity}
              index={props.index}
            />
          </Grid>
        )}
        <Grid container item justifyContent="center" alignItems="center">
          <Box
            display="flex"
            justifyContent="center"
            alignItems="center"
            width="50%"
            height="50%"
          >
            <Link
              to={`/product/${productSlug}/${props.product.id}`}
              component={RouterLink}
            >
              <Box
                onClick={props.onClick}
                data-testid={`product-image-${props.index}`}
              >
                <Image
                  fallback={notfound}
                  alt={props.product.name ?? t('product.productName')}
                  src={props.product.imageUrls?.thumb ?? ''}
                  title={props.product.name ?? t('product.productName')}
                />
              </Box>
            </Link>
          </Box>
        </Grid>
        <Grid item width="100%">
          <Box flex="1">
            <Typography
              variant="caption"
              component="p"
              gutterBottom
              textTransform="uppercase"
              color="text.secondary"
              data-testid={`product-manufacturer-name-${props.index}`}
            >
              {props.product.manufacturerName}
            </Typography>
            <Typography
              component="div"
              gutterBottom
              data-testid={`product-name-${props.index}`}
            >
              <Dotdotdot clamp={3}>
                <Link
                  to={`/product/${productSlug}/${props.product.id}`}
                  component={RouterLink}
                >
                  <Typography
                    onClick={props.onClick}
                    sx={{ overflowWrap: 'break-word' }}
                  >
                    {props.product.name}
                  </Typography>
                </Link>
              </Dotdotdot>
            </Typography>
            {props.product.manufacturerNumber && (
              <Box mb={2}>
                <Typography
                  variant="caption"
                  component="p"
                  data-testid={`product-manufacturer-number-${props.index}`}
                >
                  {t('product.mfr')} {props.product.manufacturerNumber}
                </Typography>
                {props.product.customerPartNumber && (
                  <CustomerPartNumber
                    partNumber={props.product.customerPartNumber}
                    stylingForPage="PLP"
                  />
                )}
              </Box>
            )}
            {!authState?.isAuthenticated && (
              <div className="search-results__cmp-container">
                {isCompetitiveMarketPrice && (
                  <span
                    className="search-results__cmp-text-content"
                    data-testid={`search-results-cmp-text-content-${props.index}`}
                  >
                    {format(props.product.cmp ?? 0)}
                  </span>
                )}
                <p
                  className="search-results__cmp-signin-info-text"
                  data-testid={`search-results-cmp-signin-info-text-${props.index}`}
                >
                  <Link to="/login" component={RouterLink}>
                    <span
                      className="search-results__cmp-signin-link-text"
                      data-testid={`search-results-cmp-signin-link-text-${props.index}`}
                    >
                      {t('search.signIn')}
                    </span>
                  </Link>
                  {t('search.signInInfo')}
                </p>
              </div>
            )}
            {authState?.isAuthenticated && (
              <>
                <Box pr={0} mb={2}>
                  <Typography
                    color="primary"
                    variant="h5"
                    my={2}
                    mx={0}
                    data-testid={`product-price-${props.index}`}
                  >
                    {props.pricingLoading ? (
                      <Skeleton
                        variant="rectangular"
                        data-testid={`search-result-pricing-skeleton-${props.index}`}
                      />
                    ) : (
                      hasValidPrice && (
                        <>
                          {isCompetitiveMarketPrice && (
                            <Typography
                              variant="subtitle2"
                              component="p"
                              color="secondary02.main"
                              fontWeight="bold"
                              sx={{ textDecoration: 'line-through' }}
                              data-testid={`product-cmp-${props.index}`}
                            >
                              {format(props.product.cmp ?? 0)}{' '}
                            </Typography>
                          )}
                          <Typography
                            component="span"
                            variant="h4"
                            fontWeight="bold"
                            textTransform="lowercase"
                            data-testid={`product-sell-price-${props.index}`}
                          >
                            {format(productPricing?.sellPrice ?? 0)}{' '}
                          </Typography>
                          <Typography
                            component="span"
                            fontWeight="fontWeightHeavy"
                            textTransform="lowercase"
                            data-testid={`product-each-${props.index}`}
                          >
                            {productPricing?.orderUom ?? t('product.each')}
                          </Typography>
                          {isCompetitiveMarketPrice && (
                            <Typography
                              variant="caption"
                              color="success.main"
                              component="p"
                              data-testid={`product-savings-${props.index}`}
                            >
                              {t('common.youSave')}{' '}
                              <Typography
                                variant="caption"
                                fontWeight="bold"
                                component="span"
                                data-testid={`product-saved-price-${props.index}`}
                              >
                                {format(saved.price)}{' '}
                              </Typography>
                              {'('}
                              {saved.percentage}
                              {'%'}
                              {')'}
                            </Typography>
                          )}
                        </>
                      )
                    )}
                  </Typography>
                  <AvailabilityChip
                    branch={shippingBranch}
                    loading={props.pricingLoading}
                    stock={
                      shippingBranch?.isPricingOnly
                        ? productPricing?.totalAvailableQty
                        : productPricing?.branchAvailableQty
                    }
                  />
                </Box>
                <Box display="flex" alignItems={'center'} width="100%">
                  <QtyInput
                    disabled={props.pricingLoading || !hasValidPrice || !cart}
                    value={quantity}
                    onUpdate={setQuantity}
                    increment={props.product.minIncrementQty || 1}
                    max={999999}
                    index={props.index}
                    size="small"
                    fullWidth
                    noDebounce
                    data-testid={`quantity-input-${props.index}`}
                  />

                  <FeedbackButton
                    disabled={
                      props.pricingLoading ||
                      !hasValidPrice ||
                      !cart ||
                      disableAddToCart
                    }
                    testId={`add-to-cart-button-${props.index}`}
                    fullWidth
                    onClick={handleAddToCart}
                    size="medium"
                    value={t('common.addToCart')}
                    valueDone={t('common.addedToCart')}
                    sx={{ ml: 2, height: '37px' }}
                  />
                </Box>
              </>
            )}
          </Box>
        </Grid>
      </Grid>
    );
  }

  return (
    <Card
      elevation={0}
      sx={{
        pb: 2,
        mb: 2,
        flex: 1,
        display: 'flex',
        flexDirection: 'column',
        width: '100%'
      }}
      data-testid={`search-result-${props.index}`}
    >
      <CardContent
        sx={{
          flex: 1,
          display: 'flex',
          flexDirection: 'column',
          p: 2
        }}
      >
        {authState?.isAuthenticated && (
          <Box pb={1.5}>
            <AddToListbutton
              availableInList={availableInList}
              updatedAddedToLists={updatedAddedToLists}
              partNumber={props.product.partNumber ?? ''}
              quantity={quantity}
              index={props.index}
            />
          </Box>
        )}
        <Link
          component={RouterLink}
          to={`/product/${productSlug}/${props.product.id}`}
        >
          <Box
            display="flex"
            justifyContent="center"
            alignItems="center"
            paddingBottom={3}
            height={120}
            onClick={props.onClick}
            data-testid={`product-image-${props.index}`}
          >
            <Image
              fallback={notfound}
              alt={props.product.name ?? t('product.productName')}
              src={props.product.imageUrls?.thumb ?? ''}
              title={props.product.name ?? t('product.productName')}
            />
          </Box>
        </Link>
        <Typography
          variant="caption"
          component="p"
          gutterBottom
          textTransform="uppercase"
          color="text.secondary"
          data-testid={`product-manufacturer-name-${props.index}`}
        >
          {props.product?.manufacturerName}
        </Typography>
        <Typography component="div" gutterBottom>
          <Dotdotdot clamp={3}>
            <Link
              to={`/product/${productSlug}/${props.product.id}`}
              component={RouterLink}
              data-testid={`product-name-${props.index}`}
            >
              <Typography
                onClick={props.onClick}
                sx={{ overflowWrap: 'break-word' }}
              >
                {props.product.name}
              </Typography>
            </Link>
          </Dotdotdot>
        </Typography>
        {!!props.product.manufacturerNumber && (
          <Typography
            variant="caption"
            component="p"
            data-testid={`product-manufacturer-number-${props.index}`}
          >
            {t('product.mfr')} {props.product.manufacturerNumber}
          </Typography>
        )}
        {props.product.customerPartNumber && (
          <CustomerPartNumber
            partNumber={props.product.customerPartNumber}
            stylingForPage="PLP"
          />
        )}
      </CardContent>
      {!authState?.isAuthenticated && isCompetitiveMarketPrice && (
        <div className="search-results__cmp-container">
          <span
            className="search-results__cmp-text-content"
            data-testid={`search-results-cmp-text-content-${props.index}`}
          >
            {format(props.product.cmp ?? 0)}
          </span>

          <p
            className="search-results__cmp-signin-info-text"
            data-testid={`search-results-cmp-signin-info-text-${props.index}`}
          >
            <Link to="/login" component={RouterLink}>
              <span
                className="search-results__cmp-signin-link-text"
                data-testid={`search-results-cmp-siginin-link-text-${props.index}`}
              >
                {t('search.signIn')}
              </span>
            </Link>
            {t('search.signInInfo')}
          </p>
        </div>
      )}
      {authState?.isAuthenticated && (
        <>
          <Box pl={2} pr={2} mb={2}>
            <Typography
              color="primary"
              variant="h5"
              mb={2}
              mx={0}
              data-testid={`product-price-${props.index}`}
            >
              {props.pricingLoading ? (
                <Skeleton
                  variant="rectangular"
                  data-testid={`search-result-pricing-skeleton-${props.index}`}
                />
              ) : (
                hasValidPrice && (
                  <>
                    {isCompetitiveMarketPrice && (
                      <Typography
                        variant="subtitle2"
                        component="p"
                        color="secondary02.main"
                        fontWeight="bold"
                        sx={{ textDecoration: 'line-through' }}
                        data-testid={`product-cmp-${props.index}`}
                      >
                        {format(props.product.cmp ?? 0)}{' '}
                      </Typography>
                    )}
                    <Typography
                      component="span"
                      variant="h4"
                      fontWeight="bold"
                      textTransform="lowercase"
                      data-testid={`product-sell-price-${props.index}`}
                    >
                      {format(productPricing?.sellPrice ?? 0)}{' '}
                    </Typography>
                    <Typography
                      component="span"
                      fontWeight="regular"
                      textTransform="lowercase"
                      data-testid={`product-each-${props.index}`}
                    >
                      {productPricing?.orderUom ?? t('product.each')}
                    </Typography>
                    {isCompetitiveMarketPrice && (
                      <Typography
                        variant="caption"
                        color="success.main"
                        component="p"
                        data-testid={`product-savings-${props.index}`}
                      >
                        {t('common.youSave')}{' '}
                        <Typography
                          variant="caption"
                          fontWeight="bold"
                          component="span"
                          data-testid={`product-saved-price-${props.index}`}
                        >
                          {format(saved.price)}{' '}
                        </Typography>
                        {'('}
                        {saved.percentage}
                        {'%'}
                        {')'}
                      </Typography>
                    )}
                  </>
                )
              )}
            </Typography>
            <AvailabilityChip
              branch={shippingBranch}
              loading={props.pricingLoading}
              stock={
                shippingBranch?.isPricingOnly
                  ? productPricing?.totalAvailableQty
                  : productPricing?.branchAvailableQty
              }
              index={props.index}
            />
          </Box>
          <CardActions sx={{ py: 0, px: 1 }}>
            <QtyInput
              disabled={props.pricingLoading || !hasValidPrice || !cart}
              value={quantity}
              onUpdate={setQuantity}
              increment={props.product?.minIncrementQty || 1}
              max={9999}
              size="extra-small"
              index={props.index}
              noDebounce
              style={{ height: '30px' }}
              data-testid={`quantity-input-${props.index}`}
            />
            <AdvancedToolTip
              title="Warning"
              text={t('cart.maxLimitToolTip')}
              icon={<WarningIcon width={16} height={16} />}
              placement="bottom"
              disabled={disableAddToCart}
            >
              <FeedbackButton
                disabled={
                  props.pricingLoading ||
                  !hasValidPrice ||
                  !cart ||
                  disableAddToCart
                }
                testId={`add-to-cart-button-${props.index}`}
                fullWidth
                onClick={handleAddToCart}
                size="small"
                value={t('common.addToCart')}
                valueDone={t('common.addedToCart')}
              />
            </AdvancedToolTip>
          </CardActions>
        </>
      )}
    </Card>
  );

  /**
   * Callback Definitions
   */
  function handleAddToCart() {
    addItemToCart(
      props.product.partNumber ?? '',
      quantity,
      props.product.minIncrementQty ?? 0,
      productPricing
    );
  }

  function updatedAddedToLists(value: string[]) {
    setAvailableInList(value);
  }
}

export default SearchResult;
