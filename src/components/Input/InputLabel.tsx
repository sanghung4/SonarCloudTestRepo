import clsx from 'clsx';
import { InputLabelProps } from 'components/Input';

function InputLabel(props: InputLabelProps) {
  /**
   * Props
   */
  const { className, 'data-testid': testId, label, required } = props;

  /**
   * Render
   */
  return (
    <label
      className={clsx('mb-2 text-neutral-700 text-normal text-base', className)}
      data-testid={testId && `${testId}-label`}
    >
      {label}
      {required && <span className="text-support-2-100">*</span>}
    </label>
  );
}

export default InputLabel;
