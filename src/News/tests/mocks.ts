import { MockedResponse } from '@apollo/client/testing/core';
import { newsQuery } from 'News/utils/query';

export const success = {
  request: {
    query: newsQuery
  },
  result: {
    data: {
      blogNewsCollection: {
        items: [
          {
            sys: {
              id: '357kwU3kl9QAeGhi6UaPgh'
            },
            title:
              'MORSCO names chip hornsby executive Chairman, Shasha Nikolic CEO',
            slugBlog:
              'morsco-names-chip-hornsby-executive-chairman-shasha-nikolic-ceo',
            dateBlog: '2023-03-21T00:00:00.000Z',
            textcontentbodyBlog: {
              shortText: null,
              longText:
                'Hornsby, who has served as MORSCO’s CEO since 2011, was elevated to the role of Executive Chairman to concentrate on executing MORSCO’s strategic priorities and long-term direction.'
            }
          },
          {
            sys: {
              id: '26DWkCGphEQfhtn1BrPBPK'
            },
            title: 'Reece Limited FY19 Full Year Results',
            slugBlog: 'reece-limited-fy19-full-year-results',
            dateBlog: '2023-03-08T00:00:00.000Z',
            textcontentbodyBlog: {
              shortText: null,
              longText:
                'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. '
            }
          },
          {
            sys: {
              id: 'RwGYJobAHcMorD4GB7OQi'
            },
            title: 'Mauris Pellentesque Pulvinar Habitant',
            slugBlog: null,
            dateBlog: '2023-03-01T01:22:00.000+05:30',
            textcontentbodyBlog: {
              shortText: 'Quis Nostrud Exercitation Ullamco Laboris',
              longText:
                'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi. \n'
            }
          },
          {
            sys: {
              id: '26DWkCGphEQfhtn1BrPBP1'
            },
            title: 'Reece Limited FY18 Full Year Results',
            slugBlog: 'reece-limited-fy18-full-year-results',
            dateBlog: '2022-03-08T00:00:00.000Z',
            textcontentbodyBlog: {
              shortText: null,
              longText:
                'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. '
            }
          },
          {
            sys: {
              id: '26DWkCGphEQfhtn1BrPBP2'
            },
            title: 'Reece Limited FY17 Full Year Results',
            slugBlog: 'reece-limited-fy17-full-year-results',
            dateBlog: '2020-03-08T00:00:00.000Z',
            textcontentbodyBlog: {
              shortText: null,
              longText:
                'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. '
            }
          },
          {
            sys: {
              id: '26DWkCGphEQfhtn1BrPBP3'
            },
            title: 'Reece Limited FY16 Full Year Results',
            slugBlog: 'reece-limited-fy16-full-year-results',
            dateBlog: '2019-03-08T00:00:00.000Z',
            textcontentbodyBlog: {
              shortText: null,
              longText:
                'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. '
            }
          },
          {
            sys: {
              id: '26DWkCGphEQfhtn1BrPBP4'
            },
            title: 'Reece Limited FY15 Full Year Results',
            slugBlog: 'reece-limited-fy15-full-year-results',
            dateBlog: '2018-03-08T00:00:00.000Z',
            textcontentbodyBlog: {
              shortText: 'Results are out',
              longText:
                'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. '
            }
          }
        ]
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
    query: newsQuery
  },
  result: {
    data: {
      aboutUs: null
    }
  }
};

export const mockError: MockedResponse = {
  request: {
    query: newsQuery
  },
  error: new Error('Error Encountered')
};
