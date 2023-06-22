import { ContractContextType } from 'Contract/ContractProvider';
import { ContractProductLineItem } from 'Contract/ProductList';
import {
  AccountQuery,
  ContractDetails,
  ContractProduct,
  GetContractDetailsDocument
} from 'generated/graphql';

export const mockList: ContractProduct[] = [
  {
    id: 'test0',
    brand: 'Test Brand 1',
    name: 'Test Item 1',
    sequenceNumber: '1',
    lineComments: null,
    partNumber: 'TESTPART1',
    mfr: 'TB1-001-TI1',
    thumb:
      'https://images.tradeservice.com/ProductImages/DIR100108/OOATEY_31911_SML.jpg',
    netPrice: 1.99,
    qty: {
      quantityOrdered: 123,
      quantityReleasedToDate: 52,
      quantityShipped: 49,
      __typename: 'ContractProductQty'
    },
    technicalDocuments: [
      { name: 'Test Doc', url: 'https://google.com', __typename: 'TechDoc' },
      { name: 'Testing Procedures', url: '*', __typename: 'TechDoc' },
      { name: "Do's and don'ts", url: '*', __typename: 'TechDoc' },
      { name: 'Warranty', url: '*', __typename: 'TechDoc' },
      { name: 'Terms of Service', url: '*', __typename: 'TechDoc' },
      { name: 'Additional Details', url: '*', __typename: 'TechDoc' },
      { name: null, url: null, __typename: 'TechDoc' },
      { name: null, url: '#', __typename: 'TechDoc' },
      { name: 'No URL', url: null, __typename: 'TechDoc' }
    ],
    techSpecifications: [
      {
        name: 'length',
        value: "10'",
        __typename: 'TechSpec'
      },
      {
        name: 'material',
        value: 'CPVC',
        __typename: 'TechSpec'
      },
      {
        name: 'pressure_rating',
        value: '400 PSI at 73 Deg F',
        __typename: 'TechSpec'
      },
      {
        name: 'temperature_rating',
        value: '180 Deg F',
        __typename: 'TechSpec'
      },
      {
        name: 'Applicable Standard',
        value: 'NSF 14/61',
        __typename: 'TechSpec'
      },
      {
        name: 'Application',
        value: 'Drinking Water System, Hot and Cold Water Distribution',
        __typename: 'TechSpec'
      },
      {
        name: 'Cpvc Pipe',
        value: '1',
        __typename: 'TechSpec'
      },
      {
        name: 'End Connection',
        value: 'Plain x Plain',
        __typename: 'TechSpec'
      },
      {
        name: 'Length',
        value: "10'",
        __typename: 'TechSpec'
      },
      {
        name: 'Material',
        value: 'CPVC',
        __typename: 'TechSpec'
      },
      {
        name: 'Material Specification',
        value: 'ASTM D1784/D2846',
        __typename: 'TechSpec'
      },
      {
        name: 'Nominal Pipe Size',
        value: '3/4"',
        __typename: 'TechSpec'
      },
      {
        name: 'Pipe',
        value: '2',
        __typename: 'TechSpec'
      },
      {
        name: 'Pressure Rating',
        value: '400 PSI at 73 Deg F',
        __typename: 'TechSpec'
      },
      {
        name: 'Schedule',
        value: 'SDR 11',
        __typename: 'TechSpec'
      },
      {
        name: 'Temperature Rating',
        value: '180 Deg F',
        __typename: 'TechSpec'
      },
      {
        name: 'Thickness',
        value: '0.08"',
        __typename: 'TechSpec'
      }
    ],
    displayOnly: false,
    __typename: 'ContractProduct'
  },
  {
    id: 'test1',
    brand: 'Test Brand 1',
    name: 'Test Video Do Not Watch! Anti-carrot, bad for your eyes!',
    sequenceNumber: '2',
    lineComments: null,
    partNumber: null,
    mfr: null,
    thumb:
      'https://images.tradeservice.com/ProductImages/DIR100093/CHRPIP_CTS-12005_SML.jpg',
    netPrice: 2.99,
    qty: {
      quantityOrdered: 1989,
      quantityReleasedToDate: 6,
      quantityShipped: 4,
      __typename: 'ContractProductQty'
    },
    technicalDocuments: [],
    techSpecifications: [],
    displayOnly: null,
    __typename: 'ContractProduct'
  },
  {
    id: 'test2',
    brand: 'Brant Carthel',
    name: 'Why is he a brand?',
    sequenceNumber: '3',
    lineComments: null,
    partNumber: 'IDUNNOLOL',
    mfr: 'BrantBrand001',
    thumb:
      'https://ecomm-prod-product-img-store.s3.amazonaws.com/Moen/S/t933.jpg',
    netPrice: 8.24,
    qty: {
      quantityOrdered: 35648,
      quantityReleasedToDate: 1234,
      quantityShipped: 1234,
      __typename: 'ContractProductQty'
    },
    technicalDocuments: [],
    techSpecifications: [
      {
        name: 'Kind',
        value: 'Develper',
        __typename: 'TechSpec'
      },
      {
        name: 'Education',
        value: 'College Probably',
        __typename: 'TechSpec'
      },
      {
        name: 'Likes',
        value: 'Not sure, maybe King of the Hill?',
        __typename: 'TechSpec'
      }
    ],
    displayOnly: false,
    __typename: 'ContractProduct'
  },
  {
    id: 'comment1',
    brand: null,
    name: 'DEFINITELY A RICKSY TEST',
    sequenceNumber: '4',
    lineComments: null,
    partNumber: null,
    mfr: null,
    thumb: null,
    netPrice: null,
    qty: null,
    technicalDocuments: null,
    techSpecifications: null,
    displayOnly: true,
    __typename: 'ContractProduct'
  },
  {
    id: 'test3',
    brand: 'Mr. Rick J51',
    name: 'Plumbus',
    sequenceNumber: '4',
    lineComments: null,
    partNumber: 'PLB01',
    mfr: '001MRJ51PLB2017',
    thumb: null,
    netPrice: 99.99,
    qty: {
      quantityOrdered: 2489238,
      quantityReleasedToDate: 4763,
      quantityShipped: 420,
      __typename: 'ContractProductQty'
    },
    technicalDocuments: [],
    techSpecifications: null,
    displayOnly: false,
    __typename: 'ContractProduct'
  },
  {
    id: 'test4',
    brand: 'Costco Kirkland B18-Σ',
    name: 'Mr. Meeseeks',
    sequenceNumber: '5',
    lineComments: null,
    partNumber: 'CAN_SORTA_DO_IT_003',
    mfr: 'WHAT-DO-YOU-WANT',
    thumb: 'https://i.ibb.co/K5k6KfN/image.png',
    netPrice: 59.99,
    qty: {
      quantityOrdered: 22346,
      quantityReleasedToDate: 238,
      quantityShipped: 75,
      __typename: 'ContractProductQty'
    },
    technicalDocuments: [],
    techSpecifications: [
      {
        name: 'Task Precision',
        value: '50%',
        __typename: 'TechSpec'
      },
      {
        name: 'Color',
        value: 'Red',
        __typename: 'TechSpec'
      },
      {
        name: 'Hazards',
        value: 'Tobacco comtamination, incompetence',
        __typename: 'TechSpec'
      },
      {
        name: 'Phrases',
        value: '"What do you want?", "Can sorta do it."',
        __typename: 'TechSpec'
      },
      {
        name: 'Drawbacks',
        value: 'Lack of Motivation, Existential Crisis',
        __typename: 'TechSpec'
      }
    ],
    displayOnly: false,
    __typename: 'ContractProduct'
  },
  {
    id: 'comment2',
    brand: 'Rickle In Time',
    name: 'Genuine Time Crystal (Slightly Used)',
    sequenceNumber: '6',
    lineComments: null,
    partNumber: 'C137-TC01',
    mfr: 'DefNotStolen',
    thumb: 'https://i.ibb.co/H7XFf0P/image.png',
    netPrice: 12999.99,
    qty: {
      quantityOrdered: 1,
      quantityReleasedToDate: 0,
      quantityShipped: 0,
      __typename: 'ContractProductQty'
    },
    technicalDocuments: [
      {
        name: 'Theoradical Time Safety',
        url: '*',
        __typename: 'TechDoc'
      },
      {
        name: 'General Relativity Documentations',
        url: '*',
        __typename: 'TechDoc'
      },
      { name: 'Time Police Regulations', url: '*', __typename: 'TechDoc' }
    ],
    techSpecifications: [],
    displayOnly: false,
    __typename: 'ContractProduct'
  },
  {
    id: 'comment3',
    brand: null,
    name: '***** TEST *****',
    sequenceNumber: '7',
    lineComments: null,
    partNumber: null,
    mfr: null,
    thumb: null,
    netPrice: null,
    qty: null,
    technicalDocuments: null,
    techSpecifications: null,
    displayOnly: true,
    __typename: 'ContractProduct'
  },
  {
    id: null,
    brand: null,
    name: null,
    sequenceNumber: '8',
    lineComments: null,
    partNumber: null,
    mfr: null,
    thumb: null,
    netPrice: null,
    qty: null,
    technicalDocuments: null,
    techSpecifications: null,
    displayOnly: true,
    __typename: 'ContractProduct'
  },
  {
    id: 'comment4',
    brand: null,
    name: '***** SECOND COMMENT *****',
    sequenceNumber: '9',
    lineComments: null,
    partNumber: null,
    mfr: null,
    thumb: null,
    netPrice: null,
    qty: null,
    technicalDocuments: null,
    techSpecifications: null,
    displayOnly: true,
    __typename: 'ContractProduct'
  },
  {
    id: 'test5',
    brand: 'Subwy',
    name: '  12"                       sndwich   ',
    sequenceNumber: '10',
    lineComments: null,
    partNumber: 'sndwich',
    mfr: '12sndwich',
    thumb: null,
    netPrice: 8.99,
    qty: {
      quantityOrdered: 1,
      quantityReleasedToDate: 0,
      quantityShipped: 0,
      __typename: 'ContractProductQty'
    },
    technicalDocuments: [],
    techSpecifications: [],
    displayOnly: false,
    __typename: 'ContractProduct'
  }
];

