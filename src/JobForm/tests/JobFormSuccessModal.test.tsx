import JobFormSuccessModal from 'JobForm/JobFormSuccessModal';
import { render } from 'test-utils/TestWrapper';
import { testIds } from 'test-utils/testIds';
import { ReactNode } from 'react';
import { AuthContext } from 'AuthProvider';
import { fireEvent } from '@testing-library/react';

const mockHistory = { push: jest.fn() };
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useHistory: () => mockHistory
}));

jest.mock('react-i18next', () => ({
  ...jest.requireActual('react-i18next'),
  useTranslation: () => ({ t: (t: string) => t })
}));

function MockEmptyAuthProvider(props: { children: ReactNode }) {
  return (
    <AuthContext.Provider
      value={{
        authState: null,
        isLoggingOut: false,
        profile: {
          isEmployee: true,
          permissions: [],
          userId: '',
          isVerified: true
        }
      }}
    >
      {props.children}
    </AuthContext.Provider>
  );
}

describe('JobFormSuccessModal', () => {
  it('should render component with necessary data', () => {
    const { queryByTestId } = render(<JobFormSuccessModal />);
    expect(
      queryByTestId(testIds.JobForm.JobFormCard.successModal)
    ).toBeDefined();
    expect(
      queryByTestId(testIds.JobForm.JobFormCard.successModalTitle)
    ).toBeDefined();
    expect(
      queryByTestId(testIds.JobForm.JobFormCard.successModalTitle)
    ).toHaveTextContent('jobForm.jobFormSuccessMessageTitle');
    expect(
      queryByTestId(testIds.JobForm.JobFormCard.successModalEmployeeMessage)
    ).toBe(null);
    expect(
      queryByTestId(testIds.JobForm.JobFormCard.successModalReturnHome)
    ).toBeDefined();
  });

  it('should render modal message if employee', () => {
    const { queryByTestId } = render(
      <MockEmptyAuthProvider>
        <JobFormSuccessModal />
      </MockEmptyAuthProvider>
    );
    expect(
      queryByTestId(testIds.JobForm.JobFormCard.successModalEmployeeMessage)
    ).toBeDefined();
  });

  it('should call history.push when return to home is clicked', () => {
    const { getByTestId } = render(<JobFormSuccessModal />);

    fireEvent.click(
      getByTestId(testIds.JobForm.JobFormCard.successModalReturnHome)
    );

    expect(mockHistory.push).toHaveBeenCalledWith('/');
  });
});
