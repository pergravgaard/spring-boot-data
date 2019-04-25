package com.company.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFilter implements Filter {

    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) req;
        System.out.println("CUSTOM AUTH FILTER");
        HttpServletResponse response = (HttpServletResponse) resp;
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("PGR", null));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(req, response);
        }
        catch (AuthenticationException ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED); // send 401
        }
    }

}
