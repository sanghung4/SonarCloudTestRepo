import { TouchableOpacityProps } from 'react-native';
import { CustomIconNames } from 'components/CustomIcon';

export type ContainerProps = TouchableOpacityProps;
export interface MessageProps {
  text1?: string;
  text2?: string;
  title?: string;
  icon?: CustomIconNames;
  color?: string;
  background?: string;
  padding?: boolean;
  paddingHorizontal?: boolean;
  paddingVertical?: boolean;
  paddingLeft?: boolean;
  paddingRight?: boolean;
  paddingTop?: boolean;
  paddingBottom?: boolean;
  onClose?: () => void;
  onPress?: () => void;
  testID?: string | undefined;
}
