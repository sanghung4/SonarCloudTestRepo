// TO-DO: Refactor this entirely
import { useSnackbar } from '@dialexa/reece-component-library';
import { fireEvent, waitFor } from '@testing-library/react';
import { useLocation } from 'react-router-dom';

import { BranchContext, BranchContextType } from 'providers/BranchProvider';

import Lists from 'Lists';
import ListsProvider, {
  ListContext,
  ListContextType,
  ListMode
} from 'providers/ListsProvider';
import ListDrawer from 'Lists/ListDrawer';
import mocks, { LIST_PROVIDER_MOCKS } from 'Lists/test/index.mocks';
import * as t from 'locales/en/translation.json';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';
import { noop } from 'lodash-es';
import { Cart } from 'generated/graphql';
import userEvent from '@testing-library/user-event';
import { CartContextType } from 'providers/CartProvider';

window.scrollTo = jest.fn();
window.HTMLElement.prototype.scrollIntoView = jest.fn();

jest.mock('@dialexa/reece-component-library', () => ({
  ...jest.requireActual('@dialexa/reece-component-library'),
  useSnackbar: jest.fn()
}));

const pushAlert = jest.fn();

jest.mock('react-router-dom', () => ({
  useHistory: () => ({
    push: jest.fn(),
    replace: jest.fn()
  }),
  useLocation: jest.fn()
}));

const VALID_ACCOUNTS = {
  billTo: {
    id: 'testAccount'
  },
  shipTo: {
    id: 'testAccount'
  }
};

const NO_LISTS_ACCOUNT = {
  billTo: {
    id: 'noListsAccount'
  },
  shipTo: {
    id: 'testAccount'
  }
};

