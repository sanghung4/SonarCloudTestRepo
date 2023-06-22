import {
  ReactNode,
  createContext,
  useState,
  useEffect,
  Dispatch,
  useContext
} from 'react';

import {
  DeliveryData,
  PaymentData,
  Step,
  WillCallData
} from 'Checkout/util/types';
import {
  AddressInput,
  Branch,
  Cart,
  CartInput,
  CreditCardInput,
  Delivery,
  DeliveryInput,
  DeliveryMethodEnum,
  LineItem,
  OrderPreviewResponse,
  OrderResponse,
  WillCall,
  WillCallInput
} from 'generated/graphql';
import { useCartContext, CartContractType } from 'providers/CartProvider';
import { omit, pick } from 'lodash-es';
import {
  defaultBranchData,
  defaultOrderData,
  defaultOrderPreviewData,
  defaultPaymentData
} from './util';
import { useHistory, useLocation } from 'react-router-dom';

/**
 * Types
 */
export type CheckoutContextType = {
  deliveryData: DeliveryData;
  deliveryMethod: DeliveryMethodEnum;
  deliveryMethodObject?: Delivery | WillCall;
  disableContinue: boolean;
  setDisableContinue: Dispatch<boolean>;
  orderData: OrderResponse;
  orderPreviewData: OrderPreviewResponse;
  paymentData: PaymentData;
  poNumberError: boolean;
  setDeliveryData: Dispatch<DeliveryData>;
  setOrderData: Dispatch<OrderResponse>;
  setOrderPreviewData: Dispatch<OrderPreviewResponse>;
  setPaymentData: Dispatch<PaymentData>;
  setPoNumberError: Dispatch<boolean>;
  setShipToBranch: Dispatch<Branch>;
  setStep: Dispatch<Step>;
  setMnCronShoppingCartId: Dispatch<string>;
  setWillCallData: Dispatch<WillCallData>;
  shipToBranch: Branch;
  step: Step;
  willCallData: WillCallData;
  updateCart: (newPaymentData: PaymentData) => Promise<void>;
  updateDelivery: (deliveryData: DeliveryData) => Promise<void>;
  updateWillCall: (willCallData: WillCallData) => Promise<void>;
  tempCartItems: LineItem[];
  setTempCartItems: Dispatch<LineItem[]>;
  mnCronShoppingCartId: string;
  orderedCart?: Cart;
  setOrderedCart: Dispatch<Cart | undefined>;
  orderedContract?: CartContractType;
  setOrderedContract: Dispatch<CartContractType | undefined>;
  orderedLineNotes: Record<string, string>;
  setOrderedLineNotes: Dispatch<Record<string, string>>;
  isCreditSelectionChanging: boolean;
  setIsCreditSelectionChanging: Dispatch<boolean>;
  isWillCall: boolean;
  isDelivery: boolean;
};

/**
 * Context
 */
export const initialCheckoutContextValues: CheckoutContextType = {
  deliveryData: {},
  deliveryMethod: DeliveryMethodEnum.Delivery,
  disableContinue: false,
  setDisableContinue: () => {},
  orderData: defaultOrderData,
  orderPreviewData: defaultOrderPreviewData,
  paymentData: defaultPaymentData,
  poNumberError: true,
  shipToBranch: defaultBranchData,
  mnCronShoppingCartId: '',
  step: Step.INFO,
  setDeliveryData: () => {},
  setOrderData: () => {},
  setOrderPreviewData: () => {},
  setPaymentData: () => {},
  setPoNumberError: () => {},
  setShipToBranch: () => {},
  setStep: () => {},
  setWillCallData: () => {},
  setMnCronShoppingCartId: () => {},
  willCallData: {},
  deliveryMethodObject: undefined,
  updateCart: () => new Promise(() => {}),
  updateDelivery: () => new Promise(() => {}),
  updateWillCall: () => new Promise(() => {}),
  tempCartItems: [],
  setTempCartItems: () => {},
  setOrderedCart: () => {},
  setOrderedContract: () => {},
  orderedLineNotes: {},
  setOrderedLineNotes: () => {},
  isCreditSelectionChanging: false,
  setIsCreditSelectionChanging: () => {},
  isWillCall: false,
  isDelivery: false
};

