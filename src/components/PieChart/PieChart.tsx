import { DateDropdown } from "../DateDropdown";
import { PieChart as RMPieChart } from "react-minimal-pie-chart";
import { PieChartProps } from "./types";

const noData = [{ title: "N/A", value: 1, color: "#d1d5db" }];

const PieChart = ({
  data,
  view,
  weekOptions,
  pieChartWeek,
  setPieChartWeek,
}: PieChartProps) => {
  return (
    <>
      <div className='flex text-gray-800 items-center'>
        Percentage of Total {view}
        <DateDropdown
          options={weekOptions}
          selected={pieChartWeek}
          setSelected={setPieChartWeek}
        />
      </div>
      <div className='flex w-full'>
        <RMPieChart
          data={data.length ? data : noData}
          label={({ dataEntry }) => Math.round(dataEntry.percentage) + "%"}
          labelStyle={{
            fill: "#374151",
            fontSize: ".25rem",
            fontFamily: "sans-serif",
          }}
          radius={28}
          labelPosition={112}
          lineWidth={50}
          style={{ width: "70%", display: "flex" }}
        />
        <div className='flex w-1/4 items-center'>
          <ul className='list-none'>
            {data.length
              ? data.map((division, lIndex) => (
                  <li
                    key={lIndex}
                    className='flex text-sm text-gray-900 py-2 items-center'
                  >
                    <div
                      className='rounded-full h-5 w-5 min-w-[20px]'
                      style={{
                        background: division.color,
                      }}
                    />
                    <p className='ml-2'>{division.title}</p>
                  </li>
                ))
              : noData.map((division, lIndex) => (
                  <li
                    key={lIndex}
                    className='flex text-sm text-gray-900 py-2 items-center'
                  >
                    <div
                      className='rounded-full h-5 w-5 min-w-[20px]'
                      style={{
                        background: division.color,
                      }}
                    />
                    <p className='ml-2'>{division.title}</p>
                  </li>
                ))}
          </ul>
        </div>
      </div>
    </>
  );
};

export default PieChart;
