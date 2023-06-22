import {
  ChangeEvent,
  createContext,
  Dispatch,
  ReactNode,
  SetStateAction,
  useState
} from 'react';

import { DataMap } from '@reece/global-types';
import { noop, pickBy } from 'lodash-es';
import { useHistory } from 'react-router-dom';

import ContractProductDetailsModal from 'Contract/ProductDetailsModal';
import {
  ContractProduct,
  ContractDetails,
  ErpAccount,
  ErpSystemEnum
} from 'generated/graphql';
import { VALUE_OVER_10MIL } from 'Cart/util';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import { valueOfReleasingContract } from './util';

type QtyInputMap = DataMap<string>;
type Props = {
  contractData?: ContractDetails;
  account?: ErpAccount;
  children: ReactNode;
};

export enum DialogEnum {
  Clear = 1,
  Release,
  Navigation,
  SubtotalToLarge
}

export type ContractContextType = {
  account?: ErpAccount;
  contractData?: ContractDetails;
  dialogType?: DialogEnum;
  goToCart: () => void;
  handleQtyClear: () => void;
  handleReleaseAll: () => void;
  handleReleaseOver10mil: () => void;
  handleSearchInputChange: (e: ChangeEvent<HTMLInputElement>) => void;
  isReviewReady: boolean;
  qtyInputMap: QtyInputMap;
  resetDialog: () => void;
  search: string;
  setIsReviewReady: Dispatch<SetStateAction<boolean>>;
  setModal: Dispatch<SetStateAction<ContractProduct | undefined>>;
  setQtyInputMap: Dispatch<QtyInputMap>;
  setSearch: Dispatch<SetStateAction<string>>;
};
const defaultContractContext: ContractContextType = {
  contractData: undefined,
  dialogType: undefined,
  goToCart: noop,
  handleQtyClear: noop,
  handleReleaseAll: noop,
  handleReleaseOver10mil: noop,
  handleSearchInputChange: noop,
  isReviewReady: false,
  qtyInputMap: {},
  resetDialog: noop,
  search: '',
  setIsReviewReady: noop,
  setModal: noop,
  setQtyInputMap: noop,
  setSearch: noop
};

export const ContractContext = createContext<ContractContextType>(
  defaultContractContext
);

export function ContractProvider({ account, children, contractData }: Props) {
  /**
   * Custom Hooks
   */
  const history = useHistory();

  /**
   * States
   */
  const [qtyInputMap, setQtyInputMapSimple] = useState<QtyInputMap>({});
  const [search, setSearch] = useState('');
  const [modal, setModal] = useState<ContractProduct | undefined>();
  const [dialogType, setDialogType] = useState<DialogEnum | undefined>();
  const [isReviewReady, setIsReviewReady] = useState(false);

  /**
   * Context
   */
  const { selectedAccounts } = useSelectedAccountsContext();
  const isMincron = selectedAccounts.erpSystemName === ErpSystemEnum.Mincron;

  /**
   * Render
   */
  return (
    <ContractContext.Provider
      value={{
        account,
        contractData,
        dialogType,
        goToCart,
        handleQtyClear,
        handleReleaseAll,
        handleReleaseOver10mil,
        handleSearchInputChange,
        isReviewReady,
        qtyInputMap,
        resetDialog,
        search,
        setIsReviewReady,
        setModal,
        setQtyInputMap,
        setSearch
      }}
    >
      <ContractProductDetailsModal product={modal} onClose={handleResetModal} />
      {children}
    </ContractContext.Provider>
  );

  /**
   * Handles
   */
  function goToCart() {
    history.push('/cart', {
      canShowNavAlert: true
    });
  }
  function handleQtyClear() {
    setDialogType(DialogEnum.Clear);
  }
  function handleSearchInputChange(e: ChangeEvent<HTMLInputElement>) {
    setSearch(e.target.value);
  }
  function handleResetModal() {
    setModal(undefined);
  }
  function handleReleaseAll() {
    const value = valueOfReleasingContract(contractData);
    const isTooLarge = value > VALUE_OVER_10MIL && isMincron;
    setDialogType(isTooLarge ? DialogEnum.SubtotalToLarge : DialogEnum.Release);
  }
  function handleReleaseOver10mil() {
    setDialogType(DialogEnum.SubtotalToLarge);
  }
  function resetDialog() {
    setDialogType(undefined);
  }
  function setQtyInputMap(input: QtyInputMap) {
    const filterOutBlanks = pickBy(input, (data) => !!parseInt(data));
    setQtyInputMapSimple(filterOutBlanks);
  }
}
