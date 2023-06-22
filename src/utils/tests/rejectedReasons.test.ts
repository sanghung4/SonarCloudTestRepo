import { RejectionReason } from 'generated/graphql';
import { getRejectionReason } from 'utils/rejectionReasons';

describe('Utils - Rejected Reasons', () => {
  it('Expect `getRejectedReasons` to return correct string for locale string for not a company member', () => {
    const reason = RejectionReason.NotACompanyMember;
    const result = getRejectionReason(reason);
    expect(result).toBe('rejectionReasons.notACompanyMember');
  });

  it('Expect `getRejectedReasons` to return correct string for locale string for not authorized', () => {
    const reason = RejectionReason.NotAuthorized;
    const result = getRejectionReason(reason);
    expect(result).toBe('rejectionReasons.notAuthorized');
  });

  it('Expect `getRejectedReasons` to return correct string for locale string for other', () => {
    const reason = RejectionReason.Other;
    const result = getRejectionReason(reason);
    expect(result).toBe('rejectionReasons.other');
  });

  it('Expect `getRejectedReasons` to return correct string for locale string for undefined', () => {
    const reason = undefined;
    const result = getRejectionReason(reason);
    expect(result).toBe('common.na');
  });
});
