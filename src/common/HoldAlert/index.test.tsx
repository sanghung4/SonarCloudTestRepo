import HoldAlert from 'common/HoldAlert';
import HoldAlertDialog from 'common/HoldAlert/HoldAlertDialog';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';
import { fireEvent, screen, waitFor } from '@testing-library/react';

describe('common - HoldAlert', () => {
  it('Should match snapshot when account is not on hold', () => {
    const { container } = render(<HoldAlert />, {
      selectedAccountsConfig: {
        selectedAccounts: {
          billToErpAccount: { creditHold: false }
        }
      }
    });
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot when shipTo account is not on hold', () => {
    const { container } = render(<HoldAlert />, {
      selectedAccountsConfig: {
        selectedAccounts: {
          shipToErpAccount: { creditHold: false }
        }
      }
    });
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot on desktop', () => {
    setBreakpoint('desktop');
    const { container } = render(<HoldAlert />, {
      selectedAccountsConfig: {
        selectedAccounts: {
          billToErpAccount: { creditHold: true }
        }
      }
    });
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot on mobile', () => {
    setBreakpoint('mobile');
    const { container } = render(<HoldAlert />, {
      selectedAccountsConfig: {
        selectedAccounts: {
          billToErpAccount: { creditHold: true }
        }
      }
    });
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot on desktop for shipTo account', () => {
    setBreakpoint('desktop');
    const { container } = render(<HoldAlert />, {
      selectedAccountsConfig: {
        selectedAccounts: {
          shipToErpAccount: { creditHold: true }
        }
      }
    });
    expect(container).toMatchSnapshot();
    expect(container).toBeInTheDocument();
  });

  it('Should match snapshot on mobile for shipTo account', () => {
    setBreakpoint('mobile');
    const { container } = render(<HoldAlert />, {
      selectedAccountsConfig: {
        selectedAccounts: {
          shipToErpAccount: { creditHold: true }
        }
      }
    });
    expect(container).toMatchSnapshot();
  });

  it('Should close Alert on clicking the cross-icon', async () => {
    setBreakpoint('desktop');
    const { container } = render(<HoldAlert />, {
      selectedAccountsConfig: {
        selectedAccounts: {
          shipToErpAccount: { creditHold: true }
        }
      }
    });
    expect(container).toBeInTheDocument();

    const closeIcon = screen.getByTestId('close-hold-alert');
    const holdAlert = screen.getByTestId('hold-alert');

    fireEvent.click(closeIcon);

    await waitFor(() => {
      expect(holdAlert).not.toBeVisible();
    });
    expect(container).toBeInTheDocument();
  });
});

describe('common - HoldAlert Dialog', () => {
  it('Should match snapshot on desktop', () => {
    setBreakpoint('desktop');
    const wrapper = render(<HoldAlertDialog open onClose={jest.fn()} />);
    expect(wrapper).toMatchSnapshot();
  });

  it('Should match snapshot on mobile', () => {
    setBreakpoint('mobile');
    const wrapper = render(<HoldAlertDialog open onClose={jest.fn()} />);
    expect(wrapper).toMatchSnapshot();
  });
});
