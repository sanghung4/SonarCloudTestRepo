import BranchFilter from 'CustomerApproval/BranchFilter';
import { mockCoercedUsers } from 'CustomerApproval/tests/mocks';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

/**
 * Mock props
 */
const mockDefaultProps = {
  filterValue: '',
  setFilter: jest.fn(),
  submitFilter: jest.fn(),
  resetFilters: jest.fn()
};

/**
 * Test
 */
describe('CustomerApproval - BranchFilter', () => {
  it('Should match snaptshot with no userList on desktop', () => {
    setBreakpoint('desktop');
    const { container } = render(<BranchFilter {...mockDefaultProps} />);
    expect(container).toMatchSnapshot();
  });

  it('Should match snaptshot with no userList on mobile', () => {
    setBreakpoint('mobile');
    const { container } = render(<BranchFilter {...mockDefaultProps} />);
    expect(container).toMatchSnapshot();
  });

  it('Should match snaptshot with userList', () => {
    setBreakpoint('desktop');
    const { container } = render(
      <BranchFilter {...mockDefaultProps} userList={mockCoercedUsers} />
    );
    expect(container).toMatchSnapshot();
  });
});
