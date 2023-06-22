import React, {
  ChangeEvent,
  FormEvent,
  useCallback,
  useContext,
  useMemo,
  useRef,
  useState
} from 'react';

import {
  Box,
  Button,
  Card,
  Container,
  Grid,
  Hidden,
  Link,
  TextField,
  Typography,
  alpha,
  styled,
  useScreenSize,
  useTheme,
  Collapse,
  Alert,
  IconButton,
  lighten
} from '@dialexa/reece-component-library';
import { useDropzone } from 'react-dropzone';
import { useTranslation } from 'react-i18next';

import { ListContext, ListMode } from 'providers/ListsProvider';
import Breadcrumbs, { BreadcrumbConfig } from 'common/Breadcrumbs';
import Loader from 'common/Loader';
import { CloudUploadIcon, WarningIcon, CloseIcon } from 'icons';
import useDocumentTitle from 'hooks/useDocumentTitle';

import exampleFile from 'images/Upload_List_Example_File.svg';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import { ListsInfoTypography } from './util/styled';

const LinkButton = styled(Button)(({ theme }) => ({
  fontWeight: 500,
  '&, &:hover': {
    textDecoration: 'underline',
    color: `${theme.palette.primary02.main} !important`
  }
}));

const OL = styled('ol')(({ theme }) => ({
  paddingInlineStart: theme.spacing(3),
  marginBlockStart: 0,
  marginBlockEnd: 0
}));

