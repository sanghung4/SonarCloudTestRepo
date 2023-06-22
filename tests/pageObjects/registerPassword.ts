class registerPassword {

  get passwordInput() { return $('[data-testid="passwordInput"]'); }
  get confirmPasswordInput() { return $('[data-testid="confirmPasswordInput"]'); }
  get continueButton() { return $('[data-testid="continueButton"]'); }

  async fillInPassword(password: string) {
    (await this.passwordInput).click();
    await browser.pause(500);
    await browser.keys(password);
    (await this.confirmPasswordInput).click();
    await browser.pause(500);
    await browser.keys(password);
  }
}

export default new registerPassword();
