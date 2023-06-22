import { MockedResponse } from '@apollo/client/testing';
import {
  BranchListItemFragment,
  GetBranchesListDocument
} from 'generated/graphql';

export const mockGetBranchesListQuery: MockedResponse<{
  branches: BranchListItemFragment[];
}> = {
  request: { query: GetBranchesListDocument },
  result: {
    data: {
      branches: [
        {
          branchId: '1321',
          name: 'Murray Supply',
          address1: '2509 Clements Ferry Road',
          city: 'Charleston',
          state: 'South Carolina',
          zip: '29492',
          isActive: true,
          isAvailableInStoreFinder: true,
          isShoppable: true,
          isPricingOnly: false,
          isPlumbing: false,
          isWaterworks: false,
          isHvac: false,
          isBandK: false,
          __typename: 'Branch',
          id: '1234'
        },
        {
          branchId: '1046',
          name: 'Morrison Supply',
          address1: '1666 South San Marcos',
          city: 'San Antonio',
          state: 'Texas',
          zip: '78207',
          isActive: true,
          isAvailableInStoreFinder: true,
          isShoppable: true,
          isPricingOnly: false,
          isPlumbing: true,
          isWaterworks: false,
          isHvac: false,
          isBandK: false,
          __typename: 'Branch',
          id: '5678'
        }
      ]
    }
  }
};
