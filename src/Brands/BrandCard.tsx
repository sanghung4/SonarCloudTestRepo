import {
  Box,
  Card,
  Divider,
  Grid,
  Link,
  Typography
} from '@dialexa/reece-component-library';
import { kebabCase } from 'lodash-es';
import { useTranslation } from 'react-i18next';

export type BrandCardProps = {
  thumbnail: string;
  title: string;
  description: string;
  url: string;
  sx: boolean;
};

const noBg = 'linear-gradient(rgba(0, 0, 0, 0) 0%, rgba(0, 0, 0, 0) 100%)';
const thumbSx = (thumb: string) => ({
  backgroundImage: `${noBg}, url(${thumb})`,
  backgroundRepeat: 'no-repeat',
  backgroundSize: 'cover'
});

const bPressSx = (thumb: string) => ({
  backgroundImage: `${noBg}, url(${thumb})`,
  backgroundRepeat: 'no-repeat',
  backgroundSize: 'cover',
  width: '220px',
  height: '168px',
  marginLeft: '67px'
});

export default function BrandCard(props: BrandCardProps) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  /**
   * Render
   */
  return (
    <Grid container justifyContent="center">
      <Box component={Card} width={374} height={486} borderRadius="2px">
        <Grid
          container
          item
          height="100%"
          flexDirection="column"
          justifyContent="space-between"
        >
          <Grid item>
            <Box
              height={180}
              width="100%"
              sx={
                props.sx ? bPressSx(props.thumbnail) : thumbSx(props.thumbnail)
              }
              data-testid={kebabCase(`${props.title}-thumbnail`)}
            />
            <Box px={2.5} py={1.25}>
              <Typography
                fontSize={20}
                color="primary"
                data-testid={kebabCase(`${props.title}-title`)}
              >
                {props.title}
              </Typography>
              <Typography
                fontSize={15}
                pt={0.6}
                data-testid={kebabCase(`${props.title}-description`)}
              >
                {props.description}
              </Typography>
            </Box>
          </Grid>
          <Grid item>
            <Divider />
            <Box p={2}>
              <Link href={props.url}>
                <Typography
                  color="primary02.main"
                  fontSize={16}
                  lineHeight={1.5}
                  data-testid={kebabCase(`${props.title}-learn-more-link`)}
                >
                  {t('common.learnMore')}
                </Typography>
              </Link>
            </Box>
          </Grid>
        </Grid>
      </Box>
    </Grid>
  );
}
