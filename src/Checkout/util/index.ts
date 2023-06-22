import { startCase } from 'lodash-es';
import { t } from 'i18next';

import {
  Branch,
  OrderPreviewResponse,
  OrderResponse,
  PaymentMethodTypeEnum,
  PreferredTimeEnum
} from 'generated/graphql';
import { CustomAddressInput, PaymentData } from './types';
import { CartContractType } from 'providers/CartProvider';

/**
 * Default Values
 */
export const defaultBranchData: Branch = {
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
};

export const defaultOrderData: OrderResponse = { lineItems: [] };
export const defaultOrderPreviewData: OrderPreviewResponse = {
  subTotal: '',
  tax: '',
  orderTotal: ''
};
export const defaultPaymentData: PaymentData = {
  paymentMethodType: PaymentMethodTypeEnum.Billtoaccount
};

export const initialAddressValues: CustomAddressInput = {
  companyName: '',
  street1: '',
  street2: '',
  city: '',
  state: '',
  zip: '',
  phoneNumber: '',
  country: 'USA',
  custom: true
};

/**
 * Utils
 */
export function preferredTimeValues(value: unknown) {
  const enumValue = value as PreferredTimeEnum;
  return enumValue === PreferredTimeEnum.Asap
    ? PreferredTimeEnum.Asap
    : startCase(enumValue.toLowerCase());
}

export function contractInfo({
  contract,
  orderedContract
}: {
  contract?: CartContractType;
  orderedContract?: CartContractType;
}) {
  const na = t('common.na');
  const output = {
    contractNumber: na,
    contractDescription: na,
    job: na
  };
  if (contract) {
    const { data } = contract;

    output.contractNumber = data?.contractNumber ?? na;
    output.contractDescription = data?.contractDescription ?? na;

    const jobName = data?.jobName;
    const jobNumber = data?.customerInfo?.jobNumber;
    if (jobName && jobNumber) {
      output.job = `${jobName} - ${jobNumber}`;
    } else if (jobName || jobNumber) {
      output.job = jobName || jobNumber!;
    }
  } else if (orderedContract) {
    const { data } = orderedContract;

    output.contractNumber = data?.contractNumber ?? na;
    output.contractDescription = data?.contractDescription ?? na;

    const jobName = data?.jobName;
    const jobNumber = data?.customerInfo?.jobNumber;
    if (jobName && jobNumber) {
      output.job = `${jobName} - ${jobNumber}`;
    } else if (jobName || jobNumber) {
      output.job = jobName || jobNumber!;
    }
  }
  return output;
}
