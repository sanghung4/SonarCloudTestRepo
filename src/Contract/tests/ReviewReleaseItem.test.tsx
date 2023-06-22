import { act, fireEvent } from '@testing-library/react';
import { ContractContextType } from 'Contract/ContractProvider';
import ReviewReleaseItem from 'Contract/ReviewReleaseItem';
import { MockContractProvider } from 'Contract/tests/mockComponents';
import {
  ContractsProviderMocks,
  GetContractDetailsMock,
  mockList
} from 'Contract/tests/mocks';
import { ContractDetails } from 'generated/graphql';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

const contractData = GetContractDetailsMock.result.data
  .contract as ContractDetails;
const mockContext: ContractContextType = {
  ...ContractsProviderMocks,
  contractData
};

describe('Contract - Review Release Item', () => {
  it('Should match the snapshot on desktop', () => {
    setBreakpoint('desktop');
    const { container } = render(
      <ReviewReleaseItem data={mockList[0]!} sequence="1" idx={1} />
    );
    expect(container).toMatchSnapshot();
  });

  it('Should match the snapshot on null thumb', () => {
    setBreakpoint('desktop');
    const { container } = render(
      <ReviewReleaseItem data={mockList[4]!} sequence="1" idx={1} />
    );
    expect(container).toMatchSnapshot();
  });

  it('Should match the snapshot on mobile', () => {
    setBreakpoint('mobile');
    const { container } = render(
      <ReviewReleaseItem data={mockList[0]!} sequence="1" idx={1} />
    );
    expect(container).toMatchSnapshot();
  });

  it('Should expect `handleQtyUpdate` to be called', async () => {
    setBreakpoint('desktop');
    const setQtyInputMap = jest.fn();
    const { getByTestId } = render(
      <MockContractProvider
        context={mockContext}
        setQtyInputMap={setQtyInputMap}
      >
        <ReviewReleaseItem data={mockList[0]!} sequence="1" idx={1} />
      </MockContractProvider>
    );
    const input = getByTestId('review-contract-qty-input1');
    fireEvent.focus(input);
    fireEvent.change(input, {
      target: { value: '123' }
    });
    fireEvent.blur(input);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(setQtyInputMap).toBeCalled();
    expect(setQtyInputMap).toBeCalledWith({ '1': '123' });
  });
});
