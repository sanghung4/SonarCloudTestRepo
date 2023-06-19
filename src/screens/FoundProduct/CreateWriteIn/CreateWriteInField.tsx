import React from 'react';
import { FormInput } from 'components/FormInput';
import { InputLength, writeInForm, WriteInFormKey } from 'constants/form';
import { useField } from 'providers/Form';
import {
  Keyboard,
  NativeSyntheticEvent,
  TextInputContentSizeChangeEventData,
} from 'react-native';
import { isMincron } from 'utils/apollo';
import { useConfig } from 'hooks/useConfig';
import { getScreenTestingIds } from 'test-utils/testIds';

export const CreateWriteInField = <T extends WriteInFormKey>({
  fieldName,
  label,
  readonly,
  required,
  numberOfLines,
  maxLength,
  keyboardType,
  onContentSizeChange,
}: typeof writeInForm[T] & {
  fieldName: T;
  onContentSizeChange?: (
    event: NativeSyntheticEvent<TextInputContentSizeChangeEventData>
  ) => void;
}) => {
  const [{ count }] = useConfig();
  const [values] = useField(fieldName);

  const testIds = getScreenTestingIds('CreateWriteIn');

  return (
    <FormInput
      {...values}
      errorMessage=""
      testID={`${testIds.form}-${fieldName}`}
      disabled={readonly}
      required={required}
      label={label}
      placeholder={label}
      multiline={!!numberOfLines}
      numberOfLines={numberOfLines}
      maxLength={
        fieldName === 'quantity'
          ? isMincron(count)
            ? InputLength.MINCRON
            : InputLength.ECLIPSE
          : maxLength
      }
      keyboardType={keyboardType}
      blurOnSubmit={true}
      onSubmitEditing={() => {
        Keyboard.dismiss();
      }}
      onContentSizeChange={onContentSizeChange}
    />
  );
};
