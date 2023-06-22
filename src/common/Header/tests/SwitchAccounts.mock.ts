import { MockedResponse } from '@apollo/client/testing';
import {
  ErpSystemEnum,
  UserAccountsDocument,
  UserAccountsQuery
} from 'generated/graphql';

const userAccounntsData: UserAccountsQuery = {
  userAccounts: [
    {
      id: 'f8149f07-735b-4b54-81c7-07e39d3749a5',
      name: '287169 - Ecommerce MORSCO Test Bill-To - 300  E Vickery Blvd FORT WORTH, TX',
      erpAccountId: '287169',
      erpSystemName: ErpSystemEnum.Eclipse,
      shipTos: [
        {
          id: '2882e752-7323-41cc-976a-7bec199aaa9a',
          name: '287172 - Ecommerce MORSCO Test Shop Acct - 300 Vickery Blvd FORT WORTH, TX',
          erpAccountId: '287172',
          erpSystemName: ErpSystemEnum.Eclipse,
          __typename: 'EcommAccount'
        }
      ],
      __typename: 'EcommAccount'
    }
  ]
};

const userAccounntsDataMulti: UserAccountsQuery = {
  userAccounts: [
    {
      id: 'f8149f07-735b-4b54-81c7-07e39d3749a5',
      name: '287169 - Ecommerce MORSCO Test Bill-To - 300  E Vickery Blvd FORT WORTH, TX',
      erpAccountId: '287169',
      erpSystemName: ErpSystemEnum.Eclipse,
      shipTos: [
        {
          id: '2882e752-7323-41cc-976a-7bec199aaa9a',
          name: '287172 - Ecommerce MORSCO Test Shop Acct - 300 Vickery Blvd FORT WORTH, TX',
          erpAccountId: '287172',
          erpSystemName: ErpSystemEnum.Eclipse,
          __typename: 'EcommAccount'
        },
        {
          id: 'c9a4f736-0964-4564-8800-3cefeedb3656',
          name: '845833 - UNITED PACIFIC VV3 - 12905 HESPERIA RD. VICTORVILLE, CA',
          erpAccountId: '845833',
          erpSystemName: ErpSystemEnum.Eclipse,
          __typename: 'EcommAccount'
        }
      ],
      __typename: 'EcommAccount'
    },
    {
      id: 'b8149f07-735b-4b54-81c7-07e39d3749ac',
      name: '41970 - ALT Bill-to - 300 N LOMA DR ALBUQUERQUE, NM',
      erpAccountId: '41970',
      erpSystemName: ErpSystemEnum.Eclipse,
      shipTos: [
        {
          id: '28a2e652-7323-41cc-976a-7bec199aaa9b',
          name: '1321312 - ALT Test Shop Acct - TST',
          erpAccountId: '1321312',
          erpSystemName: ErpSystemEnum.Eclipse,
          __typename: 'EcommAccount'
        },
        {
          id: 'c9a4f736-0964-4564-8800-3cefeedb3657',
          name: '23428 - GALE APARTMENT - 6353 JUAN TABO BLVD NE, APT 6, ALBUQUERQUE, NM',
          erpAccountId: '23428',
          erpSystemName: ErpSystemEnum.Eclipse,
          __typename: 'EcommAccount'
        }
      ],
      __typename: 'EcommAccount'
    }
  ]
};

export const mockUserAccountQuerySuccess: MockedResponse<UserAccountsQuery> = {
  request: {
    query: UserAccountsDocument,
    variables: { userId: 'test' }
  },
  result: {
    data: userAccounntsData
  }
};
export const mockUserAccountQueryMulti: MockedResponse<UserAccountsQuery> = {
  request: {
    query: UserAccountsDocument,
    variables: { userId: 'test' }
  },
  result: {
    data: userAccounntsDataMulti
  }
};
export const mockUserAccountQueryMultiShipTo: MockedResponse<UserAccountsQuery> =
  {
    request: {
      query: UserAccountsDocument,
      variables: { userId: 'test' }
    },
    result: {
      data: {
        ...userAccounntsDataMulti,
        userAccounts: [userAccounntsDataMulti.userAccounts![0]]
      }
    }
  };
export const mockUserAccountQueryError: MockedResponse<UserAccountsQuery> = {
  request: {
    query: UserAccountsDocument,
    variables: { userId: 'test' }
  },
  error: {
    name: 'test',
    message: 'test error'
  }
};
