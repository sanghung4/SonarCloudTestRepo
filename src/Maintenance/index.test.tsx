import { Fragment } from 'react';
import MaintenancePage from 'Maintenance';
import { Configuration } from 'utils/configuration';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

describe('Maintenance Page', () => {
  it('should render TEST when maintenance is set as false', () => {
    setBreakpoint('desktop');
    Configuration.showMaintenancePage = false;
    const { container } = render(<MaintenancePage children="TEST" />);
    expect(container).toMatchSnapshot();
  });

  it('should render correctly on desktop', () => {
    setBreakpoint('desktop');
    Configuration.showMaintenancePage = true;
    const { container } = render(<MaintenancePage children={Fragment} />);
    expect(container).toMatchSnapshot();
  });

  it('should render correctly on mobile', () => {
    setBreakpoint('mobile');
    Configuration.showMaintenancePage = true;
    const { container } = render(<MaintenancePage children={Fragment} />);
    expect(container).toMatchSnapshot();
  });
});
