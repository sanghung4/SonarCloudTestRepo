import { useMemo } from 'react';

// For Okta username match criteria, see here:
// https://support.okta.com/help/s/question/0D51Y000069RzZI/password-policy-does-not-contain-part-of-username?language=en_US
export default function useUsernameParts(username: string | undefined | null) {
  return useMemo(() => {
    const tld = /(\.[a-zA-Z]{2,63}){1,}$/gm;
    const specialChar = /[^a-zA-Z0-9]+/gm;
    return username
      ? username
          .toLowerCase() // Matches username and password all in lowercase, just in case
          .replace(tld, '') // remove TLD
          .split(specialChar) // split the user parts by special char
          .filter((item) => item.length >= 4) // Parts has to be more than 4 char
      : [''];
  }, [username]);
}
