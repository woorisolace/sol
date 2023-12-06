package com.example.clean.Config;

import com.example.clean.Config.oauth.CustomLoginFailureHandler;
import com.example.clean.Config.oauth.CustomLoginSuccessHandler;
import com.example.clean.Config.oauth.OAuthLoginFailureHandler;
import com.example.clean.Config.oauth.OAuthLoginSuccessHandler;
import com.example.clean.Service.MemberService;
import com.example.clean.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//보안인증시 반드시 구성되어야 하는 부분
@Configuration
@Log4j2 //println 대체
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    MemberService memberService;
    UserService userService;

    private final OAuthLoginSuccessHandler oAuthLoginSuccessHandler;
    private final OAuthLoginFailureHandler oAuthLoginFailureHandler;
    private final CustomLoginSuccessHandler customLoginSuccessHandler;
    private final CustomLoginFailureHandler customLoginFailureHandler;
    private final LogoutSuccessHandler logoutSuccessHandler;

    //1. 보안인증의 암호는 암호화로 처리를 해야한다.
    //암호는 복원이 불가능하다.
    @Bean
    //사용자가 작성한 변수나 클래스를 스프링부트에 등록
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //복호화 불가능
    }

    //3. 인가가 필요한 리소스 설정하기(가장 중요)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //antMatchers("/test")는 정확한 /test URL만 일치.
        //mvcMatchers("/test")는 /test, /test/, /test.html, /test.xyz 등 다양하게 일치.
        http.authorizeHttpRequests((auth) -> {
            auth.antMatchers("/", "/login", "/register").permitAll();
            // "/noticelist", "/qnalist", "/productlist", "/livinglist", "/kitchenlist", "/restroomlist", "/salelist"
            auth.antMatchers("/logout",
                "/mypage", "/myinfo_detail", "/member_oderlist", "/myreviewlist", "/mylikelist").hasRole("USER");//회원로그인시
            auth.antMatchers("/logout").hasRole("ADMIN");//관리자로그인시
        });

        //로그인처리에 대한 설정
        //loginPage(로그인 화면으로 사용할 페이지), defaultSuccessUrl(로그인 성공시 이동할 페이지)
        //failureUrl(로그인 실패시 이동할 페이지), usernameParameter(username으로 사용할 변수)
        http.formLogin()
            .loginPage("/login")
            .successHandler(customLoginSuccessHandler)
            .usernameParameter("email")
            .failureUrl("/login")
            .failureHandler(customLoginFailureHandler);

        http.csrf().disable();//페이지 변조방지를 사용안함
        //로그아웃에 대한 설정
        //logoutRequestMatcher(로그아웃 후 처리할 위치)
        //logoutSuccessUrl(로그아웃시 이동할 페이지)
        http.logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessHandler(logoutSuccessHandler)
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID");
        http.oauth2Login()
            .loginPage("/login")
            .successHandler(oAuthLoginSuccessHandler)
            .failureHandler(oAuthLoginFailureHandler);
        http.rememberMe()
            .key("uniqueAndSecret")
            .rememberMeParameter("autoLogin")
            .rememberMeCookieName("rememberMeCookie")
            .tokenValiditySeconds(60 * 60 * 24 * 30); //초*분*시*일=30일간 유지

        return http.build();
    }

}
