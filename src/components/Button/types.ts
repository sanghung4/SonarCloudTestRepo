import { ButtonProps as RNEButtonProps } from 'react-native-elements';

export enum TEST_IDS {
  BUTTON_DEFAULT = 'button',
}

export interface ButtonProps extends RNEButtonProps {
  color?: 'primary' | 'secondary';
}
