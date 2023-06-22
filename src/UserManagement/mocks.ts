import {
  UnapprovedAccountRequestsDocument,
  GetApprovedAccountRequestsDocument,
  RejectedAccountRequestsDocument
} from 'generated/graphql';
import { GraphQLError } from 'graphql';

export const mocks = [
  {
    request: {
      query: UnapprovedAccountRequestsDocument,
      variables: {
        accountId: '123456'
      }
    },
    result: {
      data: {
        unapprovedAccountRequests: [
          {
            id: 'f355e04d-9be3-45fd-82ea-3dab3707a883',
            email: 'tim+userman2@dialexa.com',
            firstName: 'John',
            lastName: 'Doe',
            companyName: '',
            phoneNumber: '(222) 222-2222',
            roleId: null,
            phoneType: {
              id: 'bcdb73c5-9d45-4933-b036-b5cf869a75ba',
              name: 'MOBILE'
            },
            createdAt: '5/12/2021'
          },
          {
            id: 'ab4e4f52-4ea9-4a19-950c-4404b7c4bb2d',
            email: 'tim+userman3@dialexa.com',
            firstName: 'Elliot',
            lastName: 'Clark',
            companyName: '',
            phoneNumber: '(222) 222-2222',
            roleId: null,
            phoneType: {
              id: 'bcdb73c5-9d45-4933-b036-b5cf869a75ba',
              name: 'MOBILE'
            },
            createdAt: '5/12/2021'
          },
          {
            id: '8410507e-afd9-48c2-8aae-d2dd88140aba',
            email: 'tim+userman5@dialexa.com',
            firstName: 'Brian',
            lastName: 'Bryan',
            companyName: '',
            phoneNumber: '(934) 024-8920',
            roleId: null,
            phoneType: {
              id: 'bcdb73c5-9d45-4933-b036-b5cf869a75ba',
              name: 'MOBILE'
            },
            createdAt: '5/12/2021'
          }
        ]
      }
    }
  },
  {
    request: {
      query: GetApprovedAccountRequestsDocument,
      variables: {
        accountId: '123456'
      }
    },
    result: {
      data: {
        accountUsers: [
          {
            id: '21766126-9cec-4d15-9b78-d2231da91931',
            email: 'eclipseadmindev+35648@morsco.com',
            firstName: 'john',
            lastName: 'admindev',
            phoneNumber: '(324) 234-2342',
            role: {
              id: 'c1ab78f0-d877-4fe8-bfef-99586d8e4131',
              name: 'Account Admin'
            },
            approverId: null,
            phoneType: {
              id: 'bcdb73c5-9d45-4933-b036-b5cf869a75ba',
              name: 'MOBILE'
            },
            contactUpdatedAt: '1/1/2021',
            contactUpdatedBy: '1/1/2021'
          },
          {
            id: '3286e61a-7b81-4cb3-8986-95e648b9080a',
            email: 'eclipsestanduserdev+35648@morsco.com',
            firstName: 'eclipse',
            lastName: 'standuserdev',
            phoneNumber: '(342) 423-4234',
            role: {
              id: '2419358e-5c74-4d07-a8d2-37c324afbe6c',
              name: 'Standard Access'
            },
            approverId: null,
            phoneType: {
              id: 'bcdb73c5-9d45-4933-b036-b5cf869a75ba',
              name: 'MOBILE'
            },
            contactUpdatedAt: '1/1/2021',
            contactUpdatedBy: '1/1/2021'
          },
          {
            id: '8410507e-afd9-48c2-8aae-d2dd88140aba',
            email: 'tim+userman6@dialexa.com',
            firstName: 'tim',
            lastName: 'standuserdev',
            phoneNumber: '(342) 423-4234',
            role: {
              id: '2419358e-5c74-4d07-a8d2-37c324afbe6c',
              name: 'Purchase with Approval'
            },
            approverId: null,
            phoneType: {
              id: 'bcdb73c5-9d45-4933-b036-b5cf869a75ba',
              name: 'MOBILE'
            },
            contactUpdatedAt: '1/1/2021',
            contactUpdatedBy: '1/1/2021'
          }
        ]
      }
    }
  },
  {
    request: {
      query: RejectedAccountRequestsDocument,
      variables: {
        accountId: '123456'
      }
    },
    result: {
      data: {
        rejectedAccountRequests: [
          {
            id: '4185a419-79cb-44de-9d94-1e29ce00bbbd',
            email: 'tim+userman1@dialexa.com',
            firstName: 'Tim',
            lastName: 'Waite',
            companyName: '',
            phoneNumber: '(222) 222-2222',
            phoneType: {
              id: 'bcdb73c5-9d45-4933-b036-b5cf869a75ba',
              name: 'MOBILE'
            },
            rejectionReason: {
              id: '5d506bb6-26b3-4b41-a670-5bd486eb0494',
              type: 'NOT_AUTHORIZED',
              description: 'Not authorized to be on E-Commerce'
            },
            rejectedAt: '5/12/2021',
            rejectedBy: 'seth+rddemo@dialexa.com',
            contactUpdatedAt: '1/2/2021'
          },
          {
            id: '4185a419-79cb-44de-9d94-1e29ce00bbbe',
            email: 'bob@dialexa.com',
            firstName: 'bob',
            lastName: 'bob',
            companyName: '',
            phoneNumber: '(222) 222-2222',
            phoneType: {
              id: 'bcdb73c5-9d45-4933-b036-b5cf869a75ba',
              name: 'MOBILE'
            },
            rejectionReason: {
              id: '5d506bb6-26b3-4b41-a670-5bd486eb0494',
              type: 'OTHER',
              description: 'Other'
            },
            rejectedAt: '5/12/2021',
            rejectedBy: 'seth+rddemo@dialexa.com',
            contactUpdatedAt: '1/2/2021'
          },
          {
            id: '4185a419-79cb-44de-9d94-1e29ce00bbbf',
            email: 'john@dialexa.com',
            firstName: 'john',
            lastName: 'john',
            companyName: '',
            phoneNumber: '(222) 222-2222',
            phoneType: {
              id: 'bcdb73c5-9d45-4933-b036-b5cf869a75ba',
              name: 'MOBILE'
            },
            rejectionReason: {
              id: '5d506bb6-26b3-4b41-a670-5bd486eb0494',
              type: 'NOT_A_COMPANY_MEMBER',
              description: 'Not a member of the company'
            },
            rejectedAt: '5/12/2021',
            rejectedBy: 'seth+rddemo@dialexa.com',
            contactUpdatedAt: '1/2/2021'
          },
          {
            id: '4185a419-79cb-44de-9d94-1e29ce00bbbg',
            email: 'Cameron@dialexa.com',
            firstName: 'Cameron',
            lastName: 'Cameron',
            companyName: '',
            phoneNumber: '(222) 222-2222',
            phoneType: {
              id: 'bcdb73c5-9d45-4933-b036-b5cf869a75ba',
              name: 'MOBILE'
            },
            rejectionReason: {
              id: '5d506bb6-26b3-4b41-a670-5bd486eb0494',
              type: 'NOT_A_COMPANY_MEMBER',
              description: 'Not a member of the company'
            },
            rejectedAt: '5/12/2021',
            rejectedBy: 'seth+rddemo@dialexa.com',
            contactUpdatedAt: '1/2/2021'
          }
        ]
      }
    }
  }
] as const;

export const mockOtherError = [
  {
    request: {
      query: UnapprovedAccountRequestsDocument,
      variables: {
        accountId: '123456'
      }
    },
    error: new GraphQLError('Other Error!')
  }
];

export const mockAccountNotFoundError = [
  {
    request: {
      query: UnapprovedAccountRequestsDocument,
      variables: {
        accountId: '123456'
      }
    },
    error: new GraphQLError('{"error":"Account with given ID not found."}')
  }
];
