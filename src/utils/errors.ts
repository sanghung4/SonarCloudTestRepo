import { createError } from 'apollo-errors';

export const BadRequestError = createError('BadRequestError', {
  message: 'A bad request was detected'
});

export const UnauthorizedError = createError('UnauthorizedError', {
  message: 'Unauthorized'
});

export const ForbiddenError = createError('ForbiddenError', {
  message: 'Forbidden'
});

export const UnknownError = createError('UnknownError', {
  message: 'An unexpected error has occurred'
});
