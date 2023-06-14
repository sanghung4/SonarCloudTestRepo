import { Location, Maybe, WriteInInput } from 'api';
import { writeInForm, WriteInFormKey } from 'constants/form';
import { transform } from 'lodash';
import { FormErrors } from 'providers/Form';
import { WriteInForm } from './types';

export const UOM_INDEX_MAP = {
  EA: 0,
  FT: 1,
  OTHER: 2,
};

export const getWriteInForm = () => {
  return transform(
    writeInForm,
    (form, field, key: WriteInFormKey) => {
      if (key === WriteInFormKey.CREATED_BY) {
        return;
      }
      form[key] = field;
    },
    {} as typeof writeInForm
  );
};

export const getInitialValues = (location?: Maybe<Location>) => {
  return transform(
    WriteInFormKey,
    (values, key) => {
      if (key === WriteInFormKey.CREATED_BY) {
        return;
      }
      if (key === WriteInFormKey.LOCATION) {
        values[key] = location ? location.id : '';
      } else {
        values[key] = '';
      }
    },
    {} as WriteInForm
  );
};

export const validate = (values: WriteInForm) => {
  return transform(
    WriteInFormKey,
    (errors, key) => {
      if (writeInForm[key].required && !values[key]) {
        errors[key] = `Requires ${writeInForm[key].label}`;
      } else {
        errors[key] = '';
      }
    },
    {} as FormErrors<typeof writeInForm>
  );
};

export const getPayload = (values: WriteInForm, selectedUOMIndex: number) => {
  return transform(
    WriteInFormKey,
    (res, key) => {
      if (key === WriteInFormKey.CREATED_BY) {
        return;
      }
      if (key === WriteInFormKey.QUANTITY) {
        res[key] = parseInt(values[key], 10);
      } else if (key === WriteInFormKey.UOM) {
        let value = '';
        switch (selectedUOMIndex) {
          case UOM_INDEX_MAP.FT:
            value = 'FT';
            break;
          default:
            value = 'EA';
            break;
        }
        res[key] = value;
      } else {
        res[key] = values[key];
      }
    },
    {} as WriteInInput
  );
};
