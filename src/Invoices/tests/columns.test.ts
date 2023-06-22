import { camelCase } from 'lodash-es';
import {
  ageCell,
  currencyCell,
  eclipseColumns,
  invoiceNumberCell,
  InvoicesCellProps,
  invoicesColumnsMemo,
  MAX_ROW_COUNT,
  mincronColumns,
  pdfIconCell,
  selectionCell,
  statusCell,
  sortInvoiceCurrency,
  invoiceNumberCellForMincron
} from 'Invoices/util';
import { render } from 'test-utils/TestWrapper';
import { format } from 'utils/currency';
import { mockInvoiceRow } from 'Invoices/mocks';

const mockProps = {
  isMincron: false,
  search: '',
  t: jest.fn()
};
// @ts-ignore - The data matches, but I purposely put ts-ignore since I don't want redundant keys
const mockCell = {
  row: {
    id: '1',
    isSelected: true,
    values: mockInvoiceRow,
    getToggleRowSelectedProps: jest.fn()
  },
  selectedFlatRows: []
} as InvoicesCellProps;
const mockT = (t: string) => t;

describe('Invoices List - Columns', () => {
  // invoicesColumnsMemo
  it('Expect `invoicesColumnsMemo` to return Eclipse columns', () => {
    mockProps.isMincron = false;
    const getColumn = invoicesColumnsMemo(mockProps);
    const [first, second] = getColumn;
    const [expectFirst, expectSecond] = eclipseColumns(mockProps);
    expect(first.id).toBe(expectFirst.id);
    expect(second.accessor).toBe(expectSecond.accessor);
  });
  it('Expect `invoicesColumnsMemo` to return Mincron columns', () => {
    mockProps.isMincron = true;
    const getColumn = invoicesColumnsMemo(mockProps);
    const [first, second] = getColumn;
    const [expectFirst, expectSecond] = mincronColumns(mockProps);
    expect(first.accessor).toBe(expectFirst.accessor);
    expect(second.accessor).toBe(expectSecond.accessor);
  });

  // selectionCell
  it('expect `selectionCell` to be NOT disabled', () => {
    mockCell.row.isSelected = true;
    mockCell.selectedFlatRows = [];
    const result = selectionCell(mockCell);
    expect(result?.props.disabled).toBeFalsy();
  });
  it('expect `selectionCell` to be disabled', () => {
    mockCell.row.isSelected = false;
    mockCell.selectedFlatRows = Array(MAX_ROW_COUNT).fill(mockInvoiceRow);
    const result = selectionCell(mockCell);
    expect(result?.props.disabled).toBeTruthy();
  });

  // pdfIconCell
  it('expect `pdfIconCell` to have the right `href` when invoicenumber start with `S`', () => {
    mockCell.value = 'https://google.com';
    mockCell.row.values['invoiceNumber'] = 'S110227973.001';
    const result = pdfIconCell(mockCell);
    expect(result?.props.href).toBe(mockCell.value);
  });
  it('expect `pdfIconCell` to not have the a tag when invoicenumber start without `S`', () => {
    mockCell.row.values['invoiceNumber'] = 'CK110227973.001';
    const result = pdfIconCell(mockCell);
    expect(result?.props.height).toBe(24);
  });
  it('expect `pdfIconCell` to return null when value is empty', () => {
    mockCell.value = '';
    expect(pdfIconCell(mockCell)).toBe(null);
  });

  // invoiceNumberCell
  it('expect `invoiceNumberCell` to return the matching value', () => {
    mockCell.value = 'testMatching';
    const result = invoiceNumberCell('')(mockCell);

    const { container } = render(result.props.children);
    expect(container).toHaveTextContent(mockCell.value);
  });
  it('expect `invoiceNumberCellForMincron` to have the a tag for mincron', () => {
    mockCell.value = '53241231';
    const result = invoiceNumberCellForMincron('')(mockCell);
    const { container } = render(result.props.children);
    expect(container).toHaveTextContent(mockCell.value);
  });

  // statusCell
  it('expect `statusCell` to match snapshot when value is "Open"', () => {
    mockCell.value = 'Open';
    const result = statusCell(mockT)(mockCell);
    const expected = `invoices.${camelCase(mockCell.value)}`;
    expect(result.props.children).toBe(expected);
  });
  it('expect `statusCell` to return the same value when value is NOT "Open"', () => {
    mockCell.value = 'test';
    const result = statusCell(mockT)(mockCell);
    expect(result.props.children).toBe(mockCell.value);
  });

  // currencyCell
  it('expect `currencyCell` to match value', () => {
    mockCell.value = '12.99';
    const result = currencyCell(mockCell);
    expect(result.props.children).toBe(format(parseFloat(mockCell.value)));
  });

  // ageCell
  it('expect `ageCell` to match value when value is "Past Due"', () => {
    mockCell.value = 'Past Due';
    const result = ageCell(mockT)(mockCell);
    const expected = `invoices.${camelCase(mockCell.value)}`;
    expect(result.props.children).toBe(expected);
  });
  it('expect `ageCell` to match value when value is "Current"', () => {
    mockCell.value = 'Current';
    const result = ageCell(mockT)(mockCell);
    const expected = `invoices.${camelCase(mockCell.value)}`;
    expect(result.props.children).toBe(expected);
  });
  it('expect `ageCell` to match value when value is "Deposit"', () => {
    mockCell.value = 'Deposit';
    const result = ageCell(mockT)(mockCell);
    const expected = `invoices.${camelCase(mockCell.value)}`;
    expect(result.props.children).toBe(expected);
  });
  it('expect `ageCell` to match value when value is "Future"', () => {
    mockCell.value = 'Future';
    const result = ageCell(mockT)(mockCell);
    const expected = `invoices.${camelCase(mockCell.value)}`;
    expect(result.props.children).toBe(expected);
  });
  it('expect `ageCell` to match value when value is "Over120"', () => {
    mockCell.value = 'Over120';
    const result = ageCell(mockT)(mockCell);
    const expected = `invoices.${camelCase(mockCell.value)}`;
    expect(result.props.children).toBe(expected);
  });
  it('expect `ageCell` to match the same value when value is "TEST"', () => {
    mockCell.value = 'TEST';
    const result = ageCell(mockT)(mockCell);
    expect(result.props.children).toBe(mockCell.value);
  });
  it('expect `sortInvoiceCurrency` to return 1 when A is bigger than B', () => {
    const id = 'test';
    const rowA = { ...mockInvoiceRow, values: { [id]: '$1,000.00' } };
    const rowB = { ...mockInvoiceRow, values: { [id]: '$1.00' } };
    const result = sortInvoiceCurrency(rowA, rowB, id);
    expect(result).toBe(1);
  });
  it('expect `sortInvoiceCurrency` to return -1 when A is smaller than B', () => {
    const id = 'test';
    const rowA = { ...mockInvoiceRow, values: { [id]: '$1,200.11' } };
    const rowB = { ...mockInvoiceRow, values: { [id]: '$1,123,456.00' } };
    const result = sortInvoiceCurrency(rowA, rowB, id);
    expect(result).toBe(-1);
  });
});
