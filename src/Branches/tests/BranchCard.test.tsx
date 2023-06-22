import { act, fireEvent } from '@testing-library/react';
import BranchCard from 'Branches/BranchCard';
import { BranchContext } from 'providers/BranchProvider';
import {
  mockBasicBranch,
  mockBranch,
  mockBranchContext
} from 'Branches/tests/mocks';
import { render } from 'test-utils/TestWrapper';

/**
 * Types
 */
type BranchCardProps = Parameters<typeof BranchCard>[0];

/**
 * Mock values
 */
const mocks = { branch: { ...mockBranchContext } };
let mockUseRouteMatch = false;

/**
 * Mock methods
 */
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useRouteMatch: () => mockUseRouteMatch
}));

/**
 * Test Setup
 */
function setup(m: typeof mocks, props?: BranchCardProps) {
  return render(
    <BranchContext.Provider value={m.branch}>
      <BranchCard {...props} />
    </BranchContext.Provider>
  );
}

/**
 * Test
 */
describe('Branches - BranchCard', () => {
  afterEach(() => {
    mocks.branch = { ...mockBranchContext };
    mockUseRouteMatch = false;
  });

  it('should match snapshot when loading', () => {
    const props: BranchCardProps = {
      loading: true,
      availabilityLoading: false
    };
    const { container } = setup(mocks, props);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot with basic branch data', () => {
    const props: BranchCardProps = {
      branch: mockBasicBranch,
      loading: false,
      availabilityLoading: false
    };
    const { container } = setup(mocks, props);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot with detailed branch data', () => {
    const props: BranchCardProps = {
      branch: mockBranch,
      loading: false,
      availabilityLoading: false
    };
    const { container } = setup(mocks, props);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot with detailed branch data with distance < 100', () => {
    const props: BranchCardProps = {
      branch: { ...mockBranch, distance: 24.5 },
      loading: false,
      availabilityLoading: false
    };
    const { container } = setup(mocks, props);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot with as from location as distance', () => {
    const props: BranchCardProps = {
      branch: mockBranch,
      isLocationDistance: true,
      loading: false,
      availabilityLoading: false
    };
    mocks.branch.homeBranch = { ...mockBranch, branchId: '1234' };
    const { container } = setup(mocks, props);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot with branch that is also the shipping branch', () => {
    const props: BranchCardProps = {
      branch: mockBranch,
      loading: false,
      availabilityLoading: false
    };
    mocks.branch.shippingBranch = { ...mockBranch };
    const { container } = setup(mocks, props);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot as product/list page', () => {
    const props: BranchCardProps = {
      branch: mockBranch,
      loading: false,
      availabilityLoading: false
    };
    mockUseRouteMatch = true;
    const { container } = setup(mocks, props);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot after more details is opened', async () => {
    const props: BranchCardProps = {
      branch: mockBranch,
      loading: false,
      availabilityLoading: false
    };
    const { container, getByTestId } = setup(mocks, props);
    fireEvent.click(getByTestId(`${mockBranch.branchId}-store-details`));

    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('expect onBranchSelected to be called after clicking the Select button', () => {
    const setShippingBranch = jest.fn();
    const props: BranchCardProps = {
      branch: mockBranch,
      setShippingBranch,
      loading: false,
      availabilityLoading: false
    };
    const { getByTestId } = setup(mocks, props);
    fireEvent.click(getByTestId(`${mockBranch.branchId}-select-branch-button`));
    expect(setShippingBranch).toBeCalledWith(mockBranch.branchId);
  });
  it('expect onBranchSelected to be called after clicking the Select button with undefined branch', () => {
    const setShippingBranch = jest.fn();
    const props: BranchCardProps = {
      setShippingBranch,
      loading: false,
      availabilityLoading: false
    };
    const { getByTestId } = setup(mocks, props);
    fireEvent.click(getByTestId('undefined-select-branch-button'));
    expect(setShippingBranch).toBeCalledWith('');
  });
});
