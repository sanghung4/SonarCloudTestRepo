import clsx from 'clsx';

type CatalogStatusProps = {
  text?: string;
  'data-testid'?: string;
};

function CatalogStatus({ text, 'data-testid': testId }: CatalogStatusProps) {
  /*
   * Render
   */
  const checkText = text?.toLowerCase();
  return (
    <span
      className={clsx('px-2 py-[1px] rounded-full font-semibold', {
        'text-primary-3-50': !checkText,
        'text-primary-3-80 bg-primary-3-20': checkText === 'draft',
        'text-support-7-100 bg-support-7-20': checkText === 'active',
        'text-support-6-100 bg-support-6-20': checkText === 'submitted',
        'text-black bg-gray-300': checkText === 'archived'
      })}
      data-testid={testId}
    >
      {text ?? 'Unknown'}
    </span>
  );
}

export default CatalogStatus;