export const mockData: ContractDetails = {
  contractNumber: '5342749',
  jobName: 'Steve Jobs',
  contractDescription: 'Test Contract Name',
  purchaseOrderNumber: '823745398475',
  accountInformation: {
    shipToAddress: {
      address1: '123 Fake St',
      address2: 'Suite Fake',
      address3: 'Fake Address 3',
      city: 'Fakinton',
      state: 'FA',
      zip: '99999',
      county: null,
      country: 'United Fake Republic',
      __typename: 'ContractAddress'
    },
    branch: {
      address1: '999 N Dallas Pkwy',
      address2: 'Suite 999',
      address3: 'Door 502',
      city: 'Dallas',
      state: 'TX',
      zip: '75248',
      county: null,
      country: 'USA',
      __typename: 'ContractAddress'
    },
    __typename: 'AccountInformation'
  },
  contractDates: {
    promisedDate: '01/01/2021',
    contractDate: '07/26/2021',
    firstReleaseDate: '11/27/2021',
    lastReleaseDate: '01/07/2022',
    __typename: 'ContractDates'
  },
  customerInfo: {
    customerNumber: 'TX1234',
    jobNumber: 'JOB1234',
    enteredBy: 'Arthur Guo',
    __typename: 'CustomerInfo'
  },
  contractSummary: {
    subTotal: '21.00',
    taxAmount: '1.73',
    otherCharges: '11.21',
    totalAmount: '33.94',
    firstShipmentDate: '1.00',
    lastShipmentDate: '3.00',
    __typename: 'ContractSummary'
  },
  contractProducts: mockList,
  __typename: 'ContractDetails'
};

