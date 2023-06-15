import { BannerProps } from "./types";
import { MdHelp } from "react-icons/md";
import clsx from "clsx";
import { usePricing } from "../../pages/Pricing/PricingProvider";

const Banner = ({ header }: BannerProps) => {

  const { toggleModal } = usePricing();

  return (
    <div className={clsx(
      "flex",
      "w-full",
      "h-16",
      "lg:h-24",
      "justify-center",
      {"justify-between": header === "Special Pricing"},
      "items-center",
      "bg-white",
      "border-b-0",
      "border-gray-400",
      "shadow-md",
    )}>
      {header === "Special Pricing" && <div/>}
      <h1 className='text-reece-800 text-2xl lg:text-4xl font-bold'>
        {header}
      </h1>
      { header === "Special Pricing" &&
        <button 
        className="flex items-center mr-8" 
        onClick={() => {toggleModal(true, "help")}}>
          <MdHelp className="text-3xl text-primary-1-100"/>
          <h2 className="pl-1 text-base text-primary-3-80">Help</h2>
        </button>
      }
    </div>
  );
};

export default Banner;
