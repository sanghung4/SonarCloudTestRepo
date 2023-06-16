const axios = require('axios').default;

const configService = axios.create({
  baseURL: process.env.SPRING_CLOUD_CONFIG_URI,
});

function getConfigName() {
  if (process.env.CONFIG_ENV != null) {
    return `pi-batch-load-${process.env.CONFIG_ENV}.json`;
  }

  return 'pi-batch-load.json';
}

async function loadConfig() {
  const configName = getConfigName();
  const resp = await configService.get(`/${configName}`);
  const config = resp.data;

  for (const key of Object.keys(config)) {
    process.env[key] = config[key];
  }
}

exports.loadConfig = loadConfig;
