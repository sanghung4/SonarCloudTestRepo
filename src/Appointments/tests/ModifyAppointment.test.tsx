import ModifyAppointment from 'Appointments/ModifyAppointment';
import { render } from 'test-utils/TestWrapper';
import { setBreakpoint } from 'test-utils/mockMediaQuery';

describe('Modify Appointment', () => {
  it('Renders Modify Appointment in desktop', () => {
    setBreakpoint('desktop');
    const { container } = render(<ModifyAppointment />, {
      authConfig: { authState: { isAuthenticated: true } }
    });

    expect(container).toMatchSnapshot();
  });

  it('Renders Modify Appointment in mobile', () => {
    setBreakpoint('mobile');
    const { container } = render(<ModifyAppointment />, {
      authConfig: { authState: { isAuthenticated: true } }
    });

    expect(container).toMatchSnapshot();
  });
});
