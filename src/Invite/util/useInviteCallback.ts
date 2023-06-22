import { useCallback, useEffect } from 'react';

import { useSnackbar } from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { useHistory } from 'react-router-dom';

import {
  InviteUserInput,
  useInviteUserMutation,
  useAccountErpIdLazyQuery,
  ErpSystemEnum
} from 'generated/graphql';
import { InviteUserFormInput } from 'Invite';
import { useDomainInfo } from 'hooks/useDomainInfo';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';

export default function useInviteCallback() {
  /**
   * Custom hooks
   */
  const history = useHistory();
  const { pushAlert } = useSnackbar();
  const { t } = useTranslation();
  const { brand } = useDomainInfo();

  /**
   * Data
   */
  const [inviteUser, { loading: inviteUserLoading }] = useInviteUserMutation();
  const [getErpAccount, { data: erpAccountData }] = useAccountErpIdLazyQuery();

  /**
   * Context
   */
  const { selectedAccounts } = useSelectedAccountsContext();

  /**
   * Callbacks
   */
  const handleInviteUser = useCallback(handleInviteUserCb, [
    erpAccountData,
    history,
    inviteUser,
    pushAlert,
    selectedAccounts,
    t
  ]);

  /**
   * Effect
   */
  useEffect(getErpAccountEffect, [getErpAccount, selectedAccounts, brand]);

  /**
   * Callback def
   */
  async function handleInviteUserCb(invite: Omit<InviteUserFormInput, 'erp'>) {
    if (erpAccountData) {
      const inviteUserInput: InviteUserInput = {
        ...invite,
        billToAccountId: selectedAccounts?.billTo?.id ?? '',
        erpAccountId: erpAccountData.account[0]?.erpAccountId ?? '',
        erp: selectedAccounts?.erpSystemName ?? ErpSystemEnum.Eclipse
      };

      try {
        await inviteUser({ variables: { inviteUserInput } });
        pushAlert(t('user.userInviteSuccess'), { variant: 'success' });
        history.push('/user-management');
      } catch (error) {
        try {
          pushAlert(JSON.parse((error as any).message).error, {
            variant: 'error'
          });
        } catch {
          console.error('Could not parse error response');
          pushAlert(t('user.userInviteError'), { variant: 'error' });
        }
      }
    }
  }

  /**
   * Effect def
   */
  function getErpAccountEffect() {
    const accountId = selectedAccounts?.billTo?.id ?? '';
    if (accountId) {
      getErpAccount({ variables: { accountId, brand } });
    }
  }

  /**
   * Output
   */
  return { inviteUserLoading, handleInviteUser };
}
