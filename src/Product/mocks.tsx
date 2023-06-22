import {
  GetProductDocument,
  GetProductPricingDocument
} from 'generated/graphql';

export const success = {
  request: {
    query: GetProductDocument,
    variables: {
      productInput: { productId: 'MSC-33860', customerNumber: undefined }
    }
  },
  result: {
    data: {
      product: {
        id: 'MSC-33860',
        partNumber: '33860',
        name: '1/2" Wrot Copper Cap',
        manufacturerName: 'Generic',
        manufacturerNumber: 'CCAPD',
        price: 0,
        stock: null,
        taxonomy: null,
        categories: [
          'Pipe & Fittings',
          'Metal Pipe & Fittings',
          'Copper Tube & Fittings'
        ],
        technicalDocuments: [
          {
            name: 'MFR Catalog Page',
            url: 'http://images.tradeservice.com/ATTACHMENTS/DIR100145/MUELERE00056_18_20.pdf',
            __typename: 'TechDoc'
          },
          {
            name: 'Technical Specification',
            url: 'http://images.tradeservice.com/ATTACHMENTS/DIR100240/MUELERE03088_1.pdf',
            __typename: 'TechDoc'
          },
          {
            name: 'MFR Item Data',
            url: 'http://images.tradeservice.com/ATTACHMENTS/DIR100145/MUELERE00601_1.pdf',
            __typename: 'TechDoc'
          }
        ],
        environmentalOptions: [],
        upc: '68576823241',
        unspsc: '40183104',
        seriesModelFigureNumber: null,
        productOverview:
          'Cap Fitting; Type Round Head; Nominal Size 1/2"; End Connection Female Soldered; Material Copper; Process Wrot; Application Ground Potable Water Supply System; Applicable Standard ASME B16.22, ANSI/NSF 61 G; Packaging Quantity 50 per Inner Pack, 1000 per Master Carton',
        featuresAndBenefits:
          'Copper Solder-Joint Fittings for Supply/Pressurized Systems have Been the Leading Brand of Copper Fittings for Over 80 Years; Available in Both Wrot Copper and Cast Bronze; The Acknowledged Experts at Engineering and Manufacturing Precision Solder-Joint Copper Fittings; Quality, Consistency and Reliability have Made the Streamline Brand Trusted and Specified All Around the World',
        techSpecifications: [
          {
            name: 'material',
            value: 'Copper',
            __typename: 'TechSpec'
          },
          {
            name: 'Applicable Standard',
            value: 'ASME B16.22, ANSI/NSF 61 G',
            __typename: 'TechSpec'
          },
          {
            name: 'Application',
            value: 'Ground Potable Water Supply System',
            __typename: 'TechSpec'
          },
          {
            name: 'End Connection',
            value: 'Female Soldered',
            __typename: 'TechSpec'
          },
          {
            name: 'Material',
            value: 'Copper',
            __typename: 'TechSpec'
          },
          {
            name: 'Nominal Size',
            value: '1/2"',
            __typename: 'TechSpec'
          },
          {
            name: 'Process',
            value: 'Wrot',
            __typename: 'TechSpec'
          },
          {
            name: 'Type',
            value: 'Round Head',
            __typename: 'TechSpec'
          }
        ],
        imageUrls: {
          thumb:
            'http://images.tradeservice.com/ProductImages/DIR100103/MUELER_W-07004_SML.jpg',
          small:
            'http://images.tradeservice.com/ProductImages/DIR100103/MUELER_W-07004_SML.jpg',
          medium:
            'http://images.tradeservice.com/ProductImages/DIR100103/MUELER_W-07004_MED.jpg',
          large:
            'http://images.tradeservice.com/ProductImages/DIR100103/MUELER_W-07004_LRG.jpg',
          __typename: 'ImageUrls'
        },
        packageDimensions: null,
        minIncrementQty: 0,
        erp: 'ECLIPSE',
        __typename: 'Product'
      }
    }
  }
};

export const pricing = {
  request: {
    query: GetProductPricingDocument,
    variables: {
      input: {
        customerId: '',
        branchId: 'any',
        productIds: ['33860'],
        includeListData: false
      }
    }
  },
  result: {
    data: {
      productPricing: {
        customerId: 'testAccount',
        branch: 'any',
        products: [
          {
            productId: '33860',
            orderUom: 'ea',
            catalogId: '',
            branchAvailableQty: 2,
            totalAvailableQty: 3,
            listIds: [],
            sellPrice: 3.256
          }
        ]
      }
    }
  }
};
