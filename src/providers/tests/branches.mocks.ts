import { mockBranch } from 'Branches/tests/mocks';
import { mockCart } from 'Cart/tests/mocks';
import {
  FindBranchesQuery,
  GetBranchQuery,
  UpdateWillCallBranchMutation
} from 'generated/graphql';

export const GET_BRANCH_RES: GetBranchQuery = {
  branch: { ...mockBranch }
};
export const FIND_BRANCHES_EMPTY: FindBranchesQuery = {
  branchSearch: { latitude: 0, longitude: 0, branches: [] }
};
export const UPDATE_WILL_CALL_RES: UpdateWillCallBranchMutation = {
  updateWillCallBranch: { ...mockCart }
};
