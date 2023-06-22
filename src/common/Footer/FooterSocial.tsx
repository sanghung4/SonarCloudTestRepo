import {
  Box,
  Link,
  Typography,
  useScreenSize,
  useTheme
} from '@dialexa/reece-component-library';
import { LinkedIn } from '@mui/icons-material';
import { useTranslation } from 'react-i18next';

export default function FooterSocial() {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  const { spacing } = useTheme();

  /**
   * Render
   */
  return (
    <Box
      mr={2}
      display="inline-block"
      textAlign="left"
      mx={isSmallScreen ? 6 : 0}
    >
      <Typography lineHeight={spacing(4)} color="primary" variant="h5">
        {t('common.stayConnected')}
      </Typography>
      <Box mt={2} mb={2}>
        <Link
          ml={1}
          target="_blank"
          href="https://www.linkedin.com/company/reeceusa/"
          color="inherit"
          underline="none"
          data-testid="terms-of-access-footer-linkedin"
        >
          <LinkedIn color="primary" sx={{ fontSize: '30px' }} />
        </Link>
      </Box>
    </Box>
  );
}
