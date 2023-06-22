import React, { useContext, useMemo } from 'react';

import { Box, Button } from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { StarEmptyIcon, StarFilledIcon } from 'icons';
import { ListContext } from 'providers/ListsProvider';

type Props = {
  partNumber?: string;
  quantity?: number;
  index?: number;
  availableInList?: string[];
  routePath?: string;
  updatedAddedToLists?: (val: string[]) => void;
  isAddAlltoList?: boolean;
  cartId?: string;
};

function AddToListButton(props: Props) {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();

  /**
   * Context
   */
  const {
    initiateAddAllToList,
    initiateAddToList,
    selectedPartNumber,
    availableListIds
  } = useContext(ListContext);

  /**
   * Memos
   */
  const isAdded = useMemo(isAddedMemo, [
    availableListIds,
    selectedPartNumber,
    props
  ]);

  /**
   * Callbacks
   */
  function handleAddToList() {
    props.isAddAlltoList
      ? initiateAddAllToList(props.cartId ?? '', props.availableInList!)
      : props.quantity &&
        props.partNumber &&
        initiateAddToList(
          props.partNumber,
          props.quantity,
          props.availableInList!
        );
  }
  function handleButtonText() {
    let buttonText = '';
    if (props.routePath) {
      return buttonText;
    }
    if (!!props.availableInList?.length || isAdded) {
      buttonText = props.isAddAlltoList
        ? `${t('common.addedAllToList')}`
        : `${t('common.addedToList')}`;
    } else {
      buttonText = props.isAddAlltoList
        ? `${t('common.addAllToList')}`
        : `${t('common.addToList')}`;
    }
    return buttonText;
  }

  return (
    <Button
      color="primaryLight"
      variant="inline"
      data-testid={
        props.index === undefined
          ? `add-to-list-button`
          : `add-to-list-button-${props.index}`
      }
      sx={
        !!props.availableInList?.length || isAdded
          ? { textDecoration: 'underline', fontWeight: 'bold' }
          : undefined
      }
      startIcon={
        !!props.availableInList?.length || isAdded ? (
          <Box component={StarFilledIcon} sx={{ color: 'secondary.main' }} />
        ) : (
          <StarEmptyIcon />
        )
      }
      onClick={handleAddToList}
    >
      {handleButtonText()}
    </Button>
  );

  function isAddedMemo() {
    const isAdded = Boolean(
      (selectedPartNumber && selectedPartNumber === props.partNumber) ||
        (!selectedPartNumber && props.cartId && availableListIds?.length)
    );
    if (isAdded) {
      props.updatedAddedToLists?.(availableListIds);
    }
    if (!availableListIds.length) {
      return false;
    }
    return isAdded;
  }
}

export default AddToListButton;
