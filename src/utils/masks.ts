import { InputHTMLAttributes } from 'react';

import IMask from 'imask';
import { format, parse } from 'date-fns';

type MaskProps = IMask.AnyMaskedOptions & InputHTMLAttributes<HTMLInputElement>;

export const phoneMask: MaskProps = {
  mask: '(000) 000-0000',
  placeholder: '(000) 000-0000'
};

export const dateMask: MaskProps = {
  mask: Date,
  pattern: 'm{/}`d{/}`Y',
  format: (date) => format(date, 'P'),
  parse: (date) => parse(date, 'P', new Date())
};

export const currencyMask: MaskProps = {
  mask: '$num',
  placeholder: '$0.00',
  blocks: {
    num: {
      mask: Number,
      padFractionalZeros: true,
      radix: '.',
      mapToRadix: [','],
      min: 0.01
    }
  }
};

export const zipcodeMask: MaskProps = {
  mask: '00000',
  placeholder: '00000'
};

export const numberMask: MaskProps = {
  mask: Number
};
