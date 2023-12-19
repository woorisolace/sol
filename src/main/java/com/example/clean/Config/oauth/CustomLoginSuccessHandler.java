package com.example.clean.Config.oauth;

import com.example.clean.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//커스텀 로그인 성공시 처리할 핸들러
//로그인한 이메일을 user 섹션에 등록
@Slf4j
@Component
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        HttpSession session = request.getSession();

        //섹션이 존재하면 로그인한 이메일을 등록
        if (session != null) {
            String email = authentication.getName();
            String oauthType = "";
            userService.userIdToSession(session, email, oauthType);
        }

        // 세션에 저장된 원래 페이지 URL을 가져옴
        String originalRequestUrl = (String) session.getAttribute("originalRequestUrl");

        // 원래 페이지 URL이 있으면 해당 페이지로 리다이렉트, 없으면 메인 페이지로 이동
        if (originalRequestUrl != null) {
            super.setDefaultTargetUrl(originalRequestUrl);
        } else {
            super.setDefaultTargetUrl("/");
        }

        super.onAuthenticationSuccess(request, response, authentication);

    }
}
