package com.aris.server;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AdminURLForwardFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        try {
            System.out.println("Do filter called");
            System.out.println("Request = " + ((HttpServletRequest) servletRequest).getRequestURI());
            if (((HttpServletRequest) servletRequest).getRequestURI().equals("/umc/redirect")) {
                System.out.println("Servlet request for redirect called");
                RequestDispatcher dispatcher = servletRequest.getRequestDispatcher("redirect.html");
                dispatcher.forward(servletRequest, servletResponse);
            }
        } finally{
            if (filterChain != null) {
                System.out.println("Finally block\n request = " + ((HttpServletRequest) servletRequest).getRequestURI());
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
