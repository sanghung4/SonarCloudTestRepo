import { MockedResponse } from '@apollo/client/testing/core';
import { noop } from 'lodash-es';

import {
  Branch,
  GetHomeBranchDocument,
  GetHomeBranchQuery,
  GetHomeBranchQueryVariables,
  SendContactFormDocument
} from 'generated/graphql';
import * as t from 'locales/en/translation.json';

const homeBranchVariables: GetHomeBranchQueryVariables = {
  shipToAccountId: '4875f301-5e0a-4af0-b8ce-03ccae8495cf'
};
const homeBranchData: GetHomeBranchQuery = {
  homeBranch: {
    branchId: '6051',
    name: 'Fortiline Waterworks',
    entityId: '51',
    address1: '1628 Barton Chapel Road',
    address2: null,
    city: 'Augusta',
    state: 'Georgia',
    zip: '30909',
    phone: '(703) 737-3705',
    longitude: -82.087268,
    latitude: 33.4617053
  }
};

const homeBranchDataEmpty: GetHomeBranchQuery = {
  homeBranch: {
    branchId: '',
    name: null,
    entityId: null,
    address1: null,
    address2: null,
    city: null,
    state: null,
    zip: null,
    phone: null,
    longitude: 0,
    latitude: 0
  }
};

export const HomeBranchCardMocks = [
  {
    request: {
      query: GetHomeBranchDocument,
      variables: homeBranchVariables
    },
    result: {
      data: homeBranchData,
      loading: false
    }
  },
  {
    request: {
      query: GetHomeBranchDocument,
      variables: homeBranchVariables
    },
    result: {
      data: homeBranchDataEmpty,
      loading: false
    }
  }
];

export const branchContextMock = {
  homeBranch: {
    branchId: '6051',
    businessHours: 'M-F: 7:00am - 5:00pm;SAT: 9:00am 1:00 PM',
    isBandK: false,
    isHvac: false,
    isPlumbing: false,
    isWaterworks: true,
    longitude: -82.087268,
    latitude: 33.4617053,
    name: 'Fortiline Waterworks',
    address1: '1628 Barton Chapel Road',
    address2: null,
    city: 'Augusta',
    state: 'Georgia',
    zip: '30909',
    phone: '(703) 737-3705'
  } as Branch,
  homeBranchLoading: false,
  homeBranchError: '',
  shippingBranch: {} as Branch,
  shippingBranchLoading: false,
  nearbyBranches: [],
  nearbyBranchesLoading: false,
  branchSelectOpen: false,
  setBranchSelectOpen: noop,
  stock: {},
  setStock: noop,
  setShippingBranch: noop
};

export type MockSupportVar = {
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  zip: string;
  topic: string;
  message: string;
};

export const SUPPORT_VAR_SUCCCESS: MockSupportVar = {
  firstName: 'Test',
  lastName: 'Jobs',
  email: 'test@email.com',
  phoneNumber: '(406) 555-1234',
  zip: '77494',
  topic: t.support.topicOther,
  message: "I'm Mr Meeseeks look at me!"
};

export const SUPPORT_VAR_FAIL: MockSupportVar = {
  firstName: 'Fail',
  lastName: 'Fail',
  email: 'fail@email.com',
  phoneNumber: '(123) 456-7890',
  zip: '11111',
  topic: t.support.topicOther,
  message: "I'm fail"
};

export const mockSupportSuccess: MockedResponse = {
  request: {
    query: SendContactFormDocument,
    variables: { contactFormInput: SUPPORT_VAR_SUCCCESS }
  },
  result: {
    data: { sendContactForm: 'Email sent successfully!' }
  }
};

export const mockSupportFail = {
  request: {
    query: SendContactFormDocument,
    variables: { contactFormInput: SUPPORT_VAR_FAIL }
  },
  result: {
    data: { sendContactForm: 'Fail' }
  }
};
