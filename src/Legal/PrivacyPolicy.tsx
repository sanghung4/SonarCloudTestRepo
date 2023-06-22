import React from 'react';

import { Box, Link, Typography } from '@dialexa/reece-component-library';
import { Link as RouterLink } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

function PrivacyPolicy() {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  return (
    <>
      <Box>
        <Typography variant="h4">{t('legal.privacyPolicy')}</Typography>
      </Box>
      <Box mt={4} mx={1}>
        <Typography variant="body1">
          MORSCO, Inc. a Delaware corporation, d/b/a Reece and/or any present or
          future parent, subsidiary, affiliate or business unit of MORSCO, Inc.
          (collectively, “Reece”) owns and operates a website with a home page
          located at{' '}
          {
            <Link to="/" component={RouterLink}>
              {window.location.host}
            </Link>
          }{' '}
          (including linked/connected websites, collectively, the "Site"). We
          value the privacy of individuals who visit the Site (the "User(s)", or
          "you"). This Website Privacy Policy (this "Privacy Policy") has been
          created to inform Users of the types of individual and technical
          information that is or may be collected on the Site and the privacy
          policies and practices that Reece follows with respect to the use and
          dissemination of such information.
        </Typography>
      </Box>
      <Box mt={4} mx={1}>
        <Typography variant="body1">
          <b>1. Effective Date</b>. This Privacy Policy is effective as of
          September 24, 2021 and may be updated and modified from time to time
          by us in our sole discretion. If we choose to update or modify this
          Privacy Policy, such updated or modified policy shall become effective
          after its posting to the Site. Accordingly, we encourage you to
          periodically review this Privacy Policy and the Site in order to keep
          apprised of our current privacy practices. If you do not agree to any
          provision of this Privacy Policy, or any update or modification of it,
          please do not use the Site.
        </Typography>
      </Box>
      <Box mt={4} mx={1}>
        <Typography variant="body1">
          <b>2. What information do we collect?</b>
        </Typography>
      </Box>
      <Box mt={2} mx={1}>
        <Typography variant="body1">
          a. Personal Information: Our goal is to help you understand what
          Personal Information (as defined below) is collected and how such
          information will be used before you decide whether to disclose it.
          Personally identifiable information ("Personal Information") means
          individually identifiable information about a User, including without
          limitation the User's full name, company name, title, user name and
          password, e mail address, telephone number and street address.
          Personal Information does not include information in a form that is
          aggregated with other information so as not to be reasonably
          identifiable as the User, and information in a form that otherwise is
          detached, combined, organized, segmented, modified or processed so as
          not to be reasonably capable of being associated with the User
          ("Nonpersonal Information"). As a result, Nonpersonal Information is
          not restricted by this Privacy Policy as to use, sharing or otherwise.
        </Typography>
      </Box>
      <Box mt={2} mx={1}>
        <Typography variant="body1">
          b. Information That You Provide. Information collected from you will
          vary, depending on how you use the Site, how the Site is configured
          and what information you may choose to provide to us (whether via
          mail, telephone, e mail or otherwise). It may be possible to browse
          various sections of the Site without submitting any Personal
          Information. In addition, for security reasons there may be features
          on our Site that require you to confirm your Personal Information or
          provide additional Personal Information. You may choose not to provide
          us with Personal Information, but as a consequence you may not be able
          to use the Site, or take advantage of certain features of our Site,
          and we may not be able to provide you with certain requested
          information, products and/or services.
        </Typography>
      </Box>
      <Box mt={2} mx={1}>
        <Typography variant="body1">
          c. Automatic Collection: Like many websites on the Internet, we
          automatically track certain information (either directly or through
          third party services, such as Google Analytics) about you as you visit
          and use the Site in order to help us (1) better understand, improve
          and modify the Site, (2) better understand how the Site is used and
          experienced and (3) better understand how we can enhance your
          experience or others overall experience on one or more of the Site,
          and (4) ensure that a User is using the Site in accordance with the
          Terms of Use. This tracked or automatically gathered information may
          include, among other things, your computer's Internet Protocol (IP)
          address, the URLs and Site pages that you have visited, sections of or
          content on Site pages on which you click or in which you are
          interested, the number of times you visit each Site page, what
          downloads and/or search queries you have made, how long you spent on
          particular sections of each Site and on each Site generally, how much
          content has been accessed and/or copies, and your type of web browser.
          This automatically gathered data includes information provided through
          the use of "cookies", which are described in more detail below.
        </Typography>
      </Box>
      <Box mt={2} mx={1}>
        <Typography variant="body1">
          d. Information from Cookies: We may send small files or other similar
          strings of information to your Workstation (collectively "Cookies")
          and store such Cookies on your Workstation so that we can recognize it
          as a unique machine the next time you visit the Site. Cookies may also
          be used to perform statistical and administrative tasks including
          measuring site and page traffic, and positioning images on the web.
          You acknowledge that certain Cookies may be integral to enabling you
          to access our Site, or essential to the performance of our Site, and
          recognize that our Site may have limited functionality, or no
          functionality at all, if you decline to accept Cookies. Therefore, you
          agree to accept these Cookies in connection with using our Site.
          Should you change your mind and decide that you are not comfortable
          with Cookies being stored on your Workstation, please call
          817-870-2227 immediately.
        </Typography>
      </Box>
      <Box mt={4} mx={1}>
        <Typography variant="body1">
          <b>3. How is information about me used and shared?</b>
        </Typography>
      </Box>
      <Box mt={2} mx={1}>
        <Typography variant="body1">
          a. Personal Information. Except as provided in this Policy, we do not
          willfully disclose Personal Information about our users to anyone
          outside of our corporate family without first receiving the User's
          permission. We do not sell, trade, or otherwise transfer your Personal
          Information to unaffiliated parties, and will not share your Personal
          Information with affiliates if prohibited by applicable law or
          regulation.
        </Typography>
      </Box>
      <Box mt={2} mx={1}>
        <Typography variant="body1">
          b. Support. Notwithstanding the above, we may engage or use other
          companies and individuals (1) to perform supporting functions for the
          various tools, functionality, information, products and services
          offered through our Site, (2) to perform or support various tasks or
          initiatives instrumental to the business of, or related to operating
          or improving, the Site, or (3) to assist us in testing, maintaining or
          improving the features, content or effectiveness of the Site or in
          performing research or development. These service providers may be
          permitted to receive from us and use Nonpersonal Information, as well
          as Personal Information provided by you or collected from you, but
          such service providers will not be permitted to use Personal
          Information for any purpose other than in connection with performing
          the limited support functions for the website.
        </Typography>
      </Box>
      <Box mt={2} mx={1}>
        <Typography variant="body1">
          c. Administrative Use and Required Disclosure. We reserve the right to
          use Personal Information to send you emails, letters or other messages
          relating to the products, services, promotions and discounts offered
          by Reece and other emails and notifications about major changes to our
          Site (although we are not obligated to notify you regarding such Site
          changes). We may also release Personal Information or other
          information that we collect from you if we believe that such action is
          reasonably necessary to (1) comply with legal process, (2) enforce the
          Terms of Use for the Site, (3) identify, contact or bring legal action
          against persons or entities who are or we believe have caused or might
          cause injury to us or a third party, (4) defend or respond to claims
          brought or threatened against Reece, its employees, directors,
          suppliers, service providers, Site users or others, or (5) otherwise
          protect or assert the rights, property, interests or personal safety
          of Reece, its employees, directors, suppliers, service providers, Site
          users or others. Any such release decisions may be made by us in our
          sole discretion.
        </Typography>
      </Box>
      <Box mt={2} mx={1}>
        <Typography variant="body1">
          d. Asset transfers: We continue to develop our business and in doing
          so might choose to acquire other businesses or sell our business or
          certain assets of our business. The Personal Information and other
          information that we collect from you in connection with your use of
          the Site is considered one component of our business assets and would
          likely be transferred/included in such transactions. We will, however,
          not sell your Personal Information to companies whose goal is to
          market services and products, and in such event we will use
          commercially reasonable efforts to protect your Personal Information.
        </Typography>
      </Box>
      <Box mt={2} mx={1}>
        <Typography variant="body1">
          e. Nonpersonal Information. Nonpersonal Information may be shared with
          third parties, including listing providers, advertisers, service
          providers and others, so that we may better understand how the Site is
          used, to enhance the overall experience on the Site and otherwise in
          connection with our operation of the Site and conducting our business.
        </Typography>
      </Box>
      <Box mt={2} mx={1}>
        <Typography variant="body1">
          f. Children's Privacy: In compliance with the Children's Online
          Privacy Protection Act, 15 U.S.C. §§ 6501 – 6506 and 16 C.F.R. §§
          312.1 – 312.12, does not permit children under 13 years of age to
          become Subscribers of the Site. Reece does not collect information
          from children. By using the Site, Subscriber represents that
          Subscriber is not under 13 years of age and will not permit such a
          person from accessing the Site. You agree to contact Reece immediately
          that if you have knowledge that a child under the age of 13 has used
          the Site or submitted Personal Information to us, via our Site or
          otherwise.
        </Typography>
      </Box>
      <Box mt={4} mx={1}>
        <Typography variant="body1">
          <b>4. How can I access and change my Personal Information?</b> You may
          change the Personal Information you have submitted by calling
          817-870-2227. By contacting Reece to make such changes, you expressly
          authorize MORSCO to access your Personal Information and any
          associated data or accounts for the purpose of completing your
          requested edits, modifications and/or deletions. If you would like to
          receive a copy of the Personal Information we have about you as
          submitted to us via the Site, please send a request via U.S. Mail
          using the contact information set forth below. You may also ask us to
          remove your name and other Personal Information from our database, in
          which case we will make reasonable efforts to do so, subject to legal
          and other considerations. You understand that any changes you may
          make, or ask us to make, regarding your Personal Information, may
          adversely affect or otherwise change the availability, deliverability
          or quality of the Site for you, and prevent you from accessing the
          information, features, products or services in which you are
          interested or your overall experience on or relating to the Site. As
          set forth in the Terms of Use, Reece reserves the right to terminate
          any User for any reason, including as a result of User's desire for
          Reece to alter or remove Personal Information.
        </Typography>
      </Box>
      <Box mt={4} mx={1}>
        <Typography variant="body1">
          <b>5. How do we protect your information?</b> Whenever Personal
          Information is stored on our computers, that information is protected
          from unauthorized access or use by way of passwords or other
          industry-acknowledged means. We will use commercially reasonable
          efforts only to electronically transmit or ask for Personal
          Information over secure Internet connections using SSL (Secure Sockets
          Layer) encryption, an accepted standard for online security, but
          cannot guarantee that all such information will be encrypted in such
          fashion. Since we take our responsibility to secure Personal
          Information seriously, you agree to assist us in this effort by
          limiting the disclosure of Personal Information to only what
          information is asked, watching your web browser to monitor whether the
          connection is encrypted prior to submitting such information,
          informing us immediately if you suspect that Personal Information
          (whether yours or that of another) is accidentally being disclosed,
          and alerting us if you believe that Personal Information is being used
          in a manner that is contrary to this Privacy Policy, the Terms of Use
          or otherwise in any improper manner. You hereby acknowledge that no
          data transmission over the Internet nor any storage of information on
          servers or other media is guaranteed or entirely secure, and agree
          that even though Reece will takes steps to safeguard Personal
          Information, Reece will not be responsible for the disclosure of
          Personal Information if Reece has taken reasonable steps to safeguard
          this information in accordance with this Privacy Policy. We cannot
          promise, guarantee or warrant the complete security of Personal
          Information.
        </Typography>
      </Box>
      <Box mt={4} mx={1}>
        <Typography variant="body1">
          <b>6. Opt-out of Google Analytics.</b> Our Site enables Google
          Analytics, which collects and analyzes certain information described
          in Section 2.c. above. In order to provide Users with more choice on
          how their data is collected by Google Analytics, we want you inform
          you of the Google Analytics Opt-out Browser Add-on. The add-on
          communicates with the Google Analytics JavaScript to indicate that
          information about your visit to the Site should not be sent to Google
          Analytics. To opt-out, download or install the add-on for your current
          web browser. The Google Analytics Opt-out Browser Add-on is available
          for Microsoft Internet Explorer, Google Chrome, Mozilla Firefox, Apple
          Safari and Opera.
        </Typography>
      </Box>
      <Box mt={4} mx={1}>
        <Typography variant="body1">
          <b>7. Contacting Us.</b> Reece welcomes User's feedback. Users who
          have any questions or comments concerning the Privacy Policy or their
          experiences with the Site should contact Morsco Reece at:
        </Typography>
      </Box>
      <Box mt={2} mx={1}>
        <Typography variant="body1">
          Privacy Administrator
          <br />
          MORSCO, Inc.
          <br />
          15850 Dallas Parkway
          <br />
          Dallas, TX 75248
          <br />
          Email: info@morsco.com
          <br />
        </Typography>
      </Box>
      <Box mt={4} mx={1}>
        <Typography variant="body1">
          <b>8. Terms of Use.</b> Please also visit our Terms and Conditions
          section establishing the use, disclaimers, and limitations of
          liability governing the use of our Site.
        </Typography>
      </Box>
      <Box mt={4} mx={1}>
        <Typography variant="body1">
          <b>9. Your Consent.</b> By using the Site, User consents to this
          Privacy Policy and the Terms of Use. Reece reserves the right to
          modify, alter or update this Privacy Policy at any time. Reece will
          post any revised Privacy Policy on the Site to apprise Users of what
          information Reece collects, how it may be used, and under what
          circumstances it is disclosed. Any modifications, alterations, or
          updates of the Privacy Policy will be effective upon posting, and
          User's continued use of the Site will constitute User's consent to any
          revised Privacy Policy.
        </Typography>
      </Box>
    </>
  );
}
export default PrivacyPolicy;
