import { act, fireEvent } from '@testing-library/react';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';
import { userForApprover, userInfo } from './mocks';
import PurchaseApproverDialog from 'User/PurchaseApproverDialog';

const mockFn = {
  closeFunc: jest.fn()
};

describe('PurchaseApproverDialog', () => {
  it('Should have close icon when loading is false', () => {
    setBreakpoint('desktop');
    const { getByTestId } = render(
      <PurchaseApproverDialog
        open
        onClose={mockFn.closeFunc}
        user={userInfo}
        loading={false}
      />
    );
    const closeIcon = getByTestId('dialog-close-icon');
    expect(closeIcon).toBeTruthy();
  });
  it('Should not have close button when loading is true', () => {
    setBreakpoint('desktop');
    const { queryByTestId } = render(
      <PurchaseApproverDialog
        open
        onClose={mockFn.closeFunc}
        user={userInfo}
        loading={true}
      />
    );
    const closeIcon = queryByTestId('dialog-close-icon');
    expect(closeIcon).not.toBeInTheDocument();
  });
  it('Should not show dialog when dialog is not in open state', () => {
    setBreakpoint('desktop');
    const { queryByTestId } = render(
      <PurchaseApproverDialog
        open={false}
        onClose={mockFn.closeFunc}
        user={userInfo}
        loading={false}
      />
    );
    const closeIcon = queryByTestId('dialog-close-icon');
    expect(closeIcon).not.toBeInTheDocument();
  });
  it('Should have purchase approver users assigned to the user', async () => {
    setBreakpoint('desktop');
    const { getByTestId } = render(
      <PurchaseApproverDialog
        open
        onClose={mockFn.closeFunc}
        user={userInfo}
        usersForApprover={[userForApprover]}
        loading={false}
      />
    );
    const purchaseApprovalUser = getByTestId('user-for-approver-0');
    expect(purchaseApprovalUser).toBeTruthy();

    await act(() => new Promise((res) => setTimeout(res, 0)));
    const closeButton = getByTestId('dialog-close-icon');

    fireEvent.click(closeButton);

    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mockFn.closeFunc).toBeCalled();
  });
  it('Should have purchase approver users assigned to the user in mobile view', async () => {
    setBreakpoint('mobile');
    const { getByTestId } = render(
      <PurchaseApproverDialog
        open
        onClose={mockFn.closeFunc}
        user={userInfo}
        usersForApprover={[userForApprover]}
        loading={false}
      />
    );
    const purchaseApprovalUser = getByTestId('user-for-approver-0');
    expect(purchaseApprovalUser).toBeTruthy();
  });
});
