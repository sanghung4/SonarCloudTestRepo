import { getNodeAutoInstrumentations } from '@opentelemetry/auto-instrumentations-node';
import { JaegerExporter } from '@opentelemetry/exporter-jaeger';
import { NodeSDK } from '@opentelemetry/sdk-node';

import { logger } from './utils/logging';

const sdk = new NodeSDK({
  traceExporter: new JaegerExporter({
    endpoint: 'http://jaeger-collector:14268/api/traces',
  }),
  instrumentations: [getNodeAutoInstrumentations()],
});

sdk
  .start()
  .then(() => {
    logger.info('Tracing initialized');
  })
  .catch((err) => {
    logger.error({ errors: [err] }, 'Error initializing tracing');
  });

process.on('SIGTERM', () => {
  sdk
    .shutdown()
    .then(() => {
      logger.info('Tracing terminated');
    })
    .catch((err) => {
      logger.info('Error terminating tracing', err);
    })
    .finally(() => {
      // eslint-disable-next-line no-process-exit
      process.exit(0);
    });
});
