import { LocationSummary, Location } from 'api';

export const getLocationStatus = (location: LocationSummary | Location) => {
  if (location.totalCounted === 0) {
    return 'Not Started';
  }
  if (location.committed) {
    return 'Completed';
  }
  return 'Started';
};

export const countStatusMap = {
  Completed: 'success',
  Started: 'blue',
  'Not Started': 'default',
} as const;
