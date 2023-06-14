import React, { useState, useMemo } from 'react';
import {
  NativeSyntheticEvent,
  TextInputContentSizeChangeEventData,
  View,
  StyleSheet,
} from 'react-native';
import { ButtonGroup } from 'react-native-elements';
import { map } from 'lodash';
import { ApolloError } from '@apollo/client';
import { useCreateWriteInMutation } from 'api';
import { useOverlay } from 'providers/Overlay';
import Form, { FormMethods } from 'providers/Form';
import { getError } from 'utils/error';
import { WriteInFormKey } from 'constants/form';
import { ErrorType } from 'constants/error';
import { pagePadding, FontWeight as fw, Colors } from 'constants/style';
import { Text } from 'components/Text';
import { useLocation } from 'hooks/useLocation';
import { useLoading } from 'hooks/useLoading';
import { WriteInForm } from './types';
import {
  getInitialValues,
  getPayload,
  getWriteInForm,
  validate,
} from './utils';
import { CreateWriteInField } from './CreateWriteInField';
import { useNavigation } from '@react-navigation/native';
import { RouteNames } from 'constants/routes';
import { AppNavigation } from 'navigation/types';
import { CustomButton } from 'components/CustomButton';
import { handleMutationComplete } from 'utils/apollo';
import { getScreenTestingIds } from 'test-utils/testIds';
import firebaseUtils from 'utils/firebaseUtils';

const CreateWriteIn = () => {
  const { location } = useLocation();
  const navigation = useNavigation<AppNavigation<'FoundProduct'>>();

  const { showAlert, toggleLoading } = useOverlay();
  const form = useMemo(getWriteInForm, []);
  const [textAreaHeight, setTextAreaHeight] = useState(0);
  const [selectedUOMIndex, setSelectedUOMIndex] = useState(0);
  const [createWriteIn, { loading }] = useCreateWriteInMutation({
    onCompleted: (resp) => {
      handleMutationComplete(resp.createWriteIn);
    },
  });

  useLoading([loading]);

  const onContentSizeChange = (
    event: NativeSyntheticEvent<TextInputContentSizeChangeEventData>
  ) => {
    const { contentSize } = event.nativeEvent;
    if (contentSize.height === textAreaHeight) {
      return;
    }
    setTextAreaHeight(contentSize.height);
  };

  const onSubmitPress = (
    values: WriteInForm,
    actions: FormMethods<WriteInForm>
  ) => {
    createWriteIn({
      variables: {
        writeIn: getPayload(values, selectedUOMIndex),
      },
    })
      .then(({ data }) => {
        if (data && data.createWriteIn.success) {
          toggleLoading(false);
          actions.resetForm();
        } else {
          const error = new ApolloError({});
          showAlert(getError(ErrorType.CREATE_WRITE_IN, error));
        }
        navigation.navigate(RouteNames.LOCATION_ITEMS);
      })
      .catch((error) => {
        firebaseUtils.crashlyticsRecordError(error);
        showAlert(getError(ErrorType.CREATE_WRITE_IN, error));
      });
  };

  const testIds = getScreenTestingIds('CreateWriteIn');

  return (
    <Form
      initialValues={getInitialValues(location)}
      validate={validate}
      onSubmit={onSubmitPress}
    >
      {({ submitForm }) => (
        <View style={styles.container}>
          <View style={styles.header}>
            <Text fontWeight={fw.BOLD}>Basic Details</Text>
            <Text>
              Include basic information regarding this write in. Fill in all of
              the necessary fields below.
            </Text>
          </View>
          <View style={styles.form}>
            {map(form, (field, key: WriteInFormKey) => {
              if (key === WriteInFormKey.UOM) {
                return (
                  <View key={key}>
                    <Text>
                      {field.label}
                      <Text color={Colors.SUPPORT_2100}>{} *</Text>
                    </Text>
                    <View style={styles.buttonGroup}>
                      <ButtonGroup
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
                <CreateWriteInField
                  key={key}
                  fieldName={key}
                  onContentSizeChange={onContentSizeChange}
                  {...field}
                />
              );
            })}
          </View>

          <CustomButton
            title="Submit"
            onPress={submitForm}
            containerStyle={styles.actionButtonContainer}
            testID={testIds.submitButton}
          />
        </View>
      )}
    </Form>
  );
};

const styles = StyleSheet.create({
  container: { backgroundColor: Colors.WHITE, flex: 1 },
  header: {
    width: '100%',
    backgroundColor: Colors.WHITE,
    paddingHorizontal: pagePadding.X,
    paddingVertical: 16,
  },
  form: {
    width: '100%',
    paddingHorizontal: pagePadding.X,
  },
  pickerContainer: {
    width: '96%',
    marginBottom: 10,
    marginLeft: 0,
  },
  pickerText: {
    alignSelf: 'flex-start',
    marginLeft: 10,
    marginRight: 15,
  },
  buttonGroup: { flexDirection: 'row', marginVertical: 10 },
  actionButtonContainer: {
    marginHorizontal: 24,
  },
});

export default CreateWriteIn;
