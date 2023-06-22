import {
  Box,
  Button,
  Grid,
  List,
  ListItemText,
  styled,
  Typography
} from '@dialexa/reece-component-library';

/**
 * INDEX (Product)
 */
export const BranchesButtonContainer = styled(Grid)(({ theme }) => ({
  display: 'flex',
  flexDirection: 'row',
  alignItems: 'center',
  padding: '8px 8px',
  gap: '16px',
  width: 256,
  height: 56,
  backgroundColor: theme.palette.primary.contrastText,
  borderRadius: '2px',
  boxShadow: '2px 2px 10px rgba(64, 64, 64, 0.15)',
  [theme.breakpoints.down('md')]: {
    width: '100%'
  }
}));
export const ProductDetailsContainer = styled(Grid)(() => ({
  display: 'flex',
  justifyContent: 'center',
  alignItems: 'flex-end',
  flexDirection: 'column'
}));
export const ProductImageContainer = styled(Grid)(() => ({
  alignSelf: 'center',
  paddingTop: '100px',
  height: 1
}));
export const ProductInfoContainer = styled(Grid)(({ theme }) => ({
  width: 256,
  [theme.breakpoints.down('md')]: {
    width: '100%'
  }
}));
export const ProductMfnTypography = styled(Typography)(({ theme }) => ({
  paddingTop: 0,
  color: 'primary.main',
  fontWeight: 400,
  [theme.breakpoints.down('md')]: {
    paddingTop: theme.spacing(2)
  }
}));
export const QtyInfoContainer = styled(Grid)(({ theme }) => ({
  width: 528,
  [theme.breakpoints.down('md')]: {
    width: 'auto'
  }
}));
export const QtyInputContainer = styled(Grid)(({ theme }) => ({
  width: 256,
  [theme.breakpoints.down('md')]: {
    width: 175
  }
}));

/**
 * EnvironmentalOptions
 */
export const EnvironmentalImage = styled('img')({
  marginLeft: 'auto',
  marginRight: 'auto',
  display: 'block',
  width: '50%'
});

/**
 * ProductAvailabilityChip
 */
export const AvailabilityTypography = styled(Typography)(({ theme }) => ({
  fontFamily: 'Roboto',
  fontSize: '16px',
  fontWeight: 500,
  marginLeft: '8px',
  letterSpacing: '0.005em',
  [theme.breakpoints.down('md')]: {
    marginLeft: '12px'
  }
}));

export const AvailabilityInfoTypography = styled(Typography)(({ theme }) => ({
  width: 195,
  height: 16,
  fontFamily: 'Roboto',
  fontStyle: 'normal',
  fontWeight: 400,
  fontSize: 12,
  lineHeight: 1,
  marginLeft: '8px',
  letterSpacing: '0.005em',
  color: theme.palette.text.primary,
  [theme.breakpoints.down('md')]: {
    marginLeft: '12px'
  }
}));

/**
 * ProductCodes
 */
export const ProductCodesLabel = styled('span')(({ theme }) => ({
  fontWeight: 500,
  paddingRight: theme.spacing(1)
}));
export const ProductCodesListItemText = styled(ListItemText)(({ theme }) => ({
  color: theme.palette.secondary02.main,
  display: 'flex',
  justifyContent: 'space-between'
}));
export const ProductCodesValue = styled('span')(({ theme }) => ({
  textAlign: 'right',
  paddingLeft: theme.spacing(1)
}));

/**
 * SHARED
 */
export const BulletList = styled(List)(({ theme }) => ({
  listStyleType: 'disc',
  marginBlockStart: 0,
  paddingInlineStart: theme.spacing(2.5)
}));
export const StockAvailabilityContainer = styled(Box)(({ theme }) => ({
  display: 'flex',
  alignItems: 'center',
  padding: theme.spacing(1),
  width: 256,
  height: 56,
  borderRadius: '2px',
  [theme.breakpoints.down('md')]: {
    width: '100%'
  }
}));
export const CmpTypography = styled(Typography)(({ theme }) => ({
  fontStyle: 'normal',
  fontWeight: 700,
  fontSize: '39.0625px',
  lineHeight: '46px',
  display: 'flex',
  alignItems: 'center',
  textAlign: 'right',
  letterSpacing: '0.0075em',
  color: theme.palette.primary.main
}));

export const SignInForPriceButton = styled(Button)(({ theme }) => ({
  padding: '10px 5px'
}));
