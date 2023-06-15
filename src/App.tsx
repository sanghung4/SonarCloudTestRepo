import { Sidebar } from "./components/Sidebar";
import { Switch, Route, useHistory, BrowserRouter } from "react-router-dom";
import { Slide, ToastContainer } from "react-toastify";
import { LoginCallback, Security, SecureRoute } from "@okta/okta-react";
import { OktaAuth, toRelativeUrl } from "@okta/okta-auth-js";
import { MincronCount } from "./pages/MincronCount";
import { Pricing } from "./pages/Pricing";
import { Metrics } from "./pages/Metrics";
import { CountStatus } from "./pages/CountStatus";
import { PricingAdmin } from "./pages/PricingAdmin";
import { oktaAuthOptions } from "./utils/configuration";
import { CountStatusDetails } from "./pages/CountStatusDetails";
import { AuthProvider } from "./store/AuthProvider";

const oktaAuth = new OktaAuth(oktaAuthOptions);

function App() {
  const history = useHistory();

  const restoreOriginalUri = async () => {
    history.replace(toRelativeUrl("/", window.location.origin));
  };

  const triggerLogin = async () => {
    await oktaAuth?.signInWithRedirect();
  };

  const customAuthHandler = async () => {
    const previousAuthState = oktaAuth?.authStateManager.getPreviousAuthState();
    if (!previousAuthState || !previousAuthState.isAuthenticated) {
      // App initialization stage
      await triggerLogin();
    } else {
      // Ask the user to trigger the login process during token autoRenew process
      console.log("Placeholder for renewal modal");
    }
  };

  return (
    <Security
      oktaAuth={oktaAuth}
      onAuthRequired={customAuthHandler}
      restoreOriginalUri={restoreOriginalUri}
    >
      <div className='relative min-h-screen lg:flex'>
        <ToastContainer autoClose={4000} transition={Slide} />
        <AuthProvider>
          <Sidebar />
          <div className='w-full min-h-screen bg-background overflow-auto'>
            <Switch>
              <SecureRoute path='/metrics' exact component={Metrics} />
              <SecureRoute path='/mincron' component={MincronCount} />
              <SecureRoute path='/count/:id/total/:total' component={CountStatusDetails} />
              <SecureRoute path='/count' component={CountStatus} />
              <SecureRoute path='/PricingAdmin' component={PricingAdmin} />
              <SecureRoute path='/pricing' component={Pricing} />
              <Route path='/' component={LoginCallback} />
            </Switch>
          </div>
        </AuthProvider>
      </div>
    </Security>
  );
}

const AppWithProviders = () => {
  return (
    <BrowserRouter>
      <App />
    </BrowserRouter>
  );
};

export default AppWithProviders;
