import { Quote } from 'generated/graphql';

export const mockQuote: Quote = {
  amount: null,
  bidExpireDate: '01/14/2022',
  billToName: 'HORIZON PLUMBING LTD',
  branchInfo: {
    branchName: 'CRRLLTN PLBG',
    city: 'CARROLLTON',
    country: '',
    postalCode: '75007-4652',
    state: 'TX',
    streetLineOne: '2901 TRADE CTR',
    streetLineThree: '',
    streetLineTwo: 'SUITE 150'
  },
  customerPO: '99',
  deliveryMethod: 'Shipped',
  email: 'morscoemployeeuat@morsco.com',
  invoiceDueDate: '02/28/2022',
  invoiceNumber: null,
  lineItems: [
    {
      erpPartNumber: '4148',
      imageUrls: {
        thumb:
          'http://images.tradeservice.com/ProductImages/DIR100090/MATCO_ZMGL9000_SML.jpg'
      },
      manufacturerName: 'Generic',
      manufacturerNumber: 'IG9F',
      orderQuantity: 1,
      productId: 'MSC-4148',
      productName: '3/4" Galvanized Malleable Iron 90 Degree Elbow',
      productOrderTotal: 2.114,
      shipQuantity: 1,
      status: 'Stock',
      unitPrice: 2.114
    }
  ],
  orderDate: '12/15/2021',
  orderNumber: 'S100000075',
  orderStatus: 'B',
  orderTotal: 28.119999,
  orderedBy: 'Eclipse UAT',
  requiredDate: '12/15/2021',
  shipAddress: {},
  shipDate: '01/05/2022',
  shipToId: '11336',
  specialInstructions: null,
  subTotal: 25.99,
  tax: 2.13,
  webStatus: 'SUBMITTED'
};

export const failureMockQuote: Quote = {
  amount: null,
  bidExpireDate: '01/14/2022',
  billToName: 'HORIZON PLUMBING LTD',
  branchInfo: {
    branchName: 'CRRLLTN PLBG',
    city: 'CARROLLTON',
    country: '',
    postalCode: '75007-4652',
    state: 'TX',
    streetLineOne: '2901 TRADE CTR',
    streetLineThree: '',
    streetLineTwo: 'SUITE 150'
  },
  customerPO: '99',
  email: 'morscoemployeeuat@morsco.com',
  invoiceDueDate: '02/28/2022',
  invoiceNumber: null,
  lineItems: [
    {
      erpPartNumber: '4148',
      imageUrls: {
        thumb:
          'http://images.tradeservice.com/ProductImages/DIR100090/MATCO_ZMGL9000_SML.jpg'
      },
      manufacturerName: 'Generic',
      manufacturerNumber: 'IG9F',
      orderQuantity: 1,
      productId: 'MSC-4148',
      productName: '3/4" Galvanized Malleable Iron 90 Degree Elbow',
      productOrderTotal: 2.114,
      shipQuantity: 1,
      status: 'Stock',
      unitPrice: 2.114
    }
  ],
  orderDate: '12/15/2021',
  orderNumber: 'S100000075',
  orderStatus: 'B',
  orderTotal: 28.119999,
  orderedBy: 'Eclipse UAT',
  requiredDate: '12/15/2021',
  shipAddress: {},
  shipDate: '01/05/2022',
  specialInstructions: null,
  subTotal: 25.99,
  tax: 2.13,
  webStatus: 'SUBMITTED'
};
