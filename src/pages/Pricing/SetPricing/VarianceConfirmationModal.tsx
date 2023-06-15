import { Modal } from "../../../components/Modal";
import { usePricing } from "../PricingProvider";
import { VarianceConfirmationModalProps } from "./types";

export const VarianceConfirmationModal = (props: VarianceConfirmationModalProps) => {
  const { openModal, toggleModal } = usePricing();

  return (
    <Modal
      {...props}
      open={openModal === "varianceConfirmation"}
      onClose={() => toggleModal(false)}
      title="Are you sure you want to add?"
    >
      <div className='my-5 px-2 text-m text-gray-500 max-w-[500px]'>
        Your modified special price is 25% {props.isGreaterThan25Percent ? "greater" : "lower"} than or equal to the current special price.
      </div>
    </Modal>
  );
};
