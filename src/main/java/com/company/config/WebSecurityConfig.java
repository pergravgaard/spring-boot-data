package com.company.config;

import com.company.security.CustomAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    private CustomAuthenticationFilter authenticationFilter() {
        return new CustomAuthenticationFilter(authenticationManager);
    }

    // See https://spring.io/guides/topicals/spring-security-architecture
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(authenticationFilter(), BasicAuthenticationFilter.class);
        http.authorizeRequests()
                .antMatchers("/favicon.ico").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable(); // and remember to disable CSRF for post/put requests
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider); // TODO: Necessary?
    }

}
