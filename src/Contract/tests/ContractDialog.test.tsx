import { act, fireEvent } from '@testing-library/react';

import { mockCartContext } from 'Cart/tests/mocks';
import ContractDialogs from 'Contract/ContractDialogs';
import {
  ContractContext,
  ContractContextType,
  DialogEnum
} from 'Contract/ContractProvider';
import { ContractsProviderMocks } from 'Contract/tests/mocks';
import { CartContext, CartContextType } from 'providers/CartProvider';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

const mockProvider: ContractContextType = { ...ContractsProviderMocks };
const mockCart: CartContextType = { ...mockCartContext };

describe('Contract - Dialog', () => {
  it('Matches snapshot as "Release All" in desktop', () => {
    setBreakpoint('desktop');
    mockProvider.dialogType = DialogEnum.Release;
    const wrapper = render(
      <ContractContext.Provider value={mockProvider}>
        <ContractDialogs count={1} />
      </ContractContext.Provider>
    );
    expect(wrapper).toMatchSnapshot();
  });

  it('Matches snapshot as "Release All" in mobile', () => {
    setBreakpoint('mobile');
    mockProvider.dialogType = DialogEnum.Release;
    const wrapper = render(
      <ContractContext.Provider value={mockProvider}>
        <ContractDialogs count={1} />
      </ContractContext.Provider>
    );
    expect(wrapper).toMatchSnapshot();
  });

  it('Matches snapshot as "undefined"', () => {
    setBreakpoint('desktop');
    mockProvider.dialogType = undefined;
    const wrapper = render(
      <ContractContext.Provider value={mockProvider}>
        <ContractDialogs count={1} />
      </ContractContext.Provider>
    );
    expect(wrapper).toMatchSnapshot();
  });

  it('Matches snapshot as "Clear All"', () => {
    setBreakpoint('desktop');
    mockProvider.dialogType = DialogEnum.Clear;
    const wrapper = render(
      <ContractContext.Provider value={mockProvider}>
        <ContractDialogs count={1} />
      </ContractContext.Provider>
    );
    expect(wrapper).toMatchSnapshot();
  });

  it('Confirm button action as "Release All"', async () => {
    mockProvider.dialogType = DialogEnum.Release;
    const wrapper = render(
      <ContractContext.Provider value={mockProvider}>
        <ContractDialogs count={1} />
      </ContractContext.Provider>,
      { cartConfig: mockCart }
    );

    const button = wrapper.getByTestId('alert-confirm-button');
    fireEvent.click(button);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mockProvider.resetDialog).toBeCalledTimes(1);
    expect(mockProvider.goToCart).toBeCalledTimes(1);
    expect(mockCart.releaseContractToCart).toBeCalledTimes(1);
    expect(mockCart.releaseContractToCart).toBeCalledWith(
      mockProvider.contractData
    );
  });

  it('Confirm button action as "Release All"(Fortiline and value over 10 million)', async () => {
    mockProvider.dialogType = DialogEnum.SubtotalToLarge;
    const wrapper = render(
      <ContractContext.Provider value={mockProvider}>
        <ContractDialogs count={1} />
      </ContractContext.Provider>,
      { cartConfig: mockCart }
    );

    const button = wrapper.getByTestId('alert-cancel-button');
    fireEvent.click(button);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mockProvider.resetDialog).toBeCalledTimes(1);
  });

  it('Confirm button action as "Clear All"', async () => {
    mockProvider.dialogType = DialogEnum.Clear;
    const wrapper = render(
      <ContractContext.Provider value={mockProvider}>
        <ContractDialogs count={1} />
      </ContractContext.Provider>
    );

    const button = wrapper.getByTestId('alert-confirm-button');
    fireEvent.click(button);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mockProvider.setQtyInputMap).toBeCalledTimes(1);
  });
});
