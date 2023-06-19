import React from 'react';
import { Input } from 'components/Input';
import { StyleProp, StyleSheet, ViewStyle } from 'react-native';
import { FormInputProps } from './types';
import { FormInputLabel } from './FormInputLabel';
import { Colors } from 'constants/style';
import { getComponentTestingIds } from 'test-utils/testIds';

const FormInput = ({
  required,
  disabled,
  readonly,
  error,
  label,
  labelStyle,
  disabledInputStyle,
  inputContainerStyle,
  maxLength,
  numberOfLines,
  testID,
  ...rest
}: FormInputProps) => {
  const testIds = getComponentTestingIds('FormInput', testID);
  return (
    <Input
      label={
        <FormInputLabel
          label={label}
          labelStyle={labelStyle}
          required={required}
        />
      }
      maxLength={maxLength}
      numberOfLines={numberOfLines}
      inputContainerStyle={[
        getInputContainer(error, disabled),
        inputContainerStyle,
      ]}
      disabledInputStyle={[styles.disabled, disabledInputStyle]}
      disabled={readonly || disabled}
      testID={testIds.component}
      {...rest}
    />
  );
};

FormInput.defaultProps = {
  autoComplete: 'off',
  autoCorrect: false,
  keyboardType: 'default',
};

const getInputContainer = (
  error?: boolean,
  disabled?: boolean
): StyleProp<ViewStyle> => ({
  borderBottomColor: error ? Colors.SUPPORT_2100 : Colors.SECONDARY_270,
  opacity: disabled ? 0.6 : 1,
});

const styles = StyleSheet.create({
  disabled: {
    opacity: 1,
  },
});

export default FormInput;
