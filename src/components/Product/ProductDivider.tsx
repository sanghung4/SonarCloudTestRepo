import React from 'react';
import { StyleSheet } from 'react-native';
import { Divider } from 'react-native-elements';
import { getComponentTestingIds } from 'test-utils/testIds';
import { ProductDividerProps } from './types';

export const ProductDivider = ({
  style,
  testID,
  ...props
}: ProductDividerProps) => {
  const testIds = getComponentTestingIds('Product', testID);
  return (
    <Divider
      testID={testIds.divider}
      style={StyleSheet.flatten([styles.divider, style && style])}
      {...props}
    />
  );
};

const styles = StyleSheet.create({
  divider: {
    backgroundColor: '#D7D7D7',
  },
});
