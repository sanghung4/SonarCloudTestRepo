/* eslint-disable no-native-reassign */
import { fireEvent } from '@testing-library/react';
import { act } from 'react-dom/test-utils';
import { render } from 'test-utils/TestWrapper';
import { useLocalStorage } from 'hooks/useLocalStorage';

const toBeValue = 'aaa';
function MockComponent() {
  const [value, setValue] = useLocalStorage<string>('test', '');
  const handleButton = () => setValue(toBeValue);
  return (
    <>
      <span data-testid="data">{value}</span>
      <button data-testid="button" onClick={handleButton} />
    </>
  );
}

function MockComponent2() {
  const [value2, setValue2] = useLocalStorage<() => string>('test2', () => '');
  const handleButton2 = () => setValue2(() => toBeValue);
  return (
    <>
      <span data-testid="data2">{value2}</span>
      <button data-testid="button2" onClick={handleButton2} />
    </>
  );
}

describe('Utils - useLocalstorage', () => {
  afterEach(() => {
    window.localStorage.clear();
  });

  it('expect to useLocalstorage to update the data', () => {
    const { getByTestId } = render(<MockComponent />);
    expect(getByTestId('data')).toHaveTextContent('');
    act(() => {
      fireEvent.click(getByTestId('button'));
    });
    expect(getByTestId('data')).toHaveTextContent(toBeValue);
  });

  it('expect to useLocalstorage to update the data that has function as value', () => {
    const { getByTestId } = render(<MockComponent2 />);
    expect(getByTestId('data2')).toHaveTextContent('');
    act(() => {
      fireEvent.click(getByTestId('button2'));
    });
    expect(getByTestId('data2')).toHaveTextContent(toBeValue);
  });
});
