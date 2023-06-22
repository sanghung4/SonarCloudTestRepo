import { UserContextType } from 'AuthProvider';
import { ContractsDocument, PhoneType } from 'generated/graphql';

export const success = {
  request: {
    query: ContractsDocument,
    variables: {
      erpAccountId: '210852',
      pageNumber: '1',
      searchFilter: '',
      fromDate: '',
      toDate: '',
      sortOrder: 'lastRelease',
      sortDirection: 'DESC'
    }
  },
  result: {
    data: {
      contracts: {
        rowsReturned: 7,
        startRow: 1,
        totalRows: 7,
        results: [
          {
            contractNumber: '5432705',
            description: 'TESTING EQUIPMENT',
            contractDate: '09/16/2021',
            promiseDate: '00/00/0000',
            firstReleaseDate: '09/16/2021',
            lastReleaseDate: '09/16/2021',
            jobNumber: 'ORGANIC',
            jobName: 'ORGANIC CONST.',
            purchaseOrderNumber: ''
          },
          {
            contractNumber: '5416999',
            description: 'TAP-MATE TAPPING MACHINE',
            contractDate: '08/31/2021',
            promiseDate: '00/00/0000',
            firstReleaseDate: '08/31/2021',
            lastReleaseDate: '08/31/2021',
            jobNumber: 'ORGANIC',
            jobName: 'ORGANIC CONST.',
            purchaseOrderNumber: ''
          },
          {
            contractNumber: '5404224',
            description: "FT.RILEY- 6' AVT - GWO# 272000133",
            contractDate: '08/18/2021',
            promiseDate: '00/00/0000',
            firstReleaseDate: '08/18/2021',
            lastReleaseDate: '08/18/2021',
            jobNumber: '',
            jobName: '',
            purchaseOrderNumber: 'GWO#272000133'
          },
          {
            contractNumber: '5370587',
            description: 'AVT VLVS F/ K GWO # 272000133',
            contractDate: '07/16/2021',
            promiseDate: '00/00/0000',
            firstReleaseDate: '07/16/2021',
            lastReleaseDate: '07/16/2021',
            jobNumber: 'ORGANIC',
            jobName: 'ORGANIC CONST.',
            purchaseOrderNumber: 'GWO# 272000133'
          },
          {
            contractNumber: '5199026',
            description: 'TAP-MATE TAPPING MACHINE',
            contractDate: '01/26/2021',
            promiseDate: '00/00/0000',
            firstReleaseDate: '01/26/2021',
            lastReleaseDate: '02/26/2021',
            jobNumber: '',
            jobName: '',
            purchaseOrderNumber: '119099 OP'
          },
          {
            contractNumber: '5189302',
            description: 'AVT MACHINE W/ POWER PACK',
            contractDate: '01/14/2021',
            promiseDate: '00/00/0000',
            firstReleaseDate: '01/14/2021',
            lastReleaseDate: '01/14/2021',
            jobNumber: 'ORGANIC',
            jobName: 'ORGANIC CONST.',
            purchaseOrderNumber: '119268 000 OP'
          },
          {
            contractNumber: '5432703',
            description: 'TESTING EQUIPMENT - FORT BLISS',
            contractDate: '09/16/2021',
            promiseDate: '00/00/0000',
            firstReleaseDate: '09/16/2021',
            lastReleaseDate: '09/16/2021',
            jobNumber: 'ORGANIC',
            jobName: 'ORGANIC CONST.',
            purchaseOrderNumber: '.'
          }
        ]
      }
    },
    loading: false
  }
};

export const nullish = {
  request: {
    query: ContractsDocument,
    variables: {
      erpAccountId: '210852',
      pageNumber: '1',
      searchFilter: '',
      fromDate: '',
      toDate: ''
    }
  },
  result: {
    data: {
      contracts: {
        results: [
          {
            contractNumber: null,
            description: null,
            contractDate: null,
            promiseDate: null,
            firstReleaseDate: null,
            lastReleaseDate: null,
            jobNumber: null,
            jobName: null,
            purchaseOrderNumber: null
          },
          {
            contractNumber: null,
            description: null,
            contractDate: null,
            promiseDate: null,
            firstReleaseDate: null,
            lastReleaseDate: null,
            jobNumber: null,
            jobName: null,
            purchaseOrderNumber: null
          }
        ]
      }
    },
    loading: false
  }
};

export const empty = {
  request: {
    query: ContractsDocument,
    variables: {
      erpAccountId: '210852',
      pageNumber: '1',
      searchFilter: '',
      fromDate: '',
      toDate: ''
    }
  },
  result: {
    data: {
      contracts: {
        rowsReturned: 0,
        startRow: 0,
        totalRows: 0,
        results: []
      }
    },
    loading: false
  }
};

export const loading = {
  request: {
    query: ContractsDocument,
    variables: {
      erpAccountId: '210852',
      pageNumber: '1',
      searchFilter: '',
      fromDate: '',
      toDate: ''
    }
  },
  result: {
    data: {
      contracts: {
        rowsReturned: 0,
        startRow: 0,
        totalRows: 0,
        results: []
      }
    },
    loading: true
  }
};

export const invoiceOnlyAuth: UserContextType = {
  authState: { isAuthenticated: true },
  ecommUser: {
    id: '3773a69d-18b4-4def-a567-b8ee653ed6ad',
    email: 'mincron_200060_admin_user1@test.com',
    firstName: 'Test',
    lastName: 'Test',
    phoneNumber: '(123) 124-5123',
    role: {
      name: 'Invoice Only'
    },
    phoneType: PhoneType.Mobile
  },
  isLoggingOut: false
};
