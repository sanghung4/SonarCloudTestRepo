import { RefCallback, forwardRef } from 'react';
import { IMaskInput } from 'react-imask';
import cn from 'classnames';

import './styles.scss';
import {
  currencyMask,
  numberMask,
  phoneMask,
  zipcodeMask
} from '../../utils/masks';

/**
 * Constants
 */
const maskMap = {
  currency: currencyMask,
  number: numberMask,
  phone: phoneMask,
  zipcode: zipcodeMask
};

/**
 * Types
 */
export interface MaskedInputProps {
  label?: string;
  testId?: string;
  message?: string;
  error?: boolean;
  value: string;
  mask: keyof typeof maskMap;
  required?: boolean;
  placeholder?: string;
  className?: string;
  disabled?: boolean;
  id?: string;
  name?: string;
  maxLength?: number;
  pattern?: string;
  onChange?: (event: { target: { name: string; value: string } }) => void;
}

/**
 * Component
 */
const MaskedInput = forwardRef<HTMLInputElement, MaskedInputProps>(
  (props, ref) => {
    /**
     * Props
     */
    const {
      label,
      testId,
      message,
      error,
      value,
      mask,
      required,
      placeholder,
      className,
      disabled,
      id,
      name,
      maxLength,
      pattern,
      onChange,
      ...rest
    } = props;

    /**
     * Render
     */
    return (
      <div
        className={cn('masked-input', className, {
          disabled: disabled,
          error: error
        })}
      >
        {/* Label */}
        {label && (
          <label className="masked-input__label" htmlFor={id ?? name}>
            {label}
            {required && (
              <span className="masked-input__required-star"> *</span>
            )}
          </label>
        )}
        {/* Input Container */}
        <IMaskInput
          className="masked-input__input"
          inputRef={ref as RefCallback<HTMLInputElement>}
          {...maskMap[mask]}
          id={id ?? name}
          data-testid={testId}
          name={name}
          disabled={disabled}
          placeholder={placeholder}
          value={value}
          maxLength={maxLength}
          pattern={pattern}
          onAccept={(value: any) => {
            onChange?.({ target: { name: props.name ?? '', value } });
          }}
          overwrite
          {...rest}
        />
        {/* Message */}
        {(!!message || !!maxLength) && (
          <div className="masked-input__message-wrapper">
            {/* Message text */}
            {!!message && (
              <span className="masked-input__message">{message}</span>
            )}
            {/* Text length count */}
            {maxLength && (
              <span className="masked-input__count">
                {value.length ?? 0}/{maxLength}
              </span>
            )}
          </div>
        )}
      </div>
    );
  }
);

export default MaskedInput;
