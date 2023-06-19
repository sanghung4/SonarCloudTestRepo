import { FontWeight } from 'constants/style';
import { ColorValue, StyleProp, TextStyle } from 'react-native';
import * as RNE from 'react-native-elements';

export interface TextProps extends RNE.TextProps {
  color?: ColorValue;
  bold?: boolean;
  medium?: boolean;
  regular?: boolean;
  centered?: boolean;
  h5Style?: StyleProp<TextStyle>;
  h5?: boolean;
  smallStyle?: StyleProp<TextStyle>;
  small?: boolean;
  captionStyle?: StyleProp<TextStyle>;
  caption?: boolean;
  fontSize?: TextStyle['fontSize'];
  fontWeight?: FontWeight;
}
