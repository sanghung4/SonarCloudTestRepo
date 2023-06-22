import { subMonths } from 'date-fns';
import { Invoice, InvoicesDocument } from 'generated/graphql';
import { mockRow } from 'test-utils/mockTableInstance';
import { formatDate } from 'utils/dates';

export const success = {
  request: {
    query: InvoicesDocument,
    variables: {
      accountId: '123456',
      erpName: 'ECLIPSE'
    }
  },
  result: {
    data: {
      invoices: {
        bucketNinety: 0.0,
        bucketOneTwenty: 0.0,
        bucketSixty: 0.0,
        bucketThirty: 0.0,
        bucketFuture: 0.0,
        currentAmt: 35224.96,
        invoices: [
          {
            age: 'Future',
            customerPo: '109687',
            invoiceDate: formatDate(subMonths(new Date(), 1)),
            dueDate: '03/01/2022',
            invoiceNumber: 'S110227972.001',
            openBalance: '12.39',
            originalAmt: '12.39',
            status: 'Open',
            invoiceUrl: 'test.com',
            jobName: null,
            jobNumber: null
          },
          {
            age: 'Current',
            customerPo: '1096872',
            invoiceDate: formatDate(subMonths(new Date(), 1)),
            dueDate: '03/01/2022',
            invoiceNumber: 'S110227975.001',
            openBalance: '12.39',
            originalAmt: '12.39',
            status: 'Open',
            invoiceUrl: 'test.com',
            jobName: null,
            jobNumber: null
          },
          {
            age: 'Over120',
            customerPo: '109687',
            invoiceDate: formatDate(subMonths(new Date(), 1)),
            dueDate: '03/01/2022',
            invoiceNumber: 'S110220294.001',
            openBalance: '12.39',
            originalAmt: '12.39',
            status: 'Open',
            invoiceUrl: 'test.com',
            jobName: null,
            jobNumber: null
          },
          {
            age: '91-120',
            customerPo: '109687',
            invoiceDate: '03/01/2021',
            dueDate: '03/01/2022',
            invoiceNumber: 'S110222345.001',
            openBalance: '12.39',
            originalAmt: '12.39',
            status: 'Open',
            invoiceUrl: 'test.com',
            jobName: null,
            jobNumber: null
          },
          {
            age: 'Deposit',
            customerPo: '109687',
            invoiceDate: '03/01/2021',
            dueDate: '03/01/2022',
            invoiceNumber: 'S110220974.001',
            openBalance: '12.39',
            originalAmt: '12.39',
            status: 'Open',
            invoiceUrl: 'test.com',
            jobName: null,
            jobNumber: null
          },
          {
            age: '31-60',
            customerPo: '109687',
            invoiceDate: '03/01/2021',
            dueDate: '03/01/2022',
            invoiceNumber: 'S110227973.001',
            openBalance: '12.39',
            originalAmt: '12.39',
            status: 'Open',
            invoiceUrl: 'test.com',
            jobName: null,
            jobNumber: null
          }
        ],
        totalAmt: 35232.27,
        totalPastDue: 7.3085938
      }
    }
  }
};

