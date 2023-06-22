import { ComponentStory, ComponentMeta } from '@storybook/react';
import Stepper from '.';

export default {
  title: 'Progress/Stepper',
  component: Stepper
} as ComponentMeta<typeof Stepper>;

const Template: ComponentStory<typeof Stepper> = (args) => (
  <Stepper {...args} />
);

export const Primary = Template.bind({});
Primary.args = {
  steps: ['Step 1', 'Step 2', 'Step 3'],
  activeStep: 1
};
