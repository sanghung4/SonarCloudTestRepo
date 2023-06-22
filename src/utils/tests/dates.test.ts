import { compareDates, formatDate } from 'utils/dates';
import {
  Row,
  TableRowProps,
  TableExpandedToggleProps,
  TableToggleRowsSelectedProps
} from 'react-table';

describe('Utils - formatDate', () => {
  test('returns an empty string when no dateString is provided', () => {
    expect(formatDate('')).toMatch('');
  });

  test('formats a string to a date string', () => {
    expect(formatDate('1/1/1970')).toMatch('01/01/1970');
  });

  test('formats a Date to a date string', () => {
    expect(formatDate(new Date('1/1/1970'))).toMatch('01/01/1970');
  });
});

describe('Utils - compareDates', () => {
  test('compares two Row objects', () => {
    const inputA = generateRowDate('01/01/2020');
    const inputB = generateRowDate('01/01/2021');

    expect(compareDates(inputA, inputB, 'date', false)).toEqual(1);
    expect(compareDates(inputA, inputB, 'date', true)).toEqual(1);
    expect(compareDates(inputB, inputA, 'date', false)).toEqual(1);
    expect(compareDates(inputB, inputA, 'date', true)).toEqual(-1);
  });

  test('compares two Date objects', () => {
    const inputA = new Date('01/01/2020');
    const inputB = new Date('01/01/2021');

    expect(compareDates(inputA, inputB, 'date', false)).toEqual(1);
    expect(compareDates(inputA, inputB, 'date', true)).toEqual(1);
    expect(compareDates(inputB, inputA, 'date', false)).toEqual(1);
    expect(compareDates(inputB, inputA, 'date', true)).toEqual(-1);
  });

  test('compares two Date strings', () => {
    const inputA = '01/01/2020';
    const inputB = '01/01/2021';

    expect(compareDates(inputA, inputB, 'date', false)).toEqual(1);
    expect(compareDates(inputA, inputB, 'date', true)).toEqual(1);
    expect(compareDates(inputB, inputA, 'date', false)).toEqual(1);
    expect(compareDates(inputB, inputA, 'date', true)).toEqual(-1);
  });
});

function generateRowDate(date: string): Row {
  return {
    id: '',
    cells: [],
    allCells: [],
    values: {
      date
    },
    getRowProps: () => ({} as TableRowProps),
    index: 0,
    original: {},
    subRows: [],
    state: {},
    setState: () => {},
    isExpanded: false,
    canExpand: false,
    toggleRowExpanded: () => {},
    getToggleRowExpandedProps: () => ({} as TableExpandedToggleProps),
    depth: 0,
    isGrouped: false,
    groupByID: '',
    groupByVal: '',
    leafRows: [],
    isSelected: false,
    isSomeSelected: false,
    toggleRowSelected: () => {},
    getToggleRowSelectedProps: () => ({} as TableToggleRowsSelectedProps)
  };
}
