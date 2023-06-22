import {
  Button,
  Grid,
  Link,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import {
  PoshTypography as Typography,
  usePoshStyles
} from 'Brands/Posh/util/styles';
import { links } from 'utils/links';

const vimeoId = '816751310?h=679a71ae0a';

export default function PoshVimeo() {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  const sx = usePoshStyles(isSmallScreen);

  /**
   * Render
   */
  return (
    <Grid container justifyContent="center" bgcolor="lightGray.main">
      <Grid
        container
        mx={4}
        my={isSmallScreen ? 4 : 7.75}
        flexDirection="column"
        justifyContent="center"
        alignItems="center"
      >
        <Typography
          textAlign="center"
          fontSize={isSmallScreen ? 36 : 64}
          fontWeight={700}
          lineHeight={1}
          mb={4}
        >
          {t('poshMarketing.video.title')}
        </Typography>
        <Grid my={2}>
          <iframe
            title="vimeo"
            src={`https://player.vimeo.com/video/${vimeoId}`}
            width={isSmallScreen ? 251 : 996}
            height={isSmallScreen ? 140 : 578}
            frameBorder="0"
            style={{ display: 'initial' }}
            allowFullScreen
          />
        </Grid>
        <Grid mt={4}>
          <Link href={links.posh.youTubeEs}>
            <Button variant="secondary" sx={sx.button}>
              {t('poshMarketing.video.spanishVersion')}
            </Button>
          </Link>
        </Grid>
      </Grid>
    </Grid>
  );
}
