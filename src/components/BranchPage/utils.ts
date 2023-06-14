import { GetLocationsQuery } from 'api';
import { OldColors, Colors } from 'constants/style';

export const ButtonValues = [
  {
    title: 'Not Started',
    value: 'Not Started',
    color: Colors.SECONDARY_2100,
  },
  {
    title: 'Started',
    value: 'Started',
    color: OldColors.BLUE,
  },
  {
    title: 'Completed',
    value: 'Completed',
    color: Colors.SUPPORT_1100,
  },
];

export const getLocationData = (data?: GetLocationsQuery) => ({
  locations: data?.locations.content || [],
  totalLocations: data?.locations.totalLocations || 1,
  totalCounted: data?.locations.totalCounted || 0,
});
