import { useEffect } from 'react';
import {
  Dialog,
  Box,
  DialogActions,
  DialogTitle,
  DialogContent,
  Button,
  LoadingButton,
  InputLabel,
  Grid,
  useSnackbar
} from '@dialexa/reece-component-library';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';

import FormRadioInput from 'common/FormRadioInput';
import {
  BranchListItemFragment,
  useUpdateBranchMutation
} from 'generated/graphql';
import { useTranslation } from 'react-i18next';

type EditModalProps = {
  open: boolean;
  onClose: () => void;
  selectedBranch?: BranchListItemFragment;
};

const RADIO_OPTIONS = [
  { label: 'Yes', value: true },
  { label: 'No', value: false }
];

function EditModal(props: EditModalProps) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();
  const { pushAlert } = useSnackbar();
  const {
    control,
    handleSubmit,
    reset,
    formState: { isValid, isDirty }
  } = useForm<BranchListItemFragment>({
    resolver: yupResolver(
      yup.object({
        isActive: yup.boolean().required(),
        isAvailableInStoreFinder: yup.boolean().required(),
        isPricingOnly: yup.boolean().required(),
        isShoppable: yup.boolean().required()
      })
    )
  });

  /**
   * Data
   */
  const [updateBranch, { loading }] = useUpdateBranchMutation({
    onCompleted: () => {
      props.onClose();
      pushAlert(`${props.selectedBranch?.branchId} updated`, {
        variant: 'success'
      });
    },
    onError: () => {
      props.onClose();
      pushAlert(`Something went wrong`, { variant: 'error' });
    }
  });

  /**
   * Callbacks
   */
  const handleSubmitCallback = (data: BranchListItemFragment) => {
    updateBranch({
      variables: {
        input: {
          id: props.selectedBranch?.id ?? '',
          isActive: data.isActive,
          isAvailableInStoreFinder: data.isAvailableInStoreFinder,
          isPricingOnly: data.isPricingOnly,
          isShoppable: data.isShoppable
        }
      }
    });
  };

  /**
   * Effects
   */
  useEffect(() => {
    if (props.selectedBranch) {
      reset({
        isActive: props.selectedBranch.isActive,
        isAvailableInStoreFinder: props.selectedBranch.isAvailableInStoreFinder,
        isShoppable: props.selectedBranch.isShoppable,
        isPricingOnly: props.selectedBranch.isPricingOnly
      });
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [props.selectedBranch]);

  /**
   * Render
   */
  return (
    <Dialog open={props.open} onClose={props.onClose}>
      <Box component="form" onSubmit={handleSubmit(handleSubmitCallback)}>
        <DialogTitle>
          {`Update Branch ${props.selectedBranch?.branchId}: ${props.selectedBranch?.name}`}
        </DialogTitle>

        <DialogContent>
          <Grid container columns={1} spacing={2}>
            <Grid item md={1}>
              <InputLabel>{t('branchManagement.isActive')}</InputLabel>
              <FormRadioInput
                control={control}
                name="isActive"
                options={RADIO_OPTIONS}
                disabled={loading}
              />
            </Grid>
            <Grid item md={1}>
              <InputLabel>{t('branchManagement.isAvailable')}</InputLabel>
              <FormRadioInput
                control={control}
                name="isAvailableInStoreFinder"
                options={RADIO_OPTIONS}
                disabled={loading}
              />
            </Grid>
            <Grid item md={1}>
              <InputLabel>{t('branchManagement.isShoppable')}</InputLabel>
              <FormRadioInput
                control={control}
                name="isShoppable"
                options={RADIO_OPTIONS}
                disabled={loading}
              />
            </Grid>
            <Grid item md={1}>
              <InputLabel>{t('branchManagement.isPricingOnly')}</InputLabel>
              <FormRadioInput
                control={control}
                name="isPricingOnly"
                options={RADIO_OPTIONS}
                disabled={loading}
              />
            </Grid>
          </Grid>
        </DialogContent>
        <DialogActions>
          <Button
            variant="inline"
            onClick={() => reset(props.selectedBranch)}
            disabled={!isDirty}
          >
            Reset
          </Button>
          <LoadingButton
            type="submit"
            variant="contained"
            loading={loading}
            disabled={!isValid || !isDirty}
          >
            Submit
          </LoadingButton>
        </DialogActions>
      </Box>
    </Dialog>
  );
}

export default EditModal;
