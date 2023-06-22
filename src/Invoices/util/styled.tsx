import {
  Box,
  Button,
  Grid,
  styled,
  Divider,
  alpha,
  Card,
  Typography
} from '@dialexa/reece-component-library';
import { ToggleButton } from '@mui/material';

export const StyledInvoiceBucketContainer = styled(Box)(({ theme }) => ({
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'center',
  alignItems: 'center',
  padding: '12px 16px',
  gap: '27px',
  width: '100%',
  height: '84px',
  cursor: 'pointer',
  background: theme.palette.secondary05.main,
  ':hover': {
    border: `3px solid ${theme.palette.primary02.main}`
  },
  border: `1px solid ${theme.palette.secondary05.light}`,
  boxShadow: `3px 3px 4px ${alpha(theme.palette.text.primary, 0.2)}`,
  borderRadius: '8px',
  [theme.breakpoints.down('md')]: {
    height: 56
  }
}));

export const InvoicesHeaderContainer = styled(Grid)(({ theme }) => ({
  justifyContent: 'space-between',
  alignItems: 'center',
  paddingLeft: 8,
  marginBottom: 24
}));

export const InvoicesTitleTypography = styled(Typography)(({ theme }) => ({
  width: 148,
  height: 46,
  fontFamily: 'Roboto',
  fontStyle: 'normal',
  fontWeight: 500,
  fontSize: '39px',
  lineHeight: '46px',
  letterSpacing: '0.0075em',
  color: theme.palette.primary.main,
  [theme.breakpoints.down('md')]: {
    width: 119,
    height: 37,
    fontSize: '32px',
    lineHeight: '37px',
    marginTop: 16
  }
}));

export const OpenInvoicesButtonContainer = styled(Box)(({ theme }) => ({
  display: 'flex',
  flexDirection: 'row',
  justifyContent: 'center',
  alignItems: 'center',
  padding: '16px',
  gap: '27px',
  width: '100%',
  height: '56px',
  ':hover': {
    border: `3px solid ${theme.palette.primary02.main}`
  },
  background: theme.palette.secondary05.main,
  cursor: 'pointer',
  border: `1px solid ${theme.palette.secondary05.light}`,
  boxShadow: `3px 3px 4px ${alpha(theme.palette.text.primary, 0.2)}`,
  borderRadius: '8px',
  [theme.breakpoints.down('md')]: {
    justifyContent: 'space-between',
    gap: '0px'
  }
}));

export const InvoicesStatusButton = styled(ToggleButton)(({ theme }) => ({
  '&.Mui-selected, &.Mui-selected:hover': {
    color: theme.palette.primary.contrastText,
    backgroundColor: theme.palette.primary02.main
  }
}));

export const InvoicesBucketContainer = styled(Card)(({ theme }) => ({
  paddingLeft: 40,
  paddingRight: 40,
  paddingTop: 24,
  paddingBottom: 24,
  marginBottom: 24,
  [theme.breakpoints.down('md')]: {
    paddingLeft: 12,
    paddingRight: 12
  }
}));

export const BucketInfoTypography = styled(Typography)(({ theme }) => ({
  textAlign: 'center',
  [theme.breakpoints.down('md')]: {
    textAlign: 'left'
  }
}));

export const StyledDividerContainer = styled(Grid)(({ theme }) => ({
  paddingTop: 16,
  paddingBottom: 16,
  paddingLeft: 40,
  paddingRight: 40,
  marginTop: 0,
  marginBottom: 0,
  [theme.breakpoints.down('md')]: {
    paddingLeft: 0,
    paddingRight: 0,
    marginTop: 16,
    marginBottom: 16
  }
}));

export const StyledDivider = styled(Divider)(({ theme }) => ({
  position: 'relative',
  border: `1px solid ${theme.palette.secondary03.main}`,
  top: '30&'
}));

export const BucketContainer = styled(Box)(({ theme }) => ({
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'center',
  alignItems: 'center',
  padding: '24px 0px',
  gap: '27px',
  width: '100%',
  height: '112px',
  boxShadow: `3px 3px 4px ${alpha(theme.palette.text.primary, 0.2)}`,
  borderRadius: '8px',
  cursor: 'pointer',
  [theme.breakpoints.down('md')]: {
    padding: '24px 16px',
    height: 56
  }
}));

export const BucketTitleTypography = styled(Typography)(({ theme }) => ({
  width: '100%',
  textAlign: 'center',
  [theme.breakpoints.down('md')]: {
    textAlign: 'left'
  }
}));

export const BucketPricingTypography = styled(Typography)(({ theme }) => ({
  textAlign: 'center',
  [theme.breakpoints.down('md')]: {
    textAlign: 'right'
  }
}));

export const PdfButtonContainer = styled(Grid)(({ theme }) => ({
  justifyContent: 'flex-start',
  alignSelf: 'start',
  marginLeft: '-16px'
}));

export const StyledButton = styled(Button)(({ theme }) => ({
  width: 176,
  height: 40,
  [theme.breakpoints.down('md')]: {
    width: 136,
    marginTop: 16
  }
}));
