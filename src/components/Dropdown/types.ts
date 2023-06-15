export interface DropdownOption {
  value: string;
  display: string;
}

export interface DropdownOptionComparison {
  comparisonA: DropdownOption;
  comparisonB: DropdownOption;
}

export interface DropdownProps {
  label?: string;
  disabled?: boolean;
  textSize?: "small" | "large";
  options: DropdownOption[];
  selected: DropdownOption;
  setSelected: (selection: DropdownOption) => void;
}
