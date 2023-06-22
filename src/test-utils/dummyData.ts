import {
  AccountLookUp,
  ApprovedUser,
  EcommAccount,
  ErpSystemEnum,
  ShipToAccount,
  PhoneType,
  Role
} from 'generated/graphql';

export const dummyUserAccounts: EcommAccount[] = [
  {
    id: '12345',
    erpAccountId: '12345',
    name: 'mock userAccounts name 0',
    erpSystemName: ErpSystemEnum.Eclipse,
    shipTos: []
  },
  {
    id: '12345',
    erpAccountId: '67890',
    name: 'mock userAccounts name 1',
    erpSystemName: ErpSystemEnum.Eclipse,
    shipTos: []
  }
];

export const dummyUser: ApprovedUser = {
  id: '12345',
  phoneType: PhoneType.Home,
  email: 'testuser@email.com',
  firstName: 'test',
  lastName: 'user',
  phoneNumber: '1234567890',
  role: { id: '12345', name: 'testrole', description: 'testrole' }
};

export const dummyAccountLookup: AccountLookUp = {
  branchId: '1234',
  companyName: 'company name',
  isBillTo: true,
  erpAccountId: '12345',
  erpName: ErpSystemEnum.Eclipse
};

export const dummyRefreshShipToAccount: ShipToAccount[] = [
  {
    id: '34894cb4-07c6-4c7b-8416-79a28cee75c6',
    name: '850298 - TEST - 1234 ST DALLAS, TX 75032',
    __typename: 'ShipToAccount'
  },
  {
    id: 'd4ba0784-9a6d-45b9-97d1-1039cffbc12d',
    name: '11336 - HORIZON PLUMBING LTD SHOP - 2706 W PIONEER PKWY ARLINGTON, TX 76013-5906',
    __typename: 'ShipToAccount'
  },
  {
    id: '23dc05bb-9f0e-4e38-a3bc-cf701d4e20c0',
    name: '24042 - HORIZON PLUMBING LTD SERVICE - 2706 W PIONEER PARKWAY ARLINGTON, TX 76013-5906',
    __typename: 'ShipToAccount'
  },
  {
    id: '8a6ff69f-9909-4590-b847-7fc4d52eab0a',
    name: '501349 - HORIZON PLUMBING BASIQ IDIQ SERVICE - 2706 W PIONEER PARKWAY ARLINGTON, TX 76013-5906',
    __typename: 'ShipToAccount'
  }
];

