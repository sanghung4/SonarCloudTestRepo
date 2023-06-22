import { fireEvent, waitFor } from '@testing-library/react';
import { render, TestRender } from 'test-utils/TestWrapper';
import * as t from 'locales/en/translation.json';

import CCPA from 'Legal/CCPAForm';
import {
  CCPA_VAR_FAIL,
  CCPA_VAR_SUCCCESS,
  mockCCPAFail,
  mockCCPASuccess,
  MockCCPAVar
} from 'Legal/tests/mocks';
import { SnackbarProvider } from '@dialexa/reece-component-library';

describe('CCPA Form - Validation', () => {
  it('First Name field is left blank', async () => {
    const { getByTestId } = render(<CCPA />);
    const firstNameInput = getByTestId('firstNameInput');
    const firstNameHelper = getByTestId('firstNameHelper');

    fireEvent.focus(firstNameInput);
    fireEvent.change(firstNameInput, { target: { value: '      ' } });
    fireEvent.blur(firstNameInput);
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(firstNameHelper).toHaveTextContent(t.validation.firstNameRequired);
  });

  it('First Name field is valid as long it is filled', async () => {
    const { getByTestId } = render(<CCPA />);
    const firstNameInput = getByTestId('firstNameInput');
    const firstNameHelper = getByTestId('firstNameHelper');

    fireEvent.focus(firstNameInput);
    fireEvent.change(firstNameInput, { target: { value: 'Test' } });
    fireEvent.blur(firstNameInput);
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(firstNameHelper).not.toHaveTextContent(
      t.validation.firstNameRequired
    );
  });

  it('Last Name field is left blank', async () => {
    const { getByTestId } = render(<CCPA />);
    const lastNameInput = getByTestId('lastNameInput');
    const lastNameHelper = getByTestId('lastNameHelper');

    fireEvent.focus(lastNameInput);
    fireEvent.change(lastNameInput, { target: { value: '      ' } });
    fireEvent.blur(lastNameInput);
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(lastNameHelper).toHaveTextContent(t.validation.lastNameRequired);
  });

  it('Last Name field is valid as long it is filled', async () => {
    const { getByTestId } = render(<CCPA />);
    const lastNameInput = getByTestId('lastNameInput');
    const lastNameHelper = getByTestId('lastNameHelper');

    fireEvent.focus(lastNameInput);
    fireEvent.change(lastNameInput, { target: { value: 'Test' } });
    fireEvent.blur(lastNameInput);
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(lastNameHelper).not.toHaveTextContent(t.validation.lastNameRequired);
  });

  it('Phone Number field is not valid', async () => {
    const { getByTestId } = render(<CCPA />);
    const phoneNumberInput = getByTestId('phoneNumberInput');
    const phoneNumberHelper = getByTestId('phoneNumberHelper');

    fireEvent.focus(phoneNumberInput);
    fireEvent.change(phoneNumberInput, { target: { value: 1337 } });
    fireEvent.blur(phoneNumberInput);
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(phoneNumberHelper).toHaveTextContent(
      t.validation.phoneNumberInvalid
    );
  });

  it('Phone Number field is valid', async () => {
    const { getByTestId } = render(<CCPA />);
    const phoneNumberInput = getByTestId('phoneNumberInput');
    const phoneNumberHelper = getByTestId('phoneNumberHelper');

    fireEvent.focus(phoneNumberInput);
    fireEvent.change(phoneNumberInput, { target: { value: 4065551234 } });
    fireEvent.blur(phoneNumberInput);
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(phoneNumberHelper).not.toHaveTextContent(
      t.validation.phoneNumberInvalid
    );
  });

  it('Zip Code field is left blank', async () => {
    const { getByTestId } = render(<CCPA />);
    const zipCodeInput = getByTestId('zipCodeInput');
    const zipCodeHelper = getByTestId('zipCodeHelper');

    fireEvent.focus(zipCodeInput);
    fireEvent.blur(zipCodeInput);
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(zipCodeHelper).toHaveTextContent(t.validation.zipRequired);
  });

  it('Zip Code "123A" is not valid', async () => {
    const { getByTestId } = render(<CCPA />);
    const zipCodeInput = getByTestId('zipCodeInput');
    const zipCodeHelper = getByTestId('zipCodeHelper');

    fireEvent.focus(zipCodeInput);
    fireEvent.change(zipCodeInput, { target: { value: '123A' } });
    fireEvent.blur(zipCodeInput);
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(zipCodeHelper).not.toHaveTextContent(t.validation.zipRequired);
    expect(zipCodeHelper).toHaveTextContent(t.validation.zipInvalid);
    expect(zipCodeInput).toHaveValue('123');
  });

  it('Zip Code "77494-1234" is valid because it automatically parse "77494"', async () => {
    const { getByTestId } = render(<CCPA />);
    const zipCodeInput = getByTestId('zipCodeInput');
    const zipCodeHelper = getByTestId('zipCodeHelper');

    fireEvent.focus(zipCodeInput);
    fireEvent.change(zipCodeInput, { target: { value: '77494-1234' } });
    fireEvent.blur(zipCodeInput);
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(zipCodeHelper).not.toHaveTextContent(t.validation.zipRequired);
    expect(zipCodeHelper).not.toHaveTextContent(t.validation.zipInvalid);
    expect(zipCodeInput).toHaveValue('77494');
  });

  it('Zip Code "75080" is valid', async () => {
    const { getByTestId } = render(<CCPA />);
    const zipCodeInput = getByTestId('zipCodeInput');
    const zipCodeHelper = getByTestId('zipCodeHelper');

    fireEvent.focus(zipCodeInput);
    fireEvent.change(zipCodeInput, { target: { value: 75080 } });
    fireEvent.blur(zipCodeInput);
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(zipCodeHelper).not.toHaveTextContent(t.validation.zipRequired);
    expect(zipCodeHelper).not.toHaveTextContent(t.validation.zipInvalid);
    expect(zipCodeInput).toHaveValue('75080');
  });

  it('Email field is left blank', async () => {
    const { getByTestId } = render(<CCPA />);
    const emailInput = getByTestId('emailInput');
    const emailHelper = getByTestId('emailHelper');

    fireEvent.focus(emailInput);
    fireEvent.blur(emailInput);
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(emailHelper).toHaveTextContent(t.validation.emailRequired);
  });
  it('Email field is not valid', async () => {
    const { getByTestId } = render(<CCPA />);
    const emailInput = getByTestId('emailInput');
    const emailHelper = getByTestId('emailHelper');

    fireEvent.focus(emailInput);
    fireEvent.change(emailInput, { target: { value: 'notanemail' } });
    fireEvent.blur(emailInput);
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(emailHelper).not.toHaveTextContent(t.validation.emailRequired);
    expect(emailHelper).toHaveTextContent(t.validation.emailInvalid);
  });

  it('Email field is valid', async () => {
    const { getByTestId } = render(<CCPA />);
    const emailInput = getByTestId('emailInput');
    const emailHelper = getByTestId('emailHelper');

    fireEvent.focus(emailInput);
    fireEvent.change(emailInput, {
      target: { value: 'farmer.macjoy@yahoo.com' }
    });
    fireEvent.blur(emailInput);
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(emailHelper).not.toHaveTextContent(t.validation.emailRequired);
    expect(emailHelper).not.toHaveTextContent(t.validation.emailInvalid);
  });

  it('Topic field is left blank', async () => {
    const { getByTestId } = render(<CCPA />);
    const topicContainer = getByTestId('topicContainer');
    const topicSelect = getByTestId('topicSelect');
    const submitButton = getByTestId('sendButton');

    const requiredTxt = new RegExp(t.validation.topicRequired);
    fireEvent.mouseDown(topicSelect);
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    fireEvent.click(submitButton);
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(topicContainer).toHaveTextContent(requiredTxt);
  });

  it('Topic field has filled', async () => {
    const { getByTestId } = render(<CCPA />);
    const topicContainer = getByTestId('topicContainer');
    const topicSelect = getByTestId('topicSelect');
    const topicInput = getByTestId('topicInput');
    const submitButton = getByTestId('sendButton');

    const requiredTxt = new RegExp(t.validation.topicRequired);
    fireEvent.mouseDown(topicSelect);
    fireEvent.change(topicInput, {
      target: { value: t.legal.ccpaRequestTypeOptOut }
    });
    fireEvent.click(submitButton);
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(topicContainer).not.toHaveTextContent(requiredTxt);
  });

  it('Message field is left blank', async () => {
    const { getByTestId } = render(<CCPA />);
    const messageInput = getByTestId('messageInput');
    const messageHelper = getByTestId('messageHelper');

    fireEvent.focus(messageInput);
    fireEvent.change(messageInput, { target: { value: '      ' } });
    fireEvent.blur(messageInput);
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(messageHelper).toHaveTextContent(t.validation.messageRequired);
  });

  it('Message is valid as long it is filled', async () => {
    const { getByTestId } = render(<CCPA />);
    const messageInput = getByTestId('messageInput');
    const messageHelper = getByTestId('messageHelper');

    fireEvent.focus(messageInput);
    fireEvent.change(messageInput, { target: { value: 'Test' } });
    fireEvent.blur(messageInput);
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(messageHelper).not.toHaveTextContent(t.validation.messageRequired);
  });
});