function ListUpload() {
  /**
   * Context
   */
  const {
    selectedUploadList,
    setListMode,
    setOpen,
    setSelectedUploadList,
    uploadListLoading,
    newListName,
    getListRefetchLoading,
    setNewListName,
    uploadNewList,
    uploadToList,
    fileUploadError,
    setFileUploadError,
    lists
  } = useContext(ListContext);
  const { selectedAccounts } = useSelectedAccountsContext();

  /**
   * Callback
   */
  const onDropAccepted = useCallback(onDropAcceptedCb, [setSelectedUploadList]);
  const onDropRejected = useCallback(onDropRejectedCb, [setSelectedUploadList]);

  /**
   * Custom hooks
   */
  const {
    getInputProps,
    getRootProps,
    inputRef: fileUploadRef,
    isDragAccept,
    isDragReject,
    open: openFileDialog
  } = useDropzone({
    multiple: false,
    noClick: true,
    onDropAccepted,
    onDropRejected
  });
  const { isSmallScreen } = useScreenSize();
  const theme = useTheme();
  const { t } = useTranslation();
  useDocumentTitle(t('common.uploadList'));

  /**
   * State
   */
  const [acceptedFile, setAcceptedFile] = useState<File>();
  const [hasRejectedFile, setHasRejectedFile] = useState(false);

  /**
   * Ref
   */
  const uploadContainerRef = useRef<HTMLDivElement>(null);

  /**
   * Memo
   */
  const style = useMemo(
    () => ({
      ...(isDragAccept
        ? {
            backgroundColor: alpha(theme.palette.primary02.main, 0.25)
          }
        : {}),
      ...(isDragReject
        ? { backgroundColor: alpha(theme.palette.error.main, 0.25) }
        : {})
    }),
    [isDragAccept, isDragReject, theme.palette]
  );

  const listNameAlreadyExists = useMemo(
    () => lists.some((l) => l.name === newListName),
    [lists, newListName]
  );
  return (
    <>
      <Breadcrumbs
        pageTitle={t('lists.fileUpload')}
        config={[
          {
            text: t('common.myLists'),
            to: { pathname: '/lists' }
          } as BreadcrumbConfig
        ]}
      />
      <Container>
        <Card
          sx={{
            px: isSmallScreen ? 3 : 4,
            py: 3,
            mb: 3,
            displayPrint: 'none',
            textAlign: isSmallScreen ? 'center' : 'start'
          }}
        >
          <Typography variant="h5" data-testid="file-upload-title">
            {t('lists.fileUpload')}
          </Typography>
        </Card>
        <Card
          sx={{
            px: isSmallScreen ? 3 : 4,
            py: isSmallScreen ? 2 : 3,
            mb: 3,
            displayPrint: 'none'
          }}
        >
          <Typography
            variant="h6"
            sx={{ mt: 2, mb: 3 }}
            data-testid="how-to-format-header"
          >
            {t('lists.howToFormat')}
          </Typography>
          <Grid container>
            <Grid item xs={12} md={6}>
              <Typography sx={{ mb: 2 }}>
                {t('lists.toUpload')}{' '}
                <Link
                  href="/templates/list_template.csv"
                  underline="always"
                  download
                  sx={{ color: 'primary02.main' }}
                  data-testid="download-template-link"
                >
                  {t('lists.downloadTemplate')}
                </Link>
                .
              </Typography>
              <Typography>{t('lists.formatYourFile')}</Typography>
              <Typography component="div" data-testid="upload-list-steps">
                <OL>
                  <li>{t('lists.uploadStep1')}</li>
                  <li>{t('lists.uploadStep2')}</li>
                  <li>{t('lists.uploadStep3')}</li>
                  <li>{t('lists.uploadStep4')}</li>
                  <li>{t('lists.uploadStep5')}</li>
                  <li>{t('lists.uploadStep6')}</li>
                </OL>
              </Typography>
              <ListsInfoTypography>
                {t('lists.listsUploadTip')}
              </ListsInfoTypography>
            </Grid>
            <Grid item xs={12} md={6} data-testid="example-file">
              <Typography sx={{ mt: isSmallScreen ? 4 : 0 }}>
                {t('lists.exampleFile')}:
              </Typography>
              <Box
                component="img"
                src={exampleFile}
                alt="Upload List Example File"
                sx={{
                  width: (theme) =>
                    isSmallScreen ? `calc(100% + ${theme.spacing(3)})` : '100%',
                  ml: isSmallScreen ? -1.5 : 0,
                  mr: isSmallScreen ? -1.5 : 0,
                  imageRendering: '-webkit-optimize-contrast'
                }}
              />
            </Grid>
          </Grid>
          <Typography
            variant="h6"
            sx={{ mt: isSmallScreen ? 6 : 12, mb: 2 }}
            data-testid="upload-the-file-header"
          >
            {t('lists.uploadTheFile')}
          </Typography>
          <Box
            justifyContent="center"
            position={isSmallScreen ? undefined : 'sticky'}
            mx={isSmallScreen ? -1.5 : 1.5}
            mb={2}
          >
            <Collapse in={!!fileUploadError.length}>
              <Alert
                icon={<Box component={WarningIcon} sx={{ mr: 1 }} />}
                sx={(theme) => ({
                  color: theme.palette.error.dark,
                  bgcolor: `${lighten(theme.palette.error.main, 0.88)}`,
                  ...theme.typography.body2,
                  '& .MuiAlert-icon': {
                    color: theme.palette.error.dark
                  },
                  border: 1,
                  alignItems: 'center',
                  borderColor: theme.palette.error.dark
                })}
                data-testid="list-upload-alert"
                action={
                  <IconButton
                    color="inherit"
                    size="small"
                    data-testid="lost-upload-close-alert"
                    onClick={() => {
                      setFileUploadError([]);
                    }}
                  >
                    <CloseIcon />
                  </IconButton>
                }
              >
                <Typography
                  variant="subtitle2"
                  display="inline"
                  fontWeight="bold"
                  data-testid="error-title"
                >
                  {`${t('common.errorsTitle')}: `}
                </Typography>
                {t('lists.uploadFileErrorDesc')}
                {fileUploadError?.map((error, i) => (
                  <Typography
                    fontSize={14}
                    data-testid="upload-file-error-description"
                  >
                    {error ?? t(`lists.uploadFileErrorDesc`)}
                  </Typography>
                ))}
              </Alert>
            </Collapse>
          </Box>
          <Card
            {...(!acceptedFile ? getRootProps({ style }) : {})}
            square
            sx={{
              bgcolor: (theme) => alpha(theme.palette.primary02.main, 0.05),
              mx: isSmallScreen ? -1.5 : 1.5,
              p: 4,
              border: 1,
              borderColor: 'secondary03.main',
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
              position: 'relative'
            }}
            ref={uploadContainerRef}
            data-testid="dropzone-root"
          >
            {uploadListLoading ? <Loader backdrop /> : null}
            {!acceptedFile ? (
              <>
                <Box component={CloudUploadIcon} sx={{ mb: 3 }} />
                <Hidden smDown>
                  <Typography fontWeight={500} data-testid="drag-drop-text">
                    {t('lists.dragDrop')}
                  </Typography>
                  <Typography fontWeight={500} sx={{ mb: 3 }}>
                    {t('common.or')}
                  </Typography>
                </Hidden>
                <Button onClick={onChooseFile} data-testid="choose-file-button">
                  {t('common.chooseFile')}
                </Button>
                <input
                  {...getInputProps()}
                  id="choose-file-form"
                  name="chooseFileForm"
                  type="file"
                  onChange={handleChooseFile}
                  data-testid="choose-file-input"
                  style={{ display: 'none' }}
                />
              </>
            ) : (
              <Box
                component="form"
                onSubmit={handleFormSubmit}
                display="flex"
                flexDirection="column"
                alignItems="center"
                width={1}
              >
                <Box
                  display="flex"
                  flexDirection="column"
                  alignItems="center"
                  sx={{ width: isSmallScreen ? '100%' : '50%' }}
                >
                  <Box
                    display="flex"
                    flexDirection={isSmallScreen ? 'column' : 'row'}
                    justifyContent="center"
                    alignItems="center"
                    sx={{ mb: 4 }}
                  >
                    <Typography
                      fontWeight={500}
                      data-testid="file-name-label"
                    >{`${t('lists.fileSelected')}: ${
                      acceptedFile.name
                    }`}</Typography>
                    <LinkButton
                      type="button"
                      variant="inline"
                      onClick={handleRemoveFile}
                      sx={{
                        ml: isSmallScreen ? 0 : 1,
                        mt: isSmallScreen ? 1 : 0
                      }}
                      data-testid="remove-file-button"
                    >
                      {t('common.remove')}
                    </LinkButton>
                  </Box>
                  <TextField
                    label={t('lists.createNew')}
                    value={newListName}
                    onChange={(e) => setNewListName(e.target.value)}
                    inputProps={{ 'data-testid': 'upload-new-list-input' }}
                    fullWidth
                    autoFocus
                    error={
                      !!newListName &&
                      listNameAlreadyExists &&
                      !getListRefetchLoading
                    }
                    helperText={
                      !!newListName &&
                      listNameAlreadyExists &&
                      !getListRefetchLoading
                        ? t('lists.listExistsWarning')
                        : null
                    }
                    disabled={!!selectedUploadList}
                    data-testid="create-new"
                  />
                  <Typography
                    textTransform="uppercase"
                    color="textSecondary"
                    fontWeight={500}
                    sx={{ my: 3 }}
                  >
                    {t('common.or')}
                  </Typography>
                  {!selectedUploadList ? (
                    <Button
                      type="button"
                      variant="secondary"
                      onClick={handleSelectExistingList}
                      disabled={!!newListName}
                    >
                      {t('lists.selectExistingList')}
                    </Button>
                  ) : (
                    <TextField
                      label={
                        <>
                          {t('lists.selectedList')}
                          <LinkButton
                            variant="inline"
                            onClick={handleSelectExistingList}
                            sx={{
                              ml: 2
                            }}
                          >
                            {t('lists.changeList')}
                          </LinkButton>
                        </>
                      }
                      value={selectedUploadList?.name}
                      inputProps={{
                        'data-testid': 'upload-existing-list-input'
                      }}
                      disabled
                      fullWidth
                      sx={{
                        '& .MuiInputLabel-root': {
                          color: 'text.primary'
                        }
                      }}
                    />
                  )}
                  <Button
                    type="submit"
                    disabled={
                      (!newListName && !selectedUploadList) ||
                      (listNameAlreadyExists && !!newListName) ||
                      (!newListName.trim().length && !selectedUploadList)
                    }
                    sx={{ mt: 4 }}
                  >
                    {t('lists.uploadFile')}
                  </Button>
                </Box>
              </Box>
            )}
            {hasRejectedFile ? (
              <Typography
                color="error"
                sx={{ display: 'flex', alignItems: 'center', mt: 2 }}
                data-testid="upload-csv-error"
              >
                <Box component={WarningIcon} sx={{ mr: 1 }} />
                {t('validation.csvRequired')}
              </Typography>
            ) : null}
            <Typography fontWeight={500} color="textSecondary" sx={{ mt: 2 }}>
              {t('lists.fileTypes')}
            </Typography>
          </Card>
        </Card>
      </Container>
    </>
  );

  function handleChooseFile(event: ChangeEvent<HTMLInputElement>) {
    setFileUploadError([]);
    const [file] = event?.target?.files ?? [undefined];
    if (
      file?.type === 'text/csv' ||
      file?.type === 'application/vnd.ms-excel'
    ) {
      setAcceptedFile(file);
      setHasRejectedFile(false);
    } else {
      setAcceptedFile(undefined);
      setHasRejectedFile(true);
    }

    setNewListName('');
    setSelectedUploadList(undefined);
    scrollToUploadContainer();
  }

  function onDropAcceptedCb(droppedFiles: File[]) {
    setAcceptedFile(droppedFiles[0]);
    setSelectedUploadList(undefined);
    scrollToUploadContainer();
  }

  function onDropRejectedCb() {
    setHasRejectedFile(true);
    setSelectedUploadList(undefined);
  }

  function handleRemoveFile() {
    setNewListName('');
    setSelectedUploadList(undefined);
    if (fileUploadRef?.current?.value) fileUploadRef.current.value = '';
    setAcceptedFile(undefined);
    setHasRejectedFile(false);
    setFileUploadError([]);
  }

  function handleSelectExistingList() {
    setListMode(ListMode.UPLOAD);
    setOpen(true);
  }

  function onChooseFile() {
    setFileUploadError([]);
    openFileDialog();
  }

  function handleFormSubmit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    handleUploadFile();
  }

  function handleUploadFile() {
    if (acceptedFile) {
      if (newListName) {
        uploadNewList(
          acceptedFile,
          newListName,
          selectedAccounts.billTo?.id ?? ''
        );
      } else if (selectedUploadList) {
        uploadToList(acceptedFile, selectedUploadList.id);
      }
    }
  }

  function scrollToUploadContainer() {
    if (uploadContainerRef.current) {
      uploadContainerRef.current.scrollIntoView({
        behavior: 'smooth',
        block: 'start',
        inline: 'nearest'
      });
    }
  }
}

export default ListUpload;
