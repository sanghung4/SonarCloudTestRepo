import {
  FieldValues,
  UseControllerProps,
  useController
} from 'react-hook-form';
import MaskedInput, { MaskedInputProps } from 'components/MaskedInput';

/**
 * Types
 */
interface FormMaskedInputProps<TFields extends FieldValues>
  extends UseControllerProps<TFields> {
  label?: string;
  testId?: string;
  disabled?: boolean;
  required?: boolean;
  message?: string;
  placeholder?: string;
  className?: string;
  mask: MaskedInputProps['mask'];
  pattern?: string;
}

/**
 * Component
 */
function FormMaskedInput<TFields extends FieldValues>(
  props: FormMaskedInputProps<TFields>
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
    mask,
    pattern,
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
    <MaskedInput
      label={label}
      testId={testId}
      disabled={disabled}
      required={required}
      placeholder={placeholder}
      className={className}
      mask={mask}
      pattern={pattern}
      message={invalid ? error?.message : message}
      error={!!error?.message}
      {...field}
    />
  );
}

export default FormMaskedInput;
