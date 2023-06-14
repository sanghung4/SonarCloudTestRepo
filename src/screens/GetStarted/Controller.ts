import { CustomIconNames } from 'components/CustomIcon';
import { getScreenTestingIds } from 'test-utils/testIds';
import { RouteNames } from '../../constants/routes';
import { ControllerProps } from './types';

const testIds = getScreenTestingIds('GetStarted');

export const controller: ControllerProps[] = [
  {
    icon: CustomIconNames.CountLocations,
    text: 'View Count Locations',
    screen: RouteNames.COUNT_LOCATIONS,
    managerOnly: false,
    eclipseOnly: false,
    testID: testIds.navTileCountLocations,
  },
  {
    icon: CustomIconNames.ScanLocation,
    text: 'Scan Location with Camera',
    screen: RouteNames.SCAN_LOCATION,
    managerOnly: false,
    eclipseOnly: false,
    testID: testIds.navTileScanLocation,
  },
  {
    icon: CustomIconNames.ManualEntry,
    text: 'Manually Enter Location',
    screen: RouteNames.MANUAL_LOCATION_ENTRY,
    managerOnly: false,
    eclipseOnly: false,
    testID: testIds.navTileManualEntry,
  },
  {
    icon: CustomIconNames.WriteIns,
    text: 'View All \nWrite-Ins',
    screen: RouteNames.WRITE_INS,
    managerOnly: true,
    eclipseOnly: false,
    testID: testIds.navTileWriteIns,
  },
  {
    icon: CustomIconNames.BranchSummaries,
    text: 'View Branch Summaries',
    screen: RouteNames.BRANCH_SUMMARIES,
    managerOnly: true,
    eclipseOnly: false,
    testID: testIds.navTileBranchSummaries,
  },
  {
    icon: CustomIconNames.Variance,
    text: 'View Variance Summary',
    screen: RouteNames.VARIANCE_SUMMARY,
    managerOnly: true,
    eclipseOnly: true,
    testID: testIds.navTileVariance,
  },
  {
    icon: CustomIconNames.VarianceCountLocations,
    text: 'View Variance Count Locations',
    screen: RouteNames.VARIANCE_LOCATION_LIST,
    managerOnly: false,
    eclipseOnly: true,
    testID: testIds.navTileVarianceCountLocations,
  },
];
