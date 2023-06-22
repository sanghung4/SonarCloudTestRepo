import { useContext } from 'react';

import { Redirect } from 'react-router-dom';

import { AuthContext, UserContextType } from 'AuthProvider';
import Loader from 'common/Loader';

export enum Permission {
  APPROVE_ACCOUNT_USER = 'approve_account_user',
  APPROVE_ALL_USERS = 'approve_all_users',
  APPROVE_CART = 'approve_cart',
  EDIT_LIST = 'edit_list',
  EDIT_PROFILE = 'edit_profile',
  INVITE_USER = 'invite_user',
  MANAGE_PAYMENT_METHODS = 'manage_payment_methods',
  MANAGE_ROLES = 'manage_roles',
  SUBMIT_CART_WITHOUT_APPROVAL = 'submit_cart_without_approval',
  SUBMIT_QUOTE_ORDER = 'submit_quote_order',
  VIEW_INVOICE = 'view_invoice',
  TOGGLE_FEATURES = 'toggle_features',
  REFRESH_CONTACT = 'refresh_contact',
  MANAGE_BRANCHES = 'manage_branches'
}

interface Props {
  permissions: Permission[];
  redirectTo?: string;
  children: JSX.Element;
}

/**
 * @param profile user profile grabbed from useContext(AuthContext)
 * @param permissionsRequired array of permissions user needs to pass the check
 * @returns true if user has the valid permissions
 */
export function checkUserPermission(
  profile: UserContextType['profile'] | undefined,
  permissionsRequired: Permission[]
) {
  return permissionsRequired.every((el) =>
    (profile?.permissions ?? []).includes(el)
  );
}

/**
 * This wrapper checks current user permissions against @param permissions and if the required permissions
 * are met by the user, the child components are rendered. If a @param redirectTo is provided, on failure
 * the user is redirected to the provided url, else if not provided, null is returned blocking the
 * content from the user.
 */
export default function PermissionRequired(props: Props) {
  const { profile } = useContext(AuthContext);

  if (!profile?.userId) {
    return <Loader />;
  }

  return checkUserPermission(profile, props.permissions) ? (
    props.children
  ) : props.redirectTo ? (
    <Redirect to={props.redirectTo} />
  ) : null;
}
