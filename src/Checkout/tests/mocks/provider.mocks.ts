import { CheckoutContextType } from 'Checkout/CheckoutProvider';
import { Step } from 'Checkout/util/types';
import { DeliveryMethodEnum, PaymentMethodTypeEnum } from 'generated/graphql';

export const mockCheckoutContext: CheckoutContextType = {
  deliveryData: {},
  deliveryMethod: DeliveryMethodEnum.Delivery,
  disableContinue: false,
  setDisableContinue: jest.fn(),
  orderData: {
    lineItems: []
  },
  orderPreviewData: {
    orderTotal: '',
    subTotal: '',
    tax: ''
  },
  paymentData: { paymentMethodType: PaymentMethodTypeEnum.Billtoaccount },
  poNumberError: false,
  shipToBranch: {
    branchId: '',
    latitude: 0,
    longitude: 0,
    isPlumbing: false,
    isWaterworks: false,
    isHvac: false,
    isBandK: false,
    isActive: false,
    isAvailableInStoreFinder: false,
    isPricingOnly: false,
    isShoppable: false,
    id: ''
  },
  mnCronShoppingCartId: '',
  step: Step.INFO,
  setDeliveryData: jest.fn(),
  setOrderData: jest.fn(),
  setOrderPreviewData: jest.fn(),
  setPaymentData: jest.fn(),
  setPoNumberError: jest.fn(),
  setShipToBranch: jest.fn(),
  setStep: jest.fn(),
  setWillCallData: jest.fn(),
  setMnCronShoppingCartId: jest.fn(),
  willCallData: {},
  deliveryMethodObject: undefined,
  updateCart: jest.fn(),
  updateDelivery: jest.fn(),
  updateWillCall: jest.fn(),
  tempCartItems: [],
  setTempCartItems: jest.fn(),
  setOrderedCart: jest.fn(),
  setOrderedContract: jest.fn(),
  orderedLineNotes: {},
  setOrderedLineNotes: jest.fn(),
  isCreditSelectionChanging: false,
  setIsCreditSelectionChanging: jest.fn(),
  isWillCall: false,
  isDelivery: false
};
