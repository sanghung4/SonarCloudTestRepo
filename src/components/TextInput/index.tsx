import cn from 'classnames';

import './styles.scss';
import { MouseEvent, forwardRef, useState } from 'react';
import { PasswordHide, PasswordShow } from '../../icons';

/**
 * Types
 */
export interface TextInputProps
  extends React.InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  leftIcon?: React.ReactNode;
  testId?: string;
  action?: { icon: React.ReactNode; onClick: () => void };
  message?: string;
  error?: boolean;
  value: string;
}

/**
 * Component
 */
const TextInput = forwardRef<HTMLInputElement, TextInputProps>((props, ref) => {
  /**
   * Props
   */
  const {
    label,
    name,
    leftIcon,
    disabled,
    message,
    value,
    required,
    id,
    testId,
    action,
    maxLength,
    error,
    className,
    type,
    ...rest
  } = props;

  /**
   * useState
   */
  const [showPassword, setShowPassword] = useState(false);

  /**
   * Render
   */
  return (
    <div
      className={cn('text-input', className, {
        disabled: disabled,
        error: error
      })}
    >
      {/* Label */}
      {label && (
        <label className="text-input__label" htmlFor={id ?? name}>
          {label}
          {required && <span className="text-input__required-star"> *</span>}
        </label>
      )}
      {/* Input Container */}
      <div className="text-input__wrapper">
        {leftIcon && <div className="text-input__icon-wrapper">{leftIcon}</div>}
        <input
          className={cn('text-input__input', {
            'left-icon': !!leftIcon,
            action: action,
            password: type === 'password'
          })}
          id={id ?? name}
          name={name}
          disabled={disabled}
          value={value}
          maxLength={maxLength}
          ref={ref}
          type={showPassword ? 'text' : type}
          data-testid={testId}
          {...rest}
        />
        {type === 'password' && (
          <button
            className="text-input__show-password"
            onClick={handlePasswordToggle}
            aria-label="show-password"
            type="button"
          >
            {showPassword ? <PasswordShow /> : <PasswordHide />}
          </button>
        )}

        {!!action && (
          <button className="text-input__action" onClick={action.onClick}>
            {action.icon}
          </button>
        )}
      </div>
      {/* Message */}
      {(!!message || !!maxLength) && (
        <div className="text-input__message-wrapper">
          {/* Message text */}
          {!!message && <span className="text-input__message">{message}</span>}
          {/* Text length count */}
          {maxLength && (
            <span className="text-input__count">
              {value?.length ?? 0}/{maxLength}
            </span>
          )}
        </div>
      )}
    </div>
  );

  /**
   * Callback Definitions
   */
  function handlePasswordToggle(e: MouseEvent<HTMLButtonElement>) {
    e.preventDefault();
    setShowPassword((prev) => !prev);
  }
});

export default TextInput;
