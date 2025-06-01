package com.gobookee.common.filter;

import com.gobookee.common.PasswordEncoder;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@WebFilter(servletNames = {"userLoginServlet", "userInsertServlet", "updatePwdHandlerServlet", "updateProfileHandlerServlet"})
public class PasswordEncoderFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        PasswordEncoder pe = new PasswordEncoder((HttpServletRequest) request);
        chain.doFilter(pe, response);
    }
}
