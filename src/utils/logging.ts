import {
  ApolloServerPlugin,
  GraphQLRequestContext,
  GraphQLRequestContextDidEncounterErrors,
  GraphQLRequestContextWillSendResponse,
  GraphQLRequestListener
} from 'apollo-server-plugin-base';
import pino from 'pino';
import { v4 as uuidv4 } from 'uuid';

import { Context } from '../context';

export const logger = pino();

export const loggingPlugin: ApolloServerPlugin<Context> = {
  requestDidStart(
    requestContext: GraphQLRequestContext<Context>
  ): GraphQLRequestListener<Context> | void {
    const start = Date.now();
    requestContext.context.requestId = uuidv4();
    const headers = requestContext.request.http?.headers!;
    const headersObj: Record<string, string> = {};
    for (const e of headers) {
      const key = e[0];
      const value = key !== 'authorization' ? e[1] : 'Bearer ****';
      headersObj[key] = value;
    }

    requestContext.context.logger = logger.child({
      requestId: requestContext.context.requestId,
      query: requestContext.request.query,
      queryVariables: requestContext.request.variables,
      headers: headersObj
    });

    return {
      didEncounterErrors(
        requestContext: GraphQLRequestContextDidEncounterErrors<Context>
      ) {
        requestContext.context.logger?.error(
          { errors: requestContext.errors },
          'Errors encountered'
        );
      },
      willSendResponse(
        requestContext: GraphQLRequestContextWillSendResponse<Context>
      ) {
        requestContext.context.logger?.info(
          { requestDuration: Date.now() - start },
          'Request complete'
        );
      }
    };
  }
};
