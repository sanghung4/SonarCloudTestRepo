import { ComponentStory, ComponentMeta } from '@storybook/react';
import SelectInput from '.';

export default {
  title: 'Inputs/Select Input',
  component: SelectInput
} as ComponentMeta<typeof SelectInput>;

const Template: ComponentStory<typeof SelectInput> = (args) => (
  <SelectInput {...args} />
);

export const Primary = Template.bind({});
Primary.args = {
  label: 'Select Input',
  options: [
    { label: 'Option 1', value: '1' },
    { label: 'Option 2', value: '2' },
    { label: 'Option 3', value: '3' },
    { label: 'Option 4', value: '4' },
    { label: 'Option 5', value: '5' }
  ]
};
