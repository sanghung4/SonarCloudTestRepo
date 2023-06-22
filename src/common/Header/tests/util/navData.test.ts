import { TFunction } from 'i18next';

import { getNavData } from 'common/Header/util';
import { NavProps } from 'common/Header/util/navData';
import { Permission } from 'common/PermissionRequired';
import { ErpSystemEnum } from 'generated/graphql';
import { UserContextType } from 'AuthProvider';

/**
 * Default Mocks
 */
const mockT = ((data: string) => data) as TFunction;
const defaultProps: NavProps = {
  t: mockT,
  authState: {},
  isWaterworks: false
};
const mockProfile: UserContextType['profile'] = {
  permissions: [],
  userId: '',
  isEmployee: false,
  isVerified: false
};
const mockEnvironment = {
  isProd: false,
  isSandbox: false,
  isDev: false,
  isTest: false,
  isUat: false
};

/**
 * Mock props
 */
let props = { ...defaultProps };

/**
 * Mock methods
 */
jest.mock('utils/configuration', () => ({
  ...jest.requireActual('utils/configuration'),
  getEnvironment: () => mockEnvironment
}));

/**
 * Mock function
 */
const assignMockPerm = (perm: Permission): UserContextType['profile'] => ({
  ...mockProfile,
  permissions: [perm]
});

/**
 * Tests
 */
