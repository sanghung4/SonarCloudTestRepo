import React from 'react';

import { waitFor } from '@testing-library/react';
import Quotes from 'Quotes';
import { render } from 'test-utils/TestWrapper';
import { setBreakpoint } from 'test-utils/mockMediaQuery';

describe('Quotes test', () => {
  it('should match snapshot on desktop', async () => {
    setBreakpoint('desktop');
    const { container } = render(<Quotes />);

    // Wait for debounce to fire
    await waitFor(() => new Promise((res) => setTimeout(res, 350)));

    expect(container).toMatchSnapshot();
  });
  it('should match snapshot on mobile', async () => {
    setBreakpoint('mobile');
    const { container } = render(<Quotes />);

    // Wait for debounce to fire
    await waitFor(() => new Promise((res) => setTimeout(res, 350)));

    expect(container).toMatchSnapshot();
  });
});
