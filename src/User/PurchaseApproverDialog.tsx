import {
  Box,
  Dialog,
  DialogTitle,
  Typography,
  IconButton,
  Grid,
  DialogContent
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { UserInfo as ApproveeInfo } from 'generated/graphql';
import { UserInfo } from 'User';
import { DeleteIcon } from 'icons';
import { WarningIcon } from 'icons';
import Loader from 'common/Loader';
import { BoxStyled } from 'User/utils/styled';

type Props = {
  open: boolean;
  onClose: () => void;
  user: UserInfo;
  usersForApprover?: ApproveeInfo[];
  loading?: boolean;
};

function PurchaseApproverDialog(props: Props) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  if (props.loading) {
    return <Loader />;
  }

  return (
    <Dialog open={props.open} maxWidth="sm" onClose={props.onClose}>
      <Box>
        <DialogTitle>
          <Typography py={1} variant="h5">
            {t('user.purchaseApproverPrompt')}
          </Typography>
          <IconButton
            onClick={props.onClose}
            size="large"
            sx={(theme) => ({
              position: 'absolute',
              right: theme.spacing(1.5),
              top: theme.spacing(1.5),
              color: 'primary'
            })}
          >
            <Box
              component={DeleteIcon}
              data-testid="dialog-close-icon"
              sx={{ color: 'primary' }}
            />
          </IconButton>
        </DialogTitle>
        <DialogContent>
          <Box
            bgcolor={'#FFF6E6'}
            mt={0.25}
            mb={2}
            border={1}
            borderRadius={1}
            borderColor={'secondary.main'}
          >
            <Grid container spacing={2}>
              <Grid item xs={1}>
                <BoxStyled
                  component={WarningIcon}
                  width={25}
                  sx={{ mt: 2.25, color: 'secondary.main' }}
                />
              </Grid>
              <Grid item xs={11}>
                <Typography px={2} py={2} pt={2} pb={1}>
                  <Typography>
                    <b>
                      {props.user.firstName} {` `} {props.user.lastName}
                    </b>
                    {` `} {t('user.purchaseApproverAlert')}
                  </Typography>
                </Typography>
                <Typography px={2} pb={2}>
                  {props.usersForApprover?.map((item, index) => (
                    <Box
                      key={index}
                      mt={1}
                      mb={0}
                      data-testid={`user-for-approver-${index}`}
                    >
                      <Typography>
                        {item.firstName} {` `} {item.lastName} {` - `}{' '}
                        <b>{item.email} </b>
                      </Typography>
                    </Box>
                  ))}
                </Typography>
              </Grid>
            </Grid>
          </Box>
        </DialogContent>
      </Box>
    </Dialog>
  );
}

export default PurchaseApproverDialog;
