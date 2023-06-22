import { ComponentStory, ComponentMeta } from '@storybook/react';
import TextInput from '.';

export default {
  title: 'Inputs/Text Input',
  component: TextInput
} as ComponentMeta<typeof TextInput>;

const Template: ComponentStory<typeof TextInput> = (args) => (
  <TextInput {...args} />
);

export const Primary = Template.bind({});
Primary.args = { label: 'Text Input' };

export const Password = Template.bind({});
Password.args = { label: 'Text Input', type: 'password' };
