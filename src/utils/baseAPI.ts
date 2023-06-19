import { RESTDataSource, RequestOptions } from 'apollo-datasource-rest';
import { Request, Response } from 'apollo-server-env';

import { Context } from '../context';

// import { node as nodeConfig } from '../config';

// This class prevents caching when running in a development environment.
// Otherwise changes to local back end services won't be reflected here.
// There doesn't seem to be a better way to do this.
// https://github.com/apollographql/apollo-server/issues/1562
export class BaseAPI extends RESTDataSource<Context> {
  private deleteCacheForRequest(req: Request) {
    this.memoizedResults.delete(this.cacheKeyFor(req));
  }

  protected didReceiveResponse(response: Response, req: Request) {
    this.deleteCacheForRequest(req);
    return super.didReceiveResponse(response, req);
  }

  protected didEncounterError(error: Error, req: Request) {
    this.context.logger?.error(error, 'Error caught handing API call.');
    this.deleteCacheForRequest(req);
    return super.didEncounterError(error, req);
  }

  willSendRequest(req: RequestOptions) {
    if (this.context.token) {
      req.headers.set('Authorization', `Bearer ${this.context.token}`);
    }
    if (this.context.branchId) {
      req.headers.set('X-Branch-Id', this.context.branchId);
    }
    if (this.context.countId) {
      req.headers.set('X-Count-Id', this.context.countId);
    }
    if (this.context.sessionId) {
      req.headers.set('X-Session-Id', this.context.sessionId);
    }
     if (this.context.keepAlive) {
      req.headers.set('Connection', this.context.keepAlive);
    }
  }
}
