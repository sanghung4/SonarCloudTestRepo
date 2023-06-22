import { act, fireEvent } from '@testing-library/react';
import CustomAddress from 'Checkout/CustomAddress';
import { render } from 'test-utils/TestWrapper';

describe('Checkout - CustomAddress', () => {
  it('should match snapshot', () => {
    const { container } = render(
      <CustomAddress onConfirm={jest.fn()} onCancel={jest.fn()} />
    );
    expect(container).toMatchSnapshot();
  });

  it('expect error messages to pop up when submitting without inputs', async () => {
    const { getByTestId, getAllByText } = render(
      <CustomAddress onConfirm={jest.fn()} onCancel={jest.fn()} />
    );
    fireEvent.click(getByTestId('confirm-address-button'));
    await act(() => new Promise((res) => setTimeout(res, 0)));
    const errorElements = getAllByText(/(is required)/);
    expect(errorElements.length).toBeGreaterThan(0);
  });
});
