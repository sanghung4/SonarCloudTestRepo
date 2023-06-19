export const okta = {
  issuer: `${process.env.OKTA_DOMAIN}`,
  clientId: `${process.env.OKTA_CLIENT_ID}`,
  isDisabled: `${process.env.OKTA_DISABLED}` === 'true',
  audience:  process.env.OKTA_AUDIENCE || 'api://default'
};
