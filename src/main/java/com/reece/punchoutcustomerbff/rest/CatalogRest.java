package com.reece.punchoutcustomerbff.rest;

import com.reece.punchoutcustomerbff.dto.*;
import com.reece.punchoutcustomerbff.service.CatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints for dealing with the catalog.
 * @author john.valentino
 */
@RestController
@Tag(name = "catalog-rest", description = "the catalog controller which handles operations to catalogs")
@Slf4j
public class CatalogRest {

    @Autowired
    private CatalogService catalogService;

    @Operation(summary = "Rename a catalog by its id")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Catalog successfully renamed",
                content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResultDto.class)),
                }
            ),
            @ApiResponse(responseCode = "403", description = "Invalid token", content = @Content),
        }
    )
    @PostMapping("/catalog/{catalogId}/rename")
    public ResultDto renameCatalog(
        @PathVariable(value = "catalogId") String catalogId,
        @RequestBody RenameCatalogDto input
    ) {
        catalogService.renameCatalog(catalogId, input);
        return new ResultDto();
    }

    @Operation(summary = "View a catalog in detail by its id")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Catalog successfully retrieved",
                content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CatalogViewDto.class)),
                }
            ),
            @ApiResponse(responseCode = "403", description = "Invalid token", content = @Content),
        }
    )
    @GetMapping("/catalog/view/{catalogId}")
    public CatalogViewDto viewCatalog(
        @PathVariable(value = "catalogId") String catalogId,
        @RequestParam int page,
        @RequestParam int perPage
    ) {
        return catalogService.listCatalogProducts(catalogId, page, perPage);
    }

    @Operation(summary = "View a catalog/upload products in detail by their id/ids")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Catalog successfully retrieved",
                content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CatalogViewDto.class)),
                }
            ),
            @ApiResponse(responseCode = "403", description = "Invalid token", content = @Content),
        }
    )
    @PostMapping("/catalog/view")
    public CatalogViewDto viewCatalogUploads(
        @RequestParam(required = false, value = "catalogId") Optional<String> catalogId,
        @RequestParam int page,
        @RequestParam int perPage,
        @RequestBody List<UUID> uploadIds
    ) {
        return catalogService.listCatalogAndUploadProducts(page, perPage, uploadIds, catalogId);
    }

    @Operation(summary = "Create a new catalog for a given customer by their id")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Catalog successfully created",
                content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CatalogViewDto.class)),
                }
            ),
            @ApiResponse(responseCode = "403", description = "Invalid token", content = @Content),
        }
    )
    @PostMapping("/catalog/new/{customerId}")
    public CatalogViewDto saveCatalog(
        @PathVariable(value = "customerId") String customerId,
        @RequestBody NewCatalogInputDto input
    ) {
        return catalogService.saveNewCatalog(customerId, input);
    }
}
