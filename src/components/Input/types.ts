import { ChangeEventHandler } from 'react';

export type InputChangeHandler = ChangeEventHandler<HTMLInputElement>;

export type InputProps = React.ComponentPropsWithoutRef<'input'> & {
  label?: string;
  status?: 'error' | 'success' | 'neutral';
  helperText?: string;
  endIcon?: React.ReactNode;
  onChange?: InputChangeHandler;
  className?: React.HTMLAttributes<HTMLDivElement>['className'];
  labelClassName?: string;
  inputClassName?: string;
  loading?: boolean;
  noHelper?: boolean;
  'data-testid'?: string;
};

export type InputLabelProps = {
  label?: string;
  className?: string;
  required?: boolean;
  'data-testid'?: string;
};

export type SearchInputProps = Omit<InputProps, 'onSubmit' | 'endIcon'> & {
  onSearch: (content: string) => void;
};
