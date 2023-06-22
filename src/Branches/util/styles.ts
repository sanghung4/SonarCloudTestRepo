import { SxProps } from '@dialexa/reece-component-library';
import {
  styled,
  Box,
  lighten,
  Typography
} from '@dialexa/reece-component-library';

const branchButtonSx: SxProps = {
  fontSize: '14px',
  lineHeight: '18px',
  fontWeight: 400
};
const branchButtonStartIconLoadingSx: SxProps = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-46%, -45%)',
  '& .MuiCircularProgress-root svg': { color: 'primary02.main' }
};
const moreDetailsButtonSx: SxProps = { mt: '1rem', p: 0 };

export const BranchWarningContainer = styled(Box)(({ theme }) => ({
  display: 'flex',
  alignItems: 'center',
  backgroundColor: `${lighten(theme.palette.secondary.light, 0.88)}`,
  border: '1px solid',
  borderColor: theme.palette.secondary.main
}));

export const BranchWarningTypography = styled(Typography)(({ theme }) => ({
  fontWeight: 500,
  fontSize: 20,
  letterSpacing: '0.0075em',
  color: theme.palette.text.primary,
  marginRight: 11,
  marginTop: 8,
  marginBottom: 8
}));

/**
 * Main style
 */
export const styles = {
  branchButton: {
    branchButtonSx,
    branchButtonStartIconLoadingSx
  },
  branchCard: {
    moreDetailsButtonSx
  }
};
