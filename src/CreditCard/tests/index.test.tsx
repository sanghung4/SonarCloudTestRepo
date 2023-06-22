import { fireEvent } from '@testing-library/react';
import CreditCard from 'CreditCard';
import { CreditCardContext } from 'CreditCard/CreditCardProvider';
import { mockCreditCartContext } from 'CreditCard/tests/mocks/context.mocks';
import { mockCreditCard } from 'CreditCard/tests/mocks/index.mocks';
import { CreditCardState } from 'CreditCard/util/config';
import { render } from 'test-utils/TestWrapper';

/**
 * Mock values
 */
const mocks = {
  cc: { ...mockCreditCartContext }
};

/**
 * Setup function
 */
function setup(mock: typeof mocks) {
  return render(
    <CreditCardContext.Provider value={mock.cc}>
      <CreditCard />
    </CreditCardContext.Provider>
  );
}
/**
 * Test
 */
describe('CreditCard', () => {
  afterEach(() => {
    mocks.cc = { ...mockCreditCartContext };
  });

  it('expect to match snapshot with basic props', () => {
    const { container } = setup(mocks);
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot with valid credit card', () => {
    mocks.cc.creditCard = { ...mockCreditCard };
    const { container } = setup(mocks);
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot while loading', () => {
    mocks.cc.creditCardListLoading = true;
    const { container } = setup(mocks);
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot while updating list', () => {
    mocks.cc.updatingList = true;
    const { container } = setup(mocks);
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot while change', () => {
    mocks.cc.creditCardState = CreditCardState.CHANGE;
    const { container } = setup(mocks);
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot while change with creditCardData', () => {
    const cc = { ...mockCreditCard };
    mocks.cc.creditCardData = [cc, cc];
    mocks.cc.creditCardState = CreditCardState.CHANGE;
    const { container } = setup(mocks);
    expect(container).toMatchSnapshot();
  });

  it('expect "Add Cart" button to call setCreditCardState when clicked in change state', () => {
    mocks.cc.creditCardState = CreditCardState.CHANGE;
    const { getByTestId } = setup(mocks);
    fireEvent.click(getByTestId('add-card-button'));
    expect(mocks.cc.setCreditCardState).toBeCalledWith(CreditCardState.ADD);
  });

  it('expect "Use Selected Card" button to call setCreditCardState when clicked in change state', () => {
    mocks.cc.creditCardState = CreditCardState.CHANGE;
    mocks.cc.selectedCreditCard = 'test';
    const { getByTestId } = setup(mocks);
    fireEvent.click(getByTestId('use-selected-card-button'));
    expect(mocks.cc.setCreditCardState).toBeCalledWith(
      CreditCardState.SELECTED
    );
  });

  it('expect "Cancel" button to call setCreditCardState when clicked in change state', () => {
    mocks.cc.creditCardState = CreditCardState.CHANGE;
    mocks.cc.creditCard = { ...mockCreditCard };
    const { getByTestId } = setup(mocks);
    fireEvent.click(getByTestId('cancel-button'));
    expect(mocks.cc.setCreditCardState).toBeCalledWith(
      CreditCardState.SELECTED
    );
  });

  it('expect to match snapshot while add', () => {
    mocks.cc.creditCardState = CreditCardState.ADD;
    const { container } = setup(mocks);
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot while add and loading', () => {
    mocks.cc.creditCardState = CreditCardState.ADD;
    mocks.cc.getCreditCardLoading = true;
    const { container } = setup(mocks);
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot while add and valid cc data', () => {
    const cc = { ...mockCreditCard };
    mocks.cc.creditCardState = CreditCardState.ADD;
    mocks.cc.creditCard = { ...cc };
    mocks.cc.creditCardData = [cc, cc];
    const { container } = setup(mocks);
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot while selected and creditCardData', () => {
    const cc = { ...mockCreditCard };
    mocks.cc.creditCardData = [cc, cc];
    mocks.cc.creditCardState = CreditCardState.SELECTED;
    const { container } = setup(mocks);
    expect(container).toMatchSnapshot();
  });

  it('expect "Change Card" button to call setCreditCardState when clicked in change state', () => {
    const cc = { ...mockCreditCard };
    mocks.cc.creditCardData = [cc, cc];
    mocks.cc.creditCardState = CreditCardState.SELECTED;
    const { getByTestId } = setup(mocks);
    fireEvent.click(getByTestId('change-card-button'));
    expect(mocks.cc.setCreditCardState).toBeCalledWith(CreditCardState.CHANGE);
  });

  it('expect "Add Card" button to call setCreditCardState when clicked in change state', () => {
    const cc = { ...mockCreditCard };
    mocks.cc.creditCardData = [cc, cc];
    mocks.cc.creditCardState = CreditCardState.SELECTED;
    const { getByTestId } = setup(mocks);
    fireEvent.click(getByTestId('add-card-button'));
    expect(mocks.cc.setCreditCardState).toBeCalledWith(CreditCardState.ADD);
  });

  it('expect "Cancel" button to call setCreditCardState when clicked in add state', () => {
    mocks.cc.creditCardState = CreditCardState.ADD;
    const cc = { ...mockCreditCard };
    mocks.cc.creditCardData = [cc, cc];
    const { getByTestId } = setup(mocks);
    fireEvent.click(getByTestId('cancel-add-new-card-button'));
    expect(mocks.cc.setCreditCardState).toBeCalledWith(CreditCardState.CHANGE);
  });
});
