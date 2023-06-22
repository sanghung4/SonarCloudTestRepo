import React, { ChangeEvent, useContext } from 'react';

import {
  Box,
  Grid,
  DateRange as DateRangeType,
  MenuItem,
  Select,
  TextField,
  Typography,
  Alert
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { DateRangeContext } from 'common/DateRange';
import DateRangeInput from 'common/DateRange/DateRangeInput';
import FilterActionButtons from 'common/TablePageLayout/FilterActionButtons';
import { ToggleButtonGroup } from '@mui/material';
import { InvoicesStatusButton } from 'Invoices/util/styled';
import { Age } from 'Invoices/util';
import { invoiceFilterOptions, statusOptions } from 'Invoices/index';
import {
  DateRangeErrorTypography,
  DateRangeWarningContainer,
  DateRangeWarningTypography
} from './util/Styled';
import { WarningIcon } from 'icons';
import 'Search/util/styles.scss';

type Props = {
  search: string;
  setSearch: (search: string) => void;
  onViewResultsClicked: () => void;
  resultsCount?: number;
  onReset: () => void;
  placeholder?: string;
  dataTestId?: string;
  invoicesAgingFilterValue?: string;
  handleFilterChange?: (filterValue: Age) => void;
  selectFilter?: string;
  applied?: boolean;
  setApplied?: (applied: boolean) => void;
  setReset?: (reset: boolean) => void;
  range?: DateRangeType;
  filterValue?: string;
  selectFilterLabel?: string;
  selectFilterDefault?: string;
  setSelectFilter?: (selectFilter: string) => void;
  setFilterValue?: (filterValue: string) => void;
  setInitialState?: (initialState: boolean) => void;
  setInvoicesAgingFilterValue?: (invoiceAgingFilter: string) => void;
  warningMessage?: string;
  showSelectFilter?: boolean;
  ordersPage?: boolean;
};

function SearchCard(props: Props) {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();

  /**
   * Context
   */
  const ctx = useContext(DateRangeContext);

  const invoiceAgingFilters = Object.values(invoiceFilterOptions);

  const invocieStatusFilters = Object.values(statusOptions);

  /**
   * Callbacks
   */
  const onResetClicked = () => {
    if (props.showSelectFilter && props.setInvoicesAgingFilterValue) {
      props.setFilterValue && props.setFilterValue(statusOptions.All);
      props.setInvoicesAgingFilterValue(statusOptions.All);
    }
    props.setApplied && props.setApplied(false);
    props.setInitialState && props.setInitialState(true);
    props.setReset && props.setReset(true);
    props.onReset();
  };

  const handleSearch = (e: ChangeEvent<HTMLInputElement>) => {
    props.setSearch(e.target.value);
    if (props.setApplied) {
      e.target.value !== '' ? props.setApplied(false) : props.setApplied(true);
    }
  };

  const handleInvoiceStatus = (_e: any, status: string) => {
    if (
      props.setInvoicesAgingFilterValue &&
      (status === statusOptions.All || status === statusOptions.Closed)
    ) {
      props.setInvoicesAgingFilterValue(statusOptions.All);
    }
    props.setFilterValue && props.setFilterValue(status);
    if (props.setSelectFilter) {
      props.setSelectFilter(status!);
    }
    props.setApplied && props.setApplied(false);
  };

  return (
    <Grid container spacing={1} alignContent="flex-end" alignItems="flex-start">
      <Grid item xs={12} md={props?.ordersPage ? 4 : 3}>
        <TextField
          sx={{ zIndex: 0 }}
          label={t('common.searchBy')}
          placeholder={props.placeholder || ''}
          value={props.search}
          onChange={handleSearch}
          inputProps={{
            'data-testid': props.dataTestId || ''
          }}
          fullWidth
        />
      </Grid>

      {props.dataTestId === 'search-invoices-input' && props.showSelectFilter && (
        <>
          <Grid item xs={12} md="auto" marginBottom="7px">
            <Typography mb={1}>{t('common.status')}</Typography>
            <ToggleButtonGroup
              fullWidth
              value={props.filterValue}
              data-testid="invoice-status-toggle-button"
              onChange={handleInvoiceStatus}
              exclusive
            >
              {invocieStatusFilters.map((option) => (
                <InvoicesStatusButton disableRipple value={option} key={option}>
                  {option}
                </InvoicesStatusButton>
              ))}
            </ToggleButtonGroup>
          </Grid>
          <Grid item xs={12} md marginBottom="7px">
            <Select
              label={props.selectFilterLabel}
              name={props.selectFilterLabel}
              value={props.invoicesAgingFilterValue}
              onChange={(event: any) => {
                if (props.handleFilterChange) {
                  props.handleFilterChange(event?.target.value);
                }
              }}
              inputProps={{
                'data-testid': `${props.dataTestId}-select-filter`
              }}
              fullWidth
            >
              {invoiceAgingFilters.map((option) => (
                <MenuItem value={option} key={option}>
                  {option}
                </MenuItem>
              ))}
            </Select>
          </Grid>
        </>
      )}
      <Grid item xs={12} md={4} lg={3}>
        <DateRangeInput
          invoiceStatus={props.filterValue}
          popper
          dataTestId={props.dataTestId}
        />
        <Grid xs={12}>
          {props.warningMessage === 'error' && (
            <DateRangeErrorTypography>
              {t('invoices.rangeError')}
            </DateRangeErrorTypography>
          )}
          {props.warningMessage === 'warning' && !props.ordersPage && (
            <DateRangeWarningContainer>
              <Box component={WarningIcon} color="secondary.main" />
              <Box flex={1}>
                <DateRangeWarningTypography>
                  {t('invoices.rangeWarning')}
                </DateRangeWarningTypography>
              </Box>
            </DateRangeWarningContainer>
          )}
        </Grid>
      </Grid>
      <FilterActionButtons
        warningMessage={props.warningMessage}
        range={props.range}
        applied={props.applied}
        invoiceStatus={props.filterValue}
        dirty={Boolean(
          ctx?.value?.from ||
            ctx?.value?.to ||
            props.search.length > 0 ||
            props.showSelectFilter
        )}
        hideApplyOnMobile={false}
        resultsCount={
          props.dataTestId === 'search-invoices-input' && props.showSelectFilter
            ? undefined
            : props.resultsCount
        }
        onViewResults={() => {
          if (props.setInitialState) {
            props.setInitialState(false);
          }
          props.setApplied && props.setApplied(true);
          props.onViewResultsClicked();
        }}
        onReset={onResetClicked}
      />
      {props.warningMessage === 'warning' && props.ordersPage && (
        <Grid item xs={12}>
          <Alert
            className="search-alert"
            icon={<WarningIcon className="search-alert__warning-icon" />}
            data-testid="orders-search-warning-alert"
          >
            <b>{t('common.warning')}:</b> {t('common.thirtyDayWarningMessage')}
          </Alert>
        </Grid>
      )}
    </Grid>
  );
}

export default SearchCard;
