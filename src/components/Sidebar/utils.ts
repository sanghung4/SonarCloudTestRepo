import {
  DatabaseIcon,
  CreditCardIcon,
  ChartSquareBarIcon,
  CalculatorIcon,
  CogIcon,
} from "@heroicons/react/solid";
import { NavigationTypes } from "./types";

function today(){
  const date = new Date();
  date.setHours(0, 0, 0, 0);
  return date.toISOString().slice(0, 10);
};

export const navigation: NavigationTypes[] = [
  {
    name: "Metrics",
    href: "/metrics",
    group: "WMS Admin - Metrics",
    icon: ChartSquareBarIcon,
  },
  {
    name: "Pricing",
    href: "/pricing",
    group: "WMS Admin - Pricing" ,
    icon: CreditCardIcon,
  },
  {
    name: "Pricing",
    href: "/pricing",
    group: "WMS Admin - Pricing Read Only",
    icon: CreditCardIcon,
  },
  {
    name: "Count Status",
    href: `/count?startDate=${today()}&endDate=${today()}&countStatus=All Status`,
    group: "WMS Admin - Count",
    icon: CalculatorIcon,
  },
  {
    name: "Pricing Admin",
    href: "/pricingAdmin",
    group: "WMS Admin - Pricing Administrators",
    icon: CogIcon,
  },
];
