package com.example.clean.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

  //메인페이지
  @GetMapping("/")
  public String loginForm() throws Exception {
    return "/main";
  }

  //회사소개
  @GetMapping("/companyinfo")
  public String companyinfoForm() throws Exception {
    return "/companyinfo";
  }

}
