package com.example.clean.Config.oauth;

import com.example.clean.DTO.MemberDTO;
import com.example.clean.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

//로그인 실패시 처리하는 핸들러
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String message;

        if (exception instanceof BadCredentialsException) {
            message = "아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해주세요.";
        } else if (exception instanceof UsernameNotFoundException) {
            message = "계정이 존재하지 않습니다. 회원가입 진행 후 로그인 해주세요.";
        } else {
            message = "로그인에 실패했습니다. 다시 시도해주세요.";
        }

        setDefaultFailureUrl("/login/login/error?message=" + URLEncoder.encode(message, StandardCharsets.UTF_8.toString()));

        super.onAuthenticationFailure(request, response, exception);
    }
}

