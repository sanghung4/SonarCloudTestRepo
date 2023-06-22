import { Image, Link } from '@dialexa/reece-component-library';
import {
  ContactUsButton,
  NeedAssistanceImage,
  NeedAssistanceParagraph,
  NeedAssistanceTitle
} from 'AboutUs/util/styles';
import notfound from 'images/notfound.png';
import 'News/styles.scss';

type NeedAssistanceProps = {
  backgroundImage: string;
  assistanceImage: string;
  title: string;
  paragraph: string;
  contactUsText: string;
};

function NeedAssistance(props: NeedAssistanceProps) {
  return (
    <div className="news__need-assistance">
      <div className="news__need-assistance__image">
        <Image
          src={props.backgroundImage}
          alt="Background"
          fallback={notfound}
        />
      </div>
      <div className="news__need-assistance__tile">
        <div className="news__need-assistance__tile__icon">
          <NeedAssistanceImage
            fallback={notfound}
            src={props.assistanceImage}
            alt="Logo"
          />
        </div>
        <NeedAssistanceTitle>{props.title}</NeedAssistanceTitle>
        <NeedAssistanceParagraph>{props.paragraph}</NeedAssistanceParagraph>
        <div className="news__need-assistance__tile__support-button">
          <Link href="/support">
            <ContactUsButton
              border={2}
              borderLeft={2}
              borderRight={2}
              data-testid="contact-us-button"
              marginTop="20px"
              margin={5}
            >
              {props.contactUsText}
            </ContactUsButton>
          </Link>
        </div>
      </div>
    </div>
  );
}

export default NeedAssistance;