export const GetContractDetailsMock = {
  request: {
    query: GetContractDetailsDocument,
    variables: {
      contractNumber: '5342749',
      erpAccountId: '210852'
    }
  },
  result: {
    data: { contract: mockData },
    loading: false
  }
};

export const GetContractDetailsLoadingMock = {
  request: {
    query: GetContractDetailsDocument,
    variables: {
      contractNumber: '5342749',
      erpAccountId: '210852'
    }
  },
  result: {
    data: null,
    loading: true
  }
};

export const accountsMock: AccountQuery = {
  account: [
    {
      branchId: '1045',
      companyName: '****USE 71754****',
      erpAccountId: '200060',
      erpName: 'ECLIPSE',
      __typename: 'ErpAccount'
    },
    {
      branchId: '6001',
      companyName: 'HYATT PIPELINE, LLC',
      erpAccountId: '200060',
      erpName: 'MINCRON',
      __typename: 'ErpAccount'
    }
  ]
};

export const ContractsProviderMocks: ContractContextType = {
  dialogType: undefined,
  qtyInputMap: {},
  search: '',
  goToCart: jest.fn(),
  handleQtyClear: jest.fn(),
  resetDialog: jest.fn(),
  handleSearchInputChange: jest.fn(),
  setModal: jest.fn(),
  setQtyInputMap: jest.fn(),
  setSearch: jest.fn(),
  setIsReviewReady: jest.fn(),
  contractData: {},
  handleReleaseAll: jest.fn(),
  handleReleaseOver10mil: jest.fn(),
  isReviewReady: false
};

export const mockReviewRelease = {
  items: [
    {
      sequence: '1',
      qty: '30',
      product: mockList[0]
    },
    {
      sequence: '2',
      qty: '30',
      product: mockList[1]
    },
    {
      sequence: '3',
      qty: '30',
      product: mockList[2]
    }
  ] as ContractProductLineItem[],
  map: { '2': '20', '3': '1', '4': '4' }
};
