import React from 'react';
import { Text } from 'components/Text';
import { Colors, fontSize, FontWeight as fw } from 'constants/style';
import { ProductFeaturedTitleProps } from './types';
import { getComponentTestingIds } from 'test-utils/testIds';
import { StyleSheet } from 'react-native';

export const ProductFeaturedTitle = ({
  style,
  testID,
  ...rest
}: ProductFeaturedTitleProps) => {
  const testIds = getComponentTestingIds('Product', testID);
  return (
    <Text
      testID={testIds.featuredTitle}
      fontWeight={fw.MEDIUM}
      color={Colors.PRIMARY_1100}
      fontSize={fontSize.SMALL}
      style={StyleSheet.flatten([styles.text, style && style])}
      {...rest}
    />
  );
};

const styles = StyleSheet.create({
  text: {
    marginRight: 8,
  }
});
