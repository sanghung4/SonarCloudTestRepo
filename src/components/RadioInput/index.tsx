import { forwardRef } from 'react';
import { RadioGroup } from '@headlessui/react';
import cn from 'classnames';

import './styles.scss';
import {
  RadioSelected,
  RadioSelectedDisabled,
  RadioUnselected,
  RadioUnselectedDisabled
} from '../../icons';

/**
 * Types
 */
export type RadioInputOption = { label: string; value: string };

type RadioInputProps = {
  value: string | null;
  onChange: (value: string) => void;
  options: RadioInputOption[];
  label?: string;
  required?: boolean;
  className?: string;
  message?: string;
  error?: boolean;
  disabled?: boolean;
  name?: string;
  id?: string;
  testId?: string;
};

/**
 * Component
 */
const RadioInput = forwardRef<HTMLInputElement, RadioInputProps>(
  (props, ref) => {
    /**
     * Props
     */
    const {
      value,
      onChange,
      options,
      label,
      required,
      className,
      message,
      error,
      disabled,
      id,
      name,
      testId
    } = props;

    /**
     * Render
     */
    return (
      <RadioGroup
        className={cn('radio-input', className, {
          disabled: disabled,
          error: error
        })}
        disabled={disabled}
        value={value}
        onChange={onChange}
        id={id ?? name}
        name={name}
        data-testid={testId}
        ref={ref}
      >
        {/* Label and Options */}
        <div className="radio-input__wrapper">
          {/* Label */}
          {label && (
            <RadioGroup.Label className="radio-input__label">
              {label}
              {required && (
                <span className="radio-input__required-star"> *</span>
              )}
            </RadioGroup.Label>
          )}
          {/* Options */}
          <div className="radio-input__options">
            {options.map((opt, index) => (
              <RadioGroup.Option
                className="radio-input__option"
                value={opt.value}
                key={`radio-option-${index}`}
              >
                {({ active, checked }) => (
                  <>
                    {disabled ? (
                      <div className="radio-input__option__icon-wrapper">
                        {checked ? (
                          <RadioSelectedDisabled className="radio-input__option__icon disabled" />
                        ) : (
                          <RadioUnselectedDisabled className="radio-input__option__icon disabled" />
                        )}
                      </div>
                    ) : (
                      <div className="radio-input__option__icon-wrapper">
                        {checked || active ? (
                          <RadioSelected className="radio-input__option__icon" />
                        ) : (
                          <RadioUnselected className="radio-input__option__icon" />
                        )}
                      </div>
                    )}

                    {opt.label}
                  </>
                )}
              </RadioGroup.Option>
            ))}
          </div>
        </div>
        {/* Message */}
        {!!message && (
          <div className="radio-input__message-wrapper">
            <span className="radio-input__message">{message}</span>
          </div>
        )}
      </RadioGroup>
    );
  }
);

export default RadioInput;
