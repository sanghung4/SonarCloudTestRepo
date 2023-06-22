import { act, fireEvent, within } from '@testing-library/react';

import { mockCart } from 'Cart/tests/mocks';
import SelectAccountsForm from 'SelectAccounts/SelectAccountsForm';
import { clickButton, fillFormTextInput } from 'test-utils/actionUtils';
import { testIds as allTestIds } from 'test-utils/testIds';
import { render } from 'test-utils/TestWrapper';
import { EcommAccount } from 'generated/graphql';
import { CartContextType } from 'Cart/util/CartProvider/CartContextTypes';
import { useSnackbar } from '@dialexa/reece-component-library';
import { useLocalStorage } from 'hooks/useLocalStorage';
import {
  REFRESH_SHIP_TO_ACCOUNT_SUCCESS,
  USER_ACCOUNTS_SUCCESS
} from 'test-utils/mockResponses';
import {
  dummyRefreshShipToAccount,
  dummyEcommAccounts
} from 'test-utils/dummyData';

/******************************/
/* Types                      */
/******************************/
interface SetupRenderProps {
  cart?: CartContextType['cart'];
  getUserCart?: CartContextType['getUserCart'];
  index?: number;
  mockSetLocalStorage?: (value: { billToId: string; shipToId: string }) => void;
  noAccount?: boolean;
  pushAlert?: ReturnType<typeof useSnackbar>['pushAlert'];
  updateAccounts?: (billTo?: EcommAccount, shipTo?: EcommAccount) => void;
}

/******************************/
/* Misc                       */
/******************************/
const testIds = allTestIds.SelectAccounts.SelectAccountsForm;

/******************************/
/* Custom Hook Mocks          */
/******************************/
jest.mock('@dialexa/reece-component-library', () => ({
  ...jest.requireActual('@dialexa/reece-component-library'),
  useSnackbar: jest.fn()
}));
jest.mock('hooks/useLocalStorage', () => ({
  useLocalStorage: jest.fn()
}));

/******************************/
/* Setup Functions            */
/******************************/
const setupRender = ({
  cart,
  getUserCart,
  index,
  mockSetLocalStorage,
  noAccount,
  pushAlert,
  updateAccounts
}: SetupRenderProps) => {
  // mock useLocalStorage
  const localStorage = noAccount
    ? { billToId: '', shipToId: '' }
    : {
        billToId: dummyEcommAccounts[index ?? 0].id ?? '',
        shipToId: dummyEcommAccounts[index ?? 0].shipTos?.[0].id ?? ''
      };
  (useLocalStorage as jest.Mock).mockReturnValue([
    localStorage,
    mockSetLocalStorage ?? jest.fn()
  ]);

  // mock useSnackbar
  (useSnackbar as jest.Mock).mockReturnValue({
    pushAlert: pushAlert ?? jest.fn()
  });

  // return rendered page
  return render(
    <div>
      <SelectAccountsForm onContinue={jest.fn()} />
    </div>,
    {
      authConfig: {
        authState: { isAuthenticated: true },
        profile: {
          userId: 'testuser',
          permissions: [],
          isEmployee: true,
          isVerified: true
        }
      },
      cartConfig: { cart, getUserCart: getUserCart ?? jest.fn() },
      selectedAccountsConfig: { updateAccounts: updateAccounts ?? jest.fn() },
      mocks: noAccount
        ? []
        : [USER_ACCOUNTS_SUCCESS, REFRESH_SHIP_TO_ACCOUNT_SUCCESS]
    }
  );
};

