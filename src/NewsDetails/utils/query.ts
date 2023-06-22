import { gql } from '@apollo/client';
import { BlogNews, FollowsocialReece, ReeceDivisions } from 'News/utils/query';

export const newsDetailsQuery = gql`
  query ($blogNewsId: String!) {
    blogNews(id: $blogNewsId) {
      title
      slugBlog
      dateBlog
      overrideSysDate
      textcontentbodyBlog {
        shortText
        richText {
          json
        }
      }
      newMediaBlogCollection(limit: 5) {
        items {
          media {
            url
          }
        }
      }
    }
    reeceDivisionsCollection(where: { title: "Need Assistance?" }) {
      items {
        title
        logo {
          url
        }
        backgroundImage {
          url
        }
        linkText
        paragraph
        paragraphColor
      }
    }
    followsocialReeceCollection(
      where: { internalname: "Follow Us on Social - Reece" }
    ) {
      items {
        socialImageCollection(limit: 5) {
          items {
            internalname
            media {
              url
            }
            url
          }
        }
      }
    }
  }
`;

export type NewsDetailsQuery = {
  blogNews: BlogNews;
  reeceDivisionsCollection: {
    items: [ReeceDivisions];
    __typename?: 'ReeceDivisionsCollection';
  };
  followsocialReeceCollection: {
    items: [FollowsocialReece];
    __typename?: 'FollowsocialReeceCollection';
  };
};
