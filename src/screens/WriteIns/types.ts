import { WriteIn } from 'api';

export enum WriteInFilter {
  ALL = 'View All',
  RESOLVED = 'Resolved',
  UNRESOLVED = 'Unresolved',
}

export interface WriteInProps {
  item: WriteIn;
}
