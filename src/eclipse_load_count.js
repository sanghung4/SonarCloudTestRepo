const { formatElapsedTime } = require('./utils');

const axios = require('axios').default;
const { logger } = require('./logger');

const { loadConfig } = require('./config');
const sessionIdManager = require('./session-id-manager');

async function eclipseLoad() {
  await loadConfig();
  const startTime = Date.now();
  logger.info('Starting pi-batch-load-eclipse');

  const inventoryService = axios.create({
    baseURL: `${process.env.API_URL_INVENTORY_BATCH_SERVICE}`,
    auth: {
      username: process.env.INTERNAL_USERNAME,
      password: process.env.INTERNAL_PASSWORD,
    },
  });

  let initialSessionId = sessionIdManager.getSessionId();
  logger.info(
    `Calling EclipseConnect to get list of counts using session ${initialSessionId}`
  );

  const url = `/internal/eclipse/_load`;
  const sessionId = sessionIdManager.getSessionId();

  await inventoryService
    .put(url, null, {
      headers: { 'X-Session-Id': sessionId },
    })
    .finally(() => {
      sessionIdManager.returnSessionId(sessionId);
    });

  logger.info(
    `Completed pi-batch-load-eclipse, Elapsed time: ${formatElapsedTime(
      Date.now() - startTime
    )}.`
  );
}

exports.eclipseLoad = eclipseLoad;
