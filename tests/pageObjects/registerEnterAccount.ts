import { BrowserRouter } from "react-router-dom";

class registerEnterAccountPage {

  // elements on page
  get accountNumberInput() { return $('[data-testid="accountNumberInput"]'); }
  get continueButton() { return $('[data-testid="continueButton"]'); }

  open() {
      browser.url('/register');
  }

  async enterAccountNumber(accountNumber: number) {
    (await this.accountNumberInput).click();
    await browser.keys(accountNumber.toString())
  }
}

export default new registerEnterAccountPage();
