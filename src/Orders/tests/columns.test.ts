import { Order } from 'generated/graphql';
import { mockOrder } from 'Orders/tests/mocks';
import {
  formatOrderTotal,
  sortOrderCurrency,
  sortOrderDate
} from 'Orders/util/columns';
import { mockRow } from 'test-utils/mockTableInstance';

/**
 * Mocks
 */
const mockedRow = { ...mockRow<Order>(mockOrder, mockOrder, {}) };

/**
 * TEST
 */
describe('Orders - util/columns', () => {
  // 🟢 1 - formatOrderTotal - undefined
  it('expect `formatOrderTotal` to format undefined as $0.00', () => {
    // arrange
    const data: Order = { ...mockOrder };
    // act
    const result = formatOrderTotal(data);
    // assert
    expect(result).toBe('$0.00');
  });

  // 🟢 2 - formatOrderTotal - 10
  it('expect `formatOrderTotal` to format 10 as $10.00', () => {
    // arrange
    const data: Order = { ...mockOrder, orderTotal: 10 };
    // act
    const result = formatOrderTotal(data);
    // assert
    expect(result).toBe('$10.00');
  });

  // 🟢 3 - formatOrderTotal - 12345.67
  it('expect `formatOrderTotal` to format 12345.67 as $12,345.67', () => {
    // arrange
    const data: Order = { ...mockOrder, orderTotal: 12345.67 };
    // act
    const result = formatOrderTotal(data);
    // assert
    expect(result).toBe('$12,345.67');
  });

  // 🟢 4 - sortOrderDate - A is blank
  it('expect `sortOrderDate` to return 1 when A values is blank', () => {
    // arrange
    const id = 'test';
    const rowA = { ...mockedRow, values: { [id]: '' } };
    const rowB = { ...mockedRow, values: { [id]: 'test' } };
    // act
    const result = sortOrderDate(rowA, rowB, id);
    // assert
    expect(result).toBe(1);
  });

  // 🟢 5 - sortOrderDate - B is blank
  it('expect `sortOrderDate` to return -1 when B values is blank', () => {
    // arrange
    const id = 'test';
    const rowA = { ...mockedRow, values: { [id]: 'test' } };
    const rowB = { ...mockedRow, values: { [id]: '' } };
    // act
    const result = sortOrderDate(rowA, rowB, id);
    // assert
    expect(result).toBe(-1);
  });
  // 🟢 6 - sortOrderDate - both are blank
  it('expect `sortOrderDate` to return -1 when both values is blank', () => {
    // arrange
    const id = 'test';
    const rowA = { ...mockedRow, values: { [id]: '' } };
    const rowB = { ...mockedRow, values: { [id]: '' } };
    // act
    const result = sortOrderDate(rowA, rowB, id);
    // assert
    expect(result).toBe(-1);
  });

  // 🟢 7 - sortOrderCurrency - A > B
  it('expect `sortOrderCurrency` to return -1 when A is bigger than B', () => {
    // arrange
    const id = 'test';
    const rowA = { ...mockedRow, values: { [id]: '$1,000.00' } };
    const rowB = { ...mockedRow, values: { [id]: '$1.00' } };
    // act
    const result = sortOrderCurrency(rowA, rowB, id);
    // assert
    expect(result).toBe(-1);
  });

  // 🟢 8 - sortOrderCurrency - A < B
  it('expect `sortOrderCurrency` to return 1 when A is smaller than B', () => {
    // arrange
    const id = 'test';
    const rowA = { ...mockedRow, values: { [id]: '$1,200.11' } };
    const rowB = { ...mockedRow, values: { [id]: '$1,123,456.00' } };
    // act
    const result = sortOrderCurrency(rowA, rowB, id);
    // assert
    expect(result).toBe(1);
  });
});
