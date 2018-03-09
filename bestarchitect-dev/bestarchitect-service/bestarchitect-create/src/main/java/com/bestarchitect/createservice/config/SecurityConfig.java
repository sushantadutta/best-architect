package com.bestarchitect.createservice.config;

import com.bestarchitect.createservice.request.processor.RestRequestPipeLineFactory;
import com.bestarchitect.security.filter.RequestValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.csrf.CsrfFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private RestRequestPipeLineFactory restRequestPipelineFactory;

    @Bean
    public RequestValidationFilter requestValidationFilter() {
        return new RequestValidationFilter(restRequestPipelineFactory);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .and()
                .exceptionHandling()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
                .formLogin().disable()
                .logout().disable()
                .csrf().disable();
        http.addFilterBefore(requestValidationFilter(), CsrfFilter.class);

    }
}
