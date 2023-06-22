import { act } from '@testing-library/react';

import BranchButton from 'Branches/BranchButton';
import { BranchContext } from 'providers/BranchProvider';
import {
  mockBasicBranch,
  mockBranch,
  mockBranchContext
} from 'Branches/tests/mocks';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

/**
 * Mock values
 */
const mocks = { branch: { ...mockBranchContext } };

/**
 * Test Setup
 */
function setup(m: typeof mocks) {
  return render(
    <BranchContext.Provider value={m.branch}>
      <BranchButton />
    </BranchContext.Provider>
  );
}

/**
 * Test
 */
describe('Branches - BranchButton', () => {
  afterEach(() => {
    mocks.branch = { ...mockBranchContext };
  });

  it('should be disabled while loading', async () => {
    setBreakpoint('desktop');
    mocks.branch.shippingBranchLoading = true;
    const { getByTestId } = setup(mocks);

    await act(() => new Promise((res) => setTimeout(res, 0)));
    const button = getByTestId('change-branch-button');
    expect(button).toHaveAttribute('disabled');
  });

  it('should be matching snapshot on desktop', async () => {
    setBreakpoint('desktop');
    mocks.branch.shippingBranch = mockBasicBranch;
    const { container } = setup(mocks);

    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('should be matching snapshot on mobile', async () => {
    setBreakpoint('mobile');
    mocks.branch.shippingBranch = mockBasicBranch;
    const { container } = setup(mocks);

    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('should be matching snapshot with detailed branch data', async () => {
    setBreakpoint('desktop');
    mocks.branch.shippingBranch = mockBranch;
    const { container } = setup(mocks);

    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('should be matching snapshot when homeBranchError is true', async () => {
    setBreakpoint('desktop');
    mocks.branch.homeBranchError = 'test';
    const { container } = setup(mocks);

    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('should be matching snapshot on with shippingBranchState', async () => {
    setBreakpoint('desktop');
    mocks.branch.shippingBranch = { ...mockBasicBranch, state: 'Wyoming' };
    const { container } = setup(mocks);

    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });
});
