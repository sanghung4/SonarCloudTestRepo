import React from 'react';
import { StyleSheet } from 'react-native';
import { Button as RNEButton } from 'react-native-elements';
import { Colors, FontWeight } from 'constants/style';
import { CustomButtonProps } from './types';
import { CustomIcon } from 'components/CustomIcon';
import { getComponentTestingIds } from 'test-utils/testIds';

const CustomButton = ({
  disabled,
  titleStyle,
  buttonStyle,
  containerStyle,
  iconContainerStyle,
  type = 'primary',
  icon,
  iconColor,
  iconProps,
  iconRight,
  loading,
  testID,
  ...rest
}: CustomButtonProps) => {
  const testIds = getComponentTestingIds('CustomButton', testID);

  return (
    <RNEButton
      disabled={loading || disabled}
      loading={loading}
      loadingProps={{ color: styles[`${type}Text`].color }}
      disabledTitleStyle={[styles[`${type}DisabledText`]]}
      disabledStyle={[styles[`${type}DisabledButton`]]}
      containerStyle={[styles.baseContainer, containerStyle]}
      titleStyle={[
        styles.baseText,
        styles[`${type}Text`],
        icon && !iconRight && styles.iconLeftText,
        icon && iconRight && styles.iconRightText,
        titleStyle,
      ]}
      buttonStyle={[
        styles.baseButton,
        styles[`${type}Button`],
        icon && styles.iconButton,
        buttonStyle,
      ]}
      icon={
        icon && (
          <CustomIcon
            name={icon}
            size={24}
            color={iconColor || styles[`${type}Text`].color}
            {...iconProps}
          />
        )
      }
      iconRight={iconRight}
      testID={testIds.component}
      {...rest}
    />
  );
};

const styles = StyleSheet.create({
  // Base Styles
  baseText: {
    fontSize: 16,
    lineHeight: 24,
    fontWeight: FontWeight.MEDIUM,
    paddingVertical: 0,
  },
  baseContainer: {
    borderRadius: 2,
  },
  baseButton: {
    paddingHorizontal: 23, // 24 - 1 due to border width
    paddingVertical: 9, // 10 - 1 due to border width
    borderRadius: 2,
    borderWidth: 1,
  },
  baseIconContainer: {
    marginHorizontal: 0,
  },

  // Text Styles
  primaryText: {
    color: Colors.WHITE,
  },
  secondaryText: {
    color: Colors.PRIMARY_1100,
  },
  linkText: {
    color: Colors.PRIMARY_2100,
    textDecorationLine: 'underline',
  },

  // Button Styles
  primaryButton: {
    backgroundColor: Colors.PRIMARY_1100,
    borderColor: Colors.PRIMARY_1100,
  },
  secondaryButton: {
    backgroundColor: Colors.WHITE,
    borderColor: Colors.PRIMARY_1100,
  },
  linkButton: {
    backgroundColor: 'transparent',
    borderColor: 'transparent',
  },

  // Icon Styles
  iconButton: {
    paddingHorizontal: 15, // 16 - 1 due to border width
  },
  iconLeftText: {
    paddingLeft: 8,
  },
  iconRightText: {
    paddingRight: 8,
  },

  // Disabled Styles
  primaryDisabledText: {
    color: Colors.WHITE,
  },
  primaryDisabledButton: {
    backgroundColor: Colors.SECONDARY_3100,
    borderColor: Colors.SECONDARY_3100,
  },
  secondaryDisabledText: {
    color: Colors.SECONDARY_3100,
  },
  secondaryDisabledButton: {
    borderColor: Colors.SECONDARY_3100,
  },
  linkDisabledText: {
    color: Colors.SECONDARY_3100,
  },
  linkDisabledButton: {
    backgroundColor: 'transparent',
  },
});

export default CustomButton;
