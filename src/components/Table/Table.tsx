import clsx from 'clsx';

import {
  Pagination,
  TableProps,
  TableRendererBodyLoading,
  TemplateObj
} from 'components/Table';

/**
 * Basic table renderer (No pagination)
 */
function Table<TData extends object = TemplateObj>(props: TableProps<TData>) {
  /**
   * Props
   */
  const testId = props['data-testid'];
  const { data, columns, config } = props.table;

  /**
   * Render
   */
  return (
    <div className={props.className}>
      <table
        className={clsx('rounded overflow-hidden w-full')}
        data-testid={testId}
      >
        {/* HEAD */}
        <thead
          className={clsx(
            'bg-primary-1-90 text-white',
            props.customHeaderClassName
          )}
        >
          <tr>
            {columns.map((column, i) => (
              <th
                className="text-left"
                key={`thead-group-${i}`}
                data-testid={`${testId}-head-${column as string}`}
              >
                {config?.[column]?.header?.(column) ?? String(column)}
              </th>
            ))}
          </tr>
        </thead>
        {/* BODY */}
        {props.loading || !data.length ? (
          <TableRendererBodyLoading {...props} />
        ) : (
          <>
            <tbody className="[&>*:nth-child(odd)]:bg-white [&>*:nth-child(even)]:bg-secondary-4-60">
              {data.map((row, i) => (
                <tr
                  className="text-base text-primary-3-100"
                  key={`row-${i}`}
                  data-testid={`${testId}-tr-${i}`}
                >
                  {columns.map((column) => (
                    <td
                      key={`${column.toString()}-${i}`}
                      data-testid={`${testId}-${column.toString()}-${i}`}
                    >
                      {config?.[column]?.cell?.(row) ?? ''}
                    </td>
                  ))}
                </tr>
              ))}
            </tbody>
          </>
        )}
      </table>
      {Boolean(props.showItemCount || props.showPagination) && (
        <div className="flex gap-4 px-4 py-6">
          <span
            className="text-secondary-2-100 text-base grow"
            data-testid={`${testId}-count`}
          >
            {Boolean(props.showItemCount) &&
              `${(props.itemCount ?? 0).toLocaleString()} items`}
          </span>
          <span>
            {Boolean(props.showPagination) && (
              <Pagination
                disabled={props.loading}
                onChange={props.onPageChange}
                currentPage={props.currentPage}
                totalPages={props.pages}
                data-testid={`${testId}-pagination`}
              />
            )}
          </span>
        </div>
      )}
    </div>
  );
}

export default Table;
