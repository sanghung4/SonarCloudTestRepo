import { Fragment } from 'react';
import cn from 'classnames';

import './styles.scss';

/**
 * Types
 */
type StepperProps = {
  steps: string[];
  activeStep: number;
  onStepClick?: (step: number) => void;
};

/**
 * Component
 */
function Stepper(props: StepperProps) {
  /**
   * Props
   */
  const { activeStep, onStepClick, steps } = props;

  /**
   * Render
   */
  return (
    <div className="stepper">
      {/* Steps are each generated */}
      {steps.map((step, index) => (
        <Fragment key={index}>
          <div
            className={cn('stepper__step-bar', {
              completed: activeStep >= index
            })}
          />
          <button
            className={cn('stepper__step-button', {
              completed: activeStep >= index,
              active: activeStep === index
            })}
            onClick={() => onStepClick?.(index)}
          >
            <div className="stepper__step-bubble" />
            <p className="stepper__step-text">{step}</p>
          </button>
        </Fragment>
      ))}
    </div>
  );
}

export default Stepper;
