import { Grid } from '@dialexa/reece-component-library';
import { FieldValues } from 'react-hook-form';

import { FormFieldContainer, InputLabelWrapper } from './utils/styled';
import JobFormInputLabel from './JobFormInputLabel';
import FormSelectInput, { FormSelectInputProps } from 'common/FormSelectInput';

interface JobFormSelectInputProps<TFieldValues extends FieldValues>
  extends FormSelectInputProps<TFieldValues> {
  label: string;
  tooltip?: string;
  mdLabelSpan?: number;
  mdFieldSpan?: number;
  xsLabelSpan?: number;
  xsFieldSpan?: number;
}

function JobFormSelectInput<TFieldValues extends FieldValues>(
  props: JobFormSelectInputProps<TFieldValues>
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
        display="flex"
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
        <FormSelectInput
          {...rest}
          required={required}
          sx={{ '.MuiSelect-select.MuiInputBase-input': { paddingY: 1 } }}
          testId={testId}
        />
      </Grid>
    </FormFieldContainer>
  );
}

export default JobFormSelectInput;
