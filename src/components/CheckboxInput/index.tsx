import { forwardRef } from 'react';
import { Switch } from '@headlessui/react';
import cn from 'classnames';

import {
  CheckboxUnselected,
  CheckboxSelected,
  CheckboxSelectedDisabled,
  CheckboxUnselectedDisabled
} from '../../icons';

import './styles.scss';

/**
 * Types
 */
type CheckboxInputProps = {
  label?: string;
  defaultChecked?: boolean;
  checked?: boolean;
  onChange?: (checked: boolean) => void;
  name?: string;
  testId?: string;
  id?: string;
  disabled?: boolean;
  className?: string;
};

/**
 * Component
 */
const CheckboxInput = forwardRef<HTMLButtonElement, CheckboxInputProps>(
  (props, ref) => {
    /**
     * Props
     */
    const {
      label,
      defaultChecked,
      checked,
      onChange,
      name,
      testId,
      id,
      disabled,
      className
    } = props;

    /**
     * Render
     */
    return (
      <Switch
        className={cn('checkbox-input', className, { disabled })}
        defaultChecked={defaultChecked}
        checked={checked}
        onChange={onChange}
        name={name}
        data-testid={testId}
        id={id ?? name}
        ref={ref}
        disabled={disabled}
      >
        {({ checked }) => (
          <>
            {disabled ? (
              <div className="checkbox-input__icon-wrapper">
                {checked ? (
                  <CheckboxSelectedDisabled className="checkbox-input__icon" />
                ) : (
                  <CheckboxUnselectedDisabled className="checkbox-input__icon" />
                )}
              </div>
            ) : (
              <div className="checkbox-input__icon-wrapper">
                {checked ? (
                  <CheckboxSelected className="checkbox-input__icon" />
                ) : (
                  <CheckboxUnselected className="checkbox-input__icon" />
                )}
              </div>
            )}
            <span className="checkbox-input__label">{label}</span>
          </>
        )}
      </Switch>
    );
  }
);

export default CheckboxInput;
