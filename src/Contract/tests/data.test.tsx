import { accountMemo } from 'Contract/util/data';
import { accountsMock } from 'Contract/tests/mocks';

describe('Contract Details - Data', () => {
  it('Expect the data memo to filter out Mincron accounts', () => {
    const result = accountMemo(accountsMock);
    expect(result?.companyName).toBe(accountsMock.account![1]?.companyName);
  });
});
