import React from 'react';

import { fireEvent, waitFor } from '@testing-library/react';

import AddToListButton from 'Product/AddToListButton';

import { ListContext, ListContextType } from 'providers/ListsProvider';
import { LIST_PROVIDER_MOCKS } from 'Lists/test/index.mocks';

import { render } from 'test-utils/TestWrapper';

import * as t from 'locales/en/translation.json';

describe('AddToListButton tests', () => {
  it('Should show the add to list button', async () => {
    const { getByText } = render(<AddToListButton partNumber="1234" />);

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    expect(getByText(`${t.common.addToList}`)).toBeTruthy();
  });

  it('Should show added to list button when the product already in the list', async () => {
    const availableInList = ['1234', '2345'];

    const { getByText } = render(
      <AddToListButton partNumber="1234" availableInList={availableInList} />
    );

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    expect(getByText(`${t.common.addedToList}`)).toBeTruthy();
  });

  it('Should show the add to list button with product index and quantity', async () => {
    const index = 1;

    const { getByTestId } = render(
      <AddToListButton partNumber="1234" index={index} quantity={2} />
    );

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    const indexedList = getByTestId(`add-to-list-button-${index}`);
    expect(indexedList).toBeTruthy();
  });

  it('Should initiate add to list when button is clicked', async () => {
    const initiateAddToList = jest.fn();

    const index = 1;
    const mockListProvider = {
      ...LIST_PROVIDER_MOCKS.VALID_LIST,
      initiateAddToList
    } as ListContextType;

    const { getByTestId, getByText } = render(
      <ListContext.Provider value={mockListProvider}>
        <AddToListButton partNumber="1234" index={index} quantity={2} />
      </ListContext.Provider>
    );

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    const addToListButton = getByTestId(`add-to-list-button-${index}`);

    expect(addToListButton).toBeTruthy();

    fireEvent.click(addToListButton);

    expect(initiateAddToList).toHaveBeenCalledTimes(1);
  });

  it('Should show added to list after selected product updated to list', async () => {
    const initiateAddToList = jest.fn();
    const mockUpdatedAddedToLists = jest.fn();
    const index = 1;
    const mockListProvider = {
      ...LIST_PROVIDER_MOCKS.VALID_LIST,
      initiateAddToList,
      selectedPartNumber: '1234'
    } as ListContextType;
    const { getByTestId, getByText } = render(
      <ListContext.Provider value={mockListProvider}>
        <AddToListButton
          partNumber={'1234'}
          index={index}
          quantity={2}
          updatedAddedToLists={mockUpdatedAddedToLists}
        />
      </ListContext.Provider>
    );

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    const addToListButton = getByTestId(`add-to-list-button-${index}`);

    expect(addToListButton).toBeTruthy();

    expect(getByText(`${t.common.addedToList}`)).toBeTruthy();
  });

  it('Should show the add all to list button in cart', async () => {
    const { getByText } = render(
      <AddToListButton isAddAlltoList={true} cartId={'a234'} />
    );

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    expect(getByText(`${t.common.addAllToList}`)).toBeTruthy();
  });

  it('Should show the added all to list button in cart', async () => {
    const initiateAddToList = jest.fn();
    const initiateAddAllToList = jest.fn();
    const mockUpdatedAddedToLists = jest.fn();
    const index = -1;
    const mockListProvider = {
      ...LIST_PROVIDER_MOCKS.VALID_LIST,
      initiateAddAllToList,
      initiateAddToList,
      selectedPartNumber: '1234'
    } as ListContextType;

    const { getByTestId, getByText } = render(
      <ListContext.Provider value={mockListProvider}>
        <AddToListButton
          index={index}
          updatedAddedToLists={mockUpdatedAddedToLists}
          availableInList={['a123']}
          isAddAlltoList={true}
          cartId={'b234'}
        />
      </ListContext.Provider>
    );

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    const addToListButton = getByTestId(`add-to-list-button-${index}`);

    expect(addToListButton).toBeTruthy();

    expect(getByText(`${t.common.addedAllToList}`)).toBeTruthy();

    fireEvent.click(addToListButton);

    expect(initiateAddAllToList).toHaveBeenCalledTimes(1);
  });

  it('Should show the added all to list button in cart when added to a new list', async () => {
    const initiateAddToList = jest.fn();
    const initiateAddAllToList = jest.fn();
    const mockUpdatedAddedToLists = jest.fn();
    const index = -1;
    const mockListProvider = {
      ...LIST_PROVIDER_MOCKS.VALID_LIST,
      initiateAddAllToList,
      initiateAddToList
    } as ListContextType;

    const { getByTestId, getByText } = render(
      <ListContext.Provider value={mockListProvider}>
        <AddToListButton
          index={index}
          updatedAddedToLists={mockUpdatedAddedToLists}
          availableInList={[]}
          isAddAlltoList={true}
          cartId={'b234'}
        />
      </ListContext.Provider>
    );

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    const addToListButton = getByTestId(`add-to-list-button-${index}`);

    expect(addToListButton).toBeTruthy();

    expect(getByText(`${t.common.addedAllToList}`)).toBeTruthy();

    fireEvent.click(addToListButton);

    expect(initiateAddAllToList).toHaveBeenCalledTimes(1);
  });
});
