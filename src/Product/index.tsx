import {
  SyntheticEvent,
  useContext,
  useEffect,
  useMemo,
  useState
} from 'react';

import {
  Box,
  Button,
  Container,
  Divider,
  Grid,
  Hidden,
  Image,
  Link,
  Skeleton,
  Tab,
  TabPanel,
  Tabs,
  Tooltip,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { kebabCase } from 'lodash-es';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink, useHistory, useParams } from 'react-router-dom';

import { AuthContext } from 'AuthProvider';
import AdvancedToolTip from 'common/AdvancedToolTip';
import Breadcrumbs, { BreadcrumbConfig } from 'common/Breadcrumbs';
import { ErrorTypes } from 'common/ErrorBoundary/ErrorComponent';
import FeedbackButton from 'common/FeedbackButton';
import { HeaderContext } from 'common/Header/HeaderProvider';
import QtyInput from 'common/QtyInput';
import {
  ErpSystemEnum,
  Product,
  useGetProductQuery,
  useGetProductPricingQuery
} from 'generated/graphql';
import useDocumentTitle from 'hooks/useDocumentTitle';
import useSavings from 'hooks/useSavings';
import {
  BranchIcon,
  ChevronLeftIcon,
  SignInIcon,
  TechnicalSpecsIcon,
  HelpIcon,
  WarningIcon
} from 'icons';
import notfound from 'images/notfound.png';
import AddToListButton from 'Product/AddToListButton';
import EnvironmentalOptions from 'Product/EnvironmentalOptions';
import FeaturesAndBenefits from 'Product/FeaturesAndBenefits';
import PackageDimensions from 'Product/PackageDimensions';
import ProductAvailabilityChip from 'Product/ProductAvailabilityChip';
import ProductCodes from 'Product/ProductCodes';
import ProductOverview from 'Product/ProductOverview';
import TechnicalDocuments from 'Product/TechnicalDocuments';
import TechnicalSpecifications from 'Product/TechnicalSpecifications';
import {
  BranchesButtonContainer,
  ProductDetailsContainer,
  ProductImageContainer,
  ProductInfoContainer,
  ProductMfnTypography,
  QtyInfoContainer,
  QtyInputContainer,
  SignInForPriceButton
} from 'Product/util/styled';
import { BranchContext } from 'providers/BranchProvider';
import { useCartContext } from 'providers/CartProvider';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import slugify from 'react-slugify';
import CustomerPartNumber from 'Search/CustomerPartNumber';
import { trackSearchResult } from 'utils/analytics';
import { format } from 'utils/currency';
import { timestamp } from 'utils/dates';

const PAGE_SIZE = 12;

