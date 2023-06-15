import { DateRange, Range } from "react-date-range";
import { useState } from "react";
import { Banner } from "../../components/Banner";
import { Modal } from "../../components/Modal";
import { format } from "date-fns";
import { toast, Slide } from "react-toastify";
import { Button } from "../../components/Button";
import { usePurgeMincronCountsMutation } from "../../graphql";

const MincronCount = () => {
  const [isDateRangeSelected, setIsDateRangeSelected] = useState<boolean>(true);
  const [confirm, setConfirm] = useState<boolean>(false);
  const [range, setRange] = useState<Range>({
    startDate: new Date(),
    endDate: new Date(),
    color: "#007ce6",
    key: "selection",
  });

  const [purgeMincronCountsMutation] = usePurgeMincronCountsMutation();

  const testToast = () => {

    if (range.startDate && range.endDate) {
      const variables = { 
        input: { 
          startDate: format(new Date(range.startDate), "yyyy-MM-dd HH:mm:ss"), 
          endDate: format(new Date(range.endDate), "yyyy-MM-dd 23:59:59")
       } 
      }
       purgeMincronCountsMutation({variables})
       .then((response)=>{
          toast.success(`${response.data?.purgeMincronCounts}`, { transition: Slide });
          setConfirm(false);
          setIsDateRangeSelected(true);
        })
      .catch(error=>{
        toast.error("Mincron Count Purge Failure", { transition: Slide });
        setConfirm(false);
        setIsDateRangeSelected(true);
        throw(error);
      })
    } 
  };

  const testClose = () => {
    toast.error("Count ID Reset Failure", { transition: Slide });
    setConfirm(false);
  };

  // get yesterdays date so we can exclude today as an option
  const yesterday = new Date();
  yesterday.setDate(new Date().getDate() - 1);

  return (
    <>
      <Banner header='Mincron Count ID Reset' />
      <Modal
        open={confirm}
        onConfirm={() => testToast()}
        onClose={() => {
          testClose();
        }}
      >
        <div className='my-5 px-2 text-m text-gray-500'>
          Review and confirm selected date range for purge: <br />
          <span className='text-red-600 font-bold mr-1'>
            {(range.startDate &&
              format(new Date(range.startDate), "MMMM do, yyyy")) ||
              ""}
          </span>
          through
          <span className='text-red-600 font-bold ml-1'>
            {(range.endDate &&
              format(new Date(range.endDate), "MMMM do, yyyy")) ||
              ""}
          </span>
        </div>
      </Modal>
      <div className='mt-12 grid place-items-center'>
        <div className='bg-white rounded-lg drop-shadow-md p-10'>
          <DateRange
            maxDate={yesterday}
            startDatePlaceholder='Start Date'
            endDatePlaceholder='End Date'
            ranges={[range]}
            onChange={(ranges) => {
              setRange(ranges.selection);
              setIsDateRangeSelected(false);
            }}
          />
          <Button
            disabled={isDateRangeSelected}
            className='bg-red-600 text-red-50 w-full block'
            onClick={() => setConfirm(true)}
            title='Reset Count IDs'
          />
        </div>
      </div>
    </>
  );
};

export default MincronCount;
