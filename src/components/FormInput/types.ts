import { InputProps } from 'react-native-elements';

export interface FormInputProps extends InputProps {
  label?: string;
  required?: boolean;
  readonly?: boolean;
  error?: boolean;
}

export interface FormInputLabelProps {
  label: FormInputProps['label'];
  labelStyle: FormInputProps['labelStyle'];
  required: FormInputProps['required'];
}
