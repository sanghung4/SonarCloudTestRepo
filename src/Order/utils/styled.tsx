import {
  Box,
  Grid,
  GridProps,
  styled,
  Typography,
  TypographyProps
} from '@dialexa/reece-component-library';

/**
 * Line Item
 */
const LineItemContainerComp = styled(Grid)(({ theme }) => ({
  paddingLeft: theme.spacing(2),
  paddingRight: theme.spacing(2),
  [theme.breakpoints.down('md')]: { paddingLeft: 0, paddingRight: 0 },
  '@media print': {
    'page-break-inside': 'avoid !important',
    'page-break-after': 'avoid !important',
    padding: 0,
    margin: 0,
    display: 'flex'
  },
  '@page': {
    size: 'auto',
    paddingTop: 60,
    marginTop: 60
  }
}));
export const LineItemContainer = (prop: GridProps) => (
  <LineItemContainerComp container spacing={2} py={2} {...prop} />
);

export const LineItemGridPricing = styled(Grid)(() => ({
  '@media print': { width: '100%', verticalAlign: 'top' }
}));

export const ActionButtonsGrid = styled(Grid)(() => ({
  '@media print': { padding: '1px !important', margin: '0 !important' }
}));

const LineItemGridProductInfoConp = styled(Grid)(() => ({
  flexDirection: 'column',
  '@media print': { width: '100%', verticalAlign: 'top' }
}));
export const LineItemGridProductInfo = (prop: GridProps) => (
  <LineItemGridProductInfoConp item container {...prop} />
);

export const LineItemGridThumb = styled(Grid)(() => ({
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  '@media print': { justifyContent: 'flex-start', width: '100%' }
}));

const LineItemSubGridComp = styled(Grid)(() => ({
  '@media print': { width: '25%', verticalAlign: 'top' }
}));
export const LineItemSubGrid = (prop: GridProps) => (
  <LineItemSubGridComp item xs={3} md {...prop} />
);

export const LineItemThumbContainer = styled(Box)(() => ({
  width: 80,
  height: 80,
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  '@media print': { justifyContent: 'top', imageSize: 'small' }
}));

const LineItemTextComp = styled(Typography)(({ theme }) => ({
  textAlign: 'center',
  display: 'flex',
  justifyContent: 'center',
  paddingTop: theme.spacing(2.75),
  [theme.breakpoints.down('md')]: { paddingTop: 0 }
}));
export const LineItemText = (prop: TypographyProps) => (
  <LineItemTextComp variant="subtitle2" {...prop} />
);
export const LineItemTitleText = (prop: TypographyProps) => (
  <Typography color="primary" variant="subtitle2" align="center" {...prop} />
);

export const ChargeIconContainer = styled(Box)(({ theme }) => ({
  display: 'flex',
  height: theme.spacing(8),
  width: theme.spacing(8),
  justifyContent: 'center',
  alignItems: 'center',
  bgcolor: 'lightestGray.main'
}));
