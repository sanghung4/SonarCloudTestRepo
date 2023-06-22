import { useContext, useMemo } from 'react';

import {
  Box,
  Container,
  Grid,
  Skeleton,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { camelCase } from 'lodash-es';
import { useTranslation } from 'react-i18next';
import { useLocation, useParams } from 'react-router-dom';

import { AuthContext } from 'AuthProvider';
import Breadcrumbs from 'common/Breadcrumbs';
import { useGetOrderQuery, useGetProductPricingQuery } from 'generated/graphql';
// import { PrintIcon } from 'icons';
import DeliverySummaryCard from 'Order/DeliverySummaryCard';
import InfoCard from 'Order/InfoCard';
import SummaryCard from 'Order/SummaryCard';
import ProductsCard from 'Order/ProductsCard';
import { useQueryParams } from 'hooks/useSearchParam';
import Loader from 'common/Loader';
import useDocumentTitle from 'hooks/useDocumentTitle';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import { BranchContext } from 'providers/BranchProvider';
import BackOrderWarning from 'Order/BackOrderWarning';

// Test Data
// 302841 / 425275
//   - S109763308.001 (Comment)
//   - S110109307.001 (Comment)
//   - S110099895.001 (Comment)
//   - S110115782.001 (Comment)
//   - S110162526.001 (2 Comments)
// 35648 / 11336
//   - S109376323.001 (NonStock)
//   - S109376285.001 (NonStock)
//   - S109369907.001 (NonStock, big order)
//   - S109369907.001 (NonStock, big order)
//   - S109348247.002 (NonStock, big order)
//   - S109348170.001 (NonStock, big order)
//
//   - Most orders in March have unavailable items.

/**
 * Types
 */
export type OrderRouterState = {
  fromInvoices?: boolean;
  search?: string;
};

type OrderParams = {
  orderStatus?: string;
};

function Order() {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();
  const { isSmallScreen } = useScreenSize();
  const { id } = useParams<{ id: string }>();
  const location = useLocation<OrderRouterState>();
  const [queryParams] = useQueryParams<OrderParams>();

  /**
   * Context
   */
  const { profile } = useContext(AuthContext);
  const {
    selectedAccounts: { billTo, shipTo },
    isMincron
  } = useSelectedAccountsContext();
  const { shippingBranch } = useContext(BranchContext);

  /**
   * Data
   */
  const { data: orderQuery, loading } = useGetOrderQuery({
    fetchPolicy: 'cache-first',
    variables: {
      accountId: isMincron ? billTo?.id ?? '' : shipTo?.id ?? '',
      orderId: id.split('.')[0],
      userId: profile?.userId ?? '',
      invoiceNumber: id.split('.')?.[1],
      orderStatus: isMincron ? queryParams.orderStatus : undefined
    }
  });

  const partNumbers = useMemo(partNumbersMemo, [orderQuery?.order.lineItems]);

  const isBackOrdered = useMemo(backOrderMemo, [orderQuery?.order]);

  const { data: pricingData, loading: pricingDataLoading } =
    useGetProductPricingQuery({
      fetchPolicy: 'cache-first',
      skip: !orderQuery?.order.lineItems?.length,
      variables: {
        input: {
          customerId: billTo?.erpAccountId ?? '',
          branchId: shippingBranch?.branchId ?? '',
          productIds: partNumbers,
          includeListData: true
        }
      }
    });

  /**
   * Page Title
   */
  useDocumentTitle(
    t('dynamicPageTitles.order', {
      orderNumber: id
    })
  );

  /**
   * Callbacks
   */
  // Commenting this out until we starting fixing printing.
  // const handlePrint = () => window.print();

  return (
    <>
      {loading && <Loader backdrop />}
      <Breadcrumbs
        pageTitle={id}
        config={[
          {
            text: location.state?.fromInvoices
              ? t('common.invoices')
              : t('common.orders'),
            to: location.state?.fromInvoices
              ? `/invoices${location.state?.search ?? ''}`
              : `/orders${location.state?.search ?? ''}`
          }
        ]}
      />

      <Container>
        <Grid
          className="printGridBlock"
          container
          alignItems="center"
          spacing={2}
          sx={{ pt: isSmallScreen ? 3 : 0, pb: 3 }}
        >
          <Grid item md={4} className="printGridWidth_33_inline">
            <Typography component="h1" variant="h5">
              {t('common.orderNumber')}:{' '}
              <Box component="span" fontWeight={400} data-testid="order-number">
                {id}
              </Box>
            </Typography>
          </Grid>
          <Grid item md xs={12} className="printGridWidth_66_inline">
            <Typography component="h2" variant="h5">
              {t('common.status')}
              {': '}
              <Box
                color={
                  orderQuery?.order?.webStatus === 'INVOICED'
                    ? 'success.main'
                    : orderQuery?.order?.webStatus === 'SUBMITTED'
                    ? 'purple.main'
                    : 'darkYellow.main'
                }
                data-testid="order-status"
                component="span"
                fontWeight={400}
              >
                {loading ? (
                  <Skeleton
                    width={120}
                    height={24}
                    sx={{ display: 'inline-block', pl: 1 }}
                  />
                ) : (
                  t(`orders.${camelCase(orderQuery?.order?.webStatus ?? '')}`)
                )}
              </Box>
            </Typography>
          </Grid>
          {/* <Hidden mdDown>
            <Grid item className="printButton">
              <Button
                startIcon={<PrintIcon color="primary" />}
                onClick={handlePrint}
                variant="inline"
                data-testid="print-button"
              >
                {t('common.print')}
              </Button>
            </Grid>
          </Hidden> */}
        </Grid>
        <Grid className="printGridBlock" container spacing={2} sx={{ pb: 3 }}>
          <Grid item md={4} className="printGridWidth_33_inline">
            <InfoCard loading={loading} order={orderQuery?.order} />
          </Grid>
          <Grid item md={8} className="printGridWidth_66_inline">
            <DeliverySummaryCard loading={loading} order={orderQuery?.order} />
          </Grid>
          {isMincron && isBackOrdered && (
            <Grid container item className="printGridBlock">
              <Grid item md={12} className="printGridWidth_100">
                <BackOrderWarning />
              </Grid>
            </Grid>
          )}
          <Grid
            container
            item
            spacing={2}
            direction={isSmallScreen ? 'column-reverse' : 'row'}
            className="printGridBlock"
          >
            <Grid item md={8.5} className="printGridWidth_100">
              <ProductsCard
                loading={loading || pricingDataLoading}
                order={orderQuery?.order}
                isMincron={isMincron}
                pricingData={pricingData?.productPricing.products ?? []}
              />
            </Grid>
            <Grid item md={3.5} className="printGridWidth_50 printBreakPage">
              <SummaryCard
                loading={loading || pricingDataLoading}
                order={orderQuery?.order}
                pricingData={pricingData?.productPricing.products ?? []}
              />
            </Grid>
          </Grid>
        </Grid>
      </Container>
    </>
  );

  function partNumbersMemo() {
    const lineItemList = orderQuery?.order.lineItems;
    if (lineItemList?.length) {
      return lineItemList
        .map((item) => item?.productId)
        .map((productId) => productId?.replace('MSC-', ''))
        .filter((entry) => !!entry) as string[];
    }
    return [];
  }

  function backOrderMemo() {
    return !!orderQuery?.order?.lineItems?.filter((i) => {
      return !!i.backOrderedQuantity;
    })?.length;
  }
}

export default Order;
