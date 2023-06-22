import { RejectionReason } from 'generated/graphql';

export const getRejectionReason = (
  reason: RejectionReason | undefined
): string => {
  switch (reason) {
    case RejectionReason.NotACompanyMember:
      return 'rejectionReasons.notACompanyMember';
    case RejectionReason.NotAuthorized:
      return 'rejectionReasons.notAuthorized';
    case RejectionReason.Other:
      return 'rejectionReasons.other';
    default:
      return 'common.na';
  }
};
