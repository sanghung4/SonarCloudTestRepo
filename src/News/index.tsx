import { useQuery } from '@apollo/client';
import { Link, Pagination } from '@dialexa/reece-component-library';

import { NewsQuery, newsQuery } from 'News/utils/query';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink, useLocation } from 'react-router-dom';

import Container from 'components/Container';
import Loader from 'common/Loader';
import { useQueryParams } from 'hooks/useSearchParam';
import NeedAssistance from 'News/utils/NeedAssistance';
import OnSocial from 'News/utils/OnSocial';
import { formatDateTimeZone } from 'utils/dates';
import 'News/styles.scss';

const PAGE_SIZE = 6;

export type NewsParams = {
  page?: string;
};

function News() {
  /**
   * Custom Hooks
   */
  const { search } = useLocation();
  const { t } = useTranslation();
  const [queryParams, setQueryParams] = useQueryParams<NewsParams>();
  const { page = '1' } = queryParams;

  /**
   * Data
   */
  const { data, loading } = useQuery<NewsQuery>(newsQuery, {
    context: {
      clientName: 'contentful'
    }
  });

  /**
   * Callbacks
   */
  const handlePagination = (newPage: number) => {
    window.scrollTo({ top: 0, behavior: 'smooth' });
    setQueryParams({
      ...queryParams,
      page: newPage.toString()
    });
  };

  const paginationElement = (itemCount: number) => (
    <Pagination
      count={Math.ceil((itemCount || 0) / PAGE_SIZE)}
      current={Number(page)}
      ofText={t('common.of')}
      onChange={handlePagination}
      onPrev={handlePagination}
      onNext={handlePagination}
      data-testid="pagination-container"
      dataTestIdCurrentPage="current-page-number"
      dataTestIdTotalNumberOfPages="total-number-of-pages"
    />
  );

  /**
   * Render
   */
  if (loading) {
    return <Loader />;
  }
  return (
    <div className="news" data-testid="news-page">
      <Container className="news__container" maxWidth="lg">
        <div className="news__title-container">
          <div className="news__title-container__title-item">
            <span className="news__title">{t('news.title')}</span>
          </div>
          <div className="news__title-container__social-item">
            <OnSocial
              socialItems={
                data?.followsocialReeceCollection?.items[0]
                  ?.socialImageCollection?.items
              }
            />
          </div>
        </div>
        <div className="news__list-container">
          {data?.blogNewsCollection?.items
            ?.map((item, index) => (
              <div
                className="news__list-container__news-item"
                data-testid={`news-${index}`}
                key={index}
              >
                <div className="news__list-container__news-item__news-box">
                  <div className="news__list-container__news-item__news-box__title">
                    {item?.title}
                  </div>
                  <div className="news__list-container__news-item__news-box__date-time">
                    {formatDateTimeZone(
                      item?.dateBlog,
                      'MM/dd/yyyy | hh:mma z'
                    )}
                  </div>
                  <div className="news__list-container__news-item__news-box__brief-text">
                    {item?.textcontentbodyBlog?.longText}
                  </div>
                </div>
                <div
                  className="news__list-container__news-item__learn-more-box"
                  data-testid={`learn-more-${index}`}
                >
                  <Link
                    to={{
                      pathname: `/newsdetails/${item?.sys?.id}`,
                      state: { search }
                    }}
                    component={RouterLink}
                  >
                    <div className="news__list-container__news-item__learn-more-box__text">
                      {t('news.readMore')}
                    </div>
                  </Link>
                </div>
              </div>
            ))
            .slice((Number(page) - 1) * PAGE_SIZE, Number(page) * PAGE_SIZE)}
        </div>
        <div className="news__pagination" data-testid="pagination-counter-top">
          {!loading &&
            Boolean(data?.blogNewsCollection?.items?.length) &&
            paginationElement(data?.blogNewsCollection?.items?.length ?? 0)}
        </div>
      </Container>
      {data?.reeceDivisionsCollection?.items[0] && (
        <NeedAssistance
          backgroundImage={
            data?.reeceDivisionsCollection?.items[0]?.backgroundImage?.url ?? ''
          }
          assistanceImage={
            data?.reeceDivisionsCollection?.items[0]?.logo?.url ?? ''
          }
          title={data?.reeceDivisionsCollection?.items[0]?.title ?? ''}
          paragraph={data?.reeceDivisionsCollection?.items[0]?.paragraph ?? ''}
          contactUsText={
            data?.reeceDivisionsCollection?.items[0]?.linkText ??
            t('support.header')
          }
        />
      )}
    </div>
  );
}

export default News;
