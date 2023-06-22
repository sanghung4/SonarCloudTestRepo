import { checkStatus } from 'utils/statusMapping';

describe('statusMapping util', () => {
  test('should return "delivery" without an underscore in status', () => {
    expect(checkStatus('OT OUR TRUCK')).toEqual('delivery');
  });
  test('should return "delivery" with an underscore in status', () => {
    expect(checkStatus('OT_OUR_TRUCK')).toEqual('delivery');
  });
  test('should return `undefined`with an underscore in status', () => {
    expect(checkStatus('NOTFOUNDTEST')).toEqual(undefined);
  });
});
