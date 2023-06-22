import { Box, Typography, styled } from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import leak from 'images/leak.svg';
import leak_bg from 'images/leak_bg.svg';

const ImgStyled = styled('img')(() => ({
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  height: '60%',
  '&:first-child': {
    height: '100%'
  }
}));

function SidebarError() {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  /**
   * Render
   */
  return (
    <Box
      px={4}
      pt={10}
      display="flex"
      flexDirection="column"
      alignItems="center"
    >
      <Box position="relative" width="171px" height="195px">
        <ImgStyled src={leak_bg} alt="leaky pipe" />
        <ImgStyled src={leak} alt="leaky pipe" />
      </Box>
      <Typography
        variant="h1"
        pt={3}
        pb={1}
        color="primary02.main"
        fontWeight={400}
      >
        {t('common.ohNo')}
      </Typography>
      <Typography variant="h4">{t('common.leak')} </Typography>
      <Box py={2}>
        <Typography variant="body1" align="center">
          {t('branch.error')}
        </Typography>
      </Box>
    </Box>
  );
}
export default SidebarError;
