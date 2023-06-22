import {
  Divider,
  Table,
  TableBody,
  TableContainer,
  SxProps,
  Theme
} from '@dialexa/reece-component-library';

import { TableInstance } from 'react-table';

import {
  tableSx,
  TableRendererBodyLoading,
  TableRendererBodyRow,
  TableRendererFooter,
  TableRendererHead
} from 'common/TablePageLayout/util';

export type TableRendererProps<TData extends object = {}> = {
  loading?: boolean;
  noResultsMessage?: string;
  resultsCount?: number;
  resultsCountText?: string;
  tableInstance: TableInstance<TData>;
  testId: string;
  hasGroups?: boolean;
  onRowClick?: (row: TData) => void;
  customHeaderProps?: SxProps<Theme>;
  customCellProps?: SxProps<Theme>;
  isWaterworks?: boolean;
  noResultsContactMessage?: string;
  noResultsContactBranch?: string;
  primaryKey: string;
};

/**
 * This expects a table instance from react table to be passed to it.
 * Requires pagination to be included.
 */
function TableTemplate<TData extends object = {}>(
  props: TableRendererProps<TData>
) {
  return (
    <>
      <TableContainer sx={{ p: '0 !important' }}>
        <Table
          {...props.tableInstance.getTableProps()}
          data-testid={props.testId}
          sx={tableSx}
        >
          <TableRendererHead {...props} />
          {props.loading || props.resultsCount === 0 ? (
            <TableRendererBodyLoading {...props} />
          ) : (
            <TableBody {...props.tableInstance.getTableBodyProps()}>
              {props.tableInstance.page.map((row, rowIndex) => {
                props.tableInstance.prepareRow(row);
                return (
                  <TableRendererBodyRow
                    {...props}
                    row={row}
                    rowIndex={rowIndex}
                    key={rowIndex}
                  />
                );
              })}
            </TableBody>
          )}
        </Table>
      </TableContainer>
      <Divider />
      <TableRendererFooter {...props} />
    </>
  );
}

export default TableTemplate;