export const successMincron = {
  request: {
    query: InvoicesDocument,
    variables: {
      accountId: '123456',
      erpName: 'MINCRON'
    }
  },
  result: {
    data: {
      invoices: {
        bucketNinety: 0.0,
        bucketOneTwenty: 0.0,
        bucketSixty: 0.0,
        bucketThirty: 0.0,
        bucketFuture: 0.0,
        currentAmt: 35224.96,
        invoices: [
          {
            age: 'Future',
            customerPo: '109687',
            invoiceDate: formatDate(subMonths(new Date(), 1)),
            dueDate: '03/01/2022',
            invoiceNumber: 'S110227972.001',
            openBalance: '12.39',
            originalAmt: '12.39',
            status: 'Open',
            invoiceUrl: 'test.com',
            jobNumber: '6566',
            jobName: 'NEP 7 ASHVELLE'
          },
          {
            age: 'Current',
            customerPo: '109687',
            invoiceDate: formatDate(subMonths(new Date(), 1)),
            dueDate: '03/01/2022',
            invoiceNumber: 'S110227975.001',
            openBalance: '12.39',
            originalAmt: '12.39',
            status: 'Open',
            invoiceUrl: 'test.com',
            jobNumber: '56787',
            jobName: 'WALTER HORN'
          },
          {
            age: 'Over120',
            customerPo: '109687',
            invoiceDate: formatDate(subMonths(new Date(), 1)),
            dueDate: '03/01/2022',
            invoiceNumber: 'S110220294.001',
            openBalance: '12.39',
            originalAmt: '12.39',
            status: 'Closed',
            invoiceUrl: 'test.com',
            jobNumber: 'SHOP',
            jobName: 'SEQUAYESH TR'
          },
          {
            age: '91-120',
            customerPo: '109687',
            invoiceDate: formatDate(subMonths(new Date(), 1)),
            dueDate: '03/01/2022',
            invoiceNumber: 'S110222345.001',
            openBalance: '12.39',
            originalAmt: '12.39',
            status: 'Open',
            invoiceUrl: 'test.com',
            jobNumber: 'RETREAT',
            jobName: 'RETREAT AT ARDEN'
          },
          {
            age: 'Deposit',
            customerPo: '109687',
            invoiceDate: formatDate(subMonths(new Date(), 1)),
            dueDate: '03/01/2022',
            invoiceNumber: 'S110220974.001',
            openBalance: '12.39',
            originalAmt: '12.39',
            status: 'Closed',
            invoiceUrl: 'test.com',
            jobNumber: 'AVLNEP7',
            jobName: 'WALTER HORN'
          },
          {
            age: '31-60',
            customerPo: '109687',
            invoiceDate: '03/01/2021',
            dueDate: '03/01/2022',
            invoiceNumber: 'S110227973.001',
            openBalance: '12.39',
            originalAmt: '12.39',
            status: 'Open',
            invoiceUrl: 'test.com',
            jobNumber: 'AIKENON',
            jobName: 'AIKEN AVL'
          }
        ],
        totalAmt: 35232.27,
        totalPastDue: 7.3085938
      }
    }
  }
};

export const mockTestTableMemo = {
  invoices: {
    bucketNinety: 0.0,
    bucketOneTwenty: 0.0,
    bucketSixty: 0.0,
    bucketThirty: 0.0,
    bucketFuture: 0.0,
    currentAmt: 35224.96,
    invoices: [
      {
        age: 'Future',
        customerPo: '109687',
        invoiceDate: '05/02/2022',
        dueDate: '03/01/2022',
        invoiceNumber: 'S110227972.001',
        openBalance: '12.39',
        originalAmt: '12.39',
        status: 'Open',
        invoiceUrl: 'test.com',
        jobNumber: '6566',
        jobName: 'NEP 7 ASHVELLE'
      },
      {
        age: 'Current',
        customerPo: '109687',
        invoiceDate: '03/01/2021',
        dueDate: '03/01/2022',
        invoiceNumber: 'S110227975.001',
        openBalance: '12.39',
        originalAmt: '12.39',
        status: 'Open',
        invoiceUrl: 'test.com',
        jobNumber: '56787',
        jobName: 'WALTER HORN'
      },
      {
        age: 'Over120',
        customerPo: '109687',
        invoiceDate: '03/01/2021',
        dueDate: '03/01/2022',
        invoiceNumber: 'S110220294.001',
        openBalance: '12.39',
        originalAmt: '12.39',
        status: 'Open',
        invoiceUrl: 'test.com',
        jobNumber: 'SHOP',
        jobName: 'SEQUAYESH TR'
      },
      {
        age: '91-120',
        customerPo: '109687',
        invoiceDate: '03/01/2021',
        dueDate: '03/01/2022',
        invoiceNumber: 'S110222345.001',
        openBalance: '12.39',
        originalAmt: '12.39',
        status: 'Open',
        invoiceUrl: 'test.com',
        jobNumber: 'RETREAT',
        jobName: 'RETREAT AT ARDEN'
      },
      {
        age: 'Deposit',
        customerPo: '109687',
        invoiceDate: '03/01/2021',
        dueDate: '03/01/2022',
        invoiceNumber: 'S110220974.001',
        openBalance: '12.39',
        originalAmt: '12.39',
        status: 'Open',
        invoiceUrl: 'test.com',
        jobNumber: 'AVLNEP7',
        jobName: 'WALTER HORN'
      },
      {
        age: '31-60',
        customerPo: '109687',
        invoiceDate: '03/01/2021',
        dueDate: '03/01/2022',
        invoiceNumber: 'S110227973.001',
        openBalance: '12.39',
        originalAmt: '12.39',
        status: 'Open',
        invoiceUrl: 'test.com',
        jobNumber: 'AIKENON',
        jobName: 'AIKEN AVL'
      }
    ],
    totalAmt: 35232.27,
    totalPastDue: 7.3085938
  }
};

const rowToMock = success.result.data.invoices.invoices[0];
export const mockInvoiceRow = mockRow<Invoice>(rowToMock, rowToMock, {});
