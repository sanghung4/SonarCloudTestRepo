import { DataMap } from '@reece/global-types';
import { ContractDetails } from 'generated/graphql';
import {
  findProducts,
  validateProducts
} from 'providers/utils/CartProviderUtils';

// Gives total amount of contract when Releasing all
export function valueOfReleasingContract(
  contract?: ContractDetails,
  qtyMap?: DataMap<string>
) {
  const foundProducts = findProducts(contract, qtyMap);
  const validProducts = validateProducts(foundProducts);
  const subtotal = validProducts.reduce(
    (sum, { product, qty }) => sum + qty * (product?.netPrice ?? 0),
    0
  );
  return subtotal;
}
