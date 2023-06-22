import { useContext, useEffect, useState } from 'react';

import {
  AppBar,
  Box,
  Button,
  Container,
  Divider,
  Grid,
  Hidden,
  IconButton,
  Link,
  Toolbar,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink, useHistory, useLocation } from 'react-router-dom';

import { AuthContext } from 'AuthProvider';
import BranchButton from 'Branches/BranchButton';
import CartButton from 'common/Header/CartButton';
import DesktopSubheader from 'common/Header/DesktopSubheader';
import MobileDrawer from 'common/Header/MobileDrawer';
import { indexStyles, ReeceLogo } from 'common/Header/util';
import { MenuIcon, SearchIcon, StarFilledIcon } from 'icons';
import logoSrc from 'images/logo.svg';
import { useDomainInfo } from 'hooks/useDomainInfo';
import SearchBar from './SearchBar';
import { HeaderContext } from './HeaderProvider';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import { SelectAccountsHeaderContainer } from './util/styles';

function Header() {
  /**
   * Custom Hooks
   */
  const { isWaterworks, subdomain } = useDomainInfo();
  const history = useHistory();
  const location = useLocation();
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * State
   */
  const [drawerOpen, setDrawerOpen] = useState(false);

  /**
   * Context
   */
  const { authState, profile } = useContext(AuthContext);
  const { searchOpen, setSearchOpen } = useContext(HeaderContext);
  const { isMincron } = useSelectedAccountsContext();

  /**
   * Callback
   */

  /**
   * Effect
   */
  useEffect(() => {
    document.body.classList.toggle('overflow-hidden', searchOpen);
  }, [searchOpen]);

  /**
   * Styles
   */
  const { containerSx, toolbarSx } = indexStyles(searchOpen, isSmallScreen);

  if (
    location.pathname.includes('/select-accounts') ||
    location.pathname.includes('/register')
  ) {
    return (
      <Box displayPrint="none">
        <Box zIndex="drawer" bottom={0} bgcolor="transparent">
          <AppBar color="inherit" position="static" elevation={0}>
            <Toolbar disableGutters sx={toolbarSx}>
              <Container maxWidth="lg" sx={containerSx}>
                <SelectAccountsHeaderContainer>
                  <ReeceLogo alt="Reece" src={logoSrc} />
                  {subdomain !== 'reece' && (
                    <>
                      <Box mx={1.5} my={isSmallScreen ? 0 : 1}>
                        <Divider orientation="vertical" />
                      </Box>
                      <ReeceLogo
                        alt={subdomain}
                        src={`/logos/${subdomain}.svg`}
                      />
                    </>
                  )}
                </SelectAccountsHeaderContainer>
              </Container>
            </Toolbar>
          </AppBar>
        </Box>
      </Box>
    );
  }

  /**
   * Render
   */
  return (
    <Box displayPrint="none">
      <Hidden mdUp>
        <Box height={searchOpen ? 0 : 'auto'}></Box>
      </Hidden>
      <MobileDrawer open={drawerOpen} setOpen={setDrawerOpen} />
      <Box zIndex="drawer"  bottom={0} bgcolor="transparent" sx = {
        {...(isSmallScreen && searchOpen && { position:"absolute", top:0, width:"100%"})}
      }>
        <AppBar color="inherit" position="static" elevation={0}>
          <Box bgcolor="common.white">
            <Toolbar disableGutters sx={toolbarSx}>
              {!isWaterworks && !isMincron && (
                <Hidden mdUp>
                  <SearchBar />
                </Hidden>
              )}
              <Container maxWidth="lg" sx={containerSx}>
                <Grid container spacing={isSmallScreen ? 0 : 2} wrap="nowrap">
                  <Grid
                    container
                    item
                    md={isSmallScreen ? 4 : 8}
                    justifyContent={isSmallScreen ? 'space-between' : undefined}
                    alignItems="center"
                    wrap="nowrap"
                    position="relative"
                  >
                    <Hidden mdUp>
                      <Box display="flex" flex="1" justifyContent="center">
                        <IconButton
                          onClick={() => setDrawerOpen(true)}
                          size="large"
                          sx={{ mr: 'auto' }}
                          data-testid="mobile-hamburger-menu"
                        >
                          <MenuIcon />
                        </IconButton>
                      </Box>
                    </Hidden>
                    <Box
                      lineHeight="1"
                      display="flex"
                      flex={isSmallScreen ? '0 1 auto' : '0 1 25%'}
                    >
                      <Link
                        to="/"
                        component={RouterLink}
                        sx={{ display: 'flex' }}
                      >
                        <ReeceLogo alt="Reece" src={logoSrc} />
                      </Link>

                      {subdomain !== 'reece' && (
                        <>
                          <Box mx={1.5} my={isSmallScreen ? 0 : 1}>
                            <Divider orientation="vertical" />
                          </Box>
                          <Link
                            to="/"
                            component={RouterLink}
                            sx={{ display: 'flex' }}
                          >
                            <ReeceLogo
                              alt={subdomain}
                              src={`/logos/${subdomain}.svg`}
                            />
                          </Link>
                        </>
                      )}
                    </Box>
                    {!isWaterworks && !isMincron && (
                      <Hidden mdDown>
                        <Box ml={5} display="flex" flex={1}>
                          <SearchBar />
                        </Box>
                      </Hidden>
                    )}
                    <Hidden mdUp>
                      <Box display="flex" flex="1" justifyContent="flex-end">
                        {!isWaterworks && !isMincron && (
                          <IconButton
                            onClick={() => setSearchOpen(true)}
                            size="large"
                            sx={{ ml: 'auto' }}
                            data-testid="header-search-button"
                          >
                            <SearchIcon />
                          </IconButton>
                        )}
                        <CartButton />
                      </Box>
                    </Hidden>
                  </Grid>
                  <Hidden mdDown>
                    <Grid
                      container
                      item
                      md={4}
                      justifyContent="flex-end"
                      alignItems="center"
                      wrap="nowrap"
                    >
                      {location.pathname !== '/select-accounts' && (
                        <>
                          {!isWaterworks && !isMincron && (
                            <Button
                              variant="text"
                              color="gray"
                              iconColor="primary"
                              startIcon={<StarFilledIcon />}
                              onClick={handleListsClick}
                              data-testid="my-lists-button"
                              sx={{
                                fontSize: '14px',
                                lineHeight: '18px',
                                fontWeight: 400,
                                whiteSpace: 'nowrap'
                              }}
                            >
                              {t('common.myLists')}
                            </Button>
                          )}
                          <BranchButton data-testid="branch-button-desktop" />{' '}
                          <CartButton />
                        </>
                      )}
                    </Grid>
                  </Hidden>
                </Grid>
              </Container>
            </Toolbar>
            {!!(
              authState?.isAuthenticated &&
              (!profile?.isEmployee ||
                (profile?.isEmployee && profile?.isVerified)) &&
              location.pathname !== '/select-accounts'
            ) && (
              <Hidden mdUp>
                <Divider />
                <Toolbar disableGutters>
                  <BranchButton data-testid="branch-button-mobile" />
                </Toolbar>
              </Hidden>
            )}
          </Box>
          <Hidden mdDown>
            <DesktopSubheader />
          </Hidden>
        </AppBar>
      </Box>
  </Box>
  );

  function handleListsClick() {
    history.push(authState?.isAuthenticated ? '/lists' : '/login');
  }
}

export default Header;
