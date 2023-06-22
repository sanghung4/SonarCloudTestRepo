import {
  Alert,
  Box,
  styled,
  Typography
} from '@dialexa/reece-component-library';

export const DateRangeErrorTypography = styled(Typography)(({ theme }) => ({
  height: 17,
  fontStyle: 'normal',
  fontWeight: 400,
  fontSize: ' 12.8px',
  lineHeight: '16px',
  color: theme.palette.orangeRed.main
}));

export const DateRangeWarningContainer = styled(Box)(({ theme }) => ({
  display: 'flex',
  flexDirection: 'row',
  alignItems: 'center',
  padding: '8px',
  gap: '8px',
  background: '#FFF6E6',
  border: `1px solid ${theme.palette.secondary.main}`,
  borderRadius: '5px'
}));

export const DateRangeWarningTypography = styled(Typography)(({ theme }) => ({
  display: 'flex',
  alignItems: 'center',
  fontStyle: 'normal',
  fontWeight: 500,
  fontSize: '12.8px',
  lineHeight: '16px',
  color: theme.palette.text.primary
}));

export const PricingInfoTypography = styled(Typography)(({ theme }) => ({
  fontStyle: 'normal',
  fontSize: '16px',
  lineHeight: '24px',
  display: 'flex',
  alignItems: 'center',
  letterSpacing: '0.005em',
  color: theme.palette.primary02.main
}));

export const StyledAlert = styled(Alert)(({ theme }) => ({
  display: 'flex',
  height: 75,
  alignItems: 'center',
  color: 'primary02.main',
  border: `1px solid ${theme.palette.primary02.main}`,
  '& .MuiAlert-message': {
    color: 'primary02.main',
    p: 0
  },
  '& .MuiAlert-icon': {
    color: 'primary02.main'
  },
  '& a': {
    fontWeight: 500,
    color: 'primary02.main',
    '&:hover': {
      color: 'primary.main',
      cursor: 'pointer'
    }
  }
}));
