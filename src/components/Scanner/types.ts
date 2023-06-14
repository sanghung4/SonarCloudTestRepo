import { BarCodeEvent } from 'expo-barcode-scanner';
import { StyleProp, ViewStyle } from 'react-native';

export interface ScannerProps {
  scanning?: boolean;
  onBarCodeScanned: (event: BarCodeEvent) => void;
  showMask?: boolean;
  showPlaceholder?: boolean;
  containerStyle?: StyleProp<ViewStyle>;
  scannerStyle?: StyleProp<ViewStyle>;
  maskStyle?: StyleProp<ViewStyle>;
  placeholderStyle?: StyleProp<ViewStyle>;
}

export interface ScannerMaskProps {
  maskStyle?: StyleProp<ViewStyle>;
}

export enum SCANNER_TEST_IDS {
  SCANNER = 'ScannerTest',
  SECONDARY_VIEW = 'SecondaryView',
  SCANNER_MASK = 'ScannerMask',
}
