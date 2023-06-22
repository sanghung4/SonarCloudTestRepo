import { MockedResponse } from '@apollo/client/testing/core';
import { aboutUsQuery } from 'AboutUs/util/query';

export const success = {
  request: {
    query: aboutUsQuery,
    variables: {
      aboutUsId: '13j7MQQu4r6gSXv6KLiTYX',
      isPreview: false
    }
  },
  result: {
    data: {
      aboutUs: {
        heroImage: {
          url: 'https://images.ctfassets.net/32w9pi4zc4mq/74kpoSopFV9ixA1s90nmdY/36433020a557c165ea65293d3610da72/220219_Frisco_Lifestyle_InBranchInteraction_2__1_.jpg'
        },
        title: 'Reece.',
        title2: 'Works for you.',
        divisionsTitle: {
          title: 'Four Divisions. One Reece.',
          paragraph:
            'Reece is a leading supplier in the Plumbing, HVAC, Waterworks, and Bath + Kitchen industry. No matter what your business needs are, Reece aims to be a trusted, local partner for industry professionals.'
        },
        divisionsCarouselCollection: {
          items: [
            {
              title: 'Plumbing',
              paragraph:
                'Our goal is to be the number one supplier to all plumbers and contractors because we offer the parts they need and support the essential work they do. Big or small, no matter what you need, we have the parts and expertise to help you get the job done.',
              linkText: 'Find a Store',
              logo: null,
              backgroundImage: null
            },
            {
              title: 'HVAC',
              paragraph: null,
              linkText: null,
              logo: {
                url: 'https://images.ctfassets.net/32w9pi4zc4mq/452kW8fY5YZTSbBYxgedn2/eaa474887302154126c6de09ced725be/Group__4_.png'
              },
              backgroundImage: {
                url: 'https://images.ctfassets.net/32w9pi4zc4mq/27ezV64K0dF1caQCL7qcru/8f130f0cfca1e326240e22ce0376c15e/Mask_group.png'
              }
            },
            {
              title: 'Waterworks',
              paragraph: null,
              linkText: null,
              logo: {
                url: 'https://images.ctfassets.net/32w9pi4zc4mq/11HMCy9nR8sjv4SJ376FDv/7a3f1ad4f7b7b2628c0be2c5b70ac960/Group__5_.png'
              },
              backgroundImage: {
                url: 'https://images.ctfassets.net/32w9pi4zc4mq/4P0toqdWglcmD61w8tW9di/f64951a8a277b730048fcea90c33f474/Mask_group__1_.png'
              }
            },
            {
              title: 'Bath+Kitchen',
              paragraph: null,
              linkText: null,
              logo: {
                url: 'https://images.ctfassets.net/32w9pi4zc4mq/sipSRoyRjSp0UYJnPxMlm/63b8e53ee180892d9365f4f5bff49800/Group__6_.png'
              },
              backgroundImage: {
                url: 'https://images.ctfassets.net/32w9pi4zc4mq/3wFBqRKCBdAfqnVxWjeKqN/dc00683e8481c084bfcd7d99e94832fe/Mask_group__2_.png'
              }
            }
          ]
        },
        infoSectionsCollection: {
          items: [
            {
              title: 'We are Reece, and we work for you.',
              paragraph:
                'MORSCO and all of the partner companies are uniting under one brand, Reece. But what does this really mean?\nIt means that the services you know and depend on are backed by a brand with over 100 years of success.',
              text: null
            },
            {
              title: 'Because at Reece, we work for you.',
              paragraph:
                'As Reece, we are looking to the future with a shared commitment to being the #1 choice and partner to trade professionals.We are continuing to innovate across the Plumbing, HVAC, Waterworks, and Bath+Kitchen industries while creating a better experience for our customers.',
              text: '- The reece team'
            }
          ]
        },
        brands: {
          title: 'Our Brands',
          listCollection: {
            items: [
              {
                url: 'morrisonsupply',
                statesList: ['Kansas, New Mexico, Texas, Oklahoma'],
                logo: {
                  url: 'https://images.ctfassets.net/32w9pi4zc4mq/HqsGBPQZdK4B4hgxgUTdF/8f76b1dc94ca9c47ec96acfd9590a76e/morrison.svg'
                },
                urlText: 'Shop Now'
              },
              {
                url: 'morscohvacsupply',
                statesList: ['Arizona, Oklahoma, South Carolina, Texas '],
                logo: {
                  url: 'https://images.ctfassets.net/32w9pi4zc4mq/6ntUDZ7BZ87CEiTR7a8DEo/56a5fc6a2dce53a322de8fc80d3c1587/morscoHvac.svg'
                },
                urlText: 'Shop Now'
              },
              {
                url: 'expressionshomegallery',
                statesList: ['Arizona, California, Oklahoma, Texas '],
                logo: {
                  url: 'https://images.ctfassets.net/32w9pi4zc4mq/6aDwLH1uGi1hFUdePPRrJI/2e04568b2de49464de20073345471333/expressions.svg'
                },
                urlText: 'Shop Now'
              },
              {
                url: 'murraysupply',
                statesList: ['North Carolina, South Carolina.'],
                logo: {
                  url: 'https://images.ctfassets.net/32w9pi4zc4mq/6q854hesJE7DhuCMugdH6f/06c519697775deb6142c411978834335/murray.svg'
                },
                urlText: 'Shop Now'
              },
              {
                url: 'fortiline',
                statesList: [
                  'Alabama, Arizona, Florida, Georgia, Kansas, Kentucky, North Carolina, Oklahoma, South Carolina, Tennessee, Virginia, Texas '
                ],
                logo: {
                  url: 'https://images.ctfassets.net/32w9pi4zc4mq/rJdKqmpXIfZmpVjcPOovZ/90b604feb95198b3cbefa404c79d3dbc/fortiline.svg'
                },
                urlText: 'Shop Now'
              },
              {
                url: 'devoreandjohnson',
                statesList: ['Georgia '],
                logo: {
                  url: 'https://images.ctfassets.net/32w9pi4zc4mq/2enTRkrnvRsA8BalCp9K9n/c72bbfe85f6e0bd3c32fec436d83a94f/devore.svg'
                },
                urlText: 'Shop Now'
              },
              {
                url: 'expresspipe',
                statesList: ['California '],
                logo: {
                  url: 'https://images.ctfassets.net/32w9pi4zc4mq/cq6A2T9xGFruyKfRB26W6/4b57fb3115a73b87eb3f8cae773e90f7/express.svg'
                },
                urlText: 'Shop Now'
              },
              {
                url: 'wholesalespecialties',
                statesList: ['Colorado '],
                logo: {
                  url: 'https://images.ctfassets.net/32w9pi4zc4mq/5Att9cRKVxaRceoONhcsIq/a81ff9b0e117d619fd4ea45ca3a4a5a7/wholesale.svg'
                },
                urlText: 'Shop Now'
              },
              {
                url: 'fwcaz',
                statesList: ['Arizona', 'Nevada'],
                logo: {
                  url: 'https://images.ctfassets.net/32w9pi4zc4mq/5R5vsulHiLVyFHqDzXx274/e739f321451f00751f6eb2bbd2c53052/farnsworth.svg'
                },
                urlText: 'Shop Now'
              }
            ]
          }
        },
        needAssistanceBlock: {
          backgroundImage: {
            url: 'https://images.ctfassets.net/32w9pi4zc4mq/5GCdJPVZlGEzMtzhLVTNOR/5e5a29bd160aab6c9959dcafe2725be9/Mask_group__3_.png'
          },
          logo: {
            url: 'https://images.ctfassets.net/32w9pi4zc4mq/55uIera9vdaaXlVUYkRTds/1133da505e3ed00941d00b885a7b42a8/Vector.png'
          },
          title: 'Need Assistance?',
          paragraph:
            'We are here to answer any question and help on projects, big or small. Reach out to a local branch today.',
          linkText: 'Contact Us'
        },
        decorationImage: {
          url: 'https://images.ctfassets.net/32w9pi4zc4mq/6XaWLcQsewwB9Nokw3PTLe/7edcbcb591e336ed8e423a89cfffc417/plus_signs.svg'
        }
      }
    },
    loading: false
  }
};

export const mockNullResponse = {
  request: {
    query: aboutUsQuery,
    variables: {
      aboutUsId: 'A13j7MQQu4r6gSXv6KLiTYX',
      isPreview: false
    }
  },
  result: {
    data: {
      aboutUs: null
    }
  }
};

export const mockError: MockedResponse = {
  request: {
    query: aboutUsQuery,
    variables: {
      aboutUsId: '13j7MQQu4r6gSXv6KLiTYX',
      isPreview: false
    }
  },
  error: new Error('Error Encountered')
};
