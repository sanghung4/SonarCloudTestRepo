import { BranchContextType } from 'providers/BranchProvider';
import { noop } from 'lodash-es';

export const PAGE_SIZE = 5;
export const NUM_LOADING = 5;
export const MAX_PAGES = 10;
export const VALID_ROUTES_WHEN_BRANCH_IS_MISSING = [
  '/account',
  '/customer-approval',
  '/error',
  '/select-accounts',
  '/support',
  '/user-management',
  '/location-search',
  '/credit-forms',
  '/job-form'
];
export const defaultBranchContext: BranchContextType = {
  homeBranchLoading: false,
  homeBranchError: '',
  shippingBranchLoading: false,
  nearbyBranchesLoading: false,
  branchSelectOpen: false,
  setBranchSelectOpen: noop,
  setStock: noop,
  setShippingBranch: noop,
  division: 'none',
  setDivision: noop,
  setProductId: noop
};
