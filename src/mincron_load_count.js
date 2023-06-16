const axios = require('axios').default;
const Bluebird = require('bluebird');
const { logger } = require('./logger');
const { formatElapsedTime } = require('./utils');

const { loadConfig } = require('./config');
async function mincronLoad() {
  await loadConfig();
  const startTime = Date.now();
  logger.info('Starting pi-batch-load-mincron');

  const inventoryService = axios.create({
    baseURL: `${process.env.API_URL_INVENTORY_BATCH_SERVICE}`,
    auth: {
      username: process.env.INTERNAL_USERNAME,
      password: process.env.INTERNAL_PASSWORD,
    },
  });

  const mincronService = axios.create({
    baseURL: `${process.env.API_URL_MINCRON_SERVICE}`,
  });

  const NUM_CONCURRENT_OPERATIONS = parseInt(
    process.env.NUM_CONCURRENT_OPERATIONS || '1',
    10
  );

  logger.info('Calling mincron-service to get list of counts.');
  await Bluebird.map(
    mincronService.get('/inventory/counts').then((resp) => {
      return resp.data.counts;
    }),
    (mincronCount) => {
      const url = `/internal/branches/${mincronCount.branchNum}/counts/${mincronCount.countId}/_load`;
      logger.info(
        `Loading count ${mincronCount.countId} at branch ${mincronCount.branchNum}.`
      );
      return inventoryService.put(url);
    },
    {
      concurrency: NUM_CONCURRENT_OPERATIONS,
    }
  );

  logger.info(
    `Completed pi-batch-load-mincron, Elapsed time: ${formatElapsedTime(
      Date.now() - startTime
    )}.`
  );
}

exports.mincronLoad = mincronLoad;
