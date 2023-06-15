import { ChangeEventHandler, KeyboardEventHandler, Fragment, useMemo, useState } from "react";
import { Combobox, Transition } from "@headlessui/react";
import { AutocompleteInputProps } from "./types";
import clsx from "clsx";
import { MdRefresh } from "react-icons/md";

const AutocompleteInput = ({
  placeholder,
  label,
  options,
  filterOptions = true,
  disabled,
  selectedOption,
  renderOptionsEnd,
  endIcon,
  loading,
  inputValue,
  onSelectChange,
  onInputValueChange,
  onKeyDown,
}: AutocompleteInputProps) => {

  const [openOptions, setOpenOptions] = useState(true);
  const [onBlurCall, setOnBlurCall] = useState(false);
  const [mouseEventOnOptions, setMouseEventOnOptions] = useState(false);

  // - ACTIONS - //
  const handleInputChange: ChangeEventHandler<HTMLInputElement> = (e) => {
    onInputValueChange(e.target.value);
  };

  const handleEnterKey: KeyboardEventHandler<HTMLInputElement> = (e) => { 
    onKeyDown(e.key)
    if(e.key === 'Enter'){ 
      setOpenOptions(true)
    }
  }

  // - VALUES - //
  const displayedOptions = useMemo(() => {
    return inputValue && filterOptions
      ? options.filter((option) => option.display.toLowerCase().includes(inputValue.toLowerCase()))
      : options;
  }, [inputValue, options]);

  
  // - JSX - //
  return (
    <Combobox
      as='div'
      className='flex flex-col h-max relative w-[472px]'
      disabled={disabled}
      value={selectedOption}
      onChange={onSelectChange}
    >
      
      {({open}) => (
        <>
        {
          selectedOption === undefined  && onBlurCall === false
          ? setOpenOptions(true) 
          : setOpenOptions(open)
        }
          {/* Label */}
          {label && (
            <Combobox.Label className='mb-2 text-neutral-700 font-bold'>{label}</Combobox.Label>
          )}
          {/* Input */}
          <Combobox.Button className='relative'>
            <Combobox.Input
              className={clsx(
               "w-full",
                "h-[64px]",
                "bg-white",
                "border",
                "border-[#D6D7D9]",
                "focus:outline-reece-500",
                "px-4",
                "py-3",
                "rounded",
                { "pr-10": endIcon || loading },
                { "text-gray-400": disabled }
              )}
              onFocus={()=>setOnBlurCall(false)}
              name="autocompleteInput"
              onBlur={()=> {mouseEventOnOptions === true ? setOnBlurCall(false) : setOnBlurCall(true)}}
              placeholder={placeholder}
              onKeyDown={handleEnterKey}
              onChange={handleInputChange}
              displayValue={(option: AutocompleteInputProps["selectedOption"]) =>
                option?.display.replace(/\n/g, " ") ?? inputValue
              }
            />
           
            {(endIcon || loading) && (
              <div className='absolute right-2 top-0 flex items-center text-2xl h-full text-gray-400'>
                {loading ? <MdRefresh className='animate-spin' /> : endIcon}
              </div>
            )}
          </Combobox.Button>

          {/* Options */}
          <Transition
            as={Fragment}
            enterFrom='opacity-0'
            enterTo='opacity-100'
            enter='transition ease-in duration-100'
            leaveFrom='opacity-100'
            leaveTo='opacity-0'
            leave='transition ease-in duration-100'
            show={ displayedOptions.length > 0 && openOptions }
            
          >
            <Combobox.Options static className='absolute w-full mt-1 bg-white rounded top-full z-50 overflow-y-auto overflow-x-hidden shadow-lg max-h-60 ring-1 ring-black ring-opacity-5'
            onMouseEnter={()=>selectedOption === undefined   ? setMouseEventOnOptions(true) : null}
            onMouseLeave={()=>{selectedOption === undefined   ? setMouseEventOnOptions(false) : null}}
            >
              {displayedOptions.map((option, index) => (
                <Combobox.Option
                  value={option}
                  key={index}
                  className={({ active, selected }) =>
                    clsx(
                      "cursor-pointer",
                      "relative",
                      "py-2",
                      "px-4",
                      active ? "text-white bg-reece-500" : "text-gray-900",
                      { "bg-reece-200": selected }
                    )
                  }
                >
                  {option.display}
                </Combobox.Option>
              ))}
              {renderOptionsEnd}
            </Combobox.Options>
          </Transition>
        </>
      )}
    </Combobox>
  );
};

export default AutocompleteInput;
