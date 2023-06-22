import { mockUseSyncQueryParamProps } from 'CustomerApproval/tests/mocks';
import useSyncQueryParam from 'CustomerApproval/util/useSyncQueryParam';
import * as t from 'locales/en/translation.json';
import { render } from 'test-utils/TestWrapper';

/**
 * Mock values
 */
const mocks = {
  props: { ...mockUseSyncQueryParamProps }
};

/**
 * Setup function
 */
function setup(p: typeof mocks) {
  function MockComponent() {
    useSyncQueryParam(p.props);
    return null;
  }
  render(<MockComponent />);
}

/**
 * Test
 */
describe('CustomerApproval - util/useCustomerApprovalTable', () => {
  it('Expect to not call `setQueryParams`', () => {
    mocks.props.branch = 'test';
    mocks.props.page = '1';
    mocks.props.sortBy = ['!test'];
    mocks.props.tableInstance.state.filters = [
      { id: 'branchId', value: 'test' }
    ];
    mocks.props.tableInstance.state.pageIndex = NaN;
    mocks.props.tableInstance.state.sortBy = [{ desc: true, id: 'test' }];
    setup(mocks);
    expect(mocks.props.setQueryParams).not.toBeCalled();
  });
  it('Expect to call `setQueryParams`', () => {
    mocks.props.branch = 'test';
    mocks.props.page = '1';
    mocks.props.sortBy = ['!nomatch'];
    mocks.props.tableInstance.state.filters = [];
    mocks.props.tableInstance.state.pageIndex = 2;
    mocks.props.tableInstance.state.sortBy = [{ id: 'ok' }];
    setup(mocks);
    expect(mocks.props.setQueryParams).toBeCalledWith({
      branch: t.common.all,
      page: '3',
      sortBy: ['ok']
    });
  });
});
