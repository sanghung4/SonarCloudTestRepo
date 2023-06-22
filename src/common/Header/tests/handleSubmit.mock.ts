import { FormEvent } from 'react';

export const mockFormEvent: FormEvent<HTMLFormElement> = {
  nativeEvent: {} as Event,
  currentTarget: document.createElement('form'),
  target: {
    addEventListener: jest.fn(),
    dispatchEvent: jest.fn(),
    removeEventListener: jest.fn()
  },
  bubbles: false,
  cancelable: false,
  defaultPrevented: false,
  eventPhase: 0,
  isTrusted: false,
  preventDefault: jest.fn(),
  isDefaultPrevented: jest.fn(),
  stopPropagation: jest.fn(),
  isPropagationStopped: jest.fn(),
  persist: jest.fn(),
  timeStamp: 0,
  type: 'test'
};
export const mockPropsDefault = {
  authenticated: false,
  email: '',
  push: jest.fn(),
  setCachedTerm: jest.fn(),
  setOpen: jest.fn(),
  setPage: jest.fn(),
  setTerm: jest.fn(),
  term: ''
};
