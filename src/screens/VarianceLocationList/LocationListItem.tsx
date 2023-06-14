import React from 'react';
import { Text } from 'components/Text';
import { StyleSheet, TouchableOpacity } from 'react-native';
import { CustomIcon, CustomIconNames } from 'components/CustomIcon';
import { LocationListItemProps } from './types';
import { FontWeight } from 'constants/style';
import { getComponentTestingIds } from 'test-utils/testIds';

export const LocationListItem = ({
  testID,
  location,
  onPress,
}: LocationListItemProps) => {
  const testIds = getComponentTestingIds('LocationListItem', testID);
  return (
    <TouchableOpacity
      style={styles.container}
      onPress={onPress}
      testID={`${testIds.location}-${location.id}`}
    >
      <Text
        fontSize={20}
        fontWeight={FontWeight.BOLD}
        testID={`${testIds.title}-${location.id}`}
      >
        {location.id}
      </Text>
      <Text
        style={styles.totalProductsText}
        testID={`${testIds.products}-${location.id}`}
      >
        {location.totalProducts}
        {location.totalProducts === 1 ? ' Product' : ' Products'}
      </Text>
      <CustomIcon name={CustomIconNames.RightChevron} />
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  container: {
    paddingVertical: 16,
    paddingLeft: 16,
    paddingRight: 8,
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  totalProductsText: {
    marginLeft: 'auto',
    marginRight: 8,
  },
});
