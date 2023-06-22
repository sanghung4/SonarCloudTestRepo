import { CreditCardState } from 'CreditCard/util/config';
import { CreditCardContextType } from 'CreditCard/util/types';

export const mockCreditCartContext: CreditCardContextType = {
  setCreditCard: jest.fn(),
  creditCardState: CreditCardState.NONE,
  setCreditCardState: jest.fn(),
  deleteCreditCard: jest.fn(),
  expiredCreditCards: {},
  getCreditCardElementInfo: jest.fn(),
  getCreditCardLoading: false,
  getCreditCardSetupUrl: jest.fn(),
  parseCCResponse: jest.fn(),
  parsingCCResponse: false,
  setParsingCCResponse: jest.fn(),
  selectedCreditCard: '',
  setSelectedCreditCard: jest.fn(),
  shouldSaveCreditCard: false,
  setShouldSaveCreditCard: jest.fn(),
  updateCartCreditCard: jest.fn(),
  updatingList: false
};
