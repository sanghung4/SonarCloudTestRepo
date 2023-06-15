import { format } from "date-fns";

export const getFormattedDate = (dateString: string): string => {
    const date = new Date(dateString);
    return format(date, 'yyyy-LL-dd');
}

export const getFormattedTime = (dateString: string): string => {
    const date = new Date(dateString);
    return format(date, 'h:mm:ss a');
}

