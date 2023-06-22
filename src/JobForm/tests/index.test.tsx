import JobForm from 'JobForm';
import {
  mockUserAccountsQuerySuccess,
  mockUserQuerySuccess,
  mockEntitySearchQuerySuccess,
  mockEntitySearchQueryError,
  mockCreateJobFormMutationSuccess
} from 'test-utils/mockResponses';
import { render } from 'test-utils/TestWrapper';
import { testIds } from 'test-utils/testIds';
import {
  clickButton,
  clickRadio,
  fillFormSelectInput,
  fillFormTextInput,
  fillMaskedFormTextInput
} from 'test-utils/actionUtils';
import { MockedResponse } from '@apollo/client/testing';

/******************************/
/* Mocks                      */
/******************************/
const mockHooks = {
  useDomainInfo: jest.fn()
};

global.scrollTo = jest.fn();

/******************************/
/* Custom Hook Mocks          */
/******************************/
jest.mock('hooks/useDomainInfo', () => ({
  useDomainInfo: () => mockHooks.useDomainInfo()
}));

jest.mock('@dialexa/reece-component-library', () => ({
  ...jest.requireActual('@dialexa/reece-component-library'),
  useSnackbar: () => ({ pushAlert: jest.fn() })
}));

/******************************/
/* Setup Functions            */
/******************************/
const renderWithJobFormLink = () => {
  // Mock any return values needed
  mockHooks.useDomainInfo.mockReturnValue({
    brand: 'brand',
    companyNameList: '',
    isWaterworks: true
  });

  // return rendered page
  return render(<JobForm />, {
    mocks: [mockUserAccountsQuerySuccess, mockUserQuerySuccess],
    authConfig: {
      authState: { isAuthenticated: true },
      profile: {
        userId: 'testuser',
        permissions: [],
        isEmployee: true,
        isVerified: true
      }
    }
  });
};

