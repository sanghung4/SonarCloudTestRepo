/* istanbul ignore file */

import { ComponentStory, ComponentMeta } from '@storybook/react';
import Card from '.';

export default {
  title: 'Layout/Card',
  component: Card
} as ComponentMeta<typeof Card>;

const Template: ComponentStory<typeof Card> = (args) => <Card {...args} />;

export const Primary = Template.bind({});
Primary.args = {
  children: <div style={{ height: '100px', padding: '24px' }}>Card</div>
};
