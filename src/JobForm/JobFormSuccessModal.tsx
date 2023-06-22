import {
  Box,
  Button,
  Card,
  CardActions,
  CardContent,
  Divider,
  Grid,
  Typography,
  Link
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { AuthContext } from 'AuthProvider';
import { testIds } from 'test-utils/testIds';
import { useContext } from 'react';
import checkMark from 'images/checkmark.png';
import { useHistory } from 'react-router-dom';

const CREDIT_APP_EMAIL = 'creditapps@morsco.com';

type Props = {
  customerDetails?: string;
};

function JobFormSuccessModal(props: Props) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();
  const history = useHistory();

  /**
   * Context
   */
  const { profile } = useContext(AuthContext);

  return (
    <Grid
      display="flex"
      direction="column"
      alignItems="center"
      marginBottom={10}
      data-testid={testIds.JobForm.JobFormCard.successModal}
    >
      <Card style={{ width: 378, textAlign: 'center', paddingBottom: 16 }}>
        <CardContent>
          <Box
            width={40}
            marginBottom={3}
            component="img"
            alt="checkmark"
            src={checkMark}
            display="block"
            marginX="auto"
          />
          <Typography
            variant="h6"
            gutterBottom
            color="primary"
            fontWeight="500"
            marginBottom={3}
            data-testid={testIds.JobForm.JobFormCard.successModalTitle}
          >
            {t('jobForm.jobFormSuccessMessageTitle')}
          </Typography>
          {profile?.isEmployee && (
            <Typography
              variant="body2"
              gutterBottom
              marginBottom={3}
              data-testid={
                testIds.JobForm.JobFormCard.successModalEmployeeMessage
              }
            >
              {t('jobForm.jobFormSuccessMessage')} {props.customerDetails}.
            </Typography>
          )}
          <Typography variant="body2" gutterBottom marginBottom={5}>
            {t('jobForm.jobFormSuccessMessageContent1')}
            <Link href={`mailto:${CREDIT_APP_EMAIL}`} color="primary02.main">
              {t('creditForms.email')}
            </Link>{' '}
            {t('jobForm.jobFormSuccessMessageContent2')}
          </Typography>
          <Divider />
        </CardContent>
        <CardActions style={{ justifyContent: 'center' }}>
          <Button
            size="small"
            onClick={() => history.push('/')}
            data-testid={testIds.JobForm.JobFormCard.successModalReturnHome}
          >
            {t('jobForm.returnToHomePage')}
          </Button>
        </CardActions>
      </Card>
    </Grid>
  );
}

export default JobFormSuccessModal;
