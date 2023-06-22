import React from 'react';

import { fireEvent } from '@testing-library/react';

import { mockQuote, failureMockQuote } from 'Quote/testmock';
import SummaryCard from 'Quote/SummaryCard';
import { render } from 'test-utils/TestWrapper';
import { mockCartContext } from 'Cart/tests/mocks';

const mockContext = { ...mockCartContext };
let mockHistory = { push: jest.fn() };

jest.mock('react-router-dom', () => ({
  useHistory: () => mockHistory
}));

describe('Summary Cards Tests', () => {
  afterEach(() => {
    mockHistory = { push: jest.fn() };
  });

  it('should not have subTotal value when loading is true', async () => {
    const { findByTestId } = render(
      <SummaryCard loading quote={mockQuote} onReject={() => {}} />
    );
    const subTotalValue = await findByTestId('quote-sub-total');
    expect(subTotalValue).not.toHaveTextContent('$25.99');
  });

  it('should test the approve quote button', async () => {
    const { findByTestId } = render(
      <SummaryCard loading={false} quote={mockQuote} onReject={() => {}} />,
      { cartConfig: mockContext }
    );
    const approveButton = await findByTestId('approve-quote-button');
    expect(approveButton).toBeInTheDocument();
    fireEvent.click(approveButton);
    expect(mockContext.setQuoteId).toHaveBeenCalledWith('S100000075');
    expect(mockContext.setQuoteShipToId).toHaveBeenCalledWith('11336');
    expect(mockHistory.push).toBeCalledWith('/checkout');
  });

  it('should test the approve button without quote prop', async () => {
    const { findByTestId } = render(
      <SummaryCard loading={false} onReject={() => {}} />,
      { cartConfig: mockContext }
    );
    const approveButton = await findByTestId('approve-quote-button');
    fireEvent.click(approveButton);
    expect(approveButton).toBeInTheDocument();
  });

  it('should test the approve quote button without mandatory props', async () => {
    const { findByTestId } = render(
      <SummaryCard
        loading={false}
        quote={failureMockQuote}
        onReject={() => {}}
      />,
      { cartConfig: mockContext }
    );
    const approveButton = await findByTestId('approve-quote-button');
    expect(approveButton).toBeInTheDocument();
    fireEvent.click(approveButton);
    expect(mockContext.setQuoteShipToId).toHaveBeenCalledWith('');
    expect(mockHistory.push).toBeCalledWith('/checkout');
  });

  it('should test the decline quote button', async () => {
    const onReject = jest.fn();
    const { findByTestId } = render(
      <SummaryCard loading={false} quote={mockQuote} onReject={onReject} />
    );
    const declineQuoteButton = await findByTestId('decline-quote-button');
    expect(declineQuoteButton).toBeInTheDocument();
    fireEvent.click(declineQuoteButton);
    expect(onReject).toHaveBeenCalled();
  });
});
