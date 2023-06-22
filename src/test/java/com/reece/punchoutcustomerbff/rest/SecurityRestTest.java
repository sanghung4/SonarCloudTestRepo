package com.reece.punchoutcustomerbff.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.reece.punchoutcustomerbff.dto.LoginInputDto;
import com.reece.punchoutcustomerbff.dto.LoginResponseDto;
import com.reece.punchoutcustomerbff.dto.ResultDto;
import com.reece.punchoutcustomerbff.util.AuthenticationManagerImpl;
import com.reece.punchoutcustomerbff.service.SecurityService;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

@ExtendWith(MockitoExtension.class)
public class SecurityRestTest {

  @Mock
  private SecurityService securityService;

  @Mock
  private AuthenticationManager authenticationManager = new AuthenticationManagerImpl();

  @InjectMocks
  private SecurityRest subject;

  @Mock
  HttpSession httpSession;

  @BeforeEach
  public void before() {
  }

  @Test
  public void testLogin() {
    // given
    LoginInputDto input = new LoginInputDto();

    // and
    when(securityService.login(input, authenticationManager, httpSession)).thenReturn(new LoginResponseDto());

    // when
    LoginResponseDto result = subject.login(input, httpSession);

    // then
    assertThat(result.isSuccess(), equalTo(true));
  }

  @Test
  public void testSecured() {
    ResultDto result = subject.securedVerification();
    assertThat(result.isSuccess(), equalTo(true));
  }

}
