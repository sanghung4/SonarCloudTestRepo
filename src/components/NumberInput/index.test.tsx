import { useState } from 'react';

import NumberInput, { NumberInputProps } from 'components/NumberInput';
import {
  blurInput,
  changeInput,
  clickButton,
  focusInput,
  keyboardDown
} from 'test-utils/actionUtils';
import { render } from 'test-utils/TestWrapper';

/**
 * Types
 */
type DummyParentProp = {
  valueToSet?: number;
};

/**
 * Mock value
 */
let mocks: NumberInputProps = {};

/**
 * Dummy
 */
function DummyParent(props: NumberInputProps & DummyParentProp) {
  const { valueToSet, ...inputProps } = props;
  const [value, setValue] = useState(props.value);
  return (
    <>
      <NumberInput
        data-testid="input"
        upbuttonprops={{ 'data-testid': 'up' }}
        downbuttonprops={{ 'data-testid': 'down' }}
        {...inputProps}
        value={value}
      />
      <button
        onClick={() => setValue(valueToSet)}
        data-testid="change-parent-value-btn"
      />
    </>
  );
}

/**
 * Test setup
 */
function setup(props: NumberInputProps, valueToSet?: number) {
  return render(<DummyParent {...props} valueToSet={valueToSet} />);
}

/**
 * TEST
 */
describe('component/NumberInput', () => {
  // ⚪ Reset mocks
  afterEach(() => {
    mocks = {};
  });

  // 🟢 1 - Default
  it('expect sub-component to be rendered with default props', async () => {
    // act
    const { findByTestId } = setup(mocks);
    const input = await findByTestId('input');
    const upButton = await findByTestId('up');
    const downButton = await findByTestId('down');
    // assert
    expect(input).toHaveValue('1');
    expect(upButton).toBeInTheDocument();
    expect(downButton).toBeInTheDocument();
  });

  // 🟢 2 - allowzero
  it('expect input to have value 0 with allowzero', async () => {
    // arrange
    mocks.allowzero = 'true'; // passing boolean cause test to throw warnings
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
    // act 1
    const { findByTestId } = setup(mocks);
    const input = await findByTestId('input');
    // assert 1
    expect(input).toHaveValue('1');
    // act 2
    await clickButton('up');
    // assert 2
    expect(input).toHaveValue('2');
  });

  // 🟢 5 - minus button
  it('expect input value to be decreased with the click of the minus button', async () => {
    // arrange
    mocks.value = 2;
    // act 1
    const { findByTestId } = setup(mocks);
    const input = await findByTestId('input');
    // assert 1
    expect(input).toHaveValue('2');
    // act 2
    await clickButton('down');
    // assert 2
    expect(input).toHaveValue('1');
  });

  // 🟢 6 - plus button (10 incremental)
  it('expect incremental value to be default and incremented with the click of the plus button', async () => {
    // arrange
    mocks.increment = 10;
    // act 1
    const { findByTestId } = setup(mocks);
    const input = await findByTestId('input');
    // assert 1
    expect(input).toHaveValue('10');
    // act 2
    await clickButton('up');
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
    await clickButton('up');
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
    await clickButton('down');
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

  // 🟢 12 - Input text (blur only)
  it('expect value to stay the same with blur event', async () => {
    // arrange
    mocks.allowzero = 'true';
    // act 1
    const { findByTestId } = setup(mocks);
    const input = await findByTestId('input');
    // assert 1
    expect(input).toHaveValue('0');
    // act 2
    await blurInput('input');
    // assert 2
    expect(input).toHaveValue('0');
  });

  // 🟢 13 - Input text (change and blur + max cap)
  it('expect value to stay the same with change and blur event with max cap', async () => {
    // arrange
    mocks.allowzero = 'true';
    mocks.max = 200;
    // act 1
    const { findByTestId } = setup(mocks);
    const input = await findByTestId('input');
    // assert 1
    expect(input).toHaveValue('0');
    // act 2
    await changeInput('input', '1000');
    await blurInput('input');
    // assert 2
    expect(input).toHaveValue('0');
  });

  // 🟢 14 - Input text with keypress
  it('expect input value to be updated with keypress', async () => {
    // act 1
    const { findByTestId } = setup(mocks);
    const input = await findByTestId('input');
    // assert 1
    expect(input).toHaveValue('1');
    // act 2
    await focusInput('input');
    await changeInput('input', '10');
    await keyboardDown('1', input);
    await keyboardDown('e', input);
    await keyboardDown('Enter', input);
    // assert 2
    expect(input).toHaveValue('10');
  });

  // 🟢 15 - Change parent value
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
});
