import { act, fireEvent } from '@testing-library/react';
import { CreditCardFormProps } from 'CreditCard/CreditCardForm';
import { CreditCardContext } from 'CreditCard/CreditCardProvider';
import { mockCreditCartContext } from 'CreditCard/tests/mocks/context.mocks';
import {
  mockCartHolderInput,
  mockCreditCard
} from 'CreditCard/tests/mocks/index.mocks';
import { CreditCardState } from 'CreditCard/util/config';
import { CreditCardContextType } from 'CreditCard/util/types';
import PaymentInformation from 'PaymentInformation';
import { PaymentDialogProps } from 'PaymentInformation/PaymentDialog';
import { SelectedAccountsContextType } from 'providers/SelectedAccountsProvider';
import { render } from 'test-utils/TestWrapper';

/**
 * Types
 */
type Mock = {
  selectedAccountsConfig?: Partial<SelectedAccountsContextType>;
  creditCardContext: CreditCardContextType;
};

/**
 * Mock values
 */
const defaultMocks: Mock = {
  creditCardContext: { ...mockCreditCartContext }
};
let mocks: Mock = { ...defaultMocks };

/**
 * Mock methods
 */
jest.mock('PaymentInformation/PaymentDialog', () => ({
  __esModule: true,
  default: (props: PaymentDialogProps) => (
    <button
      onClick={() => {
        props.handleDelete();
        props.onClose();
      }}
      data-testid="confirm-delete-button"
    />
  )
}));
jest.mock('CreditCard/CreditCardForm', () => ({
  __esModule: true,
  default: (props: CreditCardFormProps) => (
    <button
      onClick={() => {
        props.onCancel?.();
        props.onSubmit(mockCartHolderInput, false);
      }}
      data-testid="cc-form-button"
    />
  )
}));

/**
 * Setup
 */
function setup(m: Mock) {
  const wrapper = render(
    <CreditCardContext.Provider value={m.creditCardContext}>
      <PaymentInformation />
    </CreditCardContext.Provider>,
    { selectedAccountsConfig: m.selectedAccountsConfig }
  );
  return wrapper;
}

describe('Payment Information', () => {
  // ⚪ Reset mocks
  afterEach(() => {
    mocks = { ...defaultMocks };
  });

  // 🟢 1 - Default
  it('Expect container to be rendered', async () => {
    // act
    const { findByTestId } = setup(mocks);
    const container = await findByTestId('payment-information-container');

    // assert
    expect(container).toBeInTheDocument();
  });

  // 🟢 2 - add-card-button
  it('Expect setCreditCardState to be called upon clicking "add-card-button" button', async () => {
    // act
    const { findByTestId } = setup(mocks);
    const button = await findByTestId('add-card-button');
    fireEvent.click(button);

    // assert
    expect(mocks.creditCardContext.setCreditCardState).toBeCalledWith(
      CreditCardState.ADD
    );
  });

  // 🟢 3 - Loading
  it('Expect loader to be rendered', async () => {
    // arrange
    mocks.creditCardContext.updatingList = true;
    mocks.creditCardContext.creditCardState = CreditCardState.ADD;

    // act
    const { findAllByRole } = setup(mocks);
    const loaders = await findAllByRole('progressbar');

    // assert
    expect(loaders.length).toBeTruthy();
  });

  // 🟢 4 - CreditCardData & CreditCardState.ADD
  it('Expect "payment-information-add-card" container to be rendered', async () => {
    // arrange
    mocks.creditCardContext.updatingList = false;
    mocks.creditCardContext.creditCardData = [mockCreditCard];
    mocks.creditCardContext.creditCardState = CreditCardState.ADD;

    // act
    const { findByTestId } = setup(mocks);
    const container = await findByTestId('payment-information-add-card');

    // assert
    expect(container).toBeInTheDocument();
  });

  // 🟢 5 - CreditCardData & CreditCardState.CHANGE & updatingList
  it('Expect "payment-information-update-loader" container to be rendered', async () => {
    // arrange
    mocks.creditCardContext.deleteCreditCardLoading = true;
    mocks.creditCardContext.creditCardData = [mockCreditCard];
    mocks.creditCardContext.creditCardState = CreditCardState.CHANGE;

    // act
    const { findAllByRole } = setup(mocks);
    const loaders = await findAllByRole('progressbar');

    // assert
    expect(loaders.length).toBeTruthy();
  });

  // 🟢 6 - CreditCardData & CreditCardState.CHANGE
  it('Expect "payment-information-subcontainer" container to be rendered', async () => {
    // arrange
    mocks.creditCardContext.deleteCreditCardLoading = false;
    mocks.creditCardContext.creditCardData = [mockCreditCard];
    mocks.creditCardContext.creditCardState = CreditCardState.CHANGE;

    // act
    const { findByTestId } = setup(mocks);
    const container = await findByTestId('payment-information-subcontainer');

    // assert
    expect(container).toBeInTheDocument();
  });

  // 🟢 7 - CreditCardData & CreditCardState.CHANGE & expired
  it('Expect "card-header-expired" container to be rendered', async () => {
    // arrange
    mocks.creditCardContext.deleteCreditCardLoading = false;
    mocks.creditCardContext.creditCardData = [mockCreditCard];
    mocks.creditCardContext.creditCardState = CreditCardState.CHANGE;
    mocks.creditCardContext.expiredCreditCards = {
      [mockCreditCard.elementPaymentAccountId]: true
    };

    // act
    const { findByTestId } = setup(mocks);
    const container = await findByTestId('card-header-expired');

    // assert
    expect(container).toBeInTheDocument();
  });

  // 🟢 8 - delete-card-button
  it('Expect setCreditCardState to be called upon clicking "delete-card-button" button', async () => {
    // act
    const { findByTestId } = setup(mocks);
    const button = await findByTestId('delete-card-button');
    fireEvent.click(button);

    // assert
    expect(mocks.creditCardContext.setCreditCardState).toBeCalledWith(
      CreditCardState.DELETE
    );
  });

  // 🟢 9 - PaymentDialog > handleDeleteCreditCard
  it('Expect deleteCreditCard to be called upon clicking "delete-card-button" and "confirm-delete-button"', async () => {
    // arrange
    mocks.creditCardContext.deleteCreditCardLoading = false;

    // act 1
    const { findByTestId } = setup(mocks);
    await act(async () => {
      const deleteButton = await findByTestId('delete-card-button');
      fireEvent.click(deleteButton);
    });
    // assert 1
    expect(mocks.creditCardContext.setCreditCardState).toBeCalledWith(
      CreditCardState.DELETE
    );

    // act 2
    await act(async () => {
      const confirmButton = await findByTestId('confirm-delete-button');
      fireEvent.click(confirmButton);
    });
    // assert 2
    expect(mocks.creditCardContext.deleteCreditCard).toBeCalled();
  });

  // 🟢 10 - handleCancel and handleSubmit
  it('Expect setCreditCardState to be called upon clicking on mocked "cc-form-button"', async () => {
    // arrange
    mocks.creditCardContext.creditCardListLoading = false;
    mocks.creditCardContext.updatingList = false;
    mocks.creditCardContext.creditCardData = [mockCreditCard];
    mocks.creditCardContext.creditCardState = CreditCardState.ADD;

    // act
    const { findByTestId } = setup(mocks);
    const button = await findByTestId('cc-form-button');
    fireEvent.click(button);

    // assert
    expect(mocks.creditCardContext.setCreditCardState).toBeCalledWith(
      CreditCardState.NONE
    );
  });
});
