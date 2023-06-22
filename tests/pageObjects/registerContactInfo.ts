interface userContactInfo {
  accountNumber: number;
  firstName: string;
  lastName: string;
  phoneNumber: string;
  email: string;
  password: string;
}

class registerContactInfoPage {

  get firstNameInput() { return $('[data-testid="firstNameInput"]'); }
  get lastNameInput() { return $('[data-testid="lastNameInput"]'); }
  get phoneNumberInput() { return $('[data-testid="phoneNumberInput"]'); }
  get phoneTypeDropdown() { return $('[data-testid="phoneTypeDropdown"]'); }
  get homeOption() { return $('[data-value="Home"]'); }
  get emailInput() { return $('[data-testid="emailInput"]'); }
  get continueButton() { return $('[data-testid="continueButton"]'); }

  async fillOutContactInfo(userInfo: userContactInfo) {
    (await this.firstNameInput).click();
    await browser.keys(userInfo.firstName);

    (await this.lastNameInput).click();
    await browser.pause(500);
    await browser.keys(userInfo.lastName);
    await browser.pause(500);
    
    (await this.phoneNumberInput).click();
    await browser.pause(500);
    await browser.keys(userInfo.phoneNumber);
    await browser.pause(500);
    
    (await this.phoneTypeDropdown).click();
    await browser.pause(500);
    (await this.homeOption).click();
    await browser.pause(500);
    
    (await this.emailInput).click();
    await browser.pause(500);
    await browser.keys(userInfo.email);
    (await this.continueButton).click();
  }
}

export default new registerContactInfoPage();
