import {
  Box,
  Typography,
  useScreenSize,
  useTheme
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { WarningIcon } from 'icons';

export default function Warning() {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  const { contrastText, main } = useTheme().palette.waterWorksWarning;
  const { spacing } = useTheme();
  /**
   * Render
   */
  return (
    <Box
      borderRadius="5px"
      border={1}
      borderColor={contrastText}
      bgcolor={main}
      display="flex"
      flexDirection="row"
      alignItems="center"
      pl={isSmallScreen ? 1 : 3}
      pr={isSmallScreen ? 1 : 4}
      py={isSmallScreen ? 1 : 3.5}
    >
      <WarningIcon
        color={contrastText}
        width={isSmallScreen ? spacing(8) : spacing(4)}
      />
      <Typography color={contrastText} ml={1}>
        {t('cart.warning')}
      </Typography>
    </Box>
  );
}
