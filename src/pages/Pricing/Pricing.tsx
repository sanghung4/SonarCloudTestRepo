import { SecureRoute } from "@okta/okta-react";
import { SetPricing } from "./SetPricing/SetPricing";
import { ReviewPricing } from "./ReviewPricing/ReviewPricing";
import { PricingProvider } from "./PricingProvider";
import { useRouteMatch } from "react-router-dom";

const Pricing = () => {
  const { path } = useRouteMatch();

  return (
    <PricingProvider>
      <SecureRoute exact path={path} component={SetPricing} />
      <SecureRoute path={`${path}/review`} component={ReviewPricing} />
    </PricingProvider>
  );
};

export default Pricing;
