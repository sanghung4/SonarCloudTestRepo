import { PhoneType } from 'generated/graphql';

export const userInfo = {
  firstName: 'test',
  lastName: 'test',
  company: 'test',
  phone: '(222) 222-2222',
  email: 'tim+userman2@dialexa.com',
  language: 'en',
  id: 'ab4e4f52-4ea9-4a19-950c-4404b7c4bb2d',
  phoneType: PhoneType.Mobile,
  createdAt: '5/12/2021',
  branchId: '342wfergw-9d45-4933-b036-b5cf86wefw',
  accountNumber: '123456',
  contactUpdatedAt: '1/1/2021',
  contactUpdatedBy: '1/1/2021',
  roleId: 'Account Admin'
};

export const userInfoWithNoName = {
  firstName: '',
  lastName: 'test',
  company: 'test',
  phone: '(222) 222-2222',
  email: 'tim+userman2@dialexa.com',
  language: 'en',
  id: 'ab4e4f52-4ea9-4a19-950c-4404b7c4bb2d',
  phoneType: PhoneType.Mobile,
  createdAt: '5/12/2021',
  branchId: '342wfergw-9d45-4933-b036-b5cf86wefw',
  accountNumber: '123456',
  contactUpdatedAt: '1/1/2021',
  contactUpdatedBy: '1/1/2021'
};

export const userForApprover = {
  firstName: 'Test1',
  lastName: 'Dev1',
  email: 'msco4523@gmail.com',
  company: 'test',
  phone: '(222) 222-2222',
  language: 'en',
  id: 'ab4e4f52-4ea9-4a19-950c-4404b7c4bb2d',
  phoneType: PhoneType.Mobile,
  createdAt: '5/12/2021',
  branchId: '342wfergw-9d45-4933-b036-b5cf86wefw',
  accountNumber: '123456',
  contactUpdatedAt: '1/1/2021',
  contactUpdatedBy: '1/1/2021',
  roleId: ''
};

export type RoleInfo = {
  id?: string;
  approverId?: string;
};

export const mockSelectedRole: RoleInfo = {
  id: '123',
  approverId: ''
};
