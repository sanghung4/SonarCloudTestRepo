import { act, fireEvent, waitFor } from '@testing-library/react';

import ContractLineItem from 'Contract/ContractLineItem';
import { ContractProvider } from 'Contract/ContractProvider';
import { GetContractDetailsMock } from 'Contract/tests/mocks';
import { ContractProduct } from 'generated/graphql';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

describe('Contract Details - Line Item', () => {
  const itemData = GetContractDetailsMock.result.data.contract
    .contractProducts![0] as ContractProduct;
  const emptyData = {} as ContractProduct;
  const sequenceNumber = 0;

  beforeAll(() => setBreakpoint('desktop'));

  it('Normal LineItem Snapshot match', () => {
    const { container } = render(
      <ContractProvider>
        <ContractLineItem
          sequence={sequenceNumber.toString()}
          data={itemData}
          loading={false}
        />
      </ContractProvider>
    );
    expect(container).toMatchSnapshot();
  });

  it('Loading LineItem Snapshot match', () => {
    const { container } = render(
      <ContractProvider>
        <ContractLineItem
          sequence={sequenceNumber.toString()}
          data={itemData}
          loading
        />
      </ContractProvider>
    );
    expect(container).toMatchSnapshot();
  });

  it('LineItem Snapshot match when data is empty', () => {
    const { container } = render(
      <ContractProvider>
        <ContractLineItem
          sequence={sequenceNumber.toString()}
          data={emptyData}
          loading={false}
        />
      </ContractProvider>
    );
    expect(container).toMatchSnapshot();
  });

  it('Line item details modal open/close', async () => {
    const { queryByTestId } = render(
      <ContractProvider>
        <ContractLineItem
          sequence={sequenceNumber.toString()}
          data={itemData}
          loading={false}
        />
      </ContractProvider>
    );

    const openButton = queryByTestId(`contract-line-item-button0`);

    act(() => {
      fireEvent.click(openButton!);
    });
    const closeButton = queryByTestId(
      `contract-product-modal-close-button-bottom`
    );
    expect(closeButton).toBeInTheDocument();

    act(() => {
      fireEvent.click(closeButton!);
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 200)));
    expect(
      queryByTestId('contract-product-details-container')
    ).not.toBeInTheDocument();
  });
});
