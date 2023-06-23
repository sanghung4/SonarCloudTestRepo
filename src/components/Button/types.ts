import { ButtonHTMLAttributes, DetailedHTMLProps, ReactNode } from 'react';

export type ButtonSizes = 'base' | 'sm' | 'lg';

export type ButtonProps = DetailedHTMLProps<
  ButtonHTMLAttributes<HTMLButtonElement>,
  HTMLButtonElement
> & {
  size?: ButtonSizes;
  disabled?: boolean;
  className?: string;
  loading?: boolean;
  title?: string;
  icon?: ReactNode;
  iconPosition?: 'left' | 'right';
  align?: 'justify-center' | 'justify-start';
  'data-testid'?: string;
};
