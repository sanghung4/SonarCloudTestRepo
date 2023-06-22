import userEvent from '@testing-library/user-event';
import { fireEvent, waitFor, within } from '@testing-library/react';
import { Branch } from 'generated/graphql';
import { render } from 'test-utils/TestWrapper';

import { BranchContext, Divisions } from 'providers/BranchProvider';
import BranchSidebar from 'Branches/BranchSidebar';
import { createMockBranches, mockBranchContext } from 'Branches/tests/mocks';
import * as t from 'locales/en/translation.json';
import { act } from 'react-dom/test-utils';
import { CartContext } from 'providers/CartProvider';
import { mockCartContext } from 'Cart/tests/mocks';

/**
 * Mock values
 */
const mocks = {
  provider: { ...mockBranchContext }
};

/**
 * Setup
 */
function setup(m: typeof mocks) {
  return render(
    <BranchContext.Provider value={m.provider}>
      <BranchSidebar />
    </BranchContext.Provider>
  );
}

/**
 * TEST
 */
describe('Branches - BranchSidebar', () => {
  afterEach(() => {
    mocks.provider = { ...mockBranchContext };
  });

  it('should match snapshot', async () => {
    mocks.provider.branchSelectOpen = true;
    mocks.provider.nearbyBranches = createMockBranches(12, {});
    mocks.provider.shippingBranch = mocks.provider.nearbyBranches[2];
    const { baseElement } = setup(mocks);

    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(baseElement).toMatchSnapshot();
  });

  it('should match snapshot when loading', async () => {
    mocks.provider.homeBranchLoading = true;
    mocks.provider.nearbyBranchesLoading = true;
    mocks.provider.shippingBranchLoading = true;
    mocks.provider.branchSelectOpen = true;
    const { baseElement } = setup(mocks);

    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(baseElement).toMatchSnapshot();
  });

  it('should match snapshot when loading with branches', async () => {
    mocks.provider.homeBranchLoading = true;
    mocks.provider.nearbyBranchesLoading = true;
    mocks.provider.shippingBranchLoading = true;
    mocks.provider.branchSelectOpen = true;
    mocks.provider.nearbyBranches = createMockBranches(12, {});
    const { baseElement } = setup(mocks);

    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(baseElement).toMatchSnapshot();
  });

  it('should match snapshot when nearbyBranchesLoading is false, nearbyBranches is empty, and branchSelectOpen is on', async () => {
    mocks.provider.nearbyBranchesLoading = false;
    mocks.provider.branchSelectOpen = true;
    mocks.provider.nearbyBranches = [];
    const { baseElement } = setup(mocks);

    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(baseElement).toMatchSnapshot();
  });

  it('should fire the setBranchSelectFunction when backdrop is clicked', async () => {
    const { getByTestId } = setup(mocks);

    const backdrop = getByTestId('branch-sidebar-backdrop');
    userEvent.click(backdrop);
    expect(mocks.provider.setBranchSelectOpen).toHaveBeenCalled();
  });

  it('should show the loading state if nearby branches are loading', async () => {
    mocks.provider.nearbyBranchesLoading = true;
    const { queryByText } = setup(mocks);

    const button = queryByText(t.branch.moreBranches);
    expect(button).toBeNull();
  });

  it('should display more branches when a user selects show more', async () => {
    window.HTMLElement.prototype.scrollIntoView = function () {};

    const branches: Branch[] = createMockBranches(12, {});
    mocks.provider.nearbyBranches = branches;
    mocks.provider.branchSelectOpen = true;
    const { getByText } = setup(mocks);

    expect(getByText(branches[4].name!)).toBeInTheDocument();
    userEvent.click(getByText(t.branch.moreBranches));
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(getByText(branches[9].name!)).toBeInTheDocument();
  });

  it('should test the warning message based on cart items', async () => {
    mocks.provider.branchSelectOpen = true;
    mocks.provider.nearbyBranches = createMockBranches(12, {});
    mocks.provider.shippingBranch = mocks.provider.nearbyBranches[2];
    /**
     * Setting the cart items count greater than zero so as to test the warning message
     */
    mockCartContext.itemCount = 3;
    const { findByTestId } = render(
      <CartContext.Provider value={mockCartContext}>
        <BranchContext.Provider value={mocks.provider}>
          <BranchSidebar />
        </BranchContext.Provider>
      </CartContext.Provider>
    );
    const warningMessage = await findByTestId('change-branch-warning-message');
    expect(warningMessage).toBeInTheDocument();
  });

  it('should call handleApplyDivision on change', async () => {
    const handleApplyDivision = jest.fn();
    window.HTMLElement.prototype.scrollIntoView = function () {};
    const branches: Branch[] = createMockBranches(12, {});
    mocks.provider.nearbyBranches = branches;
    mocks.provider.branchSelectOpen = true;
    mocks.provider.setDivision = jest.fn();

    const { getByTestId } = setup(mocks);

    const filterSelect = getByTestId('branch-filter-select');

    fireEvent.mouseDown(filterSelect);
    const newOne = filterSelect.querySelector('input');
    fireEvent.change(newOne!, { target: { value: 'plumbing' } });
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(handleApplyDivision).not.toBeCalled();
  });

  it('Should filter by type Plumbing', async () => {
    const branches: Branch[] = createMockBranches(1, { isPlumbing: true });
    mocks.provider.division = Divisions.PLUMBING;
    mocks.provider.nearbyBranches = branches;
    mocks.provider.branchSelectOpen = true;
    const { getByText } = setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(getByText(branches[0].name!)).toBeInTheDocument();
  });

  it('Should filter by type waterworks', async () => {
    const branches: Branch[] = createMockBranches(2, { isWaterworks: true });
    mocks.provider.division = Divisions.WATERWORKS;
    mocks.provider.nearbyBranches = branches;
    mocks.provider.branchSelectOpen = true;
    const { getByText } = setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(getByText(branches[0].name!)).toBeInTheDocument();
  });

  it('Should filter by type bandk', async () => {
    const branches: Branch[] = createMockBranches(1, { isBandK: true });
    mocks.provider.division = Divisions.BANDK;
    mocks.provider.nearbyBranches = branches;
    mocks.provider.branchSelectOpen = true;
    const { getByText } = setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(getByText(branches[0].name!)).toBeInTheDocument();
  });

  it('Should filter by type hvac', async () => {
    const branches: Branch[] = createMockBranches(2, { isHvac: true });
    mocks.provider.division = Divisions.HVAC;
    mocks.provider.nearbyBranches = branches;
    mocks.provider.branchSelectOpen = true;
    const { getByText } = setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(getByText(branches[0].name!)).toBeInTheDocument();
  });
});
