import { Dispatch, useContext } from 'react';

import { ApolloError } from '@apollo/client';
import { useSnackbar } from '@dialexa/reece-component-library';
import { map, omit } from 'lodash-es';
import { useTranslation } from 'react-i18next';

import { CreditCardState } from 'CreditCard/util/config';
import { useAddCreditCard } from 'CreditCard/util/Provider/useAddCreditCard';
import {
  CreditCard,
  CreditCardElementInfoQuery,
  CreditCardListQuery,
  CreditCardSetupUrlQuery,
  DeleteCreditCardMutation
} from 'generated/graphql';
import { formatDate } from 'utils/dates';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import { CartContext } from 'providers/CartProvider';

export type UseCreditCardFnProps = {
  creditCardData?: CreditCard[];
  setCreditCard: Dispatch<CreditCard | undefined>;
  setCreditCardData: Dispatch<CreditCard[] | undefined>;
  setCreditCardState: Dispatch<CreditCardState>;
  setGetCreditCardLoading: Dispatch<boolean>;
  setIframeUrl: Dispatch<string | undefined>;
  setParsingCCResponse: Dispatch<boolean>;
  setResUrl: Dispatch<string | undefined>;
  setShouldSaveCreditCard: Dispatch<boolean>;
  setUpdatingList: Dispatch<boolean>;
  shouldSaveCreditCard: boolean;
  updateCartCreditCard: (arg?: CreditCard) => void;
};

export default function useCreditCardFn(props: UseCreditCardFnProps) {
  /**
   * Props
   */
  const {
    creditCardData,
    setCreditCard,
    setCreditCardData,
    setCreditCardState,
    setGetCreditCardLoading,
    setIframeUrl,
    setParsingCCResponse,
    setResUrl,
    setShouldSaveCreditCard,
    setUpdatingList,
    shouldSaveCreditCard,
    updateCartCreditCard
  } = props;

  /**
   * Custom Hooks
   */
  const { selectedAccounts } = useSelectedAccountsContext();
  const { pushAlert } = useSnackbar();
  const { t } = useTranslation();

  /**
   * Context
   */
  const { cart, updateCart, deleteCreditCardFromCart } =
    useContext(CartContext);

  /**
   * Add Credit Card Data Hook
   */
  const { addCreditCardLoading, addCreditCardMutation } = useAddCreditCard({
    setCreditCardData,
    setUpdatingList
  });

  /**
   * Functions
   */
  function deleteComplete(res: DeleteCreditCardMutation) {
    pushAlert(t('creditCard.cardDeleted'), { variant: 'success' });
    const newData = creditCardData?.filter(
      (cc) => cc.elementPaymentAccountId !== res.deleteCreditCard
    );
    setCreditCardData(newData);
    setUpdatingList(false);

    if (
      !cart ||
      cart?.creditCard?.elementPaymentAccountId !== res.deleteCreditCard
    ) {
      return;
    }
    if (newData?.length) {
      const creditCard = {
        cardHolder: newData[0].cardHolder,
        creditCardNumber: newData[0].creditCardNumber,
        creditCardType: newData[0].creditCardType,
        elementPaymentAccountId: newData[0].elementPaymentAccountId,
        expirationDate: { date: newData[0].expirationDate.date },
        postalCode: newData[0].postalCode,
        streetAddress: newData[0].streetAddress
      };
      updateCart?.(cart.id, { creditCard });
      return;
    }
    deleteCreditCardFromCart(cart.id);
  }
  function deleteError(err: ApolloError) {
    const is409 = map(err.graphQLErrors, 'extensions.code').indexOf(409) !== -1;
    const translationKey = is409 ? 'cardInUse' : 'errorDelete';
    pushAlert(t(`creditCard.${translationKey}`), { variant: 'error' });
    setUpdatingList(false);
  }
  function elementInfoComplete(creditCardRes: CreditCardElementInfoQuery) {
    const { creditCard } = creditCardRes.creditCardElementInfo;
    setResUrl(undefined);
    setParsingCCResponse(false);
    setGetCreditCardLoading(false);

    if (window.location.pathname === '/checkout') {
      setCreditCard(creditCard);
      updateCartCreditCard(creditCard);
      setCreditCardState(CreditCardState.SELECTED);
    } else {
      setCreditCardState(CreditCardState.NONE);
    }

    if (shouldSaveCreditCard) {
      setShouldSaveCreditCard(false);
      setUpdatingList(true);

      const creditCardToSave = omit(creditCard, '__typename');
      const date = formatDate(
        creditCardToSave.expirationDate.date,
        'MM/dd/yyyy'
      );
      const variables = {
        accountId: selectedAccounts.billTo?.erpAccountId ?? '',
        creditCard: { ...creditCardToSave, expirationDate: { date } }
      };

      addCreditCardMutation({ variables });
    } else {
      setUpdatingList(true);
      setCreditCard(creditCard);
      updateCartCreditCard(creditCard);
      setCreditCardData([...(creditCardData ?? []), creditCard]);
      setUpdatingList(false);
    }
  }
  function listComplete(data: CreditCardListQuery) {
    const { ADD, CHANGE } = CreditCardState;
    const creditCards = data.creditCardList?.creditCardList?.creditCard;
    setCreditCardData(creditCards ?? []);
    setCreditCardState(!creditCards?.length ? ADD : CHANGE);
  }
  function setupUrlComplete(setupUrlRes: CreditCardSetupUrlQuery) {
    if (setupUrlRes?.creditCardSetupUrl?.elementSetupUrl) {
      setIframeUrl(setupUrlRes.creditCardSetupUrl.elementSetupUrl);
      window.addEventListener(
        'message',
        windowMessageEvent(setIframeUrl, setResUrl),
        false
      );
    }
  }

  /**
   * Output
   */
  return {
    addCreditCardLoading,
    deleteComplete,
    deleteError,
    elementInfoComplete,
    listComplete,
    setupUrlComplete
  };
}

/**
 * Misc
 */
export function windowMessageEvent(
  setIframeUrl: Dispatch<string | undefined>,
  setResUrl: Dispatch<string | undefined>
) {
  return (event: MessageEvent<any>) => {
    if (
      event.origin === window.location.origin &&
      typeof event.data === 'string' &&
      event.data.indexOf(`${window.location.origin}/credit_callback`) === 0
    ) {
      setResUrl(event.data);
      setIframeUrl(undefined);
    }
  };
}