export const dummyEcommAccounts: EcommAccount[] = [
  {
    id: 'fff484a6-b4b3-4a98-924e-f17bd833627e',
    name: '197816 - STERNENBERG & KROHN LLC - 5612 RAMPART ST  HOUSTON, TX',
    erpAccountId: '197816',
    erpSystemName: ErpSystemEnum.Eclipse,
    shipTos: [
      {
        id: '7226dab6-5db0-42db-8d82-a8eee8c62603',
        name: '200001 - STERNENBERG & KROHN LLC SHOP  - 5612 RAMPART ST HOUSTON, TX',
        erpAccountId: '200001',
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      }
    ],
    __typename: 'EcommAccount'
  },
  {
    id: '910ddc88-2eee-44ef-a70f-1c0422ce441b',
    name: '740394 - 1 SOURCE MECHANICAL - 518 E. ROUTR 66 WILLIAMS, AZ',
    erpAccountId: '740394',
    erpSystemName: ErpSystemEnum.Eclipse,
    shipTos: [
      {
        id: '6591d08b-0570-4064-97ee-6f6b39891977',
        name: '740394 - 1 SOURCE MECHANICAL - 518 E. ROUTR 66 WILLIAMS, AZ',
        erpAccountId: '740394',
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      }
    ],
    __typename: 'EcommAccount'
  },
  {
    id: 'e95aaed0-4fe3-46dc-a3c2-2fd5f39c149b',
    name: '12323 - DURWOOD GREENE CONSTRUCTION - PO BOX 1338 STAFFORD, TX',
    erpAccountId: '12323',
    erpSystemName: ErpSystemEnum.Eclipse,
    shipTos: [
      {
        id: 'c0cb69ec-7477-41a2-8605-25fb3266d1f0',
        name: '65837 - DURWOOD GREENE CONSTRUCTION SHOP - 10126 CASH RD STAFFORD, TX',
        erpAccountId: '65837',
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      }
    ],
    __typename: 'EcommAccount'
  },
  {
    id: '5af36e55-c064-4387-bdfa-87d85435a193',
    name: '603863 - SPADES HOSPITALITY LLC - 1814 RUSTIC HILLS CT SUGAR LAND, TX',
    erpAccountId: '603863',
    erpSystemName: ErpSystemEnum.Eclipse,
    shipTos: [
      {
        id: null,
        name: null,
        erpAccountId: '603876',
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      },
      {
        id: 'e68f9154-aadd-48ea-94e4-abb5b5fce582',
        name: '603885 - SPADES HOSPITALITY HYATT PL AUSTIN  - 7300 N FM 620 AUSTIN, TX',
        erpAccountId: '603885',
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      }
    ],
    __typename: 'EcommAccount'
  },
  {
    id: '9ce62062-1978-4b30-856a-b1b024406f44',
    name: '200090 - ASSOCIATED PIPELINE INC - 2270 KIT FOX CIRCLE CUMMING, GA',
    erpAccountId: '200090',
    erpSystemName: ErpSystemEnum.Eclipse,
    shipTos: null,
    __typename: 'EcommAccount'
  },
  {
    id: '38c27228-c699-4a15-92d5-93e410aa3ab1',
    name: '210913 - DIANE BALTHARD - 18031 DARMEL PL SANTA ANA, CA',
    erpAccountId: null,
    erpSystemName: ErpSystemEnum.Eclipse,
    shipTos: [
      {
        id: '43023874-45be-4092-975b-c8cd1b69301b',
        name: '210913 - DIANE BALTHARD - 18031 DARMEL PL SANTA ANA, CA',
        erpAccountId: '210913',
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      }
    ],
    __typename: 'EcommAccount'
  },
  {
    id: '7e675d6f-046e-4b34-a749-e7e30c6ec21c',
    name: '515 - NORTH PLAINS ELECTRIC CO-OP - PO BOX 1008 PERRYTON, TX',
    erpAccountId: null,
    erpSystemName: ErpSystemEnum.Eclipse,
    shipTos: [
      {
        id: 'f767bd35-944d-47bc-9020-b6ce9104f2c6',
        name: '5656 - NORTH PLAINS ELECTRIC CO-OP SHOP - 14585 US HWY 83 PERRYTON, TX',
        erpAccountId: '5656',
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      }
    ],
    __typename: 'EcommAccount'
  },
  {
    id: 'e909bc8f-408a-4392-b2bb-1441a94bf8fb',
    name: '217260 - ABBY DEVELOPMENT I LP - 1014 PRUITT PL TYLER, TX',
    erpAccountId: '217260',
    erpSystemName: ErpSystemEnum.Eclipse,
    shipTos: null,
    __typename: 'EcommAccount'
  },
  {
    id: '5b0e34c6-a58c-43d2-b075-3cdef23a6d9e',
    name: '123123 - DAVID KAZAR - 18642 SHADOW CANYON DR HELOTES, TX',
    erpAccountId: '123123',
    erpSystemName: ErpSystemEnum.Eclipse,
    shipTos: [
      {
        id: 'b549d67a-40e4-48b0-af39-a3c5a449fdf6',
        name: '123123 - DAVID KAZAR - 18642 SHADOW CANYON DR HELOTES, TX',
        erpAccountId: '123123',
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      }
    ],
    __typename: 'EcommAccount'
  },
  {
    id: '828bf410-327d-44c9-af04-99746233269b',
    name: '301323 - WEINSTEIN MANAGEMENT CO - dba WEINSTEIN PROPERTIES RICHMOND, VA',
    erpAccountId: '301323',
    erpSystemName: ErpSystemEnum.Eclipse,
    shipTos: null,
    __typename: 'EcommAccount'
  },
  {
    id: '6491720d-84b3-47d3-b1fc-4041210293b3',
    name: '200001 - STERNENBERG & KROHN LLC SHOP  - 5612 RAMPART ST HOUSTON, TX',
    erpAccountId: '200001',
    erpSystemName: ErpSystemEnum.Eclipse,
    shipTos: null,
    __typename: 'EcommAccount'
  },
  {
    id: '26524648-90e7-4aeb-9d0c-25852113097c',
    name: '200008 - TERAMOR HOMES LLC SHOP  - 12919 SOUTHWEST FWY STE 120 STAFFORD, TX',
    erpAccountId: '200008',
    erpSystemName: ErpSystemEnum.Eclipse,
    shipTos: [
      {
        id: '2503fe43-a07a-4cfa-8623-e73ae98f0ce4',
        name: 'SHOP - SHOP - 603 JONQUIL LANE HAYESVILLE, NC - null',
        erpAccountId: 'SHOP',
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      }
    ],
    __typename: 'EcommAccount'
  },
  {
    id: 'ddde48fc-7610-47cb-9093-403d202365b8',
    name: '10470 - ANSON PARK I - 2249 VOGEL ST ABILENE, TX',
    erpAccountId: '10470',
    erpSystemName: ErpSystemEnum.Eclipse,
    shipTos: [
      {
        id: '7185b5af-8ebe-4f38-af6c-76f3fe536c98',
        name: '64481 - ANSON PARK I SHOP - 2249 VOGEL ST ABILENE, TX',
        erpAccountId: '64481',
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      }
    ],
    __typename: 'EcommAccount'
  },
  {
    id: '4b283b58-a90a-4030-88d7-b9309ccff985',
    name: '690442 -  DESIGNS BY DOLORES - 4924 E. APACHE CREEK CAVE CREEK, AZ',
    erpAccountId: '690442',
    erpSystemName: ErpSystemEnum.Eclipse,
    shipTos: [
      {
        id: 'dec424a9-d499-400f-a216-c794291d1b4e',
        name: '690442 -  DESIGNS BY DOLORES - 4924 E. APACHE CREEK CAVE CREEK, AZ',
        erpAccountId: '690442',
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      }
    ],
    __typename: 'EcommAccount'
  },
  {
    id: 'db6769af-bdde-4731-9e42-315ac8d4c663',
    name: '200060 - ****USE 71754**** - 3007 PRADERA ST MISSION, TX',
    erpAccountId: '200060',
    erpSystemName: ErpSystemEnum.Eclipse,
    shipTos: null,
    __typename: 'EcommAccount'
  },
  {
    id: '518d994e-9b16-4679-8243-94e6de3e68fa',
    name: '200012 - A-1 PLUMBING HEATING & SEWER - 250 MONROE DR MCDONOUGH, GA',
    erpAccountId: '200012',
    erpSystemName: ErpSystemEnum.Eclipse,
    shipTos: null,
    __typename: 'EcommAccount'
  },
  {
    id: 'b4bc63e3-811d-47d5-83cc-245d4a9407d9',
    name: '302841 - MTB MECHANICAL, INC. - 1201 INDUSTRIAL DRIVE MATTHEWS, NC',
    erpAccountId: '302841',
    erpSystemName: ErpSystemEnum.Eclipse,
    shipTos: [
      {
        id: '33a8ab95-5732-4efa-9cdb-77124416f41c',
        name: '425275 - MTB MECHANICAL - SHOP - 1201 INDUSTRIAL DR MATTHEWS, NC',
        erpAccountId: '425275',
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      },
      {
        id: 'df9f44e1-ac93-4f55-ad65-8229041d99d5',
        name: '304240 - MTB MECHANICAL, INC.        15893 - SERVICE DEPARTMENT MATTHEWS, NC',
        erpAccountId: '304240',
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      }
    ],
    __typename: 'EcommAccount'
  },
  {
    id: '221d11b9-fc84-4dbf-a2cc-af23d9cff5aa',
    name: '287169 - Ecommerce MORSCO Test Bill-To - 300  E Vickery Blvd FORT WORTH, TX',
    erpAccountId: '287169',
    erpSystemName: ErpSystemEnum.Eclipse,
    shipTos: [
      {
        id: 'ce72bbfc-0fa3-4e2b-9225-45f1682f8280',
        name: '287172 - Ecommerce MORSCO Test Shop Acct - 300 Vickery Blvd FORT WORTH, TX',
        erpAccountId: '287172',
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      },
      {
        id: '890a0f27-6745-4290-ba5b-b2554307e298',
        name: '287176 - Ecommerce MORSCO Test JOB Acct - 311 E VICKERY Blvd FORT WORTH, TX',
        erpAccountId: '287176',
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      },
      {
        id: '74ee3413-a88b-4575-9504-26538c469553',
        name: '325538 - Ecommerce MORSCO Test C.O.D. Acct - 300 Vickery Blvd FORT WORTH, TX',
        erpAccountId: '325538',
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      }
    ],
    __typename: 'EcommAccount'
  },
  {
    id: '507ee1d7-4f6b-4ebd-af79-a06d3dfeefce',
    name: '35648 - HORIZON PLUMBING LTD - 2706 W PIONEER PKWY ARLINGTON, TX',
    erpAccountId: '35648',
    erpSystemName: ErpSystemEnum.Eclipse,
    shipTos: [
      {
        id: 'd4ba0784-9a6d-45b9-97d1-1039cffbc12d',
        name: '11336 - HORIZON PLUMBING LTD SHOP - 2706 W PIONEER PKWY ARLINGTON, TX',
        erpAccountId: '11336',
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      },
      {
        id: '23dc05bb-9f0e-4e38-a3bc-cf701d4e20c0',
        name: '24042 - HORIZON PLUMBING LTD SERVICE - 2706 W PIONEER PARKWAY ARLINGTON, TX',
        erpAccountId: '24042',
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      },
      {
        id: '8a6ff69f-9909-4590-b847-7fc4d52eab0a',
        name: '501349 - HORIZON PLUMBING BASIQ IDIQ SERVICE - 2706 W PIONEER PARKWAY ARLINGTON, TX',
        erpAccountId: '501349',
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      }
    ],
    __typename: 'EcommAccount'
  },
  {
    id: '773be4d8-3fac-4a00-97fe-4301dba0df18',
    name: '10783 - AUDRAIN HEATING & COOLING LLC - 502 W BROADWAY ST FRITCH, TX',
    erpAccountId: '10783',
    erpSystemName: ErpSystemEnum.Eclipse,
    shipTos: [
      {
        id: '928b7843-42da-4f95-822c-ff6eedfb36a9',
        name: '64526 - AUDRAIN HEATING & COOLING LLC SHOP - 502 W BROADWAY ST FRITCH, TX',
        erpAccountId: '64526',
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      }
    ],
    __typename: 'EcommAccount'
  },
  {
    id: '90112de2-185c-4c00-9b61-66edf2d5f170',
    name: '22214 - THRASH CONSTRUCTION SERVICES - 1010 MARSHALL ST SHREVEPORT, LA',
    erpAccountId: '22214',
    erpSystemName: ErpSystemEnum.Eclipse,
    shipTos: [
      {
        id: '54a08702-28d5-4214-b47a-3b5a57d82981',
        name: '73932 - THRASH CONSTRUCTION SERVICES SHOP - 1010 MARSHALL ST SHREVEPORT, LA',
        erpAccountId: '73932',
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      },
      {
        id: '9c102216-7510-4d9a-9e22-ad29cbf62d95',
        name: '217769 - THRASH CONSTRUCTION SERVICES - 1010 MARSHALL ST SHREVEPORT, LA',
        erpAccountId: '217769',
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      }
    ],
    __typename: 'EcommAccount'
  },
  {
    id: 'cb7f4c2f-69c6-4060-b49e-6c984e297a7e',
    name: '15744 - ABBY DEVELOPMENT I LP - 2103 CHANDLER ST KILGORE, TX',
    erpAccountId: '15744',
    erpSystemName: ErpSystemEnum.Eclipse,
    shipTos: [
      {
        id: '51056f89-5daf-4dd7-884a-4d79a17ad558',
        name: '67447 - ABBY DEVELOPMENT I LP SHOP - 2103 CHANDLER ST KILGORE, TX',
        erpAccountId: '67447',
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      },
      {
        id: 'be7d4f01-b57a-4ba3-ac1b-fbebf0245679',
        name: '217260 - ABBY DEVELOPMENT I LP - 1014 PRUITT PL TYLER, TX',
        erpAccountId: '217260',
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      }
    ],
    __typename: 'EcommAccount'
  },
  {
    id: 'fe751a0c-3c6c-4fd0-9d3d-a6c5a9bf68e5',
    name: '154075 - A GOOD PLUMBING - 26157 JEFFERSON AVE MURRIETA, CA',
    erpAccountId: '154075',
    erpSystemName: ErpSystemEnum.Eclipse,
    shipTos: [
      {
        id: '9f85d1b9-7844-4b60-b067-5d24c20a03cc',
        name: '156520 - A GOOD ENCLAVE PARK 1 - PORTOLA SPRINGS ENCLAVE 6 IRVINE, CA',
        erpAccountId: '156520',
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      },
      {
        id: '9c852777-32bb-4fec-bd14-99ed14b22bba',
        name: '159942 - A GOOD CARLSBAD FIRE STATION - CANYON AND WINDTRAIL WAY CARLSBAD, CA',
        erpAccountId: '159942',
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      },
      {
        id: '95bc8125-4b4d-47d6-b57e-fd4467eb9ccc',
        name: '162320 - A GOOD PLUMBING ESRI BLDG L - 364 NEW YORK STREET REDLANDS, CA',
        erpAccountId: '162320',
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      }
    ],
    __typename: 'EcommAccount'
  },
  {
    id: '9f272559-81cc-48ee-959c-1e560a18aa1b',
    name: '200060 - ****USE 71754**** - 3007 PRADERA ST MISSION, TX',
    erpAccountId: '200060',
    erpSystemName: ErpSystemEnum.Eclipse,
    shipTos: null,
    __typename: 'EcommAccount'
  },
  {
    id: '83e886f7-6f9f-4868-8dfa-973e8a034fcf',
    name: '208979 - WOODRUFF ROEBUCK WATER - P.O. BOX 182 WOODRUFF, SC',
    erpAccountId: '208979',
    erpSystemName: ErpSystemEnum.Eclipse,
    shipTos: null,
    __typename: 'EcommAccount'
  },
  {
    id: 'a0a4e2be-62dd-42a0-a644-686d43f4b5c7',
    name: '286 - SOUTHWEST POOLS & SPAS - 2438 INDUSTRIAL BLVD PMB 124 ABILENE, TX',
    erpAccountId: '286',
    erpSystemName: ErpSystemEnum.Eclipse,
    shipTos: [
      {
        id: '701c8c76-7286-47b5-a188-c25a8c302d9c',
        name: '5427 - SOUTHWEST POOLS & SPAS SHOP - 8233 BUFFALO GAP RD ABILENE, TX',
        erpAccountId: '5427',
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      },
      {
        id: '83d766ab-d93c-4226-aae9-da496560b327',
        name: '664661 - SOUTHWEST PEREZ 16418 KOONCE L - 16418 KOONCE LN CHRISTOVAL, TX',
        erpAccountId: '664661',
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      }
    ],
    __typename: 'EcommAccount'
  },
  {
    id: null,
    name: null,
    erpAccountId: null,
    erpSystemName: ErpSystemEnum.Eclipse,
    shipTos: [
      {
        id: null,
        name: null,
        erpAccountId: null,
        erpSystemName: ErpSystemEnum.Eclipse,
        __typename: 'EcommAccount'
      }
    ],
    __typename: 'EcommAccount'
  }
];

export const dummyRoles: Role[] = [
  {
    __typename: 'Role',
    id: '1',
    name: 'A',
    description: 'AAAAA'
  },
  {
    __typename: 'Role',
    id: '2',
    name: 'B',
    description: 'AAAAA'
  },
  {
    __typename: 'Role',
    id: '3',
    name: 'Purchase with Approval',
    description: 'Purchase with Approval'
  }
];
