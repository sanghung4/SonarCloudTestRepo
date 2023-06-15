import { RadioProps } from "./types";

const Radio = ({ label, name, checked, value, disabled, onChange }: RadioProps) => {
  return (
    <label className='inline-flex items-center text-neutral-600'>
      <input
        className='w-5 h-5 mr-2'
        type='radio'
        name={name}
        value={value}
        checked={checked}
        disabled={disabled}
        onChange={onChange}
      />
      <p>{label}</p>
    </label>
  );
};

export default Radio;
