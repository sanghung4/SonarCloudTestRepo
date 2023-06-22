import { findIndex, intersectionWith, xorWith } from 'lodash-es';
import { t } from 'i18next';

import { Cart, ContractDetails, LineItem, Maybe } from 'generated/graphql';

export function findCartProductIndex(itemId: string, cart?: Cart) {
  const clonedProducts = [...(cart?.products ?? [])];
  const index = findIndex(clonedProducts, (item) => item?.id === itemId);
  return { clonedProducts, index };
}

// Use for Release # of items of contract (NOT all)
export function pullContractProductFromMap(
  contract: ContractDetails,
  qtyMap: Record<string, string>,
  merge: boolean,
  cartLineItems?: Maybe<LineItem>[]
) {
  if (!contract.contractProducts?.length) {
    return [];
  }

  const foundProducts = findProducts(contract, qtyMap);
  const validProducts = validateProducts(foundProducts);
  const output = convertMapOfProductFromContractToCart(validProducts);
  if (!merge || !cartLineItems) {
    return output;
  }

  return mergeLineItems(cartLineItems, output);
}

// Release all items from contract
export function pullAllContractProducts(contract: ContractDetails) {
  if (!contract.contractProducts?.length) {
    return [];
  }

  const foundProducts = findProducts(contract);
  const validProducts = validateProducts(foundProducts);
  const output = convertMapOfProductFromContractToCart(validProducts);

  return output;
}

// Convert type from ContractProduct to LineItem
type FoundProduct = ReturnType<typeof findProducts>[0];
export function convertMapOfProductFromContractToCart(
  products: FoundProduct[]
) {
  return products.map(
    ({ product, qty }) =>
      ({
        customerPartNumber: product.partNumber,
        id: product.sequenceNumber ?? `${product.id ?? ''}-${product.index}`,
        pricePerUnit: product.netPrice,
        product: {
          // Purposely added index to the object to prevent duplicate ID
          id: product.sequenceNumber ?? `${product.id ?? ''}-${product.index}`,
          price: product.netPrice,
          imageUrls: { medium: product.thumb },
          manufacturerName: product.brand,
          manufacturerNumber: product.mfr,
          name: product.name,
          partNumber: product.partNumber
        },
        quantity: qty,
        uom: product.pricingUom?.toLowerCase() || t('product.each')
      } as LineItem)
  );
}

//  Compiles products for release into a single list (merges qtyMap with contractProducts if qtyMap is passed through)
export function findProducts(
  contract?: ContractDetails,
  qtyMap?: Record<string, string>
) {
  if (!contract?.contractProducts?.length) {
    console.error('Failure to find products in contract!');
    return [];
  }
  if (qtyMap) {
    return Object.entries(qtyMap).map(([sequence, value], index) => {
      const foundProduct = contract.contractProducts!.find(
        ({ sequenceNumber }) => sequenceNumber === sequence
      );
      const qty = parseInt(value);
      const product = { ...foundProduct, index }; // Add "index" to prevent duplicate ID
      return { product, qty };
    });
  }
  return contract.contractProducts.map((product, index) => {
    const { quantityReleasedToDate: released, quantityOrdered: contract } =
      product?.qty || {}; // Use for all items qty logic
    const qty = Math.max((contract ?? 0) - (released ?? 0), 0);
    const modifiedProduct = { ...product, index }; // Add "index" to prevent duplicate ID
    return { product: modifiedProduct, qty };
  });
}

export function validateProducts(products: FoundProduct[]) {
  return products.filter(
    ({ product, qty }) => product && !product.displayOnly && qty
  );
}

// Used for adding additional products released to cart
const filterOutNull = (data: Maybe<LineItem>[]) =>
  data.filter((item) => Boolean(item)) as LineItem[];
const compareId = (a: Maybe<LineItem>, b: Maybe<LineItem>) => a?.id === b?.id;

export function mergeLineItems(
  oldLI: Maybe<LineItem>[],
  newLI: Maybe<LineItem>[]
) {
  const oldCleaned = filterOutNull(oldLI);
  const newCleaned = filterOutNull(newLI);

  const alreadyExistingItems = intersectionWith(
    newCleaned,
    oldCleaned,
    compareId
  ) as LineItem[];
  const xorLineItems = xorWith(oldCleaned, newCleaned, compareId);

  const intersectLineItems = alreadyExistingItems.map((item) => {
    const index = findIndex(oldCleaned, (old) => old?.id === item.id);
    // istanbul ignore next
    item.quantity = (oldCleaned[index]?.quantity ?? 0) + (item?.quantity ?? 0);

    return item;
  });

  return [...xorLineItems, ...intersectLineItems];
}
