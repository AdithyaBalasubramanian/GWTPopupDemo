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
            if (((HttpServletRequest) servletRequest).getRequestURI().equals("/umc/redirect")) {
                RequestDispatcher dispatcher = servletRequest.getRequestDispatcher("redirect.html");
                dispatcher.forward(servletRequest, servletResponse);
            }
        } finally{
            if (filterChain != null) {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
