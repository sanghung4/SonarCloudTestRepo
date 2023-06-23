package com.reece.punchoutcustomersync.configurations;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Used for requiring a valid X-Auth-Token for every request.
 * @author john.valentino
 */
@Service
@Configurable
@Slf4j
public class SecurityFilter extends GenericFilterBean {

  @Value("${management.apikey}")
  private String apikey;

  @Value("${management.securePath}")
  private String securePath;

  @Override
  public void doFilter(
      ServletRequest request,
      ServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    // pull the token out of the header
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String token = httpRequest.getHeader("X-Auth-Token");
    String pathInfo = httpRequest.getRequestURI();

    if (!pathInfo.startsWith(securePath)) {
      log.info(pathInfo + " is not secured");
      chain.doFilter(request, response);
      return;
    }

    if (token == null || !token.equals(apikey)) {
      log.info(token + " token does not match the api key for " + pathInfo);
      HttpServletResponse res = (HttpServletResponse) response;
      res.setStatus(401);
      return;
    }

    chain.doFilter(request, response);
  }


}
