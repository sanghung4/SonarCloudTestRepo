import { ContextFunction } from 'apollo-server-core';
import { Logger } from 'pino';

import dataSources from './dataSources';
import { getToken, decodeAccessToken } from './utils/auth';
import { logger } from './utils/logging';
import { okta as oktaConfig } from './config';


export enum ErpSystem {
  MINCRON = 'MINCRON',
  ECLIPSE = 'ECLIPSE'
}

export interface Context {
  token?: string;
  branchId?: string;
  countId?: string;
  sessionId?: string;
  keepAlive?: string;
  erpSystem?: ErpSystem;
  dataSources: typeof dataSources;
  logger?: Logger;
  requestId?: string;
}

const context: ContextFunction<any, Context> = async ({ req }) => {
  try {
    const { authorization } = req.headers;

    if (oktaConfig.isDisabled) {
      return {
        branchId: req.headers['x-branchid'],
        countId: req.headers['x-countid'],
        erpSystem: req.headers['x-erpsystem'],
        sessionId: req.headers['x-session-id'],
        keepAlive: req.headers['Connection'],
        ...req
      };
    }

    if (!authorization) {
      return {}
    }

    const token = getToken(authorization);
    const jwt = await decodeAccessToken(token);

    return {
      branchId: req.headers['x-branchid'],
      countId: req.headers['x-countid'],
      erpSystem: req.headers['x-erpsystem'],
      sessionId: req.headers['x-session-id'],
      keepAlive: req.headers['Connection'],
      token,
      jwt,
      ...req
    };
  } catch (e) {
    logger.error(e, 'Error caught creating GraphQL context.');
    throw e;
  }
};

export default context;
