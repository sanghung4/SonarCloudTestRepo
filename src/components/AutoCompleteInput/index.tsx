import { Ref, forwardRef, useEffect, useMemo, useState } from 'react';
import { Combobox } from '@headlessui/react';
import cn from 'classnames';

import './styles.scss';
import { ArrowDropDownIcon, ChevronRightIcon } from '../../icons';

/**
 * Types
 */
export type AutoCompleteInputOption<TValue> = { label: string; value: TValue };

type AutoCompleteInputProps<TValue> = {
  label?: string;
  required?: boolean;
  disabled?: boolean;
  message?: string;
  value: AutoCompleteInputOption<TValue> | null;
  options: AutoCompleteInputOption<TValue>[];
  onChange: (value: AutoCompleteInputOption<TValue>) => void;
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
const AutoCompleteInput = forwardRef(
  <TValue extends unknown>(
    props: AutoCompleteInputProps<TValue>,
    ref: Ref<HTMLInputElement>
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
     * useState
     */
    const [query, setQuery] = useState('');

    /**
     * useMemos
     */
    const filteredOptions = useMemo(filteredOptionsMemo, [options, query]);

    /**
     * useEffects
     */
    useEffect(clearQuery, [value]);

    /**
     * Render
     */
    return (
      <Combobox
        as="div"
        className={({ open }) =>
          cn('auto-complete-input', className, {
            disabled: disabled,
            error: error,
            open: open
          })
        }
        disabled={disabled}
        value={value}
        onChange={onChange}
        data-testid={testId}
        name={name}
      >
        {/* Input and Options */}
        <div className="auto-complete-input__wrapper">
          {/* Label */}
          {label && (
            <Combobox.Label className="auto-complete-input__label">
              {label}
              {required && (
                <span className="auto-complete-input__required-star"> *</span>
              )}
            </Combobox.Label>
          )}
          {/* Input */}
          {/* Button is used so that the autocomplete opens whenever it is focused */}
          <Combobox.Button as="div" className="auto-complete-input__button">
            <Combobox.Input
              required={required}
              onChange={(event) => setQuery(event.target.value)}
              className="auto-complete-input__input"
              displayValue={(opt: AutoCompleteInputOption<TValue> | null) =>
                opt?.label ?? ''
              }
              placeholder={placeholder}
              id={id}
              ref={ref}
            />
            <ArrowDropDownIcon className="auto-complete-input__input-icon" />
          </Combobox.Button>
          {/* Options */}
          {!!filteredOptions.length && (
            <Combobox.Options className="auto-complete-input__options">
              {filteredOptions.map((option, index) => (
                <Combobox.Option
                  className={({ active }) =>
                    cn('auto-complete-input__option', { active })
                  }
                  value={option}
                  key={index}
                >
                  {option.label}
                  <ChevronRightIcon className="auto-complete-input__option__arrow" />
                </Combobox.Option>
              ))}
            </Combobox.Options>
          )}
        </div>
        {/* Message */}
        {!!message && (
          <div className="auto-complete-input__message-wrapper">
            <span className="auto-complete-input__message">{message}</span>
          </div>
        )}
      </Combobox>
    );

    /**
     * Hoisted useMemo Definitions
     */
    function filteredOptionsMemo() {
      // If a query is entered, filter the options based on their labels
      if (query) {
        return options.filter(({ label }) =>
          label.toLowerCase().includes(query.toLowerCase())
        );
      }

      return options;
    }

    /**
     * Hoisted useEffect Definitions
     */
    function clearQuery() {
      setQuery('');
    }
  }
);

export default AutoCompleteInput as <T extends unknown>(
  props: AutoCompleteInputProps<T> & { ref: Ref<HTMLInputElement> }
) => JSX.Element;
