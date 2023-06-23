import { useState } from 'react';

import { useOktaAuth } from '@okta/okta-react';
import { useFormik } from 'formik';
import { useNavigate } from 'react-router-dom';
import * as Yup from 'yup';

import { LoginRequest, useApiLogin } from 'api/login.api';
import { Button } from 'components/Button';
import { Input } from 'components/Input';
import { useAuthContext } from 'providers/AuthProvider';
import { ReactComponent as CloseIcon } from 'resources/icons/close.svg';
import warning from 'resources/icons/warning.svg';
import logo from 'resources/images/logo.svg';
import { ReactComponent as OktaIcon } from 'resources/icons/okta.svg';
import { configuration, Environments } from 'util/configurations';

/**
 * Component
 */
function Login() {
  /**
   * Custom hooks
   */
  const navigate = useNavigate();
  const { oktaAuth } = useOktaAuth();

  /**
   * Context
   */
  const { setSessionId } = useAuthContext();

  /**
   * States
   */
  const [loginError, setLoginError] = useState(false);

  /**
   * API
   */
  const { loading, call: login } = useApiLogin();

  /**
   * Callbacks
   */
  const onSubmit = async (values: LoginRequest) => {
    setLoginError(false);
    const res = await login(values);
    if (!res?.data) {
      // Null-ish when there's exception
      return;
    }
    setLoginError(!res.data.success);
    if (res.data.success && res.data.sessionId) {
      setSessionId(res.data.sessionId);
      navigate('/');
    }
  };
  const oktaLogin = () => {
    oktaAuth.signInWithRedirect();
  };

  /**
   * Form
   */
  const formik = useFormik({
    initialValues: { email: '', password: '' },
    validationSchema: Yup.object({
      email: Yup.string()
        .email('Please enter valid email address')
        .required('Email address is required'),
      password: Yup.string().required('Password is required')
    }),
    onSubmit
  });

  /**
   * Render
   */
  if (configuration.environment === Environments.PROD) {
    return (
      <div
        className="bg-primary-1-100 w-full h-screen flex justify-center items-center"
        data-testid="login_container"
      >
        <div className="bg-white w-[444px] rounded p-8 shadow-lg flex flex-col justify-center items-center">
          <img src={logo} alt="logo" width={140} height={48} />
          <h4 className="text-2xl text-primary-1-100 font-bold mt-6 mb-20">
            Punchout | Admin Portal
          </h4>
          <Button
            type="button"
            title="Sign in with Okta"
            className="bg-primary-1-100 text-white w-full"
            data-testid="login_okta-button"
            onClick={oktaLogin}
          />
        </div>
      </div>
    );
  }
  return (
    <div
      className="bg-primary-1-100 w-full h-screen flex justify-center items-center"
      data-testid="login_container-dev"
    >
      <div>
        <div className="flex flex-1 justify-center items-stretch gap-8 mb-24">
          <img src={logo} alt="logo" />
          <div className="inline-block min-h-[1em] w-0.5 self-stretch bg-white opacity-100 dark:opacity-50" />
          <div className="flex flex-col">
            <h1 className="text-white text-5xl">Punchout</h1>
            <h4 className="text-white text-2xl uppercase">Admin Portal</h4>
          </div>
        </div>
        <div className="h-24">
          {loginError && (
            <div
              className="bg-support-2-10 w-[966px] rounded p-5 border-support-2-100 flex gap-3 text-support-2-100"
              data-testid="login_error-container"
            >
              <img src={warning} alt="alert" />
              <span className="grow">
                <b>Error:</b> Incorrect email or password
              </span>
              <button
                onClick={(e) => {
                  e.preventDefault();
                  setLoginError(false);
                }}
                data-testid="login_error-close-button"
              >
                <CloseIcon />
              </button>
            </div>
          )}
        </div>
        <div className="bg-white w-[966px] rounded p-8 shadow-lg flex">
          <div className="flex-1">
            <h5 className="text-primary-3-100 text-xl font-medium mb-4">
              Login with Email
            </h5>
            <form onSubmit={formik.handleSubmit}>
              <Input
                id="email"
                name="email"
                type="email"
                label="Email"
                required
                placeholder="Email address"
                className="mb-10"
                value={formik.values.email}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
                disabled={loading}
                status={
                  formik.touched.email && formik.errors.email
                    ? 'error'
                    : 'neutral'
                }
                helperText={
                  formik.touched.email && formik.errors.email
                    ? formik.errors.email
                    : ''
                }
                data-testid="login_email"
              />
              <Input
                id="password"
                name="password"
                type="password"
                label="Password"
                required
                placeholder="Input password"
                value={formik.values.password}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
                disabled={loading}
                status={
                  formik.touched.password && formik.errors.password
                    ? 'error'
                    : 'neutral'
                }
                helperText={
                  formik.touched.password && formik.errors.password
                    ? formik.errors.password
                    : ''
                }
                data-testid="login_password"
              />
              <div className="flex justify-end mt-4">
                <Button
                  type="submit"
                  title="Sign in"
                  disabled={loading}
                  loading={loading}
                  className="bg-primary-1-100 text-white"
                  data-testid="login_submit"
                />
              </div>
            </form>
          </div>
          <div className="mx-12 flex flex-col justify-center items-center">
            <div className="border-r-[1px] border-secondary-2-100 flex-1" />
            <span className="my-4 text-base text-secondary-2-100">OR</span>
            <div className="border-r-[1px] border-secondary-2-100 flex-1" />
          </div>
          <div className="flex-1">
            <h5 className="text-primary-3-100 text-xl font-medium mb-12">
              SSO Login
            </h5>
            <Button
              type="button"
              title="Sign in with Okta"
              disabled={loading}
              loading={loading}
              iconPosition="left"
              icon={<OktaIcon />}
              className="bg-primary-2-100 text-white w-full"
              data-testid="login_okta-button"
              onClick={oktaLogin}
            />
          </div>
        </div>
      </div>
    </div>
  );
}

export default Login;
