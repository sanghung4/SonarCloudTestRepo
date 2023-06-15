import { Fragment } from "react";
import { Dialog, Transition } from "@headlessui/react";
import { usePricing } from "../PricingProvider";
import { MdClose } from "react-icons/md";
import { SvgList } from "../../../components/SvgList";

const HelpModal = () => {
  const { openModal, toggleModal } = usePricing();

  // Handles closing the modal
  const handleCloseModal = () => {
    toggleModal(false);
  };

  return (
    <Transition.Root show={openModal === "help"} as={Fragment}>
      <Dialog as='div' className='fixed z-10 inset-0 overflow-y-auto' onClose={handleCloseModal}>
        <div className='flex items-center justify-center min-h-screen'>
          {/* Backdrop */}
          <Transition.Child
            enter='ease-out duration-300'
            enterFrom='opacity-0'
            enterTo='opacity-100'
            leave='ease-in duration-200'
            leaveFrom='opacity-100'
            leaveTo='opacity-0'
            className='fixed inset-0 bg-gray-700 bg-opacity-75 transition-opacity'
          />
          {/* Content */}
          <Transition.Child
            as={Fragment}
            enter='ease-out duration-300'
            enterFrom='opacity-0 translate-y-4 md:translate-y-0 md:scale-95'
            enterTo='opacity-100 translate-y-0 md:scale-100'
            leave='ease-in duration-200'
            leaveFrom='opacity-100 translate-y-0 md:scale-100'
            leaveTo='opacity-0 translate-y-4 md:translate-y-0 md:scale-95'
          >
            <Dialog.Panel className='rounded bg-white py-10 px-14 relative w-[500px]'>
              {/* Close Button */}
              <button
                className='absolute right-14 top-10 text-2xl text-primary-1-100'
                onClick={handleCloseModal}
              >
                <MdClose />
              </button>
              {/* Title */}
              <Dialog.Title className='text-3xl font-semibold text-primary-1-100'>
                Contact Us
              </Dialog.Title>
              <Dialog.Description as='div' className='pb-1'>
                <div className='w-full flex justify-center'>
                  <SvgList name='userTesting' className='w-32 m-6' />
                </div>
                <h2 className='text-lg mt-4 font-bold text-primary-1-100'>Help Ticket</h2>
                <hr className='my-3 h-0 border border-solid border-secondary-3-100 ' />
                <p className='text-secondary-2-100'>
                  Create a help ticket to report and request assistance with an issue you are
                  having. This should not be used for CMP Pricing Requests.
                </p>
              </Dialog.Description>
              <div className='mt-5 md:mt-4 md:flex md:flex-row'>
                <a
                  className='w-full inline-flex justify-center rounded-md border border-transparent shadow-sm mb-2 px-4 py-2 bg-reece-800 text-base font-medium text-white hover:bg-gren-500 focus:outline-none md:mb-0 md:w-auto md:text-sm'
                  href='https://morsco.service-now.com/sp?id=sc_cat_item&sys_id=3f1dd0320a0a0b99000a53f7604a2ef9'
                  target='_blank'
                  rel='noreferrer'
                >
                  {" General Help "}
                </a>
              </div>
            </Dialog.Panel>
          </Transition.Child>
        </div>
      </Dialog>
    </Transition.Root>
  );
};

export default HelpModal;
