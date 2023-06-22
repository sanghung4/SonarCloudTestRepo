import { act, fireEvent } from '@testing-library/react';

import CustomerApproval from 'CustomerApproval';
import { dataMocks } from 'CustomerApproval/tests/mocks';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

/**
 * Mock variables
 */
const mocks = {
  history: { push: jest.fn(), go: jest.fn() },
  location: { search: '' }
};

/**
 * Mock methods
 */
jest.mock('react-router-dom', () => ({
  useHistory: () => ({ ...mocks.history }),
  useLocation: () => ({ ...mocks.location })
}));

/**
 * Setup function
 */
function setup() {
  return render(<CustomerApproval />, { mocks: dataMocks });
}

/**
 * Test
 */
describe('CustomerApproval', () => {
  it('Expect to match snapshot on desktop', async () => {
    setBreakpoint('desktop');
    const { container } = setup();
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('Expect to match snapshot on mobile', async () => {
    setBreakpoint('mobile');
    const { container } = setup();
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('Should navigate to the View User page for approval or rejection', async () => {
    setBreakpoint('desktop');
    const { container } = setup();

    await act(() => new Promise((res) => setTimeout(res, 0)));
    const row = container.getElementsByTagName('td')[1];
    fireEvent.click(row);

    await act(() => new Promise((res) => setTimeout(res, 0)));
    const user = dataMocks[0].result.data.allUnapprovedAccountRequests[0];
    expect(mocks.history.push).toHaveBeenCalledWith({
      pathname: `/user/${user.id}`,
      state: expect.any(Object)
    });
  });

  it('Should navigate to the View User page for approval or rejection with broken data', async () => {
    setBreakpoint('desktop');
    const { container } = setup();

    await act(() => new Promise((res) => setTimeout(res, 0)));
    const row = container.getElementsByTagName('tr')[3];
    const cell = row.getElementsByTagName('td')[1];
    fireEvent.click(cell);

    await act(() => new Promise((res) => setTimeout(res, 0)));
    const user = dataMocks[0].result.data.allUnapprovedAccountRequests[2];
    expect(mocks.history.push).toHaveBeenCalledWith({
      pathname: `/user/${user.id}`,
      state: expect.any(Object)
    });
  });
});
