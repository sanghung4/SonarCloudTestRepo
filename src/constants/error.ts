import { noop } from 'lodash';
import { AlertPayload } from 'providers/Overlay/types';
import {
  getComponentTestingIds,
  getScreenTestingIds,
} from 'test-utils/testIds';

export type APIError = {
  code: ErrorCodes;
  message: string | null;
};

export enum ErrorCode {
  UNPROCESSABLE_ENTITY = '422',
  NOT_FOUND = '404',
  CONFLICT = '409',
  FORBIDDEN = '403',
  BAD_REQUEST = '400',
  SERVER = '500',
}

export enum AuthErrorCode {
  UNAUTHENTICATED = 'UNAUTHENTICATED',
}

export enum EclipseErrorCode {
  NO_ECLIPSE_CREDENTIALS = 'NO_ECLIPSE_CREDENTIALS_FOUND',
  INVALID_ECLIPSE_CREDENTIALS = 'INVALID_ECLIPSE_CREDENTIALS',
}

export type ErrorCodes = ErrorCode | AuthErrorCode | EclipseErrorCode;

export type ErrorDictionary = {
  [K in ErrorCodes]?: AlertPayload;
};

export enum ErrorType {
  LOCATIONS = 'locations',
  LOCATION = 'location',
  PRODUCT = 'product',
  COUNT = 'count',
  ADD_TO_COUNT = 'addToCount',
  UPDATE_COUNT = 'updateCount',
  COMPLETE_COUNT = 'completeCount',
  WRITE_IN = 'writeIn',
  WRITE_INS = 'writeIns',
  CREATE_WRITE_IN = 'createWriteIn',
  UPDATE_WRITE_IN = 'updateWriteIn',
  RESOLVE_WRITE_IN = 'resolveWriteIn',
}

const locationsError: ErrorDictionary = {
  [ErrorCode.BAD_REQUEST]: {
    title: 'Uh Oh!',
    description:
      'There has been an error getting the branch summaries. Please try again.',
  },
};

const locationError: ErrorDictionary = {
  [ErrorCode.BAD_REQUEST]: {
    title: 'Uh Oh!',
    description:
      'There has been an error scanning or entering the location. Please try again.',
    actions: [
      {
        title: 'Ok, Try Again',
        onPress: noop,
        key: 'ok-try-again',
      },
      {
        title: 'Cancel',
        type: 'link',
        onPress: noop,
        key: 'cancel',
      },
    ],
  },
  [ErrorCode.UNPROCESSABLE_ENTITY]: {
    title: 'Uh Oh!',
    description:
      'There has been an error scanning or entering the location. Please try again.',
    actions: [
      { title: 'Ok, Try Again', onPress: noop, key: 'ok-try-again' },
      { title: 'Cancel', type: 'link', onPress: noop, key: 'cancel' },
    ],
  },
  [ErrorCode.NOT_FOUND]: {
    title: 'Oops!',
    description:
      'The location you scanned or entered does not exist. Please try again.',
    actions: [
      { title: 'Ok, Got It', onPress: noop },
      { title: 'Cancel', type: 'link', onPress: noop },
    ],
  },
  [ErrorCode.CONFLICT]: {
    title: 'Oops!',
    description:
      'This location has already been counted by another individual. Please scan or enter a new location to begin a count in a location that has yet to be completed.',
    actions: [
      { title: 'Scan a New Location', onPress: noop },
      { title: 'Cancel', type: 'link', onPress: noop },
    ],
  },
  [ErrorCode.FORBIDDEN]: {
    title: 'Oh no!',
    description:
      'The location you scanned is currently being counted by another team member. Please scan or enter a new location to being counting a location that has yet to be completed.',
    actions: [
      { title: 'Ok, Scan a New Location', onPress: noop },
      { title: 'Cancel', type: 'link', onPress: noop },
    ],
  },
};

const productError: ErrorDictionary = {
  [ErrorCode.BAD_REQUEST]: {
    title: 'Uh Oh!',
    description:
      'There has been an error scanning the product. Please try again.',
    actions: [
      { title: 'Ok, Try Again', onPress: noop, key: 'ok-try-again' },
      { title: 'Cancel', type: 'link', onPress: noop, key: 'cancel' },
    ],
  },
  [ErrorCode.NOT_FOUND]: {
    title: 'Oops!',
    description: 'The product you scanned does not exist. Please try again.',
    actions: [
      { title: 'Ok, Got It', onPress: noop },
      { title: 'Cancel', type: 'link', onPress: noop },
    ],
  },
};

