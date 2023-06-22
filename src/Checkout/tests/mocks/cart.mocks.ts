import { LineItem } from 'generated/graphql';

export const mockLineItems: LineItem[] = [
  {
    id: 'a1982c9c-fd1b-4c91-8f27-22a65083272c',
    cartId: null,
    customerPartNumber: null,
    pricePerUnit: 43,
    priceLastUpdatedAt: null,
    erpPartNumber: '12345',
    quantity: 1,
    qtyAvailableLastUpdatedAt: null,
    qtyAvailable: 215,
    __typename: 'LineItem'
  },
  {
    id: null,
    cartId: null,
    customerPartNumber: null,
    pricePerUnit: 43,
    priceLastUpdatedAt: null,
    erpPartNumber: '67890',
    quantity: 1,
    qtyAvailableLastUpdatedAt: null,
    qtyAvailable: 215,
    __typename: 'LineItem'
  },
  {
    id: null,
    cartId: null,
    customerPartNumber: null,
    pricePerUnit: null,
    priceLastUpdatedAt: null,
    erpPartNumber: '26548',
    quantity: null,
    qtyAvailableLastUpdatedAt: null,
    qtyAvailable: 215,
    __typename: 'LineItem'
  }
];
