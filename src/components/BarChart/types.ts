import {
  DateDropdownOption,
  DateDropdownOptionComparison,
} from "../DateDropdown/types";
import { ChartData } from "chart.js";

export interface BarChartProps {
  data: ChartData<"bar"> | undefined;
  view: string;
  weekOptions: DateDropdownOption[];
  barChartWeeks: DateDropdownOptionComparison;
  setBarChartWeeks: (data: DateDropdownOptionComparison) => void;
}
