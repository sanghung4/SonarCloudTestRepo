import { pagePadding } from 'constants/style';
import React from 'react';
import { View, StyleSheet } from 'react-native';
import { getComponentTestingIds } from 'test-utils/testIds';
import { ProductContentProps } from './types';

export const ProductContent = ({
  children,
  style,
  testID,
  ...rest
}: ProductContentProps) => {
  const testIds = getComponentTestingIds('Product', testID);
  return (
    <View
      testID={testIds.content}
      style={StyleSheet.flatten([styles.container, style && style])}
      {...rest}
    >
      {children}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    paddingVertical: 8,
    paddingHorizontal: pagePadding.X,
  },
});
