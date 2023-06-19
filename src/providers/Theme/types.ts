import { FullTheme } from 'react-native-elements';
import { theme } from 'providers';

export type Theme = Partial<FullTheme> & typeof theme;
