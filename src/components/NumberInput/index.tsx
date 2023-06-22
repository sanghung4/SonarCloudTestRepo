import {
  ButtonHTMLAttributes,
  ChangeEvent,
  CSSProperties,
  DetailedHTMLProps,
  InputHTMLAttributes,
  useEffect,
  useRef,
  useState
} from 'react';

import { Modify } from '@dialexa/reece-component-library';
import { Add, Remove } from '@mui/icons-material';
import cn from 'classnames';

import 'components/NumberInput/styles.scss';
import { isUndefined } from 'lodash-es';

/**
 * Constants
 */
const invalidChars = ['-', '+', 'e'];
const baseCn = 'number-input__';
const regexNonInteger = /[^\d-]/g;

/**
 * Type
 */
export type NumberInputSizes = 'large' | 'medium' | 'small' | 'extra-small';
export type ButtonProps = DetailedHTMLProps<
  ButtonHTMLAttributes<HTMLButtonElement>,
  HTMLButtonElement
> & { 'data-testid'?: string };
export type InputBaseProps = DetailedHTMLProps<
  InputHTMLAttributes<HTMLInputElement>,
  HTMLInputElement
>;
export type NumberInputProps = Modify<
  InputBaseProps,
  {
    allowzero?: boolean | string;
    buttonContainerStyle?: CSSProperties;
    'data-testid'?: string;
    downbuttonprops?: ButtonProps;
    fullWidth?: boolean;
    increment?: number;
    max?: number;
    min?: number;
    onBlur?: (newValue: number) => void;
    onClick?: (newValue: number) => void;
    size?: NumberInputSizes;
    sync?: boolean;
    upbuttonprops?: ButtonProps;
    value?: number;
  }
>;

/**
 * Component
 */
function NumberInput(props: NumberInputProps) {
  /**
   * Props
   */
  const {
    onClick,
    onBlur,
    disabled,
    fullWidth,
    max = Number.MAX_SAFE_INTEGER,
    size,
    value,
    increment,
    sync,
    buttonContainerStyle,
    downbuttonprops,
    upbuttonprops,
    ...other
  } = props;
  const defaultNumber = props?.allowzero ? 0 : 1;
  const min = props.min || defaultNumber;
  const parentValue = value || increment || defaultNumber;

  /**
   * Refs
   */
  const inputEl = useRef<HTMLInputElement>(null);

  /**
   * States
   */
  const [number, setNumber] = useState<number>(parentValue);
  const [lastValue, setLastValue] = useState<number>(parentValue);
  const [focusValue, setFocusValue] = useState<string>();
  const [changed, setChanged] = useState<boolean>(false);

  /**
   * Effects
   */
  // eslint-disable-next-line react-hooks/exhaustive-deps
  useEffect(syncParentValueEffect, [sync, value]);
  useEffect(preventInvalidInputKeyEffect, []);

  // Can't useEffect because that will cause input caching issue and infinite loops
  if (value && value !== lastValue) {
    setLastValue(value);
    setNumber(value);
  }

  /**
   * Render
   */
  return (
    <div className={cn(baseCn + 'container', fullWidth && 'full-width')}>
      <div
        className={cn(baseCn + 'button-container', disabled, size)}
        style={buttonContainerStyle}
      >
        <button
          disabled={disabled}
          onClick={() => handleClick(-1)}
          {...downbuttonprops}
          className={cn(baseCn + 'button', downbuttonprops?.className)}
        >
          <Remove />
        </button>
      </div>
      <input
        ref={inputEl}
        value={focusValue ?? number}
        onFocus={handleFocus}
        onChange={handleChange}
        onBlur={handleBlur}
        onKeyDown={handleEnter}
        {...other}
        disabled={disabled}
        className={cn(baseCn + 'input', size)}
      />
      <div
        className={cn(baseCn + 'button-container', disabled, size)}
        style={buttonContainerStyle}
      >
        <button
          disabled={disabled}
          onClick={() => handleClick(1)}
          className={cn(baseCn + 'button', upbuttonprops?.className)}
          {...upbuttonprops}
        >
          <Add />
        </button>
      </div>
    </div>
  );
  /**
   * Handles
   */
  // 🟤 handle - click +/-
  function handleClick(direction: 1 | -1) {
    inputEl.current?.blur();
    const toChangeTo = number + direction * (increment || 1);
    onClick?.(toChangeTo);
    setValidValue(toChangeTo);
  }
  // 🟤 handle - textbox onFocus
  function handleFocus() {
    setFocusValue(number.toString());
  }
  // 🟤 handle - textbox onChange
  function handleChange({ target: { value } }: ChangeEvent<HTMLInputElement>) {
    const filteredValue = value.replace(regexNonInteger, '');
    setChanged(true);
    if (isUndefined(props.max)) {
      setFocusValue(filteredValue);
      return;
    }
    const maxLength = max.toString().length;
    if (filteredValue.length <= maxLength) {
      setFocusValue(filteredValue);
    }
  }
  // 🟤 handle - textbox onBlur
  function handleBlur() {
    let newValue = parseInt(focusValue ?? '', 10) || lastValue || defaultNumber;
    if (changed && !focusValue) {
      newValue = defaultNumber;
    }

    onBlur?.(newValue);
    setValidValue(value || number);
    setNumber(newValue);
    setChanged(false);
    setFocusValue(undefined);
  }
  // 🟤 handle - textbox press "Enter" to save
  function handleEnter({ key }: React.KeyboardEvent<HTMLInputElement>) {
    if (key !== 'Enter') {
      return;
    }
    inputEl.current?.blur();
    handleBlur();
  }

  /**
   * Effect defs
   */
  // 🟡 effect - sync value from props (parent) if sync is true
  function syncParentValueEffect() {
    // istanbul ignore next
    if (sync && number !== parentValue) {
      setLastValue(number);
      setNumber(parentValue);
    }
  }
  // 🟡 effect - prevent `invalidChars` being inserted into the textbox
  function preventInvalidInputKeyEffect() {
    // istanbul ignore next
    if (inputEl.current) {
      inputEl.current.addEventListener('keydown', preventInvalidKeys);
    }
    return () => {
      // istanbul ignore next
      if (inputEl.current) {
        // eslint-disable-next-line react-hooks/exhaustive-deps
        inputEl.current.removeEventListener('keydown', preventInvalidKeys);
      }
    };
  }

  /**
   * Special Functions
   */
  // 🔣 util - filter and then setNumber
  function setValidValue(value: number) {
    // istanbul ignore next
    if (isNaN(value)) {
      setNumber(parentValue);
      return;
    }
    value = value < min ? min : value;
    value = value > max ? max : value;
    setNumber(value);
  }
  // 🔣 util - used by `preventInvalidInputKeyEffect`
  function preventInvalidKeys(e: KeyboardEvent) {
    if (invalidChars.includes(e.key)) {
      e.preventDefault();
    }
  }
}

export default NumberInput;
