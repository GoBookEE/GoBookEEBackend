package com.gobookee.common.filter;

import com.gobookee.common.MessageRedirectTemplate;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();

        // 로그인 없이 허용되는 경로
        boolean isPublic = uri.equals(contextPath + "/") || uri.startsWith(contextPath + "/login") || uri.startsWith(contextPath + "/signup") ||
                uri.startsWith(contextPath + "/resources/") || uri.startsWith(contextPath + "/find") || uri.startsWith(contextPath + "/ajax") || uri.startsWith(contextPath + "/update")
                || uri.startsWith(contextPath + "/logout") || uri.startsWith(contextPath + "/sendemail") || uri.startsWith(contextPath + "/verifycode");


        boolean loggedIn = session != null && session.getAttribute("loginUser") != null;

        if (loggedIn || isPublic) {
            chain.doFilter(request, response);
        } else {
            // 로그인되지 않은 사용자는 로그인 페이지로 리다이렉트
            MessageRedirectTemplate.builder()
                    .msg("로그인 후 이용가능합니다.")
                    .loc("/loginpage")
                    .response(response)
                    .error("error")
                    .request(request)
                    .build().forward();
        }
    }

}
