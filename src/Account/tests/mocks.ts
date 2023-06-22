import { MockedResponse } from '@apollo/client/testing/core';
import {
  AccountDetailsDocument,
  ErpAccount,
  ResendLegacyInviteEmailDocument,
  UpdateUserPasswordDocument,
  UserDocument
} from 'generated/graphql';
import { PhoneType } from 'generated/graphql';

export const mockUser = {
  id: '11111111-1111-1111-1111-111111111111',
  firstName: 'John',
  lastName: 'Doe',
  phoneNumber: '(111) 111-1111',
  phoneTypeId: 'bcdb73c5-9d45-4933-b036-b5cf869a75ba',
  email: 'john.doe@morsco.com',
  role: 'Morsco Admin'
};

export const mockBlankUserData = {
  id: '',
  firstName: '',
  lastName: '',
  phoneNumber: '',
  phoneTypeId: 'bcdb73c5-9d45-4933-b036-b5cf869a75ba',
  email: '',
  role: ''
};

export const mocks = [
  {
    request: {
      query: UserDocument,
      variables: {
        userId: '11111111-1111-1111-1111-111111111111'
      }
    },
    result: {
      data: {
        user: {
          id: '11111111-1111-1111-1111-111111111111',
          email: 'john.doe@morsco.com',
          firstName: 'Morsco',
          lastName: 'EmployeeDev',
          phoneNumber: '(111) 111-1111',
          role: {
            id: 'd59d29fd-5ae1-46d4-8739-bb06440bc685',
            name: 'Morsco Admin',
            description: ''
          },
          phoneType: {
            id: 'bcdb73c5-9d45-4933-b036-b5cf869a75ba',
            name: 'MOBILE'
          }
        }
      }
    }
  },
  {
    request: {
      query: AccountDetailsDocument,
      variables: {
        accountId: '22222222-2222-2222-2222-222222222222'
      }
    },
    result: {
      data: {
        account: [
          {
            companyName: 'HORIZON PLUMBING LTD',
            street1: '2706 W PIONEER PKWY',
            street2: '',
            city: 'ARLINGTON',
            state: 'TX',
            zip: '76013-5906'
          }
        ]
      }
    }
  }
];

export const migrationMocks = [
  {
    request: {
      query: ResendLegacyInviteEmailDocument,
      variables: {
        legacyUserEmail: 'test@email.com'
      }
    },
    result: {
      data: {
        resendLegacyInviteEmail: 'test'
      }
    }
  }
];

export const mockErpAccount: ErpAccount = {
  branchId: '123',
  city: 'Billings',
  companyName: 'Big Sky Waterworks',
  creditHold: false,
  email: ['bigsky@mt.com'],
  erpAccountId: '123-234t4gr334-3yghrhht-123',
  phoneNumber: '(406) 555-1234',
  state: 'MT',
  street1: '123 S 24th St',
  street2: 'Suite 43',
  zip: '59102'
};

export const mockUserwithMultiAccounts = {
  request: {
    query: UserDocument,
    variables: {
      userId: '11111111-1111-1111-1111-111111111111'
    }
  },
  result: {
    data: {
      user: {
        id: '11111111-1111-1111-1111-111111111111',
        email: 'john.doe@morsco.com',
        firstName: 'Morsco',
        lastName: 'EmployeeDev',
        phoneNumber: '(111) 111-1111',
        role: {
          id: 'd59d29fd-5ae1-46d4-8739-bb06440bc685',
          name: 'Morsco Admin',
          description: ''
        },
        phoneType: {
          id: 'bcdb73c5-9d45-4933-b036-b5cf869a75ba',
          name: 'MOBILE'
        }
      }
    },
    loading: false
  }
};

export const mockAccountDetails = {
  request: {
    query: AccountDetailsDocument,
    variables: {
      accountId: '22222222-2222-2222-2222-222222222222',
      brand: 'Reece'
    }
  },
  result: {
    data: {
      account: [
        {
          companyName: 'TEXAS MILITARY DEPARTMENT SHOP',
          street1: '2200 W 35TH ST BLDG 11 2ND FLR',
          street2: '',
          city: 'AUSTIN',
          state: 'TX',
          zip: '78703-1222',
          erpName: 'ECLIPSE'
        },
        {
          companyName: 'HORIZON PLUMBING LTD',
          street1: '2706 W PIONEER PKWY',
          street2: '',
          city: 'ARLINGTON',
          state: 'TX',
          zip: '76013-5906',
          erpName: 'MINCRON'
        }
      ]
    },
    loading: false
  }
};

export const mockUserInfo = {
  firstName: 'test',
  lastName: 'test',
  company: 'test',
  phone: '(222) 222-2222',
  email: 'tim+userman2@dialexa.com',
  language: 'en',
  id: 'testuser',
  phoneType: PhoneType.Mobile,
  createdAt: '5/12/2021',
  branchId: '342wfergw-9d45-4933-b036-b5cf86wefw',
  accountNumber: '123456',
  contactUpdatedAt: '1/1/2021',
  contactUpdatedBy: '1/1/2021'
};

export type MockUpdatePassword = {
  oldUserPassword: string;
  newUserPassword: string;
  userId: string;
};

export const UPDATE_PASSWORD_VAR_FAIL: MockUpdatePassword = {
  oldUserPassword: 'FailPassword1',
  newUserPassword: 'FailNewPassword1',
  userId: 'testuser'
};

export const UPDATE_PASSWORD_VAR_SUCCESS: MockUpdatePassword = {
  userId: 'testuser',
  oldUserPassword: 'Password1',
  newUserPassword: 'NewPassword1'
};

export const mockUpdatePasswordSuccess: MockedResponse = {
  request: {
    query: UpdateUserPasswordDocument,
    variables: { updateUserPasswordInput: UPDATE_PASSWORD_VAR_SUCCESS }
  },
  result: {
    data: { updateUserPassword: true }
  }
};

export const mockUpdatePasswordFail: MockedResponse = {
  request: {
    query: UpdateUserPasswordDocument,
    variables: { updateUserPasswordInput: UPDATE_PASSWORD_VAR_FAIL }
  },
  error: new Error('Update Password Failed')
};

export const mockUpdatePasswordOktaFail: MockedResponse = {
  request: {
    query: UpdateUserPasswordDocument,
    variables: { updateUserPasswordInput: UPDATE_PASSWORD_VAR_FAIL }
  },
  error: new Error(
    'HTTP 403, Okta E0000014 (Update of credentials failed - Old Password is not correct), ErrorId oae21F8mxl-T2-HX1eQWQSGFw'
  )
};
