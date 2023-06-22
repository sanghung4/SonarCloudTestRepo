import {
  GetOrderDocument,
  Order,
  OrderLineItem,
  ProductPricing
} from 'generated/graphql';

export const lineItemMocks: OrderLineItem[] = [
  {
    imageUrls: null,
    manufacturerName: 'Generic',
    manufacturerNumber: 'PVC-300_2IN',
    erpPartNumber: '30614',
    orderQuantity: 9,
    productId: '3613945',
    productName: '2" PVC DWV 90D Elbow',
    productOrderTotal: 7.812,
    shipQuantity: 9,
    status: 'Stock',
    unitPrice: 0.868,
    pricingUom: 'FT'
  },
  {
    imageUrls: null,
    manufacturerName: null,
    manufacturerNumber: null,
    erpPartNumber: null,
    orderQuantity: null,
    productId: null,
    productName: '!!! DELIVERY IMMEDIATELY !!!!!',
    productOrderTotal: 0.821,
    shipQuantity: null,
    status: 'Comment',
    unitPrice: null
  },
  {
    imageUrls: null,
    manufacturerName: null,
    manufacturerNumber: null,
    erpPartNumber: null,
    orderQuantity: null,
    productId: null,
    productName: '!!! DELIVERY IMMEDIATELY !!!!!',
    productOrderTotal: 24.256,
    shipQuantity: null,
    status: 'MiscCharge',
    unitPrice: null
  },
  {
    imageUrls: null,
    manufacturerName: null,
    manufacturerNumber: null,
    erpPartNumber: '30310',
    orderQuantity: 1,
    productId: null,
    productName: 'PVC 2 DWV 1/8 ST BEND (PDWVS4K) Your # 000755',
    productOrderTotal: 0.821,
    shipQuantity: 1,
    status: 'NonStock',
    unitPrice: 0.821
  },
  {
    imageUrls: {
      thumb:
        'http://images.tradeservice.com/ProductImages/DIR100103/CHRPIP_PVC-321_6IN_SML.jpg'
    },
    manufacturerName: 'Generic',
    manufacturerNumber: 'PVC-321_2IN',
    erpPartNumber: '30243',
    orderQuantity: 4,
    productId: '3613886',
    productName: '2" PVC DWV 45D Straight Elbow - SCH 40, Hub',
    productOrderTotal: 3.212,
    shipQuantity: 4,
    status: 'Delete',
    unitPrice: 0.803
  },
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
    productId: '3613984',
    productName: '2" x 1-1/2" x 2" PVC Sanitary Reducing Tee - Hub',
    productOrderTotal: 3.02,
    shipQuantity: 2,
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
    productId: '3618055',
    productName: '2" X 20\' PVC SCH 40 Foam Core Pipe - Plain End',
    productOrderTotal: 37,
    shipQuantity: 100,
    status: 'Stock',
    unitPrice: 0.37
  }
];

export const lineItemMocks2: OrderLineItem[] = [
  {
    imageUrls: null,
    manufacturerName: null,
    manufacturerNumber: null,
    erpPartNumber: '30310',
    orderQuantity: 1,
    productId: null,
    productName: 'PVC 2 DWV 1/8 ST BEND (PDWVS4K) Your # 000755',
    productOrderTotal: 0.821,
    shipQuantity: 1,
    status: 'NonStock',
    unitPrice: 0.821
  },
  {
    imageUrls: {
      thumb:
        'http://images.tradeservice.com/ProductImages/DIR100103/CHRPIP_PVC-321_6IN_SML.jpg'
    },
    manufacturerName: 'Generic',
    manufacturerNumber: 'PVC-321_2IN',
    erpPartNumber: '30243',
    orderQuantity: 4,
    backOrderedQuantity: 0,
    productId: '3613886',
    productName: '2" PVC DWV 45D Straight Elbow - SCH 40, Hub',
    productOrderTotal: 3.212,
    shipQuantity: 4,
    status: 'NonStock',
    unitPrice: 0.803
  },
  {
    imageUrls: {
      thumb:
        'http://images.tradeservice.com/ProductImages/DIR100103/CHRPIP_PVC-321_6IN_SMG.jpg'
    },
    manufacturerName: 'Generic',
    manufacturerNumber: 'PVC-322_2IN',
    erpPartNumber: '30244',
    orderQuantity: 4,
    backOrderedQuantity: 1,
    productId: '3613887',
    productName: '2" PVC DWV 46D Straight Elbow - SCH 40, Hub',
    productOrderTotal: 3.216,
    shipQuantity: 3,
    status: 'NonStock',
    unitPrice: 0.804
  }
];

