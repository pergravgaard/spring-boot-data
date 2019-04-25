package com.company.security;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class FirstFilter implements Filter {

    private String printUri(ServletRequest req) {
        return ((HttpServletRequest) req).getRequestURI();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("FIRST FILTER BEFORE " + printUri(request));
        chain.doFilter(request, response);
        System.out.println("FIRST FILTER AFTER " + printUri(request));
    }

}
