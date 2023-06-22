import { ApolloError } from '@apollo/client';

export const parseError = (e: ApolloError): string[] => {
  let msgs;
  try {
    msgs = JSON.parse(e.message);
    msgs = Array.isArray(msgs)
      ? msgs.filter((val) => typeof val === 'string')
      : [];
  } catch (e) {
    msgs = [];
  }
  return msgs;
};
