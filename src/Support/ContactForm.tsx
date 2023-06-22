import React, { useContext, useMemo } from 'react';

import {
  Box,
  CircularProgress,
  Grid,
  LoadingButton,
  MenuItem,
  Select,
  TextField,
  Typography,
  useScreenSize,
  useSnackbar
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { useFormik } from 'formik';
import * as Yup from 'yup';

import { AuthContext } from 'AuthProvider';
import InputMask from 'common/InputMask';
import {
  ContactFormInput,
  useSendContactFormMutation,
  useUserQuery
} from 'generated/graphql';
import { phoneMask } from 'utils/masks';
import updateZipcode from 'utils/updateZipCode';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';

function ContactFormComponent() {
  /**
   * Custom Hook
   */
  const { isSmallScreen } = useScreenSize();
  const { pushAlert } = useSnackbar();
  const { t } = useTranslation();

  /**
   * Context
   */
  const { profile } = useContext(AuthContext);
  const { selectedAccounts } = useSelectedAccountsContext();

  /**
   * Data
   */
  const { data: userQuery, loading: userLoading } = useUserQuery({
    variables: {
      userId: profile?.userId
    }
  });
  const [contactFormQuery, { loading: sendLoading }] =
    useSendContactFormMutation({
      onCompleted: (response) => {
        if (response.sendContactForm === 'Email sent successfully!') {
          formik.resetForm();
          pushAlert(t('common.messageSent'), {
            variant: 'success'
          });
        } else {
          pushAlert(t('common.messageFail'), {
            variant: 'error'
          });
        }
      },
      onError: () => {
        pushAlert(t('common.messageFail'), {
          variant: 'error'
        });
      }
    });

  /**
   * Memo
   */
  const initialValues = useMemo(() => {
    return {
      firstName: userQuery?.user?.firstName || '',
      lastName: userQuery?.user?.lastName || '',
      phoneNumber: userQuery?.user?.phoneNumber || '',
      email: userQuery?.user?.email || '',
      zip: (selectedAccounts.billToErpAccount?.zip || '').slice(0, 5),
      topic: '',
      message: ''
    };
  }, [userQuery, selectedAccounts]);
  const topicList = useMemo(
    () => [
      t('support.topicCustomerAccount'),
      t('support.topicProduct'),
      t('support.topicFeedback'),
      t('support.topicOther')
    ],
    [t]
  );

  /**
   * Form
   */
  const formik = useFormik({
    initialValues,
    enableReinitialize: true,
    validationSchema: Yup.object({
      firstName: Yup.string()
        .trim()
        .required(t('validation.firstNameRequired')),
      lastName: Yup.string().trim().required(t('validation.lastNameRequired')),
      phoneNumber: Yup.string().matches(/^\(\d{3}\)\s{1}\d{3}-\d{4}$/, {
        message: t('validation.phoneNumberInvalid')
      }),
      email: Yup.string()
        .email(t('validation.emailInvalid'))
        .required(t('validation.emailRequired')),
      zip: Yup.string()
        .matches(/^\d{5}$/, {
          message: t('validation.zipInvalid')
        })
        .required(t('validation.zipRequired')),
      topic: Yup.string().required(t('validation.topicRequired')),
      message: Yup.string().trim().required(t('validation.messageRequired'))
    }),
    onSubmit: (values) => {
      contactFormQuery({
        variables: {
          contactFormInput: values as ContactFormInput
        }
      });
    }
  });

  return (
    <Grid container justifyContent="center">
      <Grid item xs={12} md={8}>
        <Box
          px={5}
          my={5}
          borderLeft={isSmallScreen ? 'none' : '1px solid'}
          borderColor="secondary03.main"
        >
          <Typography variant="h5" color="primary">
            {t('support.contactTitle')}
          </Typography>
          <Box
            component="form"
            onSubmit={formik.handleSubmit}
            noValidate
            sx={{ pt: isSmallScreen ? 4 : 5 }}
          >
            <Grid container spacing={isSmallScreen ? 1 : 2}>
              {/* <>-------------------------<> FIRST NAME <>-------------------------<> */}
              <Grid item xs={12} md={6}>
                <TextField
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
                  inputProps={{ 'data-testid': 'firstNameInput' }}
                  FormHelperTextProps={{
                    //@ts-ignore
                    'data-testid': 'firstNameHelper'
                  }}
                  fullWidth
                  required
                  disabled={userLoading || sendLoading}
                />
              </Grid>
              {/* <>-------------------------<> LAST NAME <>-------------------------<> */}
              <Grid item xs={12} md={6}>
                <TextField
                  id="contact-last-name"
                  name="lastName"
                  label={t('common.lastName')}
                  placeholder={t('common.lastName')}
                  value={formik.values.lastName}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                  error={Boolean(
                    formik.touched.lastName && formik.errors.lastName
                  )}
                  helperText={
                    formik.touched.lastName && formik.errors.lastName
                      ? formik.errors.lastName
                      : ' '
                  }
                  inputProps={{ 'data-testid': 'lastNameInput' }}
                  FormHelperTextProps={{
                    //@ts-ignore
                    'data-testid': 'lastNameHelper'
                  }}
                  fullWidth
                  required
                  disabled={userLoading || sendLoading}
                />
              </Grid>
              {/* <>-------------------------<> PHONE NUMBER <>-------------------------<> */}
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
                    'data-testid': 'phoneNumberInput'
                  }}
                  InputProps={{ inputComponent: InputMask as any }}
                  FormHelperTextProps={{
                    //@ts-ignore
                    'data-testid': 'phoneNumberHelper'
                  }}
                  fullWidth
                  disabled={userLoading || sendLoading}
                />
              </Grid>
              {/* <>-------------------------<> ZIP CODE <>-------------------------<> */}
              <Grid item xs={12} md={6}>
                <TextField
                  id="zip-code"
                  name="zip"
                  label={t('common.zip')}
                  placeholder="00000"
                  value={formik.values.zip}
                  onChange={updateZipcode(formik.handleChange)}
                  onBlur={formik.handleBlur}
                  error={Boolean(formik.touched.zip && formik.errors.zip)}
                  helperText={
                    formik.touched.zip && formik.errors.zip
                      ? formik.errors.zip
                      : ' '
                  }
                  inputProps={{
                    'data-testid': 'zipCodeInput'
                  }}
                  FormHelperTextProps={{
                    // @ts-ignore
                    'data-testid': 'zipCodeHelper'
                  }}
                  fullWidth
                  required
                  disabled={userLoading || sendLoading}
                />
              </Grid>
              {/* <>-------------------------<> EMAIL ADDRESS <>-------------------------<> */}
              <Grid item xs={12}>
                <TextField
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
                  inputProps={{ 'data-testid': 'emailInput' }}
                  FormHelperTextProps={{
                    // @ts-ignore
                    'data-testid': 'emailHelper'
                  }}
                  fullWidth
                  required
                  disabled={userLoading || sendLoading}
                />
              </Grid>
              {/* <>-------------------------<> TOPIC <>-------------------------<> */}
              <Grid item xs={12} data-testid="topicContainer">
                <Select
                  id="topic"
                  data-testid="topicSelect"
                  name="topic"
                  label={t('common.topic')}
                  placeholder="Select"
                  value={formik.values.topic}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                  error={Boolean(formik.touched.topic && formik.errors.topic)}
                  helperText={
                    formik.touched.topic && formik.errors.topic
                      ? formik.errors.topic
                      : ' '
                  }
                  inputProps={{ 'data-testid': 'topicInput' }}
                  fullWidth
                  required
                  disabled={userLoading || sendLoading}
                >
                  {topicList.map((topic, i) => (
                    <MenuItem value={topic} key={i}>
                      {topic}
                    </MenuItem>
                  ))}
                </Select>
              </Grid>
              {/* <>-------------------------<> THE MAIN MESSAGE BOX <>-------------------------<> */}
              <Grid item xs={12}>
                <TextField
                  id="message"
                  name="message"
                  label={t('support.message')}
                  value={formik.values.message}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                  error={Boolean(
                    formik.touched.message && formik.errors.message
                  )}
                  helperText={
                    formik.touched.message && formik.errors.message
                      ? formik.errors.message
                      : ' '
                  }
                  inputProps={{ 'data-testid': 'messageInput' }}
                  FormHelperTextProps={{
                    // @ts-ignore
                    'data-testid': 'messageHelper'
                  }}
                  fullWidth
                  required
                  multiline
                  rows={3}
                  disabled={userLoading || sendLoading}
                />
              </Grid>
              {/* <>-------------------------<> SUBMIT BUTTON <>-------------------------<> */}
              <Grid container item xs={12} justifyContent="center">
                <Box width={isSmallScreen ? 0.5 : 0.25} mt={1}>
                  <LoadingButton
                    variant="contained"
                    disableElevation
                    type="submit"
                    data-testid="sendButton"
                    disabled={userLoading}
                    loading={sendLoading}
                    fullWidth
                    size="large"
                    loadingIndicator={
                      <CircularProgress color="primary02.main" size={24} />
                    }
                  >
                    {t('common.send')}
                  </LoadingButton>
                </Box>
              </Grid>
            </Grid>
          </Box>
        </Box>
      </Grid>
    </Grid>
  );
}
export default ContactFormComponent;
