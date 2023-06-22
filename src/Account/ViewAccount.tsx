import React, { useState } from 'react';

import {
  Box,
  Button,
  Card,
  CardHeader,
  Divider,
  IconButton,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { EditIcon } from 'icons';
import UpdatePasswordDialog from 'Account/UpdatePasswordDialog';
import UserInfoText from 'Account/UserInfoText';

interface Props {
  user: any;
  onEditClicked: () => void;
}

function ViewAccount(props: Props) {
  /**
   * Custom hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * State
   */
  const [changePasswordDialogOpen, setChangePasswordDialogOpen] =
    useState(false);

  /**
   * Callbacks
   */

  const handleCloseChangePasswordDialog = () => {
    setChangePasswordDialogOpen(false);
  };

  const handleOpenChangePasswordDialog = () => {
    setChangePasswordDialogOpen(true);
  };

  return (
    <Box data-testid="viewaccount-component">
      <UpdatePasswordDialog
        open={changePasswordDialogOpen}
        onClose={handleCloseChangePasswordDialog}
        user={props.user}
      />
      <Card square={isSmallScreen} raised={!isSmallScreen}>
        <Box p={1}>
          <CardHeader
            action={
              <IconButton
                onClick={props.onEditClicked}
                size="large"
                data-testid="editaccount-button"
              >
                <EditIcon />
              </IconButton>
            }
            title={t('common.accountInformation')}
            titleTypographyProps={{ variant: isSmallScreen ? 'h5' : 'h4' }}
            data-testid="account-information-title"
          />
        </Box>
        <Box>
          <Divider />
        </Box>
        <UserInfoText
          fieldName={` ${t('common.name')}: `}
          fieldValue={`${props.user.firstName} ${props.user.lastName}`}
          color="primary"
          testId="name-field"
        />
        <Box pr={2} pl={2}>
          <Divider />
        </Box>
        <UserInfoText
          fieldName={` ${t('common.phoneNumber')}:`}
          fieldValue={props.user.phoneNumber || ''}
          color="primary"
          testId="phone-number-field"
        />
        <Box pr={2} pl={2}>
          <Divider />
        </Box>
        <UserInfoText
          fieldName={` ${t('common.email')}:`}
          fieldValue={props.user.email}
          color="primary"
          testId="email-field"
        />
        <Box pr={2} pl={2}>
          <Divider />
        </Box>
        <UserInfoText
          fieldName={` ${t('common.role')}: `}
          fieldValue={props.user.role}
          color="primary"
          testId="role-field"
        />
        <Box pr={2} pl={isSmallScreen ? 3 : 5} pb={5}>
          <Button
            onClick={handleOpenChangePasswordDialog}
            color="primaryLight"
            variant="inline"
            data-testid="change-password-button"
          >
            {t('user.changePassword')}
          </Button>
        </Box>
      </Card>
    </Box>
  );
}

export default ViewAccount;
