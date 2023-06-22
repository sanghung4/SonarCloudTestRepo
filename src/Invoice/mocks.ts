import { GetInvoiceDocument } from 'generated/graphql';

export const successMincron = {
  request: {
    query: GetInvoiceDocument,
    variables: {
      accountId: '123456',
      invoiceNumber: '5460026'
    }
  },
  result: {
    data: {
      invoice: {
        invoiceNumber: '5460026',
        status: 'Open',
        terms: 'NET 30 DAYS',
        customerPo: '40251/13325',
        invoiceDate: '10/28/2020',
        dueDate: '10/28/2020',
        originalAmt: '',
        openBalance: '',
        age: '',
        jobNumber: 'AVLNEP7',
        jobName: 'NEP 7 ASHEVILLE',
        address: {
          streetLineOne: '911 BLUEBERRY WAY',
          streetLineTwo: '',
          streetLineThree: '',
          city: 'NORTHLAKE',
          state: 'TX',
          postalCode: '76226',
          country: ''
        },
        shipDate: '11/06/2020',
        deliveryMethod: 'Our Truck',
        subtotal: '3184.28',
        tax: '262.76',
        otherCharges: '0.0',
        paidToDate: '1000.00',
        invoiceItems: [
          {
            id: '1738433F4-91A7-4637-8C60-B4DF59A060C1',
            brand: 'Advanced Drainage Systems, Inc.',
            name: '120 x 7.5, 100 Sq Yd, Geosynthetic, Double, Photodegradable, Erosion Control Blanket',
            partNumber: '8X1125STRAWDN',
            mfr: '00S2TT',
            thumb: 'null',
            price: '43.59',
            qty: {
              quantityOrdered: 1,
              quantityShipped: 1
            }
          }
        ]
      }
    }
  }
};

export const successMincron_5448540 = {
  request: {
    query: GetInvoiceDocument,
    variables: {
      accountId: '91492',
      invoiceNumber: '5448540'
    }
  },
  result: {
    data: {
      invoice: {
        invoiceNumber: '5282411',
        status: 'Open',
        terms: 'NET 30 DAYS',
        customerPo: 'WILKESBORO JIM',
        invoiceDate: '10/06/2021',
        dueDate: '10/28/2020',
        originalAmt: '',
        openBalance: '',
        age: '',
        jobNumber: 'SHOP',
        jobName: 'SHOP',
        address: {
          streetLineOne: '911 BLUEBERRY WAY',
          streetLineTwo: '',
          streetLineThree: '',
          city: 'NORTHLAKE',
          state: 'TX',
          postalCode: '76226',
          country: ''
        },
        shipDate: '11/06/2021',
        deliveryMethod: 'Our Truck',
        subtotal: '2184.28',
        tax: '162.73',
        otherCharges: '0.0',
        paidToDate: '139.91',
        invoiceItems: {
          id: 'F02FD85F-4DCE-4EA7-8459-2DA7CFD689DC',
          brand: 'Certex USA, name:2 x 12, Nylon, Eye and Eye, Ply Sling',
          partNumber: 'SLING212,mfr:CX07-0126-12',
          thumb:
            'https://images.tradeservice.com/ProductImages/DIR100193/CERTEX_CX07-0126-12_LINE_SML.jpg',
          price: '40.00',
          qty: {
            quantityOrdered: 1,
            quantityShipped: 1
          }
        }
      }
    }
  }
};
