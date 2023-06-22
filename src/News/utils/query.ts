import { gql } from '@apollo/client';
import { Document } from '@contentful/rich-text-types';
import { Maybe } from 'graphql/jsutils/Maybe';

export const newsQuery = gql`
  query {
    blogNewsCollection(order: dateBlog_DESC) {
      items {
        sys {
          id
        }
        title
        slugBlog
        dateBlog
        textcontentbodyBlog {
          shortText
          longText
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

export type MediaReece = {
  internalname?: string;
  media: {
    url: string;
    __typename?: 'Asset';
  };
  url?: string;
  __typename?: 'MediaReece';
};

export type BlogNews = {
  sys: {
    id: string;
    __typename?: 'Sys';
  };
  title: string;
  slugBlog: Maybe<string>;
  dateBlog: string;
  overrideSysDate?: boolean;
  textcontentbodyBlog: {
    shortText: Maybe<string>;
    longText?: Maybe<string>;
    richText?: {
      json: Document;
      __typename?: 'ContentsRichText';
    };
    __typename?: 'Contents';
  };
  newMediaBlogCollection?: {
    items: [MediaReece];
    __typename?: 'BlogNewsNewMediaBlogCollection';
  };
  __typename?: 'BlogNews';
};

export type ReeceDivisions = {
  title: string;
  logo?: {
    url: string;
    __typename?: 'Asset';
  };
  backgroundImage?: {
    url: string;
    __typename?: 'Asset';
  };
  linkText?: string;
  paragraph?: string;
  paragraphColor?: string;
  __typename?: 'ReeceDivisions';
};

export type FollowsocialReece = {
  socialImageCollection: {
    items: [MediaReece];
    __typename?: 'FollowsocialReeceSocialImageCollection';
  };
  __typename?: 'FollowsocialReece';
};

export type NewsQuery = {
  blogNewsCollection: {
    items: [BlogNews];
    __typename?: 'BlogNewsCollection';
  };
  reeceDivisionsCollection: {
    items: [ReeceDivisions];
    __typename?: 'ReeceDivisionsCollection';
  };
  followsocialReeceCollection: {
    items: [FollowsocialReece];
    __typename?: 'FollowsocialReeceCollection';
  };
};
