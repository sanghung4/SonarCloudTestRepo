package com.reece.platform.inventory.filter;

import com.reece.platform.inventory.service.SessionIdHolder;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class EclipseCredentialsFilter implements Filter {

    @Autowired
    private SessionIdHolder sessionIdHolder;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        val sessionId = ((HttpServletRequest) request).getHeader("X-Eclipse-Credentials");
        sessionIdHolder.setEclipseCredentials(sessionId);
        chain.doFilter(request, response);
        sessionIdHolder.setEclipseCredentials(null);
    }
}
