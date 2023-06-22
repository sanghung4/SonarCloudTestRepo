import { ComponentStory, ComponentMeta } from '@storybook/react';
import RadioInput from '.';

export default {
  title: 'Inputs/Radio Input',
  component: RadioInput
} as ComponentMeta<typeof RadioInput>;

const Template: ComponentStory<typeof RadioInput> = (args) => (
  <RadioInput {...args} />
);

export const Primary = Template.bind({});
Primary.args = {
  label: 'Radio Input',
  options: [
    { label: 'Option 1', value: '1' },
    { label: 'Option 2', value: '2' },
    { label: 'Option 3', value: '3' }
  ]
};
