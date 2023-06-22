import { Row } from 'react-table';
import {
  appliedRangeMemo,
  defaultCellValue,
  handleGoToPage,
  handlePage,
  sortByParser,
  sortDate,
  sortDirectionString,
  trimAccessor
} from 'utils/tableUtils';

describe('Orders - util', () => {
  // defaultCellValue
  it('expect `defaultCellValue` to handle normal value', () => {
    const input = 'TEST';
    const output = defaultCellValue({ value: input });
    expect(output).toBe(input);
  });
  it('expect `defaultCellValue` to handle blank value', () => {
    const input = '';
    const output = defaultCellValue({ value: input });
    expect(output).toBe('-');
  });

  // sortByParser
  it('expect `sortByParser` to handle `!TEST`', () => {
    const input = '!TEST';
    const [output] = sortByParser([input]);
    expect(output.desc).toBe(true);
    expect(output.id).toBe('TEST');
  });
  it('expect `sortByParser` to handle `TEST`', () => {
    const input = 'TEST';
    const [output] = sortByParser([input]);
    expect(output.desc).toBe(false);
    expect(output.id).toBe('TEST');
  });

  // handleGoToPage
  it('expect `handleGoToPage` to call go', () => {
    const go = jest.fn();
    handleGoToPage(go)(3);
    expect(go).toBeCalledWith(2);
  });

  // handlePage
  it('expect `handlePage` to handle "3"', () => {
    const input = '3';
    const output = handlePage(input);
    expect(output).toBe(2);
  });
  it('expect `handlePage` to handle "-1"', () => {
    const input = '-1';
    const output = handlePage(input);
    expect(output).toBe(0);
  });
  it('expect `handlePage` to handle "NotANumber"', () => {
    const input = 'NotANumber';
    const output = handlePage(input);
    expect(output).toBe(0);
  });

  // sortDirectionString
  it('Expect `sortDirectionString` to return DESC', () => {
    const output = sortDirectionString(false);
    expect(output).toBe('DESC');
  });
  it('Expect `sortDirectionString` to return ASC', () => {
    const output = sortDirectionString(true);
    expect(output).toBe('ASC');
  });
  it('Expect `sortDirectionString` to return DESC when undefined', () => {
    const output = sortDirectionString();
    expect(output).toBe('DESC');
  });
  it('Expect `sortDirectionString` to return DESC when null', () => {
    const output = sortDirectionString(null);
    expect(output).toBe('DESC');
  });

  // sortDate
  it('Expect `sortDate` to return 0', () => {
    const rowKey = 'day';
    // @ts-ignore
    const row = (date: string) => ({ values: { [rowKey]: date } } as Row);
    const result = sortDate(row('01/01/2021'), row('01/01/2021'), rowKey);
    expect(result).toBe(0);
  });
  it('Expect `sortDate` to return -1', () => {
    const rowKey = 'day';
    // @ts-ignore
    const row = (date: string) => ({ values: { [rowKey]: date } } as Row);
    const result = sortDate(row('01/01/2021'), row('01/01/2022'), rowKey);
    expect(result).toBe(-1);
  });
  it('Expect `sortDate` to return 1', () => {
    const rowKey = 'day';
    // @ts-ignore
    const row = (date: string) => ({ values: { [rowKey]: date } } as Row);
    const result = sortDate(row('01/01/2022'), row('01/01/2021'), rowKey);
    expect(result).toBe(1);
  });

  // appliedRangeMemo
  it('Expect `appliedRangeMemo` to return undefined dates', () => {
    const result = appliedRangeMemo({});
    expect(result.from).toBe(undefined);
    expect(result.to).toBe(undefined);
  });
  it('Expect `appliedRangeMemo` to return correct dates', () => {
    const from = '01/01/2021';
    const to = '01/01/2022';
    const result = appliedRangeMemo({ from, to });
    expect(result.from?.toString()).toBe(new Date(from).toString());
    expect(result.to?.toString()).toBe(new Date(to).toString());
  });

  // trimAccessor
  it('Expect `trimAccessor` to return the same data if it is not a string', () => {
    const mock = { test: 1 };
    const result = trimAccessor<typeof mock>('test')(mock);
    expect(result).toBe(mock.test);
  });
  it('Expect `trimAccessor` to return trimmed string', () => {
    const mock = { test: ' test spaces   ' };
    const result = trimAccessor<typeof mock>('test')(mock);
    expect(result).toBe('test spaces');
  });
});
