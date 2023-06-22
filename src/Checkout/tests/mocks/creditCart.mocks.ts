import { CreditCard } from 'generated/graphql';

export const mockCreditCard: CreditCard = {
  __typename: 'CreditCard',
  cardHolder: 'test',
  creditCardNumber: '1234 5678 9012 3456',
  creditCardType: 'visa',
  elementPaymentAccountId: 'test123',
  expirationDate: {
    __typename: 'DateWrapper',
    date: '12/31/2099'
  },
  postalCode: '12345',
  streetAddress: '112 E Test St Alhambra CA 91801'
};
