import useContractsEffect from 'Contracts/util/useContractsEffect';
import { TableInstance } from 'react-table';
import { mockTableInstance } from 'test-utils/mockTableInstance';
import { render } from 'test-utils/TestWrapper';

/**
 * Mock values
 */
const mockTable = { ...mockTableInstance };
const mocks = {
  fn: jest.fn(),
  params: {
    page: '',
    sortBy: [],
    searchBy: '',
    from: '',
    to: ''
  }
};

/**
 * Mock methods
 */
jest.mock('hooks/useSearchParam', () => ({
  ...jest.requireActual('hooks/useSearchParam'),
  useQueryParams: () => [mocks.params, mocks.fn]
}));

/**
 * Setup function
 */
function setup(m: TableInstance) {
  function MockComponent() {
    useContractsEffect(m);
    return null;
  }
  render(<MockComponent />);
}

/**
 * Test
 */
describe('Contracts - util/useContractsEffects', () => {
  it('expect `useContractsEffects` to call `setQueryParams`', () => {
    mockTable.state.pageIndex = 0;
    mocks.params.page = '5';
    setup(mockTable);
    expect(mocks.fn).toBeCalled();
  });

  it('expect `useContractsEffects` to call `setQueryParams` in different condition', () => {
    mockTable.state.pageIndex = 4;
    mockTable.state.sortBy = [{ id: 'test', desc: true }];
    mocks.params.page = '5';
    setup(mockTable);
    expect(mocks.fn).toBeCalled();
  });
});
