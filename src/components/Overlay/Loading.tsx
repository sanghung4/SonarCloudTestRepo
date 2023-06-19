import { Colors } from 'constants/style';
import React from 'react';
import { ActivityIndicator } from 'react-native';

const Loading = () => {
  return <ActivityIndicator size="large" color={Colors.PRIMARY_2100} />;
};

export default Loading;
