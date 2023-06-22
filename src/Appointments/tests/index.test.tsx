import Appointments from 'Appointments';
import { render } from 'test-utils/TestWrapper';
import { setBreakpoint } from 'test-utils/mockMediaQuery';

describe('Appointments', () => {
  it('Renders Appointments in desktop', () => {
    setBreakpoint('desktop');
    const { container } = render(<Appointments />, {
      authConfig: { authState: { isAuthenticated: true } }
    });

    expect(container).toMatchSnapshot();
  });

  it('Renders Appointments in mobile', () => {
    setBreakpoint('mobile');
    const { container } = render(<Appointments />, {
      authConfig: { authState: { isAuthenticated: true } }
    });

    expect(container).toMatchSnapshot();
  });
});
