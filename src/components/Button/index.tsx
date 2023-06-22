import { forwardRef, ReactNode } from 'react';
import cn from 'classnames';

import './styles.scss';

/**
 * Types
 */
interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  icon?: React.ReactNode;
  iconPosition?: 'left' | 'right';
  size?: 'default' | 'x-small' | 'small' | 'large';
  testId?: string;
  variant?:
    | 'primary'
    | 'secondary'
    | 'alternative'
    | 'text-link'
    | 'text-link-dark'
    | 'icon';
  label?: ReactNode;
}

/**
 * Component
 */
const Button = forwardRef<HTMLButtonElement, ButtonProps>((props, ref) => {
  /**
   * Props
   */
  const {
    icon,
    iconPosition = 'left',
    label,
    size = 'default',
    variant = 'primary',
    className,
    testId,
    ...rest
  } = props;

  return (
    <button
      className={cn('button', className, size, variant, {
        'icon-left': icon && iconPosition === 'left',
        'icon-right': icon && iconPosition === 'right'
      })}
      {...rest}
      ref={ref}
      data-testid={testId}
    >
      {icon && iconPosition === 'left' && icon}
      {label}
      {icon && iconPosition === 'right' && icon}
    </button>
  );
});

export default Button;
