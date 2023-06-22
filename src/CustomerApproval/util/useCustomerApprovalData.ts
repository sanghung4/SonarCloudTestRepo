import { Dispatch, useMemo } from 'react';

import { CoercedUser } from 'CustomerApproval/util/types';
import { useGetAllUnapprovedAccountRequestsQuery } from 'generated/graphql';

export default function useCustomerApprovalData(
  setNumOfResults: Dispatch<number | undefined>
) {
  /**
   * Data
   */
  const { data: unapprovedData, loading } =
    useGetAllUnapprovedAccountRequestsQuery();

  /**
   * Memo
   */
  const data = useMemo(getUsersOrEmptyMemo, [
    setNumOfResults,
    unapprovedData?.allUnapprovedAccountRequests
  ]);

  /**
   * Memos Def
   */
  function getUsersOrEmptyMemo(): CoercedUser[] {
    const tempData =
      unapprovedData?.allUnapprovedAccountRequests as CoercedUser[];
    setNumOfResults(tempData ? tempData.length : 0);
    return tempData ?? [];
  }

  /**
   * Output
   */
  return { data, loading };
}
