package com.example.clean.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberReviewController {

  //리뷰목록
  @GetMapping("/reviewlist")
  public String memberreviewlistForm() throws Exception {
    return "/review/review_list";
  }
  
  
  //리뷰상세
  @GetMapping("/reviewdetail")
  public String memberreviewdetailForm() throws Exception {
    return "/review/review_detail";
  }
  //리뷰등록
  @GetMapping("/reviewinsert")
  public String memberreviewinsertForm() throws Exception {
    return "/review/review_insert";
  }

}
