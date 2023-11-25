package com.example.clean.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyInfoController {
  
  //마이페이지메인
  @GetMapping("/mypage")
  public String mypage1Form() throws Exception {
    return "/member/my_page";
  }


  //내정보상세
  @GetMapping("/myinfo_detail")
  public String myinfodetailForm() throws Exception {
    return "/member/myinfo_detail";
  }
  //내정보수정
  @GetMapping("/myinfo_update")
  public String myinfoupdateForm() throws Exception {
    return "/member/myinfo_update";
  }


  //나의주문내역
  @GetMapping("/member_oderlist")
  public String myoderlistForm() throws Exception {
    return "/member/oder_list";
  }
  //나의주문상세
  @GetMapping("/member_oderdetail")
  public String myoderdetailForm() throws Exception {
    return "/member/oder_detail";
  }


  //나의리뷰목록
  @GetMapping("/myreviewlist")
  public String myreviewlistForm() throws Exception {
    return "/member/my_review_list";
  }
  //나의리뷰상세
  @GetMapping("/myreviewdetail")
  public String myreviewdetailForm() throws Exception {
    return "/member/my_review";
  }


  //나의관심상품
  @GetMapping("/mylikelist")
  public String mylikelistForm() throws Exception {
    return "/member/my_like_list";
  }

}
