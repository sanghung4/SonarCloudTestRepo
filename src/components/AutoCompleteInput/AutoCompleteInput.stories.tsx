/* istanbul ignore file */

import { ComponentStory, ComponentMeta } from '@storybook/react';
import AutoCompleteInput from '.';

export default {
  title: 'Inputs/AutoComplete Input',
  component: AutoCompleteInput
} as ComponentMeta<typeof AutoCompleteInput>;

const Template: ComponentStory<typeof AutoCompleteInput> = (args) => (
  <AutoCompleteInput {...args} />
);

export const Primary = Template.bind({});
Primary.args = {
  label: 'AutoComplete Input',
  options: [
    { label: 'Option 1', value: '1' },
    { label: 'Option 2', value: '2' },
    { label: 'Option 3', value: '3' },
    { label: 'Option 4', value: '4' },
    { label: 'Option 5', value: '5' }
  ]
};
