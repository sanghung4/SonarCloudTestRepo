import { Dispatch, ReactNode, useContext, useState } from 'react';
import { DataMap } from '@reece/global-types';

import {
  ContractContext,
  ContractContextType,
  ContractProvider
} from 'Contract/ContractProvider';
import { mockData } from 'Contract/tests/mocks';
import { ContractDetails } from 'generated/graphql';
import { ContractProductLineItem } from 'Contract/ProductList';
import ReviewRelease from 'Contract/ReviewRelease';

type MockContractProviderType = {
  children: ReactNode;
  context: ContractContextType;
  setQtyInputMap?: Dispatch<DataMap<string>>;
  qtyInputMap?: DataMap<string>;
};
type MockProps = {
  contract?: ContractDetails;
};
export function MockContractProvider({
  children,
  context,
  setQtyInputMap,
  qtyInputMap
}: MockContractProviderType) {
  const [qtyInputMapHook, setQtyInputMapHook] = useState<DataMap<string>>({});
  return (
    <ContractContext.Provider
      value={{
        ...context,
        qtyInputMap: qtyInputMap ?? qtyInputMapHook,
        setQtyInputMap: setQtyInputMap ?? setQtyInputMapHook
      }}
    >
      {children}
    </ContractContext.Provider>
  );
}

const mockQtyInputMap = { '1': '123', '2': '', '3': '456' };
export function MockProviderTestComponent({ contract }: MockProps) {
  if (!contract) {
    contract = { ...mockData };
  }
  function MockChildComponent() {
    const {
      dialogType,
      goToCart,
      handleQtyClear,
      handleReleaseAll,
      handleReleaseOver10mil,
      qtyInputMap,
      resetDialog,
      setQtyInputMap
    } = useContext(ContractContext);
    return (
      <>
        <span data-testid="mock-dialog">{`${dialogType}`}</span>
        <span data-testid="mock-qtys">{`${
          Object.keys(qtyInputMap).length
        }`}</span>
        <button data-testid="mockfn-0" onClick={goToCart} />
        <button data-testid="mockfn-1" onClick={handleQtyClear} />
        <button data-testid="mockfn-2" onClick={handleReleaseAll} />
        <button data-testid="mockfn-3" onClick={resetDialog} />
        <button
          data-testid="mockfn-4"
          onClick={() => setQtyInputMap(mockQtyInputMap)}
        />
        <button data-testid="mockfn-5" onClick={handleReleaseOver10mil} />
      </>
    );
  }
  return (
    <ContractProvider contractData={contract}>
      <MockChildComponent />
    </ContractProvider>
  );
}

type MockReviewReleaseContextProps = {
  isReviewReady?: boolean;
  products: ContractProductLineItem[];
  context: ContractContextType;
};

export function MockReviewReleaseContext(props: MockReviewReleaseContextProps) {
  const [isReviewReady, setIsReviewReady] = useState(
    props.isReviewReady ?? false
  );
  return (
    <ContractContext.Provider
      value={{
        ...props.context,
        isReviewReady,
        setIsReviewReady
      }}
    >
      <ReviewRelease list={props.products} />
    </ContractContext.Provider>
  );
}
