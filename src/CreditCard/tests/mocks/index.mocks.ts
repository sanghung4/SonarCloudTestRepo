import { CardHolderInput, CreditCard } from 'generated/graphql';

export const mockCreditCard: CreditCard = {
  __typename: 'CreditCard',
  cardHolder: 'Test Holder',
  creditCardNumber: '1234567890123456',
  creditCardType: 'Visa',
  elementPaymentAccountId: 'test',
  expirationDate: {
    __typename: 'DateWrapper',
    date: '10/20/2028'
  },
  postalCode: '75080',
  streetAddress: '123 random st'
};
export const mockCartHolderInput: CardHolderInput = {
  cardHolder: 'test',
  postalCode: '75080',
  returnUrl: '/test',
  streetAddress: '107 East Beacon St'
};
