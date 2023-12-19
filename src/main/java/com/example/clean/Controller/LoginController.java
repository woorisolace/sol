package com.example.clean.Controller;

import com.example.clean.DTO.MemberDTO;
import com.example.clean.Entity.UserEntity;
import com.example.clean.Service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class LoginController {

  private final MemberService memberService;
  private final PasswordEncoder passwordEncoder;

  //로그인페이지
  @GetMapping("/login")
  public String loginForm(Model model) throws Exception {
    //model.addAttribute("errorMessage", "오류메세지 테스트");
    return "/login/login_main";
  }


  //로그인 에러
  @GetMapping("/login/login/error")
  public String loginError(@RequestParam(value = "message", required = false) String error,
                           @RequestParam(value = "searchUrl", required = false) String searchUrl,
                           Model model) throws Exception {

    model.addAttribute("message", error);
    model.addAttribute("searchUrl", "/login/login/error");

    return "/login/login_main";
  }


  //로그인처리
  @PostMapping("/login")
  public String loginProc(String email, Model model,
                          HttpServletRequest request) throws Exception {

    HttpSession session = request.getSession();
    String originalRequestUrl = (String) session.getAttribute("originalRequestUrl");

    // 세션에 저장된 원래 페이지 URL이 있으면 해당 페이지로 이동하고, 없으면 기본 페이지로 이동
    return "redirect:" + (originalRequestUrl != null ? originalRequestUrl : "/");

  }


  //로그아웃페이지
  @GetMapping("/logout")
  public String logoutProc(HttpSession session, HttpServletResponse response) throws Exception {
    // 세션 초기화 또는 필요한 로그아웃 작업 수행
    session.invalidate();
    // 로그인 페이지로 리다이렉트
    return "redirect:/login";
  }


  //회원가입페이지
  @GetMapping("/register")
  public String registerForm(Model model) throws Exception {
    MemberDTO memberDTO = new MemberDTO();
    model.addAttribute("memberDTO", memberDTO);

    return "/login/register";
  }


  //회원가입처리
  @PostMapping("/register")
  public String registerProc(@Valid MemberDTO memberDTO, BindingResult bindingResult,
                             Model model) throws Exception {
    if(bindingResult.hasErrors()) {
      model.addAttribute("memberDTO",memberDTO); //회원가입 실패시 입력 데이터 값 유지
      return "/login/register"; //오류 발생시 가입페이지로
    }

    try {
      memberService.saveMember(memberDTO);
      model.addAttribute("errorMessage", "가입에 성공하였습니다.");
    } catch (IllegalStateException e) {
      model.addAttribute("errorMessage", e.getMessage());
      return "/login/register";
    }

    return "redirect:/login";
  }

}
