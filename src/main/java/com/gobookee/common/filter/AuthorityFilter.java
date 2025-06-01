package com.gobookee.common.filter;

import com.gobookee.common.MessageRedirectTemplate;
import com.gobookee.users.model.dto.User;
import com.gobookee.users.model.dto.UserType;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "AuthorityFilter", urlPatterns = {"/place/insert","/place/insertpage"})
public class AuthorityFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser.getUserType().equals(UserType.OWNER)) {
            chain.doFilter(request, response);
        } else {
            // 사장님이 아니면 메인 페이지로 리다이렉트
            MessageRedirectTemplate.builder()
                    .msg("사장님만 이용가능합니다.")
                    .loc("/")
                    .response(response)
                    .error("error")
                    .request(request)
                    .build().forward();
        }
    }
}
