import { ErpAccount, ErpSystemEnum } from 'generated/graphql';
import { SelectedAccounts, SelectedAccountsContextType } from 'providers/SelectedAccountsProvider';

export const mockErpAccount: ErpAccount = {
  __typename: 'ErpAccount',
  branchId: 'test-id-123',
  city: 'Houston',
  companyName: 'HG',
  creditHold: false,
  email: ['test@fixurgadget.com'],
  erp: ErpSystemEnum.Eclipse,
  erpAccountId: 'test-erp-account-id-123',
  phoneNumber: '832-965-9280',
  poReleaseRequired: '',
  state: 'Texas',
  street1: '123 S Gessner Rd',
  street2: 'Suite 245',
  territory: "Walter White's territory",
  zip: '77472',
  alwaysCod: false
};

export const mockBillTo: SelectedAccounts['billTo'] = {
  shipTos: [{ erpAccountId: 'test' }]
};

export const mockSelectedAccountsContext: SelectedAccountsContextType = {
  selectedAccounts: {
    billToErpAccount: mockErpAccount
  },
  updateShippingBranchId: jest.fn(),
  updateAccounts: jest.fn(),
  clearAccounts: jest.fn(),
  loading: false,
  isMincron: false,
  isEclipse: true,
  error: undefined
}
