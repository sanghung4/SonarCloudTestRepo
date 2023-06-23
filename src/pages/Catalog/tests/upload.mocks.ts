import { CatalogUploadResponse } from 'api/catalog.api';
import { mockProduct1 } from 'api/mocks/catalog.mocks';

/**
 * Types
 */
type Mocks = {
  success: CatalogUploadResponse;
  error: CatalogUploadResponse;
};

/**
 * Mocks
 */
const mocks: Mocks = {
  success: { uploadId: 'upload-id', products: [mockProduct1] },
  error: { error: 'ERROR', products: [mockProduct1] }
};
export default mocks;
