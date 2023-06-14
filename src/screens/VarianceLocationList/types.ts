import { VarianceLocationSummary } from 'api';

export interface LocationListItemProps {
  testID?: string;
  location: VarianceLocationSummary;
  onPress: () => void;
}

export enum SortByEnum {
  ASCENDING = 'Location: Ascending',
  DESCENDING = 'Location: Descending',
  NET_HIGHTOLOW = 'Net Cost: High to Low',
  NET_LOWTOHIGH = 'Net Cost: Low to High',
  GROSS_HIGHTOLOW = 'Gross Cost: High to Low',
  GROSS_LOWTOHIGH = 'Gross Cost: Low to High',
}
