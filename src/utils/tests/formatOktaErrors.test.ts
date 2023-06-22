import { formatOktaErrors } from 'utils/formatOktaErrors';

describe('Utils - formatOktaErrors', () => {
  it('should return only the error message', () => {
    const error =
      'Error: HTTP 403, Okta E0000014 (Update of credentials failed - Password cannot be your current password), ErrorId oaelVIm0laSTMmyKrWqhv7t5Q';
    const expected =
      'Update of credentials failed - Password cannot be your current password';
    expect(formatOktaErrors(error)).toBe(expected);
  });
});
