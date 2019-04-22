package com.company.config;

import com.company.security.CustomAuthenticationProvider;
import com.company.security.CustomSecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider authenticationProvider;

    // See https://spring.io/guides/topicals/spring-security-architecture
    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        http.httpBasic();
        http.addFilterAfter(new CustomSecurityFilter(authenticationProvider), LogoutFilter.class);
        http.authorizeRequests()
                .anyRequest().permitAll() // allow all requests
                .and().csrf().disable(); // and remember to disable CSRF for post/put requests
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);

//        auth.inMemoryAuthentication()
//                .withUser("user").password("password").roles("USER")
//                .and()
//                .withUser("admin").password("password").roles("ADMIN");
    }

}
