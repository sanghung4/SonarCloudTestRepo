import { CountWithStatus } from "../../graphql";

export const applyFilter = (search: string) => (count: CountWithStatus) => {
  if (!search || search === "") return count;
  else if (
    count.branchName?.toLocaleLowerCase().includes(search?.toLocaleLowerCase())
  )
    return count;
  else if (
    count.branchId?.toLocaleLowerCase().includes(search?.toLocaleLowerCase())
  )
    return count;
};

export const getEndDate = (date: Date): Date => {
  const endDate = new Date(date);
  endDate.setHours(endDate.getHours() + 24);
  return endDate;
};

// Tests string to see if it starts with `404` and contains ["message":"Invalid count id]
export const inProgressRegex = new RegExp(
  `(^404)(?:.*"message":"Invalid count id)`
);

// Tests string to see if it starts with `500` and contains ["path":"/inventory/branches/counts/(xxx)"]
export const inProgressPendingRegex = new RegExp(
  `(^500)(?:.*"path":"\\/inventory\\/branches\\/\\d+\\/counts\\/\\d+")`
);
