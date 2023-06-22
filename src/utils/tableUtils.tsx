import { Dispatch } from 'react';
import { Row } from 'react-table';
import { Maybe } from 'generated/graphql';
import { TableInstance } from 'react-table';
import { Box, Tooltip } from '@dialexa/reece-component-library';
import trimSpaces from 'utils/trimSpaces';

export function defaultCellValue({ value }: { value: string }) {
  return value ? value : '-';
}

export function sortByParser(sortBy: string[]) {
  return sortBy.map((s) => ({
    id: s.replace('!', ''),
    desc: s.includes('!')
  }));
}

type PageIndexFn = (pageIndex: number) => number;
export function handleGoToPage(go: Dispatch<number | PageIndexFn>) {
  return (pageNum: number) => go(pageNum - 1);
}

export function handlePage(page: string) {
  const parsed = parseInt(page);
  return parsed && parsed > 0 ? parsed - 1 : 0;
}

export function sortDirectionString(input?: Maybe<boolean>) {
  return input ? 'ASC' : 'DESC';
}

export function sortDate(rowA: Row, rowB: Row, columnId: string) {
  const a = new Date(rowA.values[columnId]);
  const b = new Date(rowB.values[columnId]);
  return Number(a > b) - Number(a < b);
}

export function appliedRangeMemo({ from, to }: { from?: string; to?: string }) {
  return {
    from: from ? new Date(from) : undefined,
    to: to ? new Date(to) : undefined
  };
}

export function trimAccessor<T extends Record<string, any>>(kind: keyof T) {
  return (obj: T) => {
    const data = obj[kind];
    if (typeof data === 'string') {
      return data.trim();
    }
    return data;
  };
}

export function TruncatedCell({ value }: TableInstance) {
  return (
    <Tooltip title={value} enterTouchDelay={0}>
      <Box whiteSpace="nowrap" overflow="hidden" textOverflow="ellipsis">
        {value}
      </Box>
    </Tooltip>
  );
}

export function TruncatedCellWithCentralEllipsis({ value }: TableInstance) {
  return (
    <Tooltip title={value} enterTouchDelay={0}>
      <Box whiteSpace="nowrap">
        {value && trimSpaces(value).length > 13
          ? trimSpaces(value).substring(0, 4) +
            '...' +
            trimSpaces(value).slice(-7)
          : trimSpaces(value)}
      </Box>
    </Tooltip>
  );
}
