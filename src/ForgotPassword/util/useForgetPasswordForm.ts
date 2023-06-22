import { Dispatch } from 'react';

import { useFormik } from 'formik';
import { useTranslation } from 'react-i18next';
import * as Yup from 'yup';

import { AuthParam } from 'ForgotPassword';
import { useInvitedUserEmailSentLazyQuery } from 'generated/graphql';
import { useQueryParams } from 'hooks/useSearchParam';

type IsInviteSentQueryFn = ReturnType<
  typeof useInvitedUserEmailSentLazyQuery
>[0];
type UseForgetPasswordForm = {
  isInviteSentQuery: IsInviteSentQueryFn;
  setEmail: Dispatch<string>;
};

export default function useForgetPasswordForm(props: UseForgetPasswordForm) {
  /**
   * Custom Hooks
   */
  const [{ email = '' }] = useQueryParams<AuthParam>();
  const { t } = useTranslation();

  /**
   * Form
   */
  const formik = useFormik({
    initialValues: { email },
    validationSchema: Yup.object({
      email: Yup.string()
        .email(t('validation.emailInvalid'))
        .required(t('validation.emailRequired'))
    }),
    onSubmit: onForgetPasswordFormSubmit(props)
  });

  /**
   * Output
   */
  return formik;
}

// Split off for easier test coverage
export function onForgetPasswordFormSubmit(props: UseForgetPasswordForm) {
  return ({ email }: { email: string }) => {
    props.setEmail(email);
    props.isInviteSentQuery({ variables: { email } });
  };
}
