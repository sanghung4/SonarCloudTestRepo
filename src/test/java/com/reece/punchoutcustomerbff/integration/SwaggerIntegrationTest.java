package com.reece.punchoutcustomerbff.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;

import javax.el.MethodNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.reece.punchoutcustomerbff.util.BaseIntegrationTest;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;

@EnableAutoConfiguration(exclude = { LiquibaseAutoConfiguration.class })
@SpringBootTest
public class SwaggerIntegrationTest extends BaseIntegrationTest {
	private static final String URL_API_DOCS = "/v3/api-docs.yaml";

    @Test
    public void validSwaggerDocumentation() throws Exception {
		this.mockAdminLoggedIn();

        MvcResult rs = mockMvc.perform(MockMvcRequestBuilders.get(URL_API_DOCS)
				.header("Origin", "*").header("X-Auth-Token",
						SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.contentType("application/vnd.oai.openapi;charset=UTF-8")).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        
        String swaggerContents = rs.getResponse().getContentAsString(Charset.forName("UTF-8"));
        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true); // implicit
        parseOptions.setResolveFully(true);
        SwaggerParseResult swaggerResults = new OpenAPIV3Parser().readContents(swaggerContents, null, parseOptions);

        ArrayList<String> restControllerNames = new ArrayList<String>();
        swaggerResults.getOpenAPI().getPaths().forEach((endpointName, endpoint) -> {
                Operation endpointOperation;
                try {
                    endpointOperation = getEndpointRestControllerOperation(endpoint);
                    String restControllerName = endpointOperation.getTags().get(0);
                    if (!restControllerNames.contains(restControllerName)) {
                        restControllerNames.add(restControllerName);
                    }
                    Assertions.assertNotNull(endpointOperation.getSummary(), MessageFormat.format("The endpoint {0} does not contain an \"@Operation\" summary annotation", endpointName));

                    // TODO: Check for nullability on request/response schemas.
                } catch (Exception e) {
                    Assertions.assertNull(e, e.getMessage());
                }
            }
        );

        assertEquals(restControllerNames.size(), swaggerResults.getOpenAPI().getTags().size(), "All rest controllers must contain a \"@Tag\" summary");
        
    }

    private Operation getEndpointRestControllerOperation(PathItem endpoint) throws MethodNotFoundException {
        if (endpoint.getGet() != null) {
            return endpoint.getGet();
        }
        else if (endpoint.getPost() != null) {
            return endpoint.getPost();
        }
        else if (endpoint.getDelete() != null) {
            return endpoint.getDelete();
        }
        else if (endpoint.getPatch() != null) {
            return endpoint.getPatch();
        }
        else if (endpoint.getPut() != null) {
            return endpoint.getPut();
        }
        throw new MethodNotFoundException(MessageFormat.format("Unable to find HTTP Method for endpoint {0}.", endpoint.getDescription()));
    }
}
