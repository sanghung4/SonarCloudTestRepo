import {
  Box,
  Button,
  Divider,
  Grid,
  Typography
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

export type ForgotPasswordConfirmationProps = {
  email: string;
  onResendClicked: () => void;
};

export default function ForgotPasswordConfirmation(
  props: ForgotPasswordConfirmationProps
) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  /**
   * Render
   */
  return (
    <>
      <Grid item py={3}>
        <Typography textAlign="center">
          {t('forgotPassword.resetEmailSent')}
        </Typography>
        <Typography textAlign="center" fontWeight={600}>
          {props.email}
        </Typography>
        <Typography textAlign="center">
          {t('forgotPassword.checkInbox')}
        </Typography>
      </Grid>
      <Box component={Divider} my={2} width="100%" />
      <Grid item>
        <Typography pt={2} pb={3}>
          {t('forgotPassword.noEmail')}
        </Typography>
      </Grid>
      <Grid item>
        <Button
          onClick={props.onResendClicked}
          variant="secondary"
          data-testid="resend-link-button"
        >
          {t('forgotPassword.resendLink')}
        </Button>
      </Grid>
    </>
  );
}
