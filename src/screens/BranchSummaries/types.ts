import { LocationSummary } from 'api';
import { StyleProp, ViewStyle } from 'react-native';

export interface BranchProgressBarProps {
  value: number;
  total?: number;
  minLabel?: React.ReactNode;
  maxLabel?: React.ReactNode;
  minLabelColor: string;
  minMaxLabelsWrapperStyle: StyleProp<ViewStyle>;
  progressBarContainerStyle: StyleProp<ViewStyle>;
  testID?: string;
}

export interface BranchItemProps {
  location: LocationSummary;
}
