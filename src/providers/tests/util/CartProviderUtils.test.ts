import { mockCartProducts } from 'Cart/tests/mocks';
import {
  mockData as mockContract,
  mockList as mockContractProducts
} from 'Contract/tests/mocks';
import { LineItem, Maybe } from 'generated/graphql';
import {
  findProducts,
  mergeLineItems,
  pullAllContractProducts,
  pullContractProductFromMap
} from 'providers/utils/CartProviderUtils';

/**
 * TEST
 */
describe('provider/util/CartProviderUtils', () => {
  // 🟢 pullContractProductFromMap - no products
  it('Expect empty array from pullContractProductFromMap with no products in contact', () => {
    const result = pullContractProductFromMap({}, {}, false);
    expect(result.length).toBeFalsy();
  });

  // 🟢 pullContractProductFromMap - products
  it('Expect truthy array from pullContractProductFromMap with products in contact', () => {
    const qtyInputMap: Record<string, string> = {
      [mockContractProducts[0].id!]: '2'
    };
    const result = pullContractProductFromMap(mockContract, qtyInputMap, false);
    expect(result.length).toBeTruthy();
  });

  // 🟢 pullContractProductFromMap - products & merge
  it('Expect truthy array from pullContractProductFromMap with products in contact and merge', () => {
    const qtyInputMap: Record<string, string> = {
      [mockContractProducts[0].id!]: '2'
    };
    const result = pullContractProductFromMap(
      mockContract,
      qtyInputMap,
      true,
      []
    );
    expect(result.length).toBeTruthy();
  });

  // 🟢 pullAllContractProducts - no products
  it('Expect falsey array from pullAllContractProducts with no products in contact', () => {
    const result = pullAllContractProducts({});
    expect(result.length).toBeFalsy();
  });

  // 🟢 findProducts - error
  it('Expect errors from findProducts with empty result', () => {
    const consoleBackup = global.console;
    global.console = { ...consoleBackup, error: jest.fn() };
    const result = findProducts({});
    expect(result.length).toBeFalsy();
    expect(global.console.error).toBeCalledWith(
      'Failure to find products in contract!'
    );
    global.console = { ...consoleBackup };
  });

  // 🟢 mergeLineItems - identical
  it('Expect result length from mergeLineItems with identical items', () => {
    const result = mergeLineItems(mockCartProducts, mockCartProducts);
    expect(result.length).toBe(2);
  });

  // 🟢 mergeLineItems - different
  it('Expect result length from mergeLineItems with different items', () => {
    const oldItem: Maybe<LineItem>[] = [
      ...mockCartProducts,
      null,
      { id: 'null' }
    ];
    const newItem: Maybe<LineItem>[] = [...mockCartProducts, { id: 'blank' }];
    const result = mergeLineItems(oldItem, newItem);
    expect(result.length).toBe(4);
  });
});
