import { useEffect, useState } from 'react';

import { ArrowBack } from '@mui/icons-material';
import { useTranslation } from 'react-i18next';
import { Link, Route, Switch, useLocation, useHistory } from 'react-router-dom';

import Button from 'components/Button';
import Container from 'components/Container';
import Stepper from 'components/Stepper';
import accountRegistration from 'images/account-registration.png';
import StepOne from 'Register/StepOne';
import StepTwo from 'Register/StepTwo';
import StepThree from 'Register/StepThree';
import StepFour from 'Register/StepFour';
import StepFive from 'Register/StepFive';
import { testIds } from 'test-utils/testIds';
import 'Register/styles.scss';
/**
 * Constants
 */
const TEST_IDS = testIds.Register;

/**
 * Component
 */
function NewRegister() {
  /**
   * Custom Hooks
   */
  const { pathname } = useLocation();
  const { t } = useTranslation();
  const history = useHistory();

  /**
   * States
   */
  const [activeStep, setActiveStep] = useState(0);
  const [pageTitle, setPageTitle] = useState('');

  /**
   * Effects
   */
  useEffect(
    pathnameEffect,
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [pathname]
  );

  /**
   * Render
   */
  return (
    <div className="register" data-testid={TEST_IDS.page}>
      {/* Banner */}
      <div className="register__banner">
        <Container className="register__banner__container">
          <Link to="/" className="register__banner__button-container">
            <Button
              icon={<ArrowBack />}
              label={t('common.backToSite')}
              variant="text-link-dark"
            />
          </Link>
          <h1 className="register__banner__title">
            {t('common.registerForMaX')}
          </h1>
        </Container>
      </div>
      {/* Content */}
      <Container className="register__container">
        {/* Left half */}
        <Container className="register__content" maxWidth="sm">
          {/* Mobile back button */}
          <Link to="/" className="register__content__button-container">
            <Button
              icon={<ArrowBack />}
              label={t('common.backToSite')}
              variant="text-link-dark"
            />
          </Link>

          {/* Screen stepper */}
          <Stepper
            steps={[
              t('common.emailAddress'),
              t('register.companyInfo'),
              t('register.accountInfo'),
              t('register.contactInfo'),
              t('register.completeRegistration')
            ]}
            activeStep={activeStep}
          />
          {/* Screen title */}
          <h1 className="register__content__title">{pageTitle}</h1>
          {/* Screen subroutes */}
          <Switch>
            <Route exact path={`/register/step-1`}>
              <StepOne />
            </Route>
            <Route exact path={'/register/step-2'}>
              <StepTwo />
            </Route>
            <Route exact path={'/register/step-3'}>
              <StepThree />
            </Route>
            <Route exact path={'/register/step-4'}>
              <StepFour />
            </Route>
            <Route exact path={'/register/step-5'}>
              <StepFive />
            </Route>
          </Switch>
        </Container>
        {/* Right half */}
        <img
          className="register__image"
          src={accountRegistration}
          alt="reece"
        />
      </Container>
    </div>
  );
  /**
   * Effect Defs
   */
  function pathnameEffect() {
    const stepNames = [
      '/register/step-1',
      '/register/step-2',
      '/register/step-3',
      '/register/step-4',
      '/register/step-5'
    ];
    const stepTitles = [
      t('register.whatIsEmail'),
      t('register.companyInformation'),
      t('register.accountInformation'),
      t('register.contactInformation'),
      t('register.completeRegistration')
    ];
    const stepIndex = stepNames.indexOf(pathname);
    if (stepIndex >= 0) {
      setActiveStep(stepIndex);
      setPageTitle(stepTitles[stepIndex]);
    } else {
      history.replace('/register/step-1');
    }
  }
}

export default NewRegister;
