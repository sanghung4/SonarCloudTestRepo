import { subDays } from 'date-fns';

import { CoercedUser } from 'CustomerApproval/util/types';
import { UseCustomerApprovalTableProps } from 'CustomerApproval/util/useCustomerApprovalTable';
import {
  GetAllUnapprovedAccountRequestsDocument,
  PhoneType
} from 'generated/graphql';
import { UseSyncQueryParamProps } from 'CustomerApproval/util/useSyncQueryParam';
import { mockTableInstance } from 'test-utils/mockTableInstance';
import { TableInstance } from 'react-table';

export const mockCoercedUsers: CoercedUser[] = [
  {
    id: '1',
    email: 'test1@gmail.com',
    firstName: 'Testing',
    lastName: 'AllThings',
    branchId: '123',
    companyName: 'Morsco',
    phoneNumber: '1112223333',
    phoneType: PhoneType.Home,
    createdAt: subDays(new Date(), 10).toString(),
    accountNumber: '1122333'
  },
  {
    id: '2',
    email: 'test2@gmail.com',
    firstName: 'Tes2',
    lastName: 'AllTings',
    branchId: '123',
    companyName: 'Morsco',
    phoneNumber: '1112223333',
    phoneType: PhoneType.Mobile,
    createdAt: subDays(new Date(), 10).toString(),
    accountNumber: '1122333'
  },
  {
    id: '3',
    email: 'test3@gmail.com',
    firstName: 'Han',
    lastName: 'Solo',
    branchId: '456',
    companyName: 'Morsco',
    phoneNumber: '1112223333',
    phoneType: PhoneType.Office,
    createdAt: subDays(new Date(), 10).toString(),
    accountNumber: '1122333'
  }
];

export const mockCoercedUsersAlt: CoercedUser[] = [
  {
    id: '1',
    email: 'test1@gmail.com',
    firstName: 'Testing',
    lastName: 'AllThings',
    branchId: '222',
    companyName: 'Morsco',
    phoneNumber: '1112223333',
    phoneType: PhoneType.Home,
    createdAt: subDays(new Date(), 10).toString(),
    accountNumber: '1122333'
  },
  {
    id: '2',
    email: 'test2@gmail.com',
    firstName: 'Tes2',
    lastName: 'AllTings',
    branchId: '333',
    companyName: 'Morsco',
    phoneNumber: '1112223333',
    phoneType: PhoneType.Mobile,
    createdAt: subDays(new Date(), 10).toString(),
    accountNumber: '1122333'
  },
  {
    id: '3',
    email: 'test3@gmail.com',
    firstName: 'Han',
    lastName: 'Solo',
    branchId: undefined,
    companyName: 'Morsco',
    phoneNumber: '1112223333',
    phoneType: PhoneType.Office,
    createdAt: subDays(new Date(), 10).toString(),
    accountNumber: '1122333'
  }
];

export const dataMocks = [
  {
    request: {
      query: GetAllUnapprovedAccountRequestsDocument
    },
    result: {
      data: {
        allUnapprovedAccountRequests: [
          {
            id: '5',
            email: 'test5@gmail.com',
            firstName: 'Test5',
            lastName: 'AllThings',
            branchId: '777',
            phoneNumber: '33388877777',
            language: 'English',
            phoneType: {
              id: '123',
              name: 'Mobile'
            },
            companyName: 'Morsco',
            createdAt: subDays(new Date(), 10).toString(),
            accountNumber: '1122333'
          },
          {
            id: '6',
            email: 'test6@gmail.com',
            firstName: 'Test6',
            lastName: 'AllTings',
            branchId: '888',
            phoneNumber: '33388997777',
            companyName: 'Morsco',
            phoneType: {
              id: '123',
              name: 'Mobile'
            },
            createdAt: subDays(new Date(), 10).toString(),
            accountNumber: '1122333'
          },
          {
            id: '7',
            email: 'test7@gmail.com',
            firstName: 'Test7',
            lastName: 'AllOfTheThings',
            branchId: undefined,
            phoneNumber: '30588877777',
            companyName: 'Morsco',
            phoneType: {
              id: '456',
              name: 'Office'
            },
            createdAt: subDays(new Date(), 10).toString(),
            accountNumber: undefined
          }
        ]
      }
    }
  }
];

export const mockUseCustomerApprovalTableProps: UseCustomerApprovalTableProps =
  {
    branch: '',
    data: [],
    filterValue: '',
    page: '0',
    setFilterValue: jest.fn(),
    sortBy: []
  };

export const mockUseSyncQueryParamProps: UseSyncQueryParamProps = {
  branch: '',
  filterValue: '',
  page: '0',
  setQueryParams: jest.fn(),
  sortBy: [],
  tableInstance: { ...(mockTableInstance as TableInstance<any>) }
};
