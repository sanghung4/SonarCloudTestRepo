import { useContext, useMemo, useState } from 'react';

import {
  Box,
  Button,
  Card,
  Container,
  Divider,
  Grid,
  Hidden,
  Link,
  Pagination,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { useHistory } from 'react-router-dom';

import { AuthContext } from 'AuthProvider';
import { ProductCardComponent } from 'Checkout/util/styles';
import Breadcrumbs from 'common/Breadcrumbs';
import {
  PreviouslyPurchasedProduct,
  usePreviouslyPurchasedProductsQuery,
  useGetProductPricingQuery
} from 'generated/graphql';
import useDocumentTitle from 'hooks/useDocumentTitle';
import { ChevronLeftIcon, WarningIcon } from 'icons';
import PreviouslyPurchasedProductsLineItem from 'PreviouslyPurchasedProducts/PreviouslyPurchasedProductsLineItem';
import { BranchContext } from 'providers/BranchProvider';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import AdvancedToolTip from 'common/AdvancedToolTip';
import { useCartContext } from 'providers/CartProvider';

const PAGE_SIZE = 10;

function PreviouslyPurchasedProducts() {
  /**
   * Custom hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  const history = useHistory();
  useDocumentTitle(t('common.previouslyPurchasedProducts'));

  /**
   * Context
   */
  const { profile } = useContext(AuthContext);
  const { selectedAccounts } = useSelectedAccountsContext();
  const { shippingBranch } = useContext(BranchContext);
  const { disableAddToCart } = useCartContext();

  /**
   * State
   */
  const [currentPage, setCurrentPage] = useState(1);

  /**
   * Queries
   */
  const {
    data: previouslyPurchasedProductsQuery,
    loading: previouslyPurchasedProductsLoading,
    called: previouslyPurchasedProductsCalled
  } = usePreviouslyPurchasedProductsQuery({
    variables: {
      ecommShipToId: selectedAccounts?.shipTo?.id!,
      userId: profile?.userId!,
      currentPage: currentPage - 1,
      pageSize: PAGE_SIZE,
      customerNumber: selectedAccounts.billToErpAccount?.erpAccountId
    }
  });
  const partNumbers = useMemo(partNumbersMemo, [
    previouslyPurchasedProductsQuery?.previouslyPurchasedProducts.products
  ]);
  const { data: pricingData, loading: pricingDataLoading } =
    useGetProductPricingQuery({
      fetchPolicy: 'no-cache',
      skip: !previouslyPurchasedProductsQuery?.previouslyPurchasedProducts
        .products?.length,
      variables: {
        input: {
          customerId: selectedAccounts.billTo?.erpAccountId ?? '',
          branchId: shippingBranch?.branchId ?? '',
          productIds: partNumbers,
          includeListData: true
        }
      }
    });

  /**
   * Memos
   */
  const pageCount = useMemo(pageCountMemo, [previouslyPurchasedProductsQuery]);
  const productList = useMemo(productListMemo, [
    previouslyPurchasedProductsLoading,
    previouslyPurchasedProductsQuery,
    previouslyPurchasedProductsCalled
  ]);
  const activeProductCount = useMemo(activeProductCountMemo, [
    previouslyPurchasedProductsQuery,
    currentPage
  ]);

  /**
   * Render
   */
  return (
    <>
      <Hidden mdDown>
        <Breadcrumbs pageTitle={t('common.previouslyPurchasedProducts')} />
      </Hidden>
      <Hidden mdUp>
        <Link onClick={history.goBack} marginY={1}>
          <Button startIcon={<ChevronLeftIcon />} variant="text">
            {t('common.back')}
          </Button>
        </Link>
      </Hidden>
      <Box mb={3}>
        <Container>
          <Hidden mdUp>
            <ProductCardComponent>
              <Grid
                container
                justifyContent="space-between"
                alignItems="center"
                spacing={1}
              >
                <Grid item xs={12} md="auto">
                  <Typography
                    variant="h5"
                    align={isSmallScreen ? 'center' : 'left'}
                  >
                    {t('common.previouslyPurchasedProducts')}
                  </Typography>
                </Grid>
              </Grid>
            </ProductCardComponent>
          </Hidden>
          <Hidden mdDown>
            <Grid
              container
              justifyContent="space-between"
              alignItems="center"
              spacing={1}
              marginBottom={3}
              paddingY={2}
            >
              <Grid item xs={12} md="auto">
                <Typography
                  variant="h2"
                  align="left"
                  fontWeight={500}
                  lineHeight="2.875rem"
                >
                  {t('common.previouslyPurchasedProducts')}
                </Typography>
              </Grid>
            </Grid>
          </Hidden>
          <Card sx={{ flex: 1 }}>
            {!productList ||
            previouslyPurchasedProductsQuery?.previouslyPurchasedProducts
              ?.products?.length === 0 ? (
              <Box
                height={2 / 3}
                display="flex"
                alignItems="center"
                flexDirection="column"
              >
                <Box py={25}>
                  <Typography color="textSecondary" align="center" variant="h4">
                    {t('previouslyPurchasedProducts.noProductsFound')}
                  </Typography>
                  <Typography align="center" variant="body1">
                    {t('previouslyPurchasedProducts.ifThisIsWrong')}{' '}
                    <Link sx={{ color: 'primary02.main' }} href="/support">
                      {t('previouslyPurchasedProducts.contactABranch')}
                    </Link>
                  </Typography>
                </Box>
              </Box>
            ) : (
              <>
                <Box
                  px={isSmallScreen ? 3 : 6}
                  py={isSmallScreen ? 2 : 4}
                  display="flex"
                  alignItems="center"
                  bgcolor="common.white"
                >
                  {!previouslyPurchasedProductsLoading &&
                    previouslyPurchasedProductsCalled && (
                      <Grid
                        container
                        item
                        xs={12}
                        direction={isSmallScreen ? 'column-reverse' : 'row'}
                        justifyContent={
                          isSmallScreen ? 'center' : 'space-between'
                        }
                        mb={isSmallScreen && disableAddToCart ? 16 : undefined}
                        textAlign="center"
                      >
                        <Grid item xs="auto" mt={2}>
                          <Typography variant="body1">
                            <AdvancedToolTip
                              title="Warning"
                              text={t('cart.maxLimitToolTip')}
                              icon={<WarningIcon />}
                              placement="bottom"
                              disabled={disableAddToCart && isSmallScreen}
                            >
                              <Box
                                textAlign="center"
                                mx={1}
                                my={0.5}
                                component="span"
                                data-testid="paginated-and-total-product-amount"
                              >
                                {`${activeProductCount} ${t('common.of')} ${
                                  previouslyPurchasedProductsQuery
                                    ?.previouslyPurchasedProducts?.pagination
                                    ?.totalItemCount
                                } ${t('common.product')}`}
                              </Box>
                            </AdvancedToolTip>
                          </Typography>
                        </Grid>
                        <Grid
                          item
                          container
                          justifyContent={isSmallScreen ? 'center' : 'flex-end'}
                          xs
                        >
                          <Pagination
                            current={currentPage}
                            ofText={t('common.of')}
                            count={pageCount}
                            onChange={setCurrentPage}
                            onNext={setCurrentPage}
                            onPrev={setCurrentPage}
                            align="center"
                            data-testid="pagination-nav"
                          />
                        </Grid>
                      </Grid>
                    )}
                </Box>
                <Divider />
                <Box px={3}>
                  {productList.map(
                    (product: PreviouslyPurchasedProduct, idx: number) => {
                      return (
                        <Box px={isSmallScreen ? 2 : 8} key={idx}>
                          <PreviouslyPurchasedProductsLineItem
                            key={idx}
                            item={product}
                            pricingData={getProductPricing(
                              product?.product?.partNumber ?? ''
                            )}
                            loading={previouslyPurchasedProductsLoading}
                            pricingDataLoading={pricingDataLoading}
                          />
                          {idx < productList.length - 1 && <Divider />}
                        </Box>
                      );
                    }
                  )}
                </Box>
                <Divider />
                <Box
                  px={isSmallScreen ? 3 : 4}
                  py={isSmallScreen ? 2 : 3}
                  display="flex"
                  alignItems="center"
                  bgcolor="common.white"
                >
                  {!previouslyPurchasedProductsLoading &&
                    previouslyPurchasedProductsCalled && (
                      <Grid
                        container
                        item
                        xs={12}
                        direction={isSmallScreen ? 'column-reverse' : 'row'}
                        justifyContent={
                          isSmallScreen ? 'center' : 'space-between'
                        }
                      >
                        <Grid container item xs="auto" justifyContent="center">
                          <Box textAlign="center" mx={1} my={0.5}>
                            {`${activeProductCount} ${t('common.of')} ${
                              previouslyPurchasedProductsQuery
                                ?.previouslyPurchasedProducts?.pagination
                                ?.totalItemCount
                            } ${t('common.products')}`}
                          </Box>
                        </Grid>
                        <Grid
                          item
                          container
                          justifyContent={isSmallScreen ? 'center' : 'flex-end'}
                          xs
                        >
                          <Pagination
                            current={currentPage}
                            ofText={t('common.of')}
                            count={pageCount}
                            onChange={setCurrentPage}
                            onNext={setCurrentPage}
                            onPrev={setCurrentPage}
                            align="center"
                            data-testid="pagination-container"
                            dataTestIdCurrentPage="current-page-number"
                            dataTestIdTotalNumberOfPages="total-number-of-pages"
                          />
                        </Grid>
                      </Grid>
                    )}
                </Box>
              </>
            )}
          </Card>
        </Container>
      </Box>
    </>
  );

  /**
   * Memo
   */

  function pageCountMemo() {
    return Math.ceil(
      previouslyPurchasedProductsQuery?.previouslyPurchasedProducts?.pagination
        ?.totalItemCount! / PAGE_SIZE
    );
  }

  function partNumbersMemo() {
    const productList =
      previouslyPurchasedProductsQuery?.previouslyPurchasedProducts.products;
    if (productList?.length) {
      const partNumbers = productList.map((item) => item?.product?.partNumber);
      return partNumbers.filter((entry) => !!entry) as string[];
    }
    return [];
  }

  function activeProductCountMemo() {
    return (
      currentPage * PAGE_SIZE -
      (PAGE_SIZE -
        previouslyPurchasedProductsQuery?.previouslyPurchasedProducts?.products
          ?.length!)
    );
  }

  function productListMemo() {
    return previouslyPurchasedProductsLoading ||
      !previouslyPurchasedProductsCalled
      ? new Array(PAGE_SIZE).fill(null)
      : previouslyPurchasedProductsQuery?.previouslyPurchasedProducts
          ?.products!;
  }

  /**
   * Utils
   */
  function getProductPricing(productId: string) {
    const productPricing = pricingData?.productPricing?.products.find(
      (p) => p.productId === productId
    );

    return productPricing;
  }
}

export default PreviouslyPurchasedProducts;
