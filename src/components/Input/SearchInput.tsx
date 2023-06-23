import { FormEvent, useState } from 'react';

import clsx from 'clsx';

import {
  helperTextColors,
  InputLabel,
  SearchInputProps
} from 'components/Input';
import { ReactComponent as SearchIcon } from 'resources/icons/search.svg';
import LoadingIcon from 'components/LoadingIcon/LoadingIcon';

function SearchInput({ onSearch, ...props }: SearchInputProps) {
  /**
   * props
   */
  const {
    className,
    inputClassName,
    label,
    status: propsStatus,
    helperText,
    disabled,
    readOnly,
    loading,
    type,
    noHelper,
    value: defaultValue,
    ...rest
  } = props;
  const status = propsStatus ?? 'neutral';
  const testId = rest['data-testid'];
  const isError = status === 'error';

  /**
   * State
   */
  const [value, setValue] = useState(defaultValue?.toString() ?? '');

  /**
   * Callback
   */
  const onSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    onSearch(value);
  };

  /**
   * Render
   */
  return (
    <form onSubmit={onSubmit}>
      <div className={clsx('flex flex-col h-max', className)}>
        {/* Label */}
        <InputLabel
          label={label}
          data-testid={testId}
          required={rest.required}
        />
        {/* Input and Button */}
        <div className="relative pr-12">
          <input
            className={clsx(
              'w-full bg-white border px-4 py-[11px] rounded-l',
              inputClassName,
              {
                'border-common-error focus:outline-none': isError,
                'focus:outline-reece-500 border-common-border': !isError,
                'text-secondary-2-30 bg-secondary-2-10': disabled,
                'text-primary-3-100 bg-secondary-2-10': readOnly
              }
            )}
            disabled={disabled}
            readOnly={readOnly}
            type="search"
            value={value}
            onChange={(e) => setValue(e.currentTarget.value)}
            {...rest}
          />
          <span className="absolute inset-y-0 right-0 flex items-center">
            <button
              type="submit"
              disabled={!value || disabled}
              data-testid={testId && `${testId}-submit`}
              className={clsx(
                'px-4 py-[15px] bg-primary-1-100 text-white rounded-r',
                'disabled:bg-secondary-2-30 disabled:text-secondary-2-100',
                { 'bg-common-error': isError }
              )}
            >
              {loading ? (
                <LoadingIcon
                  size={18}
                  data-testid={testId && `${testId}-loading`}
                />
              ) : (
                <SearchIcon />
              )}
            </button>
          </span>
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
    </form>
  );
}
export default SearchInput;
