import React, { useState } from 'react';
import { StyleSheet } from 'react-native';

import { useOverlay } from 'providers/Overlay';
import { ErrorType } from 'constants/error';
import { getError } from 'utils/error';

import { Section } from 'components/Section';
import { Text } from 'components/Text';
import { Input } from 'components/Input';

import { useConfig } from 'hooks/useConfig';
import { useLocation } from 'hooks/useLocation';
import { useLoading } from 'hooks/useLoading';
import { NUMBER_ACCESSORY_NATIVE_ID } from 'constants/form';
import { RouteNames } from 'constants/routes';
import { Colors } from 'constants/style';
import { AppScreenProps } from 'navigation/types';
import { ScreenLayout } from 'components/ScreenLayout';
import { CustomButton } from 'components/CustomButton';
import { getScreenTestingIds } from 'test-utils/testIds';
import { NumericKeyboardAccessory } from 'components/NumericKeyboardAccessory';
import useRenderListener from 'hooks/useRenderListener';

const ManualLocationEntry = ({
  navigation,
}: AppScreenProps<'ManualLocationEntry'>) => {
  useRenderListener();

  const { showAlert } = useOverlay();
  const [{ count }] = useConfig();
  const [value, setValue] = useState('');

  const { loading: getLocationLoading, getLocation } = useLocation({
    onCompleted: () => {
      navigation.navigate(RouteNames.LOCATION_ITEMS);
      setValue('');
    },
    onError: (error) => {
      showAlert(getError(ErrorType.LOCATION, error));
    },
  });

  useLoading([getLocationLoading]);

  const onChangeText = (text: string) => {
    setValue(text);
  };

  const onSubmitPress = () => {
    const formattedValue = value.toUpperCase();
    getLocation(formattedValue);
  };

  const testIds = getScreenTestingIds('ManualLocationEntry');

  return (
    <ScreenLayout testID={testIds.screenLayout}>
      <ScreenLayout.StaticContent padding>
        <Section width="100%" justifyContent="center" alignItems="center">
          <Section alignItems="center" width="100%" marginBottom={24}>
            <Section marginTop={24} marginBottom={12}>
              <Text h4 color={Colors.PRIMARY_1100} centered>
                Get Started
              </Text>
            </Section>
            <Section>
              <Text centered color={Colors.SECONDARY_2100}>
                Branch Num:{count?.branch.id} Count ID:{count?.id}
              </Text>
            </Section>
            <Section flexDirection="row" marginBottom={24} alignItems="center">
              <Text color={Colors.SECONDARY_2100}> {count?.branch.name}</Text>
            </Section>
            <Text
              centered
              color={Colors.SECONDARY_2100}
              style={styles.textWidth}
            >
              Enter your location area code to begin your count. You can scan
              using your camera, select from a list, or manually enter the area
              number.
            </Text>
          </Section>
          <Section width={'90%'}>
            <Input
              testID={testIds.locationInput}
              inputAccessoryViewID={NUMBER_ACCESSORY_NATIVE_ID}
              value={value}
              keyboardType="default"
              placeholder="Location Code"
              autoComplete="off"
              autoCorrect={false}
              autoCapitalize="none"
              onChangeText={onChangeText}
              onSubmitEditing={onSubmitPress}
            />
          </Section>
          <CustomButton
            title="Scan location using camera"
            type="link"
            onPress={() => navigation.navigate(RouteNames.SCAN_LOCATION)}
            testID={testIds.scanLocationUsingCameraButton}
          />
        </Section>
        <CustomButton
          disabled={!value}
          containerStyle={styles.actionButtonContainer}
          title="Submit Location Code"
          onPress={onSubmitPress}
          testID={testIds.submitLocationCodeButton}
        />
      </ScreenLayout.StaticContent>

      <NumericKeyboardAccessory
        inputAccessoryViewID={NUMBER_ACCESSORY_NATIVE_ID}
        onPress={(num) => {
          const currentValue = value;
          onChangeText(currentValue.concat(num));
        }}
      />
    </ScreenLayout>
  );
};

const styles = StyleSheet.create({
  separator: {
    marginHorizontal: 8,
    marginBottom: 16,
  },
  inputContainer: {
    flex: 1,
  },
  spacing: {
    marginBottom: 12,
  },
  textWidth: {
    width: '90%',
  },
  actionButtonContainer: {
    marginTop: 32,
  },
});

export default ManualLocationEntry;
