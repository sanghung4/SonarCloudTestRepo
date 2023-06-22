import { useContext, useMemo } from 'react';

import { CheckoutContext } from 'Checkout/CheckoutProvider';
import { Maybe } from 'generated/graphql';
import { format } from 'utils/currency';
import { useCartContext } from 'providers/CartProvider';

export type UseOrderSummaryProps = {
  page: string;
  showTax?: boolean;
};
export type OrderSummaryData = {
  subTotal: string;
  tax: Maybe<string>;
  orderTotal: string;
};

export function useOrderSummary({ page, showTax }: UseOrderSummaryProps) {
  /**
   * Context
   */
  const { orderData, orderPreviewData } = useContext(CheckoutContext);
  const { cart, checkingOutWithQuote, contract, quoteData } = useCartContext();

  /**
   * Memo
   */
  const summaryData = useMemo<OrderSummaryData>(() => {
    /**
     * Variables
     */
    const productCount = cart?.products?.length ?? 0;
    const isCartPage = page === 'cart';
    const isCheckoutPage = page === 'checkout';

    /**
     * Logics
     */
    if (isCartPage || (isCheckoutPage && contract && !showTax)) {
      const output = {
        subTotal: productCount ? format((cart?.subtotal ?? 0) / 100) : '—',
        tax: null,
        orderTotal: productCount ? format((cart?.subtotal ?? 0) / 100) : '—'
      };
      if (contract && (!isCheckoutPage || (isCheckoutPage && !showTax))) {
        const newTotal = !cart?.products?.length
          ? 0
          : cart.products.reduce(
              (sum, item) =>
                sum + (item?.quantity || 0) * (item?.pricePerUnit || 0),
              0
            );
        output.subTotal = productCount ? format(newTotal) : '—';
        output.orderTotal = productCount ? format(newTotal) : '—';
      }
      return output;
    }
    if (checkingOutWithQuote) {
      return {
        subTotal: format(quoteData?.subTotal ?? 0),
        tax: format(quoteData?.tax ?? 0),
        orderTotal: format(quoteData?.orderTotal ?? 0)
      };
    }
    // Regular
    return {
      subTotal: showTax
        ? isCheckoutPage
          ? format(parseFloat(orderPreviewData.subTotal || '0'))
          : format(orderData.subTotal ?? 0)
        : format((cart?.subtotal ?? 0) / 100),
      tax: isCheckoutPage
        ? format(parseFloat(orderPreviewData.tax || '0'))
        : format(orderData.tax ?? 0),
      orderTotal: showTax
        ? isCheckoutPage
          ? format(parseFloat(orderPreviewData.orderTotal || '0'))
          : format(orderData.orderTotal ?? 0)
        : format((cart?.subtotal ?? 0) / 100)
    };
  }, [
    cart,
    checkingOutWithQuote,
    contract,
    page,
    quoteData,
    orderData,
    orderPreviewData,
    showTax
  ]);

  return summaryData;
}
