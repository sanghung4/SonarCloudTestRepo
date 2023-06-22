import {
  Box,
  Image,
  Skeleton,
  Typography
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { Product } from 'generated/graphql';
import notfound from 'images/notfound.png';
import { SearchTopResultContainer } from './styles';

type Props = {
  product?: Product;
  loading?: boolean;
  onClick?: (id: string) => void;
};

function SearchTopResult(props: Props) {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();

  /**
   * Render
   */
  if (props.loading) {
    return (
      <SearchTopResultContainer>
        <Box display="flex" paddingRight={2} height={80}>
          <Skeleton variant="rectangular" height={64} width={64} />
        </Box>
        <Box display="flex" flexGrow={1} flexDirection="column">
          <Skeleton width="40%" />
          <Skeleton />
          <Skeleton width="60%" />
        </Box>
      </SearchTopResultContainer>
    );
  }

  return (
    <SearchTopResultContainer display="flex" onClick={handleClick}>
      {/* Image Container */}
      <Box display="flex" paddingRight={2}>
        <Box height={64} width={64}>
          <Image
            alt={props.product?.name ?? t('common.productPicture')}
            fallback={notfound}
            src={props.product?.imageUrls?.thumb ?? t('common.productPicture')}
          />
        </Box>
      </Box>
      {/* Details Container */}
      <Box display="flex" flexGrow={1} flexDirection="column">
        <Typography variant="caption" display="block" paddingBottom={0.5}>
          {props.product?.manufacturerName}
        </Typography>
        <Typography
          variant="body1"
          paddingBottom={0.5}
          fontWeight={600}
          lineHeight="1.2rem"
        >
          {props.product?.name}
        </Typography>
        <Typography variant="caption" display="block">
          {`${t('contract.mfrNum')} ${
            props.product?.manufacturerNumber ?? '-'
          }`}
        </Typography>
      </Box>
    </SearchTopResultContainer>
  );

  /**
   * Callback Definitions
   */
  function handleClick() {
    if (props.onClick && props.product) {
      props.onClick(props.product.id);
    }
  }
}

export default SearchTopResult;
