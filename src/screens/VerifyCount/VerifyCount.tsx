import React, { useState, useRef, useEffect } from 'react';
import { StyleSheet, View } from 'react-native';
import { ApolloError } from '@apollo/client';

import { pagePadding, FontWeight as fw, Colors } from 'constants/style';
import {
  GetCountQuery,
  useGetCountLazyQuery,
  useRegisterLoginMutation,
} from 'api';
import AsyncStorage from '@react-native-async-storage/async-storage';

import { Input } from 'components/Input';
import { Text } from 'components/Text';
import * as storage from 'constants/storage';
import { useConfig } from 'hooks/useConfig';
import { useOverlay } from 'providers/Overlay';
import { getError, getAPIError } from 'utils/error';
import { EclipseErrorCode, ErrorType } from 'constants/error';
import { RouteNames } from 'constants/routes';
import { SvgIcons } from 'components/SVG/Svg';
import { initialRefs, initialValues, validation } from './utils';
import { FormKey, InitialValues } from './types';
import { AppScreenProps } from 'navigation/types';
import { ScreenLayout } from 'components/ScreenLayout';
import { getScreenTestingIds } from 'test-utils/testIds';
import useRenderListener from 'hooks/useRenderListener';
import firebaseUtils from '../../utils/firebaseUtils';
import { showErrorToast } from 'components/ToastConfig';
import { useAppState } from 'providers/AppState';

const VerifyCount = ({ navigation }: AppScreenProps<'VerifyCount'>) => {
  useRenderListener();
  const {resetProductQuantityMap} = useAppState()

  const { showAlert } = useOverlay();
  const inputRefs = useRef(initialRefs);

  const [errors, setErrors] = useState(initialValues);
  const [form, setForm] = useState(initialValues);
  const [branchId, setBranchId] = useState<string | null>('');
  const [countId, setCountId] = useState<string | null>('');

  const disabled =
    Object.values(errors).some((err) => !!err) ||
    Object.values(form).some((val) => !val);

  const [_, setConfig] = useConfig();

  const [registerLogin] = useRegisterLoginMutation();

  const [getCount, { loading }] = useGetCountLazyQuery({
    fetchPolicy: 'network-only',
    onCompleted: async ({ count }: GetCountQuery) => {
      setConfig({ count });

      if(count.branch.id !== branchId || count.id !== countId){
        await AsyncStorage.removeItem(storage.PRODUCT_QUANTITY_MAP)
        resetProductQuantityMap()
      }

      if (count) {
        registerLogin();
        AsyncStorage.multiSet([
          [storage.BRANCH_ID, count.branch.id],
          [storage.COUNT_ID, count.id],
          [storage.ERP_SYSTEM, count.erpSystem],
        ]);
        
      }

      navigation.navigate(RouteNames.GET_STARTED);
    },
    onError: (error: ApolloError) => {
      showErrorToast({
        title: '',
        text1: "The branch ID or count ID entered is incorrect. Try again",
      });
    },
  });

  useEffect(() => {
    firebaseUtils.crashlyticsSetUserId();
  }, [])
  
  useEffect(()=>{
    async ()=>{
      const getBranchId = await AsyncStorage.getItem(storage.BRANCH_ID);
      const getCountId = await AsyncStorage.getItem(storage.COUNT_ID);
      setBranchId(getBranchId);
      setCountId(getCountId)
    }
  },[])

  const setFieldValue = (key: keyof InitialValues) => (value: string) => {
    const error = validation[key](value);
    setForm((prevForm) => ({ ...prevForm, [key]: value }));
    setErrors((prevErrors) => ({ ...prevErrors, [key]: error }));
  };

  const onSubmitPress = () => {
    if (loading) {
      return;
    }
    getCount({
      variables: {
        id: form.countID,
        branchId: form.branchID,
      },
    });
  };

  const testIds = getScreenTestingIds('VerifyCount');

  return (
    <ScreenLayout
      testID={testIds.screenLayout}
      pageAction={{
        loading,
        disabled,
        title: 'Submit',
        onPress: onSubmitPress,
      }}
    >
      <ScreenLayout.ScrollContent paddingHorizontal>
        <View style={styles.contentContainer}>
          <SvgIcons
            size={175}
            style={styles.image}
            name={'CountIDEntryImage'}
          />
          <View style={styles.textContainer}>
            <Text
              centered
              h3
              fontWeight={fw.BOLD}
              color={Colors.PRIMARY_1100}
              style={styles.title}
            >
              Count ID Entry
            </Text>
            <Text centered color={Colors.SECONDARY_2100}>
              Please enter your Branch ID and Count ID to begin counting. If you are unsure, 
              please ask your Branch Manager for this information.
            </Text>
          </View>
          <View style={styles.formContainer}>
            <Input
              testID={testIds.branchInput}
              value={form[FormKey.BRANCH_ID]}
              ref={(node) => (inputRefs.current[FormKey.BRANCH_ID] = node)}
              onChangeText={setFieldValue(FormKey.BRANCH_ID)}
              errorMessage={errors[FormKey.BRANCH_ID]}
              errorStyle={styles.formError}
              keyboardType="number-pad"
              autoComplete="off"
              blurOnSubmit={false}
              label="Branch ID"
              placeholder="Ex: Mincron 032, Eclipse 1003"
              returnKeyType="next"
              onSubmitEditing={() =>
                inputRefs.current[FormKey.COUNT_ID]?.focus()
              }
            />
            <Input
              testID={testIds.countInput}
              value={form[FormKey.COUNT_ID]}
              ref={(node) => (inputRefs.current[FormKey.COUNT_ID] = node)}
              onChangeText={setFieldValue(FormKey.COUNT_ID)}
              errorMessage={errors[FormKey.COUNT_ID]}
              errorStyle={styles.formError}
              keyboardType="number-pad"
              autoComplete="off"
              enablesReturnKeyAutomatically
              blurOnSubmit={false}
              label="Count ID"
              placeholder="Count ID"
              returnKeyType="go"
              onSubmitEditing={onSubmitPress}
            />
          </View>
        </View>
      </ScreenLayout.ScrollContent>
    </ScreenLayout>
  );
};

const styles = StyleSheet.create({
  contentContainer: {
    flex: 1,
    width: '100%',
    alignItems: 'center',
    paddingVertical: 20 + pagePadding.Y,
  },
  title: {
    marginBottom: 12,
  },
  image: {
    marginBottom: 36,
  },
  textContainer: {
    alignItems: 'center',
    width: '100%',
    paddingVertical: 20,
  },
  formContainer: {
    flex: 1,
    width: '100%',
  },
  formError: {
    color: Colors.SUPPORT_2100,
  },
});

export default VerifyCount;
