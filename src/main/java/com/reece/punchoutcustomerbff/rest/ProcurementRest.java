package com.reece.punchoutcustomerbff.rest;

import com.reece.punchoutcustomerbff.dto.ProcurementSystemListDto;
import com.reece.punchoutcustomerbff.service.ProcurementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * General endpoint related to Procurement.
 * @author john.valentino
 */
@RestController
@Tag(name = "procurement-rest", description = "the procurement controller which handles operations to procurement systems")
@Slf4j
public class ProcurementRest {

  @Autowired
  private ProcurementService procurementService;
  
  @Operation(summary = "Get a list of all procurement systems")
  @ApiResponses(value = { 
    @ApiResponse(responseCode = "200", description = "Procurement system list successfully retrieved", 
      content = { @Content(mediaType = "application/json", 
        schema = @Schema(implementation = ProcurementSystemListDto.class)) }),
    @ApiResponse(responseCode = "403", description = "Invalid token", 
      content = @Content) })
  @GetMapping("/procurement-system/list")
  public ProcurementSystemListDto list() {
    return procurementService.retrieve();
  }

}
