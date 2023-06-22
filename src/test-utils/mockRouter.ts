import { useHistory, useLocation } from 'react-router-dom';

export const mockHistory: ReturnType<typeof useHistory> = {
  length: 0,
  action: 'PUSH',
  location: {
    pathname: '',
    search: '',
    state: '',
    hash: ''
  },
  push: jest.fn(),
  replace: jest.fn(),
  go: jest.fn(),
  goBack: jest.fn(),
  goForward: jest.fn(),
  block: jest.fn(),
  listen: jest.fn(),
  createHref: jest.fn()
};

export const mockLocation: ReturnType<typeof useLocation> = {
  pathname: '',
  search: '',
  state: undefined,
  hash: ''
};