const renderWithJobFormCard = (
  isCustomer?: boolean,
  mocks?: MockedResponse[]
) => {
  // Mock any return values needed
  mockHooks.useDomainInfo.mockReturnValue({
    brand: 'brand',
    companyNameList: '',
    isWaterworks: false
  });

  // return rendered page
  return render(<JobForm />, {
    mocks: mocks ?? [
      mockUserAccountsQuerySuccess,
      mockUserQuerySuccess,
      mockEntitySearchQuerySuccess,
      mockCreateJobFormMutationSuccess
    ],
    authConfig: {
      authState: { isAuthenticated: true },
      profile: {
        userId: 'testuser',
        permissions: [],
        isEmployee: !isCustomer,
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
  });
};

const fillStepOne = async () => {
  await fillFormTextInput(testIds.JobForm.JobFormCard.projectJobName, 'value');
  await fillFormTextInput(
    testIds.JobForm.JobFormCard.projectAddressOne,
    'address'
  );
  await fillFormTextInput(testIds.JobForm.JobFormCard.projectCity, 'value');
  await fillFormSelectInput(testIds.JobForm.JobFormCard.projectState, 'AL');
  await fillMaskedFormTextInput(
    testIds.JobForm.JobFormCard.projectZip,
    '12345'
  );
  await fillMaskedFormTextInput(
    testIds.JobForm.JobFormCard.projectEstimate,
    '123'
  );
  await clickRadio(testIds.JobForm.JobFormCard.projectTaxExempt, 1);
};

const fillStepTwo = async () => {
  await fillFormTextInput(testIds.JobForm.JobFormCard.contractorName, 'value');
  await fillFormTextInput(
    testIds.JobForm.JobFormCard.contractorAddressOne,
    'address'
  );
  await fillFormTextInput(testIds.JobForm.JobFormCard.contractorCity, 'value');
  await fillFormSelectInput(testIds.JobForm.JobFormCard.contractorState, 'AL');
  await fillMaskedFormTextInput(
    testIds.JobForm.JobFormCard.contractorZip,
    '12345'
  );
  await fillMaskedFormTextInput(
    testIds.JobForm.JobFormCard.contractorPhone,
    '1234567890'
  );
};

const fillStepThree = async () => {
  await fillFormTextInput(testIds.JobForm.JobFormCard.ownerName, 'value');
  await fillFormTextInput(
    testIds.JobForm.JobFormCard.ownerAddressOne,
    'address'
  );
  await fillFormTextInput(testIds.JobForm.JobFormCard.ownerCity, 'value');
  await fillFormSelectInput(testIds.JobForm.JobFormCard.ownerState, 'AL');
  await fillMaskedFormTextInput(testIds.JobForm.JobFormCard.ownerZip, '12345');
};

/******************************/
/* Tests Suits                */
/******************************/
describe('JobForm', () => {
  it('Should render page', async () => {
    // Render the page
    const { findByTestId } = renderWithJobFormLink();

    // Await finding the item by test id to get past loading
    const jobForm = await findByTestId(testIds.JobForm.page);

    // Expect page to be rendered
    expect(jobForm).toBeInTheDocument();
  });

  it('Should render JobFormLink', async () => {
    // Render the page
    const { findByTestId } = renderWithJobFormLink();

    // Await finding the item by test id to get past loading
    const jobFormLink = await findByTestId(testIds.JobForm.JobFormLink.page);

    // Expect page to be rendered
    expect(jobFormLink).toBeInTheDocument();
  });

  it('Should render JobFormCard', async () => {
    const { findByTestId } = renderWithJobFormCard();

    // Await finding the item by test id to get past loading
    const jobFormCard = await findByTestId(testIds.JobForm.JobFormCard.page);

    // Expect page to be rendered
    expect(jobFormCard).toBeInTheDocument();
  });
});

describe('JobForm - JobFormCard - Step 1', () => {
  it('Should render Step 1', async () => {
    const { findByTestId } = renderWithJobFormCard();

    // Await finding the item by test id to get past loading
    const jobFormCardStep = await findByTestId(
      testIds.JobForm.JobFormCard.stepOne
    );

    // Expect page to be rendered
    expect(jobFormCardStep).toBeInTheDocument();
  });

  it('(Employee) Should render employee inputs', async () => {
    const { findByTestId } = renderWithJobFormCard();

    const customerNumberInput = await findByTestId(
      testIds.JobForm.JobFormCard.customerNumberEmployee
    );
    const customerNameReset = await findByTestId(
      testIds.JobForm.JobFormCard.customerNameReset
    );

    const customerNameLookup = await findByTestId(
      testIds.JobForm.JobFormCard.customerNameLookup
    );

    expect(customerNumberInput).toBeInTheDocument();
    expect(customerNameReset).toBeInTheDocument();
    expect(customerNameLookup).toBeInTheDocument();
  });

  it('(Customer) Should render customer inputs', async () => {
    const { findByTestId } = renderWithJobFormCard(true);

    const customerNumberInput = await findByTestId(
      testIds.JobForm.JobFormCard.customerNumberCustomer
    );

    expect(customerNumberInput).toBeInTheDocument();
  });

  it('Should not allow progress if fields are empty', async () => {
    const { findByTestId } = renderWithJobFormCard(true);

    await clickButton(testIds.JobForm.JobFormCard.stepOneContinue);

    const jobFormCardStep = await findByTestId(
      testIds.JobForm.JobFormCard.stepOne
    );

    expect(jobFormCardStep).toBeInTheDocument();
  });

  it('Should allow customer lookup - error', async () => {
    const { findByTestId } = renderWithJobFormCard(false, [
      mockUserAccountsQuerySuccess,
      mockUserQuerySuccess,
      mockEntitySearchQueryError
    ]);

    await fillFormTextInput(
      testIds.JobForm.JobFormCard.customerNumberEmployee,
      '12345'
    );

    await clickButton(testIds.JobForm.JobFormCard.customerNameLookup);

    const customerNumberErrorText = await findByTestId(
      `${testIds.JobForm.JobFormCard.customerNumberEmployee}-error-text`
    );

    expect(customerNumberErrorText).toBeInTheDocument();
  });

  it('Should render project file input when tax exempt', async () => {
    const { findByTestId } = renderWithJobFormCard();

    await clickRadio(testIds.JobForm.JobFormCard.projectTaxExempt, 0);

    const fileInput = await findByTestId(
      testIds.JobForm.JobFormCard.projectTaxFile
    );

    expect(fileInput).toBeInTheDocument();
    expect(fileInput).not.toBeRequired();
  });

  it('Should allow progress when required fields are filled', async () => {
    const { findByTestId } = renderWithJobFormCard(true);

    await fillStepOne();
    await clickButton(testIds.JobForm.JobFormCard.stepOneContinue);

    const jobFormCardStep = await findByTestId(
      testIds.JobForm.JobFormCard.stepTwo
    );
    expect(jobFormCardStep).toBeInTheDocument();
  });
});

describe('JobForm - JobFormCard - Step 2', () => {
  it('Should not allow progress if fields are empty', async () => {
    const { findByTestId } = renderWithJobFormCard(true);
    // Fill step one & continue
    await fillStepOne();
    await clickButton(testIds.JobForm.JobFormCard.stepOneContinue);
    // Click continue
    await clickButton(testIds.JobForm.JobFormCard.stepTwoContinue);

    const jobFormCardStep = await findByTestId(
      testIds.JobForm.JobFormCard.stepTwo
    );

    expect(jobFormCardStep).toBeInTheDocument();
  });

  it('Should allow the user to go back', async () => {
    const { findByTestId } = renderWithJobFormCard(true);
    // Fill step one & continue
    await fillStepOne();
    await clickButton(testIds.JobForm.JobFormCard.stepOneContinue);
    // Fill step two & go back
    await fillStepTwo();
    await clickButton(testIds.JobForm.JobFormCard.stepTwoBack);

    const jobFormCardStep = await findByTestId(
      testIds.JobForm.JobFormCard.stepOne
    );
    expect(jobFormCardStep).toBeInTheDocument();
  });

  it('Should allow progress if required fields are filled', async () => {
    const { findByTestId } = renderWithJobFormCard(true);
    // Fill step one & continue
    await fillStepOne();
    await clickButton(testIds.JobForm.JobFormCard.stepOneContinue);
    // Fill step two & continue
    await fillStepTwo();
    await clickButton(testIds.JobForm.JobFormCard.stepTwoContinue);

    const jobFormCardStep = await findByTestId(
      testIds.JobForm.JobFormCard.stepThree
    );
    expect(jobFormCardStep).toBeInTheDocument();
  });
});

describe('JobForm - JobFormCard - Step 3', () => {
  it('Should not allow submission if fields are empty', async () => {
    const { findByTestId } = renderWithJobFormCard(true);
    // Fill step one & continue
    await fillStepOne();
    await clickButton(testIds.JobForm.JobFormCard.stepOneContinue);
    // Fill step two & continue
    await fillStepTwo();
    await clickButton(testIds.JobForm.JobFormCard.stepTwoContinue);
    // Click submit
    await clickButton(testIds.JobForm.JobFormCard.stepThreeContinue);

    const jobFormCardStep = await findByTestId(
      testIds.JobForm.JobFormCard.stepThree
    );

    expect(jobFormCardStep).toBeInTheDocument();
  });

  it('Should allow the user to go back', async () => {
    const { findByTestId } = renderWithJobFormCard(true);
    // Fill step one & continue
    await fillStepOne();
    await clickButton(testIds.JobForm.JobFormCard.stepOneContinue);
    // Fill step two & continue
    await fillStepTwo();
    await clickButton(testIds.JobForm.JobFormCard.stepTwoContinue);
    // Click back
    await clickButton(testIds.JobForm.JobFormCard.stepThreeBack);

    const jobFormCardStep = await findByTestId(
      testIds.JobForm.JobFormCard.stepTwo
    );

    expect(jobFormCardStep).toBeInTheDocument();
  });

  it('Should allow submission if fields are filled', async () => {
    const { findByTestId } = renderWithJobFormCard(true);
    // Fill step one & continue
    await fillStepOne();
    await clickButton(testIds.JobForm.JobFormCard.stepOneContinue);
    // Fill step two & continue
    await fillStepTwo();
    await clickButton(testIds.JobForm.JobFormCard.stepTwoContinue);
    // Click submit
    await fillStepThree();
    await clickButton(testIds.JobForm.JobFormCard.stepThreeContinue);

    const jobFormSuccessModal = await findByTestId(
      testIds.JobForm.JobFormCard.successModal
    );

    expect(jobFormSuccessModal).toBeInTheDocument();
  });
});
