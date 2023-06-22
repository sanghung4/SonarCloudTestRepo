import {
  Box,
  Grid,
  Link,
  Typography,
  useTheme
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import footerItemLinkProp from 'common/Footer/util/footerItemLinkProp';
import useFooterData from 'common/Footer/util/useFooterData';
import useFooterStyles from 'common/Footer/util/useFooterStyles';
import { MaxIcon } from 'icons';

export default function FooterLinks() {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();
  const { spacing } = useTheme();
  const styles = useFooterStyles();
  const footerData = useFooterData();

  /**
   * Render
   */
  return (
    <Grid container spacing={1}>
      {Object.entries(footerData).map(([key, list]) => (
        <Grid key={key} item {...styles.footerColumnGridProp}>
          {key === 'max' ? (
            <Box width={spacing(7)} height={spacing(4)}>
              <MaxIcon width="100%" height="100%" />
            </Box>
          ) : (
            <Typography lineHeight={spacing(4)} color="primary" variant="h5">
              {t(key)}
            </Typography>
          )}
          {list
            .filter((item) => item.condition)
            .map((item, index) => (
              <Box key={key + index} mt={2} mb={2}>
                <Link
                  {...footerItemLinkProp(item)}
                  color="inherit"
                  underline="none"
                  data-testid={`terms-of-access-footer-` + item.name}
                >
                  {t(item.name)}
                </Link>
              </Box>
            ))}
        </Grid>
      ))}
    </Grid>
  );
}
