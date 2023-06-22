import {
  FieldValues,
  UseControllerProps,
  useController
} from 'react-hook-form';
import SelectInput, { SelectInputOption } from 'components/SelectInput';

/**
 * Types
 */
interface FormSelectInputProps<TValue, TFields extends FieldValues>
  extends UseControllerProps<TFields> {
  label?: string;
  options: SelectInputOption<TValue>[];
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
function FormSelectInput<TValue extends unknown, TFields extends FieldValues>(
  props: FormSelectInputProps<TValue, TFields>
) {
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
    <SelectInput
      label={label}
      options={options}
      testId={testId}
      disabled={disabled}
      required={required}
      placeholder={placeholder}
      className={className}
      //@ts-ignore
      message={invalid ? error?.value?.message : message}
      //@ts-ignore
      // the reason this required  is bc the react hook form
      // library does not pass through the correct properties in the error variable
      error={!!error?.value?.message}
      {...field}
    />
  );
}

export default FormSelectInput;
