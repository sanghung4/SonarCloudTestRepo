import { Bar } from "react-chartjs-2";
import { DateDropdown, DateDropdownOption } from "../DateDropdown";
import { BarChartProps } from "./types";
import { barOptions, defaultBarData } from "./utils";

const BarChart = ({
  data,
  view,
  weekOptions,
  barChartWeeks,
  setBarChartWeeks,
}: BarChartProps) => {
  const handleSelectDateStart = (selection: DateDropdownOption) => {
    setBarChartWeeks({ ...barChartWeeks, comparisonA: selection });
  };

  const handleSelectDateEnd = (selection: DateDropdownOption) => {
    setBarChartWeeks({ ...barChartWeeks, comparisonB: selection });
  };

  return (
    <div>
      <div className='flex mb-10 text-gray-800 items-center'>
        Total {view}
        <DateDropdown
          options={weekOptions}
          selected={barChartWeeks.comparisonA}
          setSelected={handleSelectDateStart}
        />
        to
        <DateDropdown
          options={weekOptions}
          selected={barChartWeeks.comparisonB}
          setSelected={handleSelectDateEnd}
        />
      </div>
      <Bar options={barOptions} data={data || defaultBarData} />
    </div>
  );
};

export default BarChart;
