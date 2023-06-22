import React, { useContext, useEffect, useMemo, useState } from 'react';

import {
  Box,
  Button,
  Container,
  Divider,
  Grid,
  Hidden,
  Pagination,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { Redirect, useHistory } from 'react-router-dom';
import { kebabCase } from 'lodash-es';

import { AuthContext } from 'AuthProvider';
import BackToTop from 'common/BackToTop';
import {
  AggregationResults,
  Product,
  ProductSearchResult,
  useGetProductPricingQuery,
  useSearchProductQuery
} from 'generated/graphql';
import SearchBreadcrumbs from 'Search/SearchBreadcrumbs';
import SearchFilters from 'Search/SearchFilters';
import SearchResult from 'Search/SearchResult';
import SearchResultSkeleton from 'Search/SearchResultSkeleton';
import SearchNoResults from 'Search/SearchNoResults';
import SearchVisitorAlert from 'Search/SearchVisitorAlert';
import useSearchQueryParams from 'Search/util/useSearchQueryParams';
import { useDomainInfo } from 'hooks/useDomainInfo';
import { HeaderContext } from 'common/Header/HeaderProvider';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import { BranchContext } from 'providers/BranchProvider';
import FeaturedSearch from 'Search/FeaturedSearch';
import AdvancedToolTip from 'common/AdvancedToolTip';
import { WarningIcon } from 'icons';
import { useCartContext } from 'providers/CartProvider';

/**
 * Types
 */
export type Filters = Pick<
  ProductSearchResult,
  'selectedAttributes' | 'selectedCategories' | 'categoryLevel'
>;

export type LocationStateSearch = {
  categories: string[];
  filters: ProductSearchResult['selectedAttributes'];
};

function Search() {
  /**
   * Custom components
   */
  const history = useHistory();
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  const { engine } = useDomainInfo();

  const [params, setParams] = useSearchQueryParams();
  const { criteria = '', page = 1, categories = [], filters = [] } = params;

  /**
   * Context
   */
  const { authState, activeFeatures, profile } = useContext(AuthContext);
  const { setSearchPage, setTrackedSearchTerm, setPageIndex } =
    useContext(HeaderContext);
  const { selectedAccounts } = useSelectedAccountsContext();
  const { shippingBranch } = useContext(BranchContext);
  const { disableAddToCart } = useCartContext();

  /**
   * State
   */
  const [filtersOpen, setFiltersOpen] = useState(false);
  const [totalCount, setTotalCount] = useState(0);

  /**
   * Memos
   */
  const filterCount = useMemo(filterCountMemo, [categories, filters]);
  const pageSize = activeFeatures?.includes('POST_SEARCH_API') ? 24 : 12;

  /**
   * Queries
   */
  const { data, previousData, loading } = useSearchProductQuery({
    fetchPolicy: 'no-cache',
    variables: {
      productSearch: {
        searchTerm: criteria.replace('"', '\\"'),
        page: Number(page),
        size: pageSize,
        categoryLevel: categories.length,
        categories,
        filters,
        engine,
        state: selectedAccounts.shipToErpAccount?.state?.toLowerCase(),
        customerNumber: selectedAccounts.billToErpAccount?.erpAccountId
      },
      userId: profile?.userId ?? '',
      shipToAccountId: selectedAccounts.shipTo?.id ?? ''
    },
    onCompleted: (data) => {
      setTotalCount(data.searchProduct?.pagination?.totalItemCount ?? 0);
    }
  });

  const partNumbers = useMemo(partNumbersMemo, [data?.searchProduct?.products]);

  const { data: pricingData, loading: pricingLoading } =
    useGetProductPricingQuery({
      fetchPolicy: 'no-cache',
      skip: Boolean(!authState?.isAuthenticated || !data?.searchProduct),
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
   * Effects
   */
  useEffect(handleBadPage, [history, page, params, setParams]);

  useEffect(() => {
    if (
      shippingBranch?.isPricingOnly &&
      filters.includes('inStockLocation|in_stock_location')
    ) {
      setParams({
        ...params,
        page: '1',
        filters: filters.filter(
          (filter) => filter !== 'inStockLocation|in_stock_location'
        )
      });
    }
  }, [shippingBranch?.isPricingOnly, filters, params, setParams]);

  /**
   * Callbacks
   */
  const handlePagination = (newPage: number) => {
    window.scrollTo({ top: 0, behavior: 'smooth' });
    setParams({
      ...params,
      page: newPage.toString()
    });
  };

  const PaginationElement = () => (
    <Pagination
      count={Math.ceil(
        (data?.searchProduct?.pagination?.totalItemCount || 0) / pageSize
      )}
      current={Number(page)}
      ofText={t('common.of')}
      onChange={handlePagination}
      onPrev={handlePagination}
      onNext={handlePagination}
      data-testid="pagination-container"
      dataTestIdCurrentPage="current-page-number"
      dataTestIdTotalNumberOfPages="total-number-of-pages"
    />
  );

  const searchString = (value: string) => {
    if (value) {
      const newSearchValue = value.replaceAll(/(")\1{1,}/g, '"');
      return `${newSearchValue} (${totalCount} ${
        totalCount !== 1 ? 'results' : 'result'
      })`;
    } else {
      return null;
    }
  };

  return criteria === null ? (
    <Redirect to="/" />
  ) : !loading &&
    (data?.searchProduct?.products?.length === 0 || !data?.searchProduct) ? (
    <SearchNoResults searchTerm={criteria} />
  ) : (
    <>
      <Hidden mdUp>
        <SearchFilters
          loading={Boolean(previousData) && loading}
          count={data?.searchProduct?.pagination?.totalItemCount || 0}
          filters={data?.searchProduct?.aggregates as AggregationResults}
          filtersOpen={filtersOpen}
          onShowHide={(value) => setFiltersOpen(value)}
        />
      </Hidden>
      <Box
        flex="1"
        pb={isSmallScreen ? 6 : 12}
        sx={{ bgcolor: 'common.white' }}
      >
        {!authState?.isAuthenticated && !loading ? (
          <SearchVisitorAlert />
        ) : null}
        <Hidden mdUp>
          <Box m={3}>
            <Grid container alignItems="center">
              <Grid item xs>
                {!loading ? <SearchBreadcrumbs /> : null}
              </Grid>
              <Grid
                container
                xs
                justifyContent="flex-end"
                alignItems="center"
                data-testid={kebabCase(
                  `${
                    categories.length !== 0
                      ? categories[categories.length - 1]
                      : criteria
                  }-title`
                )}
              >
                <Typography
                  component="span"
                  textAlign="right"
                  sx={{ fontWeight: 700 }}
                >
                  &nbsp;
                  {categories.length !== 0
                    ? categories[categories.length - 1]
                    : criteria}
                </Typography>
                <Typography
                  variant="subtitle1"
                  display="block"
                  sx={{ fontWeight: 700 }}
                >
                  &nbsp; ({totalCount} {totalCount !== 1 ? 'results' : 'result'}
                  )
                </Typography>
              </Grid>
            </Grid>
          </Box>
        </Hidden>
        <Container
          maxWidth="lg"
          sx={(theme) => ({
            [theme.breakpoints.down('md')]: {
              py: 0,
              px: 4
            }
          })}
        >
          <Grid container spacing={6} sx={{ mt: 0 }}>
            <Hidden mdDown>
              <Grid item md={3}>
                <Box
                  mt={0}
                  display="flex"
                  flexDirection="column"
                  alignItems="flex-end"
                >
                  <SearchFilters
                    loading={loading}
                    count={data?.searchProduct?.pagination?.totalItemCount || 0}
                    filters={
                      data?.searchProduct?.aggregates as AggregationResults
                    }
                    filtersOpen={filtersOpen}
                    onShowHide={(value: boolean) => setFiltersOpen(value)}
                  />
                </Box>
              </Grid>
            </Hidden>
            <Grid item xs={12} md={9}>
              {activeFeatures?.includes('FEATURED_SEARCH') && (
                <FeaturedSearch />
              )}
              <Hidden mdDown>
                {!loading ? <SearchBreadcrumbs /> : null}
                <Box mt={2} mb={5}>
                  <Grid container alignItems="center">
                    <Grid
                      item
                      md={6}
                      data-testid={
                        categories.length
                          ? kebabCase(
                              `${categories[categories.length - 1]}-title`
                            )
                          : kebabCase(`${criteria}-title`)
                      }
                    >
                      <Typography
                        variant="h5"
                        component="span"
                        sx={{ fontWeight: 700 }}
                      >
                        {criteria
                          ? searchString(criteria)
                          : searchString(categories[categories.length - 1])}
                      </Typography>
                    </Grid>
                    <Grid
                      container
                      item
                      md={6}
                      justifyContent="flex-end"
                      data-testid="pagination-counter-top"
                    >
                      {/* {!loading ? <SearchSort /> : null} */}
                      {!loading &&
                      (data?.searchProduct?.pagination?.totalItemCount || 0) >
                        0 ? (
                        <PaginationElement />
                      ) : null}
                    </Grid>
                  </Grid>
                </Box>
              </Hidden>
              <Grid container spacing={2} data-testid="search-container">
                {loading ? (
                  [...new Array(3)].map((_, i) => (
                    <Grid
                      container
                      item
                      md={3}
                      direction="column"
                      key={i}
                      sx={(theme) => ({
                        '&:not(:last-child)': {
                          [theme.breakpoints.down('md')]: {
                            mb: 4
                          }
                        }
                      })}
                    >
                      <SearchResultSkeleton
                        testid={`search-result-skeleton-${i}`}
                      />
                    </Grid>
                  ))
                ) : (
                  <>
                    <Hidden mdUp>
                      <Grid
                        item
                        xs
                        mb={disableAddToCart && isSmallScreen ? 16 : undefined}
                      >
                        <AdvancedToolTip
                          title="Warning"
                          text={t('cart.maxLimitToolTip')}
                          icon={<WarningIcon />}
                          placement="bottom"
                          disabled={disableAddToCart && isSmallScreen}
                        >
                          <Button
                            variant="secondary"
                            size="small"
                            fullWidth
                            onClick={() => setFiltersOpen(true)}
                            sx={{ mb: 3 }}
                            data-testid="filter-count-mob"
                          >
                            {t('common.filter')}
                            {filterCount > 0 ? ` (${filterCount})` : null}
                          </Button>
                        </AdvancedToolTip>
                      </Grid>
                    </Hidden>
                    {data?.searchProduct?.products?.map((product, i) => (
                      <Grid
                        container
                        item
                        md={3}
                        direction="column"
                        key={i}
                        sx={(theme) => ({
                          '&:not(:last-child)': {
                            [theme.breakpoints.down('md')]: {
                              mb: 4
                            }
                          }
                        })}
                      >
                        <SearchResult
                          product={product as Product}
                          pricing={pricingData?.productPricing.products ?? []}
                          pricingLoading={pricingLoading}
                          index={i}
                          onClick={() => handleResultClick(i)}
                        />
                        {isSmallScreen && (
                          <Box pt={3}>
                            <Divider />
                          </Box>
                        )}
                      </Grid>
                    ))}
                  </>
                )}
              </Grid>
              {!loading &&
              (data?.searchProduct?.pagination?.totalItemCount || 0) > 0 ? (
                <Box
                  mt={isSmallScreen ? 6 : 4}
                  mb={isSmallScreen ? 6 : 0}
                  display="flex"
                  justifyContent="center"
                  data-testid="pagination-counter-bottom"
                >
                  <PaginationElement />
                </Box>
              ) : null}
            </Grid>
          </Grid>
        </Container>
      </Box>
      {!loading && isSmallScreen ? <BackToTop /> : null}
    </>
  );

  /**
   * Memo defs
   */
  function filterCountMemo() {
    return categories.length + filters.length;
  }

  function partNumbersMemo() {
    return (data?.searchProduct?.products || []).map(
      (product) => product.partNumber ?? ''
    );
  }

  /**
   * Effect defs
   */
  function handleBadPage() {
    if (page !== null && page < 0) {
      setParams({ ...params, page: '1' });
    }
  }

  /**
   * Callback Definitions
   */
  function handleResultClick(pageIndex: number) {
    setTrackedSearchTerm(criteria);
    setSearchPage(Number(page));
    setPageIndex(pageIndex);
  }
}

export default Search;
