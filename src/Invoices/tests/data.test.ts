import { mockTestTableMemo, success } from 'Invoices/mocks';
import {
  onInvoiceDataError,
  openInvoiceUrl,
  tableDataMemo,
  TableDataMemoProps
} from 'Invoices/util';

const mockTableDM = {
  appliedRange: {
    from: undefined,
    to: undefined
  },
  bucket: 'Total',
  invoiceStatus: 'All',
  data: undefined
} as TableDataMemoProps;

describe('Invoices List - Data', () => {
  // tableDataMemo
  it('Expect `tableDataMemo` to return blank when data is undefined', () => {
    mockTableDM.data = undefined;
    const result = tableDataMemo(mockTableDM);
    expect(result.length).toBe(0);
  });
  it('Expect `tableDataMemo` to return all rows with data when from/to are undefined', () => {
    const mockData = success.result.data;
    mockTableDM.data = mockData;
    const result = tableDataMemo(mockTableDM);
    expect(result.length).toBe(6);
  });
  it('Expect `tableDataMemo` to return X rows of data', () => {
    mockTableDM.appliedRange.from = new Date('01/01/2021');
    mockTableDM.appliedRange.to = new Date('12/31/2021');
    mockTableDM.data = mockTestTableMemo;
    const result = tableDataMemo(mockTableDM);
    expect(result.length).toBe(5);
  });
  it('Expect `tableDataMemo` to return all rows of data when from is undefined', () => {
    mockTableDM.appliedRange.from = undefined;
    mockTableDM.appliedRange.to = new Date('12/31/2021');
    mockTableDM.data = mockTestTableMemo;
    const result = tableDataMemo(mockTableDM);
    expect(result.length).toBe(5);
  });
  it('Expect `tableDataMemo` to return all rows of data when to is undefined', () => {
    mockTableDM.appliedRange.from = new Date('01/01/2021');
    mockTableDM.appliedRange.to = undefined;
    mockTableDM.data = mockTestTableMemo;
    const result = tableDataMemo(mockTableDM);
    expect(result.length).toBe(6);
  });
  it('Expect `tableDataMemo` to return 1 row of data when bucket is set as "Future"', () => {
    mockTableDM.appliedRange.from = undefined;
    mockTableDM.appliedRange.to = undefined;
    mockTableDM.bucket = 'Future';
    mockTableDM.data = mockTestTableMemo;
    const result = tableDataMemo(mockTableDM);
    expect(result.length).toBe(1);
  });

  // openInvoiceUrl
  it('Expect `openInvoiceUrl` to open', () => {
    const open = jest.spyOn(window, 'open');
    openInvoiceUrl({ invoicesUrl: 'https://google.com' });
    expect(open).toBeCalledTimes(1);
  });
  it('Expect `openInvoiceUrl` to NOT open when undefined is passed over', () => {
    const open = jest.spyOn(window, 'open');
    openInvoiceUrl();
    expect(open).toBeCalledTimes(0);
  });

  // onInvoiceDataError
  it('Expect `onInvoiceDataError` to call once', () => {
    const mockFn = jest.fn();
    onInvoiceDataError(mockFn, (t: string) => t)();
    expect(mockFn).toBeCalledTimes(1);
    expect(mockFn).toBeCalledWith('invoices.invoiceError', {
      variant: 'error'
    });
  });
});
