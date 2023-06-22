import { ApolloError } from '@apollo/client';
import { parseError } from 'utils/errorhandling';

const mockError: ApolloError = {
  graphQLErrors: [],
  message: JSON.stringify(['error']),
  clientErrors: [],
  networkError: null,
  extraInfo: null,
  name: 'test'
};
describe('error Handling', () => {
  test('parses the error properly', () => {
    expect(parseError(mockError)).toEqual(['error']);
  });
  test('returns empty array when message is empty', () => {
    expect(parseError({ ...mockError, message: '' })).toEqual([]);
  });
  test('absorbs the error and parse correctly', () => {
    expect(parseError({ ...mockError, message: '[Error Parsing]' })).toEqual([]);
  });
  test('returns [] when messages type is not array', () => {
    expect(parseError({ ...mockError, message: '{message:"test"}' })).toEqual([]);
  });
});
