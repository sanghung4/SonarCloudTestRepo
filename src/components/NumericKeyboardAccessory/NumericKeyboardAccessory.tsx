import React from 'react';
import { Colors, OldColors } from 'constants/style';
import { range } from 'lodash';
import {
  InputAccessoryView,
  Platform,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import { getValue, isDark } from './utils';
import { NumericKeyboardAccessoryProps } from './types';
import { getComponentTestingIds } from 'test-utils/testIds';

const NumericKeyboardAccessory = ({
  onPress,
  inputAccessoryViewID,
  testID,
}: NumericKeyboardAccessoryProps) => {
  const testIds = getComponentTestingIds('NumericKeyboardAccessory', testID);
  return Platform.OS === 'ios' ? (
    <InputAccessoryView nativeID={inputAccessoryViewID}>
      <View testID={testIds.component} style={styles.container}>
        {range(10).map((_, index) => (
          <TouchableOpacity
            testID={testIds.button}
            onPress={() => onPress(getValue(index))}
            style={styles.numberButton}
            key={index}
          >
            <Text style={styles.buttonText}>{getValue(index)}</Text>
          </TouchableOpacity>
        ))}
      </View>
    </InputAccessoryView>
  ) : null;
};

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    backgroundColor: isDark()
      ? OldColors.IOS_KEYBOARD_DARK
      : OldColors.IOS_KEYBOARD_LIGHT,
    justifyContent: 'space-evenly',
    paddingVertical: 10,
    paddingHorizontal: 2,
  },
  numberButton: {
    borderRadius: 5,
    backgroundColor: isDark()
      ? OldColors.IOS_KEY_DARK
      : OldColors.IOS_KEY_LIGHT,
    marginHorizontal: 2,
    flex: 1,
    paddingVertical: 12,
    elevation: 4,
    shadowColor: Colors.BLACK,
    shadowOpacity: 0.4,
    shadowOffset: { height: 1, width: 1 },
    shadowRadius: 1,
  },
  buttonText: {
    textAlign: 'center',
    fontSize: 22,
    fontFamily: 'System',
    color: isDark() ? Colors.WHITE : Colors.BLACK,
  },
});

export default NumericKeyboardAccessory;
