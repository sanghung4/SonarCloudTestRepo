import { ViewProps } from 'react-native';
import { LocationItem, Maybe } from 'api';

export type FormItem = { key: string; value: number | null };
export type FormStructure<T = any> = Record<string, T>;
export type FormStructureCallback<T> = (item: LocationItem) => T;
export interface LocationItemProps {
  item: LocationItem;
  onSubmitEditing: () => void;
  inputAccessoryViewID?: string;
  onFocus: () => void;
  onUpdated: (args: { productTag: string; quantity: string }) => void;
  onPress: () => void;
  validation: (valid: boolean) => void;
}

export type ControlAccessoryViewProps = {
  prevDisabled: boolean;
  nextDisabled: boolean;
  onDonePress: () => void;
  onNextPress: () => void;
  onPrevPress: () => void;
  onLayout?: ViewProps['onLayout'];
};

export type QuantityMap = { [key: string]: string | null };

export type QuantityToUpdate =
  | {
      productTag: string;
      quantity: string;
    }
  | {};

export type TagNumber = Maybe<string> | undefined;
