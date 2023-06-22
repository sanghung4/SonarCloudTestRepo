import { PropsWithChildren } from 'react';

import { Box, Checkbox } from '@dialexa/reece-component-library';
import { TFunction } from 'i18next';
import { camelCase } from 'lodash-es';
import { Link as RouterLink } from 'react-router-dom';
import { CellProps, Column, Row } from 'react-table';

import { Invoice } from 'generated/graphql';
import { PdfIcon } from 'icons';
import { MAX_ROW_COUNT, sortAge } from 'Invoices/util';
import { format } from 'utils/currency';
import { sortDate } from 'utils/tableUtils';
import { formatDate, getDateDifferenceInDays } from 'utils/dates';
import {
  TruncatedCell,
  TruncatedCellWithCentralEllipsis
} from 'utils/tableUtils';

const maxInvoiceAge = 360;

/**
 * Types
 */
export type InvoicesCellProps = PropsWithChildren<CellProps<Invoice, string>>;

type GetColumnDefProps = {
  isMincron: boolean;
  search: string;
  t: TFunction;
};

type InvoiceLinkProp = {
  value: string;
  search: string;
  link: string;
};

/**
 * Main Columns Config
 */
export const eclipseColumns = ({ search, t }: GetColumnDefProps) =>
  [
    // 0 - [Checkbox]
    {
      id: 'selection',
      Cell: selectionCell,
      width: 50
    },
    // 1 - PDF
    {
      accessor: 'invoiceUrl',
      width: 55,
      Header: 'PDF',
      disableGlobalFilter: true,
      Cell: pdfIconCell
    },
    // 2 - Invoice Number
    {
      accessor: 'invoiceNumber',
      Header: t('invoices.invoiceNumber') as string,
      Cell: invoiceNumberCell(search)
    },
    // 3 - Status
    {
      accessor: 'status',
      width: 100,
      Header: t('common.status') as string,
      disableGlobalFilter: true,
      Cell: statusCell(t)
    },
    // 4 - Customer PO
    {
      accessor: 'customerPo',
      width: 150,
      Header: t('common.poNumber') as string,
      Cell: TruncatedCellWithCentralEllipsis
    },
    // 5 - Job Number
    {
      accessor: 'jobNumber',
      width: 100,
      Header: t('invoices.jobNumber') as string
    },
    // 6 - Job Name
    {
      accessor: 'jobName',
      width: 150,
      Header: t('invoices.jobName') as string,
      Cell: TruncatedCell
    },
    // 7 - Invoice Date
    {
      accessor: 'invoiceDate',
      Header: t('invoices.invoiceDate') as string,
      width: 120,
      sortType: sortDate
    },
    // 8 - Original Amount
    {
      accessor: 'originalAmt',
      Header: t('invoices.originalAmt') as string,
      width: 120,
      Cell: currencyCell,
      sortType: sortInvoiceCurrency
    },
    // 9 - Open Balance
    {
      accessor: 'openBalance',
      Header: t('invoices.openBalance') as string,
      width: 100,
      Cell: currencyCell,
      sortType: sortInvoiceCurrency
    },
    // 10 - Invoice Age
    {
      accessor: 'age',
      id: 'age',
      Header: t('invoices.age') as string,
      width: 100,
      Cell: ageCell(t),
      sortType: sortAge
    }
  ] as Column<Invoice>[];

export const mincronColumns = ({ search, t }: GetColumnDefProps) =>
  [
    // 0 - Invoice Number
    {
      accessor: 'invoiceNumber',
      width: 100,
      Header: t('invoices.invoiceNumber') as string,
      Cell: invoiceNumberCellForMincron(search)
    },
    // 1 - Status
    {
      accessor: 'status',
      width: 100,
      Header: t('common.status') as string,
      disableGlobalFilter: true,
      Cell: statusCell(t)
    },
    // 2 - Customer PO
    {
      accessor: 'customerPo',
      width: 150,
      Header: t('common.poNumber') as string,
      Cell: TruncatedCellWithCentralEllipsis
    },
    // 3 - Job Number
    {
      accessor: 'jobNumber',
      width: 100,
      Header: t('invoices.jobNumber') as string
    },
    // 4 - Job Name
    {
      accessor: 'jobName',
      width: 150,
      Header: t('invoices.jobName') as string,
      Cell: TruncatedCell
    },
    // 5 - Invoice Date
    {
      accessor: 'invoiceDate',
      width: 120,
      Header: t('invoices.invoiceDate') as string,
      sortType: sortDate
    },
    // 6 - Original Amount
    {
      accessor: 'originalAmt',
      width: 120,
      Header: t('invoices.originalAmt') as string,
      Cell: currencyCell,
      sortType: sortInvoiceCurrency
    },
    // 7 - Open Balance
    {
      accessor: 'openBalance',
      width: 100,
      Header: t('invoices.openBalance') as string,
      Cell: currencyCell,
      sortType: sortInvoiceCurrency
    },
    // 8 - Invoice Age
    {
      accessor: 'age',
      id: 'age',
      width: 100,
      Header: t('invoices.age') as string,
      Cell: ageCell(t),
      sortType: sortAge
    }
  ] as Column<Invoice>[];