const fillCCPAFormInputs = (utils: TestRender, variables: MockCCPAVar) => {
  const firstNameInput = utils.getByTestId('firstNameInput');
  const lastNameInput = utils.getByTestId('lastNameInput');
  const emailInput = utils.getByTestId('emailInput');
  const phoneNumberInput = utils.getByTestId('phoneNumberInput');
  const zipCodeInput = utils.getByTestId('zipCodeInput');
  const messageInput = utils.getByTestId('messageInput');
  const topicSelect = utils.getByTestId('topicSelect');
  const topicInput = utils.getByTestId('topicInput');

  fireEvent.focus(firstNameInput);
  fireEvent.change(firstNameInput, {
    target: { value: variables.firstName }
  });
  fireEvent.blur(firstNameInput);

  fireEvent.focus(lastNameInput);
  fireEvent.change(lastNameInput, {
    target: { value: variables.lastName }
  });
  fireEvent.blur(lastNameInput);

  fireEvent.focus(emailInput);
  fireEvent.change(emailInput, {
    target: { value: variables.email }
  });
  fireEvent.blur(emailInput);

  fireEvent.focus(phoneNumberInput);
  fireEvent.change(phoneNumberInput, {
    target: { value: variables.phoneNumber }
  });
  fireEvent.blur(phoneNumberInput);

  fireEvent.focus(zipCodeInput);
  fireEvent.change(zipCodeInput, {
    target: { value: variables.zip }
  });
  fireEvent.blur(zipCodeInput);

  fireEvent.focus(messageInput);
  fireEvent.change(messageInput, {
    target: { value: variables.message }
  });
  fireEvent.blur(messageInput);

  fireEvent.mouseDown(topicSelect);
  fireEvent.change(topicInput, {
    target: { value: variables.topic }
  });
  fireEvent.blur(topicInput);
};

