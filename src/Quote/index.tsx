import React, { useContext, useMemo } from 'react';

import {
  Box,
  Button,
  Container,
  Grid,
  Hidden,
  Skeleton,
  Typography,
  useScreenSize,
  useSnackbar,
  useTheme
} from '@dialexa/reece-component-library';
import { camelCase } from 'lodash-es';
import { useTranslation } from 'react-i18next';
import { useHistory, useLocation, useParams } from 'react-router-dom';

import DeliverySummaryCard from './DeliverySummaryCard';
import ProductsCard from './ProductsCard';
import SummaryCard from './SummaryCard';
import InfoCard from './InfoCard';
import { AuthContext } from 'AuthProvider';
import Breadcrumbs from 'common/Breadcrumbs';
import Loader from 'common/Loader';
import {
  useGetProductPricingQuery,
  useQuoteQuery,
  useRejectQuoteMutation
} from 'generated/graphql';
import { PrintIcon } from 'icons';
import useDocumentTitle from 'hooks/useDocumentTitle';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import { BranchContext } from 'providers/BranchProvider';

type RouterState = {
  search?: string;
};

type RouterParams = {
  id: string;
};

function Quote() {
  /**
   * Custom Hooks
   */
  const history = useHistory();
  const location = useLocation<RouterState>();
  const theme = useTheme();
  const { id } = useParams<RouterParams>();
  const { isSmallScreen } = useScreenSize();
  const { pushAlert } = useSnackbar();
  const { t } = useTranslation();

  /**
   * Context
   */
  const { profile } = useContext(AuthContext);
  const { selectedAccounts } = useSelectedAccountsContext();
  const { shippingBranch } = useContext(BranchContext);

  /**
   * Data
   */
  const { data: quoteQuery, loading } = useQuoteQuery({
    variables: {
      accountId: selectedAccounts?.shipTo?.id ?? '',
      quoteId: id,
      userId: profile?.userId ?? ''
    }
  });
  const [rejectQuote, { loading: rejectLoading }] = useRejectQuoteMutation({
    variables: {
      userId: profile?.userId ?? '',
      shipToAccountId: selectedAccounts?.shipTo?.id ?? '',
      quoteId: id
    },
    onCompleted: () => {
      pushAlert(t('quotes.quoteDeclined'), { variant: 'success' });
      history.push('/quotes');
    }
  });

  const partNumbers = useMemo(partNumbersMemo, [quoteQuery?.quote.lineItems]);

  const { data: pricingData } = useGetProductPricingQuery({
    fetchPolicy: 'cache-first',
    skip: !quoteQuery?.quote.lineItems?.length,
    variables: {
      input: {
        customerId: selectedAccounts.billTo?.erpAccountId ?? '',
        branchId: shippingBranch?.branchId ?? '',
        productIds: partNumbers,
        includeListData: false
      }
    }
  });

  /**
   * Page Title
   */
  useDocumentTitle(t('dynamicPageTitles.quote', { quoteId: id ?? '' }));

  /**
   * Memo
   */
  const colorMap = useMemo(colorMapMemo, [theme]);

  const summaryCard = (
    <Grid item md={4} xs={12}>
      <SummaryCard
        loading={loading}
        quote={quoteQuery?.quote}
        onReject={rejectQuote}
      />
    </Grid>
  );

  function partNumbersMemo() {
    const lineItemList = quoteQuery?.quote.lineItems;
    if (lineItemList?.length) {
      return lineItemList
        .map((item) => item?.productId)
        .map((productId) => productId?.replace('MSC-', ''))
        .filter((entry) => !!entry) as string[];
    }
    return [];
  }

  return (
    <>
      {loading || rejectLoading ? <Loader backdrop /> : null}
      <Box displayPrint="none">
        <Breadcrumbs
          pageTitle={id}
          config={[
            {
              text: t('common.quotes'),
              to: `/quotes${location.state?.search ?? ''}`
            }
          ]}
        />
      </Box>
      <Container>
        <Grid
          container
          alignItems="center"
          spacing={2}
          sx={{ pb: 3, pt: isSmallScreen ? 3 : 0 }}
        >
          <Grid item md={4} xs={12}>
            <Typography component="h1" variant="h5">
              {t('common.quoteNumber')}
              {': '}
              <Box component="span" fontWeight={400}>
                {id}
              </Box>
            </Typography>
          </Grid>
          <Grid item md xs={12}>
            <Typography component="h2" variant="h5">
              {t('common.status')}
              {': '}
              <Box
                component="span"
                fontWeight={400}
                sx={{
                  color:
                    quoteQuery?.quote?.webStatus === undefined
                      ? 'text.primary'
                      : colorMap[quoteQuery?.quote?.webStatus as QuoteStatus]
                }}
              >
                {loading ? (
                  <Skeleton
                    width={120}
                    height={24}
                    sx={{ display: 'inline-block', pl: 1 }}
                  />
                ) : (
                  t(`quotes.${camelCase(quoteQuery?.quote.webStatus ?? '')}`)
                )}
              </Box>
            </Typography>
          </Grid>
          <Hidden mdDown>
            <Grid item sx={{ displayPrint: 'none' }}>
              <Button
                startIcon={<PrintIcon color="primary" />}
                onClick={() => window.print()}
                variant="inline"
              >
                {t('common.print')}
              </Button>
            </Grid>
          </Hidden>
        </Grid>
        <Grid container spacing={2} sx={{ pb: 3 }}>
          {isSmallScreen ? summaryCard : null}
          <Grid item md={4} xs={12}>
            <InfoCard loading={loading} quote={quoteQuery?.quote} />
          </Grid>
          <Grid item md={8} xs={12}>
            <DeliverySummaryCard loading={loading} quote={quoteQuery?.quote} />
          </Grid>
          <Grid item container spacing={2}>
            <Grid item md={8} xs={12}>
              <ProductsCard
                loading={loading}
                quote={quoteQuery?.quote}
                pricingData={pricingData?.productPricing.products ?? []}
              />
            </Grid>
            {isSmallScreen ? null : summaryCard}
          </Grid>
        </Grid>
      </Container>
    </>
  );

  function colorMapMemo() {
    return {
      ACTIVE: theme.palette.success.main,
      EXPIRED: theme.palette.error.main,
      ORDER_PENDING: theme.palette.secondary.main,
      REQUESTED: theme.palette.secondary02.main,
      SUBMITTED: theme.palette.secondary02.main
    };
  }

  type QuoteStatus = keyof ReturnType<typeof colorMapMemo>;
}

export default Quote;
