import React, { useState } from 'react';
import {
  NativeSyntheticEvent,
  StyleSheet,
  TextInputContentSizeChangeEventData,
  View,
} from 'react-native';
import { ButtonGroup } from 'react-native-elements';
import { map, isNumber, isString, transform } from 'lodash';
import { ApolloError } from '@apollo/client';
import { GetWriteInDocument, useUpdateWriteInMutation, WriteIn } from 'api';
import { Text } from 'components/Text';
import Form, { FormErrors, useField } from 'providers/Form';
import { getError } from 'utils/error';
import { InputLength, writeInForm, WriteInFormKey } from 'constants/form';
import { Colors } from 'constants/style';
import { ErrorType } from 'constants/error';
import { FormInput } from 'components/FormInput';
import { useOverlay } from 'providers/Overlay';
import { getPayload } from '../FoundProduct/CreateWriteIn/utils';
import { handleMutationComplete, isMincron } from 'utils/apollo';
import { useConfig } from 'hooks/useConfig';
import { AppScreenProps } from 'navigation/types';
import { ScreenLayout } from 'components/ScreenLayout';
import { getScreenTestingIds } from 'test-utils/testIds';
import useRenderListener from 'hooks/useRenderListener';
import firebaseUtils from 'utils/firebaseUtils';

const UpdateWriteInField = <T extends WriteInFormKey>({
  fieldName,
  label,
  readonly,
  required,
  numberOfLines,
  keyboardType,
  maxLength,
  style,
  onContentSizeChange,
}: typeof writeInForm[T] & {
  fieldName: T;
  onContentSizeChange?: (
    event: NativeSyntheticEvent<TextInputContentSizeChangeEventData>
  ) => void;
}) => {
  useRenderListener();
  const [{ count }] = useConfig();
  const [values] = useField(fieldName);
  const testIds = getScreenTestingIds('UpdateWriteIn');
  return (
    <FormInput
      {...values}
      errorMessage=""
      testID={`${testIds.form}-${fieldName}`}
      disabled={readonly}
      required={required}
      maxLength={
        fieldName === 'quantity'
          ? isMincron(count)
            ? InputLength.MINCRON
            : InputLength.ECLIPSE
          : maxLength
      }
      label={label}
      placeholder={label}
      multiline={!!numberOfLines}
      numberOfLines={numberOfLines}
      keyboardType={keyboardType}
      style={style}
      onContentSizeChange={onContentSizeChange}
    />
  );
};

type WriteInForm = Record<WriteInFormKey, string>;

const toString = (value: string | number | null) => {
  if (isNumber(value)) {
    return value.toString();
  }
  if (isString(value)) {
    return value;
  }
  return '';
};

const getInitialValues = (writeIn: WriteIn | null) => {
  return transform(
    WriteInFormKey,
    (values, key) => {
      if (writeIn) {
        const value = toString(writeIn[key] || '');
        values[key] = value;
      } else {
        values[key] = '';
      }
    },
    {} as WriteInForm
  );
};

const validate = (values: WriteInForm) => {
  return transform(
    WriteInFormKey,
    (errors, key) => {
      if (writeInForm[key].required && !values[key]) {
        errors[key] = `Requires ${writeInForm[key].label}`;
      } else {
        errors[key] = '';
      }
    },
    {} as FormErrors<typeof writeInForm>
  );
};

const UpdateWriteIn = ({
  navigation,
  route,
}: AppScreenProps<'UpdateWriteIn'>) => {
  const { showAlert } = useOverlay();

  const { writeIn } = route.params;

  const [selectedUOMIndex, setSelectedUOMIndex] = useState(
    writeIn?.uom === 'FT' ? 1 : 0
  );

  const [updateWriteIn, { loading }] = useUpdateWriteInMutation({
    onCompleted: (resp) => {
      handleMutationComplete(resp.updateWriteIn);
    },
  });

  const onSubmitPress = (values: WriteInForm) => {
    if (!writeIn) {
      return;
    }

    updateWriteIn({
      variables: {
        id: writeIn.id,
        writeIn: getPayload(values, selectedUOMIndex),
      },
      refetchQueries: [
        {
          query: GetWriteInDocument,
          variables: {
            id: writeIn.id,
          },
        },
      ],
    })
      .then(({ data }) => {
        if (data && data.updateWriteIn.success) {
          navigation.goBack();
        } else {
          const error = new ApolloError({});
          showAlert(getError(ErrorType.UPDATE_WRITE_IN, error));
        }
      })
      .catch((error) => {
        firebaseUtils.crashlyticsRecordError(error);
        showAlert(getError(ErrorType.UPDATE_WRITE_IN, error));
      });
  };

  const testIds = getScreenTestingIds('UpdateWriteIn');

  return (
    <Form
      initialValues={getInitialValues(writeIn)}
      validate={validate}
      onSubmit={onSubmitPress}
    >
      {({ submitForm }) => (
        <ScreenLayout
          pageAction={{
            loading,
            title: 'Update',
            onPress: submitForm,
          }}
          testID={testIds.screenLayout}
        >
          <ScreenLayout.ScrollContent
            keyboardShouldPersistTaps="handled"
            banner={{ title: 'Update Write-In' }}
            padding
            testID={testIds.scrollContent}
          >
            {map(writeInForm, (field, key: WriteInFormKey) => {
              if (key === WriteInFormKey.UOM) {
                return (
                  <View key={key}>
                    <Text>
                      {field.label}
                      <Text color={Colors.SUPPORT_2100}>{} *</Text>
                    </Text>
                    <View style={styles.buttonGroup}>
                      <ButtonGroup
                        testID={'uom'}
                        selectedIndex={selectedUOMIndex}
                        onPress={(index) => setSelectedUOMIndex(index)}
                        buttons={['EA', 'FT']}
                        containerStyle={styles.pickerContainer}
                        textStyle={styles.pickerText}
                      />
                    </View>
                  </View>
                );
              }
              return (
                <UpdateWriteInField key={key} fieldName={key} {...field} />
              );
            })}
          </ScreenLayout.ScrollContent>
        </ScreenLayout>
      )}
    </Form>
  );
};

const styles = StyleSheet.create({
  pickerContainer: {
    width: '100%',
    marginBottom: 10,
    marginLeft: 0,
  },
  pickerText: {
    alignSelf: 'flex-start',
    marginLeft: 10,
    marginRight: 15,
  },
  buttonGroup: { flexDirection: 'row', marginVertical: 10 },
  bannerStyle: { alignSelf: 'center', marginVertical: 15 },
});

export default UpdateWriteIn;
