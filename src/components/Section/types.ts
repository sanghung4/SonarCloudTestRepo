import { ColorValue, ViewStyle } from 'react-native';

export interface SectionProps extends ViewStyle {
  bg?: ColorValue;
  testID?: string | undefined;
  border?: {
    width?:
      | number
      | {
          top?: number;
          bottom?: number;
          left?: number;
          right?: number;
        };
    color?: string;
    style?: string;
    radius?:
      | number
      | {
          topLeft?: number;
          topRight?: number;
          bottomLeft?: number;
          bottomRight?: number;
        };
  };
  children?: React.ReactNode;
}
