import { useContext } from 'react';
import {
  FullTheme,
  ThemeContext,
  ThemeProps,
  ThemeProvider,
} from 'react-native-elements';

export function useTheme<T = FullTheme>(): ThemeProps<T> {
  return useContext(ThemeContext) as ThemeProps<T>;
}

export default ThemeProvider;
