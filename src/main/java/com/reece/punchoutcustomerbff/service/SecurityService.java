package com.reece.punchoutcustomerbff.service;

import com.reece.punchoutcustomerbff.dto.LoginInputDto;
import com.reece.punchoutcustomerbff.dto.LoginResponseDto;
import com.reece.punchoutcustomerbff.models.daos.AuthorizedUserDao;
import com.reece.punchoutcustomerbff.models.repositories.AuthorizedUserRepository;
import com.reece.punchoutcustomerbff.util.B64;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * The purpose of this service is to handle the details needed for
 * security related operations such as login.
 * @author john.valentino
 */
@Service
@Slf4j
public class SecurityService {

  @Autowired
  private AuthorizedUserRepository authorizedUserRepository;

  protected SecurityService instance = this;

  public LoginResponseDto login(LoginInputDto input, AuthenticationManager authManager,
                                HttpSession session) {
    LoginResponseDto login = LoginResponseDto.builder().success(Boolean.FALSE).build();
    log.info("Attempting to login the user by email " + input.getEmail());

    try {
      Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(
              input.getEmail(), input.getPassword()));
      SecurityContextHolder.getContext().setAuthentication(auth);

      login = !session.isNew() ? validateSession(session, input.getEmail(), input.getPassword())
          : getNewSession(session, input.getEmail());

    } catch (SecurityServiceException e) {
      log.error("User by email " + input.getEmail() + " gave invalid credentials", e);
    }

    return login;
  }

  private LoginResponseDto getNewSession(HttpSession session, String email) {
    session.setAttribute("email", email);
    return LoginResponseDto.builder().success(Boolean.TRUE).sessionId(session.getId()).build();
  }

  private LoginResponseDto validateSession(HttpSession session, String email, String password){
    String oldEmail = String.valueOf(session.getAttribute("email"));
    return oldEmail.equals(email) && isValidUser(email, password) != null?
        LoginResponseDto.builder().success(Boolean.TRUE).sessionId(session.getId()).build() :
        LoginResponseDto.builder().success(Boolean.FALSE).build();
  }

  public AuthorizedUserDao isValidUser(String email, String password) {
    log.info("Checking to see if " + email + " is a valid user with its given password...");
    List<AuthorizedUserDao> users = authorizedUserRepository.findByEmail(email);

    if (users.size() == 0) {
      log.info(email + " doesn't have any email matches, so not valid");
      return null;
    }

    AuthorizedUserDao user = users.get(0);
    String expected = Md5Crypt.md5Crypt(password.getBytes(), user.getSalt());

    if (expected.equals(user.getPassword())) {
      log.info("Email " + email + " gave a password that matches the salted MD5 hash");
      return user;
    }

    log.info("Email " + email + " gave a password that doesn't match the salted MD5 hash");
    return null;
  }

  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;

    String authUserId = instance.retrieveCurrentlyLoggedInUserId();
    log.info("Authenticating " + authUserId);

    // if they have not logged in, do so
    if (authUserId == null || authUserId.equals("anonymousUser")) {
      log.info("Not logged in to we have to first login...");
      AuthorizedUserDao user = instance.isValidUser(
          auth.getPrincipal().toString(), auth.getCredentials().toString());
      if (user != null) {
        // Note: In the reference this was get.getId(), so I am not sure if this matters
        return new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
      }

      throw new SecurityServiceException("Invalid username and/or password");
    }

    // they are already logged in
    log.info(authUserId + " is already logged in");
    return authentication;
  }


  public AuthorizedUserDao currentLoggedInUser() {
    String id = this.retrieveCurrentlyLoggedInUserId();
    AuthorizedUserDao user = authorizedUserRepository.findByEmail(id).get(0);
    return user;
  }

  public String retrieveCurrentlyLoggedInUserId() {
    return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
  }

  public String generateSalt() {
    return "$1$" + B64.getRandomSalt(8);
  }

}
