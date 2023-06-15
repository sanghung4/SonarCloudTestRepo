import { useState, useEffect } from "react";
import { Banner } from "../../components/Banner";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  ArcElement,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ChartData,
} from "chart.js";
import { MetricCard } from "../../components/MetricCard";
import { TotalUsage } from "../../components/TotalUsage";
import { PercentageChange } from "../../components/PercentageChange";
import { BarChart } from "../../components/BarChart";
import { PieChart } from "../../components/PieChart";
import clsx from "clsx";
import { getWeeks } from "../../utils/selectionWeeks";
import {
  MetricsDivision,
  MetricsPercentageTotalDivision,
  useGetPercentageChangeQuery,
  useGetPercentageTotalQuery,
  useGetTotalOverviewQuery,
  useGetTotalUsageQuery,
} from "../../graphql";
import { Data as PieChartData } from "react-minimal-pie-chart/types/commonTypes";
import { DateDropdownOptionComparison } from "../../components/DateDropdown/types";

const Metrics = () => {
  // ----------------- //
  // ----- State ----- //
  // ----------------- //
  const [interval, setInterval] = useState(true);
  const [view, setView] = useState("Users");
  const [weeks] = useState(getWeeks);
  const [usageWeek, setUsageWeek] = useState(weeks[0]);

  const [barChartData, setBarChartData] = useState<ChartData<"bar">>();
  const [pieChartData, setPieChartData] = useState<PieChartData>([]);
  const [pieChartWeek, setPieChartWeek] = useState(weeks[0]);
  const [percentageWeeks, setPercentageWeeks] =
    useState<DateDropdownOptionComparison>({
      comparisonA: weeks[1],
      comparisonB: weeks[0],
    });
  const [barChartWeeks, setBarChartWeeks] =
    useState<DateDropdownOptionComparison>({
      comparisonA: weeks[1],
      comparisonB: weeks[0],
    });

  // --------------------- //
  // ----- Constants ----- //
  // --------------------- //
  const viewTabs = ["Users", "Logins", "Branches"];

  // --------------- //
  // ----- API ----- //
  // --------------- //

  // total usage api call
  const { data: totalUsageData } = useGetTotalUsageQuery({
    variables: {
      input: { startDate: usageWeek.start, endDate: usageWeek.end },
    },
  });

  // barchart api call
  const { data: totalUsageDataA } = useGetTotalUsageQuery({
    variables: {
      input: {
        startDate: barChartWeeks.comparisonA.start,
        endDate: barChartWeeks.comparisonA.end,
      },
    },
  });

  // barchart api call comparison
  const { data: totalUsageDataB } = useGetTotalUsageQuery({
    variables: {
      input: {
        startDate: barChartWeeks.comparisonB.start,
        endDate: barChartWeeks.comparisonB.end,
      },
    },
  });

  // percentage change api call
  const { data: percentageChangeData } = useGetPercentageChangeQuery({
    variables: {
      input: {
        startDateWeekOld: percentageWeeks.comparisonA.start,
        endDateWeekOld: percentageWeeks.comparisonA.end,
        startDateWeekNew: percentageWeeks.comparisonB.start,
        endDateWeekNew: percentageWeeks.comparisonB.end,
      },
    },
  });

  // metric cards api call
  const { data: totalOverviewData } = useGetTotalOverviewQuery({
    variables: {
      input: {
        startDateWeekOld: weeks[1].start,
        endDateWeekOld: weeks[1].end,
        startDateWeekNew: weeks[0].start,
        endDateWeekNew: weeks[0].end,
      },
    },
  });

  // piechart api call
  const { data: percentageTotalData } = useGetPercentageTotalQuery({
    variables: {
      input: {
        startDate: pieChartWeek.start,
        endDate: pieChartWeek.end,
      },
    },
  });

  // ------------------- //
  // ----- Effects ----- //
  // ------------------- //
  // Set data for bar chart
  useEffect(() => {
    if (
      totalUsageDataA?.totalUsage.response &&
      totalUsageDataB?.totalUsage.response
    ) {
      const labels: string[] = [];

      const { response: responseA } = totalUsageDataA.totalUsage;
      const { response: responseB } = totalUsageDataB.totalUsage;

      // Get labels from data set A
      responseA.forEach((division) => {
        if (division && division.division !== "Grand Total") {
          labels.push(division.division);
        }
      });

      const datasetA = {
        label: barChartWeeks.comparisonA.display,
        data: labels.map((name) =>
          getDataValue(
            responseA.find((division) => division?.division === name)
          )
        ),
        backgroundColor: "#007ce6",
      };

      // Get labels from data set B
      responseB.forEach((division) => {
        if (
          division &&
          division.division !== "Grand Total" &&
          !labels.includes(division.division)
        ) {
          labels.push(division.division);
        }
      });

      const datasetB = {
        label: barChartWeeks.comparisonB.display,
        data: labels.map((name) =>
          getDataValue(
            responseB.find((division) => division?.division === name)
          )
        ),
        backgroundColor: "#f97316",
      };

      setBarChartData({
        labels: labels,
        datasets: [datasetA, datasetB],
      });
    }
  }, [totalUsageDataA, totalUsageDataB, view]);

  // Set data for pie chart
  useEffect(() => {
    if (percentageTotalData?.percentageTotal?.response) {
      const displayData: PieChartData = [];
      const colors = ["#f97316", "#007ce6", "#16a34a", "#7c3aed", "#eab308"];
      percentageTotalData.percentageTotal.response.forEach(
        (division, index) => {
          if (division) {
            displayData.push({
              title: division.division,
              value: getDataValue(division),
              color: colors[index],
            });
          }
        }
      );

      setPieChartData(displayData);
    }
  }, [percentageTotalData, view]);

  // ------------------- //
  // ----- Helpers ----- //
  // ------------------- //
  const getDataValue = (
    chartItem:
      | MetricsPercentageTotalDivision
      | MetricsDivision
      | undefined
      | null
  ) => {
    if (view === "Users") return chartItem?.userCount || 0;
    else if (view === "Logins") return chartItem?.loginCount || 0;
    else return chartItem?.branchCount || 0;
  };

  // -------------------------- //
  // ----- Chart Register ----- //
  // -------------------------- //
  ChartJS.register(ArcElement, Tooltip, Legend);
  ChartJS.register(
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend
  );
  return (
    <>
      <Banner header='Metrics' />
      <div className='flex items-center justify-center'>
        <div className='w-full 2xl:w-5/6 3xl:w-2/3'>
          <div className='flex my-10 mx-5 md:mx-8 text-reece-800 font-semibold text-xl'>
            BIA Usage Report
            <ul className='flex bg-white max-w-fit rounded-full ml-3'>
              <li
                className={clsx(
                  interval
                    ? "bg-reece-500 border-reece-500 text-white"
                    : "px-3 text-gray-700 border-white",
                  "px-3 border rounded-full cursor-pointer  text-base font-normal"
                )}
                // onClick={() => toggleInterval(true)}
              >
                Weekly
              </li>
              <li
                className={clsx(
                  !interval
                    ? "bg-reece-500 border-reece-500 text-white"
                    : "px-3  border-white",
                  "px-3 border rounded-full cursor-not-allowed text-gray-700 text-base font-normal"
                )}
                // onClick={() => toggleInterval(false)}
              >
                Monthly (WIP)
              </li>
            </ul>
          </div>
          <div className='flex flex-wrap my-4 mx-1 md:mx-4'>
            {totalOverviewData?.totalOverview?.response ? (
              totalOverviewData?.totalOverview?.response.map(
                (card, cardIndex) =>
                  card && (
                    <MetricCard
                      key={cardIndex}
                      title={card.metric}
                      amount={card.quantity || ""}
                      percentage={
                        card.percentageChange === "∞"
                          ? "0"
                          : card.percentageChange || ""
                      }
                    />
                  )
              )
            ) : (
              <MetricCard key={123} title='' amount='0' percentage='0' />
            )}
          </div>
          <div className='flex mb-8 mx-5 md:mx-8'>
            View:
            <ul className='flex bg-white max-w-fit rounded-full ml-3'>
              {viewTabs.map((viewType, sIndex) => (
                <li
                  className={clsx(
                    view === viewType
                      ? "bg-reece-500 border-reece-500 text-white"
                      : "px-3 text-gray-700 border-white",
                    "px-3 border rounded-full cursor-pointer"
                  )}
                  key={sIndex}
                  onClick={() => setView(viewType)}
                >
                  {viewType}
                </li>
              ))}
            </ul>
          </div>
          <div className='grid grid-cols-3 md:grid-cols-6 lg:grid-cols-12 gap-5 md:gap-8 mx-5 mb-5 md:mx-8 md:mb-8'>
            <div className='col-span-3 md:col-span-6 lg:col-span-12 xl:col-span-7 2xl:col-span-7 bg-white rounded p-5 shadow-md'>
              <BarChart
                data={barChartData}
                view={view}
                weekOptions={weeks}
                barChartWeeks={barChartWeeks}
                setBarChartWeeks={setBarChartWeeks}
              />
            </div>
            <div className='col-span-3 md:col-span-6 lg:col-span-12 xl:col-span-5 2xl:col-span-5 bg-white rounded p-5 shadow-md'>
              <TotalUsage
                usageData={totalUsageData?.totalUsage.response}
                weekOptions={weeks}
                usageWeek={usageWeek}
                setUsageWeek={setUsageWeek}
              />
            </div>
            <div className='col-span-3 md:col-span-6 lg:col-span-12 xl:col-span-6 2xl:col-span-6 bg-white rounded p-5 shadow-md'>
              <PercentageChange
                percentageData={percentageChangeData?.percentageChange.response}
                weekOptions={weeks}
                percentageWeeks={percentageWeeks}
                setPercentageWeeks={setPercentageWeeks}
              />
            </div>
            <div className='max-h-sm col-span-3 md:col-span-6 lg:col-span-12 xl:col-span-6 bg-white rounded p-5 shadow-md'>
              <PieChart
                data={pieChartData}
                view={view}
                weekOptions={weeks}
                pieChartWeek={pieChartWeek}
                setPieChartWeek={setPieChartWeek}
              />
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default Metrics;
