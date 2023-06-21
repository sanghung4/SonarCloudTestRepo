package com.reece.platform.eclipse.service.EclipseService;

import com.reece.platform.eclipse.model.DTO.EclipseRestSessionDTO;
import com.reece.platform.eclipse.service.EclipseService.EclipseService;
import com.reece.platform.eclipse.service.EclipseService.EclipseSessionService;
import com.reece.platform.eclipse.testConstants.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.RequestMatcher;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

public class EclipseSessionServiceTest {
    private MockRestServiceServer mockServer;

    @InjectMocks
    private EclipseSessionService eclipseSessionService;

    @MockBean(name = "xml")
    private RestTemplate restTemplateXML;

    @MockBean(name = "json")
    private RestTemplate restTemplateJSON;

    class XMLRequestMatcher implements RequestMatcher {
        private final String element;

        public XMLRequestMatcher(String element){
            this.element = element;
        }

        @Override
        public void match(ClientHttpRequest clientHttpRequest) throws IOException, AssertionError {
            String body = clientHttpRequest.getBody().toString();
            assertTrue(body.contains(element));
        }
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        restTemplateXML = mock(RestTemplate.class);
        restTemplateJSON = mock(RestTemplate.class);
        RestTemplate rt = new RestTemplate();
        eclipseSessionService = new EclipseSessionService(restTemplateXML, rt);
        mockServer = MockRestServiceServer.bindTo(rt).ignoreExpectOrder(true).build();
        ReflectionTestUtils.setField(eclipseSessionService, "eclipseApiEndpoint", "http://ewitest.morsco.com");
        ReflectionTestUtils.setField(eclipseSessionService, "eclipseEndpoint", "http://ewitest.morsco.com");
        ReflectionTestUtils.setField(eclipseSessionService, "loginId", "bob");
        ReflectionTestUtils.setField(eclipseSessionService, "password", "bobebob");
    }

    @Test
    public void getSessionToken_success() throws URISyntaxException {
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://ewitest.morsco.com/Sessions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(TestConstants.ValidSessionToken())
                );

        Optional<EclipseRestSessionDTO> response = eclipseSessionService.getSessionToken();
        assert(response.isPresent());
        assertEquals("abcdefg", response.get().getSessionToken());
    }
}
