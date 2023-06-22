import { Dispatch, useEffect } from 'react';

import { useSnackbar } from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { useCheckoutContext } from 'Checkout/CheckoutProvider';
import { CreditCardState } from 'CreditCard/util/config';
import {
  CreditCard,
  useCreditCardElementInfoLazyQuery
} from 'generated/graphql';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';

type GetCreditCardElementInfo = ReturnType<
  typeof useCreditCardElementInfoLazyQuery
>[0];

export type UseCreditCardEffectProps = {
  getCreditCardElementInfo: GetCreditCardElementInfo;
  parsingCCResponse: boolean;
  resUrl?: string;
  setCreditCard: Dispatch<CreditCard | undefined>;
  setCreditCardState: Dispatch<CreditCardState>;
  setGetCreditCardLoading: Dispatch<boolean>;
  setIframeUrl: Dispatch<string | undefined>;
  setParsingCCResponse: Dispatch<boolean>;
  setResUrl: Dispatch<string | undefined>;
};

export default function useCreditCardEffect(props: UseCreditCardEffectProps) {
  /**
   * Custom Hooks
   */
  const { selectedAccounts } = useSelectedAccountsContext();
  const { pushAlert } = useSnackbar();
  const { t } = useTranslation();
  const accountId = selectedAccounts.billTo?.erpAccountId ?? '';

  /**
   * Context
   */
  const { paymentData, isCreditSelectionChanging } = useCheckoutContext();

  /**
   * Effect
   */
  useEffect(parseCCResponse, [accountId, props, pushAlert, t]);
  // eslint-disable-next-line react-hooks/exhaustive-deps
  useEffect(setCreditCardFromCart, [paymentData.creditCard, props]);

  /**
   * Effect defs
   */
  function parseCCResponse() {
    const {
      getCreditCardElementInfo,
      parsingCCResponse,
      resUrl,
      setCreditCard,
      setCreditCardState,
      setGetCreditCardLoading,
      setIframeUrl,
      setParsingCCResponse,
      setResUrl
    } = props;

    if (resUrl && !parsingCCResponse) {
      setParsingCCResponse(true);

      const parsedResUrl = new URL(resUrl);
      const resParams = new URLSearchParams(parsedResUrl.search);

      const hasStatus = resParams.has('HostedPaymentStatus');
      const isNotCancelled =
        resParams.get('HostedPaymentStatus') !== 'Cancelled';

      if (
        hasStatus &&
        isNotCancelled &&
        resParams.has('ExpressResponseCode') &&
        resParams.get('ExpressResponseCode') === '0'
      ) {
        setGetCreditCardLoading(true);
        getCreditCardElementInfo({
          variables: {
            accountId: accountId,
            elementSetupId: resParams.get('TransactionSetupID') ?? ''
          }
        });
      } else {
        setResUrl(undefined);
        setCreditCard(undefined);
        setCreditCardState(CreditCardState.ADD);
        setParsingCCResponse(false);
        if (!hasStatus || (hasStatus && isNotCancelled)) {
          pushAlert(t('creditCard.errorProcessing'), { variant: 'error' });
        }
      }
      setIframeUrl(undefined);
    }
  }

  function setCreditCardFromCart() {
    const { setCreditCard, setCreditCardState } = props;
    if (paymentData.creditCard && !isCreditSelectionChanging) {
      setCreditCard(paymentData.creditCard);
      setCreditCardState(CreditCardState.SELECTED);
    }
  }

  /**
   * Output
   */
  return { parseCCResponse };
}
