import { BranchContextType, Divisions } from 'providers/BranchProvider';
import { Branch, ErpSystemEnum } from 'generated/graphql';

export const mockBasicBranch: Branch = {
  branchId: '1004',
  name: 'Test Branch',
  isBandK: false,
  isHvac: true,
  isWaterworks: false,
  isPlumbing: false,
  longitude: 34.0,
  latitude: -96.9023,
  isActive: false,
  isAvailableInStoreFinder: false,
  isPricingOnly: false,
  isShoppable: false,
  id: ''
};

export const mockBranch: Branch = {
  __typename: 'Branch',
  branchId: '9876',
  name: 'Test Branch WY',
  isBandK: false,
  isHvac: true,
  isWaterworks: false,
  isPlumbing: false,
  longitude: 34.0,
  latitude: -96.9023,
  address1: '12 2nd St',
  address2: 'Ste 303',
  city: 'Greenriver',
  state: 'Wyoming',
  zip: '82935',
  phone: '307-555-1234',
  actingBranchManager: 'Thomas McPherson',
  actingBranchManagerEmail: 't.mcpherson@reece.com',
  actingBranchManagerPhone: '307-555-0000',
  businessHours: 'M-F: 9AM - 5PM;SAT: 11AM - 5PM;SUN: 1PM - 3PM',
  distance: 954,
  erpSystemName: ErpSystemEnum.Eclipse,
  isActive: false,
  isAvailableInStoreFinder: false,
  isPricingOnly: false,
  isShoppable: false,
  id: ''
};

type Config = {
  isWaterworks?: boolean;
  isBandK?: boolean;
  isHvac?: boolean;
  isPlumbing?: boolean;
};
export function createMockBranches(count: number, config: Config) {
  const output: Branch[] = [];
  const template = (n: number) =>
    ({
      branchId: `${n}`,
      name: `Test ${n}`,
      isBandK: false,
      isHvac: false,
      isPlumbing: false,
      isWaterworks: false,
      latitude: 0,
      longitude: 0,
      ...config
    } as Branch);
  [...Array(count)].forEach((_, i) => output.push(template(i)));
  return output;
}

export const mockBranchContext: BranchContextType = {
  homeBranchLoading: false,
  homeBranchError: '',
  shippingBranchLoading: false,
  nearbyBranchesLoading: false,
  branchSelectOpen: false,
  setBranchSelectOpen: jest.fn(),
  setStock: jest.fn(),
  setShippingBranch: jest.fn(),
  division: 'none',
  setDivision: jest.fn(),
  setProductId: jest.fn()
};
