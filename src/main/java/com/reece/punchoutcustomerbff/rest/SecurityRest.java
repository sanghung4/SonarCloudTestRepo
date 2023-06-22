package com.reece.punchoutcustomerbff.rest;

import com.reece.punchoutcustomerbff.dto.LoginInputDto;
import com.reece.punchoutcustomerbff.dto.LoginResponseDto;
import com.reece.punchoutcustomerbff.dto.ResultDto;
import com.reece.punchoutcustomerbff.service.SecurityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controls endpoints used for security related matters, such as login
 * @author john.valentino
 */
@RestController
@Tag(name = "security-rest", description = "the security controller which handles security related operations, such as login")
@Slf4j
public class SecurityRest {

  @Autowired
  private SecurityService securityService;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Operation(summary = "Validates login credentials and returns a token to be used throughout other controller endpoints")
  @ApiResponses(value = { 
    @ApiResponse(responseCode = "200", description = "Login credentials are valid", 
      content = { @Content(mediaType = "application/json", 
        schema = @Schema(implementation = LoginResponseDto.class)) }),
    @ApiResponse(responseCode = "403", description = "Invalid token", 
      content = @Content) })
  @PostMapping("/security/login")
  public LoginResponseDto login(@RequestBody LoginInputDto input, HttpSession session) {
    return securityService.login(input, authenticationManager, session);
  }

  @Operation(summary = "The purpose of this is to test that session authentication is working.")
  @ApiResponses(value = { 
    @ApiResponse(responseCode = "200", description = "ResultDto always returns if you are logged in.", 
      content = { @Content(mediaType = "application/json", 
        schema = @Schema(implementation = ResultDto.class)) }),
    @ApiResponse(responseCode = "403", description = "Invalid token", 
      content = @Content) })
  @GetMapping("/security/verify")
  public ResultDto securedVerification() {
    return new ResultDto();
  }

}
