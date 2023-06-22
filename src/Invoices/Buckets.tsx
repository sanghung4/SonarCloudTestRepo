import React from 'react';

import {
  alpha,
  DateRange,
  Grid,
  Link,
  Skeleton,
  Typography
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { AccountInvoice } from 'generated/graphql';
import {
  Age,
  ECLIPSE_BILLTRUST,
  emptyDateRange,
  MINCRON_BILLTRUST
} from 'Invoices/util';
import BucketButton from 'Invoices/BucketButton';
import { kebabCase } from 'lodash-es';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import { format } from 'utils/currency';
import InvoiceBucketButton from 'Invoices/InvoiceBucketButton';
import {
  BucketInfoTypography,
  InvoicesBucketContainer,
  InvoicesHeaderContainer,
  InvoicesTitleTypography,
  OpenInvoicesButtonContainer,
  StyledButton,
  StyledDivider,
  StyledDividerContainer
} from 'Invoices/util/styled';
import { invoiceFilterOptions, statusOptions } from 'Invoices';

type Props = {
  accountInvoice?: AccountInvoice;
  activeBucket: Age | string;
  onBucketSelected: (bucket: Age) => void;
  loading?: boolean;
  setApplied?: (applied: boolean) => void;
  setInitialState?: (initialState: boolean) => void;
  setRange?: (range: DateRange) => void;
  setSelectFilter?: (selectFilter: string) => void;
  setFilterValue?: (filterValue: string) => void;
  setInvoicesAgingFilterValue?: (invoiceAgingFilter: string) => void;
};

function Buckets(props: Props) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();
  const { isEclipse } = useSelectedAccountsContext();

  const handleFilterChange = (filterValue: string) => {
    if (props.setApplied && props.setInitialState && props.setRange) {
      props.setInitialState(false);
      props.setApplied(true);
      props.setRange(emptyDateRange);
    }
    props.setInvoicesAgingFilterValue &&
      props.setInvoicesAgingFilterValue(
        (invoiceFilterOptions as any)[filterValue]
      );
    props.setFilterValue && props.setFilterValue(statusOptions.Open);
    props.setSelectFilter && props.setSelectFilter(statusOptions.Open);
  };

  return (
    <>
      <InvoicesHeaderContainer item container spacing={1}>
        <Grid>
          <InvoicesTitleTypography
            variant="h5"
            data-testid={kebabCase(`${t('common.invoices')}-title`)}
          >
            {t('common.invoices')}
          </InvoicesTitleTypography>
        </Grid>
        <Grid>
          <Link
            data-testid="pay-bills-link"
            href={isEclipse ? ECLIPSE_BILLTRUST : MINCRON_BILLTRUST}
            underline="none"
            sx={{ width: 1 }}
            target="_blank"
          >
            <StyledButton
              variant="alternative"
              size="small"
              data-testid="pay-bills-button"
              fullWidth
            >
              {t('invoices.payBills')}
            </StyledButton>
          </Link>
        </Grid>
      </InvoicesHeaderContainer>
      <InvoicesBucketContainer>
        <Grid container>
          <Grid container item md={5} xs={12} spacing={1.5}>
            <Grid item xs={12}>
              <OpenInvoicesButtonContainer
                component="button"
                data-testid="total-button"
                onClick={() => {
                  handleFilterChange('Total');
                  props.onBucketSelected('Total');
                }}
              >
                <BucketInfoTypography color="text.secondary" variant="h5">
                  {t('invoices.totalOpenInvoices')}
                </BucketInfoTypography>
                <Typography color="primary.main" variant="h6">
                  {props.loading ? (
                    <Skeleton width={86} />
                  ) : (
                    format(props?.accountInvoice?.totalAmt ?? 0)
                  )}
                </Typography>
              </OpenInvoicesButtonContainer>
            </Grid>
            <Grid pl={1.5} pt={1.5} md xs={12}>
              <InvoiceBucketButton
                active={props.activeBucket === 'Future'}
                title={t('invoices.future')}
                loading={props.loading}
                onClick={() => {
                  handleFilterChange('Future');
                  props.onBucketSelected('Future');
                }}
                value={props.accountInvoice?.bucketFuture}
              />
            </Grid>
            <Grid item md xs={12}>
              <InvoiceBucketButton
                active={props.activeBucket === 'Current'}
                title={t('invoices.current')}
                loading={props.loading}
                onClick={() => {
                  handleFilterChange('Current');
                  props.onBucketSelected('Current');
                }}
                value={props?.accountInvoice?.currentAmt}
              />
            </Grid>
            <Grid item md xs={12}>
              <InvoiceBucketButton
                active={props.activeBucket === 'TotalPastDue'}
                title="Past due"
                loading={props.loading}
                onClick={() => {
                  handleFilterChange('TotalPastDue');
                  props.onBucketSelected('TotalPastDue');
                }}
                value={props?.accountInvoice?.totalPastDue}
              />
            </Grid>
          </Grid>
          <StyledDividerContainer item md="auto" xs={12}>
            <StyledDivider orientation="vertical" />
          </StyledDividerContainer>
          <Grid container item md xs={12} spacing={1.5}>
            <Grid item xs={12} pb={1}>
              <Typography
                textAlign="center"
                color="text.secondary"
                variant="h5"
              >
                {t('invoices.invoicesPastDue')}
              </Typography>
            </Grid>
            <Grid item md xs={12}>
              <BucketButton
                active={props.activeBucket === '31-60'}
                title="31-60 days"
                loading={props.loading}
                onClick={() => {
                  handleFilterChange('31-60');
                  props.onBucketSelected('31-60');
                }}
                value={props?.accountInvoice?.bucketThirty}
                sx={{
                  background: (theme) => theme.palette.secondary06.main,
                  border: (theme) =>
                    `1px solid ${theme.palette.secondary.main}`,
                  '&:hover': {
                    border: (theme) =>
                      `3px solid ${theme.palette.secondary.main}`
                  }
                }}
              />
            </Grid>
            <Grid item md xs={12}>
              <BucketButton
                active={props.activeBucket === '61-90'}
                title="61-90 days"
                loading={props.loading}
                onClick={() => {
                  handleFilterChange('61-90');
                  props.onBucketSelected('61-90');
                }}
                value={props?.accountInvoice?.bucketSixty}
                sx={{
                  background: (theme) =>
                    `linear-gradient(0deg, ${alpha(
                      theme.palette.secondary07.main,
                      0.7
                    )}, ${alpha(theme.palette.secondary07.main, 0.7)}), ${alpha(
                      theme.palette.secondary07.light,
                      0.5
                    )}`,
                  border: (theme) =>
                    `1px solid ${theme.palette.orangeRed.main}`,
                  '&:hover': {
                    border: (theme) =>
                      `3px solid ${theme.palette.orangeRed.main}`
                  }
                }}
              />
            </Grid>
            <Grid item md xs={12}>
              <BucketButton
                active={props.activeBucket === '91-120'}
                title="91-120 days"
                loading={props.loading}
                onClick={() => {
                  handleFilterChange('91-120');
                  props.onBucketSelected('91-120');
                }}
                value={props?.accountInvoice?.bucketNinety}
                sx={{
                  background: (theme) =>
                    `linear-gradient(0deg, ${alpha(
                      theme.palette.secondary07.main,
                      0.5
                    )}, ${alpha(theme.palette.secondary07.main, 0.5)}), ${alpha(
                      theme.palette.secondary07.light,
                      0.7
                    )}`,
                  border: (theme) =>
                    `1px solid ${theme.palette.orangeRed.main}`,
                  '&:hover': {
                    border: (theme) =>
                      `3px solid ${theme.palette.orangeRed.main}`
                  }
                }}
              />
            </Grid>
            <Grid item md xs={12}>
              <BucketButton
                active={props.activeBucket === 'Over120'}
                title="121+ days"
                loading={props.loading}
                onClick={() => {
                  handleFilterChange('Over120');
                  props.onBucketSelected('Over120');
                }}
                value={props?.accountInvoice?.bucketOneTwenty}
                sx={{
                  background: (theme) =>
                    `linear-gradient(0deg, ${alpha(
                      theme.palette.secondary07.main,
                      0.2
                    )}, ${alpha(theme.palette.secondary07.main, 0.2)}), ${
                      theme.palette.secondary07.light
                    }`,
                  border: (theme) =>
                    `1px solid ${theme.palette.orangeRed.main}`,
                  '&:hover': {
                    border: (theme) =>
                      `3px solid ${theme.palette.orangeRed.main}`
                  }
                }}
              />
            </Grid>
          </Grid>
        </Grid>
      </InvoicesBucketContainer>
    </>
  );
}

export default Buckets;
