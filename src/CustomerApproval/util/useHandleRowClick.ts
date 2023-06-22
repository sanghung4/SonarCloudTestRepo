import { useCallback } from 'react';

import { useHistory, useLocation } from 'react-router-dom';

import { CoercedUser } from 'CustomerApproval/util/types';
import { UserInfo } from 'User';

export default function useHandleRowClick() {
  /**
   * Custom hooks
   */
  const history = useHistory();
  const { search } = useLocation();

  /**
   * Callbacks
   */
  return useCallback(handleRowClickCb, [history, search]);

  /**
   * Callback defs
   */
  function handleRowClickCb(row: CoercedUser) {
    const userInfo: UserInfo = {
      id: row.id,
      firstName: row.firstName,
      lastName: row.lastName,
      language: 'English',
      email: row.email,
      company: row.companyName,
      phone: row.phoneNumber,
      phoneType: row.phoneType,
      branchId: row.branchId ?? '',
      accountNumber: row.accountNumber ?? ''
    };

    history.push({
      pathname: `/user/${row.id}`,
      state: { selectedUser: userInfo, customerApproval: true, search }
    });

    history.go(0);
  }
}
