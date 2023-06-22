import { useMemo } from 'react';

import {
  AlertColor,
  DateRange as DateRangeType
} from '@dialexa/reece-component-library';
import { TFunction } from 'i18next';

import {
  Invoice,
  InvoicesQuery,
  InvoicesUrlQuery,
  useInvoicesQuery,
  useInvoicesUrlLazyQuery
} from 'generated/graphql';
import { SelectedAccounts } from 'providers/SelectedAccountsProvider';

/**
 * Types
 */
type MessageOptions = { variant?: AlertColor };
type PushAlertFunction = (message: string, options?: MessageOptions) => void;

type UseInvoicesDataProps = {
  appliedRange: DateRangeType;
  bucket: string;
  invoiceStatus: string;
  pushAlert: PushAlertFunction;
  selectedAccounts: SelectedAccounts;
  t: TFunction;
};

export type TableDataMemoProps = {
  appliedRange: DateRangeType;
  bucket: string;
  invoiceStatus: string;
  data: InvoicesQuery | undefined;
};

/**
 * Main Data Hook
 */
export function useInvoicesData({
  appliedRange,
  bucket,
  invoiceStatus,
  pushAlert,
  selectedAccounts,
  t
}: UseInvoicesDataProps) {
  const { billTo, erpSystemName } = selectedAccounts;

  // - Main Data -
  const { data, loading } = useInvoicesQuery({
    skip: !billTo?.erpAccountId,
    variables: {
      accountId: billTo?.erpAccountId ?? '',
      erpName: erpSystemName ?? ''
    },
    onError: onInvoiceDataError(pushAlert, t)
  });

  // - URL Data -
  const [getInvoicesUrl, { data: invoicesQuery, loading: invoicesUrlLoading }] =
    useInvoicesUrlLazyQuery({ onCompleted: openInvoiceUrl });

  // - Cached Data for Table (from main data) -
  const tableData = useMemo(
    () => tableDataMemo({ appliedRange, bucket, invoiceStatus, data }),
    [appliedRange, bucket, invoiceStatus, data]
  );

  // - Open Invoice URL Effect -
  //useEffect(openInvoiceUrl, [invoicesQuery]);

  return {
    data,
    getInvoicesUrl,
    invoicesQuery,
    invoicesUrlLoading,
    loading,
    tableData
  };
}

/**
 * Defs
 */
// This is where the filters are applied
export function tableDataMemo({
  appliedRange,
  bucket,
  invoiceStatus,
  data
}: TableDataMemoProps) {
  const { from, to } = appliedRange;
  return (data?.invoices.invoices
    .filter((i) =>
      to || from
        ? (to ? new Date(i.invoiceDate) <= to! : true) &&
          (from ? new Date(i.invoiceDate) >= from! : true)
        : true
    )
    .filter((i) => {
      if (bucket !== 'Total') {
        if (bucket === 'TotalPastDue') {
          return (
            i.age === '31-60' ||
            i.age === '61-90' ||
            i.age === '91-120' ||
            i.age === 'Over120'
          );
        }
        return i.age === bucket;
      }
      return true;
    })
    .filter((i) => {
      const rowStatus = i?.status?.toLowerCase();
      const invStatus = invoiceStatus?.toLowerCase();
      return invoiceStatus !== 'All' ? rowStatus === invStatus : true;
    }) || []) as Invoice[];
}

// When the main invoice data failed to load
export function onInvoiceDataError(pushAlert: PushAlertFunction, t: TFunction) {
  return () => pushAlert(t('invoices.invoiceError'), { variant: 'error' });
}

// Open invoiced URL
export function openInvoiceUrl(data?: InvoicesUrlQuery) {
  if (!data?.invoicesUrl) {
    return;
  }
  window.open(data?.invoicesUrl, '_blank');
}
