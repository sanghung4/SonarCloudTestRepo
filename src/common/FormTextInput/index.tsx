import { MutableRefObject } from 'react';
import { Box, Input, SxProps, Theme } from '@dialexa/reece-component-library';
import {
  UseControllerProps,
  useController,
  FieldValues
} from 'react-hook-form';

import { currencyMask, numberMask, phoneMask, zipcodeMask } from 'utils/masks';
import InputMask from 'common/InputMask';
import { FormInputErrorText } from 'common/utils/styled';
import { kebabCase } from 'lodash-es';

export interface FormTextInputProps<TFields extends FieldValues>
  extends UseControllerProps<TFields> {
  testId?: string;
  mask?: 'currency' | 'number' | 'phone' | 'zipcode';
  placeholder?: string;
  required?: boolean;
  readOnly?: boolean;
  disabled?: boolean;
  inputRef?: MutableRefObject<HTMLInputElement | undefined>;
  sx?: SxProps<Theme>;
  maxLength?: number;
}

function FormTextInput<TFields extends FieldValues>(
  props: FormTextInputProps<TFields>
) {
  /**
   * Props
   */
  const { mask, required, disabled, readOnly, testId, inputRef, sx, ...rest } =
    props;

  /**
   * Custom Hooks
   */
  const { field, fieldState } = useController(rest);

  /**
   * Data
   */
  const maskMap = {
    currency: currencyMask,
    number: numberMask,
    phone: phoneMask,
    zipcode: zipcodeMask
  };

  /**
   * Render
   */
  return (
    <Box
      data-testid={testId}
      display="flex"
      flexGrow={1}
      flexDirection="column"
    >
      <Input
        {...field}
        fullWidth
        readOnly={readOnly}
        required={required}
        disabled={disabled}
        placeholder={props.placeholder}
        id={props.name}
        sx={sx}
        error={!!fieldState.error}
        inputRef={inputRef}
        inputComponent={!!props.mask ? (InputMask as any) : undefined}
        inputProps={{
          ...(!!props.mask && maskMap[props.mask]),
          'data-testid': kebabCase([testId, 'input'].join(' ')),
          maxLength: props.maxLength
        }}
      />
      {!!fieldState.error && (
        <FormInputErrorText
          data-testid={kebabCase([testId, 'error-text'].join(' '))}
        >
          {fieldState.error.message}
        </FormInputErrorText>
      )}
    </Box>
  );
}

export default FormTextInput;
