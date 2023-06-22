import { subMonths } from 'date-fns';
import { Invoice, Maybe } from 'generated/graphql';
import { Row, TableInstance } from 'react-table';

export type AgeTuple = typeof AGES;
export type Age = AgeTuple[number];

export const AGES = [
  'Total',
  'Future',
  'Current',
  '31-60',
  '61-90',
  '91-120',
  'Over120',
  'Deposit',
  'TotalPastDue'
] as const;
export const ECLIPSE_BILLTRUST = 'http://morsco.billtrust.com/';
export const MINCRON_BILLTRUST = 'http://fortiline.billtrust.com/';
export const MAX_ROW_COUNT = 15;
export const emptyDateRange = {
  from: undefined,
  to: undefined
};

export const defaultTimeRange = {
  from: subMonths(new Date(), 1),
  to: new Date()
};

export function sortAge(
  rowA: Row<Invoice>,
  rowB: Row<Invoice>,
  columnId: string
) {
  const a = AGES.indexOf(rowA.values[columnId]);
  const b = AGES.indexOf(rowB.values[columnId]);

  return a === b ? 0 : a > b ? 1 : -1;
}

type HandleDownloadClickedCbProps = {
  erpAccountId?: Maybe<string>;
  tableInstance: TableInstance<Invoice>;
  getInvoicesUrl: Function;
};
export function handleDownloadClickedCb({
  erpAccountId,
  getInvoicesUrl,
  tableInstance
}: HandleDownloadClickedCbProps) {
  const invoiceNumbers = tableInstance.selectedFlatRows.map((r) => {
    const row = r.original;
    return row.invoiceNumber;
  });

  getInvoicesUrl({
    variables: {
      accountId: erpAccountId ?? '',
      invoiceNumbers
    }
  });
}
