import {
  styled,
  TableCell,
  TableContainer
} from '@dialexa/reece-component-library';

export const noOverflowSx = { overflow: 'hidden', textOverflow: 'ellipsis' };

export const TableContainerStyled = styled(TableContainer)(({ theme }) => ({
  '& .MuiTableRow-root:last-child td': {
    border: '0 none'
  },
  '& .MuiTableCell-root': {
    fontSize: theme.typography.pxToRem(12),
    color: theme.palette.primary03.main,
    padding: theme.spacing(2, 0),
    verticalAlign: 'top',
    '&:first-child': {
      paddingRight: theme.spacing(1.5)
    },
    '&:last-child': {
      paddingLeft: theme.spacing(1.5)
    }
  }
}));
export const TableCellLabel = styled(TableCell)({
  fontWeight: 700,
  width: '0.1%',
  whiteSpace: 'nowrap'
});

export const TableCellValue = styled(TableCell)({
  fontWeight: 400
});
