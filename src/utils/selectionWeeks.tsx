import { previousFriday, previousMonday, format } from "date-fns";

export const getWeeks = () => {
  const dateLimit = new Date(2022, 3, 29);
  let anchor = new Date();
  let monday: Date, friday: Date;
  const weeks = [];
  do {
    friday = previousFriday(anchor);
    monday = previousMonday(friday);
    weeks.push({
      start: format(monday, "yyyy-MM-dd"),
      end: format(friday, "yyyy-MM-dd"),
      display: `${format(monday, "MM/dd")} - ${format(friday, "MM/dd")}`,
    });
    anchor = monday;
  } while (anchor > dateLimit);
  return weeks;
};
