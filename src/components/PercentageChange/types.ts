import {
  DateDropdownOption,
  DateDropdownOptionComparison,
} from "../DateDropdown/types";
import { GetPercentageChangeQuery } from "../../graphql";

export interface PercentageChangeProps {
  percentageData: GetPercentageChangeQuery["percentageChange"]["response"];
  weekOptions: DateDropdownOption[];
  percentageWeeks: DateDropdownOptionComparison;
  setPercentageWeeks: (data: DateDropdownOptionComparison) => void;
}
