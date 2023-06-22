import { JobFormContext } from 'JobForm/JobFormProvider';
import { useContext } from 'react';

export function JobFormContextDisplay() {
  const { jobFormLoading, defaultValues, userAccountOptions } =
    useContext(JobFormContext);

  return (
    <div data-testid="job-form-provider-display">
      <span data-testid="job-form-provider-loading">{`${jobFormLoading}`}</span>
      {/* Iterate over all stepOne entries */}
      {Object.entries(defaultValues.stepOne).map(([key, value]) => (
        <span
          data-testid={`job-form-provider-defaultValues-stepOne-${key}`}
          key={key}
        >
          {value}
        </span>
      ))}
      {/* Iterate over all stepTwo entries */}
      {Object.entries(defaultValues.stepTwo).map(([key, value]) => (
        <span
          data-testid={`job-form-provider-defaultValues-stepTwo-${key}`}
          key={key}
        >
          {value}
        </span>
      ))}
      {/* Iterate over all stepThree entries */}
      {Object.entries(defaultValues.stepThree).map(([key, value]) => (
        <span
          data-testid={`job-form-provider-defaultValues-stepThree-${key}`}
          key={key}
        >
          {value}
        </span>
      ))}
      {/* Iterate over all userAccountOptions */}
      {userAccountOptions.map((option, index) => (
        <span
          data-testid={`job-form-provider-userAccountOption-${index}`}
          key={option.label}
        >{`${option.label} - ${option.value}`}</span>
      ))}
    </div>
  );
}
