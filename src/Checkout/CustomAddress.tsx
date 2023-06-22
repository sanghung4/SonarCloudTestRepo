import {
  Box,
  Button,
  Grid,
  MenuItem,
  Select,
  TextField
} from '@dialexa/reece-component-library';
import { useFormik } from 'formik';
import { useTranslation } from 'react-i18next';
import * as Yup from 'yup';

import { CustomAddressInput } from 'Checkout/util/types';
import InputMask from 'common/InputMask';
import { phoneMask } from 'utils/masks';
import { stateAbbreviations } from 'utils/states';
import { useCheckoutContext } from './CheckoutProvider';
import { Delivery } from 'generated/graphql';
import { omit } from 'lodash-es';
import { initialAddressValues } from './util';
import updateZipcode from 'utils/updateZipCode';
import { regexPatterns } from 'JobForm/utils/forms';

type Props = {
  onConfirm: (customAddress: CustomAddressInput) => void;
  onCancel: () => void;
};

export default function CustomAddress(props: Props) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  /**
   * Context
   */
  const { deliveryMethodObject } = useCheckoutContext();
  const delivery = (deliveryMethodObject as Delivery)?.deliveryInstructions
    ? (deliveryMethodObject as Delivery)
    : undefined;
  const customAddress = delivery
    ? ({
        ...omit(delivery.address, ['__typename', 'id']),
        phoneNumber: delivery.phoneNumber
      } as CustomAddressInput)
    : undefined;

  const initialValues =
    delivery && delivery.address?.custom
      ? customAddress!
      : initialAddressValues;

  /**
   * Form
   */
  const formik = useFormik({
    initialValues,
    onSubmit: props.onConfirm,
    validationSchema: Yup.object({
      street1: Yup.string().required(t('validation.address1Required')),
      city: Yup.string().required(t('validation.cityRequired')),
      state: Yup.string().required(t('validation.stateRequired')),
      zip: Yup.string()
        .required(t('validation.zipRequired'))
        .matches(regexPatterns.zip, {
          message: t('validation.zipInvalid'),
          excludeEmptyString: true
        }),
      phoneNumber: Yup.string().matches(/^\(\d{3}\)\s{1}\d{3}-\d{4}$/, {
        message: t('validation.phoneNumberInvalid')
      })
    })
  });

  return (
    <form onSubmit={formik.handleSubmit} noValidate>
      <Grid container spacing={2}>
        <Grid item xs={12}>
          <TextField
            id="company-name"
            name="companyName"
            label={t('common.companyCO')}
            placeholder={t('common.enterCompanyName')}
            value={formik.values.companyName}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            inputProps={{ 'data-testid': 'custom-address-company-name-input' }}
            helperText=" "
            fullWidth
          />
        </Grid>
        <Grid item xs={12}>
          <TextField
            id="street1"
            name="street1"
            label={t('common.address1')}
            placeholder={t('common.enterAddress')}
            value={formik.values.street1}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            error={!!(formik.touched.street1 && formik.errors.street1)}
            helperText={
              formik.touched.street1 && formik.errors.street1
                ? formik.errors.street1
                : ' '
            }
            inputProps={{ 'data-testid': 'custom-address-address-1-input' }}
            fullWidth
            required
          />
        </Grid>
        <Grid item xs={12}>
          <TextField
            id="street2"
            name="street2"
            label={t('common.address2')}
            placeholder={t('common.enterAddress')}
            value={formik.values.street2}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            inputProps={{ 'data-testid': 'custom-address-address-2-input' }}
            helperText=" "
            fullWidth
          />
        </Grid>
        <Grid item xs={12} md={7}>
          <TextField
            id="city"
            name="city"
            label={t('common.city')}
            placeholder={t('common.enterCity')}
            value={formik.values.city}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            error={!!(formik.touched.city && formik.errors.city)}
            helperText={
              formik.touched.city && formik.errors.city
                ? formik.errors.city
                : ' '
            }
            inputProps={{ 'data-testid': 'custom-address-city-input' }}
            fullWidth
            required
          />
        </Grid>
        <Grid item xs={12} md={5}>
          <Select
            id="state"
            name="state"
            label={t('common.state')}
            placeholder={t('common.selectState')}
            value={formik.values.state}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            error={!!(formik.touched.state && formik.errors.state)}
            helperText={
              formik.touched.state && formik.errors.state
                ? formik.errors.state
                : ' '
            }
            inputProps={{ 'data-testid': 'custom-address-state-input' }}
            fullWidth
            required
          >
            {stateAbbreviations.map((state) => (
              <MenuItem key={state} value={state}>
                {state}
              </MenuItem>
            ))}
          </Select>
        </Grid>
        <Grid item xs={12} md={5}>
          <TextField
            id="zip-code"
            name="zip"
            label={t('common.zip')}
            placeholder={t('common.enterZip')}
            value={formik.values.zip}
            onChange={updateZipcode(formik.handleChange)}
            onBlur={formik.handleBlur}
            error={!!(formik.touched.zip && formik.errors.zip)}
            helperText={
              formik.touched.zip && formik.errors.zip ? formik.errors.zip : ' '
            }
            inputProps={{ 'data-testid': 'custom-address-zip-input' }}
            fullWidth
            required
          />
        </Grid>
        <Grid item xs={12} md={7}>
          <TextField
            id="phone-number"
            name="phoneNumber"
            label={t('common.phoneNumber')}
            placeholder={t('common.enterPhone')}
            value={formik.values.phoneNumber}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            inputProps={{
              ...phoneMask,
              'data-testid': 'custom-address-phone-number-input'
            }}
            InputProps={{ inputComponent: InputMask as any }}
            fullWidth
          />
        </Grid>
      </Grid>
      <Box display="flex" alignItems="center" mt={2}>
        <Button type="submit" data-testid="confirm-address-button">
          {t('common.confirmAddress')}
        </Button>
        <Button
          type="button"
          variant="text"
          onClick={props.onCancel}
          sx={{ ml: 2 }}
          data-testid="cancel-address-button"
        >
          {t('common.cancel')}
        </Button>
      </Box>
    </form>
  );
}
