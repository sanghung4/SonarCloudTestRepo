import { List, ListLineItem } from 'generated/graphql';

export function removeListLineItemGap(list: List) {
  // create a reference to close the `sortOrder` gap
  const reference = list.listLineItems
    // structure to resort
    .map((data, i) => ({ sortOrder: data.sortOrder || 0, index: i }))
    // sort via sortOrder
    .sort((a, b) => a.sortOrder - b.sortOrder)
    // With it sorted and create a new sortOrder to fill numbering gap
    .map((data, i) => ({ sortOrder: i, index: data.index }));

  // Apply the fixed sort order from `reference`
  const lineItems = list.listLineItems.map((item, index) => {
    const refOrder = reference.find((i) => i.index === index);
    // istanbul ignore next
    const sortOrder = refOrder?.sortOrder ?? item.sortOrder;
    return { ...item, sortOrder } as ListLineItem;
  });

  return { ...list, listLineItems: lineItems } as List;
}
