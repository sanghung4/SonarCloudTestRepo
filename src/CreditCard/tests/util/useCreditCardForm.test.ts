import { submitCreditCardForm } from 'CreditCard/util/useCreditCardForm';

const initialValues = {
  cardHolder: '',
  streetAddress: '',
  city: '',
  state: '',
  postalCode: '',
  saveCard: true
};

describe('CreditCard - util/useCreditCardForm', () => {
  it('Expect `submitCreditCardForm` to call mocked function', () => {
    const mockFn = jest.fn();
    submitCreditCardForm(mockFn)(initialValues);
    expect(mockFn).toBeCalled();
  });
});
