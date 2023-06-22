package com.reece.platform.mincron.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.as400.data.PcmlException;
import com.ibm.as400.data.ProgramCallDocument;
import com.reece.platform.mincron.exceptions.*;
import com.reece.platform.mincron.model.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.reece.platform.mincron.service.PooledMincronConnection.PROGRAM_CALL_DOCUMENT;

@RequiredArgsConstructor
@Service
public class MincronService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MincronService.class);
    private static final String MINCRON_ACCOUNT_NOT_FOUND_MSG = "Invalid Account ID.";
    private final RestTemplate restTemplate;
    private final ProgramCallDocumentFactory programCallDocumentFactory;

    @Value("${mincron_host_websmart}")
    private String mincronHostWebsmart;

    /**
     * Retrieve account from Mincron
     *
     * @param customerId ID of customer / account to get from Mincron
     * @return GetAccountResponseDTO containing requested account
     */
    public GetAccountResponseDTO getAccount(String customerId, Boolean retrieveShipToList) throws AccountNotFoundException, JsonProcessingException {
        try (val pooledConnection = programCallDocumentFactory.getPooledMincronConnection()) {
            String programName = "AIRC103";
            val result = pooledConnection.callProgram(programName, new PCMLParameters("ACCOUNTID", customerId), "CUSTNAME", "ADDRLINE1", "ADDRLINE2", "ADDRLINE3", "CITY", "STATE", "ZIP", "AREACODE", "PREFIX", "SUFFIX", "HOMEBRANCH");

            String customerName = (String) result.get("CUSTNAME");
            if (customerName.equals(MINCRON_ACCOUNT_NOT_FOUND_MSG)) {
                throw new AccountNotFoundException(String.format("No account with id %s found.", customerId),
                        HttpStatus.NOT_FOUND);
            }

            GetAccountResponseDTO getAccountResponseDTO = new GetAccountResponseDTO();
            getAccountResponseDTO.setStreet1((String) result.get("ADDRLINE1"));
            getAccountResponseDTO.setStreet2(result.get("ADDRLINE2") + " "
                    + result.get("ADDRLINE3"));
            getAccountResponseDTO.setCity((String) result.get("CITY"));
            getAccountResponseDTO.setCompanyName(customerName.trim());
            getAccountResponseDTO.setState((String) result.get("STATE"));
            getAccountResponseDTO.setZip((String) result.get( "ZIP"));
            getAccountResponseDTO.setErpAccountId(customerId.trim());
            getAccountResponseDTO.setPhoneNumber(
                    result.get("AREACODE") + "-" +
                            result.get( "PREFIX") + "-" +
                            result.get( "SUFFIX")

            );

            // Branch IDs in data systems outside of Mincron prefix Mincron branches with a "6"
            getAccountResponseDTO.setBranchId(String.format("6%s", result.get("HOMEBRANCH")));

            if (retrieveShipToList) {
                getAccountResponseDTO.setShipToAccounts(getJobsForAccount(customerId).stream().map(account ->
                        new GetAccountResponseDTO(account, getAccountResponseDTO.getPhoneNumber())).collect(Collectors.toList()));
            }

            return getAccountResponseDTO;
        }
    }

    public CountDTO validateCount(String branchNumber, String countId) {
        try (val pooledConnection = programCallDocumentFactory.getPooledMincronConnection()) {
            val programName = "AIRC300";
            val result = pooledConnection.callProgram(
                    programName,
                    new PCMLParameters("BRANCH#", branchNumber, "COUNTID", countId),
                    "CNTVALID", "BRNAME");

            val valid = "0".equals(result.get("CNTVALID"));
            if (!valid) {
                throw new MincronException("Invalid count id '" + countId + "' for branch number '" + branchNumber + "'", HttpStatus.NOT_FOUND);
            }

            val branchName = (String) result.get("BRNAME");
            return new CountDTO(branchNumber, countId, branchName);
        }
    }

    public List<LocationCodeDTO> getLocations(String branchNumber, String countId) {
        try (val pooledConnection = programCallDocumentFactory.getPooledMincronConnection()) {
            val programName = "AIRC302";
            val result = pooledConnection.callProgram(
                    programName,
                    new PCMLParameters("BRANCH#", branchNumber, "COUNTID", countId),
                    "NUMOFLOCS"
            );

            val numberOfLocs = Integer.parseInt((String) result.get("NUMOFLOCS"), 10);

            val pcd = (ProgramCallDocument) result.get(PROGRAM_CALL_DOCUMENT);
            val indicies = new int[1];

            return IntStream.range(0, numberOfLocs).mapToObj(i -> {
                indicies[0] = i;
                try {
                    val row = pcd.getStringValue(programName + ".LOCATIONS.ROW", indicies);
                    val bin = pcd.getStringValue(programName + ".LOCATIONS.BIN", indicies);
                    val shelf = pcd.getStringValue(programName + ".LOCATIONS.SHELF", indicies);
                    return new LocationCodeDTO(row, bin, shelf);
                } catch (PcmlException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
        }
    }

    public Optional<NextLocationDTO> getNextLocation(String branchNumber, String countId, String locationId) {
        try (val pooledConnection = programCallDocumentFactory.getPooledMincronConnection()) {
            val programName = "AIRC303";
            val result = pooledConnection.callProgram(
                    programName,
                    new PCMLParameters("BRANCH#", branchNumber, "COUNTID", countId, "CURRENTLOC", locationId),
                    "ERRORCODE", "NEXTLOC"
            );

            val errorCode = (String) result.get("ERROR_CODE");
            val nextLocationId = (String) result.get("NEXTLOC");

            if ("1".equals(errorCode) || nextLocationId == null || nextLocationId.length() == 0) {
                return Optional.empty();
            }

            return Optional.of(new NextLocationDTO(nextLocationId));
        }
    }

    @SneakyThrows
    public LocationDTO getItemsAtLocation(String branchNum, String countId, String locationId) {
        try (val pooledConnection = programCallDocumentFactory.getPooledMincronConnection()) {
            val programName = "AIRC310";
            val result = pooledConnection.callProgram(
                    programName,
                    new PCMLParameters("BRANCH#", branchNum, "COUNTID", countId, "LOCATION", locationId),
                    "ERROR_CODE", "TOTALQTY", "ITEMCOUNT"
            );

            val errorCode = (String) result.get("ERROR_CODE");
            var additionalItemsExist = false;

            if (errorCode != null && errorCode.length() > 0) {
                // Error Code 3 means that there are more than 400 items
                // It's not really an error, more of an FYI
                if ("3".equals(errorCode)) {
                    additionalItemsExist = true;
                } else {
                    throw new GetLocationException(errorCode);
                }
            }

            val totalQty = Integer.parseInt((String) result.get("TOTALQTY"), 10);
            val itemCount = Integer.parseInt((String) result.get("ITEMCOUNT"), 10);

            val pcd = (ProgramCallDocument) result.get(PROGRAM_CALL_DOCUMENT);

            final List<LocationItemDTO> parmary = new ArrayList<>();
            val indices = new int[1];
            for (int i = 0; i < itemCount; i++) {
                indices[0] = i;
                val itemNum = pcd.getStringValue(programName + ".PARMARY.ITEM#", indices);
                val prodNum = pcd.getStringValue(programName + ".PARMARY.PROD#", indices);
                var prodDesc = pcd.getStringValue(programName + ".PARMARY.PRODDESC", indices);
                val catalogNum = pcd.getStringValue(programName + ".PARMARY.CAT#", indices);
                val tagNum = pcd.getStringValue(programName + ".PARMARY.TAG#", indices);
                val uom = pcd.getStringValue(programName + ".PARMARY.UOM", indices);

                val descLine2 = pcd.getStringValue(programName + ".PARMARY.DLINE2", indices);
                val descLine3 = pcd.getStringValue(programName + ".PARMARY.DLINE3", indices);

                if (descLine2 != null && !descLine2.isEmpty()) {
                    prodDesc += "\n" + descLine2;
                }

                if (descLine3 != null && !descLine3.isEmpty()) {
                    prodDesc += "\n" + descLine3;
                }

                parmary.add(new LocationItemDTO(itemNum, prodNum, prodDesc, catalogNum, tagNum, uom));
            }

            LOGGER.debug("errorCode={}, totalQty={}, itemCount={}, parmary={}", errorCode, totalQty, itemCount, parmary);
            return new LocationDTO(locationId, totalQty, itemCount, parmary, additionalItemsExist);
        }
    }

    public CreateContactResponseDTO createContact(String accountId, CreateContactRequestDTO createContactRequestDTO) throws InvalidPhoneNumberException, JsonProcessingException, PcmlException, AccountNotFoundException {
        try (val pooledConnection = programCallDocumentFactory.getPooledMincronConnection()) {
            val programName = "AIRC110";
            val userId = UUID.randomUUID().toString().replace("-", "").substring(0, 10);

            var username = String.format("%s %s", createContactRequestDTO.getFirstName(), createContactRequestDTO.getLastName());
            // Mincron limits us to 30 characters
            if (username.length() > 30) username = username.substring(0, 30);

            val password = UUID.randomUUID().toString().replace("-", "").substring(0, 10);

            var phone = createContactRequestDTO.getTelephone().replaceAll("\\D+", "");

            if (phone.length() != 10) throw new InvalidPhoneNumberException();

            val areacode = phone.substring(0, 3);
            val prefix = phone.substring(3, 6);
            val suffix = phone.substring(6, 10);

            val params = new PCMLParameters(
                    "PIACCOUNTID", accountId,
                    "PIUSERID", userId,
                    "PIUSERNAME", username,
                    "PIPWD", password,
                    "PIEMAIL", createContactRequestDTO.getEmail(),
                    "PIAREACODE", areacode,
                    "PIPREFIX", prefix,
                    "PISUFFIX", suffix,
                    "PIOPTION", "A",// A = add, D = delete, U = update
                    "PIVIEWAR", "Y",// Y N or blank (Authorized to view A/R)
                    "PICCPROCESS", "Y", // Y N or blank (Authorized to view CC process)
                    "PIBILLPAY", "Y",// Y N or blank (Authorized to view online bill pay)
                    "PICOMPORDER", "Y",// Y N or blank (Authorized to check out ecomm orders)
                    "PIMAINTUSERS", "Y",// Y N or blank (Authorized to maintiain ecomm users)
                    "PIORDEMAILFLG", "Y",// Y N or blank (Send email after order created)
                    "PISHPEMAILFLG", "Y"// Y N or blank (Send email after order shipped)
            );
            val result = pooledConnection.callProgram(programName, params, "PIUSERNAME", "PIUSERID", "PIPWD");

            CreateContactResponseDTO createContactResponseDTO = new CreateContactResponseDTO();
            createContactResponseDTO.setErpUsername((String) result.get("PIUSERNAME"));
            createContactResponseDTO.setContactId((String) result.get("PIUSERID"));
            createContactResponseDTO.setErpPassword((String) result.get("PIPWD"));

            return createContactResponseDTO;
        }
    }

    /**
     * Sends a request to update user information in Mincron ERP.
     *
     * @param existingAccountId     mincron accountId
     * @param existingUserId        mincron userId
     * @param editContactRequestDTO updated user information
     * @return user's information
     * @throws InvalidPhoneNumberException
     * @throws JsonProcessingException
     * @throws PcmlException
     * @throws AccountNotFoundException
     */
    public EditContactResponseDTO updateContact(String existingAccountId, String existingUserId, EditContactRequestDTO editContactRequestDTO) throws InvalidPhoneNumberException {
        try (val pooledConnection = programCallDocumentFactory.getPooledMincronConnection()) {
            val programName = "AIRC110";
            val accountId = existingAccountId;
            val userId = existingUserId;
            val password = editContactRequestDTO.getErpUserPassword();
            val email = editContactRequestDTO.getEmail();

            var username = String.format("%s %s", editContactRequestDTO.getFirstName(), editContactRequestDTO.getLastName());
            // Mincron limits us to 30 characters
            if (username.length() > 30) username = username.substring(0, 30);

            var phone = editContactRequestDTO.getPhoneNumber().replaceAll("\\D+", "");

            if (phone.length() != 10) throw new InvalidPhoneNumberException();

            val areacode = phone.substring(0, 3);
            val prefix = phone.substring(3, 6);
            val suffix = phone.substring(6, 10);

            val params = new PCMLParameters(
                    "PIACCOUNTID", accountId,
                    "PIUSERID", userId,
                    "PIUSERNAME", username,
                    "PIPWD", password,
                    "PIEMAIL", email,
                    "PIAREACODE", areacode,
                    "PIPREFIX", prefix,
                    "PISUFFIX", suffix,
                    "PIOPTION", "U",// A = add, D = delete, U = update
                    "PIVIEWAR", "Y",// Y N or blank (Authorized to view A/R)
                    "PICCPROCESS", "Y", // Y N or blank (Authorized to view CC process)
                    "PIBILLPAY", "Y",// Y N or blank (Authorized to view online bill pay)
                    "PICOMPORDER", "Y",// Y N or blank (Authorized to check out ecomm orders)
                    "PIMAINTUSERS", "Y",// Y N or blank (Authorized to maintiain ecomm users)
                    "PIORDEMAILFLG", "Y",// Y N or blank (Send email after order created)
                    "PISHPEMAILFLG", "Y"// Y N or blank (Send email after order shipped)
            );
            val result = pooledConnection.callProgram(programName, params, "PIUSERNAME", "PIAREACODE", "PIPREFIX", "PISUFFIX", "PIEMAIL");

            EditContactResponseDTO editContactResponseDTO = new EditContactResponseDTO();


            String fullName = (String) result.get("PIUSERNAME");
            int endOfFirstNameIndex = fullName.indexOf(" ");
            String firstName = fullName.substring(0, endOfFirstNameIndex);
            String lastName = fullName.substring(endOfFirstNameIndex + 1);

            editContactResponseDTO.setFirstName(firstName);
            editContactResponseDTO.setLastName(lastName);

            String phoneNumber = result.get("PIAREACODE") + "-" +
                    result.get("PIPREFIX") + "-" +
                    result.get("PISUFFIX");

            editContactResponseDTO.setPhoneNumber(phoneNumber);
            editContactResponseDTO.setEmail((String) result.get("PIEMAIL"));
            return editContactResponseDTO;
        }
    }

    /**
     * Sends a request to delete a user in Mincron ERP.
     *
     * @param accountId mincron accountId
     * @param userId    mincron userID
     * @return accountId and userId for recently deleted user
     * @throws JsonProcessingException
     * @throws MincronException
     */
    public DeleteContactResponseDTO deleteContact(String accountId, String userId) {
        try (val pooledConnection = programCallDocumentFactory.getPooledMincronConnection()) {
            val programName = "AIRC110";
            val params = new PCMLParameters(
                    "PIACCOUNTID", accountId,
                    "PIUSERID", userId,
                    "PIUSERNAME", "",
                    "PIPWD", "",
                    "PIEMAIL", "",
                    "PIAREACODE", "",
                    "PIPREFIX", "",
                    "PISUFFIX", "",
                    "PIOPTION", "D",// A = add, D = delete, U = update
                    "PIVIEWAR", "Y",// Y N or blank (Authorized to view A/R)
                    "PICCPROCESS", "Y", // Y N or blank (Authorized to view CC process)
                    "PIBILLPAY", "Y",// Y N or blank (Authorized to view online bill pay)
                    "PICOMPORDER", "Y",// Y N or blank (Authorized to check out ecomm orders)
                    "PIMAINTUSERS", "Y",// Y N or blank (Authorized to maintiain ecomm users)
                    "PIORDEMAILFLG", "Y",// Y N or blank (Send email after order created)
                    "PISHPEMAILFLG", "Y"// Y N or blank (Send email after order shipped)
            );
            val result = pooledConnection.callProgram(programName, params, "PIACCOUNTID", "PIUSERID");

            DeleteContactResponseDTO deleteContactResponseDTO = new DeleteContactResponseDTO();
            deleteContactResponseDTO.setErpAccountId((String) result.get("PIACCOUNTID"));
            deleteContactResponseDTO.setErpUserId((String) result.get("PIUSERID"));

            return deleteContactResponseDTO;
        }
    }

    // This is unused by the application but was added for debug purposes
    public List<ContactResponseDTO> getContacts(String accountId) {
        try (val pooledConnection = programCallDocumentFactory.getPooledMincronConnection()) {

            val programName = "AIRC111";
            val result = pooledConnection.callProgram(programName, new PCMLParameters("PIACCOUNTID", accountId));

            val pcd = (ProgramCallDocument) result.get(PROGRAM_CALL_DOCUMENT);

            // This is kind of a sketchy workaround but works
            // We don't have a count of contacts so try parsing them until we hit the exception or null data
            boolean endOfListReached = false;
            val indices = new int[1];
            var count = 0;
            List<ContactResponseDTO> contactResponseDTOList = new ArrayList<>();
            while (!endOfListReached) {
                try {
                    indices[0] = count;
                    val username = pcd.getStringValue(programName + ".POCONLIST.USERNAME", indices);
                    val useremail = pcd.getStringValue(programName + ".POCONLIST.USEREMAIL", indices);
                    val phone = String.format("%s-%s-%s",
                            pcd.getStringValue(programName + ".POCONLIST.PHAREACODE", indices),
                            pcd.getStringValue(programName + ".POCONLIST.PHPREFIX", indices),
                            pcd.getStringValue(programName + ".POCONLIST.PHSUFFIX", indices)
                    );
                    if (username.equals("")) {
                        endOfListReached = true;
                        continue;
                    }
                    count++;

                    ContactResponseDTO contactResponseDTO = new ContactResponseDTO();

                    String[] names = username.split(" ");
                    contactResponseDTO.setFirstName(names[0]);
                    contactResponseDTO.setLastName(names[names.length - 1]);

                    List<String> emails = new ArrayList<>();
                    emails.add(useremail);
                    contactResponseDTO.setEmail(emails);

                    List<String> phones = new ArrayList<>();
                    phones.add(phone);
                    contactResponseDTO.setTelephone(phones);

                    contactResponseDTOList.add(contactResponseDTO);
                } catch (PcmlException e) {
                    endOfListReached = true;
                }
            }
            return contactResponseDTOList;
        }
    }

    private ErpAccountsDTO getAccounts(String accountId) throws AccountNotFoundException, JsonProcessingException {
        GetAccountResponseDTO billToAccount = getAccount(accountId, false);
        ErpAccountsDTO erpAccountsDTO = new ErpAccountsDTO();

        erpAccountsDTO.setBillTo(new ErpAccountInfoDTO(billToAccount));
        erpAccountsDTO.setShipTo(getJobsForAccount(accountId));

        return erpAccountsDTO;
    }

    private List<ErpAccountInfoDTO> getJobsForAccount(String accountId) throws JsonProcessingException {
        String url = String.format("%s/customerJobList?accountId=%s", mincronHostWebsmart, accountId);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        MincronJobListDTO mincronJobListDTO = new MincronJobListDTO(response.getBody());

        return mincronJobListDTO.getShipTos().stream().map(job -> new ErpAccountInfoDTO(job)).collect(Collectors.toList());
    }

    public void updateCount(String branchNum, String countId, String locationId, String tagNum, int countedQty) {
        try (val pooledConnection = programCallDocumentFactory.getPooledMincronConnection()) {
            val programName = "AIRC311";
            val result = pooledConnection.callProgram(
                    programName,
                    new PCMLParameters("BRANCH#", branchNum, "COUNTID", countId, "LOCATION", locationId, "TAG#", tagNum, "COUNTED_QTY", String.valueOf(countedQty)),
                    "ERROR_CODE"
            );

            val errorCode = (String) result.get("ERROR_CODE");

            if (errorCode != null && errorCode.length() > 0) {
                if ("1".equals(errorCode)) {
                    throw new MincronException("Unable to find item to post count qty for.", HttpStatus.BAD_REQUEST, "1");
                }

                throw new MincronException("Unknown error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    public void addToCount(String branchNum, String countId, String locationId, String prodNum, int countedQty) {
        try (val pooledConnection = programCallDocumentFactory.getPooledMincronConnection()) {
            val programName = "AIRC312";
            val result = pooledConnection.callProgram(
                    programName,
                    new PCMLParameters("BRANCH#", branchNum, "COUNTID", countId, "LOCATION", locationId, "PROD_NUM", prodNum, "PROD_QTY", String.valueOf(countedQty)),
                    "ERROR_CODE"
            );

            val errorCode = (String) result.get("ERROR_CODE");

            if (errorCode != null && errorCode.length() > 0) {
                if ("1".equals(errorCode)) {
                    throw new MincronException("Either invalid branch/count id combination, or no available blank tags.", HttpStatus.BAD_REQUEST, "1");
                }

                throw new MincronException("Unknown error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @SneakyThrows
    public MincronCountsDTO getAllCounts() {
        try (val pooledConnection = programCallDocumentFactory.getPooledMincronConnection()) {
            val programName = "AIRC304";
            val result = pooledConnection.callProgram(
                    programName,
                    new PCMLParameters(),
                    "ERROR_CODE", "NUM_COUNTS"
            );

            val errorCode = (String) result.get("ERROR_CODE");

            if ("1".equals(errorCode)) {
                // No active counts found
                return new MincronCountsDTO(false, Collections.emptyList());
            }

            var moreThan100ActiveCounts = "2".equals(errorCode);

            val counts = new ArrayList<MincronCountSummaryDTO>();
            val numCounts = Integer.parseInt((String) result.get("NUM_COUNTS"));
            val pcd = (ProgramCallDocument) result.get(PROGRAM_CALL_DOCUMENT);
            val indicies = new int[1];
            for (int i = 0; i < numCounts; i++) {
                indicies[0] = i;
                val branchNum = pcd.getStringValue(programName + ".COUNTINFO.BRID", indicies);
                val countId = pcd.getStringValue(programName + ".COUNTINFO.CYCLE", indicies);
                val numItems = pcd.getIntValue(programName + ".COUNTINFO.NUMITEMS", indicies);
                val countDate = pcd.getStringValue(programName + ".COUNTINFO.CNTDATE", indicies);
                val status = pcd.getStringValue(programName + ".COUNTINFO.STATUS", indicies);
                counts.add(new MincronCountSummaryDTO(branchNum, countId, numItems, countDate, status));
            }

            return new MincronCountsDTO(moreThan100ActiveCounts, counts);
        }
    }

    @SneakyThrows
    public ProductSearchResultDTO searchProducts(String branchNum, String query, String lastItem) {
        try (val pooledConnection = programCallDocumentFactory.getPooledMincronConnection()) {
            val programName = "AIRC301";
            val result = pooledConnection.callProgram(
                    programName,
                    new PCMLParameters("SRCH_BRN", branchNum, "SRCH_STRING", query, "LAST_ITEM", lastItem),
                    "ERROR_CODE", "LAST_ITEM"
            );

            val errorCode = (String) result.get("ERROR_CODE");

            if (errorCode != null && errorCode.length() > 0) {
                throw new ProductSearchException(errorCode);
            }

            val newLastItem = (String) result.get("LAST_ITEM");

            val pcd = (ProgramCallDocument) result.get(PROGRAM_CALL_DOCUMENT);

            val itemCount = 10;
            final List<ProductSearchResultItemDTO> items = new ArrayList<>(itemCount);
            val indices = new int[1];
            for (int i = 0; i < itemCount; i++) {
                indices[0] = i;
                val prodNum = pcd.getStringValue(programName + ".RETURNI.PROD#", indices);
                var prodDesc = pcd.getStringValue(programName + ".RETURNI.PRODDESC", indices);
                val uom = pcd.getStringValue(programName + ".RETURNI.UOM", indices);

                val descLine2 = pcd.getStringValue(programName + ".RETURNI.DLINE2", indices);
                val descLine3 = pcd.getStringValue(programName + ".RETURNI.DLINE3", indices);

                if (descLine2 != null && !descLine2.isEmpty()) {
                    prodDesc += "\n" + descLine2;
                }

                if (descLine3 != null && !descLine3.isEmpty()) {
                    prodDesc += "\n" + descLine3;
                }
                items.add(new ProductSearchResultItemDTO(prodNum, prodDesc, uom));
            }

            return new ProductSearchResultDTO(newLastItem, items);
        }
    }
}
