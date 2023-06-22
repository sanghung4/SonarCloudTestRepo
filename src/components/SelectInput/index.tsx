import { Listbox } from '@headlessui/react';
import cn from 'classnames';
import './styles.scss';
import { ArrowDropDownIcon, ChevronRightIcon } from '../../icons';
import { Ref, forwardRef } from 'react';

/**
 * Types
 */
export type SelectInputOption<TValue> = { label: string; value: TValue };

type SelectInputProps<TValue> = {
  label?: string;
  required?: boolean;
  disabled?: boolean;
  message?: string;
  value: SelectInputOption<TValue> | null;
  options: SelectInputOption<TValue>[];
  onChange: (value: SelectInputOption<TValue>) => void;
  testId?: string;
  id?: string;
  placeholder?: string;
  name?: string;
  error?: boolean;
  className?: string;
};

/**
 * Component
 */
const SelectInput = forwardRef(
  <TValue extends unknown>(
    props: SelectInputProps<TValue>,
    ref: Ref<HTMLButtonElement>
  ) => {
    /**
     * Props
     */
    const {
      disabled,
      label,
      required,
      message,
      value,
      onChange,
      options,
      testId,
      id,
      placeholder = 'Select',
      name,
      error,
      className
    } = props;

    /**
     * Render
     */
    return (
      <Listbox
        className={({ open }) =>
          cn('select-input', className, { open, error, disabled })
        }
        disabled={disabled}
        as="div"
        name={name}
        id={id}
        onChange={onChange}
        value={value}
        data-testid={testId}
      >
        {/* Input and Options */}
        <div className="select-input__wrapper">
          {/* Label */}
          {label && (
            <Listbox.Label className="select-input__label">
              {label}
              {required && (
                <span className="select-input__required-star"> *</span>
              )}
            </Listbox.Label>
          )}
          <Listbox.Button
            ref={ref}
            className={({ open }) => cn('select-input__button', { open })}
          >
            {({ value }) => (
              <>
                {value?.label ?? placeholder}
                <ArrowDropDownIcon className="select-input__button-icon" />
              </>
            )}
          </Listbox.Button>

          {options.length && (
            <Listbox.Options className="select-input__options">
              {options.map((option, index) => (
                <Listbox.Option
                  value={option}
                  className={({ active }) =>
                    cn('select-input__option', { active })
                  }
                  key={index}
                >
                  {option.label}
                  <ChevronRightIcon className="select-input__option__arrow" />
                </Listbox.Option>
              ))}
            </Listbox.Options>
          )}
        </div>
        {/* Message */}
        {!!message && (
          <div className="select-input__message-wrapper">
            <span className="select-input__message">{message}</span>
          </div>
        )}
      </Listbox>
    );
  }
);

export default SelectInput as <T extends unknown>(
  props: SelectInputProps<T> & { ref: Ref<HTMLButtonElement> }
) => JSX.Element;
