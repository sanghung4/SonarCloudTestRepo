import clsx from "clsx";
import { MdRefresh } from "react-icons/md";
import { ButtonProps } from "./types";

const Button = ({
  size,
  title,
  className,
  loading,
  icon,
  disabled = false,
  iconPosition = "right",
  onClick,
}: ButtonProps) => {
  const getTextSize = () => {
    switch (size) {
      case "lg":
        return "text-lg";
      case "sm":
        return "text-sm";
      default:
        return "text-base";
    }
  };

  const getIconSize = () => {
    switch (size) {
      case "lg":
        return "h-7 w-7";
      case "sm":
        return "h-5 w-5";
      default:
        return "h-6 w-6";
    }
  };

  return (
    <button
      className={clsx("py-3", "px-5", "rounded", "font-medium", getTextSize(), className, {
        "bg-gray-300": disabled,
        "noHover":disabled
      })}
      onClick={onClick}
      disabled={disabled || loading}
    >
      {loading ? (
        <MdRefresh className={clsx("animate-spin", getIconSize())} />
      ) : (
        <div className='flex justify-center items-center'>
          <span className='mr-2'>{iconPosition === "left" && icon}</span>
          {title}
          <span className='ml-2'>{iconPosition === "right" && icon}</span>
        </div>
      )}
    </button>
  );
};

export default Button;
