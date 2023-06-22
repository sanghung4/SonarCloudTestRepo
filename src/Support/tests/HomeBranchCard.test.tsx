import HomeBranchCard from 'Support/HomeBranchCard';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';
import { HomeBranchCardMocks } from 'Support/tests/mocks';
import { waitFor } from '@testing-library/react';

const shipToId = HomeBranchCardMocks[0].request.variables.shipToAccountId;

describe('Support - Home Branch Card', () => {
  it('should match snapshot on desktop', async () => {
    setBreakpoint('desktop');
    const { container } = render(<HomeBranchCard />, {
      mocks: [HomeBranchCardMocks[0]],
      selectedAccountsConfig: { selectedAccounts: { shipTo: { id: shipToId } } }
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on mobile', async () => {
    setBreakpoint('mobile');
    const { container } = render(<HomeBranchCard />, {
      mocks: [HomeBranchCardMocks[0]],
      selectedAccountsConfig: { selectedAccounts: { shipTo: { id: shipToId } } }
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on desktop with no Home Branch', () => {
    setBreakpoint('desktop');
    const { container } = render(<HomeBranchCard />);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on mobile with no Home Branch', () => {
    setBreakpoint('mobile');
    const { container } = render(<HomeBranchCard />);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on desktop with empty data', async () => {
    setBreakpoint('desktop');
    const { container } = render(<HomeBranchCard />, {
      mocks: [HomeBranchCardMocks[1]],
      selectedAccountsConfig: { selectedAccounts: { shipTo: { id: shipToId } } }
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on mobile with empty data', async () => {
    setBreakpoint('mobile');
    const { container } = render(<HomeBranchCard />, {
      mocks: [HomeBranchCardMocks[1]],
      selectedAccountsConfig: { selectedAccounts: { shipTo: { id: shipToId } } }
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(container).toMatchSnapshot();
  });
});
