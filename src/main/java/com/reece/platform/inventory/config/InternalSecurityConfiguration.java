package com.reece.platform.inventory.config;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Configuration
@Order(1)
public class InternalSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${internal_username}")
    private String internalUsername;

    @Value("${internal_password}")
    private String internalPassword;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
            .withUser(internalUsername)
            .password(passwordEncoder().encode(internalPassword))
            .authorities("ROLE_INTERNAL");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/internal/counts/_delete/**")
            .permitAll()
            .antMatchers("/internal/**")
            .authenticated()
            .and()
            .csrf()
            .disable()
            .httpBasic()
            .authenticationEntryPoint(internalAuthEntryPoint());
    }

    @Bean
    public AuthenticationEntryPoint internalAuthEntryPoint() {
        val entryPoint = new InternalAuthenticationEntryPoint();
        entryPoint.setRealmName("internal realm");
        return entryPoint;
    }

    public static class InternalAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

        @Override
        public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
        ) throws IOException {
            response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            val writer = response.getWriter();
            writer.println("HTTP Status 401 - " + authException.getMessage());
        }
    }
}
