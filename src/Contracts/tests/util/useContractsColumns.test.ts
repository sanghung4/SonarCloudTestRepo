import { sortDate } from 'Contracts/util/useContractsColumns';
import { Contract } from 'generated/graphql';
import { mockRow as mockRowFn } from 'test-utils/mockTableInstance';

const mockRow = mockRowFn<Contract>({}, {}, {});
const mockId = 'test';

describe('Contracts - util/useContractsColumns', () => {
  it('expect `sortDate` to return 1 with blank data', () => {
    const a = { ...mockRow };
    const b = { ...mockRow };
    const result = sortDate()(a, b, '');
    expect(result).toBe(1);
  });

  it('expect `sortDate` to return 1 when only B has value', () => {
    const a = { ...mockRow, values: { [mockId]: '' } };
    const b = { ...mockRow, values: { [mockId]: 'test' } };
    const result = sortDate()(a, b, mockId);
    expect(result).toBe(1);
  });

  it('expect `sortDate` to return -1 when only A has value', () => {
    const a = { ...mockRow, values: { [mockId]: 'test' } };
    const b = { ...mockRow, values: { [mockId]: '' } };
    const result = sortDate()(a, b, mockId);
    expect(result).toBe(-1);
  });

  it('expect `sortDate` to return -1 when A and B have no value', () => {
    const a = { ...mockRow, values: { [mockId]: '' } };
    const b = { ...mockRow, values: { [mockId]: '' } };
    const result = sortDate()(a, b, mockId);
    expect(result).toBe(-1);
  });
});