export const lineItemMocks3: OrderLineItem[] = [
  {
    imageUrls: {
      thumb:
        'http://images.tradeservice.com/ProductImages/DIR100103/CHRPIP_PVC-321_6IN_SML.jpg'
    },
    manufacturerName: null,
    manufacturerNumber: null,
    erpPartNumber: null,
    orderQuantity: null,
    productId: '3613886',
    productName: '2" PVC DWV 45D Straight Elbow - SCH 40, Hub',
    productOrderTotal: 3.212,
    shipQuantity: 4,
    status: 'Stock',
    unitPrice: 0.803
  },
  {
    imageUrls: null,
    manufacturerName: null,
    manufacturerNumber: null,
    erpPartNumber: '30310',
    orderQuantity: 1,
    productId: null,
    productName: 'PVC 2 DWV 1/8 ST BEND (PDWVS4K) Your # 000755',
    productOrderTotal: 0.821,
    shipQuantity: 1,
    status: 'Comment',
    unitPrice: 0.821
  },
  {
    imageUrls: null,
    manufacturerName: null,
    manufacturerNumber: null,
    erpPartNumber: null,
    orderQuantity: null,
    productId: null,
    productName: 'NULL',
    productOrderTotal: null,
    shipQuantity: null,
    status: 'Comment',
    unitPrice: null
  }
];

export const pricingDataMocks: ProductPricing[] = [
  {
    productId: '1234',
    sellPrice: 3.234,
    orderUom: 'ea',
    branchAvailableQty: 1,
    totalAvailableQty: 2,
    catalogId: ''
  },
  {
    productId: '4321',
    sellPrice: 0.68,
    orderUom: 'ea',
    branchAvailableQty: 1,
    totalAvailableQty: 2,
    catalogId: ''
  }
];

export const pricingDataMocks2: ProductPricing[] = [
  {
    productId: '3613886',
    sellPrice: 0.68,
    orderUom: 'ea',
    branchAvailableQty: 1,
    totalAvailableQty: 2,
    catalogId: ''
  },
  {
    productId: '3613945',
    sellPrice: 0.68,
    orderUom: 'ea',
    branchAvailableQty: 1,
    totalAvailableQty: 2,
    catalogId: ''
  }
];

export const lineItemMocks4: OrderLineItem[] = [
  {
    imageUrls: null,
    manufacturerName: null,
    manufacturerNumber: null,
    erpPartNumber: '30310',
    orderQuantity: 1,
    productId: null,
    productName: 'PVC 2 DWV 1/8 ST BEND (PDWVS4K) Your # 000755',
    productOrderTotal: 0.821,
    shipQuantity: 1,
    status: 'NonStock',
    unitPrice: 0.821,
    lineNumber: '1'
  },
  {
    imageUrls: {
      thumb:
        'http://images.tradeservice.com/ProductImages/DIR100103/CHRPIP_PVC-321_6IN_SML.jpg'
    },
    manufacturerName: 'Generic',
    manufacturerNumber: 'PVC-321_2IN',
    erpPartNumber: '30243',
    orderQuantity: 4,
    productId: '3613886',
    productName: '2" PVC DWV 45D Straight Elbow - SCH 40, Hub',
    productOrderTotal: 3.212,
    shipQuantity: 4,
    status: 'NonStock',
    unitPrice: 0.803,
    lineNumber: '2'
  }
];

