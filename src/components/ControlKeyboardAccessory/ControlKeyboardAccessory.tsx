import React from 'react';
import { InputAccessoryView, Platform, StyleSheet, View } from 'react-native';
import { Button } from 'components/Button';
import { Colors, FontWeight as fw } from 'constants/style';
import { ControlKeyboardAccessoryProps } from './types';
import { getComponentTestingIds } from 'test-utils/testIds';

const ControlKeyboardAccessory = ({
  inputAccessoryViewID,
  prevDisabled,
  nextDisabled,
  onDonePress,
  onNextPress,
  onPrevPress,
  onLayout,
  testID,
}: ControlKeyboardAccessoryProps) => {
  const testIds = getComponentTestingIds('ControlKeyboardAccessory', testID);
  return Platform.OS === 'ios' ? (
    <InputAccessoryView nativeID={inputAccessoryViewID}>
      <View
        testID={testIds.component}
        style={styles.accessoryContainer}
        onLayout={onLayout}
      >
        <View style={styles.accessoryPrevNextContainer}>
          <Button
            title="Prev"
            type="clear"
            titleStyle={styles.buttonText}
            onPress={onPrevPress}
            disabled={prevDisabled}
            testID={testIds.previous}
          />
          <Button
            title="Next"
            type="clear"
            titleStyle={styles.buttonText}
            onPress={onNextPress}
            disabled={nextDisabled}
            style={styles.nextButtonStyle}
            testID={testIds.next}
          />
        </View>
        <Button
          title="Done"
          type="clear"
          titleStyle={styles.buttonText}
          onPress={onDonePress}
          testID={testIds.done}
        />
      </View>
    </InputAccessoryView>
  ) : null;
};

const styles = StyleSheet.create({
  accessoryContainer: {
    backgroundColor: Colors.WHITE,
    flexDirection: 'row',
    justifyContent: 'space-between',
    borderColor: Colors.SECONDARY_3100,
    borderTopWidth: 1,
    paddingHorizontal: 12,
  },
  accessoryPrevNextContainer: {
    flexDirection: 'row',
  },
  buttonText: { fontWeight: fw.MEDIUM, fontSize: 20 },
  nextButtonStyle: { marginLeft: 10 },
});

export default ControlKeyboardAccessory;
