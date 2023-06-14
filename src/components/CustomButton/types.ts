import { CustomIconNames } from 'components/CustomIcon';
import { CustomIconProps } from 'components/CustomIcon/types';
import { ButtonProps } from 'react-native-elements';

export interface CustomButtonProps
  extends Omit<ButtonProps, 'type' | 'icon' | 'iconPosition'> {
  type?: 'primary' | 'secondary' | 'link';
  color?: string;
  icon?: CustomIconNames;
  iconColor?: string;
  iconProps?: CustomIconProps;
}
