import { FormKey, InitialRefs, InitialValues, Validation } from './types';

export const initialRefs: InitialRefs = {
  [FormKey.BRANCH_ID]: null,
  [FormKey.COUNT_ID]: null,
};

export const initialValues: InitialValues = {
  [FormKey.BRANCH_ID]: '',
  [FormKey.COUNT_ID]: '',
};

export const validation: Validation = {
  [FormKey.BRANCH_ID]: (value: string) => {
    let error = '';
    if (!value) {
      error = 'Branch ID is required';
    }
    return error;
  },
  [FormKey.COUNT_ID]: (value: string) => {
    let error = '';
    if (!value) {
      error = 'Cycle / Count ID is required';
    }
    return error;
  },
};
