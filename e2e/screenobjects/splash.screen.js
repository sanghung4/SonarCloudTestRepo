import AppScreen from './screen';

const SELECTORS = {
  LOGIN_BUTTON: '~Login with Okta',
};

class SplashScreen extends AppScreen {
  constructor() {
    super(SELECTORS.LOGIN_BUTTON);
  }

  get loginButton() {
    return $(SELECTORS.LOGIN_BUTTON);
  }
}

export default new SplashScreen();
