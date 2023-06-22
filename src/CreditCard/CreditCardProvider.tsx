import { ReactNode, createContext } from 'react';

import { defaultCreditCardContext } from 'CreditCard/util/config';
import useCreditCardData from 'CreditCard/util/Provider/useCreditCardData';
import useCreditCardEffect from 'CreditCard/util/Provider/useCreditCardEffect';
import useExpiredCreditCardMemo from 'CreditCard/util/Provider/useExpiredCreditCardMemo';
import { CreditCardContextType } from 'CreditCard/util/types';

type Props = {
  children: ReactNode;
};

export const CreditCardContext = createContext<CreditCardContextType>(
  defaultCreditCardContext
);

export default function CreditCardProvider(props: Props) {
  /**
   * Hooks
   */
  const {
    resUrl,
    setResUrl,
    iframeUrl,
    setIframeUrl,
    creditCard,
    setCreditCard,
    creditCardData,
    creditCardState,
    setCreditCardState,
    getCreditCardLoading,
    setGetCreditCardLoading,
    parsingCCResponse,
    setParsingCCResponse,
    selectedCreditCard,
    setSelectedCreditCard,
    shouldSaveCreditCard,
    setShouldSaveCreditCard,
    updatingList,
    setUpdatingList,
    creditCardListLoading,
    getCreditCardSetupUrl,
    getCreditCardElementInfo,
    addCreditCardLoading,
    deleteCreditCardLoading,
    updateCartCreditCard,
    deleteCreditCard
  } = useCreditCardData();

  /**
   * Effect
   */
  const { parseCCResponse } = useCreditCardEffect({
    resUrl,
    setResUrl,
    setIframeUrl,
    setCreditCard,
    setCreditCardState,
    setGetCreditCardLoading,
    parsingCCResponse,
    setParsingCCResponse,
    getCreditCardElementInfo
  });

  /**
   * Memo
   */
  const expiredCreditCards = useExpiredCreditCardMemo({
    creditCardData,
    setUpdatingList
  });

  return (
    <CreditCardContext.Provider
      value={{
        addCreditCardLoading,
        creditCard,
        setCreditCard,
        creditCardData,
        creditCardListLoading,
        creditCardState,
        setCreditCardState,
        deleteCreditCard,
        deleteCreditCardLoading,
        expiredCreditCards,
        getCreditCardElementInfo,
        getCreditCardLoading,
        getCreditCardSetupUrl,
        iframeUrl,
        parseCCResponse,
        parsingCCResponse,
        setParsingCCResponse,
        selectedCreditCard,
        setSelectedCreditCard,
        shouldSaveCreditCard,
        setShouldSaveCreditCard,
        updateCartCreditCard,
        updatingList
      }}
    >
      {props.children}
    </CreditCardContext.Provider>
  );
}
