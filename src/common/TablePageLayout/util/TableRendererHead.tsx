import {
  Box,
  Grid,
  TableCell,
  TableHead,
  TableRow,
  useScreenSize
} from '@dialexa/reece-component-library';

import { TableRendererProps } from 'common/TablePageLayout/TableRenderer';
import { tableHeaderCellGridAlign } from 'common/TablePageLayout/util';
import {
  SortingAscIcon,
  SortingDescIcon,
  SortingContractsNoneIcon
} from 'icons';

export function TableRendererHead<TData extends object>(
  props: TableRendererProps<TData>
) {
  /**
   * Props
   */
  const {
    customCellProps,
    customHeaderProps,
    tableInstance: { columns, headerGroups },
    testId
  } = props;
  /**
   * Custom hooks
   */
  const { isSmallScreen } = useScreenSize();

  /**
   * Render
   */
  return (
    <TableHead sx={customHeaderProps}>
      {headerGroups.map((headerGroup) => (
        <TableRow {...headerGroup.getHeaderGroupProps()}>
          {headerGroup.headers.map((column, i) => {
            const {
              align,
              getHeaderProps,
              getSortByToggleProps,
              isSorted,
              isSortedDesc,
              render
            } = column;
            return (
              <TableCell
                {...getHeaderProps(getSortByToggleProps())}
                align={align}
              >
                <Grid
                  sx={customCellProps}
                  container
                  wrap="nowrap"
                  justifyContent={tableHeaderCellGridAlign(align)}
                  alignItems="center"
                  spacing={isSmallScreen ? 1 : 0}
                >
                  <Grid item xs="auto" zeroMinWidth>
                    <Box display="block">{render('Header')}</Box>
                  </Grid>
                  {isSorted ? (
                    <Grid item xs={1}>
                      <Box
                        ml={1}
                        component={
                          isSortedDesc ? SortingAscIcon : SortingDescIcon
                        }
                        data-testid={
                          isSortedDesc
                            ? `${columns[i].id}-ascending`
                            : `${columns[i].id}-descending`
                        }
                      />
                    </Grid>
                  ) : (
                    testId === 'contracts-table' && (
                      <Box ml={1} data-testid={`${columns[i].id}-sorting-off`}>
                        <SortingContractsNoneIcon />
                      </Box>
                    )
                  )}
                </Grid>
              </TableCell>
            );
          })}
        </TableRow>
      ))}
    </TableHead>
  );
}
