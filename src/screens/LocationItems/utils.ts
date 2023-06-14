import { Location, LocationItem, LocationItemStatus } from 'api';
import { isArray, isNumber, toString, transform, orderBy } from 'lodash';
import { TextInput } from 'react-native';
import { FormStructure, FormStructureCallback } from './types';

export const locationMap = (
  location: Location | null
): {
  location: Location | null;
  totalCounted: number;
  totalProducts: number;
  items: LocationItem[];
} => ({
  location,
  totalCounted:
    location && isNumber(location.totalCounted) ? location.totalCounted : 0,
  totalProducts:
    location && isNumber(location.totalProducts) ? location.totalProducts : 1,
  items:
    location && isArray(location.items)
      ? orderBy(location.items, 'catNum')
      : [],
});

export const isStaged = (item: LocationItem): boolean =>
  item.status === LocationItemStatus.STAGED;

export const isComplete = (validation: Record<string, boolean>): boolean =>
  Object.values(validation).every(Boolean);

export const formStructure = <T>(
  items: LocationItem[],
  cb: FormStructureCallback<T>
) => {
  return transform<LocationItem, FormStructure<T>>(
    items,
    (res, item) => item.tagNum && (res[item.tagNum] = cb(item)),
    {}
  );
};

export const getInitialInputRefs = (items: LocationItem[]) =>
  formStructure<TextInput | null>(items, () => null);

export const getInitialValidRefs = (items: LocationItem[]) =>
  formStructure(items, () => false);

export const cleanValue = (value: string): string =>
  value.replace(/[^0-9]/g, '');

export const validation = (value: string): string => {
  let error = '';
  if (!value) {
    error = 'Value is required';
  }
  return error;
};

export const valueToString = (item: LocationItem): string => {
  return isStaged(item) ? toString(item.quantity) : '';
};

export const CONTROL_ACCESSORY_NATIVE_ID = 'CONTROL_ACCESSORY_NATIVE_ID';
