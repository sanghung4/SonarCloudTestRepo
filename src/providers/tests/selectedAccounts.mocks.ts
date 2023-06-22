import { mockErpAccount } from 'Account/tests/mocks';
import {
  ErpSystemEnum,
  GetSelectedErpAccountQuery,
  GetSelectedErpAccountsQuery
} from 'generated/graphql';
import { SelectedAccounts } from 'providers/SelectedAccountsProvider';
import { dummyUserAccounts } from 'test-utils/dummyData';

export const mockSelectedAccounts: SelectedAccounts = {
  erpSystemName: ErpSystemEnum.Eclipse,
  shipTo: {
    ...dummyUserAccounts[0],
    accountName: 'test-1',
    accountNumber: '12345'
  },
  billTo: { ...dummyUserAccounts[0] },
  shippingBranchId: 'test-branch'
};
export const mockMincronSelectedAccounts: SelectedAccounts = {
  ...mockSelectedAccounts,
  erpSystemName: ErpSystemEnum.Mincron,
  billTo: {
    ...mockSelectedAccounts.billTo,
    erpSystemName: ErpSystemEnum.Mincron
  }
};

export const GET_ERP_ACCOUNT_EMPTY: GetSelectedErpAccountQuery = {
  account: []
};
export const GET_ERP_ACCOUNT_RES: GetSelectedErpAccountQuery = {
  account: [
    {
      ...mockErpAccount,
      branchId: null,
      erpName: ErpSystemEnum.Mincron,
      erp: ErpSystemEnum.Mincron
    }
  ]
};
export const GET_ERP_ACCOUNTS_EMPTY: GetSelectedErpAccountsQuery = {
  billToAccount: [],
  shipToAccount: []
};
export const GET_ERP_ACCOUNTS_RES: GetSelectedErpAccountsQuery = {
  billToAccount: [{ ...mockErpAccount, erpName: ErpSystemEnum.Eclipse }],
  shipToAccount: [
    { ...mockErpAccount, erpName: ErpSystemEnum.Eclipse, branchId: null }
  ]
};
