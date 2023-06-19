import { StyleProp, ViewStyle } from 'react-native';
import { SvgProps } from 'react-native-svg';

export enum CustomIconNames {
  BranchSummaries = 'BranchSummaries',
  WriteIns = 'WriteIns',
  CountLocations = 'CountLocations',
  ScanLocation = 'ScanLocation',
  ManualEntry = 'ManualEntry',
  Variance = 'Variance',
  VarianceCountLocations = 'VarianceCountLocations',
  Search = 'Search',
  RightChevron = 'RightChevron',
  Refresh = 'Refresh',
  CircleCheck = 'CircleCheck',
  Info = 'Info',
  Warning = 'Warning',
  Delete = 'Delete',
  RadioActive = 'RadioActive',
  RadioInactive = 'RadioInactive',
  DoubleCarat = 'DoubleCarat',
}

export interface CustomIconProps extends SvgProps {
  name: CustomIconNames;
  size?: SvgProps['height'];
  containerStyle?: StyleProp<ViewStyle>;
  onPress?: () => void;
}
