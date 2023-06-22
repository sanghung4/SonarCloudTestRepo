import { lazy, useContext } from 'react';

// lib
import { LoginCallback, SecureRoute } from '@okta/okta-react';
import Loader from 'common/Loader';
import { Location, Action } from 'history';
import { Redirect, Route, Switch } from 'react-router-dom';

// Provider/Context
import { AuthContext } from 'AuthProvider';
import CheckoutProvider from 'Checkout/CheckoutProvider';
import CreditCardProvider from 'CreditCard/CreditCardProvider';
import { useCartContext } from 'providers/CartProvider';

// Components
import Account from 'Account';
import Appointments from 'Appointments';
import MigrationSetupMessage from 'Account/MigrationSetupMessage';
import AboutUs from 'AboutUs';
import BranchManagement from 'BranchManagement';
import BrandPage from 'Brands';
import BigDogMarketing from 'Brands/BigDog';
import PoshMarketing from 'Brands/Posh';
import Cart from 'Cart';
import Checkout from 'Checkout';
import NavigationAlert from 'common/Alerts/NavigationAlert';
import ErrorComponent from 'common/ErrorBoundary/ErrorComponent';
import Company from 'Company';
import Contract from 'Contract';
import Contracts from 'Contracts';
import CreditCardCallback from 'CreditCard/CreditCardCallback';
import CreditForms from 'CreditForms';
import CustomerApproval from 'CustomerApproval';
import FeatureToggle from 'FeatureToggle';
import ForgotPassword from 'ForgotPassword';
import Home from 'Home';
import Invite from 'Invite';
import Invoice from 'Invoice';
import Invoices from 'Invoices';
import Legal from 'Legal';
import DoNotSellMyInfo from 'Legal/DoNotSellMyInfo';
import Lists from 'Lists';
import ListUpload from 'Lists/Upload';
import LocationSearch from 'LocationSearch';
import Login from 'Login';
import Logout from 'Logout';
import MaintenancePage from 'Invoices/Maintenance';
import ModifyAppointment from 'Appointments/ModifyAppointment';
import News from 'News';
import NewsDetails from 'NewsDetails';
import Order from 'Order';
import Orders from 'Orders';
import PaymentInformation from 'PaymentInformation';
import Portal from 'Portal';
import PreviouslyPurchasedProducts from 'PreviouslyPurchasedProducts';
import Product from 'Product';
import PurchaseApprovals from 'PurchaseApprovals';
import ReviewPurchase from 'PurchaseApprovals/ReviewPurchase';
import Quote from 'Quote';
import Quotes from 'Quotes';
import RegisterNew from 'Register';
import RegisterOld from 'Register-old';
import VerifyEmail from 'Register-old/VerifyEmail';
import Search from 'Search';
import SelectAccounts from 'SelectAccounts';
import Support from 'Support';
import User from 'User';
import UserManagement from 'UserManagement';
import WorksForYou from 'WorksForYou';

// misc
import useSearchParam from 'hooks/useSearchParam';
import RegisterComplete from 'Register/RegisterComplete';

/**
 * Lazy Loaded Components
 */
const JobForm = lazy(() => import('./JobForm'));

