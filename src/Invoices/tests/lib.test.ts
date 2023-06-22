import { Invoice } from 'generated/graphql';
import { mockInvoiceRow, success } from 'Invoices/mocks';
import { AGES, sortAge, handleDownloadClickedCb } from 'Invoices/util';
import { TableInstance } from 'react-table';
import { mockRow, mockTableInstance } from 'test-utils/mockTableInstance';

describe('Invoices List - Lib', () => {
  // sortAge
  it('expect `sortAge` to return 0', () => {
    const columnId = 'test';
    const rowA = { ...mockInvoiceRow, values: { [columnId]: AGES[0] } };
    const rowB = { ...mockInvoiceRow, values: { [columnId]: AGES[0] } };
    const result = sortAge(rowA, rowB, columnId);
    expect(result).toBe(0);
  });
  it('expect `sortAge` to return 1', () => {
    const columnId = 'test';
    const rowA = { ...mockInvoiceRow, values: { [columnId]: AGES[1] } };
    const rowB = { ...mockInvoiceRow, values: { [columnId]: AGES[0] } };
    const result = sortAge(rowA, rowB, columnId);
    expect(result).toBe(1);
  });
  it('expect `sortAge` to return -1', () => {
    const columnId = 'test';
    const rowA = { ...mockInvoiceRow, values: { [columnId]: AGES[0] } };
    const rowB = { ...mockInvoiceRow, values: { [columnId]: AGES[1] } };
    const result = sortAge(rowA, rowB, columnId);
    expect(result).toBe(-1);
  });

  // handleDownloadClickedCb
  it('expect `handleDownloadClickedCb` is called', () => {
    const getInvoicesUrl = jest.fn();
    const [firstInvoiceMock] = success.result.data.invoices.invoices;
    const invoiceNumber = '12345';
    // @ts-ignore
    const mockTable = mockTableInstance as TableInstance<unknown>;
    const inputs = {
      erpAccountId: 'test',
      getInvoicesUrl,
      tableInstance: mockTable as TableInstance<Invoice>
    };
    inputs.tableInstance.selectedFlatRows = [
      // @ts-ignore
      {
        ...mockRow,
        original: { ...firstInvoiceMock, invoiceNumber } as Invoice
      }
    ];

    handleDownloadClickedCb(inputs);
    expect(getInvoicesUrl).toBeCalledTimes(1);
    expect(getInvoicesUrl).toBeCalledWith({
      variables: {
        accountId: inputs.erpAccountId,
        invoiceNumbers: [invoiceNumber]
      }
    });
  });
  it('expect `handleDownloadClickedCb` is called with no erpAccountId', () => {
    const getInvoicesUrl = jest.fn();
    const [firstInvoiceMock] = success.result.data.invoices.invoices;
    const invoiceNumber = '12345';
    // @ts-ignore
    const mockTable = mockTableInstance as TableInstance<unknown>;
    const inputs = {
      erpAccountId: undefined,
      getInvoicesUrl,
      tableInstance: mockTable as TableInstance<Invoice>
    };
    inputs.tableInstance.selectedFlatRows = [
      // @ts-ignore
      {
        ...mockRow,
        original: { ...firstInvoiceMock, invoiceNumber } as Invoice
      }
    ];

    handleDownloadClickedCb(inputs);
    expect(getInvoicesUrl).toBeCalledTimes(1);
    expect(getInvoicesUrl).toBeCalledWith({
      variables: {
        accountId: '',
        invoiceNumbers: [invoiceNumber]
      }
    });
  });
});
