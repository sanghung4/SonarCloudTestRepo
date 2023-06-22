import {
  FieldValues,
  UseControllerProps,
  useController
} from 'react-hook-form';
import RadioInput, { RadioInputOption } from 'components/RadioInput';

/**
 * Types
 */
interface FormRadioInputProps<TFields extends FieldValues>
  extends UseControllerProps<TFields> {
  label?: string;
  options: RadioInputOption[];
  testId?: string;
  disabled?: boolean;
  required?: boolean;
  message?: string;
  className?: string;
}

/**
 * Component
 */
function FormRadioInput<TFields extends FieldValues>(
  props: FormRadioInputProps<TFields>
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
    <RadioInput
      label={label}
      options={options}
      testId={testId}
      disabled={disabled}
      required={required}
      message={invalid ? error?.message : message}
      className={className}
      error={invalid || !!error?.message}
      {...field}
    />
  );
}

export default FormRadioInput;
