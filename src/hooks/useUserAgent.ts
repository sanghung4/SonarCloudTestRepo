export default function useUserAgent() {
  const ua = navigator.userAgent;
  const isiOS = Boolean(process.browser && /iPad|iPhone|iPod/.test(ua));

  return {
    ua,
    isiOS
  };
}
