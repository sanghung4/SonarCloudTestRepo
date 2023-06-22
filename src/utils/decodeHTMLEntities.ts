import { unescape } from 'lodash-es';

function decodeHTMLEntities(input: string) {
  return unescape(input).replace(/(&#x2F;)+/gm, '/');
}

export default decodeHTMLEntities;
