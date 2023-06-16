const { createLogger, format, transports } = require('winston');
// const CloudWatchTransport = require('winston-aws-cloudwatch');
// const AWS = require('aws-sdk');

// AWS.config.update({
//   region: 'us-east-1',
// });

exports.logger = createLogger({
  format: format.combine(format.timestamp(), format.json()),
  defaultMeta: { service: 'pi-batch-load' },
  transports: [
    // new CloudWatchTransport({
    //   logGroupName: 'pi-batch-load',
    //   logStreamName: 'pi-batch-load',
    //   createLogGroup: true,
    //   createLogStream: true,
    // }),
    new transports.Console(),
  ],
});
