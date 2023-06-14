import React from 'react';
import { BarCodeScanner } from 'expo-barcode-scanner';
import {
  StyleProp,
  StyleSheet,
  View,
  ViewStyle,
  Platform,
  Dimensions,
} from 'react-native';
import { useHeaderHeight } from '@react-navigation/stack';
import { ScannerMask } from './ScannerMask';
import { ScannerProps } from './types';
import { SCANNER_TEST_IDS } from './types';
import { Colors } from 'constants/style';

const Scanner = ({
  scanning,
  showMask,
  showPlaceholder,
  onBarCodeScanned,
  containerStyle,
  scannerStyle,
  placeholderStyle,
  maskStyle,
}: ScannerProps) => {
  const headerHeight = useHeaderHeight();
  const isIOS = Platform.OS === 'ios';

  return (
    <View
      testID={SCANNER_TEST_IDS.SCANNER}
      style={[
        getContainerStyle(headerHeight),
        containerStyle && containerStyle,
      ]}
    >
      {showPlaceholder ? (
        <View
          testID={SCANNER_TEST_IDS.SECONDARY_VIEW}
          style={[
            StyleSheet.absoluteFillObject,
            placeholderStyle && placeholderStyle,
          ]}
        />
      ) : (
        <BarCodeScanner
          type={BarCodeScanner.Constants.Type.back}
          onBarCodeScanned={scanning ? undefined : onBarCodeScanned}
          style={[StyleSheet.absoluteFillObject, scannerStyle && scannerStyle]}
        />
      )}
      {isIOS && showMask ? <ScannerMask maskStyle={maskStyle} /> : null}
    </View>
  );
};

Scanner.defaultProps = {
  showMask: true,
};

const getContainerStyle = (headerHeight: number): StyleProp<ViewStyle> => ({
  backgroundColor: Colors.BLACK,
  width: '100%',
  flex: 1,
  ...Platform.select({
    android: {
      height: Dimensions.get('window').height - headerHeight,
    },
  }),
});

export default Scanner;
