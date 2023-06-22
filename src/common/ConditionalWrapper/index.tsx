type Props = {
  condition: boolean;
  wrapper: (children: JSX.Element) => JSX.Element;
  children: JSX.Element;
};

function ConditionalWrapper(props: Props) {
  return props.condition ? props.wrapper(props.children) : props.children;
}

export default ConditionalWrapper;
