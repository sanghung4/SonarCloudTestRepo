import { useState } from 'react';
import { fireEvent, waitFor } from '@testing-library/react';

import { render } from 'test-utils/TestWrapper';
import useDebounce from 'hooks/useDebounce';

const mockValue = 'OK';
const delay = 10;

function MockComponent() {
  const [localValue, setLocalValue] = useState('');
  const { value, loading } = useDebounce(localValue, delay);
  const buttonClickEvent = () => {
    setLocalValue(mockValue);
  };
  return (
    <>
      <div data-testid="debounce-test-value">{value}</div>
      <div data-testid="debounce-test-loading">{`${loading}`}</div>
      <button data-testid="debounce-test-button" onClick={buttonClickEvent} />
    </>
  );
}

describe('useDebounce utils', () => {
  it('Expect the debounce hook is loading', async () => {
    const { getByTestId } = render(<MockComponent />);

    fireEvent.click(getByTestId('debounce-test-button'));
    expect(getByTestId('debounce-test-loading')).toHaveTextContent('true');
    await waitFor(() => new Promise((res) => setTimeout(res, delay)));
    expect(getByTestId('debounce-test-loading')).toHaveTextContent('false');
  });

  it('Expect the debounce hook is setting values', async () => {
    const { getByTestId } = render(<MockComponent />);

    fireEvent.click(getByTestId('debounce-test-button'));
    expect(getByTestId('debounce-test-value')).toBeEmptyDOMElement();
    await waitFor(() => new Promise((res) => setTimeout(res, delay)));
    expect(getByTestId('debounce-test-value')).toHaveTextContent(mockValue);
  });
});
