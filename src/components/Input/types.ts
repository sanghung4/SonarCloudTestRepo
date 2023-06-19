import { TextInputProps, StyleProp, ViewStyle } from 'react-native';

export interface SearchInputProps extends TextInputProps {
  containerStyle?: StyleProp<ViewStyle>;
}
