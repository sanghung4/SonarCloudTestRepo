import { Dispatch } from 'react';

import { QueryLazyOptions } from '@apollo/client';

import { CreditCardState } from 'CreditCard/util/config';
import { CardHolderInput, CreditCard, Exact } from 'generated/graphql';

export type ExpiredCreditCardsType = { [key: string]: boolean };

export type CreditCardContextType = {
  addCreditCardLoading?: boolean;
  creditCard?: CreditCard;
  setCreditCard: Dispatch<CreditCard | undefined>;
  creditCardData?: CreditCard[];
  creditCardListLoading?: boolean;
  creditCardState: CreditCardState;
  setCreditCardState: Dispatch<CreditCardState>;
  deleteCreditCard: (accountId: string, creditCardId: string) => void;
  deleteCreditCardLoading?: boolean;
  expiredCreditCards: ExpiredCreditCardsType;
  getCreditCardElementInfo?: (
    options?:
      | QueryLazyOptions<
          Exact<{
            accountId: string;
            elementSetupId: string;
          }>
        >
      | undefined
  ) => void;
  getCreditCardLoading: boolean;
  getCreditCardSetupUrl?: (
    options?:
      | QueryLazyOptions<
          Exact<{
            accountId: string;
            cardHolderInput: CardHolderInput;
          }>
        >
      | undefined
  ) => void;
  iframeUrl?: string;
  parseCCResponse: () => void;
  parsingCCResponse: boolean;
  setParsingCCResponse: Dispatch<boolean>;
  selectedCreditCard: string;
  setSelectedCreditCard: Dispatch<string>;
  shouldSaveCreditCard: boolean;
  setShouldSaveCreditCard: Dispatch<boolean>;
  updateCartCreditCard: (creditCard: CreditCard | undefined) => void;
  updatingList: boolean;
};
