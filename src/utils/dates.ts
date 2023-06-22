import { format } from 'date-fns';
import { format as formatTz } from 'date-fns-tz';
import { Row } from 'react-table';

type CompareInput = Row | Date | string;

function createDate(input: CompareInput, columnId?: string): Date {
  if (input instanceof Date) return input;

  return new Date(typeof input === 'string' ? input : input.values[columnId!]);
}

function compareDates(
  inputA: CompareInput,
  inputB: CompareInput,
  columnId?: string,
  desc?: boolean
) {
  const compared =
    createDate(inputA, columnId) < createDate(inputB, columnId) ? 1 : -1;

  return desc ? compared : !compared || 1;
}

function formatDate(dateString?: string | Date, pattern: string = 'P') {
  if (!dateString) {
    return '';
  }
  try {
    const date = new Date(dateString);
    return format(date, pattern);
  } catch {
    return '';
  }
}

function formatDateTimeZone(dateString: string | Date, pattern: string = 'P') {
  if (!dateString) return '';
  const date = new Date(dateString);
  return formatTz(date, pattern);
}

function getDateDifferenceInDays(
  inputA: CompareInput,
  inputB: CompareInput,
  columnId?: string
) {
  const differenceSec =
    createDate(inputA, columnId).getTime() -
    createDate(inputB, columnId).getTime();

  const difference = Math.floor(differenceSec / (1000 * 60 * 60 * 24));

  return difference;
}

export const timestamp = new Date().toISOString();

export {
  compareDates,
  formatDate,
  formatDateTimeZone,
  getDateDifferenceInDays
};
