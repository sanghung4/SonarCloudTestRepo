import { act, fireEvent } from '@testing-library/react';
import CreditCardForm, { CreditCardFormProps } from 'CreditCard/CreditCardForm';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

/**
 * Mock values
 */
const mocks: CreditCardFormProps = { onSubmit: jest.fn() };

/**
 * Setup function
 */
function setup(props: CreditCardFormProps) {
  return render(<CreditCardForm {...props} />);
}

/**
 * Test
 */
describe('CreditCard - CreditCardForm', () => {
  afterEach(() => {
    mocks.hideSaveCard = undefined;
    mocks.canSaveCard = true;
    mocks.onCancel = undefined;
    mocks.onSubmit = jest.fn();
  });

  it('expect to match snapshot on desktop', () => {
    setBreakpoint('desktop');
    const { container } = setup(mocks);
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot on mobile', () => {
    setBreakpoint('mobile');
    const { container } = setup(mocks);
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot with hideSaveCard', () => {
    setBreakpoint('desktop');
    mocks.hideSaveCard = true;
    const { container } = setup(mocks);
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot with valid onCancel', () => {
    setBreakpoint('desktop');
    mocks.onCancel = jest.fn();
    const { container } = setup(mocks);
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot after clicking submit on a blank data', async () => {
    setBreakpoint('desktop');
    const { container, getByTestId } = setup(mocks);
    const submit = getByTestId('add-new-card-button');
    fireEvent.click(submit);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });
});
