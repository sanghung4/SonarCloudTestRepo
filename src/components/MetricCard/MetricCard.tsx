import clsx from "clsx";
import { useState, useEffect } from "react";
import { InformationCircleIcon } from "@heroicons/react/solid";
import { SvgList } from "../SvgList";
import { MetricCardProps } from "./types";

const MetricCard = ({ title, amount, percentage }: MetricCardProps) => {
  const [isPositive, toggleIsPositive] = useState(true);
  const [value, setValue] = useState("");

  useEffect(() => {
    toggleIsPositive(parseInt(percentage) >= 0 || percentage === "∞");
    setValue(parseInt(percentage) >= 0 ? percentage : percentage.substring(1));
  }, [percentage]);

  return (
    <div className='w-1/2 md:w-1/3 xl:w-1/4 3xl:w-1/6'>
      <div className='p-5 bg-white rounded shadow-md mx-4 mb-4'>
        <div className='flex justify-between text-sm 2xl:text-base text-gray-800 '>
          Total {title.charAt(0).toUpperCase() + title.slice(1)}
          <div className='group'>
            <InformationCircleIcon className='w-5 h-5 text-gray-300' />
            <span className='tooltip-text opacity-80 bg-gray-900 text-white p-3 -mt-20 -ml-56 rounded hidden group-hover:flex absolute text-center z-50'>
              <InformationCircleIcon className='w-5 h-5 text-white mr-4' />
              {title
                ? `All ${title} for the last full work week (M-F)`
                : "loading..."}
            </span>
          </div>
        </div>
        <div className='flex items-center pt-1'>
          <div className='text-xl 2xl:text-2xl font-bold text-gray-900'>
            {amount}
          </div>
          <div>
            <span
              className={clsx(
                isPositive
                  ? "text-green-800 bg-green-100"
                  : "text-red-800 bg-red-100",
                "flex items-center px-2 py-0.5 mx-2 text-xs 2xl:text-sm rounded-full"
              )}
            >
              {isPositive ? (
                <SvgList name='PositiveChevron' />
              ) : (
                <SvgList name='NegativeChevron' />
              )}
              <span>{value}%</span>
            </span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MetricCard;
