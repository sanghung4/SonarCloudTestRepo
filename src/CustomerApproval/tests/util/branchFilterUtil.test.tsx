import {
  mockCoercedUsers,
  mockCoercedUsersAlt
} from 'CustomerApproval/tests/mocks';
import {
  autocompleteOnChange,
  useBranchFilterAutocomplete
} from 'CustomerApproval/util/branchFilterUtil';
import { CoercedUser } from 'CustomerApproval/util/types';
import { render } from 'test-utils/TestWrapper';

/**
 * Setup function
 */
function setup(p?: CoercedUser[]) {
  const output: string[] = [];
  function MockComponent() {
    Object.assign(output, useBranchFilterAutocomplete(p));
    return null;
  }
  render(<MockComponent />);
  return output;
}

/**
 * Test
 */
describe('CustomerApproval - util/branchFilterUtil', () => {
  it('expect `useBranchFilterAutocomplete` to return nothing', () => {
    const result = setup();
    expect(result.length).toBe(0);
  });
  it('expect `useBranchFilterAutocomplete` to return 3', () => {
    const result = setup(mockCoercedUsers);
    expect(result.length).toBe(3);
    expect(result[1]).toBe(mockCoercedUsers[0].branchId);
  });
  it('expect `useBranchFilterAutocomplete` to return 4 with alternative mock', () => {
    const result = setup(mockCoercedUsersAlt);
    expect(result.length).toBe(4);
    expect(result[1]).toBe('-');
  });

  it('expect `autocompleteOnChange` to call the mocked function with "-"', () => {
    const mockFn = jest.fn();
    autocompleteOnChange(mockFn)('', '-');
    expect(mockFn).toBeCalledWith('');
  });
  it('expect `autocompleteOnChange` to call the mocked function with "test"', () => {
    const mockFn = jest.fn();
    autocompleteOnChange(mockFn)('', 'test');
    expect(mockFn).toBeCalledWith('test');
  });
});
