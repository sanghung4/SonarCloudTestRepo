import {
  SearchSuggestionDocument,
  SearchSuggestionResult
} from 'generated/graphql';
import { MockedResponse } from '@apollo/client/testing/core';
export const authConfigLoggedIn = {
  authState: {
    isAuthenticated: true
  },
  profile: {
    userId: 'test',
    permissions: [],
    isEmployee: true,
    isVerified: true
  },
  activeFeatures: ['LISTS']
};

export const authConfigLoggedOut = {
  authState: {
    isAuthenticated: false
  },
  profile: {
    userId: '',
    permissions: [],
    isEmployee: false,
    isVerified: false
  },
  activeFeatures: ['LISTS']
};

export const authConfigWithPayment = {
  ...authConfigLoggedIn,
  activeFeatures: ['MANAGE_PAYMENTS']
};

export const mockCompanyList = [
  {
    link: 'Morrison Supply Company',
    list: 'Morrison Supply',
    sub: 'morrisonsupply',
    engine: 'plumbing_hvac'
  },
  {
    link: 'DeVore & Johnson',
    list: 'DeVore & Johnson',
    sub: 'devoreandjohnson',
    engine: 'plumbing_hvac'
  },
  {
    link: 'Murray Supply',
    list: 'Murray Supply Company',
    sub: 'murraysupply',
    engine: 'plumbing_hvac'
  },
  {
    link: 'Morsco HVAC',
    list: 'Morsco HVAC Supply',
    sub: 'morscohvacsupply',
    engine: 'plumbing_hvac'
  },
  {
    link: 'Farnsworth Wholesale',
    list: 'Farnsworth Wholesale Company',
    sub: 'fwcaz',
    engine: 'plumbing_hvac'
  },
  {
    link: 'Expressions Home Gallery',
    list: 'Expressions Home Gallery',
    sub: 'expressionshomegallery',
    engine: 'bath_kitchen'
  },
  {
    link: 'Wholesale Specialties',
    list: 'Wholesale Specialties',
    sub: 'wholesalespecialties',
    engine: 'plumbing_hvac'
  }
];

export const mockSearchSuggestionQuery: MockedResponse<SearchSuggestionResult> =
  {
    request: { query: SearchSuggestionDocument },
    result: {
      data: {
        suggestions: ['suggestion1', 'suggestion2'],
        topCategories: [{ count: 1, value: '2' }],
        topProducts: [{ id: '123' }]
      }
    }
  };
