package com.reece.punchoutcustomersync.rest;

import com.reece.punchoutcustomerbff.dto.CatalogDto;
import com.reece.punchoutcustomerbff.dto.ListCustomersDto;
import com.reece.punchoutcustomerbff.dto.ResultDto;
import com.reece.punchoutcustomersync.dto.AuditInputDto;
import com.reece.punchoutcustomersync.dto.AuditOutputDto;
import com.reece.punchoutcustomersync.service.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The purpose of this class is to contain the endpoints related to sync operations.
 * @author john.valentino
 */
@Tag(name = "sync-rest", description = "the sync controller which handles operations to syncing customer and catalog data")
@RestController
@Slf4j
public class SyncRest {

  @Autowired
  private CustomerService customerService;

  @Autowired
  private CatalogService catalogService;

  @Autowired
  private AuditService auditService;

  @Autowired
  private RefreshService refreshService;

  @Autowired
  private SyncService syncService;

  /**
   * Endpoint for returning all customers that have eligible catalogs, with
   * those single eligible catalogs per customer.
   * @return ListCustomersDto List of eligible customers for sync.
   */
  @Operation(deprecated = true, summary = "Returns all customers with eligible catalogs, with a single eligible catalog per customer.")
  @ApiResponses(value = { 
    @ApiResponse(responseCode = "200", description = "List of sync-eligble customers successfully retrieved.", 
      content = { @Content(mediaType = "application/json", 
        schema = @Schema(implementation = ListCustomersDto.class)) }),
    @ApiResponse(responseCode = "403", description = "Invalid API token", 
      content = @Content) })
  @GetMapping("/sync/customers")
  public ListCustomersDto retrieveCustomers() {
    return customerService.retrieveCustomersWithEligibleCatalogs();
  }

  @Operation(deprecated = true, summary = "Returns a catalog by a given id with all of the catalogs associated products")
  @ApiResponses(value = { 
    @ApiResponse(responseCode = "200", description = "List of catalogs with product mappings successfully retrieved.", 
      content = { @Content(mediaType = "application/json", 
        schema = @Schema(implementation = CatalogDto.class)) }),
    @ApiResponse(responseCode = "403", description = "Invalid API token", 
      content = @Content) })
  @GetMapping("/sync/catalog/{catalogId}")
  public CatalogDto retrieveCatalogWithMappings(@PathVariable(value="catalogId") String catalogId) {
    return catalogService.retrieveCatalogWithMappings(catalogId);
  }

  @Operation(deprecated = true, summary = "Creates sync audit and log records with the corresponding file data")
  @ApiResponses(value = { 
    @ApiResponse(responseCode = "200", description = "Audit data successfully created", 
      content = { @Content(mediaType = "application/json", 
        schema = @Schema(implementation = AuditOutputDto.class)) }),
    @ApiResponse(responseCode = "403", description = "Invalid API token", 
      content = @Content) })
  @PostMapping("/sync/audit")
  public AuditOutputDto audit(@RequestBody AuditInputDto input) {
    return auditService.audit(input);
  }

  @Operation(summary = "Refreshes test data in the database")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Test data refreshed",
          content = { @Content(mediaType = "application/json",
              schema = @Schema(implementation = ResultDto.class)) }),
      @ApiResponse(responseCode = "403", description = "Invalid API token",
          content = @Content) })
  @PostMapping("/sync/refresh-test")
  public ResultDto refreshTestData() {
    return refreshService.refresh();
  }

  @Operation(summary = "Executes the sync to sync products with data from Kourier and produces a CSV to Greenwing")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Sync executed successfully",
                  content = { @Content(mediaType = "application/json",
                          schema = @Schema(implementation = ResultDto.class)) }) })
  @PostMapping("/product-sync")
  public ResultDto sync() {
    syncService.syncCatalogProducts();
    return new ResultDto();
  }

}
