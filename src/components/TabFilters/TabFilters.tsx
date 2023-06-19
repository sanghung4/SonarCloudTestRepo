import React from 'react';
import { StyleSheet, TouchableOpacity, View } from 'react-native';
import { TabFiltersProps } from './types';
import { Text } from 'components/Text';
import { Colors, FontWeight } from 'constants/style';
import { getComponentTestingIds } from 'test-utils/testIds';

const TabFilters = ({ onPress, filters, active, testID }: TabFiltersProps) => {
  const testIds = getComponentTestingIds('TabFilters', testID);
  return (
    <View style={styles.container} testID={testIds.component}>
      {filters.map(({ value, color, title, disabled }) => (
        <TouchableOpacity
          onPress={() => onPress(value)}
          key={value}
          style={styles.tab}
          disabled={disabled}
          testID={`${testIds.tabButton}-${value.replace(/\s/g, '-')}`}
        >
          <View
            style={[
              styles.titleWrapper,
              value === active && {
                borderBottomColor: color || Colors.PRIMARY_1100,
              },
            ]}
          >
           <Text
              style={[
                styles.tabTitle,
                value === active && { color: color || Colors.PRIMARY_1100 },
              ]}
              testID={`${testIds.tabButton}-${value.replace(/\s/g, '-')}-text`}
            >
              {title}
            </Text>
            </View>
        </TouchableOpacity>
      ))}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    justifyContent: 'space-evenly',
  },
  tab: {
    paddingTop: 20,
    paddingBottom: 20,
  },
  tabTitle: {
    color: Colors.PRIMARY_3100,
    lineHeight: 24,
    fontSize: 16,
    fontWeight: FontWeight.MEDIUM,
    justifyContent:'center',
  },
  titleWrapper: {
    paddingHorizontal: 8,
    borderBottomWidth: 3,
    borderBottomColor: 'transparent',
  },
});

export default TabFilters;
