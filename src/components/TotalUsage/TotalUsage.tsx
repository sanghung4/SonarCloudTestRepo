import React, { useState, useEffect } from "react";
import { DateDropdown } from "../DateDropdown";
import { DivisionTable } from "../DivisionTable";
import { SvgList } from "../SvgList";
import { TotalUsageData, TotalUsageProps } from "./types";

const TotalUsage = ({
  usageData,
  weekOptions,
  usageWeek,
  setUsageWeek,
}: TotalUsageProps) => {
  const [totalUsage, setTotalUsage] = useState<TotalUsageData[]>([]);

  useEffect(() => {
    if (usageData && usageData.length > 1) {
      const formattedUsage: TotalUsageData[] = [];

      usageData.forEach((item) => {
        item && formattedUsage.push({ ...item, expand: false });
      });

      setTotalUsage(formattedUsage);
    } else {
      setTotalUsage([]);
    }
  }, [usageData]);

  const handleShowMore = (index: number) => {
    setTotalUsage((prevState) => {
      prevState[index] = {
        ...prevState[index],
        expand: prevState[index].expand ? false : true,
      };
      return [...prevState];
    });
  };

  return (
    <div className='min-w-full'>
      <div className='flex mb-10 text-gray-800 items-center'>
        Total BIA Usage
        <DateDropdown
          options={weekOptions}
          selected={usageWeek}
          setSelected={setUsageWeek}
        />
      </div>
      <table className='min-w-full max-w-full leading-normal overflow-x-scroll table-auto'>
        <thead>
          <tr>
            <th className='px-4 py-3 text-gray-700 text-xs 2xl:text-standar first:text-left text-right font-semibold uppercase border-t border-gray-200'>
              <div className='flex'>
                <div className='h-1 w-5 mr-2'></div>
                Division
              </div>
            </th>
            <th className='px-4 py-3 text-gray-700 text-xs 2xl:text-standar first:text-left text-right font-semibold uppercase border-t border-gray-200'>
              Total Users
            </th>
            <th className='px-4 py-3 text-gray-700 text-xs 2xl:text-standar first:text-left text-right font-semibold uppercase border-t border-gray-200'>
              Total Logins
            </th>
            <th className='px-4 py-3 text-gray-700 text-xs 2xl:text-standar first:text-left text-right font-semibold uppercase border-t border-gray-200'>
              No. of Branches
            </th>
          </tr>
        </thead>
        <tbody>
          {totalUsage.length ? (
            totalUsage.map((divisionData, sIndex) => (
              <React.Fragment key={sIndex}>
                <tr
                  className='last:bg-gray-100 last:border-b text-sm text-right border-t border-gray-200'
                  key={sIndex}
                >
                  <td className='px-4 py-2 border-t border-gray-200'>
                    <div className='flex items-center'>
                      {divisionData.branches &&
                      divisionData?.branches?.length > 0 ? (
                        <button
                          className='text-gray-600 whitespace-no-wrap'
                          onClick={() => handleShowMore(sIndex)}
                        >
                          {divisionData.expand ? (
                            <SvgList
                              name='ChevronUp'
                              className='h-8 w-5 mr-2'
                            />
                          ) : (
                            <SvgList
                              name='ChevronDown'
                              className='h-8 w-5 mr-2'
                            />
                          )}
                        </button>
                      ) : (
                        <div className='h-8 w-5 mr-2'></div>
                      )}
                      <p className='text-gray-900 text-left whitespace-no-wrap'>
                        {divisionData.division}
                      </p>
                    </div>
                  </td>
                  <td className='px-4 py-2'>{divisionData.userCount}</td>
                  <td className='px-4 py-2'>{divisionData.loginCount}</td>
                  <td className='px-4 py-2'>{divisionData.branchCount}</td>
                </tr>
                {divisionData.expand && (
                  <tr className='text-gray-900 text-sm min-w-full'>
                    <td className='p-4' colSpan={4}>
                      <DivisionTable data={divisionData.branches} />
                    </td>
                  </tr>
                )}
              </React.Fragment>
            ))
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

export default TotalUsage;
