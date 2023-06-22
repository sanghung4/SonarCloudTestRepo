export default function trimSpaces(input?: string) {
  return (input ?? '').replace(/\s{2,}/g, ' ').trim();
}
