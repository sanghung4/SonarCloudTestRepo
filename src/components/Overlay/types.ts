import { GestureResponderEvent } from 'react-native';

export interface NeedHelpProps {
  onDismissRequest?: (event: GestureResponderEvent) => void;
}

export enum OVERLAY_TEST_IDS {
  ALERT = 'alertTest',
  OVERLAY = 'overlayTest',
}