function ProductPage() {
  /**
   * Custom Hooks
   */
  const history = useHistory();
  const { id, slug } = useParams<{ id: string; slug: string }>();
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Context
   */
  const { authState, user, activeFeatures } = useContext(AuthContext);
  const { setBranchSelectOpen, shippingBranch, setProductId } =
    useContext(BranchContext);
  const { addItemToCart, cart, disableAddToCart } = useCartContext();
  const { searchPage, trackedSearchTerm, pageIndex } =
    useContext(HeaderContext);
  const { selectedAccounts } = useSelectedAccountsContext();

  /**
   * State
   */
  const [currentTab, setCurrentTab] = useState(0);
  const [quantity, setQuantity] = useState(0);
  const [availableInList, setAvailableInList] = useState<string[]>([]);

  /**
   * GraphQL
   */
  const { data, loading } = useGetProductQuery({
    fetchPolicy: 'no-cache',
    variables: {
      productInput: {
        productId: id,
        customerNumber: selectedAccounts.billToErpAccount?.erpAccountId
      }
    },
    onCompleted: (data) => {
      trackSearchResult({
        authenticated: authState?.isAuthenticated,
        user: authState?.isAuthenticated ? user?.email : null,
        pageNumber: searchPage || null,
        pageIndex: pageIndex + 1 || null,
        searchIndex: (searchPage - 1) * PAGE_SIZE + pageIndex + 1 || null,
        product: data?.product,
        searchTerm: trackedSearchTerm,
        timestamp: timestamp
      });
    },
    onError: (e) => {
      history.push({
        pathname: '/error',
        state: { errorType: ErrorTypes.NOT_FOUND }
      });
    }
  });

  const { data: pricingData, loading: pricingLoading } =
    useGetProductPricingQuery({
      fetchPolicy: 'no-cache',
      skip: !authState?.isAuthenticated || !data?.product,
      variables: {
        input: {
          customerId: selectedAccounts.billTo?.erpAccountId ?? '',
          branchId: shippingBranch?.branchId ?? '',
          productIds: [data?.product?.partNumber ?? ''],
          includeListData: true
        }
      }
    });
  const widthQtyInfoContainer = isSmallScreen ? '55px' : '60px';

  /**
   * Page Title
   */
  useDocumentTitle(
    t('dynamicPageTitles.product', {
      mfrName: data?.product?.manufacturerName ?? '',
      productName: data?.product?.name ?? '',
      mfrNumber: data?.product?.manufacturerNumber ?? ''
    })
  );

  /**
   * Memos
   */

  const breadcrumbConfig = useMemo(breadcrumbConfigMemo, [data]);

  const additionalCodesTitle = useMemo(showAdditionalCodesMemo, [
    data?.product
  ]);

  const pricingAndAvailability = useMemo(pricingAndAvailabilityMemo, [
    data?.product?.partNumber,
    pricingData?.productPricing.products
  ]);

  const competitiveMarketPrice = format(data?.product?.cmp ?? 0);
  const uom = pricingAndAvailability?.orderUom ?? t('product.each');
  const sellPrice = format(pricingAndAvailability?.sellPrice ?? 0);

  const showAvailabilityChip =
    shippingBranch?.isPricingOnly ||
    !!pricingAndAvailability?.branchAvailableQty ||
    (!pricingAndAvailability?.totalAvailableQty &&
      !pricingAndAvailability?.branchAvailableQty);

  // eslint-disable-next-line react-hooks/rules-of-hooks
  const saved = useSavings(
    data?.product?.cmp,
    pricingAndAvailability?.sellPrice
  );
  const CMPEnabled = activeFeatures?.includes('COMPETITIVE_MARKET_PRICE');
  const isCompetitiveMarketPrice = saved.isSavings && CMPEnabled;

  /**
   * Effects
   */

  useEffect(() => {
    if (!loading && data) {
      setQuantity(data?.product?.minIncrementQty || 1);
    }
  }, [loading, data, setQuantity]);

  useEffect(() => {
    if (!pricingLoading && pricingData) {
      setAvailableInList(pricingAndAvailability?.listIds || []);
    }
  }, [
    pricingLoading,
    pricingData,
    pricingAndAvailability?.listIds,
    setAvailableInList
  ]);

  useEffect(() => {
    if (!loading && data?.product && slug) {
      const { id, manufacturerName, manufacturerNumber, name } = data.product;
      const toSlugify = `${manufacturerName}-${name}-${manufacturerNumber}`;
      const productSlug = slugify(toSlugify);
      history.replace(`/product/${productSlug}/${id}`);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [loading, data?.product, slug]);

  /**
   * Callbacks
   */
  const handleTabChange = (_: SyntheticEvent, newValue: number) => {
    setCurrentTab(newValue);
  };

  const updatedAddedToLists = (value: string[]) => {
    setAvailableInList(value);
  };

  const handleBranchChange = () => {
    setBranchSelectOpen(true);
    setProductId(data?.product?.partNumber ?? '');
  };

  /**
   * Render vars
   */
  const image = () =>
    loading ? (
      <Skeleton variant="rectangular" height={300} width="100%" />
    ) : (
      <Box
        component={Image}
        fallback={notfound}
        alt={data?.product?.name ?? t('common.productPicture')}
        src={data?.product?.imageUrls?.medium ?? ''}
        p={2}
        data-testid="product-picture"
      />
    );

  const getPricingLayout = () => {
    if (authState?.isAuthenticated) {
      if (isCompetitiveMarketPrice) {
        // logged in user
        // CMP enabled -> show all prices (CMP, sell price, savings percentage)
        // CMP disabled -> show sell price
        return (
          <Typography color="primary" variant="h4" data-testid="product-price">
            {loading || pricingLoading ? (
              <Skeleton width={120} />
            ) : pricingAndAvailability?.sellPrice === 0 ? (
              <Typography
                variant="h4"
                component="span"
                fontWeight={700}
                color="secondary03.main"
                data-testid="price-unavailable"
              >
                {t('product.priceUnavailable')}
              </Typography>
            ) : (
              <>
                <Box display="flex" alignItems="center">
                  <Typography
                    variant="subtitle1"
                    fontWeight="regular"
                    component="p"
                    color="secondary02.main"
                  >
                    {t('product.cmp')}
                    {': '}
                    <Typography
                      variant="subtitle1"
                      component="span"
                      fontWeight="bold"
                      sx={{
                        textDecoration: 'line-through'
                      }}
                      data-testid="cmp-price"
                    >
                      {competitiveMarketPrice}{' '}
                    </Typography>
                  </Typography>
                  <Tooltip
                    enterTouchDelay={0}
                    title={t('product.cmpTooltipText') as string}
                    placement={isSmallScreen ? 'top' : 'right'}
                  >
                    <HelpIcon
                      style={{
                        width: '20px',
                        height: '20px',
                        marginLeft: '8px'
                      }}
                    />
                  </Tooltip>
                </Box>
                <Typography
                  variant="h2"
                  fontWeight="fontWeightHeavy"
                  textTransform="lowercase"
                  data-testid="sell-price"
                >
                  {sellPrice}{' '}
                  <Typography component="span" variant="h3" fontWeight="light">
                    {uom}
                  </Typography>
                </Typography>

                <Typography
                  variant="subtitle1"
                  color="success.main"
                  component="p"
                  fontWeight="regular"
                  data-testid="price"
                >
                  {t('common.youSave')}{' '}
                  <Typography
                    component="span"
                    variant="subtitle1"
                    fontWeight="bold"
                    data-testid="saved-price"
                  >
                    {format(saved.price)}{' '}
                  </Typography>
                  {'('}
                  {saved.percentage}
                  {'%'}
                  {')'}
                </Typography>
              </>
            )}
          </Typography>
        );
      } else {
        return (
          <Typography
            variant="h2"
            fontWeight="fontWeightHeavy"
            textTransform="lowercase"
            data-testid="sell-price"
          >
            {sellPrice}{' '}
            <Typography component="span" variant="h3" fontWeight="light">
              {uom}
            </Typography>
          </Typography>
        );
      }
    } else {
      if (isCompetitiveMarketPrice) {
        // logged out user (visitors)
        // CMP enabled -> show CMP
        // CMP disabled -> show nothing
        return (
          <Typography
            variant="h2"
            fontWeight="fontWeightHeavy"
            textTransform="lowercase"
            data-testid="cmp-price"
          >
            {competitiveMarketPrice}{' '}
            <Typography component="span" variant="h3" fontWeight="light">
              {uom}
            </Typography>
          </Typography>
        );
      }
    }
    return;
  };

  /**
   * Render
   */
  return (
    <Box flex="1" height="100%" bgcolor="common.white">
      <Container maxWidth="lg" disableGutters={isSmallScreen}>
        <Grid container>
          {loading ? (
            <Box pt={2} pb={3} width={1}>
              <Typography variant="body1" width={100} pl={3}>
                <Skeleton />
              </Typography>
            </Box>
          ) : data?.product?.name && !isSmallScreen ? (
            <Breadcrumbs
              config={breadcrumbConfig}
              pageTitle={data.product.name}
            />
          ) : (
            <Grid item xs={12} py={1}>
              <Button
                onClick={history.goBack}
                startIcon={<ChevronLeftIcon />}
                variant="inline"
                data-testid="go-back-button"
              >
                {t('common.back')}
              </Button>
            </Grid>
          )}
          <Hidden mdDown>
            <Grid
              item
              sm={6}
              xs={12}
              flexDirection="column"
              display="flex"
              justifyContent="center"
              alignItems="flex-end"
              height={1}
            >
              <Grid item mr={4}>
                {loading ? (
                  <Skeleton width={100} height={24} />
                ) : (
                  !!authState?.isAuthenticated && (
                    <AddToListButton
                      availableInList={availableInList}
                      updatedAddedToLists={updatedAddedToLists}
                      partNumber={data?.product?.partNumber ?? ''}
                      quantity={quantity}
                    />
                  )
                )}
              </Grid>
              <ProductImageContainer item xs={6}>
                {image()}
              </ProductImageContainer>
            </Grid>
            <Divider
              orientation="vertical"
              flexItem
              sx={{ bgcolor: 'secondary04.main' }}
            />
          </Hidden>
          <Grid item xs>
            <Grid container direction="column" wrap="nowrap">
              <Box
                px={isSmallScreen ? 3 : 4}
                data-testId="product-detail-container"
              >
                <ProductMfnTypography
                  variant="h5"
                  data-testid="product-manufacturer-name"
                >
                  {loading ? (
                    <Skeleton width={100} />
                  ) : (
                    data?.product?.manufacturerName
                  )}
                </ProductMfnTypography>
                <Typography
                  color="primary"
                  variant="h4"
                  data-testid="product-name"
                >
                  {loading ? <Skeleton height={30} /> : data?.product?.name}
                </Typography>
                <Grid container>
                  <Grid item xs display="flex">
                    <Typography
                      variant="h5"
                      pt={1}
                      color="secondary02.main"
                      fontWeight={400}
                      data-testid="product-manufacturer-number"
                    >
                      {loading ? (
                        <Skeleton width={120} />
                      ) : (
                        !!data?.product?.manufacturerNumber &&
                        `${t('product.mfr')} ${data.product.manufacturerNumber}`
                      )}
                    </Typography>
                    <Hidden mdDown>
                      {loading ? (
                        <Skeleton width={120} />
                      ) : (
                        <CustomerPartNumber
                          partNumber={data?.product?.customerPartNumber}
                          stylingForPage="PDP"
                        />
                      )}
                    </Hidden>
                  </Grid>
                </Grid>
                <Hidden mdUp>
                  <Box>
                    {loading ? (
                      <Skeleton width={100} height={24} />
                    ) : (
                      <CustomerPartNumber
                        partNumber={data?.product?.customerPartNumber}
                        stylingForPage="PDP"
                      />
                    )}
                  </Box>
                  <Box pt={3}>
                    <ProductDetailsContainer item xs={12}>
                      <Grid item>
                        {loading ? (
                          <Skeleton width={100} height={24} />
                        ) : (
                          !!authState?.isAuthenticated && (
                            <AddToListButton
                              availableInList={availableInList}
                              updatedAddedToLists={updatedAddedToLists}
                              partNumber={data?.product?.partNumber ?? ''}
                              quantity={quantity}
                            />
                          )
                        )}
                      </Grid>
                      <Grid
                        container
                        item
                        xs={6}
                        direction="row"
                        alignSelf="center"
                      >
                        {image()}
                      </Grid>
                    </ProductDetailsContainer>
                  </Box>
                </Hidden>
                <Grid container alignItems="center" py={3}>
                  <Grid item xs>
                    {getPricingLayout()}
                  </Grid>
                </Grid>
                <Grid container gap={2} justifyContent="flex-start">
                  {showAvailabilityChip && (
                    <ProductInfoContainer item>
                      <ProductAvailabilityChip
                        loading={loading || pricingLoading}
                        stock={
                          shippingBranch?.isPricingOnly
                            ? pricingAndAvailability?.totalAvailableQty
                            : pricingAndAvailability?.branchAvailableQty
                        }
                      />
                    </ProductInfoContainer>
                  )}
                  {!!pricingAndAvailability?.totalAvailableQty &&
                    !shippingBranch?.isPricingOnly && (
                      <BranchesButtonContainer item>
                        {loading ? (
                          <Skeleton
                            variant="rectangular"
                            width={160}
                            height={24}
                          />
                        ) : (
                          !!authState?.isAuthenticated && (
                            <Button
                              color="primaryLight"
                              data-testid="find-at-other-branches-button"
                              size="small"
                              variant="inline"
                              startIcon={
                                <BranchIcon
                                  style={{
                                    width: '30px',
                                    height: '30px',
                                    marginRight: '8px'
                                  }}
                                />
                              }
                              onClick={handleBranchChange}
                              sx={{ fontWeight: 500 }}
                            >
                              {t('product.checkNearByBranches')}
                            </Button>
                          )
                        )}
                      </BranchesButtonContainer>
                    )}
                </Grid>
                <Box py={3}>
                  {loading ? (
                    <Skeleton height={66} />
                  ) : !authState?.isAuthenticated ? (
                    <Box pb={5}>
                      <Link to="/login" component={RouterLink}>
                        <SignInForPriceButton
                          variant="primary"
                          size="large"
                          fullWidth
                          startIcon={<SignInIcon />}
                          data-testid="log-in-for-price-button"
                        >
                          {t('product.signInForPrice')}
                        </SignInForPriceButton>
                      </Link>
                    </Box>
                  ) : (
                    <>
                      <QtyInfoContainer container>
                        <QtyInputContainer>
                          <QtyInput
                            fullWidth
                            disabled={
                              !cart || !pricingAndAvailability?.sellPrice
                            }
                            value={
                              quantity <= 1
                                ? data?.product?.minIncrementQty || 1
                                : quantity
                            }
                            onUpdate={setQuantity}
                            increment={data?.product?.minIncrementQty || 1}
                            size={!isSmallScreen ? 'large' : 'medium'}
                            max={999999}
                            noDebounce
                            style={{ height: widthQtyInfoContainer }}
                          />
                        </QtyInputContainer>
                        <Grid item xs ml={2} mb={isSmallScreen ? 15 : 0}>
                          <AdvancedToolTip
                            title="Warning"
                            text={t('cart.maxLimitToolTip')}
                            icon={<WarningIcon />}
                            placement="bottom"
                            disabled={disableAddToCart}
                          >
                            <FeedbackButton
                              disabled={
                                disableAddToCart ||
                                !pricingAndAvailability?.sellPrice ||
                                !cart
                              }
                              testId={`${data?.product?.id}-add-to-cart-button`}
                              fullWidth
                              onClick={handleAddToCart}
                              value={t('common.addToCart')}
                              valueDone={t('common.addedToCart')}
                              sx={{ height: widthQtyInfoContainer }}
                            />
                          </AdvancedToolTip>
                        </Grid>
                      </QtyInfoContainer>
                    </>
                  )}
                </Box>
              </Box>
              {isSmallScreen && (
                <EnvironmentalOptions product={data?.product} />
              )}
              <Box px={3} pb={3} bgcolor="common.white">
                <Hidden mdUp>
                  <Box pb={3} data-testid="product-details-accordian">
                    <TechnicalDocuments data={data} />
                    <ProductCodes data={data} />
                    <ProductOverview data={data} />
                    <FeaturesAndBenefits data={data} />
                    <TechnicalSpecifications
                      data={data}
                      isSmallScreen={isSmallScreen}
                    />
                    <PackageDimensions
                      data={data}
                      isSmallScreen={isSmallScreen}
                    />
                  </Box>
                </Hidden>
                <Hidden mdDown>
                  {!!data?.product?.technicalDocuments?.length ? (
                    <>
                      <Box pb={3}>
                        <Divider sx={{ bgcolor: 'secondary04.main' }} />
                      </Box>
                      <Typography
                        variant="h5"
                        color="primary"
                        data-testid="technical-docs"
                      >
                        {loading ? <Skeleton /> : t('product.techDocs')}
                      </Typography>
                      {loading ? (
                        <Skeleton sx={{ py: 2 }} />
                      ) : (
                        <Grid container py={2}>
                          {data.product?.technicalDocuments?.map((doc) => (
                            <Grid item xs={6} key={doc?.name}>
                              <Link href={doc?.url ?? ''} target="_blank">
                                <Button
                                  variant="inline"
                                  data-testid={kebabCase(`${doc?.name}-button`)}
                                  startIcon={<TechnicalSpecsIcon />}
                                >
                                  {doc?.name}
                                </Button>
                              </Link>
                            </Grid>
                          ))}
                        </Grid>
                      )}
                    </>
                  ) : null}
                  <Divider sx={{ bgcolor: 'secondary04.main' }} />
                  <Box pt={3} bgcolor="common.white">
                    {additionalCodesTitle ? (
                      <Typography
                        variant="h5"
                        color="primary"
                        data-testid="additional-product-codes"
                      >
                        {loading ? <Skeleton /> : t('product.productCodes')}
                      </Typography>
                    ) : null}
                    {loading ? (
                      <Skeleton sx={{ py: 2 }} />
                    ) : (
                      <Grid container py={2} color="fsecondary02.main">
                        {data?.product?.id &&
                        data?.product?.erp === ErpSystemEnum.Eclipse ? (
                          <Grid item xs data-testid="product-item-number">
                            <Box fontWeight="bold">
                              {t('product.itemNumber')}
                            </Box>
                            {data?.product?.id}
                          </Grid>
                        ) : null}
                        {data?.product?.upc ? (
                          <Grid item xs data-testid="product-upc">
                            <Box fontWeight="bold">{t('product.upc')}</Box>
                            {data.product.upc}
                          </Grid>
                        ) : null}
                        {data?.product?.unspsc ? (
                          <Grid item xs data-testid="product-unspsc">
                            <Box fontWeight="bold">{t('product.unspc')}</Box>
                            {data.product.unspsc}
                          </Grid>
                        ) : null}
                        {data?.product?.seriesModelFigureNumber ? (
                          <Grid item xs={5} data-testid="product-series">
                            <Box fontWeight="bold">{t('product.series')}</Box>
                            {data.product.seriesModelFigureNumber}
                          </Grid>
                        ) : null}
                      </Grid>
                    )}
                  </Box>
                </Hidden>
                {!isSmallScreen ? (
                  <>
                    {data?.product?.environmentalOptions?.length ? (
                      <Divider sx={{ bgcolor: 'secondary04.main' }} />
                    ) : null}
                    <EnvironmentalOptions product={data?.product as Product} />
                  </>
                ) : null}
              </Box>
            </Grid>
          </Grid>
        </Grid>
        <Hidden mdDown>
          {loading || (!loading && !Boolean(data?.product)) ? null : (
            <Box mt={7.5}>
              <Tabs value={currentTab} onChange={handleTabChange}>
                <Tab
                  label={t('product.productOverview')}
                  id="product-overview-tab"
                  aria-controls="product-overview-tabpanel"
                  sx={{
                    display: data?.product?.productOverview
                      ? 'inline-flex'
                      : 'none'
                  }}
                  data-testid="prod-overview-tab"
                />
                <Tab
                  label={t('product.featuresBenefits')}
                  id="features-and-benefits-tab"
                  aria-controls="features-and-benefits-tabpanel"
                  sx={{
                    display: data?.product?.featuresAndBenefits
                      ? 'inline-flex'
                      : 'none'
                  }}
                  data-testid="prod-feature-benefits-tab"
                />
                <Tab
                  label={t('product.techSpecs')}
                  id="technical-specifications-tab"
                  aria-controls="technical-specifications-tabpanel"
                  sx={{
                    display:
                      data?.product?.techSpecifications &&
                      data?.product?.techSpecifications?.length > 0
                        ? 'inline-flex'
                        : 'none'
                  }}
                  data-testid="prod-tech-spec-tab"
                />
                <Tab
                  label={t('product.dimensions')}
                  id="package-dimensions-tab"
                  aria-controls="package-dimensions-tabpanel"
                  sx={{
                    display: data?.product?.packageDimensions
                      ? 'inline-flex'
                      : 'none'
                  }}
                  data-testid="prod-dimensions-tab"
                />
              </Tabs>
              <TabPanel
                value={currentTab}
                index={0}
                aria-labelledby="product-overview-tab"
              >
                <ProductOverview data={data} />
              </TabPanel>
              <TabPanel
                value={currentTab}
                index={1}
                aria-labelledby="features-and-benefits-tab"
              >
                <FeaturesAndBenefits data={data} />
              </TabPanel>
              <TabPanel
                value={currentTab}
                index={2}
                aria-labelledby="technical-specifications-tab"
              >
                <TechnicalSpecifications
                  data={data}
                  isSmallScreen={isSmallScreen}
                />
              </TabPanel>
              <TabPanel
                value={currentTab}
                index={3}
                aria-labelledby="package-dimensions-tab"
              >
                <PackageDimensions data={data} isSmallScreen={isSmallScreen} />
              </TabPanel>
            </Box>
          )}
        </Hidden>
      </Container>
    </Box>
  );

  /**
   * Memo Defs
   */
  function breadcrumbConfigMemo() {
    return data?.product?.categories?.length
      ? data.product.categories.map((t, i) => {
          return {
            text: t ?? '',
            to: {
              pathname: '/search',
              search: `?categories=${data
                .product!.categories!.slice(0, i + 1)
                .map((c) => encodeURIComponent(c).trim())
                .join('&categories=')}`
            }
          } as BreadcrumbConfig;
        })
      : ([] as BreadcrumbConfig[]);
  }

  function showAdditionalCodesMemo() {
    return !!(
      data?.product?.upc ||
      data?.product?.unspsc ||
      data?.product?.seriesModelFigureNumber ||
      (data?.product?.id && data?.product?.erp === ErpSystemEnum.Eclipse)
    );
  }

  function pricingAndAvailabilityMemo() {
    return pricingData?.productPricing.products.find(
      ({ productId }) => productId === data?.product?.partNumber
    );
  }

  /**
   * Handles
   */
  function handleAddToCart() {
    addItemToCart(
      data?.product?.partNumber ?? '',
      quantity,
      data?.product?.minIncrementQty || 1,
      pricingAndAvailability
    );
  }
}

export default ProductPage;
