export interface NavigationTypes {
  name: string;
  href: string;
  group: string;
  icon: (props: React.ComponentProps<"svg">) => JSX.Element;
}
