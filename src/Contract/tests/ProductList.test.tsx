import { act, fireEvent } from '@testing-library/react';

import {
  ContractContextType,
  ContractProvider
} from 'Contract/ContractProvider';
import {
  ContractsProviderMocks,
  GetContractDetailsMock,
  mockData
} from 'Contract/tests/mocks';
import ContractProductList from 'Contract/ProductList';
import { ContractDetails } from 'generated/graphql';
import { render } from 'test-utils/TestWrapper';
import { MockContractProvider } from './mockComponents';
import { VALUE_OVER_10MIL } from 'Cart/util';

const contractData = GetContractDetailsMock.result.data
  .contract as ContractDetails;
const mockContext: ContractContextType = {
  ...ContractsProviderMocks,
  contractData
};

describe('Contract Details - Product List', () => {
  it('Snapshot match', () => {
    const { container } = render(
      <ContractProvider contractData={contractData}>
        <ContractProductList />
      </ContractProvider>
    );

    expect(container).toMatchSnapshot();
  });

  it('Snapshot match for empty list', async () => {
    const { getByTestId, container } = render(
      <ContractProvider>
        <ContractProductList />
      </ContractProvider>
    );
    const noResult = getByTestId('contract-product-list-empty');

    expect(noResult).toBeInTheDocument();
    expect(container).toMatchSnapshot();
  });

  it('Search input and buttons test', async () => {
    const { getByTestId } = render(
      <ContractProvider contractData={contractData}>
        <ContractProductList />
      </ContractProvider>
    );
    const searchInput = getByTestId('contract-search-input-top');
    const searchButton = getByTestId('contract-search-button-top');
    const clearButton = getByTestId('contract-searchclear-button-top');
    expect(searchInput).toHaveValue('');
    expect(searchButton).toBeDisabled();
    expect(clearButton).toBeDisabled();

    fireEvent.focus(searchInput);
    fireEvent.change(searchInput, {
      target: { value: 'test_123' }
    });
    fireEvent.blur(searchInput);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(searchInput).toHaveValue('test_123');
    expect(searchButton).not.toBeDisabled();
    expect(clearButton).not.toBeDisabled();

    fireEvent.click(clearButton);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(searchInput).toHaveValue('');
    expect(searchButton).toBeDisabled();
    expect(clearButton).toBeDisabled();
  });

  it('Search "a" test', async () => {
    const { getByTestId, findAllByTestId } = render(
      <ContractProvider contractData={contractData}>
        <ContractProductList />
      </ContractProvider>
    );
    const searchInput = getByTestId('contract-search-input-top');
    const searchButton = getByTestId('contract-search-button-top');
    const clearButton = getByTestId('contract-searchclear-button-top');
    const beforeSearch = await findAllByTestId('contract-line-item-container', {
      exact: false
    });
    expect(beforeSearch).toHaveLength(7);

    fireEvent.focus(searchInput);
    fireEvent.change(searchInput, {
      target: { value: 'a' }
    });
    fireEvent.blur(searchInput);
    fireEvent.click(searchButton);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    const afterSearch = await findAllByTestId('contract-line-item-container', {
      exact: false
    });
    expect(afterSearch).toHaveLength(5);

    fireEvent.click(clearButton);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    const postSearch = await findAllByTestId('contract-line-item-container', {
      exact: false
    });
    expect(postSearch).toHaveLength(7);
  });

  it('Search "6" as sequence number test', async () => {
    const { getByTestId, findAllByTestId } = render(
      <ContractProvider contractData={contractData}>
        <ContractProductList />
      </ContractProvider>
    );
    const searchInput = getByTestId('contract-search-input-top');
    const searchButton = getByTestId('contract-search-button-top');
    const clearButton = getByTestId('contract-searchclear-button-top');
    const beforeSearch = await findAllByTestId('contract-line-item-container', {
      exact: false
    });
    expect(beforeSearch).toHaveLength(7);

    fireEvent.focus(searchInput);
    fireEvent.change(searchInput, {
      target: { value: '6' }
    });
    fireEvent.blur(searchInput);
    fireEvent.click(searchButton);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    const afterSearch = await findAllByTestId('contract-line-item-container', {
      exact: false
    });
    expect(afterSearch).toHaveLength(1);

    fireEvent.click(clearButton);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    const postSearch = await findAllByTestId('contract-line-item-container', {
      exact: false
    });
    expect(postSearch).toHaveLength(7);
  });

  it('Search "404" as no result test', async () => {
    const { getByTestId, findAllByTestId } = render(
      <ContractProvider contractData={contractData}>
        <ContractProductList />
      </ContractProvider>
    );
    const searchInput = getByTestId('contract-search-input-top');
    const searchButton = getByTestId('contract-search-button-top');
    const clearButton = getByTestId('contract-searchclear-button-top');
    const beforeSearch = await findAllByTestId('contract-line-item-container', {
      exact: false
    });
    expect(beforeSearch).toHaveLength(7);

    fireEvent.focus(searchInput);
    fireEvent.change(searchInput, {
      target: { value: '404' }
    });
    fireEvent.blur(searchInput);
    fireEvent.click(searchButton);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    const noResult = getByTestId('contract-product-list-empty');
    expect(noResult).toBeInTheDocument();

    fireEvent.click(clearButton);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    const postSearch = await findAllByTestId('contract-line-item-container', {
      exact: false
    });
    expect(postSearch).toHaveLength(7);
  });

  it('Search with excessive amount of spaces test', async () => {
    const { getByTestId, findAllByTestId } = render(
      <ContractProvider contractData={contractData}>
        <ContractProductList />
      </ContractProvider>
    );
    const searchInput = getByTestId('contract-search-input-top');
    const searchButton = getByTestId('contract-search-button-top');
    const clearButton = getByTestId('contract-searchclear-button-top');
    const beforeSearch = await findAllByTestId('contract-line-item-container', {
      exact: false
    });
    expect(beforeSearch).toHaveLength(7);

    fireEvent.focus(searchInput);
    fireEvent.change(searchInput, {
      target: { value: '12" sndwich' }
    });
    fireEvent.blur(searchInput);
    fireEvent.click(searchButton);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    const afterSearch = await findAllByTestId('contract-line-item-container', {
      exact: false
    });
    expect(afterSearch).toHaveLength(1);

    fireEvent.focus(searchInput);
    fireEvent.change(searchInput, {
      target: { value: '      12"     sndwich      ' }
    });
    fireEvent.blur(searchInput);
    fireEvent.click(searchButton);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    const afterAnotherSearch = await findAllByTestId(
      'contract-line-item-container',
      { exact: false }
    );
    expect(afterAnotherSearch).toHaveLength(1);

    fireEvent.click(clearButton);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    const postSearch = await findAllByTestId('contract-line-item-container', {
      exact: false
    });
    expect(postSearch).toHaveLength(7);
  });

  it('Line item qty input and clear qty button test', async () => {
    const { getByTestId, findByTestId } = render(
      <MockContractProvider context={mockContext}>
        <ContractProductList />
      </MockContractProvider>
    );

    const clearQtyButton = getByTestId('contract-clearqtys-button-top');
    expect(clearQtyButton).toBeDisabled();

    const firstQtyInput = await findByTestId('contract-qty-input1');
    fireEvent.focus(firstQtyInput);
    fireEvent.change(firstQtyInput, {
      target: { value: '123' }
    });
    fireEvent.blur(firstQtyInput);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(clearQtyButton).not.toBeDisabled();

    fireEvent.click(clearQtyButton);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mockContext.handleQtyClear).toBeCalled();
  });

  it('Line item qty input and release all button test', async () => {
    const { getByTestId, findByTestId } = render(
      <MockContractProvider context={mockContext}>
        <ContractProductList />
      </MockContractProvider>
    );

    const releaseButton = getByTestId('contract-releasetocart-button-top');
    expect(releaseButton).toBeDisabled();

    const firstQtyInput = await findByTestId('contract-qty-input1');
    fireEvent.focus(firstQtyInput);
    fireEvent.change(firstQtyInput, {
      target: { value: '123' }
    });
    fireEvent.blur(firstQtyInput);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(releaseButton).not.toBeDisabled();

    fireEvent.click(releaseButton);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mockContext.setIsReviewReady).toBeCalled();
    expect(mockContext.setIsReviewReady).toBeCalledWith(true);
  });

  it('Line item qty input and release all button test when it is over 10Mil', async () => {
    const mockContract = { ...mockData };
    mockContract.contractProducts![0]!.netPrice! = VALUE_OVER_10MIL * 2;
    const { getByTestId, findByTestId } = render(
      <MockContractProvider context={mockContext}>
        <ContractProductList />
      </MockContractProvider>
    );

    const releaseButton = getByTestId('contract-releasetocart-button-top');
    expect(releaseButton).toBeDisabled();

    const firstQtyInput = await findByTestId('contract-qty-input1');
    fireEvent.focus(firstQtyInput);
    fireEvent.change(firstQtyInput, {
      target: { value: '2' }
    });
    fireEvent.blur(firstQtyInput);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    fireEvent.click(releaseButton);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mockContext.handleReleaseOver10mil).toBeCalled();
  });
});
