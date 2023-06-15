import clsx from "clsx";
import { PageButtonProps } from "./types";

export const PageButton = ({ active, pageNumber, dots, onClick }: PageButtonProps) => {
  // - ACTIONS - //
  const handleClick = () => {
    onClick && pageNumber && onClick(pageNumber);
  };

  // - JSX - //
  return (
    <button
      disabled={dots}
      aria-current='page'
      onClick={handleClick}
      className={clsx(
        "px-4",
        "py-2",
        "border",
        "text-sm",
        "font-medium",
        active
          ? "z-10 bg-indigo-50 border-indigo-500 text-indigo-600"
          : "bg-white border-gray-300 text-gray-500 hover:bg-gray-50"
      )}
    >
      {dots ? "..." : pageNumber}
    </button>
  );
};
