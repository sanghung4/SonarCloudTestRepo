import React, { useEffect, useState } from 'react';
import { useIsFocused } from '@react-navigation/native';
import { Icon } from 'react-native-elements';

import { Colors, FontWeight as fw } from 'constants/style';
import { useConfig } from 'hooks/useConfig';

import { Text } from 'components/Text';
import { BottomSheet } from 'components/BottomSheet';
import { Scanner } from 'components/Scanner';
import { usePermissions } from 'hooks/usePermissions';
import { useOverlay } from 'providers/Overlay';
import { BarCodeEvent } from 'expo-barcode-scanner';
import { useLocation } from 'hooks/useLocation';
import { RouteNames } from 'constants/routes';
import { getError } from 'utils/error';
import { ErrorType } from 'constants/error';
import { useLoading } from 'hooks/useLoading';
import { StyleSheet } from 'react-native';
import { AppScreenProps } from 'navigation/types';
import { ScreenLayout } from 'components/ScreenLayout';
import { CustomButton } from 'components/CustomButton';
import { getScreenTestingIds } from 'test-utils/testIds';
import useRenderListener from 'hooks/useRenderListener';

const ScanLocation = ({ navigation }: AppScreenProps<'ScanLocation'>) => {
  useRenderListener();

  const { showAlert, toggleLoading } = useOverlay();
  const [{ count }] = useConfig();
  const isFocused = useIsFocused();
  const permission = usePermissions();

  const { getLocation, loading } = useLocation({
    onCompleted: () => {
      toggleLoading(false);
      setScanning(false);
      navigation.navigate(RouteNames.LOCATION_ITEMS);
    },
    onError: (e) => {
      setScanning(false);
      showAlert(getError(ErrorType.LOCATION, e));
    },
  });

  const [isInfoVisible, setIsInfoVisible] = useState(true);
  const [scanning, setScanning] = useState(false);

  useLoading([loading]);

  useEffect(() => {
    if (isFocused) {
      setIsInfoVisible(true);
    }
  }, [isFocused]);

  const onScan = (code: BarCodeEvent) => {
    setScanning(true);
    showAlert({
      title: 'Scan Successful',
      description: (
        <Text style={styles.alertDescription}>
          Are you ready to start counting location{' '}
          <Text fontWeight={fw.BOLD}>{code.data.toUpperCase()}</Text>
        </Text>
      ),
      actions: [
        {
          title: 'Yes',
          type: 'primary',
          onPress: () => getLocation(code.data.toUpperCase()),
        },
        {
          title: 'No, rescan.',
          type: 'link',
          onPress: () => setScanning(false),
        },
      ],
      onClose: () => setScanning(false),
    });
  };

  const testIds = getScreenTestingIds('ScanLocation');

  return (
    <ScreenLayout testID={testIds.screenLayout}>
      <Scanner
        scanning={scanning}
        showPlaceholder={!isFocused || !permission.granted}
        onBarCodeScanned={onScan}
      />

      <Icon
        type="material"
        name="border-color"
        raised
        color={Colors.PRIMARY_3100}
        onPress={() => setIsInfoVisible(true)}
        containerStyle={styles.infoIcon}
        testID={testIds.pencilIcon}
        accessibilityLabel="pencil-icon"
      />

      <BottomSheet
        isVisible={permission.granted && isInfoVisible}
        onClose={() => setIsInfoVisible(false)}
        onBackdropPress={() => setIsInfoVisible(false)}
        contentStyle={styles.bottomSheetContent}
        testID={testIds.bottomSheet}
      >
        <BottomSheet.Title testID={testIds.bottomSheet}>
          Location Scanner
        </BottomSheet.Title>
        <BottomSheet.Description testID={testIds.bottomSheet}>
          Scan a location barcode to pull up the items for count {count?.id}{' '}
          that require quantity entry.
        </BottomSheet.Description>
        <CustomButton
          title="Manually Enter Your Location"
          type="link"
          onPress={() => navigation.navigate(RouteNames.MANUAL_LOCATION_ENTRY)}
          testID={testIds.bottomSheet}
        />
      </BottomSheet>
    </ScreenLayout>
  );
};

const styles = StyleSheet.create({
  alertDescription: {
    flexGrow: 1,
    marginTop: 12,
  },
  bottomSheetContent: {
    paddingTop: 32,
    alignItems: 'center',
  },
  infoIcon: {
    position: 'absolute',
    bottom: 36,
    right: 16,
  },
});

export default ScanLocation;
