import { Dispatch, useContext } from 'react';

import { mockCart } from 'Cart/tests/mocks';
import { mockQuote } from 'Quote/testmock';
import { Cart } from 'generated/graphql';
import CartProvider, { useCartContext } from 'providers/CartProvider';

export function MockCartProviderComponent() {
  return (
    <CartProvider>
      <MockCartProviderTestComponent />
    </CartProvider>
  );
}

export function MockCartProviderTestComponent() {
  const context = useCartContext();
  return (
    <>
      {/* Displays */}
      <Span testid="t-cart-id" data={context.cart?.id} />
      <Span testid="t-cart-loading" data={context.cartLoading} />
      <Span testid="t-quote-co" data={context.checkingOutWithQuote} />
      <Span testid="t-contract-id" data={context.contract?.id} />
      <Span testid="t-item-count" data={context.itemCount} />
      <Span testid="t-item-loading" data={context.itemLoading} />
      <Span testid="t-line-notes" data={context.lineNotes} />
      <Span testid="t-previous-cart-id" data={context.previousCart?.id} />
      <Span testid="t-quote-po" data={context.quoteData?.customerPO} />
      <Span testid="t-quote-id" data={context.quoteId} />
      <Span testid="t-quote-shipto-id" data={context.quoteShipToId} />
      <Span testid="t-update-cart-loading" data={context.updateCartLoading} />
      {/* Buttons */}
      <Btn testid="tb-set-cart" click={setCart} />
      <Btn testid="tb-set-prev-cart" click={setPrevCart} />
      <Btn testid="tb-set-quote" click={setQuote} />
      <Btn testid="tb-set-quote-ship" click={setQuoteShip} />
      <Btn testid="tb-clear-quote" click={context.clearQuote} />
    </>
  );
  /**
   * Button actions
   */
  function setCart() {
    context.updateWillCallBranch?.(mockCart);
  }
  function setPrevCart() {
    context.setPreviousCart({ id: 'undefined' } as Cart);
  }
  function setQuote() {
    context.setQuoteId('test1234');
    context.setQuoteData(mockQuote);
  }
  function setQuoteShip() {
    context.setQuoteShipToId('test1234');
    context.setQuoteData(mockQuote);
  }
}

type SpanProps = {
  testid: string;
  data: any;
};

function Span(props: SpanProps) {
  return <span data-testid={props.testid}>{`${props.data}`}</span>;
}

type BtnProps = {
  testid: string;
  click: Dispatch<any>;
};

function Btn(props: BtnProps) {
  return <button data-testid={props.testid} onClick={props.click} />;
}
