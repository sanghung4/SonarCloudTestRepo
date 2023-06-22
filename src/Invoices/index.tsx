import { useCallback, useEffect, useMemo, useState, useContext } from 'react';

import {
  Button,
  DateRange as DateRangeType,
  Grid,
  useSnackbar
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { useLocation } from 'react-router-dom';

import { AuthContext } from 'AuthProvider';
import DateRange from 'common/DateRange';
import TablePageLayout from 'common/TablePageLayout';
import TableRenderer from 'common/TablePageLayout/TableRenderer';
import PermissionRequired, { Permission } from 'common/PermissionRequired';
import { AccountInvoice } from 'generated/graphql';
import Buckets from 'Invoices/Buckets';
import { PdfDownloadIcon } from 'icons';
import SearchCard from 'Search/SearchCard';
import {
  handleDownloadClickedCb,
  invoicesColumnsMemo,
  useInvoicesData,
  useInvoicesQueryParam,
  useInvoicesQueryParamUtil,
  useInvoicesTable,
  Age,
  defaultTimeRange
} from 'Invoices/util';
import useDocumentTitle from 'hooks/useDocumentTitle';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import {
  differenceInCalendarDays,
  intervalToDuration,
  differenceInMonths
} from 'date-fns';
import { PdfButtonContainer } from './util/styled';
import MaintenancePage from 'Invoices/Maintenance';

export enum statusOptions {
  'Open' = 'Open',
  'Closed' = 'Closed',
  'All' = 'All'
}

export enum invoiceFilterOptions {
  'Total' = 'All',
  'Future' = 'Future',
  'Current' = 'Current',
  '31-60' = '31-60 days',
  '61-90' = '61-90 days',
  '91-120' = '91-120 days',
  'Over120' = '121+ days',
  'TotalPastDue' = 'All past due'
}

function Invoices() {
  /**
   * Custom hooks
   */
  const { search } = useLocation();
  const { pushAlert } = useSnackbar();
  const { t } = useTranslation();
  const {
    appliedRange,
    bucket,
    invoiceStatus,
    page,
    queryParams,
    searchBy,
    sortBy
  } = useInvoicesQueryParam();

  useDocumentTitle(t('common.invoices'));

  /**
   * Context
   */
  const { selectedAccounts, isEclipse, isMincron } =
    useSelectedAccountsContext();
  const { activeFeatures } = useContext(AuthContext);

  /**
   * Data
   */
  const { data, getInvoicesUrl, invoicesUrlLoading, loading, tableData } =
    useInvoicesData({
      appliedRange,
      bucket,
      invoiceStatus,
      pushAlert,
      selectedAccounts,
      t
    });

  /**
   * Memos
   */
  const columns = useMemo(
    () => invoicesColumnsMemo({ t, search, isMincron }),
    [search, t, isMincron]
  );
  /**
   * State
   */
  const [range, setRange] = useState<DateRangeType>(defaultTimeRange);
  const [filtersLoading, setFiltersLoading] = useState(false);
  const [searchValue, setSearchValue] = useState(searchBy);
  const [statusFilter, setStatusFilter] = useState(invoiceStatus);
  const [invociesAgingFilterValue, setInvoicesAgingFilterValue] = useState(
    invoiceFilterOptions[bucket as keyof typeof invoiceFilterOptions] as string
  );
  const [initialState, setInitialState] = useState(true);
  const [filterValue, setFilterValue] = useState(invoiceStatus);
  const [applied, setApplied] = useState(false);
  const [reset, setReset] = useState(false);

  const warningMessage = useMemo(() => {
    if (
      (statusFilter === statusOptions.All ||
        statusFilter === statusOptions.Closed) &&
      range !== undefined
    ) {
      const monthsDifference = differenceInMonths(new Date(), range.from!);
      const differenceInDays = differenceInCalendarDays(
        new Date(),
        range.from!
      );
      if (isNaN(monthsDifference) || isNaN(differenceInDays)) {
        return;
      } else {
        const { days } = intervalToDuration({
          start: new Date(),
          end: range.from!
        });
        if (
          (monthsDifference < 14 && differenceInDays > 90) ||
          (monthsDifference === 14 && days === 0)
        ) {
          return 'warning';
        } else if (monthsDifference >= 14) {
          return 'error';
        }
      }
      return '';
    }
  }, [range, statusFilter]);

  /**
   * Table
   */
  const tableInstance = useInvoicesTable({
    columns,
    data: tableData,
    page,
    sortBy,
    searchBy
  });

  const resultsCount = useMemo(() => {
    if ((initialState && reset) || (initialState && !queryParams.bucket)) {
      return 0;
    } else if (!reset && queryParams.from && queryParams.to) {
      setRange(appliedRange);
      setApplied(true);
    } else if (!reset && queryParams.bucket) {
      setRange(appliedRange);
      setFilterValue(statusOptions.Open);
      setApplied(true);
    }
    return tableInstance.rows.length;
  }, [
    initialState,
    tableInstance.rows.length,
    queryParams.bucket,
    appliedRange,
    queryParams.from,
    queryParams.to,
    reset
  ]);

  /**
   * data
   */
  const errorMessage = initialState
    ? `${t('invoices.defaultState')}`
    : `${t('invoices.noInvoices')}`;
  /**
   * Effects
   */
  useEffect(showLoadingWhenFiltersChange, [appliedRange, setFiltersLoading]);

  /**
   * Special Param Hook (has to be placed after all other hooks)
   */
  const { clearRange, handleBucketChange, handleReset, handleViewResults } =
    useInvoicesQueryParamUtil({
      loading,
      range,
      searchValue,
      setRange,
      setSearchValue,
      statusFilter,
      setStatusFilter,
      tableInstance,
      invociesAgingFilterValue
    });

  const handleSelectFilterChange = (status: Age) => {
    setInvoicesAgingFilterValue(status);
    if (status !== (invoiceFilterOptions.Total as string)) {
      setFilterValue(statusOptions.Open);
      setStatusFilter(statusOptions.Open);
    }
    setApplied(false);
  };

  /**
   * Table callbacks
   */
  const handleDownloadClicked = useCallback(
    () =>
      handleDownloadClickedCb({
        erpAccountId: selectedAccounts.billTo?.erpAccountId,
        getInvoicesUrl,
        tableInstance
      }),
    [getInvoicesUrl, selectedAccounts, tableInstance]
  );

  return (
    <>
      {activeFeatures?.includes('INVOICE_MAINTENANCE') ? (
        <MaintenancePage />
      ) : (
        <PermissionRequired
          permissions={[Permission.VIEW_INVOICE]}
          redirectTo="/"
        >
          <DateRange
            value={range}
            setApplied={setApplied}
            onChange={setRange}
            onClear={clearRange}
          >
            <TablePageLayout
              invoicesPage
              pageTitle={t('common.invoices')}
              loading={loading}
              filters={
                <Grid
                  container
                  spacing={2}
                  alignContent="flex-end"
                  alignItems="flex-end"
                >
                  <SearchCard
                    placeholder={
                      isEclipse
                        ? t('invoices.invoiceSearchPlaceHolder')
                        : t('invoices.invoiceSearchPlaceHolderForMincron')
                    }
                    search={searchValue}
                    setSearch={setSearchValue}
                    onReset={handleReset}
                    onViewResultsClicked={handleViewResults}
                    resultsCount={resultsCount}
                    range={range}
                    applied={applied}
                    setApplied={setApplied}
                    setReset={setReset}
                    dataTestId="search-invoices-input"
                    selectFilter={statusFilter}
                    setSelectFilter={setStatusFilter}
                    filterValue={filterValue}
                    setFilterValue={setFilterValue}
                    invoicesAgingFilterValue={invociesAgingFilterValue}
                    setInvoicesAgingFilterValue={setInvoicesAgingFilterValue}
                    handleFilterChange={handleSelectFilterChange}
                    selectFilterLabel={t('invoices.age')}
                    setInitialState={setInitialState}
                    warningMessage={warningMessage}
                    selectFilterDefault={t('common.all')}
                    showSelectFilter
                  />
                  <PdfButtonContainer container item md={3} xs={12}>
                    {tableInstance.selectedFlatRows.length > 0 ? (
                      <Button
                        startIcon={<PdfDownloadIcon />}
                        variant="inline"
                        data-testid="download-pdf-button"
                        onClick={handleDownloadClicked}
                      >
                        {t('invoices.downloadPDF')}
                      </Button>
                    ) : null}
                  </PdfButtonContainer>
                </Grid>
              }
              customContent={
                <Buckets
                  accountInvoice={data?.invoices as AccountInvoice}
                  activeBucket={bucket}
                  setApplied={setApplied}
                  setRange={setRange}
                  setInitialState={setInitialState}
                  loading={loading || filtersLoading}
                  onBucketSelected={handleBucketChange}
                  setSelectFilter={setStatusFilter}
                  setFilterValue={setFilterValue}
                  setInvoicesAgingFilterValue={setInvoicesAgingFilterValue}
                />
              }
              table={
                <TableRenderer
                  loading={
                    (queryParams.bucket && loading) ||
                    filtersLoading ||
                    invoicesUrlLoading
                  }
                  resultsCountText={t('common.invoices')}
                  resultsCount={resultsCount}
                  noResultsMessage={errorMessage}
                  // @ts-ignore
                  tableInstance={tableInstance}
                  testId="invoices-table"
                  primaryKey="invoiceNumber"
                />
              }
            />
          </DateRange>
        </PermissionRequired>
      )}
    </>
  );

  /**
   * Effect defs
   */
  function showLoadingWhenFiltersChange() {
    setFiltersLoading(true);
    const timeout = setTimeout(() => setFiltersLoading(false), 300);
    return () => clearTimeout(timeout);
  }
}

export default Invoices;
