import { createPrometheusExporterPlugin } from '@bmatei/apollo-prometheus-exporter';
import * as health from '@cloudnative/health-connect';
import express from 'express';
import dotenv from 'dotenv';
import cors from 'cors';
import { makeExecutableSchema } from 'graphql-tools';
import { ApolloServer } from 'apollo-server-express';

dotenv.config();

import { server as config } from './config';
import context from './context';
import typeDefs from './typeDefs';
import resolvers from './resolvers';
import dataSources from './dataSources';
import { loggingPlugin } from './utils/logging';

const schema = makeExecutableSchema({
  typeDefs,
  resolvers
});

const app = express();

const prometheusExporterPlugin = createPrometheusExporterPlugin({ app });

const server = new ApolloServer({
  context,
  schema,
  dataSources: () => dataSources,
  plugins: [loggingPlugin, prometheusExporterPlugin]
});

const healthcheck = new health.HealthChecker();

app.use(cors());
app.use('/live', health.LivenessEndpoint(healthcheck));
app.use('/ready', health.ReadinessEndpoint(healthcheck));
app.use('/health', health.HealthEndpoint(healthcheck));

server.applyMiddleware({ app, path: '/' });

app.listen(config, () =>
  console.log(
    `🚀  Server ready at http://localhost:${config.port}${server.graphqlPath}`
  )
);
