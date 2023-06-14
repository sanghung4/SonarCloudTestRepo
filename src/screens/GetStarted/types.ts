import { CustomIconNames } from 'components/CustomIcon';
import { RouteNames } from 'constants/routes';

export interface ControllerProps {
  icon: CustomIconNames;
  text: string;
  screen:
    | RouteNames.BRANCH_SUMMARIES
    | RouteNames.WRITE_INS
    | RouteNames.COUNT_LOCATIONS
    | RouteNames.SCAN_LOCATION
    | RouteNames.MANUAL_LOCATION_ENTRY
    | RouteNames.VARIANCE_SUMMARY
    | RouteNames.VARIANCE_LOCATION_LIST;
  managerOnly: boolean;
  eclipseOnly: boolean;
  testID: string;
}

export interface NavTileProps {
  item: ControllerProps;
}
