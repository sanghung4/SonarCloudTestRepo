package com.reece.punchoutcustomerbff.rest;

import com.reece.punchoutcustomerbff.dto.ResultDto;
import com.reece.punchoutcustomerbff.dto.UploadInputDto;
import com.reece.punchoutcustomerbff.dto.UploadOutputDto;
import com.reece.punchoutcustomerbff.service.UploadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints for dealing with uploading.
 * @author john.valentino
 */
@RestController
@Tag(name = "upload-rest", description = "the upload controller which handles operations related to uploads")
@Slf4j
public class UploadRest {

  @Autowired
  private UploadService uploadService;

  @Operation(summary = "Stores the content of a CSV file that can be later used to create a new catalog.")
  @ApiResponses(value = { 
    @ApiResponse(responseCode = "200", description = "Returns the parsed content of the CSV with associated products when available.", 
      content = { @Content(mediaType = "application/json", 
        schema = @Schema(implementation = UploadOutputDto.class)) }),
    @ApiResponse(responseCode = "403", description = "Invalid token", 
      content = @Content) })
  @PostMapping("/upload/{customerId}")
  public UploadOutputDto upload(@RequestBody UploadInputDto input,
                                @PathVariable(value="customerId") String customerId) {
    return uploadService.upload(input, customerId);
  }

  @Operation(summary = "Deletes the upload record of the given ID.")
  @ApiResponses(value = { 
    @ApiResponse(responseCode = "200", description = "Upload successfully deleted.", 
      content = { @Content(mediaType = "application/json", 
        schema = @Schema(implementation = ResultDto.class)) }),
    @ApiResponse(responseCode = "403", description = "Invalid token", 
      content = @Content) })
  @DeleteMapping("/upload/{uploadId}")
  public ResultDto delete(@PathVariable(value="uploadId") String uploadId) {
    uploadService.deleteUpload(uploadId);
    return new ResultDto();
  }

}
