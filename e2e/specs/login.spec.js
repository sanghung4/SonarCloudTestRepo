import SplashScreen from '../screenobjects/splash.screen';
import WebviewScreen from '../screenobjects/webview.screen';
import LoginScreen from '../screenobjects/login.screen';
import { CONTEXT_REF } from '../helpers/WebView';

const APPIUM_USERNAME = process.env.APPIUM_USERNAME;
const APPIUM_PW = process.env.APPIUM_PW;

describe('I should log in', () => {
  beforeEach(() => {
    SplashScreen.waitForIsShown(true);
  });

  it('should click Login with Okta', () => {
    SplashScreen.loginButton.click();
    // To be able to use the site in the webview webdriver.io first needs
    // change the context from native to webview
    WebviewScreen.waitForWebViewIsDisplayedByXpath(true);
    WebviewScreen.switchToContext(CONTEXT_REF.WEBVIEW);

    LoginScreen.waitForIsShown(true);
    LoginScreen.username.setValue(APPIUM_USERNAME);
    LoginScreen.password.setValue(APPIUM_PW);

    if (driver.isKeyboardShown()) {
      driver.hideKeyboard();
    }
    LoginScreen.loginButton.click();
  });
});
