package com.example.clean.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AiController {

  //이미지 인식페이지
  @GetMapping("/ai_img_search")
  public String imgsearchForm() throws Exception {
    return "/ai/img_search";
  }

  //이미지 결과페이지
  @GetMapping("/ai_img_result")
  public String imgresultForm() throws Exception {
    return "/ai/img_result";
  }



}
