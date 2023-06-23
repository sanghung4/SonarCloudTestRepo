import { Link } from 'react-router-dom';

import { Customer } from 'api/customer.api';
import { DefaultCell, defaultHeader, TableInstance } from 'components/Table';
import { ReactComponent as TrashIcon } from 'resources/icons/trash.svg';

const columns = [
  'Company Name',
  'Bill-to #',
  'Home Branch',
  'Last Updated',
  'Delete'
];

export function customerListTableConfig(data?: Customer[]) {
  return {
    data: data ?? [],
    columns,
    config: {
      [columns[0]]: {
        header: defaultHeader('uppercase'),
        cell: ({ name, id }) => (
          <Link to={`/customer/detail/${id}`}>
            <DefaultCell>{name}</DefaultCell>
          </Link>
        )
      },
      [columns[1]]: {
        header: defaultHeader('uppercase'),
        cell: ({ customerId }) => <DefaultCell>{customerId}</DefaultCell>
      },
      [columns[2]]: {
        header: defaultHeader('uppercase'),
        cell: ({ branchId }) => <DefaultCell>{branchId}</DefaultCell>
      },
      [columns[3]]: {
        header: defaultHeader('uppercase'),
        cell: ({ lastUpdate }) => (
          <DefaultCell>
            {lastUpdate
              ? new Date(lastUpdate).toLocaleDateString('en-us')
              : '--'}
          </DefaultCell>
        )
      },
      [columns[4]]: {
        header: defaultHeader('uppercase text-center'),
        cell: ({ id }) => (
          <DefaultCell className="flex justify-center">
            <button
              type="button"
              disabled
              data-testid={`customer-list-delete-${id}`}
              className="text-gray-400"
            >
              <TrashIcon />
            </button>
          </DefaultCell>
        )
      }
    }
  } as TableInstance<Customer>;
}
