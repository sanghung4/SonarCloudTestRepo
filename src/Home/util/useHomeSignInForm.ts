import { useContext, useState } from 'react';

import { useSnackbar } from '@dialexa/reece-component-library';
import { useFormik } from 'formik';
import { useTranslation } from 'react-i18next';
import { useHistory } from 'react-router-dom';
import * as Yup from 'yup';

import { AuthContext } from 'AuthProvider';
import {
  InvitedUserEmailSentQuery,
  useInvitedUserEmailSentLazyQuery,
  useResendLegacyInviteEmailMutation
} from 'generated/graphql';

const initialValues = { username: '', password: '' };

export default function useHomeSignInForm() {
  /**
   * Custom Hooks
   */
  const { pushAlert } = useSnackbar();
  const { t } = useTranslation();
  const history = useHistory();

  /**
   * Context
   */
  const { login } = useContext(AuthContext);

  /**
   * State
   */
  const [email, setEmail] = useState('');

  /**
   * Queries
   */
  const [isInviteSentQuery] = useInvitedUserEmailSentLazyQuery({
    onCompleted,
    onError
  });
  const [sendInviteEmail] = useResendLegacyInviteEmailMutation();

  /**
   * Query defs
   */
  function onCompleted(data: InvitedUserEmailSentQuery) {
    if (!data.invitedUserEmailSent) {
      sendInviteEmail({ variables: { legacyUserEmail: email } });
    }
    history.replace({ pathname: '/max-welcome', state: { email } });
  }
  function onError() {
    pushAlert(t('validation.incorrectCreds'), { variant: 'error' });
  }

  /**
   * Form
   */
  const formik = useFormik({
    initialValues,
    validationSchema: Yup.object({
      username: Yup.string()
        .email(t('validation.emailInvalid'))
        .required(t('validation.emailRequired')),
      password: Yup.string().required(t('validation.passwordRequired'))
    }),
    onSubmit
  });

  /**
   * Form def
   */
  async function onSubmit(values: typeof initialValues) {
    try {
      if (login) {
        await login(values);
      }
    } catch {
      try {
        setEmail(values.username);
        await login!(values);
      } catch {
        try {
          isInviteSentQuery({ variables: { email: values.username } });
        } catch {
          pushAlert(t('validation.incorrectCreds'), { variant: 'error' });
        }
      }
    }
  }

  /**
   * Output
   */
  return { email, formik, onCompleted, onError, onSubmit };
}
