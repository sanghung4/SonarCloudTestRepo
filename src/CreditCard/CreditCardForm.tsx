import {
  Box,
  Button,
  Checkbox,
  Divider,
  FormControlLabel,
  FormHelperText,
  Grid,
  MenuItem,
  Select,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { creditCardFormStyles } from 'CreditCard/util/styles';
import useCreditCardForm from 'CreditCard/util/useCreditCardForm';
import { CardHolderInput } from 'generated/graphql';
import { LaunchIcon, PaymentIcon } from 'icons';
import { stateAbbreviations } from 'utils/states';
import updateZipcode from 'utils/updateZipCode';
import { useEffect } from 'react';

export type CreditCardFormProps = {
  onCancel?: () => void;
  onSubmit: (cardHolderInput: CardHolderInput, saveCard: boolean) => void;
  hideSaveCard?: boolean;
  canSaveCard?: boolean;
};

const { dividerBoxWidth, InputLabelStyled, InputStyled, stateSelectOpen } =
  creditCardFormStyles;

export default function CreditCardForm(props: CreditCardFormProps) {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Form
   */
  const { errors, handleBlur, handleChange, handleSubmit, touched, values } =
    useCreditCardForm(props.onSubmit);

  useEffect(() => {
    if (!props.canSaveCard) {
      values.saveCard = false;
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [props.canSaveCard]);

  /**
   * Render
   */
  return (
    <Box
      component="form"
      onSubmit={handleSubmit}
      noValidate
      position="relative"
    >
      <Typography variant={isSmallScreen ? 'body2' : 'body1'} pb={3}>
        {t('creditCard.enterInformation')}
      </Typography>
      <Grid container alignItems="center">
        <Grid item xs={12} md={3}>
          <InputLabelStyled htmlFor="cardHolder" shrink={false} required>
            <Typography component="span" variant="body1" color="textSecondary">
              {t('common.name')}
            </Typography>
          </InputLabelStyled>
        </Grid>
        <Grid item xs={12} md={props.hideSaveCard ? 9 : 6}>
          <InputStyled
            id="cardHolder"
            name="cardHolder"
            placeholder={t('common.name')}
            value={values.cardHolder}
            onChange={handleChange}
            onBlur={handleBlur}
            error={!!(touched.cardHolder && errors.cardHolder)}
            inputProps={{ 'data-testid': 'card-holder-input' }}
            fullWidth
            required
          />
          <FormHelperText error data-testid="card-holder-error">
            {touched.cardHolder && errors.cardHolder ? errors.cardHolder : ' '}
          </FormHelperText>
        </Grid>
        {!props.hideSaveCard && <Grid item md={3} />}
        <Grid item xs={12} md={3}>
          <InputLabelStyled htmlFor="streetAddress" shrink={false} required>
            <Typography component="span" variant="body1" color="textSecondary">
              {t('common.address')}
            </Typography>
          </InputLabelStyled>
        </Grid>
        <Grid item xs={12} md={props.hideSaveCard ? 9 : 6}>
          <InputStyled
            id="streetAddress"
            name="streetAddress"
            placeholder={t('common.address')}
            value={values.streetAddress}
            onChange={handleChange}
            onBlur={handleBlur}
            error={!!(touched.streetAddress && errors.streetAddress)}
            inputProps={{ 'data-testid': 'street-address-input' }}
            fullWidth
            required
          />
          <FormHelperText error>
            {touched.streetAddress && errors.streetAddress
              ? errors.streetAddress
              : ' '}
          </FormHelperText>
        </Grid>
        {!props.hideSaveCard && <Grid item md={3} />}
        <Grid item xs={12} md={3}>
          <InputLabelStyled htmlFor="city" shrink={false} required>
            <Typography component="span" variant="body1" color="textSecondary">
              {t('common.city')}
            </Typography>
          </InputLabelStyled>
        </Grid>
        <Grid item xs={12} md={props.hideSaveCard ? 9 : 6}>
          <InputStyled
            id="city"
            name="city"
            placeholder={t('common.city')}
            value={values.city}
            onChange={handleChange}
            onBlur={handleBlur}
            error={!!(touched.city && errors.city)}
            inputProps={{ 'data-testid': 'city-input' }}
            fullWidth
            required
          />
          <FormHelperText error>
            {touched.city && errors.city ? errors.city : ' '}
          </FormHelperText>
        </Grid>
        {!props.hideSaveCard && <Grid item md={3} />}
        <Grid item xs={12} md={3}>
          <InputLabelStyled htmlFor="postalCode" shrink={false} required>
            <Typography component="span" variant="body1" color="textSecondary">
              {t('common.stateZip')}
            </Typography>
          </InputLabelStyled>
        </Grid>
        <Grid item xs={12} md={props.hideSaveCard ? 9 : 6}>
          <Grid container spacing={isSmallScreen ? 2 : 4}>
            <Grid item xs={6} md={5}>
              <Select
                id="state"
                name="state"
                placeholder={t('common.state')}
                value={values.state}
                onChange={handleChange}
                onBlur={handleBlur}
                error={!!(touched.state && errors.state)}
                helperText={touched.state && errors.state ? errors.state : ' '}
                inputProps={{ 'data-testid': 'state-input' }}
                fullWidth
                required
                sx={[
                  { mt: isSmallScreen ? 1 : 2 },
                  !values.state && stateSelectOpen
                ]}
              >
                {stateAbbreviations.map((state) => (
                  <MenuItem key={state} value={state}>
                    {state}
                  </MenuItem>
                ))}
              </Select>
            </Grid>
            <Grid item xs={6} md={7}>
              <InputStyled
                id="postalCode"
                name="postalCode"
                placeholder={t('common.zip')}
                value={values.postalCode}
                onChange={updateZipcode(handleChange)}
                onBlur={handleBlur}
                error={!!(touched.postalCode && errors.postalCode)}
                inputProps={{ 'data-testid': 'postal-code-input' }}
                fullWidth
                required
              />
              <FormHelperText error>
                {touched.postalCode && errors.postalCode
                  ? errors.postalCode
                  : ' '}
              </FormHelperText>
            </Grid>
          </Grid>
        </Grid>
        {!props.hideSaveCard && <Grid item md={3} />}
        {!props.hideSaveCard && (
          <>
            <Grid item md={3} />
            <Grid
              container
              item
              xs={12}
              md={6}
              justifyContent={isSmallScreen ? 'flex-start' : 'center'}
              alignItems="center"
            >
              <FormControlLabel
                control={
                  <Checkbox
                    id="saveCard"
                    name="saveCard"
                    checked={values.saveCard}
                    onChange={handleChange}
                    color="primary"
                  />
                }
                label={t('creditCard.saveCard') as string}
              />
            </Grid>
            <Grid item md={3} />
          </>
        )}
        <Grid item md={3} />
        <Grid container item xs={12} md={props.hideSaveCard ? 9 : 6}>
          <Box
            display="inline-flex"
            flexWrap="wrap"
            mt={isSmallScreen ? 4 : 3}
            width={1}
          >
            <Box
              display="inline-flex"
              mr={isSmallScreen ? 0 : 2}
              mb={isSmallScreen ? 2 : 0}
              width={isSmallScreen ? 1 : 'auto'}
            >
              {props.canSaveCard ? (
                <Button
                  type="submit"
                  variant="alternative"
                  fullWidth={isSmallScreen}
                  startIcon={<LaunchIcon />}
                  data-testid="add-new-card-button"
                >
                  {t('creditCard.addCard')}
                </Button>
              ) : (
                <Grid container item direction="column" spacing={2}>
                  <Grid item>
                    <Button
                      type="submit"
                      variant="alternative"
                      fullWidth={isSmallScreen}
                      startIcon={<PaymentIcon />}
                      data-testid="enter-credit-card-button"
                    >
                      {t('creditCard.enderCreditCardNumber')}
                    </Button>
                  </Grid>
                  <Grid item>
                    <FormHelperText>
                      {t('creditCard.reviewOrder')}
                    </FormHelperText>
                  </Grid>
                </Grid>
              )}
            </Box>
            {!!props.onCancel && (
              <Button
                variant="text"
                color="primaryLight"
                fullWidth={isSmallScreen}
                onClick={props.onCancel}
                data-testid="cancel-add-new-card-button"
              >
                {t('common.cancel')}
              </Button>
            )}
          </Box>
        </Grid>
      </Grid>
      {!props.hideSaveCard && (
        <Box ml={isSmallScreen ? -2 : -4} pt={8} width={dividerBoxWidth}>
          <Divider light />
        </Box>
      )}
    </Box>
  );
}
