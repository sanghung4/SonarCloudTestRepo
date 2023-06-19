import React from 'react';
import { Text } from 'components/Text';
import { FormInputLabelProps } from './types';
import { Colors } from 'constants/style';

export const FormInputLabel = ({
  label,
  labelStyle,
  required,
}: FormInputLabelProps) => {
  return label ? (
    <Text style={labelStyle}>
      {label}
      {required ? <Text style={{ color: Colors.SUPPORT_2100 }}> *</Text> : null}
    </Text>
  ) : null;
};
