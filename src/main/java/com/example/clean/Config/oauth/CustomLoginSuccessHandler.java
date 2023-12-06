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

        if (session != null) { //섹션이 존재하면 로그인한 이메일을 등록
            String email = authentication.getName();
            String oauthType = "";
            //session.setAttribute("user", email);
            userService.userIdToSession(session, email, oauthType);

        }

        super.setDefaultTargetUrl("/"); //성공시 이동할 페이지
        super.onAuthenticationSuccess(request, response, authentication);


    }
}
