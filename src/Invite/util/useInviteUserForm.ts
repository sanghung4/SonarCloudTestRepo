import { useMemo } from 'react';

import { FormikProps, useFormik } from 'formik';
import { useTranslation } from 'react-i18next';
import * as Yup from 'yup';

import { useGetRolesQuery, useGetApproversQuery } from 'generated/graphql';
import { InviteUserFormValues, InviteUserProps } from 'Invite/InviteUser';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';

const initialValues: InviteUserFormValues = {
  firstName: '',
  lastName: '',
  email: '',
  userRoleId: '',
  approverId: ''
};

export default function useInviteUserForm(props: InviteUserProps) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  /**
   * Context
   */
  const { selectedAccounts } = useSelectedAccountsContext();

  /**
   * Data
   */
  const { data: { roles } = { roles: [] } } = useGetRolesQuery();
  const { data: { approvers } = { approvers: [] } } = useGetApproversQuery({
    variables: { billToAccountId: selectedAccounts?.billTo?.id ?? '' }
  });

  /**
   * Form
   */
  const formik: FormikProps<InviteUserFormValues> = useFormik({
    initialValues,
    validationSchema: Yup.object({
      firstName: Yup.string().required(t('validation.firstNameRequired')),
      lastName: Yup.string().required(t('validation.lastNameRequired')),
      userRoleId: Yup.string().required(t('validation.roleRequired')),
      approverId: Yup.string().when('userRoleId', approverIdSchema),
      email: Yup.string()
        .email(t('validation.emailInvalid'))
        .required(t('validation.emailRequired'))
    }),
    onSubmit
  });

  /**
   * Form defs
   */
  function approverIdSchema(_: string, schema: any) {
    return requiresApprover
      ? schema.required(t('validation.approverRequired'))
      : schema;
  }
  function onSubmit(values: InviteUserFormValues) {
    props.onSubmitInvite(values);
  }

  /**
   * Memo
   */
  const requiresApprover = useMemo(getRequiresApprover, [
    formik.values.userRoleId,
    roles
  ]);

  /**
   * Memo defs
   */
  function getRequiresApprover() {
    const targetName = 'Purchase with Approval';
    const findApprovalRoles = roles.find((r) => r?.name === targetName);
    const approverRequired = findApprovalRoles?.id === formik.values.userRoleId;
    return approverRequired;
  }

  /**
   * Output
   */
  return { approvers, formik, requiresApprover, roles };
}
