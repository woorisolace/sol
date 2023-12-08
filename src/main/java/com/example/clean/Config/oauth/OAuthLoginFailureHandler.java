package com.example.clean.Config.oauth;

import com.example.clean.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//소셜로그인 실패시 처리하는 핸들러
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        //로그인 실패 메세지를 모델에 추가하여 전달
        request.getSession().setAttribute("message", "일치하는 회원정보가 없습니다.");
        log.error("LOGIN FAILED : {}", exception.getMessage());

        super.onAuthenticationFailure(request, response, exception);
    }


}
