export interface DateDropdownOption {
  display: string;
  end: string;
  start: string;
}

export interface DateDropdownOptionComparison {
  comparisonA: DateDropdownOption;
  comparisonB: DateDropdownOption;
}

export interface DropdownProps {
  options: DateDropdownOption[];
  selected: DateDropdownOption;
  setSelected: (selection: DateDropdownOption) => void;
}
