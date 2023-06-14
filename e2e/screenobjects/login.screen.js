import AppScreen from './screen';

const SELECTORS = {
  LOGIN_SCREEN: '#okta-signin-username',
  LOGIN_BUTTON: '#okta-signin-submit',
  USERNAME: '#okta-signin-username',
  PASSWORD: '#okta-signin-password',
};

class LoginScreen extends AppScreen {
  constructor() {
    super(SELECTORS.LOGIN_SCREEN);
  }

  get loginButton() {
    return $(SELECTORS.LOGIN_BUTTON);
  }

  get username() {
    return $(SELECTORS.USERNAME);
  }

  get password() {
    return $(SELECTORS.PASSWORD);
  }
}

export default new LoginScreen();
