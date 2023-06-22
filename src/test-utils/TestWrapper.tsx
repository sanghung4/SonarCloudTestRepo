import React, { JSXElementConstructor, ReactElement, ReactNode } from 'react';

import { ThemeProvider } from '@dialexa/reece-component-library';
import { MockedProvider, MockedResponse } from '@apollo/client/testing';
import { OktaAuth } from '@okta/okta-auth-js';
import { render as jestRender, RenderResult } from '@testing-library/react';
import { I18nextProvider } from 'react-i18next';
import { MemoryRouter } from 'react-router-dom';

import { UserContextType, AuthContext } from 'AuthProvider';
import { Permission } from 'common/PermissionRequired';
import i18n from 'test-utils/mockI18N';
import {
  SelectedAccountsContext,
  SelectedAccountsContextType
} from 'providers/SelectedAccountsProvider';
import { CartContext, CartContextType } from 'providers/CartProvider';
import {
  CheckoutContext,
  CheckoutContextType,
  initialCheckoutContextValues
} from 'Checkout/CheckoutProvider';

/**
 * NOTE:
 * To setup mocks follow the examples here:
 * https://www.apollographql.com/docs/react/development-testing/testing/
 *
 * To get a successful response in your test (not just the loading state)
 * you can do the following:
 *
 * it('Should render', async () => {
 *   const { container } = renderMockRouter(<MyComponent />, mocks);
 *
 *   await waitFor(() => new Promise((res) => setTimeout(res, 0)));
 *
 *   expect(container).toMatchSnapshot();
 * });
 *
 */

/******************************/
/* Types                      */
/******************************/
type Props = {
  children?: ReactNode;
  authConfig?: Partial<UserContextType>;
  selectedAccountsConfig?: Partial<SelectedAccountsContextType>;
  cartConfig?: Partial<CartContextType>;
  checkoutConfig?: Partial<CheckoutContextType>;
  mocks?: ReadonlyArray<MockedResponse>;
};

type WrapperProps<TKey extends keyof Props> = {
  value: Props[TKey];
  children: React.ReactNode;
};

/******************************/
/* Defaults                   */
/******************************/
const defaultAuth: UserContextType = {
  authState: {
    isAuthenticated: true
  },
  isLoggingOut: false,
  oktaAuth: new OktaAuth({ issuer: 'http://test.com' }),
  profile: {
    userId: 'testuser',
    permissions: Object.values(Permission),
    isEmployee: false,
    isVerified: false
  }
};

const defaultSelectedAccounts: SelectedAccountsContextType = {
  clearAccounts: () => {},
  updateAccounts: () => {},
  updateShippingBranchId: () => {},
  loading: false,
  selectedAccounts: {
    billTo: { erpAccountId: '' },
    shipTo: { erpAccountId: '' },
    shippingBranchId: ''
  },
  isMincron: false,
  isEclipse: false
};

const defaultCartContextValue: CartContextType = {
  addItemToCart: () => {},
  addItemsToCart: () => {},
  addAllListItemsToCart: () => {},
  cart: undefined,
  cartLoading: false,
  checkingOutWithQuote: false,
  contractBranch: undefined,
  clearContract: () => {},
  clearQuote: () => {},
  deleteCartItems: () => new Promise(() => {}),
  disableAddToCart: false,
  deleteItem: () => {},
  getUserCart: () => new Promise(() => {}),
  itemCount: 0,
  itemLoading: undefined,
  lineNotes: {},
  releaseContractToCart: () => {},
  setContract: () => {},
  setLineNotes: () => {},
  setQuoteId: () => {},
  setQuoteData: () => {},
  setQuoteShipToId: () => {},
  setPreviousCart: () => {},
  setSelectedBranch: () => {},
  updateCart: undefined,
  updateDelivery: undefined,
  updateItemQuantity: () => {},
  updateWillCall: undefined,
  updateWillCallBranch: undefined,
  refreshCart: undefined,
  isWillCall: false,
  deleteCreditCardFromCart: () => new Promise(() => {})
};

/******************************/
/* Wrappers                   */
/******************************/
function SelectedAccountsProviderWrapper({
  value,
  children
}: WrapperProps<'selectedAccountsConfig'>) {
  if (value) {
    return (
      <SelectedAccountsContext.Provider
        value={{ ...defaultSelectedAccounts, ...value }}
      >
        {children}
      </SelectedAccountsContext.Provider>
    );
  }

  return <>{children}</>;
}

function CartProviderWrapper({ value, children }: WrapperProps<'cartConfig'>) {
  if (value) {
    return (
      <CartContext.Provider value={{ ...defaultCartContextValue, ...value }}>
        {children}
      </CartContext.Provider>
    );
  }

  return <>{children}</>;
}

function CheckoutProviderWrapper({
  value,
  children
}: WrapperProps<'checkoutConfig'>) {
  if (value) {
    return (
      <CheckoutContext.Provider
        value={{ ...initialCheckoutContextValues, ...value }}
      >
        {children}
      </CheckoutContext.Provider>
    );
  }

  return <>{children}</>;
}

function TestWrapper(props: Props) {
  return (
    <ThemeProvider>
      <I18nextProvider i18n={i18n}>
        <AuthContext.Provider value={{ ...defaultAuth, ...props.authConfig }}>
          <MockedProvider mocks={props.mocks ?? []}>
            <SelectedAccountsProviderWrapper
              value={props.selectedAccountsConfig}
            >
              <CartProviderWrapper value={props.cartConfig}>
                <CheckoutProviderWrapper value={props.checkoutConfig}>
                  {props.children}
                </CheckoutProviderWrapper>
              </CartProviderWrapper>
            </SelectedAccountsProviderWrapper>
          </MockedProvider>
        </AuthContext.Provider>
      </I18nextProvider>
    </ThemeProvider>
  );
}

function jestRenderFn(wrapped: JSX.Element) {
  return jestRender(wrapped, {
    wrapper: MemoryRouter as
      | JSXElementConstructor<{
          children: ReactElement<any, string | JSXElementConstructor<any>>;
        }>
      | undefined
  });
}

export function render(component: ReactNode, config?: Props) {
  const wrapped = <TestWrapper {...config}>{component}</TestWrapper>;
  return jestRenderFn(wrapped);
}
export type TestRender = RenderResult<
  typeof import('@testing-library/dom/types/queries'),
  Element | DocumentFragment
>;

export function renderWithComponent(component: ReactNode, config?: Props) {
  const wrapped = <TestWrapper {...config}>{component}</TestWrapper>;
  return {
    render: jestRenderFn(wrapped),
    component: wrapped
  };
}

export default TestWrapper;
