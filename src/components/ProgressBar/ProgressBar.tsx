import React from 'react';
import { StyleProp, StyleSheet, View, ViewStyle } from 'react-native';
import { borderRadius, Colors } from 'constants/style';
import { percentage } from 'utils/numberUtils';
import { getComponentTestingIds } from 'test-utils/testIds';

export type ProgressBarProps = {
  value: number;
  total?: number;
  containerStyle?: StyleProp<ViewStyle>;
  progressBarStyle?: StyleProp<ViewStyle>;
  barColor?: string;
  testID?: string;
};

const ProgressBar: React.FC<ProgressBarProps> = ({
  value,
  total = 100,
  containerStyle,
  progressBarStyle,
  barColor,
  testID,
}) => {
  const testIds = getComponentTestingIds('ProgressBar', testID);
  return (
    <View
      testID={testIds.component}
      style={StyleSheet.flatten([
        styles.container,
        containerStyle && containerStyle,
      ])}
    >
      <View
        style={StyleSheet.flatten([
          getProgressBar(percentage(value, total), barColor),
          progressBarStyle && progressBarStyle,
        ])}
      />
    </View>
  );
};

const getProgressBar = (
  progress: number,
  barColor?: string
): StyleProp<ViewStyle> => ({
  height: '100%',
  width: `${progress}%`,
  backgroundColor: barColor ? barColor : Colors.SUPPORT_1100,
  borderRadius: borderRadius.ROUNDED,
});

const styles = StyleSheet.create({
  container: {
    height: 10,
    backgroundColor: Colors.SECONDARY_3100,
    borderRadius: borderRadius.ROUNDED,
    borderColor: Colors.WHITE,
    borderWidth: 1,
  },
});

export default ProgressBar;
