import { CustomAddressInput } from 'Checkout/util/types';
import { Delivery, PreferredTimeEnum, WillCall } from 'generated/graphql';

export const mockCustomAddress: CustomAddressInput = {
  custom: true,
  companyName: 'Softlayer',
  street1: '123 Chimney Rock',
  street2: '#107',
  city: 'Houston',
  state: 'Texas',
  zip: '77012',
  country: 'USA'
};

export const mockDeliveryMethodObjWillCall: WillCall = {
  __typename: 'WillCall',
  branchId: 'branch12345',
  id: 'willcall123',
  pickupInstructions: 'just leave it',
  preferredDate: '07/02/2022',
  preferredTime: PreferredTimeEnum.Asap
};

export const mockDeliveryMethodObjDelivery: Delivery = {
  __typename: 'Delivery',
  address: {
    __typename: 'Address',
    id: 'sl1234',
    ...mockCustomAddress
  },
  deliveryInstructions: 'do not use packing peanuts',
  id: 'delivery123',
  phoneNumber: '213-555-5555',
  preferredDate: '07/10/2022',
  preferredTime: PreferredTimeEnum.Asap,
  shipToId: 'shipto1234567',
  shouldShipFullOrder: false
};
