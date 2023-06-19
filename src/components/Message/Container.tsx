import React from 'react';
import { TouchableOpacity, View } from 'react-native';
import { ContainerProps } from './types';

export const Container = (props: ContainerProps) => {
  if (props.onPress) {
    return <TouchableOpacity {...props} />;
  } else {
    return <View {...props} />;
  }
};
