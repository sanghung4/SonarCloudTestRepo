import { ProductCatalog } from 'api/catalog.api';
import { Image } from 'components/Image';
import { DefaultCell, defaultHeader, TableInstance } from 'components/Table';
import { ReactComponent as TrashIcon } from 'resources/icons/trash.svg';

const columns = [
  'Image',
  'Product Name / Description',
  'Part #',
  'UOM',
  'Manufacturer',
  'MFR #',
  'Category 1',
  'Category 2',
  'Category 3',
  'Delete'
];

export function catalogListTableConfig(data?: ProductCatalog[]) {
  return {
    data: data ?? [],
    columns,
    config: {
      // Image
      [columns[0]]: {
        header: defaultHeader('uppercase text-center'),
        cell: ({ id, product }) => (
          <DefaultCell>
            <Image
              src={product?.imageFullSize ?? '#'}
              alt={id}
              className="max-w-[50px] max-h-[50px]"
            />
          </DefaultCell>
        )
      },
      // Product Name / Description
      [columns[1]]: {
        header: defaultHeader('uppercase min-w-[220px]'),
        cell: ({ product }) => (
          <DefaultCell>{product?.name ?? 'N/A'}</DefaultCell>
        )
      },
      // Part #
      [columns[2]]: {
        header: defaultHeader('uppercase min-w-[75px]'),
        cell: ({ partNumber }) => (
          <DefaultCell>{partNumber ?? '--'}</DefaultCell>
        )
      },
      // UOM
      [columns[3]]: {
        header: defaultHeader('uppercase'),
        cell: ({ product }) => (
          <DefaultCell>{product?.unspsc ?? '--'}</DefaultCell>
        )
      },
      // Manufacturer
      [columns[4]]: {
        header: defaultHeader('uppercase'),
        cell: ({ product }) => (
          <DefaultCell>{product?.manufacturer ?? '--'}</DefaultCell>
        )
      },
      // MFR #
      [columns[5]]: {
        header: defaultHeader('uppercase min-w-[75px]'),
        cell: ({ product }) => (
          <DefaultCell>{product?.manufacturerPartNumber ?? '--'}</DefaultCell>
        )
      },
      // Category 1
      [columns[6]]: {
        header: defaultHeader('uppercase min-w-[110px]'),
        cell: ({ product }) => (
          <DefaultCell>{product?.categoryLevel1Name ?? '--'}</DefaultCell>
        )
      },
      // Category 2
      [columns[7]]: {
        header: defaultHeader('uppercase min-w-[110px]'),
        cell: ({ product }) => (
          <DefaultCell>{product?.categoryLevel2Name ?? '--'}</DefaultCell>
        )
      },
      // Category 3
      [columns[8]]: {
        header: defaultHeader('uppercase min-w-[110px]'),
        cell: ({ product }) => (
          <DefaultCell>{product?.categoryLevel3Name ?? '--'}</DefaultCell>
        )
      },
      // Delete
      [columns[9]]: {
        header: defaultHeader('uppercase text-center'),
        cell: ({ id }) => (
          <DefaultCell className="flex justify-center">
            <button
              type="button"
              disabled
              data-testid={`catalog-list-delete-${id}`}
              className="text-gray-400"
            >
              <TrashIcon />
            </button>
          </DefaultCell>
        )
      }
    }
  } as TableInstance<ProductCatalog>;
}
