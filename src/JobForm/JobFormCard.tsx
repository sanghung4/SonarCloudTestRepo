import { useContext, useEffect, useState } from 'react';
import {
  Box,
  Button,
  Grid,
  LoadingButton,
  Step,
  StepLabel,
  Stepper,
  Typography,
  useScreenSize,
  useSnackbar
} from '@dialexa/reece-component-library';
import { useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';

import {
  FormRequiredText,
  FormSectionLabel,
  FormSection,
  FormActionContainer,
  FormSectionFields,
  JobFormContainer,
  JobStepperWrapper,
  FormBackButton,
  FormSectionDetails
} from './utils/styled';
import Loader from 'common/Loader';
import { characterLimits, FormFields, parseCustomerName } from './utils/forms';
import FormStepContainer from './FormStepContainer';
import JobFormTextInput from './JobFormTextInput';
import JobFormSelectInput from './JobFormSelectInput';
import JobFormRadioInput from './JobFormRadioInput';
import JobFormFileInput from './JobFormFileInput';
import JobFormAutocompleteInput from './JobFormAutocompleteInput';
import { stateAbbreviationOptions } from 'utils/states';
import { useJobFormContext } from './JobFormProvider';
import { AuthContext } from 'AuthProvider';
import {
  JobFormInput,
  ErpSystemEnum,
  useCreateJobFormMutation,
  useEntitySearchLazyQuery,
  EntitySearchQuery
} from 'generated/graphql';
import { testIds } from 'test-utils/testIds';
import { PageHeaderCard } from './utils/styled';
import trimSpaces from 'utils/trimSpaces';

interface JobFormContentProps {
  onSubmitCompleted: () => void;
  setCustomerDetails: (customerDetails: string) => void;
}

function JobFormContent({
  onSubmitCompleted,
  setCustomerDetails
}: JobFormContentProps) {
  /**
   * Context
   */
  const { defaultValues, resolver, userAccountOptions } = useJobFormContext();
  const { profile } = useContext(AuthContext);

  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { pushAlert } = useSnackbar();
  const { t } = useTranslation();
  const {
    clearErrors,
    control,
    getValues,
    handleSubmit,
    resetField,
    setError,
    setValue,
    trigger,
    reset,
    watch
  } = useForm<FormFields>({
    defaultValues,
    resolver: resolver,
    mode: 'onTouched',
    reValidateMode: 'onSubmit'
  });

  /**
   * State
   */
  const [uploadRequired, setUploadRequired] = useState(false);
  const [currentStep, setCurrentStep] = useState(0);

  /**
   * API
   */
  const [getEntity, { loading: getEntityLoading }] = useEntitySearchLazyQuery({
    onCompleted: onGetEntityQueryCompleted,
    onError: () => {
      setError('stepOne.customerNumber', {
        type: 'invalid',
        message: t('validation.customerNumberInvalid')
      });
    },
    fetchPolicy: 'network-only',
    notifyOnNetworkStatusChange: true
  });

  const [createJobForm, { loading: createJobFormLoading }] =
    useCreateJobFormMutation({
      onCompleted: (jobFormResults) => {
        reset();
        setCurrentStep(0);
        setUploadRequired(false);
        scrollToTop();
        onSubmitCompleted();
      },
      onError: (e) => {
        console.error(e.message);
        pushAlert(t('common.messageFail'), { variant: 'error' });
      }
    });

  /**
   * Effects
   */
  // eslint-disable-next-line react-hooks/exhaustive-deps
  useEffect(watchValuesEffect, [t, profile, userAccountOptions]);
  // eslint-disable-next-line react-hooks/exhaustive-deps
  useEffect(setCustomerValuesEffect, [defaultValues]);

  /**
   * Render
   */
  return (
    <Box
      component="form"
      onSubmit={handleSubmit(submitJobForm, submitJobFormError)}
      data-testid={testIds.JobForm.JobFormCard.page}
    >
      {createJobFormLoading && <Loader backdrop size="page" />}

      <PageHeaderCard data-testid="job-form-header">
        <Typography variant="h5">
          {currentStep && profile?.isEmployee
            ? t('dynamicPageTitles.jobForm', {
                companyName: getValues('stepOne.customerName')
              })
            : t('common.jobForm')}
        </Typography>
      </PageHeaderCard>

      <JobFormContainer>
        {/* Stepper */}
        <JobStepperWrapper>
          <Stepper
            activeStep={currentStep}
            background="gray"
            hideLabels
            highlightCompleted={false}
          >
            {[...Array(3)].map((_, index) => (
              <Step key={index}>
                <StepLabel>&nbsp;</StepLabel>
              </Step>
            ))}
          </Stepper>
          <Typography color="primary02.main" fontWeight="500">
            Step {currentStep + 1} of 3
          </Typography>
        </JobStepperWrapper>
        {/* Step 1 */}
        <FormStepContainer
          show={currentStep === 0}
          testId={testIds.JobForm.JobFormCard.stepOne}
        >
          {/* Job Information Section */}
          <FormSection>
            <FormSectionLabel>
              {t('jobForm.customerInformation')}
            </FormSectionLabel>
            <FormRequiredText>{t('jobForm.fieldsRequired')}</FormRequiredText>
            {/* Fields */}
            <FormSectionFields>
              {/* Customer Number */}
              {profile?.isEmployee ||
              (!profile?.isEmployee && userAccountOptions.length === 1) ? (
                <JobFormTextInput
                  mask="number"
                  control={control}
                  readOnly={userAccountOptions.length === 1}
                  disabled={userAccountOptions.length === 1}
                  label={t('jobForm.customerNumber')}
                  tooltip={t('jobForm.customerNumberTooltip')}
                  name="stepOne.customerNumber"
                  testId={testIds.JobForm.JobFormCard.customerNumberEmployee}
                  required
                />
              ) : (
                <JobFormAutocompleteInput
                  control={control}
                  label={t('jobForm.customerNumber')}
                  tooltip={t('jobForm.customerNumberTooltip')}
                  name="stepOne.customerNumber"
                  options={userAccountOptions}
                  testId={testIds.JobForm.JobFormCard.customerNumberCustomer}
                  required
                />
              )}
              {/* Customer Name */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.customerName')}
                name="stepOne.customerName"
                testId={testIds.JobForm.JobFormCard.customerName}
                disabled
                required
              />
              {/* Customer Lookup Actions (Employee Only) */}
              {profile?.isEmployee && (
                <Box
                  display="flex"
                  justifyContent="right"
                  paddingY={isSmallScreen ? 1 : 0}
                >
                  <Button
                    variant="secondary"
                    onClick={handleResetClick}
                    sx={{ marginRight: 2 }}
                    data-testid={testIds.JobForm.JobFormCard.customerNameReset}
                  >
                    {t('common.reset')}
                  </Button>
                  <LoadingButton
                    variant="contained"
                    loading={getEntityLoading}
                    onClick={handleLookupCustomerClick}
                    disableElevation
                    data-testid={testIds.JobForm.JobFormCard.customerNameLookup}
                  >
                    {t('jobForm.customerLookup')}
                  </LoadingButton>
                </Box>
              )}
              {/* Customer Email */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.email')}
                name="stepOne.customerEmail"
                testId={testIds.JobForm.JobFormCard.customerEmail}
                required
              />
              {/* Customer Phone Number */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.phoneNumber')}
                name="stepOne.customerPhoneNumber"
                mask="phone"
                mdFieldSpan={4}
                xsFieldSpan={6}
                testId={testIds.JobForm.JobFormCard.customerPhone}
                required
              />
            </FormSectionFields>
          </FormSection>
          {/* Project Information Section */}
          <FormSection>
            <FormSectionLabel>
              {t('jobForm.projectInformation')}
            </FormSectionLabel>
            {/* Fields */}
            <FormSectionFields>
              {/* Project Job Name */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.projectJobName')}
                name="stepOne.projectJobName"
                testId={testIds.JobForm.JobFormCard.projectJobName}
                required
              />
              {/* Project Lot & Tracking */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.projectLot')}
                name="stepOne.projectLotTrack"
                testId={testIds.JobForm.JobFormCard.projectLot}
              />
              {/* Project Street Address */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.streetAddress')}
                name="stepOne.projectAddress"
                testId={testIds.JobForm.JobFormCard.projectAddressOne}
                required
              />
              {/* Project Street Address 2 */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.apartmentSuiteOther')}
                name="stepOne.projectAddressOther"
                testId={testIds.JobForm.JobFormCard.projectAddressTwo}
              />
              {/* Project City */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.city')}
                name="stepOne.projectCity"
                mdFieldSpan={5}
                xsFieldSpan={6}
                testId={testIds.JobForm.JobFormCard.projectCity}
                required
              />
              {/* State / Zip Row */}
              <Grid
                container
                columns={10}
                columnSpacing={isSmallScreen ? 3 : 0}
              >
                {/* Project State */}
                <Grid item md={6} xs={4}>
                  <JobFormSelectInput
                    control={control}
                    label={t('jobForm.state')}
                    name="stepOne.projectState"
                    options={stateAbbreviationOptions}
                    mdLabelSpan={5}
                    mdFieldSpan={4}
                    testId={testIds.JobForm.JobFormCard.projectState}
                    required
                  />
                </Grid>
                {/* Project Zip */}
                <Grid item md={4} xs={6}>
                  <JobFormTextInput
                    control={control}
                    label={t('jobForm.zip')}
                    name="stepOne.projectZip"
                    mask="zipcode"
                    placeholder="00000"
                    testId={testIds.JobForm.JobFormCard.projectZip}
                    maxLength={characterLimits.zip}
                    required
                  />
                </Grid>
              </Grid>

              {/* Project Estimate */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.projectEstimate')}
                name="stepOne.projectEstimate"
                mask="currency"
                mdFieldSpan={4}
                xsFieldSpan={6}
                testId={testIds.JobForm.JobFormCard.projectEstimate}
                required
              />

              {/* Project Tax Exempt */}
              <JobFormRadioInput
                control={control}
                label={t('jobForm.projectTaxExempt')}
                name="stepOne.projectTaxExempt"
                details={t('jobForm.projectTaxExemptDetails')}
                options={[
                  { label: t('common.yes'), value: 'yes' },
                  { label: t('common.no'), value: 'no' }
                ]}
                testId={testIds.JobForm.JobFormCard.projectTaxExempt}
                required
              />

              {/* Project Tax File */}
              {uploadRequired && (
                <JobFormFileInput
                  name="stepOne.projectTaxFormFile"
                  control={control}
                  label={t('jobForm.projectUpload')}
                  onFileReset={handleFileReset}
                  testId={testIds.JobForm.JobFormCard.projectTaxFile}
                />
              )}
            </FormSectionFields>
          </FormSection>
          {/* Actions */}
          <FormActionContainer>
            <Button
              onClick={handleStepOneContinueClick}
              data-testid={testIds.JobForm.JobFormCard.stepOneContinue}
            >
              {t('common.continue')}
            </Button>
          </FormActionContainer>
        </FormStepContainer>
        {/* Step 2 */}
        <FormStepContainer
          show={currentStep === 1}
          testId={testIds.JobForm.JobFormCard.stepTwo}
        >
          {/* General Contractor Information Section */}
          <FormSection>
            <FormSectionLabel>
              {t('jobForm.contractorInformation')}
            </FormSectionLabel>
            {/* Fields */}
            <FormSectionFields>
              {/* Contactor Name */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.contractorName')}
                name="stepTwo.gcContractorName"
                testId={testIds.JobForm.JobFormCard.contractorName}
                required
              />
              {/* Street Address */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.streetAddress')}
                name="stepTwo.gcAddress"
                testId={testIds.JobForm.JobFormCard.contractorAddressOne}
                required
              />
              {/* Other Address */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.apartmentSuiteOther')}
                name="stepTwo.gcAddressOther"
                testId={testIds.JobForm.JobFormCard.contractorAddressTwo}
              />
              {/* City */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.city')}
                name="stepTwo.gcCity"
                mdFieldSpan={5}
                xsFieldSpan={6}
                testId={testIds.JobForm.JobFormCard.contractorCity}
                required
              />
              {/* State / Zip Row */}
              <Grid
                container
                columns={10}
                columnSpacing={isSmallScreen ? 3 : 0}
              >
                {/* Project State */}
                <Grid item md={6} xs={4}>
                  <JobFormSelectInput
                    control={control}
                    label={t('jobForm.state')}
                    name="stepTwo.gcState"
                    options={stateAbbreviationOptions}
                    mdLabelSpan={5}
                    mdFieldSpan={4}
                    testId={testIds.JobForm.JobFormCard.contractorState}
                    required
                  />
                </Grid>
                {/* Project Zip */}
                <Grid item md={4} xs={6}>
                  <JobFormTextInput
                    control={control}
                    label={t('jobForm.zip')}
                    mask="zipcode"
                    name="stepTwo.gcZip"
                    testId={testIds.JobForm.JobFormCard.contractorZip}
                    required
                    maxLength={characterLimits.zip}
                  />
                </Grid>
              </Grid>

              {/* Phone Number */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.phoneNumber')}
                name="stepTwo.gcPhoneNumber"
                mask="phone"
                mdFieldSpan={4}
                xsFieldSpan={6}
                testId={testIds.JobForm.JobFormCard.contractorPhone}
                required
              />
            </FormSectionFields>
          </FormSection>
          {/* Bonding Information Section */}
          <FormSection>
            <FormSectionLabel>
              {t('jobForm.bondingInformation')}
            </FormSectionLabel>
            <FormSectionDetails>
              {t('jobForm.bondingStateFederal')}
            </FormSectionDetails>
            {/* Fields */}
            <FormSectionFields>
              {/* Surety Name */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.bondingSuretyName')}
                tooltip={t('jobForm.bondingSuretyNameTooltip')}
                name="stepTwo.bondingSuretyName"
                testId={testIds.JobForm.JobFormCard.bondingName}
              />
              {/* Street Address */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.streetAddress')}
                name="stepTwo.bondingAddress"
                testId={testIds.JobForm.JobFormCard.bondingAddressOne}
              />
              {/* Other Address */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.apartmentSuiteOther')}
                name="stepTwo.bondingAddressOther"
                testId={testIds.JobForm.JobFormCard.bondingAddressTwo}
              />
              {/* City */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.city')}
                name="stepTwo.bondingCity"
                mdFieldSpan={5}
                xsFieldSpan={6}
                testId={testIds.JobForm.JobFormCard.bondingCity}
              />
              {/* State / Zip Row */}
              <Grid
                container
                columns={10}
                columnSpacing={isSmallScreen ? 3 : 0}
              >
                {/* Project State */}
                <Grid item md={6} xs={4}>
                  <JobFormSelectInput
                    control={control}
                    label={t('jobForm.state')}
                    name="stepTwo.bondingState"
                    options={stateAbbreviationOptions}
                    mdLabelSpan={5}
                    mdFieldSpan={4}
                    testId={testIds.JobForm.JobFormCard.bondingState}
                  />
                </Grid>
                {/* Project Zip */}
                <Grid item md={4} xs={6}>
                  <JobFormTextInput
                    control={control}
                    label={t('jobForm.zip')}
                    name="stepTwo.bondingZip"
                    testId={testIds.JobForm.JobFormCard.bondingZip}
                    maxLength={characterLimits.zip}
                    mask="zipcode"
                  />
                </Grid>
              </Grid>
              {/* Phone Number */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.phoneNumber')}
                name="stepTwo.bondingPhoneNumber"
                mask="phone"
                mdFieldSpan={4}
                xsFieldSpan={6}
                testId={testIds.JobForm.JobFormCard.bondingPhone}
              />
              {/* Bond Number */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.bondingBondNumber')}
                tooltip={t('jobForm.bondingBondNumberTooltip')}
                name="stepTwo.bondingBondNumber"
                testId={testIds.JobForm.JobFormCard.bondingBond}
              />
            </FormSectionFields>
          </FormSection>
          {/* Actions */}
          <FormActionContainer>
            <FormBackButton
              variant="inline"
              onClick={handleBackClick}
              data-testid={testIds.JobForm.JobFormCard.stepTwoBack}
            >
              {t('common.back')}
            </FormBackButton>
            <Button
              onClick={handleStepTwoContinueClick}
              data-testid={testIds.JobForm.JobFormCard.stepTwoContinue}
            >
              {t('common.continue')}
            </Button>
          </FormActionContainer>
        </FormStepContainer>
        {/* Step 3 */}
        <FormStepContainer
          show={currentStep === 2}
          testId={testIds.JobForm.JobFormCard.stepThree}
        >
          {/* Owner Information Section */}
          <FormSection>
            <FormSectionLabel>{t('jobForm.ownerInformation')}</FormSectionLabel>
            {/* Fields */}
            <FormSectionFields>
              {/* Owner Name */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.ownerName')}
                name="stepThree.ownerOwnerName"
                testId={testIds.JobForm.JobFormCard.ownerName}
                required
              />
              {/* Street Address */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.streetAddress')}
                name="stepThree.ownerAddress"
                testId={testIds.JobForm.JobFormCard.ownerAddressOne}
                required
              />
              {/* Other Address */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.apartmentSuiteOther')}
                name="stepThree.ownerAddressOther"
                testId={testIds.JobForm.JobFormCard.ownerAddressTwo}
              />
              {/* City */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.city')}
                name="stepThree.ownerCity"
                mdFieldSpan={5}
                xsFieldSpan={6}
                testId={testIds.JobForm.JobFormCard.ownerCity}
                required
              />
              {/* State / Zip Row */}
              <Grid
                container
                columns={10}
                columnSpacing={isSmallScreen ? 3 : 0}
              >
                {/* Owner State */}
                <Grid item md={6} xs={4}>
                  <JobFormSelectInput
                    control={control}
                    label={t('jobForm.state')}
                    name="stepThree.ownerState"
                    options={stateAbbreviationOptions}
                    mdLabelSpan={5}
                    mdFieldSpan={4}
                    testId={testIds.JobForm.JobFormCard.ownerState}
                    required
                  />
                </Grid>
                {/* Owner Zip */}
                <Grid item md={4} xs={6}>
                  <JobFormTextInput
                    control={control}
                    label={t('jobForm.zip')}
                    name="stepThree.ownerZip"
                    testId={testIds.JobForm.JobFormCard.ownerZip}
                    required
                    maxLength={characterLimits.zip}
                    mask="zipcode"
                  />
                </Grid>
              </Grid>
              {/* Phone Number */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.phoneNumber')}
                name="stepThree.ownerPhoneNumber"
                mask="phone"
                mdFieldSpan={4}
                xsFieldSpan={6}
                testId={testIds.JobForm.JobFormCard.ownerPhone}
              />
            </FormSectionFields>
          </FormSection>
          {/* Lender Information Section */}
          <FormSection>
            <FormSectionLabel>
              {t('jobForm.lenderInformation')}
            </FormSectionLabel>
            {/* Fields */}
            <FormSectionFields>
              {/* Lender Name */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.lenderName')}
                tooltip={t('jobForm.lenderNameTooltip')}
                name="stepThree.lenderLenderName"
                testId={testIds.JobForm.JobFormCard.lenderName}
              />
              {/* Street Address */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.streetAddress')}
                name="stepThree.lenderAddress"
                testId={testIds.JobForm.JobFormCard.lenderAddressOne}
              />
              {/* Other Address */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.apartmentSuiteOther')}
                name="stepThree.lenderAddressOther"
                testId={testIds.JobForm.JobFormCard.lenderAddressTwo}
              />
              {/* City */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.city')}
                name="stepThree.lenderCity"
                mdFieldSpan={5}
                xsFieldSpan={6}
                testId={testIds.JobForm.JobFormCard.lenderCity}
              />
              {/* State / Zip Row */}
              <Grid
                container
                columns={10}
                columnSpacing={isSmallScreen ? 3 : 0}
              >
                {/* Lender State */}
                <Grid item md={6} xs={4}>
                  <JobFormSelectInput
                    control={control}
                    label={t('jobForm.state')}
                    name="stepThree.lenderState"
                    options={stateAbbreviationOptions}
                    mdLabelSpan={5}
                    mdFieldSpan={4}
                    testId={testIds.JobForm.JobFormCard.lenderState}
                  />
                </Grid>
                {/* Lender Zip */}
                <Grid item md={4} xs={6}>
                  <JobFormTextInput
                    control={control}
                    label={t('jobForm.zip')}
                    name="stepThree.lenderZip"
                    testId={testIds.JobForm.JobFormCard.lenderZip}
                    maxLength={characterLimits.zip}
                    mask="zipcode"
                  />
                </Grid>
              </Grid>
              {/* Phone Number */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.phoneNumber')}
                name="stepThree.lenderPhoneNumber"
                mask="phone"
                mdFieldSpan={4}
                xsFieldSpan={6}
                testId={testIds.JobForm.JobFormCard.lenderPhone}
              />
              {/* Loan Number */}
              <JobFormTextInput
                control={control}
                label={t('jobForm.lenderLoanNumber')}
                tooltip={t('jobForm.lenderLoanNumberTooltip')}
                name="stepThree.lenderLoanNumber"
                testId={testIds.JobForm.JobFormCard.lenderLoan}
              />
            </FormSectionFields>
          </FormSection>
          {/* Actions */}
          <FormActionContainer>
            <FormBackButton
              variant="inline"
              onClick={handleBackClick}
              data-testid={testIds.JobForm.JobFormCard.stepThreeBack}
            >
              {t('common.back')}
            </FormBackButton>
            <Button
              type="submit"
              data-testid={testIds.JobForm.JobFormCard.stepThreeContinue}
            >
              {t('common.submit')}
            </Button>
          </FormActionContainer>
        </FormStepContainer>
      </JobFormContainer>
    </Box>
  );

  /**
   * Effect Definitions
   */
  function watchValuesEffect() {
    const subscription = watch((value, { name }) => {
      // If the project is tax exempt, show the upload file input
      if (name === 'stepOne.projectTaxExempt') {
        const isTaxExempt = value.stepOne?.projectTaxExempt === 'yes';
        setUploadRequired(isTaxExempt);
        isTaxExempt && handleFileReset();
      }
    });

    return () => subscription.unsubscribe();
  }

  function setCustomerValuesEffect() {
    const assignedValues = { ...defaultValues };

    if (defaultValues.stepOne.customerNumber && !profile?.isEmployee) {
      const selectedCustomer = userAccountOptions.find(
        (opt) => opt.value === defaultValues.stepOne.customerNumber
      );
      assignedValues.stepOne.customerName = parseCustomerName(
        selectedCustomer?.label ?? ''
      );
    }
    reset(assignedValues);
  }

  /**
   * Callback Definitions
   */
  function onGetEntityQueryCompleted(data: EntitySearchQuery) {
    if (
      data?.entitySearch &&
      data.entitySearch.companyName &&
      data.entitySearch.erpAccountId &&
      data.entitySearch.erpName === ErpSystemEnum.Eclipse &&
      data.entitySearch.isBillTo
    ) {
      setValue('stepOne.customerName', data.entitySearch.companyName);
      setValue('stepOne.customerNumber', data.entitySearch.erpAccountId);
      setCustomerDetails &&
        setCustomerDetails(
          data.entitySearch.erpAccountId + '-' + data.entitySearch.companyName
        );
    } else {
      setError('stepOne.customerNumber', {
        type: 'invalid',
        message: t('validation.customerNumberInvalid')
      });
    }
  }

  function handleFileReset() {
    resetField('stepOne.projectTaxFormFile');
  }

  function handleResetClick() {
    resetField('stepOne.customerNumber');
    resetField('stepOne.customerName');
  }

  async function handleLookupCustomerClick() {
    clearErrors(['stepOne.customerNumber', 'stepOne.customerName']);

    const isValid = await trigger('stepOne.customerNumber');
    const customerNumber = getValues('stepOne.customerNumber');
    if (isValid && customerNumber) {
      getEntity({ variables: { accountId: customerNumber } });
    }
  }

  function handleStepOneContinueClick() {
    // Move on to step two if step one is valid
    trigger('stepOne').then((valid) => {
      valid && setCurrentStep(1);
      valid && scrollToTop();
    });
  }

  function handleStepTwoContinueClick() {
    trigger('stepTwo').then((valid) => {
      valid && setCurrentStep(2);
      valid && scrollToTop();
    });
  }

  function handleBackClick() {
    setCurrentStep((prev) => --prev);
  }

  function submitJobFormError() {
    setTimeout(() => {
      const errorEl = document.querySelector('.MuiFormLabel-root.Mui-error');
      if (errorEl) {
        const offset = isSmallScreen ? 16 : 24;
        window.scrollTo({
          top: errorEl.getBoundingClientRect().top + window.scrollY - offset,
          behavior: 'smooth'
        });
      }
    }, 1);
  }

  function submitJobForm({ stepOne, stepTwo, stepThree }: FormFields) {
    // Set Variables for create call
    const jobFormInput: JobFormInput = {
      bonding: {
        bondNumber: trimSpaces(stepTwo.bondingBondNumber),
        city: stepTwo.bondingCity,
        phoneNumber: stepTwo.bondingPhoneNumber,
        postalCode: stepTwo.bondingZip,
        state: stepTwo.bondingState,
        streetLineOne: stepTwo.bondingAddress,
        streetLineTwo: stepTwo.bondingAddressOther,
        suretyName: stepTwo.bondingSuretyName
      },
      generalContractor: {
        city: stepTwo.gcCity,
        generalContractor: stepTwo.gcContractorName,
        phoneNumber: stepTwo.gcPhoneNumber,
        postalCode: stepTwo.gcZip,
        state: stepTwo.gcState,
        streetLineOne: stepTwo.gcAddress,
        streetLineTwo: stepTwo.gcAddressOther
      },
      job: {
        customerName: stepOne.customerName,
        customerNumber: stepOne.customerNumber,
        email: stepOne.customerEmail,
        phoneNumber: stepOne.customerPhoneNumber,
        userName: stepOne.customerEmail
      },
      lender: {
        city: stepThree.lenderCity,
        lenderName: stepThree.lenderLenderName,
        loanNumber: stepThree.lenderLoanNumber,
        phoneNumber: stepThree.lenderPhoneNumber,
        postalCode: stepThree.lenderZip,
        state: stepThree.lenderState,
        streetLineOne: stepThree.lenderAddress,
        streetLineTwo: stepThree.lenderAddressOther
      },
      owner: {
        city: stepThree.ownerCity,
        ownerName: stepThree.ownerOwnerName,
        phoneNumber: stepThree.ownerPhoneNumber,
        postalCode: stepThree.ownerZip,
        state: stepThree.ownerState,
        streetLineOne: stepThree.ownerAddress,
        streetLineTwo: stepThree.ownerAddressOther
      },
      project: {
        city: stepOne.projectCity,
        estimatedProjectAmount: parseFloat(
          stepOne.projectEstimate.replace(/[$,]/, '')
        ),
        jobName: stepOne.projectJobName,
        lotNoAndTrack: stepOne.projectLotTrack,
        postalCode: stepOne.projectZip,
        state: stepOne.projectState,
        streetLineOne: stepOne.projectAddress,
        streetLineTwo: stepOne.projectAddressOther,
        taxable: stepOne.projectTaxExempt === t('common.no')
      }
    };

    createJobForm({
      variables: { file: stepOne.projectTaxFormFile ?? undefined, jobFormInput }
    });
  }

  function scrollToTop() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }
}

export default JobFormContent;
