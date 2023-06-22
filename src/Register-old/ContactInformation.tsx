import React from 'react';

import {
  Box,
  Button,
  Grid,
  MenuItem,
  Select,
  TextField,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useFormik } from 'formik';
import { useTranslation } from 'react-i18next';
import * as Yup from 'yup';

import InputMask from 'common/InputMask';
import {
  UserInput,
  useGetPhoneTypesQuery,
  GetUserInviteQuery
} from 'generated/graphql';
import { phoneMask } from 'utils/masks';

interface Props {
  contactInfo?: UserInput;
  reenterEmail: boolean;
  userInviteData: GetUserInviteQuery | undefined;
  onSubmitContact: (contact: UserInput) => void;
}

function ContactInformation(props: Props) {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Data
   */
  const { data: phoneTypesQuery } = useGetPhoneTypesQuery();
  const userInvite = props.userInviteData?.userInvite;

  /**
   * Form
   */
  const formik = useFormik({
    initialValues: {
      firstName: userInvite?.firstName || props.contactInfo?.firstName || '',
      lastName: userInvite?.lastName || props.contactInfo?.lastName || '',
      phoneNumber: props.contactInfo?.phoneNumber || '',
      phoneTypeId: props.contactInfo?.phoneTypeId || '',
      email: userInvite?.email || ''
    },
    validationSchema: Yup.object({
      firstName: Yup.string().required(t('validation.firstNameRequired')),
      lastName: Yup.string().required(t('validation.lastNameRequired')),
      phoneNumber: Yup.string()
        .matches(/^\(\d{3}\)\s{1}\d{3}-\d{4}$/, {
          message: t('validation.phoneNumberInvalid')
        })
        .required(t('validation.phoneNumberRequired')),
      phoneTypeId: Yup.string().required(t('validation.phoneTypeRequired')),
      email: Yup.string()
        .email(t('validation.emailInvalid'))
        .required(t('validation.emailRequired'))
    }),
    onSubmit: (values) => {
      props.onSubmitContact(values);
    }
  });

  return (
    <Box
      display="flex"
      flexDirection="column"
      alignItems="center"
      pt={isSmallScreen ? 3 : 6}
      pb={isSmallScreen ? 6 : 0}
    >
      <Typography
        variant={isSmallScreen ? 'h4' : 'h3'}
        component="h1"
        color="primary"
        sx={{ fontWeight: 700 }}
      >
        {t('common.contactInformation')}
      </Typography>
      <Box
        component="form"
        onSubmit={formik.handleSubmit}
        noValidate
        sx={{ pt: isSmallScreen ? 4 : 5 }}
      >
        <Grid container spacing={isSmallScreen ? 1 : 2}>
          <Grid item xs={12} md={6}>
            <TextField
              autoFocus={!props.reenterEmail}
              id="contact-first-name"
              name="firstName"
              label={t('common.firstName')}
              placeholder={t('common.firstName')}
              value={formik.values.firstName}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              error={Boolean(
                formik.touched.firstName && formik.errors.firstName
              )}
              helperText={
                formik.touched.firstName && formik.errors.firstName
                  ? formik.errors.firstName
                  : ' '
              }
              inputProps={{ 'data-testid': 'registration-first-name-input' }}
              fullWidth
              required
            />
          </Grid>
          <Grid item xs={12} md={6}>
            <TextField
              id="contact-last-name"
              name="lastName"
              label={t('common.lastName')}
              placeholder={t('common.lastName')}
              value={formik.values.lastName}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              error={Boolean(formik.touched.lastName && formik.errors.lastName)}
              helperText={
                formik.touched.lastName && formik.errors.lastName
                  ? formik.errors.lastName
                  : ' '
              }
              inputProps={{ 'data-testid': 'registration-last-name-input' }}
              fullWidth
              required
            />
          </Grid>
          <Grid item xs={12} md={6}>
            <TextField
              id="phone-number"
              name="phoneNumber"
              type="tel"
              label={t('common.phoneNumber')}
              value={formik.values.phoneNumber}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              error={Boolean(
                formik.touched.phoneNumber && formik.errors.phoneNumber
              )}
              helperText={
                formik.touched.phoneNumber && formik.errors.phoneNumber
                  ? formik.errors.phoneNumber
                  : ' '
              }
              inputProps={{
                ...phoneMask,
                'data-testid': 'registration-phone-number-input'
              }}
              InputProps={{ inputComponent: InputMask as any }}
              fullWidth
              required
            />
          </Grid>
          <Grid item xs={12} md={6}>
            <Select
              id="phone-type"
              name="phoneTypeId"
              label={t('common.phoneType')}
              placeholder="Select"
              value={formik.values.phoneTypeId}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              error={Boolean(
                formik.touched.phoneTypeId && formik.errors.phoneTypeId
              )}
              helperText={
                formik.touched.phoneTypeId && formik.errors.phoneTypeId
                  ? formik.errors.phoneTypeId
                  : ' '
              }
              renderValue={(id) => {
                const selectedType = phoneTypesQuery?.phoneTypes
                  .find((pt) => pt === id)
                  ?.toLowerCase();

                return selectedType ? t(`common.${selectedType}`) : 'Select';
              }}
              inputProps={{ 'data-testid': 'registration-phone-type-input' }}
              fullWidth
              required
            >
              {phoneTypesQuery?.phoneTypes.length
                ? phoneTypesQuery.phoneTypes.map((pt) => (
                    <MenuItem value={pt} key={pt}>
                      {t(`common.${pt.toLowerCase()}`)}
                    </MenuItem>
                  ))
                : null}
            </Select>
          </Grid>
          <Grid item xs={12}>
            <TextField
              autoFocus={props.reenterEmail}
              id="email"
              name="email"
              type="email"
              label={t('common.emailAddress')}
              placeholder={t('common.enterEmailAddress')}
              value={formik.values.email}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              error={Boolean(formik.touched.email && formik.errors.email)}
              helperText={
                formik.touched.email && formik.errors.email
                  ? formik.errors.email
                  : ' '
              }
              inputProps={{ 'data-testid': 'registration-email-address-input' }}
              fullWidth
              required
              disabled={!!userInvite}
            />
          </Grid>
          <Grid container item xs={12} justifyContent="center">
            <Box
              width={isSmallScreen ? 0.75 : 0.5}
              mt={isSmallScreen ? 3 : 8}
              mb={5}
            >
              <Button
                type="submit"
                fullWidth
                data-testid="registration-continue-button"
              >
                {t('common.continue')}
              </Button>
            </Box>
          </Grid>
        </Grid>
      </Box>
    </Box>
  );
}

export default ContactInformation;
