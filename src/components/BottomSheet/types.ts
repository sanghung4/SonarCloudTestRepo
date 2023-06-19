import { StyleProp, ViewStyle, GestureResponderEvent } from 'react-native';

export interface BottomSheetProps {
  minHeight?: number;
  children?: React.ReactNode;
  contentStyle?: StyleProp<ViewStyle>;
  containerStyle?: StyleProp<ViewStyle>;
  closeIcon?: boolean;
  isVisible: boolean;
  onBackdropPress?: (e: GestureResponderEvent) => void;
  onClose?: (e: GestureResponderEvent) => void;
  testID?: string;
}

export interface BottomSheetTitleProps {
  children?: React.ReactNode;
  testID?: string;
}
export interface BottomSheetSubtitleProps {
  children?: React.ReactNode;
  testID?: string;
}
export interface BottomSheetDescriptionProps {
  children?: React.ReactNode;
  testID?: string;
}

export enum BOTTOM_SHEET_TEST_IDS {
  COMPONENT = 'bottomSheetTest',
  TITLE = 'bottomSheetTitleTest',
  SUBTITLE = 'bottomSheetSubtitleTest',
  DESCRIPTION = 'bottomSheetDescriptionTest',
}
