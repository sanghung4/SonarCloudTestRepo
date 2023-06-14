import React from 'react';
import { StyleProp, StyleSheet, View, ViewStyle } from 'react-native';
import { ProgressBar } from 'components/ProgressBar';
import { Text } from 'components/Text';

import { Colors, FontWeight } from 'constants/style';
import { percentage } from 'utils/numberUtils';
import { getComponentTestingIds } from 'test-utils/testIds';

export interface PercentageBarProps {
  value: number;
  variation?: string;
  total?: number;
  minLabel?: React.ReactNode;
  maxLabel?: React.ReactNode;
  minLabelColor?: string;
  labelWrapperStyle?: StyleProp<ViewStyle>;
  minMaxLabelsWrapperStyle?: StyleProp<ViewStyle>;
  progressBarContainerStyle: StyleProp<ViewStyle>;
  containerStyle?: StyleProp<ViewStyle>;
  testID?: string;
}

const PercentageBar = ({
  value,
  total = 100,
  minLabel = `${percentage(value, total)} % Complete`,
  maxLabel = `${total - value} products to go`,
  minLabelColor,
  labelWrapperStyle,
  minMaxLabelsWrapperStyle,
  progressBarContainerStyle,
  containerStyle,
  variation,
  testID,
}: PercentageBarProps) => {
  const testIds = getComponentTestingIds('PercentageBar', testID);
  return (
    <View
      testID={testIds.component}
      style={StyleSheet.flatten([containerStyle && containerStyle])}
    >
      <View
        style={[styles.labelWrapper, labelWrapperStyle && labelWrapperStyle]}
      />
      <View
        style={StyleSheet.flatten([
          styles.minMaxLabelsWrapper,
          minMaxLabelsWrapperStyle && minMaxLabelsWrapperStyle,
        ])}
      >
        <Text
          color={minLabelColor}
          style={styles.countText}
          fontWeight={FontWeight.MEDIUM}
          testID={testIds.text}
        >
          {variation !== 'COUNT_SUMMARY' ? minLabel : maxLabel}
        </Text>
        <ProgressBar
          value={value}
          total={total}
          containerStyle={progressBarContainerStyle}
          testID={testIds.progressBar}
        />
        {variation === 'COUNT_SUMMARY' && (
          <Text color={Colors.WHITE}>{`${percentage(value, total)}%`}</Text>
        )}
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  minMaxLabelsWrapper: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    alignItems: 'center',
  },
  labelWrapper: {
    width: '100%',
  },
  countText: {
    marginRight: 15,
  },
});

export default PercentageBar;
