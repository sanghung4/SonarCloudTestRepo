import { FooterItem } from 'common/Footer/util/useFooterData';
import { Link } from 'react-router-dom';

export default function footerItemLinkProp(item: FooterItem) {
  const externalRegExp = /^(http|https):\/\//gm;
  const isExternal = externalRegExp.test(item.to);

  return isExternal
    ? { target: '_blank', href: item.to }
    : { to: item.to, component: Link };
}
