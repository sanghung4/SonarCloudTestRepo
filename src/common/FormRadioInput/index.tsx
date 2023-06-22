import {
  FieldValues,
  UseControllerProps,
  useController
} from 'react-hook-form';
import {
  Box,
  FormControl,
  Radio,
  SxProps,
  Theme
} from '@dialexa/reece-component-library';

import { FormInputErrorText } from 'common/utils/styled';
import { StyledRadioControlLabel, StyledRadioGroup } from './styled';
import { kebabCase } from 'lodash-es';

export type FormRadioOption = { label: string; value: unknown };

export interface FormRadioInputProps<TFields extends FieldValues>
  extends UseControllerProps<TFields> {
  options: FormRadioOption[];
  testId?: string;
  required?: boolean;
  disabled?: boolean;
  sx?: SxProps<Theme>;
}

function FormRadioInput<TFields extends FieldValues>(
  props: FormRadioInputProps<TFields>
) {
  /**
   * Props
   */
  const { options, testId, required, disabled, sx, ...rest } = props;

  /**
   * Custom Hooks
   */
  const {
    field: { ref, onChange, ...restField },
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
      <FormControl component="fieldset" error={!!fieldState.error} ref={ref}>
        <StyledRadioGroup
          row
          id={props.name}
          onChange={(_, value) => onChange(value)}
          {...restField}
          sx={sx}
        >
          {options.map((item, index) => (
            <StyledRadioControlLabel
              value={item.value}
              label={item.label}
              key={kebabCase(`${props.name}-${item.label}`)}
              control={
                <Radio
                  inputProps={
                    {
                      'data-testid': kebabCase(
                        [testId, 'radio', index].join(' ')
                      )
                    } as React.InputHTMLAttributes<HTMLInputElement>
                  }
                />
              }
            />
          ))}
        </StyledRadioGroup>
      </FormControl>
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

export default FormRadioInput;
