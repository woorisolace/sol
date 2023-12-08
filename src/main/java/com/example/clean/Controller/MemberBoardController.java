package com.example.clean.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberBoardController {

  //공지목록
  @GetMapping("/noticelist")
  public String noticelistForm() throws Exception {
    return "/notice/notice_list";
  }
  //공지상세
  @GetMapping("/noticedetail")
  public String noticedetailForm() throws Exception {
    return "/notice/notice_detail";
  }


  //질문목록
  @GetMapping("/qnalist")
  public String qnalistForm() throws Exception {
    return "/notice/qna_list";
  }
  //질문작성
  @GetMapping("/qnainsert")
  public String qnainsertForm() throws Exception {
    return "/notice/qna_insert";
  }
  //질문상세
  @GetMapping("/qnadetail")
  public String qnadetailForm() throws Exception {
    return "/notice/qna_detail";
  }

}
