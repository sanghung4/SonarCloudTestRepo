import { FormInputProps } from 'components/FormInput';
import { Platform } from 'react-native';

export type Form<FormKeys extends string> = {
  [K in FormKeys]: Partial<FormInputProps>;
};

export enum WriteInFormKey {
  CATALOG_NUM = 'catalogNum',
  UPC_NUM = 'upcNum',
  DESCRIPTION = 'description',
  LOCATION = 'locationId',
  UOM = 'uom',
  QUANTITY = 'quantity',
  CREATED_BY = 'createdBy',
  COMMENT = 'comment',
}

export const writeInForm: Form<WriteInFormKey> = {
  [WriteInFormKey.CATALOG_NUM]: {
    label: 'Catalog Number',
  },
  [WriteInFormKey.UPC_NUM]: {
    label: 'UPC Number',
  },
  [WriteInFormKey.DESCRIPTION]: {
    label: 'Item Description',
    required: true,
    maxLength: 90,
    numberOfLines: 1,
    style: { maxHeight: 100 },
  },
  [WriteInFormKey.LOCATION]: {
    label: 'Location',
    required: true,
  },
  [WriteInFormKey.UOM]: {
    label: 'Unit of Measure (UOM)',
  },
  [WriteInFormKey.QUANTITY]: {
    label: 'Quantity',
    required: true,
    keyboardType: 'number-pad',
    maxLength: 7,
  },
  [WriteInFormKey.CREATED_BY]: {
    label: 'Submit By',
    readonly: true,
  },
  [WriteInFormKey.COMMENT]: {
    label: 'Comments',
    maxLength: 80,
    numberOfLines: 1,
    style: { maxHeight: Platform.OS === 'android' ? 90 : 75 },
  },
};

export enum InputLength {
  MINCRON = 7,
  ECLIPSE = 6,
}

export const NUMBER_ACCESSORY_NATIVE_ID = 'NUMBER_ACCESSORY_NATIVE_ID';

// TODO: Go back to these screens and swap out to use Form

// export enum VerifyCountFormKey {
//   COUNT_ID = 'id',
//   BRANCH_ID = 'branchId',
// }

// export const verifyCountForm: Form<VerifyCountFormKey> = {
//   [VerifyCountFormKey.BRANCH_ID]: {
//     label: 'Branch',
//     returnKeyType: 'next',
//   },
//   [VerifyCountFormKey.COUNT_ID]: {
//     label: 'Count ID',
//     returnKeyType: 'go',
//   },
// };
