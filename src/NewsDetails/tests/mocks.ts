import { MockedResponse } from '@apollo/client/testing/core';
import { newsDetailsQuery } from 'NewsDetails/utils/query';

export const success = {
  request: {
    query: newsDetailsQuery,
    variables: {
      blogNewsId: '26DWkCGphEQfhtn1BrPBPK'
    }
  },
  result: {
    data: {
      blogNews: {
        title:
          'MORSCO names chip hornsby executive Chairman, Shasha Nikolic CEO',
        slugBlog:
          'morsco-names-chip-hornsby-executive-chairman-shasha-nikolic-ceo',
        dateBlog: '2023-03-21T00:00:00.000Z',
        overrideSysDate: true,
        textcontentbodyBlog: {
          shortText: null,
          richText: {
            json: {
              nodeType: 'document',
              data: {},
              content: [
                {
                  nodeType: 'paragraph',
                  data: {},
                  content: [
                    {
                      nodeType: 'text',
                      value:
                        'Hornsby, who has served as MORSCO’s CEO since 2011, was elevated to the role of Executive Chairman to concentrate on executing MORSCO’s strategic priorities and long-term direction.',
                      marks: [],
                      data: {}
                    }
                  ]
                },
                {
                  nodeType: 'paragraph',
                  data: {},
                  content: [
                    {
                      nodeType: 'text',
                      value:
                        'Hornsby, who has served as MORSCO’s CEO since 2011, was elevated to the role of Executive Chairman to concentrate on executing MORSCO’s strategic priorities and long-term direction.',
                      marks: [],
                      data: {}
                    }
                  ]
                },
                {
                  nodeType: 'paragraph',
                  data: {},
                  content: [
                    {
                      nodeType: 'text',
                      value:
                        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.',
                      marks: [],
                      data: {}
                    }
                  ]
                },
                {
                  nodeType: 'paragraph',
                  data: {},
                  content: [
                    {
                      nodeType: 'text',
                      value:
                        'Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.',
                      marks: [],
                      data: {}
                    }
                  ]
                },
                {
                  nodeType: 'paragraph',
                  data: {},
                  content: [
                    {
                      nodeType: 'text',
                      value:
                        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.',
                      marks: [],
                      data: {}
                    }
                  ]
                },
                {
                  nodeType: 'paragraph',
                  data: {},
                  content: [
                    {
                      nodeType: 'text',
                      value: 'Mauris Pellentesque Pulvinar Habitant',
                      marks: [
                        {
                          type: 'bold'
                        }
                      ],
                      data: {}
                    }
                  ]
                },
                {
                  nodeType: 'paragraph',
                  data: {},
                  content: [
                    {
                      nodeType: 'text',
                      value:
                        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Augue lacus viverra vitae congue eu. Amet consectetur adipiscing elit duis tristique sollicitudin nibh sit amet. Gravida in fermentum et sollicitudin. Id consectetur purus ut faucibus pulvinar elementum.',
                      marks: [],
                      data: {}
                    }
                  ]
                },
                {
                  nodeType: 'blockquote',
                  data: {},
                  content: [
                    {
                      nodeType: 'paragraph',
                      data: {},
                      content: [
                        {
                          nodeType: 'text',
                          value:
                            'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor.',
                          marks: [],
                          data: {}
                        }
                      ]
                    }
                  ]
                },
                {
                  nodeType: 'paragraph',
                  data: {},
                  content: [
                    {
                      nodeType: 'text',
                      value:
                        'Porttitor lacus luctus accumsan tortor posuere ac. Nunc mi ipsum faucibus vitae. A iaculis at erat pellentesque adipiscing. Neque vitae tempus quam pellentesque. Augue eget arcu dictum varius duis. Iaculis eu non diam phasellus vestibulum lorem sed risus ultricies. ',
                      marks: [],
                      data: {}
                    }
                  ]
                },
                {
                  nodeType: 'paragraph',
                  data: {},
                  content: [
                    {
                      nodeType: 'text',
                      value:
                        'Sed ullamcorper morbi tincidunt ornare massa eget. Amet consectetur adipiscing elit duis tristique sollicitudin nibh sit amet. Gravida in fermentum et sollicitudin.',
                      marks: [],
                      data: {}
                    }
                  ]
                }
              ]
            }
          }
        },
        newMediaBlogCollection: {
          items: [
            {
              media: {
                url: 'https://images.ctfassets.net/32w9pi4zc4mq/2aSqgt2zhs5Mms2WydAt7q/df6c178cba1a6706a7e09fa0badfa9d4/Reece-Plumbing-News.png'
              }
            }
          ]
        }
      },
      reeceDivisionsCollection: {
        items: [
          {
            title: 'Need Assistance?',
            logo: {
              url: 'https://images.ctfassets.net/32w9pi4zc4mq/55uIera9vdaaXlVUYkRTds/1133da505e3ed00941d00b885a7b42a8/Vector.png'
            },
            backgroundImage: {
              url: 'https://images.ctfassets.net/32w9pi4zc4mq/5GCdJPVZlGEzMtzhLVTNOR/5e5a29bd160aab6c9959dcafe2725be9/Mask_group__3_.png'
            },
            linkText: 'Contact Us',
            paragraph:
              'We’re here to answer any question and help on projects, big or small. Reach out to a local branch today.',
            paragraphColor: null
          }
        ]
      },
      followsocialReeceCollection: {
        items: [
          {
            socialImageCollection: {
              items: [
                {
                  internalname: 'Share-Reece-Social',
                  media: {
                    url: 'https://images.ctfassets.net/32w9pi4zc4mq/1VpunhbohvAGZkkJdaQURz/f9a916ebf6554b31102973922fc83fa1/reece-social-share.svg'
                  },
                  url: 'https://reece.com/'
                },
                {
                  internalname: 'Instagram-Reece-Social',
                  media: {
                    url: 'https://images.ctfassets.net/32w9pi4zc4mq/E5PAiIUxZlzNN7IUbpiC8/7eb7dfa9e3236878f85e7804be31cf21/reece-social-instagram.svg'
                  },
                  url: 'https://www.instagram.com/'
                },
                {
                  internalname: 'Facebook-Reece-Social',
                  media: {
                    url: 'https://images.ctfassets.net/32w9pi4zc4mq/2jQFKAiujhvrRjHUdyxxT6/2744bc3754d65660f29d9297d58be009/reece-social-facebook.svg'
                  },
                  url: 'https://www.facebook.com/'
                },
                {
                  internalname: 'Twitter-Reece-Social',
                  media: {
                    url: 'https://images.ctfassets.net/32w9pi4zc4mq/S11csj9JfsrIQaOKgLhHJ/9380a52c7660d75eb4d76a84dd751fe5/reece-social-twitter.svg'
                  },
                  url: 'https://twitter.com/'
                },
                {
                  internalname: 'Linkedin-Reece-Social',
                  media: {
                    url: 'https://images.ctfassets.net/32w9pi4zc4mq/scVvY52n3g6UvHEW9AiCv/f059408dd60ff17e8ae22eb61f731a3d/reece-social-linkedin.svg'
                  },
                  url: 'https://www.linkedin.com/company/reeceusa/'
                }
              ]
            }
          }
        ]
      }
    },
    loading: false
  }
};

export const mockNullResponse = {
  request: {
    query: newsDetailsQuery
  },
  result: {
    data: {
      aboutUs: null
    }
  }
};

export const mockError: MockedResponse = {
  request: {
    query: newsDetailsQuery
  },
  error: new Error('Error Encountered')
};