export const eclipseMocks = [
  {
    request: {
      query: GetOrderDocument,
      variables: {
        accountId: '123456',
        orderId: 'S123456',
        userId: 'testuser',
        invoiceNumber: '001'
      }
    },
    result: {
      data: {
        order: {
          amount: null,
          shipToName: 'HORIZON PLUMBING LTD SHOP',
          customerPO: '484986',
          deliveryMethod: 'OUR_TRUCK',
          invoiceNumber: null,
          lineItems: lineItemMocks,
          orderDate: '08/12/2020',
          orderNumber: 'S109052294',
          orderStatus: 'I',
          orderTotal: 1043.21,
          orderedBy: 'Matt',
          shipAddress: {
            streetLineOne: '911 BLUEBERRY WAY',
            streetLineTwo: '',
            streetLineThree: '',
            city: 'NORTHLAKE',
            state: 'TX',
            postalCode: '76226',
            country: ''
          },
          shipDate: '08/14/2020',
          specialInstructions:
            'DELIVER BY 9AM 8/13\nDAVID T (817)401-8390\n\nTOLL BROTHERS/PECAN SQUARE\n911 BLUEBERRY WAY\nNORTHLAKE, TX  76226',
          subTotal: 963.57,
          tax: 79.64,
          webStatus: 'INVOICED',
          creditCard: null
        }
      }
    }
  },
  {
    request: {
      query: GetOrderDocument,
      variables: {
        accountId: '123456',
        orderId: 'S123456',
        userId: 'testuser',
        invoiceNumber: '001'
      }
    },
    result: {
      data: {
        order: {
          amount: null,
          shipToName: 'HORIZON PLUMBING LTD SHOP',
          customerPO: '484986',
          deliveryMethod: 'OUR_TRUCK',
          invoiceNumber: null,
          lineItems: lineItemMocks2,
          orderDate: '08/12/2020',
          orderNumber: 'S109052294',
          orderStatus: 'I',
          orderTotal: 1043.21,
          orderedBy: 'Matt',
          shipAddress: {
            streetLineOne: '911 BLUEBERRY WAY',
            streetLineTwo: '',
            streetLineThree: '',
            city: 'NORTHLAKE',
            state: 'TX',
            postalCode: '76226',
            country: ''
          },
          shipDate: '08/14/2020',
          specialInstructions:
            'DELIVER BY 9AM 8/13\nDAVID T (817)401-8390\n\nTOLL BROTHERS/PECAN SQUARE\n911 BLUEBERRY WAY\nNORTHLAKE, TX  76226',
          subTotal: 963.57,
          tax: 79.64,
          webStatus: 'INVOICED',
          creditCard: null
        }
      }
    }
  }
];

export const mincronMocks = [
  {
    request: {
      query: GetOrderDocument,
      variables: {
        accountId: '123456',
        orderId: 'S123456',
        userId: 'testuser',
        invoiceNumber: '001',
        orderStatus: 'Invoiced'
      }
    },
    result: {
      data: {
        order: {
          amount: null,
          shipToName: 'HORIZON PLUMBING LTD SHOP',
          customerPO: '484986',
          deliveryMethod: 'OUR_TRUCK',
          invoiceNumber: null,
          lineItems: lineItemMocks,
          orderDate: '08/12/2020',
          orderNumber: 'S109052294',
          orderStatus: 'I',
          orderTotal: 1043.21,
          orderedBy: 'Matt',
          shipAddress: {
            streetLineOne: '911 BLUEBERRY WAY',
            streetLineTwo: '',
            streetLineThree: '',
            city: 'NORTHLAKE',
            state: 'TX',
            postalCode: '76226',
            country: ''
          },
          shipDate: '08/14/2020',
          specialInstructions:
            'DELIVER BY 9AM 8/13\nDAVID T (817)401-8390\n\nTOLL BROTHERS/PECAN SQUARE\n911 BLUEBERRY WAY\nNORTHLAKE, TX  76226',
          subTotal: 963.57,
          tax: 79.64,
          webStatus: 'INVOICED',
          creditCard: null
        }
      }
    }
  },
  {
    request: {
      query: GetOrderDocument,
      variables: {
        accountId: '123456',
        orderId: 'S123456',
        userId: 'testuser',
        invoiceNumber: '001',
        orderStatus: 'Submitted'
      }
    },
    result: {
      data: {
        order: {
          amount: null,
          shipToName: 'HORIZON PLUMBING LTD SHOP',
          customerPO: '484986',
          deliveryMethod: 'OUR_TRUCK',
          invoiceNumber: null,
          lineItems: lineItemMocks2,
          orderDate: '08/12/2020',
          orderNumber: 'S109052294',
          orderStatus: 'I',
          orderTotal: 1043.21,
          orderedBy: 'Matt',
          shipAddress: {
            streetLineOne: '911 BLUEBERRY WAY',
            streetLineTwo: '',
            streetLineThree: '',
            city: 'NORTHLAKE',
            state: 'TX',
            postalCode: '76226',
            country: ''
          },
          shipDate: '08/14/2020',
          specialInstructions:
            'DELIVER BY 9AM 8/13\nDAVID T (817)401-8390\n\nTOLL BROTHERS/PECAN SQUARE\n911 BLUEBERRY WAY\nNORTHLAKE, TX  76226',
          subTotal: 963.57,
          tax: 79.64,
          webStatus: 'SUBMITTED',
          creditCard: null
        }
      }
    }
  },
  {
    request: {
      query: GetOrderDocument,
      variables: {
        accountId: '123456',
        orderId: 'S123456',
        userId: 'testuser',
        invoiceNumber: '001',
        orderStatus: 'Unknown'
      }
    },
    result: {
      data: {
        order: {
          amount: null,
          shipToName: 'HORIZON PLUMBING LTD SHOP',
          customerPO: '484986',
          deliveryMethod: 'OUR_TRUCK',
          invoiceNumber: null,
          lineItems: lineItemMocks2,
          orderDate: '08/12/2020',
          orderNumber: 'S109052294',
          orderStatus: 'I',
          orderTotal: 1043.21,
          orderedBy: 'Matt',
          shipAddress: {
            streetLineOne: '911 BLUEBERRY WAY',
            streetLineTwo: '',
            streetLineThree: '',
            city: 'NORTHLAKE',
            state: 'TX',
            postalCode: '76226',
            country: ''
          },
          shipDate: '08/14/2020',
          specialInstructions:
            'DELIVER BY 9AM 8/13\nDAVID T (817)401-8390\n\nTOLL BROTHERS/PECAN SQUARE\n911 BLUEBERRY WAY\nNORTHLAKE, TX  76226',
          subTotal: 963.57,
          tax: 79.64,
          webStatus: 'UNKNOWN',
          creditCard: null
        }
      }
    }
  }
];

