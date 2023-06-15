import { DateDropdownOption } from "../DateDropdown";
import { GetTotalUsageQuery, MetricsDivision } from "../../graphql";

export interface TotalUsageData extends MetricsDivision {
  expand: boolean;
}

export interface TotalUsageProps {
  usageData: GetTotalUsageQuery["totalUsage"]["response"];
  weekOptions: DateDropdownOption[];
  usageWeek: DateDropdownOption;
  setUsageWeek: (data: DateDropdownOption) => void;
}
