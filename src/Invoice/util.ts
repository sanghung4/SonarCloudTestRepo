import { EclipseAddress, Maybe } from 'generated/graphql';

export function handleAddress(address?: Maybe<EclipseAddress>) {
  const addressArray = Object.values(address ?? {});
  if (!address || !addressArray.length) {
    return '-';
  }
  return addressArray.filter((a) => !!a).join(', ');
}
