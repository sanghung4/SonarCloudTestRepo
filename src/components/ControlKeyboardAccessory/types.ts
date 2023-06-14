import { ViewProps } from 'react-native';

export type ControlKeyboardAccessoryProps = {
  inputAccessoryViewID: string;
  prevDisabled: boolean;
  nextDisabled: boolean;
  onDonePress: () => void;
  onNextPress: () => void;
  onPrevPress: () => void;
  onLayout?: ViewProps['onLayout'];
  testID?: string | undefined;
};