describe('Lists tests', () => {
  beforeAll(() => setBreakpoint());

  beforeEach(() => {
    (useLocation as jest.Mock).mockReturnValue({
      pathname: '/lists'
    });

    (useSnackbar as jest.Mock).mockImplementation(() => ({
      pushAlert
    }));
  });

  const defaultGetUserCart = () => new Promise<Cart | undefined>(noop);
  const cartContext: CartContextType = {
    addItemToCart: noop,
    addItemsToCart: noop,
    addAllListItemsToCart: noop,
    cart: undefined,
    cartLoading: false,
    contractBranch: undefined,
    clearContract: noop,
    clearQuote: noop,
    deleteCartItems: defaultGetUserCart,
    deleteItem: noop,
    getUserCart: defaultGetUserCart,
    itemCount: 0,
    itemLoading: undefined,
    lineNotes: {},
    releaseContractToCart: noop,
    setContract: noop,
    setLineNotes: noop,
    setQuoteId: noop,
    setQuoteData: noop,
    setQuoteShipToId: noop,
    setPreviousCart: noop,
    setSelectedBranch: noop,
    updateCart: undefined,
    updateDelivery: undefined,
    updateItemQuantity: noop,
    updateWillCall: undefined,
    updateWillCallBranch: undefined,
    refreshCart: undefined,
    isWillCall: false,
    checkingOutWithQuote: false,
    disableAddToCart: false,
    deleteCreditCardFromCart: defaultGetUserCart
  };

  // it('Should make separate calls for the product and the pricing & availability', async () => {
  //   const branchContext = {
  //     shippingBranch: {
  //       branchId: 'any'
  //     }
  //   } as BranchContextType;

  //   const { getByTestId, findByTestId } = render(
  //     <BranchContext.Provider value={branchContext}>
  //       <ListsProvider>
  //         <Lists />
  //       </ListsProvider>
  //     </BranchContext.Provider>,
  //     {
  //       mocks: mocks.lists,
  //       selectedAccountsConfig: {
  //         selectedAccounts: VALID_ACCOUNTS
  //       }
  //     }
  //   );

  //   const listContainer = await findByTestId('list-line-items-container');
  //   expect(listContainer).toBeInTheDocument();

  //   const listItemId = 'L1_I1';
  //   const priceSkeleton = getByTestId(`price-skeleton-${listItemId}`);
  //   expect(listContainer).toMatchSnapshot('1 > loading');
  //   expect(priceSkeleton).toBeInTheDocument();

  //   await waitFor(() => new Promise((res) => setTimeout(res, 100)));
  //   expect(listContainer).toMatchSnapshot('2 > loaded');
  //   expect(priceSkeleton).not.toBeInTheDocument();
  // });

  it('Should show empty state', async () => {
    const { findByText } = render(
      <ListsProvider>
        <Lists />
      </ListsProvider>,
      {
        mocks: mocks.lists,
        selectedAccountsConfig: {
          selectedAccounts: NO_LISTS_ACCOUNT
        }
      }
    );

    expect(await findByText('You have no lists')).toBeTruthy();
  });

  // Covering https://reeceusa.atlassian.net/browse/MAX-1448
  it('Should handle if the queryParams does not match a list', async () => {
    jest.mock('hooks/useSearchParam', () => [{ name: 'DNE' }, jest.fn()]);

    const { findByTestId } = render(
      <ListsProvider>
        <Lists />
      </ListsProvider>,
      {
        mocks: mocks.lists,
        selectedAccountsConfig: {
          selectedAccounts: VALID_ACCOUNTS
        }
      }
    );
    const changeListButton = await findByTestId('change-list-button');
    expect(changeListButton).toHaveTextContent(
      'List List One (3 items)arrow-drop-down.svg'
    );
  });

  // it('Should delete an item from the list', async () => {
  //   const branchContext = {
  //     shippingBranch: {
  //       branchId: 'any'
  //     }
  //   } as BranchContextType;

  //   const { findByTestId, queryByTestId } = render(
  //     <BranchContext.Provider value={branchContext}>
  //       <ListsProvider>
  //         <Lists />
  //       </ListsProvider>
  //     </BranchContext.Provider>,
  //     {
  //       mocks: mocks.lists,
  //       selectedAccountsConfig: {
  //         selectedAccounts: VALID_ACCOUNTS
  //       }
  //     }
  //   );

  //   const listItemId = 'L1_I1';
  //   const listLineItem = await findByTestId(`lists-line-item-${listItemId}`);
  //   const deleteButton = await findByTestId(`delete-button-${listItemId}`);
  //   const listLineItemsContainer = await findByTestId(
  //     'list-line-items-container'
  //   );

  //   expect(listLineItem).toBeInTheDocument();
  //   await waitFor(() => {
  //     expect(
  //       within(listLineItem).queryByTestId('quantity-input')
  //     ).toBeInTheDocument();
  //   });
  //   expect(listLineItemsContainer).toMatchSnapshot();

  //   fireEvent.click(deleteButton);

  //   expect(listLineItemsContainer).toMatchSnapshot();

  //   await waitFor(() => {
  //     expect(
  //       queryByTestId(`lists-line-item-${listItemId}`)
  //     ).not.toBeInTheDocument();
  //   });
  //   expect(listLineItemsContainer).toMatchSnapshot();
  // });

  it('Clicking delete list should bring up list dialog and clicking close button should close it', async () => {
    const branchContext = {
      shippingBranch: {
        branchId: 'any'
      }
    } as BranchContextType;

    const { findByTestId } = render(
      <BranchContext.Provider value={branchContext}>
        <ListsProvider>
          <Lists />
        </ListsProvider>
      </BranchContext.Provider>,
      {
        mocks: mocks.lists,
        selectedAccountsConfig: {
          selectedAccounts: VALID_ACCOUNTS
        }
      }
    );
    const moreActionsButton = await findByTestId('list-more-actions-button');
    fireEvent.click(moreActionsButton);
    const deleteListButton = await findByTestId(`delete-list-button`);

    fireEvent.click(deleteListButton);

    const closeButton = await findByTestId('close-button');
    const yesButton = await findByTestId('yes-button');
    const cancelButton = await findByTestId('cancel-button');

    await waitFor(() => {
      expect(closeButton).toBeInTheDocument();
      expect(yesButton).toBeInTheDocument();
      expect(cancelButton).toBeInTheDocument();
    });

    //Testing close button
    fireEvent.click(closeButton);

    await waitFor(() => {
      expect(closeButton).not.toBeInTheDocument();
      expect(yesButton).not.toBeInTheDocument();
      expect(cancelButton).not.toBeInTheDocument();
    });
  });

  it('Clicking delete list should bring up list dialog and clicking cancel button should close it', async () => {
    const branchContext = {
      shippingBranch: {
        branchId: 'any'
      }
    } as BranchContextType;

    const { findByTestId } = render(
      <BranchContext.Provider value={branchContext}>
        <ListsProvider>
          <Lists />
        </ListsProvider>
      </BranchContext.Provider>,
      {
        mocks: mocks.lists,
        selectedAccountsConfig: {
          selectedAccounts: VALID_ACCOUNTS
        }
      }
    );
    const moreActionsButton = await findByTestId('list-more-actions-button');
    fireEvent.click(moreActionsButton);

    const deleteListButton = await findByTestId(`delete-list-button`);

    fireEvent.click(deleteListButton);

    const closeButton = await findByTestId('close-button');
    const yesButton = await findByTestId('yes-button');
    const cancelButton = await findByTestId('cancel-button');

    await waitFor(() => {
      expect(closeButton).toBeInTheDocument();
      expect(yesButton).toBeInTheDocument();
      expect(cancelButton).toBeInTheDocument();
    });

    fireEvent.click(cancelButton);

    await waitFor(() => {
      expect(closeButton).not.toBeInTheDocument();
      expect(yesButton).not.toBeInTheDocument();
      expect(cancelButton).not.toBeInTheDocument();
    });
  });

  it('Clicking delete list should bring up list dialog and clicking yes should delete it', async () => {
    const branchContext = {
      shippingBranch: {
        branchId: 'any'
      }
    } as BranchContextType;

    const { findByTestId } = render(
      <BranchContext.Provider value={branchContext}>
        <ListsProvider>
          <Lists />
        </ListsProvider>
      </BranchContext.Provider>,
      {
        mocks: mocks.lists,
        selectedAccountsConfig: {
          selectedAccounts: VALID_ACCOUNTS
        }
      }
    );

    const moreActionsButton = await findByTestId('list-more-actions-button');
    fireEvent.click(moreActionsButton);
    const deleteListButton = await findByTestId(`delete-list-button`);

    fireEvent.click(deleteListButton);

    const listDialogContainer = await findByTestId('list-dialog-popup');
    expect(listDialogContainer).toBeInTheDocument();

    const closeButton = await findByTestId('close-button');
    const yesButton = await findByTestId('yes-button');
    const cancelButton = await findByTestId('cancel-button');

    //Testing yes button
    fireEvent.click(yesButton);

    await waitFor(() => {
      expect(closeButton).not.toBeInTheDocument();
      expect(yesButton).not.toBeInTheDocument();
      expect(cancelButton).not.toBeInTheDocument();
    });
  });

  it('Should call the CartProvider with the correct listId when add to cart is pressed', async () => {
    const branchContext = {
      shippingBranch: {
        branchId: 'any'
      }
    } as BranchContextType;

    const addAllListItemsToCart = jest.fn();

    const { getByTestId } = render(
      <BranchContext.Provider value={branchContext}>
        <ListsProvider>
          <Lists />
        </ListsProvider>
      </BranchContext.Provider>,
      {
        mocks: mocks.lists,
        cartConfig: { ...cartContext, addAllListItemsToCart },
        selectedAccountsConfig: {
          selectedAccounts: VALID_ACCOUNTS
        }
      }
    );

    await waitFor(() => new Promise((res) => setTimeout(res, 100)));
    const addAllToCartButton = getByTestId('lists-add-all-to-cart');
    expect(addAllToCartButton).toBeTruthy();
    fireEvent.click(addAllToCartButton);

    expect(addAllListItemsToCart).toHaveBeenCalledTimes(1);
  });

  // Check ProdId existence - MAX - 2972
  it('Should display product id', async () => {
    const branchContext = {
      shippingBranch: {
        branchId: 'any'
      }
    } as BranchContextType;

    const { findByTestId } = render(
      <BranchContext.Provider value={branchContext}>
        <ListsProvider>
          <Lists />
        </ListsProvider>
      </BranchContext.Provider>,
      {
        mocks: mocks.lists,
        selectedAccountsConfig: {
          selectedAccounts: VALID_ACCOUNTS
        }
      }
    );

    const listItemId = 'L1_I1';
    const listLineItem = await findByTestId(`lists-line-item-${listItemId}`);
    const productId = await findByTestId(`product-id-${listItemId}`);

    expect(listLineItem).toBeInTheDocument();
    expect(productId).toBeInTheDocument();
  });

  //MAX-2972: Search in a list by Product Id
  // it('Should find item in the list by ProdId', async () => {
  //   const branchContext = {
  //     shippingBranch: {
  //       branchId: 'any'
  //     }
  //   } as BranchContextType;

  //   const { findByTestId } = render(
  //     <BranchContext.Provider value={branchContext}>
  //       <ListsProvider>
  //         <Lists />
  //       </ListsProvider>
  //     </BranchContext.Provider>,
  //     {
  //       mocks: mocks.lists,
  //       selectedAccountsConfig: {
  //         selectedAccounts: VALID_ACCOUNTS
  //       }
  //     }
  //   );

  //   const listItemId = 'L1_I1';
  //   const listLineItem = await findByTestId(`lists-line-item-${listItemId}`);
  //   const productId = await findByTestId(`product-id-${listItemId}`);
  //   const searchProdId = screen.getByPlaceholderText(
  //     'Enter Product Name, Brand, SKU, Reece Part #'
  //   );
  //   const viewResultsButton = await findByTestId('view-search-in-list-results');

  //   expect(listLineItem).toBeInTheDocument();
  //   expect(productId).toBeInTheDocument();

  //   fireEvent.change(searchProdId, { target: { value: 'L1_I1' } });
  //   fireEvent.click(viewResultsButton);

  //   expect(productId).toBeInTheDocument();
  //   expect(productId).toHaveTextContent('MSC-L1_I1');

  //   expect(viewResultsButton).toBeEnabled();
  // });
  //Check Manufactutrer number existence
  // it('Should display manufacturer number', async () => {
  //   const branchContext = {
  //     shippingBranch: {
  //       branchId: 'any'
  //     }
  //   } as BranchContextType;

  //   const { findByTestId } = render(
  //     <BranchContext.Provider value={branchContext}>
  //       <ListsProvider>
  //         <Lists />
  //       </ListsProvider>
  //     </BranchContext.Provider>,
  //     {
  //       mocks: mocks.lists,
  //       selectedAccountsConfig: {
  //         selectedAccounts: VALID_ACCOUNTS
  //       }
  //     }
  //   );

  //   const listItemId = 'L1_I1';
  //   const listLineItem = await findByTestId(`lists-line-item-${listItemId}`);
  //   const manufacturerNumber = await findByTestId(
  //     `manufacturer-number-${listItemId}`
  //   );
  //   const listLineItemsContainer = await findByTestId(
  //     'list-line-items-container'
  //   );

  //   expect(listLineItem).toBeInTheDocument();
  //   expect(manufacturerNumber).toHaveTextContent('MFR# 3');
  //   expect(listLineItemsContainer).toMatchSnapshot();
  // });
  // check the manufacturer name existence
  // it('Should display manufacturer name', async () => {
  //   const branchContext = {
  //     shippingBranch: {
  //       branchId: 'any'
  //     }
  //   } as BranchContextType;

  //   const { findByTestId } = render(
  //     <BranchContext.Provider value={branchContext}>
  //       <ListsProvider>
  //         <Lists />
  //       </ListsProvider>
  //     </BranchContext.Provider>,
  //     {
  //       mocks: mocks.lists,
  //       selectedAccountsConfig: {
  //         selectedAccounts: VALID_ACCOUNTS
  //       }
  //     }
  //   );

  //   const listItemId = 'L1_I1';
  //   const manufacturerName = await findByTestId(
  //     `manufacturer-name-${listItemId}`
  //   );
  //   const listLineItemsContainer = await findByTestId(
  //     'list-line-items-container'
  //   );

  //   expect(manufacturerName).toBeInTheDocument();
  //   expect(manufacturerName).toHaveTextContent('Tesla');
  //   expect(listLineItemsContainer).toMatchSnapshot();
  // });
  // Check the product name existence
  // it('Should display product name', async () => {
  //   const branchContext = {
  //     shippingBranch: {
  //       branchId: 'any'
  //     }
  //   } as BranchContextType;

  //   const { findByTestId } = render(
  //     <BranchContext.Provider value={branchContext}>
  //       <ListsProvider>
  //         <Lists />
  //       </ListsProvider>
  //     </BranchContext.Provider>,
  //     {
  //       mocks: mocks.lists,
  //       selectedAccountsConfig: {
  //         selectedAccounts: VALID_ACCOUNTS
  //       }
  //     }
  //   );

  //   const listItemId = 'L1_I1';
  //   const productName = await findByTestId(`item-name-${listItemId}`);
  //   const listLineItemsContainer = await findByTestId(
  //     'list-line-items-container'
  //   );

  //   expect(productName).toBeInTheDocument();
  //   expect(productName).toHaveTextContent('Model S Plaid');
  //   expect(listLineItemsContainer).toMatchSnapshot();
  // });
  it('should fire the setBranchSelectOpen when findAtOtherBranches is clicked', async () => {
    const setBranchSelectOpen = jest.fn();
    const setProductId = jest.fn();
    // @ts-ignore
    const branchContext = {
      shippingBranch: {
        branchId: 'any'
      },
      setBranchSelectOpen,
      setProductId
    } as BranchContextType;

    const { findAllByTestId } = render(
      <BranchContext.Provider value={branchContext}>
        <ListsProvider>
          <Lists />
        </ListsProvider>
      </BranchContext.Provider>,
      {
        mocks: mocks.lists,
        selectedAccountsConfig: {
          selectedAccounts: VALID_ACCOUNTS
        }
      }
    );
    await waitFor(() => new Promise((res) => setTimeout(res, 100)));
    const branchButton = await findAllByTestId('check-nearby-branches-button');
    fireEvent.click(branchButton[0]);
    expect(branchContext.setBranchSelectOpen).toHaveBeenCalled();
    expect(branchContext.setProductId).toHaveBeenCalled();
  });
  it('Should display check nearby branches button', async () => {
    const branchContext = {
      shippingBranch: {
        branchId: 'any'
      }
    } as BranchContextType;

    const { findAllByTestId } = render(
      <BranchContext.Provider value={branchContext}>
        <ListsProvider>
          <Lists />
        </ListsProvider>
      </BranchContext.Provider>,
      {
        mocks: mocks.lists,
        selectedAccountsConfig: {
          selectedAccounts: VALID_ACCOUNTS
        }
      }
    );

    const branchButton = await findAllByTestId('check-nearby-branches-button');

    expect(branchButton[0]).toBeInTheDocument();
    expect(branchButton[0]).toHaveTextContent(t.product.checkNearByBranches);
  });
  it.skip('Should display the current branch details', async () => {
    const branchContext = {
      shippingBranch: {
        branchId: 'any'
      }
    } as BranchContextType;

    const { findAllByTestId } = render(
      <BranchContext.Provider value={branchContext}>
        <ListsProvider>
          <Lists />
        </ListsProvider>
      </BranchContext.Provider>,
      {
        mocks: mocks.lists,
        selectedAccountsConfig: {
          selectedAccounts: VALID_ACCOUNTS
        }
      }
    );

    const branchDetails = await findAllByTestId('current-branch-details');
    expect(branchDetails[0]).toBeInTheDocument();
    expect(branchDetails).toBeTruthy();
  });

  it('Should call the addItemToCart when add to cart is clicked', async () => {
    const branchContext = {
      shippingBranch: {
        branchId: 'any'
      }
    } as BranchContextType;
    const addAllListItemsToCart = jest.fn();

    const { findAllByTestId } = render(
      <BranchContext.Provider value={branchContext}>
        <ListsProvider>
          <Lists />
        </ListsProvider>
      </BranchContext.Provider>,
      {
        mocks: mocks.lists,
        cartConfig: { ...cartContext, addAllListItemsToCart },
        selectedAccountsConfig: {
          selectedAccounts: VALID_ACCOUNTS
        }
      }
    );

    await waitFor(() => new Promise((res) => setTimeout(res, 100)));

    const cartButton = await findAllByTestId('add-to-cart-button', {
      exact: false
    });

    expect(cartButton).toBeTruthy();
    expect(cartButton[0]).toHaveTextContent('Add to Cart');
    fireEvent.click(cartButton[0]);
    expect(cartButton[0]).toMatchSnapshot();
  });

  // checking the product Image and Quantity Input
  it('Should display product Image', async () => {
    const branchContext = {
      shippingBranch: {
        branchId: 'any'
      }
    } as BranchContextType;

    const { findAllByRole, findAllByTestId } = render(
      <BranchContext.Provider value={branchContext}>
        <ListsProvider>
          <Lists />
        </ListsProvider>
      </BranchContext.Provider>,
      {
        mocks: mocks.lists,
        selectedAccountsConfig: {
          selectedAccounts: VALID_ACCOUNTS
        }
      }
    );
    const productImage = await findAllByRole('img');
    const qtyInput = await findAllByTestId('list-line-item-qty-input');

    expect(productImage.length).toBeTruthy();
    expect(qtyInput.length).toBeTruthy();
  });

  // check the cancel button existence
  // TO-DO: Rewrite unit tests for Lists
  it.skip('cancel and close buttons should close the drawer on click', async () => {
    const setOpenFunction = jest.fn();

    const mockListProvider = {
      ...LIST_PROVIDER_MOCKS.VALID_LIST,
      listMode: ListMode.CHANGE,
      setOpen: setOpenFunction
    } as ListContextType;

    const { findByTestId } = render(
      <ListContext.Provider value={mockListProvider}>
        <ListDrawer />
      </ListContext.Provider>,
      {
        mocks: mocks.lists,
        selectedAccountsConfig: {
          selectedAccounts: VALID_ACCOUNTS
        }
      }
    );
    const cancelButton = await findByTestId('cancel-button');
    expect(cancelButton).toBeInTheDocument();
    fireEvent.click(cancelButton);
    expect(setOpenFunction).toBeCalledWith(false);

    const closeButton = await findByTestId('list-drawer-close-button');
    expect(closeButton).toBeInTheDocument();
    fireEvent.click(closeButton);
    expect(setOpenFunction).toBeCalledWith(false);
  });
  // TO-DO: Rewrite unit tests for Lists
  it.skip('change button should change list and close drawer on click', async () => {
    const setOpenFunction = jest.fn();

    const mockListProvider = {
      ...LIST_PROVIDER_MOCKS.VALID_LIST,
      listMode: ListMode.CHANGE,
      resetUploadState: jest.fn(),
      setSelectedList: jest.fn(),
      setRenaming: jest.fn(),
      setOpen: setOpenFunction
    } as ListContextType;

    const { findByTestId } = render(
      <ListContext.Provider value={mockListProvider}>
        <ListDrawer></ListDrawer>
      </ListContext.Provider>,
      {
        mocks: mocks.lists
      }
    );

    const searchTextBox = await findByTestId('search-for-lists-input');
    userEvent.type(searchTextBox, 'Copper');

    const listSelectButton = await findByTestId(
      `list-item-${LIST_PROVIDER_MOCKS.VALID_LIST.lists[0].name}`
    );
    expect(listSelectButton).toBeInTheDocument();

    const changeListSubmitButton = await findByTestId(
      'Change-list-submit-button'
    );
    expect(changeListSubmitButton).toBeInTheDocument();

    //Clicking without selecting a list does nothing
    fireEvent.click(changeListSubmitButton);
    expect(setOpenFunction).not.toBeCalled();

    //Clicking after selecting a list moves it forward
    fireEvent.click(listSelectButton);
    expect(listSelectButton).not.toBeInTheDocument();
    fireEvent.click(changeListSubmitButton);
    expect(setOpenFunction).toBeCalledWith(false);
  });
  // TO-DO: Rewrite unit tests for Lists
  it.skip('add button should add to list and close drawer on click', async () => {
    const setOpenFunction = jest.fn();

    const mockListProvider = {
      ...LIST_PROVIDER_MOCKS.VALID_LIST,
      listMode: ListMode.ADD_ITEM,
      resetUploadState: jest.fn(),
      setSelectedList: jest.fn(),
      setRenaming: jest.fn(),
      setAvailableListIds: jest.fn(),
      setOpen: setOpenFunction
    } as ListContextType;

    const { findByTestId } = render(
      <ListContext.Provider value={mockListProvider}>
        <ListDrawer></ListDrawer>
      </ListContext.Provider>,
      {
        mocks: mocks.lists
      }
    );

    const listSelectButton = await findByTestId(
      `list-item-${LIST_PROVIDER_MOCKS.VALID_LIST.lists[0].name}`
    );
    expect(listSelectButton).toBeInTheDocument();
    fireEvent.click(listSelectButton);

    expect(listSelectButton).not.toBeInTheDocument();

    const addToListSubmitButton = await findByTestId(
      'Add-to-List-submit-button'
    );
    expect(addToListSubmitButton).toBeInTheDocument();
    fireEvent.click(addToListSubmitButton);

    expect(setOpenFunction).toBeCalledWith(false);
  });
  // TO-DO: Rewrite unit tests for Lists
  it.skip('add item textbox enter should create new list, add item to it and close drawer on click', async () => {
    const setOpenFunction = jest.fn();
    const toggleItemInListsFunction = jest.fn(() => {
      return true;
    });

    const mockListProvider = {
      ...LIST_PROVIDER_MOCKS.VALID_LIST,
      listMode: ListMode.ADD_ITEM,
      resetUploadState: jest.fn(),
      setSelectedList: jest.fn(),
      setRenaming: jest.fn(),
      setAvailableListIds: jest.fn(),
      createList: jest.fn(),
      toggleItemInLists: toggleItemInListsFunction,
      setOpen: setOpenFunction
    } as ListContextType;

    const { findByTestId } = render(
      <ListContext.Provider value={mockListProvider}>
        <ListDrawer></ListDrawer>
      </ListContext.Provider>,
      {
        mocks: mocks.lists
      }
    );

    const createListTextBox = await findByTestId('new-list-name-input');
    userEvent.type(createListTextBox, 'Copper');
    userEvent.keyboard('{enter}');

    expect(setOpenFunction).toBeCalledWith(false);
  });
  // TO-DO: Rewrite unit tests for Lists
  it.skip('create list button should create list and close drawer on click', async () => {
    const setOpenFunction = jest.fn();

    const mockListProvider = {
      ...LIST_PROVIDER_MOCKS.VALID_LIST,
      listMode: ListMode.CREATE,
      resetUploadState: jest.fn(),
      setSelectedList: jest.fn(),
      setRenaming: jest.fn(),
      setAvailableListIds: jest.fn(),
      createList: jest.fn(),
      setOpen: setOpenFunction
    } as ListContextType;

    const { findByTestId } = render(
      <ListContext.Provider value={mockListProvider}>
        <ListDrawer></ListDrawer>
      </ListContext.Provider>,
      {
        mocks: mocks.lists
      }
    );

    const createListSubmitButton = await findByTestId(
      'Create-List-submit-button'
    );
    expect(createListSubmitButton).toBeInTheDocument();

    //Clicking without entering a listname does nothing
    fireEvent.click(createListSubmitButton);
    expect(setOpenFunction).not.toBeCalled();

    //Clicking after entering a listname succeeds
    const createListTextBox = await findByTestId('new-list-name-input');
    userEvent.type(createListTextBox, 'Copper');
    fireEvent.click(createListSubmitButton);
    expect(setOpenFunction).toBeCalledWith(false);
  });
  // TO-DO: Rewrite unit tests for Lists
  it.skip('upload list button should upload list and close drawer on click', async () => {
    const setOpenFunction = jest.fn();

    const mockListProvider = {
      ...LIST_PROVIDER_MOCKS.VALID_LIST,
      listMode: ListMode.UPLOAD,
      resetUploadState: jest.fn(),
      setSelectedList: jest.fn(),
      setRenaming: jest.fn(),
      setAvailableListIds: jest.fn(),
      createList: jest.fn(),
      setSelectedUploadList: jest.fn(),
      setOpen: setOpenFunction
    } as ListContextType;

    const { findByTestId } = render(
      <ListContext.Provider value={mockListProvider}>
        <ListDrawer></ListDrawer>
      </ListContext.Provider>,
      {
        mocks: mocks.lists
      }
    );

    const createListTextBox = await findByTestId('search-for-lists-input');
    userEvent.type(createListTextBox, 'Copper');

    const listSelectButton = await findByTestId(
      `list-item-${LIST_PROVIDER_MOCKS.VALID_LIST.lists[1].name}`
    );
    expect(listSelectButton).toBeInTheDocument();
    fireEvent.click(listSelectButton);

    //Call without setListToSelect being set
    const uploadListSubmitButton = await findByTestId(
      'Select-List-submit-button'
    );
    expect(uploadListSubmitButton).toBeInTheDocument();
    fireEvent.click(uploadListSubmitButton);
    expect(setOpenFunction).toBeCalled();

    //Call with setListToSelect being set
    fireEvent.click(uploadListSubmitButton);
    expect(setOpenFunction).toBeCalled();
  });
  // TO-DO: Rewrite unit tests for Lists
  // describe('List Upload Response tests', () => {
  //   beforeAll(() => setBreakpoint('desktop'));

  //   beforeEach(() => {
  //     (useLocation as jest.Mock).mockReturnValue({
  //       pathname: '/lists'
  //     });

  //     (useSnackbar as jest.Mock).mockImplementation(() => ({
  //       pushAlert
  //     }));
  //   });

  //   it('Should select and display a new list', async () => {
  //     jest.mock('hooks/useSearchParam', () => [{ name: 'New List' }, jest.fn()]);
  //     (useLocation as jest.Mock).mockImplementation(() => ({
  //       pathname: '/lists',
  //       search: '?name=New+List'
  //     }));

  //     const { container, findByTestId, getByTestId } = render(
  //       <ListContext.Provider value={LIST_PROVIDER_MOCKS.UPLOAD_NEW_LIST}>
  //         <Lists />
  //       </ListContext.Provider>,
  //       {
  //         mocks: mocks.uploadList,
  //         selectedAccountsConfig: {
  //           selectedAccounts: VALID_ACCOUNTS
  //         }
  //       }
  //     );

  //     const changeListButton = await findByTestId('change-list-button');
  //     expect(changeListButton).toHaveTextContent(
  //       'List New List (3 items)arrow-drop-down.svg'
  //     );

  //     expect(pushAlert).toHaveBeenCalledWith(
  //       t.lists.createNewSuccess.replace('{{name}}', 'New List'),
  //       { variant: 'success' }
  //     );

  //     const uploadErrors = getByTestId('list-upload-errors');
  //     expect(uploadErrors).toBeInTheDocument();
  //     expect(uploadErrors).toHaveTextContent(
  //       t.lists.uploadError_other.replace('{{count}}', '4')
  //     );
  //     expect(container).toMatchSnapshot();
  //   });

  //   it('Should select and display an existing list', async () => {
  //     jest.mock('hooks/useSearchParam', () => [
  //       { name: 'Existing List' },
  //       jest.fn()
  //     ]);
  //     (useLocation as jest.Mock).mockImplementation(() => ({
  //       pathname: '/lists',
  //       search: '?name=Existing+List'
  //     }));

  //     const { container, findByTestId, getByTestId } = render(
  //       <ListContext.Provider value={LIST_PROVIDER_MOCKS.UPLOAD_TO_LIST}>
  //         <Lists />
  //       </ListContext.Provider>,
  //       {
  //         mocks: mocks.uploadList,
  //         selectedAccountsConfig: {
  //           selectedAccounts: VALID_ACCOUNTS
  //         }
  //       }
  //     );

  //     const changeListButton = await findByTestId('change-list-button');
  //     expect(changeListButton).toHaveTextContent(
  //       'List Existing List (3 items)arrow-drop-down.svg'
  //     );

  //     expect(pushAlert).toHaveBeenCalledWith(
  //       t.lists.uploadSuccess_one.replace('{{count}}', '1'),
  //       { variant: 'success' }
  //     );

  //     const uploadErrors = getByTestId('list-upload-errors');
  //     expect(uploadErrors).toBeInTheDocument();
  //     expect(uploadErrors).toHaveTextContent(
  //       t.lists.duplicateError_one.replace('{{count}}', '1')
  //     );
  //     const lineItem = getByTestId('lists-line-item-L1_I3');
  //     expect(within(lineItem).getByTestId('quantity-input')).toHaveValue('10');

  //     expect(container).toMatchSnapshot();
  //   });
});
