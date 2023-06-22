import { Box, TableCell, TableRow } from '@dialexa/reece-component-library';

import { TableRendererProps } from 'common/TablePageLayout/TableRenderer';
import { tableCellSx, uncommonId } from 'common/TablePageLayout/util';
import { TableMinusIcon, TablePlusIcon } from 'icons';
import { Row } from 'react-table';

export type TableBodyRowProp<TData extends object> = {
  row: Row<TData>;
  rowIndex: number;
} & TableRendererProps<TData>;

export function TableRendererBodyRow<TData extends object>(
  props: TableBodyRowProp<TData>
) {
  /**
   * Props
   */
  const {
    hasGroups,
    onRowClick,
    primaryKey,
    row: {
      cells,
      getRowProps,
      getToggleRowExpandedProps,
      isExpanded,
      isGrouped,
      original,
      subRows,
      values
    },
    rowIndex
  } = props;

  /**
   * Render
   */
  return (
    <TableRow
      {...getRowProps()}
      onClick={() => onRowClick && onRowClick(original)}
      hover
      data-testid={`row_${values[primaryKey]}`}
    >
      {cells.map((cell, i) => {
        return (
          <TableCell
            {...cell.getCellProps()}
            align={cell.column.align}
            sx={tableCellSx(
              !isGrouped && !!hasGroups,
              !!isExpanded && !!hasGroups,
              onRowClick
            )}
            data-testid={`${uncommonId(cell.column.id)}-${rowIndex}`}
            key={`cell-${i}`}
          >
            {cell.isGrouped ? (
              subRows.length > 1 ? (
                <Box {...getToggleRowExpandedProps()} component="span" pt={1.5}>
                  {isExpanded ? (
                    <Box
                      component={TableMinusIcon}
                      color="primary02.main"
                      sx={(theme) => ({
                        width: theme.spacing(2),
                        height: theme.spacing(2)
                      })}
                    />
                  ) : (
                    <Box
                      component={TablePlusIcon}
                      color="primary02.main"
                      sx={(theme) => ({
                        width: theme.spacing(2),
                        height: theme.spacing(2)
                      })}
                    />
                  )}
                </Box>
              ) : (
                ''
              )
            ) : (
              cell.render(cell.isAggregated ? 'Aggregated' : 'Cell')
            )}
          </TableCell>
        );
      })}
    </TableRow>
  );
}
