import {
  FieldValues,
  UseControllerProps,
  useController
} from 'react-hook-form';
import TextInput, { TextInputProps } from 'components/TextInput';

/**
 * Types
 */
interface FormTextInputProps<TFields extends FieldValues>
  extends UseControllerProps<TFields> {
  label?: string;
  testId?: string;
  disabled?: boolean;
  required?: boolean;
  message?: string;
  placeholder?: string;
  className?: string;
  type?: TextInputProps['type'];
  maxLength?: number
}

/**
 * Component
 */
function FormTextInput<TFields extends FieldValues>(
  props: FormTextInputProps<TFields>
) {
  /**
   * Props
   */
  const {
    label,
    testId,
    disabled,
    required,
    message,
    placeholder,
    className,
    type,
    maxLength,
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
    <TextInput
      label={label}
      testId={testId}
      disabled={disabled}
      required={required}
      placeholder={placeholder}
      className={className}
      type={type}
      message={invalid ? error?.message : message}
      error={!!error?.message}
      maxLength={maxLength}
      {...field}
    />
  );
}

export default FormTextInput;
