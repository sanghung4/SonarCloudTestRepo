import { Maybe } from 'yup';

export type EncodedFile = {
  fileName: string;
  encoded: string | ArrayBuffer | null;
};

export async function convertFileList(files: Maybe<FileList>) {
  const fileList: EncodedFile[] = [];
  if (!files) {
    return [];
  }
  // interface FileList doesn't support native `.map` on Typescript
  for (const file of files) {
    try {
      const base64raw = await encodeFileToBase64(file);
      let base64string = '';
      // istanbul ignore next
      if (typeof base64raw === 'string') {
        base64string = base64raw;
      } else {
        base64string = window.btoa(
          String.fromCharCode(...new Uint8Array(base64raw ?? []))
        );
      }
      const encodedWithoutHeader = base64string.split(',')[1];
      fileList.push({
        fileName: file.name,
        encoded: encodedWithoutHeader
      });
    } catch (e) {
      console.error(e);
    }
  }
  return fileList;
}

export function encodeFileToBase64(file: File) {
  return new Promise<FileReader['result']>((resolve, reject) => {
    const reader = new FileReader();
    reader.onloadend = () => resolve(reader.result);
    reader.readAsDataURL(file);
  });
}
