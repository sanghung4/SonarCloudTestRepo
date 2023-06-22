import { useContext } from 'react';

import {
  Box,
  Button,
  Card,
  Dialog,
  DialogActions,
  DialogTitle,
  DialogContent,
  Grid,
  IconButton,
  Typography
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { DateRangeContext } from 'common/DateRange';
import DateRangeInput from 'common/DateRange/DateRangeInput';
import { DeleteIcon } from 'icons';

type Props = {
  open: boolean;
  onViewResults: () => void;
  onClose: () => void;
};

export default function DateFilterDialog(props: Props) {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();

  /**
   * Context
   */
  const { onClear } = useContext(DateRangeContext);

  /**
   * Callbacks
   */
  const handleViewResultsClicked = () => {
    props.onViewResults();
    props.onClose();
  };

  return (
    <Dialog open={props.open} fullScreen>
      <Box pt={3} display="flex" flexDirection="column" height={1}>
        <DialogTitle>
          <IconButton
            onClick={props.onClose}
            size="large"
            sx={(theme) => ({
              position: 'absolute',
              top: theme.spacing(3.5),
              right: theme.spacing(1)
            })}
          >
            <DeleteIcon />
          </IconButton>
          <Typography color="primary" variant="h5" align="center">
            {t('orders.filterOrders')}
          </Typography>
        </DialogTitle>
        <DialogContent sx={{ flexGrow: 1 }}>
          <DateRangeInput />
        </DialogContent>
        <Box component={Card} borderRadius={0} py={2}>
          <DialogActions>
            <Grid container>
              <Grid item xs={1} />
              <Grid item xs={5}>
                <Button
                  data-testid="view-results-button"
                  variant="primary"
                  fullWidth
                  onClick={handleViewResultsClicked}
                >
                  {t('common.viewResults')}
                </Button>
              </Grid>
              <Grid item xs={5}>
                {!!onClear && (
                  <Button variant="inline" fullWidth onClick={onClear}>
                    {t('common.clear')}
                  </Button>
                )}
              </Grid>
              <Grid item xs={1} />
            </Grid>
          </DialogActions>
        </Box>
      </Box>
    </Dialog>
  );
}
