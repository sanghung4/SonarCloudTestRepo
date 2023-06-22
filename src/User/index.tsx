import React, { useContext, useState } from 'react';

import { Container, useScreenSize } from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { useLocation } from 'react-router-dom';

import { AuthContext } from 'AuthProvider';
import EditUser from 'User/EditUser';
import ViewUser from 'User/ViewUser';
import Breadcrumbs from 'common/Breadcrumbs';
import { PhoneType } from 'generated/graphql';

export type UserInfo = {
  firstName: string;
  lastName: string;
  company: string;
  phone: string;
  email: string;
  language: string;
  id: any;
  roleId?: string;
  approverId?: string;
  phoneType: PhoneType;
  createdAt?: string;
  branchId?: string;
  accountNumber?: string;
  contactUpdatedAt?: string;
  contactUpdatedBy?: string;
};

export const standardUserRoles = [
  'Standard Access',
  'Purchase - No Invoices',
  'Purchase with Approval',
  'Invoice Only'
]

type State = {
  selectedUser: UserInfo;
  customerApproval?: boolean;
  search?: string;
};

function User() {
  /**
   * Custom Hooks
   */
  const { profile } = useContext(AuthContext);
  const location = useLocation<State>();
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * State
   */
  const [isEditing, setIsEditing] = useState(false);

  /**
   * Callbacks
   */
  const handleEditClicked = () => setIsEditing(true);
  const handleCancelEditing = () => setIsEditing(false);

  return (
    <>
      {location.state?.customerApproval ? (
        <Breadcrumbs
          config={[
            {
              text: t('common.customerApproval'),
              to: `/customer-approval${location.state?.search ?? ''}`
            }
          ]}
          pageTitle={t('common.userDetails')}
        />
      ) : (
        <Breadcrumbs
          config={[
            {
              text: t('common.userManagement'),
              to: `/user-management${location.state?.search ?? ''}`
            }
          ]}
          pageTitle={t('common.userDetails')}
        />
      )}
      <Container disableGutters={isSmallScreen}>
        {isEditing ? (
          <EditUser
            loggedInId={profile?.userId}
            user={location.state?.selectedUser}
            onCancel={handleCancelEditing}
          />
        ) : (
          <ViewUser
            data-testid="view-user-button"
            user={location.state?.selectedUser}
            onEditClicked={handleEditClicked}
            customerApproval={location.state?.customerApproval ?? false}
          />
        )}
      </Container>
    </>
  );
}

export default User;
