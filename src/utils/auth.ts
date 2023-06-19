import { AuthenticationError } from 'apollo-server-express';
import { okta as oktaConfig } from '../config';
import OktaJwtVerifier from '@okta/jwt-verifier'

const oktaJwtVerifier = new OktaJwtVerifier({
  issuer: `${oktaConfig.issuer}`
});

export const decodeAccessToken = async (token: string) => {
  try {
    const { claims } = await oktaJwtVerifier.verifyAccessToken(token, oktaConfig.audience);
    return { claims, token };
  } catch (error) {
    if (error instanceof AuthenticationError) {
      throw error;
    } else {
      throw new AuthenticationError('Unable to validate token');
    }
  }
};

export const getToken = (authorization: string) => {
  const [authType, token] = authorization.trim().split(' ');

  if (authType !== 'Bearer') {
    throw new AuthenticationError(`Invalid token type: ${authType}`);
  }

  return token;
};
