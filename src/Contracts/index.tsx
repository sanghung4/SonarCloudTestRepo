import { useContext, useState } from 'react';

import {
  Box,
  useScreenSize,
  DateRange as DateRangeType,
  Divider,
  Grid,
  Pagination,
  Typography
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { Link, Redirect } from 'react-router-dom';

import { AuthContext } from 'AuthProvider';
import DateRange from 'common/DateRange';
import Loader from 'common/Loader';
import TablePageLayout from 'common/TablePageLayout';
import TableRenderer from 'common/TablePageLayout/TableRenderer';
import ContractCard from 'Contracts/ContractCard';
import useContractsEffect from 'Contracts/util/useContractsEffect';
import useContractsTable from 'Contracts/util/useContractsTable';
import { Contract } from 'generated/graphql';
import SearchCard from 'Search/SearchCard';
import { formatDate } from 'utils/dates';
import { appliedRangeMemo, handleGoToPage } from 'utils/tableUtils';
import { useQueryParams } from 'hooks/useSearchParam';
import useDocumentTitle from 'hooks/useDocumentTitle';

const customHeaderProps = {
  backgroundColor: 'lighterBlue.main',
  fontSize: '16px',
  fontWeight: 500,
  marginRight: '8px',
  fontFamily: 'Roboto',
  paddingTop: 0,
  paddingBottom: 0
};

const customCellProps = {
  height: '45px'
};

const emptyDateRange = {
  from: undefined,
  to: undefined
};

export type ContractsParams = {
  page: string;
  sortBy: string[];
  searchBy: string;
  from: string;
  to: string;
};

function Contracts() {
  /**
   * Custom hooks
   */
  const [queryParams, setQueryParams] = useQueryParams<ContractsParams>({
    arrayKeys: ['sortBy']
  });
  const { searchBy = '', from, to } = queryParams;
  const { t } = useTranslation();
  const { isSmallScreen } = useScreenSize();
  useDocumentTitle(t('common.contracts'));

  /**
   * Context
   */
  const { ecommUser } = useContext(AuthContext);

  /**
   * States
   */
  const [searchValue, setSearchValue] = useState(searchBy);
  const [range, setRange] = useState<DateRangeType>(
    appliedRangeMemo({ from, to })
  );

  /**
   * Table
   */
  const {
    contractsQuery,
    tableInstance,
    refetch: getContracts,
    loading: contractsLoading
  } = useContractsTable();

  /**
   * Effects
   */
  useContractsEffect(tableInstance);

  return ecommUser?.role?.name === t('roles.invoiceOnly') ? (
    <Redirect to="/" />
  ) : (
    <DateRange value={range} onChange={setRange} onClear={clearRange}>
      <TablePageLayout
        pageTitle={t('contracts.contracts')}
        filters={
          <SearchCard
            placeholder={t('contracts.contractSearchPlaceholder')}
            search={searchValue}
            setSearch={setSearchValue}
            onReset={onReset}
            onViewResultsClicked={onViewResultsClicked}
            resultsCount={contractsQuery?.contracts?.totalRows}
            dataTestId="search-contracts-input"
          />
        }
        table={
          <>
            {isSmallScreen ? (
              contractsLoading ? (
                <Loader />
              ) : tableInstance.pageCount ? (
                <Box pb={2}>
                  <Box display="flex" flexDirection="column" width={1}>
                    {tableInstance.data.map((d: any, i: number) => (
                      <ContractCard
                        contract={d as Contract}
                        isEven={i % 2 === 0}
                        key={`contract-card-${i}`}
                        index={i}
                      />
                    ))}
                  </Box>
                  <Divider />
                  <Box px={3} py={2}>
                    <Grid
                      container
                      item
                      xs={12}
                      direction="column-reverse"
                      justifyContent="center"
                    >
                      <Grid container item xs="auto" justifyContent="center">
                        <Box textAlign="center" mt={2} fontSize="18px">
                          {!!contractsQuery?.contracts?.totalRows &&
                            `${contractsQuery?.contracts?.totalRows} ${t(
                              'contracts.contracts'
                            )}`}
                        </Box>
                      </Grid>
                      {!!tableInstance.pageCount && (
                        <Grid container item justifyContent="center" xs>
                          <Pagination
                            current={tableInstance.state.pageIndex + 1}
                            count={tableInstance.pageCount}
                            ofText={t('common.of')}
                            onChange={handleGoToPage(tableInstance.gotoPage)}
                            onPrev={tableInstance.previousPage}
                            onNext={tableInstance.nextPage}
                            data-testid="pagination-container"
                            dataTestIdCurrentPage="current-page-number"
                            dataTestIdTotalNumberOfPages="total-number-of-pages"
                          />
                        </Grid>
                      )}
                    </Grid>
                  </Box>
                </Box>
              ) : (
                <Box>
                  <Grid container item justifyContent="center" xs>
                    <Typography
                      variant="h4"
                      align="center"
                      color="secondary03.main"
                    >
                      {t('contracts.noContracts')}
                    </Typography>
                  </Grid>
                  <Grid container item justifyContent="center" xs>
                    <Typography
                      variant="h5"
                      align="center"
                      color="secondary03.main"
                      pb={10}
                      pr={6}
                      pl={6}
                    >
                      {t('contracts.noContractsContactMessage')}{' '}
                      <Link to="/support">
                        {t('contracts.noContractsContactBranch')}
                      </Link>
                    </Typography>
                  </Grid>
                </Box>
              )
            ) : (
              <TableRenderer
                loading={contractsLoading}
                resultsCount={contractsQuery?.contracts?.totalRows}
                noResultsMessage={t('contracts.noContracts')}
                resultsCountText={t('contracts.contracts').toLowerCase()}
                tableInstance={tableInstance}
                testId="contracts-table"
                customHeaderProps={customHeaderProps}
                customCellProps={customCellProps}
                isWaterworks
                noResultsContactMessage={t(
                  'contracts.noContractsContactMessage'
                )}
                noResultsContactBranch={t('contracts.noContractsContactBranch')}
                primaryKey="contractNumber"
              />
            )}
          </>
        }
      />
    </DateRange>
  );
  /**
   * Callbacks
   */
  async function onViewResultsClicked() {
    tableInstance.setGlobalFilter(searchValue);
    tableInstance.gotoPage(0);

    setQueryParams({
      ...queryParams,
      from: formatDate(range.from ?? ''),
      to: formatDate(range.to ?? ''),
      searchBy: searchValue
    });

    getContracts();
  }

  async function onReset() {
    tableInstance.setGlobalFilter(undefined);
    tableInstance.gotoPage(0);
    clearRange();
    setSearchValue('');

    setQueryParams({
      ...queryParams,
      searchBy: '',
      from: '',
      to: ''
    });

    getContracts();
  }
  function clearRange() {
    setRange(emptyDateRange);
  }
}

export default Contracts;