export const CheckoutContext = createContext(initialCheckoutContextValues);
/**
 * Provider
 */
function CheckoutProvider(props: { children: ReactNode }) {
  /**
   * Hooks
   */
  const history = useHistory();
  const location = useLocation();

  /**
   * Context
   */
  const {
    cart,
    isWillCall,
    updateCart: cartUpdateCart,
    updateDelivery: cartUpdateDelivery,
    updateWillCall: cartUpdateWillCall,
    contract
  } = useCartContext();

  /**
   * State
   */
  const [disableContinue, setDisableContinue] = useState(false);
  const [deliveryData, setDeliveryData] = useState<DeliveryData>({});
  const [deliveryMethod, setDeliveryMethod] = useState(
    DeliveryMethodEnum.Delivery
  );
  const [mnCronShoppingCartId, setMnCronShoppingCartId] = useState('');
  const [orderData, setOrderData] = useState(defaultOrderData);
  const [orderPreviewData, setOrderPreviewData] =
    useState<OrderPreviewResponse>(defaultOrderPreviewData);
  const [paymentData, setPaymentData] = useState(defaultPaymentData);
  const [poNumberError, setPoNumberError] = useState(true);
  const [step, setStep] = useState(Step.INFO);
  const [shipToBranch, setShipToBranch] = useState(defaultBranchData);
  const [willCallData, setWillCallData] = useState<WillCallData>({});
  const [tempCartItems, setTempCartItems] = useState<LineItem[]>([]);
  const [orderedCart, setOrderedCart] = useState<Cart>();
  const [orderedContract, setOrderedContract] = useState<CartContractType>();
  const [orderedLineNotes, setOrderedLineNotes] = useState<
    Record<string, string>
  >({});
  const [isCreditSelectionChanging, setIsCreditSelectionChanging] =
    useState(false);

  /**
   * Consts
   */
  const cartDeliveryMethod = isWillCall ? cart?.willCall : cart?.delivery;
  const orderedCartDeliveryMethod =
    orderedCart?.deliveryMethod === DeliveryMethodEnum.Willcall
      ? orderedCart?.willCall
      : orderedCart?.delivery;
  const deliveryMethodObject = cart
    ? cartDeliveryMethod
    : orderedCart
    ? orderedCartDeliveryMethod
    : undefined;

  const onBackButtonEvent = () => {
    switch (window.location.pathname) {
      case '/checkout':
        setStep(Step.INFO);
        break;
      case '/checkout/review':
        if (!contract) {
          history.push('/');
        } else {
          setStep(Step.REVIEW);
        }
        break;
      case '/checkout/complete':
        setStep(Step.CONFIRMATION);
        break;
    }
  };

  /**
   * Effect
   */
  useEffect(() => {
    window.addEventListener('popstate', onBackButtonEvent);
    return () => {
      window.removeEventListener('popstate', onBackButtonEvent);
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    if (cart) {
      setDeliveryMethod(cart.deliveryMethod || DeliveryMethodEnum.Delivery);
      setDeliveryData({
        address: cart.delivery.address,
        preferredDate: cart.delivery.preferredDate,
        preferredTime: cart.delivery.preferredTime,
        deliveryInstructions: cart.delivery.deliveryInstructions
      });
      setWillCallData({
        preferredDate: cart.willCall.preferredDate,
        preferredTime: cart.willCall.preferredTime,
        pickupInstructions: cart.willCall.pickupInstructions
      });
      setPaymentData({
        paymentMethodType: cart.paymentMethodType,
        poNumber: cart.poNumber,
        creditCard: cart.creditCard
      });
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [cart]);

  // Add query params
  useEffect(() => {
    switch (step) {
      case Step.INFO:
        if (location.pathname !== '/checkout') {
          history.push('/checkout', { canShowCustomNavAlert: !!contract });
        }
        break;
      case Step.REVIEW:
        if (location.pathname !== '/checkout/review') {
          history.push('/checkout/review', {
            canShowCustomNavAlert: !!contract
          });
        }
        break;
      case Step.PAYMENT:
        if (location.pathname !== '/checkout/payment') {
          history.push('/checkout/payment', {
            canShowCustomNavAlert: !!contract
          });
        }
        break;
      case Step.CONFIRMATION:
        if (location.pathname !== '/checkout/complete') {
          history.push('/checkout/complete');
        }
        break;
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [step]);

  /**
   * Callbacks
   */
  const updateCartPayment = async (newPaymentData: PaymentData) => {
    const { creditCard, ...paymentInput } = newPaymentData;

    const updatedCreditCard = {
      ...creditCard,
      expirationDate: omit(creditCard?.expirationDate, '__typename')
    } as CreditCardInput;

    const updateCartData: CartInput = {
      ...paymentInput,
      creditCard: creditCard ? omit(updatedCreditCard, '__typename') : null
    };

    await cartUpdateCart?.(cart!.id, updateCartData).then((resp) => {
      if (resp) {
        const data = {
          paymentMethodType: resp.paymentMethodType,
          poNumber: resp.poNumber,
          creditCard: resp.creditCard
        };
        setPaymentData(data);
      }
    });
  };

  const updateDelivery = async (deliveryData: DeliveryData) => {
    const { address, ...deliveryInput } = deliveryData;
    const addressInput = omit(address, ['__typename', 'id']) as AddressInput;
    const updateDeliveryData: DeliveryInput = {
      cartId: cart!.id,
      shouldShipFullOrder: cart!.delivery.shouldShipFullOrder,
      address: addressInput,
      ...deliveryInput
    };
    await cartUpdateDelivery?.(updateDeliveryData).then((resp) => {
      if (resp) {
        setDeliveryData(resp);
      }
    });
  };

  const updateWillCall = async (willCallData: WillCallData) => {
    const updateWillCallData: WillCallInput = {
      cartId: cart!.id,
      ...willCallData
    };
    await cartUpdateWillCall?.(updateWillCallData).then((resp) => {
      if (resp) {
        const willCallData: WillCallData = pick(resp, [
          'preferredDate',
          'preferredTime',
          'pickupInstructions'
        ]);
        setWillCallData(willCallData);
      }
    });
  };

  /**
   * Render
   */
  return (
    <CheckoutContext.Provider
      value={{
        deliveryData,
        deliveryMethod,
        disableContinue,
        setDisableContinue,
        orderData,
        orderPreviewData,
        paymentData,
        poNumberError,
        setDeliveryData,
        setOrderData,
        setOrderPreviewData,
        setPaymentData,
        setPoNumberError,
        setShipToBranch,
        setStep,
        setWillCallData,
        shipToBranch,
        step,
        willCallData,
        deliveryMethodObject,
        updateCart: updateCartPayment,
        updateDelivery,
        updateWillCall,
        tempCartItems,
        setTempCartItems,
        mnCronShoppingCartId,
        setMnCronShoppingCartId,
        orderedCart,
        setOrderedCart,
        orderedContract,
        setOrderedContract,
        orderedLineNotes,
        setOrderedLineNotes,
        isCreditSelectionChanging,
        setIsCreditSelectionChanging,
        isDelivery: deliveryMethod === DeliveryMethodEnum.Delivery,
        isWillCall: deliveryMethod === DeliveryMethodEnum.Willcall
      }}
    >
      {props.children}
    </CheckoutContext.Provider>
  );
}

export const useCheckoutContext = () => useContext(CheckoutContext);

export default CheckoutProvider;
