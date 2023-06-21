package com.reece.platform.products.branches.service;

import com.google.maps.errors.ApiException;
import com.reece.platform.products.branches.exception.BranchNotFoundException;
import com.reece.platform.products.branches.exception.WorkdayException;
import com.reece.platform.products.branches.model.DTO.*;
import com.reece.platform.products.branches.model.entity.Branch;
import com.reece.platform.products.branches.model.repository.BranchesDAO;
import com.reece.platform.products.branches.utilities.BrandDomainMapper;
import com.reece.platform.products.model.DTO.GetTerritoryResponseDTO;
import com.reece.platform.products.model.ErpEnum;
import com.reece.platform.products.pdw.repository.DataWarehouseRepository;
import com.reece.platform.products.service.ErpService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class BranchesService {

    @Value("${branchesETL.overrideEmail:#{null}}")
    private String overrideEmail;

    @Value("${branchesETL.workdayEndpoint}")
    private String workdayEndpoint;

    @Value("${branchesETL.workdayUser}")
    private String workdayUser;

    @Value("${branchesETL.workdayPassword}")
    private String workdayPassword;

    private final DataWarehouseRepository dataWarehouseRepository;
    private final BranchesDAO branchesDAO;
    private final GeocodingService geocodingService;
    private final BrandDomainMapper mapper;
    private final ErpService erpService;
    private final RestTemplate restTemplate;

    @Autowired
    public BranchesService(
        DataWarehouseRepository dataWarehouseRepository,
        BranchesDAO branchesDAO,
        GeocodingService geocodingService,
        BrandDomainMapper mapper,
        ErpService erpService,
        RestTemplateBuilder restTemplateBuilder
    ) {
        this.dataWarehouseRepository = dataWarehouseRepository;
        this.branchesDAO = branchesDAO;
        this.geocodingService = geocodingService;
        this.mapper = mapper;
        this.erpService = erpService;
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * Get Branch by id
     * @param branchId
     * @return
     * @throws BranchNotFoundException
     */
    public BranchResponseDTO getBranch(String branchId) throws BranchNotFoundException {
        List<Branch> branches = branchesDAO.findAllByBranchId(branchId);
        if (branches.isEmpty()) throw new BranchNotFoundException();

        return new BranchResponseDTO(branches.get(0), mapper);
    }

    /**
     * Get Branch by Entity Id
     * @param entityId
     * @return
     * @throws BranchNotFoundException
     */
    public BranchResponseDTO getBranchByEntityId(String entityId , String erpSystemName) throws BranchNotFoundException {
        List<Branch> branches = branchesDAO
                .findAllByEntityId(entityId)
                .orElseThrow(BranchNotFoundException::new);
        Branch branch = branches.stream().filter(a-> a.getErpSystemName().equals(ErpEnum.valueOf(erpSystemName))).findFirst().get();
        return new BranchResponseDTO(branch, mapper);
    }

    /**
     * Get All Branches with no filtering - for engineer role
     * @return
     */
    public List<BranchResponseDTO> getAllBranches() {
        var branches = branchesDAO.findAllBranches();
        return branches.stream().map(branch -> new BranchResponseDTO(branch, mapper)).collect(Collectors.toList());
    }

    /**
     * Get Branches
     * Will fetch branches for store finder if location.isStoreFinder is set
     * if location.isStoreFinder is false, will fetch for shoppable branches
     * @param location
     * @return
     */
    public GeolistResponseDTO getBranches(BranchesRequestDTO location) {
        var latitude = location.getLatitude();
        var longitude = location.getLongitude();
        var territory = location.getTerritory();

        // Get the branches that fall under the user's territory
        List<String> validBranches = getBranchesForTerritory(territory);

        // Query top x results based on location proximity and user's territory
        int count = location.getCount() == 0 ? 10 : location.getCount();
        var branches = branchesDAO.findByLocation(longitude, latitude, count);

        var test = branches
            .stream()
            .filter(b ->
                b.isActiveAndAvailable(location.getIsStoreFinder(), location.getIsShoppable()) &&
                b.isWithinSearchRadius(location.getBranchSearchRadius()) &&
                b.isInTerritory(validBranches)
            )
            .map(branch -> new BranchResponseDTO(branch, mapper))
            .collect(Collectors.toList());

        return new GeolistResponseDTO(latitude, longitude, test);
    }


    /**
     * Get Branch list by proximity to home branch
     * @param branchIds
     * @param homeBranchId
     * @param longitude
     * @param latitude
     * @return
     */
    public BranchProximityResponseDTO getBranchListByProximity(
            List<String> branchIds,
            String homeBranchId,
            Float longitude,
            Float latitude
    ) {
        BranchesRequestDTO userLocation = new BranchesRequestDTO();

        if (latitude == null || longitude == null || latitude == 0 || longitude == 0) {
            // Use home branch as "currentLocation"
            List<Branch> homeBranches = branchesDAO.findAllByBranchId(homeBranchId);

            Branch homeBranch = homeBranches.get(0);

            latitude = homeBranch.getLatitude().floatValue();
            longitude = homeBranch.getLongitude().floatValue();

            userLocation.setLongitude(longitude);
            userLocation.setLatitude(latitude);
        }

        // Current User Long Lat Point String Literal
        String point = String.format("POINT(%s %2s)", longitude, latitude);

        // Find branch details by ID
        List<BranchWithDistanceResponseDTO> branchList = branchesDAO
                .findByBranchIdAndGetDistance(point, branchIds)
                .stream()
                .map(BranchWithDistanceResponseDTO::new)
                .collect(Collectors.toList());

        return new BranchProximityResponseDTO(branchList, userLocation);
    }


    /**
     * Update Branch toggles for supporting EHG branch availability
     * @param id
     * @param updatedBranch
     * @return
     */
    public BranchResponseDTO updateBranch(UUID id, UpdateBranchDTO updatedBranch) throws BranchNotFoundException {
        var branch = branchesDAO.findById(id).orElseThrow(BranchNotFoundException::new);

        if (updatedBranch.getIsActive() != null) {
            branch.setIsActive(updatedBranch.getIsActive());
        }

        if (updatedBranch.getIsAvailableInStoreFinder() != null) {
            branch.setIsAvailableInStoreFinder(updatedBranch.getIsAvailableInStoreFinder());
        }

        if (updatedBranch.getIsShoppable() != null) {
            branch.setIsShoppable(updatedBranch.getIsShoppable());
        }

        if (updatedBranch.getIsPricingOnly() != null) {
            branch.setIsPricingOnly(updatedBranch.getIsPricingOnly());
        }

        // note: have to use native query as the 'distance' column is not available in save() method
        var updateBranch = branchesDAO.updateBranch(
            branch.getIsActive(),
            branch.getIsAvailableInStoreFinder(),
            branch.getIsShoppable(),
            branch.getIsPricingOnly(),
            id
        );
        return new BranchResponseDTO(updateBranch, mapper);
    }

    /**
     * Back job sync for snowflake.
     * NOTE: Should be deprecated with new Workday integration
     */
    public void syncWithSnowflake() {
        // TODO: override branch manager email on local
        log.info("ENTERING branchesService.syncWithSnowflake");
        dataWarehouseRepository.setJDBCQueryResultSetFormat();
        var pdwBranches = dataWarehouseRepository
            .getAllBranches()
            .stream()
            .collect(Collectors.toMap(Branch::getBranchId, Function.identity(), (a, b) -> a));

        log.info("Found {} branches in Snowflake", pdwBranches.size());

        if (!pdwBranches.isEmpty()) {
            var branches = branchesDAO
                .findAllBranches()
                .stream()
                .collect(Collectors.toMap(Branch::getBranchId, Function.identity(), (a, b) -> a));

            for (Branch snowflakeBranch : pdwBranches.values()) {
                var existingBranch = branches.get(snowflakeBranch.getBranchId());

                // Update the branch with the snowflake data
                if (existingBranch != null) {
                    snowflakeBranch.setId(existingBranch.getId());

                    if (existingBranch.getFullAddress().equals(snowflakeBranch.getFullAddress())) {
                        snowflakeBranch.setLocation(existingBranch.getLocation());
                    }
                }

                if(existingBranch != null && existingBranch.getIsActive() != null) {
                    snowflakeBranch.setIsActive(existingBranch.getIsActive());
                } else {
                    snowflakeBranch.setIsActive(false);
                }

                if(existingBranch != null && existingBranch.getIsShoppable() != null) {
                    snowflakeBranch.setIsShoppable(existingBranch.getIsShoppable());
                } else {
                    snowflakeBranch.setIsShoppable(true);
                }

                if(existingBranch != null && existingBranch.getIsPricingOnly() != null) {
                    snowflakeBranch.setIsPricingOnly(existingBranch.getIsPricingOnly());
                } else {
                    snowflakeBranch.setIsPricingOnly(false);
                }

                if(existingBranch != null && existingBranch.getIsAvailableInStoreFinder() != null) {
                    snowflakeBranch.setIsAvailableInStoreFinder(existingBranch.getIsAvailableInStoreFinder());
                } else {
                    snowflakeBranch.setIsAvailableInStoreFinder(false);
                }

                if (overrideEmail != null && !overrideEmail.isBlank()) {
                    snowflakeBranch.setActingBranchManagerEmail(overrideEmail);
                }

                if (snowflakeBranch.getLocation() == null) {
                    try {
                        snowflakeBranch.setLocation(
                            geocodingService.getLocationFromAddress(snowflakeBranch.getFullAddress())
                        );
                    } catch (IOException | InterruptedException | ApiException e) {
                        log.warn(
                            "Unable to fetch coordinates for branch {} with address {}",
                            snowflakeBranch.getBranchId(),
                            snowflakeBranch.getFullAddress(),
                            e
                        );

                        // Default to this location so it doesn't crash the build
                        snowflakeBranch.setLocation(geocodingService.getPointFromLatLng(0.0, 0.0));
                    }
                }
            }

            // MAX-4780 Starts

            //branchesDAO.saveAll(pdwBranches.values());
            Collection<Branch> pdwBranchValues = pdwBranches.values();
            for (Branch branch : pdwBranchValues) {
                if (branchesDAO.findByBranchId(branch.getBranchId()) != null) {
                    branchesDAO.updateBranch(
                            branch.getBranchId(),
                            branch.getEntityId(),
                            branch.getName(),
                            branch.getAddress1(),
                            branch.getAddress2(),
                            branch.getCity(),
                            branch.getState(),
                            branch.getZip(),
                            branch.getPhone(),
                            branch.getLocation(),
                            branch.getErpSystemName().toString(),
                            branch.getWebsite(),
                            branch.getWorkdayId(),
                            branch.getWorkdayLocationName(),
                            branch.getRvp(),
                            branch.getIsPlumbing(),
                            branch.getIsWaterworks(),
                            branch.getIsHvac(),
                            branch.getIsBandK(),
                            branch.getActingBranchManager(),
                            branch.getActingBranchManagerPhone(),
                            branch.getActingBranchManagerEmail(),
                            branch.getBusinessHours(),
                            branch.getIsActive() != null ? branch.getIsActive() : false,
                            branch.getIsAvailableInStoreFinder() != null ? branch.getIsAvailableInStoreFinder() : false,
                            branch.getIsShoppable() != null ? branch.getIsShoppable() : true,
                            branch.getIsPricingOnly() != null ? branch.getIsPricingOnly() : false,
                            branch.getId()
                    );
                } else {
                    branchesDAO.createBranch(
                            branch.getBranchId(),
                            branch.getEntityId(),
                            branch.getName(),
                            branch.getAddress1(),
                            branch.getAddress2(),
                            branch.getCity(),
                            branch.getState(),
                            branch.getZip(),
                            branch.getPhone(),
                            branch.getLocation(),
                            branch.getErpSystemName().toString(),
                            branch.getWebsite(),
                            branch.getWorkdayId(),
                            branch.getWorkdayLocationName(),
                            branch.getRvp(),
                            branch.getIsPlumbing(),
                            branch.getIsWaterworks(),
                            branch.getIsHvac(),
                            branch.getIsBandK(),
                            branch.getActingBranchManager(),
                            branch.getActingBranchManagerPhone(),
                            branch.getActingBranchManagerEmail(),
                            branch.getBusinessHours(),
                            branch.getIsActive() != null ? branch.getIsActive() : false,
                            branch.getIsAvailableInStoreFinder() != null ? branch.getIsAvailableInStoreFinder() : false,
                            branch.getIsShoppable() != null ? branch.getIsShoppable() : true,
                            branch.getIsPricingOnly() != null ? branch.getIsPricingOnly() : false
                    );
                }
            }

            branches
                    .values()
                    .stream()
                    .filter(b -> pdwBranches.get(b.getBranchId()) == null)
                    .forEach(b -> branchesDAO.updateBranchStatusByBranchId(false, b.getBranchId()));

            /*
            // Flag any branches for deletion that aren't in the snowflake view
            branches
                .values()
                .stream()
                .filter(b -> pdwBranches.get(b.getBranchId()) == null)
                .forEach(branchesDAO::delete);
            */

            // MAX-4780 ends
        } else {
            log.warn("No branches found in the Snowflake view");
        }

        log.info("EXITING branchesService.syncWithSnowflake");
    }

    /**
     * Fetch the branches that are included in a territory
     * @param territory
     * @return
     */
    private List<String> getBranchesForTerritory(String territory) {
        if (territory != null && !territory.isEmpty()) {
            try {
                GetTerritoryResponseDTO territoryResponse = erpService.getTerritoryById(territory);
                return territoryResponse.getBranchList();
            } catch (Exception e) {
                log.warn("Unable to fetch territory info for " + territory);
            }
        }

        return Collections.emptyList();
    }

    /**
     * Create headers for workday sync
     * @return
     */
    private HttpHeaders createWorkdayHeaders() {
        return new HttpHeaders() {
            {
                String auth = String.format("%s:%s", workdayUser, workdayPassword);
                byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.US_ASCII));
                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
            }
        };
    }

    /**
     * Checks if given Workday string boolean value is equal to "X"
     *
     * @param workDayString workDay string value to check
     * @return boolean indicating whether given Workday boolean string is true
     */
    private boolean convertWorkDayStringToBool(String workDayString) {
        return workDayString != null && workDayString.equals("X");
    }

    /**
     * Fetches branches from Workday and checks if any new branches need to be added to the MAX branches table.
     *
     * @return list of new branches added from Workday
     */
    public List<WorkdayBranchDTO> refreshWorkdayBranches() throws WorkdayException {
        ResponseEntity<WorkdayBranchResponseDTO> workDayDataResponse = restTemplate.exchange(
            workdayEndpoint,
            HttpMethod.GET,
            new HttpEntity<>(createWorkdayHeaders()),
            WorkdayBranchResponseDTO.class,
            0
        );
        if (workDayDataResponse.getBody() != null && workDayDataResponse.getBody().getBranches() != null) {
            List<WorkdayBranchDTO> rawWorkdayBranches = Arrays.asList(workDayDataResponse.getBody().getBranches());

            if (!rawWorkdayBranches.isEmpty()) {
                List<Branch> branchesToDelete = new ArrayList<>();
                List<Branch> branchesToUpdate = new ArrayList<>();
                List<Branch> branchesToInsert = new ArrayList<>();
                List<Branch> existingBranches = new ArrayList<>(branchesDAO.findAllBranches());

                List<Branch> workdayBranches = rawWorkdayBranches.stream()
                        .map(this::convertWorkdayBranch)
                        .filter(branch -> branch.getState() != null)
                        .collect(Collectors.toList());
                for (Branch workdayBranch : workdayBranches) {
                    List<Branch> existingMatches = existingBranches.stream()
                            .filter(existingBranch -> existingBranch.getBranchId().equals(workdayBranch.getBranchId()) &&
                            existingBranch.getErpSystemName().equals(workdayBranch.getErpSystemName())).collect(Collectors.toList());
                    // Update branch
                    if(existingMatches.size() > 0) {
                        var branchToUpdate = existingMatches.get(0);
                        populateBranchCustomFields(branchToUpdate, workdayBranch); // If branch diff needed, check that here
                        branchesToUpdate.add(workdayBranch);

                        existingBranches.removeIf(existingBranch -> existingBranch.getBranchId().equals(workdayBranch.getBranchId())
                                && existingBranch.getErpSystemName().equals(workdayBranch.getErpSystemName()));
                    } else {
                        // Insert branch
                        populateBranchDefaults(workdayBranch);
                        branchesToInsert.add(workdayBranch);
                        existingBranches.removeIf(existingBranch -> existingBranch.getBranchId().equals(workdayBranch.getBranchId())
                                && existingBranch.getErpSystemName().equals(workdayBranch.getErpSystemName()));
                    }
                }

                // Update branches
                branchesToUpdate.stream().forEach(branch -> saveBranch(branch));

                // Insert branches
                branchesDAO.saveAll(branchesToInsert);

                // Delete branches
                branchesToDelete.addAll(existingBranches);
                branchesToDelete.forEach(branchToDelete -> branchToDelete.setIsActive(false));
                branchesToDelete.stream().forEach(branch -> saveBranch(branch));
                // DAO calls
                return rawWorkdayBranches;
            }
        }
        throw new WorkdayException("Empty or null response from Workday.");
    }

    private Branch convertWorkdayBranch(WorkdayBranchDTO workdayBranchDTO) {
        Branch branch = new Branch();
        branch.setBranchId(workdayBranchDTO.getLocation_Code());
        branch.setIsBandK(convertWorkDayStringToBool(workdayBranchDTO.getB_K()));
        branch.setIsWaterworks(convertWorkDayStringToBool(workdayBranchDTO.getWaterworks()));
        branch.setIsHvac(convertWorkDayStringToBool(workdayBranchDTO.getHVAC()));
        branch.setIsPlumbing(convertWorkDayStringToBool(workdayBranchDTO.getPlumbing()));
        // Workday branch manager values often contain the ID of the user (i.e. "John Doe (12345)")
        // Regex is used to match all non-alphabetical or whitespace characters and replace with an empty string
        var nonAlphaOrSpaceReg = "[^A-Za-z\s]";
        branch.setActingBranchManager(
                workdayBranchDTO.getManager() != null ? workdayBranchDTO.getManager().replaceAll(nonAlphaOrSpaceReg, "") : ""
        );
        if (overrideEmail != null && !overrideEmail.isBlank()) {
            branch.setActingBranchManagerEmail(overrideEmail);
        } else {
            branch.setActingBranchManagerEmail(workdayBranchDTO.getBranch_Manager_Email());
        }
        branch.setActingBranchManagerPhone(workdayBranchDTO.getBranch_Manager_Phone_Number());
        branch.setName(workdayBranchDTO.getBrand() == null ? "" : workdayBranchDTO.getBrand());
        branch.setAddress1(workdayBranchDTO.getAddress_Line_1());
        branch.setAddress2(workdayBranchDTO.getAddress_Line_2());
        branch.setCity(workdayBranchDTO.getCity());
        branch.setZip(workdayBranchDTO.getZip_Code());
        branch.setState(workdayBranchDTO.getState());
        branch.setPhone(workdayBranchDTO.getPhone());
        branch.setBusinessHours(workdayBranchDTO.getLocation_Attributes());
        try {
            branch.setLocation(geocodingService.getLocationFromAddress(workdayBranchDTO.getFullAddress()));
        } catch (Exception e) {
            log.warn(
                    "Unable to fetch coordinates for branch {} with address {}",
                    workdayBranchDTO.getLocation_Code(),
                    workdayBranchDTO.getFullAddress(),
                    e
            );

            // Default to this location so it doesn't crash the build
            branch.setLocation(geocodingService.getPointFromLatLng(0.0, 0.0));
        }
        branch.setErpSystemName(
                branch.getBrand().equals("Fortiline Waterworks") ? ErpEnum.MINCRON : ErpEnum.ECLIPSE
        );
        if(branch.getErpSystemName().equals(ErpEnum.MINCRON)) {
            branch.setEntityId(formatMincronEntityId(branch.getBranchId()));
        }
        return branch;
    }

    // Populate branch defaults
    private Branch populateBranchDefaults(Branch defaultedBranch) {
        defaultedBranch.setIsAvailableInStoreFinder(false);
        defaultedBranch.setIsShoppable(true);
        defaultedBranch.setIsPricingOnly(false);
        defaultedBranch.setIsActive(false);
        if(ErpEnum.MINCRON.equals(defaultedBranch.getErpSystemName())) {
            defaultedBranch.setEntityId(formatMincronEntityId(defaultedBranch.getBranchId()));
        }
        return defaultedBranch;
    }

    private Branch populateBranchCustomFields(Branch existingBranch, Branch workdayBranch) {
        workdayBranch.setId(existingBranch.getId());
        workdayBranch.setIsAvailableInStoreFinder(existingBranch.getIsAvailableInStoreFinder());
        workdayBranch.setIsShoppable(existingBranch.getIsShoppable());
        workdayBranch.setIsPricingOnly(existingBranch.getIsPricingOnly());
        workdayBranch.setIsActive(existingBranch.getIsActive());
        workdayBranch.setEntityId(
                ErpEnum.MINCRON.equals(workdayBranch.getErpSystemName()) ?
                    formatMincronEntityId(workdayBranch.getBranchId())
                    : existingBranch.getEntityId()
                );
        return workdayBranch;
    }

    public void saveBranch(Branch branch) {
        branchesDAO.updateBranch(
                branch.getBranchId(),
                branch.getEntityId(),
                branch.getName(),
                branch.getAddress1(),
                branch.getAddress2(),
                branch.getCity(),
                branch.getState(),
                branch.getZip(),
                branch.getPhone(),
                branch.getLocation(),
                branch.getErpSystemName().toString(),
                branch.getWebsite(),
                branch.getWorkdayId(),
                branch.getWorkdayLocationName(),
                branch.getRvp(),
                branch.getIsPlumbing(),
                branch.getIsWaterworks(),
                branch.getIsHvac(),
                branch.getIsBandK(),
                branch.getActingBranchManager(),
                branch.getActingBranchManagerPhone(),
                branch.getActingBranchManagerEmail(),
                branch.getBusinessHours(),
                branch.getIsActive() != null ? branch.getIsActive() : false,
                branch.getIsAvailableInStoreFinder() !=null ? branch.getIsAvailableInStoreFinder() : false,
                branch.getIsShoppable() != null ? branch.getIsShoppable() : true,
                branch.getIsPricingOnly() != null ? branch.getIsPricingOnly() : false,
                branch.getId()
        );
    }

    // Using static entityId mapping that is only valid for Mincron
    //  This value previously came from Snowflake, their source mapping is unknown
    private static String formatMincronEntityId(String branchId) {
        if(branchId == null) {
            return null;
        }
        return branchId.replaceAll("^60*", "");
    }
}
