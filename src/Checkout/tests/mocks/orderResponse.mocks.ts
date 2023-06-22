import { OrderLineItem, OrderResponse } from 'generated/graphql';

const nextYear = new Date().getFullYear() + 1;

export const mockOrderResponse: OrderResponse = {
  __typename: 'OrderResponse',
  amount: '5',
  billToName: 'Test Bill To',
  branchInfo: {
    __typename: 'BranchOrderInfo',
    branchName: 'Test branch name',
    city: 'Katy',
    country: 'USA',
    postalCode: '77494',
    state: 'Texas',
    streetLineOne: '1234 Grand Harbor Dr',
    streetLineTwo: 'Apt 234',
    streetLineThree: 'Room 1234'
  },
  creditCard: {
    __typename: 'CreditCard',
    cardHolder: 'John Appleseed',
    creditCardNumber: '1234 5678 9012 3456',
    creditCardType: 'Visa',
    elementPaymentAccountId: 'cc-12345',
    expirationDate: {
      __typename: 'DateWrapper',
      date: `01/01/${nextYear}`
    },
    postalCode: '77494',
    streetAddress: '1234 Grand Harbor Dr'
  },
  customerPO: 'PO 123456',
  deliveryMethod: 'Our Truck',
  email: 'steven.career@apul.com',
  invoiceNumber: 'INV 123456',
  lineItems: [],
  orderDate: '01/01/2022',
  orderNumber: 'O 123456',
  orderStatus: 'Invoiced',
  shipAddress: {
    city: 'Katy',
    country: 'USA',
    postalCode: '77494',
    state: 'Texas',
    streetLineOne: '1234 Grand Harbor Dr',
    streetLineTwo: 'Apt 234',
    streetLineThree: 'Room 1234'
  },
  shipDate: '06/06/2022',
  shipToName: 'sheep ship',
  specialInstructions: 'do a flip',
  subTotal: 3.0,
  tax: 1.0,
  webStatus: 'ok'
};

export const lineItemsMock: OrderLineItem[] = [
  {
    imageUrls: {
      thumb:
        'http://images.tradeservice.com/ProductImages/DIR100103/CHRPIP_PVC-401_4X4X3_SML.jpg',
      __typename: 'ImageUrls'
    },
    manufacturerName: 'Generic',
    manufacturerNumber: 'PVC-401_2X1-1/2X2',
    erpPartNumber: '30888',
    orderQuantity: 2,
    backOrderedQuantity: 0,
    productId: '3613984',
    productName: '2" x 1-1/2" x 2" PVC Sanitary Reducing Tee - Hub',
    productOrderTotal: 3.02,
    shipQuantity: 3,
    status: 'Purge',
    unitPrice: 1.51
  },
  {
    imageUrls: {
      thumb:
        'http://images.tradeservice.com/ProductImages/DIR100103/CHRPIP_PVC-10002_SML.jpg',
      __typename: 'ImageUrls'
    },
    manufacturerName: 'Generic',
    manufacturerNumber: 'PVC-4112_1-1/2X20',
    erpPartNumber: '48362',
    orderQuantity: 80,
    backOrderedQuantity: 0,
    productId: '3709227',
    productName: '1-1/2" X 20\' PVC SCH 40 Foam Core Pipe - Plain End',
    productOrderTotal: 21.6,
    shipQuantity: 80,
    status: 'Review',
    unitPrice: 0.27
  },
  {
    imageUrls: {
      thumb:
        'http://images.tradeservice.com/ProductImages/DIR100103/CHRPIP_PVC-10002_SML.jpg',
      __typename: 'ImageUrls'
    },
    manufacturerName: 'Generic',
    manufacturerNumber: 'PVC-4200_2X20',
    erpPartNumber: '48370',
    orderQuantity: 100,
    backOrderedQuantity: 0,
    productId: '3618055',
    productName: '2" X 20\' PVC SCH 40 Foam Core Pipe - Plain End',
    productOrderTotal: 37,
    shipQuantity: 100,
    status: 'Stock',
    unitPrice: 0.37
  }
];

export const backOrderdLineItemsMock: OrderLineItem[] = [
  {
    imageUrls: {
      thumb:
        'http://images.tradeservice.com/ProductImages/DIR100103/CHRPIP_PVC-401_4X4X3_SML.jpg',
      __typename: 'ImageUrls'
    },
    manufacturerName: 'Generic',
    manufacturerNumber: 'PVC-401_2X1-1/2X2',
    erpPartNumber: '30888',
    orderQuantity: 2,
    backOrderedQuantity: 1,
    productId: '3613984',
    productName: '2" x 1-1/2" x 2" PVC Sanitary Reducing Tee - Hub',
    productOrderTotal: 3.02,
    shipQuantity: 1,
    status: 'Purge',
    unitPrice: 1.51
  },
  {
    imageUrls: {
      thumb:
        'http://images.tradeservice.com/ProductImages/DIR100103/CHRPIP_PVC-10002_SML.jpg',
      __typename: 'ImageUrls'
    },
    manufacturerName: 'Generic',
    manufacturerNumber: 'PVC-4112_1-1/2X20',
    erpPartNumber: '48362',
    orderQuantity: 80,
    backOrderedQuantity: 0,
    productId: '3709227',
    productName: '1-1/2" X 20\' PVC SCH 40 Foam Core Pipe - Plain End',
    productOrderTotal: 21.6,
    shipQuantity: 80,
    status: 'Review',
    unitPrice: 0.27
  },
  {
    imageUrls: {
      thumb:
        'http://images.tradeservice.com/ProductImages/DIR100103/CHRPIP_PVC-10002_SML.jpg',
      __typename: 'ImageUrls'
    },
    manufacturerName: 'Generic',
    manufacturerNumber: 'PVC-4200_2X20',
    erpPartNumber: '48370',
    orderQuantity: 100,
    backOrderedQuantity: 0,
    productId: '3618055',
    productName: '2" X 20\' PVC SCH 40 Foam Core Pipe - Plain End',
    productOrderTotal: 37,
    shipQuantity: 100,
    status: 'Stock',
    unitPrice: 0.37
  }
];
