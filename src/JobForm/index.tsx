import { Container } from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import Breadcrumbs from 'common/Breadcrumbs';
import JobFormLink from './JobFormLink';
import JobFormCard from './JobFormCard';
import JobFormProvider, { JobFormContext } from './JobFormProvider';
import JobFormSuccessModal from './JobFormSuccessModal';
import useDocumentTitle from 'hooks/useDocumentTitle';
import { testIds } from 'test-utils/testIds';
import { useState } from 'react';

function JobForm() {
  /**
   * Context
   */
  const { t } = useTranslation();
  const [isSubmitCompleted, setIsSubmitCompleted] = useState(false);
  const [customerDetails, setCustomerDetails] = useState('');

  useDocumentTitle(t('common.jobForm'));
  return (
    <JobFormProvider>
      <Breadcrumbs
        pageTitle={t('common.jobForm')}
        config={[{ text: t('common.resources'), to: '/' }]}
      />
      {isSubmitCompleted ? (
        <JobFormSuccessModal customerDetails={customerDetails} />
      ) : (
        <Container data-testid={testIds.JobForm.page}>
          <JobFormContext.Consumer>
            {({ showJobFormCard }) =>
              showJobFormCard ? (
                <JobFormCard
                  onSubmitCompleted={() => setIsSubmitCompleted(true)}
                  setCustomerDetails={setCustomerDetails}
                />
              ) : (
                <JobFormLink />
              )
            }
          </JobFormContext.Consumer>
        </Container>
      )}
    </JobFormProvider>
  );
}

export default JobForm;
