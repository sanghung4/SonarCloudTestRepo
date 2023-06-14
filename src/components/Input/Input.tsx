import React from 'react';
import { isString } from 'lodash';
import { StyleSheet, TextInput } from 'react-native';
import {
  Input as RNEInput,
  InputProps as RNEInputProps,
  Text as RNEText,
} from 'react-native-elements';

import { borderRadius, Colors } from 'constants/style';
import { getComponentTestingIds } from 'test-utils/testIds';

export interface InputProps extends RNEInputProps {}

const Input = React.forwardRef<TextInput, InputProps>(
  ({ label, labelStyle, testID, ...props }, ref) => {
    const testIds = getComponentTestingIds('Input', testID);
    return (
      <RNEInput
        testID={testIds.component}
        ref={ref}
        label={
          isString(label) ? (
            <RNEText style={StyleSheet.flatten([styles.label, labelStyle])}>
              {label}
            </RNEText>
          ) : (
            label
          )
        }
        containerStyle={styles.containerStyle}
        {...props}
      />
    );
  }
);

const styles = StyleSheet.create({
  label: {
    color: Colors.BLACK,
    height: 24,
    marginBottom: 8,
  },
  containerStyle: {
    paddingHorizontal: 0,
  },
  style: {
    backgroundColor: Colors.WHITE,
    borderWidth: 1,
    borderColor: Colors.PRIMARY_2100,
    borderRadius: borderRadius.SMALL,
    paddingHorizontal: 16,
    paddingVertical: 12,
    shadowColor: Colors.PRIMARY_2100,
    shadowRadius: 2,
    shadowOpacity: 0.5,
    shadowOffset: {
      width: 0,
      height: 0,
    },
  },
});

export default Input;
