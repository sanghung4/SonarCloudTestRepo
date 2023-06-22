import { gql } from '@apollo/client';

export const aboutUsQuery = gql`
  query ($aboutUsId: String!, $isPreview: Boolean!) {
    aboutUs(id: $aboutUsId, preview: $isPreview) {
      heroImage {
        url
      }
      title
      title2
      divisionsTitle {
        title
        paragraph
      }
      divisionsCarouselCollection {
        items {
          title
          paragraph
          paragraphColor
          linkText
          logo {
            url
          }
          backgroundImage {
            url
          }
        }
      }
      infoSectionsCollection {
        items {
          title
          paragraph
          text
        }
      }
      brands {
        title
        listCollection {
          items {
            url
            statesList
            logo {
              url
            }
            urlText
          }
        }
      }
      needAssistanceBlock {
        backgroundImage {
          url
        }
        logo {
          url
        }
        title
        paragraph
        linkText
      }
    }
  }
`;
