package com.gobookee.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 리다이렉션 시 사용하는 템플릿
 * 사용법 : 메시지 msg와 리다이렉션 대상 주소 loc, 해당 창 닫을지 여부 close를 넘겨주기
 * msg.jsp를 통해 화면에 alert 창으로 msg를 띄워주고 loc 주소로 리다이렉션 시켜준다.
 */
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

    public void forward() throws ServletException, IOException {
        request.setAttribute("msg", msg);
        request.setAttribute("loc", loc);
        request.getRequestDispatcher(PATH).forward(request, response);
    }
}
