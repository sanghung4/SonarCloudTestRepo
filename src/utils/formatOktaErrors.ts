export function formatOktaErrors(error: string) {
  const regExp = /\(([^)]+)\)/;
  return regExp.exec(error)![0].replace(/[{()}]/g, '');
}
