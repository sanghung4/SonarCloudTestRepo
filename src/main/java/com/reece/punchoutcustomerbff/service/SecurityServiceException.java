package com.reece.punchoutcustomerbff.service;

import org.springframework.security.core.AuthenticationException;

/**
 * General security exception
 * @author john.valentino
 */
public class SecurityServiceException extends AuthenticationException {

  public SecurityServiceException(String message) {
    super(message);
  }

}
