import { ReactNode } from 'react';

import { DataMap, WrapperProps } from '@reece/global-types';

export type TemplateObj = Record<string, unknown>;

export type TableProps<TData extends object = TemplateObj> = {
  className?: string;
  customHeaderClassName?: string;
  'data-testid'?: string;
  loading?: boolean;
  noResultsMessage?: string;
  table: TableInstance<TData>;
  showItemCount?: boolean;
  itemCount?: number;
  showPagination?: boolean;
  currentPage?: number;
  pages?: number;
  onPageChange?: (page: number) => void;
};

export type TableInstance<TData extends object = TemplateObj> = {
  data: TData[];
  columns: string[];
  config: DataMap<{
    header?: (h: string) => ReactNode;
    cell: (value: TData) => ReactNode;
  }>;
};

export type DefaultCellProps = WrapperProps & {
  className?: string;
};

export type PaginationProps = {
  disabled?: boolean;
  onChange?: (page: number) => void;
  currentPage?: number;
  totalPages?: number;
  'data-testid'?: string;
};
