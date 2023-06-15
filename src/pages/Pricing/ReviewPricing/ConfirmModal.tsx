import { Modal } from "../../../components/Modal";
import { usePricing } from "../PricingProvider";
import { ConfirmModalProps } from "./types";

export const ConfirmModal = (props: ConfirmModalProps) => {
  const { openModal, toggleModal } = usePricing();

  return (
    <Modal
      {...props}
      open={openModal === "confirm"}
      onClose={() => toggleModal(false)}
    >
      <div className='my-5 px-2 text-m text-gray-500 max-w-[500px]'>
        Once you submit these changes it will take approximately one day for
        Eclipse to be updated. Until that time the pricing will appear unchanged
        in this tool. Are you sure you wish to continue?
      </div>
    </Modal>
  );
};
