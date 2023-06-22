import { stateCode } from 'utils/states';

describe('stateCodes util', () => {
  test('should return correct state code for the state name', () => {
    expect(stateCode('Florida')).toEqual('FL');
  });
  test('should return correct state code for the state name with multiple words', () => {
    expect(stateCode('North Carolina')).toEqual('NC');
  });
  test('should return undefined for incorrect state code', () => {
    expect(stateCode('British Columbia')).toEqual(undefined);
  });
});
