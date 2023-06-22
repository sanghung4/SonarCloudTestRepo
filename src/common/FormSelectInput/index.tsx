import {
  FieldValues,
  UseControllerProps,
  useController
} from 'react-hook-form';
import {
  Box,
  MenuItem,
  Select,
  SxProps,
  Theme
} from '@dialexa/reece-component-library';
import { FormInputErrorText } from 'common/utils/styled';
import { MenuStyles } from './styled';
import { kebabCase } from 'lodash-es';

/**
 * Types
 */
export type FormSelectOption = { label: string; value: string };

export interface FormSelectInputProps<TFields extends FieldValues>
  extends UseControllerProps<TFields> {
  options: FormSelectOption[];
  testId?: string;
  required?: boolean;
  disabled?: boolean;
  sx?: SxProps<Theme>;
}

/**
 * Component
 */
function FormSelectInput<TFields extends FieldValues>(
  props: FormSelectInputProps<TFields>
) {
  /**
   * Props
   */
  const { options, testId, required, disabled, sx, ...rest } = props;

  /**
   * Custom Hooks
   */
  const {
    field: { onChange, onBlur, value, name },
    fieldState
  } = useController({ ...rest });

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
      <Select
        onChange={onChange}
        onBlur={onBlur}
        name={name}
        value={value}
        id={props.name}
        error={!!fieldState.error}
        fullWidth
        MenuProps={{ sx: MenuStyles }}
        inputProps={{
          'data-testid': kebabCase([testId, 'input'].join(' '))
        }}
        sx={sx}
      >
        {/* Select Options */}
        {options.map((opt) => (
          <MenuItem value={opt.value} key={opt.label}>
            {opt.label}
          </MenuItem>
        ))}
      </Select>
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

export default FormSelectInput;
