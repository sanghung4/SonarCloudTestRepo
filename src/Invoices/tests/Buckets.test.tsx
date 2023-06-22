import { fireEvent } from '@testing-library/react';

import { AccountInvoice } from 'generated/graphql';
import Buckets from 'Invoices/Buckets';
import { AGES } from 'Invoices/util';
import { kebabCase } from 'lodash-es';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

const accountInvoiceMocks = {
  bucketFuture: 464645.37,
  bucketNinety: 23931.21,
  bucketOneTwenty: 22690.76,
  bucketSixty: -3685.85,
  bucketThirty: 27524.5,
  currentAmt: 1066040.16,
  totalAmt: 1601146.15,
  totalPastDue: 535105.99
} as AccountInvoice;

describe('Invoices List - Buckets', () => {
  // Snapshots
  it('expect buckets to render normally with total on desktop', () => {
    setBreakpoint('desktop');
    const mockSelectFn = jest.fn();
    const { container } = render(
      <Buckets
        accountInvoice={accountInvoiceMocks}
        activeBucket={AGES[0]}
        onBucketSelected={mockSelectFn}
      />
    );
    expect(container).toMatchSnapshot();
  });

  it('expect buckets to render normally with total on mobile', () => {
    setBreakpoint('mobile');
    const mockSelectFn = jest.fn();
    const { container } = render(
      <Buckets
        accountInvoice={accountInvoiceMocks}
        activeBucket={AGES[0]}
        onBucketSelected={mockSelectFn}
      />
    );
    expect(container).toMatchSnapshot();
  });

  it('expect buckets to render with no data', () => {
    setBreakpoint('desktop');
    const mockSelectFn = jest.fn();
    const { container } = render(
      <Buckets activeBucket={AGES[0]} onBucketSelected={mockSelectFn} />
    );
    expect(container).toMatchSnapshot();
  });

  // Button clicks
  it('expect button click for "total-button"', async () => {
    setBreakpoint('desktop');
    const mockSelectFn = jest.fn();
    const { findByTestId } = render(
      <Buckets activeBucket={AGES[0]} onBucketSelected={mockSelectFn} />
    );
    const button = await findByTestId('total-button');
    fireEvent.click(button);

    expect(mockSelectFn).toBeCalledTimes(1);
    expect(mockSelectFn).toBeCalledWith(AGES[0]);
  });

  it('expect button click for "future-button"', () => {
    setBreakpoint('desktop');
    const mockSelectFn = jest.fn();
    const { getByTestId } = render(
      <Buckets activeBucket={AGES[0]} onBucketSelected={mockSelectFn} />
    );
    const button = getByTestId('future-button');
    fireEvent.click(button);

    expect(mockSelectFn).toBeCalledTimes(1);
    expect(mockSelectFn).toBeCalledWith(AGES[1]);
  });

  it('expect button click for "current-button"', () => {
    setBreakpoint('desktop');
    const mockSelectFn = jest.fn();
    const { getByTestId } = render(
      <Buckets activeBucket={AGES[0]} onBucketSelected={mockSelectFn} />
    );
    const button = getByTestId('current-button');
    fireEvent.click(button);

    expect(mockSelectFn).toBeCalledTimes(1);
    expect(mockSelectFn).toBeCalledWith(AGES[2]);
  });

  it('expect button click for "31-60-button"', () => {
    setBreakpoint('desktop');
    const mockSelectFn = jest.fn();
    const { getByTestId } = render(
      <Buckets activeBucket={AGES[0]} onBucketSelected={mockSelectFn} />
    );
    const button = getByTestId(kebabCase(`${'31-60 days'}-button`));
    fireEvent.click(button);

    expect(mockSelectFn).toBeCalledTimes(1);
    expect(mockSelectFn).toBeCalledWith(AGES[3]);
  });

  it('expect button click for "61-90-button"', () => {
    setBreakpoint('desktop');
    const mockSelectFn = jest.fn();
    const { getByTestId } = render(
      <Buckets activeBucket={AGES[0]} onBucketSelected={mockSelectFn} />
    );
    const button = getByTestId(kebabCase(`${'61-90 days'}-button`));
    fireEvent.click(button);

    expect(mockSelectFn).toBeCalledTimes(1);
    expect(mockSelectFn).toBeCalledWith(AGES[4]);
  });

  it('expect button click for "91-120-button"', () => {
    setBreakpoint('desktop');
    const mockSelectFn = jest.fn();
    const { getByTestId } = render(
      <Buckets activeBucket={AGES[0]} onBucketSelected={mockSelectFn} />
    );
    const button = getByTestId(kebabCase(`${'91-120 days'}-button`));
    fireEvent.click(button);

    expect(mockSelectFn).toBeCalledTimes(1);
    expect(mockSelectFn).toBeCalledWith(AGES[5]);
  });

  it('expect button click for "120-button"', () => {
    setBreakpoint('desktop');
    const mockSelectFn = jest.fn();
    const { getByTestId } = render(
      <Buckets activeBucket={AGES[0]} onBucketSelected={mockSelectFn} />
    );
    const button = getByTestId(kebabCase(`${'121 days'}-button`));
    fireEvent.click(button);

    expect(mockSelectFn).toBeCalledTimes(1);
    expect(mockSelectFn).toBeCalledWith(AGES[6]);
  });
});
