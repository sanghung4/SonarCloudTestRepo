import { useState } from 'react';

import clsx from 'clsx';
import { MdRefresh } from 'react-icons/md';

import { InputLabel, InputProps } from 'components/Input';
import { ReactComponent as ShowPassword } from 'resources/icons/password-show.svg';
import { ReactComponent as HidePassword } from 'resources/icons/password-hide.svg';

/**
 * Consts
 */
export const helperTextColors = {
  success: 'text-green-500',
  error: 'text-red-500',
  neutral: 'text-neutral-600'
};

function Input(props: InputProps) {
  /**
   * Props
   */
  const {
    endIcon,
    className,
    inputClassName,
    label,
    labelClassName,
    status: propsStatus,
    helperText,
    disabled,
    readOnly,
    loading,
    type,
    noHelper,
    ...rest
  } = props;
  const status = propsStatus ?? 'neutral';
  const testId = rest['data-testid'];
  const isError = status === 'error';

  /**
   * State
   */
  const [showPassword, setShowPassword] = useState(false);

  /**
   * Render
   */
  return (
    <div className={clsx('flex flex-col h-max', className)}>
      {/* Label */}
      <InputLabel
        label={label}
        data-testid={testId}
        required={rest.required}
        className={labelClassName}
      />
      {/* Input and Icon */}
      <div className="relative">
        <input
          className={clsx(
            'w-full bg-white border px-4 py-[11px] rounded',
            inputClassName,
            {
              'border-common-error focus:outline-none': isError,
              'focus:outline-reece-500 border-common-border': !isError,
              'text-secondary-2-30 bg-secondary-2-10': disabled,
              'text-primary-3-100 bg-secondary-2-10': readOnly,
              'pr-10': endIcon || loading
            }
          )}
          disabled={disabled}
          readOnly={readOnly}
          type={
            type === 'password' ? (showPassword ? 'text' : 'password') : type
          }
          {...rest}
        />
        {type === 'password' && (
          <button
            type="button"
            className="absolute right-[16px] top-0 bottom-0"
            disabled={disabled}
            onClick={() => setShowPassword(!showPassword)}
            data-testid={testId && `${testId}-toggle-password`}
          >
            <span
              className={clsx(
                { 'text-gray-400': disabled },
                { 'text-primary-1-100': !disabled }
              )}
            >
              {showPassword ? <ShowPassword /> : <HidePassword />}
            </span>
          </button>
        )}
        {(endIcon || loading) && (
          <div className="absolute right-2 top-0 flex items-center text-2xl h-full text-gray-400">
            {loading ? (
              <MdRefresh
                className="animate-spin"
                data-testid={testId && `${testId}-loader`}
              />
            ) : (
              endIcon
            )}
          </div>
        )}
      </div>
      {/* Helper Text */}
      {!noHelper && (
        <p
          className={clsx(
            "pl-2 mt-1 text-xs before:content-[''] before:inline-block",
            helperTextColors[status]
          )}
          data-testid={testId && `${testId}-helper-text`}
        >
          {helperText ?? ' '}
        </p>
      )}
    </div>
  );
}

export default Input;
