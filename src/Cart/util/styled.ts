import {
  Dialog,
  styled,
  TextField,
  Box,
  lighten,
  Typography
} from '@dialexa/reece-component-library';

export const LineNotesTextfield = styled(TextField)(() => ({
  marginTop: 0,
  padding: 0,
  '& .MuiOutlinedInput-root': { padding: 0 }
}));

export const LineNotesModal = styled(Dialog)(({ theme }) => ({
  [theme.breakpoints.down('md')]: {
    '& .MuiPaper-root': { margin: 0, width: '100%' }
  }
}));

export const CheckoutWarningContainer = styled(Box)(({ theme }) => ({
  marginTop: '10px',
  borderRadius: '5px',
  display: 'flex',
  alignItems: 'center',
  backgroundColor: `${lighten(theme.palette.secondary.light, 0.88)}`,
  border: '1px solid',
  borderColor: theme.palette.secondary.main
}));

export const CheckoutWarningTypography = styled(Typography)(({ theme }) => ({
  fontWeight: 400,
  fontSize: 12,
  fontStyle: 'Regular',
  color: theme.palette.text.primary,
  marginRight: 5,
  marginTop: 5,
  marginBottom: 5,
  lineHeight: '20px'
}));

export const CheckoutWarningMobileTypography = styled(Typography)(
  ({ theme }) => ({
    fontWeight: 400,
    fontSize: 16,
    width: '286px',
    fontStyle: 'Regular',
    color: theme.palette.text.primary,
    marginRight: 5,
    marginTop: 5,
    marginBottom: 5,
    lineHeight: '20px'
  })
);
