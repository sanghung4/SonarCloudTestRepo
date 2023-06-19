import { LocationItem, LocationItemStatus } from 'api';
import { isNumber } from 'lodash';

export const getInitialValue = (item: LocationItem | null) =>
  item && item.status === LocationItemStatus.STAGED && isNumber(item.quantity)
    ? item.quantity
    : null;
