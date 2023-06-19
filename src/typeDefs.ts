import { mergeTypeDefs } from '@graphql-tools/merge';
import { schema as accountSchema } from './account';
import { schema as productSchema } from './product';
import { schema as inventorySchema } from './inventory';
import { schema as pickingSchema } from './picking';
import { schema as counterSchema } from './counter';
import { schema as metricsSchema } from './metrics';
import { schema as pricingSchema } from './pricing';
import { schema as varianceSchema } from './variance';
import { schema as customerSchema } from './customer';
import { schema as userSchema } from './user';

const config = {
  useSchemaDefinition: false,
  throwOnConflict: false,
  reverseDirectives: true
};

export default mergeTypeDefs(
  [accountSchema, productSchema, inventorySchema, pickingSchema, counterSchema, metricsSchema, pricingSchema, varianceSchema, customerSchema, userSchema],
  config
);
