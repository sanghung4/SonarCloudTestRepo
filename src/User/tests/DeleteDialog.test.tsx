import { act, fireEvent } from '@testing-library/react';
import DeleteDialog from 'User/DeleteDialog';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';
import { userInfo, userInfoWithNoName } from './mocks';

const mocks = {
  submitFunc: jest.fn(),
  closeFunc: jest.fn(),
  handleEmployeeLeftChange: jest.fn(),
  pushAlert: jest.fn()
};

jest.mock('@dialexa/reece-component-library', () => ({
  ...jest.requireActual('@dialexa/reece-component-library'),
  useSnackbar: () => ({ pushAlert: mocks.pushAlert })
}));

describe('DeleteDialog', () => {
  it('Should have "delete" button when loading is false', () => {
    setBreakpoint('desktop');
    const { getByTestId } = render(
      <DeleteDialog
        open
        onClose={mocks.closeFunc}
        onSubmit={mocks.submitFunc}
        user={userInfo}
        loading={false}
      />
    );
    const loadingIcon = getByTestId('submit-dialog-button');
    expect(loadingIcon).toHaveTextContent('Delete');
  });
  it('Should not have "delete" button when loading is true', () => {
    setBreakpoint('desktop');
    const { getByTestId } = render(
      <DeleteDialog
        open
        onClose={mocks.closeFunc}
        onSubmit={mocks.submitFunc}
        user={userInfo}
        loading
      />
    );
    const loadingIcon = getByTestId('submit-dialog-button');
    expect(loadingIcon).not.toHaveTextContent('Delete');
  });
  it('Should match snapshot when firstname is empty string', () => {
    setBreakpoint('desktop');
    const { baseElement } = render(
      <DeleteDialog
        open
        onClose={mocks.closeFunc}
        onSubmit={mocks.submitFunc}
        user={userInfoWithNoName}
        loading
      />
    );
    expect(baseElement).toHaveTextContent('Has the employee left the company');
  });
  it('Should check if handleEmployeeLeftChange function called', async () => {
    const { getByTestId, getByRole } = render(
      <DeleteDialog
        open
        onClose={mocks.closeFunc}
        onSubmit={mocks.submitFunc}
        user={userInfo}
        loading={false}
      />
    );

    const radioButton = getByRole('radio', { name: 'Yes' });

    fireEvent.click(radioButton);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(radioButton).toBeChecked();

    const submitBtn = getByTestId('submit-dialog-button');
    fireEvent.click(submitBtn);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    expect(mocks.submitFunc).toHaveBeenCalledWith(true);
  });

  it('Should call close function when close or cancel is clicked', async () => {
    setBreakpoint('desktop');
    const { findByTestId } = render(
      <DeleteDialog
        open
        onClose={mocks.closeFunc}
        onSubmit={mocks.submitFunc}
        user={userInfo}
        loading={false}
      />
    );
    const closeBtn = await findByTestId('delete-dialog-close-btn');
    const cancelBtn = await findByTestId('delete-dialog-cancel-btn');
    expect(closeBtn).toBeInTheDocument();
    expect(cancelBtn).toBeInTheDocument();

    fireEvent.click(closeBtn);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    expect(mocks.closeFunc).toBeCalled();
  });
});
