import { Order, OrdersQuery } from 'generated/graphql';

export const mockOrder: Order = {
  orderNumber: '1',
  orderStatus: 'I',
  shipDate: '12/02/2021',
  customerPO: 'BRECKENRUIDGE',
  orderDate: '12/01/2021',
  invoiceNumber: '10000000',
  webStatus: 'SUBMITTED',
  shipAddress: {
    __typename: 'OrderAddress',
    streetLineOne: '12345 Plano Rd',
    streetLineTwo: 'Suite 100',
    streetLineThree: 'Office 3',
    city: 'Dallas',
    state: 'TX',
    postalCode: '75243',
    country: 'USA'
  },
  amount: '0',
  __typename: 'Order'
};

export const ORDERS_SUCCESS: OrdersQuery = {
  orders: {
    orders: [
      {
        orderNumber: '1',
        orderStatus: 'I',
        shipDate: '12/02/2021',
        customerPO: 'BRECKENRUIDGE',
        orderDate: '12/01/2021',
        invoiceNumber: null,
        webStatus: 'SUBMITTED',
        shipAddress: null,
        amount: null,
        __typename: 'Order'
      },
      {
        orderNumber: '2',
        orderStatus: 'I',
        shipDate: '12/02/2021',
        customerPO: 'BRECKENRUIDGE',
        orderDate: '12/01/2021',
        invoiceNumber: '1',
        webStatus: 'SUBMITTED',
        shipAddress: null,
        amount: null,
        __typename: 'Order'
      },
      {
        orderNumber: '2',
        orderStatus: 'I',
        shipDate: '12/02/2021',
        customerPO: 'BRECKENRUIDGE',
        orderDate: '12/01/2021',
        invoiceNumber: '2',
        webStatus: 'SUBMITTED',
        shipAddress: null,
        amount: null,
        __typename: 'Order'
      },
      {
        orderNumber: '2',
        orderStatus: 'I',
        shipDate: '12/02/2021',
        customerPO: 'BRECKENRUIDGE',
        orderDate: '12/01/2021',
        invoiceNumber: null,
        webStatus: 'SUBMITTED',
        shipAddress: null,
        amount: null,
        __typename: 'Order'
      },
      {
        orderNumber: '4',
        orderStatus: 'I',
        shipDate: '12/02/2021',
        customerPO: 'BRECKENRUIDGE',
        orderDate: '12/01/2021',
        invoiceNumber: null,
        webStatus: null,
        shipAddress: null,
        amount: null,
        __typename: 'Order'
      },
      {
        orderNumber: '5',
        orderStatus: 'I',
        shipDate: '12/02/2021',
        customerPO: 'BRECKENRUIDGE',
        orderDate: '12/01/2021',
        invoiceNumber: null,
        webStatus: 'SUBMITTED',
        shipAddress: null,
        amount: null,
        __typename: 'Order'
      },
      {
        orderNumber: '6',
        orderStatus: 'I',
        shipDate: '12/02/2021',
        customerPO: 'BRECKENRUIDGE',
        orderDate: '12/01/2021',
        invoiceNumber: null,
        webStatus: 'SUBMITTED',
        shipAddress: null,
        amount: null,
        __typename: 'Order'
      },
      {
        orderNumber: '7',
        orderStatus: 'I',
        shipDate: '12/02/2021',
        customerPO: 'BRECKENRUIDGE',
        orderDate: '12/01/2021',
        invoiceNumber: null,
        webStatus: 'SUBMITTED',
        shipAddress: null,
        amount: null,
        __typename: 'Order'
      },
      {
        orderNumber: '8',
        orderStatus: 'I',
        shipDate: '12/02/2021',
        customerPO: 'BRECKENRUIDGE',
        orderDate: '12/01/2021',
        invoiceNumber: null,
        webStatus: 'SUBMITTED',
        shipAddress: null,
        amount: null,
        __typename: 'Order'
      },
      {
        orderNumber: '9',
        orderStatus: 'I',
        shipDate: '12/02/2021',
        customerPO: 'BRECKENRUIDGE',
        orderDate: '12/01/2021',
        invoiceNumber: null,
        webStatus: 'SUBMITTED',
        shipAddress: null,
        amount: null,
        __typename: 'Order'
      },
      {
        orderNumber: '10',
        orderStatus: 'I',
        shipDate: '12/02/2021',
        customerPO: 'BRECKENRUIDGE',
        orderDate: '12/01/2021',
        invoiceNumber: null,
        webStatus: 'SUBMITTED',
        shipAddress: null,
        amount: null,
        __typename: 'Order'
      },
      {
        orderNumber: '',
        orderStatus: '',
        shipDate: '',
        customerPO: '',
        orderDate: '',
        invoiceNumber: null,
        webStatus: '',
        shipAddress: null,
        amount: null,
        __typename: 'Order'
      }
    ],
    pagination: {
      totalItemCount: 12
    }
  }
};
