import {
  alpha,
  Grid,
  lighten,
  styled,
  SxProps,
  Theme
} from '@dialexa/reece-component-library';
import { Dispatch } from 'react';

export function tableSx(theme: Theme) {
  return {
    '& .MuiTableBody-root': {
      '& .MuiTableRow-root:nth-child(even)': {
        bgcolor: 'lightestGray.main',
        '&:hover': {
          bgcolor: alpha(theme.palette.primary02.main, 0.1)
        }
      },
      '& .MuiTableCell-root': {
        py: 2,
        px: 2.5,
        border: 0,
        height: theme.spacing(6),
        position: 'relative'
      }
    },
    '& .MuiCheckbox-root': {
      p: 0,
      position: 'relative',
      top: theme.spacing(-0.5)
    }
  };
}
export function tableHeaderCellGridAlign(align?: 'left' | 'right' | 'center') {
  return align === 'right' ? 'flex-end' : 'flex-start';
}
export function tableCellSx(
  group: boolean,
  expand: boolean,
  onRowClick?: Dispatch<any>
) {
  return [
    group && {
      bgcolor: (theme) => lighten(theme.palette.primary02.main, 0.9),
      '& .MuiTableCell-root': {
        fontSize: '0.8rem',
        py: 1.5,
        px: 2.5
      }
    },
    expand && {
      bgcolor: (theme) => lighten(theme.palette.primary02.main, 0.85)
    },
    onRowClick !== undefined && {
      cursor: 'pointer'
    }
  ] as SxProps<Theme>;
}

export const FilterActionButtonContainer = styled(Grid)(({ theme }) => ({
  marginTop: 35,
  [theme.breakpoints.down('md')]: {
    marginTop: 0
  }
}));

export const ResultsInfoContainer = styled(Grid)(({ theme }) => ({
  marginTop: 44,
  [theme.breakpoints.down('md')]: {
    marginTop: 0
  }
}));
