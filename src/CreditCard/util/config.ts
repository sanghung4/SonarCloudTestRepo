import { CreditCardContextType } from 'CreditCard/util/types';
import { noop } from 'lodash-es';

export enum CreditCardState {
  NONE,
  ADD,
  CHANGE,
  DELETE,
  SELECTED
}

export const defaultCreditCardContext: CreditCardContextType = {
  setCreditCard: noop,
  creditCardState: CreditCardState.NONE,
  setCreditCardState: noop,
  deleteCreditCard: noop,
  expiredCreditCards: {},
  getCreditCardLoading: false,
  parseCCResponse: noop,
  parsingCCResponse: false,
  setParsingCCResponse: noop,
  selectedCreditCard: '',
  setSelectedCreditCard: noop,
  shouldSaveCreditCard: false,
  setShouldSaveCreditCard: noop,
  updateCartCreditCard: noop,
  updatingList: false
};
