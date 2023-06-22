import { act, fireEvent } from '@testing-library/react';
import DeliveryMethod from 'Cart/DeliveryMethod';

import { createMockBranches, mockBranchContext } from 'Branches/tests/mocks';
import { BranchContext, BranchContextType } from 'providers/BranchProvider';
import { mockCartContext } from 'Cart/tests/mocks';
import { DeliveryMethodEnum } from 'generated/graphql';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

const mockCartContextVal = { ...mockCartContext };
const mockBranchContextVal: BranchContextType = { ...mockBranchContext };

describe('Cart - DeliveryMethod', () => {
  it('Should test branch info under willcall (no contract)', async () => {
    mockBranchContextVal.shippingBranchLoading = false;
    mockBranchContextVal.shippingBranch = {
      ...createMockBranches(1)[0],
      address1: '107 Test St'
    };
    const { findByTestId } = render(
      <DeliveryMethod
        isDisabled={false}
        deliveryMethod={DeliveryMethodEnum.Willcall}
      />,
      { cartConfig: mockCartContextVal }
    );
    const branchInfo = await findByTestId('cart-branch-info');
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(branchInfo).toBeInTheDocument();
  });

  it('Should test the radio buttons under willcall (no contract)', async () => {
    mockBranchContextVal.shippingBranchLoading = false;
    mockBranchContextVal.shippingBranch = {
      ...createMockBranches(1)[0],
      address1: '107 Test St'
    };
    const { findByTestId } = render(
      <DeliveryMethod
        isDisabled={false}
        deliveryMethod={DeliveryMethodEnum.Willcall}
      />,
      { cartConfig: mockCartContextVal }
    );
    const willCall = await findByTestId('will-call-radio-button');
    const delivery = await findByTestId('delivery-radio-button');
    expect(willCall).toBeChecked();
    fireEvent.click(delivery);
    expect(willCall).not.toBeChecked();
    expect(delivery).toBeChecked();
  });

  it('Should expect `setBranchSelectOpen` to be called', async () => {
    setBreakpoint('desktop');
    mockBranchContextVal.shippingBranchLoading = false;
    mockBranchContextVal.shippingBranch = {
      ...createMockBranches(1)[0],
      address1: '107 Test St'
    };
    const { getByTestId } = render(
      <BranchContext.Provider value={mockBranchContextVal}>
        <DeliveryMethod
          isDisabled={false}
          deliveryMethod={DeliveryMethodEnum.Willcall}
        />
      </BranchContext.Provider>,
      { cartConfig: mockCartContextVal }
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    fireEvent.click(getByTestId('change-branch-button'));
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mockBranchContextVal.setBranchSelectOpen).toBeCalled();
  });
});
