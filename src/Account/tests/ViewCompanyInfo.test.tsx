import { waitFor } from '@testing-library/react';

import { mockErpAccount } from 'Account/tests/mocks';
import ViewCompanyInfo from 'Account/ViewCompanyInfo';
import { render } from 'test-utils/TestWrapper';
import { setBreakpoint } from 'test-utils/mockMediaQuery';

describe('Account - View Company Info', () => {
  it('should match snapshot on desktop', async () => {
    setBreakpoint('desktop');
    const { container } = render(<ViewCompanyInfo company={mockErpAccount} />);
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on mobile', async () => {
    setBreakpoint('mobile');
    const { container } = render(<ViewCompanyInfo company={mockErpAccount} />);
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on desktop with no company', async () => {
    setBreakpoint('desktop');
    const { container } = render(<ViewCompanyInfo company={{}} />);
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on mobile with no company', async () => {
    setBreakpoint('mobile');
    const { container } = render(<ViewCompanyInfo company={{}} />);
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });
});
