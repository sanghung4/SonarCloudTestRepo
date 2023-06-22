import {
  Box,
  Grid,
  styled,
  Typography
} from '@dialexa/reece-component-library';

/**
 * PreviouslyPurchasedProductsLineItem
 */
export const PPPLineItemContainer = styled(Grid)(({ theme }) => ({
  flexWrap: 'nowrap',
  padding: theme.spacing(4, 6),
  [theme.breakpoints.down('md')]: {
    flexWrap: 'wrap',
    padding: theme.spacing(4, 0)
  }
}));

export const PPPLineItemLastPurchaseDate = styled(Box)(({ theme }) => ({
  fontWeight: 'fontWeightMedium',
  color: theme.palette.primary02.main,
  fontSize: 16,
  marginLeft: 4,
  marginTop: 8
}));

export const PPPLineItemAtBranch = styled(Typography)(({ theme }) => ({
  color: theme.palette.primary02.main,
  fontSize: '0.875rem',
  lineHeight: '1.375rem',
  fontWeight: 400,
  marginLeft: 8,
  overflow: 'hidden',
  whiteSpace: 'nowrap',
  width: '35%',
  textDecoration: 'underline'
}));
