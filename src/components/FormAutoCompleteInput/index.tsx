import {
  FieldValues,
  UseControllerProps,
  useController
} from 'react-hook-form';
import AutoCompleteInput, {
  AutoCompleteInputOption
} from 'components/AutoCompleteInput';

/**
 * Types
 */
interface FormAutoCompleteInputProps<TValue, TFields extends FieldValues>
  extends UseControllerProps<TFields> {
  label?: string;
  options: AutoCompleteInputOption<TValue>[];
  testId?: string;
  disabled?: boolean;
  required?: boolean;
  message?: string;
  placeholder?: string;
  className?: string;
}

/**
 * Component
 */
function FormAutoCompleteInput<
  TValue extends unknown,
  TFields extends FieldValues
>(props: FormAutoCompleteInputProps<TValue, TFields>) {
  /**
   * Props
   */
  const {
    label,
    options,
    testId,
    disabled,
    required,
    message,
    placeholder,
    className,
    ...rest
  } = props;

  /**
   * Custom Hooks
   */
  const {
    field,
    fieldState: { invalid, error }
  } = useController(rest);

  /**
   * Render
   */
  return (
    <AutoCompleteInput
      label={label}
      options={options}
      testId={testId}
      disabled={disabled}
      required={required}
      message={invalid ? error?.message : message}
      placeholder={placeholder}
      className={className}
      error={invalid || !!error?.message}
      {...field}
    />
  );
}

export default FormAutoCompleteInput;
