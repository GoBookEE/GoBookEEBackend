package com.gobookee.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageRedirectTemplate {
    private String msg;
    private String loc;
    private String close;
    private ServletRequest request;
    private ServletResponse response;
    private static final String PATH = CommonPathTemplate.getPath("/common/msg");

    public void responseRequest() throws ServletException, IOException {
        request.setAttribute("msg", msg);
        request.setAttribute("loc", loc);
        request.getRequestDispatcher(PATH).forward(request, response);
    }
}
