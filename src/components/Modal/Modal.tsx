import { Fragment } from "react";
import { Dialog, Transition } from "@headlessui/react";
import Button from "../Button/Button";
import { ModalProps } from "./types";

const Modal = ({
  open,
  children,
  closeText,
  confirmText,
  title,
  confirmDisabled,
  onConfirm,
  onClose,
}: ModalProps) => {
  return (
    <Transition.Root show={open} as={Fragment}>
      <Dialog
        as='div'
        className='fixed z-10 inset-0 overflow-y-auto'
        onClose={onClose}
      >
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
            <Dialog.Panel className='relative align-center bg-white rounded-lg px-4 pt-5 pb-4 text-left overflow-hidden shadow-xl transform transition-all'>
              <Dialog.Title
                as='h3'
                className='text-lg font-medium leading-8 text-gray-900 ml-2 mt-1'
              >
                {title}
              </Dialog.Title>
              <Dialog.Description as='div' className='pb-1'>
                {children}
              </Dialog.Description>
              <div className='mt-5 md:mt-4 md:flex md:flex-row-reverse'>
                <Button
                  className='w-full inline-flex justify-center rounded-md border border-transparent shadow-sm mb-2 px-4 py-2 bg-reece-700 hover:bg-reece-600  text-base font-medium text-white focus:outline-none md:mb-0 md:ml-4 md:w-auto md:text-sm'
                  onClick={onConfirm}
                  disabled={confirmDisabled}
                  title={confirmText}
                />
                <Button
                  className='w-full inline-flex justify-center rounded-md border border-reece-700 shadow-sm px-4 py-2 bg-white text-base font-medium text-reece-700 hover:border-reece-500 hover:text-reece-500 focus:outline-none md:w-auto md:text-sm'
                  onClick={onClose}
                  title={closeText}
                />
              </div>
            </Dialog.Panel>
          </Transition.Child>
        </div>
      </Dialog>
    </Transition.Root>
  );
};

Modal.defaultProps = {
  closeText: "Cancel",
  confirmText: "Confirm",
  title: "Confirmation Needed",
};

export default Modal;
