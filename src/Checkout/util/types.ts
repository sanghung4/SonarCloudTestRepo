import { Cart, Delivery, WillCall } from 'generated/graphql';

/**
 * Enum
 */
export enum Step {
  INFO,
  PAYMENT,
  REVIEW,
  CONFIRMATION
}

/**
 * Data
 */
export type CustomAddressInput = {
  city: string;
  companyName?: string | null;
  country: string;
  custom: boolean;
  state: string;
  street1: string;
  street2?: string | null;
  zip: string;
  phoneNumber?: string | null;
};

export type DeliveryData = {
  address?: Delivery['address'];
  preferredDate?: Delivery['preferredDate'];
  preferredTime?: Delivery['preferredTime'];
  deliveryInstructions?: Delivery['deliveryInstructions'];
  phoneNumber?: Delivery['phoneNumber'];
};

export type WillCallData = {
  preferredDate?: WillCall['preferredDate'];
  preferredTime?: WillCall['preferredTime'];
  pickupInstructions?: WillCall['pickupInstructions'];
};

export type PaymentData = {
  paymentMethodType?: Cart['paymentMethodType'];
  poNumber?: Cart['poNumber'];
  creditCard?: Cart['creditCard'];
};

export type StepInfo = {
  number: number;
  showNext: boolean;
  showPrev: boolean;
  showTax: boolean;
  buttonLabel?: string;
  buttonAction?: () => any;
  alignActions?: 'left' | 'column';
};
