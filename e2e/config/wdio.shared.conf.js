// eslint-disable-next-line @typescript-eslint/no-var-requires
require('dotenv').config();

exports.config = {
  //
  // ====================
  // Runner Configuration
  // ====================
  runner: 'local',
  // ===================
  // Test Configurations
  // ===================
  // Level of logging verbosity: trace | debug | info | warn | error | silent
  logLevel: 'info',
  bail: 0,
  baseUrl: 'http://localhost',
  waitforTimeout: 10000,
  connectionRetryTimeout: 90000,
  connectionRetryCount: 3,
  // ====================
  // Runner Configuration
  // ====================
  framework: 'mocha',
  reporters: ['spec'],
  //
  // Options to be passed to Mocha.
  // See the full list at http://mochajs.org/
  mochaOpts: {
    ui: 'bdd',
    timeout: 60000,
  },
  // ====================
  // Appium Configuration
  // ====================
  services: [
    [
      'appium',
      {
        // For options see
        // https://github.com/webdriverio/webdriverio/tree/master/packages/wdio-appium-service
        args: {
          // Auto download ChromeDriver
          relaxedSecurity: true,
          // chromedriverAutodownload: true,
          // For more arguments see
          // https://github.com/webdriverio/webdriverio/tree/master/packages/wdio-appium-service
        },
        command: 'appium',
      },
    ],
  ],
  port: 4723,
};
