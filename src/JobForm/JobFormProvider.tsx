import { useEffect, useState, useMemo, createContext, useContext } from 'react';

import * as yup from 'yup';
import { yupResolver } from '@hookform/resolvers/yup';
import { useTranslation } from 'react-i18next';

import { AuthContext } from 'AuthProvider';
import {
  characterLimits,
  FormAutocompleteOption,
  FormFields,
  initialDefaultValues,
  parseCustomerName,
  regexPatterns
} from './utils/forms';
import {
  EcommAccount,
  useUserAccountsQuery,
  useUserQuery
} from 'generated/graphql';
import Loader from 'common/Loader';
import { useDomainInfo } from 'hooks/useDomainInfo';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';

interface JobFormContextType {
  jobFormLoading: boolean;
  defaultValues: FormFields;
  resolver?: ReturnType<typeof yupResolver>;
  userAccountOptions: FormAutocompleteOption[];
  showJobFormCard: boolean;
}

export const JobFormContext = createContext<JobFormContextType>({
  jobFormLoading: true,
  defaultValues: initialDefaultValues,
  userAccountOptions: [],
  showJobFormCard: false
});

const JobFormProvider = (props: { children: React.ReactNode }) => {
  /******************************/
  /* Custom Hooks               */
  /******************************/
  const { isWaterworks } = useDomainInfo();
  const { profile } = useContext(AuthContext);
  const { t } = useTranslation();

  /******************************/
  /* State                      */
  /******************************/
  const [defaultValues, setDefaultValues] = useState(initialDefaultValues);

  /******************************/
  /* Context                    */
  /******************************/
  const {
    selectedAccounts,
    loading: selectedAccountsLoading,
    isMincron
  } = useSelectedAccountsContext();

  /******************************/
  /* API                        */
  /******************************/
  const { data: userAccountsData, loading: userAccountsLoading } =
    useUserAccountsQuery({
      skip: !profile || profile.isEmployee,
      variables: {
        userId: profile?.userId
      }
    });

  const { data: userQueryData, loading: userQueryLoading } = useUserQuery({
    variables: {
      userId: profile?.userId
    }
  });

  /******************************/
  /* Validation / Resolvers     */
  /******************************/
  const characterLimitText = (params: { max: number }) =>
    t('validation.characterLimit', params);

  const validation = {
    name: yup.string().max(characterLimits.default, characterLimitText),
    email: yup
      .string()
      .email(t('validation.emailInvalid'))
      .max(characterLimits.email, characterLimitText),
    phoneNumber: yup
      .string()
      .matches(regexPatterns.phone, {
        message: t('validation.phoneNumberInvalid'),
        excludeEmptyString: true
      })
      .max(characterLimits.phoneNumber, characterLimitText),
    address: yup.string().max(characterLimits.default, characterLimitText),
    addressOther: yup.string().max(characterLimits.default, characterLimitText),
    city: yup
      .string()
      .matches(regexPatterns.alphaOnly, {
        message: t('validation.formatAlphaInvalid'),
        excludeEmptyString: true
      })
      .max(characterLimits.city, characterLimitText),
    state: yup.string().max(characterLimits.state, characterLimitText),
    zip: yup
      .string()
      .max(characterLimits.zip, characterLimitText)
      .matches(regexPatterns.zip, {
        message: t('validation.zipInvalid'),
        excludeEmptyString: true
      }),
    currency: yup.string().matches(regexPatterns.currency, {
      message: t('validation.amountInvalid'),
      excludeEmptyString: true
    }),
    bondNumber: yup
      .string()
      .trim()
      .max(characterLimits.bondNumber, characterLimitText),
    loanNumber: yup
      .string()
      .matches(regexPatterns.numberOnly, {
        message: t('validation.formatNumericInvalid'),
        excludeEmptyString: true
      })
      .max(characterLimits.loan, characterLimitText)
  };

  const resolver = yupResolver(
    yup
      .object({
        stepOne: yup
          .object({
            customerNumber: yup
              .string()
              .required(t('validation.customerNumberRequired')),
            customerName: yup
              .string()
              .required(t('validation.customerNameRequired')),
            customerEmail: validation.email.required(
              t('validation.emailRequired')
            ),
            customerPhoneNumber: validation.phoneNumber.required(
              t('validation.phoneNumberRequired')
            ),
            projectJobName: validation.name.required(
              t('validation.jobNameRequired')
            ),
            projectLotTrack: yup.string().max(characterLimits.default),
            projectAddress: validation.address.required(
              t('validation.streetAddressRequired')
            ),
            projectAddressOther: validation.addressOther,
            projectCity: validation.city.required(t('validation.cityRequired')),
            projectState: validation.state.required(
              t('validation.stateRequired')
            ),
            projectZip: validation.zip.required(t('validation.zipRequired')),
            projectEstimate: validation.currency.required(
              t('validation.estimateRequired')
            ),
            projectTaxExempt: yup
              .string()
              .required(t('validation.taxExemptRequired')),
            projectTaxFormFile: yup.mixed().when('projectTaxExempt', {
              is: 'Yes',
              then: yup.mixed().optional()
            })
          })
          .required(),
        stepTwo: yup
          .object({
            gcContractorName: validation.name.required(
              t('validation.contractorNameRequired')
            ),
            gcAddress: validation.address.required(
              t('validation.streetAddressRequired')
            ),
            gcAddressOther: validation.addressOther,
            gcCity: validation.city.required(t('validation.cityRequired')),
            gcState: validation.state.required(t('validation.stateRequired')),
            gcZip: validation.zip.required(t('validation.zipRequired')),
            gcPhoneNumber: validation.phoneNumber.required(
              t('validation.phoneNumberRequired')
            ),
            bondingSuretyName: validation.name,
            bondingAddress: validation.address,
            bondingAddressOther: validation.addressOther,
            bondingCity: validation.city,
            bondingState: validation.state,
            bondingZip: validation.zip,
            bondingPhoneNumber: validation.phoneNumber,
            bondingBondNumber: validation.bondNumber
          })
          .required(),
        stepThree: yup
          .object({
            ownerOwnerName: validation.name.required(
              t('validation.ownerNameRequired')
            ),
            ownerAddress: validation.address.required(
              t('validation.streetAddressRequired')
            ),
            ownerAddressOther: validation.addressOther,
            ownerCity: validation.city.required(t('validation.cityRequired')),
            ownerState: validation.state.required(
              t('validation.stateRequired')
            ),
            ownerZip: validation.zip.required(t('validation.zipRequired')),
            ownerPhoneNumber: validation.phoneNumber,
            lenderLenderName: validation.name,
            lenderAddress: validation.address,
            lenderAddressOther: validation.addressOther,
            lenderCity: validation.city,
            lenderState: validation.state,
            lenderZip: validation.zip,
            lenderPhoneNumber: validation.phoneNumber,
            lenderLoanNumber: validation.loanNumber
          })
          .required()
      })
      .required()
  );

  /******************************/
  /* Memos                      */
  /******************************/
  const userAccountOptions = useMemo(() => {
    // Sort the items by their erpAccountId (remove undefined and null options)
    const formatted = [...(userAccountsData?.userAccounts ?? [])]
      .sort(
        (a, b) =>
          parseInt(a?.erpAccountId ?? '0') - parseInt(b?.erpAccountId ?? '0')
      )
      .filter((item) => item?.erpAccountId && item?.name) as EcommAccount[];

    // Turn each account into a autocomplete option
    return formatted.map((item) => ({
      value: item.erpAccountId,
      label: item.name
    })) as FormAutocompleteOption[];
  }, [userAccountsData]);

  const jobFormLoading = useMemo(
    () => selectedAccountsLoading || userAccountsLoading || userQueryLoading,
    [selectedAccountsLoading, userAccountsLoading, userQueryLoading]
  );

  /******************************/
  /* Effects                    */
  /******************************/
  useEffect(() => {
    if (selectedAccounts && !profile?.isEmployee) {
      const { billTo } = selectedAccounts;

      setDefaultValues(({ stepOne, ...rest }) => ({
        stepOne: {
          ...stepOne,
          customerName: parseCustomerName(billTo?.name ?? ''),
          customerNumber: billTo?.erpAccountId ?? ''
        },
        ...rest
      }));
    }
  }, [selectedAccounts, profile?.isEmployee]);

  useEffect(() => {
    if (userQueryData?.user) {
      const { user } = userQueryData;
      setDefaultValues(({ stepOne, ...rest }) => ({
        stepOne: {
          ...stepOne,
          customerEmail: user.email ?? '',
          customerPhoneNumber: user.phoneNumber ?? ''
        },
        ...rest
      }));
    }
  }, [userQueryData]);

  /******************************/
  /* Render                     */
  /******************************/
  return (
    <JobFormContext.Provider
      value={{
        jobFormLoading,
        defaultValues,
        resolver,
        userAccountOptions,
        showJobFormCard: !isWaterworks && !isMincron
      }}
    >
      {jobFormLoading ? <Loader /> : props.children}
    </JobFormContext.Provider>
  );
};

export const useJobFormContext = () => useContext(JobFormContext);

export default JobFormProvider;
