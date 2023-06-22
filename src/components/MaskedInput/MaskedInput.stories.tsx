import { ComponentStory, ComponentMeta } from '@storybook/react';
import MaskedInput from '.';

export default {
  title: 'Inputs/Masked Input',
  component: MaskedInput
} as ComponentMeta<typeof MaskedInput>;

const Template: ComponentStory<typeof MaskedInput> = (args) => (
  <MaskedInput {...args} />
);

export const Phone = Template.bind({});
Phone.args = {
  mask: 'phone',
  label: 'Label'
};

export const Currency = Template.bind({});
Currency.args = {
  mask: 'currency',
  label: 'Label'
};

export const Number = Template.bind({});
Number.args = {
  mask: 'number',
  label: 'Label'
};
