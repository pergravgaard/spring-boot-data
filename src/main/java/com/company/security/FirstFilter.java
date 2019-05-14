package com.company.security;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

public class FirstFilter implements Filter {

    private String printUri(ServletRequest req) {
        return ((HttpServletRequest) req).getRequestURI();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("FIRST FILTER BEFORE " + printUri(request));
//        printRequestHeaders(request);
        chain.doFilter(request, response);
        System.out.println("FIRST FILTER AFTER " + printUri(request));
    }

    private void printRequestHeaders(ServletRequest req) {
        HttpServletRequest request = ((HttpServletRequest) req);
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            System.out.println(headerName + ": " + headerValue);
        }
    }

}
