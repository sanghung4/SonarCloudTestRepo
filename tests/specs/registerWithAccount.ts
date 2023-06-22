const { percySnapshot } = require('@percy/webdriverio')

import registerEnterAccountPage from '../pageObjects/registerEnterAccount';
import registerConfirmCompanyInfo from '../pageObjects/registerConfirmCompanyInfo';
import registerContactInfo from '../pageObjects/registerContactInfo';
import registerPassword from '../pageObjects/registerPassword';
import registerComplete from '../pageObjects/registerComplete';

import { Chance } from 'chance';

const chance = new Chance();

const testUser = {
    accountNumber: chance.integer({ min: 1, max: 1000 }),
    firstName: chance.first(),
    lastName: chance.last(),
    phoneNumber: '123-123-1234',
    email: chance.email(),
    password: 'Password1'
}

describe('A user registering with an existing account number', () => {
    it('should be able to enter an existing account number', async () => {
        await registerEnterAccountPage.open();
        await percySnapshot(browser, 'Register w/an Account Page');
    
        await registerEnterAccountPage.enterAccountNumber(testUser.accountNumber);
        (await registerEnterAccountPage.continueButton).click();
        
        // confirm Company Info page is loaded to signify a valid existing account number was entered
        await expect(registerConfirmCompanyInfo.confirmButton).toBeClickable();
        await percySnapshot(browser, 'Register Confirm Company Info Page');
    });

    it('should be able to confirm their company information', async () => {
        (await registerConfirmCompanyInfo.confirmButton).click();

         // confirm Contact Info page is loaded to signify the company info was correct
         await expect(registerContactInfo.continueButton).toBeClickable();
         await percySnapshot(browser, 'Register Contact Information');
    });

    it('should be able to enter their contact information', async () => {
        await registerContactInfo.fillOutContactInfo(testUser);
        
         // confirm valid confirmation information was entered
         await expect(registerPassword.continueButton).toBeClickable();
         await percySnapshot(browser, 'Register Password');
    });

    it('should be able to enter a desired password and complete their registration flow', async () => {
        await registerPassword.fillInPassword(testUser.password);
        (await registerPassword.continueButton).click();

         // confirm Contact Info page is loaded to signify the company info was correct
         await expect(registerComplete.startBrowsingButton).toBeClickable();
         await percySnapshot(browser, 'Register Complete');
    });
});