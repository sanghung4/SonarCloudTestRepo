import { CustomIconNames } from 'components/CustomIcon';
import { Message } from 'components/Message';
import { Colors, OldColors } from 'constants/style';
import React from 'react';
import { ToastConfig, ToastConfigParams } from 'react-native-toast-message';
import { getScreenTestingIds } from 'test-utils/testIds';
import { CustomToastProps, CustomToastStyles } from './types';

const defaultToastStyles: CustomToastStyles = {
  error: {
    icon: CustomIconNames.Warning,
    color: Colors.SUPPORT_2100,
    background: Colors.SUPPORT_210,
    paddingHorizontal: true,
  },
  message: {
    icon: CustomIconNames.Info,
    color: Colors.PRIMARY_2100,
    background: Colors.PRIMARY_210,
    paddingHorizontal: true,
  },
  success: {
    icon: CustomIconNames.CircleCheck,
    color: Colors.SUPPORT_1100,
    background: Colors.SUPPORT_110,
    paddingHorizontal: true,
  },
  warn: {
    icon: CustomIconNames.Warning,
    color: Colors.SUPPORT_3100,
    background: Colors.SECONDARY_110,
    paddingHorizontal: true,
  },
};

const testIds = getScreenTestingIds('Toast');

const CustomToastConfig: ToastConfig = {
  warn: ({
    text1,
    text2,
    onPress,
    hide,
    props,
  }: ToastConfigParams<CustomToastProps>) => (
    <Message
      text1={text1}
      text2={text2}
      title={props.title}
      testID={testIds.warn}
      onPress={onPress}
      onClose={props.closeable ? hide : undefined}
      {...defaultToastStyles.warn}
    />
  ),
  error: ({
    text1,
    text2,
    hide,
    onPress,
    props,
  }: ToastConfigParams<CustomToastProps>) => (
    <Message
      text1={text1}
      text2={text2}
      title={props.title}
      testID={testIds.error}
      onPress={onPress}
      onClose={props.closeable ? hide : undefined}
      {...defaultToastStyles.error}
    />
  ),
  message: ({
    text1,
    text2,
    hide,
    onPress,
    props,
  }: ToastConfigParams<CustomToastProps>) => (
    <Message
      text1={text1}
      text2={text2}
      title={props.title}
      testID={testIds.message}
      onPress={onPress}
      onClose={props.closeable ? hide : undefined}
      {...defaultToastStyles.message}
    />
  ),
  success: ({
    text1,
    text2,
    hide,
    onPress,
    props,
  }: ToastConfigParams<CustomToastProps>) => (
    <Message
      text1={text1}
      text2={text2}
      title={props.title}
      testID={testIds.success}
      onPress={onPress}
      onClose={props.closeable ? hide : undefined}
      {...defaultToastStyles.success}
    />
  ),
};

export default CustomToastConfig;
