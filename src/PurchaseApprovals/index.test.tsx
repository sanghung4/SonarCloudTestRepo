import React from 'react';

import PurchaseApprovals from 'PurchaseApprovals';
import { render } from 'test-utils/TestWrapper';
import { setBreakpoint } from 'test-utils/mockMediaQuery';

describe('Purchased Products', () => {
  it('Renders Purchased Products in desktop', () => {
    setBreakpoint('desktop');
    const { container } = render(<PurchaseApprovals />, {
      authConfig: { authState: { isAuthenticated: true } }
    });

    expect(container).toMatchSnapshot();
  });
  it('Renders Purchased Products in mobile', () => {
    setBreakpoint('mobile');
    const { container } = render(<PurchaseApprovals />, {
      authConfig: { authState: { isAuthenticated: true } }
    });

    expect(container).toMatchSnapshot();
  });
});
