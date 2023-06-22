import { fireEvent } from '@testing-library/react';

export const CSV_FILE = new File(
  [
    'Part #,Description,MFR Name,QT\n' +
      '12345,Test 1,Reece,\n' +
      '54321,Test 2,Reece,5'
  ],
  'list.csv',
  { type: 'text/csv' }
);

export const JSON_FILE = new File(
  [
    JSON.stringify([
      {
        partNumber: '12345',
        description: 'Test 1',
        mfrName: 'Reece',
        qty: 1
      },
      {
        partNumber: '54321',
        description: 'Test 2',
        mfrName: 'Reece',
        qty: 5
      }
    ])
  ],
  'list.json',
  {
    type: 'application/json'
  }
);

export const mockData = function mockData(files: File[]) {
  return {
    dataTransfer: {
      files,
      items: files.map((file) => ({
        kind: 'file',
        type: file.type,
        getAsFile: () => file
      })),
      types: ['Files']
    }
  };
};

export const dispatchEvt = function dispatchEvt(
  node: HTMLElement,
  type: string,
  data?: any
) {
  const event = new Event(type, { bubbles: true });
  if (data) Object.assign(event, data);
  fireEvent(node, event);
};

export const BASE_USE_DROPZONE = {
  getInputProps: () => ({
    multiple: false
  }),
  getRootProps: jest.fn(),
  isDragAccept: false,
  isDragReject: false
};

export const ACCEPTED_FILES = {
  isDragAccept: true,
  isDragReject: false
};

export const FILE_REJECTIONS = {
  isDragAccept: false,
  isDragReject: true
};
