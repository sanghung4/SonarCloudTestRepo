import {
  Age,
  clearRange,
  defaultTimeRange,
  emptyDateRange,
  handleBucketChange,
  HandleInvoiceCBProps,
  handleReset,
  handleViewResults,
  SyncInvoiceParamEffectProps,
  syncQueryParamEffect
} from 'Invoices/util';
import { mockTableInstance } from 'test-utils/mockTableInstance';

const mockInvoiceTable = {
  ...mockTableInstance,
  goToPage: jest.fn(),
  setGlobalFilter: jest.fn()
};
const mockEffectProps: SyncInvoiceParamEffectProps = {
  loading: false,
  page: '1',
  queryParams: { bucket: 'Total' },
  setQueryParams: jest.fn(),
  sortBy: [],
  // @ts-ignore - ignoreing type because we cannot strictly mock <Invoice>
  tableInstance: mockInvoiceTable
};

const mockCbProps: HandleInvoiceCBProps = {
  setRange: jest.fn(),
  setSearchValue: jest.fn(),
  setQueryParams: jest.fn(),
  setStatusFilter: jest.fn(),
  // @ts-ignore - ignoreing type because we cannot strictly mock <Invoice>
  tableInstance: mockInvoiceTable,
  queryParams: { bucket: 'Total' },
  range: emptyDateRange,
  searchValue: '',
  statusFilter: 'All'
};

describe('Invoices List - queryParam', () => {
  it('expect `syncQueryParamEffect` to stop at loading', () => {
    mockEffectProps.loading = true;
    syncQueryParamEffect(mockEffectProps);
    expect(mockEffectProps.setQueryParams).toBeCalledTimes(0);
    expect(mockEffectProps.tableInstance.gotoPage).toBeCalledTimes(0);
  });

  it('expect `syncQueryParamEffect` to stop at different page', () => {
    mockEffectProps.loading = false;
    mockEffectProps.tableInstance.state.pageIndex = 2;
    mockEffectProps.page = '1';
    syncQueryParamEffect(mockEffectProps);
    expect(mockEffectProps.setQueryParams).toBeCalledTimes(1);
    expect(mockEffectProps.tableInstance.gotoPage).toBeCalledTimes(0);
  });
  it('expect `syncQueryParamEffect` to stop at different sort string', () => {
    mockEffectProps.tableInstance.state.pageIndex = 0;
    mockEffectProps.tableInstance.state.sortBy = [
      {
        desc: false,
        id: 'invoiceDate'
      }
    ];
    mockEffectProps.sortBy = ['!invoiceDate'];
    syncQueryParamEffect(mockEffectProps);
    expect(mockEffectProps.setQueryParams).toBeCalledTimes(1);
    expect(mockEffectProps.tableInstance.gotoPage).toBeCalledTimes(0);
  });

  // clearRange
  it('expect `clearRange` to be called', () => {
    clearRange(mockCbProps)();
    expect(mockCbProps.setRange).toBeCalledTimes(1);
    expect(mockCbProps.setRange).toBeCalledWith(emptyDateRange);
  });

  // handleReset
  it('expect `handleReset` to be called', () => {
    handleReset(mockCbProps)();

    expect(mockCbProps.setRange).toBeCalledTimes(1);
    expect(mockCbProps.setSearchValue).toBeCalledTimes(1);
    expect(mockCbProps.tableInstance.setGlobalFilter).toBeCalledTimes(1);
    expect(mockCbProps.tableInstance.gotoPage).toBeCalledTimes(1);
    expect(mockCbProps.setQueryParams).toBeCalledTimes(1);

    expect(mockCbProps.setRange).toBeCalledWith(defaultTimeRange);
    expect(mockCbProps.setSearchValue).toBeCalledWith('');
    expect(mockCbProps.tableInstance.setGlobalFilter).toBeCalledWith(undefined);
    expect(mockCbProps.tableInstance.gotoPage).toBeCalledWith(0);
    expect(mockCbProps.setQueryParams).toBeCalledWith({
      bucket: '',
      page: '',
      sortBy: [''],
      from: '',
      to: '',
      searchBy: '',
      invoiceStatus: ''
    });
  });

  // handleViewResults
  it('expect `handleViewResults` to be called with undefined values', () => {
    mockCbProps.range = emptyDateRange;
    mockCbProps.searchValue = '';
    handleViewResults(mockCbProps)();

    expect(mockCbProps.tableInstance.setGlobalFilter).toBeCalledTimes(1);
    expect(mockCbProps.tableInstance.gotoPage).toBeCalledTimes(1);
    expect(mockCbProps.setQueryParams).toBeCalledTimes(1);

    expect(mockCbProps.tableInstance.setGlobalFilter).toBeCalledWith('');
    expect(mockCbProps.tableInstance.gotoPage).toBeCalledWith(0);
    expect(mockCbProps.setQueryParams).toBeCalledWith({
      ...mockCbProps.queryParams,
      from: '',
      to: '',
      searchBy: '',
      invoiceStatus: 'All'
    });
  });
  it('expect `handleViewResults` to be called with values', () => {
    mockCbProps.range = {
      from: new Date('01/01/2021'),
      to: new Date('12/31/2021')
    };
    mockCbProps.searchValue = 'search';
    handleViewResults(mockCbProps)();

    expect(mockCbProps.tableInstance.setGlobalFilter).toBeCalledTimes(1);
    expect(mockCbProps.tableInstance.gotoPage).toBeCalledTimes(1);
    expect(mockCbProps.setQueryParams).toBeCalledTimes(1);

    expect(mockCbProps.tableInstance.setGlobalFilter).toBeCalledWith(
      mockCbProps.searchValue
    );
    expect(mockCbProps.tableInstance.gotoPage).toBeCalledWith(0);
    expect(mockCbProps.setQueryParams).toBeCalledWith({
      ...mockCbProps.queryParams,
      from: '01/01/2021',
      to: '12/31/2021',
      searchBy: mockCbProps.searchValue,
      invoiceStatus: 'All'
    });
  });

  // handleBucketChange
  it('expect `handleBucketChange` to be called with values', () => {
    const bucket: Age = 'Future';
    handleBucketChange(mockCbProps)(bucket);

    expect(mockCbProps.tableInstance.gotoPage).toBeCalledTimes(1);
    expect(mockCbProps.setQueryParams).toBeCalledTimes(1);

    expect(mockCbProps.tableInstance.gotoPage).toBeCalledWith(0);
    expect(mockCbProps.setQueryParams).toBeCalledWith({
      ...mockCbProps.queryParams,
      bucket,
      from: '',
      to: '',
      invoiceStatus: ''
    });
  });
});
