import { DependencyList, useState } from 'react';
import { fireEvent, waitFor } from '@testing-library/react';

import { render } from 'test-utils/TestWrapper';
import useDebouncedCallback from 'hooks/useDebouncedCallback';

const mockValue = 'OK';
const delay = 10;

type TestProps = {
  dep?: DependencyList;
};

function MockComponent(props: TestProps) {
  const [localValue, setLocalValue] = useState('');
  const cb = () => {
    setLocalValue(mockValue);
  };
  const buttonClickEvent = useDebouncedCallback(cb, delay, props.dep);
  return (
    <>
      <div data-testid="debounce-test-value">{localValue}</div>
      <button data-testid="debounce-test-button" onClick={buttonClickEvent} />
    </>
  );
}

describe('useDebouncedCallback utils', () => {
  it('Expect the debounce hook is setting values', async () => {
    const { getByTestId } = render(<MockComponent dep={[]} />);

    fireEvent.click(getByTestId('debounce-test-button'));
    expect(getByTestId('debounce-test-value')).toBeEmptyDOMElement();
    await waitFor(() => new Promise((res) => setTimeout(res, delay)));
    expect(getByTestId('debounce-test-value')).toHaveTextContent(mockValue);
  });

  it('Expect the debounce hook is setting values with undefined dep', async () => {
    const { getByTestId } = render(<MockComponent />);

    fireEvent.click(getByTestId('debounce-test-button'));
    expect(getByTestId('debounce-test-value')).toBeEmptyDOMElement();
    await waitFor(() => new Promise((res) => setTimeout(res, delay)));
    expect(getByTestId('debounce-test-value')).toHaveTextContent(mockValue);
  });
});
