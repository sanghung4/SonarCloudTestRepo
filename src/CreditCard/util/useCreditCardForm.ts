import { FN } from '@reece/global-types';
import { useFormik } from 'formik';
import { useTranslation } from 'react-i18next';
import * as Yup from 'yup';

import { CardHolderInput } from 'generated/graphql';

const initialValues = {
  cardHolder: '',
  streetAddress: '',
  city: '',
  state: '',
  postalCode: '',
  saveCard: true
};
type CCForm = typeof initialValues;

export default function useCreditCardForm(
  submitFn: FN<[CardHolderInput, boolean]>
) {
  /**
   * Translation
   */
  const { t } = useTranslation();

  /**
   * Options
   */
  const validationSchema = Yup.object({
    cardHolder: Yup.string().required(t('validation.nameRequired')),
    streetAddress: Yup.string().required(t('validation.addressRequired')),
    city: Yup.string()
      .matches(/^[a-zA-Z]+[a-zA-Z. \-']*$/, {
        message: t('validation.cityInvalid')
      })
      .required(t('validation.cityRequired')),
    state: Yup.string().required(t('validation.stateRequired')),
    postalCode: Yup.string()
      .matches(/^\d{5}$/, { message: t('validation.zipInvalid') })
      .required(t('validation.zipRequired'))
  });
  const onSubmit = submitCreditCardForm(submitFn);

  /**
   * Form
   */
  return useFormik({ initialValues, validationSchema, onSubmit });
}

export function submitCreditCardForm(submitFn: FN<[CardHolderInput, boolean]>) {
  return ({ city, state, saveCard, ...cardHolderInput }: CCForm) => {
    const returnUrl = `${window.location.origin}/credit_callback`;
    submitFn({ ...cardHolderInput, returnUrl }, saveCard);
  };
}
