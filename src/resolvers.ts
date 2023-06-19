import { mergeResolvers } from 'graphql-tools';
import { resolver as accountResolvers } from './account';
import { resolver as productResolvers } from './product';
import { resolver as inventoryResolvers } from './inventory';
import { resolver as pickingResolvers } from './picking';
import counterResolvers from './counter/resolver';
import { resolver as metricsResolvers } from './metrics';
import { resolver as pricingResolvers } from './pricing';
import { resolver as varianceResolvers } from './variance';
import { resolver as customerResolvers } from './customer';
import { resolver as userResolvers } from './user';

export default mergeResolvers([
  accountResolvers,
  productResolvers,
  inventoryResolvers,
  pricingResolvers,
  pickingResolvers,
  counterResolvers,
  metricsResolvers,
  varianceResolvers,
  customerResolvers,
  userResolvers,
]);
