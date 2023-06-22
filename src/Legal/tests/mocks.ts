import { MockedResponse } from '@apollo/client/testing/core';
import { SendContactFormDocument } from 'generated/graphql';
import * as t from 'locales/en/translation.json';

export type MockCCPAVar = {
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  zip: string;
  topic: string;
  message: string;
};

export const CCPA_VAR_SUCCCESS: MockCCPAVar = {
  firstName: 'Test',
  lastName: 'Jobs',
  email: 'test@email.com',
  phoneNumber: '(406) 555-1234',
  zip: '77494',
  topic: t.legal.ccpaRequestTypeOptOut,
  message: "I'm Mr Meeseeks look at me!"
};

export const CCPA_VAR_FAIL: MockCCPAVar = {
  firstName: 'Fail',
  lastName: 'Fail',
  email: 'fail@email.com',
  phoneNumber: '(123) 456-7890',
  zip: '11111',
  topic: t.legal.ccpaRequestTypeOptOut,
  message: "I'm fail"
};

export const mockCCPASuccess: MockedResponse = {
  request: {
    query: SendContactFormDocument,
    variables: { contactFormInput: CCPA_VAR_SUCCCESS }
  },
  result: {
    data: { sendContactForm: 'Email sent successfully!' }
  }
};

export const mockCCPAFail = {
  request: {
    query: SendContactFormDocument,
    variables: { contactFormInput: CCPA_VAR_FAIL }
  },
  result: {
    data: { sendContactForm: 'Fail' }
  }
};
