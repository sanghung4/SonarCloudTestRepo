import { TextInput } from 'react-native';
import { VarianceLocationItem } from 'api';
import { transform } from 'lodash';

export const INPUT_ACCESSORY_ID = 'input-accessory-view-id';

export const getInitialVarianceInputRefs = (items: VarianceLocationItem[]) => {
  return transform<VarianceLocationItem, { [key: string]: TextInput | null }>(
    items,
    (result, current) => {
      current.tagNum && (result[current.tagNum] = null);
    },
    {}
  );
};
