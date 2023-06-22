package com.reece.punchoutcustomerbff.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.reece.punchoutcustomerbff.dto.LoginInputDto;
import com.reece.punchoutcustomerbff.dto.LoginResponseDto;
import com.reece.punchoutcustomerbff.models.daos.AuthorizedUserDao;
import com.reece.punchoutcustomerbff.models.repositories.AuthorizedUserRepository;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Md5Crypt;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class SecurityServiceTest {

    @Mock
    private AuthorizedUserRepository authorizedUserRepository;

    @Mock
    private SecurityService instance;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpSession session;

    @InjectMocks
    private SecurityService subject;

    private MockedStatic<SecurityContextHolder> mockSecurityContextHolder;

    @BeforeEach
    public void before() {
        mockSecurityContextHolder = mockStatic(SecurityContextHolder.class);
    }

    @AfterEach
    public void after() {
        mockSecurityContextHolder.close();
    }

    // This is only used to generate credentials to be inserted into the database
    @Test
    public void testGenerateCreds() {
        String randomSalt = subject.generateSalt();
        log.info("Salt: " + randomSalt);

        String plaintextPassword = "bravo";

        String result = Md5Crypt.md5Crypt(plaintextPassword.getBytes(), randomSalt);

        log.info("Password: " + result);
    }

    @Test
    public void testLoginSuccess() {
        // given: credentials
        LoginInputDto input = new LoginInputDto();
        input.setEmail("alpha");
        input.setPassword("bravo");

        // and: we mock the call to the auth manager
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("alpha", "bravo");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(token)).thenReturn(authentication);

        // and: mock the session ID
        when(session.getId()).thenReturn("charlie");

        // and: mock the spring session
        SecurityContext securityContext = mock(SecurityContext.class);
        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(session.isNew()).thenReturn(true);

        // when: we login
        LoginResponseDto result = subject.login(input, authenticationManager, session);

        // then: login is successful
        assertThat(result.isSuccess(), equalTo(true));
        assertThat(result.getSessionId(), equalTo("charlie"));
        Mockito.verify(securityContext).setAuthentication(authentication);
    }

    @Test
    public void testLoginSuccessWithSessionStartedWithSameEmailAndPass() {
        // given: credentials
        LoginInputDto input = new LoginInputDto();
        input.setEmail("alpha");
        input.setPassword("bravo");
        List<AuthorizedUserDao> users = List.of(
            AuthorizedUserDao
                .builder()
                .email("alpha")
                .password("$1$eUyPmeLf$roagFNp6VhwGMMa39CCpK.")
                .salt("$1$eUyPmeLf")
                .build()
        );

        // and: we mock the call to the auth manager
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("alpha", "bravo");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(token)).thenReturn(authentication);

        // and: mock the session ID
        when(session.getId()).thenReturn("charlie");

        // and: mock the spring session
        SecurityContext securityContext = mock(SecurityContext.class);
        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(session.isNew()).thenReturn(false);
        when(session.getAttribute("email")).thenReturn("alpha");
        when(authorizedUserRepository.findByEmail("alpha")).thenReturn(users);

        // when: we login
        LoginResponseDto result = subject.login(input, authenticationManager, session);

        // then: login is successful
        assertThat(result.isSuccess(), equalTo(true));
        assertThat(result.getSessionId(), equalTo("charlie"));
        Mockito.verify(securityContext).setAuthentication(authentication);
    }

    @Test
    public void testLoginSuccessWithSessionStartedWithSameEmailAndDifferentPass() {
        // given: credentials
        LoginInputDto input = new LoginInputDto();
        input.setEmail("alpha");
        input.setPassword("bravo");
        List<AuthorizedUserDao> users = List.of(
            AuthorizedUserDao.builder().email("alpha").password("$1$eUyPmeLf$roagFNp6VhwG.").salt("$1$eUyPmeLf").build()
        );

        // and: we mock the call to the auth manager
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("alpha", "bravo");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(token)).thenReturn(authentication);

        // and: mock the spring session
        SecurityContext securityContext = mock(SecurityContext.class);
        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(session.isNew()).thenReturn(false);
        when(session.getAttribute("email")).thenReturn("alpha");
        when(authorizedUserRepository.findByEmail("alpha")).thenReturn(users);

        // when: we login
        LoginResponseDto result = subject.login(input, authenticationManager, session);

        // then: login is successful
        assertThat(result.isSuccess(), equalTo(false));
        Mockito.verify(securityContext).setAuthentication(authentication);
    }

    @Test
    public void testLoginSuccessWithSessionStartedWithDifferentEmailAndSamePass() {
        // given: credentials
        LoginInputDto input = new LoginInputDto();
        input.setEmail("alpha");
        input.setPassword("bravo");

        // and: we mock the call to the auth manager
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("alpha", "bravo");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(token)).thenReturn(authentication);

        // and: mock the spring session
        SecurityContext securityContext = mock(SecurityContext.class);
        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(session.isNew()).thenReturn(false);
        when(session.getAttribute("email")).thenReturn("beta");

        // when: we login
        LoginResponseDto result = subject.login(input, authenticationManager, session);

        // then: login is successful
        assertThat(result.isSuccess(), equalTo(false));
        Mockito.verify(securityContext).setAuthentication(authentication);
    }

    @Test
    public void testLoginFailure() {
        // given: credentials
        LoginInputDto input = new LoginInputDto();
        input.setEmail("alpha");
        input.setPassword("bravo");

        // and: we mock the call to the auth manager
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("alpha", "bravo");
        when(authenticationManager.authenticate(token)).thenThrow(new SecurityServiceException("oops"));

        // when: we login
        LoginResponseDto result = subject.login(input, authenticationManager, session);

        // then: login is successful
        assertThat(result.isSuccess(), equalTo(false));
        assertThat(result.getSessionId(), equalTo(null));
    }

    @Test
    void testIsValidUserWhenNoUsers() {
        // given: credentials
        String email = "alpha";
        String password = "bravo";

        // and: an empty list is returned
        List<AuthorizedUserDao> users = new ArrayList<>();
        when(authorizedUserRepository.findByEmail(email)).thenReturn(users);

        // when:
        AuthorizedUserDao result = subject.isValidUser(email, password);

        // then: the expected result is null
        assertThat(result, equalTo(null));
    }

    @Test
    void testIsValidUserWhenSuccess() {
        // given: credentials
        String email = "alpha";
        String password = "bravo";

        // and: A user is returned
        AuthorizedUserDao user = new AuthorizedUserDao();
        user.setEmail("alpha");
        user.setSalt("$1$eUyPmeLf");
        user.setPassword("$1$eUyPmeLf$roagFNp6VhwGMMa39CCpK.");

        List<AuthorizedUserDao> users = new ArrayList<>();
        users.add(user);

        when(authorizedUserRepository.findByEmail(email)).thenReturn(users);

        // when:
        AuthorizedUserDao result = subject.isValidUser(email, password);

        // then:
        assertThat(result.getEmail(), equalTo("alpha"));
    }

    @Test
    void testIsValidUserWhenPasswordDoesNotMatch() {
        // given: credentials
        String email = "alpha";
        String password = "bad";

        // and: A user is returned
        AuthorizedUserDao user = new AuthorizedUserDao();
        user.setEmail("alpha");
        user.setSalt("$1$eUyPmeLf");
        user.setPassword("$1$eUyPmeLf$roagFNp6VhwGMMa39CCpK.");

        List<AuthorizedUserDao> users = new ArrayList<>();
        users.add(user);

        when(authorizedUserRepository.findByEmail(email)).thenReturn(users);

        // when:
        AuthorizedUserDao result = subject.isValidUser(email, password);

        // then:
        assertThat(result, equalTo(null));
    }

    @Test
    void testAuthenticateWhenAnonButAlreadyLoggedIn() {
        // given:
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("alpha", "bravo");

        // and:
        String authUserId = "anonymousUser";
        when(instance.retrieveCurrentlyLoggedInUserId()).thenReturn(authUserId);

        // and:
        AuthorizedUserDao user = new AuthorizedUserDao();
        user.setEmail("charlie");
        user.setPassword("delta");
        when(instance.isValidUser("alpha", "bravo")).thenReturn(user);

        // when:
        Authentication result = subject.authenticate(authentication);

        // then:
        assertThat(result.getPrincipal(), equalTo("charlie"));
        assertThat(result.getCredentials().toString(), equalTo("delta"));
    }

    @Test
    void testAuthenticateWhenAnonButNotAlreadyLoggedIn() {
        // given:
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("alpha", "bravo");

        // and:
        String authUserId = "anonymousUser";
        when(instance.retrieveCurrentlyLoggedInUserId()).thenReturn(authUserId);

        // and:
        AuthorizedUserDao user = null;
        when(instance.isValidUser("alpha", "bravo")).thenReturn(user);

        // when:
        String message = null;
        try {
            subject.authenticate(authentication);
        } catch (SecurityServiceException e) {
            message = e.getMessage();
        }

        // then
        assertThat(message, equalTo("Invalid username and/or password"));
    }

    @Test
    void testAuthenticateWhenNullButNotAlreadyLoggedIn() {
        // given:
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("alpha", "bravo");

        // and:
        String authUserId = null;
        when(instance.retrieveCurrentlyLoggedInUserId()).thenReturn(authUserId);

        // and:
        AuthorizedUserDao user = null;
        when(instance.isValidUser("alpha", "bravo")).thenReturn(user);

        // when:
        String message = null;
        try {
            subject.authenticate(authentication);
        } catch (SecurityServiceException e) {
            message = e.getMessage();
        }

        // then
        assertThat(message, equalTo("Invalid username and/or password"));
    }

    @Test
    void testAuthenticateWhenAlreadyLoggedIn() {
        // given:
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("alpha", "bravo");

        String authUserId = "123";
        when(instance.retrieveCurrentlyLoggedInUserId()).thenReturn(authUserId);

        // when:
        Authentication result = subject.authenticate(authentication);

        // then:
        assertThat(result.getPrincipal(), equalTo("alpha"));
        assertThat(result.getCredentials().toString(), equalTo("bravo"));
    }
}
