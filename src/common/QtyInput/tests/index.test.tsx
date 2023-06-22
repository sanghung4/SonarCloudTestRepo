import { useState } from 'react';

import QtyInput, { QtyInputProps } from 'common/QtyInput';
import { render } from 'test-utils/TestWrapper';
import {
  blurInput,
  changeInput,
  clickButton,
  focusInput
} from 'test-utils/actionUtils';
import { act } from '@testing-library/react';

/**
 * Types
 */
type MockMinQtyDialogProps = {
  onClose: () => void;
  onConfirm: () => void;
  open: boolean;
};
type DummyParentProp = {
  valueToSet?: number;
};

/**
 * Mocks
 */
const defaultQtyInputProps: QtyInputProps = {
  value: 0,
  onUpdate: jest.fn(),
  productName: 'cheese'
};
let mocks: QtyInputProps = { ...defaultQtyInputProps };

/**
 * Dummy
 */
function DummyParent(props: QtyInputProps & DummyParentProp) {
  const { valueToSet, ...inputProps } = props;
  const [value, setValue] = useState(props.value);
  return (
    <>
      <QtyInput data-testid="input" {...inputProps} value={value} />
      <button
        onClick={() => setValue(valueToSet ?? 0)}
        data-testid="change-parent-value-btn"
      />
    </>
  );
}
function MockMinimumQuantityDialog(props: MockMinQtyDialogProps) {
  if (!props.open) {
    return null;
  }
  return <button data-testid="dialog-confirm" onClick={props.onConfirm} />;
}

/**
 * Mock Methods
 */
jest.mock('common/QtyInput/MinimumQuantityDialog', () => ({
  ...jest.requireActual('common/QtyInput/MinimumQuantityDialog'),
  __esModule: true,
  default: MockMinimumQuantityDialog
}));

/**
 * Test setup
 */
function setup(props: QtyInputProps, valueToSet?: number) {
  return render(<DummyParent {...props} valueToSet={valueToSet} />);
}

/**
 * Test
 */
