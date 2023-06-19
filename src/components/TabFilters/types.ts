interface Tab {
  title: string;
  value: string;
  color?: string;
  disabled?: boolean;
  testID?: string;
}

export interface TabFiltersProps {
  filters: Tab[];
  onPress: (value: string) => void;
  active: string;
  testID?: string;
}
