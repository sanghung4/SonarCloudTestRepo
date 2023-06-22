import { Box } from '@dialexa/reece-component-library';

type Props = {
  show: boolean;
  children: React.ReactNode;
  testId: string;
};

function FormStepContainer(props: Props) {
  /**
   * Props
   */
  const { show, children, testId } = props;

  /**
   * Render
   */
  if (!show) {
    return null;
  }

  return (
    <Box
      display="flex"
      flexDirection="column"
      width="100%"
      maxWidth={(theme) => theme.spacing(86)}
      data-testid={testId}
    >
      {children}
    </Box>
  );
}

export default FormStepContainer;
