import { Location, LocationItem } from 'api';

export interface DetailComponentProps {
  item: LocationItem;
  location: Location;
  value: string;
  valid: boolean;
  onChange: (text: string) => void;
  mincron: boolean;
  testID: string;
}
