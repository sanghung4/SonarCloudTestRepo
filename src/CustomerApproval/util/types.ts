import { PhoneType } from 'generated/graphql';

export type CoercedUser = {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  branchId?: string;
  phoneNumber: string;
  phoneType: PhoneType;
  companyName: string;
  createdAt: string;
  accountNumber?: string;
};

export type SearchParams = {
  branch: string;
  page: string;
  sortBy: string[];
};