/******************************/
/* Tests Suits                */
/******************************/
describe('SelectAccounts - SelectAccountsForm', () => {
  it('Should render form', async () => {
    const { findByTestId } = setupRender({ noAccount: true });

    await act(async () => {
      const billTo = await findByTestId(testIds.billTo);
      const shipTo = await findByTestId(testIds.shipTo);
      const continueButton = await findByTestId(testIds.continue);

      expect(billTo).toBeInTheDocument();
      expect(shipTo).toBeInTheDocument();
      expect(continueButton).toBeInTheDocument();
    });
  });

  it('Expect refreshed components to be rendered after clicking refresh ship-to', async () => {
    const { findByTestId } = setupRender({ noAccount: true });

    await act(async () => {
      const button = await findByTestId(testIds.refreshShipToButton);
      expect(button).toBeInTheDocument();
    });
    await clickButton(testIds.refreshShipToButton);
    await act(async () => {
      const done = await findByTestId(testIds.refreshShipToDone);
      expect(done).toBeInTheDocument();
    });
  });

  it('Expect no change of ship-to accounts upon reload', async () => {
    const index = 18; // 18 is one has the billToAccountId for refreshShipTo mock
    const { findByTestId } = setupRender({ index });

    const shipToInput = await findByTestId(`${testIds.shipTo}-input`);
    await act(async () => {
      const button = await findByTestId(testIds.refreshShipToButton);
      expect(button).toBeInTheDocument();
      expect(shipToInput).toHaveValue(
        dummyEcommAccounts[index].shipTos![0].name!
      );
    });
    await clickButton(testIds.refreshShipToButton);
    await act(async () => {
      const done = await findByTestId(testIds.refreshShipToDone);
      expect(done).toBeInTheDocument();
      expect(shipToInput).toHaveValue(dummyRefreshShipToAccount[1].name);
    });
  });

  it('Expect make default check to be triggered and checked', async () => {
    const { findByTestId } = setupRender({ noAccount: true });

    await clickButton(testIds.makeDefault);
    await act(async () => {
      const checkboxRoot = await findByTestId(testIds.makeDefault);
      const checkbox = checkboxRoot.getElementsByTagName('input')[0];
      expect(checkbox).toBeChecked();
    });
  });

  it('Expect to render default selected accounts', async () => {
    const index = 1;
    const { findByTestId } = setupRender({ index });

    const billToInput = await findByTestId(`${testIds.billTo}-input`);
    const shipToInput = await findByTestId(`${testIds.shipTo}-input`);
    expect(billToInput).toHaveDisplayValue(dummyEcommAccounts[index].name!);
    expect(shipToInput).toHaveDisplayValue(
      dummyEcommAccounts[index].shipTos![0].name!
    );
  });

  it('Expect to render default selected accounts with no ship-tos', async () => {
    const index = 4;
    const { findByTestId } = setupRender({ index });

    const billToInput = await findByTestId(`${testIds.billTo}-input`);
    const shipToInput = await findByTestId(`${testIds.shipTo}-input`);
    expect(billToInput).toHaveDisplayValue(dummyEcommAccounts[index].name!);
    expect(shipToInput).toHaveDisplayValue(dummyEcommAccounts[index].name!);
  });

  it('Expect to render blanks when ship-to names are null', async () => {
    const index = 3;
    const { findByTestId } = setupRender({ index });

    const billToInput = await findByTestId(`${testIds.billTo}-input`);
    const shipToInput = await findByTestId(`${testIds.shipTo}-input`);
    expect(billToInput).toHaveDisplayValue(dummyEcommAccounts[index].name!);
    expect(shipToInput).toHaveDisplayValue('');
  });

  it('Expect to render new value when changing the ship-to and bill-to accounts', async () => {
    const index = 1;
    const { findByTestId } = setupRender({ index });

    const billToInput = await findByTestId(`${testIds.billTo}-input`);
    const shipToInput = await findByTestId(`${testIds.shipTo}-input`);
    expect(billToInput).toHaveDisplayValue(dummyEcommAccounts[index].name!);
    expect(shipToInput).toHaveDisplayValue(
      dummyEcommAccounts[index].shipTos![0].name!
    );
  });

  it('Expect the bill-to input to update value onChange', async () => {
    const index = 2;
    const { findByTestId } = setupRender({ index });

    const billToAutocomplete = await findByTestId(testIds.billTo);
    const billToInput = within(billToAutocomplete).getByRole('textbox');

    await fillFormTextInput(testIds.billTo, '1');
    act(() => {
      fireEvent.focus(billToAutocomplete);
      fireEvent.keyDown(billToAutocomplete, { key: 'ArrowDown' });
      fireEvent.keyDown(billToAutocomplete, { key: 'Enter' });
      fireEvent.blur(billToAutocomplete);
    });
    expect(billToInput).toHaveDisplayValue(dummyEcommAccounts[21].name!);
  });

  it('Expect the ship-to input to update value onChange', async () => {
    const index = 3;
    const { findByTestId } = setupRender({ index });

    const shipToAutocomplete = await findByTestId(testIds.shipTo);
    const shipToInput = within(shipToAutocomplete).getByRole('textbox');

    await fillFormTextInput(testIds.shipTo, '1');
    act(() => {
      fireEvent.focus(shipToAutocomplete);
      fireEvent.keyDown(shipToAutocomplete, { key: 'ArrowDown' });
      fireEvent.keyDown(shipToAutocomplete, { key: 'Enter' });
      fireEvent.blur(shipToAutocomplete);
    });
    expect(shipToInput).toHaveDisplayValue(
      dummyEcommAccounts[index].shipTos![1].name!
    );
  });

  it('Expect clicking on the submit button to call getUserCart when cart is valid', async () => {
    const getUserCart = jest.fn();
    const { findByTestId } = setupRender({
      index: 1,
      cart: mockCart,
      getUserCart
    });

    await act(async () => {
      const continueButton = await findByTestId(testIds.continue);
      expect(continueButton).toBeInTheDocument();
    });
    await clickButton(testIds.continue);
    expect(getUserCart).toBeCalled();
  });

  it('Expect clicking on the submit button to throw error', async () => {
    const updateAccounts = jest.fn(() => {
      throw new Error('TEST');
    });
    const pushAlert = jest.fn();
    const { findByTestId } = setupRender({
      index: 1,
      pushAlert,
      updateAccounts
    });

    await act(async () => {
      const continueButton = await findByTestId(testIds.continue);
      expect(continueButton).toBeInTheDocument();
    });
    await clickButton(testIds.continue);
    expect(pushAlert).toBeCalled();
  });

  it('Expect submit to call setDefaultAccountId when default is checked', async () => {
    const mockSetLocalStorage = jest.fn();
    setupRender({
      index: 1,
      cart: mockCart,
      mockSetLocalStorage
    });

    await clickButton(testIds.makeDefault);
    await clickButton(testIds.continue);
    expect(mockSetLocalStorage).toBeCalled();
  });
});
