import clsx from 'clsx';

import { DefaultCellProps } from 'components/Table';

export function defaultHeader(className?: string) {
  return (v: string) => (
    <DefaultCell className={clsx('uppercase, font-medium', className)}>
      {v}
    </DefaultCell>
  );
}

export const DefaultCell = ({ className, children }: DefaultCellProps) => (
  <div className={clsx('px-4 py-3 text-xs font-normal', className)}>
    {children}
  </div>
);

export const paginationInputSizeConfig = {
  offset: 22,
  gap: 10
};
export const paginationInputSize = (length: number) => {
  const { gap, offset } = paginationInputSizeConfig;
  return (length || 1) * gap + offset;
};
