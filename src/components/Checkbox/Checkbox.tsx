import clsx from "clsx";
import { MdCheckBoxOutlineBlank } from "react-icons/md";
import { CheckboxProps } from "./types";

const Checkbox = ({ classNames, label, checked, onChange }: CheckboxProps) => {
  // - HOOKS - //

  // - STATE - //

  // - EFFECTS - //

  // - ACTIONS - //

  // - HELPERS - //

  // - JSX - //
  return (
    <label
      className={clsx("flex", "items-center", "cursor-pointer", classNames)}
    >
      <input
        className='mr-2 h-4 w-4 p-0.5 rounded-sm bg-neutral-200 border-2 border-neutral-400 checked:bg-blue-500 checked:border-blue-500'
        type='checkbox'
        checked={checked}
        onChange={onChange}
      />
      {label}
    </label>
  );
};

export default Checkbox;
