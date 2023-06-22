import { TableInstance } from 'react-table';

import {
  mockCoercedUsers,
  mockUseCustomerApprovalTableProps
} from 'CustomerApproval/tests/mocks';
import useCustomerApprovalTable, {
  filterMemoLogic,
  formatDays
} from 'CustomerApproval/util/useCustomerApprovalTable';
import * as t from 'locales/en/translation.json';
import { mockTableInstance } from 'test-utils/mockTableInstance';
import { render } from 'test-utils/TestWrapper';

/**
 * Types
 */
type HookOutput = ReturnType<typeof useCustomerApprovalTable>;

/**
 * Mock values
 */
const defaultMockHookOutput: HookOutput = {
  handleFilterReset: jest.fn(),
  handleSubmitFilter: jest.fn(),
  tableInstance: mockTableInstance as TableInstance<any>
};
const mocks = {
  props: { ...mockUseCustomerApprovalTableProps },
  table: { ...(mockTableInstance as TableInstance<any>) }
};
const mockT = (t: string) => t;

/**
 * Mock methods
 */
jest.mock('react-table', () => ({
  ...jest.requireActual('react-table'),
  useTable: () => ({ ...mocks.table })
}));

/**
 * Setup function
 */
function setup(p: typeof mocks) {
  const output: HookOutput = { ...defaultMockHookOutput };
  function MockComponent() {
    Object.assign(output, useCustomerApprovalTable(p.props));
    return null;
  }
  render(<MockComponent />);
  return output;
}

/**
 * Test
 */
describe('CustomerApproval - util/useCustomerApprovalTable', () => {
  afterEach(() => {
    mocks.props = { ...mockUseCustomerApprovalTableProps };
    mocks.table = { ...(mockTableInstance as TableInstance<any>) };
  });

  it('expect `handleFilterReset` to call some functions', () => {
    setup(mocks).handleFilterReset();
    expect(mocks.table.setAllFilters).toBeCalledWith([]);
    expect(mocks.props.setFilterValue).toBeCalledWith(t.common.all);
  });

  it('expect `handleSubmitFilter` to call some functions when filterValue is all', () => {
    mocks.props.filterValue = t.common.all;
    setup(mocks).handleSubmitFilter();
    expect(mocks.table.gotoPage).toBeCalledWith(0);
    expect(mocks.table.setAllFilters).toBeCalledWith([]);
  });
  it('expect `handleSubmitFilter` to call some functions when filterValue is NOT all', () => {
    mocks.props.filterValue = 'test';
    setup(mocks).handleSubmitFilter();
    expect(mocks.table.gotoPage).toBeCalledWith(0);
    expect(mocks.table.setFilter).toBeCalledWith(
      'branchId',
      mocks.props.filterValue
    );
  });

  it('expect `formatDays` to return 2 Days', () => {
    const result = formatDays(2, mockT);
    expect(result).toBe('2 common.days');
  });
  it('expect `formatDays` to return 1 Day', () => {
    const result = formatDays(1, mockT);
    expect(result).toBe('1 common.day');
  });
  it('expect `formatDays` to return < 1 Day', () => {
    const result = formatDays(0, mockT);
    expect(result).toBe('< 1 common.day');
  });

  it('expect `filterMemoLogic` to return branch', () => {
    const setFilter = jest.fn();
    const result = filterMemoLogic('', [], setFilter, mockT);
    expect(setFilter).toBeCalledWith('');
    expect(result[0].id).toBe('branchId');
    expect(result[0].value).toBe('');
  });
  it('expect `filterMemoLogic` to return blank', () => {
    const setFilter = jest.fn();
    const users = [...mockCoercedUsers];
    const result = filterMemoLogic('', users, setFilter, mockT, 3);
    expect(setFilter).toBeCalledWith('common.all');
    expect(result[0]).toBe(undefined);
  });
});
