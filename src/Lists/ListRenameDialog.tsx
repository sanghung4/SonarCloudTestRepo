import React, { Dispatch, FormEvent, useContext } from 'react';

import {
  Button,
  Dialog,
  DialogContent,
  Grid,
  IconButton,
  DialogTitle
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { ListContext } from 'providers/ListsProvider';
import { CloseIcon } from 'icons';
import { ListNameInput } from 'Lists/util/styled';

type Props = {
  listName: string;
  setListName: Dispatch<string>;
  nameAlreadyExists: boolean;
};

export default function ListRenameDialog(props: Props) {
  const { listName, setListName, nameAlreadyExists } = props;

  /**
   * Custom hooks
   */
  const { t } = useTranslation();

  /**
   * Context
   */
  const { isRenaming, selectedList, setRenaming, updateList } =
    useContext(ListContext);

  /**
   * Callbacks
   */
  const saveRename = () => {
    if (!nameAlreadyExists) {
      updateList(listName, selectedList?.listLineItems ?? []);
    }
  };

  const cancelRename = () => {
    setRenaming(false);
    setListName(selectedList?.name ?? '');
  };

  return (
    <Dialog
      open={isRenaming}
      onClose={cancelRename}
      maxWidth="xs"
      data-testid="list-rename-dialog-popup"
    >
      <Grid container justifyContent="space-between">
        <DialogTitle>Rename List</DialogTitle>
        <IconButton
          onClick={cancelRename}
          size="large"
          data-testid="close-button"
        >
          <CloseIcon />
        </IconButton>
      </Grid>

      <DialogContent>
        <Grid
          width={560}
          item
          container
          xs={12}
          component="form"
          onSubmit={(e: FormEvent) => {
            saveRename();
            e.preventDefault();
          }}
        >
          <ListNameInput
            label={t('common.listName')}
            required
            disabled={!isRenaming}
            fullWidth
            value={listName}
            onChange={(e) => setListName(e.target.value)}
            error={
              (isRenaming && !listName) || (isRenaming && nameAlreadyExists)
            }
            inputProps={{
              'data-testid': 'list-name-input'
            }}
            helperText={
              isRenaming
                ? !listName
                  ? t('lists.listEmptyWarning')
                  : nameAlreadyExists
                  ? t('lists.listExistsWarning')
                  : null
                : null
            }
          />
        </Grid>
        <Grid
          container
          item
          xs={12}
          justifyContent="flex-end"
          alignContent="flex-start"
        >
          <Grid item py={1.5} pl={2} justifyContent="flex-end">
            <Button
              color="primaryLight"
              variant="inline"
              onClick={cancelRename}
              data-testid="cancel-rename-button"
            >
              {t('common.cancel')}
            </Button>
            <Button
              sx={{
                marginLeft: 4
              }}
              onClick={saveRename}
              data-testid="save-rename-button"
              disabled={!listName || nameAlreadyExists}
            >
              {t('common.save')}
            </Button>
          </Grid>
        </Grid>
      </DialogContent>
    </Dialog>
  );
}
