/* istanbul ignore file */

import { ComponentStory, ComponentMeta } from '@storybook/react';
import CheckboxInput from '.';

export default {
  title: 'Inputs/Checkbox Input',
  component: CheckboxInput
} as ComponentMeta<typeof CheckboxInput>;

const Template: ComponentStory<typeof CheckboxInput> = (args) => (
  <CheckboxInput {...args} />
);

export const Primary = Template.bind({});
Primary.args = {
  label: 'Checkbox'
};
