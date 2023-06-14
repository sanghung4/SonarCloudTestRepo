import { Appearance } from 'react-native';

export const isDark = (): boolean => {
  return Appearance.getColorScheme() === 'dark';
};

export const getValue = (index: number) => (index === 9 ? '0' : `${index + 1}`);
