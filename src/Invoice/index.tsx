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

import Breadcrumbs from 'common/Breadcrumbs';
import Loader from 'common/Loader';
import { ErpSystemEnum, useGetInvoiceQuery } from 'generated/graphql';
// import { PrintIcon } from 'icons';
import DeliverySummaryCard from 'Invoice/DeliverySummaryCard';
import InfoCard from 'Invoice/InfoCard';
import ProductsCard from 'Invoice/ProductsCard';
import SummaryCard from 'Invoice/SummaryCard';
import useDocumentTitle from 'hooks/useDocumentTitle';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';

/**
 * Types
 */
type RouterState = {
  fromInvoices?: boolean;
  search?: string;
};

function Invoice() {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();
  const { isSmallScreen } = useScreenSize();
  const { id } = useParams<{ id: string }>();
  const location = useLocation<RouterState>();

  /**
   * Context
   */
  const { selectedAccounts } = useSelectedAccountsContext();
  const isMincron = selectedAccounts?.erpSystemName === ErpSystemEnum.Mincron;

  /**
   * Data
   */
  const { data: invoiceQuery, loading } = useGetInvoiceQuery({
    variables: {
      accountId: selectedAccounts?.billTo?.erpAccountId ?? '',
      invoiceNumber: id
    }
  });

  /**
   * Page Title
   */
  useDocumentTitle(
    t('dynamicPageTitles.invoice', {
      invoiceId: invoiceQuery?.invoice.invoiceNumber ?? ''
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
            text: t('common.invoices'),
            to: `/invoices${location.state?.search ?? ''}`
          }
        ]}
      />
      <Container>
        <Grid
          container
          alignItems="center"
          spacing={2}
          pt={isSmallScreen ? 3 : 0}
          pb={3}
        >
          <Grid item md={3}>
            <Typography component="h1" variant="h5">
              {t('invoices.invoiceNumber')}:{' '}
              <Box
                component="span"
                fontWeight={400}
                data-testid="invoice-number"
              >
                {id}
              </Box>
            </Typography>
          </Grid>
          <Grid item md={2} xs={12}>
            <Typography component="h2" variant="h5">
              {t('common.status')}:{' '}
              <Box
                color={
                  invoiceQuery?.invoice?.status === 'Open'
                    ? 'success.main'
                    : 'primary'
                }
                data-testid="invoice-status"
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
                  invoiceQuery?.invoice?.status &&
                  t(`orders.${camelCase(invoiceQuery?.invoice?.status ?? '')}`)
                )}
              </Box>
            </Typography>
          </Grid>
          <Grid item md={5.95}>
            <Typography component="h2" variant="h5">
              {t('invoice.terms')}:{' '}
              <Box
                component="span"
                fontWeight={400}
                data-testid="invoice-terms"
              >
                {invoiceQuery?.invoice?.terms}
              </Box>
            </Typography>
          </Grid>
          {/* <Hidden mdDown>
            <Grid item>
              <Button
                startIcon={<PrintIcon color="primary" />}
                onClick={handlePrint}
                variant="inline"
                data-testid="print-button"
                className="noprint"
              >
                {t('common.print')}
              </Button>
            </Grid>
          </Hidden> */}
        </Grid>
        <Grid container spacing={2} pb={3} sx={{ breakInside: 'avoid' }}>
          <Grid item md={6}>
            <InfoCard loading={loading} invoice={invoiceQuery?.invoice} />
          </Grid>
          <Grid item md={6}>
            <DeliverySummaryCard
              loading={loading}
              invoice={invoiceQuery?.invoice}
            />
          </Grid>
          <Grid
            container
            item
            spacing={2}
            direction={isSmallScreen ? 'column-reverse' : 'row'}
          >
            <Grid item md={8}>
              <ProductsCard
                loading={loading}
                invoice={invoiceQuery?.invoice}
                isMincron={isMincron}
              />
            </Grid>
            <Grid item md={4}>
              <SummaryCard loading={loading} invoice={invoiceQuery?.invoice} />
            </Grid>
          </Grid>
        </Grid>
      </Container>
    </>
  );
}

export default Invoice;
