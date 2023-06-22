import { ReactNode } from 'react';

import {
  Accordion,
  AccordionSummary,
  AccordionDetails
} from '@dialexa/reece-component-library';

import { ArrowDropDownIcon } from 'icons';

export type AccordionWrapperProps = {
  label: string;
  testId: string;
  children: ReactNode;
  variant?: 'default' | 'warning';
};

export default function AccordionWrapper(props: AccordionWrapperProps) {
  /**
   * Variables
   */
  const accordionSx = {
    '& .MuiAccordionSummary-content': {
      color: props.variant === 'warning' ? 'orangeRed.main' : undefined
    }
  };
  const testId = `${props.testId.toLowerCase().replaceAll(' ', '-')}-dropdown`;

  /**
   * Render
   */
  return (
    <Accordion type="product" sx={accordionSx}>
      <AccordionSummary data-testid={testId} expandIcon={<ArrowDropDownIcon />}>
        {props.label}
      </AccordionSummary>
      <AccordionDetails sx={{ px: 0 }}>{props.children}</AccordionDetails>
    </Accordion>
  );
}
