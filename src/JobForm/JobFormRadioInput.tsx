import { Grid } from '@dialexa/reece-component-library';
import { FieldValues } from 'react-hook-form';

import JobFormInputLabel from './JobFormInputLabel';
import {
  FormRadioInputDetails,
  FormFieldContainer,
  InputLabelWrapper
} from './utils/styled';
import FormRadioInput, { FormRadioInputProps } from 'common/FormRadioInput';

interface JobFormRadioInputProps<TFieldValues extends FieldValues>
  extends FormRadioInputProps<TFieldValues> {
  label: string;
  details?: string;
  tooltip?: string;
  mdLabelSpan?: number;
  mdFieldSpan?: number;
  xsLabelSpan?: number;
  xsFieldSpan?: number;
}

function JobFormRadioInput<TFieldValues extends FieldValues>(
  props: JobFormRadioInputProps<TFieldValues>
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
    details,
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
        flexDirection="column"
        justifyItems="flex-start"
      >
        <JobFormInputLabel
          htmlFor={props.name}
          title={label}
          tooltip={tooltip}
          required={required}
        />
        <FormRadioInputDetails>{details}</FormRadioInputDetails>
      </InputLabelWrapper>

      {/* Input */}
      <Grid item md={mdFieldSpan ?? 7} xs={xsFieldSpan ?? 10}>
        <FormRadioInput {...rest} required={required} testId={testId} />
      </Grid>
    </FormFieldContainer>
  );
}

export default JobFormRadioInput;
