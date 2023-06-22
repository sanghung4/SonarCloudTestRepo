import { waitFor } from '@testing-library/react';
import { BranchContext } from 'providers/BranchProvider';

import SupportInfo from 'Support/SupportInfo';
import { branchContextMock, HomeBranchCardMocks } from 'Support/tests/mocks';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

const shipToId = HomeBranchCardMocks[0].request.variables.shipToAccountId;

describe('Support - Support Info', () => {
  it('should match the snapshot on desktop', async () => {
    setBreakpoint('desktop');
    const { container } = render(<SupportInfo />, {
      mocks: [HomeBranchCardMocks[0]],
      selectedAccountsConfig: { selectedAccounts: { shipTo: { id: shipToId } } }
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(container).toMatchSnapshot();
  });

  it('should match the snapshot on moble', async () => {
    setBreakpoint('desktop');
    const { container } = render(<SupportInfo />, {
      mocks: [HomeBranchCardMocks[0]],
      selectedAccountsConfig: { selectedAccounts: { shipTo: { id: shipToId } } }
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(container).toMatchSnapshot();
  });

  it('should match the snapshot on desktop no Home Branch', async () => {
    setBreakpoint('desktop');
    const { container } = render(<SupportInfo />);
    expect(container).toMatchSnapshot();
  });

  it('should match the snapshot on moble no Home Branch', async () => {
    setBreakpoint('desktop');
    const { container } = render(<SupportInfo />);
    expect(container).toMatchSnapshot();
  });

  it('should match the snapshot on desktop empty data', async () => {
    setBreakpoint('desktop');
    const { container } = render(<SupportInfo />, {
      mocks: [HomeBranchCardMocks[1]],
      selectedAccountsConfig: { selectedAccounts: { shipTo: { id: shipToId } } }
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(container).toMatchSnapshot();
  });

  it('should match the snapshot on moble empty data', async () => {
    setBreakpoint('desktop');

    const { container } = render(<SupportInfo />, {
      mocks: [HomeBranchCardMocks[1]],
      selectedAccountsConfig: { selectedAccounts: { shipTo: { id: shipToId } } }
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot with hours on desktop', async () => {
    setBreakpoint('desktop');
    const { container } = render(
      <BranchContext.Provider value={branchContextMock}>
        <SupportInfo />
      </BranchContext.Provider>,
      {
        mocks: [HomeBranchCardMocks[0]],
        selectedAccountsConfig: {
          selectedAccounts: { shipTo: { id: shipToId } }
        }
      }
    );
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(container).toMatchSnapshot();
  });
});
