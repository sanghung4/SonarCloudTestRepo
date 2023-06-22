import { Link } from '@dialexa/reece-component-library';
import 'News/styles.scss';

export type OnSocialProps = {
  socialItems?: [
    {
      internalname?: string;
      url?: string;
      media?: { url: string };
    }
  ];
};

function OnSocial(props: OnSocialProps) {
  return (
    <div className="news__social">
      {props.socialItems?.map((item, i) => (
        <div className="news__social__item" key={`social-${i}`}>
          <Link
            ml={2}
            target="_blank"
            href={item.url}
            color="inherit"
            underline="none"
            data-testid={`${item.internalname}-social-item`}
          >
            <img
              alt={item.internalname}
              src={item.media?.url}
              className="news__social__item__image"
            />
          </Link>
        </div>
      ))}
    </div>
  );
}

export default OnSocial;
