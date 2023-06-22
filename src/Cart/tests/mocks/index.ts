import { mockData as mockContract } from 'Contract/tests/mocks';
import {
  Cart,
  ErpSystemEnum,
  LineItem,
  Maybe,
  PaymentMethodTypeEnum
} from 'generated/graphql';
import { CartContextType, CartContractType } from 'providers/CartProvider';

export const mockCartProducts: LineItem[] = [
  {
    id: 'a1982c9c-fd1b-4c91-8f27-22a65083272c',
    cartId: null,
    customerPartNumber: null,
    pricePerUnit: 43,
    priceLastUpdatedAt: null,
    erpPartNumber: '33860',
    quantity: 1,
    qtyAvailableLastUpdatedAt: null,
    qtyAvailable: 215,
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
    },
    __typename: 'LineItem'
  },
  {
    id: null,
    cartId: null,
    customerPartNumber: null,
    pricePerUnit: 43,
    priceLastUpdatedAt: null,
    erpPartNumber: '33860',
    quantity: 1,
    qtyAvailableLastUpdatedAt: null,
    qtyAvailable: 215,
    product: {
      id: '',
      partNumber: null,
      name: null,
      manufacturerName: null,
      manufacturerNumber: null,
      price: 0,
      stock: null,
      taxonomy: null,
      categories: null,
      technicalDocuments: null,
      environmentalOptions: [],
      upc: '68576823241',
      unspsc: '40183104',
      seriesModelFigureNumber: null,
      productOverview: '',
      featuresAndBenefits: '',
      techSpecifications: null,
      imageUrls: {
        __typename: 'ImageUrls'
      },
      packageDimensions: null,
      minIncrementQty: 0,
      erp: 'ECLIPSE',
      __typename: 'Product'
    },
    __typename: 'LineItem'
  },
  {
    id: null,
    cartId: null,
    customerPartNumber: null,
    pricePerUnit: null,
    priceLastUpdatedAt: null,
    erpPartNumber: '33860',
    quantity: null,
    qtyAvailableLastUpdatedAt: null,
    qtyAvailable: 215,
    product: {
      id: '',
      partNumber: null,
      name: null,
      manufacturerName: null,
      manufacturerNumber: null,
      price: 0,
      stock: null,
      taxonomy: null,
      categories: null,
      technicalDocuments: null,
      environmentalOptions: [],
      upc: '68576823241',
      unspsc: '40183104',
      seriesModelFigureNumber: null,
      productOverview: '',
      featuresAndBenefits: '',
      techSpecifications: null,
      imageUrls: {
        __typename: 'ImageUrls'
      },
      packageDimensions: null,
      minIncrementQty: 1,
      erp: 'ECLIPSE',
      __typename: 'Product'
    },
    __typename: 'LineItem'
  }
];

export const mockCart: Cart = {
  id: 'cbce873c-af2b-49ca-8ca4-d4e839ace0a8',
  ownerId: '71b573d3-cf21-4479-8752-f08ff4f29ee5',
  approverId: null,
  shipToId: '34f70cd1-0bc2-4cbe-b63d-8753b93aba34',
  poNumber: null,
  pricingBranchId: null,
  shippingBranchId: '1003',
  paymentMethodType: PaymentMethodTypeEnum.Billtoaccount,
  creditCard: null,
  approvalState: '61ff7867-19ef-47bb-8bf4-4ec198199ffd',
  rejectionReason: null,
  removedProducts: [],
  subtotal: 43,
  tax: 0,
  shippingHandling: 0,
  total: 0,
  deliveryMethod: null,
  delivery: {
    id: 'e8737e1e-7dbd-4a6d-bfcd-d7a80d4bc675',
    shipToId: null,
    address: {
      id: '4f181aac-c99e-4538-ad63-0b023ed45624',
      companyName: 'HORIZON PLUMBING LTD SHOP',
      street1: '2706 W PIONEER PKWY',
      street2: '',
      city: 'ARLINGTON',
      state: 'TX',
      zip: '76013-5906',
      country: 'USA',
      custom: false,
      __typename: 'Address'
    },
    deliveryInstructions: null,
    preferredDate: null,
    preferredTime: null,
    shouldShipFullOrder: false,
    phoneNumber: null,
    __typename: 'Delivery'
  },
  willCall: {
    id: '9c741f32-6db1-4b47-971e-dd45acf6aa3e',
    preferredDate: null,
    preferredTime: null,
    branchId: '1003',
    pickupInstructions: null,
    __typename: 'WillCall'
  },
  erpSystemName: ErpSystemEnum.Eclipse,
  products: mockCartProducts,
  __typename: 'Cart'
};

export const mockCartContext: CartContextType = {
  isWillCall: false,
  addItemToCart: jest.fn(),
  addItemsToCart: jest.fn(),
  addAllListItemsToCart: jest.fn(),
  cart: undefined,
  cartLoading: false,
  checkingOutWithQuote: false,
  clearContract: jest.fn(),
  contractBranch: undefined,
  clearQuote: jest.fn(),
  deleteCartItems: jest.fn(),
  deleteItem: jest.fn(),
  disableAddToCart: false,
  getUserCart: jest.fn(),
  itemAdded: false,
  itemCount: 0,
  itemLoading: undefined,
  lineNotes: {},
  releaseContractToCart: jest.fn(),
  setContract: jest.fn(),
  setItemAdded: jest.fn(),
  setLineNotes: jest.fn(),
  setPreviousCart: jest.fn(),
  setQuoteId: jest.fn(),
  setQuoteData: jest.fn(),
  setQuoteShipToId: jest.fn(),
  setSelectedBranch: jest.fn(),
  updateCart: undefined,
  updateDelivery: undefined,
  updateCartFromQuote: jest.fn(),
  updateItemQuantity: jest.fn(),
  updateWillCall: undefined,
  updateWillCallBranch: undefined,
  deleteCreditCardFromCart: jest.fn()
};

export const mockCartContract: CartContractType = {
  data: mockContract,
  id: mockContract.contractNumber!,
  shipToId: ''
};

export const mockCartMincronProduct: Maybe<LineItem>[] = [
  {
    id: null,
    cartId: null,
    customerPartNumber: null,
    pricePerUnit: 43,
    priceLastUpdatedAt: null,
    erpPartNumber: '33860',
    quantity: 1,
    qtyAvailableLastUpdatedAt: null,
    qtyAvailable: 215,
    product: {
      id: '',
      partNumber: null,
      name: null,
      manufacturerName: null,
      manufacturerNumber: null,
      price: 0,
      stock: null,
      taxonomy: null,
      categories: null,
      technicalDocuments: null,
      environmentalOptions: [],
      upc: '68576823241',
      unspsc: '40183104',
      seriesModelFigureNumber: null,
      productOverview: '',
      featuresAndBenefits: '',
      techSpecifications: null,
      imageUrls: {
        __typename: 'ImageUrls'
      },
      packageDimensions: null,
      minIncrementQty: 0,
      erp: 'MINCRON',
      __typename: 'Product'
    },
    __typename: 'LineItem'
  }
];
