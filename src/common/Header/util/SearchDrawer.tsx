import {
  Backdrop,
  Box,
  duration,
  Transition
} from '@dialexa/reece-component-library';
import { ChevronLeftIcon } from 'icons';
import { useTranslation } from 'react-i18next';
import { DrawerBackButton, SearchDrawerContainer } from './styles';

type Props = {
  open: boolean;
  headerHeight: number;
  onBackClick: () => void;
  children: React.ReactNode;
};

function SearchDrawer(props: Props) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  /**
   * Render
   */
  return (
    <>
      <Backdrop
        open={props.open}
        transitionDuration={{
          enter: duration.leavingScreen,
          exit: duration.enteringScreen
        }}
        sx={{ zIndex: -1 }}
      />
      <Transition in={props.open} timeout={150}>
        {(state) => (
          <SearchDrawerContainer offset={props.headerHeight} status={state}>
            <Box m={2} display="flex" flexDirection="column" flex="1">
              <DrawerBackButton
                variant="text"
                startIcon={<ChevronLeftIcon />}
                onClick={props.onBackClick}
              >
                {t('common.back')}
              </DrawerBackButton>
              <Box flex="1" overflow="hidden auto">
                {props.children}
              </Box>
            </Box>
          </SearchDrawerContainer>
        )}
      </Transition>
    </>
  );
}

export default SearchDrawer;
