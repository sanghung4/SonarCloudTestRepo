export interface AutocompleteOption {
  value: string;
  display: string;
}

export interface AutocompleteInputProps {
  placeholder?: string;
  label?: string;
  disabled?: boolean;
  endIcon?: React.ReactNode;
  filterOptions?: boolean;
  options: AutocompleteOption[];
  selectedOption?: AutocompleteOption;
  renderOptionsEnd?: React.ReactNode;
  loading?: boolean;
  inputValue: string;
  onSelectChange: (option: AutocompleteOption) => void;
  onInputValueChange: (value: string) => void;
  onKeyDown: (value: string) => void;
}
