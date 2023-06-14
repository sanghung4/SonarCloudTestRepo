import React from 'react';
import { Text } from 'components/Text';
import { StyleSheet, View } from 'react-native';
import { FontWeight } from 'constants/style';
import { ProductHeaderProps } from './types';
import { removeLineBreaks } from 'utils/stringUtils';
import { getComponentTestingIds } from 'test-utils/testIds';

export const ProductHeader = ({
  title,
  containerStyle,
  testID,
}: ProductHeaderProps) => {
  const testIds = getComponentTestingIds('Product', testID);
  return (
    <View
      style={StyleSheet.flatten([
        styles.container,
        containerStyle && containerStyle,
      ])}
    >
      <Text
        fontSize={14}
        fontWeight={FontWeight.MEDIUM}
        testID={testIds.description}
      >
        {removeLineBreaks(title)}
      </Text>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    paddingVertical: 8,
    paddingHorizontal: 24,
  },
});
