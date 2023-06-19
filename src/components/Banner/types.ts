import { StyleProp, TextStyle } from 'react-native';

export interface BannerProps {
  title: React.ReactNode;
  titleStyle?: StyleProp<TextStyle>;
  rightComponent?: React.ReactNode;
  testID?: string;
}