export const orderMocks: Order[] = [
  {
    amount: null,
    shipToName: 'HORIZON PLUMBING LTD SHOP',
    customerPO: '484986',
    deliveryMethod: 'OUR_TRUCK',
    invoiceNumber: null,
    lineItems: lineItemMocks,
    orderDate: '08/12/2020',
    orderNumber: 'S109052294',
    orderStatus: 'I',
    orderTotal: 1043.21,
    orderedBy: 'Matt',
    shipAddress: {
      streetLineOne: '911 BLUEBERRY WAY',
      streetLineTwo: '',
      streetLineThree: '',
      city: 'NORTHLAKE',
      state: 'TX',
      postalCode: '76226',
      country: ''
    },
    shipDate: '08/14/2020',
    specialInstructions:
      'DELIVER BY 9AM 8/13\nDAVID T (817)401-8390\n\nTOLL BROTHERS/PECAN SQUARE\n911 BLUEBERRY WAY\nNORTHLAKE, TX  76226',
    subTotal: 963.57,
    tax: 79.64,
    webStatus: 'UNKNOWN',
    creditCard: null
  },
  {
    amount: null,
    shipToName: 'HORIZON PLUMBING LTD SHOP',
    customerPO: '484986',
    deliveryMethod: 'OUR_TRUCK',
    invoiceNumber: null,
    lineItems: lineItemMocks,
    orderDate: '08/12/2020',
    orderNumber: 'S109052294',
    orderStatus: 'I',
    orderTotal: 1043.21,
    orderedBy: 'Matt',
    shipAddress: {
      streetLineOne: '911 BLUEBERRY WAY',
      streetLineTwo: '',
      streetLineThree: '',
      city: 'NORTHLAKE',
      state: 'TX',
      postalCode: '76226',
      country: ''
    },
    shipDate: '08/14/2020',
    specialInstructions:
      'DELIVER BY 9AM 8/13\nDAVID T (817)401-8390\n\nTOLL BROTHERS/PECAN SQUARE\n911 BLUEBERRY WAY\nNORTHLAKE, TX  76226',
    subTotal: 963.57,
    tax: 79.64,
    webStatus: 'UNKNOWN',
    creditCard: {
      __typename: 'CreditCard',
      cardHolder: 'Farmer MacJoy',
      creditCardNumber: '1234 5678 9012 3456',
      creditCardType: 'Visa',
      elementPaymentAccountId: 'eieio',
      expirationDate: { date: '10/30/28' },
      postalCode: '57785',
      streetAddress: '123 Farm Rd'
    }
  },
  {
    lineItems: [
      {
        status: 'Stock',
        erpPartNumber: '1234'
      },
      {
        status: 'Stock',
        erpPartNumber: '4321'
      }
    ]
  },
  {
    lineItems: [
      {
        status: 'Stock'
      },
      {
        status: 'NonStock'
      }
    ]
  }
];
