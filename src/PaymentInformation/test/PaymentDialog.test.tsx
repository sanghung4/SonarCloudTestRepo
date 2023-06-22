import { fireEvent } from '@testing-library/react';

import { CreditCardContext } from 'CreditCard/CreditCardProvider';
import { mockCreditCartContext } from 'CreditCard/tests/mocks/context.mocks';
import { mockCreditCard } from 'CreditCard/tests/mocks/index.mocks';
import { CreditCardContextType } from 'CreditCard/util/types';
import { CreditCard } from 'generated/graphql';
import PaymentDialog from 'PaymentInformation/PaymentDialog';
import { render } from 'test-utils/TestWrapper';

/**
 * Types
 */
type Mock = {
  onClose: jest.Mock;
  handleDelete: jest.Mock;
  open: boolean;
  cardToDelete?: CreditCard;
  creditCardContext: CreditCardContextType;
};

/**
 * Mock values
 */
const defaultMocks: Mock = {
  onClose: jest.fn(),
  open: false,
  handleDelete: jest.fn(),
  cardToDelete: undefined,
  creditCardContext: { ...mockCreditCartContext }
};
let mocks: Mock = { ...defaultMocks };

/**
 * Setup
 */
function setup(m: Mock) {
  const wrapper = render(
    <CreditCardContext.Provider value={m.creditCardContext}>
      <PaymentDialog
        onClose={m.onClose}
        handleDelete={m.handleDelete}
        open={m.open}
        cardToDelete={m.cardToDelete}
      />
    </CreditCardContext.Provider>
  );
  return wrapper;
}

/**
 * TEST
 */
describe('PaymentInforation/PaymentDialog', () => {
  // ⚪ Reset mocks
  afterEach(() => {
    mocks = { ...defaultMocks };
  });

  // 🟢 1 - undefined cardToDelete
  it('Expect CreditCardListItem not to be rendered when cardToDelete is undefined', () => {
    // arrange
    mocks.open = true;

    // act
    const { queryAllByTestId } = setup(mocks);
    const found = queryAllByTestId('card-to-delete');

    // assert
    expect(found.length).toBeFalsy();
  });

  // 🟢 2 - truthy cardToDelete
  it('Expect CreditCardListItem to be rendered with cardToDelete data', () => {
    // arrange
    mocks.open = true;
    mocks.cardToDelete = { ...mockCreditCard };

    // act
    const { queryAllByTestId } = setup(mocks);
    const found = queryAllByTestId('card-to-delete');

    // assert
    expect(found.length).toBeTruthy();
  });

  // 🟢 3 - truthy cardToDelete & not open
  it('Expect CreditCardListItem not to be rendered with cardToDelete data but not being opened', () => {
    // arrange
    mocks.open = false;
    mocks.cardToDelete = { ...mockCreditCard };

    // act
    const { queryAllByTestId } = setup(mocks);
    const found = queryAllByTestId('card-to-delete');

    // assert
    expect(found.length).toBeFalsy();
  });

  // 🟢 4 - Confirm Delete Button
  it('Expect handleDelete and onClose to be called when "confirm-delete-button" is pressed', async () => {
    // arrange
    mocks.open = true;

    // act
    const { findByTestId } = setup(mocks);
    const button = await findByTestId('confirm-delete-button');
    fireEvent.click(button);

    // assert
    expect(mocks.handleDelete).toBeCalled();
    expect(mocks.onClose).toBeCalled();
  });
});
