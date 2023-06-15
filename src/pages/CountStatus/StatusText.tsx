import { useEffect, useState } from "react";
import clsx from "clsx";
import { StatusTextProps } from "./types";
import { CountStatus } from "../../graphql";
import { inProgressPendingRegex, inProgressRegex } from "./utils";

export const StatusText = ({ count }: StatusTextProps) => {
  const [maskError, setMaskError] = useState(false);
  const [maskedStatus, setMaskedStatus] = useState<string>();

  useEffect(() => {
    // Only mask errors for Mincron counts (Eclipse counts are 4 digits long)
    const isMincron = count.branchId.length !== 4;

    if (count.status === CountStatus.Error && isMincron && count.errorMessage) {
      // Check if the count is "IN PROGRESS"
      const isInProgress = inProgressRegex.test(count.errorMessage);
      // Check if the count is "IN PROGRESS - PENDING"
      const isPending = inProgressPendingRegex.test(count.errorMessage);
      // Mask the input if it is in progress or pending
      setMaskError(isInProgress || isPending);
      // Set the masked status if appropriate
      setMaskedStatus(
        isInProgress
          ? "IN PROGRESS"
          : isPending
          ? "IN PROGRESS - PENDING"
          : count.status
      );
    }
  }, [count]);

  const parseError = (error:string)=>{
    if (error.includes('KGW-500')) {
      return "This count has no products"
    }else{
      return error;
    }
  }

  return maskError ? (
    <p className='text-gray-900 whitespace-no-wrap'>{maskedStatus}</p>
  ) : (
    <div>
      <p
        className={clsx(
          count.status === CountStatus.Error
            ? "text-red-600 font-semibold"
            : "text-gray-900",
          "whitespace-no-wrap"
        )}
      >
        {count.status == "COMPLETE" ? "LOADED" : count.status}
      </p>

      {count.errorMessage && count.status === CountStatus.Error && (
        <p className='text-gray-900 font-semibold min-w-full break-all'>
          {parseError(count.errorMessage)}
        </p>
      )}
    </div>
  );
};
