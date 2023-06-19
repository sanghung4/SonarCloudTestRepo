import { CounterAPI } from './counter/counterAPI';
import { AccountAPI } from './account';
import { InventoryAPI, InventoryBatchAPI } from './inventory';
import { PickingAPI } from './picking';
import { ProductAPI, EclipseAPI } from './product';
import { MetricsAPI } from './metrics';
import { PricingAPI } from './pricing';
import { VarianceAPI } from './variance';
import { CustomerAPI } from './customer';
import { UserAPI } from './user';

export default {
  accountAPI: new AccountAPI(),
  eclipseAPI: new EclipseAPI(),
  productAPI: new ProductAPI(),
  inventoryAPI: new InventoryAPI(),
  inventoryBatchAPI: new InventoryBatchAPI(),
  pricingAPI: new PricingAPI(),
  pickingAPI: new PickingAPI(),
  counterAPI: new CounterAPI(),
  metricsAPI: new MetricsAPI(),
  varianceAPI: new VarianceAPI(),
  customerAPI: new CustomerAPI(),
  userAPI: new UserAPI(),
};
