import {
  Container,
  Toolbar,
  Grid,
  AppBar
} from '@dialexa/reece-component-library';
import { useLocation } from 'react-router-dom';

import useFooterStyles from 'common/Footer/util/useFooterStyles';
import FooterLinks from 'common/Footer/FooterLinks';
import FooterSocial from 'common/Footer/FooterSocial';
import FooterLegal from 'common/Footer/FooterLegal';
import { testIds } from 'test-utils/testIds';

export default function Footer() {
  /**
   * Custom Hooks
   */
  const styles = useFooterStyles();
  const { pathname } = useLocation();

  /**
   * Render
   */
  return (
    <AppBar
      color="inherit"
      position="relative"
      elevation={0}
      sx={styles.appBarSx}
      data-testid={testIds.Footer.component}
    >
      {!pathname.includes('select-accounts') &&
        !pathname.includes('register') &&
        !pathname.includes('lists') && (
          <Toolbar disableGutters>
            <Container maxWidth="lg" data-testid={testIds.Footer.linksToolbar}>
              <Grid mt={1} container spacing={1}>
                <Grid item md={8} sm={12}>
                  <FooterLinks />
                </Grid>
                <Grid item md={4} sm={12}>
                  <FooterSocial />
                </Grid>
              </Grid>
            </Container>
          </Toolbar>
        )}
      <Toolbar disableGutters sx={styles.toolbarSx}>
        <Container maxWidth="lg">
          <FooterLegal />
        </Container>
      </Toolbar>
    </AppBar>
  );
}
