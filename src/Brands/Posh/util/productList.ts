import f0 from 'images/posh/faucets/kitchenFaucetChrome.png';
import f1 from 'images/posh/faucets/showerChrome.png';
import f2 from 'images/posh/faucets/lavatoryFaucetChrome.png';
import f3 from 'images/posh/faucets/singleHandleFaucetChrome.png';
import f4 from 'images/posh/faucets/tubFaucetChrome.png';
import f5 from 'images/posh/faucets/kitchenFaucetNickel.png';
import f6 from 'images/posh/faucets/kitchenFaucetGold.png';
import f7 from 'images/posh/faucets/kitchenFaucetBlack.png';
import f8 from 'images/posh/faucets/showerNickel.png';
import f9 from 'images/posh/faucets/showerGold.png';
import f10 from 'images/posh/faucets/showerBlack.png';
import f11 from 'images/posh/faucets/lavatoryFaucetNickel.png';
import f12 from 'images/posh/faucets/lavatoryFaucetGold.png';
import f13 from 'images/posh/faucets/lavatoryFaucetBlack.png';
import f14 from 'images/posh/faucets/singleHandleFaucetNickel.png';
import f15 from 'images/posh/faucets/singleHandleFaucetGold.png';
import f16 from 'images/posh/faucets/singleHandleFaucetBlack.png';
import f17 from 'images/posh/faucets/tubFaucetNickel.png';
import f18 from 'images/posh/faucets/tubFaucetGold.png';
import f19 from 'images/posh/faucets/tubFaucetBlack.png';
import f20 from 'images/posh/faucets/showerTrimWithTubSpoutChrome.png';
import f21 from 'images/posh/faucets/showerTrimWithTubSpoutNickel.png';
import f22 from 'images/posh/faucets/showerTrimWithTubSpoutGold.png';
import f23 from 'images/posh/faucets/showerTrimWithTubSpoutBlack.png';

import { links } from 'utils/links';

type PoshFaucet = {
  title: string;
  features: string[];
  imageFinishes: string[];
  url: string;
};

const list = [
  {
    title: 'Posh Solus Pull-down Kitchen Faucet',
    features: [
      'Brass construction',
      'Ceramic cartridge',
      '16” stainless steel braided inlet hose with 3/8” compression fitting',
      'Neoperl aerator',
      'Available Finishes: Chrome, Brushed Nickel, Brushed Gold, Matte Black'
    ]
  },
  {
    title: 'Posh Solus Pressure Balance Shower',
    features: [
      'Brass construction',
      'Multifunction showerhead',
      'Flow Rate: Chrome 2.0gpm Max @ 80psi',
      'Flow Rate: Other Finishes 1.8gpm Max @ 80psi',
      'Available Finishes: Chrome, Brushed Nickel, Brushed Gold, Matte Black'
    ]
  },
  {
    title: 'Posh Solus Pressure Balance Tub and Shower',
    features: [
      'Brass construction',
      'Multifunction showerhead',
      'Flow Rate: Chrome 2.0gpm Max @ 80psi',
      'Flow Rate: Other Finishes 1.8gpm Max @ 80psi',
      'Available Finishes: Chrome, Brushed Nickel, Brushed Gold, Matte Black'
    ]
  },
  {
    title: 'Posh Solus Widespread Lavatory Faucet',
    features: [
      'Brass construction',
      '35mm ceramic cartridge',
      'Neopearl Aerator',
      'Flow Rate: 1.2gpm Max @ 60psi',
      'Available Finishes: Chrome, Brushed Nickel, Brushed Gold, Matte Black'
    ]
  },
  {
    title: 'Posh Solus Single Handle Lavatory Faucet',
    features: [
      'Brass construction',
      'Neoperl Aerator',
      'Flexible stainless steel inlet hoses with female 3/8” compression fitting',
      'Flow Rate: 2.4gpm Min @ 20psi',
      'Available Finishes: Chrome, Brushed Nickel, Brushed Gold, Matte Black'
    ]
  },
  {
    title: 'Posh Solus Roman Tub Faucet',
    features: [
      'Brass construction',
      '25mm ceramic cartridge',
      'Neoperl aerator',
      'Available Finishes: Chrome, Brushed Nickel, Brushed Gold, Matte Black'
    ]
  }
];
const finishes = [
  [f0, f5, f6, f7],
  [f1, f8, f9, f10],
  [f20, f21, f22, f23],
  [f2, f11, f12, f13],
  [f3, f14, f15, f16],
  [f4, f17, f18, f19]
];
const urls = links.posh.shopProducts;

export const productList: PoshFaucet[] = list.map((item, index) => {
  const url = urls[index];
  const imageFinishes = finishes[index];
  return { ...item, imageFinishes, url };
});
