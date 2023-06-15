import { DateDropdownOption } from "../DateDropdown";
import { Data } from "react-minimal-pie-chart/types/commonTypes";

export interface PieChartProps {
  data: Data;
  view: string;
  weekOptions: DateDropdownOption[];
  pieChartWeek: DateDropdownOption;
  setPieChartWeek: (data: DateDropdownOption) => void;
}
