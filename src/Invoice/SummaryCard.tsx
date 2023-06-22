import React from 'react';

import {
  Box,
  Card,
  Grid,
  Divider,
  Paper,
  Skeleton,
  Typography,
  alpha
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { format } from 'utils/currency';
import { Invoice } from 'generated/graphql';

type Props = {
  loading?: boolean;
  invoice?: Invoice;
};

function SummaryCard(props: Props) {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();

  return (
    <Card sx={{ px: 2, py: 3 }}>
      <Box pb={3} px={3}>
        <Typography
          color="primary"
          variant="h5"
          data-testid="invoice-summary-header"
        >
          {t('invoice.invoiceSummary')}
        </Typography>
      </Box>
      <Paper
        elevation={0}
        sx={{
          px: 2,
          py: 3,
          bgcolor: (theme) => alpha(theme.palette.primary02.main, 0.05)
        }}
      >
        {/* INVOICE SUBTOTAL*/}

        <Grid container px={1}>
          <Grid item xs={6}>
            <Typography color="primary" fontWeight={500}>
              {t('invoice.subtotal')}
            </Typography>
          </Grid>
          <Grid item xs={6}>
            <Typography
              color="primary"
              align="right"
              data-testid="invoice-summary-subtotal"
            >
              {props.loading ? (
                <Box display="flex" justifyContent="flex-end" component="span">
                  <Skeleton width={80} />
                </Box>
              ) : (
                format(parseFloat(props?.invoice?.subtotal ?? '0'))
              )}
            </Typography>
          </Grid>
          <Grid item xs={6}>
            <Typography color="primary">{t('common.tax')}</Typography>
          </Grid>
          <Grid item xs={6}>
            <Typography
              color="primary"
              align="right"
              data-testid="invoice-summary-tax"
            >
              {props.loading ? (
                <Box display="flex" justifyContent="flex-end" component="span">
                  <Skeleton width={80} />
                </Box>
              ) : (
                format(parseFloat(props?.invoice?.tax ?? '0'))
              )}
            </Typography>
          </Grid>
          <Grid item xs={6}>
            <Typography color="primary">{t('invoice.other')}</Typography>
          </Grid>
          <Grid item xs={6}>
            <Typography
              color="primary"
              align="right"
              data-testid="invoice-summary-other"
            >
              {props.loading ? (
                <Box display="flex" justifyContent="flex-end" component="span">
                  <Skeleton width={80} />
                </Box>
              ) : (
                format(parseFloat(props?.invoice?.otherCharges ?? '0'))
              )}
            </Typography>
          </Grid>
        </Grid>
        <Box my={2}>
          <Divider />
        </Box>

        {/*TOTAL AMOUNT AND PAID TO DATE*/}

        <Grid container spacing={2} px={1}>
          <Grid item xs={6}>
            <Typography color="primary" fontWeight={500}>
              {t('invoice.totalAmountDue')}
            </Typography>
          </Grid>
          <Grid item xs={6}>
            <Typography
              color="primary"
              align="right"
              data-testid="invoice-summary-totalAmountDue"
            >
              {props.loading ? (
                <Box display="flex" justifyContent="flex-end" component="span">
                  <Skeleton width={80} />
                </Box>
              ) : (
                format(parseFloat(props?.invoice?.originalAmt ?? '0'))
              )}
            </Typography>
          </Grid>
          <Grid item xs={6}>
            <Typography color="primary" sx={{ fontWeight: 500 }}>
              {t('invoice.paidToDate')}
            </Typography>
          </Grid>
          <Grid item xs={6}>
            <Typography
              color="primary"
              align="right"
              data-testid="invoice-summary-paidToDate"
            >
              {props.loading ? (
                <Box display="flex" justifyContent="flex-end" component="span">
                  <Skeleton width={80} />
                </Box>
              ) : (
                format(parseFloat(props?.invoice?.paidToDate ?? '0'))
              )}
            </Typography>
          </Grid>
        </Grid>
        <Box my={2}>
          <Divider />
        </Box>

        {/**REMAINING AMOUNT */}

        <Grid container spacing={2} px={1}>
          <Grid item xs={6}>
            <Typography color="primary" fontWeight={500}>
              {t('invoice.remainingAmount')}
            </Typography>
          </Grid>
          <Grid item xs={6}>
            <Typography
              color="primary"
              align="right"
              display="flex"
              justifyContent="flex-end"
              data-testid="invoice-summary-remainingAmount"
            >
              {props.loading ? (
                <Skeleton width={80} />
              ) : (
                format(parseFloat(props?.invoice?.openBalance ?? '0'))
              )}
            </Typography>
          </Grid>
        </Grid>
      </Paper>
    </Card>
  );
}

export default SummaryCard;
