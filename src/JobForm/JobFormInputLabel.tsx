import { Box, Tooltip, Typography } from '@dialexa/reece-component-library';
import { FormInputLabelContainer } from './utils/styled';
import { InfoIcon } from 'icons';

type JobFormInputLabelProps = {
  title: string;
  required?: boolean;
  tooltip?: string;
  htmlFor?: string;
};

function JobFormInputLabel(props: JobFormInputLabelProps) {
  /**
   * Render
   */
  return (
    <FormInputLabelContainer htmlFor={props.htmlFor}>
      <Typography component="span" color="primary.main">
        {props.title}
        {props.required && (
          <Typography
            component="span"
            color="orangeRed.main"
            fontWeight={700}
            ml={0.5}
          >
            *
          </Typography>
        )}
      </Typography>
      {!!props.tooltip && (
        <Tooltip title={props.tooltip}>
          <Box component={InfoIcon} color="primary.main" ml={1} height={16} />
        </Tooltip>
      )}
    </FormInputLabelContainer>
  );
}

export default JobFormInputLabel;
