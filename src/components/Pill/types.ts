import { StyleProp, ViewStyle, TextStyle } from 'react-native';
import { IconProps } from 'react-native-elements';

export type PillStatus =
  | 'default'
  | 'primary'
  | 'secondary'
  | 'success'
  | 'warning'
  | 'error'
  | 'blue';

export enum PillStats {
  DEFAULT = 'default',
  PRIMARY = 'primary',
  SECONDARY = 'secondary',
  SUCCESS = 'success',
  WARNING = 'warning',
  ERROR = 'error',
  BLUE = 'blue',
}

type PillVariant = 'solid' | 'outline';

export enum PillVariants {
  SOLID = 'solid',
  OUTLINE = 'outline',
}

export interface PillProps {
  style?: StyleProp<ViewStyle>;
  textStyle?: StyleProp<TextStyle>;
  Component?: React.ElementType | any;
  value: React.ReactNode;
  variant?: PillVariant;
  icon?: React.ReactElement<{}> | IconProps;
  onPress?: () => void;
  status?: PillStatus;
  rounded?: boolean;
  testID?: string;
}

export enum PILL_TEST_IDS {
  TEXT = 'pillText',
  COMPONENT = 'pillComponent',
}
