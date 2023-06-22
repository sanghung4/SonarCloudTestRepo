import { DataMap } from '@reece/global-types';

import { AuthContext } from 'AuthProvider';
import { authConfig, mockAuthContext } from 'AuthProvider.mocks';
import useFooterData, { FooterItem } from 'common/Footer/util/useFooterData';
import { Permission } from 'common/PermissionRequired';
import { ErpSystemEnum } from 'generated/graphql';
import { render } from 'test-utils/TestWrapper';
import { mockSelectedAccounts } from 'hooks/tests/mocks/useSelectedAccounts.mocks';

/**
 * Mock variables / tools
 */
const mocks = {
  auth: { ...mockAuthContext },
  isProd: false,
  isWaterworks: false,
  selectedAccounts: { ...mockSelectedAccounts }
};
const mockPermission = (p: Permission) => ({
  ...authConfig.profile,
  permissions: [p]
});

/**
 * Mock Methods
 */
jest.mock('hooks/useDomainInfo', () => ({
  ...jest.requireActual('hooks/useDomainInfo'),
  useDomainInfo: () => ({ isWaterworks: mocks.isWaterworks })
}));
jest.mock('utils/configuration', () => ({
  ...jest.requireActual('utils/configuration'),
  getEnvironment: () => ({ isProd: mocks.isProd })
}));

/**
 * Setup function for each test
 */
function setup(p: typeof mocks) {
  let output: DataMap<FooterItem[]> = {};
  function MockComponent() {
    Object.assign(output, useFooterData());
    return null;
  }
  render(
    <AuthContext.Provider value={p.auth}>
      <MockComponent />
    </AuthContext.Provider>,
    { selectedAccountsConfig: { selectedAccounts: mocks.selectedAccounts } }
  );
  return output;
}

/**
 * TEST
 */
describe('Footer - util - useFooterData', () => {
  afterEach(() => {
    mocks.auth = { ...mockAuthContext };
    mocks.isProd = false;
    mocks.isWaterworks = false;
    mocks.selectedAccounts = { ...mockSelectedAccounts };
  });

  // Contract
  it('expect Contract to be false', () => {
    mocks.isWaterworks = false;
    const result = setup(mocks).max[0];
    expect(result.condition).toBeFalsy();
  });
  it('expect Contract to be true', () => {
    mocks.isWaterworks = true;
    const result = setup(mocks).max[0];
    expect(result.condition).toBeTruthy();
  });

  // List
  it('expect List to be false', () => {
    mocks.selectedAccounts.erpSystemName = ErpSystemEnum.Mincron;
    const result = setup(mocks).max[1];
    expect(result.condition).toBeFalsy();
  });
  it('expect List to be true', () => {
    mocks.auth.activeFeatures = ['LISTS'];
    const result = setup(mocks).max[1];
    expect(result.condition).toBeTruthy();
  });

  // Quotes
  it('expect Quotes to be false', () => {
    mocks.selectedAccounts.erpSystemName = ErpSystemEnum.Mincron;
    const result = setup(mocks).max[2];
    expect(result.condition).toBeFalsy();
  });
  it('expect Quotes to be true', () => {
    const result = setup(mocks).max[2];
    expect(result.condition).toBeTruthy();
  });

  // Purchase Approvals
  it('expect Purchase Approvals to be false', () => {
    mocks.selectedAccounts.erpSystemName = ErpSystemEnum.Mincron;
    const result = setup(mocks).max[3];
    expect(result.condition).toBeFalsy();
  });
  it('expect Purchase Approvals to be true', () => {
    mocks.auth.profile = mockPermission(Permission.APPROVE_CART);
    const result = setup(mocks).max[3];
    expect(result.condition).toBeTruthy();
  });

  // Invoices
  it('expect Invoices to be false', () => {
    mocks.auth.authState = { isAuthenticated: true };
    mocks.selectedAccounts.erpSystemName = ErpSystemEnum.Mincron;
    const result = setup(mocks).max[5];
    expect(result.condition).toBeFalsy();
  });
  it('expect Invoices to be true', () => {
    mocks.auth.profile = mockPermission(Permission.VIEW_INVOICE);
    const result = setup(mocks).max[5];
    expect(result.condition).toBeTruthy();
  });

  // PPP
  it('expect PPP to be false', () => {
    mocks.selectedAccounts.erpSystemName = ErpSystemEnum.Mincron;
    const result = setup(mocks).max[6];
    expect(result.condition).toBeFalsy();
  });
  it('expect PPP to be true', () => {
    mocks.auth.authState = { isAuthenticated: true };
    const result = setup(mocks).max[6];
    expect(result.condition).toBeTruthy();
  });

  // Job Form
  it('expect Job Form to be true for prod', () => {
    mocks.isProd = true;
    mocks.selectedAccounts.erpSystemName = ErpSystemEnum.Mincron;
    const result = setup(mocks)['common.resources'][3];
    expect(result.condition).toBeTruthy();
  });
  it('expect Job Form to be true', () => {
    const result = setup(mocks)['common.resources'][3];
    expect(result.condition).toBeTruthy();
  });
});
