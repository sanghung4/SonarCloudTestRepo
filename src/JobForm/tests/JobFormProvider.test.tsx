import JobFormProvider from 'JobForm/JobFormProvider';
import { JobFormContextDisplay } from 'JobForm/utils/JobFormProvider.mocks';
import { dummyUser, dummyUserAccounts } from 'test-utils/dummyData';
import {
  mockUserAccountsQuerySuccess,
  mockUserQuerySuccess
} from 'test-utils/mockResponses';
import { render } from 'test-utils/TestWrapper';

/******************************/
/* Mocks                      */
/******************************/
const mockHooks = {
  useDomainInfo: jest.fn()
};

/******************************/
/* Custom Hook Mocks          */
/******************************/
jest.mock('hooks/useDomainInfo', () => ({
  useDomainInfo: () => mockHooks.useDomainInfo()
}));

/******************************/
/* Setup Functions            */
/******************************/
const renderProviderWithDisplay = () => {
  // mock return values
  mockHooks.useDomainInfo.mockReturnValue({
    brand: 'brand',
    companyNameList: '',
    isWaterworks: false
  });

  // return render
  return render(
    <JobFormProvider>
      <JobFormContextDisplay />
    </JobFormProvider>,
    {
      mocks: [mockUserAccountsQuerySuccess, mockUserQuerySuccess],
      authConfig: {
        authState: { isAuthenticated: true },
        profile: {
          userId: 'testuser',
          permissions: [],
          isEmployee: false,
          isVerified: true
        }
      },
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { name: 'selected billTo', erpAccountId: '12345' },
          shipTo: { name: 'selected shipTo', erpAccountId: '12345' }
        },
        isMincron: false
      }
    }
  );
};

describe('JobForm - JobFormProvider', () => {
  it('Should render displayed values', async () => {
    const { findByTestId } = renderProviderWithDisplay();

    const display = await findByTestId('job-form-provider-display');

    expect(display).toBeInTheDocument();
  });

  it('Should render correct values', async () => {
    const { findByTestId } = renderProviderWithDisplay();

    const loading = await findByTestId('job-form-provider-loading');

    const stepOneCustomerNumber = await findByTestId(
      'job-form-provider-defaultValues-stepOne-customerNumber'
    );
    const stepOneCustomerName = await findByTestId(
      'job-form-provider-defaultValues-stepOne-customerName'
    );
    const stepOneCustomerEmail = await findByTestId(
      'job-form-provider-defaultValues-stepOne-customerEmail'
    );
    const stepOneCustomerPhoneNumber = await findByTestId(
      'job-form-provider-defaultValues-stepOne-customerPhoneNumber'
    );
    const userAccountOptionZero = await findByTestId(
      'job-form-provider-userAccountOption-0'
    );
    const userAccountOptionOne = await findByTestId(
      'job-form-provider-userAccountOption-1'
    );

    expect(loading).toHaveTextContent('false');
    expect(stepOneCustomerNumber).toHaveTextContent(dummyUser.id!);
    expect(stepOneCustomerName).toHaveTextContent('selected billTo');
    expect(stepOneCustomerEmail).toHaveTextContent(dummyUser.email!);
    expect(stepOneCustomerPhoneNumber).toHaveTextContent(
      dummyUser.phoneNumber!
    );
    expect(userAccountOptionZero).toHaveTextContent(dummyUserAccounts[0].name!);
    expect(userAccountOptionOne).toHaveTextContent(dummyUserAccounts[1].name!);
  });
});
