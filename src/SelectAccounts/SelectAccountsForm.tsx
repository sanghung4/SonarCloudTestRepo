import { useContext, useEffect, useMemo, useState } from 'react';

import {
  Box,
  Button,
  InputLabel,
  Typography,
  FormControlLabel,
  Checkbox,
  useSnackbar
} from '@dialexa/reece-component-library';
import { Cached, CheckCircle } from '@mui/icons-material';
import { useTranslation } from 'react-i18next';

import { AuthContext } from 'AuthProvider';
import AutoComplete from 'common/AutoComplete';
import {
  EcommAccount,
  useGetRefreshShipToAccountLazyQuery,
  useUserAccountsQuery
} from 'generated/graphql';
import { useLocalStorage } from 'hooks/useLocalStorage';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import { testIds as fullTestIds } from 'test-utils/testIds';
import { useCartContext } from 'providers/CartProvider';
import Loader from 'common/Loader';
import { useListsContext } from 'providers/ListsProvider';

/**
 * Types
 */
type SelectAccountsFormProps = {
  onContinue?: () => void;
};

/**
 * Configs
 */
const TESTIDS = fullTestIds.SelectAccounts.SelectAccountsForm;

/**
 * Component
 */
function SelectAccountsForm(props: SelectAccountsFormProps) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();
  const [defaultAccountIds, setDefaultAccountIds] = useLocalStorage(
    'defaultAccountIds',
    { billToId: '', shipToId: '' }
  );
  const { pushAlert } = useSnackbar();

  /**
   * Context
   */
  const { selectedAccounts } = useSelectedAccountsContext();

  /**
   * Refs
   */

  /**
   * State
   */
  const [selectedBillTo, setSelectedBillTo] = useState<EcommAccount>();
  const [selectedBillToShipTos, setSelectedBillToShipTos] =
    useState<EcommAccount[]>();
  const [selectedShipTo, setSelectedShipTo] = useState<EcommAccount>();
  const [storeSelected, setStoreSelected] = useState(false);
  const [shipTosRefreshed, setShipTosRefreshed] = useState(false);
  const [refreshedBillToId, setRefreshedBillToId] = useState('');

  /**
   * Context
   */
  const { profile } = useContext(AuthContext);
  const { updateAccounts } = useSelectedAccountsContext();
  const { cart, getUserCart } = useCartContext();
  const { resetListProvider } = useListsContext();

  /**
   * Data
   */
  const { data: accountsData, loading: accountsLoading } = useUserAccountsQuery(
    {
      fetchPolicy: 'no-cache',
      skip: !profile?.userId,
      variables: { userId: profile?.userId }
    }
  );

  const [getRefreshShipToList, { loading: getRefreshShipToListLoading }] =
    useGetRefreshShipToAccountLazyQuery({
      fetchPolicy: 'network-only',
      onCompleted: (data) => {
        if (
          data?.refreshShipToAccount.length &&
          refreshedBillToId === selectedBillTo?.id
        ) {
          setSelectedBillToShipTos(data.refreshShipToAccount as EcommAccount[]);
        }
      }
    });

  /**
   * Memos
   */
  const billToOptions = useMemo(billToOptionsMemo, [accountsData]);
  const shipToOptions = useMemo(shipToOptionsMemo, [selectedBillToShipTos]);

  /**
   * Callbacks
   */

  /**
   * Effects
   */
  // Set the defaulted billTo
  // eslint-disable-next-line react-hooks/exhaustive-deps
  useEffect(selectDefaultBillTo, [
    accountsData,
    billToOptions,
    defaultAccountIds
  ]);

  // Set all of the ship to options equal to the selected billTo's shipTos
  useEffect(setShipToOptions, [selectedBillTo]);

  // Set the default shipTo
  // eslint-disable-next-line react-hooks/exhaustive-deps
  useEffect(selectDefaultShipTo, [
    accountsData,
    defaultAccountIds,
    selectedBillToShipTos,
    shipToOptions
  ]);

  // eslint-disable-next-line react-hooks/exhaustive-deps
  useEffect(submitIfOnlyOne, [
    billToOptions,
    selectedBillTo,
    selectedShipTo,
    shipToOptions
  ]);

  /**
   * Render
   */
  return (
    <>
      {accountsLoading && <Loader backdrop />}
      {/* Bill To */}
      <Box>
        <InputLabel htmlFor="bill-to">{t('selectAccounts.billTo')}</InputLabel>
        <AutoComplete
          id="bill-to-autocomplete"
          testId={TESTIDS.billTo}
          options={billToOptions}
          onChange={handleBillToChange}
          disabled={billToOptions.length <= 1}
          value={selectedBillTo?.id ?? ''}
        />
      </Box>
      {/* Ship To */}
      <Box marginTop={6}>
        <InputLabel htmlFor="ship-to">{t('selectAccounts.shipTo')}</InputLabel>
        <AutoComplete
          id="ship-to-autocomplete"
          testId={TESTIDS.shipTo}
          options={shipToOptions}
          disabled={!shipToOptions.length}
          onChange={handleShipToChange}
          value={selectedShipTo?.id ?? ''}
        />
      </Box>

      {profile?.isEmployee && (
        <Box marginTop={6}>
          <Typography
            variant="body1"
            color="textSecondary"
            alignItems="center"
            display="flex"
          >
            {getRefreshShipToListLoading && (
              <>
                <Box component={Cached} paddingRight={1} color="primary" />
                {t('common.updatingJobList')}
              </>
            )}

            {!getRefreshShipToListLoading &&
              (shipTosRefreshed ? (
                <>
                  <Box
                    component={CheckCircle}
                    paddingRight={1}
                    color="success.main"
                    data-testid={TESTIDS.refreshShipToDone}
                  />
                  {t('common.jobListUpToDate')}
                </>
              ) : (
                <>
                  <b>{t('common.doNotSeeJobList')}</b>
                  <Button
                    data-testid={TESTIDS.refreshShipToButton}
                    variant="inline"
                    color="primary"
                    onClick={handleRefreshShipToList}
                    sx={{ marginX: 0.5 }}
                  >
                    {t('common.clickHere')}
                  </Button>
                  {t('common.toUpdateJobList')}
                </>
              ))}
          </Typography>
        </Box>
      )}

      {/* Make Default */}
      <Box marginTop={3}>
        <FormControlLabel
          control={<Checkbox size="small" data-testid={TESTIDS.makeDefault} />}
          onChange={(_, value) => handleDefaultCheck(value)}
          label={`${t('account.defaultAccount')}`}
          value={storeSelected}
        />
      </Box>

      <Box
        width="50%"
        display="flex"
        justifyContent="center"
        marginTop={4}
        marginX="auto"
      >
        <Button
          fullWidth
          onClick={handleSubmit}
          disabled={
            !selectedBillTo ||
            !selectedShipTo ||
            accountsLoading ||
            getRefreshShipToListLoading
          }
          data-testid={TESTIDS.continue}
        >
          {t('selectAccounts.continue')}
        </Button>
      </Box>
    </>
  );

  /**
   * Memo Definitions
   */
  function billToOptionsMemo() {
    const sortedData = [...(accountsData?.userAccounts ?? [])].sort(
      sortAccounts
    );
    return formatOptions(sortedData);
  }

  function shipToOptionsMemo() {
    return formatOptions(selectedBillToShipTos);
  }

  /**
   * Effect Definitions
   */
  function selectDefaultBillTo() {
    const prevBillTo = accountsData?.userAccounts?.find(
      (item) => item.id === selectedAccounts.billTo?.id
    );

    const defaultBillTo = accountsData?.userAccounts?.find(
      (item) => item.id === defaultAccountIds.billToId
    );

    const firstBillTo = accountsData?.userAccounts?.find((item) =>
      profile?.isEmployee
        ? item.erpAccountId === '287169'
        : item.id === billToOptions[0]?.value
    );

    setSelectedBillTo(prevBillTo || defaultBillTo || firstBillTo);
  }

  function setShipToOptions() {
    const payload: EcommAccount[] = [];
    if (selectedBillTo?.shipTos?.length) {
      payload.push(...selectedBillTo.shipTos);
    } else if (selectedBillTo) {
      payload.push(selectedBillTo);
    }
    setSelectedBillToShipTos(payload);
  }

  function selectDefaultShipTo() {
    const prevShipTo = selectedBillToShipTos?.find(
      (item) => item.id === selectedAccounts.shipTo?.id
    );

    const defaultShipTo = selectedBillToShipTos?.find(
      (item) => item.id === defaultAccountIds.shipToId
    );
    const firstShipTo = selectedBillToShipTos?.find(
      (item) => item.id === shipToOptions[0]?.value
    );

    setSelectedShipTo(prevShipTo || defaultShipTo || firstShipTo);
  }

  function submitIfOnlyOne() {
    if (
      billToOptions.length === 1 &&
      shipToOptions.length === 1 &&
      selectedBillTo?.id &&
      selectedShipTo?.id
    ) {
      handleSubmit();
    }
  }
  /**
   * Callback Definitions
   */
  function sortAccounts(a?: EcommAccount, b?: EcommAccount) {
    return parseInt(a?.erpAccountId ?? '0') - parseInt(b?.erpAccountId ?? '0');
  }

  function formatOptions(data?: EcommAccount[]) {
    if (!data) {
      return [];
    }
    return data.map((item) => ({
      label: item.name ?? '',
      value: item.id ?? ''
    }));
  }

  function handleBillToChange(id?: string) {
    const selected = accountsData?.userAccounts?.find((item) => item.id === id);
    setShipTosRefreshed(false);
    setSelectedBillTo(selected);
  }

  function handleShipToChange(id?: string) {
    const selected = selectedBillToShipTos?.find((item) => item.id === id);
    setSelectedShipTo(selected);
  }

  function handleDefaultCheck(value: boolean) {
    setStoreSelected(value);
  }

  function handleRefreshShipToList() {
    setShipTosRefreshed(true);
    setRefreshedBillToId(selectedBillTo?.id ?? '');
    getRefreshShipToList({
      variables: { billToAccountId: selectedBillTo?.id ?? '' }
    });
  }

  function handleSubmit() {
    if (storeSelected) {
      setDefaultAccountIds({
        billToId: selectedBillTo?.id ?? '',
        shipToId: selectedShipTo?.id ?? ''
      });
    }

    try {
      cart && selectedShipTo && getUserCart(selectedShipTo);
      updateAccounts(selectedBillTo, selectedShipTo);
      // Reset List data when billto has changed
      if (
        selectedBillTo?.id &&
        selectedBillTo.id !== selectedAccounts.billTo?.id
      ) {
        resetListProvider();
      }
      props.onContinue?.();
    } catch (e) {
      console.error(e);
      pushAlert(t('common.messageFail'), {
        variant: 'error'
      });
    }
  }
}

export default SelectAccountsForm;
