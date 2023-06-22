import { useEffect } from 'react';
import decodeHTMLEntities from '../utils/decodeHTMLEntities';

function useDocumentTitle(title: string) {
  useEffect(() => {
    const prevTitle = document.title;
    document.title = decodeHTMLEntities(title) + ' - Reece';
    return () => {
      document.title = prevTitle;
    };
  }, [title]);
}

export default useDocumentTitle;
