import React from 'react';
import { TextStyle, ViewStyle } from 'react-native';
import * as RNE from 'react-native-elements';
import { useTheme } from 'providers/Theme';
import { ButtonProps, TEST_IDS } from './types';
import { Colors } from 'constants/style';

const Button = ({
  buttonStyle,
  titleStyle,
  disabled,
  loading,
  testID,
  ...rest
}: ButtonProps) => {
  const { theme } = useTheme();

  return (
    <RNE.Button
      buttonStyle={[getButtonStyle(rest, theme), buttonStyle]}
      titleStyle={[getTitleStyle(rest, theme), titleStyle]}
      disabled={disabled || loading}
      loading={loading}
      testID={testID || TEST_IDS.BUTTON_DEFAULT}
      {...rest}
    />
  );
};

Button.defaultProps = {
  type: 'solid',
};

const colors = {
  primary: (theme: RNE.FullTheme) =>
    theme.colors.primary || Colors.PRIMARY_1100,
  secondary: (theme: RNE.FullTheme) =>
    theme.colors.secondary || Colors.PRIMARY_2100,
} as const;

const getButtonStyle = (
  { type, color = 'primary' }: ButtonProps,
  theme: RNE.FullTheme
): ViewStyle => ({
  backgroundColor: type === 'solid' ? colors[color](theme) : 'transparent',
  borderColor: colors[color](theme),
  padding: 16,
});

const getTitleStyle = (
  { type, color = 'primary' }: ButtonProps,
  theme: RNE.FullTheme
): TextStyle => ({
  color: type === 'solid' ? 'white' : colors[color](theme),
});

export default Button;
