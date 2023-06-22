import { useContext, useState } from 'react';

import {
  Box,
  Button,
  Grid,
  IconButton,
  List,
  ListItem,
  SwipeableDrawer,
  Typography,
  Divider
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { useHistory } from 'react-router-dom';

import { AuthContext } from 'AuthProvider';
import CategoriesMobileWrapper from 'Categories/CategoriesMobileWrapper';
import SwitchAccounts from 'common/Header/SwitchAccounts';
import {
  handleCategoriesClose,
  MobileDrawerCollapseCompanyList,
  MobileDrawerCollapseList,
  mobileDrawerStyles,
  mobileOption
} from 'common/Header/util';
import HoldAlert from 'common/HoldAlert';
import { CloseIcon, MaxIcon, SignOutIcon } from 'icons';
import { useDomainInfo } from 'hooks/useDomainInfo';
import useUserAgent from 'hooks/useUserAgent';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';

type Props = {
  open: boolean;
  setOpen: (val: boolean) => void;
};

export default function MobileDrawer(props: Props) {
  /**
   * Custom Hooks
   */
  const history = useHistory();
  const { t } = useTranslation();
  const { isiOS } = useUserAgent();
  const { isWaterworks } = useDomainInfo();

  /**
   * Context
   */
  const { activeFeatures, authState, firstName, profile } =
    useContext(AuthContext);
  const { selectedAccounts, error: erpAccountError } =
    useSelectedAccountsContext();

  /**
   * State
   */
  const [profileOpen, setProfileOpen] = useState(false);
  const [maxOpen, setMaxOpen] = useState(false);
  const [resourcesOpen, setResourcesOpen] = useState(false);
  const [showCategories, setShowCategories] = useState(false);

  /**
   * Styles
   */
  const { drawerPaperSx, listSx } = mobileDrawerStyles;

  /**
   * Render
   */
  return (
    <SwipeableDrawer
      PaperProps={{ sx: drawerPaperSx }}
      open={props.open}
      disableBackdropTransition={!isiOS}
      disableDiscovery={isiOS}
      onClose={handleSetOpen(false)}
      onOpen={handleSetOpen(true)}
      data-testid="mobile-menu-drawer"
    >
      <CategoriesMobileWrapper
        in={showCategories}
        handleClose={handleCategoriesClose(setShowCategories, props.setOpen)}
      />
      <Grid
        container
        wrap="nowrap"
        pt={1.5}
        pb={3}
        display="flex"
        flexDirection="column"
        minHeight={1}
      >
        <Box width={1}>
          {/* ===== Title Bar ===== */}
          <Grid container item justifyContent="flex-end" flexGrow={1} px={3}>
            <IconButton
              onClick={handleSetOpen(false)}
              size="large"
              sx={{ p: 0 }}
              data-testid="mobile-close-button"
            >
              <CloseIcon />
            </IconButton>
          </Grid>
          {/* ===== Accounts ===== */}
          {!!profile?.userId && (
            <>
              <Typography variant="h5" fontWeight={500} px={3} pb={2}>
                {t('common.hello', { name: firstName })}!
              </Typography>
              <Box component={Divider} px={3} />
              {!!(
                selectedAccounts.billToErpAccount?.creditHold ||
                selectedAccounts.shipToErpAccount?.creditHold
              ) && (
                <Box pt={1} px={3}>
                  <HoldAlert />
                </Box>
              )}
              <Box px={3}>
                <SwitchAccounts onChange={handleSetOpen(false)} />
              </Box>
            </>
          )}
        </Box>
        <Box flex={1} overflow="auto" mt={authState?.isAuthenticated ? 0 : 1}>
          <List sx={listSx}>
            {/* ********** Browse Products ********** */}
            {!isWaterworks && (
              <ListItem
                button
                onClick={handleSetShowCategories(true)}
                data-testid="browse-products-button"
              >
                {t('common.browseProducts')}
              </ListItem>
            )}
            {/* ********** Lists ********** */}
            {!!activeFeatures?.includes('LISTS') && !isWaterworks && (
              <ListItem
                button
                onClick={mobileOptionHandler('/lists', true)}
                data-testid="my-lists-button"
              >
                {t('common.myLists')}
              </ListItem>
            )}
            {/* ********** MAX ********** */}
            <MobileDrawerCollapseList
              item="max"
              open={maxOpen}
              setOpen={setMaxOpen}
              urlHandler={mobileOptionHandler}
            >
              <MaxIcon />
            </MobileDrawerCollapseList>
            {/* ********** My Profile ********** */}
            {!authState?.isAuthenticated ? (
              <ListItem
                button
                onClick={mobileOptionHandler('/login')}
                data-testid="sign-in-header-button"
              >
                {t('common.signInRegister')}
              </ListItem>
            ) : (
              !erpAccountError && (
                <MobileDrawerCollapseList
                  item="profile"
                  open={profileOpen}
                  setOpen={setProfileOpen}
                  urlHandler={mobileOptionHandler}
                >
                  {t('common.myProfile')}
                </MobileDrawerCollapseList>
              )
            )}
            {/* ********** Resources ********** */}
            <MobileDrawerCollapseList
              item="resources"
              open={resourcesOpen}
              setOpen={setResourcesOpen}
              urlHandler={mobileOptionHandler}
            >
              {t('common.resources')}
            </MobileDrawerCollapseList>
            {/* ********** Brands ********** */}
            {!isWaterworks && (
              <ListItem
                button
                onClick={mobileOptionHandler('/brands')}
                data-testid="brands-button"
              >
                {t('common.brands')}
              </ListItem>
            )}
            {/* ********** Store Finder ********** */}
            <ListItem
              button
              onClick={mobileOptionHandler('/location-search')}
              data-testid="store-finder-button"
            >
              {t('common.storeFinder')}
            </ListItem>
            {/* ********** Support ********** */}
            <ListItem
              button
              onClick={mobileOptionHandler('/support')}
              data-testid="help-button"
            >
              {t('support.help')}
            </ListItem>
            {/* ********** Companies ********** */}
            <MobileDrawerCollapseCompanyList />
          </List>
        </Box>
        {/* ********** LOG OUT ********** */}
        {!!authState?.isAuthenticated && (
          <Box pt={2} display="flex" justifyContent="flex-end" width={1} pr={1}>
            <Button
              startIcon={<SignOutIcon />}
              onClick={mobileOptionHandler('/logout')}
              variant="text"
            >
              <Typography
                variant="body1"
                pl={1.5}
                color="primary02.main"
                data-testid="sign-out-button"
              >
                {t('common.signOut')}
              </Typography>
            </Button>
          </Box>
        )}
      </Grid>
    </SwipeableDrawer>
  );

  /**
   * Handles
   */
  function mobileOptionHandler(path: string, auth?: boolean) {
    return () =>
      mobileOption(
        resetOpen,
        history.push,
        authState?.isAuthenticated,
        path,
        auth
      );
  }
  function handleSetOpen(show: boolean) {
    return () => props.setOpen(show);
  }
  function handleSetShowCategories(show: boolean) {
    return () => setShowCategories(show);
  }

  /**
   * Misc
   */
  function resetOpen() {
    props.setOpen(false);
    setProfileOpen(false);
    setMaxOpen(false);
    setResourcesOpen(false);
    setProfileOpen(false);
  }
}
