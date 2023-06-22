import { SnackbarProvider } from '@dialexa/reece-component-library';
import { act, fireEvent } from '@testing-library/react';

import MigrationSetupMessage from 'Account/MigrationSetupMessage';
import { migrationMocks } from 'Account/tests/mocks';
import * as t from 'locales/en/translation.json';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

const locationMock: { state?: { email: string } } = {
  state: { email: '' }
};
const mockEmail = migrationMocks[0].request.variables.legacyUserEmail;

jest.mock('react-router-dom', () => ({
  useLocation: () => locationMock
}));

describe('Migration Setup Message', () => {
  it('should match snapshot in desktop', () => {
    setBreakpoint('desktop');
    locationMock.state = { email: mockEmail };
    const { container } = render(<MigrationSetupMessage />);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot in desktop when location state is undefined', () => {
    setBreakpoint('desktop');
    locationMock.state = undefined;
    const { container } = render(<MigrationSetupMessage />);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot in mobile', () => {
    setBreakpoint('mobile');
    locationMock.state = { email: mockEmail };
    const { container } = render(<MigrationSetupMessage />);
    expect(container).toMatchSnapshot();
  });

  it('failed to send invite email', async () => {
    setBreakpoint('desktop');
    locationMock.state = { email: mockEmail };
    const { getByTestId, findByText } = render(
      <SnackbarProvider>
        <MigrationSetupMessage />
      </SnackbarProvider>
    );

    act(() => {
      fireEvent.click(getByTestId('resend-email-button'));
    });

    const errorMsg = await findByText(t.register.emailSentErrorNotification);
    expect(errorMsg).toBeInTheDocument();
  });

  it('successfully to send invite email', async () => {
    setBreakpoint('desktop');
    locationMock.state = { email: mockEmail };
    const { getByTestId, findByText } = render(
      <SnackbarProvider>
        <MigrationSetupMessage />
      </SnackbarProvider>,
      { mocks: migrationMocks }
    );

    act(() => {
      fireEvent.click(getByTestId('resend-email-button'));
    });

    const successMsg = await findByText(t.register.emailSentNotification);
    expect(successMsg).toBeInTheDocument();
  });
});
