import { TableInstance } from 'components/Table';

/**
 * Types
 */
export type MockTableData = {
  id: string;
  bool: boolean;
  num: number;
};
type Mocks = {
  full: TableInstance<MockTableData>;
  empty: TableInstance<MockTableData>;
};

/**
 * Dummy
 */
export const mockTableData: MockTableData[] = [
  { id: '0', bool: true, num: 123 },
  { id: '1', bool: false, num: 456 },
  { id: '2', bool: true, num: 789 },
  { id: '3', bool: false, num: 0 }
];

/**
 * Mocks
 */
const mocks: Mocks = {
  full: {
    data: mockTableData,
    columns: ['id', 'bool', 'num', 'etc'],
    config: {
      id: { cell: ({ id }) => id },
      bool: { header: (k) => <p>{k}</p>, cell: ({ bool }) => bool },
      num: { cell: ({ num }) => <p>{num}</p> }
    }
  },
  empty: {
    data: [],
    columns: ['id', 'bool', 'num'],
    config: {}
  }
};

export default mocks;
