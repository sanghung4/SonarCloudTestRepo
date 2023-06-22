import {
  Box,
  Button,
  Container,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { useHistory } from 'react-router-dom';

import Loader from 'common/Loader';
import { ChevronLeftIcon } from 'icons';
import InviteUser from 'Invite/InviteUser';
import useInviteCallback from 'Invite/util/useInviteCallback';
import { InviteUserInput } from 'generated/graphql';
import useDocumentTitle from 'hooks/useDocumentTitle';

export type InviteUserFormInput = Omit<
  InviteUserInput,
  'billToAccountId' | 'erpAccountId'
>;

export default function Invite() {
  /**
   * Custom hooks
   */
  const history = useHistory();
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  useDocumentTitle(t('migrateUsers.inviteUsers'));

  /**
   * Callbacks
   */
  const { handleInviteUser, inviteUserLoading } = useInviteCallback();

  /**
   * Render
   */
  return (
    <>
      <Box width={1} justifyContent="left" pt={3} pl={1}>
        <Button
          onClick={history.goBack}
          startIcon={<ChevronLeftIcon />}
          variant="inline"
        >
          {t('common.back')}
        </Button>
      </Box>
      <Container maxWidth="sm">
        <Box
          mt={isSmallScreen ? 4 : 6}
          mb={isSmallScreen ? 2 : 15}
          display="flex"
          flexDirection="column"
          alignItems="center"
        >
          <InviteUser onSubmitInvite={handleInviteUser} />
        </Box>
      </Container>
      {inviteUserLoading && <Loader backdrop />}
    </>
  );
}
