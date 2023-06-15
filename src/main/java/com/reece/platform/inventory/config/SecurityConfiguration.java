package com.reece.platform.inventory.config;

import com.okta.spring.boot.oauth.Okta;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
@Order(2)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${auth.okta.disabled:false}")
    private Boolean isOktaDisabled;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (isOktaDisabled) {
            http.authorizeRequests().antMatchers("/**").permitAll().and().csrf().disable();
        } else {
            http
                .exceptionHandling()
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/actuator/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt();

            Okta.configureResourceServer401ResponseBody(http);
        }
    }
}
