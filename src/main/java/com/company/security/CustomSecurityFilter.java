package com.company.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.security.Principal;

@Deprecated
public class CustomSecurityFilter extends GenericFilterBean {

    private final AuthenticationProvider authenticationProvider;

    public CustomSecurityFilter(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    protected void initFilterBean() throws ServletException {
        System.out.println("INIT FILTER BEAN");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // Could also just set the username and password as request parameters and leave the rest to the UsernamePasswordAuthenticationFilter - see UsernamePasswordAuthenticationFilter.class
        // Then we do not need the request wrapper class either

        Authentication authentication = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken("Gravgaard", "password"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(new CustomHttpServletRequestWrapper((HttpServletRequest) request, authentication), response);
    }

    public static class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {

        private final Authentication authentication;
        /**
         * Constructs a request object wrapping the given request.
         *
         * @param request The request to wrap
         * @throws IllegalArgumentException if the request is null
         */
        public CustomHttpServletRequestWrapper(HttpServletRequest request, Authentication authentication) {
            super(request);
            this.authentication = authentication;
        }

        @Override
        public Principal getUserPrincipal() {
            if (authentication != null && authentication.getPrincipal() instanceof Principal) {
                return (Principal) authentication.getPrincipal();
            }
            return super.getUserPrincipal();
        }

    }

}
