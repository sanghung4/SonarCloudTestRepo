import clsx from "clsx";
import { MdRefresh } from "react-icons/md";
import { InputProps } from "./types";

const Input = ({
  endIcon,
  className,
  label,
  status,
  helperText,
  disabled,
  readOnly,
  loading,
  ...rest
}: InputProps) => {
  // - HELPERS - //
  const helperTextColor = () => {
    switch (status) {
      case "success":
        return "text-green-500";
      case "errorInvalidCustId":
      case "errorValidBillToCustId":
      case "errorInvalidEmail":
      case "errorBranchMismatch":
      case "errorUnAuthUser":
      case "errorNonNumeric":
      case "errorInvalidBillToCustId":
        return "text-red-500";
      default:
        return "text-neutral-600";
    }
  };
  const checkError = () => {
    switch (status) {
      case "success":
        return false;
      case "errorInvalidCustId":
      case "errorValidBillToCustId":
      case "errorInvalidEmail":
      case "errorBranchMismatch":
      case "errorUnAuthUser":
      case "errorNonNumeric":
      case "errorInvalidBillToCustId":
        return true;
      default:
        return false;
    }
  };

  // - JSX - //
  return (
    <div className={clsx("flex flex-col h-max", className)}>
      {/* Label */}
      {label && <label className='mb-2 text-neutral-700 font-bold'>{label}</label>}
      {/* Input and Icon */}
      <div className='relative'>
        <input
          className={clsx(
            "w-full",
            "bg-white",
            "border",
            checkError() ? "border-[#FC1703] focus:outline-none" : "focus:outline-reece-500 border-[#D6D7D9]",
            "px-4",
            "py-3",
            "rounded",
            { "text-gray-400": disabled },
            { "text-primary-3-100 bg-primary-3-10": readOnly },
            { "pr-10": endIcon || loading }
          )}
          disabled={disabled}
          readOnly={readOnly}
          {...rest}
        />
        {(endIcon || loading) && (
          <div className='absolute right-2 top-0 flex items-center text-2xl h-full text-gray-400'>
            {loading ? <MdRefresh className='animate-spin' /> : endIcon}
          </div>
        )}
      </div>
      {/* Helper Text */}
      {helperText && (
        <p className={clsx("pl-2", "mt-1", "text-xs", helperTextColor())}>{helperText}</p>
      )}
    </div>
  );
};

export default Input;