const countError: ErrorDictionary = {
  [ErrorCode.BAD_REQUEST]: {
    title: 'Uh Oh!',
    description:
      'There has been an error finding your branch and/or count ID. Please confirm that the input is correct and try again.',
    actions: [
      { title: 'Ok, Try Again', onPress: noop, key: 'ok-try-again' },
      { title: 'Cancel', type: 'link', onPress: noop, key: 'cancel' },
    ],
  },
  [EclipseErrorCode.INVALID_ECLIPSE_CREDENTIALS]: {
    title: 'Uh oh!',
    description:
      'The credentials that you submitted is invalid. Please confirm that the input is correct and try again.',
    actions: [
      { title: 'Ok, Got It', onPress: noop },
      { title: 'Cancel', type: 'link', onPress: noop },
    ],
  },
  [EclipseErrorCode.NO_ECLIPSE_CREDENTIALS]: {
    title: 'Uh oh!',
    description:
      'There was no account with the credentials that you submitted. Please confirm that the input is correct and try again.',
    actions: [
      { title: 'Ok, Got It', onPress: noop },
      { title: 'Cancel', type: 'link', onPress: noop },
    ],
  },
};

const completeCountError: ErrorDictionary = {
  [ErrorCode.BAD_REQUEST]: {
    title: 'Uh Oh!',
    description:
      'There has been an error completing this location. Please try again.',
    actions: [
      { title: 'Ok, Try Again', onPress: noop, key: 'ok-try-again' },
      { title: 'Cancel', type: 'link', onPress: noop, key: 'cancel' },
    ],
  },
};

const addToCountError: ErrorDictionary = {
  [ErrorCode.BAD_REQUEST]: {
    title: 'Uh Oh!',
    description:
      'There has been an error adding the product to your location. Please try again.',
  },
};

const updateCount: ErrorDictionary = {
  [ErrorCode.BAD_REQUEST]: {
    title: 'Uh Oh!',
    description:
      'There has been an error updating this product. Please try again.',
  },
};

const writeInError: ErrorDictionary = {
  [ErrorCode.BAD_REQUEST]: {
    title: 'Uh Oh!',
    description:
      'There has been an error getting the write-in. Please try again.',
  },
};

const writeInsError: ErrorDictionary = {
  [ErrorCode.BAD_REQUEST]: {
    title: 'Uh Oh!',
    description:
      'There has been an error getting the write-ins. Please try again.',
  },
};

const createWriteInError: ErrorDictionary = {
  [ErrorCode.BAD_REQUEST]: {
    title: 'Uh Oh!',
    description:
      'There has been an error creating this write-in. Please try again.',
  },
};

const updateWriteInError: ErrorDictionary = {
  [ErrorCode.BAD_REQUEST]: {
    title: 'Uh Oh!',
    description:
      'There has been an error updating this write-in. Please try again.',
  },
};

const resolveWriteInError: ErrorDictionary = {
  [ErrorCode.BAD_REQUEST]: {
    title: 'Uh Oh!',
    description:
      'There has been an error resolving this write-in. Please try again.',
  },
};

export const error = {
  [ErrorType.LOCATIONS]: locationsError,
  [ErrorType.LOCATION]: locationError,
  [ErrorType.PRODUCT]: productError,
  [ErrorType.COUNT]: countError,
  [ErrorType.ADD_TO_COUNT]: addToCountError,
  [ErrorType.UPDATE_COUNT]: updateCount,
  [ErrorType.COMPLETE_COUNT]: completeCountError,
  [ErrorType.WRITE_IN]: writeInError,
  [ErrorType.WRITE_INS]: writeInsError,
  [ErrorType.CREATE_WRITE_IN]: createWriteInError,
  [ErrorType.UPDATE_WRITE_IN]: updateWriteInError,
  [ErrorType.RESOLVE_WRITE_IN]: resolveWriteInError,
} as const;