function Routes() {
  /**
   * Custom Hooks
   */
  const inviteId = useSearchParam('inviteId');

  /**
   * Contexts
   */
  const { activeFeatures, featuresLoading } = useContext(AuthContext);
  const { clearContract } = useCartContext();
  const isNewRegistration =
    !activeFeatures?.length || activeFeatures.includes('NEW_REGISTRATION');
  const InvoiceMaintenance = activeFeatures?.includes('INVOICE_MAINTENANCE');
  /**
   * Render (ROUTES)
   */
  return (
    <>
      <NavigationAlert when={showContractNavPrompt} onConfirm={clearContract} />
      <Switch>
        {/* Unsecure Routes */}
        <Route path="/error" exact component={ErrorComponent} />
        <Route path="/" exact component={Home} />
        <Route exact path="/product/:slug?/:id" component={Product} />
        {featuresLoading ? (
          <Route exact path="/register" component={Loader} />
        ) : !isNewRegistration || inviteId ? (
          <Route exact path="/register" component={RegisterOld} />
        ) : (
          <Route exact path="/register/:step?" component={RegisterNew} />
        )}
        <Route exact path="/confirmation" component={RegisterComplete} />
        <Route exact path="/Appointment" component={Appointments} />
        <Route exact path="/Appointment/Manage" component={ModifyAppointment} />
        <Route exact path="/forgot-password" component={ForgotPassword} />
        <Route path="/search" component={Search} />
        <Route path="/privacy-policy" component={Legal} />
        <Route path="/terms-of-access" component={Legal} />
        <Route path="/terms-of-sale" component={Legal} />
        <Route path="/do-not-sell-my-info" component={DoNotSellMyInfo} />
        <Route exact path="/login" component={Login} />
        <Route exact path="/logout" component={Logout} />
        <Route exact path="/user/:id" component={User} />
        <Route exact path="/location-search" component={LocationSearch} />
        <Route exact path="/support" component={Support} />
        <Route path="/login/callback" component={LoginCallback} />
        <Route path="/verify" component={VerifyEmail} />
        <Route path="/credit_callback" component={CreditCardCallback} />
        <Route path="/max-welcome" component={MigrationSetupMessage} />
        <Route exact path="/credit-forms" component={CreditForms} />
        <Route exact path="/company" component={Company} />
        <Route exact path="/about" component={AboutUs} />
        <Route exact path="/news" component={News} />
        <Route exact path="/newsdetails/:id" component={NewsDetails} />
        <Route exact path="/works-for-you" component={WorksForYou} />
        <Route exact path="/brands/posh" component={PoshMarketing} />
        <Route exact path="/brands/bigdog" component={BigDogMarketing} />
        <Route exact path="/brands" component={BrandPage} />
        <Route exact path="/jobform" component={JobForm} />

        {/* Secured Routes */}
        <SecureRoute exact path="/invite-user" component={Invite} />
        <SecureRoute exact path="/account" component={Account} />
        <SecureRoute exact path="/cart" component={Cart} />
        <SecureRoute exact path="/checkout/:step?">
          <CheckoutProvider>
            <CreditCardProvider>
              <Checkout />
            </CreditCardProvider>
          </CheckoutProvider>
        </SecureRoute>
        <SecureRoute
          exact
          path="/customer-approval"
          component={CustomerApproval}
        />
        <SecureRoute exact path="/contract/:id" component={Contract} />
        <SecureRoute exact path="/invoice/:id" component={Invoice} />
        <SecureRoute
          exact
          path="/invoices"
          component={InvoiceMaintenance ? MaintenancePage : Invoices}
        />
        <SecureRoute exact path="/lists" component={Lists} />
        <SecureRoute exact path="/lists/upload" component={ListUpload} />
        <SecureRoute exact path="/payment-information">
          <CreditCardProvider>
            <PaymentInformation />
          </CreditCardProvider>
        </SecureRoute>
        <SecureRoute exact path="/portal" component={Portal} />
        <SecureRoute exact path="/order/:id" component={Order} />
        <SecureRoute exact path="/orders" component={Orders} />
        <SecureRoute exact path="/contracts" component={Contracts} />
        <SecureRoute
          exact
          path="/previously-purchased-products"
          component={PreviouslyPurchasedProducts}
        />
        <SecureRoute
          exact
          path="/purchase-approvals"
          component={PurchaseApprovals}
        />
        <SecureRoute
          exact
          path="/purchase-approvals/:id"
          component={ReviewPurchase}
        />
        <SecureRoute exact path="/quote/:id" component={Quote} />
        <SecureRoute exact path="/quotes" component={Quotes} />
        <SecureRoute exact path="/job-form" component={JobForm} />
        <SecureRoute exact path="/user-management" component={UserManagement} />
        <SecureRoute path="/select-accounts" component={SelectAccounts} />
        <SecureRoute exact path="/features" component={FeatureToggle} />
        <SecureRoute
          exact
          path="/branch-management"
          component={BranchManagement}
        />
        <Redirect to="/" />
      </Switch>
    </>
  );

  function showContractNavPrompt(
    pLocation: Location<any>,
    nLocation: Location<any> | undefined,
    action?: Action
  ): boolean {
    if (pLocation.pathname === nLocation?.pathname) return false;
    if (nLocation?.state?.ignoreNavAlertForPrev) return false;
    if (action === 'POP') return false;
    if (nLocation?.pathname?.includes('/checkout')) return false;
    return pLocation?.state?.canShowNavAlert;
  }
}

export default Routes;
