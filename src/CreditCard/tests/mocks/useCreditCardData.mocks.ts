import { mockCreditCard } from 'Checkout/tests/mocks/creditCart.mocks';
import { CreditCardState } from 'CreditCard/util/config';
import useCreditCardData from 'CreditCard/util/Provider/useCreditCardData';
import { UseCreditCardEffectProps } from 'CreditCard/util/Provider/useCreditCardEffect';
import useCreditCardFn, {
  UseCreditCardFnProps
} from 'CreditCard/util/Provider/useCreditCardFn';
import {
  AddCreditCardMutation,
  CreditCardElementInfoQuery,
  CreditCardListQuery,
  CreditCardSetupUrlQuery,
  DeleteCreditCardMutation
} from 'generated/graphql';

export const mockCreditCardData: ReturnType<typeof useCreditCardData> = {
  resUrl: undefined,
  setResUrl: jest.fn(),
  iframeUrl: undefined,
  setIframeUrl: jest.fn(),
  creditCard: undefined,
  setCreditCard: jest.fn(),
  creditCardData: undefined,
  setCreditCardData: jest.fn(),
  creditCardState: CreditCardState.NONE,
  setCreditCardState: jest.fn(),
  getCreditCardLoading: false,
  setGetCreditCardLoading: jest.fn(),
  parsingCCResponse: false,
  setParsingCCResponse: jest.fn(),
  selectedCreditCard: '',
  setSelectedCreditCard: jest.fn(),
  shouldSaveCreditCard: false,
  setShouldSaveCreditCard: jest.fn(),
  updatingList: false,
  setUpdatingList: jest.fn(),
  creditCardListLoading: false,
  getCreditCardSetupUrl: jest.fn(),
  getCreditCardElementInfo: jest.fn(),
  addCreditCardLoading: false,
  deleteCreditCardMutation: jest.fn(),
  deleteCreditCardLoading: false,
  updateCartCreditCard: jest.fn(),
  deleteCreditCard: jest.fn()
};

export const mockCreditCardFn: ReturnType<typeof useCreditCardFn> = {
  addCreditCardLoading: false,
  deleteComplete: jest.fn(),
  deleteError: jest.fn(),
  elementInfoComplete: jest.fn(),
  listComplete: jest.fn(),
  setupUrlComplete: jest.fn()
};

export const mockCreditCardEffectProps: UseCreditCardEffectProps = {
  getCreditCardElementInfo: jest.fn(),
  parsingCCResponse: false,
  setCreditCard: jest.fn(),
  setCreditCardState: jest.fn(),
  setGetCreditCardLoading: jest.fn(),
  setIframeUrl: jest.fn(),
  setParsingCCResponse: jest.fn(),
  setResUrl: jest.fn()
};

export const mockCreditCardFnProps: UseCreditCardFnProps = {
  setCreditCard: jest.fn(),
  setCreditCardState: jest.fn(),
  setGetCreditCardLoading: jest.fn(),
  setIframeUrl: jest.fn(),
  setParsingCCResponse: jest.fn(),
  setResUrl: jest.fn(),
  setCreditCardData: jest.fn(),
  setShouldSaveCreditCard: jest.fn(),
  setUpdatingList: jest.fn(),
  shouldSaveCreditCard: false,
  updateCartCreditCard: jest.fn()
};

export const mockAddCreditCardMutation: AddCreditCardMutation = {
  addCreditCard: {
    __typename: 'CreditCardResponse',
    statusResult: { success: '', description: '' },
    creditCardList: {}
  }
};
export const mockAddCreditCardMutationWithCreditCard = {
  addCreditCard: {
    ...mockAddCreditCardMutation.addCreditCard,
    creditCardList: { creditCard: [{ ...mockCreditCard }] }
  }
};

export const mockDeleteCreditCardMutation: DeleteCreditCardMutation = {
  deleteCreditCard: 'test123'
};

export const mockCreditCardElementInfoQuery: CreditCardElementInfoQuery = {
  creditCardElementInfo: {
    __typename: 'CreditCardElementInfoResponse',
    creditCard: { ...mockCreditCard }
  }
};

export const mockCreditCardListQuery: CreditCardListQuery = {
  creditCardList: { creditCardList: { creditCard: [{ ...mockCreditCard }] } }
};
export const mockCreditCardSetupUrlQuery: CreditCardSetupUrlQuery = {
  creditCardSetupUrl: {
    __typename: 'CreditCardSetupUrl',
    elementSetupUrl: '',
    elementSetupId: ''
  }
};
