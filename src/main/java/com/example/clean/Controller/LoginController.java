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

  @GetMapping("/login/login/error")
  public String loginError(Model model) throws Exception {
    model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
    return "/login/login_main";
  }

  //로그인처리
  @PostMapping("/login")
  public String loginProc(String email,Model model) throws Exception {
    return "redirect:/";
  }

  //로그아웃 종류 : 토큰만료 or 로그삭제
  //토큰만료 : 일반적으로 생각하는 로그아웃
  //로그삭제 : 탈퇴 or 다른 카카오 아이디로 로그인을 유도하는 경우의 로그아웃

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
    //model.addAttribute("errorMessage", "오류메세지 테스트");
    MemberDTO memberDTO = new MemberDTO();
    model.addAttribute("memberDTO", memberDTO);

    return "/login/register";
  }

  //아이디 찾기 페이지 -> 생략 예정
  //비번 찾기 페이지 -> 생략 예정

  //회원가입처리
  @PostMapping("/register")
  public String registerProc(@Valid MemberDTO memberDTO, BindingResult bindingResult,
                             Model model) throws Exception {
    if(bindingResult.hasErrors()) {
      model.addAttribute("memberDTO",memberDTO); //회원가입 실패시 입력 데이터 값 유지
      return "/login/register"; //오류 발생시 가입페이지로
      // redirect: 리다이렉트는 새로운 요청을 생성, 유효성 검사시 오류 메시지가 표시안됨
    }

    try {
      //MemberEntity memberEntity = MemberEntity.createMember(memberDTO, passwordEncoder);
      memberService.saveMember(memberDTO);
      model.addAttribute("errorMessage", "가입에 성공하였습니다.");
    } catch (IllegalStateException e) {
      model.addAttribute("errorMessage", e.getMessage());
      return "/login/register";
    }

    return "redirect:/login";
  }

}
/*
*   //로그아웃페이지
  @GetMapping("/logout")
  public String logoutProc(HttpSession session) throws Exception {
    String access_token = (String) session.getAttribute("access_token");
    Map<String, String> map = new HashMap<String, String>();
    map.put("Authorization", "Bearer " + access_token);

    String result = conn.HttpPostConnection("https://kapi.kakao.com/v1/user/logout", map).toString();
    System.out.println(result);
    return "redirect:/login";
  }
* */
/*
*토큰만료
* //server/src/auth/auth.controller.ts

// 로그아웃 (일반적인 로그아웃, 토큰 만료)
    @Get('/kakaoLogout')
    @Header('Content-Type', 'text/html')
    kakaoLogout(@Res() res): void {

      this.kakaoService
        .logout()
        .then((e)=>{
          console.log(e);
          return res.send(`
          <div>
            <h2>로그아웃 완료(토큰만료)</h2>
            <a href="/auth/kakaoLogin">메인 화면으로</a>
          </div>
        `);
        })
        .catch((err)=>{
          console.log(err);
          return res.send('logout error');
        })
    }
//server/src/auth/kakao.service.ts
async logout(): Promise<any> {
    const _url = 'https://kapi.kakao.com/v1/user/logout';
    const _headers = {
        Authorization: `Bearer ${this.accessToken}`,
    };
    console.log(this.accessToken);
    //console.log(JSON.stringify(_headers));
    return await lastValueFrom(
        this.http.post(_url, '', { headers: _headers })
    );
  }
*/
/*
//로그 아웃 (탈퇴 or 다른 카카오 아이디로 로그인을 유도하는 경우, 로그 삭제)
    @Get('/kakaoLogout')
    @Header('Content-Type', 'text/html')
    kakaoLogout(@Res() res): void {

      this.kakaoService
        .deleteLog()
        .then((e)=>{
          console.log(e);
          return res.send(`
          <div>
            <h2>로그아웃 완료(로그삭제)</h2>
            <a href="/auth/kakaoLogin">메인 화면으로</a>
          </div>
        `);
        })
        .catch((err)=>{
          console.log(err);
          return res.send('logout error');
        })
    }
//server/src/auth/kakao.service.ts
 async deleteLog(): Promise<any> {
      const _url = "https://kapi.kakao.com/v1/user/unlink";
      const _headers = {
        Authorization: `Bearer ${this.accessToken}`,
      }
      return await lastValueFrom(
          this.http.post(_url, '', { headers: _headers})
      )
  }
    */