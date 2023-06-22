import React from 'react';

import { waitFor } from '@testing-library/react';

import Quote from 'Quote';
import DeliverySummaryCard from 'Quote/DeliverySummaryCard';
import { mockQuote } from 'Quote/testmock';
import InfoCard from 'Quote/InfoCard';
import ProductsCard from 'Quote/ProductsCard';
import { render } from 'test-utils/TestWrapper';
import { setBreakpoint } from 'test-utils/mockMediaQuery';

describe('Quote test', () => {
  it('should match snapshot on desktop', async () => {
    setBreakpoint('desktop');
    const { container } = render(<Quote />);

    // Wait for debounce to fire
    await waitFor(() => new Promise((res) => setTimeout(res, 350)));

    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on mobile', async () => {
    setBreakpoint('mobile');
    const { container } = render(<Quote />);

    // Wait for debounce to fire
    await waitFor(() => new Promise((res) => setTimeout(res, 350)));

    expect(container).toMatchSnapshot();
  });

  it('Delivery summary render', () => {
    const { container } = render(
      <DeliverySummaryCard loading={false} quote={mockQuote} />
    );

    expect(container).toMatchSnapshot();
  });

  it('Info render', () => {
    const { container } = render(
      <InfoCard loading={false} quote={mockQuote} />
    );

    expect(container).toMatchSnapshot();
  });

  it('Products render', () => {
    const { container } = render(
      <ProductsCard loading={false} quote={mockQuote} />
    );

    expect(container).toMatchSnapshot();
  });
});
