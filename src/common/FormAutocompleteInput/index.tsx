import { useMemo } from 'react';
import {
  Box,
  SxProps,
  TextField,
  Theme
} from '@dialexa/reece-component-library';
import {
  FieldValues,
  UseControllerProps,
  useController
} from 'react-hook-form';
import { FormInputErrorText, StyledAutoComplete } from 'common/utils/styled';
import { kebabCase } from 'lodash-es';

/**
 * Types
 */
export type FormAutocompleteOption = { label: string; value: string };

export interface FormAutocompleteInputProps<TFields extends FieldValues>
  extends UseControllerProps<TFields> {
  options: FormAutocompleteOption[];
  testId?: string;
  disabled?: boolean;
  sx?: SxProps<Theme>;
}

/**
 * Component
 */
function FormAutocompleteInput<TFields extends FieldValues>(
  props: FormAutocompleteInputProps<TFields>
) {
  /**
   * Props
   */
  const { options, testId, disabled, sx, ...rest } = props;

  /**
   * Custom Hooks
   */
  const {
    field: { onChange, onBlur, value, name },
    fieldState
  } = useController({ ...rest });

  /**
   * Memos
   */
  const selectedValue = useMemo(() => {
    const selected = options.find((opt) => opt.value === value);
    return selected;
  }, [value, options]);

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
      <StyledAutoComplete
        id={name}
        options={options}
        value={selectedValue}
        onBlur={onBlur}
        onChange={(_, val) => {
          onChange(val?.value);
        }}
        getOptionLabel={(option) => option.label ?? ''}
        isOptionEqualToValue={(opt) => opt.value === selectedValue?.value}
        renderInput={({ inputProps, ...rest }) => (
          <TextField
            {...rest}
            error={!!fieldState.error}
            inputProps={{
              ...inputProps,
              'data-testid': kebabCase([testId, 'input'].join(' '))
            }}
          />
        )}
        sx={sx}
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

export default FormAutocompleteInput;
