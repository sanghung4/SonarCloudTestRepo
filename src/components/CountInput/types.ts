import { LocationItem, VarianceLocationItem } from 'api';

export interface CountInputProps {
  item: LocationItem | VarianceLocationItem;
  inputAccessoryViewID: string;
  disableDetails?: boolean;
  onPress: () => void;
  onFocus: () => void;
  onBlur: (value: string) => void;
  onChangeText: (value: string) => void;
  onSubmitEditing: () => void;
  testID?: string;
}
