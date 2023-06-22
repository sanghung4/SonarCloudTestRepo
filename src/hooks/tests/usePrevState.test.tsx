import { act, fireEvent, waitFor } from '@testing-library/react';
import { useState } from 'react';
import { render } from 'test-utils/TestWrapper';
import usePrevState from 'hooks/usePrevState';

function MockComponent() {
  const [value, setValue] = useState(0);
  const prevValue = usePrevState(value);
  const handleClick = () => setValue(value + 1);
  return (
    <>
      <span data-testid="test-value">{value}</span>
      <span data-testid="test-prevValue">{prevValue}</span>
      <button data-testid="test-button" onClick={handleClick} />
    </>
  );
}

describe('Utils - usePrevState', () => {
  it('Ensure the prev value is correct', async () => {
    const { getByTestId } = render(<MockComponent />);
    expect(getByTestId('test-value')).toHaveTextContent('0');
    expect(getByTestId('test-prevValue')).toHaveTextContent('');
    act(() => {
      fireEvent.click(getByTestId('test-button'));
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(getByTestId('test-value')).toHaveTextContent('1');
    expect(getByTestId('test-prevValue')).toHaveTextContent('0');
  });
});
