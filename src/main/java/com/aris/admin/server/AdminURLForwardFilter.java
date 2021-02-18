package com.aris.admin.server;

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
        boolean continueFilterChain = true;
        try {
            System.out.println("Do filter called");
            System.out.println("Request = " + ((HttpServletRequest) servletRequest).getRequestURI());
            if (((HttpServletRequest) servletRequest).getRequestURI().contains("/umc/userinfo")) {
                System.out.println("Servlet request for user info called");
                RequestDispatcher dispatcher = servletRequest.getRequestDispatcher("/userinfo.html");
                dispatcher.forward(servletRequest, servletResponse);
                System.out.println("dispatcher forwarded");
                continueFilterChain = false;
            }
        } finally{
            if (filterChain != null && continueFilterChain) {
                System.out.println("Finally block\n request = " + ((HttpServletRequest) servletRequest).getRequestURI());
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
