export type FormAutocompleteOption = { value: string; label: string };

export interface FormFields {
  stepOne: {
    customerName: string;
    customerNumber: string;
    customerEmail: string;
    customerPhoneNumber: string;
    projectJobName: string;
    projectLotTrack: string;
    projectAddress: string;
    projectAddressOther: string;
    projectCity: string;
    projectState: string;
    projectZip: string;
    projectEstimate: string;
    projectTaxExempt: string;
    projectTaxFormFile: File | null;
  };
  stepTwo: {
    gcContractorName: string;
    gcAddress: string;
    gcAddressOther: string;
    gcCity: string;
    gcState: string;
    gcZip: string;
    gcPhoneNumber: string;
    bondingSuretyName: string;
    bondingAddress: string;
    bondingAddressOther: string;
    bondingCity: string;
    bondingState: string;
    bondingZip: string;
    bondingPhoneNumber: string;
    bondingBondNumber: string;
  };
  stepThree: {
    ownerOwnerName: string;
    ownerAddress: string;
    ownerAddressOther: string;
    ownerCity: string;
    ownerState: string;
    ownerZip: string;
    ownerPhoneNumber: string;
    lenderLenderName: string;
    lenderAddress: string;
    lenderAddressOther: string;
    lenderCity: string;
    lenderState: string;
    lenderZip: string;
    lenderPhoneNumber: string;
    lenderLoanNumber: string;
  };
}

export const initialDefaultValues: FormFields = {
  stepOne: {
    customerNumber: '',
    customerName: '',
    customerEmail: '',
    customerPhoneNumber: '',
    projectJobName: '',
    projectLotTrack: '',
    projectAddress: '',
    projectAddressOther: '',
    projectCity: '',
    projectState: '',
    projectZip: '',
    projectEstimate: '',
    projectTaxExempt: '',
    projectTaxFormFile: null
  },
  stepTwo: {
    gcContractorName: '',
    gcAddress: '',
    gcAddressOther: '',
    gcCity: '',
    gcState: '',
    gcZip: '',
    gcPhoneNumber: '',
    bondingSuretyName: '',
    bondingAddress: '',
    bondingAddressOther: '',
    bondingCity: '',
    bondingState: '',
    bondingZip: '',
    bondingPhoneNumber: '',
    bondingBondNumber: ''
  },
  stepThree: {
    ownerOwnerName: '',
    ownerAddress: '',
    ownerAddressOther: '',
    ownerCity: '',
    ownerState: '',
    ownerZip: '',
    ownerPhoneNumber: '',
    lenderLenderName: '',
    lenderAddress: '',
    lenderAddressOther: '',
    lenderCity: '',
    lenderState: '',
    lenderZip: '',
    lenderPhoneNumber: '',
    lenderLoanNumber: ''
  }
};

export const characterLimits = {
  default: 35,
  email: 53,
  phoneNumber: 20,
  city: 20,
  state: 2,
  zip: 5,
  bondNumber: 14,
  currency: 20,
  loan: 10
};

export const parseCustomerName = (name: string) => {
  const separator = ' - ';
  const formattedName = name.split(separator);
  formattedName.shift();
  return name.includes(separator)
    ? formattedName.join(separator).slice(0, characterLimits.default)
    : name;
};

export const regexPatterns = {
  alphaOnly: /^([^0-9]*)$/,
  currency: /^\$\d{1,18}.\d{2}$/,
  numberOnly: /^[0-9]*$/,
  phone: /^\(\d{3}\)\s{1}\d{3}-\d{4}$/,
  zip: /^(\d{5})?$/,
  atLeastEightCharacters: /^.{8,}$/,
  atLeastOneUpperCaseLetter: /^(?=.*[A-Z])/,
  atLeastOneNumber: /^(?=.*\d)/
};
