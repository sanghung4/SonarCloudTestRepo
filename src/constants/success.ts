import { AlertPayload } from 'providers/Overlay/types';

export enum SuccessType {
  GENERIC = 'generic',
  CREATE_WRITE_IN = 'createWriteIn',
  UPDATE_WRITE_IN = 'updateWriteIn',
}

export type SuccessDictionary = {
  [K in SuccessType]: AlertPayload;
};

export const success: SuccessDictionary = {
  [SuccessType.GENERIC]: {
    title: 'Success!',
    description: '',
  },
  [SuccessType.CREATE_WRITE_IN]: {
    title: 'Success!',
    description: "You've created a write-in for your manager to view.",
  },
  [SuccessType.UPDATE_WRITE_IN]: {
    title: 'Success!',
    description: "You've updated the write-in.",
  },
};
