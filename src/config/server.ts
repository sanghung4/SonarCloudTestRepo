export const server = {
  port: parseInt(process.env.PORT || '', 10) || 4000,
  debug: process.env.NODE_ENV !== 'production'
};