describe('CCPA Form - Submit', () => {
  it('Should expect error message when onError', async () => {
    const utils = render(
      <SnackbarProvider>
        <CCPA />
      </SnackbarProvider>
    );

    fillCCPAFormInputs(utils, CCPA_VAR_FAIL);
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    fireEvent.click(utils.getByTestId('sendButton'));
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    const errorMsg = await utils.findByText(t.common.messageFail);
    expect(errorMsg).toBeInTheDocument();
  });

  it('Should expect success message rendered when submission succeeds', async () => {
    const utils = render(
      <SnackbarProvider>
        <CCPA />
      </SnackbarProvider>,
      { mocks: [mockCCPASuccess] }
    );

    fillCCPAFormInputs(utils, CCPA_VAR_SUCCCESS);
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    fireEvent.click(utils.getByTestId('sendButton'));
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    const successMsg = await utils.findByText(t.common.messageSent);
    expect(successMsg).toBeInTheDocument();
  });

  it('Should expect error message when submission fails', async () => {
    const utils = render(
      <SnackbarProvider>
        <CCPA />
      </SnackbarProvider>,
      { mocks: [mockCCPAFail] }
    );

    fillCCPAFormInputs(utils, CCPA_VAR_FAIL);
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    fireEvent.click(utils.getByTestId('sendButton'));
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    const errorMsg = await utils.findByText(t.common.messageFail);
    expect(errorMsg).toBeInTheDocument();
  });
});
