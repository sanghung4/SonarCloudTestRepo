import clsx from "clsx";
import { DateDropdown, DateDropdownOption } from "../DateDropdown";
import { PercentageChangeProps } from "./types";

const keys = ["Division", "Total Users", "Total Logins", "No. of Branches"];

const PercentageChange = ({
  percentageData,
  weekOptions,
  percentageWeeks,
  setPercentageWeeks,
}: PercentageChangeProps) => {
  const normalizeValue = (value: string | null | undefined) => {
    return value === "∞" ? "0.0" : value;
  };

  const handleComparisonChangeA = (selection: DateDropdownOption) => {
    setPercentageWeeks({ ...percentageWeeks, comparisonA: selection });
  };

  const handleComparisonChangeB = (selection: DateDropdownOption) => {
    setPercentageWeeks({ ...percentageWeeks, comparisonB: selection });
  };

  return (
    <div className='min-w-full'>
      <div className='flex mb-10 text-gray-800 items-center'>
        Percentage Change
        <DateDropdown
          options={weekOptions}
          selected={percentageWeeks.comparisonA}
          setSelected={handleComparisonChangeA}
        />
        to
        <DateDropdown
          options={weekOptions}
          selected={percentageWeeks.comparisonB}
          setSelected={handleComparisonChangeB}
        />
      </div>
      <table className='min-w-full max-w-full leading-normal overflow-x-scroll table-auto'>
        <thead>
          <tr>
            {keys.map((title, index) => (
              <th
                key={index}
                className='px-4 py-3 first:text-left text-right text-gray-700 text-xs font-semibold uppercase border-t border-gray-200'
              >
                {title}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {percentageData && percentageData.length > 1 ? (
            percentageData.map((divisionData, sIndex) => {
              const userChange = Number(divisionData?.userChange);
              const loginChange = Number(divisionData?.loginChange);
              const branchChange = Number(divisionData?.branchChange);
              return (
                <tr
                  className='last:bg-gray-100 last:border-b text-sm text-right border-t border-gray-200'
                  key={sIndex}
                >
                  <td className='px-4 py-2 border-t border-gray-200'>
                    <div className='flex items-center text-left justify-between h-8'>
                      <p className='text-gray-700 whitespace-no-wrap'>
                        {divisionData?.division}
                      </p>
                    </div>
                  </td>
                  <td
                    className={clsx(
                      userChange > 0
                        ? "text-green-500"
                        : userChange < 0
                        ? "text-red-500"
                        : "text-gray-900",
                      "px-4 py-2"
                    )}
                  >
                    {normalizeValue(divisionData?.userChange)}%
                  </td>
                  <td
                    className={clsx(
                      loginChange > 0
                        ? "text-green-500"
                        : loginChange < 0
                        ? "text-red-500"
                        : "text-gray-900",
                      "px-4 py-2"
                    )}
                  >
                    {normalizeValue(divisionData?.loginChange)}%
                  </td>
                  <td
                    className={clsx(
                      branchChange > 0
                        ? "text-green-500"
                        : branchChange < 0
                        ? "text-red-500"
                        : "text-gray-700",
                      "px-4 py-2"
                    )}
                  >
                    {normalizeValue(divisionData?.branchChange)}%
                  </td>
                </tr>
              );
            })
          ) : (
            <tr>
              <td
                colSpan={4}
                className='text-sm text-center text-gray-700 border-t border-gray-200 pt-4'
              >
                No data available for selected dates
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default PercentageChange;
