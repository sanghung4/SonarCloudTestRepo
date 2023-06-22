import { Dispatch, useContext, useEffect } from 'react';

import { useSnackbar } from '@dialexa/reece-component-library';
import { AuthTransaction } from '@okta/okta-auth-js';

import { AuthContext } from 'AuthProvider';
import { AuthParam } from 'ForgotPassword';
import { useQueryParams } from 'hooks/useSearchParam';

export type UseForgetPasswordEffectProps = {
  setEmail: Dispatch<string>;
  setTransaction: Dispatch<AuthTransaction | undefined>;
  transaction?: AuthTransaction;
};

export default function useForgetPasswordEffect(
  props: UseForgetPasswordEffectProps
) {
  /**
   * Props
   */
  const { setEmail, setTransaction, transaction } = props;

  /**
   * Custom hooks
   */
  const [queryParams, setQueryParams] = useQueryParams<AuthParam>();
  const { pushAlert } = useSnackbar();

  /**
   * Context
   */
  const { oktaAuth } = useContext(AuthContext);

  /**
   * Effects
   */
  // eslint-disable-next-line react-hooks/exhaustive-deps
  useEffect(validateTokenIfItExists, [
    oktaAuth,
    pushAlert,
    queryParams,
    setQueryParams,
    setTransaction,
    transaction
  ]);

  /**
   * Def
   */
  function validateTokenIfItExists() {
    const { recoveryToken } = queryParams;

    if (!recoveryToken || transaction) {
      return;
    }
    const validateToken = async () => {
      try {
        const response = await oktaAuth?.verifyRecoveryToken({ recoveryToken });
        if (response?.status === 'PASSWORD_RESET') {
          setEmail(response?.user?.profile.login);
          setTransaction(response);
        }
      } catch (e) {
        setQueryParams({ ...queryParams, recoveryToken: undefined });
        pushAlert((e as any).message, { variant: 'error' });
      }
    };

    validateToken();
  }
}
