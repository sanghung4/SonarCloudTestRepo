import { Grid } from '@dialexa/reece-component-library';
import { FieldValues } from 'react-hook-form';

import { FormFieldContainer, InputLabelWrapper } from './utils/styled';
import JobFormInputLabel from './JobFormInputLabel';
import FormAutocompleteInput, {
  FormAutocompleteInputProps
} from 'common/FormAutocompleteInput';

interface JobFormAutocompleteInputProps<TFieldValues extends FieldValues>
  extends FormAutocompleteInputProps<TFieldValues> {
  label: string;
  tooltip?: string;
  required?: boolean;
  mdLabelSpan?: number;
  mdFieldSpan?: number;
  xsLabelSpan?: number;
  xsFieldSpan?: number;
}

function JobFormAutocompleteInput<TFieldValues extends FieldValues>(
  props: JobFormAutocompleteInputProps<TFieldValues>
) {
  /**
   * Props
   */
  const {
    label,
    tooltip,
    required,
    mdLabelSpan,
    mdFieldSpan,
    xsLabelSpan,
    xsFieldSpan,
    testId,
    ...rest
  } = props;

  /**
   * Render
   */
  return (
    <FormFieldContainer
      container
      columns={10}
      data-testid={`${testId}-wrapper`}
    >
      {/* Label */}
      <InputLabelWrapper
        item
        md={mdLabelSpan ?? 3}
        xs={xsLabelSpan ?? 10}
        alignItems="flex-start"
      >
        <JobFormInputLabel
          htmlFor={props.name}
          title={label}
          tooltip={tooltip}
          required={required}
        />
      </InputLabelWrapper>

      {/* Input */}
      <Grid item md={mdFieldSpan ?? 7} xs={xsFieldSpan ?? 10}>
        <FormAutocompleteInput
          {...rest}
          sx={{
            '.MuiInputBase-root.MuiAutocomplete-inputRoot': { paddingY: 0.375 }
          }}
          testId={testId}
        />
      </Grid>
    </FormFieldContainer>
  );
}

export default JobFormAutocompleteInput;
