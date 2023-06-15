import { Fragment } from "react";
import { Listbox, Transition } from "@headlessui/react";
import { CheckIcon, SelectorIcon } from "@heroicons/react/solid";
import { DropdownProps } from "./types";
import clsx from "clsx";
import { MdArrowDropDown } from "react-icons/md";

const Dropdown = ({
  label,
  options,
  selected,
  disabled,
  setSelected,
}: DropdownProps) => {
  return (
    <Listbox
      as='div'
      className='flex flex-col h-max relative'
      value={selected}
      onChange={setSelected}
      disabled={disabled}
    >
      {label && (
        <Listbox.Label className='mb-2 text-neutral-700 font-bold'>
          {label}
        </Listbox.Label>
      )}
      <Listbox.Button
        className={clsx(
          "relative",
          "truncate",
          "bg-white",
          "border",
          "border-[#D6D7D9]",
          "text-left",
          "pl-4",
          "pr-10",
          "py-3",
          "rounded",
          { "text-gray-400": disabled }
        )}
      >
        {selected.display}
        <MdArrowDropDown
          className='absolute right-2 top-0 items-center h-full text-2xl text-gray-400'
          aria-hidden='true'
        />
      </Listbox.Button>
      <Transition
        as={Fragment}
        enterFrom='opacity-0'
        enterTo='opacity-100'
        enter='transition ease-in duration-100'
        leaveFrom='opacity-100'
        leaveTo='opacity-0'
        leave='transition ease-in duration-100'
      >
        <Listbox.Options className='absolute w-full mt-1 bg-white rounded top-full z-50 overflow-auto shadow-lg max-h-60 ring-1 ring-black ring-opacity-5'>
          {options.map((option, optionIdx) => (
            <Listbox.Option
              key={optionIdx}
              className={({ active, selected }) =>
                clsx(
                  "flex",
                  "items-center",
                  "relative",
                  "py-2",
                  "pr-4",
                  "truncate",
                  selected ? "pl-3" : "pl-3",
                  active ? "text-white bg-reece-500" : "text-gray-900"
                )
              }
              value={option}
            >
              {({ selected }) => (
                <>
                  {selected && (
                    <CheckIcon
                      className='w-5 h-5 mr-2 text-reece-500'
                      aria-hidden='true'
                    />
                  )}
                  {option.display}
                </>
              )}
            </Listbox.Option>
          ))}
        </Listbox.Options>
      </Transition>
    </Listbox>
  );
};

export default Dropdown;