describe('common - Header - util - navData', () => {
  afterEach(() => {
    props = { ...defaultProps };
    mockEnvironment.isProd = false;
    mockEnvironment.isSandbox = false;
    mockEnvironment.isDev = false;
    mockEnvironment.isTest = false;
    mockEnvironment.isUat = false;
  });

  /******************** maX ********************/
  // maX - Contracts
  it('expect maX contracts condition to be false', () => {
    const result = getNavData(props);
    const { id, condition } = result.max[0];

    expect(id).toBe('contracts');
    expect(condition).toBeFalsy();
  });
  it('expect maX contracts condition to be true', () => {
    props.erpSystemName = ErpSystemEnum.Mincron;

    const result = getNavData(props);
    const { id, condition } = result.max[0];

    expect(id).toBe('contracts');
    expect(condition).toBeTruthy();
  });

  // maX - Orders
  it('expect maX orders condition to be true', () => {
    const result = getNavData(props);
    const { id, condition } = result.max[1];

    expect(id).toBe('orders');
    expect(condition).toBeTruthy();
  });

  // maX - Previously Purchased Products
  it('expect maX PPP condition to be false', () => {
    props.erpSystemName = ErpSystemEnum.Mincron;

    const result = getNavData(props);
    const { id, condition } = result.max[2];

    expect(id).toBe('previously-purchased-products');
    expect(condition).toBeFalsy();
  });
  it('expect maX PPP condition to be true', () => {
    props.authState = { isAuthenticated: true };

    const result = getNavData(props);
    const { id, condition } = result.max[2];

    expect(id).toBe('previously-purchased-products');
    expect(condition).toBeTruthy();
  });

  // maX - Invoices
  it('expect maX Invoices condition to be false', () => {
    props.authState = { isAuthenticated: true };

    const result = getNavData(props);
    const { id, condition } = result.max[3];

    expect(id).toBe('invoices');
    expect(condition).toBeFalsy();
  });
  it('expect maX Invoices condition to be true', () => {
    props.profile = assignMockPerm(Permission.VIEW_INVOICE);

    const result = getNavData(props);
    const { id, condition } = result.max[3];

    expect(id).toBe('invoices');
    expect(condition).toBeTruthy();
  });

  // maX - Purchase Approvals
  it('expect maX Purchase Approvals condition to be false', () => {
    props.erpSystemName = ErpSystemEnum.Mincron;

    const result = getNavData(props);
    const { id, condition } = result.max[4];

    expect(id).toBe('purchase-approvals');
    expect(condition).toBeFalsy();
  });
  it('expect maX Purchase Approvals condition to be true', () => {
    props.profile = assignMockPerm(Permission.APPROVE_CART);

    const result = getNavData(props);
    const { id, condition } = result.max[4];

    expect(id).toBe('purchase-approvals');
    expect(condition).toBeTruthy();
  });

  // maX - Quotes
  it('expect maX Quotes condition to be false', () => {
    props.erpSystemName = ErpSystemEnum.Mincron;

    const result = getNavData(props);
    const { id, condition } = result.max[5];

    expect(id).toBe('quotes');
    expect(condition).toBeFalsy();
  });
  it('expect maX Quotes condition to be true', () => {
    const result = getNavData(props);
    const { id, condition } = result.max[5];

    expect(id).toBe('quotes');
    expect(condition).toBeTruthy();
  });

  // maX - Lists
  it('expect maX Lists condition to be false', () => {
    props.erpSystemName = ErpSystemEnum.Mincron;

    const result = getNavData(props);
    const { id, condition } = result.max[6];

    expect(id).toBe('lists');
    expect(condition).toBeFalsy();
  });
  it('expect maX Lists condition to be true', () => {
    props.activeFeatures = ['LISTS'];

    const result = getNavData(props);
    const { id, condition } = result.max[6];

    expect(id).toBe('lists');
    expect(condition).toBeTruthy();
  });

  // maX - Features
  it('expect maX Features condition to be false', () => {
    const result = getNavData(props);
    const { id, condition } = result.max[7];

    expect(id).toBe('features');
    expect(condition).toBeFalsy();
  });
  it('expect maX Features condition to be true', () => {
    props.profile = assignMockPerm(Permission.TOGGLE_FEATURES);

    const result = getNavData(props);
    const { id, condition } = result.max[7];

    expect(id).toBe('features');
    expect(condition).toBeTruthy();
  });

  /******************** Profile ********************/
  // Profile - Customer Approval
  it('expect Profile Customer Approval condition to be false', () => {
    const result = getNavData(props);
    const { id, condition } = result.profile[0];

    expect(id).toBe('customer-approval');
    expect(condition).toBeFalsy();
  });
  it('expect Profile Customer Approval condition to be true', () => {
    props.profile = assignMockPerm(Permission.APPROVE_ALL_USERS);

    const result = getNavData(props);
    const { id, condition } = result.profile[0];

    expect(id).toBe('customer-approval');
    expect(condition).toBeTruthy();
  });

  // Profile - User Management
  it('expect Profile User Management condition to be false', () => {
    const result = getNavData(props);
    const { id, condition } = result.profile[1];

    expect(id).toBe('user-management');
    expect(condition).toBeFalsy();
  });
  it('expect Profile User Management condition to be true', () => {
    props.profile = assignMockPerm(Permission.MANAGE_ROLES);

    const result = getNavData(props);
    const { id, condition } = result.profile[1];

    expect(id).toBe('user-management');
    expect(condition).toBeTruthy();
  });

  // Profile - Account Information
  it('expect Profile Account Info condition to be false', () => {
    const result = getNavData(props);
    const { id, condition } = result.profile[2];

    expect(id).toBe('account-information');
    expect(condition).toBeFalsy();
  });
  it('expect Profile Account Info condition to be true', () => {
    props.profile = assignMockPerm(Permission.MANAGE_ROLES);

    const result = getNavData(props);
    const { id, condition } = result.profile[2];

    expect(id).toBe('account-information');
    expect(condition).toBeTruthy();
  });

  // Profile - Payment Information
  it('expect Profile Payment Info condition to be false', () => {
    props.erpSystemName = ErpSystemEnum.Mincron;

    const result = getNavData(props);
    const { id, condition } = result.profile[3];

    expect(id).toBe('payment-information');
    expect(condition).toBeFalsy();
  });
  it('expect Profile Payment Info condition to be true', () => {
    props.profile = assignMockPerm(Permission.MANAGE_PAYMENT_METHODS);

    const result = getNavData(props);
    const { id, condition } = result.profile[3];

    expect(id).toBe('payment-information');
    expect(condition).toBeTruthy();
  });

  /******************** Resources ********************/
  // Resources - Credit Forms
  it('expect Resources Credit Forms condition to be true', () => {
    const result = getNavData(props);
    const { id, condition } = result.resources[0];

    expect(id).toBe('credit-forms');
    expect(condition).toBeTruthy();
  });

  // Resources - Job Form
  it('expect Resources Job Form condition to be true', () => {
    const result = getNavData(props);
    const { id, condition } = result.resources[1];

    expect(id).toBe('job-form');
    expect(condition).toBeTruthy();
  });

  // Resources - About
  it('expect Resources About condition to be true', () => {
    const result = getNavData(props);
    const { id, condition } = result.resources[2];

    expect(id).toBe('about');
    expect(condition).toBeTruthy();
  });

  // Resources - Works For You
  it('expect Resources Works For You condition to be true', () => {
    const result = getNavData(props);
    const { id, condition } = result.resources[4];

    expect(id).toBe('works-for-you');
    expect(condition).toBeTruthy();
  });

  // Resources - News
  it('expect News condition to be true', () => {
    props.activeFeatures = ['NEWS_BLOG'];
    const result = getNavData(props);
    const { id, condition } = result.resources[3];

    expect(id).toBe('news');
    expect(condition).toBeTruthy();
  });
});
