import { useCallback } from 'react';

import { useSnackbar } from '@dialexa/reece-component-library';
import { AuthTransaction } from '@okta/okta-auth-js';
import { useHistory } from 'react-router-dom';

export default function useOnCreatePassword(transaction?: AuthTransaction) {
  /**
   * Custom hooks
   */
  const history = useHistory();
  const { pushAlert } = useSnackbar();

  /**
   * Def
   */
  async function onCreatePasswordCb(newPassword: string) {
    try {
      await transaction?.resetPassword?.({ newPassword });
      history.push('/login');
    } catch (e) {
      pushAlert((e as any).message, { variant: 'error' });
    }
  }

  /**
   * Callbacks
   */
  return useCallback(onCreatePasswordCb, [history, pushAlert, transaction]);
}