describe('Common - QtyInput', () => {
  // ⚪ Reset mocks
  afterEach(() => {
    mocks = { ...defaultQtyInputProps };
  });

  // 🟢 1 - Default
  it('expect sub-component to be rendered with default props', async () => {
    // act
    const { findByTestId } = setup(mocks);
    const input = await findByTestId('input');
    const upButton = await findByTestId('quantity-input-up');
    const downButton = await findByTestId('quantity-input-down');
    // assert
    expect(input).toHaveValue('1');
    expect(upButton).toBeInTheDocument();
    expect(downButton).toBeInTheDocument();
  });

  // 🟢 2 - allowzero
  it('expect input to have value 0 with allowzero', async () => {
    // arrange
    mocks.allowZero = 'true'; // passing boolean cause test to throw warnings
    mocks.increment = 0;
    // act
    const { findByTestId } = setup(mocks);
    const input = await findByTestId('input');
    // assert
    expect(input).toHaveValue('0');
  });

  // 🟢 3 - fullWidth
  it('expect full-width className to be found', async () => {
    // arrange
    mocks.fullWidth = true;
    // act
    const { container } = setup(mocks);
    const input = container.getElementsByClassName('full-width');
    // assert
    expect(input.length).toBeTruthy();
  });

  // 🟢 4 - plus button
  it('expect input value to be increased with the click of the plus button', async () => {
    // arrange
    mocks.noDebounce = true;
    // act 1
    const { findByTestId } = setup(mocks);
    const input = await findByTestId('input');
    // assert 1
    expect(input).toHaveValue('1');
    // act 2
    await clickButton('quantity-input-up');
    // assert 2
    expect(input).toHaveValue('2');
  });

  // 🟢 5 - minus button
  it('expect input value to be decreased with the click of the minus button', async () => {
    // arrange
    mocks.noDebounce = true;
    // arrange
    mocks.value = 2;
    // act 1
    const { findByTestId } = setup(mocks);
    const input = await findByTestId('input');
    // assert 1
    expect(input).toHaveValue('2');
    // act 2
    await clickButton('quantity-input-down');
    // assert 2
    expect(input).toHaveValue('1');
  });

  // 🟢 6 - plus button (10 incremental)
  it('expect incremental value to be default and incremented with the click of the plus button', async () => {
    // arrange
    mocks.increment = 10;
    mocks.noDebounce = true;
    // act 1
    const { findByTestId } = setup(mocks);
    const input = await findByTestId('input');
    // assert 1
    expect(input).toHaveValue('10');
    // act 2
    await clickButton('quantity-input-up');
    // assert 2
    expect(input).toHaveValue('20');
  });

  // 🟢 7 - plus button cap
  it('expect input value to be max with the click of the plus button at max', async () => {
    // arrange
    mocks.max = 1;
    // act 1
    const { findByTestId } = setup(mocks);
    const input = await findByTestId('input');
    // assert 1
    expect(input).toHaveValue('1');
    // act 2
    await clickButton('quantity-input-up');
    // assert 2
    expect(input).toHaveValue('1');
  });

  // 🟢 8 - minus button cap
  it('expect input value to be min with the click of the minus button at min', async () => {
    // arrange
    mocks.min = 1;
    // act 1
    const { findByTestId } = setup(mocks);
    const input = await findByTestId('input');
    // assert 1
    expect(input).toHaveValue('1');
    // act 2
    await clickButton('quantity-input-down');
    // assert 2
    expect(input).toHaveValue('1');
  });

  // 🟢 9 - Input text
  it('expect input value to be updated', async () => {
    // act 1
    const { findByTestId } = setup(mocks);
    const input = await findByTestId('input');
    // assert 1
    expect(input).toHaveValue('1');
    // act 2
    await focusInput('input');
    await changeInput('input', '10');
    await blurInput('input');
    // assert 2
    expect(input).toHaveValue('10');
  });

  // 🟢 10 - Input text with max
  it('expect input value to be updated with max value', async () => {
    // arrange
    mocks.max = 200;
    // act 1
    const { findByTestId } = setup(mocks);
    const input = await findByTestId('input');
    // assert 1
    expect(input).toHaveValue('1');
    // act 2
    await focusInput('input');
    await changeInput('input', '10');
    await blurInput('input');
    // assert 2
    expect(input).toHaveValue('10');
  });

  // 🟢 11 - Input text max cap
  it('expect input value not to be updated with max cap', async () => {
    // arrange
    mocks.max = 200;
    // act 1
    const { findByTestId } = setup(mocks);
    const input = await findByTestId('input');
    // assert 1
    expect(input).toHaveValue('1');
    // act 2
    await focusInput('input');
    await changeInput('input', '1000');
    await blurInput('input');
    // assert 2
    expect(input).toHaveValue('1');
  });

  // 🟢 9 - Input text (letter)
  it('Expect input only take numbers', async () => {
    // act 1
    const { findByTestId } = setup(mocks);
    const input = await findByTestId('input');
    // assert 1
    expect(input).toHaveValue('1');
    // act 2
    await focusInput('input');
    await changeInput('input', '12e3');
    await blurInput('input');
    // assert 2
    expect(input).toHaveValue('123');
  });

  // 🟢 10 - Input text (blank)
  it('Expect input allows blank while editing', async () => {
    // act 1
    const { findByTestId } = setup(mocks);
    const input = await findByTestId('input');
    await focusInput('input');
    await changeInput('input', '');
    // assert 1
    expect(input).not.toHaveValue('1');
    // act 2
    await blurInput('input');
    // assert 2
    expect(input).toHaveValue('1');
  });

  // 🟢 11 - Change parent value
  it('expect input value to be updated when parent value changes', async () => {
    // arrange
    mocks.value = 2;
    mocks.sync = true;
    // act 1
    const { findByTestId } = setup(mocks, 5);
    const input = await findByTestId('input');
    // assert 1
    expect(input).toHaveValue('2');
    // act 2
    await clickButton('change-parent-value-btn');
    // assert 2
    expect(input).toHaveValue('5');
  });

  // 🟢 12 - Input text
  it("expect qty increment adjument dialog when value input doesn't match", async () => {
    // arrange
    mocks.increment = 5;
    // act 1
    const { findByTestId } = setup(mocks);
    const input = await findByTestId('input');
    // assert 1
    expect(input).toHaveValue('5');
    // act 2
    await focusInput('input');
    await changeInput('input', '11');
    await blurInput('input');
    const dialog = await findByTestId('dialog-confirm');
    // assert 2
    expect(dialog).toBeInTheDocument();
    // act 3
    await clickButton('dialog-confirm');
    // assert 3
    expect(dialog).not.toBeInTheDocument();
    expect(input).toHaveValue('15');
  });

  // 🟢 13 - debounce
  it('expect debouncer function to be called', async () => {
    // arrange
    mocks.max = 1;
    mocks.onUpdate = jest.fn();
    // act
    setup(mocks);
    await clickButton('quantity-input-up');
    await act(() => new Promise((res) => setTimeout(res, 500)));
    // assert 2
    expect(mocks.onUpdate).toBeCalled();
  });

  // 🟢 14 - Different testid
  it('expect sub-component to be rendered with different testids', async () => {
    // act
    const { findByTestId } = render(<QtyInput value={0} index={1} />);
    const input = await findByTestId('quantity-input-1');
    const upButton = await findByTestId('quantity-input-up-1');
    const downButton = await findByTestId('quantity-input-down-1');
    // assert
    expect(input).toHaveValue('1');
    expect(upButton).toBeInTheDocument();
    expect(downButton).toBeInTheDocument();
  });
});
