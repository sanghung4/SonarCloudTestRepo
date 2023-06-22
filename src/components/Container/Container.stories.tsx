/* istanbul ignore file */

import { ComponentStory, ComponentMeta } from '@storybook/react';
import Container from '.';

export default {
  title: 'Layout/Container',
  component: Container
} as ComponentMeta<typeof Container>;

const Template: ComponentStory<typeof Container> = (args) => (
  <Container {...args} />
);

export const Primary = Template.bind({});
Primary.args = {
  children: (
    <div
      style={{
        background: 'lightgray',
        height: '500px',
        border: '1px solid black'
      }}
    />
  )
};
