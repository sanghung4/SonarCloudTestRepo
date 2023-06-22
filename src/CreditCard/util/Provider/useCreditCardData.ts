import { useCheckoutContext } from './../../../Checkout/CheckoutProvider';
import { useContext, useState } from 'react';

import { omit } from 'lodash-es';

import {
  CreditCardState,
  defaultCreditCardContext
} from 'CreditCard/util/config';
import useCreditCardFn from 'CreditCard/util/Provider/useCreditCardFn';
import {
  CreditCard,
  useCreditCardElementInfoLazyQuery,
  useCreditCardListQuery,
  useCreditCardSetupUrlLazyQuery,
  useDeleteCreditCardMutation
} from 'generated/graphql';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import { Permission, checkUserPermission } from 'common/PermissionRequired';
import { AuthContext } from 'AuthProvider';

export default function useCreditCardData() {
  /**
   * Custom Hooks
   */
  const { selectedAccounts } = useSelectedAccountsContext();
  const { profile } = useContext(AuthContext);
  const accountId = selectedAccounts.billTo?.erpAccountId ?? '';

  /**
   * Context
   */
  const { paymentData, setPaymentData, updateCart } = useCheckoutContext();
  const canManagePaymentMethods = checkUserPermission(profile, [
    Permission.MANAGE_PAYMENT_METHODS
  ]);

  /**
   * State
   */
  const [resUrl, setResUrl] = useState<string>();
  const [iframeUrl, setIframeUrl] = useState<string>();
  const [creditCard, setCreditCard] = useState<CreditCard>();
  const [creditCardData, setCreditCardData] = useState<CreditCard[]>();
  const [creditCardState, setCreditCardState] = useState<CreditCardState>(
    defaultCreditCardContext.creditCardState
  );
  const [getCreditCardLoading, setGetCreditCardLoading] = useState(false);
  const [parsingCCResponse, setParsingCCResponse] = useState(false);
  const [selectedCreditCard, setSelectedCreditCard] = useState(
    defaultCreditCardContext.selectedCreditCard
  );
  const [shouldSaveCreditCard, setShouldSaveCreditCard] = useState(false);
  const [updatingList, setUpdatingList] = useState(false);

  /**
   * Functions
   */
  const {
    addCreditCardLoading,
    deleteComplete,
    deleteError,
    elementInfoComplete,
    listComplete,
    setupUrlComplete
  } = useCreditCardFn({
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
  });

  /**
   * GraphQL
   */
  const { loading: creditCardListLoading } = useCreditCardListQuery({
    skip: !selectedAccounts.billTo?.erpAccountId || !canManagePaymentMethods,
    variables: { accountId },
    onCompleted: listComplete
  });
  const [getCreditCardSetupUrl] = useCreditCardSetupUrlLazyQuery({
    fetchPolicy: 'no-cache',
    onCompleted: setupUrlComplete
  });
  const [getCreditCardElementInfo] = useCreditCardElementInfoLazyQuery({
    onCompleted: elementInfoComplete
  });
  const [deleteCreditCardMutation, { loading: deleteCreditCardLoading }] =
    useDeleteCreditCardMutation({
      refetchQueries: ['CreditCardList'],
      onCompleted: deleteComplete,
      onError: deleteError
    });

  /**
   * Defs
   */
  function updateCartCreditCard(creditCard?: CreditCard) {
    const newPaymentData = { ...paymentData };
    if (creditCard) {
      newPaymentData.creditCard = omit(creditCard, '__typename');
    }
    setPaymentData(newPaymentData);
    updateCart(newPaymentData);
  }

  /**
   * Function defs
   */
  async function deleteCreditCard(accountId: string, creditCardId: string) {
    setUpdatingList(true);

    await deleteCreditCardMutation({
      variables: {
        accountId,
        creditCardId
      }
    });
  }

  /**
   * Outout
   */
  return {
    resUrl,
    setResUrl,
    iframeUrl,
    setIframeUrl,
    creditCard,
    setCreditCard,
    creditCardData,
    setCreditCardData,
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
    deleteCreditCardMutation,
    deleteCreditCardLoading,
    updateCartCreditCard,
    deleteCreditCard
  };
}
