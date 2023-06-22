import { useQuery } from '@apollo/client';
import { documentToReactComponents } from '@contentful/rich-text-react-renderer';
import { Button } from '@dialexa/reece-component-library';

import Container from 'components/Container';
import { NewsDetailsQuery, newsDetailsQuery } from 'NewsDetails/utils/query';
import { useTranslation } from 'react-i18next';
import { useHistory, useLocation, useParams } from 'react-router-dom';
import { ArrowLeftIcon } from 'icons';
import { formatDateTimeZone } from 'utils/dates';
import Loader from 'common/Loader';
import NeedAssistance from 'News/utils/NeedAssistance';
import OnSocial from 'News/utils/OnSocial';
import Slider from 'components/Slider';
import { useScrollToTop } from 'hooks/useScrollToTop';
import 'NewsDetails/styles.scss';

/**
 * Types
 */
export type NewsRouterState = {
  search?: string;
};

function NewsDetails() {
  useScrollToTop();
  /**
   * Custom Hooks
   */
  const history = useHistory();
  const location = useLocation<NewsRouterState>();
  const { t } = useTranslation();
  const { id } = useParams<{ id: string }>();

  /**
   * Data
   */
  const { data, loading } = useQuery<NewsDetailsQuery>(newsDetailsQuery, {
    context: {
      clientName: 'contentful'
    },
    variables: {
      blogNewsId: id ?? ''
    }
  });

  /**
   * Callbacks
   */

  const goBackToNews = () =>
    history.push(`/news${location.state?.search ?? ''}`);

  /**
   * Render
   */
  if (loading) {
    return <Loader />;
  }
  return (
    <div className="news-details">
      <div className="news-details__header">
        <Container maxWidth="lg">
          <div className="news-details__header__section">
            <div className="news-details__header__section__back">
              <Button
                startIcon={<ArrowLeftIcon fontSize="small" />}
                variant="inline"
                onClick={goBackToNews}
                className="news-details__header__section__back__button"
                data-testid="go-back-button"
              >
                <span className="news-details__header__section__back__button__text">
                  {t('common.goBack')}
                </span>
              </Button>
            </div>
            <div className="news-details__header__section__social-item">
              <OnSocial
                socialItems={
                  data?.followsocialReeceCollection?.items[0]
                    ?.socialImageCollection?.items
                }
              />
            </div>
          </div>
        </Container>
      </div>
      <Container className="news-details__container" maxWidth="lg">
        <div
          className="news-details__container__news"
          data-testid={`news-details`}
        >
          <div className="news-details__container__news__header">
            <span className="news-details__container__news__header__date-time">
              {data?.blogNews &&
                formatDateTimeZone(
                  data?.blogNews?.dateBlog,
                  'MM/dd/yyyy | hh:mma z'
                )}
            </span>
            <div className="news-details__container__news__header__title">
              {data?.blogNews?.title}
            </div>
            <div className="news-details__container__news__header__short-text">
              {data?.blogNews?.textcontentbodyBlog?.shortText}
            </div>
          </div>
          <div className="news-details__container__news__body">
            {Boolean(data?.blogNews?.newMediaBlogCollection?.items?.length) && (
              <div className="news-details__container__news__body__images-slider">
                <Slider
                  testId="news-images-slider"
                  children={
                    data?.blogNews?.newMediaBlogCollection?.items?.map(
                      (item, idx) => (
                        <img
                          src={item?.media?.url}
                          alt={`alt-img-${idx}`}
                          key={idx}
                        />
                      )
                    ) as React.ReactNode[]
                  }
                />
              </div>
            )}
            {documentToReactComponents(
              data?.blogNews?.textcontentbodyBlog?.richText?.json!
            )}
          </div>
        </div>
        <div className="news-details__container__follow-us">
          <div
            className="news-details__container__follow-us__social"
            data-testid="follow-us-on-social-box"
          >
            <div className="news-details__container__follow-us__social__header">
              {t('news.followUs')}
            </div>
            <div className="news-details__container__follow-us__social__short-text">
              {t('news.followUsMoreText')}
            </div>
          </div>
          <div className="news-details__container__follow-us__on-social">
            <div className="news-details__container__follow-us__on-social__text">
              {t('news.socialText')}
            </div>
            <div className="news-details__container__follow-us__on-social__links">
              <OnSocial
                socialItems={
                  data?.followsocialReeceCollection?.items[0]
                    ?.socialImageCollection?.items
                }
              />
            </div>
          </div>
        </div>
      </Container>
      <NeedAssistance
        backgroundImage={
          data?.reeceDivisionsCollection?.items[0]?.backgroundImage?.url!
        }
        assistanceImage={data?.reeceDivisionsCollection?.items[0]?.logo?.url!}
        title={data?.reeceDivisionsCollection?.items[0]?.title!}
        paragraph={data?.reeceDivisionsCollection?.items[0]?.paragraph!}
        contactUsText={data?.reeceDivisionsCollection?.items[0]?.linkText!}
      />
    </div>
  );
}

export default NewsDetails;
