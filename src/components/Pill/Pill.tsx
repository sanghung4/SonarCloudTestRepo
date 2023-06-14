import { Text } from 'components/Text';
import {
  borderRadius,
  OldColors,
  Colors,
  fontSize,
  FontWeight as fw,
} from 'constants/style';
import React from 'react';
import { StyleSheet, TouchableOpacity, View } from 'react-native';
import { Icon } from 'react-native-elements';
import { getComponentTestingIds } from 'test-utils/testIds';
import { renderNode } from 'utils/render';
import { PillProps, PillStatus, PILL_TEST_IDS } from './types';

const Pill: React.FC<PillProps> = ({
  style,
  textStyle,
  onPress,
  value,
  icon,
  rounded = true,
  status = 'default',
  variant = 'solid',
  Component = onPress ? TouchableOpacity : View,
  testID,
  ...rest
}) => {
  const testIds = getComponentTestingIds('Pill', testID);
  return (
    <Component
      {...rest}
      testID={testIds.component}
      style={StyleSheet.flatten([
        styles.pill(status, icon, rounded, variant),
        style,
      ])}
      onPress={onPress}
    >
      {renderNode(Icon, icon, {
        size: 20,
        color: variant === 'outline' ? colorMap[status].color : Colors.WHITE,
        containerStyle: StyleSheet.flatten([styles.icon]),
      })}

      <Text
        testID={testIds.text}
        fontWeight={fw.MEDIUM}
        color={variant === 'outline' ? colorMap[status].color : Colors.WHITE}
        fontSize={fontSize.SMALL}
        style={textStyle}
        adjustsFontSizeToFit
        allowFontScaling={false}
        centered
      >
        {value}
      </Text>
    </Component>
  );
};

const colorMap = {
  default: {
    color: Colors.SECONDARY_2100,
    bg: '#EFEFEF',
  },
  primary: {
    color: Colors.PRIMARY_1100,
    bg: Colors.PRIMARY_110,
  },
  secondary: {
    color: Colors.PRIMARY_2100,
    bg: '#E7F0FD',
  },
  success: {
    color: Colors.SUPPORT_1100,
    bg: '#ECF2E9',
  },
  warning: {
    color: OldColors.YELLOW,
    bg: '#FEFAE6',
  },
  blue: {
    color: OldColors.BLUE,
    bg: '#C8D4E6',
  },
  error: {
    color: Colors.SUPPORT_2100,
    bg: '#FAEAE8',
  },
} as const;

const styles = {
  pill: (
    status: PillStatus,
    icon: PillProps['icon'],
    rounded: PillProps['rounded'],
    variant: PillProps['variant']
  ) => ({
    borderRadius: rounded ? borderRadius.ROUNDED : borderRadius.MEDIUM,
    borderColor: colorMap[status].color,
    borderWidth: 1,
    alignSelf: 'flex-start',
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: icon ? 16 : 10,
    paddingVertical: 2,
    minWidth: 30,
    backgroundColor:
      variant === 'outline' ? colorMap[status].bg : colorMap[status].color,
    justifyContent: 'center',
  }),
  icon: {
    marginLeft: -4,
    marginRight: 8,
  },
};

export default Pill;
