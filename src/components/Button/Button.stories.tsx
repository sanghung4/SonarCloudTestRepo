/* istanbul ignore file */

import { ComponentStory, ComponentMeta } from '@storybook/react';
import Button from '.';
import { EditIcon } from '../../icons';

export default {
  title: 'Inputs/Button',
  component: Button
} as ComponentMeta<typeof Button>;

const Template: ComponentStory<typeof Button> = (args) => <Button {...args} />;

export const Primary = Template.bind({});
Primary.args = {
  label: 'Button'
};

export const PrimaryIcon = Template.bind({});
PrimaryIcon.args = {
  label: 'Button',
  icon: <EditIcon />
};
