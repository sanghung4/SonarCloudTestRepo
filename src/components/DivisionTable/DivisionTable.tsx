import { stateCodes } from "../../utils/stateCode";
import { DivisionTableProps } from "./types";

const DivisionTable = ({ data }: DivisionTableProps) => {
  const formatCity = (city: string) => {
    // Format string to return proper city name and possibly location type
    let cityArray = city.split(" - ");
    let locationType = "";
    if (cityArray.length > 1 && cityArray[1] !== "HVAC") {
      locationType =
        cityArray[1].slice(0, 1) + cityArray[1].slice(1).toLowerCase() + " ";
    }
    cityArray = cityArray[0].split(" ");
    let formattedName = "";
    cityArray.forEach((name: string) => {
      if (name[0] === "(") {
        formattedName += name.slice(0, 2) + name.slice(2).toLowerCase() + " ";
      } else {
        formattedName += name.slice(0, 1) + name.slice(1).toLowerCase() + " ";
      }
    });
    formattedName += locationType;
    return formattedName;
  };

  return (
    <div className='max-h-60 overflow-y-auto'>
      <table className='w-full'>
        <thead>
          <tr className='top-0 sticky bg-white'>
            <th className='font-semibold text-left text-xs text-gray-700 uppercase border-b border-gray-200 py-2 px-4 w-1/2'>
              Branch ID
            </th>
            <th className='font-semibold text-xs text-gray-700 uppercase border-b text-right border-gray-200 py-2 px-4 w-1/4'>
              Total Users
            </th>
            <th className='font-semibold text-xs text-gray-700 uppercase border-b text-right border-gray-200 py-2 px-4 w-1/4'>
              Total Logins
            </th>
          </tr>
        </thead>
        <tbody>
          {data?.map((branch, index) => (
            <tr className='hover:bg-gray-100 w-full' key={index}>
              <td className='px-4 py-2 border-b border-gray-200'>
                {formatCity(branch?.city || "")}
                {stateCodes[branch?.state || ""]} - {branch?.id}
              </td>
              <td className='px-4 py-2 border-b border-gray-200 text-right'>
                {branch?.userCount}
              </td>
              <td className='px-4 py-2 border-b border-gray-200 text-right'>
                {branch?.loginCount}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default DivisionTable;
