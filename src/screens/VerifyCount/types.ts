import { StackNavigationProp } from '@react-navigation/stack';
import { AppStackParamList } from 'navigation/types';
import { TextInput } from 'react-native';

export enum FormKey {
  BRANCH_ID = 'branchID',
  COUNT_ID = 'countID',
}

type FormStruct<T> = Record<FormKey, T>;

export type InitialRefs = FormStruct<TextInput | null>;
export type InitialValues = FormStruct<string>;
export type Validation = FormStruct<(value: string) => string>;

export interface VerifyCountProps {
  navigation: StackNavigationProp<AppStackParamList, 'VerifyCount'>;
}
