import React from 'react';
import { StyleSheet, View } from 'react-native';
import { ProgressBar } from 'components/ProgressBar';
import { Text } from 'components/Text';
import { percentage } from 'utils/numberUtils';
import { Colors, FontWeight as fw } from 'constants/style';
import { BranchProgressBarProps } from './types';
import { getComponentTestingIds } from 'test-utils/testIds';

export const BranchProgressBar = ({
  value,
  total = 100,
  minLabel = `${percentage(value, total)} % complete`,
  maxLabel = `${total - value} products to go`,
  minMaxLabelsWrapperStyle,
  progressBarContainerStyle,
  testID,
}: BranchProgressBarProps) => {
  const testIds = getComponentTestingIds('BranchProgressBar', testID);
  return (
    <View style={[styles.minMaxLabelsWrapper, minMaxLabelsWrapperStyle]}>
      <View style={styles.labelContainer}>
        <Text
          style={styles.counterLabelStyle}
          fontWeight={fw.MEDIUM}
          fontSize={16}
          color={Colors.SECONDARY_290}
          testID={testIds.completionPercentage}
        >
          {minLabel}
        </Text>
        <Text
          style={styles.counterLabelStyle}
          color={Colors.SECONDARY_290}
          testID={testIds.productsToGo}
        >
          {maxLabel}
        </Text>
      </View>
      <ProgressBar
        value={value}
        total={total}
        containerStyle={progressBarContainerStyle}
        barColor={Colors.PRIMARY_2100}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  minMaxLabelsWrapper: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    alignItems: 'center',
    width: '100%',
  },
  counterLabelStyle: {
    fontFamily: 'Roboto',
    lineHeight: 22,
  },
  labelContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    width: '100%',
  },
});