/**
 * Main Columns Hook <Use this>
 */
export function invoicesColumnsMemo(props: GetColumnDefProps) {
  return props.isMincron ? mincronColumns(props) : eclipseColumns(props);
}

/**
 * Cells
 */
export function selectionCell({ row, selectedFlatRows }: InvoicesCellProps) {
  const invoiceDate = formatDate(row.values['invoiceDate']);
  const today = new Date();

  const daysOld = getDateDifferenceInDays(today, invoiceDate);

  const noCheckbox = daysOld <= 0 || daysOld > maxInvoiceAge;
  if (noCheckbox) {
    return null;
  }
  return (
    <Checkbox
      color="primary"
      size="medium"
      disabled={selectedFlatRows.length === MAX_ROW_COUNT && !row.isSelected}
      {...row.getToggleRowSelectedProps()}
      data-testid={`checkbox-${row.id}`}
    />
  );
}

export function pdfIconCell({ row, value }: InvoicesCellProps) {
  const invoiceDate = formatDate(row.values['invoiceDate']);
  const today = new Date();

  const daysOld = getDateDifferenceInDays(today, invoiceDate);

  const noPdfIcon = daysOld <= 0 || daysOld > maxInvoiceAge || !value;
  const invoicePattern = /^(s|S)(.)*$/gm;

  if (noPdfIcon) {
    return null;
  }
  if (!invoicePattern.test(row.values['invoiceNumber'])) {
    return <PdfIcon height={24} />;
  }
  return (
    <a href={value} target="_blank" rel="noreferrer">
      <PdfIcon height={24} />
    </a>
  );
}

function InvoiceNumberCellLink({ value, search, link }: InvoiceLinkProp) {
  const invoicePattern = /^(s|S)(.)*$/gm;
  const isInvoiceLink = invoicePattern.test(value);
  return isInvoiceLink || link === 'invoice' ? (
    <Box
      component={RouterLink}
      className="invoice-link"
      to={{
        pathname: `/${link}/${value}`,
        state: { fromInvoices: true, search }
      }}
      color="primary02.main"
      style={{
        textDecoration: 'none'
      }}
    >
      {value}
    </Box>
  ) : (
    <>{value}</>
  );
}

export function invoiceNumberCell(search: string) {
  return ({ value }: InvoicesCellProps) => (
    <Box
      className="invoice-link"
      sx={{
        textDecoration: 'none',
        color: 'primary02.main'
      }}
    >
      <InvoiceNumberCellLink value={value} search={search} link="order" />
    </Box>
  );
}

export function invoiceNumberCellForMincron(search: string) {
  return ({ value }: InvoicesCellProps) => (
    <Box
      className="invoice-link"
      color="primary02.main"
      sx={{
        textDecoration: 'none'
      }}
    >
      <InvoiceNumberCellLink value={value} search={search} link="invoice" />
    </Box>
  );
}

export function statusCell(t: TFunction) {
  return ({ value }: InvoicesCellProps) =>
    value === 'Open' ? (
      <Box color="success.main">{t(`invoices.${camelCase(value)}`)}</Box>
    ) : (
      <>{value}</>
    );
}

export function currencyCell({ value }: InvoicesCellProps) {
  return <Box pr={1}>{format(parseFloat(value))}</Box>;
}

export function ageCell(t: TFunction) {
  return ({ value }: InvoicesCellProps) => {
    if (value === 'Past Due') {
      return <Box color="error.main">{t(`invoices.${camelCase(value)}`)}</Box>;
    }
    const otherVales = ['Current', 'Deposit', 'Future', 'Over120'];
    if (otherVales.includes(value)) {
      return <>{t(`invoices.${camelCase(value)}`)}</>;
    }
    return <>{value}</>;
  };
}

export function sortInvoiceCurrency(
  a: Row<Invoice>,
  b: Row<Invoice>,
  id: string
) {
  const pattern = /(,|\$)+/gm; // Match "," or "$" with 1 or more times
  const actualValueA = parseFloat(a.values[id].replace(pattern, ''));
  const actualValueB = parseFloat(b.values[id].replace(pattern, ''));
  return actualValueA > actualValueB ? 1 : -1;
}
